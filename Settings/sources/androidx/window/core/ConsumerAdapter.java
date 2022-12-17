package androidx.window.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlin.reflect.KClasses;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressLint({"BanUncheckedReflection"})
/* compiled from: ConsumerAdapter.kt */
public final class ConsumerAdapter {
    @NotNull
    private final ClassLoader loader;

    /* compiled from: ConsumerAdapter.kt */
    public interface Subscription {
        void dispose();
    }

    public ConsumerAdapter(@NotNull ClassLoader classLoader) {
        Intrinsics.checkNotNullParameter(classLoader, "loader");
        this.loader = classLoader;
    }

    @Nullable
    public final Class<?> consumerClassOrNull$window_release() {
        try {
            return unsafeConsumerClass();
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    private final Class<?> unsafeConsumerClass() {
        Class<?> loadClass = this.loader.loadClass("java.util.function.Consumer");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"java.util.function.Consumer\")");
        return loadClass;
    }

    private final <T> Object buildConsumer(KClass<T> kClass, Function1<? super T, Unit> function1) {
        ConsumerHandler consumerHandler = new ConsumerHandler(kClass, function1);
        Object newProxyInstance = Proxy.newProxyInstance(this.loader, new Class[]{unsafeConsumerClass()}, consumerHandler);
        Intrinsics.checkNotNullExpressionValue(newProxyInstance, "newProxyInstance(loader,…onsumerClass()), handler)");
        return newProxyInstance;
    }

    public final <T> void addConsumer(@NotNull Object obj, @NotNull KClass<T> kClass, @NotNull String str, @NotNull Function1<? super T, Unit> function1) {
        Intrinsics.checkNotNullParameter(obj, "obj");
        Intrinsics.checkNotNullParameter(kClass, "clazz");
        Intrinsics.checkNotNullParameter(str, "methodName");
        Intrinsics.checkNotNullParameter(function1, "consumer");
        obj.getClass().getMethod(str, new Class[]{unsafeConsumerClass()}).invoke(obj, new Object[]{buildConsumer(kClass, function1)});
    }

    @NotNull
    public final <T> Subscription createSubscription(@NotNull Object obj, @NotNull KClass<T> kClass, @NotNull String str, @NotNull String str2, @NotNull Activity activity, @NotNull Function1<? super T, Unit> function1) {
        Intrinsics.checkNotNullParameter(obj, "obj");
        Intrinsics.checkNotNullParameter(kClass, "clazz");
        Intrinsics.checkNotNullParameter(str, "addMethodName");
        Intrinsics.checkNotNullParameter(str2, "removeMethodName");
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(function1, "consumer");
        Object buildConsumer = buildConsumer(kClass, function1);
        obj.getClass().getMethod(str, new Class[]{Activity.class, unsafeConsumerClass()}).invoke(obj, new Object[]{activity, buildConsumer});
        return new ConsumerAdapter$createSubscription$1(obj.getClass().getMethod(str2, new Class[]{unsafeConsumerClass()}), obj, buildConsumer);
    }

    /* compiled from: ConsumerAdapter.kt */
    private static final class ConsumerHandler<T> implements InvocationHandler {
        @NotNull
        private final KClass<T> clazz;
        @NotNull
        private final Function1<T, Unit> consumer;

        public ConsumerHandler(@NotNull KClass<T> kClass, @NotNull Function1<? super T, Unit> function1) {
            Intrinsics.checkNotNullParameter(kClass, "clazz");
            Intrinsics.checkNotNullParameter(function1, "consumer");
            this.clazz = kClass;
            this.consumer = function1;
        }

        @NotNull
        public Object invoke(@NotNull Object obj, @NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(obj, "obj");
            Intrinsics.checkNotNullParameter(method, "method");
            Object obj2 = null;
            boolean z = false;
            if (isAccept(method, objArr)) {
                KClass<T> kClass = this.clazz;
                if (objArr != null) {
                    obj2 = objArr[0];
                }
                invokeAccept(KClasses.cast(kClass, obj2));
                return Unit.INSTANCE;
            } else if (isEquals(method, objArr)) {
                if (objArr != null) {
                    obj2 = objArr[0];
                }
                if (obj == obj2) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else if (isHashCode(method, objArr)) {
                return Integer.valueOf(this.consumer.hashCode());
            } else {
                if (isToString(method, objArr)) {
                    return this.consumer.toString();
                }
                throw new UnsupportedOperationException("Unexpected method call object:" + obj + ", method: " + method + ", args: " + objArr);
            }
        }

        public final void invokeAccept(@NotNull T t) {
            Intrinsics.checkNotNullParameter(t, "parameter");
            this.consumer.invoke(t);
        }

        private final boolean isEquals(Method method, Object[] objArr) {
            if (Intrinsics.areEqual(method.getName(), "equals") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        private final boolean isHashCode(Method method, Object[] objArr) {
            return Intrinsics.areEqual(method.getName(), "hashCode") && method.getReturnType().equals(Integer.TYPE) && objArr == null;
        }

        private final boolean isAccept(Method method, Object[] objArr) {
            if (Intrinsics.areEqual(method.getName(), "accept")) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        private final boolean isToString(Method method, Object[] objArr) {
            return Intrinsics.areEqual(method.getName(), "toString") && method.getReturnType().equals(String.class) && objArr == null;
        }
    }
}
