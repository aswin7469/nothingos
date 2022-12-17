package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.android.settingslib.RestrictedLockUtilsInternal;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RestrictedLockUtilsInternal$$ExternalSyntheticLambda0 implements RestrictedLockUtilsInternal.LockSettingCheck {
    public final boolean isEnforcing(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return RestrictedLockUtilsInternal.lambda$checkIfMaximumTimeToLockIsSet$2(devicePolicyManager, componentName, i);
    }
}
