# Macemulator

Macemulator is a Java-first, macOS-inspired desktop shell for Android 10+ with an in-app browser, desktop Dock, and movable app windows.

> **Scope:** this is a desktop-style Android app, not Apple macOS and not a Windows/macOS executable emulator. It does not run EXE/MSI or Mac apps. The browser window uses Android System WebView and stays inside the app.

## What is implemented

- Full-screen desktop with gradient wallpaper, top menu bar, shortcuts, and Dock.
- Browser window inside the app with URL/search, back, forward, reload, and home.
- Finder-style demo window and settings preview.
- Windows can be dragged by their title bar, resized from the corner, minimized, maximized/restored, and closed.
- Touch input and basic connected-mouse pointer interaction.
- Java 17 Android UI, plus a small C/C++ JNI library compiled through CMake.
- Separate C#/.NET 8 geometry helper module, kept out of the APK until a .NET for Android integration is deliberately built and tested.

## Language responsibilities

| Language | Responsibility | Build status |
| --- | --- | --- |
| Java | Android Activity, views, Dock, desktop windows, WebView | Part of Android APK |
| C | Small native dimension-clamping helper | Compiled into Android native library |
| C++ | JNI bridge and native engine information | Compiled into Android native library |
| C# | Platform-neutral window geometry helper | Separate .NET 8 library; not currently packaged into APK |

C# is **not yet linked into the Android application**. Embedding it requires a .NET for Android binding or a separate supported runtime/build arrangement; this repository does not pretend otherwise.

## Browser and Chrome limitation

The Dock item labelled **Chrome** opens the browser window inside Macemulator. That window uses Android System WebView, not the installed Chrome app. It cannot access Chrome's private tabs, extensions, profile, or cookies. Android does not provide a supported API for embedding the full installed Chrome application as a child view in another app's resizable window. The in-app browser is the supported way to keep browsing inside Macemulator.

## Compatibility and toolchain

| Item | Version |
| --- | --- |
| Minimum Android | Android 10 (API 29) |
| Compile SDK | 35 |
| Java | 17 |
| Android Gradle Plugin | 8.7.3 |
| Gradle | 8.9 |
| CMake | 3.22.1 |
| Android NDK | Install a current side-by-side NDK in Android SDK Manager |
| C# module | .NET 8 SDK |

Native APK ABIs configured: `arm64-v8a`, `armeabi-v7a`, and `x86_64`.

## Build the Android APK

### Android Studio
1. Install Android SDK Platform 35, Android NDK, CMake 3.22.1, and JDK 17.
2. Open the repository in Android Studio and allow Gradle sync.
3. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

### Command line
From the repository root, with Android SDK, NDK, CMake, and JDK 17 configured:

```sh
gradle assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`.

### Build the C# helper separately
With the .NET 8 SDK installed:

```sh
dotnet build modules/csharp/Macemulator.Core/Macemulator.Core.csproj
```

The C# helper is not a prerequisite for the Android APK.

## Current limitations / next steps

- [x] Java desktop shell, Dock, and in-app WebView
- [x] Drag, resize, minimize, maximize/restore, and close window controls
- [x] Native C/C++ library wired through JNI and CMake
- [x] Standalone C# geometry helper module
- [ ] Integrate C# into Android with a supported .NET for Android build, if needed
- [ ] Replace demo file locations with Storage Access Framework
- [ ] Improve window focus/z-order and restore minimized windows from Dock
- [ ] Add keyboard shortcuts and accessibility labels
- [ ] Add automated UI/instrumentation tests on Android 10+ and validate on real devices
- [ ] Confirm CI installs the NDK/CMake toolchain and publishes an APK artifact

## Troubleshooting

- **Native build fails:** install Android NDK and CMake 3.22.1 through Android Studio SDK Manager; check that the selected NDK supports the target ABI.
- **Browser blank page:** check internet connectivity and try an HTTPS URL.
- **Chrome not embedded:** expected behavior; the in-app window uses WebView.
- **Build fails in CI:** inspect the first Gradle, SDK, NDK, or CMake error in the workflow log.

## License

No license has been selected. Until a license file is added, standard copyright restrictions apply.
