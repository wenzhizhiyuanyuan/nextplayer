package dev.anilbeesetti.nextplayer.feature.player.floating

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import dev.anilbeesetti.nextplayer.core.ui.R as UiR
import dev.anilbeesetti.nextplayer.feature.player.PlayerActivity
import dev.anilbeesetti.nextplayer.feature.player.R
import kotlin.math.roundToInt

/** Owned by PlayerService, so the window and video survive the activity stopping. */
@SuppressLint("UnsafeOptInUsageError")
internal class FloatingPlayerWindow(
    private val context: Context,
    private val player: Player,
    private val onClose: () -> Unit,
) {
    companion object {
        const val EXTRA_RETURN_TO_PLAYER = "return_from_floating_player"
    }

    private val windows = context.getSystemService(WindowManager::class.java)
    private var root: FrameLayout? = null
    private var video: PlayerView? = null
    private var params: WindowManager.LayoutParams? = null
    private var previous: Button? = null
    private var next: Button? = null
    private var playPause: Button? = null
    val isShowing: Boolean get() = root != null

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            updateControls()
            if (events.contains(Player.EVENT_VIDEO_SIZE_CHANGED)) updateSize()
        }
    }

    fun show(): Boolean {
        if (!Settings.canDrawOverlays(context)) return false
        if (isShowing) return true
        val container = FrameLayout(context).apply { setBackgroundColor(Color.BLACK) }
        val playerView = LayoutInflater.from(context)
            .inflate(
                R.layout.floating_player,
                container,
                false,
            ) as PlayerView
        container.addView(playerView)
        val handle = TextView(context).apply {
            text = context.getString(UiR.string.floating_player_title)
            setTextColor(Color.WHITE)
            setBackgroundColor(0x99000000.toInt())
            gravity = Gravity.CENTER
            contentDescription = context.getString(UiR.string.floating_player_drag)
        }
        container.addView(
            handle,
            FrameLayout.LayoutParams(
                -1,
                dp(36),
                Gravity.TOP,
            ),
        )
        val controls = LinearLayout(context).apply {
            setBackgroundColor(0xCC000000.toInt())
            gravity = Gravity.CENTER
        }
        fun button(label: Int, action: () -> Unit): Button = Button(context).apply {
            text = context.getString(label)
            contentDescription = text
            textSize = 12f
            minWidth = 0
            minimumWidth = 0
            setPadding(
                0,
                0,
                0,
                0,
            )
            setOnClickListener { action() }
            controls.addView(
                this,
                LinearLayout.LayoutParams(
                    0,
                    dp(48),
                    1f,
                ),
            )
        }
        previous = button(UiR.string.floating_player_previous) {
            if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()
        }
        playPause = button(UiR.string.floating_player_pause) {
            if (player.playWhenReady) player.pause() else player.play()
        }
        next = button(UiR.string.floating_player_next) {
            if (player.hasNextMediaItem()) player.seekToNextMediaItem()
        }
        button(UiR.string.floating_player_expand) { expand() }
        button(UiR.string.floating_player_close) {
            hide()
            player.pause()
            onClose()
        }
        container.addView(
            controls,
            FrameLayout.LayoutParams(
                -1,
                dp(48),
                Gravity.BOTTOM,
            ),
        )
        val layout = WindowManager.LayoutParams(
            1,
            1,
            if (Build.VERSION.SDK_INT >= 26) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT,
        ).apply {
            gravity = Gravity.TOP or Gravity.LEFT
            y = dp(80)
        }
        root = container
        video = playerView
        params = layout
        applySize(layout)
        installDragging(
            handle,
            layout,
        )
        return try {
            windows.addView(
                container,
                layout,
            )
            playerView.player = player
            player.addListener(listener)
            updateControls()
            true
        } catch (_: RuntimeException) {
            hide()
            false
        }
    }

    fun hide() {
        player.removeListener(listener)
        video?.player = null
        root?.let { if (it.isAttachedToWindow) windows.removeViewImmediate(it) }
        root = null
        video = null
        params = null
        previous = null
        next = null
        playPause = null
    }

    fun updateSize() {
        val layout = params ?: return
        applySize(layout)
        root?.let {
            windows.updateViewLayout(
                it,
                layout,
            )
        }
    }

    private fun screenSize(): Triple<Int, Int, Int> {
        if (Build.VERSION.SDK_INT >= 30) {
            val metrics = windows.maximumWindowMetrics
            val insets = metrics.windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout(),
            )
            return Triple(
                metrics.bounds.width(),
                metrics.bounds.height(),
                metrics.bounds.height() - insets.top - insets.bottom,
            )
        }
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windows.defaultDisplay.getRealMetrics(metrics)
        return Triple(
            metrics.widthPixels,
            metrics.heightPixels,
            metrics.heightPixels - dp(48),
        )
    }

    private fun applySize(layout: WindowManager.LayoutParams) {
        val (width, height, availableHeight) = screenSize()
        val size = player.videoSize
        val dimensions = floatingWindowSize(
            width,
            height,
            availableHeight,
            if (size.height > 0) size.width * size.pixelWidthHeightRatio / size.height else 0f,
        )
        layout.width = dimensions.width
        // Keep both control rows reachable even for extremely wide videos.
        layout.height = dimensions.height.coerceAtLeast(dp(84)).coerceAtMost(availableHeight)
        layout.x = layout.x.coerceIn(
            0,
            (width - layout.width).coerceAtLeast(0),
        )
        layout.y = layout.y.coerceIn(
            0,
            (availableHeight - layout.height).coerceAtLeast(0),
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun installDragging(handle: View, layout: WindowManager.LayoutParams) {
        var downX = 0f
        var downY = 0f
        var startX = 0
        var startY = 0
        handle.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    downY = event.rawY
                    startX = layout.x
                    startY = layout.y
                }

                MotionEvent.ACTION_MOVE -> {
                    layout.x = startX + (event.rawX - downX).roundToInt()
                    layout.y = startY + (event.rawY - downY).roundToInt()
                    updateSize()
                }

                MotionEvent.ACTION_UP -> handle.performClick()
            }
            true
        }
    }

    private fun updateControls() {
        previous?.isEnabled = player.hasPreviousMediaItem()
        next?.isEnabled = player.hasNextMediaItem()
        playPause?.setText(
            if (player.playWhenReady) {
                UiR.string.floating_player_pause
            } else {
                UiR.string.floating_player_play
            },
        )
        playPause?.contentDescription = playPause?.text
        val layout = params ?: return
        val flags = if (player.isPlaying) {
            layout.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        } else {
            layout.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
        }
        if (layout.flags != flags) {
            layout.flags = flags
            root?.let {
                windows.updateViewLayout(
                    it,
                    layout,
                )
            }
        }
    }

    private fun expand() {
        try {
            context.startActivity(
                Intent(
                    context,
                    PlayerActivity::class.java,
                )
                    .addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
                    )
                    .putExtra(
                        EXTRA_RETURN_TO_PLAYER,
                        true,
                    ),
            )
            hide()
        } catch (_: RuntimeException) {
            Toast.makeText(
                context,
                UiR.string.floating_player_open_failed,
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun dp(value: Int): Int =
        (value * context.resources.displayMetrics.density).roundToInt()
}
