/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.metadata.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

public interface Advapi32 extends Library {

    Advapi32 INSTANCE = (Advapi32) Native.loadLibrary("advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    int GetNamedSecurityInfo(String pObjectName, int ObjectType, int SecurityInfo, PointerByReference ppsidOwner, PointerByReference ppsidGroup, PointerByReference ppDacl, PointerByReference ppSacl, PointerByReference ppSecurityDescriptor);
    boolean ConvertStringSidToSid(String StringSid, WinNT.PSIDByReference Sid);
    int SetNamedSecurityInfo(String pObjectName, int ObjectType, int SecurityInfo, Pointer ppsidOwner, Pointer ppsidGroup, Pointer ppDacl, Pointer ppSacl);
}