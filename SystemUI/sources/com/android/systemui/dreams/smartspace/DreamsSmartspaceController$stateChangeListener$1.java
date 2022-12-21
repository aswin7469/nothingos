package com.android.systemui.dreams.smartspace;

import android.icu.text.DateFormat;
import android.view.View;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/dreams/smartspace/DreamsSmartspaceController$stateChangeListener$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "v", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DreamsSmartspaceController.kt */
public final class DreamsSmartspaceController$stateChangeListener$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ DreamsSmartspaceController this$0;

    DreamsSmartspaceController$stateChangeListener$1(DreamsSmartspaceController dreamsSmartspaceController) {
        this.this$0 = dreamsSmartspaceController;
    }

    public void onViewAttachedToWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = (BcSmartspaceDataPlugin.SmartspaceView) view;
        smartspaceView.setPrimaryTextColor(-1);
        this.this$0.smartspaceViews.add(smartspaceView);
        this.this$0.connectSession();
        smartspaceView.setDozeAmount(0.0f);
    }

    public void onViewDetachedFromWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        this.this$0.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
        if (this.this$0.smartspaceViews.isEmpty()) {
            this.this$0.disconnect();
        }
    }
}
