package com.android.settings.accessibility;

import android.content.Context;
import android.view.View;
import com.android.settings.core.SubSettingLauncher;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AccessibilityDialogUtils$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ AccessibilityDialogUtils$$ExternalSyntheticLambda0(Context context) {
        this.f$0 = context;
    }

    public final void onClick(View view) {
        new SubSettingLauncher(this.f$0).setDestination(AccessibilityButtonFragment.class.getName()).setSourceMetricsCategory(1873).launch();
    }
}
