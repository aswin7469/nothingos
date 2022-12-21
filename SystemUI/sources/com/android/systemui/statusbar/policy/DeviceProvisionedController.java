package com.android.systemui.statusbar.policy;

public interface DeviceProvisionedController extends CallbackController<DeviceProvisionedListener> {
    @Deprecated
    int getCurrentUser();

    boolean isCurrentUserSetup();

    boolean isDeviceProvisioned();

    boolean isUserSetup(int i);

    public interface DeviceProvisionedListener {
        void onDeviceProvisionedChanged() {
        }

        void onUserSetupChanged() {
        }

        void onUserSwitched() {
            onUserSetupChanged();
        }
    }
}
