package com.android.systemui.p012qs.tiles;

import android.app.AlarmManager;
import com.android.systemui.statusbar.policy.NextAlarmController;

/* renamed from: com.android.systemui.qs.tiles.AlarmTile$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AlarmTile$$ExternalSyntheticLambda0 implements NextAlarmController.NextAlarmChangeCallback {
    public final /* synthetic */ AlarmTile f$0;

    public /* synthetic */ AlarmTile$$ExternalSyntheticLambda0(AlarmTile alarmTile) {
        this.f$0 = alarmTile;
    }

    public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
        AlarmTile.m2972callback$lambda0(this.f$0, alarmClockInfo);
    }
}
