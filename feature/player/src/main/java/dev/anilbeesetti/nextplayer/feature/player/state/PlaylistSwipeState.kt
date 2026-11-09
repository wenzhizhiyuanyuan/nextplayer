package dev.anilbeesetti.nextplayer.feature.player.state

/** A completed swipe selects at most one adjacent playlist item. */
internal class PlaylistSwipeState(private val thresholdPx: Float) {
    private var distance = 0f

    fun drag(amount: Float) {
        distance += amount
    }

    fun finish(): Int {
        val direction = when {
            distance <= -thresholdPx -> 1
            distance >= thresholdPx -> -1
            else -> 0
        }
        cancel()
        return direction
    }

    fun cancel() {
        distance = 0f
    }
}
