package com.android.systemui.statusbar.policy;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;

@SysUISingleton
public class AccessibilityManagerWrapper implements CallbackController<AccessibilityManager.AccessibilityServicesStateChangeListener> {
    private final AccessibilityManager mAccessibilityManager;

    @Inject
    public AccessibilityManagerWrapper(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }

    public void addCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.addAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public void removeCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.removeAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public void addAccessibilityStateChangeListener(AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener) {
        this.mAccessibilityManager.addAccessibilityStateChangeListener(accessibilityStateChangeListener);
    }

    public void removeAccessibilityStateChangeListener(AccessibilityManager.AccessibilityStateChangeListener accessibilityStateChangeListener) {
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(accessibilityStateChangeListener);
    }

    public boolean isEnabled() {
        return this.mAccessibilityManager.isEnabled();
    }

    public void sendAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        this.mAccessibilityManager.sendAccessibilityEvent(accessibilityEvent);
    }

    public int getRecommendedTimeoutMillis(int i, int i2) {
        return this.mAccessibilityManager.getRecommendedTimeoutMillis(i, i2);
    }
}
