package com.android.systemui.statusbar.lockscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0018\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\r"}, mo65043d2 = {"com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$buildView$1", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$IntentStarter;", "startIntent", "", "view", "Landroid/view/View;", "intent", "Landroid/content/Intent;", "showOnLockscreen", "", "startPendingIntent", "pi", "Landroid/app/PendingIntent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$buildView$1 implements BcSmartspaceDataPlugin.IntentStarter {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    LockscreenSmartspaceController$buildView$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void startIntent(View view, Intent intent, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(intent, "intent");
        this.this$0.activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, z);
    }

    public void startPendingIntent(PendingIntent pendingIntent, boolean z) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pi");
        if (z) {
            pendingIntent.send();
        } else {
            this.this$0.activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
        }
    }
}
