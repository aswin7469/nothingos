package com.android.systemui.media;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.TransitionLayout;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/media/MediaViewController$configurationListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaViewController.kt */
public final class MediaViewController$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ MediaViewController this$0;

    MediaViewController$configurationListener$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    public void onConfigChanged(Configuration configuration) {
        if (configuration != null) {
            MediaViewController mediaViewController = this.this$0;
            TransitionLayout access$getTransitionLayout$p = mediaViewController.transitionLayout;
            boolean z = false;
            if (access$getTransitionLayout$p != null && access$getTransitionLayout$p.getRawLayoutDirection() == configuration.getLayoutDirection()) {
                z = true;
            }
            if (!z) {
                TransitionLayout access$getTransitionLayout$p2 = mediaViewController.transitionLayout;
                if (access$getTransitionLayout$p2 != null) {
                    access$getTransitionLayout$p2.setLayoutDirection(configuration.getLayoutDirection());
                }
                mediaViewController.refreshState();
            }
        }
    }
}
