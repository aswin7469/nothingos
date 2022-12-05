package kotlinx.coroutines.android;

import android.os.Build;
import android.support.annotation.Keep;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import kotlinx.coroutines.CoroutineExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AndroidExceptionPreHandler.kt */
@Keep
/* loaded from: classes2.dex */
public final class AndroidExceptionPreHandler extends AbstractCoroutineContextElement implements CoroutineExceptionHandler, Function0<Method> {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(AndroidExceptionPreHandler.class), "preHandler", "getPreHandler()Ljava/lang/reflect/Method;"))};
    private final Lazy preHandler$delegate = LazyKt.lazy(this);

    private final Method getPreHandler() {
        Lazy lazy = this.preHandler$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (Method) lazy.getValue();
    }

    public AndroidExceptionPreHandler() {
        super(CoroutineExceptionHandler.Key);
    }

    @Override // kotlin.jvm.functions.Function0
    @Nullable
    /* renamed from: invoke  reason: collision with other method in class */
    public Method mo1951invoke() {
        try {
            boolean z = false;
            Method it = Thread.class.getDeclaredMethod("getUncaughtExceptionPreHandler", new Class[0]);
            Intrinsics.checkExpressionValueIsNotNull(it, "it");
            if (Modifier.isPublic(it.getModifiers())) {
                if (Modifier.isStatic(it.getModifiers())) {
                    z = true;
                }
            }
            if (!z) {
                return null;
            }
            return it;
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // kotlinx.coroutines.CoroutineExceptionHandler
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Thread thread = Thread.currentThread();
        if (Build.VERSION.SDK_INT >= 28) {
            Intrinsics.checkExpressionValueIsNotNull(thread, "thread");
            thread.getUncaughtExceptionHandler().uncaughtException(thread, exception);
            return;
        }
        Method preHandler = getPreHandler();
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
        Object invoke = preHandler != null ? preHandler.invoke(null, new Object[0]) : null;
        if (invoke instanceof Thread.UncaughtExceptionHandler) {
            uncaughtExceptionHandler = invoke;
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler2 = uncaughtExceptionHandler;
        if (uncaughtExceptionHandler2 == null) {
            return;
        }
        uncaughtExceptionHandler2.uncaughtException(thread, exception);
    }
}
