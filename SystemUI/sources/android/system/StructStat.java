package android.system;

import libcore.util.Objects;

public final class StructStat {
    public final StructTimespec st_atim;
    public final long st_atime;
    public final long st_blksize;
    public final long st_blocks;
    public final StructTimespec st_ctim;
    public final long st_ctime;
    public final long st_dev;
    public final int st_gid;
    public final long st_ino;
    public final int st_mode;
    public final StructTimespec st_mtim;
    public final long st_mtime;
    public final long st_nlink;
    public final long st_rdev;
    public final long st_size;
    public final int st_uid;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public StructStat(long r22, long r24, int r26, long r27, int r29, int r30, long r31, long r33, long r35, long r37, long r39, long r41, long r43) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r3 = r24
            r5 = r26
            r6 = r27
            r8 = r29
            r9 = r30
            r10 = r31
            r12 = r33
            r17 = r41
            r19 = r43
            android.system.StructTimespec r15 = new android.system.StructTimespec
            r14 = r15
            r0 = 0
            r2 = r35
            r15.<init>(r2, r0)
            android.system.StructTimespec r2 = new android.system.StructTimespec
            r15 = r2
            r3 = r37
            r2.<init>(r3, r0)
            android.system.StructTimespec r2 = new android.system.StructTimespec
            r16 = r2
            r3 = r39
            r2.<init>(r3, r0)
            r0 = r21
            r1 = r22
            r3 = r24
            r0.<init>((long) r1, (long) r3, (int) r5, (long) r6, (int) r8, (int) r9, (long) r10, (long) r12, (android.system.StructTimespec) r14, (android.system.StructTimespec) r15, (android.system.StructTimespec) r16, (long) r17, (long) r19)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.system.StructStat.<init>(long, long, int, long, int, int, long, long, long, long, long, long, long):void");
    }

    public StructStat(long j, long j2, int i, long j3, int i2, int i3, long j4, long j5, StructTimespec structTimespec, StructTimespec structTimespec2, StructTimespec structTimespec3, long j6, long j7) {
        StructTimespec structTimespec4 = structTimespec;
        StructTimespec structTimespec5 = structTimespec2;
        StructTimespec structTimespec6 = structTimespec3;
        this.st_dev = j;
        this.st_ino = j2;
        this.st_mode = i;
        this.st_nlink = j3;
        this.st_uid = i2;
        this.st_gid = i3;
        this.st_rdev = j4;
        this.st_size = j5;
        this.st_atime = structTimespec4.tv_sec;
        this.st_mtime = structTimespec5.tv_sec;
        this.st_ctime = structTimespec6.tv_sec;
        this.st_atim = structTimespec4;
        this.st_mtim = structTimespec5;
        this.st_ctim = structTimespec6;
        this.st_blksize = j6;
        this.st_blocks = j7;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
