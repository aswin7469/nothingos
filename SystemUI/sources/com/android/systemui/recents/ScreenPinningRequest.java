package com.android.systemui.recents;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.RemoteException;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.util.leak.RotationUtils;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;

public class ScreenPinningRequest implements View.OnClickListener, NavigationModeController.ModeChangedListener {
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityService;
    /* access modifiers changed from: private */
    public final BroadcastDispatcher mBroadcastDispatcher;
    /* access modifiers changed from: private */
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private final Context mContext;
    /* access modifiers changed from: private */
    public int mNavBarMode;
    private RequestWindowView mRequestWindow;
    /* access modifiers changed from: private */
    public final WindowManager mWindowManager;
    private int taskId;

    @Inject
    public ScreenPinningRequest(Context context, Lazy<Optional<CentralSurfaces>> lazy, NavigationModeController navigationModeController, BroadcastDispatcher broadcastDispatcher) {
        this.mContext = context;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mAccessibilityService = (AccessibilityManager) context.getSystemService("accessibility");
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mNavBarMode = navigationModeController.addListener(this);
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    public void clearPrompt() {
        RequestWindowView requestWindowView = this.mRequestWindow;
        if (requestWindowView != null) {
            this.mWindowManager.removeView(requestWindowView);
            this.mRequestWindow = null;
        }
    }

    public void showPrompt(int i, boolean z) {
        try {
            clearPrompt();
        } catch (IllegalArgumentException unused) {
        }
        this.taskId = i;
        RequestWindowView requestWindowView = new RequestWindowView(this.mContext, z);
        this.mRequestWindow = requestWindowView;
        requestWindowView.setSystemUiVisibility(256);
        this.mWindowManager.addView(this.mRequestWindow, getWindowLayoutParams());
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public void onConfigurationChanged() {
        RequestWindowView requestWindowView = this.mRequestWindow;
        if (requestWindowView != null) {
            requestWindowView.onConfigurationChanged();
        }
    }

    /* access modifiers changed from: protected */
    public WindowManager.LayoutParams getWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2024, 264, -3);
        layoutParams.token = new Binder();
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("ScreenPinningConfirmation");
        layoutParams.gravity = 119;
        layoutParams.setFitInsetsTypes(0);
        return layoutParams;
    }

    public void onClick(View view) {
        if (view.getId() == C1894R.C1898id.screen_pinning_ok_button || this.mRequestWindow == view) {
            try {
                ActivityTaskManager.getService().startSystemLockTaskMode(this.taskId);
            } catch (RemoteException unused) {
            } catch (IllegalArgumentException e) {
                Log.d("ScreenPinningRequest", "startSystemLockTaskMode error. reason:" + e);
            }
        }
        clearPrompt();
    }

    public FrameLayout.LayoutParams getRequestLayoutParams(int i) {
        return new FrameLayout.LayoutParams(-2, -2, i == 3 ? 19 : i == 1 ? 21 : 81);
    }

    private class RequestWindowView extends FrameLayout {
        private static final int OFFSET_DP = 96;
        /* access modifiers changed from: private */
        public final ColorDrawable mColor;
        /* access modifiers changed from: private */
        public ViewGroup mLayout;
        private final BroadcastReceiver mReceiver;
        private final boolean mShowCancel;
        /* access modifiers changed from: private */
        public final Runnable mUpdateLayoutRunnable;

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

        private RequestWindowView(Context context, boolean z) {
            super(context);
            ColorDrawable colorDrawable = new ColorDrawable(0);
            this.mColor = colorDrawable;
            this.mUpdateLayoutRunnable = new Runnable() {
                public void run() {
                    if (RequestWindowView.this.mLayout != null && RequestWindowView.this.mLayout.getParent() != null) {
                        ViewGroup access$700 = RequestWindowView.this.mLayout;
                        ScreenPinningRequest screenPinningRequest = ScreenPinningRequest.this;
                        RequestWindowView requestWindowView = RequestWindowView.this;
                        access$700.setLayoutParams(screenPinningRequest.getRequestLayoutParams(requestWindowView.getRotation(requestWindowView.mContext)));
                    }
                }
            };
            this.mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        RequestWindowView requestWindowView = RequestWindowView.this;
                        requestWindowView.post(requestWindowView.mUpdateLayoutRunnable);
                    } else if (intent.getAction().equals("android.intent.action.USER_SWITCHED") || intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                        ScreenPinningRequest.this.clearPrompt();
                    }
                }
            };
            setClickable(true);
            setOnClickListener(ScreenPinningRequest.this);
            setBackground(colorDrawable);
            this.mShowCancel = z;
        }

        public void onAttachedToWindow() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ScreenPinningRequest.this.mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            float f = displayMetrics.density;
            int rotation = getRotation(this.mContext);
            inflateView(rotation);
            int color = this.mContext.getColor(C1894R.C1895color.screen_pinning_request_window_bg);
            if (ActivityManager.isHighEndGfx()) {
                this.mLayout.setAlpha(0.0f);
                if (rotation == 3) {
                    this.mLayout.setTranslationX(f * -96.0f);
                } else if (rotation == 1) {
                    this.mLayout.setTranslationX(f * 96.0f);
                } else {
                    this.mLayout.setTranslationY(f * 96.0f);
                }
                this.mLayout.animate().alpha(1.0f).translationX(0.0f).translationY(0.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{0, Integer.valueOf(color)});
                ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        RequestWindowView.this.mColor.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                });
                ofObject.setDuration(1000);
                ofObject.start();
            } else {
                this.mColor.setColor(color);
            }
            IntentFilter intentFilter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            ScreenPinningRequest.this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
        }

        private void inflateView(int i) {
            int i2;
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(), i == 3 ? C1894R.layout.screen_pinning_request_sea_phone : i == 1 ? C1894R.layout.screen_pinning_request_land_phone : C1894R.layout.screen_pinning_request, (ViewGroup) null);
            this.mLayout = viewGroup;
            viewGroup.setClickable(true);
            int i3 = 0;
            this.mLayout.setLayoutDirection(0);
            this.mLayout.findViewById(C1894R.C1898id.screen_pinning_text_area).setLayoutDirection(3);
            View findViewById = this.mLayout.findViewById(C1894R.C1898id.screen_pinning_buttons);
            WindowManagerWrapper instance = WindowManagerWrapper.getInstance();
            if (QuickStepContract.isGesturalMode(ScreenPinningRequest.this.mNavBarMode) || !instance.hasSoftNavigationBar(this.mContext.getDisplayId()) || Utilities.isTablet(this.mContext)) {
                findViewById.setVisibility(8);
            } else {
                findViewById.setLayoutDirection(3);
                swapChildrenIfRtlAndVertical(findViewById);
            }
            ((Button) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_ok_button)).setOnClickListener(ScreenPinningRequest.this);
            if (this.mShowCancel) {
                ((Button) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_cancel_button)).setOnClickListener(ScreenPinningRequest.this);
            } else {
                ((Button) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_cancel_button)).setVisibility(4);
            }
            Optional optional = (Optional) ScreenPinningRequest.this.mCentralSurfacesOptionalLazy.get();
            boolean booleanValue = ((Boolean) optional.map(new ScreenPinningRequest$RequestWindowView$$ExternalSyntheticLambda0()).orElse(false)).booleanValue();
            boolean isTouchExplorationEnabled = ScreenPinningRequest.this.mAccessibilityService.isTouchExplorationEnabled();
            if (QuickStepContract.isGesturalMode(ScreenPinningRequest.this.mNavBarMode)) {
                i2 = C1894R.string.screen_pinning_description_gestural;
            } else if (booleanValue) {
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_recents_group).setVisibility(0);
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_home_bg_light).setVisibility(4);
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_home_bg).setVisibility(4);
                i2 = isTouchExplorationEnabled ? C1894R.string.screen_pinning_description_accessible : C1894R.string.screen_pinning_description;
            } else {
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_recents_group).setVisibility(4);
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_home_bg_light).setVisibility(0);
                this.mLayout.findViewById(C1894R.C1898id.screen_pinning_home_bg).setVisibility(0);
                i2 = isTouchExplorationEnabled ? C1894R.string.screen_pinning_description_recents_invisible_accessible : C1894R.string.screen_pinning_description_recents_invisible;
            }
            NavigationBarView navigationBarView = (NavigationBarView) optional.map(new ScreenPinningRequest$RequestWindowView$$ExternalSyntheticLambda1()).orElse(null);
            if (navigationBarView != null) {
                ((ImageView) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_back_icon)).setImageDrawable(navigationBarView.getBackDrawable());
                ((ImageView) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_home_icon)).setImageDrawable(navigationBarView.getHomeDrawable());
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.screen_pinning_description_bullet_gap_width);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(getContext().getText(i2), new BulletSpan(dimensionPixelSize), 0);
            spannableStringBuilder.append(System.lineSeparator());
            spannableStringBuilder.append(getContext().getText(C1894R.string.screen_pinning_exposes_personal_data), new BulletSpan(dimensionPixelSize), 0);
            spannableStringBuilder.append(System.lineSeparator());
            spannableStringBuilder.append(getContext().getText(C1894R.string.screen_pinning_can_open_other_apps), new BulletSpan(dimensionPixelSize), 0);
            ((TextView) this.mLayout.findViewById(C1894R.C1898id.screen_pinning_description)).setText(spannableStringBuilder);
            if (isTouchExplorationEnabled) {
                i3 = 4;
            }
            this.mLayout.findViewById(C1894R.C1898id.screen_pinning_back_bg).setVisibility(i3);
            this.mLayout.findViewById(C1894R.C1898id.screen_pinning_back_bg_light).setVisibility(i3);
            addView(this.mLayout, ScreenPinningRequest.this.getRequestLayoutParams(i));
        }

        private void swapChildrenIfRtlAndVertical(View view) {
            if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
                LinearLayout linearLayout = (LinearLayout) view;
                if (linearLayout.getOrientation() == 1) {
                    int childCount = linearLayout.getChildCount();
                    ArrayList arrayList = new ArrayList(childCount);
                    for (int i = 0; i < childCount; i++) {
                        arrayList.add(linearLayout.getChildAt(i));
                    }
                    linearLayout.removeAllViews();
                    for (int i2 = childCount - 1; i2 >= 0; i2--) {
                        linearLayout.addView((View) arrayList.get(i2));
                    }
                }
            }
        }

        public void onDetachedFromWindow() {
            ScreenPinningRequest.this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged() {
            removeAllViews();
            inflateView(getRotation(this.mContext));
        }

        /* access modifiers changed from: private */
        public int getRotation(Context context) {
            if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                return 0;
            }
            return RotationUtils.getRotation(context);
        }
    }
}
