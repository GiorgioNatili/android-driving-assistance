#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_io_a2xe_experiments_myapplicationc_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
