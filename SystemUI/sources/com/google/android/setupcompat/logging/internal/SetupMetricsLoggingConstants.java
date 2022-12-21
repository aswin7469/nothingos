package com.google.android.setupcompat.logging.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface SetupMetricsLoggingConstants {

    @Retention(RetentionPolicy.SOURCE)
    public @interface MetricBundleKeys {
        public static final String COUNTER_INT = "counter";
        @Deprecated
        public static final String CUSTOM_EVENT = "CustomEvent";
        public static final String CUSTOM_EVENT_BUNDLE = "CustomEvent_bundle";
        @Deprecated
        public static final String METRIC_KEY = "MetricKey";
        public static final String METRIC_KEY_BUNDLE = "MetricKey_bundle";
        public static final String TIME_MILLIS_LONG = "timeMillis";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MetricType {
        public static final int COUNTER_EVENT = 3;
        public static final int CUSTOM_EVENT = 1;
        public static final int DURATION_EVENT = 2;
        public static final int INTERNAL = 100;
    }
}
