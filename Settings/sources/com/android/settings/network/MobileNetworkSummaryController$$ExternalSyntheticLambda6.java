package com.android.settings.network;

import androidx.preference.Preference;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSummaryController$$ExternalSyntheticLambda6 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ MobileNetworkSummaryController f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ MobileNetworkSummaryController$$ExternalSyntheticLambda6(MobileNetworkSummaryController mobileNetworkSummaryController, List list) {
        this.f$0 = mobileNetworkSummaryController;
        this.f$1 = list;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$update$4(this.f$1, preference);
    }
}
