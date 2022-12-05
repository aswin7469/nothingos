package com.android.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
/* loaded from: classes.dex */
public class SubSettings extends SettingsActivity {
    @Override // com.android.settings.core.SettingsBaseActivity, android.app.Activity
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity
    public boolean isValidFragment(String str) {
        Log.d("SubSettings", "Launching fragment " + str);
        return true;
    }

    @Override // com.android.settings.core.SettingsBaseActivity, android.app.Activity
    public void setTitle(CharSequence charSequence) {
        Bundle bundleExtra = getIntent().getBundleExtra(":settings:show_fragment_args");
        if (bundleExtra != null && bundleExtra.getBoolean("key_chosen_wifientry_need_saved", false)) {
            this.mCollapsingToolbarLayout.setTitle(charSequence);
            this.mCollapsingToolbarLayout.setExpandedTitleTypeface(Typeface.create("NDot57", 0));
            return;
        }
        super.setTitle(charSequence);
    }
}
