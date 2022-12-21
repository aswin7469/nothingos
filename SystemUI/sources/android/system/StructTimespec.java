package android.system;

import libcore.util.Objects;

public final class StructTimespec implements Comparable<StructTimespec> {
    public final long tv_nsec;
    public final long tv_sec;

    public StructTimespec(long j, long j2) {
        this.tv_sec = j;
        this.tv_nsec = j2;
        if (j2 < 0 || j2 > 999999999) {
            throw new IllegalArgumentException("tv_nsec value " + j2 + " is not in [0, 999999999]");
        }
    }

    public int compareTo(StructTimespec structTimespec) {
        long j = this.tv_sec;
        long j2 = structTimespec.tv_sec;
        if (j > j2) {
            return 1;
        }
        if (j < j2) {
            return -1;
        }
        long j3 = this.tv_nsec;
        long j4 = structTimespec.tv_nsec;
        if (j3 > j4) {
            return 1;
        }
        if (j3 < j4) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StructTimespec structTimespec = (StructTimespec) obj;
        if (this.tv_sec != structTimespec.tv_sec) {
            return false;
        }
        if (this.tv_nsec == structTimespec.tv_nsec) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.tv_sec;
        long j2 = this.tv_nsec;
        return (((int) (j ^ (j >>> 32))) * 31) + ((int) ((j2 >>> 32) ^ j2));
    }

    public String toString() {
        return Objects.toString(this);
    }
}
