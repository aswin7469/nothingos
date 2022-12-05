package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LowPriorityInflationHelper_Factory implements Factory<LowPriorityInflationHelper> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    private final Provider<RowContentBindStage> rowContentBindStageProvider;

    public LowPriorityInflationHelper_Factory(Provider<FeatureFlags> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<RowContentBindStage> provider3) {
        this.featureFlagsProvider = provider;
        this.groupManagerProvider = provider2;
        this.rowContentBindStageProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LowPriorityInflationHelper mo1933get() {
        return newInstance(this.featureFlagsProvider.mo1933get(), this.groupManagerProvider.mo1933get(), this.rowContentBindStageProvider.mo1933get());
    }

    public static LowPriorityInflationHelper_Factory create(Provider<FeatureFlags> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<RowContentBindStage> provider3) {
        return new LowPriorityInflationHelper_Factory(provider, provider2, provider3);
    }

    public static LowPriorityInflationHelper newInstance(FeatureFlags featureFlags, NotificationGroupManagerLegacy notificationGroupManagerLegacy, RowContentBindStage rowContentBindStage) {
        return new LowPriorityInflationHelper(featureFlags, notificationGroupManagerLegacy, rowContentBindStage);
    }
}
