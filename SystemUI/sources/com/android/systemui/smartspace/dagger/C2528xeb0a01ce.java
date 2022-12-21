package com.android.systemui.smartspace.dagger;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0018\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\r"}, mo64987d2 = {"com/android/systemui/smartspace/dagger/SmartspaceViewComponent$SmartspaceViewModule$providesSmartspaceView$1", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$IntentStarter;", "startIntent", "", "view", "Landroid/view/View;", "intent", "Landroid/content/Intent;", "showOnLockscreen", "", "startPendingIntent", "pi", "Landroid/app/PendingIntent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.smartspace.dagger.SmartspaceViewComponent$SmartspaceViewModule$providesSmartspaceView$1 */
/* compiled from: SmartspaceViewComponent.kt */
public final class C2528xeb0a01ce implements BcSmartspaceDataPlugin.IntentStarter {
    final /* synthetic */ ActivityStarter $activityStarter;

    C2528xeb0a01ce(ActivityStarter activityStarter) {
        this.$activityStarter = activityStarter;
    }

    public void startIntent(View view, Intent intent, boolean z) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(intent, "intent");
        this.$activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, z);
    }

    public void startPendingIntent(PendingIntent pendingIntent, boolean z) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pi");
        if (z) {
            pendingIntent.send();
        } else {
            this.$activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
        }
    }
}
