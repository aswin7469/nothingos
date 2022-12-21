package libcore.p030io;

import android.annotation.SystemApi;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.system.ErrnoException;
import android.system.OsConstants;
import java.net.DatagramSocketImpl;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.p026io.File;
import java.p026io.FileDescriptor;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InterruptedIOException;
import java.p026io.RandomAccessFile;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: libcore.io.IoUtils */
public final class IoUtils {
    private IoUtils() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int acquireRawFd(FileDescriptor fileDescriptor) {
        Objects.requireNonNull(fileDescriptor);
        FileDescriptor release$ = fileDescriptor.release$();
        int int$ = release$.getInt$();
        long ownerId$ = release$.getOwnerId$();
        if (!(int$ == -1 || ownerId$ == 0)) {
            Libcore.f857os.android_fdsan_exchange_owner_tag(release$, ownerId$, 0);
        }
        return int$;
    }

    private static boolean isParcelFileDescriptor(Object obj) {
        try {
            if (Class.forName("android.os.ParcelFileDescriptor").isInstance(obj)) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException unused) {
        }
    }

    private static long generateFdOwnerId(Object obj) {
        long j;
        if (obj == null) {
            return 0;
        }
        if (obj instanceof FileInputStream) {
            j = 5;
        } else if (obj instanceof FileOutputStream) {
            j = 6;
        } else if (obj instanceof RandomAccessFile) {
            j = 7;
        } else if (obj instanceof DatagramSocketImpl) {
            j = 10;
        } else if (obj instanceof SocketImpl) {
            j = 11;
        } else {
            j = isParcelFileDescriptor(obj) ? 8 : 255;
        }
        return (j << 56) | (((long) System.identityHashCode(obj)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setFdOwner(FileDescriptor fileDescriptor, Object obj) {
        Objects.requireNonNull(fileDescriptor);
        Objects.requireNonNull(obj);
        long ownerId$ = fileDescriptor.getOwnerId$();
        if (ownerId$ == 0) {
            long generateFdOwnerId = generateFdOwnerId(obj);
            fileDescriptor.setOwnerId$(generateFdOwnerId);
            Libcore.f857os.android_fdsan_exchange_owner_tag(fileDescriptor, ownerId$, generateFdOwnerId);
            return;
        }
        throw new IllegalStateException("Attempted to take ownership of already-owned FileDescriptor");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void close(FileDescriptor fileDescriptor) throws IOException {
        IoBridge.closeAndSignalBlockedThreads(fileDescriptor);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void closeQuietly(FileDescriptor fileDescriptor) {
        try {
            close(fileDescriptor);
        } catch (IOException unused) {
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setBlocking(FileDescriptor fileDescriptor, boolean z) throws IOException {
        int i;
        try {
            int fcntlVoid = Libcore.f857os.fcntlVoid(fileDescriptor, OsConstants.F_GETFL);
            if (!z) {
                i = OsConstants.O_NONBLOCK | fcntlVoid;
            } else {
                i = (~OsConstants.O_NONBLOCK) & fcntlVoid;
            }
            Libcore.f857os.fcntlInt(fileDescriptor, OsConstants.F_SETFL, i);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] readFileAsByteArray(String str) throws IOException {
        return new FileReader(str).readFully().toByteArray();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String readFileAsString(String str) throws IOException {
        return new FileReader(str).readFully().toString(StandardCharsets.UTF_8);
    }

    public static void deleteContents(File file) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    deleteContents(file2);
                }
                file2.delete();
            }
        }
    }

    public static boolean canOpenReadOnly(String str) {
        try {
            Libcore.f857os.close(Libcore.f857os.open(str, OsConstants.O_RDONLY, 0));
            return true;
        } catch (ErrnoException unused) {
            return false;
        }
    }

    public static void throwInterruptedIoException() throws InterruptedIOException {
        Thread.currentThread().interrupt();
        throw new InterruptedIOException();
    }

    /* renamed from: libcore.io.IoUtils$FileReader */
    private static class FileReader {
        private byte[] bytes;
        private int count;

        /* renamed from: fd */
        private FileDescriptor f856fd;
        private boolean unknownLength;

        public FileReader(String str) throws IOException {
            try {
                this.f856fd = IoBridge.open(str, OsConstants.O_RDONLY);
                try {
                    int i = (int) Libcore.f857os.fstat(this.f856fd).st_size;
                    if (i == 0) {
                        this.unknownLength = true;
                        i = 8192;
                    }
                    this.bytes = new byte[i];
                } catch (ErrnoException e) {
                    IoUtils.closeQuietly(this.f856fd);
                    throw e.rethrowAsIOException();
                }
            } catch (FileNotFoundException e2) {
                throw e2;
            }
        }

        public FileReader readFully() throws IOException {
            int length = this.bytes.length;
            while (true) {
                try {
                    C4699Os os = Libcore.f857os;
                    FileDescriptor fileDescriptor = this.f856fd;
                    byte[] bArr = this.bytes;
                    int i = this.count;
                    int read = os.read(fileDescriptor, bArr, i, length - i);
                    if (read == 0) {
                        break;
                    }
                    int i2 = this.count + read;
                    this.count = i2;
                    if (i2 == length) {
                        if (!this.unknownLength) {
                            break;
                        }
                        int i3 = length * 2;
                        byte[] bArr2 = new byte[i3];
                        System.arraycopy((Object) this.bytes, 0, (Object) bArr2, 0, length);
                        this.bytes = bArr2;
                        length = i3;
                    }
                } catch (ErrnoException e) {
                    throw e.rethrowAsIOException();
                } catch (Throwable th) {
                    IoUtils.closeQuietly(this.f856fd);
                    throw th;
                }
            }
            IoUtils.closeQuietly(this.f856fd);
            return this;
        }

        public byte[] toByteArray() {
            int i = this.count;
            byte[] bArr = this.bytes;
            if (i == bArr.length) {
                return bArr;
            }
            byte[] bArr2 = new byte[i];
            System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, i);
            return bArr2;
        }

        public String toString(Charset charset) {
            return new String(this.bytes, 0, this.count, charset);
        }
    }
}
