cmake_minimum_required(VERSION 3.22.1)

project("enclosure_native")

# 16 KB page size support - comprehensive configuration
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")

# Ensure proper alignment for 16 KB page size
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")

# Global linker flags for all targets
set(CMAKE_CXX_LINK_FLAGS "${CMAKE_CXX_LINK_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")
set(CMAKE_C_LINK_FLAGS "${CMAKE_C_LINK_FLAGS} -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384 -Wl,-z,relro -Wl,-z,now")

# Additional 16KB alignment flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fPIC -Wl,-z,noexecstack")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fPIC -Wl,-z,noexecstack")

# Add include directories
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

# Create a dummy library to ensure CMake configuration works
add_library(enclosure_native SHARED
    dummy.cpp
)

# Set properties for 16 KB alignment
set_target_properties(enclosure_native PROPERTIES
    LINK_FLAGS "-Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384"
    COMPILE_FLAGS "-Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384"
    POSITION_INDEPENDENT_CODE ON
)
