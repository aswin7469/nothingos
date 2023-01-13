package com.android.settingslib.fuelgauge;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import java.time.Duration;
import java.time.Instant;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u000b¨\u0006\r"}, mo65043d2 = {"Lcom/android/settingslib/fuelgauge/Estimate;", "", "estimateMillis", "", "isBasedOnUsage", "", "averageDischargeTime", "(JZJ)V", "getAverageDischargeTime", "()J", "getEstimateMillis", "()Z", "Companion", "SettingsLib_release"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
/* compiled from: Estimate.kt */
public final class Estimate {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final long averageDischargeTime;
    private final long estimateMillis;
    private final boolean isBasedOnUsage;

    @JvmStatic
    public static final Estimate getCachedEstimateIfAvailable(Context context) {
        return Companion.getCachedEstimateIfAvailable(context);
    }

    @JvmStatic
    public static final Instant getLastCacheUpdateTime(Context context) {
        return Companion.getLastCacheUpdateTime(context);
    }

    @JvmStatic
    public static final void storeCachedEstimate(Context context, Estimate estimate) {
        Companion.storeCachedEstimate(context, estimate);
    }

    public Estimate(long j, boolean z, long j2) {
        this.estimateMillis = j;
        this.isBasedOnUsage = z;
        this.averageDischargeTime = j2;
    }

    public final long getEstimateMillis() {
        return this.estimateMillis;
    }

    public final boolean isBasedOnUsage() {
        return this.isBasedOnUsage;
    }

    public final long getAverageDischargeTime() {
        return this.averageDischargeTime;
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0004H\u0007¨\u0006\f"}, mo65043d2 = {"Lcom/android/settingslib/fuelgauge/Estimate$Companion;", "", "()V", "getCachedEstimateIfAvailable", "Lcom/android/settingslib/fuelgauge/Estimate;", "context", "Landroid/content/Context;", "getLastCacheUpdateTime", "Ljava/time/Instant;", "storeCachedEstimate", "", "estimate", "SettingsLib_release"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
    /* compiled from: Estimate.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final Estimate getCachedEstimateIfAvailable(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            ContentResolver contentResolver = context.getContentResolver();
            if (Duration.between(getLastCacheUpdateTime(context), Instant.now()).compareTo(Duration.ofMinutes(1)) > 0) {
                Estimate estimate = null;
                return null;
            }
            long j = Settings.Global.getLong(contentResolver, "time_remaining_estimate_millis", -1);
            boolean z = false;
            if (Settings.Global.getInt(contentResolver, "time_remaining_estimate_based_on_usage", 0) == 1) {
                z = true;
            }
            return new Estimate(j, z, Settings.Global.getLong(contentResolver, "average_time_to_discharge", -1));
        }

        @JvmStatic
        public final void storeCachedEstimate(Context context, Estimate estimate) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(estimate, "estimate");
            ContentResolver contentResolver = context.getContentResolver();
            Settings.Global.putLong(contentResolver, "time_remaining_estimate_millis", estimate.getEstimateMillis());
            Settings.Global.putInt(contentResolver, "time_remaining_estimate_based_on_usage", estimate.isBasedOnUsage() ? 1 : 0);
            Settings.Global.putLong(contentResolver, "average_time_to_discharge", estimate.getAverageDischargeTime());
            Settings.Global.putLong(contentResolver, "battery_estimates_last_update_time", System.currentTimeMillis());
        }

        @JvmStatic
        public final Instant getLastCacheUpdateTime(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Instant ofEpochMilli = Instant.ofEpochMilli(Settings.Global.getLong(context.getContentResolver(), "battery_estimates_last_update_time", -1));
            Intrinsics.checkNotNullExpressionValue(ofEpochMilli, "ofEpochMilli(\n          …                     -1))");
            return ofEpochMilli;
        }
    }
}
