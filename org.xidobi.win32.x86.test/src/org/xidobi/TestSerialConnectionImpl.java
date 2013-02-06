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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.xidobi.WinApi.INVALID_HANDLE_VALUE;
import static org.xidobi.WinApi.PURGE_RXABORT;
import static org.xidobi.WinApi.PURGE_RXCLEAR;
import static org.xidobi.WinApi.PURGE_TXABORT;
import static org.xidobi.WinApi.PURGE_TXCLEAR;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.xidobi.spi.NativeCodeException;
import org.xidobi.structs.DWORD;
import org.xidobi.structs.OVERLAPPED;

/**
 * Tests the class {@link SerialConnectionImpl}.
 * 
 * @author Tobias Bre�ler
 */
public class TestSerialConnectionImpl {

	/** dummy size of OVERLAPPED */
	private static final int OVERLAPPED_SIZE = 1;
	/** dummy size of DWORD */
	private static final int DWORD_SIZE = 2;
	/** a dummy handle to the event object */
	private static final int eventHandle = 1;

	/** pointer to an {@link OVERLAPPED}-struct */
	private int ptrOverlapped = 1;
	/** pointer to an {@link DWORD} */
	private int ptrBytesTransferred = 2;
	private int ptrEvtMask = 3;

	/** Class under test */
	private SerialConnectionImpl serialConnectionImpl;

	/** check exceptions */
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private SerialPort port;
	@Mock
	private WinApi os;

	private int handle = 1534;

	@Before
	@SuppressWarnings("javadoc")
	public void setUp() {
		initMocks(this);

		when(os.sizeOf_OVERLAPPED()).thenReturn(OVERLAPPED_SIZE);
		when(os.malloc(OVERLAPPED_SIZE)).thenReturn(ptrOverlapped);

		when(os.sizeOf_DWORD()).thenReturn(DWORD_SIZE);
		when(os.malloc(DWORD_SIZE)).thenReturn(ptrBytesTransferred, ptrEvtMask);

		when(port.getPortName()).thenReturn("COM1");
		when(os.CreateEventA(0, true, false, null)).thenReturn(eventHandle);

		serialConnectionImpl = new SerialConnectionImpl(port, os, handle);
	}

	/**
	 * Verifies that an {@link IllegalArgumentException} is thrown, when <code>null</code> is
	 * passed.
	 */
	@SuppressWarnings({ "resource", "unused" })
	@Test(expected = IllegalArgumentException.class)
	public void new_withNullPort() {
		new SerialConnectionImpl(null, os, handle);
	}

	/**
	 * Verifies that an {@link IllegalArgumentException} is thrown, when <code>null</code> is
	 * passed.
	 */
	@SuppressWarnings({ "unused", "resource" })
	@Test(expected = IllegalArgumentException.class)
	public void new_withNullWinApi() {
		new SerialConnectionImpl(port, null, handle);
	}

	/**
	 * Verifies that an {@link IllegalArgumentException} is thrown, when an invalid port handle is
	 * passed.
	 */
	@SuppressWarnings({ "unused", "resource" })
	@Test(expected = IllegalArgumentException.class)
	public void new_withInvalidHandle() {
		new SerialConnectionImpl(port, os, INVALID_HANDLE_VALUE);
	}

	// /**
	// * Verifies that a {@link NativeCodeException} is thrown, when <code>PurgeComm(...)</code>
	// * fails.
	// *
	// * @throws Exception
	// */
	// @Test
	// public void close_PurgeCommFailsUnexpected() throws Exception {
	// when(os.PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
	// PURGE_TXCLEAR)).thenReturn(false);
	// when(os.CloseHandle(handle)).thenReturn(true);
	//
	// exception.expect(NativeCodeException.class);
	// exception.expectMessage("PurgeComm failed unexpected!");
	//
	// try {
	// serialConnectionImpl.close();
	// }
	// finally {
	// // verify(os).PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
	// PURGE_TXCLEAR);
	// verify(os).CloseHandle(handle);
	// }
	// }

	/**
	 * Verifies that a {@link NativeCodeException} is thrown, when <code>CloseHandle(...)</code>
	 * fails.
	 * 
	 * @throws Exception
	 */
	@Test
	public void close_CloseHandleFailsUnexpected() throws Exception {
		when(os.PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT | PURGE_TXCLEAR)).thenReturn(true);
		when(os.CloseHandle(handle)).thenReturn(false);

		exception.expect(NativeCodeException.class);
		exception.expectMessage("CloseHandle failed unexpected!");

		try {
			serialConnectionImpl.close();
		}
		finally {
			// verify(os).PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
			// PURGE_TXCLEAR);
			verify(os).CloseHandle(handle);
		}
	}

	/**
	 * Verifies that all resources are disposed, when the serial connection is closed.
	 * 
	 * @throws Exception
	 */
	@Test
	public void close_successfull() throws Exception {
		when(os.PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT | PURGE_TXCLEAR)).thenReturn(true);
		when(os.CloseHandle(handle)).thenReturn(true);

		try {
			serialConnectionImpl.close();
		}
		finally {
			// verify(os).PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
			// PURGE_TXCLEAR);
			verify(os).CloseHandle(handle);
		}
	}

	// /**
	// * Verifies that all resources are disposed, when <code>PurgeComm(...)</code> throws a
	// * {@link RuntimeException}.
	// *
	// * @throws Exception
	// */
	// @Test
	// public void close_PurgeCommThrowsException() throws Exception {
	// when(os.PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
	// PURGE_TXCLEAR)).thenThrow(new RuntimeException());
	// when(os.CloseHandle(handle)).thenReturn(true);
	//
	// exception.expect(RuntimeException.class);
	//
	// try {
	// serialConnectionImpl.close();
	// }
	// finally {
	// // verify(os).PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
	// PURGE_TXCLEAR);
	// verify(os).CloseHandle(handle);
	// }
	// }

	// /**
	// * Verifies that a {@link NativeCodeException} is thrown, when <code>PurgeComm(...)</code>
	// * throws a {@link RuntimeException} and <code>CloseHandle(...)</code> fails.
	// *
	// * @throws Exception
	// */
	// @Test
	// public void close_PurgeCommThrowsExceptionAndCloseHandleFails() throws Exception {
	// when(os.PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT |
	// PURGE_TXCLEAR)).thenThrow(new RuntimeException());
	// when(os.CloseHandle(handle)).thenReturn(false);
	//
	// exception.expect(NativeCodeException.class);
	// exception.expectMessage("CloseHandle failed unexpected!");
	//
	// try {
	// serialConnectionImpl.close();
	// }
	// finally {
	// verify(os).PurgeComm(handle, PURGE_RXABORT | PURGE_RXCLEAR | PURGE_TXABORT | PURGE_TXCLEAR);
	// verify(os).CloseHandle(handle);
	// }
	// }
}
