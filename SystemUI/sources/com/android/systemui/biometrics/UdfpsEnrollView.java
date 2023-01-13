package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import java.util.Objects;

public class UdfpsEnrollView extends UdfpsAnimationView {
    private final UdfpsEnrollDrawable mFingerprintDrawable = new UdfpsEnrollDrawable(this.mContext);
    private final UdfpsEnrollProgressBarDrawable mFingerprintProgressDrawable;
    private ImageView mFingerprintProgressView;
    private ImageView mFingerprintView;
    private final Handler mHandler;

    public UdfpsEnrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintProgressDrawable = new UdfpsEnrollProgressBarDrawable(context);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mFingerprintView = (ImageView) findViewById(C1894R.C1898id.udfps_enroll_animation_fp_view);
        this.mFingerprintProgressView = (ImageView) findViewById(C1894R.C1898id.udfps_enroll_animation_fp_progress_view);
        this.mFingerprintView.setImageDrawable(getContext().getDrawable(C1894R.C1896drawable.nt_ic_fingerprint_area_normal));
        this.mFingerprintProgressView.setImageDrawable(this.mFingerprintProgressDrawable);
        this.mFingerprintProgressView.setVisibility(8);
    }

    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    /* access modifiers changed from: package-private */
    public void updateSensorLocation(Rect rect) {
        View findViewById = findViewById(C1894R.C1898id.udfps_enroll_accessibility_view);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        findViewById.setLayoutParams(layoutParams);
        findViewById.requestLayout();
    }

    /* access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mFingerprintDrawable.setEnrollHelper(udfpsEnrollHelper);
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        this.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda0(this, i, i2));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEnrollmentProgress$0$com-android-systemui-biometrics-UdfpsEnrollView */
    public /* synthetic */ void mo30946xf84df628(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentProgress(i, i2);
        this.mFingerprintDrawable.onEnrollmentProgress(i, i2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEnrollmentHelp$1$com-android-systemui-biometrics-UdfpsEnrollView */
    public /* synthetic */ void mo30945x4361c03d(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentHelp(i, i2);
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentHelp(int i, int i2) {
        this.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda1(this, i, i2));
    }

    /* access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        Handler handler = this.mHandler;
        UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = this.mFingerprintProgressDrawable;
        Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
        handler.post(new UdfpsEnrollView$$ExternalSyntheticLambda2(udfpsEnrollProgressBarDrawable));
    }
}
