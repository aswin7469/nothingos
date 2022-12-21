package com.android.systemui.doze;

import android.app.AlarmManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DozeUi$$ExternalSyntheticLambda2 implements AlarmManager.OnAlarmListener {
    public final /* synthetic */ DozeUi f$0;

    public /* synthetic */ DozeUi$$ExternalSyntheticLambda2(DozeUi dozeUi) {
        this.f$0 = dozeUi;
    }

    public final void onAlarm() {
        this.f$0.onTimeTick();
    }
}
