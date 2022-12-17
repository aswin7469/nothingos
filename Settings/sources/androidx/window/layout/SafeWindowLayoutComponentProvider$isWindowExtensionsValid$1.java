package androidx.window.layout;

import java.lang.reflect.Method;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
final class SafeWindowLayoutComponentProvider$isWindowExtensionsValid$1 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ SafeWindowLayoutComponentProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SafeWindowLayoutComponentProvider$isWindowExtensionsValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.this$0 = safeWindowLayoutComponentProvider;
    }

    @NotNull
    public final Boolean invoke() {
        boolean z = false;
        Method method = this.this$0.getWindowExtensionsClass().getMethod("getWindowLayoutComponent", new Class[0]);
        Class access$getWindowLayoutComponentClass = this.this$0.getWindowLayoutComponentClass();
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.this$0;
        Intrinsics.checkNotNullExpressionValue(method, "getWindowLayoutComponentMethod");
        if (safeWindowLayoutComponentProvider.isPublic(method) && this.this$0.doesReturn(method, (Class<?>) access$getWindowLayoutComponentClass)) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
