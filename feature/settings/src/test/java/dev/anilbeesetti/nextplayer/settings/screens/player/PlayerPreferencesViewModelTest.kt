package dev.anilbeesetti.nextplayer.settings.screens.player

import dev.anilbeesetti.nextplayer.core.data.repository.fake.FakePreferencesRepository
import dev.anilbeesetti.nextplayer.core.model.ScreenOrientation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerPreferencesViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repository = FakePreferencesRepository()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun autoFullscreenToggleUsesVideoOrientationAndRestoresPhoneDirection() =
        runTest(context = dispatcher) {
            val viewModel = PlayerPreferencesViewModel(
                preferencesRepository = repository,
                output = PlayerPreferencesViewModel.Output(navigateUp = {}),
            )
            assertEquals(
                ScreenOrientation.CURRENT_ORIENTATION,
                viewModel.state.value.preferences.playerScreenOrientation,
            )
            viewModel.onAction(PlayerPreferencesUiEvent.ToggleAutoFullscreen)
            advanceUntilIdle()
            assertEquals(
                ScreenOrientation.VIDEO_ORIENTATION,
                repository.playerPreferences.value.playerScreenOrientation,
            )
            viewModel.onAction(PlayerPreferencesUiEvent.ToggleAutoFullscreen)
            advanceUntilIdle()
            assertEquals(
                ScreenOrientation.CURRENT_ORIENTATION,
                viewModel.state.value.preferences.playerScreenOrientation,
            )
        }

    @Test
    fun toggleTunneledPlaybackUpdatesRepositoryAndScreenState() = runTest(context = dispatcher) {
        repository.updatePlayerPreferences {
            it.copy(
                autoplay = false,
                preferredAudioLanguage = "en",
            )
        }
        val original = repository.playerPreferences.value
        val viewModel = PlayerPreferencesViewModel(
            preferencesRepository = repository,
            PlayerPreferencesViewModel.Output(navigateUp = {}),
        )

        viewModel.onAction(action = PlayerPreferencesUiEvent.ToggleTunneledPlayback)
        advanceUntilIdle()

        assertTrue(repository.playerPreferences.value.enableTunneledPlayback)
        assertEquals(
            original.copy(enableTunneledPlayback = true),
            viewModel.state.value.preferences,
        )

        viewModel.onAction(action = PlayerPreferencesUiEvent.ToggleTunneledPlayback)
        advanceUntilIdle()

        assertFalse(repository.playerPreferences.value.enableTunneledPlayback)
        assertEquals(
            original,
            viewModel.state.value.preferences,
        )
    }

    @Test
    fun screenRestoresSavedTunneledPlaybackPreference() = runTest(context = dispatcher) {
        repository.updatePlayerPreferences { it.copy(enableTunneledPlayback = true) }
        val viewModel = PlayerPreferencesViewModel(
            preferencesRepository = repository,
            PlayerPreferencesViewModel.Output(navigateUp = {}),
        )

        assertTrue(viewModel.state.value.preferences.enableTunneledPlayback)
    }
}
