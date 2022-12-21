package com.android.systemui.p012qs;

import android.content.res.Configuration;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import com.nothing.systemui.p024qs.NTQSStatusBarController;
import javax.inject.Inject;

@QSScope
/* renamed from: com.android.systemui.qs.QSContainerImplController */
public class QSContainerImplController extends ViewController<QSContainerImpl> {
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            ((QSContainerImpl) QSContainerImplController.this.mView).updateResources(QSContainerImplController.this.mQsPanelController, QSContainerImplController.this.mQuickStatusBarHeaderController);
        }
    };
    private final NTQSStatusBarController mNTQSStatusBarController;
    /* access modifiers changed from: private */
    public final QSPanelController mQsPanelController;
    /* access modifiers changed from: private */
    public final QuickStatusBarHeaderController mQuickStatusBarHeaderController;

    @Inject
    QSContainerImplController(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController, NTQSStatusBarController nTQSStatusBarController, ConfigurationController configurationController) {
        super(qSContainerImpl);
        this.mQsPanelController = qSPanelController;
        this.mQuickStatusBarHeaderController = quickStatusBarHeaderController;
        this.mConfigurationController = configurationController;
        this.mNTQSStatusBarController = nTQSStatusBarController;
    }

    public void onInit() {
        this.mQuickStatusBarHeaderController.init();
        this.mNTQSStatusBarController.init();
    }

    public void setListening(boolean z) {
        this.mQuickStatusBarHeaderController.setListening(z);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        ((QSContainerImpl) this.mView).updateResources(this.mQsPanelController, this.mQuickStatusBarHeaderController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public QSContainerImpl getView() {
        return (QSContainerImpl) this.mView;
    }
}
