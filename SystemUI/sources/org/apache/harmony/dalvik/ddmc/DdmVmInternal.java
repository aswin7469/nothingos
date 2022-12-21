package org.apache.harmony.dalvik.ddmc;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class DdmVmInternal {
    public static native StackTraceElement[] getStackTraceById(int i);

    public static native byte[] getThreadStats();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setRecentAllocationsTrackingEnabled(boolean z);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setThreadNotifyEnabled(boolean z);

    private DdmVmInternal() {
    }
}
