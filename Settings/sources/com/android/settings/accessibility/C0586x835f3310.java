package com.android.settings.accessibility;

import android.content.DialogInterface;

/* renamed from: com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0586x835f3310 implements DialogInterface.OnClickListener {
    public final /* synthetic */ AccessibilityShortcutPreferenceFragment f$0;

    public /* synthetic */ C0586x835f3310(AccessibilityShortcutPreferenceFragment accessibilityShortcutPreferenceFragment) {
        this.f$0 = accessibilityShortcutPreferenceFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.callOnTutorialDialogButtonClicked(dialogInterface, i);
    }
}
