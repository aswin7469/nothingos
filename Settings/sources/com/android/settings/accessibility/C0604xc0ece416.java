package com.android.settings.accessibility;

import com.android.settings.accessibility.AccessibilityServiceWarning;

/* renamed from: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0604xc0ece416 implements AccessibilityServiceWarning.UninstallActionPerformer {
    public final /* synthetic */ ToggleAccessibilityServicePreferenceFragment f$0;

    public /* synthetic */ C0604xc0ece416(ToggleAccessibilityServicePreferenceFragment toggleAccessibilityServicePreferenceFragment) {
        this.f$0 = toggleAccessibilityServicePreferenceFragment;
    }

    public final void uninstallPackage() {
        this.f$0.onDialogButtonFromUninstallClicked();
    }
}
