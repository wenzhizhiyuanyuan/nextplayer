package dev.anilbeesetti.nextplayer.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class SwipeGesture {
    BRIGHTNESS,
    VOLUME,
    PLAYLIST,
    NONE,
}
