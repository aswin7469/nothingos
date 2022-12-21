package com.android.systemui.p012qs.dagger;

import com.android.systemui.p012qs.FooterActionsController;
import com.android.systemui.p012qs.QSAnimator;
import com.android.systemui.p012qs.QSContainerImplController;
import com.android.systemui.p012qs.QSFooter;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.p012qs.QSSquishinessController;
import com.android.systemui.p012qs.QuickQSPanelController;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.nothing.systemui.p024qs.NTQSAnimator;
import dagger.BindsInstance;
import dagger.Subcomponent;

@QSScope
@Subcomponent(modules = {QSFragmentModule.class})
/* renamed from: com.android.systemui.qs.dagger.QSFragmentComponent */
public interface QSFragmentComponent {

    @Subcomponent.Factory
    /* renamed from: com.android.systemui.qs.dagger.QSFragmentComponent$Factory */
    public interface Factory {
        QSFragmentComponent create(@BindsInstance QSFragment qSFragment);
    }

    NTQSAnimator getNTQSAnimator();

    QSAnimator getQSAnimator();

    QSContainerImplController getQSContainerImplController();

    QSCustomizerController getQSCustomizerController();

    QSFooter getQSFooter();

    FooterActionsController getQSFooterActionController();

    QSPanelController getQSPanelController();

    QSSquishinessController getQSSquishinessController();

    QuickQSPanelController getQuickQSPanelController();
}
