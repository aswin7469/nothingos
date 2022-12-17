package androidx.core.p002os;

import android.os.Trace;

@Deprecated
/* renamed from: androidx.core.os.TraceCompat */
public final class TraceCompat {
    public static void beginSection(String str) {
        Api18Impl.beginSection(str);
    }

    public static void endSection() {
        Api18Impl.endSection();
    }

    /* renamed from: androidx.core.os.TraceCompat$Api18Impl */
    static class Api18Impl {
        static void beginSection(String str) {
            Trace.beginSection(str);
        }

        static void endSection() {
            Trace.endSection();
        }
    }
}
