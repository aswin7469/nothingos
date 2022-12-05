package com.android.systemui.accessibility;

import android.content.Context;
import android.util.Log;
/* loaded from: classes.dex */
public class AccessibilityButtonModeObserver extends SecureSettingsContentObserver<ModeChangedListener> {

    /* loaded from: classes.dex */
    public interface ModeChangedListener {
        void onAccessibilityButtonModeChanged(int i);
    }

    public AccessibilityButtonModeObserver(Context context) {
        super(context, "accessibility_button_mode");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.SecureSettingsContentObserver
    public void onValueChanged(ModeChangedListener modeChangedListener, String str) {
        modeChangedListener.onAccessibilityButtonModeChanged(parseAccessibilityButtonMode(str));
    }

    public int getCurrentAccessibilityButtonMode() {
        return parseAccessibilityButtonMode(getSettingsValue());
    }

    private int parseAccessibilityButtonMode(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.e("A11yButtonModeObserver", "Invalid string for  " + e);
            return 0;
        }
    }
}
