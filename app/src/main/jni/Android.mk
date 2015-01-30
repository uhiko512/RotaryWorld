LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := objLoader
LOCAL_SRC_FILES := objLoader.c

include $(BUILD_SHARED_LIBRARY)
