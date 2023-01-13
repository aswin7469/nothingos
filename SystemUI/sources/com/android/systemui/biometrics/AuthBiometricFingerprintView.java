package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import com.android.systemui.biometrics.AuthDialog;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\r\u001a\u00020\u000eH\u0014J\b\u0010\u000f\u001a\u00020\u0010H\u0014J\b\u0010\u0011\u001a\u00020\u0010H\u0014J\b\u0010\u0012\u001a\u00020\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u0013H\u0014J\b\u0010\u0015\u001a\u00020\u0013H\u0014J0\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u0010H\u0016J\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\u0014J\u000e\u0010 \u001a\u00020\u00132\u0006\u0010!\u001a\u00020\"J\b\u0010#\u001a\u00020\u0013H\u0002J\b\u0010$\u001a\u00020\bH\u0014R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, mo65043d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFingerprintView;", "Lcom/android/systemui/biometrics/AuthBiometricView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "<set-?>", "", "isUdfps", "()Z", "udfpsAdapter", "Lcom/android/systemui/biometrics/UdfpsDialogMeasureAdapter;", "createIconController", "Lcom/android/systemui/biometrics/AuthIconController;", "getDelayAfterAuthenticatedDurationMs", "", "getStateForAfterError", "handleResetAfterError", "", "handleResetAfterHelp", "onAttachedToWindow", "onLayout", "changed", "left", "top", "right", "bottom", "onMeasureInternal", "Lcom/android/systemui/biometrics/AuthDialog$LayoutParams;", "width", "height", "setSensorProperties", "sensorProps", "Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;", "showTouchSensorString", "supportsSmallDialog", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthBiometricFingerprintView.kt */
public class AuthBiometricFingerprintView extends AuthBiometricView {
    public Map<Integer, View> _$_findViewCache;
    private boolean isUdfps;
    private UdfpsDialogMeasureAdapter udfpsAdapter;

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
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getStateForAfterError() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean supportsSmallDialog() {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFingerprintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AuthBiometricFingerprintView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    public final boolean isUdfps() {
        return this.isUdfps;
    }

    public final void setSensorProperties(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        Intrinsics.checkNotNullParameter(fingerprintSensorPropertiesInternal, "sensorProps");
        boolean isAnyUdfpsType = fingerprintSensorPropertiesInternal.isAnyUdfpsType();
        this.isUdfps = isAnyUdfpsType;
        this.udfpsAdapter = isAnyUdfpsType ? new UdfpsDialogMeasureAdapter(this, fingerprintSensorPropertiesInternal) : null;
    }

    /* access modifiers changed from: protected */
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        AuthDialog.LayoutParams onMeasureInternal = super.onMeasureInternal(i, i2);
        Intrinsics.checkNotNullExpressionValue(onMeasureInternal, "super.onMeasureInternal(width, height)");
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.udfpsAdapter;
        AuthDialog.LayoutParams onMeasureInternal2 = udfpsDialogMeasureAdapter != null ? udfpsDialogMeasureAdapter.onMeasureInternal(i, i2, onMeasureInternal) : null;
        return onMeasureInternal2 == null ? onMeasureInternal : onMeasureInternal2;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.udfpsAdapter;
        if (udfpsDialogMeasureAdapter != null) {
            int bottomSpacerHeight = udfpsDialogMeasureAdapter.getBottomSpacerHeight();
            Log.w("AuthBiometricFingerprintView", "bottomSpacerHeight: " + bottomSpacerHeight);
            if (bottomSpacerHeight < 0) {
                View findViewById = findViewById(C1894R.C1898id.biometric_icon_frame);
                Intrinsics.checkNotNull(findViewById);
                float f = -((float) bottomSpacerHeight);
                ((FrameLayout) findViewById).setTranslationY(f);
                View findViewById2 = findViewById(C1894R.C1898id.space_above_icon);
                Intrinsics.checkNotNull(findViewById2);
                ((FrameLayout) findViewById2).setTranslationY(f);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterError() {
        showTouchSensorString();
    }

    /* access modifiers changed from: protected */
    public void handleResetAfterHelp() {
        showTouchSensorString();
    }

    /* access modifiers changed from: protected */
    public AuthIconController createIconController() {
        Context context = this.mContext;
        Intrinsics.checkNotNullExpressionValue(context, "mContext");
        ImageView imageView = this.mIconView;
        Intrinsics.checkNotNullExpressionValue(imageView, "mIconView");
        return new AuthBiometricFingerprintIconController(context, imageView);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showTouchSensorString();
    }

    private final void showTouchSensorString() {
        if (this.mIndicatorImg != null) {
            this.mIndicatorView.setVisibility(4);
            this.mIndicatorImg.setVisibility(0);
        }
        this.mIndicatorView.setText(C1894R.string.fingerprint_dialog_touch_sensor);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }
}
