package com.android.settings.applications.specialaccess.notificationaccess;

import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ApprovalPreferenceController$$ExternalSyntheticLambda1 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ ApprovalPreferenceController f$0;
    public final /* synthetic */ CharSequence f$1;

    public /* synthetic */ ApprovalPreferenceController$$ExternalSyntheticLambda1(ApprovalPreferenceController approvalPreferenceController, CharSequence charSequence) {
        this.f$0 = approvalPreferenceController;
        this.f$1 = charSequence;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$updateState$0(this.f$1, preference, obj);
    }
}
