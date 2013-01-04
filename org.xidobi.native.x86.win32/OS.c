/*
 * OS.c
 *
 * Copyright 2013 Gemtec GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include <jni.h>
#include <stdlib.h>
#include <windows.h>

#include "OS_structs.h"
#include "org_xidobi_OS.h"


JNIEXPORT jint JNICALL
Java_org_xidobi_OS_CreateFile(JNIEnv *env, jclass clazz, jstring lpFileName,
		jint dwDesiredAccess, jint dwShareMode, jint lpSecurityAttributes,
		jint dwCreationDisposition, jint dwFlagsAndAttributes,
		jint hTemplateFile) {

	const char* fileName = (*env)->GetStringUTFChars(env, lpFileName, NULL);

	HANDLE handle;
	handle = CreateFile(fileName,
						dwDesiredAccess,
						dwShareMode,
						(LPSECURITY_ATTRIBUTES) lpSecurityAttributes,
						dwCreationDisposition,
						dwFlagsAndAttributes,
						(HANDLE) hTemplateFile);

	(*env)->ReleaseStringUTFChars(env, lpFileName, fileName);

	return (jint) handle;
}

JNIEXPORT jboolean JNICALL
Java_org_xidobi_OS_CloseHandle(JNIEnv *env, jclass clazz, jint handle) {
	if (CloseHandle((HANDLE) handle))
		return JNI_TRUE;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_org_xidobi_OS_GetCommState(JNIEnv *env, jclass clazz, jint handle, jobject dcbObject) {
	DCB dcb;
	FillMemory(&dcb, sizeof(dcb), 0);

	if (!GetCommState((HANDLE) handle, &dcb))
		return JNI_FALSE;
	setDCBFields(env, dcbObject, &dcb);

	return JNI_TRUE;
}
