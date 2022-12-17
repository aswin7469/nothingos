package androidx.window.layout;

import android.app.Activity;
import java.lang.reflect.Method;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
final class SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ SafeWindowLayoutComponentProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.this$0 = safeWindowLayoutComponentProvider;
    }

    @NotNull
    public final Boolean invoke() {
        Class<?> consumerClassOrNull$window_release = this.this$0.consumerAdapter.consumerClassOrNull$window_release();
        if (consumerClassOrNull$window_release == null) {
            return Boolean.FALSE;
        }
        Class access$getWindowLayoutComponentClass = this.this$0.getWindowLayoutComponentClass();
        boolean z = false;
        Method method = access$getWindowLayoutComponentClass.getMethod("addWindowLayoutInfoListener", new Class[]{Activity.class, consumerClassOrNull$window_release});
        Method method2 = access$getWindowLayoutComponentClass.getMethod("removeWindowLayoutInfoListener", new Class[]{consumerClassOrNull$window_release});
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.this$0;
        Intrinsics.checkNotNullExpressionValue(method, "addListenerMethod");
        if (safeWindowLayoutComponentProvider.isPublic(method)) {
            SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider2 = this.this$0;
            Intrinsics.checkNotNullExpressionValue(method2, "removeListenerMethod");
            if (safeWindowLayoutComponentProvider2.isPublic(method2)) {
                z = true;
            }
        }
        return Boolean.valueOf(z);
    }
}
