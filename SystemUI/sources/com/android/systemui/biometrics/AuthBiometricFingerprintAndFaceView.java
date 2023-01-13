package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\"\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0014J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J\b\u0010\u000e\u001a\u00020\rH\u0014J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J\u0016\u0010\u0010\u001a\u00020\u000b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u0012H\u0016¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFingerprintAndFaceView;", "Lcom/android/systemui/biometrics/AuthBiometricFingerprintView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "createIconController", "Lcom/android/systemui/biometrics/AuthIconController;", "forceRequireConfirmation", "", "modality", "", "getConfirmationPrompt", "ignoreUnsuccessfulEventsFrom", "onPointerDown", "failedModalities", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthBiometricFingerprintAndFaceView.kt */
public final class AuthBiometricFingerprintAndFaceView extends AuthBiometricFingerprintView {
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
    public boolean forceRequireConfirmation(int i) {
        return i == 8;
    }

    /* access modifiers changed from: protected */
    public int getConfirmationPrompt() {
        return C1894R.string.biometric_dialog_tap_confirm_with_face;
    }

    /* access modifiers changed from: protected */
    public boolean ignoreUnsuccessfulEventsFrom(int i) {
        return i == 8;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFingerprintAndFaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AuthBiometricFingerprintAndFaceView(Context context) {
        this(context, (AttributeSet) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public boolean onPointerDown(Set<Integer> set) {
        Intrinsics.checkNotNullParameter(set, "failedModalities");
        return set.contains(8);
    }

    /* access modifiers changed from: protected */
    public AuthIconController createIconController() {
        Context context = this.mContext;
        Intrinsics.checkNotNullExpressionValue(context, "mContext");
        ImageView imageView = this.mIconView;
        Intrinsics.checkNotNullExpressionValue(imageView, "mIconView");
        return new AuthBiometricFingerprintAndFaceIconController(context, imageView);
    }
}
