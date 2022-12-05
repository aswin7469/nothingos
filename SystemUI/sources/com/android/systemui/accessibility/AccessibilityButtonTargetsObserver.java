package com.android.systemui.accessibility;

import android.content.Context;
/* loaded from: classes.dex */
public class AccessibilityButtonTargetsObserver extends SecureSettingsContentObserver<TargetsChangedListener> {

    /* loaded from: classes.dex */
    public interface TargetsChangedListener {
        void onAccessibilityButtonTargetsChanged(String str);
    }

    public AccessibilityButtonTargetsObserver(Context context) {
        super(context, "accessibility_button_targets");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.SecureSettingsContentObserver
    public void onValueChanged(TargetsChangedListener targetsChangedListener, String str) {
        targetsChangedListener.onAccessibilityButtonTargetsChanged(str);
    }

    public String getCurrentAccessibilityButtonTargets() {
        return getSettingsValue();
    }
}
