package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.util.FeatureFlagUtils;
/* loaded from: classes.dex */
public class InternetDialogUtil {
    public static boolean isProviderModelEnabled(Context context) {
        if (context == null) {
            return false;
        }
        return FeatureFlagUtils.isEnabled(context, "settings_provider_model");
    }
}
