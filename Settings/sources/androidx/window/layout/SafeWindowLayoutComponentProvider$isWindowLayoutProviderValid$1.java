package androidx.window.layout;

import java.lang.reflect.Method;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
final class SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ SafeWindowLayoutComponentProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.this$0 = safeWindowLayoutComponentProvider;
    }

    @NotNull
    public final Boolean invoke() {
        boolean z = false;
        Method declaredMethod = this.this$0.getWindowExtensionsProviderClass().getDeclaredMethod("getWindowExtensions", new Class[0]);
        Class access$getWindowExtensionsClass = this.this$0.getWindowExtensionsClass();
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.this$0;
        Intrinsics.checkNotNullExpressionValue(declaredMethod, "getWindowExtensionsMethod");
        if (safeWindowLayoutComponentProvider.doesReturn(declaredMethod, (Class<?>) access$getWindowExtensionsClass) && this.this$0.isPublic(declaredMethod)) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
