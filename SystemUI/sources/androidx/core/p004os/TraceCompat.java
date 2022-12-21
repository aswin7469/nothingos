package androidx.core.p004os;

import android.os.Trace;
import java.lang.reflect.Method;

@Deprecated
/* renamed from: androidx.core.os.TraceCompat */
public final class TraceCompat {
    private static final String TAG = "TraceCompat";
    private static Method sAsyncTraceBeginMethod;
    private static Method sAsyncTraceEndMethod;
    private static Method sIsTagEnabledMethod;
    private static Method sTraceCounterMethod;
    private static long sTraceTagApp;

    public static boolean isEnabled() {
        return Api29Impl.isEnabled();
    }

    public static void beginSection(String str) {
        Api18Impl.beginSection(str);
    }

    public static void endSection() {
        Api18Impl.endSection();
    }

    public static void beginAsyncSection(String str, int i) {
        Api29Impl.beginAsyncSection(str, i);
    }

    public static void endAsyncSection(String str, int i) {
        Api29Impl.endAsyncSection(str, i);
    }

    public static void setCounter(String str, int i) {
        Api29Impl.setCounter(str, (long) i);
    }

    private TraceCompat() {
    }

    /* renamed from: androidx.core.os.TraceCompat$Api29Impl */
    static class Api29Impl {
        private Api29Impl() {
        }

        static boolean isEnabled() {
            return Trace.isEnabled();
        }

        static void endAsyncSection(String str, int i) {
            Trace.endAsyncSection(str, i);
        }

        static void beginAsyncSection(String str, int i) {
            Trace.beginAsyncSection(str, i);
        }

        static void setCounter(String str, long j) {
            Trace.setCounter(str, j);
        }
    }

    /* renamed from: androidx.core.os.TraceCompat$Api18Impl */
    static class Api18Impl {
        private Api18Impl() {
        }

        static void beginSection(String str) {
            Trace.beginSection(str);
        }

        static void endSection() {
            Trace.endSection();
        }
    }
}