package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0014J\b\u0010\t\u001a\u00020\nH\u0014J\b\u0010\u000b\u001a\u00020\nH\u0014J\b\u0010\f\u001a\u00020\rH\u0014J\b\u0010\u000e\u001a\u00020\rH\u0014J\u001a\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\n2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u0013\u001a\u00020\rH\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0015H\u0014J\b\u0010\u0017\u001a\u00020\u0015H\u0014J\u0010\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\nH\u0016¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFaceView;", "Lcom/android/systemui/biometrics/AuthBiometricView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "createIconController", "Lcom/android/systemui/biometrics/AuthIconController;", "getDelayAfterAuthenticatedDurationMs", "", "getStateForAfterError", "handleResetAfterError", "", "handleResetAfterHelp", "onAuthenticationFailed", "modality", "failureReason", "", "resetErrorView", "supportsManualRetry", "", "supportsRequireConfirmation", "supportsSmallDialog", "updateState", "newState", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthBiometricFaceView.kt */
public final class AuthBiometricFaceView extends AuthBiometricView {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int HIDE_DELAY_MS = 500;
    public Map<Integer, View> _$_findViewCache;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* access modifiers changed from: protected */
    public int getDelayAfterAuthenticatedDurationMs() {
        return 500;
    }

    /* access modifiers changed from: protected */
    public int getStateForAfterError() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean supportsManualRetry() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean supportsRequireConfirmation() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean supportsSmallDialog() {
        return true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AuthBiometricFaceView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterError() {
        resetErrorView();
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterHelp() {
        resetErrorView();
    }

    /* access modifiers changed from: protected */
    public AuthIconController createIconController() {
        Context context = this.mContext;
        Intrinsics.checkNotNullExpressionValue(context, "mContext");
        ImageView imageView = this.mIconView;
        Intrinsics.checkNotNullExpressionValue(imageView, "mIconView");
        return new AuthBiometricFaceIconController(context, imageView);
    }

    public void updateState(int i) {
        if (i == 1 || (i == 2 && getSize() == 2)) {
            resetErrorView();
        }
        super.updateState(i);
    }

    public void onAuthenticationFailed(int i, String str) {
        if (getSize() == 2 && supportsManualRetry()) {
            this.mTryAgainButton.setVisibility(0);
            this.mConfirmButton.setVisibility(8);
        }
        super.onAuthenticationFailed(i, str);
    }

    private final void resetErrorView() {
        this.mIndicatorView.setTextColor(this.mTextColorHint);
        this.mIndicatorView.setVisibility(4);
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFaceView$Companion;", "", "()V", "HIDE_DELAY_MS", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AuthBiometricFaceView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
