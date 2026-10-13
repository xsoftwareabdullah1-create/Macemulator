package com.macemulator.desktop;

/** JNI entry point for the small C/C++ engine used by Macemulator. */
public final class NativeBridge {
    private static final boolean AVAILABLE;

    static {
        boolean loaded;
        try {
            System.loadLibrary("macemulator_native");
            loaded = true;
        } catch (UnsatisfiedLinkError error) {
            loaded = false;
        }
        AVAILABLE = loaded;
    }

    private NativeBridge() {}

    public static boolean isAvailable() {
        return AVAILABLE;
    }

    public static String engineInfo() {
        return AVAILABLE ? nativeGetEngineInfo() : "Native engine unavailable";
    }

    public static int clampDimension(int requested, int minimum, int maximum) {
        if (!AVAILABLE) {
            return Math.max(minimum, Math.min(maximum, requested));
        }
        return nativeClampDimension(requested, minimum, maximum);
    }

    private static native String nativeGetEngineInfo();
    private static native int nativeClampDimension(int requested, int minimum, int maximum);
}
