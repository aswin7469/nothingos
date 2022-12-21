package com.google.android.setupcompat.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import java.util.Arrays;

public final class WizardManagerHelper {
    public static final String ACTION_NEXT = "com.android.wizard.NEXT";
    static final String EXTRA_ACTION_ID = "actionId";
    public static final String EXTRA_IS_DEFERRED_SETUP = "deferredSetup";
    public static final String EXTRA_IS_FIRST_RUN = "firstRun";
    public static final String EXTRA_IS_PORTAL_SETUP = "portalSetup";
    public static final String EXTRA_IS_PRE_DEFERRED_SETUP = "preDeferredSetup";
    public static final String EXTRA_IS_SETUP_FLOW = "isSetupFlow";
    public static final String EXTRA_IS_SUW_SUGGESTED_ACTION_FLOW = "isSuwSuggestedActionFlow";
    private static final String EXTRA_RESULT_CODE = "com.android.setupwizard.ResultCode";
    static final String EXTRA_SCRIPT_URI = "scriptUri";
    public static final String EXTRA_THEME = "theme";
    public static final String EXTRA_USE_IMMERSIVE_MODE = "useImmersiveMode";
    static final String EXTRA_WIZARD_BUNDLE = "wizardBundle";
    public static final String SETTINGS_GLOBAL_DEVICE_PROVISIONED = "device_provisioned";
    public static final String SETTINGS_SECURE_USER_SETUP_COMPLETE = "user_setup_complete";

    public static Intent getNextIntent(Intent intent, int i) {
        return getNextIntent(intent, i, (Intent) null);
    }

    public static Intent getNextIntent(Intent intent, int i, Intent intent2) {
        Intent intent3 = new Intent(ACTION_NEXT);
        copyWizardManagerExtras(intent, intent3);
        intent3.putExtra(EXTRA_RESULT_CODE, i);
        if (!(intent2 == null || intent2.getExtras() == null)) {
            intent3.putExtras(intent2.getExtras());
        }
        intent3.putExtra(EXTRA_THEME, intent.getStringExtra(EXTRA_THEME));
        return intent3;
    }

    public static void copyWizardManagerExtras(Intent intent, Intent intent2) {
        intent2.putExtra(EXTRA_WIZARD_BUNDLE, intent.getBundleExtra(EXTRA_WIZARD_BUNDLE));
        for (String str : Arrays.asList(EXTRA_IS_FIRST_RUN, EXTRA_IS_DEFERRED_SETUP, EXTRA_IS_PRE_DEFERRED_SETUP, EXTRA_IS_PORTAL_SETUP, EXTRA_IS_SETUP_FLOW, EXTRA_IS_SUW_SUGGESTED_ACTION_FLOW)) {
            intent2.putExtra(str, intent.getBooleanExtra(str, false));
        }
        for (String str2 : Arrays.asList(EXTRA_THEME, EXTRA_SCRIPT_URI, EXTRA_ACTION_ID)) {
            intent2.putExtra(str2, intent.getStringExtra(str2));
        }
    }

    @Deprecated
    public static boolean isSetupWizardIntent(Intent intent) {
        return intent.getBooleanExtra(EXTRA_IS_FIRST_RUN, false);
    }

    public static boolean isUserSetupComplete(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), SETTINGS_SECURE_USER_SETUP_COMPLETE, 0) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isDeviceProvisioned(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), SETTINGS_GLOBAL_DEVICE_PROVISIONED, 0) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isPortalSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra(EXTRA_IS_PORTAL_SETUP, false);
    }

    public static boolean isDeferredSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra(EXTRA_IS_DEFERRED_SETUP, false);
    }

    public static boolean isPreDeferredSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra(EXTRA_IS_PRE_DEFERRED_SETUP, false);
    }

    public static boolean isInitialSetupWizard(Intent intent) {
        return intent.getBooleanExtra(EXTRA_IS_FIRST_RUN, false);
    }

    public static boolean isAnySetupWizard(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.getBooleanExtra(EXTRA_IS_SETUP_FLOW, false);
    }

    private WizardManagerHelper() {
    }
}
