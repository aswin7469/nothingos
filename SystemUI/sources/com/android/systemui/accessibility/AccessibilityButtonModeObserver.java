package com.android.systemui.accessibility;

import android.content.Context;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Inject;

@SysUISingleton
public class AccessibilityButtonModeObserver extends SecureSettingsContentObserver<ModeChangedListener> {
    private static final int ACCESSIBILITY_BUTTON_MODE_DEFAULT = 0;
    private static final String TAG = "A11yButtonModeObserver";

    @Retention(RetentionPolicy.SOURCE)
    public @interface AccessibilityButtonMode {
    }

    public interface ModeChangedListener {
        void onAccessibilityButtonModeChanged(int i);
    }

    @Inject
    public AccessibilityButtonModeObserver(Context context) {
        super(context, "accessibility_button_mode");
    }

    /* access modifiers changed from: package-private */
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
            Log.e(TAG, "Invalid string for  " + e);
            return 0;
        }
    }
}
