package android.net;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class TetheringConstants {
    public static final String EXTRA_ADD_TETHER_TYPE = "extraAddTetherType";
    public static final String EXTRA_PROVISION_CALLBACK = "extraProvisionCallback";
    public static final String EXTRA_REM_TETHER_TYPE = "extraRemTetherType";
    public static final String EXTRA_RUN_PROVISION = "extraRunProvision";
    public static final String EXTRA_SET_ALARM = "extraSetAlarm";
    public static final String EXTRA_TETHER_PROVISIONING_RESPONSE = "android.net.extra.TETHER_PROVISIONING_RESPONSE";
    public static final String EXTRA_TETHER_SILENT_PROVISIONING_ACTION = "android.net.extra.TETHER_SILENT_PROVISIONING_ACTION";
    public static final String EXTRA_TETHER_SUBID = "android.net.extra.TETHER_SUBID";
    public static final String EXTRA_TETHER_UI_PROVISIONING_APP_NAME = "android.net.extra.TETHER_UI_PROVISIONING_APP_NAME";

    private TetheringConstants() {
    }
}
