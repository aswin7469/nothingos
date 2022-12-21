package sun.nio.p035fs;

import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.p026io.IOException;

/* renamed from: sun.nio.fs.UnixException */
class UnixException extends Exception {
    static final long serialVersionUID = 7227016794320723218L;
    private int errno;
    private String msg;

    UnixException(int i) {
        this.errno = i;
        this.msg = null;
    }

    UnixException(String str) {
        this.errno = 0;
        this.msg = str;
    }

    /* access modifiers changed from: package-private */
    public int errno() {
        return this.errno;
    }

    /* access modifiers changed from: package-private */
    public void setError(int i) {
        this.errno = i;
        this.msg = null;
    }

    /* access modifiers changed from: package-private */
    public String errorString() {
        String str = this.msg;
        if (str != null) {
            return str;
        }
        return Util.toString(UnixNativeDispatcher.strerror(errno()));
    }

    public String getMessage() {
        return errorString();
    }

    private IOException translateToIOException(String str, String str2) {
        String str3 = this.msg;
        if (str3 != null) {
            return new IOException(str3);
        }
        if (errno() == UnixConstants.EACCES) {
            return new AccessDeniedException(str, str2, (String) null);
        }
        if (errno() == UnixConstants.ENOENT) {
            return new NoSuchFileException(str, str2, (String) null);
        }
        if (errno() == UnixConstants.EEXIST) {
            return new FileAlreadyExistsException(str, str2, (String) null);
        }
        return new FileSystemException(str, str2, errorString());
    }

    /* access modifiers changed from: package-private */
    public void rethrowAsIOException(String str) throws IOException {
        throw translateToIOException(str, (String) null);
    }

    /* access modifiers changed from: package-private */
    public void rethrowAsIOException(UnixPath unixPath, UnixPath unixPath2) throws IOException {
        String str = null;
        String pathForExceptionMessage = unixPath == null ? null : unixPath.getPathForExceptionMessage();
        if (unixPath2 != null) {
            str = unixPath2.getPathForExceptionMessage();
        }
        throw translateToIOException(pathForExceptionMessage, str);
    }

    /* access modifiers changed from: package-private */
    public void rethrowAsIOException(UnixPath unixPath) throws IOException {
        rethrowAsIOException(unixPath, (UnixPath) null);
    }

    /* access modifiers changed from: package-private */
    public IOException asIOException(UnixPath unixPath) {
        return translateToIOException(unixPath.getPathForExceptionMessage(), (String) null);
    }
}
