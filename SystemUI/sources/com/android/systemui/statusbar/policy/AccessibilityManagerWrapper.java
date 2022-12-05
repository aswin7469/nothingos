package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
/* loaded from: classes2.dex */
public class AccessibilityManagerWrapper implements CallbackController<AccessibilityManager.AccessibilityServicesStateChangeListener> {
    private final AccessibilityManager mAccessibilityManager;

    public AccessibilityManagerWrapper(Context context) {
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.addAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener, null);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.removeAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public int getRecommendedTimeoutMillis(int i, int i2) {
        return this.mAccessibilityManager.getRecommendedTimeoutMillis(i, i2);
    }
}
