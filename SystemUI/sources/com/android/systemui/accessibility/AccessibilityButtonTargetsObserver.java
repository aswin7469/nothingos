package com.android.systemui.accessibility;

import android.content.Context;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;

@SysUISingleton
public class AccessibilityButtonTargetsObserver extends SecureSettingsContentObserver<TargetsChangedListener> {

    public interface TargetsChangedListener {
        void onAccessibilityButtonTargetsChanged(String str);
    }

    @Inject
    public AccessibilityButtonTargetsObserver(Context context) {
        super(context, "accessibility_button_targets");
    }

    /* access modifiers changed from: package-private */
    public void onValueChanged(TargetsChangedListener targetsChangedListener, String str) {
        targetsChangedListener.onAccessibilityButtonTargetsChanged(str);
    }

    public String getCurrentAccessibilityButtonTargets() {
        return getSettingsValue();
    }
}
