package com.android.systemui.p012qs;

import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.UserHandle;
import android.safetycenter.SafetyCenterManager;
import android.view.View;
import com.android.systemui.broadcast.BroadcastDispatcher;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/qs/HeaderPrivacyIconsController$attachStateChangeListener$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "v", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.HeaderPrivacyIconsController$attachStateChangeListener$1 */
/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$attachStateChangeListener$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ HeaderPrivacyIconsController this$0;

    HeaderPrivacyIconsController$attachStateChangeListener$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onViewAttachedToWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        BroadcastDispatcher.registerReceiver$default(this.this$0.broadcastDispatcher, this.this$0.safetyCenterReceiver, new IntentFilter(SafetyCenterManager.ACTION_SAFETY_CENTER_ENABLED_CHANGED), this.this$0.backgroundExecutor, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    public void onViewDetachedFromWindow(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        this.this$0.broadcastDispatcher.unregisterReceiver(this.this$0.safetyCenterReceiver);
    }
}
