package com.nothingos.systemui.qs;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class QSStatusBarController_Factory implements Factory<QSStatusBarController> {
    private final Provider<Context> contextProvider;
    private final Provider<PrivacyDialogController> privacyDialogControllerProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;
    private final Provider<PrivacyLogger> privacyLoggerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public QSStatusBarController_Factory(Provider<Context> provider, Provider<PrivacyDialogController> provider2, Provider<PrivacyItemController> provider3, Provider<UiEventLogger> provider4, Provider<PrivacyLogger> provider5) {
        this.contextProvider = provider;
        this.privacyDialogControllerProvider = provider2;
        this.privacyItemControllerProvider = provider3;
        this.uiEventLoggerProvider = provider4;
        this.privacyLoggerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSStatusBarController mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.privacyDialogControllerProvider.mo1933get(), this.privacyItemControllerProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.privacyLoggerProvider.mo1933get());
    }

    public static QSStatusBarController_Factory create(Provider<Context> provider, Provider<PrivacyDialogController> provider2, Provider<PrivacyItemController> provider3, Provider<UiEventLogger> provider4, Provider<PrivacyLogger> provider5) {
        return new QSStatusBarController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static QSStatusBarController newInstance(Context context, PrivacyDialogController privacyDialogController, PrivacyItemController privacyItemController, UiEventLogger uiEventLogger, PrivacyLogger privacyLogger) {
        return new QSStatusBarController(context, privacyDialogController, privacyItemController, uiEventLogger, privacyLogger);
    }
}
