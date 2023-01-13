package java.p026io;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.OsConstants;
import dalvik.system.CloseGuard;
import java.nio.channels.FileChannel;
import libcore.p030io.IoBridge;
import libcore.p030io.IoTracker;
import libcore.p030io.IoUtils;
import libcore.p030io.Libcore;
import sun.nio.p033ch.FileChannelImpl;

/* renamed from: java.io.RandomAccessFile */
public class RandomAccessFile implements DataOutput, DataInput, Closeable {
    private static final int FLUSH_FDATASYNC = 2;
    private static final int FLUSH_FSYNC = 1;
    private static final int FLUSH_NONE = 0;
    private FileChannel channel;
    private Object closeLock;
    private volatile boolean closed;

    /* renamed from: fd */
    private FileDescriptor f528fd;
    private int flushAfterWrite;
    private final CloseGuard guard;
    private final IoTracker ioTracker;
    private int mode;
    private final String path;

    /* renamed from: rw */
    private boolean f529rw;
    private final byte[] scratch;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public RandomAccessFile(String name, String mode2) throws FileNotFoundException {
        this(name != null ? new File(name) : null, mode2);
    }

    public RandomAccessFile(File file, String mode2) throws FileNotFoundException {
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.scratch = new byte[8];
        this.flushAfterWrite = 0;
        String str = null;
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        this.ioTracker = new IoTracker();
        String path2 = file != null ? file.getPath() : str;
        int i = -1;
        if (mode2.equals("r")) {
            i = OsConstants.O_RDONLY;
        } else if (mode2.startsWith("rw")) {
            i = OsConstants.O_RDWR | OsConstants.O_CREAT;
            this.f529rw = true;
            if (mode2.length() > 2) {
                if (mode2.equals("rws")) {
                    this.flushAfterWrite = 1;
                } else if (mode2.equals("rwd")) {
                    this.flushAfterWrite = 2;
                } else {
                    i = -1;
                }
            }
        }
        if (i < 0) {
            throw new IllegalArgumentException("Illegal mode \"" + mode2 + "\" must be one of \"r\", \"rw\", \"rws\", or \"rwd\"");
        } else if (path2 == null) {
            throw new NullPointerException("file == null");
        } else if (!file.isInvalid()) {
            this.path = path2;
            this.mode = i;
            FileDescriptor open = IoBridge.open(path2, i);
            this.f528fd = open;
            IoUtils.setFdOwner(open, this);
            maybeSync();
            closeGuard.open("close");
        } else {
            throw new FileNotFoundException("Invalid file path");
        }
    }

    private void maybeSync() {
        int i = this.flushAfterWrite;
        if (i == 1) {
            try {
                this.f528fd.sync();
            } catch (IOException e) {
            }
        } else if (i == 2) {
            try {
                C0308Os.fdatasync(this.f528fd);
            } catch (ErrnoException e2) {
            }
        }
    }

    public final FileDescriptor getFD() throws IOException {
        FileDescriptor fileDescriptor = this.f528fd;
        if (fileDescriptor != null) {
            return fileDescriptor;
        }
        throw new IOException();
    }

    public final FileChannel getChannel() {
        FileChannel fileChannel;
        synchronized (this) {
            if (this.channel == null) {
                this.channel = FileChannelImpl.open(this.f528fd, this.path, true, this.f529rw, this);
            }
            fileChannel = this.channel;
        }
        return fileChannel;
    }

    public int read() throws IOException {
        if (read(this.scratch, 0, 1) != -1) {
            return this.scratch[0] & 255;
        }
        return -1;
    }

    private int readBytes(byte[] b, int off, int len) throws IOException {
        this.ioTracker.trackIo(len, IoTracker.Mode.READ);
        return IoBridge.read(this.f528fd, b, off, len);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return readBytes(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return readBytes(b, 0, b.length);
    }

    public final void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        int i = 0;
        do {
            int read = read(b, off + i, len - i);
            if (read >= 0) {
                i += read;
            } else {
                throw new EOFException();
            }
        } while (i < len);
    }

    public int skipBytes(int n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        long filePointer = getFilePointer();
        long length = length();
        long j = ((long) n) + filePointer;
        if (j > length) {
            j = length;
        }
        seek(j);
        return (int) (j - filePointer);
    }

    public void write(int b) throws IOException {
        byte[] bArr = this.scratch;
        bArr[0] = (byte) (b & 255);
        write(bArr, 0, 1);
    }

    private void writeBytes(byte[] b, int off, int len) throws IOException {
        this.ioTracker.trackIo(len, IoTracker.Mode.WRITE);
        IoBridge.write(this.f528fd, b, off, len);
        maybeSync();
    }

    public void write(byte[] b) throws IOException {
        writeBytes(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        writeBytes(b, off, len);
    }

    public long getFilePointer() throws IOException {
        try {
            return Libcore.f855os.lseek(this.f528fd, 0, OsConstants.SEEK_CUR);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public void seek(long pos) throws IOException {
        if (pos >= 0) {
            try {
                Libcore.f855os.lseek(this.f528fd, pos, OsConstants.SEEK_SET);
                this.ioTracker.reset();
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        } else {
            throw new IOException("offset < 0: " + pos);
        }
    }

    public long length() throws IOException {
        try {
            return Libcore.f855os.fstat(this.f528fd).st_size;
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public void setLength(long newLength) throws IOException {
        if (newLength >= 0) {
            try {
                Libcore.f855os.ftruncate(this.f528fd, newLength);
                if (getFilePointer() > newLength) {
                    seek(newLength);
                }
                maybeSync();
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        } else {
            throw new IllegalArgumentException("newLength < 0");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        if (r0 == null) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
        if (r0.isOpen() == false) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        r2.channel.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r2.f528fd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        r0 = r2.channel;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.p026io.IOException {
        /*
            r2 = this;
            dalvik.system.CloseGuard r0 = r2.guard
            r0.close()
            java.lang.Object r0 = r2.closeLock
            monitor-enter(r0)
            boolean r1 = r2.closed     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return
        L_0x000e:
            r1 = 1
            r2.closed = r1     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            java.nio.channels.FileChannel r0 = r2.channel
            if (r0 == 0) goto L_0x0021
            boolean r0 = r0.isOpen()
            if (r0 == 0) goto L_0x0021
            java.nio.channels.FileChannel r0 = r2.channel
            r0.close()
        L_0x0021:
            java.io.FileDescriptor r0 = r2.f528fd
            libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r0)
            return
        L_0x0027:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.RandomAccessFile.close():void");
    }

    public final boolean readBoolean() throws IOException {
        int read = read();
        if (read >= 0) {
            return read != 0;
        }
        throw new EOFException();
    }

    public final byte readByte() throws IOException {
        int read = read();
        if (read >= 0) {
            return (byte) read;
        }
        throw new EOFException();
    }

    public final int readUnsignedByte() throws IOException {
        int read = read();
        if (read >= 0) {
            return read;
        }
        throw new EOFException();
    }

    public final short readShort() throws IOException {
        int read = read();
        int read2 = read();
        if ((read | read2) >= 0) {
            return (short) ((read << 8) + (read2 << 0));
        }
        throw new EOFException();
    }

    public final int readUnsignedShort() throws IOException {
        int read = read();
        int read2 = read();
        if ((read | read2) >= 0) {
            return (read << 8) + (read2 << 0);
        }
        throw new EOFException();
    }

    public final char readChar() throws IOException {
        int read = read();
        int read2 = read();
        if ((read | read2) >= 0) {
            return (char) ((read << 8) + (read2 << 0));
        }
        throw new EOFException();
    }

    public final int readInt() throws IOException {
        int read = read();
        int read2 = read();
        int read3 = read();
        int read4 = read();
        if ((read | read2 | read3 | read4) >= 0) {
            return (read << 24) + (read2 << 16) + (read3 << 8) + (read4 << 0);
        }
        throw new EOFException();
    }

    public final long readLong() throws IOException {
        return (((long) readInt()) << 32) + (((long) readInt()) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final String readLine() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        int i = -1;
        boolean z = false;
        while (!z) {
            int read = read();
            i = read;
            if (read == -1 || read == 10) {
                z = true;
            } else if (read != 13) {
                stringBuffer.append((char) i);
            } else {
                z = true;
                long filePointer = getFilePointer();
                if (read() != 10) {
                    seek(filePointer);
                }
            }
        }
        if (i == -1 && stringBuffer.length() == 0) {
            return null;
        }
        return stringBuffer.toString();
    }

    public final String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    public final void writeBoolean(boolean v) throws IOException {
        write((int) v);
    }

    public final void writeByte(int v) throws IOException {
        write(v);
    }

    public final void writeShort(int v) throws IOException {
        write((v >>> 8) & 255);
        write((v >>> 0) & 255);
    }

    public final void writeChar(int v) throws IOException {
        write((v >>> 8) & 255);
        write((v >>> 0) & 255);
    }

    public final void writeInt(int v) throws IOException {
        write((v >>> 24) & 255);
        write((v >>> 16) & 255);
        write((v >>> 8) & 255);
        write((v >>> 0) & 255);
    }

    public final void writeLong(long v) throws IOException {
        write(((int) (v >>> 56)) & 255);
        write(((int) (v >>> 48)) & 255);
        write(((int) (v >>> 40)) & 255);
        write(((int) (v >>> 32)) & 255);
        write(((int) (v >>> 24)) & 255);
        write(((int) (v >>> 16)) & 255);
        write(((int) (v >>> 8)) & 255);
        write(((int) (v >>> 0)) & 255);
    }

    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public final void writeBytes(String s) throws IOException {
        int length = s.length();
        byte[] bArr = new byte[length];
        s.getBytes(0, length, bArr, 0);
        writeBytes(bArr, 0, length);
    }

    public final void writeChars(String s) throws IOException {
        int length = s.length();
        int i = length * 2;
        byte[] bArr = new byte[i];
        char[] cArr = new char[length];
        s.getChars(0, length, cArr, 0);
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = i2 + 1;
            bArr[i2] = (byte) (cArr[i3] >>> 8);
            i2 = i4 + 1;
            bArr[i4] = (byte) (cArr[i3] >>> 0);
        }
        writeBytes(bArr, 0, i);
    }

    public final void writeUTF(String str) throws IOException {
        DataOutputStream.writeUTF(str, this);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.guard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }
}
