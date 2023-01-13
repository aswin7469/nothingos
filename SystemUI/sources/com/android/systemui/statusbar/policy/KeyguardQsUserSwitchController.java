package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.keyguard.dagger.KeyguardUserSwitcherScope;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UserAvatarView;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;

@KeyguardUserSwitcherScope
public class KeyguardQsUserSwitchController extends ViewController<FrameLayout> {
    private static final AnimationProperties ANIMATION_PROPERTIES = new AnimationProperties().setDuration(360);
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardQsUserSwitchController";
    private UserSwitcherController.BaseUserAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mBarState;
    private final ConfigurationController mConfigurationController;
    private ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            if (KeyguardQsUserSwitchController.this.mIsKeyguardShowing) {
                KeyguardQsUserSwitchController.this.updateView();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Context mContext;
    UserSwitcherController.UserRecord mCurrentUser;
    public final DataSetObserver mDataSetObserver = new DataSetObserver() {
        public void onChanged() {
            if (KeyguardQsUserSwitchController.this.updateCurrentUser() || (KeyguardQsUserSwitchController.this.mIsKeyguardShowing && KeyguardQsUserSwitchController.this.mUserAvatarView.isEmpty())) {
                KeyguardQsUserSwitchController.this.updateView();
            }
        }
    };
    private final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public boolean mIsKeyguardShowing;
    private final KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() {
        public void onUnlockedChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }

        public void onKeyguardShowingChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }

        public void onKeyguardFadingAwayChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }
    };
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    private final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    private Resources mResources;
    protected final SysuiStatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onStateChanged(int i) {
            boolean goingToFullShade = KeyguardQsUserSwitchController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = KeyguardQsUserSwitchController.this.mKeyguardStateController.isKeyguardFadingAway();
            int access$100 = KeyguardQsUserSwitchController.this.mBarState;
            int unused = KeyguardQsUserSwitchController.this.mBarState = i;
            KeyguardQsUserSwitchController.this.setKeyguardQsUserSwitchVisibility(i, isKeyguardFadingAway, goingToFullShade, access$100);
        }
    };
    private final UiEventLogger mUiEventLogger;
    UserAvatarView mUserAvatarView;
    private View mUserAvatarViewWithBackground;
    private final UserSwitchDialogController mUserSwitchDialogController;
    private final UserSwitcherController mUserSwitcherController;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardQsUserSwitchController(FrameLayout frameLayout, Context context, @Main Resources resources, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController, UserSwitchDialogController userSwitchDialogController, UiEventLogger uiEventLogger) {
        super(frameLayout);
        if (DEBUG) {
            Log.d(TAG, "New KeyguardQsUserSwitchController");
        }
        this.mContext = context;
        this.mResources = resources;
        this.mUserSwitcherController = userSwitcherController;
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        this.mKeyguardStateController = keyguardStateController2;
        this.mFalsingManager = falsingManager;
        this.mConfigurationController = configurationController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(this.mView, keyguardStateController2, dozeParameters, screenOffAnimationController, false);
        this.mUserSwitchDialogController = userSwitchDialogController;
        this.mUiEventLogger = uiEventLogger;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        super.onInit();
        if (DEBUG) {
            Log.d(TAG, "onInit");
        }
        this.mUserAvatarView = (UserAvatarView) ((FrameLayout) this.mView).findViewById(C1894R.C1898id.kg_multi_user_avatar);
        this.mUserAvatarViewWithBackground = ((FrameLayout) this.mView).findViewById(C1894R.C1898id.kg_multi_user_avatar_with_background);
        this.mAdapter = new UserSwitcherController.BaseUserAdapter(this.mUserSwitcherController) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }
        };
        this.mUserAvatarView.setOnClickListener(new KeyguardQsUserSwitchController$$ExternalSyntheticLambda0(this));
        this.mUserAvatarView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, KeyguardQsUserSwitchController.this.mContext.getString(C1894R.string.accessibility_quick_settings_choose_user_action)));
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onInit$0$com-android-systemui-statusbar-policy-KeyguardQsUserSwitchController */
    public /* synthetic */ void mo45844xa1a946be(View view) {
        if (!this.mFalsingManager.isFalseTap(1) && !isListAnimating()) {
            this.mUiEventLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_SWITCH_USER_TAP);
            this.mUserSwitchDialogController.showDialog(this.mUserAvatarViewWithBackground);
        }
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        if (DEBUG) {
            Log.d(TAG, "onViewAttached");
        }
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        this.mDataSetObserver.onChanged();
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        updateCurrentUser();
        updateKeyguardShowing(true);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        if (DEBUG) {
            Log.d(TAG, "onViewDetached");
        }
        this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
    }

    private void clearAvatar() {
        if (DEBUG) {
            Log.d(TAG, "clearAvatar");
        }
        this.mUserAvatarView.setAvatar((Bitmap) null);
    }

    /* access modifiers changed from: package-private */
    public void updateKeyguardShowing(boolean z) {
        boolean z2 = this.mIsKeyguardShowing;
        boolean z3 = this.mKeyguardStateController.isShowing() || this.mKeyguardStateController.isKeyguardGoingAway();
        this.mIsKeyguardShowing = z3;
        if (z2 != z3 || z) {
            if (DEBUG) {
                Log.d(TAG, "updateKeyguardShowing: mIsKeyguardShowing=" + this.mIsKeyguardShowing + " forceViewUpdate=" + z);
            }
            if (this.mIsKeyguardShowing) {
                updateView();
            } else {
                clearAvatar();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean updateCurrentUser() {
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        this.mCurrentUser = null;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            UserSwitcherController.UserRecord item = this.mAdapter.getItem(i);
            if (item.isCurrent) {
                this.mCurrentUser = item;
                return !item.equals(userRecord);
            }
        }
        if (this.mCurrentUser != null || userRecord == null) {
            return false;
        }
        return true;
    }

    private String getContentDescription() {
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        if (userRecord == null || userRecord.info == null || TextUtils.isEmpty(this.mCurrentUser.info.name)) {
            return this.mContext.getString(C1894R.string.accessibility_multi_user_switch_switcher);
        }
        return this.mContext.getString(C1894R.string.accessibility_quick_settings_user, new Object[]{this.mCurrentUser.info.name});
    }

    /* access modifiers changed from: private */
    public void updateView() {
        if (DEBUG) {
            Log.d(TAG, "updateView");
        }
        this.mUserAvatarView.setContentDescription(getContentDescription());
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        this.mUserAvatarView.setDrawableWithBadge(getCurrentUserIcon().mutate(), userRecord != null ? userRecord.resolveId() : -10000);
    }

    /* access modifiers changed from: package-private */
    public Drawable getCurrentUserIcon() {
        Drawable drawable;
        Drawable drawable2;
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        if (userRecord == null || userRecord.picture == null) {
            UserSwitcherController.UserRecord userRecord2 = this.mCurrentUser;
            if (userRecord2 == null || !userRecord2.isGuest) {
                drawable2 = this.mContext.getDrawable(C1894R.C1896drawable.ic_avatar_user);
            } else {
                drawable2 = this.mContext.getDrawable(C1894R.C1896drawable.ic_avatar_guest_user);
            }
            drawable = drawable2;
            drawable.setTint(this.mResources.getColor(C1894R.C1895color.kg_user_switcher_avatar_icon_color, this.mContext.getTheme()));
        } else {
            drawable = new CircleFramedDrawable(this.mCurrentUser.picture, (int) this.mResources.getDimension(C1894R.dimen.kg_framed_avatar_size));
        }
        return new LayerDrawable(new Drawable[]{this.mContext.getDrawable(C1894R.C1896drawable.user_avatar_bg), drawable});
    }

    public int getUserIconHeight() {
        return this.mUserAvatarView.getHeight();
    }

    public void setKeyguardQsUserSwitchVisibility(int i, boolean z, boolean z2, int i2) {
        this.mKeyguardVisibilityHelper.setViewVisibility(i, z, z2, i2);
    }

    public void updatePosition(int i, int i2, boolean z) {
        AnimationProperties animationProperties = ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((FrameLayout) this.mView, AnimatableProperty.f375Y, (float) i2, animationProperties, z);
        PropertyAnimator.setProperty((FrameLayout) this.mView, AnimatableProperty.TRANSLATION_X, (float) (-Math.abs(i)), animationProperties, z);
    }

    public void setAlpha(float f) {
        if (!this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            ((FrameLayout) this.mView).setAlpha(f);
        }
    }

    private boolean isListAnimating() {
        return this.mKeyguardVisibilityHelper.isVisibilityAnimating();
    }
}
