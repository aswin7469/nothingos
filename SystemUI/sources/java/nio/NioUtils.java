package java.nio;

import android.annotation.SystemApi;
import android.system.OsConstants;
import java.nio.channels.FileChannel;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import sun.nio.p033ch.FileChannelImpl;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NioUtils {
    private NioUtils() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void freeDirectBuffer(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            DirectByteBuffer directByteBuffer = (DirectByteBuffer) byteBuffer;
            if (directByteBuffer.cleaner != null) {
                directByteBuffer.cleaner.clean();
            }
            directByteBuffer.memoryRef.free();
        }
    }

    public static FileDescriptor getFD(FileChannel fileChannel) {
        return ((FileChannelImpl) fileChannel).f883fd;
    }

    public static FileChannel newFileChannel(Closeable closeable, FileDescriptor fileDescriptor, int i) {
        return FileChannelImpl.open(fileDescriptor, (String) null, (OsConstants.O_ACCMODE & i) != OsConstants.O_WRONLY, (OsConstants.O_ACCMODE & i) != OsConstants.O_RDONLY, (i & OsConstants.O_APPEND) != 0, closeable);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] unsafeArray(ByteBuffer byteBuffer) {
        return byteBuffer.array();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int unsafeArrayOffset(ByteBuffer byteBuffer) {
        return byteBuffer.arrayOffset();
    }
}
