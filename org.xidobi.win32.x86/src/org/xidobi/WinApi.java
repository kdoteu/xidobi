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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xidobi.structs.DCB;
import org.xidobi.structs.HKEY;
import org.xidobi.structs.INT;
import org.xidobi.structs.OVERLAPPED;

/**
 * Interface for the native Windows API.
 * 
 * @author Christian Schwarz
 */
public interface WinApi {

	/** Opens port for input. */
	int GENERIC_READ = 0x80000000;
	/** Opens port for output. */
	int GENERIC_WRITE = 0x40000000;

	/**
	 * Opens a file or device, only if it exists. If the specified file or device does not exist,
	 * the function fails and the last-error code is set to ERROR_FILE_NOT_FOUND (2).
	 */
	int OPEN_EXISTING = 3;

	/**
	 * The file or device is being opened or created for asynchronous I/O. When subsequent I/O
	 * operations are completed on this handle, the event specified in the OVERLAPPED structure will
	 * be set to the signaled state. If this flag is specified, the file can be used for
	 * simultaneous read and write operations. If this flag is not specified, then I/O operations
	 * are serialized, even if the calls to the read and write functions specify an OVERLAPPED
	 * structure.
	 */
	int FILE_FLAG_OVERLAPPED = 0x40000000;

	/** Invalid handle value. */
	int INVALID_HANDLE_VALUE = -1;

	/** No errors. */
	int ERROR_SUCCESS = 0;
	/** Access denied or port busy. */
	int ERROR_ACCESS_DENIED = 5;
	/** File not found or port unavailable. */
	int ERROR_FILE_NOT_FOUND = 2;
	/** Overlapped I/O operation is in progress. */
	int ERROR_IO_PENDING = 997;
	/** No more data is available. Indicates in an enumeration that no more elements are available. */
	int ERROR_NO_MORE_ITEMS = 259;
	/** More data is available. */
	int ERROR_MORE_DATA = 234;
	/** Overlapped I/O event is not in a signaled state. */
	int ERROR_IO_INCOMPLETE = 996;

	/**
	 * The specified object is a mutex object that was not released by the thread that owned the
	 * mutex object before the owning thread terminated. Ownership of the mutex object is granted to
	 * the calling thread and the mutex state is set to nonsignaled. If the mutex was protecting
	 * persistent state information, you should check it for consistency.
	 */
	int WAIT_ABANDONED = 0x00000080;
	/** The state of the specified object is signaled. */
	int WAIT_OBJECT_0 = 0x00000000;
	/** The time-out interval elapsed, and the object's state is nonsignaled. */
	int WAIT_TIMEOUT = 0x00000102;
	/** The function has failed. To get extended error information, call GetLastError. */
	int WAIT_FAILED = 0xFFFFFFFF;

	/** Combines the STANDARD_RIGHTS_WRITE, KEY_SET_VALUE, and KEY_CREATE_SUB_KEY access rights. */
	int KEY_WRITE = 0x20006;
	/** Equivalent to {@link #KEY_READ}. */
	int KEY_EXECUTE = 0x20019;
	/**
	 * Combines the STANDARD_RIGHTS_READ, KEY_QUERY_VALUE, KEY_ENUMERATE_SUB_KEYS, and KEY_NOTIFY
	 * values.
	 */
	int KEY_READ = 0x20019;

	/**
	 * Registry entries subordinate to this key define the physical state of the computer, including
	 * data about the bus type, system memory, and installed hardware and software. It contains
	 * subkeys that hold current configuration data, including Plug and Play information (the Enum
	 * branch, which includes a complete list of all hardware that has ever been on the system),
	 * network logon preferences, network security information, software-related information (such
	 * as server names and the location of the server), and other system information.
	 */
	int HKEY_LOCAL_MACHINE = 0x80000002;

	/**
	 * The CreateFile function can create a handle to a communications resource, such as the serial
	 * port COM1. For communications resources, the dwCreationDisposition parameter must be
	 * OPEN_EXISTING, the dwShareMode parameter must be zero (exclusive access), and the
	 * hTemplateFile parameter must be NULL. Read, write, or read/write access can be specified, and
	 * the handle can be opened for overlapped I/O. To specify a COM port number greater than 9, use
	 * the following syntax: "\\.\COM10". This syntax works for all port numbers and hardware that
	 * allows COM port numbers to be specified.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa363858(v=vs.85).aspx">
	 * CreateFile (MSDN)</a> for more details.</i>
	 * 
	 * @param lpFileName
	 *            {@code _In_ LPCTSTR} - The name of the file or device to be created or opened. You
	 *            may use either forward slashes (/) or backslashes (\) in this name.
	 * @param dwDesiredAccess
	 *            {@code _In_ DWORD} - The requested access to the file or device, which can be
	 *            summarized as read, write, both or neither zero).
	 * @param dwShareMode
	 *            {@code _In_ DWORD} - The requested sharing mode of the file or device, which can
	 *            be read, write, both, delete, all of these, or none (refer to the following
	 *            table). Access requests to attributes or extended attributes are not affected by
	 *            this flag. If this parameter is zero and CreateFile succeeds, the file or device
	 *            cannot be shared and cannot be opened again until the handle to the file or device
	 *            is closed. For more information, see the Remarks section.
	 * @param lpSecurityAttributes
	 *            {@code _In_opt_ LPSECURITY_ATTRIBUTES} - A pointer to a SECURITY_ATTRIBUTES
	 *            structure that contains two separate but related data members: an optional
	 *            security descriptor, and a Boolean value that determines whether the returned
	 *            handle can be inherited by child processes. This parameter can be NULL.
	 * @param dwCreationDisposition
	 *            {@code _In_ DWORD} - An action to take on a file or device that exists or does not
	 *            exist.
	 * @param dwFlagsAndAttributes
	 *            {@code _In_ DWORD} - The file or device attributes and flags,
	 *            FILE_ATTRIBUTE_NORMAL being the most common default value for files.
	 * @param hTemplateFile
	 *            {@code _In_opt_ HANDLE} - A valid handle to a template file with the GENERIC_READ
	 *            access right. The template file supplies file attributes and extended attributes
	 *            for the file that is being created. This parameter can be NULL.
	 * @return {@code HANDLE} - If the function succeeds, the return value is an open handle to the
	 *         specified file, device, named pipe, or mail slot. If the function fails, the return
	 *         value is {@link #INVALID_HANDLE_VALUE}. To get extended error information, call
	 *         {@link #GetLastError()}.
	 */
	@CheckReturnValue
	int CreateFile(String lpFileName, int dwDesiredAccess, int dwShareMode, int lpSecurityAttributes, int dwCreationDisposition, int dwFlagsAndAttributes, int hTemplateFile);

	/**
	 * Closes an open object handle.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms724211(v=vs.85).aspx">
	 * CloseHandle (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A valid handle to an open object.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero. If the function
	 *         fails, the return value is zero. To get extended error information, call
	 *         {@link #GetLastError()}.
	 */
	@CheckReturnValue
	boolean CloseHandle(int handle);

	/**
	 * Retrieves the current control settings for a specified communications device.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa363260(v=vs.85).aspx">
	 * GetCommState (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A handle to the communications device. The CreateFile
	 *            function returns this handle.
	 * @param dcb
	 *            {@code _Inout_ LPDCB} - A pointer to a {@link DCB} structure that receives the
	 *            control settings information.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero. If the function
	 *         fails, the return value is zero. To get extended error information, call
	 *         {@link #GetLastError()}.
	 */
	@CheckReturnValue
	boolean GetCommState(int handle, DCB dcb);

	/**
	 * Configures a communications device according to the specifications in a device-control block
	 * (a DCB structure). The function reinitializes all hardware and control settings, but it does
	 * not empty output or input queues.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa363436(v=vs.85).aspx">
	 * SetCommState (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A handle to the communications device. The CreateFile
	 *            function returns this handle.
	 * @param dcb
	 *            {@code _In_ LPDCB} - A pointer to a {@link DCB} structure that contains the
	 *            configuration information for the specified communications device.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero. If the function
	 *         fails, the return value is zero. To get extended error information, call
	 *         {@link #GetLastError()}.
	 */
	@CheckReturnValue
	boolean SetCommState(int handle, DCB dcb);

	/**
	 * Creates or opens a named or unnamed event object.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms682396(v=vs.85).aspx">
	 * CreateEvent (MSDN)</a> for more details.</i>
	 * 
	 * @param lpEventAttributes
	 *            {@code _In_opt_ LPSECURITY_ATTRIBUTES} - A pointer to a SECURITY_ATTRIBUTES
	 *            structure. If this parameter is NULL, the handle cannot be inherited by child
	 *            processes.
	 * @param bManualReset
	 *            {@code _In_ BOOL} - If this parameter is <code>true</code>, the function creates a
	 *            manual-reset event object, which requires the use of the ResetEvent function to
	 *            set the event state to nonsignaled. If this parameter is <code>false</code>, the
	 *            function creates an auto-reset event object, and system automatically resets the
	 *            event state to nonsignaled after a single waiting thread has been released.
	 * @param bInitialState
	 *            {@code _In_ BOOL} - If this parameter is <code>true</code>, the initial state of
	 *            the event object is signaled; otherwise, it is nonsignaled.
	 * @param lpName
	 *            {@code _In_opt_ LPCTSTR} - The name of the event object. The name is limited to
	 *            MAX_PATH characters. Name comparison is case sensitive. If lpName matches the name
	 *            of an existing named event object, this function requests the EVENT_ALL_ACCESS
	 *            access right. In this case, the bManualReset and bInitialState parameters are
	 *            ignored because they have already been set by the creating process. If the
	 *            lpEventAttributes parameter is not NULL, it determines whether the handle can be
	 *            inherited, but its security-descriptor member is ignored. If lpName is NULL, the
	 *            event object is created without a name.
	 * @return {@code HANDLE} - If the function succeeds, the return value is a handle to the event
	 *         object. If the named event object existed before the function call, the function
	 *         returns a handle to the existing object and {@link #GetLastError()} returns
	 *         ERROR_ALREADY_EXISTS.
	 *         <p>
	 *         If the function fails, the return value is NULL.
	 */
	@CheckReturnValue
	int CreateEventA(int lpEventAttributes, boolean bManualReset, boolean bInitialState, @Nullable String lpName);

	/**
	 * Writes data to the specified file or input/output (I/O) device.
	 * <p>
	 * This function is designed for both synchronous and asynchronous operation.
	 * <p>
	 * <i> Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa365747(v=vs.85).aspx">
	 * WriteFile (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A handle to the file or I/O device (for example, a file,
	 *            file stream, physical disk, volume, console buffer, tape drive, socket,
	 *            communications resource, mailslot, or pipe). The hFile parameter must have been
	 *            created with the write access.
	 * @param lpBuffer
	 *            {@code _In_ LPCVOID} - A pointer to the buffer containing the data to be written
	 *            to the file or device. This buffer must remain valid for the duration of the write
	 *            operation. The caller must not use this buffer until the write operation is
	 *            completed.
	 * @param nNumberOfBytesToWrite
	 *            {@code _In_ DWORD} - The number of bytes to be written to the file or device. A
	 *            value of zero specifies a null write operation. The behavior of a null write
	 *            operation depends on the underlying file system or communications technology.
	 * @param lpNumberOfBytesWritten
	 *            {@code _Out_opt_ LPDWORD} - A pointer to the variable that receives the number of
	 *            bytes written when using a synchronous hFile parameter. WriteFile sets this value
	 *            to zero before doing any work or error checking. Use NULL for this parameter if
	 *            this is an asynchronous operation to avoid potentially erroneous results. This
	 *            parameter can be NULL only when the lpOverlapped parameter is not NULL.
	 * @param lpOverlapped
	 *            {@code _Inout_opt_ LPOVERLAPPED} - A pointer to an {@link OVERLAPPED} structure is
	 *            required if the hFile parameter was opened with FILE_FLAG_OVERLAPPED, otherwise
	 *            this parameter can be NULL.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero (
	 *         <code>true</code>). If the function fails, or is completing asynchronously, the
	 *         return value is zero (<code>false</code>). To get extended error information, call
	 *         the {@link #GetLastError()} function.
	 */
	@CheckReturnValue
	boolean WriteFile(int handle, @Nonnull byte[] lpBuffer, int nNumberOfBytesToWrite, @Nullable INT lpNumberOfBytesWritten, @Nullable OVERLAPPED lpOverlapped);

	/**
	 * Reads data from the specified file or input/output (I/O) device. Reads occur at the position
	 * specified by the file pointer if supported by the device.
	 * <p>
	 * This function is designed for both synchronous and asynchronous operations.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa365467(v=vs.85).aspx">
	 * ReadFile (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A handle to the device (for example, a file, file stream,
	 *            physical disk, volume, console buffer, tape drive, socket, communications
	 *            resource, mailslot, or pipe). The hFile parameter must have been created with read
	 *            access. For more information, see Generic Access Rights and File Security and
	 *            Access Rights. For asynchronous read operations, hFile can be any handle that is
	 *            opened with the FILE_FLAG_OVERLAPPED flag by the CreateFile function, or a socket
	 *            handle returned by the socket or accept function.
	 * @param lpBuffer
	 *            {@code _Out_ LPCVOID} - A pointer to the buffer that receives the data read from a
	 *            file or device. This buffer must remain valid for the duration of the read
	 *            operation. The caller must not use this buffer until the read operation is
	 *            completed.
	 * @param nNumberOfBytesToRead
	 *            {@code _In_ DWORD} - The maximum number of bytes to be read.
	 * @param lpNumberOfBytesRead
	 *            {@code _Out_opt_ LPDWORD} - A pointer to the variable that receives the number of
	 *            bytes read when using a synchronous hFile parameter. ReadFile sets this value to
	 *            zero before doing any work or error checking. Use NULL for this parameter if this
	 *            is an asynchronous operation to avoid potentially erroneous results. This
	 *            parameter can be NULL only when the lpOverlapped parameter is not NULL.
	 * @param lpOverlapped
	 *            {@code _Inout_opt_ LPOVERLAPPED} - A pointer to an {@link OVERLAPPED} structure is
	 *            required if the hFile parameter was opened with FILE_FLAG_OVERLAPPED, otherwise it
	 *            can be NULL.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero (
	 *         <code>true</code>). If the function fails, or is completing asynchronously, the
	 *         return value is zero (<code>false</code>). To get extended error information, call
	 *         the {@link #GetLastError()} function.
	 */
	@CheckReturnValue
	boolean ReadFile(int handle, @Nonnull byte[] lpBuffer, int nNumberOfBytesToRead, @Nullable INT lpNumberOfBytesRead, OVERLAPPED lpOverlapped);

	/**
	 * Returns the last error code, that occured during a native method call by the current thread.
	 * This method is a workaround for an issue with {@link #GetLastError()} were JNI or the VM
	 * clears the error code, see (<a href="https://code.google.com/p/xidobi/issues/detail?id=3"
	 * >Bug #3</a>).
	 * <p>
	 * A list of all Windows error codes can be found <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms681381(v=vs.85).aspx"
	 * >here</a>.
	 * <p>
	 * <b>IMPORTANT:</b> This method returns the result of {@link #GetLastError()} that were
	 * "preserved" in the native code, by calls to one of the following methods:
	 * <ul>
	 * <li> {@link #CloseHandle(int)}
	 * <li> {@link #CreateEventA(int, boolean, boolean, String)}
	 * <li> {@link #CreateFile(String, int, int, int, int, int, int)}
	 * <li> {@link #GetCommState(int, DCB)}
	 * <li> {@link #GetOverlappedResult(int, OVERLAPPED, INT, boolean)}
	 * <li> {@link #ReadFile(int, byte[], int, INT, OVERLAPPED)}
	 * <li> {@link #SetCommState(int, DCB)}
	 * <li> {@link #WaitForSingleObject(int, int)}
	 * <li> {@link #WriteFile(int, byte[], int, INT, OVERLAPPED)}
	 * </ul>
	 * 
	 * @see #GetLastError()
	 * @return the last error code
	 */
	int getPreservedError();

	/**
	 * Retrieves the results of an overlapped operation on the specified file, named pipe, or
	 * communications device. To specify a timeout interval or wait on an alertable thread, use
	 * GetOverlappedResultEx.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms683209(v=vs.85).aspx">
	 * GetOverlappedResult (MSDN)</a> for more details.</i>
	 * 
	 * @param handle
	 *            {@code _In_ HANDLE} - A handle to the file, named pipe, or communications device.
	 * @param lpOverlapped
	 *            {@code _In_ LPOVERLAPPED } - A pointer to an {@link OVERLAPPED} structure that was
	 *            specified when the overlapped operation was started.
	 * @param lpNumberOfBytesTransferred
	 *            {@code _Out_ LPDWORD} - A pointer to a variable that receives the number of bytes
	 *            that were actually transferred by a read or write operation.
	 * @param bWait
	 *            {@code _In_ BOOL} - If this parameter is <code>true</code>, and the Internal
	 *            member of the lpOverlapped structure is STATUS_PENDING, the function does not
	 *            return until the operation has been completed. If this parameter is
	 *            <code>false</code> and the operation is still pending, the function returns FALSE
	 *            and the GetLastError function returns {@link #ERROR_IO_INCOMPLETE}.
	 * @return {@code BOOL} - If the function succeeds, the return value is nonzero. If the function
	 *         fails, the return value is zero. To get extended error information, call
	 *         {@link #GetLastError()}.
	 */
	@CheckReturnValue
	boolean GetOverlappedResult(int handle, OVERLAPPED lpOverlapped, INT lpNumberOfBytesTransferred, boolean bWait);

	/**
	 * Waits until the specified object is in the signaled state or the time-out interval elapses.
	 * <p>
	 * To enter an alertable wait state, use the WaitForSingleObjectEx function. To wait for
	 * multiple objects, use the WaitForMultipleObjects.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms687032(v=vs.85).aspx">
	 * WaitForSingleObject (MSDN)</a> for more details.</i>
	 * 
	 * @param hHandle
	 *            {@code _In_ HANDLE} - A handle to the object. For a list of the object types whose
	 *            handles can be specified, see the following Remarks section. If this handle is
	 *            closed while the wait is still pending, the function's behavior is undefined. The
	 *            handle must have the SYNCHRONIZE access right. For more information, see <a href
	 *            ="http://msdn.microsoft.com/en-us/library/windows/desktop/aa379607(v=vs.85).aspx"
	 *            >Standard Access Rights</a>.
	 * @param dwMilliseconds
	 *            {@code _In_ DWORD} - The time-out interval, in milliseconds. If a nonzero value is
	 *            specified, the function waits until the object is signaled or the interval
	 *            elapses. If dwMilliseconds is zero, the function does not enter a wait state if
	 *            the object is not signaled; it always returns immediately. If dwMilliseconds is
	 *            INFINITE, the function will return only when the object is signaled.
	 * @return {@code DWORD} - If the function succeeds, the return value indicates the event that
	 *         caused the function to return. It can be one of the following values:
	 *         <ul>
	 *         <li>{@link #WAIT_ABANDONED} - The specified object is a mutex object that was not
	 *         released by the thread that owned the mutex object before the owning thread
	 *         terminated. Ownership of the mutex object is granted to the calling thread and the
	 *         mutex state is set to nonsignaled. If the mutex was protecting persistent state
	 *         information, you should check it for consistency.</li> <li>{@link #WAIT_FAILED} - The
	 *         state of the specified object is signaled.</li> <li>{@link #WAIT_OBJECT_0} - The
	 *         time-out interval elapsed, and the object's state is nonsignaled. </li> <li>
	 *         {@link #WAIT_TIMEOUT} - The function has failed. To get extended error information,
	 *         call {@link #GetLastError()}.</li>
	 *         </ul>
	 */
	@CheckReturnValue
	int WaitForSingleObject(int hHandle, int dwMilliseconds);

	/**
	 * Opens the specified registry key. Note that key names are not case sensitive.
	 * <p>
	 * To perform transacted registry operations on a key, call the RegOpenKeyTransacted function.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms724897(v=vs.85).aspx">
	 * RegOpenKeyEx (MSDN)</a> for more details.</i>
	 * 
	 * @param hKey
	 *            {@code _In_ HKEY} - A handle to an open registry key.
	 * @param lpSubKey
	 *            {@code _In_opt_ LPCTSTR} - The name of the registry subkey to be opened. Key names
	 *            are not case sensitive.
	 * @param ulOptions
	 *            {@code _Reserved_ DWORD} - This parameter is reserved and must be zero.
	 * @param samDesired
	 *            {@code _In_ REGSAM} - A mask that specifies the desired access rights to the key
	 *            to be opened. The function fails if the security descriptor of the key does not
	 *            permit the requested access for the calling process. For more information, see <a
	 *            href
	 *            ="http://msdn.microsoft.com/en-us/library/windows/desktop/ms724878(v=vs.85).aspx"
	 *            >Registry Key Security and Access Rights</a>.
	 *            <ul>
	 *            <li> {@link #KEY_WRITE} <li> {@link #KEY_READ} <li> {@link #KEY_EXECUTE}
	 *            </ul>
	 * @param phkResult
	 *            {@code _Out_ PHKEY} - A pointer to a variable that receives a handle to the opened
	 *            key. If the key is not one of the predefined registry keys, call the
	 *            {@link #RegCloseKey(HKEY)} function after you have finished using the handle.
	 * @return {@code LONG} - If the function succeeds, the return value is {@link #ERROR_SUCCESS}.
	 *         <p>
	 *         If the function fails, the return value is a nonzero error code defined in
	 *         Winerror.h.
	 */
	@CheckReturnValue
	int RegOpenKeyExA(int hKey, String lpSubKey, int ulOptions, int samDesired, HKEY phkResult);

	/**
	 * Closes a handle to the specified registry key.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms724837(v=vs.85).aspx">
	 * RegCloseKey (MSDN)</a> for more details.</i>
	 * 
	 * @param hKey
	 *            {@code _In_ HKEY} - A handle to the open key to be closed.
	 * @return {@code LONG} - If the function succeeds, the return value is {@link #ERROR_SUCCESS}.
	 *         If the function fails, the return value is a nonzero error code defined in
	 *         Winerror.h.
	 */
	@CheckReturnValue
	int RegCloseKey(HKEY hKey);

	/**
	 * Enumerates the values for the specified open registry key. The function copies one indexed
	 * value name and data block for the key each time it is called.
	 * <p>
	 * <i>Please see <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms724865(v=vs.85).aspx">
	 * RegEnumValue (MSDN)</a> for more details.</i>
	 * 
	 * @param hKey
	 *            {@code _In_ HKEY} - A handle to an open registry key. The key must have been
	 *            opened with the KEY_QUERY_VALUE access right.
	 * @param dwIndex
	 *            {@code _In_ DWORD} - The index of the value to be retrieved. This parameter should
	 *            be zero for the first call to the RegEnumValueA function and then be incremented
	 *            for subsequent calls.
	 *            <p>
	 *            Because values are not ordered, any new value will have an arbitrary index. This
	 *            means that the function may return values in any order.
	 * @param lpValueName
	 *            {@code _Out_ LPTSTR} - A pointer to a buffer that receives the name of the value
	 *            as a null-terminated string. This buffer must be large enough to include the
	 *            terminating null character.
	 * @param lpcchValueName
	 *            {@code _Inout_ LPDWORD} - A pointer to a variable that specifies the size of the
	 *            buffer pointed to by the lpValueName parameter, in characters. When the function
	 *            returns, the variable receives the number of characters stored in the buffer, not
	 *            including the terminating null character.
	 * @param lpReserved
	 *            {@code _Reserved_ LPDWORD} - This parameter is reserved and must be
	 *            <code>null</code>.
	 * @param lpType
	 *            {@code _Out_opt_ LPDWORD} - A pointer to a variable that receives a code
	 *            indicating the type of data stored in the specified value. For a list of the
	 *            possible type codes, see <a href=
	 *            "http://msdn.microsoft.com/en-us/library/windows/desktop/ms724884(v=vs.85).aspx"
	 *            >Registry Value Types</a>. The lpType parameter can be NULL if the type code is
	 *            not required.
	 * @param lpData
	 *            {@code _Out_opt_ LPBYTE} - A pointer to a buffer that receives the data for the
	 *            value entry. This parameter can be NULL if the data is not required.
	 *            <p>
	 *            If lpData is NULL and lpcbData is non-NULL, the function stores the size of the
	 *            data, in bytes, in the variable pointed to by lpcbData. This enables an
	 *            application to determine the best way to allocate a buffer for the data.
	 * @param lpcbData
	 *            {@code _Inout_opt_ LPDWORD} - A pointer to a variable that specifies the size of
	 *            the buffer pointed to by the lpData parameter, in bytes. When the function
	 *            returns, the variable receives the number of bytes stored in the buffer. This
	 *            parameter can be NULL only if lpData is NULL. If the buffer specified by lpData is
	 *            not large enough to hold the data, the function returns {@link #ERROR_MORE_DATA}
	 *            and stores the required buffer size in the variable pointed to by lpcbData. In
	 *            this case, the contents of lpData are undefined.
	 * @return {@code LONG} - If the function succeeds, the return value is {@link #ERROR_SUCCESS} .
	 *         If the function fails, the return value is a system error code. If there are no more
	 *         values available, the function returns {@link #ERROR_NO_MORE_ITEMS}. If the lpData
	 *         buffer is too small to receive the value, the function returns
	 *         {@link #ERROR_MORE_DATA}.
	 */
	@CheckReturnValue
	int RegEnumValueA(HKEY hKey, int dwIndex, byte[] lpValueName, INT lpcchValueName, int lpReserved, INT lpType, byte[] lpData, INT lpcbData);

	/**
	 * Retrieves the calling thread's last-error code value. The last-error code is maintained on a
	 * per-thread basis. Multiple threads do not overwrite each other's last-error code.
	 * <p>
	 * A list of all Windows error codes can be found <a
	 * href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms681381(v=vs.85).aspx"
	 * >here</a>.
	 * 
	 * @return {@code DWORD} - The return value is the calling thread's last-error code.
	 */
	int GetLastError();

	/**
	 * Returns a pointer to the allocated memory of the given size.
	 * 
	 * @param size
	 *            the size of the memory
	 * @return pointer to the allocated memory
	 */
	int malloc(@Nonnegative int size);

	/**
	 * Frees the memory of the given pointer.
	 * 
	 * @param pointer
	 *            pointer to the memory
	 */
	void free(int pointer);

	/**
	 * Size of an OVERLAPPED struct.
	 * 
	 * @return the size of the OVERLAPPED struct
	 */
	int sizeOf_OVERLAPPED();

	/**
	 * Size of an HKEY struct.
	 * 
	 * @return the size of the HKEY struct
	 */
	int sizeOf_HKEY();

}