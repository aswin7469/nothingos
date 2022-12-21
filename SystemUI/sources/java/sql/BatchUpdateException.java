package java.sql;

import java.util.Arrays;

public class BatchUpdateException extends SQLException {
    private static final long serialVersionUID = 5977529877145521757L;
    private final int[] updateCounts;

    public BatchUpdateException(String str, String str2, int i, int[] iArr) {
        super(str, str2, i);
        int[] iArr2;
        if (iArr == null) {
            iArr2 = null;
        } else {
            iArr2 = Arrays.copyOf(iArr, iArr.length);
        }
        this.updateCounts = iArr2;
    }

    public BatchUpdateException(String str, String str2, int[] iArr) {
        this(str, str2, 0, iArr);
    }

    public BatchUpdateException(String str, int[] iArr) {
        this(str, (String) null, 0, iArr);
    }

    public BatchUpdateException(int[] iArr) {
        this((String) null, (String) null, 0, iArr);
    }

    public BatchUpdateException() {
        this((String) null, (String) null, 0, (int[]) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public BatchUpdateException(Throwable th) {
        this(th == null ? null : th.toString(), (String) null, 0, (int[]) null, th);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public BatchUpdateException(int[] iArr, Throwable th) {
        this(th == null ? null : th.toString(), (String) null, 0, iArr, th);
    }

    public BatchUpdateException(String str, int[] iArr, Throwable th) {
        this(str, (String) null, 0, iArr, th);
    }

    public BatchUpdateException(String str, String str2, int[] iArr, Throwable th) {
        this(str, str2, 0, iArr, th);
    }

    public BatchUpdateException(String str, String str2, int i, int[] iArr, Throwable th) {
        super(str, str2, i, th);
        int[] iArr2;
        if (iArr == null) {
            iArr2 = null;
        } else {
            iArr2 = Arrays.copyOf(iArr, iArr.length);
        }
        this.updateCounts = iArr2;
    }

    public int[] getUpdateCounts() {
        int[] iArr = this.updateCounts;
        if (iArr == null) {
            return null;
        }
        return Arrays.copyOf(iArr, iArr.length);
    }
}
