package com.android.systemui.wallet.p017ui;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.wallet.p017ui.WalletCardCarousel;
import java.util.List;

/* renamed from: com.android.systemui.wallet.ui.WalletView */
public class WalletView extends FrameLayout implements WalletCardCarousel.OnCardScrollListener {
    private static final int CAROUSEL_IN_ANIMATION_DURATION = 100;
    private static final int CAROUSEL_OUT_ANIMATION_DURATION = 200;
    private static final String TAG = "WalletView";
    private final Button mActionButton;
    private final float mAnimationTranslationX;
    private final Button mAppButton;
    private final WalletCardCarousel mCardCarousel;
    private final ViewGroup mCardCarouselContainer;
    private final TextView mCardLabel;
    private View.OnClickListener mDeviceLockedActionOnClickListener;
    private final ViewGroup mEmptyStateView;
    private final TextView mErrorView;
    private FalsingCollector mFalsingCollector;
    private final ImageView mIcon;
    private boolean mIsDeviceLocked;
    private boolean mIsUdfpsEnabled;
    private final Interpolator mOutInterpolator;
    private View.OnClickListener mShowWalletAppOnClickListener;
    private final Button mToolbarAppButton;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public WalletView(Context context) {
        this(context, (AttributeSet) null);
    }

    public WalletView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsDeviceLocked = false;
        this.mIsUdfpsEnabled = false;
        inflate(context, C1893R.layout.wallet_fullscreen, this);
        this.mCardCarouselContainer = (ViewGroup) requireViewById(C1893R.C1897id.card_carousel_container);
        WalletCardCarousel walletCardCarousel = (WalletCardCarousel) requireViewById(C1893R.C1897id.card_carousel);
        this.mCardCarousel = walletCardCarousel;
        walletCardCarousel.setCardScrollListener(this);
        this.mIcon = (ImageView) requireViewById(C1893R.C1897id.icon);
        this.mCardLabel = (TextView) requireViewById(C1893R.C1897id.label);
        this.mAppButton = (Button) requireViewById(C1893R.C1897id.wallet_app_button);
        this.mToolbarAppButton = (Button) requireViewById(C1893R.C1897id.wallet_toolbar_app_button);
        this.mActionButton = (Button) requireViewById(C1893R.C1897id.wallet_action_button);
        this.mErrorView = (TextView) requireViewById(C1893R.C1897id.error_view);
        this.mEmptyStateView = (ViewGroup) requireViewById(C1893R.C1897id.wallet_empty_state);
        this.mOutInterpolator = AnimationUtils.loadInterpolator(context, 17563650);
        this.mAnimationTranslationX = ((float) walletCardCarousel.getCardWidthPx()) / 4.0f;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mCardCarousel.setExpectedViewWidth(getWidth());
    }

    private void updateViewForOrientation(int i) {
        if (i == 1) {
            renderViewPortrait();
        } else if (i == 2) {
            renderViewLandscape();
        }
        this.mCardCarousel.resetAdapter();
        ViewGroup.LayoutParams layoutParams = this.mCardCarouselContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = getResources().getDimensionPixelSize(C1893R.dimen.wallet_card_carousel_container_top_margin);
        }
    }

    private void renderViewPortrait() {
        this.mAppButton.setVisibility(0);
        this.mToolbarAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(0);
        requireViewById(C1893R.C1897id.dynamic_placeholder).setVisibility(0);
        this.mAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    private void renderViewLandscape() {
        this.mToolbarAppButton.setVisibility(0);
        this.mAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(8);
        requireViewById(C1893R.C1897id.dynamic_placeholder).setVisibility(8);
        this.mToolbarAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mCardCarousel.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }

    public void onCardScroll(WalletCardViewInfo walletCardViewInfo, WalletCardViewInfo walletCardViewInfo2, float f) {
        CharSequence labelText = getLabelText(walletCardViewInfo);
        Drawable headerIcon = getHeaderIcon(this.mContext, walletCardViewInfo);
        renderActionButton(walletCardViewInfo, this.mIsDeviceLocked, this.mIsUdfpsEnabled);
        if (walletCardViewInfo.isUiEquivalent(walletCardViewInfo2)) {
            this.mCardLabel.setAlpha(1.0f);
            this.mIcon.setAlpha(1.0f);
            this.mActionButton.setAlpha(1.0f);
            return;
        }
        this.mCardLabel.setText(labelText);
        this.mIcon.setImageDrawable(headerIcon);
        this.mCardLabel.setAlpha(f);
        this.mIcon.setAlpha(f);
        this.mActionButton.setAlpha(f);
    }

    /* access modifiers changed from: package-private */
    public void showCardCarousel(List<WalletCardViewInfo> list, int i, boolean z, boolean z2) {
        boolean data = this.mCardCarousel.setData(list, i, this.mIsDeviceLocked != z);
        this.mIsDeviceLocked = z;
        this.mIsUdfpsEnabled = z2;
        this.mCardCarouselContainer.setVisibility(0);
        this.mCardCarousel.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
        this.mIcon.setImageDrawable(getHeaderIcon(this.mContext, list.get(i)));
        this.mCardLabel.setText(getLabelText(list.get(i)));
        updateViewForOrientation(getResources().getConfiguration().orientation);
        renderActionButton(list.get(i), z, this.mIsUdfpsEnabled);
        if (data) {
            animateViewsShown(this.mIcon, this.mCardLabel, this.mActionButton);
        }
    }

    /* access modifiers changed from: package-private */
    public void animateDismissal() {
        if (this.mCardCarouselContainer.getVisibility() == 0) {
            this.mCardCarousel.animate().translationX(this.mAnimationTranslationX).setInterpolator(this.mOutInterpolator).setDuration(200).start();
            this.mCardCarouselContainer.animate().alpha(0.0f).setDuration(100).setStartDelay(50).start();
        }
    }

    /* access modifiers changed from: package-private */
    public void showEmptyStateView(Drawable drawable, CharSequence charSequence, CharSequence charSequence2, View.OnClickListener onClickListener) {
        this.mEmptyStateView.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mCardCarousel.setVisibility(8);
        this.mIcon.setImageDrawable(drawable);
        this.mIcon.setContentDescription(charSequence);
        this.mCardLabel.setText(C1893R.string.wallet_empty_state_label);
        ((ImageView) this.mEmptyStateView.requireViewById(C1893R.C1897id.empty_state_icon)).setImageDrawable(this.mContext.getDrawable(C1893R.C1895drawable.ic_qs_plus));
        ((TextView) this.mEmptyStateView.requireViewById(C1893R.C1897id.empty_state_title)).setText(charSequence2);
        this.mEmptyStateView.setOnClickListener(onClickListener);
        this.mAppButton.setOnClickListener(onClickListener);
    }

    /* access modifiers changed from: package-private */
    public void showErrorMessage(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = getResources().getText(C1893R.string.wallet_error_generic);
        }
        this.mErrorView.setText(charSequence);
        this.mErrorView.setVisibility(0);
        this.mCardCarouselContainer.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void setDeviceLockedActionOnClickListener(View.OnClickListener onClickListener) {
        this.mDeviceLockedActionOnClickListener = onClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setShowWalletAppOnClickListener(View.OnClickListener onClickListener) {
        this.mShowWalletAppOnClickListener = onClickListener;
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void show() {
        setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public void hideErrorMessage() {
        this.mErrorView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public WalletCardCarousel getCardCarousel() {
        return this.mCardCarousel;
    }

    /* access modifiers changed from: package-private */
    public Button getActionButton() {
        return this.mActionButton;
    }

    /* access modifiers changed from: package-private */
    public Button getAppButton() {
        return this.mAppButton;
    }

    /* access modifiers changed from: package-private */
    public TextView getErrorView() {
        return this.mErrorView;
    }

    /* access modifiers changed from: package-private */
    public ViewGroup getEmptyStateView() {
        return this.mEmptyStateView;
    }

    /* access modifiers changed from: package-private */
    public ViewGroup getCardCarouselContainer() {
        return this.mCardCarouselContainer;
    }

    /* access modifiers changed from: package-private */
    public TextView getCardLabel() {
        return this.mCardLabel;
    }

    private static Drawable getHeaderIcon(Context context, WalletCardViewInfo walletCardViewInfo) {
        Drawable icon = walletCardViewInfo.getIcon();
        if (icon != null) {
            icon.setTint(Utils.getColorAttrDefaultColor(context, 17956900));
        }
        return icon;
    }

    private void renderActionButton(WalletCardViewInfo walletCardViewInfo, boolean z, boolean z2) {
        View.OnClickListener onClickListener;
        CharSequence actionButtonText = getActionButtonText(walletCardViewInfo);
        if (z2 || actionButtonText == null) {
            this.mActionButton.setVisibility(8);
            return;
        }
        this.mActionButton.setVisibility(0);
        this.mActionButton.setText(actionButtonText);
        Button button = this.mActionButton;
        if (z) {
            onClickListener = this.mDeviceLockedActionOnClickListener;
        } else {
            onClickListener = new WalletView$$ExternalSyntheticLambda0(walletCardViewInfo);
        }
        button.setOnClickListener(onClickListener);
    }

    static /* synthetic */ void lambda$renderActionButton$0(WalletCardViewInfo walletCardViewInfo, View view) {
        try {
            walletCardViewInfo.getPendingIntent().send();
        } catch (PendingIntent.CanceledException unused) {
            Log.w(TAG, "Error sending pending intent for wallet card.");
        }
    }

    private static void animateViewsShown(View... viewArr) {
        for (View view : viewArr) {
            if (view.getVisibility() == 0) {
                view.setAlpha(0.0f);
                view.animate().alpha(1.0f).setDuration(100).start();
            }
        }
    }

    private static CharSequence getLabelText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        return split.length == 2 ? split[0] : walletCardViewInfo.getLabel();
    }

    private static CharSequence getActionButtonText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        if (split.length == 2) {
            return split[1];
        }
        return null;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        FalsingCollector falsingCollector = this.mFalsingCollector;
        if (falsingCollector != null) {
            falsingCollector.onTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        FalsingCollector falsingCollector2 = this.mFalsingCollector;
        if (falsingCollector2 != null) {
            falsingCollector2.onMotionEventComplete();
        }
        return dispatchTouchEvent;
    }

    public void setFalsingCollector(FalsingCollector falsingCollector) {
        this.mFalsingCollector = falsingCollector;
    }
}
