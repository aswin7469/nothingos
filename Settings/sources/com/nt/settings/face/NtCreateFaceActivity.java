package com.nt.settings.face;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settingslib.Utils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.nt.facerecognition.manager.CameraPreviewProperty;
import com.nt.settings.NtSettingsBaseActivity;
import com.nt.settings.utils.NtUtils;
import com.nt.settings.widget.AutoFitCameraPreviewTextureView;
import com.nt.settings.widget.CameraLoadingTextView;
import com.nt.settings.widget.CirclePreviewView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
/* loaded from: classes2.dex */
public class NtCreateFaceActivity extends NtSettingsBaseActivity {
    private View mAddFaceDataLayout;
    private View mAddFaceNoticeLayout;
    private View mAddFaceSuccessLayout;
    private TextView mAddedSummary;
    private TextView mAddedTitle;
    private CameraLoadingTextView mCameraLoadingView;
    private AutoFitCameraPreviewTextureView mCameraPreviewTexture;
    private CirclePreviewView mCirclePreviewView;
    private boolean mConfirmingCredentials;
    private Context mContext;
    private Display mDisplay;
    private DisplayManager mDisplayManager;
    private TextView mEnrollArrow;
    private TextView mFaceAddNoticeAlwaysView;
    private TextView mFaceAddNoticeView;
    private AlertDialog mFaceRecongnitionErrorDialog;
    private GifImageView mFingerprintProgress;
    private FooterBarMixin mFooterBarMixin;
    private boolean mFromFaceSettings;
    private boolean mIsAddingFaceData;
    private boolean mIsFaceRegisterSuccessStep;
    private boolean mIsFinishAddingFaceData;
    private NtIFaceRecognition mNtIFaceRecognition;
    private Vibrator mVibrator;
    private final int ANIMA_CIRCLE_LINE_WIDTH_IN_PIXEL = 15;
    private Surface mCameraPreviewTextureSurface = null;
    private long mStartPauseTime = -1;
    private int mDisplayState = 2;
    private Handler mUiHandler = new Handler() { // from class: com.nt.settings.face.NtCreateFaceActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("NtCreateFaceActivity", "handleMessage :" + message.what);
            switch (message.what) {
                case 1:
                    Log.d("NtCreateFaceActivity", "MSG_SET_CAMERA_LOADING_VIEW_GONE");
                    NtCreateFaceActivity ntCreateFaceActivity = NtCreateFaceActivity.this;
                    ntCreateFaceActivity.startViewVisibilityAnimation(ntCreateFaceActivity.mAddFaceNoticeLayout, NtCreateFaceActivity.this.mAddFaceDataLayout, true);
                    NtCreateFaceActivity.this.mFooterBarMixin.getPrimaryButton().setVisibility(8);
                    NtCreateFaceActivity.this.mCameraLoadingView.setCircleProperties(NtCreateFaceActivity.this.mCameraPreviewTexture.getCircleProperties());
                    NtCreateFaceActivity.this.mCameraLoadingView.setCircleOutlineProvider();
                    NtCreateFaceActivity.this.mCameraPreviewTexture.setCircleOutlineProvider();
                    NtCreateFaceActivity.this.mCirclePreviewView.setCircleProperties(NtCreateFaceActivity.this.mCameraPreviewTexture.getCircleProperties());
                    NtCreateFaceActivity.this.mFaceAddNoticeView.setVisibility(0);
                    ((ViewGroup.MarginLayoutParams) NtCreateFaceActivity.this.mFingerprintProgress.getLayoutParams()).setMargins(NtCreateFaceActivity.this.mCameraPreviewTexture.getCircleProperties().getMarginLayoutParams().leftMargin - 15, NtCreateFaceActivity.this.mCameraPreviewTexture.getCircleProperties().getTop() - 15, NtCreateFaceActivity.this.mCameraPreviewTexture.getCircleProperties().getMarginLayoutParams().rightMargin - 15, 0);
                    return;
                case 2:
                    NtCreateFaceActivity.this.mIsAddingFaceData = true;
                    NtCreateFaceActivity.this.mUiHandler.removeCallbacks(NtCreateFaceActivity.this.mRunTimeout);
                    NtCreateFaceActivity.this.mUiHandler.postDelayed(NtCreateFaceActivity.this.mRunTimeout, 30000L);
                    NtCreateFaceActivity.this.getWindow().addFlags(128);
                    NtCreateFaceActivity.this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
                    NtCreateFaceActivity.this.mUiHandler.removeMessages(11);
                    NtCreateFaceActivity.this.mUiHandler.sendEmptyMessageDelayed(11, 100L);
                    return;
                case 3:
                    Log.d("NtCreateFaceActivity", "MSG_SET_CMMERA_PREVIEW_VIEW_GONE");
                    if (NtCreateFaceActivity.this.mFaceRecongnitionErrorDialog != null) {
                        NtCreateFaceActivity.this.mFaceRecongnitionErrorDialog.dismiss();
                    }
                    if (NtCreateFaceActivity.this.mNtIFaceRecognition != null) {
                        NtCreateFaceActivity.this.mNtIFaceRecognition.endEnroll();
                        NtCreateFaceActivity.this.mNtIFaceRecognition.closeHardwareDevice();
                        if (NtCreateFaceActivity.this.mCameraPreviewTextureSurface != null) {
                            NtCreateFaceActivity.this.mCameraPreviewTextureSurface.destroy();
                            NtCreateFaceActivity.this.mCameraPreviewTextureSurface = null;
                        }
                    }
                    NtCreateFaceActivity.this.mUiHandler.removeCallbacks(NtCreateFaceActivity.this.mRunTimeout);
                    NtCreateFaceActivity.this.getWindow().clearFlags(128);
                    NtCreateFaceActivity.this.mCameraPreviewTexture.setVisibility(8);
                    NtCreateFaceActivity.this.mEnrollArrow.setVisibility(8);
                    NtCreateFaceActivity.this.mFaceAddNoticeAlwaysView.setVisibility(8);
                    NtCreateFaceActivity.this.mFaceAddNoticeView.setVisibility(8);
                    NtCreateFaceActivity.this.mAddedTitle.setVisibility(0);
                    NtCreateFaceActivity.this.mAddedSummary.setVisibility(0);
                    NtCreateFaceActivity.this.mFooterBarMixin.getPrimaryButton().setVisibility(0);
                    NtCreateFaceActivity.this.mFooterBarMixin.getPrimaryButton().setText(NtCreateFaceActivity.this.getResources().getString(R.string.nt_face_enroll_success_done));
                    NtCreateFaceActivity.this.mIsAddingFaceData = false;
                    NtCreateFaceActivity.this.mIsFaceRegisterSuccessStep = true;
                    NtCreateFaceActivity.this.getActionBar().setDisplayHomeAsUpEnabled(false);
                    return;
                case 4:
                    Log.d("NtCreateFaceActivity", "MSG_SET_FACE_SAVEFEATURE_NOTICE_TEXT_VIEW");
                    NtCreateFaceActivity.this.mFaceAddNoticeView.setText((CharSequence) message.obj);
                    NtUtils.startShakeAnim(NtCreateFaceActivity.this.mFaceAddNoticeView);
                    NtCreateFaceActivity.this.mVibrator.vibrate(20L);
                    return;
                case 5:
                    NtCreateFaceActivity.this.mCameraLoadingView.setText((CharSequence) message.obj);
                    return;
                case 6:
                case 8:
                case 9:
                default:
                    return;
                case 7:
                    int i = message.arg1;
                    Log.d("NtCreateFaceActivity", "MSG_UPDATE_SAVEFEATURE_PROGRESS progress:" + i);
                    NtCreateFaceActivity.this.mFaceAddNoticeView.setText((CharSequence) message.obj);
                    NtCreateFaceActivity.this.mCirclePreviewView.setProgress(i);
                    return;
                case 10:
                    Log.d("NtCreateFaceActivity", "MSG_CAMERA_PERVIEW_STARTED_SUCCESS :" + message.arg1);
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        NtCreateFaceActivity.this.showCameraOpenFailedDialog();
                        return;
                    } else if (i2 != 1) {
                        return;
                    } else {
                        NtCreateFaceActivity.this.mUiHandler.postDelayed(new Runnable() { // from class: com.nt.settings.face.NtCreateFaceActivity.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                NtCreateFaceActivity.this.mCameraLoadingView.setVisibility(8);
                            }
                        }, 400L);
                        CameraPreviewProperty cameraPreviewProperty = (CameraPreviewProperty) message.obj;
                        if (cameraPreviewProperty != null) {
                            Log.d("NtCreateFaceActivity", "cameraPreviewProperty getCameraWidth:" + cameraPreviewProperty.getCameraWidth() + ",getCameraHeight:" + cameraPreviewProperty.getCameraHeight());
                        }
                        if (!NtCreateFaceActivity.this.mIsAddingFaceData) {
                            return;
                        }
                        NtCreateFaceActivity.this.mCameraPreviewTexture.setAspectRatio();
                        NtCreateFaceActivity.this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
                        Log.d("NtCreateFaceActivity", "MSG_CAMERA_PERVIEW_STARTED_SUCCESS startEnroll");
                        NtCreateFaceActivity.this.mUiHandler.removeCallbacks(NtCreateFaceActivity.this.mRunTimeout);
                        NtCreateFaceActivity.this.mUiHandler.postDelayed(NtCreateFaceActivity.this.mRunTimeout, 30000L);
                        NtCreateFaceActivity.this.getWindow().addFlags(128);
                        NtCreateFaceActivity.this.mUiHandler.removeMessages(11);
                        NtCreateFaceActivity.this.mUiHandler.sendEmptyMessageDelayed(11, 100L);
                        return;
                    }
                case 11:
                    NtCreateFaceActivity.this.mNtIFaceRecognition.startEnroll(NtCreateFaceActivity.this.mUiHandler, new byte[0], NtCreateFaceActivity.this.mCirclePreviewView.getMaskRect(NtCreateFaceActivity.this.mCameraPreviewTexture.getPreviewScaleX(), NtCreateFaceActivity.this.mCameraPreviewTexture.getPreviewScaleY()));
                    return;
                case 12:
                    Log.d("NtCreateFaceActivity", "MSG_SCREEN_OFF :" + message.arg1);
                    if (!NtFaceUtil.isFaceDataExist(NtCreateFaceActivity.this.mContext) && !NtCreateFaceActivity.this.mIsFinishAddingFaceData) {
                        NtCreateFaceActivity.this.cancelCreateFaceState();
                    }
                    NtCreateFaceActivity.this.finish();
                    return;
                case 13:
                    if (NtCreateFaceActivity.this.mNtIFaceRecognition != null) {
                        NtCreateFaceActivity.this.mNtIFaceRecognition.endEnroll();
                        NtCreateFaceActivity.this.mNtIFaceRecognition.closeHardwareDevice();
                    }
                    ((GifDrawable) NtCreateFaceActivity.this.mFingerprintProgress.getDrawable()).start();
                    NtCreateFaceActivity.this.mUiHandler.sendEmptyMessageDelayed(3, 1500L);
                    NtCreateFaceActivity.this.setResult(2);
                    NtCreateFaceActivity.this.handleFinishAddingFaceData();
                    return;
            }
        }
    };
    private Runnable mRunTimeout = new Runnable() { // from class: com.nt.settings.face.NtCreateFaceActivity.2
        @Override // java.lang.Runnable
        public void run() {
            NtCreateFaceActivity.this.getWindow().clearFlags(128);
            if (!NtCreateFaceActivity.this.mIsFinishAddingFaceData) {
                NtCreateFaceActivity.this.cancelCreateFaceState();
                NtCreateFaceActivity.this.mNtIFaceRecognition.endEnroll();
                NtCreateFaceActivity.this.showFaceRecognitionTimeoutDialog();
            }
        }
    };
    private final View.OnClickListener mFooterButtonClickListener = new View.OnClickListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!NtCreateFaceActivity.this.mIsFinishAddingFaceData) {
                NtCreateFaceActivity.this.mUiHandler.obtainMessage(1).sendToTarget();
            } else {
                NtCreateFaceActivity.this.handleCancelAddingFace();
            }
        }
    };
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.7
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            int state = NtCreateFaceActivity.this.mDisplayManager.getDisplay(0).getState();
            if (NtCreateFaceActivity.this.mDisplay.getDisplayId() == i) {
                NtCreateFaceActivity.this.mDisplayState = state;
                if (state != 1) {
                    return;
                }
                NtCreateFaceActivity.this.mUiHandler.removeMessages(12);
                NtCreateFaceActivity.this.mUiHandler.sendEmptyMessage(12);
            }
        }
    };

    @Override // com.nt.settings.NtSettingsBaseActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R.style.Theme_SetupWizardOverlay);
        boolean booleanExtra = getIntent().getBooleanExtra("from_face_settings", false);
        this.mFromFaceSettings = booleanExtra;
        if (!booleanExtra && !this.mConfirmingCredentials) {
            this.mConfirmingCredentials = true;
            launchChooseOrConfirmLock();
        }
        setContentView(R.layout.nt_layout_create_face);
        findViewById(R.id.sud_layout_header).setVisibility(8);
        GlifLayout glifLayout = (GlifLayout) findViewById(R.id.setup_wizard_layout);
        glifLayout.setBackgroundColor(getBackgroundColor());
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.nt_setup).setButtonType(6).setListener(this.mFooterButtonClickListener).setTheme(R.style.SudGlifButton_NtPrimary).build());
        this.mFooterBarMixin.getButtonContainer().setBackgroundColor(0);
        Context applicationContext = getApplicationContext();
        this.mContext = applicationContext;
        startBootFaceRecognitionService(applicationContext);
        initValues();
        initViews();
        Log.d("NtCreateFaceActivity", "onCreate ");
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        Log.d("NtCreateFaceActivity", "onResume mStartPauseTime 1:" + this.mStartPauseTime);
        if (this.mIsAddingFaceData) {
            this.mCameraPreviewTexture.setAspectRatio();
            final SurfaceTexture surfaceTexture = this.mCameraPreviewTexture.getSurfaceTexture();
            this.mCameraPreviewTexture.setSurfaceTextureDefaultBufferSize();
            this.mUiHandler.removeCallbacks(this.mRunTimeout);
            this.mCirclePreviewView.setProgress(0);
            this.mUiHandler.postDelayed(new Runnable() { // from class: com.nt.settings.face.NtCreateFaceActivity.4
                @Override // java.lang.Runnable
                public void run() {
                    if (NtCreateFaceActivity.this.mNtIFaceRecognition != null) {
                        if (NtCreateFaceActivity.this.mCameraPreviewTextureSurface == null) {
                            NtCreateFaceActivity.this.mCameraPreviewTextureSurface = new Surface(surfaceTexture);
                        }
                        NtCreateFaceActivity.this.mNtIFaceRecognition.warmUpHardwareDeviceForPreview(NtCreateFaceActivity.this.mCameraPreviewTextureSurface, NtCreateFaceActivity.this.mUiHandler, NtCreateFaceActivity.this.mCameraPreviewTexture.getWidth(), NtCreateFaceActivity.this.mCameraPreviewTexture.getHeight());
                        NtCreateFaceActivity.this.mUiHandler.removeMessages(11);
                        NtCreateFaceActivity.this.mUiHandler.sendEmptyMessage(11);
                    }
                }
            }, 100L);
            this.mUiHandler.postDelayed(this.mRunTimeout, 30000L);
            getWindow().addFlags(128);
        }
        Log.d("NtCreateFaceActivity", "onResume mStartPauseTime 2:" + this.mStartPauseTime);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("finish_add_face", this.mIsFinishAddingFaceData);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mIsFinishAddingFaceData = bundle.getBoolean("finish_add_face");
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.mIsAddingFaceData) {
            this.mUiHandler.removeCallbacks(this.mRunTimeout);
            this.mNtIFaceRecognition.endEnroll();
            this.mIsAddingFaceData = false;
            showFailedDialog();
        }
        this.mStartPauseTime = System.currentTimeMillis();
        NtIFaceRecognition ntIFaceRecognition = this.mNtIFaceRecognition;
        if (ntIFaceRecognition != null) {
            ntIFaceRecognition.closeHardwareDevice();
            Surface surface = this.mCameraPreviewTextureSurface;
            if (surface != null) {
                surface.destroy();
                this.mCameraPreviewTextureSurface = null;
            }
            Log.d("NtCreateFaceActivity", "onPause destroy surface");
        }
        this.mStartPauseTime = System.currentTimeMillis();
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        Log.d("NtCreateFaceActivity", "onPause mStartPauseTime:" + this.mStartPauseTime);
    }

    private void showFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.security_settings_face_enroll_error_dialog_title);
        builder.setMessage(R.string.security_settings_face_enroll_error_generic_dialog_message);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.5
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                NtCreateFaceActivity.this.finish();
            }
        });
        create.show();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        handleCancelAddingFace();
        Log.d("NtCreateFaceActivity", "onBackPressed");
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
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

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.mNtIFaceRecognition = null;
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        Log.d("NtCreateFaceActivity", "onDestroy");
    }

    private void initValues() {
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mNtIFaceRecognition = new NtFaceRecognitionImpl(this.mContext, this.mUiHandler);
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDisplayManager = displayManager;
        this.mDisplay = displayManager.getDisplay(0);
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mUiHandler);
    }

    private void initViews() {
        this.mAddFaceNoticeLayout = findViewById(R.id.face_notice_layout);
        this.mAddFaceDataLayout = findViewById(R.id.face_camerapreview_layout);
        this.mAddFaceSuccessLayout = findViewById(R.id.face_registerssuccess_layout);
        this.mCameraLoadingView = (CameraLoadingTextView) findViewById(R.id.camera_preview_loading_text);
        AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = (AutoFitCameraPreviewTextureView) findViewById(R.id.textureview_camera_preview);
        this.mCameraPreviewTexture = autoFitCameraPreviewTextureView;
        autoFitCameraPreviewTextureView.setSurfaceTextureAvailableListener(new AutoFitCameraPreviewTextureView.OnSurfaceTextureAvailableListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.6
            @Override // com.nt.settings.widget.AutoFitCameraPreviewTextureView.OnSurfaceTextureAvailableListener
            public void onSurfaceTextureAvailableCallback(SurfaceTexture surfaceTexture, int i, int i2) {
                Log.d("NtCreateFaceActivity", "onSurfaceTextureAvailable width:" + i + ",height:" + i2 + "");
                if (surfaceTexture != null) {
                    SurfaceTexture surfaceTexture2 = NtCreateFaceActivity.this.mCameraPreviewTexture.getSurfaceTexture();
                    if (NtCreateFaceActivity.this.mCameraPreviewTextureSurface == null) {
                        NtCreateFaceActivity.this.mCameraPreviewTextureSurface = new Surface(surfaceTexture2);
                    }
                    NtCreateFaceActivity.this.mNtIFaceRecognition.warmUpHardwareDeviceForPreview(NtCreateFaceActivity.this.mCameraPreviewTextureSurface, NtCreateFaceActivity.this.mUiHandler, i, i2);
                    return;
                }
                Log.e("NtCreateFaceActivity", "onSurfaceTextureAvailable texture == null");
            }
        });
        CirclePreviewView circlePreviewView = (CirclePreviewView) findViewById(R.id.circle_preview_mark);
        this.mCirclePreviewView = circlePreviewView;
        circlePreviewView.setVisibility(8);
        this.mFaceAddNoticeAlwaysView = (TextView) findViewById(R.id.face_second_step_add_notice_always);
        this.mFaceAddNoticeView = (TextView) findViewById(R.id.face_second_step_add_notice);
        GifImageView gifImageView = (GifImageView) findViewById(R.id.face_enroll_success);
        this.mFingerprintProgress = gifImageView;
        ((GifDrawable) gifImageView.getDrawable()).stop();
        this.mEnrollArrow = (TextView) findViewById(R.id.face_enroll_arrow);
        this.mAddedTitle = (TextView) findViewById(R.id.face_added_title);
        this.mAddedSummary = (TextView) findViewById(R.id.face_added_summary);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCancelAddingFace() {
        if (!this.mIsFaceRegisterSuccessStep && !this.mIsAddingFaceData && this.mIsFinishAddingFaceData && !this.mFromFaceSettings) {
            startFaceSettingFragment();
        }
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelCreateFaceState() {
        this.mIsFinishAddingFaceData = false;
        NtIFaceRecognition ntIFaceRecognition = this.mNtIFaceRecognition;
        if (ntIFaceRecognition != null) {
            ntIFaceRecognition.endEnroll();
            this.mNtIFaceRecognition.closeHardwareDevice();
            Surface surface = this.mCameraPreviewTextureSurface;
            if (surface != null) {
                surface.destroy();
                this.mCameraPreviewTextureSurface = null;
            }
        }
        this.mIsAddingFaceData = false;
        this.mIsFaceRegisterSuccessStep = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCameraOpenFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nt_face_enroll_camera_exception);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.8
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                NtCreateFaceActivity.this.finish();
            }
        });
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFaceRecognitionTimeoutDialog() {
        if (!this.mIsFaceRegisterSuccessStep) {
            if (this.mFaceRecongnitionErrorDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.nt_security_settings_face_enroll_error_dialog_title);
                builder.setMessage(R.string.nt_security_settings_face_enroll_error_timeout_dialog_message);
                builder.setPositiveButton(R.string.nt_face_enroll_success_done, new DialogInterface.OnClickListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NtCreateFaceActivity.this.finish();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.10
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        NtCreateFaceActivity.this.mIsAddingFaceData = false;
                        if (NtCreateFaceActivity.this.mCirclePreviewView != null) {
                            NtCreateFaceActivity.this.mCirclePreviewView.setProgress(0);
                        }
                        NtCreateFaceActivity.this.finish();
                    }
                });
                AlertDialog create = builder.create();
                this.mFaceRecongnitionErrorDialog = create;
                create.setCanceledOnTouchOutside(false);
            }
            if (isFinishing()) {
                return;
            }
            this.mFaceRecongnitionErrorDialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startViewVisibilityAnimation(View view, final View view2, final boolean z) {
        if (view == null || view2 == null) {
            return;
        }
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        TranslateAnimation translateAnimation = new TranslateAnimation(point.x, 0.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(200L);
        translateAnimation.setFillAfter(false);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.nt.settings.face.NtCreateFaceActivity.11
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (z) {
                    NtCreateFaceActivity.this.mUiHandler.sendEmptyMessage(2);
                }
                view2.clearAnimation();
            }
        });
        view.setVisibility(8);
        view2.setVisibility(0);
        view2.startAnimation(translateAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinishAddingFaceData() {
        if (this.mDisplayState == 2) {
            this.mIsFinishAddingFaceData = true;
            this.mIsAddingFaceData = false;
            this.mIsFaceRegisterSuccessStep = false;
            NtFaceUtil.setFaceDataCount(this.mContext, NtFaceUtil.getFaceDataCount(this.mContext) + 1);
            Log.d("NtCreateFaceActivity", "add face success!");
        }
    }

    private void startFaceSettingFragment() {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", Settings.FaceSettingsActivity.class.getName());
        intent.putExtra("from_create_face_settings", true);
        startActivity(intent);
    }

    private void startBootFaceRecognitionService(Context context) {
        Slog.i("NtCreateFaceActivity", "startFaceBootReceiverService");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.nt.facerecognition", "com.nt.facerecognition.server.BootFaceRecognitionService"));
        intent.putExtra("start_app", "com.android.settings");
        context.startForegroundService(intent);
    }

    private void launchChooseOrConfirmLock() {
        Intent intent = new Intent();
        if (!new ChooseLockSettingsHelper.Builder(this, null).setRequestCode(4).setTitle(getString(R.string.security_settings_fingerprint_preference_title)).setRequestGatekeeperPasswordHandle(true).setUserId(UserHandle.myUserId()).setForegroundOnly(true).setReturnCredentials(true).show()) {
            intent.setClassName("com.android.settings", ChooseLockGeneric.class.getName());
            intent.putExtra("hide_insecure_options", true);
            intent.putExtra("request_gk_pw_handle", true);
            intent.putExtra("for_face", true);
            intent.putExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            startActivityForResult(intent, 1);
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4 || i == 1) {
            if (i2 == 1 || i2 == -1) {
                this.mConfirmingCredentials = false;
            } else {
                finish();
            }
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        this.mUiHandler.removeCallbacks(this.mRunTimeout);
        getWindow().clearFlags(128);
        if (!this.mConfirmingCredentials) {
            setResult(1);
            finish();
        }
        Log.d("NtCreateFaceActivity", "onStop");
    }

    @Override // android.app.Activity
    protected void onPostCreate(Bundle bundle) {
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

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setStatusBarColor(getBackgroundColor());
        getWindow().setNavigationBarColor(getBackgroundColor());
    }
}
