package com.android.systemui.p012qs.dagger;

import com.android.systemui.p012qs.QuickStatusBarHeader;
import com.android.systemui.privacy.OngoingPrivacyChip;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesPrivacyChipFactory */
public final class QSFragmentModule_ProvidesPrivacyChipFactory implements Factory<OngoingPrivacyChip> {
    private final Provider<QuickStatusBarHeader> qsHeaderProvider;

    public QSFragmentModule_ProvidesPrivacyChipFactory(Provider<QuickStatusBarHeader> provider) {
        this.qsHeaderProvider = provider;
    }

    public OngoingPrivacyChip get() {
        return providesPrivacyChip(this.qsHeaderProvider.get());
    }

    public static QSFragmentModule_ProvidesPrivacyChipFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_ProvidesPrivacyChipFactory(provider);
    }

    public static OngoingPrivacyChip providesPrivacyChip(QuickStatusBarHeader quickStatusBarHeader) {
        return (OngoingPrivacyChip) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesPrivacyChip(quickStatusBarHeader));
    }
}
