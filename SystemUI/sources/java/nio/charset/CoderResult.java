package java.nio.charset;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.Map;

public class CoderResult {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int CR_ERROR_MIN = 2;
    private static final int CR_MALFORMED = 2;
    private static final int CR_OVERFLOW = 1;
    private static final int CR_UNDERFLOW = 0;
    private static final int CR_UNMAPPABLE = 3;
    public static final CoderResult OVERFLOW = new CoderResult(1, 0);
    public static final CoderResult UNDERFLOW = new CoderResult(0, 0);
    private static Cache malformedCache = new Cache() {
        public CoderResult create(int i) {
            return new CoderResult(2, i);
        }
    };
    private static final String[] names = {"UNDERFLOW", "OVERFLOW", "MALFORMED", "UNMAPPABLE"};
    private static Cache unmappableCache = new Cache() {
        public CoderResult create(int i) {
            return new CoderResult(3, i);
        }
    };
    private final int length;
    private final int type;

    private CoderResult(int i, int i2) {
        this.type = i;
        this.length = i2;
    }

    public String toString() {
        String str = names[this.type];
        if (!isError()) {
            return str;
        }
        return str + NavigationBarInflaterView.SIZE_MOD_START + this.length + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public boolean isUnderflow() {
        return this.type == 0;
    }

    public boolean isOverflow() {
        return this.type == 1;
    }

    public boolean isError() {
        return this.type >= 2;
    }

    public boolean isMalformed() {
        return this.type == 2;
    }

    public boolean isUnmappable() {
        return this.type == 3;
    }

    public int length() {
        if (isError()) {
            return this.length;
        }
        throw new UnsupportedOperationException();
    }

    private static abstract class Cache {
        private Map<Integer, WeakReference<CoderResult>> cache;

        /* access modifiers changed from: protected */
        public abstract CoderResult create(int i);

        private Cache() {
            this.cache = null;
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x0026  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized java.nio.charset.CoderResult get(int r4) {
            /*
                r3 = this;
                monitor-enter(r3)
                if (r4 <= 0) goto L_0x0038
                java.lang.Integer r0 = new java.lang.Integer     // Catch:{ all -> 0x0036 }
                r0.<init>((int) r4)     // Catch:{ all -> 0x0036 }
                java.util.Map<java.lang.Integer, java.lang.ref.WeakReference<java.nio.charset.CoderResult>> r1 = r3.cache     // Catch:{ all -> 0x0036 }
                if (r1 != 0) goto L_0x0014
                java.util.HashMap r1 = new java.util.HashMap     // Catch:{ all -> 0x0036 }
                r1.<init>()     // Catch:{ all -> 0x0036 }
                r3.cache = r1     // Catch:{ all -> 0x0036 }
                goto L_0x0023
            L_0x0014:
                java.lang.Object r1 = r1.get(r0)     // Catch:{ all -> 0x0036 }
                java.lang.ref.WeakReference r1 = (java.lang.ref.WeakReference) r1     // Catch:{ all -> 0x0036 }
                if (r1 == 0) goto L_0x0023
                java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x0036 }
                java.nio.charset.CoderResult r1 = (java.nio.charset.CoderResult) r1     // Catch:{ all -> 0x0036 }
                goto L_0x0024
            L_0x0023:
                r1 = 0
            L_0x0024:
                if (r1 != 0) goto L_0x0034
                java.nio.charset.CoderResult r1 = r3.create(r4)     // Catch:{ all -> 0x0036 }
                java.util.Map<java.lang.Integer, java.lang.ref.WeakReference<java.nio.charset.CoderResult>> r4 = r3.cache     // Catch:{ all -> 0x0036 }
                java.lang.ref.WeakReference r2 = new java.lang.ref.WeakReference     // Catch:{ all -> 0x0036 }
                r2.<init>(r1)     // Catch:{ all -> 0x0036 }
                r4.put(r0, r2)     // Catch:{ all -> 0x0036 }
            L_0x0034:
                monitor-exit(r3)
                return r1
            L_0x0036:
                r4 = move-exception
                goto L_0x0040
            L_0x0038:
                java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0036 }
                java.lang.String r0 = "Non-positive length"
                r4.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0036 }
                throw r4     // Catch:{ all -> 0x0036 }
            L_0x0040:
                monitor-exit(r3)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.charset.CoderResult.Cache.get(int):java.nio.charset.CoderResult");
        }
    }

    public static CoderResult malformedForLength(int i) {
        return malformedCache.get(i);
    }

    public static CoderResult unmappableForLength(int i) {
        return unmappableCache.get(i);
    }

    public void throwException() throws CharacterCodingException {
        int i = this.type;
        if (i == 0) {
            throw new BufferUnderflowException();
        } else if (i == 1) {
            throw new BufferOverflowException();
        } else if (i == 2) {
            throw new MalformedInputException(this.length);
        } else if (i == 3) {
            throw new UnmappableCharacterException(this.length);
        }
    }
}
