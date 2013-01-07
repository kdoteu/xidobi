/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_xidobi_OS */

#ifndef _Included_org_xidobi_OS
#define _Included_org_xidobi_OS
#ifdef __cplusplus
extern "C" {
#endif
#undef org_xidobi_OS_GENERIC_READ
#define org_xidobi_OS_GENERIC_READ -2147483648L
#undef org_xidobi_OS_GENERIC_WRITE
#define org_xidobi_OS_GENERIC_WRITE 1073741824L
#undef org_xidobi_OS_OPEN_EXISTING
#define org_xidobi_OS_OPEN_EXISTING 3L
#undef org_xidobi_OS_FILE_FLAG_OVERLAPPED
#define org_xidobi_OS_FILE_FLAG_OVERLAPPED 1073741824L
#undef org_xidobi_OS_INVALID_HANDLE_VALUE
#define org_xidobi_OS_INVALID_HANDLE_VALUE -1L
#undef org_xidobi_OS_ERROR_ACCESS_DENIED
#define org_xidobi_OS_ERROR_ACCESS_DENIED 5L
#undef org_xidobi_OS_ERROR_FILE_NOT_FOUND
#define org_xidobi_OS_ERROR_FILE_NOT_FOUND 2L
#undef org_xidobi_OS_ERROR_IO_PENDING
#define org_xidobi_OS_ERROR_IO_PENDING 997L
#undef org_xidobi_OS_WAIT_ABANDONED
#define org_xidobi_OS_WAIT_ABANDONED 128L
#undef org_xidobi_OS_WAIT_OBJECT_0
#define org_xidobi_OS_WAIT_OBJECT_0 0L
#undef org_xidobi_OS_WAIT_TIMEOUT
#define org_xidobi_OS_WAIT_TIMEOUT 258L
#undef org_xidobi_OS_WAIT_FAILED
#define org_xidobi_OS_WAIT_FAILED -1L
/*
 * Class:     org_xidobi_OS
 * Method:    CreateFile
 * Signature: (Ljava/lang/String;IIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_xidobi_OS_CreateFile
  (JNIEnv *, jclass, jstring, jint, jint, jint, jint, jint, jint);

/*
 * Class:     org_xidobi_OS
 * Method:    CloseHandle
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_xidobi_OS_CloseHandle
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_xidobi_OS
 * Method:    GetCommState
 * Signature: (ILorg/xidobi/structs/DCB;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_xidobi_OS_GetCommState
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     org_xidobi_OS
 * Method:    SetCommState
 * Signature: (ILorg/xidobi/structs/DCB;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_xidobi_OS_SetCommState
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     org_xidobi_OS
 * Method:    CreateEventA
 * Signature: (IZZLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_xidobi_OS_CreateEventA
  (JNIEnv *, jclass, jint, jboolean, jboolean, jstring);

/*
 * Class:     org_xidobi_OS
 * Method:    WriteFile
 * Signature: (I[BILorg/xidobi/structs/INT;I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_xidobi_OS_WriteFile
  (JNIEnv *, jclass, jint, jbyteArray, jint, jobject, jint);

/*
 * Class:     org_xidobi_OS
 * Method:    GetLastError
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_xidobi_OS_GetLastError
  (JNIEnv *, jclass);

/*
 * Class:     org_xidobi_OS
 * Method:    GetOverlappedResult
 * Signature: (IILorg/xidobi/structs/INT;Z)Z
 */
JNIEXPORT jboolean JNICALL Java_org_xidobi_OS_GetOverlappedResult
  (JNIEnv *, jclass, jint, jint, jobject, jboolean);

/*
 * Class:     org_xidobi_OS
 * Method:    WaitForSingleObject
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_xidobi_OS_WaitForSingleObject
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     org_xidobi_OS
 * Method:    newOverlapped
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_xidobi_OS_newOverlapped
  (JNIEnv *, jclass);

/*
 * Class:     org_xidobi_OS
 * Method:    setOverlappedHEvent
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_xidobi_OS_setOverlappedHEvent
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     org_xidobi_OS
 * Method:    deleteOverlapped
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_xidobi_OS_deleteOverlapped
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
