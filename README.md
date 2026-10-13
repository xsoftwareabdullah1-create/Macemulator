# Macemulator

**A macOS-inspired desktop shell for Android 10+ — built with Java, C, C++, and a separate C# module.**

Macemulator turns an Android screen into a desktop-like workspace with a Dock, desktop shortcuts, movable windows, and an in-app browser. It is an Android application, **not Apple macOS** and not a Windows/macOS executable emulator.

> **Current UI:** landscape desktop layout, full-screen workspace, and an on-screen pointer indicator that follows touch input. A connected mouse can also provide its normal Android pointer.

## What works today

- **Desktop shell:** gradient wallpaper, top menu bar, shortcuts, and bottom Dock.
- **Window controls:** drag windows by the title bar, resize from the corner, minimize, maximize/restore, and close.
- **In-app browser:** WebView window with URL/search, back, forward, reload, and home.
- **Demo Finder and Settings:** visual preview windows; the Finder locations are not yet connected to real file storage.
- **Input:** touch interaction, connected mouse input, and a visible pointer indicator.
- **Native helper:** a small C/C++ JNI library provides engine information and window-dimension clamping.
- **C# helper:** a separate .NET 8 geometry library with its own CI build.

## Language architecture

| Language | Responsibility | Status |
| --- | --- | --- |
| **Java** | Android Activity, desktop layout, Dock, windows, browser/WebView, touch and pointer UI | Included in Android APK |
| **C** | Native dimension-clamping helper | Compiled by CMake |
| **C++** | JNI bridge to native helpers | Compiled into Android native library |
| **C#** | Platform-neutral geometry helper module | Built separately with .NET 8; not currently linked into the APK |

The C# project is intentionally described as a separate module: it is **not** bundled into the Android app yet. Android UI remains Java-based; native C/C++ is used where it provides a real native boundary rather than pretending every language must draw UI.

## Browser / Chrome note

The Dock item named **Chrome** opens the browser window **inside Macemulator**. That window uses Android System WebView. It is not the installed Chrome app, and does not share Chrome's private tabs, extensions, profile, or cookies. Android does not provide a supported way to embed the complete installed Chrome app as a child view inside another app's resizable window. The WebView approach keeps websites inside this app.

## Compatibility and toolchain

| Item | Version |
| --- | --- |
| Minimum Android | Android 10 (API 29) |
| Compile SDK | 35 |
| Java | 17 |
| Android Gradle Plugin | 8.7.3 |
| Gradle | 8.9 |
| CMake | 3.22.1 |
| CI Android NDK | 27.2.12479018 |
| C# helper | .NET 8 |

Configured Android native ABIs: `arm64-v8a`, `armeabi-v7a`, and `x86_64`.

## Build the Android APK

### Android Studio
1. Install Android SDK Platform 35, Android NDK, CMake 3.22.1, and JDK 17.
2. Open this repository in Android Studio and let Gradle sync.
3. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

### Command line
From the repository root with the Android SDK, NDK, CMake, and JDK configured:

```sh
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

### Build the C# helper separately

With .NET 8 installed:

```sh
dotnet build modules/csharp/Macemulator.Core/Macemulator.Core.csproj --configuration Release
```

The C# helper is not a prerequisite for the Android APK.

## GitHub Actions

Push to `main`, open a pull request, or run **Actions → Android and C# CI → Run workflow**. CI builds the Android debug APK (Java + C/C++) and builds the standalone C# helper. A successful Android job uploads the `macemulator-debug-apk` artifact.

## Known limitations and roadmap

- [x] Java desktop shell, Dock, and in-app WebView
- [x] Landscape orientation for the desktop workspace
- [x] Visible pointer indicator over the desktop
- [x] Drag, resize, minimize, maximize/restore, and close window controls
- [x] Native C/C++ helper built through JNI and CMake
- [x] Standalone C# geometry helper and CI build
- [ ] Validate cursor and layout on a physical landscape device and external mouse
- [ ] Improve window focus/z-order and add a Dock path to restore minimized windows
- [ ] Add keyboard shortcuts and accessibility labels
- [ ] Replace demo Finder locations with Android Storage Access Framework
- [ ] Add automated UI/instrumentation tests
- [ ] Integrate C# into Android only through a supported .NET for Android build and test pipeline

## Troubleshooting

- **Screen is portrait:** install a build containing the landscape activity configuration and reopen the app.
- **Pointer indicator is missing:** the on-screen indicator follows touch; the native Android mouse pointer appears when a supported physical mouse is connected.
- **Browser is blank:** check connectivity and try an HTTPS URL.
- **Chrome does not embed:** expected; the in-app browser uses WebView.
- **Native build fails:** install Android NDK and CMake 3.22.1 using Android Studio SDK Manager and inspect the first CMake/Gradle error in CI.

## License

No license has been selected yet. Until a license file is added, standard copyright restrictions apply.
