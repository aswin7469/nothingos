package com.android.settings.wifi.addappnetworks;

import android.text.TextUtils;
import com.android.settings.wifi.addappnetworks.AddAppNetworksFragment;
import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddAppNetworksFragment$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ AddAppNetworksFragment.UiConfigurationItem f$0;

    public /* synthetic */ AddAppNetworksFragment$$ExternalSyntheticLambda0(AddAppNetworksFragment.UiConfigurationItem uiConfigurationItem) {
        this.f$0 = uiConfigurationItem;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0.mWifiNetworkSuggestion.getSsid(), ((WifiEntry) obj).getSsid());
    }
}
