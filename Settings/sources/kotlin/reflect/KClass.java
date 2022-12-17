package kotlin.reflect;

import org.jetbrains.annotations.Nullable;

/* compiled from: KClass.kt */
public interface KClass<T> extends KDeclarationContainer, KAnnotatedElement {
    @Nullable
    String getQualifiedName();

    @Nullable
    String getSimpleName();

    boolean isInstance(@Nullable Object obj);
}
