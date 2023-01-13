package android.system;

import android.annotation.SystemApi;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.p026io.FileDescriptor;
import java.p026io.InterruptedIOException;
import libcore.p030io.Libcore;

/* renamed from: android.system.Os */
public final class C0308Os {
    private C0308Os() {
    }

    public static FileDescriptor accept(FileDescriptor fileDescriptor, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return accept(fileDescriptor, (SocketAddress) inetSocketAddress);
    }

    public static FileDescriptor accept(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        return Libcore.f855os.accept(fileDescriptor, socketAddress);
    }

    public static boolean access(String str, int i) throws ErrnoException {
        return Libcore.f855os.access(str, i);
    }

    public static InetAddress[] android_getaddrinfo(String str, StructAddrinfo structAddrinfo, int i) throws GaiException {
        return Libcore.f855os.android_getaddrinfo(str, structAddrinfo, i);
    }

    public static void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException {
        Libcore.f855os.bind(fileDescriptor, inetAddress, i);
    }

    public static void bind(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        Libcore.f855os.bind(fileDescriptor, socketAddress);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static StructCapUserData[] capget(StructCapUserHeader structCapUserHeader) throws ErrnoException {
        return Libcore.f855os.capget(structCapUserHeader);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void capset(StructCapUserHeader structCapUserHeader, StructCapUserData[] structCapUserDataArr) throws ErrnoException {
        Libcore.f855os.capset(structCapUserHeader, structCapUserDataArr);
    }

    public static void chmod(String str, int i) throws ErrnoException {
        Libcore.f855os.chmod(str, i);
    }

    public static void chown(String str, int i, int i2) throws ErrnoException {
        Libcore.f855os.chown(str, i, i2);
    }

    public static void close(FileDescriptor fileDescriptor) throws ErrnoException {
        Libcore.f855os.close(fileDescriptor);
    }

    public static void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException {
        Libcore.f855os.connect(fileDescriptor, inetAddress, i);
    }

    public static void connect(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        Libcore.f855os.connect(fileDescriptor, socketAddress);
    }

    public static FileDescriptor dup(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f855os.dup(fileDescriptor);
    }

    public static FileDescriptor dup2(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return Libcore.f855os.dup2(fileDescriptor, i);
    }

    public static String[] environ() {
        return Libcore.f855os.environ();
    }

    public static void execv(String str, String[] strArr) throws ErrnoException {
        Libcore.f855os.execv(str, strArr);
    }

    public static void execve(String str, String[] strArr, String[] strArr2) throws ErrnoException {
        Libcore.f855os.execve(str, strArr, strArr2);
    }

    public static void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        Libcore.f855os.fchmod(fileDescriptor, i);
    }

    public static void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        Libcore.f855os.fchown(fileDescriptor, i, i2);
    }

    public static int fcntlInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.fcntlInt(fileDescriptor, i, i2);
    }

    public static int fcntlVoid(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return Libcore.f855os.fcntlVoid(fileDescriptor, i);
    }

    public static void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException {
        Libcore.f855os.fdatasync(fileDescriptor);
    }

    public static StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f855os.fstat(fileDescriptor);
    }

    public static StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f855os.fstatvfs(fileDescriptor);
    }

    public static void fsync(FileDescriptor fileDescriptor) throws ErrnoException {
        Libcore.f855os.fsync(fileDescriptor);
    }

    public static void ftruncate(FileDescriptor fileDescriptor, long j) throws ErrnoException {
        Libcore.f855os.ftruncate(fileDescriptor, j);
    }

    public static String gai_strerror(int i) {
        return Libcore.f855os.gai_strerror(i);
    }

    public static int getegid() {
        return Libcore.f855os.getegid();
    }

    public static int geteuid() {
        return Libcore.f855os.geteuid();
    }

    public static int getgid() {
        return Libcore.f855os.getgid();
    }

    public static String getenv(String str) {
        return Libcore.f855os.getenv(str);
    }

    public static StructIfaddrs[] getifaddrs() throws ErrnoException {
        return Libcore.f855os.getifaddrs();
    }

    public static String getnameinfo(InetAddress inetAddress, int i) throws GaiException {
        return Libcore.f855os.getnameinfo(inetAddress, i);
    }

    public static SocketAddress getpeername(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f855os.getpeername(fileDescriptor);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int getpgid(int i) throws ErrnoException {
        return Libcore.f855os.getpgid(i);
    }

    public static int getpid() {
        return Libcore.f855os.getpid();
    }

    public static int getppid() {
        return Libcore.f855os.getppid();
    }

    public static StructPasswd getpwnam(String str) throws ErrnoException {
        return Libcore.f855os.getpwnam(str);
    }

    public static StructPasswd getpwuid(int i) throws ErrnoException {
        return Libcore.f855os.getpwuid(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static StructRlimit getrlimit(int i) throws ErrnoException {
        return Libcore.f855os.getrlimit(i);
    }

    public static SocketAddress getsockname(FileDescriptor fileDescriptor) throws ErrnoException {
        return Libcore.f855os.getsockname(fileDescriptor);
    }

    public static int getsockoptByte(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptByte(fileDescriptor, i, i2);
    }

    public static InetAddress getsockoptInAddr(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptInAddr(fileDescriptor, i, i2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int getsockoptInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptInt(fileDescriptor, i, i2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static StructLinger getsockoptLinger(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptLinger(fileDescriptor, i, i2);
    }

    public static StructTimeval getsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptTimeval(fileDescriptor, i, i2);
    }

    public static StructUcred getsockoptUcred(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return Libcore.f855os.getsockoptUcred(fileDescriptor, i, i2);
    }

    public static int gettid() {
        return Libcore.f855os.gettid();
    }

    public static int getuid() {
        return Libcore.f855os.getuid();
    }

    public static byte[] getxattr(String str, String str2) throws ErrnoException {
        return Libcore.f855os.getxattr(str, str2);
    }

    public static String if_indextoname(int i) {
        return Libcore.f855os.if_indextoname(i);
    }

    public static int if_nametoindex(String str) {
        return Libcore.f855os.if_nametoindex(str);
    }

    public static InetAddress inet_pton(int i, String str) {
        return Libcore.f855os.inet_pton(i, str);
    }

    public static InetAddress ioctlInetAddress(FileDescriptor fileDescriptor, int i, String str) throws ErrnoException {
        return Libcore.f855os.ioctlInetAddress(fileDescriptor, i, str);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int ioctlInt(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return Libcore.f855os.ioctlInt(fileDescriptor, i);
    }

    public static boolean isatty(FileDescriptor fileDescriptor) {
        return Libcore.f855os.isatty(fileDescriptor);
    }

    public static void kill(int i, int i2) throws ErrnoException {
        Libcore.f855os.kill(i, i2);
    }

    public static void lchown(String str, int i, int i2) throws ErrnoException {
        Libcore.f855os.lchown(str, i, i2);
    }

    public static void link(String str, String str2) throws ErrnoException {
        Libcore.f855os.link(str, str2);
    }

    public static void listen(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        Libcore.f855os.listen(fileDescriptor, i);
    }

    public static String[] listxattr(String str) throws ErrnoException {
        return Libcore.f855os.listxattr(str);
    }

    public static long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException {
        return Libcore.f855os.lseek(fileDescriptor, j, i);
    }

    public static StructStat lstat(String str) throws ErrnoException {
        return Libcore.f855os.lstat(str);
    }

    public static FileDescriptor memfd_create(String str, int i) throws ErrnoException {
        return Libcore.f855os.memfd_create(str, i);
    }

    public static void mincore(long j, long j2, byte[] bArr) throws ErrnoException {
        Libcore.f855os.mincore(j, j2, bArr);
    }

    public static void mkdir(String str, int i) throws ErrnoException {
        Libcore.f855os.mkdir(str, i);
    }

    public static void mkfifo(String str, int i) throws ErrnoException {
        Libcore.f855os.mkfifo(str, i);
    }

    public static void mlock(long j, long j2) throws ErrnoException {
        Libcore.f855os.mlock(j, j2);
    }

    public static long mmap(long j, long j2, int i, int i2, FileDescriptor fileDescriptor, long j3) throws ErrnoException {
        return Libcore.f855os.mmap(j, j2, i, i2, fileDescriptor, j3);
    }

    public static void msync(long j, long j2, int i) throws ErrnoException {
        Libcore.f855os.msync(j, j2, i);
    }

    public static void munlock(long j, long j2) throws ErrnoException {
        Libcore.f855os.munlock(j, j2);
    }

    public static void munmap(long j, long j2) throws ErrnoException {
        Libcore.f855os.munmap(j, j2);
    }

    public static FileDescriptor open(String str, int i, int i2) throws ErrnoException {
        return Libcore.f855os.open(str, i, i2);
    }

    public static FileDescriptor[] pipe() throws ErrnoException {
        return Libcore.f855os.pipe2(0);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static FileDescriptor[] pipe2(int i) throws ErrnoException {
        return Libcore.f855os.pipe2(i);
    }

    public static int poll(StructPollfd[] structPollfdArr, int i) throws ErrnoException {
        return Libcore.f855os.poll(structPollfdArr, i);
    }

    public static void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException {
        Libcore.f855os.posix_fallocate(fileDescriptor, j, j2);
    }

    public static int prctl(int i, long j, long j2, long j3, long j4) throws ErrnoException {
        return Libcore.f855os.prctl(i, j, j2, j3, j4);
    }

    public static int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.pread(fileDescriptor, byteBuffer, j);
    }

    public static int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.pread(fileDescriptor, bArr, i, i2, j);
    }

    public static int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.pwrite(fileDescriptor, byteBuffer, j);
    }

    public static int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.pwrite(fileDescriptor, bArr, i, i2, j);
    }

    public static int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.read(fileDescriptor, byteBuffer);
    }

    public static int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.read(fileDescriptor, bArr, i, i2);
    }

    public static String readlink(String str) throws ErrnoException {
        return Libcore.f855os.readlink(str);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String realpath(String str) throws ErrnoException {
        return Libcore.f855os.realpath(str);
    }

    public static int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.readv(fileDescriptor, objArr, iArr, iArr2);
    }

    public static int recvfrom(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return Libcore.f855os.recvfrom(fileDescriptor, byteBuffer, i, inetSocketAddress);
    }

    public static int recvfrom(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return Libcore.f855os.recvfrom(fileDescriptor, bArr, i, i2, i3, inetSocketAddress);
    }

    public static int recvmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        return Libcore.f855os.recvmsg(fileDescriptor, structMsghdr, i);
    }

    public static void remove(String str) throws ErrnoException {
        Libcore.f855os.remove(str);
    }

    public static void removexattr(String str, String str2) throws ErrnoException {
        Libcore.f855os.removexattr(str, str2);
    }

    public static void rename(String str, String str2) throws ErrnoException {
        Libcore.f855os.rename(str, str2);
    }

    public static long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, Int64Ref int64Ref, long j) throws ErrnoException {
        return Libcore.f855os.sendfile(fileDescriptor, fileDescriptor2, int64Ref, j);
    }

    public static int sendmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        return Libcore.f855os.sendmsg(fileDescriptor, structMsghdr, i);
    }

    public static int sendto(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetAddress inetAddress, int i2) throws ErrnoException, SocketException {
        return Libcore.f855os.sendto(fileDescriptor, byteBuffer, i, inetAddress, i2);
    }

    public static int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetAddress inetAddress, int i4) throws ErrnoException, SocketException {
        return Libcore.f855os.sendto(fileDescriptor, bArr, i, i2, i3, inetAddress, i4);
    }

    public static int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, SocketAddress socketAddress) throws ErrnoException, SocketException {
        return Libcore.f855os.sendto(fileDescriptor, bArr, i, i2, i3, socketAddress);
    }

    @Deprecated
    public static void setegid(int i) throws ErrnoException {
        Libcore.f855os.setegid(i);
    }

    public static void setenv(String str, String str2, boolean z) throws ErrnoException {
        Libcore.f855os.setenv(str, str2, z);
    }

    @Deprecated
    public static void seteuid(int i) throws ErrnoException {
        Libcore.f855os.seteuid(i);
    }

    @Deprecated
    public static void setgid(int i) throws ErrnoException {
        Libcore.f855os.setgid(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setpgid(int i, int i2) throws ErrnoException {
        Libcore.f855os.setpgid(i, i2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setregid(int i, int i2) throws ErrnoException {
        Libcore.f855os.setregid(i, i2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setreuid(int i, int i2) throws ErrnoException {
        Libcore.f855os.setreuid(i, i2);
    }

    public static int setsid() throws ErrnoException {
        return Libcore.f855os.setsid();
    }

    public static void setsockoptByte(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        Libcore.f855os.setsockoptByte(fileDescriptor, i, i2, i3);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setsockoptIfreq(FileDescriptor fileDescriptor, int i, int i2, String str) throws ErrnoException {
        Libcore.f855os.setsockoptIfreq(fileDescriptor, i, i2, str);
    }

    public static void setsockoptInt(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        Libcore.f855os.setsockoptInt(fileDescriptor, i, i2, i3);
    }

    public static void setsockoptIpMreqn(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        Libcore.f855os.setsockoptIpMreqn(fileDescriptor, i, i2, i3);
    }

    public static void setsockoptGroupReq(FileDescriptor fileDescriptor, int i, int i2, StructGroupReq structGroupReq) throws ErrnoException {
        Libcore.f855os.setsockoptGroupReq(fileDescriptor, i, i2, structGroupReq);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setsockoptLinger(FileDescriptor fileDescriptor, int i, int i2, StructLinger structLinger) throws ErrnoException {
        Libcore.f855os.setsockoptLinger(fileDescriptor, i, i2, structLinger);
    }

    public static void setsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2, StructTimeval structTimeval) throws ErrnoException {
        Libcore.f855os.setsockoptTimeval(fileDescriptor, i, i2, structTimeval);
    }

    @Deprecated
    public static void setuid(int i) throws ErrnoException {
        Libcore.f855os.setuid(i);
    }

    public static void setxattr(String str, String str2, byte[] bArr, int i) throws ErrnoException {
        Libcore.f855os.setxattr(str, str2, bArr, i);
    }

    public static void shutdown(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        Libcore.f855os.shutdown(fileDescriptor, i);
    }

    public static FileDescriptor socket(int i, int i2, int i3) throws ErrnoException {
        return Libcore.f855os.socket(i, i2, i3);
    }

    public static void socketpair(int i, int i2, int i3, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws ErrnoException {
        Libcore.f855os.socketpair(i, i2, i3, fileDescriptor, fileDescriptor2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static long splice(FileDescriptor fileDescriptor, Int64Ref int64Ref, FileDescriptor fileDescriptor2, Int64Ref int64Ref2, long j, int i) throws ErrnoException {
        return Libcore.f855os.splice(fileDescriptor, int64Ref, fileDescriptor2, int64Ref2, j, i);
    }

    public static StructStat stat(String str) throws ErrnoException {
        return Libcore.f855os.stat(str);
    }

    public static StructStatVfs statvfs(String str) throws ErrnoException {
        return Libcore.f855os.statvfs(str);
    }

    public static String strerror(int i) {
        return Libcore.f855os.strerror(i);
    }

    public static String strsignal(int i) {
        return Libcore.f855os.strsignal(i);
    }

    public static void symlink(String str, String str2) throws ErrnoException {
        Libcore.f855os.symlink(str, str2);
    }

    public static long sysconf(int i) {
        return Libcore.f855os.sysconf(i);
    }

    public static void tcdrain(FileDescriptor fileDescriptor) throws ErrnoException {
        Libcore.f855os.tcdrain(fileDescriptor);
    }

    public static void tcsendbreak(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        Libcore.f855os.tcsendbreak(fileDescriptor, i);
    }

    public static int umask(int i) {
        return Libcore.f855os.umask(i);
    }

    public static StructUtsname uname() {
        return Libcore.f855os.uname();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void unlink(String str) throws ErrnoException {
        Libcore.f855os.unlink(str);
    }

    public static void unsetenv(String str) throws ErrnoException {
        Libcore.f855os.unsetenv(str);
    }

    public static int waitpid(int i, Int32Ref int32Ref, int i2) throws ErrnoException {
        return Libcore.f855os.waitpid(i, int32Ref, i2);
    }

    public static int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.write(fileDescriptor, byteBuffer);
    }

    public static int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.write(fileDescriptor, bArr, i, i2);
    }

    public static int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return Libcore.f855os.writev(fileDescriptor, objArr, iArr, iArr2);
    }
}
