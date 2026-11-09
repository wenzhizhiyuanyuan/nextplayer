package dev.anilbeesetti.nextplayer.feature.player.state

import org.junit.Assert.assertEquals
import org.junit.Test

class PlaylistSwipeStateTest {
    @Test
    fun `up selects next and down selects previous only after release`() {
        val state = PlaylistSwipeState(64f)
        state.drag(-32f)
        state.drag(-40f)
        assertEquals(
            1,
            state.finish(),
        )
        assertEquals(
            0,
            state.finish(),
        )
        state.drag(2000f)
        assertEquals(
            -1,
            state.finish(),
        )
        assertEquals(
            0,
            state.finish(),
        )
    }

    @Test
    fun `short reversed and cancelled swipes do not switch`() {
        val state = PlaylistSwipeState(64f)
        state.drag(-63f)
        assertEquals(
            0,
            state.finish(),
        )
        state.drag(-100f)
        state.drag(80f)
        assertEquals(
            0,
            state.finish(),
        )
        state.drag(100f)
        state.cancel()
        assertEquals(
            0,
            state.finish(),
        )
    }
}
