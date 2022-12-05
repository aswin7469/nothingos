package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import java.util.Objects;
/* loaded from: classes.dex */
public class UdfpsEnrollView extends UdfpsAnimationView {
    private final UdfpsEnrollProgressBarDrawable mFingerprintProgressDrawable;
    private ImageView mFingerprintProgressView;
    private ImageView mFingerprintView;
    private final UdfpsEnrollDrawable mFingerprintDrawable = new UdfpsEnrollDrawable(((FrameLayout) this).mContext);
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public /* bridge */ /* synthetic */ void onExpansionChanged(float f, boolean z) {
        super.onExpansionChanged(f, z);
    }

    public UdfpsEnrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintProgressDrawable = new UdfpsEnrollProgressBarDrawable(context);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mFingerprintView = (ImageView) findViewById(R$id.udfps_enroll_animation_fp_view);
        this.mFingerprintProgressView = (ImageView) findViewById(R$id.udfps_enroll_animation_fp_progress_view);
        this.mFingerprintView.setImageDrawable(getContext().getDrawable(R$drawable.nt_ic_fingerprint_area_normal));
        this.mFingerprintProgressView.setImageDrawable(this.mFingerprintProgressDrawable);
        this.mFingerprintProgressView.setVisibility(8);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSensorLocation(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        View findViewById = findViewById(R$id.udfps_enroll_accessibility_view);
        int i = fingerprintSensorPropertiesInternal.sensorRadius * 2;
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i;
        findViewById.setLayoutParams(layoutParams);
        findViewById.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mFingerprintProgressDrawable.setEnrollHelper(udfpsEnrollHelper);
        this.mFingerprintDrawable.setEnrollHelper(udfpsEnrollHelper);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnrollmentProgress(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsEnrollView.this.lambda$onEnrollmentProgress$0(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$0(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentProgress(i, i2);
        this.mFingerprintDrawable.onEnrollmentProgress(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentHelp$1(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentHelp(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnrollmentHelp(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsEnrollView.this.lambda$onEnrollmentHelp$1(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        Handler handler = this.mHandler;
        final UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = this.mFingerprintProgressDrawable;
        Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
        handler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsEnrollProgressBarDrawable.this.onLastStepAcquired();
            }
        });
    }
}
