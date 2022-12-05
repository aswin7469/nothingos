package com.android.settings.applications;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
/* loaded from: classes.dex */
public class GameSettingsFeatureProviderImpl implements GameSettingsFeatureProvider {
    @Override // com.android.settings.applications.GameSettingsFeatureProvider
    public boolean isSupported(Context context) {
        return true;
    }

    @Override // com.android.settings.applications.GameSettingsFeatureProvider
    public void launchGameSettings(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.nothing.systemuitools", "com.nothing.gamemode.setting.GameModeSettingsActivity");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
