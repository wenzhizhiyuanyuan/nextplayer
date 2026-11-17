package dev.anilbeesetti.nextplayer.feature.player.floating

import kotlin.math.min
import kotlin.math.roundToInt

internal data class FloatingWindowSize(val width: Int, val height: Int)

/** Use the portrait screen width in either orientation; contain unusually tall videos. */
internal fun floatingWindowSize(
    screenWidth: Int,
    screenHeight: Int,
    availableHeight: Int,
    aspectRatio: Float,
): FloatingWindowSize {
    val width = min(
        screenWidth,
        screenHeight,
    ).coerceAtLeast(1)
    val ratio = aspectRatio.takeIf { it.isFinite() && it > 0f } ?: (16f / 9f)
    val height = (width / ratio).roundToInt().coerceIn(
        1,
        availableHeight.coerceAtLeast(1),
    )
    return FloatingWindowSize(
        width,
        height,
    )
}
