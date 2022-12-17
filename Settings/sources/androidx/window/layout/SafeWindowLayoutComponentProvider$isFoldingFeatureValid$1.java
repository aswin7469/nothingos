package androidx.window.layout;

import android.graphics.Rect;
import java.lang.reflect.Method;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
final class SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ SafeWindowLayoutComponentProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.this$0 = safeWindowLayoutComponentProvider;
    }

    @NotNull
    public final Boolean invoke() {
        Class access$getFoldingFeatureClass = this.this$0.getFoldingFeatureClass();
        boolean z = false;
        Method method = access$getFoldingFeatureClass.getMethod("getBounds", new Class[0]);
        Method method2 = access$getFoldingFeatureClass.getMethod("getType", new Class[0]);
        Method method3 = access$getFoldingFeatureClass.getMethod("getState", new Class[0]);
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.this$0;
        Intrinsics.checkNotNullExpressionValue(method, "getBoundsMethod");
        if (safeWindowLayoutComponentProvider.doesReturn(method, (KClass<?>) Reflection.getOrCreateKotlinClass(Rect.class)) && this.this$0.isPublic(method)) {
            SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider2 = this.this$0;
            Intrinsics.checkNotNullExpressionValue(method2, "getTypeMethod");
            Class cls = Integer.TYPE;
            if (safeWindowLayoutComponentProvider2.doesReturn(method2, (KClass<?>) Reflection.getOrCreateKotlinClass(cls)) && this.this$0.isPublic(method2)) {
                SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider3 = this.this$0;
                Intrinsics.checkNotNullExpressionValue(method3, "getStateMethod");
                if (safeWindowLayoutComponentProvider3.doesReturn(method3, (KClass<?>) Reflection.getOrCreateKotlinClass(cls)) && this.this$0.isPublic(method3)) {
                    z = true;
                }
            }
        }
        return Boolean.valueOf(z);
    }
}
