package com.spectralogic.ds3client.metadata.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

public interface Advapi32 extends Library {

    Advapi32 INSTANCE = (Advapi32) Native.loadLibrary("advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    int GetNamedSecurityInfo(String var1, int var2, int var3, PointerByReference var4, PointerByReference var5, PointerByReference var6, PointerByReference var7, PointerByReference var8);
    boolean ConvertStringSidToSid(String var1, WinNT.PSIDByReference var2);
    int SetNamedSecurityInfo(String var1, int var2, int var3, Pointer var4, Pointer var5, Pointer var6, Pointer var7);
}