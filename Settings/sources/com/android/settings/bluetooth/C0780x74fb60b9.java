package com.android.settings.bluetooth;

import android.content.DialogInterface;
import androidx.preference.PreferenceCategory;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda4 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0780x74fb60b9 implements DialogInterface.OnClickListener {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ PreferenceCategory f$2;

    public /* synthetic */ C0780x74fb60b9(String str, String str2, PreferenceCategory preferenceCategory) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = preferenceCategory;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        BluetoothDetailsCompanionAppsController.lambda$removeAssociationDialog$1(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
