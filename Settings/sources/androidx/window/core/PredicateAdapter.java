package androidx.window.core;

import android.annotation.SuppressLint;
import android.util.Pair;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.reflect.KClasses;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressLint({"BanUncheckedReflection"})
/* compiled from: PredicateAdapter.kt */
public final class PredicateAdapter {
    @NotNull
    private final ClassLoader loader;

    public PredicateAdapter(@NotNull ClassLoader classLoader) {
        Intrinsics.checkNotNullParameter(classLoader, "loader");
        this.loader = classLoader;
    }

    @Nullable
    public final Class<?> predicateClassOrNull$window_release() {
        try {
            return predicateClassOrThrow();
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    private final Class<?> predicateClassOrThrow() {
        Class<?> loadClass = this.loader.loadClass("java.util.function.Predicate");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"java.util.function.Predicate\")");
        return loadClass;
    }

    @NotNull
    public final <T> Object buildPredicate(@NotNull KClass<T> kClass, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(kClass, "clazz");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        PredicateStubHandler predicateStubHandler = new PredicateStubHandler(kClass, function1);
        Object newProxyInstance = Proxy.newProxyInstance(this.loader, new Class[]{predicateClassOrThrow()}, predicateStubHandler);
        Intrinsics.checkNotNullExpressionValue(newProxyInstance, "newProxyInstance(loader,…row()), predicateHandler)");
        return newProxyInstance;
    }

    @NotNull
    public final <T, U> Object buildPairPredicate(@NotNull KClass<T> kClass, @NotNull KClass<U> kClass2, @NotNull Function2<? super T, ? super U, Boolean> function2) {
        Intrinsics.checkNotNullParameter(kClass, "firstClazz");
        Intrinsics.checkNotNullParameter(kClass2, "secondClazz");
        Intrinsics.checkNotNullParameter(function2, "predicate");
        PairPredicateStubHandler pairPredicateStubHandler = new PairPredicateStubHandler(kClass, kClass2, function2);
        Object newProxyInstance = Proxy.newProxyInstance(this.loader, new Class[]{predicateClassOrThrow()}, pairPredicateStubHandler);
        Intrinsics.checkNotNullExpressionValue(newProxyInstance, "newProxyInstance(loader,…row()), predicateHandler)");
        return newProxyInstance;
    }

    /* compiled from: PredicateAdapter.kt */
    private static abstract class BaseHandler<T> implements InvocationHandler {
        @NotNull
        private final KClass<T> clazz;

        public abstract boolean invokeTest(@NotNull Object obj, @NotNull T t);

        public BaseHandler(@NotNull KClass<T> kClass) {
            Intrinsics.checkNotNullParameter(kClass, "clazz");
            this.clazz = kClass;
        }

        @NotNull
        public Object invoke(@NotNull Object obj, @NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(obj, "obj");
            Intrinsics.checkNotNullParameter(method, "method");
            Object obj2 = null;
            boolean z = false;
            if (isTest(method, objArr)) {
                KClass<T> kClass = this.clazz;
                if (objArr != null) {
                    obj2 = objArr[0];
                }
                return Boolean.valueOf(invokeTest(obj, KClasses.cast(kClass, obj2)));
            } else if (isEquals(method, objArr)) {
                if (objArr != null) {
                    obj2 = objArr[0];
                }
                Intrinsics.checkNotNull(obj2);
                if (obj == obj2) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else if (isHashCode(method, objArr)) {
                return Integer.valueOf(hashCode());
            } else {
                if (isToString(method, objArr)) {
                    return toString();
                }
                throw new UnsupportedOperationException("Unexpected method call object:" + obj + ", method: " + method + ", args: " + objArr);
            }
        }

        /* access modifiers changed from: protected */
        public final boolean isEquals(@NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(method, "<this>");
            if (Intrinsics.areEqual(method.getName(), "equals") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public final boolean isHashCode(@NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(method, "<this>");
            return Intrinsics.areEqual(method.getName(), "hashCode") && method.getReturnType().equals(Integer.TYPE) && objArr == null;
        }

        /* access modifiers changed from: protected */
        public final boolean isTest(@NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(method, "<this>");
            if (Intrinsics.areEqual(method.getName(), "test") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public final boolean isToString(@NotNull Method method, @Nullable Object[] objArr) {
            Intrinsics.checkNotNullParameter(method, "<this>");
            return Intrinsics.areEqual(method.getName(), "toString") && method.getReturnType().equals(String.class) && objArr == null;
        }
    }

    /* compiled from: PredicateAdapter.kt */
    private static final class PredicateStubHandler<T> extends BaseHandler<T> {
        @NotNull
        private final Function1<T, Boolean> predicate;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public PredicateStubHandler(@NotNull KClass<T> kClass, @NotNull Function1<? super T, Boolean> function1) {
            super(kClass);
            Intrinsics.checkNotNullParameter(kClass, "clazzT");
            Intrinsics.checkNotNullParameter(function1, "predicate");
            this.predicate = function1;
        }

        public boolean invokeTest(@NotNull Object obj, @NotNull T t) {
            Intrinsics.checkNotNullParameter(obj, "obj");
            Intrinsics.checkNotNullParameter(t, "parameter");
            return this.predicate.invoke(t).booleanValue();
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        @NotNull
        public String toString() {
            return this.predicate.toString();
        }
    }

    /* compiled from: PredicateAdapter.kt */
    private static final class PairPredicateStubHandler<T, U> extends BaseHandler<Pair<?, ?>> {
        @NotNull
        private final KClass<T> clazzT;
        @NotNull
        private final KClass<U> clazzU;
        @NotNull
        private final Function2<T, U, Boolean> predicate;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public PairPredicateStubHandler(@NotNull KClass<T> kClass, @NotNull KClass<U> kClass2, @NotNull Function2<? super T, ? super U, Boolean> function2) {
            super(Reflection.getOrCreateKotlinClass(Pair.class));
            Intrinsics.checkNotNullParameter(kClass, "clazzT");
            Intrinsics.checkNotNullParameter(kClass2, "clazzU");
            Intrinsics.checkNotNullParameter(function2, "predicate");
            this.clazzT = kClass;
            this.clazzU = kClass2;
            this.predicate = function2;
        }

        public boolean invokeTest(@NotNull Object obj, @NotNull Pair<?, ?> pair) {
            Intrinsics.checkNotNullParameter(obj, "obj");
            Intrinsics.checkNotNullParameter(pair, "parameter");
            return this.predicate.invoke(KClasses.cast(this.clazzT, pair.first), KClasses.cast(this.clazzU, pair.second)).booleanValue();
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        @NotNull
        public String toString() {
            return this.predicate.toString();
        }
    }
}
