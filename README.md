# Macemulator

A Java-first, macOS-inspired desktop shell for Android 10 and newer.

> **Honest scope:** Macemulator is an Android app that draws a desktop-like interface. It is not Apple macOS, does not run Mac applications, and does not embed the installed Chrome app. The in-app browser uses Android System WebView.

## Features in this starter

- Premium gradient wallpaper, translucent menu bar, desktop shortcuts, and Dock.
- Browser window that stays inside the app, with address/search bar, Back, Forward, Reload, and Home.
- Finder-style demo window and a simple settings window.
- Desktop windows can be dragged, manually resized from the bottom-right grip, minimized, maximized/restored, and closed.
- Touch controls work, with mouse/pointer support for connected peripherals.
- Java / Android SDK for the current UI. **No Kotlin is used.**

## Compatibility and toolchain

| Item | Version |
| --- | --- |
| Minimum Android | Android 10 (API 29) |
| Compile SDK | 35 |
| Java | 17 |
| Android Gradle Plugin | 8.7.3 |
| Gradle | 8.9 |

## Build an APK

### Android Studio
1. Clone this repository and open the project folder in Android Studio.
2. Install Android SDK Platform 35 and use JDK 17.
3. Sync Gradle, then select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

### Command line
With Android SDK and JDK 17 configured:

```bash
gradle assembleDebug
```

The APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

### GitHub Actions
Push to `main` or open a pull request to run **Android CI**. You can also start it manually from **Actions → Android CI → Run workflow**. A successful run uploads a debug APK artifact named `macemulator-debug-apk`.

## Browser limitation

The Dock's **Chrome**-labelled shortcut opens a browser window *inside Macemulator*. It uses Android System WebView, not the installed Chrome app's private tabs, extensions, cookies, or profile. Android does not provide a supported way for a third-party app to embed the full Chrome application inside its own resizable window.

## Roadmap

- [x] Java desktop shell and Dock
- [x] In-app WebView browser window
- [x] Draggable, resizable, minimize/maximize, and close controls
- [x] Demo file manager and settings windows
- [x] GitHub Actions debug APK workflow
- [ ] Persistent file manager using Android's Storage Access Framework
- [ ] Keyboard shortcuts and improved window focus/z-order
- [ ] Automated UI tests on Android 10+ devices

## Troubleshooting

- **Browser shows a blank page:** check internet access and try an HTTPS URL.
- **Chrome is not embedded:** this is expected; the in-app browser uses WebView.
- **APK build fails:** open the failed GitHub Actions run and inspect the first Gradle or Android SDK error.

## License

No license has been selected yet. Until one is added, standard copyright restrictions apply; do not assume this project is open-source licensed.
