package com.android.settings.accessibility;

import android.view.View;
import android.widget.AdapterView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MagnificationModePreferenceController$$ExternalSyntheticLambda2 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ MagnificationModePreferenceController f$0;

    public /* synthetic */ MagnificationModePreferenceController$$ExternalSyntheticLambda2(MagnificationModePreferenceController magnificationModePreferenceController) {
        this.f$0 = magnificationModePreferenceController;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.f$0.onMagnificationModeSelected(adapterView, view, i, j);
    }
}
