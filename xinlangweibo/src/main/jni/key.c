//
// Created by JIANG on 2016/11/21.
//
#include <jni.h>
#define LOG_TAG "HelloWorld"
/*
 * Class:     com_jy_xinlangweibo_constant_Constants
 * Method:    getKey
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jy_xinlangweibo_constant_Constants_getKey(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "3269135043");
}
