package sun.nio.p035fs;

/* renamed from: sun.nio.fs.UnixFileKey */
class UnixFileKey {
    private final long st_dev;
    private final long st_ino;

    UnixFileKey(long j, long j2) {
        this.st_dev = j;
        this.st_ino = j2;
    }

    public int hashCode() {
        long j = this.st_dev;
        long j2 = this.st_ino;
        return ((int) (j ^ (j >>> 32))) + ((int) ((j2 >>> 32) ^ j2));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnixFileKey)) {
            return false;
        }
        UnixFileKey unixFileKey = (UnixFileKey) obj;
        if (this.st_dev == unixFileKey.st_dev && this.st_ino == unixFileKey.st_ino) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "(dev=" + Long.toHexString(this.st_dev) + ",ino=" + this.st_ino + ')';
    }
}
