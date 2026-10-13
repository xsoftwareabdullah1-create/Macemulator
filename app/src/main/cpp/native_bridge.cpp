#include <jni.h>
#include <string>
#include "native_engine.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_macemulator_desktop_NativeBridge_nativeGetEngineInfo(JNIEnv *env, jclass) {
    const int version = macemulator_native_engine_version();
    const std::string info = "C/C++ native desktop engine v" + std::to_string(version);
    return env->NewStringUTF(info.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_macemulator_desktop_NativeBridge_nativeClampDimension(
        JNIEnv *, jclass, jint requested, jint minimum, jint maximum) {
    return macemulator_clamp_dimension(requested, minimum, maximum);
}
