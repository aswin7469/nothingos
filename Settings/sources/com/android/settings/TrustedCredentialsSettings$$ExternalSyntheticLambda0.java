package com.android.settings;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TrustedCredentialsSettings$$ExternalSyntheticLambda0 implements TabLayoutMediator.TabConfigurationStrategy {
    public final void onConfigureTab(TabLayout.Tab tab, int i) {
        tab.setText(TrustedCredentialsSettings.TABS.get(i).mLabel);
    }
}
