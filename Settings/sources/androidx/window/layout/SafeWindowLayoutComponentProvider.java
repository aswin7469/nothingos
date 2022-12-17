package androidx.window.layout;

import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.WindowExtensionsProvider;
import androidx.window.extensions.layout.WindowLayoutComponent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
public final class SafeWindowLayoutComponentProvider {
    /* access modifiers changed from: private */
    @NotNull
    public final ConsumerAdapter consumerAdapter;
    @NotNull
    private final ClassLoader loader;

    public SafeWindowLayoutComponentProvider(@NotNull ClassLoader classLoader, @NotNull ConsumerAdapter consumerAdapter2) {
        Intrinsics.checkNotNullParameter(classLoader, "loader");
        Intrinsics.checkNotNullParameter(consumerAdapter2, "consumerAdapter");
        this.loader = classLoader;
        this.consumerAdapter = consumerAdapter2;
    }

    @Nullable
    public final WindowLayoutComponent getWindowLayoutComponent() {
        if (canUseWindowLayoutComponent()) {
            try {
                return WindowExtensionsProvider.getWindowExtensions().getWindowLayoutComponent();
            } catch (UnsupportedOperationException unused) {
                WindowLayoutComponent windowLayoutComponent = null;
                return null;
            }
        } else {
            WindowLayoutComponent windowLayoutComponent2 = null;
            return null;
        }
    }

    private final boolean canUseWindowLayoutComponent() {
        return isWindowLayoutProviderValid() && isWindowExtensionsValid() && isWindowLayoutComponentValid() && isFoldingFeatureValid();
    }

    private final boolean isWindowLayoutProviderValid() {
        return validate(new SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1(this));
    }

    private final boolean isWindowExtensionsValid() {
        return validate(new SafeWindowLayoutComponentProvider$isWindowExtensionsValid$1(this));
    }

    private final boolean isFoldingFeatureValid() {
        return validate(new SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1(this));
    }

    private final boolean isWindowLayoutComponentValid() {
        return validate(new SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1(this));
    }

    private final boolean validate(Function0<Boolean> function0) {
        try {
            return function0.invoke().booleanValue();
        } catch (ClassNotFoundException | NoSuchMethodException unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public final boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /* access modifiers changed from: private */
    public final boolean doesReturn(Method method, KClass<?> kClass) {
        return doesReturn(method, JvmClassMappingKt.getJavaClass(kClass));
    }

    /* access modifiers changed from: private */
    public final boolean doesReturn(Method method, Class<?> cls) {
        return method.getReturnType().equals(cls);
    }

    /* access modifiers changed from: private */
    public final Class<?> getWindowExtensionsProviderClass() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.WindowExtensionsProvider");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"androi…indowExtensionsProvider\")");
        return loadClass;
    }

    /* access modifiers changed from: private */
    public final Class<?> getWindowExtensionsClass() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.WindowExtensions");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"androi…nsions.WindowExtensions\")");
        return loadClass;
    }

    /* access modifiers changed from: private */
    public final Class<?> getFoldingFeatureClass() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.layout.FoldingFeature");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"androi…s.layout.FoldingFeature\")");
        return loadClass;
    }

    /* access modifiers changed from: private */
    public final Class<?> getWindowLayoutComponentClass() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.layout.WindowLayoutComponent");
        Intrinsics.checkNotNullExpressionValue(loadClass, "loader.loadClass(\"androi…t.WindowLayoutComponent\")");
        return loadClass;
    }
}
