package dev.anilbeesetti.nextplayer.core.model

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerPreferencesTest {
    @Test
    fun `legacy swipe switches keep their behavior`() {
        val defaults = Json.decodeFromString<PlayerPreferences>("{}")
        assertEquals(
            SwipeGesture.BRIGHTNESS,
            defaults.effectiveLeftSwipeGesture,
        )
        assertEquals(
            SwipeGesture.VOLUME,
            defaults.effectiveRightSwipeGesture,
        )
        val disabled = Json.decodeFromString<PlayerPreferences>(
            """{"enableBrightnessSwipeGesture":false,"enableVolumeSwipeGesture":false}""",
        )
        assertEquals(
            SwipeGesture.NONE,
            disabled.effectiveLeftSwipeGesture,
        )
        assertEquals(
            SwipeGesture.NONE,
            disabled.effectiveRightSwipeGesture,
        )
    }

    @Test
    fun `explicit mappings override legacy switches and survive serialization`() {
        for (gesture in SwipeGesture.entries) {
            val preferences = PlayerPreferences(
                enableBrightnessSwipeGesture = false,
                enableVolumeSwipeGesture = false,
                leftSwipeGesture = gesture,
                rightSwipeGesture = gesture,
            )
            val restored = Json.decodeFromString<PlayerPreferences>(
                Json.encodeToString(preferences),
            )
            assertEquals(
                gesture,
                restored.effectiveLeftSwipeGesture,
            )
            assertEquals(
                gesture,
                restored.effectiveRightSwipeGesture,
            )
            assertEquals(
                preferences,
                restored,
            )
        }
    }

    @Test
    fun `existing preferences keep tunneled playback disabled`() {
        val preferences = Json.decodeFromString<PlayerPreferences>(
            string = """{"autoplay":false,"preferredAudioLanguage":"en"}""",
        )

        assertFalse(preferences.enableTunneledPlayback)
        assertFalse(preferences.autoplay)
        assertEquals(
            "en",
            preferences.preferredAudioLanguage,
        )
    }

    @Test
    fun `tunneled playback survives serialization`() {
        val preferences = PlayerPreferences(enableTunneledPlayback = true)
        val restored = Json.decodeFromString<PlayerPreferences>(
            Json.encodeToString(value = preferences),
        )

        assertTrue(restored.enableTunneledPlayback)
        assertEquals(
            preferences,
            restored,
        )
    }
}
