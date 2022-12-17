package com.google.android.setupcompat.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import java.util.Arrays;

public final class WizardManagerHelper {
    public static final String ACTION_NEXT = "com.android.wizard.NEXT";
    static final String EXTRA_ACTION_ID = "actionId";
    static final String EXTRA_SCRIPT_URI = "scriptUri";
    static final String EXTRA_WIZARD_BUNDLE = "wizardBundle";

    public static Intent getNextIntent(Intent intent, int i) {
        return getNextIntent(intent, i, (Intent) null);
    }

    public static Intent getNextIntent(Intent intent, int i, Intent intent2) {
        Intent intent3 = new Intent(ACTION_NEXT);
        copyWizardManagerExtras(intent, intent3);
        intent3.putExtra("com.android.setupwizard.ResultCode", i);
        if (!(intent2 == null || intent2.getExtras() == null)) {
            intent3.putExtras(intent2.getExtras());
        }
        intent3.putExtra("theme", intent.getStringExtra("theme"));
        return intent3;
    }

    public static void copyWizardManagerExtras(Intent intent, Intent intent2) {
        intent2.putExtra(EXTRA_WIZARD_BUNDLE, intent.getBundleExtra(EXTRA_WIZARD_BUNDLE));
        for (String str : Arrays.asList(new String[]{"firstRun", "deferredSetup", "preDeferredSetup", "portalSetup", "isSetupFlow", "isSuwSuggestedActionFlow"})) {
            intent2.putExtra(str, intent.getBooleanExtra(str, false));
        }
        for (String str2 : Arrays.asList(new String[]{"theme", EXTRA_SCRIPT_URI, EXTRA_ACTION_ID})) {
            intent2.putExtra(str2, intent.getStringExtra(str2));
        }
    }

    @Deprecated
    public static boolean isSetupWizardIntent(Intent intent) {
        return intent.getBooleanExtra("firstRun", false);
    }

    public static boolean isUserSetupComplete(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "user_setup_complete", 0) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isDeviceProvisioned(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isPreDeferredSetupWizard(Intent intent) {
        return intent != null && intent.getBooleanExtra("preDeferredSetup", false);
    }

    public static boolean isAnySetupWizard(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.getBooleanExtra("isSetupFlow", false);
    }
}
