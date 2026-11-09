package dev.anilbeesetti.nextplayer.feature.player.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.model.SwipeGesture
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberVolumeAndBrightnessGestureState(
    volumeState: VolumeState,
    brightnessState: BrightnessState,
    leftGesture: SwipeGesture,
    rightGesture: SwipeGesture,
    onPlaylistSwipe: (Int) -> Unit,
    volumeGestureSensitivity: Float,
    brightnessGestureSensitivity: Float,
): VolumeAndBrightnessGestureState {
    val coroutineScope = rememberCoroutineScope()
    val currentOnPlaylistSwipe by rememberUpdatedState(onPlaylistSwipe)
    val playlistThresholdPx = with(LocalDensity.current) { 64.dp.toPx() }
    val volumeAndBrightnessGestureState = remember(
        volumeState,
        brightnessState,
        leftGesture,
        rightGesture,
        volumeGestureSensitivity,
        brightnessGestureSensitivity,
        playlistThresholdPx,
    ) {
        VolumeAndBrightnessGestureState(
            volumeState = volumeState,
            brightnessState = brightnessState,
            leftGesture = leftGesture,
            rightGesture = rightGesture,
            playlistThresholdPx = playlistThresholdPx,
            onPlaylistSwipe = { currentOnPlaylistSwipe(it) },
            volumeGestureSensitivity = volumeGestureSensitivity,
            brightnessGestureSensitivity = brightnessGestureSensitivity,
            coroutineScope = coroutineScope,
        )
    }
    return volumeAndBrightnessGestureState
}

@Stable
class VolumeAndBrightnessGestureState(
    private val volumeState: VolumeState,
    private val brightnessState: BrightnessState,
    private val leftGesture: SwipeGesture,
    private val rightGesture: SwipeGesture,
    playlistThresholdPx: Float,
    private val onPlaylistSwipe: (Int) -> Unit,
    private val volumeGestureSensitivity: Float,
    private val brightnessGestureSensitivity: Float,
    private val coroutineScope: CoroutineScope,
) {
    var activeGesture: VerticalGesture? by mutableStateOf(value = null)
        private set

    var volumeChangePercentage: Int by mutableIntStateOf(value = 0)
        private set

    var brightnessChangePercentage: Int by mutableIntStateOf(value = 0)
        private set

    private val playlistSwipe = PlaylistSwipeState(playlistThresholdPx)
    private var gesture: SwipeGesture = SwipeGesture.NONE
    private var startingY = 0f
    private var startVolumePercentage = 0
    private var startBrightnessPercentage = 0
    private var job: Job? = null

    fun onDragStart(offset: Offset, size: IntSize) {
        val viewCenterX = size.width / 2
        job?.cancel()
        playlistSwipe.cancel()
        gesture = if (offset.x < viewCenterX) leftGesture else rightGesture
        activeGesture = when (gesture) {
            SwipeGesture.BRIGHTNESS -> VerticalGesture.BRIGHTNESS
            SwipeGesture.VOLUME -> VerticalGesture.VOLUME
            else -> null
        }
        startingY = offset.y
        volumeState.syncWithSystem()
        startVolumePercentage = volumeState.volumePercentage
        startBrightnessPercentage = brightnessState.brightnessPercentage
    }

    fun onDrag(change: PointerInputChange, dragAmount: Float) {
        if (change.isConsumed) return
        if (gesture == SwipeGesture.PLAYLIST) {
            playlistSwipe.drag(dragAmount)
            return
        }
        val activeGesture = activeGesture ?: return

        when (activeGesture) {
            VerticalGesture.VOLUME -> {
                val maxVolumePercentage = volumeState.maxVolumePercentage
                val volumeChange = (startingY - change.position.y) * (volumeGestureSensitivity / 10)
                val newVolume = startVolumePercentage + volumeChange.toInt()
                volumeChangePercentage = (newVolume - startVolumePercentage).coerceIn(
                    minimumValue = 0 - startVolumePercentage,
                    maximumValue = maxVolumePercentage - startVolumePercentage,
                )
                brightnessChangePercentage = 0
                volumeState.updateVolumePercentage(percentage = newVolume)
            }

            VerticalGesture.BRIGHTNESS -> {
                val brightnessChange =
                    (startingY - change.position.y) * (brightnessGestureSensitivity / 10)
                val newBrightness = startBrightnessPercentage + brightnessChange.toInt()
                brightnessChangePercentage = (newBrightness - startBrightnessPercentage).coerceIn(
                    minimumValue = 0 - startBrightnessPercentage,
                    maximumValue = MAX_BRIGHTNESS_PERCENTAGE - startBrightnessPercentage,
                )
                volumeChangePercentage = 0
                brightnessState.updateBrightnessPercentage(percentage = newBrightness)
            }
        }
    }

    fun onDragCancel() {
        playlistSwipe.cancel()
        gesture = SwipeGesture.NONE
        onDragEnd()
    }

    fun onDragEnd() {
        val direction = playlistSwipe.finish()
        val completedGesture = gesture
        gesture = SwipeGesture.NONE
        if (completedGesture == SwipeGesture.PLAYLIST && direction != 0) {
            onPlaylistSwipe(direction)
        }
        startingY = 0f
        startVolumePercentage = 0
        startBrightnessPercentage = 0

        job?.cancel()
        job = coroutineScope.launch {
            delay(1.seconds)
            activeGesture = null
            volumeChangePercentage = 0
            brightnessChangePercentage = 0
        }
    }

    companion object {
        private const val MAX_BRIGHTNESS_PERCENTAGE = 100
    }
}

enum class VerticalGesture {
    VOLUME,
    BRIGHTNESS,
}
