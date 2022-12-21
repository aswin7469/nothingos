package com.google.android.setupcompat.internal;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;

public final class LayoutBindBackHelper {
    private LayoutBindBackHelper() {
    }

    public static final String getScreenName(Activity activity) {
        return activity.getComponentName().toString();
    }

    public static final Bundle getExtraBundle(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putString(FocusChangedMetricHelper.Constants.ExtraKey.SCREEN_NAME, getScreenName(activity));
        bundle.putString("intentAction", activity.getIntent().getAction());
        return bundle;
    }
}
