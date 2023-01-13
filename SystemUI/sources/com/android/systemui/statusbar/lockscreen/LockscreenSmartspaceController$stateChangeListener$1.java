package com.android.systemui.statusbar.lockscreen;

import android.icu.text.DateFormat;
import android.view.View;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$stateChangeListener$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "v", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$stateChangeListener$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    LockscreenSmartspaceController$stateChangeListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onViewAttachedToWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        this.this$0.smartspaceViews.add((BcSmartspaceDataPlugin.SmartspaceView) view);
        this.this$0.connectSession();
        this.this$0.updateTextColorFromWallpaper();
        this.this$0.statusBarStateListener.onDozeAmountChanged(0.0f, this.this$0.statusBarStateController.getDozeAmount());
    }

    public void onViewDetachedFromWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        this.this$0.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
        if (this.this$0.smartspaceViews.isEmpty()) {
            this.this$0.disconnect();
        }
    }
}
