package com.android.settings.deviceinfo;

import android.util.SparseArray;
import com.android.settings.deviceinfo.StorageDashboardFragment;
import com.android.settings.deviceinfo.storage.UserIconLoader;

/* renamed from: com.android.settings.deviceinfo.StorageDashboardFragment$IconLoaderCallbacks$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0890x7ad960f8 implements UserIconLoader.FetchUserIconTask {
    public final /* synthetic */ StorageDashboardFragment.IconLoaderCallbacks f$0;

    public /* synthetic */ C0890x7ad960f8(StorageDashboardFragment.IconLoaderCallbacks iconLoaderCallbacks) {
        this.f$0 = iconLoaderCallbacks;
    }

    public final SparseArray getUserIcons() {
        return this.f$0.lambda$onCreateLoader$0();
    }
}
