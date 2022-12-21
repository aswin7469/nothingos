package com.android.systemui.statusbar.phone;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyCarrierArea;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.PreviewInflater;
import com.android.systemui.tuner.LockscreenFragment;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.nothing.systemui.util.NTLogUtil;
import java.util.List;

public class KeyguardBottomAreaView extends FrameLayout implements View.OnClickListener, KeyguardStateController.Callback, AccessibilityController.AccessibilityStateChangedCallback {
    public static final String CAMERA_LAUNCH_SOURCE_AFFORDANCE = "lockscreen_affordance";
    public static final String CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER = "lift_to_launch_ml";
    public static final String CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = "power_double_tap";
    public static final String CAMERA_LAUNCH_SOURCE_WIGGLE = "wiggle_gesture";
    private static final int DOZE_ANIMATION_ELEMENT_DURATION = 250;
    private static final int DOZE_ANIMATION_STAGGER_DELAY = 48;
    public static final String EXTRA_CAMERA_LAUNCH_SOURCE = "com.android.systemui.camera_launch_source";
    private static final String LEFT_BUTTON_PLUGIN = "com.android.systemui.action.PLUGIN_LOCKSCREEN_LEFT_BUTTON";
    /* access modifiers changed from: private */
    public static final Intent PHONE_INTENT = new Intent("android.intent.action.DIAL");
    private static final String RIGHT_BUTTON_PLUGIN = "com.android.systemui.action.PLUGIN_LOCKSCREEN_RIGHT_BUTTON";
    static final String TAG = "CentralSurfaces/KeyguardBottomAreaView";
    private AccessibilityController mAccessibilityController;
    private View.AccessibilityDelegate mAccessibilityDelegate;
    private ActivityIntentHelper mActivityIntentHelper;
    private ActivityStarter mActivityStarter;
    private KeyguardAffordanceHelper mAffordanceHelper;
    private View mAmbientIndicationArea;
    private int mBurnInXOffset;
    private int mBurnInYOffset;
    private View mCameraPreview;
    private WalletCardRetriever mCardRetriever;
    /* access modifiers changed from: private */
    public CentralSurfaces mCentralSurfaces;
    /* access modifiers changed from: private */
    public boolean mControlServicesAvailable;
    private ImageView mControlsButton;
    private ControlsComponent mControlsComponent;
    private float mDarkAmount;
    private final BroadcastReceiver mDevicePolicyReceiver;
    private boolean mDozing;
    private EmergencyCarrierArea mEmergencyCarrierArea;
    private FalsingManager mFalsingManager;
    private FlashlightController mFlashlightController;
    /* access modifiers changed from: private */
    public boolean mHasCard;
    private ViewGroup mIndicationArea;
    private int mIndicationBottomMargin;
    private int mIndicationPadding;
    private TextView mIndicationText;
    private TextView mIndicationTextBottom;
    private final boolean mIsRSA;
    /* access modifiers changed from: private */
    public KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public KeyguardAffordanceView mLeftAffordanceView;
    /* access modifiers changed from: private */
    public Drawable mLeftAssistIcon;
    private IntentButtonProvider.IntentButton mLeftButton;
    private String mLeftButtonStr;
    private ExtensionController.Extension<IntentButtonProvider.IntentButton> mLeftExtension;
    /* access modifiers changed from: private */
    public boolean mLeftIsVoiceAssist;
    private View mLeftPreview;
    private ControlsListingController.ControlsListingCallback mListingCallback;
    private ViewGroup mOverlayContainer;
    private KeyguardIndicationTextView mOwnInfoIndicationView;
    private ViewGroup mPreviewContainer;
    private PreviewInflater mPreviewInflater;
    private boolean mPrewarmBound;
    private final ServiceConnection mPrewarmConnection;
    /* access modifiers changed from: private */
    public Messenger mPrewarmMessenger;
    private ImageView mQRCodeScannerButton;
    private QRCodeScannerController mQRCodeScannerController;
    /* access modifiers changed from: private */
    public QuickAccessWalletController mQuickAccessWalletController;
    /* access modifiers changed from: private */
    public KeyguardAffordanceView mRightAffordanceView;
    private IntentButtonProvider.IntentButton mRightButton;
    private String mRightButtonStr;
    private ExtensionController.Extension<IntentButtonProvider.IntentButton> mRightExtension;
    /* access modifiers changed from: private */
    public final boolean mShowCameraAffordance;
    /* access modifiers changed from: private */
    public final boolean mShowLeftAffordance;
    private final KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    /* access modifiers changed from: private */
    public boolean mUserSetupComplete;
    /* access modifiers changed from: private */
    public ImageView mWalletButton;

    /* access modifiers changed from: private */
    public static boolean isSuccessfulLaunch(int i) {
        return i == 0 || i == 3 || i == 2;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardBottomAreaView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mHasCard = false;
        this.mCardRetriever = new WalletCardRetriever();
        this.mControlServicesAvailable = false;
        this.mPrewarmConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Messenger unused = KeyguardBottomAreaView.this.mPrewarmMessenger = new Messenger(iBinder);
            }

            public void onServiceDisconnected(ComponentName componentName) {
                Messenger unused = KeyguardBottomAreaView.this.mPrewarmMessenger = null;
            }
        };
        this.mRightButton = new DefaultRightButton();
        this.mLeftButton = new DefaultLeftButton();
        this.mListingCallback = new ControlsListingController.ControlsListingCallback() {
            public void onServicesUpdated(List<ControlsServiceInfo> list) {
                KeyguardBottomAreaView.this.post(new KeyguardBottomAreaView$2$$ExternalSyntheticLambda0(this, list));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onServicesUpdated$0$com-android-systemui-statusbar-phone-KeyguardBottomAreaView$2 */
            public /* synthetic */ void mo44162x95c76e85(List list) {
                boolean z = !list.isEmpty();
                if (z != KeyguardBottomAreaView.this.mControlServicesAvailable) {
                    boolean unused = KeyguardBottomAreaView.this.mControlServicesAvailable = z;
                    KeyguardBottomAreaView.this.updateControlsVisibility();
                    KeyguardBottomAreaView.this.updateAffordanceColors();
                }
            }
        };
        this.mAccessibilityDelegate = new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                String str;
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (view == KeyguardBottomAreaView.this.mRightAffordanceView) {
                    str = KeyguardBottomAreaView.this.getResources().getString(C1893R.string.camera_label);
                } else if (view == KeyguardBottomAreaView.this.mLeftAffordanceView) {
                    str = KeyguardBottomAreaView.this.mLeftIsVoiceAssist ? KeyguardBottomAreaView.this.getResources().getString(C1893R.string.voice_assist_label) : KeyguardBottomAreaView.this.getResources().getString(C1893R.string.phone_label);
                } else {
                    str = null;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i == 16) {
                    if (view == KeyguardBottomAreaView.this.mRightAffordanceView) {
                        KeyguardBottomAreaView.this.launchCamera(KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_AFFORDANCE);
                        return true;
                    } else if (view == KeyguardBottomAreaView.this.mLeftAffordanceView) {
                        KeyguardBottomAreaView.this.launchLeftAffordance();
                        return true;
                    }
                }
                return super.performAccessibilityAction(view, i, bundle);
            }
        };
        this.mDevicePolicyReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                KeyguardBottomAreaView.this.post(new Runnable() {
                    public void run() {
                        KeyguardBottomAreaView.this.updateCameraVisibility();
                    }
                });
            }
        };
        this.mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onUserSwitchComplete(int i) {
                KeyguardBottomAreaView.this.updateCameraVisibility();
            }

            public void onUserUnlocked() {
                KeyguardBottomAreaView.this.inflateCameraPreview();
                KeyguardBottomAreaView.this.updateCameraVisibility();
                KeyguardBottomAreaView.this.updateLeftAffordance();
            }
        };
        this.mShowLeftAffordance = getResources().getBoolean(C1893R.bool.config_keyguardShowLeftAffordance);
        this.mShowCameraAffordance = getResources().getBoolean(C1893R.bool.config_keyguardShowCameraAffordance);
        this.mIsRSA = getResources().getBoolean(C1893R.bool.config_toMeetRsaRequirements);
    }

    public void initFrom(KeyguardBottomAreaView keyguardBottomAreaView) {
        setCentralSurfaces(keyguardBottomAreaView.mCentralSurfaces);
        if (this.mAmbientIndicationArea != null) {
            View findViewById = keyguardBottomAreaView.findViewById(C1893R.C1897id.ambient_indication_container);
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
            ViewGroup viewGroup = (ViewGroup) this.mAmbientIndicationArea.getParent();
            int indexOfChild = viewGroup.indexOfChild(this.mAmbientIndicationArea);
            viewGroup.removeView(this.mAmbientIndicationArea);
            viewGroup.addView(findViewById, indexOfChild);
            this.mAmbientIndicationArea = findViewById;
            dozeTimeTick();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPreviewInflater = new PreviewInflater(this.mContext, new LockPatternUtils(this.mContext), new ActivityIntentHelper(this.mContext));
        this.mEmergencyCarrierArea = (EmergencyCarrierArea) findViewById(C1893R.C1897id.keyguard_selector_fade_container);
        this.mOverlayContainer = (ViewGroup) findViewById(C1893R.C1897id.overlay_container);
        this.mRightAffordanceView = (KeyguardAffordanceView) findViewById(C1893R.C1897id.camera_button);
        this.mLeftAffordanceView = (KeyguardAffordanceView) findViewById(C1893R.C1897id.left_button);
        this.mWalletButton = (ImageView) findViewById(C1893R.C1897id.wallet_button);
        this.mQRCodeScannerButton = (ImageView) findViewById(C1893R.C1897id.qr_code_scanner_button);
        this.mControlsButton = (ImageView) findViewById(C1893R.C1897id.controls_button);
        this.mIndicationArea = (ViewGroup) findViewById(C1893R.C1897id.keyguard_indication_area);
        this.mAmbientIndicationArea = findViewById(C1893R.C1897id.ambient_indication_container);
        this.mIndicationText = (TextView) findViewById(C1893R.C1897id.keyguard_indication_text);
        this.mIndicationTextBottom = (TextView) findViewById(C1893R.C1897id.keyguard_indication_text_bottom);
        updateIndicationBottomMargin();
        this.mOwnInfoIndicationView = (KeyguardIndicationTextView) findViewById(C1893R.C1897id.keyguard_indication_owner_info_text);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mIndicationArea.getLayoutParams();
        int i = marginLayoutParams.bottomMargin;
        int i2 = this.mIndicationBottomMargin;
        if (i != i2) {
            marginLayoutParams.bottomMargin = i2;
            this.mIndicationArea.setLayoutParams(marginLayoutParams);
        }
        this.mBurnInYOffset = getResources().getDimensionPixelSize(C1893R.dimen.default_burn_in_prevention_offset);
        updateCameraVisibility();
        KeyguardStateController keyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(this);
        setClipChildren(false);
        setClipToPadding(false);
        this.mRightAffordanceView.setOnClickListener(this);
        this.mLeftAffordanceView.setOnClickListener(this);
        initAccessibility();
        this.mActivityStarter = (ActivityStarter) Dependency.get(ActivityStarter.class);
        this.mFlashlightController = (FlashlightController) Dependency.get(FlashlightController.class);
        this.mAccessibilityController = (AccessibilityController) Dependency.get(AccessibilityController.class);
        this.mActivityIntentHelper = new ActivityIntentHelper(getContext());
        this.mIndicationPadding = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateQRCodeButtonVisibility();
        updateControlsVisibility();
        setEmergencyCarrierAreaVisibility(0);
    }

    public void setPreviewContainer(ViewGroup viewGroup) {
        this.mPreviewContainer = viewGroup;
        inflateCameraPreview();
        updateLeftAffordance();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAccessibilityController.addStateChangedCallback(this);
        this.mRightExtension = ((ExtensionController) Dependency.get(ExtensionController.class)).newExtension(IntentButtonProvider.IntentButton.class).withPlugin(IntentButtonProvider.class, RIGHT_BUTTON_PLUGIN, new KeyguardBottomAreaView$$ExternalSyntheticLambda5()).withTunerFactory(new LockscreenFragment.LockButtonFactory(this.mContext, LockscreenFragment.LOCKSCREEN_RIGHT_BUTTON)).withDefault(new KeyguardBottomAreaView$$ExternalSyntheticLambda6(this)).withCallback(new KeyguardBottomAreaView$$ExternalSyntheticLambda7(this)).build();
        this.mLeftExtension = ((ExtensionController) Dependency.get(ExtensionController.class)).newExtension(IntentButtonProvider.IntentButton.class).withPlugin(IntentButtonProvider.class, LEFT_BUTTON_PLUGIN, new KeyguardBottomAreaView$$ExternalSyntheticLambda8()).withTunerFactory(new LockscreenFragment.LockButtonFactory(this.mContext, LockscreenFragment.LOCKSCREEN_LEFT_BUTTON)).withDefault(new KeyguardBottomAreaView$$ExternalSyntheticLambda9(this)).withCallback(new KeyguardBottomAreaView$$ExternalSyntheticLambda10(this)).build();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        getContext().registerReceiverAsUser(this.mDevicePolicyReceiver, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
        KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAttachedToWindow$1$com-android-systemui-statusbar-phone-KeyguardBottomAreaView */
    public /* synthetic */ IntentButtonProvider.IntentButton mo44130x8a9a7c2() {
        return new DefaultRightButton();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAttachedToWindow$4$com-android-systemui-statusbar-phone-KeyguardBottomAreaView */
    public /* synthetic */ IntentButtonProvider.IntentButton mo44132x380a8945() {
        return new DefaultLeftButton();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mKeyguardStateController.removeCallback(this);
        this.mAccessibilityController.removeStateChangedCallback(this);
        this.mRightExtension.destroy();
        this.mLeftExtension.destroy();
        getContext().unregisterReceiver(this.mDevicePolicyReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        QuickAccessWalletController quickAccessWalletController = this.mQuickAccessWalletController;
        if (quickAccessWalletController != null) {
            quickAccessWalletController.unregisterWalletChangeObservers(QuickAccessWalletController.WalletChangeEvent.WALLET_PREFERENCE_CHANGE, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
        }
        QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
        if (qRCodeScannerController != null) {
            qRCodeScannerController.unregisterQRCodeScannerChangeObservers(0, 1);
        }
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent != null) {
            controlsComponent.getControlsListingController().ifPresent(new KeyguardBottomAreaView$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDetachedFromWindow$6$com-android-systemui-statusbar-phone-KeyguardBottomAreaView */
    public /* synthetic */ void mo44134xc522bc4(ControlsListingController controlsListingController) {
        controlsListingController.removeCallback(this.mListingCallback);
    }

    private void initAccessibility() {
        this.mLeftAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        this.mRightAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateIndicationBottomMargin();
        this.mBurnInYOffset = getResources().getDimensionPixelSize(C1893R.dimen.default_burn_in_prevention_offset);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mIndicationArea.getLayoutParams();
        int i = marginLayoutParams.bottomMargin;
        int i2 = this.mIndicationBottomMargin;
        if (i != i2) {
            marginLayoutParams.bottomMargin = i2;
            this.mIndicationArea.setLayoutParams(marginLayoutParams);
        }
        this.mIndicationTextBottom.setTextSize(0, (float) getResources().getDimensionPixelSize(17105579));
        this.mIndicationText.setTextSize(0, (float) getResources().getDimensionPixelSize(17105579));
        ViewGroup.LayoutParams layoutParams = this.mRightAffordanceView.getLayoutParams();
        layoutParams.width = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_width);
        layoutParams.height = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_height);
        this.mRightAffordanceView.setLayoutParams(layoutParams);
        updateRightAffordanceIcon();
        ViewGroup.LayoutParams layoutParams2 = this.mLeftAffordanceView.getLayoutParams();
        layoutParams2.width = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_width);
        layoutParams2.height = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_height);
        this.mLeftAffordanceView.setLayoutParams(layoutParams2);
        updateLeftAffordanceIcon();
        ViewGroup.LayoutParams layoutParams3 = this.mWalletButton.getLayoutParams();
        layoutParams3.width = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_width);
        layoutParams3.height = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_height);
        this.mWalletButton.setLayoutParams(layoutParams3);
        ViewGroup.LayoutParams layoutParams4 = this.mQRCodeScannerButton.getLayoutParams();
        layoutParams4.width = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_width);
        layoutParams4.height = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_height);
        this.mQRCodeScannerButton.setLayoutParams(layoutParams4);
        ViewGroup.LayoutParams layoutParams5 = this.mControlsButton.getLayoutParams();
        layoutParams5.width = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_width);
        layoutParams5.height = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_affordance_fixed_height);
        this.mControlsButton.setLayoutParams(layoutParams5);
        this.mIndicationPadding = getResources().getDimensionPixelSize(C1893R.dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateQRCodeButtonVisibility();
        updateAffordanceColors();
    }

    private void updateRightAffordanceIcon() {
        IntentButtonProvider.IntentButton.IconState icon = this.mRightButton.getIcon();
        this.mRightAffordanceView.setVisibility((this.mDozing || !icon.isVisible) ? 8 : 0);
        if (!(icon.drawable == this.mRightAffordanceView.getDrawable() && icon.tint == this.mRightAffordanceView.shouldTint())) {
            this.mRightAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mRightAffordanceView.setContentDescription(icon.contentDescription);
    }

    public void setCentralSurfaces(CentralSurfaces centralSurfaces) {
        this.mCentralSurfaces = centralSurfaces;
        updateCameraVisibility();
    }

    public void setAffordanceHelper(KeyguardAffordanceHelper keyguardAffordanceHelper) {
        this.mAffordanceHelper = keyguardAffordanceHelper;
    }

    public void setUserSetupComplete(boolean z) {
        this.mUserSetupComplete = z;
        updateCameraVisibility();
        updateLeftAffordanceIcon();
    }

    private Intent getCameraIntent() {
        return this.mRightButton.getIntent();
    }

    public ResolveInfo resolveCameraIntent() {
        return this.mContext.getPackageManager().resolveActivityAsUser(getCameraIntent(), 65536, KeyguardUpdateMonitor.getCurrentUser());
    }

    /* access modifiers changed from: private */
    public void updateCameraVisibility() {
        KeyguardAffordanceView keyguardAffordanceView = this.mRightAffordanceView;
        if (keyguardAffordanceView != null) {
            keyguardAffordanceView.setVisibility((this.mDozing || !this.mShowCameraAffordance || !this.mRightButton.getIcon().isVisible) ? 8 : 0);
        }
    }

    public void setLeftAssistIcon(Drawable drawable) {
        this.mLeftAssistIcon = drawable;
        updateLeftAffordanceIcon();
    }

    private void updateLeftAffordanceIcon() {
        int i = 8;
        if (!this.mShowLeftAffordance || this.mDozing) {
            this.mLeftAffordanceView.setVisibility(8);
            return;
        }
        IntentButtonProvider.IntentButton.IconState icon = this.mLeftButton.getIcon();
        KeyguardAffordanceView keyguardAffordanceView = this.mLeftAffordanceView;
        if (icon.isVisible) {
            i = 0;
        }
        keyguardAffordanceView.setVisibility(i);
        if (!(icon.drawable == this.mLeftAffordanceView.getDrawable() && icon.tint == this.mLeftAffordanceView.shouldTint())) {
            this.mLeftAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mLeftAffordanceView.setContentDescription(icon.contentDescription);
    }

    /* access modifiers changed from: private */
    public void updateWalletVisibility() {
        QuickAccessWalletController quickAccessWalletController;
        if (this.mDozing || (!this.mIsRSA && ((quickAccessWalletController = this.mQuickAccessWalletController) == null || !quickAccessWalletController.isWalletEnabled() || !this.mHasCard))) {
            this.mWalletButton.setVisibility(8);
            if (this.mControlsButton.getVisibility() == 8) {
                this.mIndicationArea.setPadding(0, 0, 0, 0);
                return;
            }
            return;
        }
        this.mWalletButton.setVisibility(0);
        this.mWalletButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda0(this));
        ViewGroup viewGroup = this.mIndicationArea;
        int i = this.mIndicationPadding;
        viewGroup.setPadding(i, 0, i, 0);
    }

    /* access modifiers changed from: private */
    public void updateControlsVisibility() {
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent != null) {
            this.mControlsButton.setImageResource(controlsComponent.getTileImageId());
            this.mControlsButton.setContentDescription(getContext().getString(this.mControlsComponent.getTileTitleId()));
            updateAffordanceColors();
            boolean booleanValue = ((Boolean) this.mControlsComponent.getControlsController().map(new KeyguardBottomAreaView$$ExternalSyntheticLambda11()).orElse(false)).booleanValue();
            if (this.mDozing || (!this.mIsRSA && (!booleanValue || !this.mControlServicesAvailable || this.mControlsComponent.getVisibility() != ControlsComponent.Visibility.AVAILABLE))) {
                this.mControlsButton.setVisibility(8);
                if (this.mWalletButton.getVisibility() == 8) {
                    this.mIndicationArea.setPadding(0, 0, 0, 0);
                    return;
                }
                return;
            }
            this.mControlsButton.setVisibility(0);
            this.mControlsButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda1(this));
            ViewGroup viewGroup = this.mIndicationArea;
            int i = this.mIndicationPadding;
            viewGroup.setPadding(i, 0, i, 0);
        }
    }

    static /* synthetic */ Boolean lambda$updateControlsVisibility$7(ControlsController controlsController) {
        return Boolean.valueOf(controlsController.getFavorites().size() > 0);
    }

    public boolean isLeftVoiceAssist() {
        return this.mLeftIsVoiceAssist;
    }

    /* access modifiers changed from: private */
    public boolean isPhoneVisible() {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!packageManager.hasSystemFeature("android.hardware.telephony") || packageManager.resolveActivity(PHONE_INTENT, 0) == null) {
            return false;
        }
        return true;
    }

    public void onStateChanged(boolean z, boolean z2) {
        this.mRightAffordanceView.setClickable(z2);
        this.mLeftAffordanceView.setClickable(z2);
        this.mRightAffordanceView.setFocusable(z);
        this.mLeftAffordanceView.setFocusable(z);
    }

    public void onClick(View view) {
        if (view == this.mRightAffordanceView) {
            launchCamera(CAMERA_LAUNCH_SOURCE_AFFORDANCE);
        } else if (view == this.mLeftAffordanceView) {
            launchLeftAffordance();
        }
    }

    public void bindCameraPrewarmService() {
        String string;
        ActivityInfo targetActivityInfo = this.mActivityIntentHelper.getTargetActivityInfo(getCameraIntent(), KeyguardUpdateMonitor.getCurrentUser(), true);
        if (targetActivityInfo != null && targetActivityInfo.metaData != null && (string = targetActivityInfo.metaData.getString("android.media.still_image_camera_preview_service")) != null) {
            Intent intent = new Intent();
            intent.setClassName(targetActivityInfo.packageName, string);
            intent.setAction("android.service.media.CameraPrewarmService.ACTION_PREWARM");
            try {
                if (getContext().bindServiceAsUser(intent, this.mPrewarmConnection, 67108865, new UserHandle(-2))) {
                    this.mPrewarmBound = true;
                }
            } catch (SecurityException e) {
                Log.w(TAG, "Unable to bind to prewarm service package=" + targetActivityInfo.packageName + " class=" + string, e);
            }
        }
    }

    public void unbindCameraPrewarmService(boolean z) {
        if (this.mPrewarmBound) {
            Messenger messenger = this.mPrewarmMessenger;
            if (messenger != null && z) {
                try {
                    messenger.send(Message.obtain((Handler) null, 1));
                } catch (RemoteException e) {
                    Log.w(TAG, "Error sending camera fired message", e);
                }
            }
            this.mContext.unbindService(this.mPrewarmConnection);
            this.mPrewarmBound = false;
        }
    }

    public void launchCamera(String str) {
        final Intent cameraIntent = getCameraIntent();
        cameraIntent.putExtra(EXTRA_CAMERA_LAUNCH_SOURCE, str);
        boolean wouldLaunchResolverActivity = this.mActivityIntentHelper.wouldLaunchResolverActivity(cameraIntent, KeyguardUpdateMonitor.getCurrentUser());
        if (!CameraIntents.isSecureCameraIntent(cameraIntent) || wouldLaunchResolverActivity) {
            this.mActivityStarter.startActivity(cameraIntent, false, (ActivityStarter.Callback) new ActivityStarter.Callback() {
                public void onActivityStarted(int i) {
                    KeyguardBottomAreaView.this.unbindCameraPrewarmService(KeyguardBottomAreaView.isSuccessfulLaunch(i));
                }
            });
        } else {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    int i;
                    ActivityOptions makeBasic = ActivityOptions.makeBasic();
                    makeBasic.setDisallowEnterPictureInPictureWhileLaunching(true);
                    makeBasic.setRotationAnimationHint(3);
                    try {
                        IActivityTaskManager service = ActivityTaskManager.getService();
                        String basePackageName = KeyguardBottomAreaView.this.getContext().getBasePackageName();
                        String attributionTag = KeyguardBottomAreaView.this.getContext().getAttributionTag();
                        Intent intent = cameraIntent;
                        i = service.startActivityAsUser((IApplicationThread) null, basePackageName, attributionTag, intent, intent.resolveTypeIfNeeded(KeyguardBottomAreaView.this.getContext().getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, makeBasic.toBundle(), UserHandle.CURRENT.getIdentifier());
                    } catch (RemoteException e) {
                        Log.w(KeyguardBottomAreaView.TAG, "Unable to start camera activity", e);
                        i = -96;
                    }
                    final boolean access$1000 = KeyguardBottomAreaView.isSuccessfulLaunch(i);
                    KeyguardBottomAreaView.this.post(new Runnable() {
                        public void run() {
                            KeyguardBottomAreaView.this.unbindCameraPrewarmService(access$1000);
                        }
                    });
                }
            });
        }
    }

    public void setDarkAmount(float f) {
        if (f != this.mDarkAmount) {
            this.mDarkAmount = f;
            dozeTimeTick();
        }
    }

    public void launchLeftAffordance() {
        if (this.mLeftIsVoiceAssist) {
            launchVoiceAssist();
        } else {
            launchPhone();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0031, code lost:
        if (((com.android.systemui.tuner.TunerService) com.android.systemui.Dependency.get(com.android.systemui.tuner.TunerService.class)).getValue(com.android.systemui.tuner.LockscreenFragment.LOCKSCREEN_RIGHT_UNLOCK, 1) != 0) goto L_0x0036;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void launchVoiceAssist() {
        /*
            r6 = this;
            com.android.systemui.statusbar.phone.KeyguardBottomAreaView$6 r1 = new com.android.systemui.statusbar.phone.KeyguardBottomAreaView$6
            r1.<init>()
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r6.mKeyguardStateController
            boolean r0 = r0.canDismissLockScreen()
            if (r0 != 0) goto L_0x0019
            com.android.systemui.Dependency$DependencyKey<java.util.concurrent.Executor> r6 = com.android.systemui.Dependency.BACKGROUND_EXECUTOR
            java.lang.Object r6 = com.android.systemui.Dependency.get(r6)
            java.util.concurrent.Executor r6 = (java.util.concurrent.Executor) r6
            r6.execute(r1)
            goto L_0x003e
        L_0x0019:
            java.lang.String r0 = r6.mRightButtonStr
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0034
            java.lang.Class<com.android.systemui.tuner.TunerService> r0 = com.android.systemui.tuner.TunerService.class
            java.lang.Object r0 = com.android.systemui.Dependency.get(r0)
            com.android.systemui.tuner.TunerService r0 = (com.android.systemui.tuner.TunerService) r0
            java.lang.String r2 = "sysui_keyguard_right_unlock"
            r3 = 1
            int r0 = r0.getValue((java.lang.String) r2, (int) r3)
            if (r0 == 0) goto L_0x0034
            goto L_0x0036
        L_0x0034:
            r0 = 0
            r3 = r0
        L_0x0036:
            com.android.systemui.statusbar.phone.CentralSurfaces r0 = r6.mCentralSurfaces
            r2 = 0
            r4 = 0
            r5 = 1
            r0.executeRunnableDismissingKeyguard(r1, r2, r3, r4, r5)
        L_0x003e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.launchVoiceAssist():void");
    }

    /* access modifiers changed from: private */
    public boolean canLaunchVoiceAssist() {
        return ((AssistManager) Dependency.get(AssistManager.class)).canVoiceAssistBeLaunchedFromKeyguard();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002d, code lost:
        if (((com.android.systemui.tuner.TunerService) com.android.systemui.Dependency.get(com.android.systemui.tuner.TunerService.class)).getValue(com.android.systemui.tuner.LockscreenFragment.LOCKSCREEN_LEFT_UNLOCK, 1) != 0) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void launchPhone() {
        /*
            r3 = this;
            android.content.Context r0 = r3.mContext
            android.telecom.TelecomManager r0 = android.telecom.TelecomManager.from(r0)
            boolean r1 = r0.isInCall()
            if (r1 == 0) goto L_0x0015
            com.android.systemui.statusbar.phone.KeyguardBottomAreaView$7 r1 = new com.android.systemui.statusbar.phone.KeyguardBottomAreaView$7
            r1.<init>(r0)
            android.os.AsyncTask.execute(r1)
            goto L_0x003c
        L_0x0015:
            java.lang.String r0 = r3.mLeftButtonStr
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0030
            java.lang.Class<com.android.systemui.tuner.TunerService> r0 = com.android.systemui.tuner.TunerService.class
            java.lang.Object r0 = com.android.systemui.Dependency.get(r0)
            com.android.systemui.tuner.TunerService r0 = (com.android.systemui.tuner.TunerService) r0
            java.lang.String r1 = "sysui_keyguard_left_unlock"
            r2 = 1
            int r0 = r0.getValue((java.lang.String) r1, (int) r2)
            if (r0 == 0) goto L_0x0030
            goto L_0x0031
        L_0x0030:
            r2 = 0
        L_0x0031:
            com.android.systemui.plugins.ActivityStarter r0 = r3.mActivityStarter
            com.android.systemui.plugins.IntentButtonProvider$IntentButton r3 = r3.mLeftButton
            android.content.Intent r3 = r3.getIntent()
            r0.startActivity(r3, r2)
        L_0x003c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.launchPhone():void");
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this && i == 0) {
            updateCameraVisibility();
        }
    }

    public KeyguardAffordanceView getLeftView() {
        return this.mLeftAffordanceView;
    }

    public KeyguardAffordanceView getRightView() {
        return this.mRightAffordanceView;
    }

    public View getLeftPreview() {
        return this.mLeftPreview;
    }

    public View getRightPreview() {
        return this.mCameraPreview;
    }

    public View getIndicationArea() {
        return this.mIndicationArea;
    }

    public void onUnlockedChanged() {
        updateCameraVisibility();
    }

    public void onKeyguardShowingChanged() {
        QuickAccessWalletController quickAccessWalletController;
        if (this.mKeyguardStateController.isShowing() && (quickAccessWalletController = this.mQuickAccessWalletController) != null) {
            quickAccessWalletController.queryWalletCards(this.mCardRetriever);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void inflateCameraPreview() {
        /*
            r4 = this;
            android.view.ViewGroup r0 = r4.mPreviewContainer
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.view.View r1 = r4.mCameraPreview
            r2 = 0
            if (r1 == 0) goto L_0x0015
            r0.removeView(r1)
            int r0 = r1.getVisibility()
            if (r0 != 0) goto L_0x0015
            r0 = 1
            goto L_0x0016
        L_0x0015:
            r0 = r2
        L_0x0016:
            com.android.systemui.statusbar.policy.PreviewInflater r1 = r4.mPreviewInflater
            android.content.Intent r3 = r4.getCameraIntent()
            android.view.View r1 = r1.inflatePreview((android.content.Intent) r3)
            r4.mCameraPreview = r1
            if (r1 == 0) goto L_0x0032
            android.view.ViewGroup r3 = r4.mPreviewContainer
            r3.addView(r1)
            android.view.View r1 = r4.mCameraPreview
            if (r0 == 0) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r2 = 4
        L_0x002f:
            r1.setVisibility(r2)
        L_0x0032:
            com.android.systemui.statusbar.phone.KeyguardAffordanceHelper r4 = r4.mAffordanceHelper
            if (r4 == 0) goto L_0x0039
            r4.updatePreviews()
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.inflateCameraPreview():void");
    }

    private void updateLeftPreview() {
        ViewGroup viewGroup = this.mPreviewContainer;
        if (viewGroup != null) {
            View view = this.mLeftPreview;
            if (view != null) {
                viewGroup.removeView(view);
            }
            if (!this.mLeftIsVoiceAssist) {
                this.mLeftPreview = this.mPreviewInflater.inflatePreview(this.mLeftButton.getIntent());
            } else if (((AssistManager) Dependency.get(AssistManager.class)).getVoiceInteractorComponentName() != null) {
                this.mLeftPreview = this.mPreviewInflater.inflatePreviewFromService(((AssistManager) Dependency.get(AssistManager.class)).getVoiceInteractorComponentName());
            }
            View view2 = this.mLeftPreview;
            if (view2 != null) {
                this.mPreviewContainer.addView(view2);
                this.mLeftPreview.setVisibility(4);
            }
            KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
            if (keyguardAffordanceHelper != null) {
                keyguardAffordanceHelper.updatePreviews();
            }
        }
    }

    public void startFinishDozeAnimation() {
        long j = 0;
        if (this.mWalletButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mWalletButton, 0);
        }
        if (this.mQRCodeScannerButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mQRCodeScannerButton, 0);
        }
        if (this.mControlsButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mControlsButton, 0);
        }
        if (this.mLeftAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mLeftAffordanceView, 0);
            j = 48;
        }
        if (this.mRightAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mRightAffordanceView, j);
        }
    }

    private void startFinishDozeAnimationElement(View view, long j) {
        view.setAlpha(0.0f);
        view.setTranslationY((float) (view.getHeight() / 2));
        view.animate().alpha(1.0f).translationY(0.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(j).setDuration(250);
    }

    public void updateLeftAffordance() {
        updateLeftAffordanceIcon();
        updateLeftPreview();
    }

    /* access modifiers changed from: private */
    /* renamed from: setRightButton */
    public void mo44131xc31f4843(IntentButtonProvider.IntentButton intentButton) {
        this.mRightButton = intentButton;
        updateRightAffordanceIcon();
        updateCameraVisibility();
        inflateCameraPreview();
    }

    /* access modifiers changed from: private */
    /* renamed from: setLeftButton */
    public void mo44133xf28029c6(IntentButtonProvider.IntentButton intentButton) {
        this.mLeftButton = intentButton;
        if (!(intentButton instanceof DefaultLeftButton)) {
            this.mLeftIsVoiceAssist = false;
        }
        updateLeftAffordance();
    }

    public void setDozing(boolean z, boolean z2) {
        this.mDozing = z;
        updateCameraVisibility();
        updateLeftAffordanceIcon();
        updateWalletVisibility();
        updateControlsVisibility();
        updateQRCodeButtonVisibility();
        if (z) {
            this.mOverlayContainer.setVisibility(4);
            setEmergencyCarrierAreaVisibility(4);
            return;
        }
        this.mOverlayContainer.setVisibility(0);
        setEmergencyCarrierAreaVisibility(0);
        if (z2) {
            startFinishDozeAnimation();
        }
    }

    public void dozeTimeTick() {
        float burnInOffset = (float) (BurnInHelperKt.getBurnInOffset(this.mBurnInYOffset * 2, false) - this.mBurnInYOffset);
        this.mIndicationArea.setTranslationY(this.mDarkAmount * burnInOffset);
        View view = this.mAmbientIndicationArea;
        if (view != null) {
            view.setTranslationY(burnInOffset * this.mDarkAmount);
        }
    }

    public void setAntiBurnInOffsetX(int i) {
        if (this.mBurnInXOffset != i) {
            this.mBurnInXOffset = i;
            float f = (float) i;
            this.mIndicationArea.setTranslationX(f);
            View view = this.mAmbientIndicationArea;
            if (view != null) {
                view.setTranslationX(f);
            }
        }
    }

    public void setAffordanceAlpha(float f) {
        this.mLeftAffordanceView.setAlpha(f);
        this.mRightAffordanceView.setAlpha(f);
        this.mIndicationArea.setAlpha(f);
        this.mWalletButton.setAlpha(f);
        this.mQRCodeScannerButton.setAlpha(f);
        this.mControlsButton.setAlpha(f);
        this.mEmergencyCarrierArea.setAlpha(f);
        this.mOwnInfoIndicationView.setAlpha(f);
    }

    private class DefaultLeftButton implements IntentButtonProvider.IntentButton {
        private IntentButtonProvider.IntentButton.IconState mIconState;

        private DefaultLeftButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            KeyguardBottomAreaView keyguardBottomAreaView = KeyguardBottomAreaView.this;
            boolean unused = keyguardBottomAreaView.mLeftIsVoiceAssist = keyguardBottomAreaView.canLaunchVoiceAssist();
            boolean z = true;
            if (KeyguardBottomAreaView.this.mLeftIsVoiceAssist) {
                IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance) {
                    z = false;
                }
                iconState.isVisible = z;
                if (KeyguardBottomAreaView.this.mLeftAssistIcon == null) {
                    this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(C1893R.C1895drawable.ic_mic_26dp);
                } else {
                    this.mIconState.drawable = KeyguardBottomAreaView.this.mLeftAssistIcon;
                }
                this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(C1893R.string.accessibility_voice_assist_button);
            } else {
                IntentButtonProvider.IntentButton.IconState iconState2 = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance || !KeyguardBottomAreaView.this.isPhoneVisible()) {
                    z = false;
                }
                iconState2.isVisible = z;
                this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(17302817);
                this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(C1893R.string.accessibility_phone_button);
            }
            return this.mIconState;
        }

        public Intent getIntent() {
            return KeyguardBottomAreaView.PHONE_INTENT;
        }
    }

    private class DefaultRightButton implements IntentButtonProvider.IntentButton {
        private IntentButtonProvider.IntentButton.IconState mIconState;

        private DefaultRightButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            boolean z = true;
            boolean z2 = KeyguardBottomAreaView.this.mCentralSurfaces != null && !KeyguardBottomAreaView.this.mCentralSurfaces.isCameraAllowedByAdmin();
            IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
            if (z2 || !KeyguardBottomAreaView.this.mShowCameraAffordance || !KeyguardBottomAreaView.this.mUserSetupComplete || KeyguardBottomAreaView.this.resolveCameraIntent() == null) {
                z = false;
            }
            iconState.isVisible = z;
            this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(C1893R.C1895drawable.ic_camera_alt_24dp);
            this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(C1893R.string.accessibility_camera_button);
            return this.mIconState;
        }

        public Intent getIntent() {
            boolean canDismissLockScreen = KeyguardBottomAreaView.this.mKeyguardStateController.canDismissLockScreen();
            if (!KeyguardBottomAreaView.this.mKeyguardStateController.isMethodSecure() || canDismissLockScreen) {
                return CameraIntents.getInsecureCameraIntent(KeyguardBottomAreaView.this.getContext());
            }
            return CameraIntents.getSecureCameraIntent(KeyguardBottomAreaView.this.getContext());
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int safeInsetBottom = windowInsets.getDisplayCutout() != null ? windowInsets.getDisplayCutout().getSafeInsetBottom() : 0;
        if (isPaddingRelative()) {
            setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), safeInsetBottom);
        } else {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), safeInsetBottom);
        }
        return windowInsets;
    }

    public void setFalsingManager(FalsingManager falsingManager) {
        this.mFalsingManager = falsingManager;
    }

    public void initWallet(QuickAccessWalletController quickAccessWalletController) {
        this.mQuickAccessWalletController = quickAccessWalletController;
        quickAccessWalletController.setupWalletChangeObservers(this.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.WALLET_PREFERENCE_CHANGE, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
        this.mQuickAccessWalletController.updateWalletPreference();
        this.mQuickAccessWalletController.queryWalletCards(this.mCardRetriever);
        updateWalletVisibility();
        updateAffordanceColors();
    }

    public void initQRCodeScanner(QRCodeScannerController qRCodeScannerController) {
        this.mQRCodeScannerController = qRCodeScannerController;
        qRCodeScannerController.registerQRCodeScannerChangeObservers(0, 1);
        updateQRCodeButtonVisibility();
        updateAffordanceColors();
    }

    private void updateQRCodeButtonVisibility() {
        QuickAccessWalletController quickAccessWalletController = this.mQuickAccessWalletController;
        if (quickAccessWalletController == null || !quickAccessWalletController.isWalletEnabled()) {
            QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
            if (qRCodeScannerController == null || !qRCodeScannerController.isEnabledForLockScreenButton()) {
                this.mQRCodeScannerButton.setVisibility(8);
                if (this.mControlsButton.getVisibility() == 8) {
                    this.mIndicationArea.setPadding(0, 0, 0, 0);
                    return;
                }
                return;
            }
            this.mQRCodeScannerButton.setVisibility(0);
            this.mQRCodeScannerButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda3(this));
            ViewGroup viewGroup = this.mIndicationArea;
            int i = this.mIndicationPadding;
            viewGroup.setPadding(i, 0, i, 0);
        }
    }

    /* access modifiers changed from: private */
    public void onQRCodeScannerClicked(View view) {
        Intent intent = this.mQRCodeScannerController.getIntent();
        if (intent != null) {
            try {
                ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, getContext().getBasePackageName(), getContext().getAttributionTag(), intent, intent.resolveTypeIfNeeded(getContext().getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, (Bundle) null, UserHandle.CURRENT.getIdentifier());
            } catch (RemoteException unused) {
                Log.e(TAG, "Unexpected intent: " + intent + " when the QR code scanner button was clicked");
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateAffordanceColors() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
        this.mWalletButton.getDrawable().setTint(colorAttrDefaultColor);
        this.mControlsButton.getDrawable().setTint(colorAttrDefaultColor);
        this.mQRCodeScannerButton.getDrawable().setTint(colorAttrDefaultColor);
        ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 17956909);
        this.mWalletButton.setBackgroundTintList(colorAttr);
        this.mControlsButton.setBackgroundTintList(colorAttr);
        this.mQRCodeScannerButton.setBackgroundTintList(colorAttr);
    }

    public void initControls(ControlsComponent controlsComponent) {
        this.mControlsComponent = controlsComponent;
        controlsComponent.getControlsListingController().ifPresent(new KeyguardBottomAreaView$$ExternalSyntheticLambda4(this));
        updateAffordanceColors();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initControls$8$com-android-systemui-statusbar-phone-KeyguardBottomAreaView */
    public /* synthetic */ void mo44129x12d52e1(ControlsListingController controlsListingController) {
        controlsListingController.addCallback(this.mListingCallback);
    }

    /* access modifiers changed from: private */
    public void onWalletClick(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mQuickAccessWalletController.startQuickAccessUiIntent(this.mActivityStarter, createLaunchAnimationController(view), this.mHasCard);
        }
    }

    /* access modifiers changed from: protected */
    public ActivityLaunchAnimator.Controller createLaunchAnimationController(View view) {
        return ActivityLaunchAnimator.Controller.fromView(view, (Integer) null);
    }

    /* access modifiers changed from: private */
    public void onControlsClick(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            Intent putExtra = new Intent(this.mContext, ControlsActivity.class).addFlags(335544320).putExtra("extra_animate", true);
            ActivityLaunchAnimator.Controller controller = null;
            if (view != null) {
                controller = ActivityLaunchAnimator.Controller.fromView(view, (Integer) null);
            }
            if (this.mControlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
                this.mActivityStarter.startActivity(putExtra, true, controller, true);
            } else {
                this.mActivityStarter.postStartActivityDismissingKeyguard(putExtra, 0, controller);
            }
        }
    }

    private class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        private WalletCardRetriever() {
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            boolean unused = KeyguardBottomAreaView.this.mHasCard = !getWalletCardsResponse.getWalletCards().isEmpty();
            KeyguardBottomAreaView.this.post(new C2971x89be7c1c(this, KeyguardBottomAreaView.this.mQuickAccessWalletController.getWalletClient().getTileIcon()));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onWalletCardsRetrieved$0$com-android-systemui-statusbar-phone-KeyguardBottomAreaView$WalletCardRetriever */
        public /* synthetic */ void mo44167xd8c0119(Drawable drawable) {
            if (drawable != null) {
                KeyguardBottomAreaView.this.mWalletButton.setImageDrawable(drawable);
            }
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            boolean unused = KeyguardBottomAreaView.this.mHasCard = false;
            KeyguardBottomAreaView.this.post(new C2970x89be7c1b(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onWalletCardRetrievalError$1$com-android-systemui-statusbar-phone-KeyguardBottomAreaView$WalletCardRetriever */
        public /* synthetic */ void mo44166x409679b9() {
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }
    }

    private void setEmergencyCarrierAreaVisibility(int i) {
        boolean z = this.mContext.getResources().getBoolean(C1893R.bool.config_showEmergencyButton);
        EmergencyCarrierArea emergencyCarrierArea = this.mEmergencyCarrierArea;
        if (!z) {
            i = 8;
        }
        emergencyCarrierArea.setVisibility(i);
    }

    private void updateIndicationBottomMargin() {
        int i = this.mContext.getResources().getDisplayMetrics().heightPixels;
        NTLogUtil.m1682i(TAG, "heightPixels=" + i);
        if (i > 0) {
            this.mIndicationBottomMargin = (int) (((float) i) * 0.177f);
        } else {
            this.mIndicationBottomMargin = getResources().getDimensionPixelSize(C1893R.dimen.nt_keyguard_indication_margin_bottom);
        }
    }
}
