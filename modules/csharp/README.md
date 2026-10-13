# C# geometry module

This .NET 8 class library contains platform-neutral window geometry helpers. It is
kept separate from the Android APK because the current app uses the Android SDK
and Java; consuming C# inside the APK requires a deliberate .NET for Android
binding/build integration. Do not claim this library is packaged into the APK
until that integration and CI build are implemented.

Build and test locally with the .NET 8 SDK:

```sh
dotnet build Macemulator.Core/Macemulator.Core.csproj
```
