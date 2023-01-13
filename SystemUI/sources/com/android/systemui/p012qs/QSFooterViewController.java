package com.android.systemui.p012qs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;

@QSScope
/* renamed from: com.android.systemui.qs.QSFooterViewController */
public class QSFooterViewController extends ViewController<QSFooterView> implements QSFooter {
    private final ActivityStarter mActivityStarter;
    private final TextView mBuildText = ((TextView) ((QSFooterView) this.mView).findViewById(C1894R.C1898id.build));
    private final View mEditButton = ((QSFooterView) this.mView).findViewById(16908291);
    private final FalsingManager mFalsingManager;
    private final PageIndicator mPageIndicator = ((PageIndicator) ((QSFooterView) this.mView).findViewById(C1894R.C1898id.footer_page_indicator));
    private final QSPanelController mQsPanelController;
    private final UserTracker mUserTracker;

    /* access modifiers changed from: protected */
    public void onViewDetached() {
    }

    @Inject
    QSFooterViewController(QSFooterView qSFooterView, UserTracker userTracker, FalsingManager falsingManager, ActivityStarter activityStarter, QSPanelController qSPanelController) {
        super(qSFooterView);
        this.mUserTracker = userTracker;
        this.mQsPanelController = qSPanelController;
        this.mFalsingManager = falsingManager;
        this.mActivityStarter = activityStarter;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mBuildText.setOnLongClickListener(new QSFooterViewController$$ExternalSyntheticLambda1(this));
        this.mEditButton.setOnClickListener(new QSFooterViewController$$ExternalSyntheticLambda2(this));
        this.mQsPanelController.setFooterPageIndicator(this.mPageIndicator);
        ((QSFooterView) this.mView).updateEverything();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$0$com-android-systemui-qs-QSFooterViewController */
    public /* synthetic */ boolean mo36081xb48f3fdc(View view) {
        CharSequence text = this.mBuildText.getText();
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        ((ClipboardManager) this.mUserTracker.getUserContext().getSystemService(ClipboardManager.class)).setPrimaryClip(ClipData.newPlainText(getResources().getString(C1894R.string.build_number_clip_data_label), text));
        Toast.makeText(getContext(), C1894R.string.build_number_copy_toast, 0).show();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$2$com-android-systemui-qs-QSFooterViewController */
    public /* synthetic */ void mo36083x2824839a(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mActivityStarter.postQSRunnableDismissingKeyguard(new QSFooterViewController$$ExternalSyntheticLambda0(this, view));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$1$com-android-systemui-qs-QSFooterViewController */
    public /* synthetic */ void mo36082xee59e1bb(View view) {
        this.mQsPanelController.showEdit(view);
    }

    public void setVisibility(int i) {
        ((QSFooterView) this.mView).setVisibility(i);
        this.mEditButton.setClickable(i == 0);
    }

    public void setExpanded(boolean z) {
        ((QSFooterView) this.mView).setExpanded(z);
    }

    public void setExpansion(float f) {
        ((QSFooterView) this.mView).setExpansion(f);
    }

    public void setKeyguardShowing(boolean z) {
        ((QSFooterView) this.mView).setKeyguardShowing();
    }

    public void disable(int i, int i2, boolean z) {
        ((QSFooterView) this.mView).disable(i2);
    }
}
