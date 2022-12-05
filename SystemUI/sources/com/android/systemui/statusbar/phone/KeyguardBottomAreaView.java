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
import android.content.res.Resources;
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
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.PreviewInflater;
import com.android.systemui.tuner.LockscreenFragment;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.ui.WalletActivity;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardBottomAreaView extends FrameLayout implements View.OnClickListener, KeyguardStateController.Callback, AccessibilityController.AccessibilityStateChangedCallback {
    private static final Intent PHONE_INTENT = new Intent("android.intent.action.DIAL");
    private AccessibilityController mAccessibilityController;
    private View.AccessibilityDelegate mAccessibilityDelegate;
    private ActivityIntentHelper mActivityIntentHelper;
    private ActivityStarter mActivityStarter;
    private KeyguardAffordanceHelper mAffordanceHelper;
    private int mBurnInXOffset;
    private int mBurnInYOffset;
    private View mCameraPreview;
    private WalletCardRetriever mCardRetriever;
    private boolean mControlServicesAvailable;
    private ImageView mControlsButton;
    private ControlsComponent mControlsComponent;
    private float mDarkAmount;
    private final BroadcastReceiver mDevicePolicyReceiver;
    private boolean mDozing;
    private FalsingManager mFalsingManager;
    private FlashlightController mFlashlightController;
    private boolean mHasCard;
    private ViewGroup mIndicationArea;
    private int mIndicationBottomMargin;
    private int mIndicationPadding;
    private TextView mIndicationText;
    private TextView mIndicationTextBottom;
    private KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardAffordanceView mLeftAffordanceView;
    private Drawable mLeftAssistIcon;
    private IntentButtonProvider.IntentButton mLeftButton;
    private String mLeftButtonStr;
    private ExtensionController.Extension<IntentButtonProvider.IntentButton> mLeftExtension;
    private boolean mLeftIsVoiceAssist;
    private View mLeftPreview;
    private ControlsListingController.ControlsListingCallback mListingCallback;
    private ViewGroup mOverlayContainer;
    private KeyguardIndicationTextView mOwnInfoIndicationView;
    private ViewGroup mPreviewContainer;
    private PreviewInflater mPreviewInflater;
    private boolean mPrewarmBound;
    private final ServiceConnection mPrewarmConnection;
    private Messenger mPrewarmMessenger;
    private QuickAccessWalletController mQuickAccessWalletController;
    private KeyguardAffordanceView mRightAffordanceView;
    private IntentButtonProvider.IntentButton mRightButton;
    private String mRightButtonStr;
    private ExtensionController.Extension<IntentButtonProvider.IntentButton> mRightExtension;
    private final boolean mShowCameraAffordance;
    private final boolean mShowLeftAffordance;
    private StatusBar mStatusBar;
    private final KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    private boolean mUserSetupComplete;
    private ImageView mWalletButton;

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSuccessfulLaunch(int i) {
        return i == 0 || i == 3 || i == 2;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements ControlsListingController.ControlsListingCallback {
        AnonymousClass2() {
        }

        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public void onServicesUpdated(final List<ControlsServiceInfo> list) {
            KeyguardBottomAreaView.this.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardBottomAreaView.AnonymousClass2.this.lambda$onServicesUpdated$0(list);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServicesUpdated$0(List list) {
            boolean z = !list.isEmpty();
            if (z != KeyguardBottomAreaView.this.mControlServicesAvailable) {
                KeyguardBottomAreaView.this.mControlServicesAvailable = z;
                KeyguardBottomAreaView.this.updateControlsVisibility();
                KeyguardBottomAreaView.this.updateAffordanceColors();
            }
        }
    }

    public KeyguardBottomAreaView(Context context) {
        this(context, null);
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
        this.mPrewarmConnection = new ServiceConnection() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                KeyguardBottomAreaView.this.mPrewarmMessenger = new Messenger(iBinder);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                KeyguardBottomAreaView.this.mPrewarmMessenger = null;
            }
        };
        this.mRightButton = new DefaultRightButton();
        this.mLeftButton = new DefaultLeftButton();
        this.mListingCallback = new AnonymousClass2();
        this.mAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.3
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                String str;
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (view != KeyguardBottomAreaView.this.mRightAffordanceView) {
                    if (view != KeyguardBottomAreaView.this.mLeftAffordanceView) {
                        str = null;
                    } else if (KeyguardBottomAreaView.this.mLeftIsVoiceAssist) {
                        str = KeyguardBottomAreaView.this.getResources().getString(R$string.voice_assist_label);
                    } else {
                        str = KeyguardBottomAreaView.this.getResources().getString(R$string.phone_label);
                    }
                } else {
                    str = KeyguardBottomAreaView.this.getResources().getString(R$string.camera_label);
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }

            @Override // android.view.View.AccessibilityDelegate
            public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
                if (i3 == 16) {
                    if (view != KeyguardBottomAreaView.this.mRightAffordanceView) {
                        if (view == KeyguardBottomAreaView.this.mLeftAffordanceView) {
                            KeyguardBottomAreaView.this.launchLeftAffordance();
                            return true;
                        }
                    } else {
                        KeyguardBottomAreaView.this.launchCamera("lockscreen_affordance");
                        return true;
                    }
                }
                return super.performAccessibilityAction(view, i3, bundle);
            }
        };
        this.mDevicePolicyReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                KeyguardBottomAreaView.this.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        KeyguardBottomAreaView.this.updateCameraVisibility();
                    }
                });
            }
        };
        this.mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.9
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onUserSwitchComplete(int i3) {
                KeyguardBottomAreaView.this.updateCameraVisibility();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onUserUnlocked() {
                KeyguardBottomAreaView.this.inflateCameraPreview();
                KeyguardBottomAreaView.this.updateCameraVisibility();
                KeyguardBottomAreaView.this.updateLeftAffordance();
            }
        };
        this.mShowLeftAffordance = getResources().getBoolean(R$bool.config_keyguardShowLeftAffordance);
        this.mShowCameraAffordance = getResources().getBoolean(R$bool.config_keyguardShowCameraAffordance);
    }

    public void initFrom(KeyguardBottomAreaView keyguardBottomAreaView) {
        setStatusBar(keyguardBottomAreaView.mStatusBar);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPreviewInflater = new PreviewInflater(((FrameLayout) this).mContext, new LockPatternUtils(((FrameLayout) this).mContext), new ActivityIntentHelper(((FrameLayout) this).mContext));
        this.mOverlayContainer = (ViewGroup) findViewById(R$id.overlay_container);
        this.mRightAffordanceView = (KeyguardAffordanceView) findViewById(R$id.camera_button);
        this.mLeftAffordanceView = (KeyguardAffordanceView) findViewById(R$id.left_button);
        this.mWalletButton = (ImageView) findViewById(R$id.wallet_button);
        this.mControlsButton = (ImageView) findViewById(R$id.controls_button);
        this.mOwnInfoIndicationView = (KeyguardIndicationTextView) findViewById(R$id.keyguard_indication_owner_info_text);
        this.mIndicationArea = (ViewGroup) findViewById(R$id.keyguard_indication_area);
        this.mIndicationText = (TextView) findViewById(R$id.keyguard_indication_text);
        this.mIndicationTextBottom = (TextView) findViewById(R$id.keyguard_indication_text_bottom);
        updateBottomMargin();
        Log.i("KeyguardBottomAreaView", "mIndicationBottomMargin=" + this.mIndicationBottomMargin);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mIndicationArea.getLayoutParams();
        if (marginLayoutParams.bottomMargin != this.mIndicationBottomMargin) {
            Log.i("KeyguardBottomAreaView", "set mIndicationBottomMargin=" + this.mIndicationBottomMargin);
            marginLayoutParams.bottomMargin = this.mIndicationBottomMargin;
            this.mIndicationArea.setLayoutParams(marginLayoutParams);
        }
        this.mBurnInYOffset = getResources().getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
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
        this.mIndicationPadding = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateControlsVisibility();
    }

    public void setPreviewContainer(ViewGroup viewGroup) {
        this.mPreviewContainer = viewGroup;
        inflateCameraPreview();
        updateLeftAffordance();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAccessibilityController.addStateChangedCallback(this);
        this.mRightExtension = ((ExtensionController) Dependency.get(ExtensionController.class)).mo1298newExtension(IntentButtonProvider.IntentButton.class).withPlugin(IntentButtonProvider.class, "com.android.systemui.action.PLUGIN_LOCKSCREEN_RIGHT_BUTTON", KeyguardBottomAreaView$$ExternalSyntheticLambda3.INSTANCE).withTunerFactory(new LockscreenFragment.LockButtonFactory(((FrameLayout) this).mContext, "sysui_keyguard_right")).withDefault(new Supplier() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final Object get() {
                IntentButtonProvider.IntentButton lambda$onAttachedToWindow$1;
                lambda$onAttachedToWindow$1 = KeyguardBottomAreaView.this.lambda$onAttachedToWindow$1();
                return lambda$onAttachedToWindow$1;
            }
        }).withCallback(new Consumer() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyguardBottomAreaView.this.lambda$onAttachedToWindow$2((IntentButtonProvider.IntentButton) obj);
            }
        }).build();
        this.mLeftExtension = ((ExtensionController) Dependency.get(ExtensionController.class)).mo1298newExtension(IntentButtonProvider.IntentButton.class).withPlugin(IntentButtonProvider.class, "com.android.systemui.action.PLUGIN_LOCKSCREEN_LEFT_BUTTON", KeyguardBottomAreaView$$ExternalSyntheticLambda2.INSTANCE).withTunerFactory(new LockscreenFragment.LockButtonFactory(((FrameLayout) this).mContext, "sysui_keyguard_left")).withDefault(new Supplier() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final Object get() {
                IntentButtonProvider.IntentButton lambda$onAttachedToWindow$4;
                lambda$onAttachedToWindow$4 = KeyguardBottomAreaView.this.lambda$onAttachedToWindow$4();
                return lambda$onAttachedToWindow$4;
            }
        }).withCallback(new Consumer() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyguardBottomAreaView.this.lambda$onAttachedToWindow$5((IntentButtonProvider.IntentButton) obj);
            }
        }).build();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        getContext().registerReceiverAsUser(this.mDevicePolicyReceiver, UserHandle.ALL, intentFilter, null, null);
        KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ IntentButtonProvider.IntentButton lambda$onAttachedToWindow$1() {
        return new DefaultRightButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ IntentButtonProvider.IntentButton lambda$onAttachedToWindow$4() {
        return new DefaultLeftButton();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
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
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent != null) {
            controlsComponent.getControlsListingController().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    KeyguardBottomAreaView.this.lambda$onDetachedFromWindow$6((ControlsListingController) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDetachedFromWindow$6(ControlsListingController controlsListingController) {
        controlsListingController.removeCallback(this.mListingCallback);
    }

    private void initAccessibility() {
        this.mLeftAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        this.mRightAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateBottomMargin();
        Log.i("KeyguardBottomAreaView", "mIndicationBottomMargin=" + this.mIndicationBottomMargin);
        this.mBurnInYOffset = getResources().getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mIndicationArea.getLayoutParams();
        int i = marginLayoutParams.bottomMargin;
        int i2 = this.mIndicationBottomMargin;
        if (i != i2) {
            marginLayoutParams.bottomMargin = i2;
            this.mIndicationArea.setLayoutParams(marginLayoutParams);
        }
        this.mIndicationTextBottom.setTextSize(0, getResources().getDimensionPixelSize(17105555));
        this.mIndicationText.setTextSize(0, getResources().getDimensionPixelSize(17105555));
        ViewGroup.LayoutParams layoutParams = this.mRightAffordanceView.getLayoutParams();
        Resources resources = getResources();
        int i3 = R$dimen.keyguard_affordance_width;
        layoutParams.width = resources.getDimensionPixelSize(i3);
        Resources resources2 = getResources();
        int i4 = R$dimen.keyguard_affordance_height;
        layoutParams.height = resources2.getDimensionPixelSize(i4);
        this.mRightAffordanceView.setLayoutParams(layoutParams);
        updateRightAffordanceIcon();
        ViewGroup.LayoutParams layoutParams2 = this.mLeftAffordanceView.getLayoutParams();
        layoutParams2.width = getResources().getDimensionPixelSize(i3);
        layoutParams2.height = getResources().getDimensionPixelSize(i4);
        this.mLeftAffordanceView.setLayoutParams(layoutParams2);
        updateLeftAffordanceIcon();
        ViewGroup.LayoutParams layoutParams3 = this.mWalletButton.getLayoutParams();
        Resources resources3 = getResources();
        int i5 = R$dimen.keyguard_affordance_fixed_width;
        layoutParams3.width = resources3.getDimensionPixelSize(i5);
        Resources resources4 = getResources();
        int i6 = R$dimen.keyguard_affordance_fixed_height;
        layoutParams3.height = resources4.getDimensionPixelSize(i6);
        this.mWalletButton.setLayoutParams(layoutParams3);
        ViewGroup.LayoutParams layoutParams4 = this.mControlsButton.getLayoutParams();
        layoutParams4.width = getResources().getDimensionPixelSize(i5);
        layoutParams4.height = getResources().getDimensionPixelSize(i6);
        this.mControlsButton.setLayoutParams(layoutParams4);
        this.mIndicationPadding = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateAffordanceColors();
    }

    private void updateRightAffordanceIcon() {
        IntentButtonProvider.IntentButton.IconState icon = this.mRightButton.getIcon();
        this.mRightAffordanceView.setVisibility((this.mDozing || !icon.isVisible) ? 8 : 0);
        if (icon.drawable != this.mRightAffordanceView.getDrawable() || icon.tint != this.mRightAffordanceView.shouldTint()) {
            this.mRightAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mRightAffordanceView.setContentDescription(icon.contentDescription);
    }

    public void setStatusBar(StatusBar statusBar) {
        this.mStatusBar = statusBar;
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
        return ((FrameLayout) this).mContext.getPackageManager().resolveActivityAsUser(getCameraIntent(), 65536, KeyguardUpdateMonitor.getCurrentUser());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCameraVisibility() {
        KeyguardAffordanceView keyguardAffordanceView = this.mRightAffordanceView;
        if (keyguardAffordanceView == null) {
            return;
        }
        keyguardAffordanceView.setVisibility((this.mDozing || !this.mShowCameraAffordance || !this.mRightButton.getIcon().isVisible) ? 8 : 0);
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
        if (icon.drawable != this.mLeftAffordanceView.getDrawable() || icon.tint != this.mLeftAffordanceView.shouldTint()) {
            this.mLeftAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mLeftAffordanceView.setContentDescription(icon.contentDescription);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWalletVisibility() {
        QuickAccessWalletController quickAccessWalletController;
        if (this.mDozing || (quickAccessWalletController = this.mQuickAccessWalletController) == null || !quickAccessWalletController.isWalletEnabled() || !this.mHasCard) {
            this.mWalletButton.setVisibility(8);
            if (this.mControlsButton.getVisibility() != 8) {
                return;
            }
            this.mIndicationArea.setPadding(0, 0, 0, 0);
            return;
        }
        this.mWalletButton.setVisibility(0);
        this.mWalletButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                KeyguardBottomAreaView.this.onWalletClick(view);
            }
        });
        ViewGroup viewGroup = this.mIndicationArea;
        int i = this.mIndicationPadding;
        viewGroup.setPadding(i, 0, i, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateControlsVisibility() {
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent == null) {
            return;
        }
        boolean booleanValue = ((Boolean) controlsComponent.getControlsController().map(KeyguardBottomAreaView$$ExternalSyntheticLambda8.INSTANCE).orElse(Boolean.FALSE)).booleanValue();
        Log.i("KeyguardBottomAreaView", "updateControlsVisibility  mDozing=" + this.mDozing + ", hasFavorites=" + booleanValue + ", mControlServicesAvailable=" + this.mControlServicesAvailable + ", mControlsComponent.getVisibility()=" + this.mControlsComponent.getVisibility());
        if (this.mDozing || !booleanValue || !this.mControlServicesAvailable || this.mControlsComponent.getVisibility() != ControlsComponent.Visibility.AVAILABLE) {
            this.mControlsButton.setVisibility(8);
            if (this.mWalletButton.getVisibility() != 8) {
                return;
            }
            this.mIndicationArea.setPadding(0, 0, 0, 0);
            return;
        }
        this.mControlsButton.setVisibility(0);
        this.mControlsButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                KeyguardBottomAreaView.this.onControlsClick(view);
            }
        });
        ViewGroup viewGroup = this.mIndicationArea;
        int i = this.mIndicationPadding;
        viewGroup.setPadding(i, 0, i, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$updateControlsVisibility$7(ControlsController controlsController) {
        return Boolean.valueOf(controlsController.getFavorites().size() > 0);
    }

    public boolean isLeftVoiceAssist() {
        return this.mLeftIsVoiceAssist;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhoneVisible() {
        PackageManager packageManager = ((FrameLayout) this).mContext.getPackageManager();
        return packageManager.hasSystemFeature("android.hardware.telephony") && packageManager.resolveActivity(PHONE_INTENT, 0) != null;
    }

    @Override // com.android.systemui.statusbar.policy.AccessibilityController.AccessibilityStateChangedCallback
    public void onStateChanged(boolean z, boolean z2) {
        this.mRightAffordanceView.setClickable(z2);
        this.mLeftAffordanceView.setClickable(z2);
        this.mRightAffordanceView.setFocusable(z);
        this.mLeftAffordanceView.setFocusable(z);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mRightAffordanceView) {
            launchCamera("lockscreen_affordance");
        } else if (view != this.mLeftAffordanceView) {
        } else {
            launchLeftAffordance();
        }
    }

    public void bindCameraPrewarmService() {
        Bundle bundle;
        String string;
        ActivityInfo targetActivityInfo = this.mActivityIntentHelper.getTargetActivityInfo(getCameraIntent(), KeyguardUpdateMonitor.getCurrentUser(), true);
        if (targetActivityInfo == null || (bundle = targetActivityInfo.metaData) == null || (string = bundle.getString("android.media.still_image_camera_preview_service")) == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(targetActivityInfo.packageName, string);
        intent.setAction("android.service.media.CameraPrewarmService.ACTION_PREWARM");
        try {
            if (!getContext().bindServiceAsUser(intent, this.mPrewarmConnection, 67108865, new UserHandle(-2))) {
                return;
            }
            this.mPrewarmBound = true;
        } catch (SecurityException e) {
            Log.w("KeyguardBottomAreaView", "Unable to bind to prewarm service package=" + targetActivityInfo.packageName + " class=" + string, e);
        }
    }

    public void unbindCameraPrewarmService(boolean z) {
        if (this.mPrewarmBound) {
            Messenger messenger = this.mPrewarmMessenger;
            if (messenger != null && z) {
                try {
                    messenger.send(Message.obtain((Handler) null, 1));
                } catch (RemoteException e) {
                    Log.w("KeyguardBottomAreaView", "Error sending camera fired message", e);
                }
            }
            ((FrameLayout) this).mContext.unbindService(this.mPrewarmConnection);
            this.mPrewarmBound = false;
        }
    }

    public void launchCamera(String str) {
        final Intent cameraIntent = getCameraIntent();
        cameraIntent.putExtra("com.android.systemui.camera_launch_source", str);
        boolean wouldLaunchResolverActivity = this.mActivityIntentHelper.wouldLaunchResolverActivity(cameraIntent, KeyguardUpdateMonitor.getCurrentUser());
        if (CameraIntents.isSecureCameraIntent(cameraIntent) && !wouldLaunchResolverActivity) {
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.4
                @Override // java.lang.Runnable
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
                        Log.w("KeyguardBottomAreaView", "Unable to start camera activity", e);
                        i = -96;
                    }
                    final boolean isSuccessfulLaunch = KeyguardBottomAreaView.isSuccessfulLaunch(i);
                    KeyguardBottomAreaView.this.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            KeyguardBottomAreaView.this.unbindCameraPrewarmService(isSuccessfulLaunch);
                        }
                    });
                }
            });
        } else {
            this.mActivityStarter.startActivity(cameraIntent, false, new ActivityStarter.Callback() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.5
                @Override // com.android.systemui.plugins.ActivityStarter.Callback
                public void onActivityStarted(int i) {
                    KeyguardBottomAreaView.this.unbindCameraPrewarmService(KeyguardBottomAreaView.isSuccessfulLaunch(i));
                }
            });
        }
    }

    public void setDarkAmount(float f) {
        if (f == this.mDarkAmount) {
            return;
        }
        this.mDarkAmount = f;
        dozeTimeTick();
    }

    public void launchLeftAffordance() {
        if (this.mLeftIsVoiceAssist) {
            launchVoiceAssist();
        } else {
            launchPhone();
        }
    }

    @VisibleForTesting
    void launchVoiceAssist() {
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.6
            @Override // java.lang.Runnable
            public void run() {
                ((AssistManager) Dependency.get(AssistManager.class)).launchVoiceAssistFromKeyguard();
            }
        };
        if (!this.mKeyguardStateController.canDismissLockScreen()) {
            ((Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR)).execute(runnable);
        } else {
            this.mStatusBar.executeRunnableDismissingKeyguard(runnable, null, !TextUtils.isEmpty(this.mRightButtonStr) && ((TunerService) Dependency.get(TunerService.class)).getValue("sysui_keyguard_right_unlock", 1) != 0, false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canLaunchVoiceAssist() {
        return ((AssistManager) Dependency.get(AssistManager.class)).canVoiceAssistBeLaunchedFromKeyguard();
    }

    private void launchPhone() {
        final TelecomManager from = TelecomManager.from(((FrameLayout) this).mContext);
        if (from.isInCall()) {
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.7
                @Override // java.lang.Runnable
                public void run() {
                    from.showInCallScreen(false);
                }
            });
            return;
        }
        boolean z = true;
        if (TextUtils.isEmpty(this.mLeftButtonStr) || ((TunerService) Dependency.get(TunerService.class)).getValue("sysui_keyguard_left_unlock", 1) == 0) {
            z = false;
        }
        this.mActivityStarter.startActivity(this.mLeftButton.getIntent(), z);
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
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

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onUnlockedChanged() {
        updateCameraVisibility();
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardShowingChanged() {
        QuickAccessWalletController quickAccessWalletController;
        if (!this.mKeyguardStateController.isShowing() || (quickAccessWalletController = this.mQuickAccessWalletController) == null) {
            return;
        }
        quickAccessWalletController.queryWalletCards(this.mCardRetriever);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void inflateCameraPreview() {
        boolean z;
        View inflatePreview;
        KeyguardAffordanceHelper keyguardAffordanceHelper;
        ViewGroup viewGroup = this.mPreviewContainer;
        if (viewGroup == null) {
            return;
        }
        View view = this.mCameraPreview;
        int i = 0;
        if (view != null) {
            viewGroup.removeView(view);
            if (view.getVisibility() == 0) {
                z = true;
                inflatePreview = this.mPreviewInflater.inflatePreview(getCameraIntent());
                this.mCameraPreview = inflatePreview;
                if (inflatePreview != null) {
                    this.mPreviewContainer.addView(inflatePreview);
                    View view2 = this.mCameraPreview;
                    if (!z) {
                        i = 4;
                    }
                    view2.setVisibility(i);
                }
                keyguardAffordanceHelper = this.mAffordanceHelper;
                if (keyguardAffordanceHelper != null) {
                    return;
                }
                keyguardAffordanceHelper.updatePreviews();
                return;
            }
        }
        z = false;
        inflatePreview = this.mPreviewInflater.inflatePreview(getCameraIntent());
        this.mCameraPreview = inflatePreview;
        if (inflatePreview != null) {
        }
        keyguardAffordanceHelper = this.mAffordanceHelper;
        if (keyguardAffordanceHelper != null) {
        }
    }

    private void updateLeftPreview() {
        ViewGroup viewGroup = this.mPreviewContainer;
        if (viewGroup == null) {
            return;
        }
        View view = this.mLeftPreview;
        if (view != null) {
            viewGroup.removeView(view);
        }
        if (this.mLeftIsVoiceAssist) {
            if (((AssistManager) Dependency.get(AssistManager.class)).getVoiceInteractorComponentName() != null) {
                this.mLeftPreview = this.mPreviewInflater.inflatePreviewFromService(((AssistManager) Dependency.get(AssistManager.class)).getVoiceInteractorComponentName());
            }
        } else {
            this.mLeftPreview = this.mPreviewInflater.inflatePreview(this.mLeftButton.getIntent());
        }
        View view2 = this.mLeftPreview;
        if (view2 != null) {
            this.mPreviewContainer.addView(view2);
            this.mLeftPreview.setVisibility(4);
        }
        KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
        if (keyguardAffordanceHelper == null) {
            return;
        }
        keyguardAffordanceHelper.updatePreviews();
    }

    public void startFinishDozeAnimation() {
        long j = 0;
        if (this.mWalletButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mWalletButton, 0L);
        }
        if (this.mControlsButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mControlsButton, 0L);
        }
        if (this.mLeftAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mLeftAffordanceView, 0L);
            j = 48;
        }
        if (this.mRightAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mRightAffordanceView, j);
        }
    }

    private void startFinishDozeAnimationElement(View view, long j) {
        view.setAlpha(0.0f);
        view.setTranslationY(view.getHeight() / 2);
        view.animate().alpha(1.0f).translationY(0.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(j).setDuration(250L);
    }

    public void updateLeftAffordance() {
        updateLeftAffordanceIcon();
        updateLeftPreview();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setRightButton */
    public void lambda$onAttachedToWindow$2(IntentButtonProvider.IntentButton intentButton) {
        this.mRightButton = intentButton;
        updateRightAffordanceIcon();
        updateCameraVisibility();
        inflateCameraPreview();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setLeftButton */
    public void lambda$onAttachedToWindow$5(IntentButtonProvider.IntentButton intentButton) {
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
        if (z) {
            this.mOverlayContainer.setVisibility(4);
            return;
        }
        this.mOverlayContainer.setVisibility(0);
        if (!z2) {
            return;
        }
        startFinishDozeAnimation();
    }

    public void dozeTimeTick() {
        this.mIndicationArea.setTranslationY((BurnInHelperKt.getBurnInOffset(this.mBurnInYOffset * 2, false) - this.mBurnInYOffset) * this.mDarkAmount);
    }

    public void setAntiBurnInOffsetX(int i) {
        if (this.mBurnInXOffset == i) {
            return;
        }
        this.mBurnInXOffset = i;
        this.mIndicationArea.setTranslationX(i);
    }

    public void setAffordanceAlpha(float f) {
        this.mLeftAffordanceView.setAlpha(f);
        this.mRightAffordanceView.setAlpha(f);
        this.mIndicationArea.setAlpha(f);
        this.mWalletButton.setAlpha(f);
        this.mControlsButton.setAlpha(f);
        this.mOwnInfoIndicationView.setAlpha(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DefaultLeftButton implements IntentButtonProvider.IntentButton {
        private IntentButtonProvider.IntentButton.IconState mIconState;

        private DefaultLeftButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public IntentButtonProvider.IntentButton.IconState getIcon() {
            KeyguardBottomAreaView keyguardBottomAreaView = KeyguardBottomAreaView.this;
            keyguardBottomAreaView.mLeftIsVoiceAssist = keyguardBottomAreaView.canLaunchVoiceAssist();
            boolean z = true;
            if (KeyguardBottomAreaView.this.mLeftIsVoiceAssist) {
                IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance) {
                    z = false;
                }
                iconState.isVisible = z;
                if (KeyguardBottomAreaView.this.mLeftAssistIcon == null) {
                    this.mIconState.drawable = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getDrawable(R$drawable.ic_mic_26dp);
                } else {
                    this.mIconState.drawable = KeyguardBottomAreaView.this.mLeftAssistIcon;
                }
                this.mIconState.contentDescription = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getString(R$string.accessibility_voice_assist_button);
            } else {
                IntentButtonProvider.IntentButton.IconState iconState2 = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance || !KeyguardBottomAreaView.this.isPhoneVisible()) {
                    z = false;
                }
                iconState2.isVisible = z;
                this.mIconState.drawable = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getDrawable(17302807);
                this.mIconState.contentDescription = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getString(R$string.accessibility_phone_button);
            }
            return this.mIconState;
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public Intent getIntent() {
            return KeyguardBottomAreaView.PHONE_INTENT;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DefaultRightButton implements IntentButtonProvider.IntentButton {
        private IntentButtonProvider.IntentButton.IconState mIconState;

        private DefaultRightButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public IntentButtonProvider.IntentButton.IconState getIcon() {
            boolean z = true;
            boolean z2 = KeyguardBottomAreaView.this.mStatusBar != null && !KeyguardBottomAreaView.this.mStatusBar.isCameraAllowedByAdmin();
            IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
            if (z2 || !KeyguardBottomAreaView.this.mShowCameraAffordance || !KeyguardBottomAreaView.this.mUserSetupComplete || KeyguardBottomAreaView.this.resolveCameraIntent() == null) {
                z = false;
            }
            iconState.isVisible = z;
            this.mIconState.drawable = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getDrawable(R$drawable.ic_camera_alt_24dp);
            this.mIconState.contentDescription = ((FrameLayout) KeyguardBottomAreaView.this).mContext.getString(R$string.accessibility_camera_button);
            return this.mIconState;
        }

        @Override // com.android.systemui.plugins.IntentButtonProvider.IntentButton
        public Intent getIntent() {
            boolean canDismissLockScreen = KeyguardBottomAreaView.this.mKeyguardStateController.canDismissLockScreen();
            if (KeyguardBottomAreaView.this.mKeyguardStateController.isMethodSecure() && !canDismissLockScreen) {
                return CameraIntents.getSecureCameraIntent(KeyguardBottomAreaView.this.getContext());
            }
            return CameraIntents.getInsecureCameraIntent(KeyguardBottomAreaView.this.getContext());
        }
    }

    @Override // android.view.View
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

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAffordanceColors() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842806);
        this.mWalletButton.getDrawable().setTint(colorAttrDefaultColor);
        this.mControlsButton.getDrawable().setTint(colorAttrDefaultColor);
        ColorStateList colorAttr = Utils.getColorAttr(((FrameLayout) this).mContext, 17956909);
        this.mWalletButton.setBackgroundTintList(colorAttr);
        this.mControlsButton.setBackgroundTintList(colorAttr);
    }

    public void initControls(ControlsComponent controlsComponent) {
        this.mControlsComponent = controlsComponent;
        controlsComponent.getControlsListingController().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.phone.KeyguardBottomAreaView$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyguardBottomAreaView.this.lambda$initControls$8((ControlsListingController) obj);
            }
        });
        updateAffordanceColors();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initControls$8(ControlsListingController controlsListingController) {
        controlsListingController.addCallback(this.mListingCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWalletClick(View view) {
        if (this.mFalsingManager.isFalseTap(1)) {
            return;
        }
        if (this.mHasCard) {
            ((FrameLayout) this).mContext.startActivity(new Intent(((FrameLayout) this).mContext, WalletActivity.class).setAction("android.intent.action.VIEW").addFlags(335544320));
        } else if (this.mQuickAccessWalletController.getWalletClient().createWalletIntent() == null) {
            Log.w("KeyguardBottomAreaView", "Could not get intent of the wallet app.");
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(this.mQuickAccessWalletController.getWalletClient().createWalletIntent(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onControlsClick(View view) {
        if (this.mFalsingManager.isFalseTap(1)) {
            return;
        }
        Intent putExtra = new Intent(((FrameLayout) this).mContext, ControlsActivity.class).addFlags(335544320).putExtra("extra_animate", true);
        if (this.mControlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
            ((FrameLayout) this).mContext.startActivity(putExtra);
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(putExtra, 0);
        }
    }

    /* loaded from: classes.dex */
    private class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        private WalletCardRetriever() {
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            KeyguardBottomAreaView.this.mHasCard = !getWalletCardsResponse.getWalletCards().isEmpty();
            Drawable tileIcon = KeyguardBottomAreaView.this.mQuickAccessWalletController.getWalletClient().getTileIcon();
            if (tileIcon != null) {
                KeyguardBottomAreaView.this.mWalletButton.setImageDrawable(tileIcon);
            }
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            KeyguardBottomAreaView.this.mHasCard = false;
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }
    }

    private void updateBottomMargin() {
        int i = ((FrameLayout) this).mContext.getResources().getDisplayMetrics().heightPixels;
        Log.i("KeyguardBottomAreaView", "heightPixels=" + i);
        if (i > 0) {
            this.mIndicationBottomMargin = (int) (i * 0.191f);
        } else {
            this.mIndicationBottomMargin = getResources().getDimensionPixelSize(R$dimen.nothing_keyguard_indication_margin_bottom);
        }
    }
}
