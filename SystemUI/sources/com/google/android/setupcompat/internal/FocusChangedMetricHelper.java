package com.google.android.setupcompat.internal;

import android.app.Activity;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FocusChangedMetricHelper {
    private FocusChangedMetricHelper() {
    }

    public static final String getScreenName(Activity activity) {
        return activity.getComponentName().toShortString();
    }

    public static final Bundle getExtraBundle(Activity activity, TemplateLayout templateLayout, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ExtraKey.PACKAGE_NAME, activity.getComponentName().getPackageName());
        bundle.putString(Constants.ExtraKey.SCREEN_NAME, activity.getComponentName().getShortClassName());
        bundle.putInt(Constants.ExtraKey.HASH_CODE, templateLayout.hashCode());
        bundle.putBoolean(Constants.ExtraKey.HAS_FOCUS, z);
        bundle.putLong(Constants.ExtraKey.TIME_IN_MILLIS, System.currentTimeMillis());
        return bundle;
    }

    public static final class Constants {

        @Retention(RetentionPolicy.SOURCE)
        public @interface ExtraKey {
            public static final String HASH_CODE = "hash";
            public static final String HAS_FOCUS = "focus";
            public static final String PACKAGE_NAME = "packageName";
            public static final String SCREEN_NAME = "screenName";
            public static final String TIME_IN_MILLIS = "timeInMillis";
        }

        private Constants() {
        }
    }
}
