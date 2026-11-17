package dev.anilbeesetti.nextplayer.feature.player.floating

import org.junit.Assert.assertEquals
import org.junit.Test

class FloatingWindowSizeTest {
    @Test
    fun widthUsesPortraitScreenWidthInBothOrientations() {
        assertEquals(
            FloatingWindowSize(
                1080,
                608,
            ),
            floatingWindowSize(
                1080,
                2400,
                2280,
                16f / 9f,
            ),
        )
        assertEquals(
            FloatingWindowSize(
                1080,
                608,
            ),
            floatingWindowSize(
                2400,
                1080,
                960,
                16f / 9f,
            ),
        )
    }

    @Test
    fun portraitVideoKeepsRatioUnlessItExceedsAvailableHeight() {
        assertEquals(
            FloatingWindowSize(
                1080,
                1920,
            ),
            floatingWindowSize(
                1080,
                2400,
                2280,
                9f / 16f,
            ),
        )
        assertEquals(
            FloatingWindowSize(
                1080,
                960,
            ),
            floatingWindowSize(
                2400,
                1080,
                960,
                9f / 16f,
            ),
        )
    }

    @Test
    fun missingVideoSizeUsesWidescreenFallback() {
        assertEquals(
            FloatingWindowSize(
                1080,
                608,
            ),
            floatingWindowSize(
                1080,
                2400,
                2280,
                Float.NaN,
            ),
        )
        assertEquals(
            FloatingWindowSize(
                1080,
                608,
            ),
            floatingWindowSize(
                1080,
                2400,
                2280,
                0f,
            ),
        )
    }
}
