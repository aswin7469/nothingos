package com.android.settings.privacy;

import android.content.Intent;
import com.android.settings.dashboard.profileselector.UserAdapter;
import java.util.ArrayList;

/* renamed from: com.android.settings.privacy.EnableContentCaptureWithServiceSettingsPreferenceController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1315xa5b4eea8 implements UserAdapter.OnClickListener {
    public final /* synthetic */ EnableContentCaptureWithServiceSettingsPreferenceController f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C1315xa5b4eea8(EnableContentCaptureWithServiceSettingsPreferenceController enableContentCaptureWithServiceSettingsPreferenceController, Intent intent, ArrayList arrayList) {
        this.f$0 = enableContentCaptureWithServiceSettingsPreferenceController;
        this.f$1 = intent;
        this.f$2 = arrayList;
    }

    public final void onClick(int i) {
        this.f$0.lambda$show$0(this.f$1, this.f$2, i);
    }
}
