package com.android.systemui.tuner;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.systemui.tuner.TunerService;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenFragment$$ExternalSyntheticLambda2 implements TunerService.Tunable {
    public final /* synthetic */ LockscreenFragment f$0;
    public final /* synthetic */ SwitchPreference f$1;
    public final /* synthetic */ Preference f$2;

    public /* synthetic */ LockscreenFragment$$ExternalSyntheticLambda2(LockscreenFragment lockscreenFragment, SwitchPreference switchPreference, Preference preference) {
        this.f$0 = lockscreenFragment;
        this.f$1 = switchPreference;
        this.f$2 = preference;
    }

    public final void onTuningChanged(String str, String str2) {
        this.f$0.mo46405xd5944fa2(this.f$1, this.f$2, str, str2);
    }
}
