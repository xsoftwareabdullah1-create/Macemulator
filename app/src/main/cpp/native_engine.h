#ifndef MACEMULATOR_NATIVE_ENGINE_H
#define MACEMULATOR_NATIVE_ENGINE_H

#ifdef __cplusplus
extern "C" {
#endif

int macemulator_native_engine_version(void);
int macemulator_clamp_dimension(int requested, int minimum, int maximum);

#ifdef __cplusplus
}
#endif

#endif
