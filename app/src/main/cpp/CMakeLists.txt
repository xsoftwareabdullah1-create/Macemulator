cmake_minimum_required(VERSION 3.22.1)
project(macemulator_native LANGUAGES C CXX)

add_library(macemulator_native SHARED
    native_engine.c
    native_bridge.cpp
)

target_compile_features(macemulator_native PRIVATE c_std_11 cxx_std_17)
target_compile_options(macemulator_native PRIVATE
    $<$<COMPILE_LANGUAGE:C>:-Wall -Wextra -Werror>
    $<$<COMPILE_LANGUAGE:CXX>:-Wall -Wextra -Werror>
)
find_library(android-lib android)
find_library(log-lib log)
target_link_libraries(macemulator_native ${android-lib} ${log-lib})
