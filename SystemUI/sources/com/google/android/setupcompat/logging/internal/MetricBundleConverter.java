package com.google.android.setupcompat.logging.internal;

import android.os.Bundle;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.internal.SetupMetricsLoggingConstants;

public final class MetricBundleConverter {
    public static Bundle createBundleForLogging(CustomEvent customEvent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetupMetricsLoggingConstants.MetricBundleKeys.CUSTOM_EVENT_BUNDLE, CustomEvent.toBundle(customEvent));
        return bundle;
    }

    public static Bundle createBundleForLoggingCounter(MetricKey metricKey, int i) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetupMetricsLoggingConstants.MetricBundleKeys.METRIC_KEY_BUNDLE, MetricKey.fromMetricKey(metricKey));
        bundle.putInt(SetupMetricsLoggingConstants.MetricBundleKeys.COUNTER_INT, i);
        return bundle;
    }

    public static Bundle createBundleForLoggingTimer(MetricKey metricKey, long j) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetupMetricsLoggingConstants.MetricBundleKeys.METRIC_KEY_BUNDLE, MetricKey.fromMetricKey(metricKey));
        bundle.putLong(SetupMetricsLoggingConstants.MetricBundleKeys.TIME_MILLIS_LONG, j);
        return bundle;
    }

    private MetricBundleConverter() {
        throw new AssertionError((Object) "Cannot instantiate MetricBundleConverter");
    }
}
