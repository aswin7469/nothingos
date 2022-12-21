package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/phone/PhoneStatusBarViewController$configurationListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ PhoneStatusBarViewController this$0;

    PhoneStatusBarViewController$configurationListener$1(PhoneStatusBarViewController phoneStatusBarViewController) {
        this.this$0 = phoneStatusBarViewController;
    }

    public void onConfigChanged(Configuration configuration) {
        ((PhoneStatusBarView) this.this$0.mView).updateResources();
    }
}
