namespace Macemulator.Core;

/// <summary>
/// Platform-neutral geometry rules for desktop windows. The Android UI currently
/// uses the Java implementation; this .NET module is the planned shared-logic
/// boundary for future cross-platform tooling and must not be described as an
/// Android runtime dependency until a .NET for Android integration is added.
/// </summary>
public static class DesktopGeometry
{
    public static int Clamp(int requested, int minimum, int maximum)
    {
        if (minimum > maximum)
            throw new ArgumentOutOfRangeException(nameof(minimum), "Minimum must not exceed maximum.");

        return Math.Clamp(requested, minimum, maximum);
    }

    public static (int Width, int Height) ClampSize(
        int width, int height, int minWidth, int minHeight, int maxWidth, int maxHeight)
    {
        if (minWidth > maxWidth || minHeight > maxHeight)
            throw new ArgumentException("Minimum dimensions must not exceed maximum dimensions.");

        return (Math.Clamp(width, minWidth, maxWidth), Math.Clamp(height, minHeight, maxHeight));
    }
}
