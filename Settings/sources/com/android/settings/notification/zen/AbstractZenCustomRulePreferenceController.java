package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.os.Bundle;
import com.android.settingslib.core.lifecycle.Lifecycle;

abstract class AbstractZenCustomRulePreferenceController extends AbstractZenModePreferenceController {
    String mId;
    AutomaticZenRule mRule;

    public void onResume() {
    }

    AbstractZenCustomRulePreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str, lifecycle);
    }

    public boolean isAvailable() {
        return this.mRule != null;
    }

    public void setIdAndRule(String str, AutomaticZenRule automaticZenRule) {
        this.mId = str;
        this.mRule = automaticZenRule;
    }

    /* access modifiers changed from: package-private */
    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("RULE_ID", this.mId);
        return bundle;
    }
}
