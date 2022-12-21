package libcore.p030io;

import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.GaiException;
import android.system.Int64Ref;
import android.system.OsConstants;
import android.system.StructAddrinfo;
import android.system.StructLinger;
import android.system.StructMsghdr;
import android.system.StructPollfd;
import android.system.StructStat;
import android.system.StructStatVfs;
import dalvik.system.BlockGuard;
import dalvik.system.SocketTagger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.p026io.FileDescriptor;
import java.p026io.InterruptedIOException;

/* renamed from: libcore.io.BlockGuardOs */
public class BlockGuardOs extends ForwardingOs {
    public BlockGuardOs(C4699Os os) {
        super(os);
    }

    private FileDescriptor tagSocket(FileDescriptor fileDescriptor) throws ErrnoException {
        try {
            SocketTagger.get().tag(fileDescriptor);
            return fileDescriptor;
        } catch (SocketException e) {
            throw new ErrnoException("socket", OsConstants.EINVAL, e);
        }
    }

    public FileDescriptor accept(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        if (!isUnixSocket(fileDescriptor) || !isNonBlockingFile(fileDescriptor)) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        FileDescriptor accept = super.accept(fileDescriptor, socketAddress);
        if (accept != null && isInetSocket(accept)) {
            tagSocket(accept);
        }
        return accept;
    }

    public boolean access(String str, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.access(str, i);
    }

    public void chmod(String str, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.chmod(str, i);
    }

    public void chown(String str, int i, int i2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.chown(str, i, i2);
    }

    public void close(FileDescriptor fileDescriptor) throws ErrnoException {
        try {
            if (fileDescriptor.isSocket$() && isLingerSocket(fileDescriptor)) {
                BlockGuard.getThreadPolicy().onNetwork();
            }
        } catch (ErrnoException unused) {
        }
        super.close(fileDescriptor);
    }

    public static boolean isNonBlockingFile(FileDescriptor fileDescriptor) throws ErrnoException {
        if ((C0308Os.fcntlInt(fileDescriptor, OsConstants.F_GETFL, 0) & OsConstants.O_NONBLOCK) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isUnixSocket(FileDescriptor fileDescriptor) throws ErrnoException {
        return isUnixDomain(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_DOMAIN));
    }

    private static boolean isUnixDomain(int i) {
        return i == OsConstants.AF_UNIX;
    }

    private static boolean isInetSocket(FileDescriptor fileDescriptor) throws ErrnoException {
        return isInetDomain(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_DOMAIN));
    }

    private static boolean isInetDomain(int i) {
        return i == OsConstants.AF_INET || i == OsConstants.AF_INET6;
    }

    private static boolean isLingerSocket(FileDescriptor fileDescriptor) throws ErrnoException {
        StructLinger structLinger = Libcore.f857os.getsockoptLinger(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_LINGER);
        return structLinger.isOn() && structLinger.l_linger > 0;
    }

    private static boolean isUdpSocket(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_PROTOCOL) == OsConstants.IPPROTO_UDP;
    }

    public void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException {
        boolean z;
        try {
            z = isUdpSocket(fileDescriptor);
        } catch (ErrnoException unused) {
            z = false;
        }
        if (!z) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        super.connect(fileDescriptor, inetAddress, i);
    }

    public void connect(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        boolean z;
        try {
            z = isUdpSocket(fileDescriptor);
        } catch (ErrnoException unused) {
            z = false;
        }
        if (!z) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        super.connect(fileDescriptor, socketAddress);
    }

    public void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.fchmod(fileDescriptor, i);
    }

    public void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.fchown(fileDescriptor, i, i2);
    }

    public void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.fdatasync(fileDescriptor);
    }

    public StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.fstat(fileDescriptor);
    }

    public StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.fstatvfs(fileDescriptor);
    }

    public void fsync(FileDescriptor fileDescriptor) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.fsync(fileDescriptor);
    }

    public void ftruncate(FileDescriptor fileDescriptor, long j) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.ftruncate(fileDescriptor, j);
    }

    public InetAddress[] android_getaddrinfo(String str, StructAddrinfo structAddrinfo, int i) throws GaiException {
        if (!((structAddrinfo.ai_flags & OsConstants.AI_NUMERICHOST) != 0)) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        return super.android_getaddrinfo(str, structAddrinfo, i);
    }

    public void lchown(String str, int i, int i2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.lchown(str, i, i2);
    }

    public void link(String str, String str2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        BlockGuard.getVmPolicy().onPathAccess(str2);
        super.link(str, str2);
    }

    public long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.lseek(fileDescriptor, j, i);
    }

    public StructStat lstat(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.lstat(str);
    }

    public void mkdir(String str, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.mkdir(str, i);
    }

    public void mkfifo(String str, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.mkfifo(str, i);
    }

    public FileDescriptor open(String str, int i, int i2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        if ((OsConstants.O_ACCMODE & i) != OsConstants.O_RDONLY) {
            BlockGuard.getThreadPolicy().onWriteToDisk();
        }
        return super.open(str, i, i2);
    }

    public int poll(StructPollfd[] structPollfdArr, int i) throws ErrnoException {
        if (i != 0) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        return super.poll(structPollfdArr, i);
    }

    public void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        super.posix_fallocate(fileDescriptor, j, j2);
    }

    public int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.pread(fileDescriptor, byteBuffer, j);
    }

    public int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.pread(fileDescriptor, bArr, i, i2, j);
    }

    public int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.pwrite(fileDescriptor, byteBuffer, j);
    }

    public int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.pwrite(fileDescriptor, bArr, i, i2, j);
    }

    public int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.read(fileDescriptor, byteBuffer);
    }

    public int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.read(fileDescriptor, bArr, i, i2);
    }

    public String readlink(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.readlink(str);
    }

    public String realpath(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.realpath(str);
    }

    public int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.readv(fileDescriptor, objArr, iArr, iArr2);
    }

    public int recvfrom(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.recvfrom(fileDescriptor, byteBuffer, i, inetSocketAddress);
    }

    public int recvfrom(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.recvfrom(fileDescriptor, bArr, i, i2, i3, inetSocketAddress);
    }

    public int recvmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.recvmsg(fileDescriptor, structMsghdr, i);
    }

    public void remove(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.remove(str);
    }

    public void rename(String str, String str2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        BlockGuard.getVmPolicy().onPathAccess(str2);
        super.rename(str, str2);
    }

    public long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, Int64Ref int64Ref, long j) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.sendfile(fileDescriptor, fileDescriptor2, int64Ref, j);
    }

    public int sendmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.sendmsg(fileDescriptor, structMsghdr, i);
    }

    public int sendto(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetAddress inetAddress, int i2) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.sendto(fileDescriptor, byteBuffer, i, inetAddress, i2);
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetAddress inetAddress, int i4) throws ErrnoException, SocketException {
        if (inetAddress != null) {
            BlockGuard.getThreadPolicy().onNetwork();
        }
        return super.sendto(fileDescriptor, bArr, i, i2, i3, inetAddress, i4);
    }

    public FileDescriptor socket(int i, int i2, int i3) throws ErrnoException {
        FileDescriptor socket = super.socket(i, i2, i3);
        if (isInetDomain(i)) {
            tagSocket(socket);
        }
        return socket;
    }

    public void socketpair(int i, int i2, int i3, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws ErrnoException {
        super.socketpair(i, i2, i3, fileDescriptor, fileDescriptor2);
        if (isInetDomain(i)) {
            tagSocket(fileDescriptor);
            tagSocket(fileDescriptor2);
        }
    }

    public StructStat stat(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.stat(str);
    }

    public StructStatVfs statvfs(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.statvfs(str);
    }

    public void symlink(String str, String str2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        BlockGuard.getVmPolicy().onPathAccess(str2);
        super.symlink(str, str2);
    }

    public int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.write(fileDescriptor, byteBuffer);
    }

    public int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.write(fileDescriptor, bArr, i, i2);
    }

    public int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return super.writev(fileDescriptor, objArr, iArr, iArr2);
    }

    public void execv(String str, String[] strArr) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.execv(str, strArr);
    }

    public void execve(String str, String[] strArr, String[] strArr2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.execve(str, strArr, strArr2);
    }

    public byte[] getxattr(String str, String str2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return super.getxattr(str, str2);
    }

    public void msync(long j, long j2, int i) throws ErrnoException {
        if ((OsConstants.MS_SYNC & i) != 0) {
            BlockGuard.getThreadPolicy().onWriteToDisk();
        }
        super.msync(j, j2, i);
    }

    public void removexattr(String str, String str2) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.removexattr(str, str2);
    }

    public void setxattr(String str, String str2, byte[] bArr, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.setxattr(str, str2, bArr, i);
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, SocketAddress socketAddress) throws ErrnoException, SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        return super.sendto(fileDescriptor, bArr, i, i2, i3, socketAddress);
    }

    public void unlink(String str) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        super.unlink(str);
    }

    public long splice(FileDescriptor fileDescriptor, Int64Ref int64Ref, FileDescriptor fileDescriptor2, Int64Ref int64Ref2, long j, int i) throws ErrnoException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return super.splice(fileDescriptor, int64Ref, fileDescriptor2, int64Ref2, j, i);
    }
}
