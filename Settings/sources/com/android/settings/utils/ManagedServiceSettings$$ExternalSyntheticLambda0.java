package com.android.settings.utils;

import com.android.settingslib.applications.ServiceListing;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ManagedServiceSettings$$ExternalSyntheticLambda0 implements ServiceListing.Callback {
    public final /* synthetic */ ManagedServiceSettings f$0;

    public /* synthetic */ ManagedServiceSettings$$ExternalSyntheticLambda0(ManagedServiceSettings managedServiceSettings) {
        this.f$0 = managedServiceSettings;
    }

    public final void onServicesReloaded(List list) {
        this.f$0.updateList(list);
    }
}
