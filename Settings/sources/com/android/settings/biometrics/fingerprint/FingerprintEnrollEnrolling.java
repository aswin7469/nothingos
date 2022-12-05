package com.android.settings.biometrics.fingerprint;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.biometrics.BiometricsEnrollEnrolling;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.nt.settings.utils.NtUtils;
import java.util.List;
/* loaded from: classes.dex */
public class FingerprintEnrollEnrolling extends BiometricsEnrollEnrolling {
    private AccessibilityManager mAccessibilityManager;
    private boolean mAnimationCancelled;
    private boolean mCanAssumeUdfps;
    private boolean mEnrolled;
    private TextView mErrorText;
    private Interpolator mFastOutLinearInInterpolator;
    private Interpolator mFastOutSlowInInterpolator;
    private FingerprintManager mFingerprintManager;
    private LottieAnimationView mFingerprintProgress;
    private LinearLayout mGuideLayout;
    private AnimatedVectorDrawable mIconAnimationDrawable;
    private AnimatedVectorDrawable mIconBackgroundBlinksDrawable;
    private int mIconTouchCount;
    private boolean mIsAccessibilityEnabled;
    private boolean mIsSetupWizard;
    private Interpolator mLinearOutSlowInInterpolator;
    private TextView mNtDescriptionTextView;
    private TextView mNtHeaderTextView;
    private OrientationEventListener mOrientationEventListener;
    private ObjectAnimator mProgressAnim;
    private ProgressBar mProgressBar;
    private boolean mRestoring;
    private Vibrator mVibrator;
    private static final VibrationEffect VIBRATE_EFFECT_ERROR = VibrationEffect.createWaveform(new long[]{0, 5, 55, 60}, -1);
    private static final AudioAttributes FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    public static final AudioAttributes VIBRATION_SONIFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(11).build();
    private int mPreviousRotation = 0;
    private final Animator.AnimatorListener mProgressAnimationListener = new Animator.AnimatorListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.2
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            FingerprintEnrollEnrolling.this.playStartHaptic();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (FingerprintEnrollEnrolling.this.mFingerprintProgress.getProgress() > 0.997f) {
                ((BiometricEnrollBase) FingerprintEnrollEnrolling.this).mFooterBarMixin.getPrimaryButton().setVisibility(0);
                if (FingerprintEnrollEnrolling.this.mIsSetupWizard && !FingerprintEnrollEnrolling.this.checkEnrolledMaximumFingerprint()) {
                    ((BiometricEnrollBase) FingerprintEnrollEnrolling.this).mFooterBarMixin.getSecondaryButton().setVisibility(0);
                }
                FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
                fingerprintEnrollEnrolling.setHeaderText(fingerprintEnrollEnrolling.getString(R.string.nt_fp_enrolling_title_finished));
                FingerprintEnrollEnrolling fingerprintEnrollEnrolling2 = FingerprintEnrollEnrolling.this;
                fingerprintEnrollEnrolling2.setDescriptionText(fingerprintEnrollEnrolling2.getString(R.string.nt_fp_enrolling_summary_finished));
                FingerprintEnrollEnrolling.this.mEnrolled = true;
                FingerprintEnrollEnrolling.this.mFingerprintProgress.postDelayed(new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NtUtils.updateGestureMode(3);
                    }
                }, 200L);
            }
        }
    };
    private final Runnable mDelayedFinishRunnable = new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.3
        @Override // java.lang.Runnable
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.launchFinish(((BiometricEnrollBase) fingerprintEnrollEnrolling).mToken);
        }
    };
    private final Animatable2.AnimationCallback mIconAnimationCallback = new Animatable2.AnimationCallback() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.4
        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            if (FingerprintEnrollEnrolling.this.mAnimationCancelled) {
                return;
            }
            FingerprintEnrollEnrolling.this.mProgressBar.post(new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.4.1
                @Override // java.lang.Runnable
                public void run() {
                    FingerprintEnrollEnrolling.this.startIconAnimation();
                }
            });
        }
    };
    private final Runnable mShowDialogRunnable = new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.5
        @Override // java.lang.Runnable
        public void run() {
            FingerprintEnrollEnrolling.this.showIconTouchDialog();
        }
    };
    private final Runnable mTouchAgainRunnable = new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.6
        @Override // java.lang.Runnable
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.showError(fingerprintEnrollEnrolling.getString(R.string.security_settings_fingerprint_enroll_lift_touch_again));
        }
    };

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 240;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase, com.android.settings.core.InstrumentedActivity, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FingerprintManager.class);
        this.mFingerprintManager = fingerprintManager;
        List sensorPropertiesInternal = fingerprintManager.getSensorPropertiesInternal();
        boolean z = false;
        this.mCanAssumeUdfps = sensorPropertiesInternal.size() == 1 && ((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).isAnyUdfpsType();
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(AccessibilityManager.class);
        this.mAccessibilityManager = accessibilityManager;
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
        listenOrientationEvent();
        if (this.mCanAssumeUdfps) {
            if (BiometricUtils.isReverseLandscape(getApplicationContext())) {
                setContentView(R.layout.udfps_enroll_enrolling_land);
            } else {
                setContentView(R.layout.udfps_enroll_enrolling);
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setDisplayShowTitleEnabled(false);
                getActionBar().setBackgroundDrawable(new ColorDrawable(0));
                findViewById(R.id.sud_layout_header).setVisibility(8);
                this.mGuideLayout = (LinearLayout) findViewById(R.id.fingerprint_enrolling_guide);
                this.mNtHeaderTextView = (TextView) findViewById(R.id.fingerprint_enrolling_title);
                this.mNtDescriptionTextView = (TextView) findViewById(R.id.fingerprint_enrolling_summary);
            }
            setDescriptionText(R.string.security_settings_udfps_enroll_start_message);
        } else {
            setContentView(R.layout.fingerprint_enroll_enrolling);
            setDescriptionText(R.string.security_settings_fingerprint_enroll_start_message);
        }
        this.mIsSetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescriptionForUdfps();
        } else {
            setHeaderText(R.string.security_settings_fingerprint_enroll_repeat_title);
        }
        this.mErrorText = (TextView) findViewById(R.id.error_text);
        this.mVibrator = (Vibrator) getSystemService(Vibrator.class);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.fingerprint_enroll_progress);
        this.mFingerprintProgress = lottieAnimationView;
        lottieAnimationView.setAnimation("fp_register_anim.json");
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_fingerprint_enroll_done).setListener(new View.OnClickListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FingerprintEnrollEnrolling.this.onDoneButtonClick(view);
            }
        }).setButtonType(4).setTheme(R.style.SudGlifButton_NtPrimary).build());
        setFooterButtonTextColor(this.mFooterBarMixin.getPrimaryButtonView(), -16777216);
        this.mFooterBarMixin.getPrimaryButton().setVisibility(8);
        this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.fingerprint_enroll_button_add).setListener(new View.OnClickListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FingerprintEnrollEnrolling.this.onAddAnotherButtonClick(view);
            }
        }).setButtonType(5).setTheme(R.style.SudGlifButton_NtSecondary).build());
        setFooterButtonTextColor(this.mFooterBarMixin.getSecondaryButtonView(), -1);
        this.mFooterBarMixin.getSecondaryButton().setVisibility(8);
        ProgressBar progressBar = this.mProgressBar;
        LayerDrawable layerDrawable = progressBar != null ? (LayerDrawable) progressBar.getBackground() : null;
        if (layerDrawable != null) {
            this.mIconAnimationDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(R.id.fingerprint_animation);
            this.mIconBackgroundBlinksDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(R.id.fingerprint_background);
            this.mIconAnimationDrawable.registerAnimationCallback(this.mIconAnimationCallback);
        }
        this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563661);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(this, 17563663);
        ProgressBar progressBar2 = this.mProgressBar;
        if (progressBar2 != null) {
            progressBar2.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling$$ExternalSyntheticLambda2
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$onCreate$0;
                    lambda$onCreate$0 = FingerprintEnrollEnrolling.this.lambda$onCreate$0(view, motionEvent);
                    return lambda$onCreate$0;
                }
            });
        }
        if (bundle != null) {
            z = true;
        }
        this.mRestoring = z;
        if (z) {
            setResult(3);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            int i = this.mIconTouchCount + 1;
            this.mIconTouchCount = i;
            if (i == 3) {
                showIconTouchDialog();
            } else {
                this.mProgressBar.postDelayed(this.mShowDialogRunnable, 500L);
            }
        } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.mProgressBar.removeCallbacks(this.mShowDialogRunnable);
        }
        return true;
    }

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling
    protected BiometricEnrollSidecar getSidecar() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = new FingerprintEnrollSidecar();
        fingerprintEnrollSidecar.setEnrollReason(2);
        return fingerprintEnrollSidecar;
    }

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling
    protected boolean shouldStartAutomatically() {
        if (this.mCanAssumeUdfps) {
            return this.mRestoring;
        }
        return true;
    }

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        updateProgress(false);
        updateTitleAndDescription();
        if (this.mRestoring) {
            startIconAnimation();
        }
    }

    @Override // android.app.Activity
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (this.mCanAssumeUdfps) {
            startEnrollment();
        }
        this.mAnimationCancelled = false;
        startIconAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling, com.android.settings.biometrics.BiometricEnrollBase, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        stopIconAnimation();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        stopListenOrientationEvent();
        super.onDestroy();
    }

    private void animateFlash() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconBackgroundBlinksDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling
    protected Intent getFinishIntent() {
        return new Intent(this, FingerprintSettings.class);
    }

    private void updateTitleAndDescription() {
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescriptionForUdfps();
            return;
        }
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            setDescriptionText(R.string.security_settings_fingerprint_enroll_start_message);
        } else {
            setDescriptionText(R.string.security_settings_fingerprint_enroll_repeat_message);
        }
    }

    private void updateTitleAndDescriptionForUdfps() {
        int currentStage = getCurrentStage();
        if (currentStage == 0) {
            setHeaderText(R.string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R.string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage == 1) {
            setHeaderText(R.string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R.string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage == 2) {
            setHeaderText(R.string.nt_fp_enrolling_title_enrolling);
            setDescriptionText(R.string.nt_fp_enrolling_summary_enrolling);
        } else if (currentStage == 3) {
            setHeaderText(R.string.nt_fp_enrolling_title_enrolling_edge);
            setDescriptionText(R.string.nt_fp_enrolling_summary_enrolling_edge);
        } else {
            setHeaderText(R.string.nt_fp_enrolling_title_pre_enroll);
            setDescriptionText(R.string.nt_fp_enrolling_summary_pre_enroll);
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
        return enrollmentSteps < getStageThresholdSteps(2) ? 2 : 3;
    }

    private int getStageThresholdSteps(int i) {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            Log.w("FingerprintEnrollEnrolling", "getStageThresholdSteps: Enrollment not started yet");
            return 1;
        }
        return Math.round(this.mSidecar.getEnrollmentSteps() * this.mFingerprintManager.getEnrollStageThreshold(i));
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (!this.mCanAssumeUdfps) {
                this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            }
            if (i == 1001 || i == 1002 || i == 1003) {
                return;
            }
            this.mGuideLayout.setVisibility(8);
            showError(charSequence);
        }
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentError(int i, CharSequence charSequence) {
        FingerprintErrorDialog.showErrorDialog(this, i);
        stopIconAnimation();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
        }
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentProgressChange(int i, int i2) {
        updateProgress(true);
        updateTitleAndDescription();
        clearError();
        animateFlash();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            this.mErrorText.postDelayed(this.mTouchAgainRunnable, 2500L);
        } else if (!this.mIsAccessibilityEnabled) {
        } else {
            String string = getString(R.string.security_settings_udfps_enroll_progress_a11y_message, new Object[]{Integer.valueOf((int) (((i - i2) / i) * 100.0f))});
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
        if (biometricEnrollSidecar == null || !biometricEnrollSidecar.isEnrolling()) {
            Log.d("FingerprintEnrollEnrolling", "Enrollment not started yet");
            return;
        }
        this.mGuideLayout.setVisibility(8);
        int progress = getProgress(this.mSidecar.getEnrollmentSteps(), this.mSidecar.getEnrollmentRemaining());
        float f = 0.1751479f;
        if (this.mSidecar.getEnrollmentRemaining() != this.mSidecar.getEnrollmentSteps()) {
            f = this.mSidecar.getEnrollmentRemaining() == 0 ? 1.0f : 0.1751479f + ((this.mSidecar.getEnrollmentSteps() - this.mSidecar.getEnrollmentRemaining()) * 0.0394083f);
        }
        if (this.mSidecar.getEnrollmentRemaining() == 9) {
            f += 0.01f;
        }
        if (z) {
            animateFingerprintProgress(f);
            return;
        }
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
        this.mFingerprintProgress.setProgress(f);
    }

    private int getProgress(int i, int i2) {
        if (i == -1) {
            return 0;
        }
        int i3 = i + 1;
        return (Math.max(0, i3 - i2) * 10000) / i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showIconTouchDialog() {
        this.mIconTouchCount = 0;
        new IconTouchDialog().show(getSupportFragmentManager(), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(CharSequence charSequence) {
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.setText(charSequence);
            if (this.mErrorText.getVisibility() == 4) {
                this.mErrorText.setVisibility(0);
                this.mErrorText.setTranslationY(getResources().getDimensionPixelSize(R.dimen.fingerprint_error_text_appear_distance));
                this.mErrorText.setAlpha(0.0f);
                this.mErrorText.animate().alpha(1.0f).translationY(0.0f).setDuration(200L).setInterpolator(this.mLinearOutSlowInInterpolator).start();
            } else {
                this.mErrorText.animate().cancel();
                this.mErrorText.setAlpha(1.0f);
                this.mErrorText.setTranslationY(0.0f);
            }
        } else if (charSequence != null) {
            String[] split = charSequence.toString().split("\n");
            setHeaderText(split[0]);
            setDescriptionText(split.length > 1 ? split[1] : "");
        }
        if (isResumed()) {
            this.mVibrator.vibrate(VIBRATE_EFFECT_ERROR, FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
        }
    }

    private void clearError() {
        if (this.mCanAssumeUdfps || this.mErrorText.getVisibility() != 0) {
            return;
        }
        this.mErrorText.animate().alpha(0.0f).translationY(getResources().getDimensionPixelSize(R.dimen.fingerprint_error_text_disappear_distance)).setDuration(100L).setInterpolator(this.mFastOutLinearInInterpolator).withEndAction(new Runnable() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FingerprintEnrollEnrolling.this.lambda$clearError$1();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearError$1() {
        this.mErrorText.setVisibility(4);
    }

    private void listenOrientationEvent() {
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.1
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) {
                int rotation = FingerprintEnrollEnrolling.this.getDisplay().getRotation();
                if ((FingerprintEnrollEnrolling.this.mPreviousRotation == 1 && rotation == 3) || (FingerprintEnrollEnrolling.this.mPreviousRotation == 3 && rotation == 1)) {
                    FingerprintEnrollEnrolling.this.mPreviousRotation = rotation;
                    FingerprintEnrollEnrolling.this.recreate();
                }
            }
        };
        this.mOrientationEventListener = orientationEventListener;
        orientationEventListener.enable();
        this.mPreviousRotation = getDisplay().getRotation();
    }

    private void stopListenOrientationEvent() {
        OrientationEventListener orientationEventListener = this.mOrientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        this.mOrientationEventListener = null;
    }

    /* loaded from: classes.dex */
    public static class IconTouchDialog extends InstrumentedDialogFragment {
        @Override // com.android.settingslib.core.instrumentation.Instrumentable
        public int getMetricsCategory() {
            return 568;
        }

        @Override // androidx.fragment.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.security_settings_fingerprint_enroll_touch_dialog_title).setMessage(R.string.security_settings_fingerprint_enroll_touch_dialog_message).setPositiveButton(R.string.security_settings_fingerprint_enroll_dialog_ok, new DialogInterface.OnClickListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollEnrolling.IconTouchDialog.1
                @Override // android.content.DialogInterface.OnClickListener
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
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(lottieAnimationView, "progress", lottieAnimationView.getProgress(), f);
        ofFloat.addListener(this.mProgressAnimationListener);
        ofFloat.setDuration(500L);
        ofFloat.start();
        this.mProgressAnim = ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDoneButtonClick(View view) {
        setResult(1);
        finish();
        if (!this.mIsSetupWizard) {
            launchFinish(this.mToken);
        }
    }

    @Override // com.android.settings.biometrics.BiometricsEnrollEnrolling, androidx.activity.ComponentActivity, android.app.Activity
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
        if (this.mVibrator != null) {
            Log.d("FingerprintEnrollEnrolling", "playStartHaptic: enroll-succeed !");
            this.mVibrator.vibrate(Process.myUid(), getOpPackageName(), EFFECT_CLICK, "enroll-succeed", VIBRATION_SONIFICATION_ATTRIBUTES);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase
    public void setHeaderText(CharSequence charSequence) {
        TextView textView = this.mNtHeaderTextView;
        if (textView == null) {
            super.setHeaderText(charSequence);
        } else {
            textView.setText(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase
    public void setHeaderText(int i) {
        TextView textView = this.mNtHeaderTextView;
        if (textView == null) {
            super.setHeaderText(i);
        } else {
            textView.setText(getText(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase
    public void setDescriptionText(CharSequence charSequence) {
        TextView textView = this.mNtDescriptionTextView;
        if (textView == null) {
            super.setDescriptionText(charSequence);
        } else {
            textView.setText(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase
    public void setDescriptionText(int i) {
        TextView textView = this.mNtDescriptionTextView;
        if (textView == null) {
            super.setDescriptionText(i);
        } else {
            textView.setText(getText(i));
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
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
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddAnotherButtonClick(View view) {
        startActivity(new Intent("android.settings.BIOMETRIC_ENROLL"));
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkEnrolledMaximumFingerprint() {
        return this.mFingerprintManager.getEnrolledFingerprints(this.mUserId).size() >= ((FingerprintSensorPropertiesInternal) this.mFingerprintManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser;
    }
}
