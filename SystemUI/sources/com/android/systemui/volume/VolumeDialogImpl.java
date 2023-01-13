package com.android.systemui.volume;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.media.AudioSystem;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Trace;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.slice.core.SliceHints;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.Prefs;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.charging.WirelessChargingAnimation;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.AlphaTintDrawableWrapper;
import com.android.systemui.util.RoundedCornerProgressDrawable;
import com.android.systemui.volume.SystemUIInterpolators;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.systemui.volume.VolumeDialogImplEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class VolumeDialogImpl implements VolumeDialog, ConfigurationController.ConfigurationListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    static final int DIALOG_HOVERING_TIMEOUT_MILLIS = 16000;
    static final int DIALOG_ODI_CAPTIONS_TOOLTIP_TIMEOUT_MILLIS = 5000;
    static final int DIALOG_SAFETYWARNING_TIMEOUT_MILLIS = 5000;
    static final int DIALOG_TIMEOUT_MILLIS = 3000;
    private static final int DRAWER_ANIMATION_DURATION = 250;
    private static final int DRAWER_ANIMATION_DURATION_SHORT = 175;
    /* access modifiers changed from: private */
    public static final String TAG = Util.logTag(VolumeDialogImpl.class);
    private static final String TYPE_DISMISS = "dismiss";
    private static final String TYPE_SHOW = "show";
    private static final String TYPE_UPDATE = "update";
    private static final int UPDATE_ANIMATION_DURATION = 80;
    private static final long USER_ATTEMPT_GRACE_PERIOD = 1000;
    private boolean isDynamicRowShowWhenExpand = false;
    private final Accessibility mAccessibility = new Accessibility();
    private final AccessibilityManagerWrapper mAccessibilityMgr;
    private int mActiveStream;
    /* access modifiers changed from: private */
    public final ActivityManager mActivityManager;
    private final ActivityStarter mActivityStarter;
    private final ValueAnimator mAnimateUpBackgroundToMatchDrawer = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    private boolean mAutomute = true;
    private View mBackgroundContainer;
    private final boolean mChangeVolumeRowTintWhenInactive;
    /* access modifiers changed from: private */
    public boolean mConfigChanged = false;
    private ConfigurableTexts mConfigurableTexts;
    private final ConfigurationController mConfigurationController;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final VolumeDialogController mController;
    private final VolumeDialogController.Callbacks mControllerCallbackH = new VolumeDialogController.Callbacks() {
        public void onShowRequested(int i, boolean z, int i2) {
            VolumeDialogImpl.this.showH(i, z, i2);
        }

        public void onDismissRequested(int i) {
            VolumeDialogImpl.this.dismissH(i);
        }

        public void onScreenOff() {
            VolumeDialogImpl.this.dismissH(4);
        }

        public void onStateChanged(VolumeDialogController.State state) {
            VolumeDialogImpl.this.onStateChangedH(state);
        }

        public void onLayoutDirectionChanged(int i) {
            VolumeDialogImpl.this.mDialogView.setLayoutDirection(i);
        }

        public void onConfigurationChanged() {
            VolumeDialogImpl.this.mDialog.dismiss();
            boolean unused = VolumeDialogImpl.this.mConfigChanged = true;
            VolumeDialogImpl.this.hideRingerDrawer();
        }

        public void onShowVibrateHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(0, false);
            }
        }

        public void onShowSilentHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(2, false);
            }
        }

        public void onShowSafetyWarning(int i) {
            VolumeDialogImpl.this.showSafetyWarningH(i);
        }

        public void onAccessibilityModeChanged(Boolean bool) {
            boolean unused = VolumeDialogImpl.this.mShowA11yStream = bool == null ? false : bool.booleanValue();
            VolumeRow access$3100 = VolumeDialogImpl.this.getActiveRow();
            if (VolumeDialogImpl.this.mShowA11yStream || 10 != access$3100.stream) {
                VolumeDialogImpl.this.updateRowsH(access$3100);
            } else {
                VolumeDialogImpl.this.dismissH(7);
            }
        }

        public void onCaptionComponentStateChanged(Boolean bool, Boolean bool2) {
            VolumeDialogImpl.this.updateODICaptionsH(bool.booleanValue(), bool2.booleanValue());
        }
    };
    /* access modifiers changed from: private */
    public Consumer<Boolean> mCrossWindowBlurEnabledListener;
    private int mCurrentRingerMode = -1;
    private final DeviceProvisionedController mDeviceProvisionedController;
    /* access modifiers changed from: private */
    public CustomDialog mDialog;
    /* access modifiers changed from: private */
    public int mDialogCornerRadius;
    private final int mDialogHideAnimationDurationMs;
    /* access modifiers changed from: private */
    public ViewGroup mDialogRowsView;
    /* access modifiers changed from: private */
    public BackgroundBlurDrawable mDialogRowsViewBackground;
    /* access modifiers changed from: private */
    public ViewGroup mDialogRowsViewContainer;
    private final int mDialogShowAnimationDurationMs;
    /* access modifiers changed from: private */
    public ViewGroup mDialogView;
    private int mDialogWidth;
    private final SparseBooleanArray mDynamic = new SparseBooleanArray();
    private final ValueAnimator mExpandVolumeDialog = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    private ViewGroup mFakeRingerDrawerContainer;
    /* access modifiers changed from: private */
    public ViewGroup mFakeRingerDrawerVibrate;
    /* access modifiers changed from: private */
    public final C3311H mHandler = new C3311H();
    private boolean mHasSeenODICaptionsTooltip;
    private boolean mHovering = false;
    /* access modifiers changed from: private */
    public final InteractionJankMonitor mInteractionJankMonitor;
    private boolean mIsAnimatingDismiss = false;
    /* access modifiers changed from: private */
    public boolean mIsRingerDrawerOpen = false;
    /* access modifiers changed from: private */
    public final KeyguardManager mKeyguard;
    private final MediaOutputDialogFactory mMediaOutputDialogFactory;
    private CaptionsToggleImageButton mODICaptionsIcon;
    private View mODICaptionsTooltipView = null;
    private ViewStub mODICaptionsTooltipViewStub;
    private ViewGroup mODICaptionsView;
    private int mPrevActiveStream;
    private ViewGroup mRinger;
    /* access modifiers changed from: private */
    public View mRingerAndDrawerContainer;
    private Drawable mRingerAndDrawerContainerBackground;
    private int mRingerCount;
    private float mRingerDrawerClosedAmount = 0.0f;
    /* access modifiers changed from: private */
    public ViewGroup mRingerDrawerContainer;
    /* access modifiers changed from: private */
    public ImageView mRingerDrawerIconAnimatingDeselected;
    /* access modifiers changed from: private */
    public ImageView mRingerDrawerIconAnimatingSelected;
    private final ValueAnimator mRingerDrawerIconColorAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    /* access modifiers changed from: private */
    public int mRingerDrawerItemSize;
    private ViewGroup mRingerDrawerMute;
    private ImageView mRingerDrawerMuteIcon;
    private ViewGroup mRingerDrawerNewSelectionBg;
    private ViewGroup mRingerDrawerNormal;
    private ImageView mRingerDrawerNormalIcon;
    /* access modifiers changed from: private */
    public ViewGroup mRingerDrawerVibrate;
    private ImageView mRingerDrawerVibrateIcon;
    private ImageButton mRingerIcon;
    /* access modifiers changed from: private */
    public int mRingerRowsPadding;
    private final List<VolumeRow> mRows = new ArrayList();
    /* access modifiers changed from: private */
    public SafetyWarningDialog mSafetyWarning;
    /* access modifiers changed from: private */
    public final Object mSafetyWarningLock = new Object();
    private TextView mSeeMoreButton;
    private ViewGroup mSelectedRingerContainer;
    private ImageView mSelectedRingerIcon;
    private ImageButton mSettingsIcon;
    private View mSettingsView;
    /* access modifiers changed from: private */
    public boolean mShowA11yStream;
    private final boolean mShowActiveStreamOnly;
    private final boolean mShowLowMediaVolumeIcon;
    private boolean mShowVibrate;
    /* access modifiers changed from: private */
    public boolean mShowing;
    /* access modifiers changed from: private */
    public boolean mSilentMode = true;
    /* access modifiers changed from: private */
    public VolumeDialogController.State mState;
    /* access modifiers changed from: private */
    public View mTopContainer;
    private final Region mTouchableRegion = new Region();
    private final boolean mUseBackgroundBlur;
    private View mVolumeDialogContentContainer;
    private Drawable mVolumeUITopContainerBackground;
    /* access modifiers changed from: private */
    public Window mWindow;
    private FrameLayout mZenIcon;

    private int getStringDescriptionResourceForRingerMode(int i) {
        return i != 0 ? i != 1 ? C1894R.string.volume_ringer_status_normal : C1894R.string.volume_ringer_status_vibrate : C1894R.string.volume_ringer_status_silent;
    }

    private boolean showActiveStreamOnly() {
        return true;
    }

    public VolumeDialogImpl(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter, InteractionJankMonitor interactionJankMonitor) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, C1894R.style.volume_dialog_theme);
        this.mContext = contextThemeWrapper;
        this.mController = volumeDialogController;
        this.mKeyguard = (KeyguardManager) contextThemeWrapper.getSystemService("keyguard");
        this.mActivityManager = (ActivityManager) contextThemeWrapper.getSystemService(SliceHints.HINT_ACTIVITY);
        this.mAccessibilityMgr = accessibilityManagerWrapper;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mConfigurationController = configurationController;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mActivityStarter = activityStarter;
        this.mShowActiveStreamOnly = showActiveStreamOnly();
        this.mHasSeenODICaptionsTooltip = Prefs.getBoolean(context, Prefs.Key.HAS_SEEN_ODI_CAPTIONS_TOOLTIP, false);
        this.mShowLowMediaVolumeIcon = contextThemeWrapper.getResources().getBoolean(C1894R.bool.config_showLowMediaVolumeIcon);
        this.mChangeVolumeRowTintWhenInactive = contextThemeWrapper.getResources().getBoolean(C1894R.bool.config_changeVolumeRowTintWhenInactive);
        this.mDialogShowAnimationDurationMs = contextThemeWrapper.getResources().getInteger(C1894R.integer.config_dialogShowAnimationDurationMs);
        this.mDialogHideAnimationDurationMs = contextThemeWrapper.getResources().getInteger(C1894R.integer.config_dialogHideAnimationDurationMs);
        boolean z = contextThemeWrapper.getResources().getBoolean(C1894R.bool.config_volumeDialogUseBackgroundBlur);
        this.mUseBackgroundBlur = z;
        this.mInteractionJankMonitor = interactionJankMonitor;
        if (z) {
            this.mCrossWindowBlurEnabledListener = new VolumeDialogImpl$$ExternalSyntheticLambda19(this, contextThemeWrapper.getColor(C1894R.C1895color.volume_dialog_background_color_above_blur), contextThemeWrapper.getColor(C1894R.C1895color.volume_dialog_background_color));
        }
        initDimens();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3324lambda$new$0$comandroidsystemuivolumeVolumeDialogImpl(int i, int i2, Boolean bool) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mDialogRowsViewBackground;
        if (!bool.booleanValue()) {
            i = i2;
        }
        backgroundBlurDrawable.setColor(i);
        this.mDialogRowsView.invalidate();
    }

    public void onUiModeChanged() {
        this.mContext.getTheme().applyStyle(this.mContext.getThemeResId(), true);
    }

    public void init(int i, VolumeDialog.Callback callback) {
        initDialog(this.mActivityManager.getLockTaskModeState());
        this.mAccessibility.init();
        this.mController.addCallback(this.mControllerCallbackH, this.mHandler);
        this.mController.getState();
        this.mConfigurationController.addCallback(this);
        ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).registerCallback();
    }

    public void destroy() {
        this.mController.removeCallback(this.mControllerCallbackH);
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mConfigurationController.removeCallback(this);
        ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).removeCallback();
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTouchableRegion.setEmpty();
        for (int i = 0; i < this.mDialogView.getChildCount(); i++) {
            unionViewBoundstoTouchableRegion(this.mDialogView.getChildAt(i));
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            unionViewBoundstoTouchableRegion(this.mODICaptionsTooltipView);
        }
        internalInsetsInfo.touchableRegion.set(this.mTouchableRegion);
    }

    private void unionViewBoundstoTouchableRegion(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        float f = (float) iArr[0];
        float f2 = (float) iArr[1];
        int width = view.getWidth();
        if (!this.mIsRingerDrawerOpen && view != this.mODICaptionsView) {
            int visibleRowsCount = getVisibleRowsCount(true);
            int i = this.mRingerDrawerItemSize;
            int i2 = this.mRingerRowsPadding;
            width -= visibleRowsCount * ((i2 * 2) + i);
            if (this.mShowA11yStream) {
                width += i + (i2 * 2);
            }
            if (this.mDynamic.get(this.mActiveStream) || this.mActiveStream == 0 || isInVoiceCall()) {
                width += this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2);
            }
        }
        this.mTouchableRegion.op((int) f, (int) f2, iArr[0] + width, iArr[1] + view.getHeight(), Region.Op.UNION);
    }

    private void initDialog(int i) {
        this.mDialog = new CustomDialog(this.mContext);
        initDimens();
        this.mConfigurableTexts = new ConfigurableTexts(this.mContext);
        this.mHovering = false;
        this.mShowing = false;
        Window window = this.mDialog.getWindow();
        this.mWindow = window;
        window.requestFeature(1);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(65538);
        this.mWindow.addFlags(17563688);
        this.mWindow.addPrivateFlags(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
        this.mWindow.setType(2020);
        this.mWindow.setWindowAnimations(16973828);
        WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
        attributes.format = -3;
        attributes.setTitle("VolumeDialogImpl");
        attributes.windowAnimations = -1;
        attributes.gravity = ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).getVolumeDialogPosition(this.mContext, shouldSlideInVolumeTray());
        attributes.y = ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).getVolumeDialogOffset(this.mContext, shouldSlideInVolumeTray());
        this.mWindow.setAttributes(attributes);
        this.mWindow.setLayout(-2, -2);
        this.mDialog.setContentView(C1894R.layout.volume_dialog);
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_dialog);
        this.mDialogView = viewGroup;
        viewGroup.setAlpha(0.0f);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setOnShowListener(new VolumeDialogImpl$$ExternalSyntheticLambda10(this));
        this.mDialog.setOnDismissListener(new VolumeDialogImpl$$ExternalSyntheticLambda12(this));
        this.mDialogView.setOnHoverListener(new VolumeDialogImpl$$ExternalSyntheticLambda13(this));
        this.mDialogRowsView = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_dialog_rows);
        if (this.mUseBackgroundBlur) {
            this.mDialogView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().addCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                    BackgroundBlurDrawable unused = VolumeDialogImpl.this.mDialogRowsViewBackground = view.getViewRootImpl().createBackgroundBlurDrawable();
                    Resources resources = VolumeDialogImpl.this.mContext.getResources();
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setCornerRadius((float) VolumeDialogImpl.this.mContext.getResources().getDimensionPixelSize(Utils.getThemeAttr(VolumeDialogImpl.this.mContext, 16844145)));
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setBlurRadius(resources.getDimensionPixelSize(C1894R.dimen.volume_dialog_background_blur_radius));
                    VolumeDialogImpl.this.mDialogRowsView.setBackground(VolumeDialogImpl.this.mDialogRowsViewBackground);
                }

                public void onViewDetachedFromWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().removeCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                }
            });
        }
        this.mDialogRowsViewContainer = (ViewGroup) this.mDialogView.findViewById(C1894R.C1898id.volume_dialog_rows_container);
        this.mTopContainer = this.mDialogView.findViewById(C1894R.C1898id.volume_dialog_top_container);
        this.mBackgroundContainer = this.mDialogView.findViewById(C1894R.C1898id.volume_dialog_bg_container);
        this.mVolumeDialogContentContainer = this.mDialogView.findViewById(C1894R.C1898id.volume_dialog_content_container);
        this.mRingerAndDrawerContainer = this.mDialogView.findViewById(C1894R.C1898id.volume_ringer_and_drawer_container);
        View view = this.mBackgroundContainer;
        if (view != null) {
            view.post(new VolumeDialogImpl$$ExternalSyntheticLambda14(this));
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.ringer);
        this.mRinger = viewGroup2;
        if (viewGroup2 != null) {
            this.mRingerIcon = (ImageButton) viewGroup2.findViewById(C1894R.C1898id.ringer_icon);
            this.mZenIcon = (FrameLayout) this.mRinger.findViewById(C1894R.C1898id.dnd_icon);
        }
        this.mSelectedRingerIcon = (ImageView) this.mDialog.findViewById(C1894R.C1898id.volume_new_ringer_active_icon);
        this.mSelectedRingerContainer = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_new_ringer_active_icon_container);
        this.mRingerDrawerMute = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_mute);
        this.mRingerDrawerNormal = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_normal);
        this.mRingerDrawerVibrate = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_vibrate);
        this.mRingerDrawerMuteIcon = (ImageView) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_mute_icon);
        this.mRingerDrawerVibrateIcon = (ImageView) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_vibrate_icon);
        this.mRingerDrawerNormalIcon = (ImageView) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_normal_icon);
        this.mRingerDrawerNewSelectionBg = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_selection_background);
        this.mFakeRingerDrawerVibrate = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_vibrate_fake);
        setupRingerDrawer();
        ViewGroup viewGroup3 = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.odi_captions);
        this.mODICaptionsView = viewGroup3;
        if (viewGroup3 != null) {
            this.mODICaptionsIcon = (CaptionsToggleImageButton) viewGroup3.findViewById(C1894R.C1898id.odi_captions_icon);
        }
        ViewStub viewStub = (ViewStub) this.mDialog.findViewById(C1894R.C1898id.odi_captions_tooltip_stub);
        this.mODICaptionsTooltipViewStub = viewStub;
        if (this.mHasSeenODICaptionsTooltip && viewStub != null) {
            this.mDialogView.removeView(viewStub);
            this.mODICaptionsTooltipViewStub = null;
        }
        this.mSettingsView = this.mDialog.findViewById(C1894R.C1898id.settings_container);
        this.mSettingsIcon = (ImageButton) this.mDialog.findViewById(C1894R.C1898id.settings);
        this.mSeeMoreButton = (TextView) this.mDialog.findViewById(C1894R.C1898id.see_more_button);
        if (this.mRows.isEmpty()) {
            addRow(3, C1894R.C1896drawable.ic_volume_media, C1894R.C1896drawable.ic_volume_media_mute, true, true);
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                addRow(2, C1894R.C1896drawable.ic_volume_ringer, C1894R.C1896drawable.ic_volume_ringer_mute, true, false);
                addRow(4, C1894R.C1896drawable.ic_alarm, C1894R.C1896drawable.ic_volume_alarm_mute, true, false);
                addRow(0, 17302817, 17302817, false, false);
                addRow(6, C1894R.C1896drawable.ic_volume_bt_sco, C1894R.C1896drawable.ic_volume_bt_sco, false, false);
                addRow(1, C1894R.C1896drawable.ic_volume_system, C1894R.C1896drawable.ic_volume_system_mute, false, false);
                addRow(10, C1894R.C1896drawable.ic_volume_accessibility, C1894R.C1896drawable.ic_volume_accessibility, true, false);
            }
        } else {
            addExistingRows();
        }
        updateRowsH(getActiveRow());
        initRingerH();
        initSettingsH(i);
        initODICaptionsH();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initDialog$2$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3319lambda$initDialog$2$comandroidsystemuivolumeVolumeDialogImpl(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        ViewGroup viewGroup = this.mDialogView;
        viewGroup.setTranslationX(((float) (-viewGroup.getWidth())) / 2.0f);
        this.mDialogView.setAlpha(0.0f);
        this.mDialogView.animate().alpha(1.0f).translationX(0.0f).setDuration((long) this.mDialogShowAnimationDurationMs).setListener(getJankListener(getDialogView(), TYPE_SHOW, 3000)).setInterpolator(new SystemUIInterpolators.LogDecelerateInterpolator()).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda16(this)).start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initDialog$1$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3318lambda$initDialog$1$comandroidsystemuivolumeVolumeDialogImpl() {
        ImageButton imageButton;
        if (!Prefs.getBoolean(this.mContext, Prefs.Key.TOUCHED_RINGER_TOGGLE, false) && (imageButton = this.mRingerIcon) != null) {
            imageButton.postOnAnimationDelayed(getSinglePressFor(imageButton), WirelessChargingAnimation.DURATION);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initDialog$3$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3320lambda$initDialog$3$comandroidsystemuivolumeVolumeDialogImpl(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initDialog$4$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ boolean m3321lambda$initDialog$4$comandroidsystemuivolumeVolumeDialogImpl(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        this.mHovering = actionMasked == 9 || actionMasked == 7;
        rescheduleTimeoutH();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initDialog$5$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3322lambda$initDialog$5$comandroidsystemuivolumeVolumeDialogImpl() {
        LayerDrawable layerDrawable = (LayerDrawable) this.mBackgroundContainer.getBackground();
        if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
            this.mVolumeUITopContainerBackground = layerDrawable.getDrawable(0);
            updateBackgroundClosedAmount();
        }
    }

    private void initDimens() {
        this.mDialogWidth = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.volume_dialog_panel_width);
        this.mDialogCornerRadius = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.volume_dialog_panel_width_half);
        this.mRingerDrawerItemSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.volume_ringer_drawer_item_size);
        this.mRingerRowsPadding = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.volume_dialog_ringer_rows_padding);
        boolean hasVibrator = this.mController.hasVibrator();
        this.mShowVibrate = hasVibrator;
        this.mRingerCount = hasVibrator ? 3 : 2;
    }

    /* access modifiers changed from: protected */
    public ViewGroup getDialogView() {
        return this.mDialogView;
    }

    private int getAlphaAttr(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{i});
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        obtainStyledAttributes.recycle();
        return (int) (f * 255.0f);
    }

    private boolean shouldSlideInVolumeTray() {
        return this.mContext.getDisplay().getRotation() != 0;
    }

    private boolean isLandscape() {
        return this.mContext.getResources().getConfiguration().orientation == 2;
    }

    private boolean isRtl() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public void setStreamImportant(int i, boolean z) {
        this.mHandler.obtainMessage(5, i, z ? 1 : 0).sendToTarget();
    }

    public void setAutomute(boolean z) {
        if (this.mAutomute != z) {
            this.mAutomute = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public void setSilentMode(boolean z) {
        if (this.mSilentMode != z) {
            this.mSilentMode = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    private void addRow(int i, int i2, int i3, boolean z, boolean z2) {
        addRow(i, i2, i3, z, z2, false);
    }

    private void addRow(int i, int i2, int i3, boolean z, boolean z2, boolean z3) {
        if (C3275D.BUG) {
            Slog.d(TAG, "Adding row for stream " + i);
        }
        VolumeRow volumeRow = new VolumeRow();
        initRow(volumeRow, i, i2, i3, z, z2);
        if (z3) {
            this.mDialogRowsView.addView(volumeRow.view, 0);
            this.mRows.add(0, volumeRow);
        } else if (i == 0 || i == 6) {
            VolumeRow findRow = findRow(3);
            int indexOfChild = this.mDialogRowsView.indexOfChild(findRow.view);
            int indexOf = this.mRows.indexOf(findRow);
            Log.d(TAG, "mediaViewIndex = " + indexOfChild + ", mediaRowIndex = " + indexOf);
            this.mDialogRowsView.addView(volumeRow.view, indexOfChild);
            this.mRows.add(indexOf, volumeRow);
        } else {
            this.mDialogRowsView.addView(volumeRow.view);
            this.mRows.add(volumeRow);
        }
    }

    private void addExistingRows() {
        int size = this.mRows.size();
        for (int i = 0; i < size; i++) {
            VolumeRow volumeRow = this.mRows.get(i);
            initRow(volumeRow, volumeRow.stream, volumeRow.iconRes, volumeRow.iconMuteRes, volumeRow.important, volumeRow.defaultStream);
            this.mDialogRowsView.addView(volumeRow.view);
            updateVolumeRowH(volumeRow);
        }
    }

    /* access modifiers changed from: private */
    public VolumeRow getActiveRow() {
        for (VolumeRow next : this.mRows) {
            if (next.stream == this.mActiveStream) {
                return next;
            }
        }
        for (VolumeRow next2 : this.mRows) {
            if (next2.stream == 3) {
                return next2;
            }
        }
        return this.mRows.get(0);
    }

    private VolumeRow findRow(int i) {
        for (VolumeRow next : this.mRows) {
            if (next.stream == i) {
                return next;
            }
        }
        return null;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("VolumeDialogImpl state:");
        printWriter.print("  mShowing: ");
        printWriter.println(this.mShowing);
        printWriter.print("  mActiveStream: ");
        printWriter.println(this.mActiveStream);
        printWriter.print("  mDynamic: ");
        printWriter.println((Object) this.mDynamic);
        printWriter.print("  mAutomute: ");
        printWriter.println(this.mAutomute);
        printWriter.print("  mSilentMode: ");
        printWriter.println(this.mSilentMode);
    }

    /* access modifiers changed from: private */
    public static int getImpliedLevel(SeekBar seekBar, int i) {
        int max = seekBar.getMax();
        int i2 = max / 100;
        int i3 = i2 - 1;
        if (i == 0) {
            return 0;
        }
        return i == max ? i2 : ((int) ((((float) i) / ((float) max)) * ((float) i3))) + 1;
    }

    private void initRow(VolumeRow volumeRow, int i, int i2, int i3, boolean z, boolean z2) {
        int unused = volumeRow.stream = i;
        int unused2 = volumeRow.iconRes = i2;
        int unused3 = volumeRow.iconMuteRes = i3;
        boolean unused4 = volumeRow.important = z;
        boolean unused5 = volumeRow.defaultStream = z2;
        AlphaTintDrawableWrapper alphaTintDrawableWrapper = null;
        View unused6 = volumeRow.view = this.mDialog.getLayoutInflater().inflate(C1894R.layout.volume_dialog_row, (ViewGroup) null);
        volumeRow.view.setId(volumeRow.stream);
        volumeRow.view.setTag(volumeRow);
        TextView unused7 = volumeRow.header = (TextView) volumeRow.view.findViewById(C1894R.C1898id.volume_row_header);
        volumeRow.header.setId(volumeRow.stream * 20);
        if (i == 10) {
            volumeRow.header.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }
        FrameLayout unused8 = volumeRow.dndIcon = (FrameLayout) volumeRow.view.findViewById(C1894R.C1898id.dnd_icon);
        SeekBar unused9 = volumeRow.slider = (SeekBar) volumeRow.view.findViewById(C1894R.C1898id.volume_row_slider);
        volumeRow.slider.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener(volumeRow));
        TextView unused10 = volumeRow.number = (TextView) volumeRow.view.findViewById(C1894R.C1898id.volume_number);
        ObjectAnimator unused11 = volumeRow.anim = null;
        LayerDrawable layerDrawable = (LayerDrawable) this.mContext.getDrawable(C1894R.C1896drawable.volume_row_seekbar);
        LayerDrawable layerDrawable2 = (LayerDrawable) ((RoundedCornerProgressDrawable) layerDrawable.findDrawableByLayerId(16908301)).getDrawable();
        Drawable unused12 = volumeRow.sliderProgressSolid = layerDrawable2.findDrawableByLayerId(C1894R.C1898id.volume_seekbar_progress_solid);
        Drawable findDrawableByLayerId = layerDrawable2.findDrawableByLayerId(C1894R.C1898id.volume_seekbar_progress_icon);
        if (findDrawableByLayerId != null) {
            alphaTintDrawableWrapper = (AlphaTintDrawableWrapper) ((RotateDrawable) findDrawableByLayerId).getDrawable();
        }
        AlphaTintDrawableWrapper unused13 = volumeRow.sliderProgressIcon = alphaTintDrawableWrapper;
        volumeRow.slider.setProgressDrawable(layerDrawable);
        ImageButton unused14 = volumeRow.icon = (ImageButton) volumeRow.view.findViewById(C1894R.C1898id.volume_row_icon);
        volumeRow.setIcon(i2, this.mContext.getTheme());
        if (volumeRow.icon == null) {
            return;
        }
        if (volumeRow.stream != 10) {
            volumeRow.icon.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda22(this, volumeRow, i));
        } else {
            volumeRow.icon.setImportantForAccessibility(2);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initRow$6$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3323lambda$initRow$6$comandroidsystemuivolumeVolumeDialogImpl(VolumeRow volumeRow, int i, View view) {
        int i2 = 0;
        boolean z = true;
        Events.writeEvent(7, Integer.valueOf(volumeRow.stream), Integer.valueOf(volumeRow.iconState));
        this.mController.setActiveStream(volumeRow.stream);
        if (volumeRow.stream == 2) {
            boolean hasVibrator = this.mController.hasVibrator();
            if (this.mState.ringerModeInternal != 2) {
                this.mController.setRingerMode(2, false);
                if (volumeRow.f401ss.level == 0) {
                    this.mController.setStreamVolume(i, 1);
                }
            } else if (hasVibrator) {
                this.mController.setRingerMode(1, false);
            } else {
                if (volumeRow.f401ss.level != 0) {
                    z = false;
                }
                VolumeDialogController volumeDialogController = this.mController;
                if (z) {
                    i2 = volumeRow.lastAudibleLevel;
                }
                volumeDialogController.setStreamVolume(i, i2);
            }
        } else {
            if (volumeRow.f401ss.level == volumeRow.f401ss.levelMin) {
                i2 = 1;
            }
            this.mController.setStreamVolume(i, i2 != 0 ? volumeRow.lastAudibleLevel : volumeRow.f401ss.levelMin);
        }
        long unused = volumeRow.userAttempt = 0;
    }

    /* access modifiers changed from: private */
    public void setRingerMode(int i) {
        Events.writeEvent(18, Integer.valueOf(i));
        incrementManualToggleCount();
        updateRingerH();
        provideTouchFeedbackH(i);
        this.mController.setRingerMode(i, false);
        maybeShowToastH(i);
    }

    private void setupRingerDrawer() {
        this.mRingerDrawerContainer = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_container);
        this.mFakeRingerDrawerContainer = (ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_drawer_container_fake);
        if (this.mRingerDrawerContainer != null) {
            if (!this.mShowVibrate) {
                this.mRingerDrawerVibrate.setVisibility(8);
                this.mFakeRingerDrawerVibrate.setVisibility(8);
            }
            ViewGroup viewGroup = this.mDialogView;
            viewGroup.setPadding(viewGroup.getPaddingLeft(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight() + getRingerDrawerOpenExtraSize(), this.mDialogView.getPaddingBottom());
            ((LinearLayout) this.mRingerDrawerContainer.findViewById(C1894R.C1898id.volume_drawer_options)).setOrientation(0);
            ((LinearLayout) this.mFakeRingerDrawerContainer.findViewById(C1894R.C1898id.volume_drawer_options_fake)).setOrientation(0);
            this.mSelectedRingerContainer.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda4(this));
            this.mRingerDrawerVibrate.setOnClickListener(new RingerDrawerItemClickListener(1));
            this.mRingerDrawerMute.setOnClickListener(new RingerDrawerItemClickListener(0));
            this.mRingerDrawerNormal.setOnClickListener(new RingerDrawerItemClickListener(2));
            int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(this.mContext);
            this.mRingerDrawerIconColorAnimator.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda5(this, Utils.getColorAttrDefaultColor(this.mContext, 16844002), colorAccentDefaultColor));
            this.mRingerDrawerIconColorAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingDeselected.clearColorFilter();
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingSelected.clearColorFilter();
                }
            });
            this.mRingerDrawerIconColorAnimator.setDuration(175);
            this.mAnimateUpBackgroundToMatchDrawer.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda6(this));
            this.mAnimateUpBackgroundToMatchDrawer.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animator, boolean z) {
                    super.onAnimationStart(animator, z);
                }
            });
            this.mExpandVolumeDialog.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    VolumeDialogImpl.this.mDialogRowsViewContainer.getLayoutParams().width = VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2) + ((int) (((float) (VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2))) * floatValue * ((float) (VolumeDialogImpl.this.getVisibleRowsCount(true) - 1))));
                    VolumeDialogImpl.this.mRingerAndDrawerContainer.getLayoutParams().width = VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2) + ((int) (((float) (VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2))) * floatValue * ((float) (VolumeDialogImpl.this.getVisibleRowsCount(true) - 1))));
                    VolumeDialogImpl.this.mRingerDrawerContainer.getLayoutParams().width = ((int) (((floatValue * ((float) (VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2)))) * ((float) (VolumeDialogImpl.this.getVisibleRowsCount(true) - 1))) - ((float) VolumeDialogImpl.this.mRingerRowsPadding))) + VolumeDialogImpl.this.mRingerDrawerItemSize + VolumeDialogImpl.this.mRingerRowsPadding;
                    VolumeDialogImpl.this.mTopContainer.requestLayout();
                }
            });
            this.mExpandVolumeDialog.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animator, boolean z) {
                    super.onAnimationStart(animator, z);
                    if (!z) {
                        VolumeDialogImpl.this.mDialogRowsViewContainer.getLayoutParams().width = VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2);
                        VolumeDialogImpl.this.mRingerDrawerContainer.getLayoutParams().width = VolumeDialogImpl.this.mRingerDrawerItemSize + VolumeDialogImpl.this.mRingerRowsPadding;
                        VolumeDialogImpl.this.mTopContainer.requestLayout();
                        VolumeDialogImpl.this.mRingerDrawerContainer.setAlpha(1.0f);
                        VolumeDialogImpl.this.mRingerDrawerContainer.setVisibility(0);
                    }
                }

                public void onAnimationEnd(Animator animator, boolean z) {
                    super.onAnimationEnd(animator);
                    if (z) {
                        VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                        volumeDialogImpl.updateRowsH(volumeDialogImpl.getActiveRow());
                        int access$2500 = VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2) + (VolumeDialogImpl.this.mRingerRowsPadding * 2);
                        VolumeDialogImpl.this.mRingerDrawerVibrate.getLayoutParams().width = access$2500;
                        VolumeDialogImpl.this.mFakeRingerDrawerVibrate.getLayoutParams().width = access$2500;
                        VolumeDialogImpl.this.mDialogRowsViewContainer.getLayoutParams().width = (VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2)) * 3;
                        VolumeDialogImpl.this.mRingerAndDrawerContainer.getLayoutParams().width = (VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2)) * 3;
                        VolumeDialogImpl.this.mRingerDrawerContainer.getLayoutParams().width = ((VolumeDialogImpl.this.mRingerDrawerItemSize + (VolumeDialogImpl.this.mRingerRowsPadding * 2)) * 3) - (VolumeDialogImpl.this.mRingerRowsPadding * 2);
                        VolumeDialogImpl.this.mTopContainer.requestLayout();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupRingerDrawer$7$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47360x9a18dd58(View view) {
        if (!this.mIsRingerDrawerOpen) {
            int i = this.mState.ringerModeInternal;
            int i2 = this.mState.ringerModeInternal;
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 == 2) {
                        if (this.mShowVibrate) {
                            i = 1;
                        }
                    }
                }
                i = 0;
            } else {
                i = 2;
            }
            setRingerMode(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupRingerDrawer$8$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47361x53906af7(int i, int i2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
        int intValue2 = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i2), Integer.valueOf(i))).intValue();
        this.mRingerDrawerIconAnimatingDeselected.setColorFilter(intValue);
        this.mRingerDrawerIconAnimatingSelected.setColorFilter(intValue2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupRingerDrawer$9$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47362xd07f896(ValueAnimator valueAnimator) {
        this.mRingerDrawerClosedAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateBackgroundClosedAmount();
    }

    private ImageView getDrawerIconViewForMode(int i) {
        if (i == 1) {
            return this.mRingerDrawerVibrateIcon;
        }
        if (i == 0) {
            return this.mRingerDrawerMuteIcon;
        }
        return this.mRingerDrawerNormalIcon;
    }

    private float getTranslationInDrawerForRingerMode(int i) {
        int i2;
        if (i == 1) {
            i2 = (-this.mRingerDrawerItemSize) * 2;
        } else if (i != 0) {
            return 0.0f;
        } else {
            i2 = -this.mRingerDrawerItemSize;
        }
        return (float) i2;
    }

    private float getTranslationInDrawerForRingerModePortrait(int i) {
        if (i == 2) {
            return 0.0f;
        }
        if (i != 1) {
            return (float) ((this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2)) * (getVisibleRowsCount(true) - 1));
        }
        return (((((float) getVisibleRowsCount(true)) - 3.0f) / 2.0f) + 1.0f) * ((float) (this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2)));
    }

    private void showRingerDrawer() {
        if (this.mIsRingerDrawerOpen) {
            Log.d(TAG, "showRingerDrawer - drawer is already open");
            return;
        }
        this.mRingerDrawerVibrateIcon.setVisibility(this.mState.ringerModeInternal == 1 ? 4 : 0);
        this.mRingerDrawerMuteIcon.setVisibility(this.mState.ringerModeInternal == 0 ? 4 : 0);
        this.mRingerDrawerNormalIcon.setVisibility(this.mState.ringerModeInternal == 2 ? 4 : 0);
        this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
        this.mRingerDrawerNewSelectionBg.setTranslationX(getTranslationInDrawerForRingerModePortrait(this.mState.ringerModeInternal));
        this.mSeeMoreButton.setTranslationX((float) ((((this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2)) * getVisibleRowsCount(true)) - this.mSeeMoreButton.getWidth()) / 2));
        int i = this.mRingerDrawerItemSize;
        int i2 = this.mRingerRowsPadding;
        this.mRingerDrawerVibrate.getLayoutParams().width = ((getVisibleRowsCount(true) - 2) * (i + (i2 * 2))) + (i2 * 2);
        this.mSeeMoreButton.setAlpha(1.0f);
        this.mSeeMoreButton.setVisibility(0);
        this.mSettingsIcon.setAlpha(0.0f);
        this.mSettingsIcon.setVisibility(4);
        long j = (long) (this.mState.ringerModeInternal == 1 ? 175 : 250);
        this.mExpandVolumeDialog.setDuration(j);
        this.mExpandVolumeDialog.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mExpandVolumeDialog.start();
        this.mSelectedRingerContainer.animate().setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setDuration(250).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda15(this));
        this.mAnimateUpBackgroundToMatchDrawer.setDuration(j);
        this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mAnimateUpBackgroundToMatchDrawer.start();
        this.mSelectedRingerContainer.animate().translationX(getTranslationInDrawerForRingerModePortrait(this.mState.ringerModeInternal)).start();
        this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(this.mState.ringerModeInternal)));
        this.mIsRingerDrawerOpen = true;
        updateRowsH(getActiveRow());
        Log.d(TAG, "showRingerDrawer");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showRingerDrawer$10$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47366x583d471e() {
        getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void hideRingerDrawer() {
        if (this.mRingerDrawerContainer == null) {
            Log.d(TAG, "hideRingerDrawer - mRingerDrawerContainer is null");
        } else if (!this.mIsRingerDrawerOpen) {
            Log.d(TAG, "hideRingerDrawer - drawer is already hide");
        } else {
            getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(4);
            this.mRingerDrawerContainer.setAlpha(0.0f);
            this.mRingerDrawerContainer.setVisibility(4);
            this.mSeeMoreButton.setAlpha(0.0f);
            this.mSeeMoreButton.setVisibility(4);
            this.mSeeMoreButton.setTranslationX(0.0f);
            this.mSettingsIcon.setAlpha(1.0f);
            this.mSettingsIcon.setVisibility(0);
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(250);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(Interpolators.FAST_OUT_SLOW_IN_REVERSE);
            this.mAnimateUpBackgroundToMatchDrawer.reverse();
            this.mSelectedRingerContainer.animate().translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(C1894R.string.volume_ringer_change));
            this.mIsRingerDrawerOpen = false;
            this.mExpandVolumeDialog.setDuration(250);
            this.mExpandVolumeDialog.setInterpolator(Interpolators.FAST_OUT_SLOW_IN_REVERSE);
            this.mExpandVolumeDialog.reverse();
            Log.d(TAG, "hideRingerDrawer caller:" + Debug.getCallers(4));
            this.isDynamicRowShowWhenExpand = false;
        }
    }

    private void initSettingsH(int i) {
        View view = this.mSettingsView;
        if (view != null) {
            view.setVisibility((!this.mDeviceProvisionedController.isCurrentUserSetup() || i != 0) ? 8 : 0);
        }
        ImageButton imageButton = this.mSettingsIcon;
        if (imageButton != null) {
            imageButton.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda17(this));
        }
        TextView textView = this.mSeeMoreButton;
        if (textView != null) {
            textView.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda18(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initSettingsH$11$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47356x41fc12ed(View view) {
        if (!this.mIsRingerDrawerOpen) {
            showRingerDrawer();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initSettingsH$12$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47357xfb73a08c(View view) {
        Events.writeEvent(8, new Object[0]);
        Intent addFlags = new Intent("android.settings.SOUND_SETTINGS").addFlags(268435456);
        dismissH(5);
        this.mMediaOutputDialogFactory.dismiss();
        this.mActivityStarter.startActivity(addFlags, true);
    }

    public void initRingerH() {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setAccessibilityLiveRegion(1);
            this.mRingerIcon.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda0(this));
        }
        updateRingerH();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0023, code lost:
        if (r2 != false) goto L_0x0038;
     */
    /* renamed from: lambda$initRingerH$13$com-android-systemui-volume-VolumeDialogImpl */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo47354x9854371(android.view.View r6) {
        /*
            r5 = this;
            android.content.Context r6 = r5.mContext
            java.lang.String r0 = "TouchedRingerToggle"
            r1 = 1
            com.android.systemui.Prefs.putBoolean(r6, r0, r1)
            com.android.systemui.plugins.VolumeDialogController$State r6 = r5.mState
            android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r6 = r6.states
            r0 = 2
            java.lang.Object r6 = r6.get(r0)
            com.android.systemui.plugins.VolumeDialogController$StreamState r6 = (com.android.systemui.plugins.VolumeDialogController.StreamState) r6
            if (r6 != 0) goto L_0x0016
            return
        L_0x0016:
            com.android.systemui.plugins.VolumeDialogController r2 = r5.mController
            boolean r2 = r2.hasVibrator()
            com.android.systemui.plugins.VolumeDialogController$State r3 = r5.mState
            int r3 = r3.ringerModeInternal
            r4 = 0
            if (r3 != r0) goto L_0x0026
            if (r2 == 0) goto L_0x002c
            goto L_0x0038
        L_0x0026:
            com.android.systemui.plugins.VolumeDialogController$State r2 = r5.mState
            int r2 = r2.ringerModeInternal
            if (r2 != r1) goto L_0x002e
        L_0x002c:
            r1 = r4
            goto L_0x0038
        L_0x002e:
            int r6 = r6.level
            if (r6 != 0) goto L_0x0037
            com.android.systemui.plugins.VolumeDialogController r6 = r5.mController
            r6.setStreamVolume(r0, r1)
        L_0x0037:
            r1 = r0
        L_0x0038:
            r5.setRingerMode(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.mo47354x9854371(android.view.View):void");
    }

    private void initODICaptionsH() {
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.setOnConfirmedTapListener(new VolumeDialogImpl$$ExternalSyntheticLambda7(this), this.mHandler);
        }
        this.mController.getCaptionsComponentState(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initODICaptionsH$14$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47353x3aad1cc6() {
        onCaptionIconClicked();
        Events.writeEvent(21, new Object[0]);
    }

    private void checkODICaptionsTooltip(boolean z) {
        boolean z2 = this.mHasSeenODICaptionsTooltip;
        if (!z2 && !z && this.mODICaptionsTooltipViewStub != null) {
            this.mController.getCaptionsComponentState(true);
        } else if (z2 && z && this.mODICaptionsTooltipView != null) {
            hideCaptionsTooltip();
        }
    }

    /* access modifiers changed from: protected */
    public void showCaptionsTooltip() {
        ViewStub viewStub;
        if (!this.mHasSeenODICaptionsTooltip && (viewStub = this.mODICaptionsTooltipViewStub) != null) {
            View inflate = viewStub.inflate();
            this.mODICaptionsTooltipView = inflate;
            inflate.findViewById(C1894R.C1898id.dismiss).setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda20(this));
            this.mODICaptionsTooltipViewStub = null;
            rescheduleTimeoutH();
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setAlpha(0.0f);
            this.mHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda21(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showCaptionsTooltip$15$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47363x6a34d0d(View view) {
        hideCaptionsTooltip();
        Events.writeEvent(22, new Object[0]);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showCaptionsTooltip$17$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47365x7992684b() {
        int[] locationOnScreen = this.mODICaptionsTooltipView.getLocationOnScreen();
        int[] locationOnScreen2 = this.mODICaptionsIcon.getLocationOnScreen();
        this.mODICaptionsTooltipView.setTranslationY(((float) (locationOnScreen2[1] - locationOnScreen[1])) - (((float) (this.mODICaptionsTooltipView.getHeight() - this.mODICaptionsIcon.getHeight())) / 2.0f));
        this.mODICaptionsTooltipView.animate().alpha(1.0f).setStartDelay((long) this.mDialogShowAnimationDurationMs).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda1(this)).start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showCaptionsTooltip$16$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47364xc01adaac() {
        if (C3275D.BUG) {
            Log.d(TAG, "tool:checkODICaptionsTooltip() putBoolean true");
        }
        Prefs.putBoolean(this.mContext, Prefs.Key.HAS_SEEN_ODI_CAPTIONS_TOOLTIP, true);
        this.mHasSeenODICaptionsTooltip = true;
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.postOnAnimation(getSinglePressFor(captionsToggleImageButton));
        }
    }

    private void hideCaptionsTooltip() {
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            this.mODICaptionsTooltipView.animate().cancel();
            this.mODICaptionsTooltipView.setAlpha(1.0f);
            this.mODICaptionsTooltipView.animate().alpha(0.0f).setStartDelay(0).setDuration((long) this.mDialogHideAnimationDurationMs).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda11(this)).start();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$hideCaptionsTooltip$18$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47347xf1dcbf05() {
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setVisibility(4);
        }
    }

    /* access modifiers changed from: protected */
    public void tryToRemoveCaptionsTooltip() {
        if (this.mHasSeenODICaptionsTooltip && this.mODICaptionsTooltipView != null) {
            ((ViewGroup) this.mDialog.findViewById(C1894R.C1898id.volume_dialog_container)).removeView(this.mODICaptionsTooltipView);
            this.mODICaptionsTooltipView = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateODICaptionsH(boolean z, boolean z2) {
        ViewGroup viewGroup = this.mODICaptionsView;
        if (viewGroup != null) {
            viewGroup.setVisibility(z ? 0 : 8);
        }
        if (z) {
            updateCaptionsIcon();
            if (z2) {
                showCaptionsTooltip();
            }
        }
    }

    private void updateCaptionsIcon() {
        boolean areCaptionsEnabled = this.mController.areCaptionsEnabled();
        if (this.mODICaptionsIcon.getCaptionsEnabled() != areCaptionsEnabled) {
            this.mHandler.post(this.mODICaptionsIcon.setCaptionsEnabled(areCaptionsEnabled));
        }
    }

    private void onCaptionIconClicked() {
        this.mController.setCaptionsEnabled(!this.mController.areCaptionsEnabled());
        updateCaptionsIcon();
    }

    private void incrementManualToggleCount() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Secure.putInt(contentResolver, "manual_ringer_toggle_count", Settings.Secure.getInt(contentResolver, "manual_ringer_toggle_count", 0) + 1);
    }

    private void provideTouchFeedbackH(int i) {
        if (i == 0) {
            VibrationEffect.get(0);
        } else if (i != 2) {
            VibrationEffect.get(1);
        } else {
            this.mController.scheduleTouchFeedback();
        }
    }

    private void maybeShowToastH(int i) {
        String str;
        int i2 = Prefs.getInt(this.mContext, Prefs.Key.SEEN_RINGER_GUIDANCE_COUNT, 0);
        if (i2 <= 12) {
            if (i == 0) {
                str = this.mContext.getString(17041694);
            } else if (i != 2) {
                str = this.mContext.getString(17041695);
            } else {
                VolumeDialogController.StreamState streamState = this.mState.states.get(2);
                if (streamState != null) {
                    str = this.mContext.getString(C1894R.string.volume_dialog_ringer_guidance_ring, new Object[]{Utils.formatPercentage((long) streamState.level, (long) streamState.levelMax)});
                } else {
                    str = null;
                }
            }
            Toast.makeText(this.mContext, str, 0).show();
            Prefs.putInt(this.mContext, Prefs.Key.SEEN_RINGER_GUIDANCE_COUNT, i2 + 1);
        }
    }

    public void show(int i) {
        this.mHandler.obtainMessage(1, i, 0).sendToTarget();
    }

    public void dismiss(int i) {
        this.mHandler.obtainMessage(2, i, 0).sendToTarget();
    }

    private Animator.AnimatorListener getJankListener(View view, String str, long j) {
        final View view2 = view;
        final String str2 = str;
        final long j2 = j;
        return new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                if (view2.isAttachedToWindow()) {
                    VolumeDialogImpl.this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(55, view2).setTag(str2).setTimeout(j2));
                } else if (C3275D.BUG) {
                    Log.d(VolumeDialogImpl.TAG, "onAnimationStart view do not attached to window:" + view2);
                }
            }

            public void onAnimationEnd(Animator animator) {
                VolumeDialogImpl.this.mInteractionJankMonitor.end(55);
            }

            public void onAnimationCancel(Animator animator) {
                VolumeDialogImpl.this.mInteractionJankMonitor.cancel(55);
            }
        };
    }

    /* access modifiers changed from: private */
    public void showH(int i, boolean z, int i2) {
        Trace.beginSection("VolumeDialogImpl#showH");
        if (C3275D.BUG) {
            Log.d(TAG, "showH r=" + Events.SHOW_REASONS[i]);
        }
        if (this.mExpandVolumeDialog.isRunning()) {
            NTLogUtil.m1686d(TAG, "ignore show dialog. Collapse anim is running.");
            return;
        }
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        rescheduleTimeoutH();
        if (this.mConfigChanged) {
            initDialog(i2);
            this.mConfigurableTexts.update();
            this.mConfigChanged = false;
        }
        initSettingsH(i2);
        this.mShowing = true;
        this.mIsAnimatingDismiss = false;
        this.mDialog.show();
        Events.writeEvent(0, Integer.valueOf(i), Boolean.valueOf(z));
        this.mController.notifyVisible(true);
        this.mController.getCaptionsComponentState(false);
        checkODICaptionsTooltip(false);
        updateBackgroundClosedAmount();
        Trace.endSection();
    }

    /* access modifiers changed from: protected */
    public void rescheduleTimeoutH() {
        this.mHandler.removeMessages(2);
        int computeTimeoutH = computeTimeoutH();
        C3311H h = this.mHandler;
        h.sendMessageDelayed(h.obtainMessage(2, 3, 0), (long) computeTimeoutH);
        NTLogUtil.m1686d(TAG, "rescheduleTimeout " + computeTimeoutH + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Debug.getCaller());
        this.mController.userActivity();
    }

    private int computeTimeoutH() {
        if (this.mHovering) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(DIALOG_HOVERING_TIMEOUT_MILLIS, 4);
        }
        if (this.mSafetyWarning != null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
        }
        if (this.mHasSeenODICaptionsTooltip || this.mODICaptionsTooltipView == null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(3000, 4);
        }
        return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
    }

    /* access modifiers changed from: protected */
    public void dismissH(int i) {
        Trace.beginSection("VolumeDialogImpl#dismissH");
        if (C3275D.BUG) {
            Log.d(TAG, "mDialog.dismiss() reason: " + Events.DISMISS_REASONS[i] + " from: " + Debug.getCaller());
        }
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        if (!this.mIsAnimatingDismiss) {
            this.mIsAnimatingDismiss = true;
            this.mDialogView.animate().cancel();
            if (this.mShowing) {
                this.mShowing = false;
                Events.writeEvent(1, Integer.valueOf(i));
            }
            this.mDialogView.setTranslationX(0.0f);
            this.mDialogView.setAlpha(1.0f);
            ViewPropertyAnimator withEndAction = this.mDialogView.animate().alpha(0.0f).setDuration((long) this.mDialogHideAnimationDurationMs).setInterpolator(new SystemUIInterpolators.LogAccelerateInterpolator()).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda23(this));
            withEndAction.translationX(((float) (-this.mDialogView.getWidth())) / 2.0f);
            withEndAction.setListener(getJankListener(getDialogView(), TYPE_DISMISS, (long) this.mDialogHideAnimationDurationMs)).start();
            checkODICaptionsTooltip(true);
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    if (C3275D.BUG) {
                        Log.d(TAG, "SafetyWarning dismissed");
                    }
                    this.mSafetyWarning.dismiss();
                }
            }
            Trace.endSection();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$dismissH$20$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3317lambda$dismissH$20$comandroidsystemuivolumeVolumeDialogImpl() {
        this.mHandler.postDelayed(new VolumeDialogImpl$$ExternalSyntheticLambda3(this), 50);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$dismissH$19$com-android-systemui-volume-VolumeDialogImpl  reason: not valid java name */
    public /* synthetic */ void m3316lambda$dismissH$19$comandroidsystemuivolumeVolumeDialogImpl() {
        this.mDialog.dismiss();
        tryToRemoveCaptionsTooltip();
        this.mIsAnimatingDismiss = false;
        this.mController.notifyVisible(false);
        hideRingerDrawer();
    }

    private boolean shouldBeVisibleH(VolumeRow volumeRow, VolumeRow volumeRow2) {
        if ((volumeRow.stream == volumeRow2.stream) && !this.mIsRingerDrawerOpen) {
            return true;
        }
        if (!this.mShowActiveStreamOnly) {
            if (volumeRow.stream == 10) {
                return this.mShowA11yStream;
            }
            if (volumeRow2.stream == 10 && volumeRow.stream == this.mPrevActiveStream) {
                return true;
            }
            if (volumeRow.defaultStream) {
                if (volumeRow2.stream == 2 || volumeRow2.stream == 4 || volumeRow2.stream == 0 || volumeRow2.stream == 10 || this.mDynamic.get(volumeRow2.stream)) {
                    return true;
                }
                return false;
            }
        }
        if (volumeRow.stream != 10 || this.mIsRingerDrawerOpen) {
            return volumeRow2.stream == 10 && volumeRow.stream == this.mPrevActiveStream;
        }
        return this.mShowA11yStream;
    }

    private boolean shouldBeVisibleExpendH(VolumeRow volumeRow, VolumeRow volumeRow2) {
        if (!this.mIsRingerDrawerOpen) {
            return false;
        }
        if (volumeRow.stream == 3 || volumeRow.stream == 2 || volumeRow.stream == 4) {
            return true;
        }
        if ((volumeRow.stream == volumeRow2.stream || this.isDynamicRowShowWhenExpand) && this.mDynamic.get(volumeRow.stream)) {
            this.isDynamicRowShowWhenExpand = true;
            return true;
        } else if ((volumeRow.stream == volumeRow2.stream || isInVoiceCall()) && volumeRow.stream == 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void updateRowsH(VolumeRow volumeRow) {
        Trace.beginSection("VolumeDialogImpl#updateRowsH");
        if (C3275D.BUG) {
            Log.d(TAG, "updateRowsH");
        }
        if (!this.mShowing) {
            trimObsoleteH();
        }
        int i = isRtl() ? -1 : 32767;
        Iterator<VolumeRow> it = this.mRows.iterator();
        while (it.hasNext()) {
            VolumeRow next = it.next();
            boolean z = true;
            boolean z2 = next == volumeRow;
            if (!shouldBeVisibleH(next, volumeRow) && !shouldBeVisibleExpendH(next, volumeRow)) {
                z = false;
            }
            Util.setVisOrGone(next.view, z);
            if (z && this.mRingerAndDrawerContainerBackground != null) {
                i = ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).getRightMostVisibleRowIndex(false, isRtl(), i, this.mDialogRowsView.indexOfChild(next.view));
                ViewGroup.LayoutParams layoutParams = next.view.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                }
                next.view.setBackgroundDrawable(this.mContext.getDrawable(C1894R.C1896drawable.volume_row_rounded_background));
            }
            if (next.view.isShown()) {
                updateVolumeRowTintH(next, z2);
            }
        }
        if (i > -1 && i < 32767) {
            View childAt = this.mDialogRowsView.getChildAt(i);
            ViewGroup.LayoutParams layoutParams3 = childAt.getLayoutParams();
            if (layoutParams3 instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
                layoutParams4.setMarginStart(0);
                layoutParams4.setMarginEnd(0);
                childAt.setBackgroundColor(0);
            }
        }
        updateBackgroundClosedAmount();
        Trace.endSection();
    }

    /* access modifiers changed from: protected */
    public void updateRingerH() {
        VolumeDialogController.State state;
        VolumeDialogController.StreamState streamState;
        if (this.mRinger != null && (state = this.mState) != null && (streamState = state.states.get(2)) != null) {
            boolean z = false;
            boolean z2 = this.mState.zenMode == 3 || this.mState.zenMode == 2 || (this.mState.zenMode == 1 && this.mState.disallowRinger);
            enableRingerViewsH(!z2);
            int i = this.mState.ringerModeInternal;
            if (i == 0) {
                this.mRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer_mute);
                this.mSelectedRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer_mute);
                this.mRingerIcon.setTag(2);
                addAccessibilityDescription(this.mRingerIcon, 0, this.mContext.getString(C1894R.string.volume_ringer_hint_unmute));
            } else if (i != 1) {
                if ((this.mAutomute && streamState.level == 0) || streamState.muted) {
                    z = true;
                }
                if (z2 || !z) {
                    this.mRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer);
                    this.mSelectedRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer);
                    if (this.mController.hasVibrator()) {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(C1894R.string.volume_ringer_hint_vibrate));
                    } else {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(C1894R.string.volume_ringer_hint_mute));
                    }
                    this.mRingerIcon.setTag(1);
                }
            } else {
                this.mRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer_vibrate);
                this.mSelectedRingerIcon.setImageResource(C1894R.C1896drawable.ic_volume_ringer_vibrate);
                addAccessibilityDescription(this.mRingerIcon, 1, this.mContext.getString(C1894R.string.volume_ringer_hint_mute));
                this.mRingerIcon.setTag(3);
            }
            ringerDrawerNewSelectionAnimation(this.mState.ringerModeInternal);
        }
    }

    private void addAccessibilityDescription(View view, int i, final String str) {
        view.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(i)));
        view.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }
        });
    }

    private void enableVolumeRowViewsH(VolumeRow volumeRow, boolean z) {
        volumeRow.dndIcon.setVisibility(z ^ true ? 0 : 8);
    }

    private void enableRingerViewsH(boolean z) {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setEnabled(z);
        }
        FrameLayout frameLayout = this.mZenIcon;
        if (frameLayout != null) {
            frameLayout.setVisibility(z ? 8 : 0);
        }
    }

    private void trimObsoleteH() {
        if (C3275D.BUG) {
            Log.d(TAG, "trimObsoleteH");
        }
        for (int size = this.mRows.size() - 1; size >= 0; size--) {
            VolumeRow volumeRow = this.mRows.get(size);
            if (volumeRow.f401ss != null && volumeRow.f401ss.dynamic && !this.mDynamic.get(volumeRow.stream)) {
                this.mRows.remove(size);
                this.mDialogRowsView.removeView(volumeRow.view);
                this.mConfigurableTexts.remove(volumeRow.header);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStateChangedH(VolumeDialogController.State state) {
        if (C3275D.BUG) {
            Log.d(TAG, "onStateChangedH() state: " + state.toString());
        }
        VolumeDialogController.State state2 = this.mState;
        if (!(state2 == null || state == null || state2.ringerModeInternal == -1 || this.mState.ringerModeInternal == state.ringerModeInternal || state.ringerModeInternal != 1)) {
            this.mController.vibrate(VibrationEffect.get(5));
        }
        this.mState = state;
        this.mDynamic.clear();
        for (int i = 0; i < state.states.size(); i++) {
            int keyAt = state.states.keyAt(i);
            if (state.states.valueAt(i).dynamic) {
                this.mDynamic.put(keyAt, true);
                if (findRow(keyAt) == null) {
                    addRow(keyAt, C1894R.C1896drawable.ic_volume_remote, C1894R.C1896drawable.ic_volume_remote_mute, true, false, true);
                }
            }
        }
        if (this.mActiveStream != state.activeStream) {
            this.mPrevActiveStream = this.mActiveStream;
            this.mActiveStream = state.activeStream;
            updateRowsH(getActiveRow());
            if (this.mShowing) {
                rescheduleTimeoutH();
            }
        }
        for (VolumeRow updateVolumeRowH : this.mRows) {
            updateVolumeRowH(updateVolumeRowH);
        }
        updateRingerH();
        this.mWindow.setTitle(composeWindowTitle());
    }

    /* access modifiers changed from: package-private */
    public CharSequence composeWindowTitle() {
        return this.mContext.getString(C1894R.string.volume_dialog_title, new Object[]{getStreamLabelH(getActiveRow().f401ss)});
    }

    /* JADX WARNING: Removed duplicated region for block: B:165:0x0297  */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x02cd  */
    /* JADX WARNING: Removed duplicated region for block: B:179:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateVolumeRowH(com.android.systemui.volume.VolumeDialogImpl.VolumeRow r17) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            boolean r2 = com.android.systemui.volume.C3275D.BUG
            if (r2 == 0) goto L_0x0021
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "updateVolumeRowH s="
            r3.<init>((java.lang.String) r4)
            int r4 = r17.stream
            java.lang.StringBuilder r3 = r3.append((int) r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.i(r2, r3)
        L_0x0021:
            com.android.systemui.plugins.VolumeDialogController$State r2 = r0.mState
            if (r2 != 0) goto L_0x0026
            return
        L_0x0026:
            android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r2 = r2.states
            int r3 = r17.stream
            java.lang.Object r2 = r2.get(r3)
            com.android.systemui.plugins.VolumeDialogController$StreamState r2 = (com.android.systemui.plugins.VolumeDialogController.StreamState) r2
            if (r2 != 0) goto L_0x0035
            return
        L_0x0035:
            com.android.systemui.plugins.VolumeDialogController.StreamState unused = r1.f401ss = r2
            int r3 = r2.level
            if (r3 <= 0) goto L_0x0041
            int r3 = r2.level
            int unused = r1.lastAudibleLevel = r3
        L_0x0041:
            int r3 = r2.level
            int r4 = r17.requestedLevel
            if (r3 != r4) goto L_0x004d
            r3 = -1
            int unused = r1.requestedLevel = r3
        L_0x004d:
            int r3 = r17.stream
            r4 = 10
            r6 = 1
            if (r3 != r4) goto L_0x0058
            r3 = r6
            goto L_0x0059
        L_0x0058:
            r3 = 0
        L_0x0059:
            int r4 = r17.stream
            r7 = 2
            if (r4 != r7) goto L_0x0062
            r4 = r6
            goto L_0x0063
        L_0x0062:
            r4 = 0
        L_0x0063:
            int r8 = r17.stream
            if (r8 != r6) goto L_0x006b
            r8 = r6
            goto L_0x006c
        L_0x006b:
            r8 = 0
        L_0x006c:
            int r9 = r17.stream
            r10 = 4
            if (r9 != r10) goto L_0x0075
            r9 = r6
            goto L_0x0076
        L_0x0075:
            r9 = 0
        L_0x0076:
            int r10 = r17.stream
            r11 = 3
            if (r10 != r11) goto L_0x007f
            r10 = r6
            goto L_0x0080
        L_0x007f:
            r10 = 0
        L_0x0080:
            if (r4 == 0) goto L_0x008a
            com.android.systemui.plugins.VolumeDialogController$State r12 = r0.mState
            int r12 = r12.ringerModeInternal
            if (r12 != r6) goto L_0x008a
            r12 = r6
            goto L_0x008b
        L_0x008a:
            r12 = 0
        L_0x008b:
            if (r4 == 0) goto L_0x0095
            com.android.systemui.plugins.VolumeDialogController$State r13 = r0.mState
            int r13 = r13.ringerModeInternal
            if (r13 != 0) goto L_0x0095
            r13 = r6
            goto L_0x0096
        L_0x0095:
            r13 = 0
        L_0x0096:
            com.android.systemui.plugins.VolumeDialogController$State r14 = r0.mState
            int r14 = r14.zenMode
            if (r14 != r6) goto L_0x009e
            r14 = r6
            goto L_0x009f
        L_0x009e:
            r14 = 0
        L_0x009f:
            com.android.systemui.plugins.VolumeDialogController$State r15 = r0.mState
            int r15 = r15.zenMode
            if (r15 != r11) goto L_0x00a7
            r15 = r6
            goto L_0x00a8
        L_0x00a7:
            r15 = 0
        L_0x00a8:
            com.android.systemui.plugins.VolumeDialogController$State r11 = r0.mState
            int r11 = r11.zenMode
            if (r11 != r7) goto L_0x00b0
            r11 = r6
            goto L_0x00b1
        L_0x00b0:
            r11 = 0
        L_0x00b1:
            if (r15 == 0) goto L_0x00b9
            if (r4 != 0) goto L_0x00b7
            if (r8 == 0) goto L_0x00e7
        L_0x00b7:
            r8 = r6
            goto L_0x00e8
        L_0x00b9:
            if (r11 == 0) goto L_0x00c4
            if (r4 != 0) goto L_0x00b7
            if (r8 != 0) goto L_0x00b7
            if (r9 != 0) goto L_0x00b7
            if (r10 == 0) goto L_0x00e7
            goto L_0x00b7
        L_0x00c4:
            if (r14 == 0) goto L_0x00e7
            if (r9 == 0) goto L_0x00ce
            com.android.systemui.plugins.VolumeDialogController$State r9 = r0.mState
            boolean r9 = r9.disallowAlarms
            if (r9 != 0) goto L_0x00b7
        L_0x00ce:
            if (r10 == 0) goto L_0x00d6
            com.android.systemui.plugins.VolumeDialogController$State r9 = r0.mState
            boolean r9 = r9.disallowMedia
            if (r9 != 0) goto L_0x00b7
        L_0x00d6:
            if (r4 == 0) goto L_0x00de
            com.android.systemui.plugins.VolumeDialogController$State r9 = r0.mState
            boolean r9 = r9.disallowRinger
            if (r9 != 0) goto L_0x00b7
        L_0x00de:
            if (r8 == 0) goto L_0x00e7
            com.android.systemui.plugins.VolumeDialogController$State r8 = r0.mState
            boolean r8 = r8.disallowSystem
            if (r8 == 0) goto L_0x00e7
            goto L_0x00b7
        L_0x00e7:
            r8 = 0
        L_0x00e8:
            int r9 = r2.levelMax
            int r9 = r9 * 100
            android.widget.SeekBar r10 = r17.slider
            int r10 = r10.getMax()
            if (r9 == r10) goto L_0x00fd
            android.widget.SeekBar r10 = r17.slider
            r10.setMax(r9)
        L_0x00fd:
            int r9 = r2.levelMin
            int r9 = r9 * 100
            android.widget.SeekBar r10 = r17.slider
            int r10 = r10.getMin()
            if (r9 == r10) goto L_0x0112
            android.widget.SeekBar r10 = r17.slider
            r10.setMin(r9)
        L_0x0112:
            android.widget.TextView r9 = r17.header
            java.lang.String r10 = r0.getStreamLabelH(r2)
            com.android.systemui.volume.Util.setText(r9, r10)
            android.widget.SeekBar r9 = r17.slider
            android.widget.TextView r10 = r17.header
            java.lang.CharSequence r10 = r10.getText()
            r9.setContentDescription(r10)
            com.android.systemui.volume.ConfigurableTexts r9 = r0.mConfigurableTexts
            android.widget.TextView r10 = r17.header
            int r11 = r2.name
            r9.add(r10, r11)
            boolean r9 = r0.mAutomute
            if (r9 != 0) goto L_0x013f
            boolean r9 = r2.muteSupported
            if (r9 == 0) goto L_0x0143
        L_0x013f:
            if (r8 != 0) goto L_0x0143
            r9 = r6
            goto L_0x0144
        L_0x0143:
            r9 = 0
        L_0x0144:
            r10 = 2131232450(0x7f0806c2, float:1.808101E38)
            r11 = 2131232448(0x7f0806c0, float:1.8081006E38)
            r14 = 2131232449(0x7f0806c1, float:1.8081008E38)
            r15 = 2131232459(0x7f0806cb, float:1.8081028E38)
            if (r12 == 0) goto L_0x0154
            r5 = r15
            goto L_0x0195
        L_0x0154:
            if (r13 != 0) goto L_0x0191
            if (r8 == 0) goto L_0x0159
            goto L_0x0191
        L_0x0159:
            boolean r5 = r2.routedToBluetooth
            if (r5 == 0) goto L_0x0167
            boolean r5 = r0.isStreamMuted(r2)
            if (r5 == 0) goto L_0x0165
            r5 = r14
            goto L_0x0195
        L_0x0165:
            r5 = r11
            goto L_0x0195
        L_0x0167:
            boolean r5 = r0.isStreamMuted(r2)
            if (r5 == 0) goto L_0x017c
            if (r4 != 0) goto L_0x017c
            boolean r5 = r2.muted
            if (r5 == 0) goto L_0x0177
            r5 = 2131232452(0x7f0806c4, float:1.8081014E38)
            goto L_0x0195
        L_0x0177:
            int r5 = r17.iconMuteRes
            goto L_0x0195
        L_0x017c:
            boolean r5 = r0.mShowLowMediaVolumeIcon
            if (r5 == 0) goto L_0x018c
            int r5 = r2.level
            int r5 = r5 * r7
            int r7 = r2.levelMax
            int r6 = r2.levelMin
            int r7 = r7 + r6
            if (r5 >= r7) goto L_0x018c
            r5 = r10
            goto L_0x0195
        L_0x018c:
            int r5 = r17.iconRes
            goto L_0x0195
        L_0x0191:
            int r5 = r17.iconMuteRes
        L_0x0195:
            android.content.Context r6 = r0.mContext
            android.content.res.Resources$Theme r6 = r6.getTheme()
            r1.setIcon(r5, r6)
            if (r5 != r15) goto L_0x01a2
            r7 = 3
            goto L_0x01bb
        L_0x01a2:
            if (r5 == r14) goto L_0x01ba
            int r6 = r17.iconMuteRes
            if (r5 != r6) goto L_0x01ab
            goto L_0x01ba
        L_0x01ab:
            if (r5 == r11) goto L_0x01b8
            int r6 = r17.iconRes
            if (r5 == r6) goto L_0x01b8
            if (r5 != r10) goto L_0x01b6
            goto L_0x01b8
        L_0x01b6:
            r7 = 0
            goto L_0x01bb
        L_0x01b8:
            r7 = 1
            goto L_0x01bb
        L_0x01ba:
            r7 = 2
        L_0x01bb:
            int unused = r1.iconState = r7
            android.widget.ImageButton r5 = r17.icon
            if (r5 == 0) goto L_0x0294
            if (r9 == 0) goto L_0x0287
            r5 = 2131953554(0x7f130792, float:1.9543582E38)
            r6 = 2131953553(0x7f130791, float:1.954358E38)
            r7 = 2131953555(0x7f130793, float:1.9543584E38)
            if (r4 == 0) goto L_0x0236
            if (r12 == 0) goto L_0x01ec
            android.widget.ImageButton r3 = r17.icon
            android.content.Context r5 = r0.mContext
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r9 = 0
            r6[r9] = r2
            java.lang.String r2 = r5.getString(r7, r6)
            r3.setContentDescription(r2)
            goto L_0x0294
        L_0x01ec:
            com.android.systemui.plugins.VolumeDialogController r3 = r0.mController
            boolean r3 = r3.hasVibrator()
            if (r3 == 0) goto L_0x0218
            android.widget.ImageButton r3 = r17.icon
            android.content.Context r5 = r0.mContext
            boolean r6 = r0.mShowA11yStream
            if (r6 == 0) goto L_0x0202
            r6 = 2131953557(0x7f130795, float:1.9543588E38)
            goto L_0x0205
        L_0x0202:
            r6 = 2131953556(0x7f130794, float:1.9543586E38)
        L_0x0205:
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r9 = 0
            r7[r9] = r2
            java.lang.String r2 = r5.getString(r6, r7)
            r3.setContentDescription(r2)
            goto L_0x0294
        L_0x0218:
            android.widget.ImageButton r3 = r17.icon
            android.content.Context r7 = r0.mContext
            boolean r9 = r0.mShowA11yStream
            if (r9 == 0) goto L_0x0223
            goto L_0x0224
        L_0x0223:
            r5 = r6
        L_0x0224:
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r9 = 0
            r6[r9] = r2
            java.lang.String r2 = r7.getString(r5, r6)
            r3.setContentDescription(r2)
            goto L_0x0294
        L_0x0236:
            if (r3 == 0) goto L_0x0244
            android.widget.ImageButton r3 = r17.icon
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r3.setContentDescription(r2)
            goto L_0x0294
        L_0x0244:
            boolean r3 = r2.muted
            if (r3 != 0) goto L_0x026f
            boolean r3 = r0.mAutomute
            if (r3 == 0) goto L_0x0251
            int r3 = r2.level
            if (r3 != 0) goto L_0x0251
            goto L_0x026f
        L_0x0251:
            android.widget.ImageButton r3 = r17.icon
            android.content.Context r7 = r0.mContext
            boolean r9 = r0.mShowA11yStream
            if (r9 == 0) goto L_0x025c
            goto L_0x025d
        L_0x025c:
            r5 = r6
        L_0x025d:
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r9 = 0
            r6[r9] = r2
            java.lang.String r2 = r7.getString(r5, r6)
            r3.setContentDescription(r2)
            goto L_0x0295
        L_0x026f:
            r6 = 1
            r9 = 0
            android.widget.ImageButton r3 = r17.icon
            android.content.Context r5 = r0.mContext
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r6[r9] = r2
            java.lang.String r2 = r5.getString(r7, r6)
            r3.setContentDescription(r2)
            goto L_0x0295
        L_0x0287:
            r9 = 0
            android.widget.ImageButton r3 = r17.icon
            java.lang.String r2 = r0.getStreamLabelH(r2)
            r3.setContentDescription(r2)
            goto L_0x0295
        L_0x0294:
            r9 = 0
        L_0x0295:
            if (r8 == 0) goto L_0x029a
            boolean unused = r1.tracking = r9
        L_0x029a:
            r2 = r8 ^ 1
            r0.enableVolumeRowViewsH(r1, r2)
            r2 = r8 ^ 1
            com.android.systemui.plugins.VolumeDialogController$StreamState r3 = r17.f401ss
            boolean r3 = r3.muted
            if (r3 == 0) goto L_0x02ae
            if (r4 != 0) goto L_0x02ae
            if (r8 != 0) goto L_0x02ae
            goto L_0x02b4
        L_0x02ae:
            if (r4 == 0) goto L_0x02b6
            if (r12 != 0) goto L_0x02b4
            if (r13 == 0) goto L_0x02b6
        L_0x02b4:
            r5 = r9
            goto L_0x02bc
        L_0x02b6:
            com.android.systemui.plugins.VolumeDialogController$StreamState r3 = r17.f401ss
            int r5 = r3.level
        L_0x02bc:
            java.lang.String r3 = "VolumeDialogImpl#updateVolumeRowSliderH"
            android.os.Trace.beginSection(r3)
            r0.updateVolumeRowSliderH(r1, r2, r5)
            android.os.Trace.endSection()
            android.widget.TextView r0 = r17.number
            if (r0 == 0) goto L_0x02d8
            android.widget.TextView r0 = r17.number
            java.lang.String r1 = java.lang.Integer.toString(r5)
            r0.setText(r1)
        L_0x02d8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.updateVolumeRowH(com.android.systemui.volume.VolumeDialogImpl$VolumeRow):void");
    }

    private boolean isStreamMuted(VolumeDialogController.StreamState streamState) {
        return (this.mAutomute && streamState.level == 0) || streamState.muted;
    }

    private void updateVolumeRowTintH(VolumeRow volumeRow, boolean z) {
        ColorStateList colorStateList;
        int i;
        if (z) {
            volumeRow.slider.requestFocus();
        }
        boolean z2 = z && volumeRow.slider.isEnabled();
        if (z2 || this.mChangeVolumeRowTintWhenInactive) {
            if (z2) {
                colorStateList = Utils.getColorAccent(this.mContext);
            } else {
                colorStateList = Utils.getColorAttr(this.mContext, 17956902);
            }
            if (z2) {
                i = Color.alpha(colorStateList.getDefaultColor());
            } else {
                i = getAlphaAttr(16844115);
            }
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16844002);
            ColorStateList colorAttr2 = Utils.getColorAttr(this.mContext, 17957103);
            if (volumeRow.sliderProgressIcon != null) {
                volumeRow.sliderProgressIcon.setTintList(colorAttr);
            }
            if (volumeRow.icon != null) {
                volumeRow.icon.setImageTintList(colorAttr2);
                volumeRow.icon.setImageAlpha(i);
            }
            if (volumeRow.number != null) {
                volumeRow.number.setTextColor(colorStateList);
                volumeRow.number.setAlpha((float) i);
            }
        }
    }

    private void updateVolumeRowSliderH(VolumeRow volumeRow, boolean z, int i) {
        int i2;
        volumeRow.slider.setEnabled(z);
        updateVolumeRowTintH(volumeRow, volumeRow.stream == this.mActiveStream);
        if (!volumeRow.tracking) {
            int progress = volumeRow.slider.getProgress();
            int impliedLevel = getImpliedLevel(volumeRow.slider, progress);
            boolean z2 = volumeRow.view.getVisibility() == 0;
            boolean z3 = SystemClock.uptimeMillis() - volumeRow.userAttempt < 1000;
            this.mHandler.removeMessages(3, volumeRow);
            boolean z4 = this.mShowing;
            if (z4 && z2 && z3) {
                if (C3275D.BUG) {
                    Log.d(TAG, "inGracePeriod");
                }
                C3311H h = this.mHandler;
                h.sendMessageAtTime(h.obtainMessage(3, volumeRow), volumeRow.userAttempt + 1000);
            } else if ((i == impliedLevel && z4 && z2) || progress == (i2 = i * 100)) {
            } else {
                if (!z4 || !z2) {
                    if (volumeRow.anim != null) {
                        volumeRow.anim.cancel();
                    }
                    volumeRow.slider.setProgress(i2, true);
                } else if (volumeRow.anim == null || !volumeRow.anim.isRunning() || volumeRow.animTargetProgress != i2) {
                    if (volumeRow.anim == null) {
                        ObjectAnimator unused = volumeRow.anim = ObjectAnimator.ofInt(volumeRow.slider, "progress", new int[]{progress, i2});
                        volumeRow.anim.setInterpolator(new DecelerateInterpolator());
                    } else {
                        volumeRow.anim.cancel();
                        volumeRow.anim.setIntValues(new int[]{progress, i2});
                    }
                    int unused2 = volumeRow.animTargetProgress = i2;
                    volumeRow.anim.setDuration(80);
                    volumeRow.anim.addListener(getJankListener(volumeRow.view, TYPE_UPDATE, 80));
                    volumeRow.anim.start();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void recheckH(VolumeRow volumeRow) {
        if (volumeRow == null) {
            if (C3275D.BUG) {
                Log.d(TAG, "recheckH ALL");
            }
            trimObsoleteH();
            for (VolumeRow updateVolumeRowH : this.mRows) {
                updateVolumeRowH(updateVolumeRowH);
            }
            return;
        }
        if (C3275D.BUG) {
            Log.d(TAG, "recheckH " + volumeRow.stream);
        }
        updateVolumeRowH(volumeRow);
    }

    /* access modifiers changed from: private */
    public void setStreamImportantH(int i, boolean z) {
        for (VolumeRow next : this.mRows) {
            if (next.stream == i) {
                boolean unused = next.important = z;
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        recheckH((com.android.systemui.volume.VolumeDialogImpl.VolumeRow) null);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showSafetyWarningH(int r4) {
        /*
            r3 = this;
            r4 = r4 & 1025(0x401, float:1.436E-42)
            if (r4 != 0) goto L_0x0008
            boolean r4 = r3.mShowing
            if (r4 == 0) goto L_0x0028
        L_0x0008:
            java.lang.Object r4 = r3.mSafetyWarningLock
            monitor-enter(r4)
            com.android.systemui.volume.SafetyWarningDialog r0 = r3.mSafetyWarning     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x0011
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            return
        L_0x0011:
            com.android.systemui.volume.VolumeDialogImpl$8 r0 = new com.android.systemui.volume.VolumeDialogImpl$8     // Catch:{ all -> 0x002c }
            android.content.Context r1 = r3.mContext     // Catch:{ all -> 0x002c }
            com.android.systemui.plugins.VolumeDialogController r2 = r3.mController     // Catch:{ all -> 0x002c }
            android.media.AudioManager r2 = r2.getAudioManager()     // Catch:{ all -> 0x002c }
            r0.<init>(r1, r2)     // Catch:{ all -> 0x002c }
            r3.mSafetyWarning = r0     // Catch:{ all -> 0x002c }
            r0.show()     // Catch:{ all -> 0x002c }
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            r4 = 0
            r3.recheckH(r4)
        L_0x0028:
            r3.rescheduleTimeoutH()
            return
        L_0x002c:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.showSafetyWarningH(int):void");
    }

    private String getStreamLabelH(VolumeDialogController.StreamState streamState) {
        if (streamState == null) {
            return "";
        }
        if (streamState.remoteLabel != null) {
            return streamState.remoteLabel;
        }
        try {
            return this.mContext.getResources().getString(streamState.name);
        } catch (Resources.NotFoundException unused) {
            Slog.e(TAG, "Can't find translation for stream " + streamState);
            return "";
        }
    }

    private Runnable getSinglePressFor(ImageButton imageButton) {
        return new VolumeDialogImpl$$ExternalSyntheticLambda8(this, imageButton);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getSinglePressFor$21$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47346x80e7b33d(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(true);
            imageButton.postOnAnimationDelayed(getSingleUnpressFor(imageButton), 200);
        }
    }

    private Runnable getSingleUnpressFor(ImageButton imageButton) {
        return new VolumeDialogImpl$$ExternalSyntheticLambda9(imageButton);
    }

    static /* synthetic */ void lambda$getSingleUnpressFor$22(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(false);
        }
    }

    private int getRingerDrawerOpenExtraSize() {
        return (this.mRingerCount - 1) * (this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2));
    }

    private void updateBackgroundForDrawerClosedAmount() {
        Drawable drawable = this.mRingerAndDrawerContainerBackground;
        if (drawable != null) {
            Rect copyBounds = drawable.copyBounds();
            if (!isLandscape()) {
                copyBounds.top = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            } else {
                copyBounds.left = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            }
            this.mRingerAndDrawerContainerBackground.setBounds(copyBounds);
        }
    }

    private int getVolumeRowsOpenExtraSize() {
        return (getVisibleRowsCount(true) - 1) * (this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2));
    }

    private void updateBackgroundClosedAmount() {
        Drawable drawable = this.mVolumeUITopContainerBackground;
        if (drawable != null) {
            Rect copyBounds = drawable.copyBounds();
            copyBounds.right = this.mRingerDrawerItemSize + (this.mRingerRowsPadding * 2) + ((int) (this.mRingerDrawerClosedAmount * ((float) getVolumeRowsOpenExtraSize())));
            this.mVolumeUITopContainerBackground.setBounds(copyBounds);
        }
    }

    private void setTopContainerBackgroundDrawable() {
        int i;
        int i2;
        if (this.mTopContainer != null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new ColorDrawable(Utils.getColorAttrDefaultColor(this.mContext, 17956909))});
            int i3 = this.mDialogWidth;
            if (!isLandscape()) {
                i = this.mDialogRowsView.getHeight();
            } else {
                i = this.mDialogRowsView.getHeight() + this.mDialogCornerRadius;
            }
            layerDrawable.setLayerSize(0, i3, i);
            if (!isLandscape()) {
                i2 = this.mDialogRowsViewContainer.getTop();
            } else {
                i2 = this.mDialogRowsViewContainer.getTop() - this.mDialogCornerRadius;
            }
            layerDrawable.setLayerInsetTop(0, i2);
            layerDrawable.setLayerGravity(0, 53);
            if (isLandscape()) {
                this.mRingerAndDrawerContainer.setOutlineProvider(new ViewOutlineProvider() {
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) VolumeDialogImpl.this.mDialogCornerRadius);
                    }
                });
                this.mRingerAndDrawerContainer.setClipToOutline(true);
            }
            this.mTopContainer.setBackground(layerDrawable);
        }
    }

    private void setDrawerContainerOutLine() {
        this.mRingerAndDrawerContainer.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) VolumeDialogImpl.this.mDialogCornerRadius);
            }
        });
        this.mRingerAndDrawerContainer.setClipToOutline(true);
    }

    /* renamed from: com.android.systemui.volume.VolumeDialogImpl$H */
    private final class C3311H extends Handler {
        private static final int DISMISS = 2;
        private static final int RECHECK = 3;
        private static final int RECHECK_ALL = 4;
        private static final int RESCHEDULE_TIMEOUT = 6;
        private static final int SET_STREAM_IMPORTANT = 5;
        private static final int SHOW = 1;
        private static final int STATE_CHANGED = 7;

        public C3311H() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    VolumeDialogImpl.this.showH(message.arg1, VolumeDialogImpl.this.mKeyguard.isKeyguardLocked(), VolumeDialogImpl.this.mActivityManager.getLockTaskModeState());
                    return;
                case 2:
                    VolumeDialogImpl.this.dismissH(message.arg1);
                    return;
                case 3:
                    VolumeDialogImpl.this.recheckH((VolumeRow) message.obj);
                    return;
                case 4:
                    VolumeDialogImpl.this.recheckH((VolumeRow) null);
                    return;
                case 5:
                    VolumeDialogImpl.this.setStreamImportantH(message.arg1, message.arg2 != 0);
                    return;
                case 6:
                    VolumeDialogImpl.this.rescheduleTimeoutH();
                    return;
                case 7:
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    volumeDialogImpl.onStateChangedH(volumeDialogImpl.mState);
                    return;
                default:
                    return;
            }
        }
    }

    private final class CustomDialog extends Dialog implements DialogInterface {
        public CustomDialog(Context context) {
            super(context, C1894R.style.volume_dialog_theme);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.dispatchTouchEvent(motionEvent);
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            super.setCanceledOnTouchOutside(true);
            super.onStart();
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            super.onStop();
            VolumeDialogImpl.this.mHandler.sendEmptyMessage(4);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!VolumeDialogImpl.this.mShowing || motionEvent.getAction() != 4) {
                return false;
            }
            VolumeDialogImpl.this.dismissH(1);
            return true;
        }
    }

    private final class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private final VolumeRow mRow;

        private VolumeSeekBarChangeListener(VolumeRow volumeRow) {
            this.mRow = volumeRow;
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2;
            if (this.mRow.f401ss != null) {
                if (C3275D.BUG) {
                    Log.d(VolumeDialogImpl.TAG, AudioSystem.streamToString(this.mRow.stream) + " onProgressChanged " + i + " fromUser=" + z);
                }
                if (z) {
                    if (this.mRow.f401ss.levelMin > 0 && i < (i2 = this.mRow.f401ss.levelMin * 100)) {
                        seekBar.setProgress(i2);
                        i = i2;
                    }
                    int access$6400 = VolumeDialogImpl.getImpliedLevel(seekBar, i);
                    if (this.mRow.f401ss.level != access$6400 || (this.mRow.f401ss.muted && access$6400 > 0)) {
                        long unused = this.mRow.userAttempt = SystemClock.uptimeMillis();
                        if (this.mRow.requestedLevel != access$6400) {
                            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
                            VolumeDialogImpl.this.mController.setStreamVolume(this.mRow.stream, access$6400);
                            int unused2 = this.mRow.requestedLevel = access$6400;
                            Events.writeEvent(9, Integer.valueOf(this.mRow.stream), Integer.valueOf(access$6400));
                        }
                    }
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (C3275D.BUG) {
                Log.d(VolumeDialogImpl.TAG, "onStartTrackingTouch " + this.mRow.stream);
            }
            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
            boolean unused = this.mRow.tracking = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (C3275D.BUG) {
                Log.d(VolumeDialogImpl.TAG, "onStopTrackingTouch " + this.mRow.stream);
            }
            boolean unused = this.mRow.tracking = false;
            long unused2 = this.mRow.userAttempt = SystemClock.uptimeMillis();
            int access$6400 = VolumeDialogImpl.getImpliedLevel(seekBar, seekBar.getProgress());
            Events.writeEvent(16, Integer.valueOf(this.mRow.stream), Integer.valueOf(access$6400));
            if (this.mRow.f401ss.level != access$6400) {
                VolumeDialogImpl.this.mHandler.sendMessageDelayed(VolumeDialogImpl.this.mHandler.obtainMessage(3, this.mRow), 1000);
            }
        }
    }

    private final class Accessibility extends View.AccessibilityDelegate {
        private Accessibility() {
        }

        public void init() {
            VolumeDialogImpl.this.mDialogView.setAccessibilityDelegate(this);
        }

        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(VolumeDialogImpl.this.composeWindowTitle());
            return true;
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    private static class VolumeRow {
        /* access modifiers changed from: private */
        public ObjectAnimator anim;
        /* access modifiers changed from: private */
        public int animTargetProgress;
        /* access modifiers changed from: private */
        public boolean defaultStream;
        /* access modifiers changed from: private */
        public FrameLayout dndIcon;
        /* access modifiers changed from: private */
        public TextView header;
        /* access modifiers changed from: private */
        public ImageButton icon;
        /* access modifiers changed from: private */
        public int iconMuteRes;
        /* access modifiers changed from: private */
        public int iconRes;
        /* access modifiers changed from: private */
        public int iconState;
        /* access modifiers changed from: private */
        public boolean important;
        /* access modifiers changed from: private */
        public int lastAudibleLevel;
        /* access modifiers changed from: private */
        public TextView number;
        /* access modifiers changed from: private */
        public int requestedLevel;
        /* access modifiers changed from: private */
        public SeekBar slider;
        /* access modifiers changed from: private */
        public AlphaTintDrawableWrapper sliderProgressIcon;
        /* access modifiers changed from: private */
        public Drawable sliderProgressSolid;
        /* access modifiers changed from: private */

        /* renamed from: ss */
        public VolumeDialogController.StreamState f401ss;
        /* access modifiers changed from: private */
        public int stream;
        /* access modifiers changed from: private */
        public boolean tracking;
        /* access modifiers changed from: private */
        public long userAttempt;
        /* access modifiers changed from: private */
        public View view;

        private VolumeRow() {
            this.requestedLevel = -1;
            this.lastAudibleLevel = 1;
        }

        /* access modifiers changed from: package-private */
        public void setIcon(int i, Resources.Theme theme) {
            ImageButton imageButton = this.icon;
            if (imageButton != null) {
                imageButton.setImageResource(i);
            }
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = this.sliderProgressIcon;
            if (alphaTintDrawableWrapper != null) {
                alphaTintDrawableWrapper.setDrawable(this.view.getResources().getDrawable(i, theme));
            }
        }
    }

    private class RingerDrawerItemClickListener implements View.OnClickListener {
        private final int mClickedRingerMode;

        RingerDrawerItemClickListener(int i) {
            this.mClickedRingerMode = i;
        }

        public void onClick(View view) {
            if (VolumeDialogImpl.this.mIsRingerDrawerOpen) {
                VolumeDialogImpl.this.setRingerMode(this.mClickedRingerMode);
            }
        }
    }

    private void ringerDrawerNewSelectionAnimation(int i) {
        int i2 = this.mCurrentRingerMode;
        if (i2 == -1) {
            Log.d(TAG, "init ringer mode =" + i);
            this.mCurrentRingerMode = i;
        } else if (i2 != i) {
            this.mCurrentRingerMode = i;
            this.mRingerDrawerIconAnimatingSelected = getDrawerIconViewForMode(i);
            this.mRingerDrawerIconAnimatingDeselected = getDrawerIconViewForMode(this.mState.ringerModeInternal);
            this.mRingerDrawerIconColorAnimator.start();
            if (this.mIsRingerDrawerOpen) {
                this.mSelectedRingerContainer.setVisibility(4);
                this.mRingerDrawerNewSelectionBg.setAlpha(1.0f);
                this.mRingerDrawerNewSelectionBg.animate().setInterpolator(Interpolators.ACCELERATE_DECELERATE).setDuration(175).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda2(this, i));
                this.mRingerDrawerNewSelectionBg.animate().translationX(getTranslationInDrawerForRingerModePortrait(i)).start();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$ringerDrawerNewSelectionAnimation$23$com-android-systemui-volume-VolumeDialogImpl */
    public /* synthetic */ void mo47359xcdf59921(int i) {
        this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
        this.mSelectedRingerContainer.setTranslationX(getTranslationInDrawerForRingerModePortrait(i));
        this.mSelectedRingerContainer.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public int getVisibleRowsCount(boolean z) {
        VolumeRow activeRow = getActiveRow();
        if (!z) {
            return 1;
        }
        int i = 0;
        for (VolumeRow next : this.mRows) {
            if (next.stream == 3 || next.stream == 2 || next.stream == 4) {
                i++;
            }
            if ((next.stream == activeRow.stream || this.isDynamicRowShowWhenExpand) && this.mDynamic.get(next.stream)) {
                i++;
            }
            if ((next.stream == activeRow.stream || isInVoiceCall()) && next.stream == 0) {
                i++;
            }
        }
        return i;
    }

    private boolean isInVoiceCall() {
        return ((VolumeDialogImplEx) NTDependencyEx.get(VolumeDialogImplEx.class)).getPhoneState() == 2;
    }
}
