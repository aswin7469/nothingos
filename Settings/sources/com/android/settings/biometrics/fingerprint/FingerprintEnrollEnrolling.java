package com.android.settings.biometrics.fingerprint;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.Process;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricsEnrollEnrolling;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.nothing.settings.security.SecurityUtils;
import java.util.List;

public class FingerprintEnrollEnrolling extends BiometricsEnrollEnrolling {
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    private static final VibrationAttributes FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES = VibrationAttributes.createForUsage(66);
    private static final VibrationEffect VIBRATE_EFFECT_ERROR = VibrationEffect.createWaveform(new long[]{0, 5, 55, 60}, -1);
    public static final AudioAttributes VIBRATION_SONIFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(11).build();
    private AccessibilityManager mAccessibilityManager;
    /* access modifiers changed from: private */
    public boolean mAnimationCancelled;
    private boolean mCanAssumeUdfps;
    private final Runnable mDelayedFinishRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.launchFinish(fingerprintEnrollEnrolling.mToken);
        }
    };
    /* access modifiers changed from: private */
    public boolean mEnrolled;
    private TextView mErrorText;
    private Interpolator mFastOutLinearInInterpolator;
    private Interpolator mFastOutSlowInInterpolator;
    private FingerprintManager mFingerprintManager;
    /* access modifiers changed from: private */
    public LottieAnimationView mFingerprintProgress;
    private LinearLayout mGuideLayout;
    private final Animatable2.AnimationCallback mIconAnimationCallback = new Animatable2.AnimationCallback() {
        public void onAnimationEnd(Drawable drawable) {
            if (!FingerprintEnrollEnrolling.this.mAnimationCancelled) {
                FingerprintEnrollEnrolling.this.mProgressBar.post(new Runnable() {
                    public void run() {
                        FingerprintEnrollEnrolling.this.startIconAnimation();
                    }
                });
            }
        }
    };
    private AnimatedVectorDrawable mIconAnimationDrawable;
    private AnimatedVectorDrawable mIconBackgroundBlinksDrawable;
    private int mIconTouchCount;
    private boolean mIsAccessibilityEnabled;
    /* access modifiers changed from: private */
    public boolean mIsSetupWizard;
    private Interpolator mLinearOutSlowInInterpolator;
    private TextView mNtDescriptionTextView;
    private TextView mNtHeaderTextView;
    private OrientationEventListener mOrientationEventListener;
    /* access modifiers changed from: private */
    public int mPreviousRotation = 0;
    private ObjectAnimator mProgressAnim;
    private final Animator.AnimatorListener mProgressAnimationListener = new Animator.AnimatorListener() {
        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
            FingerprintEnrollEnrolling.this.playStartHaptic();
        }

        public void onAnimationEnd(Animator animator) {
            if (FingerprintEnrollEnrolling.this.mFingerprintProgress.getProgress() > 0.997f) {
                FingerprintEnrollEnrolling.this.mFooterBarMixin.getPrimaryButton().setVisibility(0);
                if (FingerprintEnrollEnrolling.this.mIsSetupWizard && !FingerprintEnrollEnrolling.this.checkEnrolledMaximumFingerprint()) {
                    FingerprintEnrollEnrolling.this.mFooterBarMixin.getSecondaryButton().setVisibility(0);
                }
                FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
                fingerprintEnrollEnrolling.setHeaderText((CharSequence) fingerprintEnrollEnrolling.getString(R$string.nt_fp_enrolling_title_finished));
                FingerprintEnrollEnrolling fingerprintEnrollEnrolling2 = FingerprintEnrollEnrolling.this;
                fingerprintEnrollEnrolling2.setDescriptionText((CharSequence) fingerprintEnrollEnrolling2.getString(R$string.nt_fp_enrolling_summary_finished));
                FingerprintEnrollEnrolling.this.mEnrolled = true;
                FingerprintEnrollEnrolling.this.mFingerprintProgress.postDelayed(new Runnable() {
                    public void run() {
                        SecurityUtils.updateGestureMode(3);
                    }
                }, 200);
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressBar mProgressBar;
    private boolean mRestoring;
    private final Runnable mShowDialogRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling.this.showIconTouchDialog();
        }
    };
    private final Runnable mTouchAgainRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.showError(fingerprintEnrollEnrolling.getString(R$string.security_settings_fingerprint_enroll_lift_touch_again));
        }
    };
    private Vibrator mVibrator;

    public int getMetricsCategory() {
        return 240;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FingerprintManager.class);
        this.mFingerprintManager = fingerprintManager;
        List sensorPropertiesInternal = fingerprintManager.getSensorPropertiesInternal();
        boolean z = true;
        this.mCanAssumeUdfps = sensorPropertiesInternal.size() == 1 && ((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).isAnyUdfpsType();
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(AccessibilityManager.class);
        this.mAccessibilityManager = accessibilityManager;
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
        listenOrientationEvent();
        if (this.mCanAssumeUdfps) {
            setContentView(R$layout.udfps_enroll_enrolling);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setBackgroundDrawable(new ColorDrawable(0));
            findViewById(R$id.sud_layout_header).setVisibility(8);
            this.mGuideLayout = (LinearLayout) findViewById(R$id.fingerprint_enrolling_guide);
            this.mNtHeaderTextView = (TextView) findViewById(R$id.fingerprint_enrolling_title);
            this.mNtDescriptionTextView = (TextView) findViewById(R$id.fingerprint_enrolling_summary);
            Typeface create = Typeface.create("NDot57", 0);
            this.mNtHeaderTextView.setTypeface(create);
            ((TextView) findViewById(R$id.nt_fp_hold_to_start)).setTypeface(create);
            ((TextView) findViewById(R$id.nt_arrow)).setTypeface(create);
            setDescriptionText(R$string.security_settings_udfps_enroll_start_message);
        } else {
            setContentView(R$layout.fingerprint_enroll_enrolling);
            setDescriptionText(R$string.security_settings_fingerprint_enroll_start_message);
        }
        this.mIsSetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescriptionForUdfps();
        } else {
            setHeaderText(R$string.security_settings_fingerprint_enroll_repeat_title);
        }
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R$id.fingerprint_enroll_progress);
        this.mFingerprintProgress = lottieAnimationView;
        lottieAnimationView.setAnimation("fp_register_anim.json");
        this.mErrorText = (TextView) findViewById(R$id.error_text);
        this.mProgressBar = (ProgressBar) findViewById(R$id.fingerprint_progress_bar);
        this.mVibrator = (Vibrator) getSystemService(Vibrator.class);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(this.mIsSetupWizard ? R$string.fingerprint_enroll_button_next : R$string.security_settings_fingerprint_enroll_done).setListener(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda0(this)).setButtonType(4).setTheme(R$style.SudGlifButton_NtPrimary).build());
        setFooterButtonTextColor(this.mFooterBarMixin.getPrimaryButtonView(), -16777216);
        this.mFooterBarMixin.getPrimaryButton().setVisibility(8);
        this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R$string.fingerprint_enroll_button_add).setListener(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda1(this)).setButtonType(5).setTheme(R$style.SudGlifButton_SecondaryButton).build());
        setFooterButtonTextColor(this.mFooterBarMixin.getSecondaryButtonView(), -1);
        this.mFooterBarMixin.getSecondaryButton().setVisibility(8);
        ProgressBar progressBar = this.mProgressBar;
        LayerDrawable layerDrawable = progressBar != null ? (LayerDrawable) progressBar.getBackground() : null;
        if (layerDrawable != null) {
            this.mIconAnimationDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(R$id.fingerprint_animation);
            this.mIconBackgroundBlinksDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(R$id.fingerprint_background);
            this.mIconAnimationDrawable.registerAnimationCallback(this.mIconAnimationCallback);
        }
        this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563661);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(this, 17563663);
        ProgressBar progressBar2 = this.mProgressBar;
        if (progressBar2 != null) {
            progressBar2.setOnTouchListener(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda2(this));
        }
        if (bundle == null) {
            z = false;
        }
        this.mRestoring = z;
        if (z) {
            setResult(3);
            finish();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            int i = this.mIconTouchCount + 1;
            this.mIconTouchCount = i;
            if (i == 3) {
                showIconTouchDialog();
            } else {
                this.mProgressBar.postDelayed(this.mShowDialogRunnable, 500);
            }
        } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.mProgressBar.removeCallbacks(this.mShowDialogRunnable);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public BiometricEnrollSidecar getSidecar() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = new FingerprintEnrollSidecar();
        fingerprintEnrollSidecar.setEnrollReason(2);
        return fingerprintEnrollSidecar;
    }

    /* access modifiers changed from: protected */
    public boolean shouldStartAutomatically() {
        if (this.mCanAssumeUdfps) {
            return this.mRestoring;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        updateProgress(false);
        updateTitleAndDescription();
        if (this.mRestoring) {
            startIconAnimation();
        }
    }

    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (this.mCanAssumeUdfps) {
            startEnrollment();
        }
        this.mAnimationCancelled = false;
        startIconAnimation();
    }

    /* access modifiers changed from: private */
    public void startIconAnimation() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconAnimationDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    private void stopIconAnimation() {
        this.mAnimationCancelled = true;
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconAnimationDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.stop();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        stopIconAnimation();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        stopListenOrientationEvent();
        super.onDestroy();
    }

    private void animateFlash() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconBackgroundBlinksDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    /* access modifiers changed from: protected */
    public Intent getFinishIntent() {
        return new Intent(this, FingerprintSettings.class);
    }

    private void updateTitleAndDescription() {
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescriptionForUdfps();
            return;
        }
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            setDescriptionText(R$string.security_settings_fingerprint_enroll_start_message);
        } else {
            setDescriptionText(R$string.security_settings_fingerprint_enroll_repeat_message);
        }
    }

    private void updateTitleAndDescriptionForUdfps() {
        int currentStage = getCurrentStage();
        if (currentStage == 0) {
            setHeaderText(R$string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R$string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage == 1) {
            setHeaderText(R$string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R$string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage == 2) {
            setHeaderText(R$string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R$string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage != 3) {
            setHeaderText(R$string.nt_fp_enrolling_title_pre_enroll);
            setDescriptionText(R$string.nt_fp_enrolling_summary_pre_enroll);
        } else {
            setHeaderText(R$string.nt_fp_enrolling_title_enrolling_edge);
            setDescriptionText(R$string.nt_fp_enrolling_summary_enrolling_edge);
        }
    }

    private int getCurrentStage() {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            return -1;
        }
        int enrollmentSteps = this.mSidecar.getEnrollmentSteps() - this.mSidecar.getEnrollmentRemaining();
        if (enrollmentSteps < getStageThresholdSteps(0)) {
            return 0;
        }
        if (enrollmentSteps < getStageThresholdSteps(1)) {
            return 1;
        }
        if (enrollmentSteps < getStageThresholdSteps(2)) {
            return 2;
        }
        return 3;
    }

    private int getStageThresholdSteps(int i) {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar != null && biometricEnrollSidecar.getEnrollmentSteps() != -1) {
            return Math.round(((float) this.mSidecar.getEnrollmentSteps()) * this.mFingerprintManager.getEnrollStageThreshold(i));
        }
        Log.w("Security", "getStageThresholdSteps: Enrollment not started yet");
        return 1;
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (!this.mCanAssumeUdfps) {
                this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            }
            if (i != 1001 && i != 1002 && i != 1003) {
                this.mGuideLayout.setVisibility(8);
                showError(charSequence);
            }
        }
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        FingerprintErrorDialog.showErrorDialog(this, i);
        stopIconAnimation();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
        }
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        updateProgress(true);
        updateTitleAndDescription();
        clearError();
        animateFlash();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            this.mErrorText.postDelayed(this.mTouchAgainRunnable, 2500);
        } else if (this.mIsAccessibilityEnabled) {
            int i3 = (int) ((((float) (i - i2)) / ((float) i)) * 100.0f);
            String string = getString(R$string.security_settings_udfps_enroll_progress_a11y_message, new Object[]{Integer.valueOf(i3)});
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(16384);
            obtain.setClassName(getClass().getName());
            obtain.setPackageName(getPackageName());
            obtain.getText().add(string);
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    private void updateProgress(boolean z) {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        boolean z2 = true;
        if (biometricEnrollSidecar == null || !biometricEnrollSidecar.isEnrolling()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Enrollment not started yet mSidecar == null:");
            if (this.mSidecar != null) {
                z2 = false;
            }
            sb.append(z2);
            Log.d("Security", sb.toString());
            return;
        }
        this.mGuideLayout.setVisibility(8);
        int progress = getProgress(this.mSidecar.getEnrollmentSteps(), this.mSidecar.getEnrollmentRemaining());
        int enrollmentSteps = this.mSidecar.getEnrollmentSteps() - this.mSidecar.getEnrollmentRemaining();
        int stageThresholdSteps = getStageThresholdSteps(2);
        float f = 0.404083f / ((float) stageThresholdSteps);
        float enrollmentSteps2 = 0.4207691f / ((float) ((this.mSidecar.getEnrollmentSteps() - stageThresholdSteps) + 1));
        float f2 = 0.1751479f;
        if (enrollmentSteps > stageThresholdSteps) {
            f2 = 0.5792309f + (enrollmentSteps2 * ((float) (enrollmentSteps - stageThresholdSteps)));
        } else if (enrollmentSteps != 0) {
            f2 = 0.1751479f + (((float) enrollmentSteps) * f);
        }
        float f3 = 1.0f;
        float min = Math.min(Math.max(0.0f, f2), 1.0f);
        if (this.mSidecar.getEnrollmentRemaining() != 0) {
            f3 = min;
        }
        if (z) {
            animateFingerprintProgress(f3);
            return;
        }
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
        this.mFingerprintProgress.setProgress(f3);
    }

    private int getProgress(int i, int i2) {
        if (i == -1) {
            return 0;
        }
        int i3 = i + 1;
        return (Math.max(0, i3 - i2) * 10000) / i3;
    }

    /* access modifiers changed from: private */
    public void showIconTouchDialog() {
        this.mIconTouchCount = 0;
        new IconTouchDialog().show(getSupportFragmentManager(), (String) null);
    }

    /* access modifiers changed from: private */
    public void showError(CharSequence charSequence) {
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.setText(charSequence);
            if (this.mErrorText.getVisibility() == 4) {
                this.mErrorText.setVisibility(0);
                this.mErrorText.setTranslationY((float) getResources().getDimensionPixelSize(R$dimen.fingerprint_error_text_appear_distance));
                this.mErrorText.setAlpha(0.0f);
                this.mErrorText.animate().alpha(1.0f).translationY(0.0f).setDuration(200).setInterpolator(this.mLinearOutSlowInInterpolator).start();
            } else {
                this.mErrorText.animate().cancel();
                this.mErrorText.setAlpha(1.0f);
                this.mErrorText.setTranslationY(0.0f);
            }
        } else if (charSequence != null) {
            String[] split = charSequence.toString().split("\n");
            setHeaderText((CharSequence) split[0]);
            setDescriptionText((CharSequence) split.length > 1 ? split[1] : "");
        }
        if (isResumed()) {
            this.mVibrator.vibrate(VIBRATE_EFFECT_ERROR, FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
        }
    }

    private void clearError() {
        if (!this.mCanAssumeUdfps && this.mErrorText.getVisibility() == 0) {
            this.mErrorText.animate().alpha(0.0f).translationY((float) getResources().getDimensionPixelSize(R$dimen.fingerprint_error_text_disappear_distance)).setDuration(100).setInterpolator(this.mFastOutLinearInInterpolator).withEndAction(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda3(this)).start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$clearError$1() {
        this.mErrorText.setVisibility(4);
    }

    private void listenOrientationEvent() {
        C07281 r0 = new OrientationEventListener(this) {
            public void onOrientationChanged(int i) {
                int rotation = FingerprintEnrollEnrolling.this.getDisplay().getRotation();
                if ((FingerprintEnrollEnrolling.this.mPreviousRotation == 1 && rotation == 3) || (FingerprintEnrollEnrolling.this.mPreviousRotation == 3 && rotation == 1)) {
                    FingerprintEnrollEnrolling.this.mPreviousRotation = rotation;
                    FingerprintEnrollEnrolling.this.recreate();
                }
            }
        };
        this.mOrientationEventListener = r0;
        r0.enable();
        this.mPreviousRotation = getDisplay().getRotation();
    }

    private void stopListenOrientationEvent() {
        OrientationEventListener orientationEventListener = this.mOrientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        this.mOrientationEventListener = null;
    }

    public static class IconTouchDialog extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 568;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R$string.security_settings_fingerprint_enroll_touch_dialog_title).setMessage(R$string.security_settings_fingerprint_enroll_touch_dialog_message).setPositiveButton(R$string.security_settings_fingerprint_enroll_dialog_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            return builder.create();
        }
    }

    private void animateFingerprintProgress(float f) {
        ObjectAnimator objectAnimator = this.mProgressAnim;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        LottieAnimationView lottieAnimationView = this.mFingerprintProgress;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(lottieAnimationView, "progress", new float[]{lottieAnimationView.getProgress(), f});
        ofFloat.addListener(this.mProgressAnimationListener);
        ofFloat.setDuration(500);
        ofFloat.start();
        this.mProgressAnim = ofFloat;
    }

    public void onDoneButtonClick(View view) {
        setResult(1);
        finish();
        if (!this.mIsSetupWizard) {
            launchFinish(this.mToken);
        }
    }

    public void onBackPressed() {
        if (this.mEnrolled) {
            if (this.mIsSetupWizard) {
                setResult(1);
            } else {
                launchFinish(this.mToken);
            }
        }
        super.onBackPressed();
    }

    public void playStartHaptic() {
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.vibrate(Process.myUid(), getOpPackageName(), EFFECT_CLICK, "enroll-succeed", FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
        }
    }

    public void setHeaderText(CharSequence charSequence) {
        TextView textView = this.mNtHeaderTextView;
        if (textView == null) {
            super.setHeaderText(charSequence);
        } else {
            textView.setText(charSequence);
        }
    }

    public void setHeaderText(int i) {
        TextView textView = this.mNtHeaderTextView;
        if (textView == null) {
            super.setHeaderText(i);
        } else {
            textView.setText(getText(i));
        }
    }

    public void setDescriptionText(CharSequence charSequence) {
        TextView textView = this.mNtDescriptionTextView;
        if (textView == null) {
            super.setDescriptionText(charSequence);
        } else {
            textView.setText(charSequence);
        }
    }

    public void setDescriptionText(int i) {
        TextView textView = this.mNtDescriptionTextView;
        if (textView == null) {
            super.setDescriptionText(i);
        } else {
            textView.setText(getText(i));
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        cancelEnrollment();
        if (this.mEnrolled) {
            if (this.mIsSetupWizard) {
                setResult(1);
            } else {
                launchFinish(this.mToken);
            }
        }
        finish();
        return true;
    }

    public void onAddAnotherButtonClick(View view) {
        startActivity(new Intent("android.settings.BIOMETRIC_ENROLL"));
        finish();
    }

    public boolean checkEnrolledMaximumFingerprint() {
        if (this.mFingerprintManager.getEnrolledFingerprints(this.mUserId).size() >= ((FingerprintSensorPropertiesInternal) this.mFingerprintManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser) {
            return true;
        }
        return false;
    }
}
