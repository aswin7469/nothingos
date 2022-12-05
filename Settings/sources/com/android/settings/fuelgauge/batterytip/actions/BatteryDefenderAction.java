package com.android.settings.fuelgauge.batterytip.actions;

import android.content.Intent;
import android.os.AsyncTask;
import com.android.settings.SettingsActivity;
import com.android.settings.overlay.FeatureFactory;
/* loaded from: classes.dex */
public class BatteryDefenderAction extends BatteryTipAction {
    private SettingsActivity mSettingsActivity;

    public BatteryDefenderAction(SettingsActivity settingsActivity) {
        super(settingsActivity.getApplicationContext());
        this.mSettingsActivity = settingsActivity;
    }

    @Override // com.android.settings.fuelgauge.batterytip.actions.BatteryTipAction
    public void handlePositiveAction(int i) {
        final Intent resumeChargeIntent = FeatureFactory.getFactory(this.mContext).getPowerUsageFeatureProvider(this.mContext).getResumeChargeIntent();
        if (resumeChargeIntent != null) {
            AsyncTask.execute(new Runnable() { // from class: com.android.settings.fuelgauge.batterytip.actions.BatteryDefenderAction$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BatteryDefenderAction.this.lambda$handlePositiveAction$0(resumeChargeIntent);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePositiveAction$0(Intent intent) {
        this.mContext.sendBroadcast(intent);
    }
}
