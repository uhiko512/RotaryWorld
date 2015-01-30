#include <string.h>
#include <jni.h>

jstring Java_tw_rmstudio_uhiko_rotaryworld_GLESActivity_stringFromJNI(JNIEnv* env, jobject thiz) {
    return (*env)->NewStringUTF(env, "Hello from JNI !");
}
