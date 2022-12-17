package com.nothing.settings.face;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.Vibrator;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$raw;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.Settings;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settingslib.Utils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.nothing.settings.face.AutoFitCameraPreviewTextureView;
import com.p007nt.facerecognition.manager.CameraPreviewProperty;

public class FaceRecognitionActivity extends CustomSettingsBaseActivity {
    private final int ANIMA_CIRCLE_LINE_WIDTH_IN_PIXEL = 15;
    /* access modifiers changed from: private */
    public View mAddFaceDataLayout;
    /* access modifiers changed from: private */
    public View mAddFaceNoticeLayout;
    /* access modifiers changed from: private */
    public CameraLoadingTextView mCameraLoadingView;
    /* access modifiers changed from: private */
    public AutoFitCameraPreviewTextureView mCameraPreviewTexture;
    /* access modifiers changed from: private */
    public Surface mCameraPreviewTextureSurface = null;
    /* access modifiers changed from: private */
    public CirclePreviewView mCirclePreviewView;
    private boolean mConfirmingCredentials;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Display mDisplay;
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public void onDisplayChanged(int i) {
            int state = FaceRecognitionActivity.this.mDisplayManager.getDisplay(0).getState();
            if (FaceRecognitionActivity.this.mDisplay.getDisplayId() == i) {
                FaceRecognitionActivity.this.mDisplayState = state;
                if (state == 1) {
                    FaceRecognitionActivity.this.mUiHandler.removeMessages(12);
                    FaceRecognitionActivity.this.mUiHandler.sendEmptyMessage(12);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public DisplayManager mDisplayManager;
    /* access modifiers changed from: private */
    public int mDisplayState = 2;
    private TextView mEnrollArrow;
    private TextView mFaceAddNoticeAlwaysView;
    /* access modifiers changed from: private */
    public TextView mFaceAddNoticeView;
    /* access modifiers changed from: private */
    public IFaceEnroll mFaceEnroll;
    private AlertDialog mFaceRecognitionErrorDialog;
    LottieAnimationView mFingerprintProgress;
    LottieAnimationView mFingerprintProgressChecked;
    /* access modifiers changed from: private */
    public FooterBarMixin mFooterBarMixin;
    private final View.OnClickListener mFooterButtonClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!FaceRecognitionActivity.this.mIsFinishAddingFaceData) {
                FaceRecognitionActivity.this.mUiHandler.obtainMessage(1).sendToTarget();
            } else {
                FaceRecognitionActivity.this.handleCancelAddingFace();
            }
        }
    };
    private boolean mFromFaceSettings;
    /* access modifiers changed from: private */
    public boolean mIsAddingFaceData;
    private boolean mIsFaceRegisterSuccessStep;
    /* access modifiers changed from: private */
    public boolean mIsFinishAddingFaceData;
    /* access modifiers changed from: private */
    public Runnable mRunTimeout = new Runnable() {
        public void run() {
            FaceRecognitionActivity.this.getWindow().clearFlags(128);
            if (!FaceRecognitionActivity.this.mIsFinishAddingFaceData) {
                FaceRecognitionActivity.this.cancelCreateFaceState();
                FaceRecognitionActivity.this.mFaceEnroll.endEnroll();
                FaceRecognitionActivity.this.showFaceRecognitionTimeoutDialog();
            }
        }
    };
    private long mStartPauseTime = -1;
    /* access modifiers changed from: private */
    public Handler mUiHandler = new Handler() {
        public void handleMessage(Message message) {
            Log.d("Face", "FaceRecognitionActivity::handleMessage :" + message.what);
            switch (message.what) {
                case 1:
                    Log.d("Face", "MSG_SET_CAMERA_LOADING_VIEW_GONE");
                    FaceRecognitionActivity faceRecognitionActivity = FaceRecognitionActivity.this;
                    faceRecognitionActivity.startViewVisibilityAnimation(faceRecognitionActivity.mAddFaceNoticeLayout, FaceRecognitionActivity.this.mAddFaceDataLayout, true);
                    FaceRecognitionActivity.this.mFooterBarMixin.getPrimaryButton().setVisibility(8);
                    FaceRecognitionActivity.this.mCameraLoadingView.setCircleProperties(FaceRecognitionActivity.this.mCameraPreviewTexture.getCircleProperties());
                    FaceRecognitionActivity.this.mCameraLoadingView.setCircleOutlineProvider();
                    FaceRecognitionActivity.this.mCameraPreviewTexture.setCircleOutlineProvider();
                    FaceRecognitionActivity.this.mCirclePreviewView.setCircleProperties(FaceRecognitionActivity.this.mCameraPreviewTexture.getCircleProperties());
                    FaceRecognitionActivity.this.mFaceAddNoticeView.setVisibility(0);
                    FaceRecognitionActivity.this.adjustFaceAddedAnimationViewPosition();
                    return;
                case 2:
                    FaceRecognitionActivity.this.mIsAddingFaceData = true;
                    FaceRecognitionActivity.this.mUiHandler.removeCallbacks(FaceRecognitionActivity.this.mRunTimeout);
                    FaceRecognitionActivity.this.mUiHandler.postDelayed(FaceRecognitionActivity.this.mRunTimeout, 30000);
                    FaceRecognitionActivity.this.getWindow().addFlags(128);
                    FaceRecognitionActivity.this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
                    FaceRecognitionActivity.this.mUiHandler.removeMessages(11);
                    FaceRecognitionActivity.this.mUiHandler.sendEmptyMessageDelayed(11, 100);
                    return;
                case 3:
                    Log.d("Face", "MSG_SET_CAMERA_PREVIEW_VIEW_GONE");
                    return;
                case 4:
                    Log.d("Face", "MSG_SET_FACE_SAVE_FEATURE_NOTICE_TEXT_VIEW");
                    FaceRecognitionActivity.this.mFaceAddNoticeView.setText((CharSequence) message.obj);
                    FaceUtils.startShakeAnim(FaceRecognitionActivity.this.mFaceAddNoticeView);
                    FaceRecognitionActivity.this.mVibrator.vibrate(20);
                    return;
                case 5:
                    FaceRecognitionActivity.this.mCameraLoadingView.setText((CharSequence) message.obj);
                    return;
                case 7:
                    int i = message.arg1;
                    String charSequence = ((CharSequence) message.obj).toString();
                    FaceRecognitionActivity.this.mFaceAddNoticeView.setText(charSequence);
                    Log.d("Face", "MSG_UPDATE_SAVE_FEATURE_PROGRESS progress:" + i + " text:" + charSequence);
                    FaceRecognitionActivity.this.mCirclePreviewView.setProgress(i);
                    return;
                case 10:
                    Log.d("Face", "MSG_CAMERA_PREVIEW_STARTED_SUCCESS :" + message.arg1);
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        FaceRecognitionActivity.this.showCameraOpenFailedDialog();
                        return;
                    } else if (i2 == 1) {
                        FaceRecognitionActivity.this.mUiHandler.postDelayed(new Runnable() {
                            public void run() {
                                FaceRecognitionActivity.this.mCameraLoadingView.setVisibility(8);
                            }
                        }, 400);
                        CameraPreviewProperty cameraPreviewProperty = (CameraPreviewProperty) message.obj;
                        if (cameraPreviewProperty != null) {
                            Log.d("Face", "cameraPreviewProperty getCameraWidth:" + cameraPreviewProperty.getCameraWidth() + ",getCameraHeight:" + cameraPreviewProperty.getCameraHeight());
                        }
                        if (FaceRecognitionActivity.this.mIsAddingFaceData) {
                            FaceRecognitionActivity.this.mCameraPreviewTexture.setAspectRatio();
                            FaceRecognitionActivity.this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
                            Log.d("Face", "MSG_CAMERA_PREVIEW_STARTED_SUCCESS startEnroll");
                            FaceRecognitionActivity.this.mUiHandler.removeCallbacks(FaceRecognitionActivity.this.mRunTimeout);
                            FaceRecognitionActivity.this.mUiHandler.postDelayed(FaceRecognitionActivity.this.mRunTimeout, 30000);
                            FaceRecognitionActivity.this.getWindow().addFlags(128);
                            FaceRecognitionActivity.this.mUiHandler.removeMessages(11);
                            FaceRecognitionActivity.this.mUiHandler.sendEmptyMessageDelayed(11, 100);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                case 11:
                    Log.d("Face", "startEnroll");
                    FaceRecognitionActivity.this.mFaceEnroll.startEnroll(FaceRecognitionActivity.this.mUiHandler, new byte[0], FaceRecognitionActivity.this.mCirclePreviewView.getMaskRect(FaceRecognitionActivity.this.mCameraPreviewTexture.getPreviewScaleX(), FaceRecognitionActivity.this.mCameraPreviewTexture.getPreviewScaleY()));
                    return;
                case 12:
                    Log.d("Face", "MSG_SCREEN_OFF :" + message.arg1);
                    if (!FaceUtils.isFaceDataExist(FaceRecognitionActivity.this.mContext) && !FaceRecognitionActivity.this.mIsFinishAddingFaceData) {
                        FaceRecognitionActivity.this.cancelCreateFaceState();
                    }
                    FaceRecognitionActivity.this.finish();
                    return;
                case 13:
                    FaceRecognitionActivity.this.addAnimationListener();
                    FaceRecognitionActivity.this.mFingerprintProgress.playAnimation();
                    FaceRecognitionActivity.this.mUiHandler.sendEmptyMessageDelayed(3, 1500);
                    FaceRecognitionActivity.this.setResult(2);
                    FaceRecognitionActivity.this.handleFinishAddingFaceData();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Vibrator mVibrator;

    /* access modifiers changed from: private */
    public void adjustFaceAddedAnimationViewPosition() {
        int width = this.mCameraPreviewTexture.getCircleProperties().getMarginLayoutParams().leftMargin + ((this.mCameraPreviewTexture.getWidth() - this.mFingerprintProgress.getWidth()) / 2);
        int height = this.mCameraPreviewTexture.getCircleProperties().getMarginLayoutParams().topMargin + ((this.mCameraPreviewTexture.getHeight() - this.mFingerprintProgress.getHeight()) / 2);
        ((ViewGroup.MarginLayoutParams) this.mFingerprintProgress.getLayoutParams()).setMargins(width, height, width, height);
        ((ViewGroup.MarginLayoutParams) this.mFingerprintProgressChecked.getLayoutParams()).setMargins(width, height, width, height);
    }

    private void hidePreviewCircle() {
        Log.d("Face", "onAnimationUpdate really hide: ");
        this.mCameraPreviewTexture.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void handleFaceAdded() {
        AlertDialog alertDialog = this.mFaceRecognitionErrorDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        IFaceEnroll iFaceEnroll = this.mFaceEnroll;
        if (iFaceEnroll != null) {
            iFaceEnroll.endEnroll();
            this.mFaceEnroll.closeHardwareDevice();
            Surface surface = this.mCameraPreviewTextureSurface;
            if (surface != null) {
                surface.destroy();
                this.mCameraPreviewTextureSurface = null;
            }
        }
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        hidePreviewCircle();
        showFaceAddedTitle();
        showFaceAddedSummary();
        this.mFooterBarMixin.getPrimaryButton().setVisibility(0);
        this.mFooterBarMixin.getPrimaryButton().setText(getResources().getString(R$string.nt_face_enroll_success_done));
        this.mIsAddingFaceData = false;
        this.mIsFaceRegisterSuccessStep = true;
        getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void showFaceAddedTitle() {
        this.mFaceAddNoticeAlwaysView.setVisibility(0);
        this.mFaceAddNoticeAlwaysView.setText(R$string.nt_face_enroll_success_title);
    }

    private void showFaceAddedSummary() {
        this.mFaceAddNoticeView.setVisibility(0);
        this.mFaceAddNoticeView.setText(R$string.nt_face_enroll_success_summary);
    }

    /* access modifiers changed from: private */
    public void addCheckedAnimationListener() {
        LottieAnimationView lottieAnimationView = this.mFingerprintProgressChecked;
        if (lottieAnimationView != null) {
            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    FaceRecognitionActivity.this.handleFaceAdded();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void addAnimationListener() {
        LottieAnimationView lottieAnimationView = this.mFingerprintProgress;
        if (lottieAnimationView != null) {
            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    FaceRecognitionActivity.this.addCheckedAnimationListener();
                    FaceRecognitionActivity.this.handleFaceAdded();
                    FaceRecognitionActivity.this.mFingerprintProgressChecked.setVisibility(0);
                    FaceRecognitionActivity.this.mFingerprintProgressChecked.playAnimation();
                }
            });
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R$style.Theme_SetupWizardOverlay);
        boolean booleanExtra = getIntent().getBooleanExtra("from_face_settings", false);
        this.mFromFaceSettings = booleanExtra;
        if (!booleanExtra && !this.mConfirmingCredentials) {
            this.mConfirmingCredentials = true;
            launchChooseOrConfirmLock();
        }
        setContentView(R$layout.nt_face_recoginition_layout);
        findViewById(R$id.sud_layout_header).setVisibility(8);
        GlifLayout glifLayout = (GlifLayout) findViewById(R$id.setup_wizard_layout);
        glifLayout.setBackgroundColor(getBackgroundColor());
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R$string.nt_setup).setButtonType(6).setListener(this.mFooterButtonClickListener).setTheme(R$style.SudGlifButton_NtPrimary).build());
        this.mFooterBarMixin.getButtonContainer().setBackgroundColor(0);
        Context applicationContext = getApplicationContext();
        this.mContext = applicationContext;
        startBootFaceRecognitionService(applicationContext);
        initValues();
        initViews();
        Log.d("Face", "FaceRecognitionActivity::onCreate ");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.d("Face", "FaceRecognitionActivity::onResume mStartPauseTime 1:" + this.mStartPauseTime);
        if (this.mIsAddingFaceData) {
            this.mCameraPreviewTexture.setAspectRatio();
            final SurfaceTexture surfaceTexture = this.mCameraPreviewTexture.getSurfaceTexture();
            this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
            this.mUiHandler.removeCallbacks(this.mRunTimeout);
            this.mCirclePreviewView.setProgress(0);
            this.mUiHandler.postDelayed(new Runnable() {
                public void run() {
                    if (FaceRecognitionActivity.this.mFaceEnroll != null) {
                        if (FaceRecognitionActivity.this.mCameraPreviewTextureSurface == null) {
                            FaceRecognitionActivity.this.mCameraPreviewTextureSurface = new Surface(surfaceTexture);
                        }
                        FaceRecognitionActivity.this.mFaceEnroll.warmUpHardwareDeviceForPreview(FaceRecognitionActivity.this.mCameraPreviewTextureSurface, FaceRecognitionActivity.this.mUiHandler, FaceRecognitionActivity.this.mCameraPreviewTexture.getWidth(), FaceRecognitionActivity.this.mCameraPreviewTexture.getHeight());
                        FaceRecognitionActivity.this.mUiHandler.removeMessages(11);
                        FaceRecognitionActivity.this.mUiHandler.sendEmptyMessage(11);
                    }
                }
            }, 100);
            this.mUiHandler.postDelayed(this.mRunTimeout, 30000);
            getWindow().addFlags(128);
        }
        Log.d("Face", "onResume mStartPauseTime 2:" + this.mStartPauseTime);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("finish_add_face", this.mIsFinishAddingFaceData);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mIsFinishAddingFaceData = bundle.getBoolean("finish_add_face");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.mIsAddingFaceData) {
            this.mUiHandler.removeCallbacks(this.mRunTimeout);
            this.mFaceEnroll.endEnroll();
            this.mIsAddingFaceData = false;
            showFailedDialog();
        }
        this.mStartPauseTime = System.currentTimeMillis();
        IFaceEnroll iFaceEnroll = this.mFaceEnroll;
        if (iFaceEnroll != null) {
            iFaceEnroll.closeHardwareDevice();
            Surface surface = this.mCameraPreviewTextureSurface;
            if (surface != null) {
                surface.destroy();
                this.mCameraPreviewTextureSurface = null;
            }
            Log.d("Face", "onPause destroy surface");
        }
        this.mStartPauseTime = System.currentTimeMillis();
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        Log.d("Face", "FaceRecognitionActivity::onPause mStartPauseTime:" + this.mStartPauseTime);
    }

    private void showFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R$string.security_settings_face_enroll_error_dialog_title);
        builder.setMessage(R$string.security_settings_face_enroll_error_generic_dialog_message);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                FaceRecognitionActivity.this.finish();
            }
        });
        create.show();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public void onBackPressed() {
        handleCancelAddingFace();
        Log.d("Face", "FaceRecognitionActivity::onBackPressed");
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 3) {
            if (!this.mIsFinishAddingFaceData) {
                cancelCreateFaceState();
                setResult(0);
            }
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mFaceEnroll = null;
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        Log.d("Face", "onDestroy");
    }

    private void initValues() {
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mFaceEnroll = new FaceEnrollImpl(this.mContext, this.mUiHandler);
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager = displayManager;
        this.mDisplay = displayManager.getDisplay(0);
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mUiHandler);
    }

    private void initViews() {
        this.mAddFaceNoticeLayout = findViewById(R$id.face_notice_layout);
        this.mAddFaceDataLayout = findViewById(R$id.face_camerapreview_layout);
        this.mCameraLoadingView = (CameraLoadingTextView) findViewById(R$id.camera_preview_loading_text);
        AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = (AutoFitCameraPreviewTextureView) findViewById(R$id.textureview_camera_preview);
        this.mCameraPreviewTexture = autoFitCameraPreviewTextureView;
        autoFitCameraPreviewTextureView.setSurfaceTextureAvailableListener(new AutoFitCameraPreviewTextureView.OnSurfaceTextureAvailableListener() {
            public void onSurfaceTextureAvailableCallback(SurfaceTexture surfaceTexture, int i, int i2) {
                Log.d("Face", "onSurfaceTextureAvailable width:" + i + ",height:" + i2 + "");
                if (surfaceTexture != null) {
                    SurfaceTexture surfaceTexture2 = FaceRecognitionActivity.this.mCameraPreviewTexture.getSurfaceTexture();
                    if (FaceRecognitionActivity.this.mCameraPreviewTextureSurface == null) {
                        FaceRecognitionActivity.this.mCameraPreviewTextureSurface = new Surface(surfaceTexture2);
                    }
                    FaceRecognitionActivity.this.mFaceEnroll.warmUpHardwareDeviceForPreview(FaceRecognitionActivity.this.mCameraPreviewTextureSurface, FaceRecognitionActivity.this.mUiHandler, i, i2);
                    return;
                }
                Log.e("Face", "onSurfaceTextureAvailable texture == null");
            }
        });
        CirclePreviewView circlePreviewView = (CirclePreviewView) findViewById(R$id.circle_preview_mark);
        this.mCirclePreviewView = circlePreviewView;
        circlePreviewView.setVisibility(8);
        this.mFaceAddNoticeAlwaysView = (TextView) findViewById(R$id.face_second_step_add_notice_always);
        this.mFaceAddNoticeView = (TextView) findViewById(R$id.face_second_step_add_notice);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R$id.face_enroll_success);
        this.mFingerprintProgress = lottieAnimationView;
        lottieAnimationView.setAnimation(R$raw.face_enroll_anim_circle);
        this.mFingerprintProgress.cancelAnimation();
        LottieAnimationView lottieAnimationView2 = (LottieAnimationView) findViewById(R$id.face_enroll_success_checked);
        this.mFingerprintProgressChecked = lottieAnimationView2;
        lottieAnimationView2.setVisibility(8);
        this.mFingerprintProgressChecked.setAnimation(R$raw.face_enroll_anim_checked);
        this.mFingerprintProgressChecked.cancelAnimation();
        this.mEnrollArrow = (TextView) findViewById(R$id.face_enroll_arrow);
        Typeface create = Typeface.create("NDot57", 0);
        this.mFaceAddNoticeAlwaysView.setTypeface(create);
        this.mEnrollArrow.setTypeface(create);
        ((TextView) findViewById(R$id.introduction_title)).setTypeface(create);
    }

    public void handleCancelAddingFace() {
        if (!this.mIsFaceRegisterSuccessStep && !this.mIsAddingFaceData && this.mIsFinishAddingFaceData && !this.mFromFaceSettings) {
            startFaceSettingFragment();
        }
        finish();
    }

    public void cancelCreateFaceState() {
        this.mIsFinishAddingFaceData = false;
        IFaceEnroll iFaceEnroll = this.mFaceEnroll;
        if (iFaceEnroll != null) {
            iFaceEnroll.endEnroll();
            this.mFaceEnroll.closeHardwareDevice();
            Surface surface = this.mCameraPreviewTextureSurface;
            if (surface != null) {
                surface.destroy();
                this.mCameraPreviewTextureSurface = null;
            }
        }
        this.mIsAddingFaceData = false;
        this.mIsFaceRegisterSuccessStep = false;
    }

    public void showCameraOpenFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R$string.nt_face_enroll_camera_exception);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                FaceRecognitionActivity.this.finish();
            }
        });
        create.show();
    }

    public void showFaceRecognitionTimeoutDialog() {
        if (!this.mIsFaceRegisterSuccessStep) {
            if (this.mFaceRecognitionErrorDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R$string.nt_security_settings_face_enroll_error_dialog_title);
                builder.setMessage(R$string.nt_security_settings_face_enroll_error_timeout_dialog_message);
                builder.setPositiveButton(R$string.nt_face_enroll_success_done, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FaceRecognitionActivity.this.finish();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        FaceRecognitionActivity.this.mIsAddingFaceData = false;
                        if (FaceRecognitionActivity.this.mCirclePreviewView != null) {
                            FaceRecognitionActivity.this.mCirclePreviewView.setProgress(0);
                        }
                        FaceRecognitionActivity.this.finish();
                    }
                });
                AlertDialog create = builder.create();
                this.mFaceRecognitionErrorDialog = create;
                create.setCanceledOnTouchOutside(false);
            }
            if (!isFinishing()) {
                this.mFaceRecognitionErrorDialog.show();
            }
        }
    }

    public void startViewVisibilityAnimation(View view, final View view2, final boolean z) {
        if (view != null && view2 != null) {
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            TranslateAnimation translateAnimation = new TranslateAnimation((float) point.x, 0.0f, 0.0f, 0.0f);
            translateAnimation.setDuration(200);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (z) {
                        FaceRecognitionActivity.this.mUiHandler.sendEmptyMessage(2);
                    }
                    view2.clearAnimation();
                }
            });
            view.setVisibility(8);
            view2.setVisibility(0);
            view2.startAnimation(translateAnimation);
        }
    }

    public void handleFinishAddingFaceData() {
        if (this.mDisplayState == 2) {
            this.mIsFinishAddingFaceData = true;
            this.mIsAddingFaceData = false;
            this.mIsFaceRegisterSuccessStep = false;
            Context context = this.mContext;
            FaceUtils.setFaceDataCount(context, FaceUtils.getFaceDataCount(context) + 1);
            Log.d("Face", "add face success!");
        }
    }

    private void startFaceSettingFragment() {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", Settings.FaceSettingsActivity.class.getName());
        intent.putExtra("from_create_face_settings", true);
        startActivity(intent);
    }

    private void startBootFaceRecognitionService(final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public final void run() {
                Slog.i("Face", "startFaceBootReceiverService");
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.nt.facerecognition", "com.nt.facerecognition.server.BootFaceRecognitionService"));
                intent.putExtra("start_app", "com.android.settings");
                context.startForegroundService(intent);
            }
        });
    }

    private void launchChooseOrConfirmLock() {
        Intent intent = new Intent();
        if (!new ChooseLockSettingsHelper.Builder(this, (Fragment) null).setRequestCode(4).setTitle(getString(R$string.security_settings_fingerprint_preference_title)).setRequestGatekeeperPasswordHandle(true).setUserId(UserHandle.myUserId()).setForegroundOnly(true).setReturnCredentials(true).show()) {
            intent.setClassName("com.android.settings", ChooseLockGeneric.class.getName());
            intent.putExtra("hide_insecure_options", true);
            intent.putExtra("request_gk_pw_handle", true);
            intent.putExtra("for_face", true);
            intent.putExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            startActivityForResult(intent, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 4 && i != 1) {
            return;
        }
        if (i2 == 1 || i2 == -1) {
            this.mConfirmingCredentials = false;
        } else {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        if (!this.mConfirmingCredentials) {
            setResult(1);
            finish();
        }
        Log.d("NtCreateFaceActivity", "onStop");
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        initViews();
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        LinearLayout buttonContainer = footerBarMixin != null ? footerBarMixin.getButtonContainer() : null;
        if (buttonContainer != null) {
            buttonContainer.setBackgroundColor(getBackgroundColor());
        }
    }

    private int getBackgroundColor() {
        ColorStateList colorAttr = Utils.getColorAttr(this, 16842836);
        if (colorAttr != null) {
            return colorAttr.getDefaultColor();
        }
        return 0;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setStatusBarColor(getBackgroundColor());
        getWindow().setNavigationBarColor(getBackgroundColor());
    }
}
