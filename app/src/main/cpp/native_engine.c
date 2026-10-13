#include "native_engine.h"

int macemulator_native_engine_version(void) {
    return 1;
}

int macemulator_clamp_dimension(int requested, int minimum, int maximum) {
    if (minimum > maximum) {
        return minimum;
    }
    if (requested < minimum) {
        return minimum;
    }
    if (requested > maximum) {
        return maximum;
    }
    return requested;
}
