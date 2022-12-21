package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;

public class UdfpsEnrollHelper {
    private static final String NEW_COORDS_OVERRIDE = "com.android.systemui.biometrics.UdfpsNewCoords";
    private static final float SCALE = 0.5f;
    private static final String SCALE_OVERRIDE = "com.android.systemui.biometrics.UdfpsEnrollHelper.scale";
    private static final String TAG = "UdfpsEnrollHelper";
    private final boolean mAccessibilityEnabled;
    private int mCenterTouchCount;
    private final Context mContext;
    private final int mEnrollReason;
    private final FingerprintManager mFingerprintManager;
    private final List<PointF> mGuidedEnrollmentPoints;
    Listener mListener;
    private int mLocationsEnrolled;
    private int mRemainingSteps = -1;
    private int mTotalSteps = -1;

    interface Listener {
        void onEnrollmentHelp(int i, int i2);

        void onEnrollmentProgress(int i, int i2);

        void onLastStepAcquired();
    }

    public UdfpsEnrollHelper(Context context, FingerprintManager fingerprintManager, int i) {
        boolean z = false;
        this.mLocationsEnrolled = 0;
        this.mCenterTouchCount = 0;
        this.mContext = context;
        this.mFingerprintManager = fingerprintManager;
        this.mEnrollReason = i;
        this.mAccessibilityEnabled = ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).isEnabled();
        ArrayList arrayList = new ArrayList();
        this.mGuidedEnrollmentPoints = arrayList;
        float applyDimension = TypedValue.applyDimension(5, 1.0f, context.getResources().getDisplayMetrics());
        if (!(Settings.Secure.getIntForUser(context.getContentResolver(), NEW_COORDS_OVERRIDE, 0, -2) != 0 ? true : z) || (!Build.IS_ENG && !Build.IS_USERDEBUG)) {
            Log.v(TAG, "Using old coordinates");
            arrayList.add(new PointF(2.0f * applyDimension, 0.0f * applyDimension));
            arrayList.add(new PointF(0.87f * applyDimension, -2.7f * applyDimension));
            float f = -1.8f * applyDimension;
            arrayList.add(new PointF(f, -1.31f * applyDimension));
            arrayList.add(new PointF(f, 1.31f * applyDimension));
            arrayList.add(new PointF(0.88f * applyDimension, 2.7f * applyDimension));
            arrayList.add(new PointF(3.94f * applyDimension, -1.06f * applyDimension));
            arrayList.add(new PointF(2.9f * applyDimension, -4.14f * applyDimension));
            arrayList.add(new PointF(-0.52f * applyDimension, -5.95f * applyDimension));
            float f2 = -3.33f * applyDimension;
            arrayList.add(new PointF(f2, f2));
            arrayList.add(new PointF(-3.99f * applyDimension, -0.35f * applyDimension));
            arrayList.add(new PointF(-3.62f * applyDimension, 2.54f * applyDimension));
            arrayList.add(new PointF(-1.49f * applyDimension, 5.57f * applyDimension));
            arrayList.add(new PointF(2.29f * applyDimension, 4.92f * applyDimension));
            arrayList.add(new PointF(3.82f * applyDimension, applyDimension * 1.78f));
            return;
        }
        Log.v(TAG, "Using new coordinates");
        float f3 = -0.15f * applyDimension;
        arrayList.add(new PointF(f3, -1.02f * applyDimension));
        arrayList.add(new PointF(f3, 1.02f * applyDimension));
        float f4 = 0.0f * applyDimension;
        arrayList.add(new PointF(0.29f * applyDimension, f4));
        float f5 = 2.17f * applyDimension;
        arrayList.add(new PointF(f5, -2.35f * applyDimension));
        float f6 = 1.07f * applyDimension;
        arrayList.add(new PointF(f6, -3.96f * applyDimension));
        float f7 = -0.37f * applyDimension;
        arrayList.add(new PointF(f7, -4.31f * applyDimension));
        float f8 = -1.69f * applyDimension;
        arrayList.add(new PointF(f8, -3.29f * applyDimension));
        float f9 = -2.48f * applyDimension;
        arrayList.add(new PointF(f9, -1.23f * applyDimension));
        arrayList.add(new PointF(f9, 1.23f * applyDimension));
        arrayList.add(new PointF(f8, 3.29f * applyDimension));
        arrayList.add(new PointF(f7, 4.31f * applyDimension));
        arrayList.add(new PointF(f6, 3.96f * applyDimension));
        arrayList.add(new PointF(f5, 2.35f * applyDimension));
        arrayList.add(new PointF(applyDimension * 2.58f, f4));
    }

    /* access modifiers changed from: package-private */
    public int getStageCount() {
        return this.mFingerprintManager.getEnrollStageCount();
    }

    /* access modifiers changed from: package-private */
    public int getStageThresholdSteps(int i, int i2) {
        return Math.round(((float) i) * this.mFingerprintManager.getEnrollStageThreshold(i2));
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowProgressBar() {
        return this.mEnrollReason == 2;
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i) {
        if (this.mTotalSteps == -1) {
            this.mTotalSteps = i;
        }
        if (i != this.mRemainingSteps) {
            this.mLocationsEnrolled++;
            if (isCenterEnrollmentStage()) {
                this.mCenterTouchCount++;
            }
        }
        this.mRemainingSteps = i;
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onEnrollmentProgress(i, this.mTotalSteps);
        }
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentHelp() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onEnrollmentHelp(this.mRemainingSteps, this.mTotalSteps);
        }
    }

    /* access modifiers changed from: package-private */
    public void setListener(Listener listener) {
        int i;
        this.mListener = listener;
        if (listener != null && (i = this.mTotalSteps) != -1) {
            listener.onEnrollmentProgress(this.mRemainingSteps, i);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCenterEnrollmentStage() {
        int i;
        int i2 = this.mTotalSteps;
        if (i2 == -1 || (i = this.mRemainingSteps) == -1 || i2 - i < getStageThresholdSteps(i2, 0)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isGuidedEnrollmentStage() {
        int i;
        int i2;
        int i3;
        if (this.mAccessibilityEnabled || (i = this.mTotalSteps) == -1 || (i2 = this.mRemainingSteps) == -1 || (i3 = i - i2) < getStageThresholdSteps(i, 0) || i3 >= getStageThresholdSteps(this.mTotalSteps, 1)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isTipEnrollmentStage() {
        int i;
        int i2;
        int i3 = this.mTotalSteps;
        if (i3 == -1 || (i = this.mRemainingSteps) == -1 || (i2 = i3 - i) < getStageThresholdSteps(i3, 1) || i2 >= getStageThresholdSteps(this.mTotalSteps, 2)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isEdgeEnrollmentStage() {
        int i;
        int i2 = this.mTotalSteps;
        if (i2 == -1 || (i = this.mRemainingSteps) == -1 || i2 - i < getStageThresholdSteps(i2, 2)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public PointF getNextGuidedEnrollmentPoint() {
        if (this.mAccessibilityEnabled || !isGuidedEnrollmentStage()) {
            return new PointF(0.0f, 0.0f);
        }
        float f = 0.5f;
        if (Build.IS_ENG || Build.IS_USERDEBUG) {
            f = Settings.Secure.getFloatForUser(this.mContext.getContentResolver(), SCALE_OVERRIDE, 0.5f, -2);
        }
        List<PointF> list = this.mGuidedEnrollmentPoints;
        PointF pointF = list.get((this.mLocationsEnrolled - this.mCenterTouchCount) % list.size());
        return new PointF(pointF.x * f, pointF.y * f);
    }

    /* access modifiers changed from: package-private */
    public void animateIfLastStep() {
        Listener listener = this.mListener;
        if (listener == null) {
            Log.e(TAG, "animateIfLastStep, null listener");
            return;
        }
        int i = this.mRemainingSteps;
        if (i <= 2 && i >= 0) {
            listener.onLastStepAcquired();
        }
    }
}
