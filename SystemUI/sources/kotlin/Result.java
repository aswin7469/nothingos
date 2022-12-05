package kotlin;

import java.io.Serializable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Result.kt */
/* loaded from: classes2.dex */
public final class Result<T> implements Serializable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final Object value;

    @NotNull
    /* renamed from: constructor-impl  reason: not valid java name */
    public static Object m1934constructorimpl(@Nullable Object obj) {
        return obj;
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m1935equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof Result) && Intrinsics.areEqual(obj, ((Result) obj2).m1941unboximpl());
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m1937hashCodeimpl(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        return m1935equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m1937hashCodeimpl(this.value);
    }

    @NotNull
    public String toString() {
        return m1940toStringimpl(this.value);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ Object m1941unboximpl() {
        return this.value;
    }

    /* renamed from: isSuccess-impl  reason: not valid java name */
    public static final boolean m1939isSuccessimpl(Object obj) {
        return !(obj instanceof Failure);
    }

    /* renamed from: isFailure-impl  reason: not valid java name */
    public static final boolean m1938isFailureimpl(Object obj) {
        return obj instanceof Failure;
    }

    @Nullable
    /* renamed from: exceptionOrNull-impl  reason: not valid java name */
    public static final Throwable m1936exceptionOrNullimpl(Object obj) {
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    @NotNull
    /* renamed from: toString-impl  reason: not valid java name */
    public static String m1940toStringimpl(Object obj) {
        if (obj instanceof Failure) {
            return obj.toString();
        }
        return "Success(" + obj + ')';
    }

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class Failure implements Serializable {
        @NotNull
        public final Throwable exception;

        public Failure(@NotNull Throwable exception) {
            Intrinsics.checkNotNullParameter(exception, "exception");
            this.exception = exception;
        }

        public boolean equals(@Nullable Object obj) {
            return (obj instanceof Failure) && Intrinsics.areEqual(this.exception, ((Failure) obj).exception);
        }

        public int hashCode() {
            return this.exception.hashCode();
        }

        @NotNull
        public String toString() {
            return "Failure(" + this.exception + ')';
        }
    }
}
