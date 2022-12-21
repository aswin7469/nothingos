package com.android.systemui.statusbar.phone;

import android.view.DisplayCutout;
import android.view.WindowInsets;
import java.util.function.Consumer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0003H\u0016J\b\u0010\u000b\u001a\u00020\tH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"com/android/systemui/statusbar/phone/NotificationsQSContainerController$delayedInsetSetter$1", "Ljava/lang/Runnable;", "Ljava/util/function/Consumer;", "Landroid/view/WindowInsets;", "canceller", "cutoutInsets", "", "stableInsets", "accept", "", "insets", "run", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$delayedInsetSetter$1 implements Runnable, Consumer<WindowInsets> {
    private Runnable canceller;
    private int cutoutInsets;
    private int stableInsets;
    final /* synthetic */ NotificationsQSContainerController this$0;

    NotificationsQSContainerController$delayedInsetSetter$1(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public void accept(WindowInsets windowInsets) {
        Intrinsics.checkNotNullParameter(windowInsets, "insets");
        this.stableInsets = windowInsets.getStableInsetBottom();
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        this.cutoutInsets = displayCutout != null ? displayCutout.getSafeInsetBottom() : 0;
        Runnable runnable = this.canceller;
        if (runnable != null) {
            runnable.run();
        }
        this.canceller = this.this$0.delayableExecutor.executeDelayed(this, 500);
    }

    public void run() {
        this.this$0.bottomStableInsets = this.stableInsets;
        this.this$0.bottomCutoutInsets = this.cutoutInsets;
        this.this$0.updateBottomSpacing();
    }
}
