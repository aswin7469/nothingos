package kotlin.reflect;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
/* compiled from: KProperty.kt */
/* loaded from: classes2.dex */
public interface KProperty1<T, V> extends KProperty<V>, Function1<T, V> {

    /* compiled from: KProperty.kt */
    /* loaded from: classes2.dex */
    public interface Getter<T, V> extends Function1<T, V>, Function1 {
    }

    V get(T t);

    @NotNull
    Getter<T, V> getGetter();
}
