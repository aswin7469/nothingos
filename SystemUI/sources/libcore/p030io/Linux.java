package libcore.p030io;

import android.system.ErrnoException;
import android.system.GaiException;
import android.system.Int32Ref;
import android.system.Int64Ref;
import android.system.StructAddrinfo;
import android.system.StructCapUserData;
import android.system.StructCapUserHeader;
import android.system.StructGroupReq;
import android.system.StructIfaddrs;
import android.system.StructLinger;
import android.system.StructMsghdr;
import android.system.StructPasswd;
import android.system.StructPollfd;
import android.system.StructRlimit;
import android.system.StructStat;
import android.system.StructStatVfs;
import android.system.StructTimeval;
import android.system.StructUcred;
import android.system.StructUtsname;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.NioUtils;
import java.p026io.FileDescriptor;
import java.p026io.InterruptedIOException;

/* renamed from: libcore.io.Linux */
public final class Linux implements C4711Os {
    private native int preadBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2, long j) throws ErrnoException, InterruptedIOException;

    private native int pwriteBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2, long j) throws ErrnoException, InterruptedIOException;

    private native int readBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2) throws ErrnoException, InterruptedIOException;

    private native int recvfromBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2, int i3, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException;

    private native int sendtoBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2, int i3, InetAddress inetAddress, int i4) throws ErrnoException, SocketException;

    private native int sendtoBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2, int i3, SocketAddress socketAddress) throws ErrnoException, SocketException;

    private native int umaskImpl(int i);

    private native int writeBytes(FileDescriptor fileDescriptor, Object obj, int i, int i2) throws ErrnoException, InterruptedIOException;

    public native FileDescriptor accept(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException;

    public native boolean access(String str, int i) throws ErrnoException;

    public native void android_fdsan_exchange_owner_tag(FileDescriptor fileDescriptor, long j, long j2);

    public native long android_fdsan_get_owner_tag(FileDescriptor fileDescriptor);

    public native String android_fdsan_get_tag_type(long j);

    public native long android_fdsan_get_tag_value(long j);

    public native InetAddress[] android_getaddrinfo(String str, StructAddrinfo structAddrinfo, int i) throws GaiException;

    public native void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException;

    public native void bind(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException;

    public native StructCapUserData[] capget(StructCapUserHeader structCapUserHeader) throws ErrnoException;

    public native void capset(StructCapUserHeader structCapUserHeader, StructCapUserData[] structCapUserDataArr) throws ErrnoException;

    public native void chmod(String str, int i) throws ErrnoException;

    public native void chown(String str, int i, int i2) throws ErrnoException;

    public native void close(FileDescriptor fileDescriptor) throws ErrnoException;

    public native void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException;

    public native void connect(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException;

    public native FileDescriptor dup(FileDescriptor fileDescriptor) throws ErrnoException;

    public native FileDescriptor dup2(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native String[] environ();

    public native void execv(String str, String[] strArr) throws ErrnoException;

    public native void execve(String str, String[] strArr, String[] strArr2) throws ErrnoException;

    public native void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native int fcntlInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native int fcntlVoid(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException;

    public native StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException;

    public native StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException;

    public native void fsync(FileDescriptor fileDescriptor) throws ErrnoException;

    public native void ftruncate(FileDescriptor fileDescriptor, long j) throws ErrnoException;

    public native String gai_strerror(int i);

    public native int getegid();

    public native String getenv(String str);

    public native int geteuid();

    public native int getgid();

    public native StructIfaddrs[] getifaddrs() throws ErrnoException;

    public native String getnameinfo(InetAddress inetAddress, int i) throws GaiException;

    public native SocketAddress getpeername(FileDescriptor fileDescriptor) throws ErrnoException;

    public native int getpgid(int i);

    public native int getpid();

    public native int getppid();

    public native StructPasswd getpwnam(String str) throws ErrnoException;

    public native StructPasswd getpwuid(int i) throws ErrnoException;

    public native StructRlimit getrlimit(int i) throws ErrnoException;

    public native SocketAddress getsockname(FileDescriptor fileDescriptor) throws ErrnoException;

    public native int getsockoptByte(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native InetAddress getsockoptInAddr(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native int getsockoptInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native StructLinger getsockoptLinger(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native StructTimeval getsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native StructUcred getsockoptUcred(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    public native int gettid();

    public native int getuid();

    public native byte[] getxattr(String str, String str2) throws ErrnoException;

    public native String if_indextoname(int i);

    public native int if_nametoindex(String str);

    public native InetAddress inet_pton(int i, String str);

    public native int ioctlFlags(FileDescriptor fileDescriptor, String str) throws ErrnoException;

    public native InetAddress ioctlInetAddress(FileDescriptor fileDescriptor, int i, String str) throws ErrnoException;

    public native int ioctlInt(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native int ioctlMTU(FileDescriptor fileDescriptor, String str) throws ErrnoException;

    public native boolean isatty(FileDescriptor fileDescriptor);

    public native void kill(int i, int i2) throws ErrnoException;

    public native void lchown(String str, int i, int i2) throws ErrnoException;

    public native void link(String str, String str2) throws ErrnoException;

    public native void listen(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native String[] listxattr(String str) throws ErrnoException;

    public native long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException;

    public native StructStat lstat(String str) throws ErrnoException;

    public native FileDescriptor memfd_create(String str, int i) throws ErrnoException;

    public native void mincore(long j, long j2, byte[] bArr) throws ErrnoException;

    public native void mkdir(String str, int i) throws ErrnoException;

    public native void mkfifo(String str, int i) throws ErrnoException;

    public native void mlock(long j, long j2) throws ErrnoException;

    public native long mmap(long j, long j2, int i, int i2, FileDescriptor fileDescriptor, long j3) throws ErrnoException;

    public native void msync(long j, long j2, int i) throws ErrnoException;

    public native void munlock(long j, long j2) throws ErrnoException;

    public native void munmap(long j, long j2) throws ErrnoException;

    public native FileDescriptor open(String str, int i, int i2) throws ErrnoException;

    public native FileDescriptor[] pipe2(int i) throws ErrnoException;

    public native int poll(StructPollfd[] structPollfdArr, int i) throws ErrnoException;

    public native void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException;

    public native int prctl(int i, long j, long j2, long j3, long j4) throws ErrnoException;

    public native String readlink(String str) throws ErrnoException;

    public native int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException;

    public native String realpath(String str) throws ErrnoException;

    public native int recvmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException;

    public native void remove(String str) throws ErrnoException;

    public native void removexattr(String str, String str2) throws ErrnoException;

    public native void rename(String str, String str2) throws ErrnoException;

    public native long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, Int64Ref int64Ref, long j) throws ErrnoException;

    public native int sendmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException;

    public native void setegid(int i) throws ErrnoException;

    public native void setenv(String str, String str2, boolean z) throws ErrnoException;

    public native void seteuid(int i) throws ErrnoException;

    public native void setgid(int i) throws ErrnoException;

    public native void setpgid(int i, int i2) throws ErrnoException;

    public native void setregid(int i, int i2) throws ErrnoException;

    public native void setreuid(int i, int i2) throws ErrnoException;

    public native int setsid() throws ErrnoException;

    public native void setsockoptByte(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException;

    public native void setsockoptGroupReq(FileDescriptor fileDescriptor, int i, int i2, StructGroupReq structGroupReq) throws ErrnoException;

    public native void setsockoptIfreq(FileDescriptor fileDescriptor, int i, int i2, String str) throws ErrnoException;

    public native void setsockoptInt(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException;

    public native void setsockoptIpMreqn(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException;

    public native void setsockoptLinger(FileDescriptor fileDescriptor, int i, int i2, StructLinger structLinger) throws ErrnoException;

    public native void setsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2, StructTimeval structTimeval) throws ErrnoException;

    public native void setuid(int i) throws ErrnoException;

    public native void setxattr(String str, String str2, byte[] bArr, int i) throws ErrnoException;

    public native void shutdown(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native FileDescriptor socket(int i, int i2, int i3) throws ErrnoException;

    public native void socketpair(int i, int i2, int i3, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws ErrnoException;

    public native long splice(FileDescriptor fileDescriptor, Int64Ref int64Ref, FileDescriptor fileDescriptor2, Int64Ref int64Ref2, long j, int i) throws ErrnoException;

    public native StructStat stat(String str) throws ErrnoException;

    public native StructStatVfs statvfs(String str) throws ErrnoException;

    public native String strerror(int i);

    public native String strsignal(int i);

    public native void symlink(String str, String str2) throws ErrnoException;

    public native long sysconf(int i);

    public native void tcdrain(FileDescriptor fileDescriptor) throws ErrnoException;

    public native void tcsendbreak(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    public native StructUtsname uname();

    public native void unlink(String str) throws ErrnoException;

    public native void unsetenv(String str) throws ErrnoException;

    public native int waitpid(int i, Int32Ref int32Ref, int i2) throws ErrnoException;

    public native int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException;

    Linux() {
    }

    public int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        int i;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i = preadBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining(), j);
        } else {
            i = preadBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining(), j);
        }
        maybeUpdateBufferPosition(byteBuffer, position, i);
        return i;
    }

    public int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return preadBytes(fileDescriptor, bArr, i, i2, j);
    }

    public int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        int i;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i = pwriteBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining(), j);
        } else {
            i = pwriteBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining(), j);
        }
        maybeUpdateBufferPosition(byteBuffer, position, i);
        return i;
    }

    public int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return pwriteBytes(fileDescriptor, bArr, i, i2, j);
    }

    public int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        int i;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i = readBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining());
        } else {
            i = readBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining());
        }
        maybeUpdateBufferPosition(byteBuffer, position, i);
        return i;
    }

    public int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return readBytes(fileDescriptor, bArr, i, i2);
    }

    public int recvfrom(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        int i2;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i2 = recvfromBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining(), i, inetSocketAddress);
        } else {
            i2 = recvfromBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining(), i, inetSocketAddress);
        }
        maybeUpdateBufferPosition(byteBuffer, position, i2);
        return i2;
    }

    public int recvfrom(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return recvfromBytes(fileDescriptor, bArr, i, i2, i3, inetSocketAddress);
    }

    public int sendto(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetAddress inetAddress, int i2) throws ErrnoException, SocketException {
        int i3;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i3 = sendtoBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining(), i, inetAddress, i2);
        } else {
            i3 = sendtoBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining(), i, inetAddress, i2);
        }
        maybeUpdateBufferPosition(byteBuffer, position, i3);
        return i3;
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetAddress inetAddress, int i4) throws ErrnoException, SocketException {
        return sendtoBytes(fileDescriptor, bArr, i, i2, i3, inetAddress, i4);
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, SocketAddress socketAddress) throws ErrnoException, SocketException {
        return sendtoBytes(fileDescriptor, bArr, i, i2, i3, socketAddress);
    }

    public int umask(int i) {
        if ((i & 511) == i) {
            return umaskImpl(i);
        }
        throw new IllegalArgumentException("Invalid umask: " + i);
    }

    public int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        int i;
        int position = byteBuffer.position();
        if (byteBuffer.isDirect()) {
            i = writeBytes(fileDescriptor, byteBuffer, position, byteBuffer.remaining());
        } else {
            i = writeBytes(fileDescriptor, NioUtils.unsafeArray(byteBuffer), NioUtils.unsafeArrayOffset(byteBuffer) + position, byteBuffer.remaining());
        }
        maybeUpdateBufferPosition(byteBuffer, position, i);
        return i;
    }

    public int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return writeBytes(fileDescriptor, bArr, i, i2);
    }

    private static void maybeUpdateBufferPosition(ByteBuffer byteBuffer, int i, int i2) {
        if (i2 > 0) {
            byteBuffer.position(i2 + i);
        }
    }
}
