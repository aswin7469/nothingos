package libcore.p030io;

import android.annotation.SystemApi;
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
import java.p026io.FileDescriptor;
import java.p026io.InterruptedIOException;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: libcore.io.ForwardingOs */
public class ForwardingOs implements C4711Os {

    /* renamed from: os */
    private final C4711Os f853os;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    protected ForwardingOs(C4711Os os) {
        this.f853os = (C4711Os) Objects.requireNonNull(os);
    }

    /* access modifiers changed from: protected */
    public final C4711Os delegate() {
        return this.f853os;
    }

    public FileDescriptor accept(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        return this.f853os.accept(fileDescriptor, socketAddress);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean access(String str, int i) throws ErrnoException {
        return this.f853os.access(str, i);
    }

    public InetAddress[] android_getaddrinfo(String str, StructAddrinfo structAddrinfo, int i) throws GaiException {
        return this.f853os.android_getaddrinfo(str, structAddrinfo, i);
    }

    public void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException {
        this.f853os.bind(fileDescriptor, inetAddress, i);
    }

    public void bind(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        this.f853os.bind(fileDescriptor, socketAddress);
    }

    public StructCapUserData[] capget(StructCapUserHeader structCapUserHeader) throws ErrnoException {
        return this.f853os.capget(structCapUserHeader);
    }

    public void capset(StructCapUserHeader structCapUserHeader, StructCapUserData[] structCapUserDataArr) throws ErrnoException {
        this.f853os.capset(structCapUserHeader, structCapUserDataArr);
    }

    public void chmod(String str, int i) throws ErrnoException {
        this.f853os.chmod(str, i);
    }

    public void chown(String str, int i, int i2) throws ErrnoException {
        this.f853os.chown(str, i, i2);
    }

    public void close(FileDescriptor fileDescriptor) throws ErrnoException {
        this.f853os.close(fileDescriptor);
    }

    public void android_fdsan_exchange_owner_tag(FileDescriptor fileDescriptor, long j, long j2) {
        this.f853os.android_fdsan_exchange_owner_tag(fileDescriptor, j, j2);
    }

    public long android_fdsan_get_owner_tag(FileDescriptor fileDescriptor) {
        return this.f853os.android_fdsan_get_owner_tag(fileDescriptor);
    }

    public String android_fdsan_get_tag_type(long j) {
        return this.f853os.android_fdsan_get_tag_type(j);
    }

    public long android_fdsan_get_tag_value(long j) {
        return this.f853os.android_fdsan_get_tag_value(j);
    }

    public void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws ErrnoException, SocketException {
        this.f853os.connect(fileDescriptor, inetAddress, i);
    }

    public void connect(FileDescriptor fileDescriptor, SocketAddress socketAddress) throws ErrnoException, SocketException {
        this.f853os.connect(fileDescriptor, socketAddress);
    }

    public FileDescriptor dup(FileDescriptor fileDescriptor) throws ErrnoException {
        return this.f853os.dup(fileDescriptor);
    }

    public FileDescriptor dup2(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return this.f853os.dup2(fileDescriptor, i);
    }

    public String[] environ() {
        return this.f853os.environ();
    }

    public void execv(String str, String[] strArr) throws ErrnoException {
        this.f853os.execv(str, strArr);
    }

    public void execve(String str, String[] strArr, String[] strArr2) throws ErrnoException {
        this.f853os.execve(str, strArr, strArr2);
    }

    public void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        this.f853os.fchmod(fileDescriptor, i);
    }

    public void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        this.f853os.fchown(fileDescriptor, i, i2);
    }

    public int fcntlInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.fcntlInt(fileDescriptor, i, i2);
    }

    public int fcntlVoid(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return this.f853os.fcntlVoid(fileDescriptor, i);
    }

    public void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException {
        this.f853os.fdatasync(fileDescriptor);
    }

    public StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException {
        return this.f853os.fstat(fileDescriptor);
    }

    public StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException {
        return this.f853os.fstatvfs(fileDescriptor);
    }

    public void fsync(FileDescriptor fileDescriptor) throws ErrnoException {
        this.f853os.fsync(fileDescriptor);
    }

    public void ftruncate(FileDescriptor fileDescriptor, long j) throws ErrnoException {
        this.f853os.ftruncate(fileDescriptor, j);
    }

    public String gai_strerror(int i) {
        return this.f853os.gai_strerror(i);
    }

    public int getegid() {
        return this.f853os.getegid();
    }

    public int geteuid() {
        return this.f853os.geteuid();
    }

    public int getgid() {
        return this.f853os.getgid();
    }

    public String getenv(String str) {
        return this.f853os.getenv(str);
    }

    public String getnameinfo(InetAddress inetAddress, int i) throws GaiException {
        return this.f853os.getnameinfo(inetAddress, i);
    }

    public SocketAddress getpeername(FileDescriptor fileDescriptor) throws ErrnoException {
        return this.f853os.getpeername(fileDescriptor);
    }

    public int getpgid(int i) throws ErrnoException {
        return this.f853os.getpgid(i);
    }

    public int getpid() {
        return this.f853os.getpid();
    }

    public int getppid() {
        return this.f853os.getppid();
    }

    public StructPasswd getpwnam(String str) throws ErrnoException {
        return this.f853os.getpwnam(str);
    }

    public StructPasswd getpwuid(int i) throws ErrnoException {
        return this.f853os.getpwuid(i);
    }

    public StructRlimit getrlimit(int i) throws ErrnoException {
        return this.f853os.getrlimit(i);
    }

    public SocketAddress getsockname(FileDescriptor fileDescriptor) throws ErrnoException {
        return this.f853os.getsockname(fileDescriptor);
    }

    public int getsockoptByte(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptByte(fileDescriptor, i, i2);
    }

    public InetAddress getsockoptInAddr(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptInAddr(fileDescriptor, i, i2);
    }

    public int getsockoptInt(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptInt(fileDescriptor, i, i2);
    }

    public StructLinger getsockoptLinger(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptLinger(fileDescriptor, i, i2);
    }

    public StructTimeval getsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptTimeval(fileDescriptor, i, i2);
    }

    public StructUcred getsockoptUcred(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        return this.f853os.getsockoptUcred(fileDescriptor, i, i2);
    }

    public int gettid() {
        return this.f853os.gettid();
    }

    public int getuid() {
        return this.f853os.getuid();
    }

    public byte[] getxattr(String str, String str2) throws ErrnoException {
        return this.f853os.getxattr(str, str2);
    }

    public StructIfaddrs[] getifaddrs() throws ErrnoException {
        return this.f853os.getifaddrs();
    }

    public String if_indextoname(int i) {
        return this.f853os.if_indextoname(i);
    }

    public int if_nametoindex(String str) {
        return this.f853os.if_nametoindex(str);
    }

    public InetAddress inet_pton(int i, String str) {
        return this.f853os.inet_pton(i, str);
    }

    public int ioctlFlags(FileDescriptor fileDescriptor, String str) throws ErrnoException {
        return this.f853os.ioctlFlags(fileDescriptor, str);
    }

    public InetAddress ioctlInetAddress(FileDescriptor fileDescriptor, int i, String str) throws ErrnoException {
        return this.f853os.ioctlInetAddress(fileDescriptor, i, str);
    }

    public int ioctlInt(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        return this.f853os.ioctlInt(fileDescriptor, i);
    }

    public int ioctlMTU(FileDescriptor fileDescriptor, String str) throws ErrnoException {
        return this.f853os.ioctlMTU(fileDescriptor, str);
    }

    public boolean isatty(FileDescriptor fileDescriptor) {
        return this.f853os.isatty(fileDescriptor);
    }

    public void kill(int i, int i2) throws ErrnoException {
        this.f853os.kill(i, i2);
    }

    public void lchown(String str, int i, int i2) throws ErrnoException {
        this.f853os.lchown(str, i, i2);
    }

    public void link(String str, String str2) throws ErrnoException {
        this.f853os.link(str, str2);
    }

    public void listen(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        this.f853os.listen(fileDescriptor, i);
    }

    public String[] listxattr(String str) throws ErrnoException {
        return this.f853os.listxattr(str);
    }

    public long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException {
        return this.f853os.lseek(fileDescriptor, j, i);
    }

    public StructStat lstat(String str) throws ErrnoException {
        return this.f853os.lstat(str);
    }

    public FileDescriptor memfd_create(String str, int i) throws ErrnoException {
        return this.f853os.memfd_create(str, i);
    }

    public void mincore(long j, long j2, byte[] bArr) throws ErrnoException {
        this.f853os.mincore(j, j2, bArr);
    }

    public void mkdir(String str, int i) throws ErrnoException {
        this.f853os.mkdir(str, i);
    }

    public void mkfifo(String str, int i) throws ErrnoException {
        this.f853os.mkfifo(str, i);
    }

    public void mlock(long j, long j2) throws ErrnoException {
        this.f853os.mlock(j, j2);
    }

    public long mmap(long j, long j2, int i, int i2, FileDescriptor fileDescriptor, long j3) throws ErrnoException {
        return this.f853os.mmap(j, j2, i, i2, fileDescriptor, j3);
    }

    public void msync(long j, long j2, int i) throws ErrnoException {
        this.f853os.msync(j, j2, i);
    }

    public void munlock(long j, long j2) throws ErrnoException {
        this.f853os.munlock(j, j2);
    }

    public void munmap(long j, long j2) throws ErrnoException {
        this.f853os.munmap(j, j2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public FileDescriptor open(String str, int i, int i2) throws ErrnoException {
        return this.f853os.open(str, i, i2);
    }

    public FileDescriptor[] pipe2(int i) throws ErrnoException {
        return this.f853os.pipe2(i);
    }

    public int poll(StructPollfd[] structPollfdArr, int i) throws ErrnoException {
        return this.f853os.poll(structPollfdArr, i);
    }

    public void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException {
        this.f853os.posix_fallocate(fileDescriptor, j, j2);
    }

    public int prctl(int i, long j, long j2, long j3, long j4) throws ErrnoException {
        return this.f853os.prctl(i, j, j2, j3, j4);
    }

    public int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return this.f853os.pread(fileDescriptor, byteBuffer, j);
    }

    public int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return this.f853os.pread(fileDescriptor, bArr, i, i2, j);
    }

    public int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return this.f853os.pwrite(fileDescriptor, byteBuffer, j);
    }

    public int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return this.f853os.pwrite(fileDescriptor, bArr, i, i2, j);
    }

    public int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return this.f853os.read(fileDescriptor, byteBuffer);
    }

    public int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return this.f853os.read(fileDescriptor, bArr, i, i2);
    }

    public String readlink(String str) throws ErrnoException {
        return this.f853os.readlink(str);
    }

    public String realpath(String str) throws ErrnoException {
        return this.f853os.realpath(str);
    }

    public int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return this.f853os.readv(fileDescriptor, objArr, iArr, iArr2);
    }

    public int recvfrom(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return this.f853os.recvfrom(fileDescriptor, byteBuffer, i, inetSocketAddress);
    }

    public int recvfrom(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetSocketAddress inetSocketAddress) throws ErrnoException, SocketException {
        return this.f853os.recvfrom(fileDescriptor, bArr, i, i2, i3, inetSocketAddress);
    }

    public int recvmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        return this.f853os.recvmsg(fileDescriptor, structMsghdr, i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void remove(String str) throws ErrnoException {
        this.f853os.remove(str);
    }

    public void removexattr(String str, String str2) throws ErrnoException {
        this.f853os.removexattr(str, str2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void rename(String str, String str2) throws ErrnoException {
        this.f853os.rename(str, str2);
    }

    public long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, Int64Ref int64Ref, long j) throws ErrnoException {
        return this.f853os.sendfile(fileDescriptor, fileDescriptor2, int64Ref, j);
    }

    public int sendmsg(FileDescriptor fileDescriptor, StructMsghdr structMsghdr, int i) throws ErrnoException, SocketException {
        return this.f853os.sendmsg(fileDescriptor, structMsghdr, i);
    }

    public int sendto(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetAddress inetAddress, int i2) throws ErrnoException, SocketException {
        return this.f853os.sendto(fileDescriptor, byteBuffer, i, inetAddress, i2);
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetAddress inetAddress, int i4) throws ErrnoException, SocketException {
        return this.f853os.sendto(fileDescriptor, bArr, i, i2, i3, inetAddress, i4);
    }

    public int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, SocketAddress socketAddress) throws ErrnoException, SocketException {
        return this.f853os.sendto(fileDescriptor, bArr, i, i2, i3, socketAddress);
    }

    public void setegid(int i) throws ErrnoException {
        this.f853os.setegid(i);
    }

    public void setenv(String str, String str2, boolean z) throws ErrnoException {
        this.f853os.setenv(str, str2, z);
    }

    public void seteuid(int i) throws ErrnoException {
        this.f853os.seteuid(i);
    }

    public void setgid(int i) throws ErrnoException {
        this.f853os.setgid(i);
    }

    public void setpgid(int i, int i2) throws ErrnoException {
        this.f853os.setpgid(i, i2);
    }

    public void setregid(int i, int i2) throws ErrnoException {
        this.f853os.setregid(i, i2);
    }

    public void setreuid(int i, int i2) throws ErrnoException {
        this.f853os.setreuid(i, i2);
    }

    public int setsid() throws ErrnoException {
        return this.f853os.setsid();
    }

    public void setsockoptByte(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        this.f853os.setsockoptByte(fileDescriptor, i, i2, i3);
    }

    public void setsockoptIfreq(FileDescriptor fileDescriptor, int i, int i2, String str) throws ErrnoException {
        this.f853os.setsockoptIfreq(fileDescriptor, i, i2, str);
    }

    public void setsockoptInt(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        this.f853os.setsockoptInt(fileDescriptor, i, i2, i3);
    }

    public void setsockoptIpMreqn(FileDescriptor fileDescriptor, int i, int i2, int i3) throws ErrnoException {
        this.f853os.setsockoptIpMreqn(fileDescriptor, i, i2, i3);
    }

    public void setsockoptGroupReq(FileDescriptor fileDescriptor, int i, int i2, StructGroupReq structGroupReq) throws ErrnoException {
        this.f853os.setsockoptGroupReq(fileDescriptor, i, i2, structGroupReq);
    }

    public void setsockoptLinger(FileDescriptor fileDescriptor, int i, int i2, StructLinger structLinger) throws ErrnoException {
        this.f853os.setsockoptLinger(fileDescriptor, i, i2, structLinger);
    }

    public void setsockoptTimeval(FileDescriptor fileDescriptor, int i, int i2, StructTimeval structTimeval) throws ErrnoException {
        this.f853os.setsockoptTimeval(fileDescriptor, i, i2, structTimeval);
    }

    public void setuid(int i) throws ErrnoException {
        this.f853os.setuid(i);
    }

    public void setxattr(String str, String str2, byte[] bArr, int i) throws ErrnoException {
        this.f853os.setxattr(str, str2, bArr, i);
    }

    public void shutdown(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        this.f853os.shutdown(fileDescriptor, i);
    }

    public FileDescriptor socket(int i, int i2, int i3) throws ErrnoException {
        return this.f853os.socket(i, i2, i3);
    }

    public void socketpair(int i, int i2, int i3, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws ErrnoException {
        this.f853os.socketpair(i, i2, i3, fileDescriptor, fileDescriptor2);
    }

    public long splice(FileDescriptor fileDescriptor, Int64Ref int64Ref, FileDescriptor fileDescriptor2, Int64Ref int64Ref2, long j, int i) throws ErrnoException {
        return this.f853os.splice(fileDescriptor, int64Ref, fileDescriptor2, int64Ref2, j, i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public StructStat stat(String str) throws ErrnoException {
        return this.f853os.stat(str);
    }

    public StructStatVfs statvfs(String str) throws ErrnoException {
        return this.f853os.statvfs(str);
    }

    public String strerror(int i) {
        return this.f853os.strerror(i);
    }

    public String strsignal(int i) {
        return this.f853os.strsignal(i);
    }

    public void symlink(String str, String str2) throws ErrnoException {
        this.f853os.symlink(str, str2);
    }

    public long sysconf(int i) {
        return this.f853os.sysconf(i);
    }

    public void tcdrain(FileDescriptor fileDescriptor) throws ErrnoException {
        this.f853os.tcdrain(fileDescriptor);
    }

    public void tcsendbreak(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        this.f853os.tcsendbreak(fileDescriptor, i);
    }

    public int umask(int i) {
        return this.f853os.umask(i);
    }

    public StructUtsname uname() {
        return this.f853os.uname();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void unlink(String str) throws ErrnoException {
        this.f853os.unlink(str);
    }

    public void unsetenv(String str) throws ErrnoException {
        this.f853os.unsetenv(str);
    }

    public int waitpid(int i, Int32Ref int32Ref, int i2) throws ErrnoException {
        return this.f853os.waitpid(i, int32Ref, i2);
    }

    public int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return this.f853os.write(fileDescriptor, byteBuffer);
    }

    public int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return this.f853os.write(fileDescriptor, bArr, i, i2);
    }

    public int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return this.f853os.writev(fileDescriptor, objArr, iArr, iArr2);
    }

    public String toString() {
        return "ForwardingOs{os=" + this.f853os + "}";
    }
}
