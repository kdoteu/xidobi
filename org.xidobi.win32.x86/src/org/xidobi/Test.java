/*
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
package org.xidobi;

import static org.xidobi.OS.FILE_FLAG_OVERLAPPED;
import static org.xidobi.OS.GENERIC_READ;
import static org.xidobi.OS.GENERIC_WRITE;
import static org.xidobi.OS.OPEN_EXISTING;

/**
 * 
 * 
 * @author Tobias Bre�ler
 * 
 */
public class Test {

	/**
	 * 
	 */
	private static final byte[] LP_BUFFER = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		for (;;) {

			System.out.println("-----------------");

			int handle = OS.CreateFile("\\\\.\\COM1", GENERIC_READ | GENERIC_WRITE, 0, 0, OPEN_EXISTING, FILE_FLAG_OVERLAPPED, 0);
			System.out.println("Handle: " + handle);

			DCB dcb = new DCB();
			OS.GetCommState(handle, dcb);
			System.out.println("BaudRate: " + dcb.BaudRate);

			dcb.BaudRate = 9600;
			OS.SetCommState(handle, dcb);

			DCB dcb2 = new DCB();
			OS.GetCommState(handle, dcb2);
			System.out.println("BaudRate: " + dcb.BaudRate);

			// int eventHandle = OS.CreateEventA(0, true, false, null);
//			System.out.println("Event-Handle: " + eventHandle);
//
//			OVERLAPPED overlapped = new OVERLAPPED();
//			overlapped.hEvent = eventHandle;

			INT lpNumberOfBytesWritten = new INT();
			boolean writeFileStatus = OS.WriteFile(handle, LP_BUFFER, 9, lpNumberOfBytesWritten, null);
			System.out.println("Bytes written: " + writeFileStatus + " " + lpNumberOfBytesWritten.value);

			if (writeFileStatus == false) {
				int lastError = OS.GetLastError();
				System.err.println("Last error: " + lastError);
			}

			// OS.CloseHandle(eventHandle);

			boolean status = OS.CloseHandle(handle);
			System.out.println("Status: " + status);

			Thread.sleep(3000);
		}
	}

}
