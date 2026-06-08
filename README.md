# IR Remote

A simple, polished Android IR remote app without any ads.

The app is designed for phones with a built-in infrared blaster. It provides a clean touch interface for controlling the Logitech Z607 5.1 Surround System. Feel free to create a merge request to add support for more devices!

<p align="center">
  <img width="303" height="600" alt="image" src="https://github.com/user-attachments/assets/2408ad64-68e0-4ecc-b54c-1b448e81c823" />
</p>

The physical Logitech controller kept getting broken and I got tired of using apps with annoying ads (each time I want to control the volume of my TV!) so I reverse engineer the IR codes with the help of my crustacean friend 🦞.

> This project is independent and is not affiliated with, endorsed by, or sponsored by any device manufacturer.

## Features

- Power, mute, volume up/down
- Bluetooth, FM Radio, AUX, RCA, and SD/USB input controls
- Playback controls: previous, play/pause, next
- 5.1 / 2.1 mode and channel level controls
- Clean dark UI optimized for one-handed use
- Profile-based IR architecture for adding more controllers later

## Supported Devices

| Device        | Type               | Profile              |
| ------------- | ------------------ | -------------------- |
| Logitech Z607 | 5.1 speaker system | `Z607SpeakerProfile` |

More devices can be added through the profile system — see [Adding a new IR controller](#adding-a-new-ir-controller).

## Requirements

- Android 7.0+ / API 24+
- A device with a built-in IR emitter/blaster
- Android SDK 35 for building
- Java 17

## Project structure

```text
app/src/main/java/com/ricardomaltez/irremote/
├── MainActivity.kt
└── ir/
    ├── model/
    │   ├── CommandIds.kt
    │   ├── IrCommand.kt
    │   └── IrControllerProfile.kt
    ├── profiles/
    │   ├── ControllerProfiles.kt
    │   └── Z607SpeakerProfile.kt
    └── protocol/
        ├── IrProtocol.kt
        └── NecProtocol.kt
```

## Architecture

The app separates UI code from IR command data:

- `MainActivity` owns the Android UI and sends commands.
- `IrControllerProfile` describes one supported remote/controller.
- `IrCommand` stores stable command IDs, display labels, and encoded IR payloads.
- `IrProtocol` converts protocol-specific payloads into Android IR transmit patterns.
- `NecProtocol` currently handles NEC 38 kHz encoding.
- `ControllerProfiles` is the registry for bundled controller profiles.

This makes new controllers easier to add without rewriting the activity.

## Adding a new IR controller

1. Add command IDs to `CommandIds.kt` if the new controller needs buttons the app does not already know about.
2. Create a new profile in `ir/profiles/`, for example:

```kotlin
object MySpeakerProfile : IrControllerProfile {
    override val id = "my_speaker"
    override val displayName = "My Speaker"
    override val protocol = NecProtocol

    override val commands = listOf(
        IrCommand(CommandIds.POWER, "Power", "00000000"),
        IrCommand(CommandIds.VOLUME_UP, "Volume +", "00000000"),
    ).associateBy { it.id }
}
```

3. Register the profile in `ControllerProfiles.kt`.
4. Add or update tests under `app/src/test/`.
5. Run the test/build gate:

```bash
./gradlew testDebugUnitTest assembleDebug
```

## Building

From the repository root:

```bash
./gradlew assembleDebug
```

The debug APK will be created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Testing

Run unit tests:

```bash
./gradlew testDebugUnitTest
```

Run tests and build a debug APK:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Current tests cover:

- NEC protocol pattern generation
- Invalid NEC payload validation
- Z607 profile command completeness
- Controller registry sanity checks

## Privacy

The app does not need internet access and does not collect user data. It only uses Android's `TRANSMIT_IR` permission to send infrared commands from supported devices.

## License

This project is licensed under the **GNU General Public License v3.0** (GPL-3.0).

Copyright (C) 2026 Ricardo Maltez

The full license text is available in the [LICENSE](LICENSE) file and at
[https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html).

In short, you are free to use, modify, and distribute this software — including
commercially — provided that any derivative work is also released under the
GPL-3.0 and the source code (including modifications) is made available to
users. There is no warranty; see the license for the full terms.
