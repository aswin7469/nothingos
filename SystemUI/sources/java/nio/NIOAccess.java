package java.nio;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NIOAccess {
    private NIOAccess() {
    }

    public static long getBasePointer(Buffer buffer) {
        long j = buffer.address;
        if (j == 0) {
            return 0;
        }
        return j + ((long) (buffer.position << buffer._elementSizeShift));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Object getBaseArray(Buffer buffer) {
        if (buffer.hasArray()) {
            return buffer.array();
        }
        return null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int getBaseArrayOffset(Buffer buffer) {
        if (buffer.hasArray()) {
            return (buffer.arrayOffset() + buffer.position) << buffer._elementSizeShift;
        }
        return 0;
    }
}
