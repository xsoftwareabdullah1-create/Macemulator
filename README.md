# Macemulator

A macOS-inspired desktop environment for Android 10+ built primarily with **Java**.

> This project recreates a desktop-style interface on Android. It is not macOS and does not run macOS applications.

## Current project direction

- Java for the Android application and desktop UI
- Android Custom Views for the desktop, menu bar, Dock, and window interactions
- Optional C/C++ via the NDK and CMake for native components when needed
- Installed Chrome can be launched from the Dock; embedding the full Android Chrome app inside a resizable app window is not generally supported
- Android 10+ (API 29+)

## Planned milestones

1. Build a polished desktop canvas, menu bar, wallpaper, and Dock.
2. Add a movable pointer and mouse/touch interaction.
3. Add draggable, resizable, minimize/maximize app windows.
4. Add Finder-style file browsing and desktop settings.
5. Add Chrome launcher and app shortcuts.
6. Add GitHub Actions Android build and publish debug APK artifacts.

## Build

Open this project in Android Studio with Android SDK 35 and JDK 17, or use the included GitHub Actions workflow to build a debug APK.

## Compatibility

Minimum Android version: Android 10 (API 29).
