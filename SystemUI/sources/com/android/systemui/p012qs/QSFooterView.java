package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import com.android.settingslib.development.DevelopmentSettingsEnabler;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.TouchAnimator;

/* renamed from: com.android.systemui.qs.QSFooterView */
public class QSFooterView extends FrameLayout {
    private TextView mBuildText;
    private final ContentObserver mDeveloperSettingsObserver = new ContentObserver(new Handler(this.mContext.getMainLooper())) {
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            QSFooterView.this.setBuildText();
        }
    };
    private View mEditButton;
    private View.OnClickListener mExpandClickListener;
    private boolean mExpanded;
    private float mExpansionAmount;
    protected TouchAnimator mFooterAnimator;
    private PageIndicator mPageIndicator;
    private boolean mQsDisabled;
    private boolean mShouldShowBuildText;

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

    public QSFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPageIndicator = (PageIndicator) findViewById(C1894R.C1898id.footer_page_indicator);
        this.mBuildText = (TextView) findViewById(C1894R.C1898id.build);
        this.mEditButton = findViewById(16908291);
        updateResources();
        setImportantForAccessibility(1);
        setBuildText();
    }

    /* access modifiers changed from: private */
    public void setBuildText() {
        if (this.mBuildText != null) {
            if (DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(this.mContext)) {
                this.mBuildText.setText(this.mContext.getString(17039824, new Object[]{Build.VERSION.RELEASE_OR_CODENAME, Build.ID}));
                this.mBuildText.setSelected(true);
                this.mShouldShowBuildText = true;
                return;
            }
            this.mBuildText.setText((CharSequence) null);
            this.mShouldShowBuildText = false;
            this.mBuildText.setSelected(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    private void updateResources() {
        updateFooterAnimator();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(C1894R.dimen.qs_footers_margin_bottom);
        setLayoutParams(marginLayoutParams);
    }

    private void updateFooterAnimator() {
        this.mFooterAnimator = createFooterAnimator();
    }

    private TouchAnimator createFooterAnimator() {
        return new TouchAnimator.Builder().addFloat(this.mPageIndicator, Key.ALPHA, 0.0f, 1.0f).addFloat(this.mBuildText, Key.ALPHA, 0.0f, 1.0f).addFloat(this.mEditButton, Key.ALPHA, 0.0f, 1.0f).setStartDelay(0.9f).build();
    }

    public void setKeyguardShowing() {
        setExpansion(this.mExpansionAmount);
    }

    public void setExpandClickListener(View.OnClickListener onClickListener) {
        this.mExpandClickListener = onClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setExpanded(boolean z) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            updateEverything();
        }
    }

    public void setExpansion(float f) {
        this.mExpansionAmount = f;
        TouchAnimator touchAnimator = this.mFooterAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("development_settings_enabled"), false, this.mDeveloperSettingsObserver, -1);
    }

    public void onDetachedFromWindow() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mDeveloperSettingsObserver);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: package-private */
    public void disable(int i) {
        boolean z = true;
        if ((i & 1) == 0) {
            z = false;
        }
        if (z != this.mQsDisabled) {
            this.mQsDisabled = z;
            updateEverything();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateEverything() {
        post(new QSFooterView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateEverything$0$com-android-systemui-qs-QSFooterView  reason: not valid java name */
    public /* synthetic */ void m2913lambda$updateEverything$0$comandroidsystemuiqsQSFooterView() {
        updateVisibilities();
        updateClickabilities();
        setClickable(false);
    }

    private void updateClickabilities() {
        TextView textView = this.mBuildText;
        textView.setLongClickable(textView.getVisibility() == 0);
    }

    private void updateVisibilities() {
        this.mBuildText.setVisibility(4);
    }
}
