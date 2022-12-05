package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CopyableThrowable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes2.dex */
public final class ExceptionsConstuctorKt {
    private static final int throwableFields = fieldsCountOrDefault(Throwable.class, -1);
    private static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors = new WeakHashMap<>();

    @Nullable
    public static final <E extends Throwable> E tryCopyException(@NotNull E exception) {
        Object m1934constructorimpl;
        List<Constructor> sortedWith;
        ReentrantReadWriteLock.ReadLock readLock;
        int readHoldCount;
        ReentrantReadWriteLock.WriteLock writeLock;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Object obj = null;
        if (exception instanceof CopyableThrowable) {
            try {
                Result.Companion companion = Result.Companion;
                m1934constructorimpl = Result.m1934constructorimpl(((CopyableThrowable) exception).createCopy());
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                m1934constructorimpl = Result.m1934constructorimpl(ResultKt.createFailure(th));
            }
            if (!Result.m1938isFailureimpl(m1934constructorimpl)) {
                obj = m1934constructorimpl;
            }
            return (E) obj;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = cacheLock;
        ReentrantReadWriteLock.ReadLock readLock2 = reentrantReadWriteLock.readLock();
        readLock2.lock();
        try {
            Function1<Throwable, Throwable> function1 = exceptionCtors.get(exception.getClass());
            if (function1 != null) {
                return (E) function1.mo1949invoke(exception);
            }
            int i = 0;
            if (throwableFields != fieldsCountOrDefault(exception.getClass(), 0)) {
                readLock = reentrantReadWriteLock.readLock();
                readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
                for (int i2 = 0; i2 < readHoldCount; i2++) {
                    readLock.unlock();
                }
                writeLock = reentrantReadWriteLock.writeLock();
                writeLock.lock();
                try {
                    exceptionCtors.put(exception.getClass(), ExceptionsConstuctorKt$tryCopyException$4$1.INSTANCE);
                    Unit unit = Unit.INSTANCE;
                    return null;
                } finally {
                    while (i < readHoldCount) {
                        readLock.lock();
                        i++;
                    }
                    writeLock.unlock();
                }
            }
            Constructor<?>[] constructors = exception.getClass().getConstructors();
            Intrinsics.checkExpressionValueIsNotNull(constructors, "exception.javaClass.constructors");
            sortedWith = ArraysKt___ArraysKt.sortedWith(constructors, new Comparator<T>() { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    Constructor it = (Constructor) t2;
                    Intrinsics.checkExpressionValueIsNotNull(it, "it");
                    Integer valueOf = Integer.valueOf(it.getParameterTypes().length);
                    Constructor it2 = (Constructor) t;
                    Intrinsics.checkExpressionValueIsNotNull(it2, "it");
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(valueOf, Integer.valueOf(it2.getParameterTypes().length));
                    return compareValues;
                }
            });
            Function1<Throwable, Throwable> function12 = null;
            for (Constructor constructor : sortedWith) {
                Intrinsics.checkExpressionValueIsNotNull(constructor, "constructor");
                function12 = createConstructor(constructor);
                if (function12 != null) {
                    break;
                }
            }
            ReentrantReadWriteLock reentrantReadWriteLock2 = cacheLock;
            readLock = reentrantReadWriteLock2.readLock();
            readHoldCount = reentrantReadWriteLock2.getWriteHoldCount() == 0 ? reentrantReadWriteLock2.getReadHoldCount() : 0;
            for (int i3 = 0; i3 < readHoldCount; i3++) {
                readLock.unlock();
            }
            writeLock = reentrantReadWriteLock2.writeLock();
            writeLock.lock();
            try {
                exceptionCtors.put(exception.getClass(), function12 != null ? function12 : ExceptionsConstuctorKt$tryCopyException$5$1.INSTANCE);
                Unit unit2 = Unit.INSTANCE;
                while (i < readHoldCount) {
                    readLock.lock();
                    i++;
                }
                writeLock.unlock();
                if (function12 == null) {
                    return null;
                }
                return (E) function12.mo1949invoke(exception);
            } finally {
                while (i < readHoldCount) {
                    readLock.lock();
                    i++;
                }
                writeLock.unlock();
            }
        } finally {
            readLock2.unlock();
        }
    }

    private static final Function1<Throwable, Throwable> createConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int length = parameterTypes.length;
        if (length != 0) {
            if (length != 1) {
                if (length != 2 || !Intrinsics.areEqual(parameterTypes[0], String.class) || !Intrinsics.areEqual(parameterTypes[1], Throwable.class)) {
                    return null;
                }
                return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$1(constructor);
            }
            Class<?> cls = parameterTypes[0];
            if (Intrinsics.areEqual(cls, Throwable.class)) {
                return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$2(constructor);
            }
            if (!Intrinsics.areEqual(cls, String.class)) {
                return null;
            }
            return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3(constructor);
        }
        return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4(constructor);
    }

    private static final int fieldsCountOrDefault(@NotNull Class<?> cls, int i) {
        Integer m1934constructorimpl;
        JvmClassMappingKt.getKotlinClass(cls);
        try {
            Result.Companion companion = Result.Companion;
            m1934constructorimpl = Result.m1934constructorimpl(Integer.valueOf(fieldsCount$default(cls, 0, 1, null)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            m1934constructorimpl = Result.m1934constructorimpl(ResultKt.createFailure(th));
        }
        Integer valueOf = Integer.valueOf(i);
        if (Result.m1938isFailureimpl(m1934constructorimpl)) {
            m1934constructorimpl = valueOf;
        }
        return ((Number) m1934constructorimpl).intValue();
    }

    static /* synthetic */ int fieldsCount$default(Class cls, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return fieldsCount(cls, i);
    }

    private static final int fieldsCount(@NotNull Class<?> cls, int i) {
        do {
            Field[] declaredFields = cls.getDeclaredFields();
            Intrinsics.checkExpressionValueIsNotNull(declaredFields, "declaredFields");
            int i2 = 0;
            for (Field it : declaredFields) {
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                if (!Modifier.isStatic(it.getModifiers())) {
                    i2++;
                }
            }
            i += i2;
            cls = cls.getSuperclass();
        } while (cls != null);
        return i;
    }
}
