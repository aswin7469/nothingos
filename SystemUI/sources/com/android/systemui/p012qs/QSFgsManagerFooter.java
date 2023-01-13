package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.FgsManagerController;
import com.android.systemui.p012qs.VisibilityChangedDispatcher;
import com.android.systemui.p012qs.dagger.QSScope;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;

@QSScope
/* renamed from: com.android.systemui.qs.QSFgsManagerFooter */
public class QSFgsManagerFooter implements View.OnClickListener, FgsManagerController.OnDialogDismissedListener, FgsManagerController.OnNumberOfPackagesChangedListener, VisibilityChangedDispatcher {
    private final ImageView mCollapsedDotView;
    private final Context mContext;
    private final ImageView mDotView;
    private final Executor mExecutor;
    private final FgsManagerController mFgsManagerController;
    private final TextView mFooterText;
    private boolean mIsInitialized = false;
    private final Executor mMainExecutor;
    private int mNumPackages;
    private final View mNumberContainer;
    private final TextView mNumberView;
    private final View mRootView;
    private final View mTextContainer;
    private VisibilityChangedDispatcher.OnVisibilityChangedListener mVisibilityChangedListener;

    @Inject
    QSFgsManagerFooter(@Named("qs_fgs_manager_footer") View view, @Main Executor executor, @Background Executor executor2, FgsManagerController fgsManagerController) {
        this.mRootView = view;
        this.mFooterText = (TextView) view.findViewById(C1894R.C1898id.footer_text);
        this.mTextContainer = view.findViewById(C1894R.C1898id.fgs_text_container);
        this.mNumberContainer = view.findViewById(C1894R.C1898id.fgs_number_container);
        this.mNumberView = (TextView) view.findViewById(C1894R.C1898id.fgs_number);
        this.mDotView = (ImageView) view.findViewById(C1894R.C1898id.fgs_new);
        this.mCollapsedDotView = (ImageView) view.findViewById(C1894R.C1898id.fgs_collapsed_new);
        this.mContext = view.getContext();
        this.mMainExecutor = executor;
        this.mExecutor = executor2;
        this.mFgsManagerController = fgsManagerController;
    }

    public void setCollapsed(boolean z) {
        int i = 8;
        int i2 = 0;
        this.mTextContainer.setVisibility(z ? 8 : 0);
        View view = this.mNumberContainer;
        if (z) {
            i = 0;
        }
        view.setVisibility(i);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRootView.getLayoutParams();
        if (z) {
            i2 = -2;
        }
        layoutParams.width = i2;
        layoutParams.weight = z ? 0.0f : 1.0f;
        this.mRootView.setLayoutParams(layoutParams);
    }

    public void init() {
        if (!this.mIsInitialized) {
            this.mFgsManagerController.init();
            this.mRootView.setOnClickListener(this);
            this.mIsInitialized = true;
        }
    }

    public void setListening(boolean z) {
        if (z) {
            this.mFgsManagerController.addOnDialogDismissedListener(this);
            this.mFgsManagerController.addOnNumberOfPackagesChangedListener(this);
            this.mNumPackages = this.mFgsManagerController.getNumRunningPackages();
            refreshState();
            return;
        }
        this.mFgsManagerController.removeOnDialogDismissedListener(this);
        this.mFgsManagerController.removeOnNumberOfPackagesChangedListener(this);
    }

    public void setOnVisibilityChangedListener(VisibilityChangedDispatcher.OnVisibilityChangedListener onVisibilityChangedListener) {
        this.mVisibilityChangedListener = onVisibilityChangedListener;
    }

    public void onClick(View view) {
        this.mFgsManagerController.showDialog(this.mRootView);
    }

    public void refreshState() {
        this.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda1(this));
    }

    public View getView() {
        return this.mRootView;
    }

    public void handleRefreshState() {
        this.mMainExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleRefreshState$0$com-android-systemui-qs-QSFgsManagerFooter */
    public /* synthetic */ void mo36055xc39012a0() {
        Resources resources = this.mContext.getResources();
        int i = this.mNumPackages;
        int i2 = 0;
        String quantityString = resources.getQuantityString(C1894R.plurals.fgs_manager_footer_label, i, new Object[]{Integer.valueOf(i)});
        this.mFooterText.setText(quantityString);
        this.mNumberView.setText(Integer.toString(this.mNumPackages));
        this.mNumberView.setContentDescription(quantityString);
        if (this.mFgsManagerController.shouldUpdateFooterVisibility()) {
            this.mRootView.setVisibility((this.mNumPackages <= 0 || !this.mFgsManagerController.isAvailable()) ? 8 : 0);
            if (!this.mFgsManagerController.getShowFooterDot() || !this.mFgsManagerController.getChangesSinceDialog()) {
                i2 = 8;
            }
            this.mDotView.setVisibility(i2);
            this.mCollapsedDotView.setVisibility(i2);
            VisibilityChangedDispatcher.OnVisibilityChangedListener onVisibilityChangedListener = this.mVisibilityChangedListener;
            if (onVisibilityChangedListener != null) {
                onVisibilityChangedListener.onVisibilityChanged(this.mRootView.getVisibility());
            }
        }
    }

    public void onDialogDismissed() {
        refreshState();
    }

    public void onNumberOfPackagesChanged(int i) {
        this.mNumPackages = i;
        refreshState();
    }
}
