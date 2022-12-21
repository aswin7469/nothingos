package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.IOUtil */
public class IOUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int IOV_MAX = iovMax();

    public static native void configureBlocking(FileDescriptor fileDescriptor, boolean z) throws IOException;

    static native boolean drain(int i) throws IOException;

    static native int fdLimit();

    public static native int fdVal(FileDescriptor fileDescriptor);

    static native int iovMax();

    static native long makePipe(boolean z);

    static native boolean randomBytes(byte[] bArr);

    static native void setfdVal(FileDescriptor fileDescriptor, int i);

    private IOUtil() {
    }

    static int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j, NativeDispatcher nativeDispatcher) throws IOException {
        if (byteBuffer instanceof DirectBuffer) {
            return writeFromNativeBuffer(fileDescriptor, byteBuffer, j, nativeDispatcher);
        }
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(position <= limit ? limit - position : 0);
        try {
            temporaryDirectBuffer.put(byteBuffer);
            temporaryDirectBuffer.flip();
            byteBuffer.position(position);
            int writeFromNativeBuffer = writeFromNativeBuffer(fileDescriptor, temporaryDirectBuffer, j, nativeDispatcher);
            if (writeFromNativeBuffer > 0) {
                byteBuffer.position(position + writeFromNativeBuffer);
            }
            return writeFromNativeBuffer;
        } finally {
            Util.offerFirstTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    private static int writeFromNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j, NativeDispatcher nativeDispatcher) throws IOException {
        int i;
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i2 = position <= limit ? limit - position : 0;
        if (i2 == 0) {
            return 0;
        }
        if (j != -1) {
            i = nativeDispatcher.pwrite(fileDescriptor, ((DirectBuffer) byteBuffer).address() + ((long) position), i2, j);
        } else {
            i = nativeDispatcher.write(fileDescriptor, ((DirectBuffer) byteBuffer).address() + ((long) position), i2);
        }
        if (i > 0) {
            byteBuffer.position(position + i);
        }
        return i;
    }

    static long write(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, NativeDispatcher nativeDispatcher) throws IOException {
        return write(fileDescriptor, byteBufferArr, 0, byteBufferArr.length, nativeDispatcher);
    }

    static long write(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, int i, int i2, NativeDispatcher nativeDispatcher) throws IOException {
        IOVecWrapper iOVecWrapper = IOVecWrapper.get(i2);
        int i3 = i2 + i;
        int i4 = 0;
        int i5 = 0;
        while (i < i3) {
            try {
                if (i5 >= IOV_MAX) {
                    break;
                }
                ByteBuffer byteBuffer = byteBufferArr[i];
                int position = byteBuffer.position();
                int limit = byteBuffer.limit();
                int i6 = position <= limit ? limit - position : 0;
                if (i6 > 0) {
                    iOVecWrapper.setBuffer(i5, byteBuffer, position, i6);
                    if (!(byteBuffer instanceof DirectBuffer)) {
                        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(i6);
                        temporaryDirectBuffer.put(byteBuffer);
                        temporaryDirectBuffer.flip();
                        iOVecWrapper.setShadow(i5, temporaryDirectBuffer);
                        byteBuffer.position(position);
                        position = temporaryDirectBuffer.position();
                        byteBuffer = temporaryDirectBuffer;
                    }
                    iOVecWrapper.putBase(i5, ((DirectBuffer) byteBuffer).address() + ((long) position));
                    iOVecWrapper.putLen(i5, (long) i6);
                    i5++;
                }
                i++;
            } catch (Throwable th) {
                while (i4 < i5) {
                    ByteBuffer shadow = iOVecWrapper.getShadow(i4);
                    if (shadow != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow);
                    }
                    iOVecWrapper.clearRefs(i4);
                    i4++;
                }
                throw th;
            }
        }
        if (i5 == 0) {
            while (i4 < i5) {
                ByteBuffer shadow2 = iOVecWrapper.getShadow(i4);
                if (shadow2 != null) {
                    Util.offerLastTemporaryDirectBuffer(shadow2);
                }
                iOVecWrapper.clearRefs(i4);
                i4++;
            }
            return 0;
        }
        long writev = nativeDispatcher.writev(fileDescriptor, iOVecWrapper.address, i5);
        long j = writev;
        for (int i7 = 0; i7 < i5; i7++) {
            if (j > 0) {
                ByteBuffer buffer = iOVecWrapper.getBuffer(i7);
                int position2 = iOVecWrapper.getPosition(i7);
                int remaining = iOVecWrapper.getRemaining(i7);
                if (j <= ((long) remaining)) {
                    remaining = (int) j;
                }
                buffer.position(position2 + remaining);
                j -= (long) remaining;
            }
            ByteBuffer shadow3 = iOVecWrapper.getShadow(i7);
            if (shadow3 != null) {
                Util.offerLastTemporaryDirectBuffer(shadow3);
            }
            iOVecWrapper.clearRefs(i7);
        }
        return writev;
    }

    static int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j, NativeDispatcher nativeDispatcher) throws IOException {
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        } else if (byteBuffer instanceof DirectBuffer) {
            return readIntoNativeBuffer(fileDescriptor, byteBuffer, j, nativeDispatcher);
        } else {
            ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(byteBuffer.remaining());
            try {
                int readIntoNativeBuffer = readIntoNativeBuffer(fileDescriptor, temporaryDirectBuffer, j, nativeDispatcher);
                temporaryDirectBuffer.flip();
                if (readIntoNativeBuffer > 0) {
                    byteBuffer.put(temporaryDirectBuffer);
                }
                return readIntoNativeBuffer;
            } finally {
                Util.offerFirstTemporaryDirectBuffer(temporaryDirectBuffer);
            }
        }
    }

    private static int readIntoNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j, NativeDispatcher nativeDispatcher) throws IOException {
        int i;
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i2 = position <= limit ? limit - position : 0;
        if (i2 == 0) {
            return 0;
        }
        if (j != -1) {
            i = nativeDispatcher.pread(fileDescriptor, ((DirectBuffer) byteBuffer).address() + ((long) position), i2, j);
        } else {
            i = nativeDispatcher.read(fileDescriptor, ((DirectBuffer) byteBuffer).address() + ((long) position), i2);
        }
        if (i > 0) {
            byteBuffer.position(position + i);
        }
        return i;
    }

    static long read(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, NativeDispatcher nativeDispatcher) throws IOException {
        return read(fileDescriptor, byteBufferArr, 0, byteBufferArr.length, nativeDispatcher);
    }

    static long read(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, int i, int i2, NativeDispatcher nativeDispatcher) throws IOException {
        IOVecWrapper iOVecWrapper = IOVecWrapper.get(i2);
        int i3 = i2 + i;
        int i4 = 0;
        int i5 = 0;
        while (i < i3) {
            try {
                if (i5 >= IOV_MAX) {
                    break;
                }
                ByteBuffer byteBuffer = byteBufferArr[i];
                if (!byteBuffer.isReadOnly()) {
                    int position = byteBuffer.position();
                    int limit = byteBuffer.limit();
                    int i6 = position <= limit ? limit - position : 0;
                    if (i6 > 0) {
                        iOVecWrapper.setBuffer(i5, byteBuffer, position, i6);
                        if (!(byteBuffer instanceof DirectBuffer)) {
                            byteBuffer = Util.getTemporaryDirectBuffer(i6);
                            iOVecWrapper.setShadow(i5, byteBuffer);
                            position = byteBuffer.position();
                        }
                        iOVecWrapper.putBase(i5, ((DirectBuffer) byteBuffer).address() + ((long) position));
                        iOVecWrapper.putLen(i5, (long) i6);
                        i5++;
                    }
                    i++;
                } else {
                    throw new IllegalArgumentException("Read-only buffer");
                }
            } catch (Throwable th) {
                while (i4 < i5) {
                    ByteBuffer shadow = iOVecWrapper.getShadow(i4);
                    if (shadow != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow);
                    }
                    iOVecWrapper.clearRefs(i4);
                    i4++;
                }
                throw th;
            }
        }
        if (i5 == 0) {
            while (i4 < i5) {
                ByteBuffer shadow2 = iOVecWrapper.getShadow(i4);
                if (shadow2 != null) {
                    Util.offerLastTemporaryDirectBuffer(shadow2);
                }
                iOVecWrapper.clearRefs(i4);
                i4++;
            }
            return 0;
        }
        long readv = nativeDispatcher.readv(fileDescriptor, iOVecWrapper.address, i5);
        long j = readv;
        for (int i7 = 0; i7 < i5; i7++) {
            ByteBuffer shadow3 = iOVecWrapper.getShadow(i7);
            if (j > 0) {
                ByteBuffer buffer = iOVecWrapper.getBuffer(i7);
                int remaining = iOVecWrapper.getRemaining(i7);
                if (j <= ((long) remaining)) {
                    remaining = (int) j;
                }
                if (shadow3 == null) {
                    buffer.position(iOVecWrapper.getPosition(i7) + remaining);
                } else {
                    shadow3.limit(shadow3.position() + remaining);
                    buffer.put(shadow3);
                }
                j -= (long) remaining;
            }
            if (shadow3 != null) {
                Util.offerLastTemporaryDirectBuffer(shadow3);
            }
            iOVecWrapper.clearRefs(i7);
        }
        return readv;
    }

    public static FileDescriptor newFD(int i) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        setfdVal(fileDescriptor, i);
        return fileDescriptor;
    }
}
