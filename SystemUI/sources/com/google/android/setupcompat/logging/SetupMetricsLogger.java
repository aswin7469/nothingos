package com.google.android.setupcompat.logging;

import android.content.Context;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.SetupCompatServiceInvoker;
import com.google.android.setupcompat.logging.internal.MetricBundleConverter;
import java.util.concurrent.TimeUnit;

public class SetupMetricsLogger {
    public static void logCustomEvent(Context context, CustomEvent customEvent) {
        Preconditions.checkNotNull(context, "Context cannot be null.");
        Preconditions.checkNotNull(customEvent, "CustomEvent cannot be null.");
        SetupCompatServiceInvoker.get(context).logMetricEvent(1, MetricBundleConverter.createBundleForLogging(customEvent));
    }

    public static void logCounter(Context context, MetricKey metricKey, int i) {
        Preconditions.checkNotNull(context, "Context cannot be null.");
        Preconditions.checkNotNull(metricKey, "CounterName cannot be null.");
        Preconditions.checkArgument(i > 0, "Counter cannot be negative.");
        SetupCompatServiceInvoker.get(context).logMetricEvent(3, MetricBundleConverter.createBundleForLoggingCounter(metricKey, i));
    }

    public static void logDuration(Context context, Timer timer) {
        Preconditions.checkNotNull(context, "Context cannot be null.");
        Preconditions.checkNotNull(timer, "Timer cannot be null.");
        Preconditions.checkArgument(timer.isStopped(), "Timer should be stopped before calling logDuration.");
        logDuration(context, timer.getMetricKey(), TimeUnit.NANOSECONDS.toMillis(timer.getDurationInNanos()));
    }

    public static void logDuration(Context context, MetricKey metricKey, long j) {
        Preconditions.checkNotNull(context, "Context cannot be null.");
        Preconditions.checkNotNull(metricKey, "Timer name cannot be null.");
        Preconditions.checkArgument(j >= 0, "Duration cannot be negative.");
        SetupCompatServiceInvoker.get(context).logMetricEvent(2, MetricBundleConverter.createBundleForLoggingTimer(metricKey, j));
    }
}
