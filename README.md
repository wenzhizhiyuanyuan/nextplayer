![Next player banner](fastlane/metadata/android/en-US/images/featureGraphic.png)

# Next Player

[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/anilbeesetti/nextplayer.svg?logo=github&label=GitHub&cacheSeconds=3600)](https://github.com/anilbeesetti/nextplayer/releases/latest)
[![Google Play](https://img.shields.io/endpoint?color=green&logo=google-play&url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Ddev.anilbeesetti.nextplayer%26l%3DGoogle%2520Play%26m%3Dv%24version)](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[![IzzyOnDroid](https://img.shields.io/endpoint?url=https://apt.izzysoft.de/fdroid/api/v1/shield/dev.anilbeesetti.nextplayer)](https://apt.izzysoft.de/fdroid/index/apk/dev.anilbeesetti.nextplayer)
[![F-Droid](https://img.shields.io/f-droid/v/dev.anilbeesetti.nextplayer?logo=f-droid&label=F-Droid&cacheSeconds=3600)](https://f-droid.org/packages/dev.anilbeesetti.nextplayer)
[![GitHub all releases](https://img.shields.io/github/downloads/anilbeesetti/nextplayer/total?logo=github&cacheSeconds=3600)](https://github.com/anilbeesetti/nextplayer/releases/latest)
[![Google Play](https://img.shields.io/endpoint?color=green&logo=google-play&url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Ddev.anilbeesetti.nextplayer%26l%3Ddownloads%26m%3D%24totalinstalls)](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[![Weblate project translated](https://img.shields.io/weblate/progress/next-player?logo=weblate&logoColor=white&cacheSeconds=36000)](https://hosted.weblate.org/engage/next-player/)

Next Player is an Android native video player written in Kotlin. It provides a simple and easy-to-use interface for users to play videos on their
Android devices

**This project is still in development and is expected to have bugs. Please report any bugs you find in
the [Issues](https://github.com/anilbeesetti/nextplayer/issues) section.**

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80"/>](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[<img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" alt="Get it on IzzyOnDroid" height="80">](https://apt.izzysoft.de/fdroid/index/apk/dev.anilbeesetti.nextplayer)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/dev.anilbeesetti.nextplayer/)

> For Github releases, you can verify the app with the SHA-256 fingerprint certificate: `D3:7C:AB:F3:67:6C:F5:82:24:FB:24:2C:8D:D2:09:12:9D:05:88:03:79:29:5F:AA:A6:D0:FB:60:7B:EC:13:1B`

## Screenshots

### Phone

<div style="width:100%; display:flex; justify-content:space-between; flex-wrap:wrap; gap:8px;">

[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" width=19% alt="Home folders">](fastlane/metadata/android/en-US/images/phoneScreenshots/1.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" width=19% alt="Demo folder videos">](fastlane/metadata/android/en-US/images/phoneScreenshots/2.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" width=19% alt="Quick settings">](fastlane/metadata/android/en-US/images/phoneScreenshots/3.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" width=19% alt="Grid view">](fastlane/metadata/android/en-US/images/phoneScreenshots/4.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/6.png" width=19% alt="Search results">](fastlane/metadata/android/en-US/images/phoneScreenshots/6.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/7.png" width=19% alt="Add network connection">](fastlane/metadata/android/en-US/images/phoneScreenshots/7.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/8.png" width=19% alt="Appearance settings">](fastlane/metadata/android/en-US/images/phoneScreenshots/8.png)
</div>

### Player UI

<div style="width:100%; display:flex; justify-content:space-between;">

[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/5.png" width=49% alt="Phone player controls">](fastlane/metadata/android/en-US/images/phoneScreenshots/5.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/3.png" width=49% alt="TV player controls">](fastlane/metadata/android/en-US/images/tvScreenshots/3.png)
</div>

### Android TV

<div style="width:100%; display:flex; justify-content:space-between; flex-wrap:wrap; gap:8px;">

[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/1.png" width=49% alt="TV home folders">](fastlane/metadata/android/en-US/images/tvScreenshots/1.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/2.png" width=49% alt="TV demo folder videos">](fastlane/metadata/android/en-US/images/tvScreenshots/2.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/4.png" width=49% alt="TV appearance settings">](fastlane/metadata/android/en-US/images/tvScreenshots/4.png)
</div>

## Playback improvements in this fork

The following changes are available on this fork's `dev` branch. The upstream
store and release links above do not necessarily include them.

### Configurable vertical swipes

In **Settings → Gestures**, configure the left and right sides independently:
brightness, volume, playlist navigation, or off. The defaults remain left-side
brightness and right-side volume; previously disabled gestures remain disabled.

For playlist navigation, swipe up for the next video and down for the previous
video. A swipe must pass the distance threshold and finish before switching;
each swipe changes at most one item. Switching by swipe preserves the current
screen orientation, and unavailable adjacent items are ignored.

### Automatic fullscreen

In **Settings → Player**, **Auto fullscreen** selects the video orientation:
landscape videos open in landscape and portrait videos open in portrait. Turning
it off selects **Current phone orientation**, keeping the phone's orientation
when playback starts. This is the default for new or unset orientation preferences;
previously saved explicit orientation choices are retained.

The toggle stays in sync with **Player screen orientation**, and manual rotation
remains available. New settings include English, Simplified Chinese, and Traditional
Chinese text.

### Floating playback

The player window button opens a custom floating player instead of system
picture-in-picture. On first use, allow **Display over other apps** in Android
settings, then return to the player. **Settings → Player → Automatic floating
player** also enables floating playback when returning to the home screen after
permission has been granted.

The floating window uses the screen width in portrait orientation, even when the
phone is in landscape. Its height follows the video aspect ratio, with limits to
keep the window and controls on-screen; the video is fitted without cropping.
Drag the title bar to move the window.

Controls include **Previous**, **Play/Pause**, **Next**, **Expand**, and **Close**.
Previous and next follow the current playlist and are disabled when no adjacent
item is available.
Expand returns to the player with the current video, position, and playback state.
Close saves the playback position and stops the session. Paused floating playback
allows the screen to turn off after its normal timeout.

## Player intent API

Next Player supports MX Player-compatible VIEW intents, with additional playback
speed and direct background-play options. See the [Player intent API reference](PLAYER_API.md)
for integration examples, supported extras, URI permissions, playback results,
and compatibility details.

## Supported formats

- **Video**: H.263, H.264 AVC (Baseline Profile; Main Profile on Android 6+), H.265 HEVC, MPEG-4 SP, VP8, VP9, AV1
    - Support depends on Android device
- **Audio**: Vorbis, Opus, FLAC, ALAC, PCM/WAVE (μ-law, A-law), MP1, MP2, MP3, AMR (NB, WB), AAC (LC, ELD, HE; xHE on Android 9+), AC-3, E-AC-3, DTS,
  DTS-HD, TrueHD
    - Support provided by ExoPlayer FFmpeg extension
- **Subtitles**: SRT, SSA, ASS, TTML, VTT, DVB
    - SSA/ASS has limited styling support see [this issue](https://github.com/google/ExoPlayer/issues/8435)

## Features

- Native Android app with simple and easy-to-use interface
- Completely free and open source and without any ads or excessive permissions
- Software decoders for h264 and hevc
- Audio/Subtitle track selection
- Configurable left/right vertical swipes for brightness, volume, playlist navigation, or no action
- Horizontal swipe to seek through video
- [Material 3 (You)](https://m3.material.io/) support
- Media picker with tree, folder and file view modes
- Play videos from url
- Play videos from storage access framework (Android Document picker)
- Control playback speed
- External Subtitle support
- Zoom gesture
- Custom floating player with playlist controls and portrait-screen-width sizing
- Optional automatic fullscreen based on video orientation
- Background playback
- Android TV version
- Search Functionality
- Subtitle delay
- Network storage support (SMB/FTP/SFTP/WebDAV)

## Planned Features

- External Audio support
- Online subtitles download

## Contributing

Contributions are welcome!

### Developer documentation

- [Runtime decoder switching](feature/player/DECODER_SWITCHING.md)

### Translating

You can help translate Next Player on [Hosted Weblate](https://hosted.weblate.org/engage/next-player/).

[![Translate status](https://hosted.weblate.org/widgets/next-player/-/multi-auto.svg)](https://hosted.weblate.org/engage/next-player/)

## Credits

### Open Source Projects

- [Findroid](https://github.com/jarnedemeulemeester/findroid)
- [Just (Video) Player](https://github.com/moneytoo/Player)
- [LibreTube](https://github.com/libre-tube/LibreTube)
- [ReadYou](https://github.com/Ashinch/ReadYou)
- [Seal](https://github.com/JunkFood02/Seal)
- ...

### Special Thanks

[<img src="https://hosted.weblate.org/widgets/next-player/-/287x66-white.png"  width="200"/>](https://hosted.weblate.org/engage/next-player/)

Thanks to **Weblate** for providing free hosting for the project.

## License

Next Player is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for more information.
