package com.android.p019wm.shell.pip.p020tv;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannedString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewRootImpl;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.pip.PipUtils;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* renamed from: com.android.wm.shell.pip.tv.TvPipMenuView */
public class TvPipMenuView extends FrameLayout implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private static final int FIRST_CUSTOM_ACTION_POSITION = 3;
    private static final String TAG = "TvPipMenuView";
    private final LinearLayout mActionButtonsContainer;
    private final List<TvPipMenuActionButton> mAdditionalButtons;
    private final ImageView mArrowDown;
    private final ImageView mArrowLeft;
    private final ImageView mArrowRight;
    private final ImageView mArrowUp;
    private boolean mButtonMenuIsVisible;
    private final TvPipMenuActionButton mCloseButton;
    private Rect mCurrentPipBounds;
    private final View mEduTextContainerView;
    private final int mEduTextFadeExitAnimationDurationMs;
    private int mEduTextHeight;
    private final int mEduTextSlideExitAnimationDurationMs;
    private final TextView mEduTextView;
    private final TvPipMenuActionButton mExpandButton;
    private View mFocusedButton;
    private final HorizontalScrollView mHorizontalScrollView;
    private Listener mListener;
    private final View mMenuFrameView;
    private boolean mMoveMenuIsVisible;
    private final View mPipFrameView;
    private final int mPipMenuBorderWidth;
    private final int mPipMenuFadeAnimationDuration;
    private final int mPipMenuOuterSpace;
    private final View mPipView;
    private final int mResizeAnimationDuration;
    private final ScrollView mScrollView;
    private boolean mSwitchingOrientation;

    /* renamed from: com.android.wm.shell.pip.tv.TvPipMenuView$Listener */
    interface Listener {
        void onBackPress();

        void onCloseButtonClick();

        void onEnterMoveMode();

        boolean onExitMoveMode();

        void onFullscreenButtonClick();

        boolean onPipMovement(int i);

        void onToggleExpandedMode();
    }

    private boolean checkGravity(int i, int i2) {
        return (i & i2) == i2;
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

    public TvPipMenuView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAdditionalButtons = new ArrayList();
        inflate(context, C3353R.layout.tv_pip_menu, this);
        LinearLayout linearLayout = (LinearLayout) findViewById(C3353R.C3356id.tv_pip_menu_action_buttons);
        this.mActionButtonsContainer = linearLayout;
        linearLayout.findViewById(C3353R.C3356id.tv_pip_menu_fullscreen_button).setOnClickListener(this);
        TvPipMenuActionButton tvPipMenuActionButton = (TvPipMenuActionButton) linearLayout.findViewById(C3353R.C3356id.tv_pip_menu_close_button);
        this.mCloseButton = tvPipMenuActionButton;
        tvPipMenuActionButton.setOnClickListener(this);
        tvPipMenuActionButton.setIsCustomCloseAction(true);
        linearLayout.findViewById(C3353R.C3356id.tv_pip_menu_move_button).setOnClickListener(this);
        TvPipMenuActionButton tvPipMenuActionButton2 = (TvPipMenuActionButton) findViewById(C3353R.C3356id.tv_pip_menu_expand_button);
        this.mExpandButton = tvPipMenuActionButton2;
        tvPipMenuActionButton2.setOnClickListener(this);
        this.mScrollView = (ScrollView) findViewById(C3353R.C3356id.tv_pip_menu_scroll);
        this.mHorizontalScrollView = (HorizontalScrollView) findViewById(C3353R.C3356id.tv_pip_menu_horizontal_scroll);
        this.mMenuFrameView = findViewById(C3353R.C3356id.tv_pip_menu_frame);
        this.mPipFrameView = findViewById(C3353R.C3356id.tv_pip_border);
        this.mPipView = findViewById(C3353R.C3356id.tv_pip);
        this.mArrowUp = (ImageView) findViewById(C3353R.C3356id.tv_pip_menu_arrow_up);
        this.mArrowRight = (ImageView) findViewById(C3353R.C3356id.tv_pip_menu_arrow_right);
        this.mArrowDown = (ImageView) findViewById(C3353R.C3356id.tv_pip_menu_arrow_down);
        this.mArrowLeft = (ImageView) findViewById(C3353R.C3356id.tv_pip_menu_arrow_left);
        this.mEduTextView = (TextView) findViewById(C3353R.C3356id.tv_pip_menu_edu_text);
        this.mEduTextContainerView = findViewById(C3353R.C3356id.tv_pip_menu_edu_text_container);
        this.mResizeAnimationDuration = context.getResources().getInteger(C3353R.integer.config_pipResizeAnimationDuration);
        this.mPipMenuFadeAnimationDuration = context.getResources().getInteger(C3353R.integer.pip_menu_fade_animation_duration);
        this.mPipMenuOuterSpace = context.getResources().getDimensionPixelSize(C3353R.dimen.pip_menu_outer_space);
        this.mPipMenuBorderWidth = context.getResources().getDimensionPixelSize(C3353R.dimen.pip_menu_border_width);
        this.mEduTextHeight = context.getResources().getDimensionPixelSize(C3353R.dimen.pip_menu_edu_text_view_height);
        this.mEduTextFadeExitAnimationDurationMs = context.getResources().getInteger(C3353R.integer.pip_edu_text_view_exit_animation_duration_ms);
        this.mEduTextSlideExitAnimationDurationMs = context.getResources().getInteger(C3353R.integer.pip_edu_text_window_exit_animation_duration_ms);
        initEduText();
    }

    /* access modifiers changed from: package-private */
    public void initEduText() {
        SpannedString spannedString = (SpannedString) getResources().getText(C3353R.string.pip_edu_text);
        SpannableString spannableString = new SpannableString(spannedString);
        Arrays.stream((T[]) (Annotation[]) spannedString.getSpans(0, spannedString.length(), Annotation.class)).findFirst().ifPresent(new TvPipMenuView$$ExternalSyntheticLambda6(this, spannableString, spannedString));
        this.mEduTextView.setText(spannableString);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initEduText$0$com-android-wm-shell-pip-tv-TvPipMenuView  reason: not valid java name */
    public /* synthetic */ void m3488lambda$initEduText$0$comandroidwmshellpiptvTvPipMenuView(SpannableString spannableString, SpannedString spannedString, Annotation annotation) {
        Drawable drawable = getResources().getDrawable(C3353R.C3355drawable.home_icon, this.mContext.getTheme());
        if (drawable != null) {
            drawable.mutate();
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            spannableString.setSpan(new CenteredImageSpan(drawable), spannedString.getSpanStart(annotation), spannedString.getSpanEnd(annotation), 33);
        }
    }

    /* access modifiers changed from: package-private */
    public void setEduTextActive(boolean z) {
        this.mEduTextView.setSelected(z);
    }

    /* access modifiers changed from: package-private */
    public void hideEduText() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mEduTextHeight, 0});
        ofInt.setDuration((long) this.mEduTextSlideExitAnimationDurationMs);
        ofInt.setInterpolator(TvPipInterpolators.BROWSE);
        ofInt.addUpdateListener(new TvPipMenuView$$ExternalSyntheticLambda2(this));
        this.mEduTextView.animate().alpha(0.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) this.mEduTextFadeExitAnimationDurationMs).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda3(this)).start();
        ofInt.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$hideEduText$1$com-android-wm-shell-pip-tv-TvPipMenuView  reason: not valid java name */
    public /* synthetic */ void m3486lambda$hideEduText$1$comandroidwmshellpiptvTvPipMenuView(ValueAnimator valueAnimator) {
        this.mEduTextHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$hideEduText$2$com-android-wm-shell-pip-tv-TvPipMenuView  reason: not valid java name */
    public /* synthetic */ void m3487lambda$hideEduText$2$comandroidwmshellpiptvTvPipMenuView() {
        this.mEduTextContainerView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void onPipTransitionStarted(Rect rect) {
        Rect rect2 = this.mCurrentPipBounds;
        if (!(rect2 == null || rect == null || !PipUtils.aspectRatioChanged(((float) rect2.width()) / ((float) this.mCurrentPipBounds.height()), ((float) rect.width()) / ((float) rect.height())))) {
            this.mPipView.animate().alpha(1.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) (this.mResizeAnimationDuration / 2)).start();
        }
        boolean z = (rect.height() > rect.width()) != (this.mActionButtonsContainer.getOrientation() == 1);
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onPipTransitionStarted(), orientation changed %b", new Object[]{TAG, Boolean.valueOf(z)});
        if (z) {
            if (this.mButtonMenuIsVisible) {
                this.mSwitchingOrientation = true;
                this.mActionButtonsContainer.animate().alpha(0.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) (this.mResizeAnimationDuration / 2)).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda1(this, rect));
                return;
            }
            changeButtonScrollOrientation(rect);
            updateButtonGravity(rect);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPipTransitionStarted$3$com-android-wm-shell-pip-tv-TvPipMenuView */
    public /* synthetic */ void mo50666x3a588eaa(Rect rect) {
        changeButtonScrollOrientation(rect);
        updateButtonGravity(rect);
    }

    /* access modifiers changed from: package-private */
    public void onPipTransitionFinished() {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onPipTransitionFinished()", new Object[]{TAG});
        this.mPipView.animate().alpha(0.0f).setDuration((long) (this.mResizeAnimationDuration / 2)).setInterpolator(TvPipInterpolators.ENTER).start();
        if (this.mSwitchingOrientation) {
            this.mActionButtonsContainer.animate().alpha(1.0f).setInterpolator(TvPipInterpolators.ENTER).setDuration((long) (this.mResizeAnimationDuration / 2));
        } else {
            refocusPreviousButton();
        }
        this.mSwitchingOrientation = false;
    }

    /* access modifiers changed from: package-private */
    public void updateBounds(Rect rect) {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: updateLayout, width: %s, height: %s", new Object[]{TAG, Integer.valueOf(rect.width()), Integer.valueOf(rect.height())});
        this.mCurrentPipBounds = rect;
        if (!this.mSwitchingOrientation) {
            updateButtonGravity(rect);
        }
        updatePipFrameBounds();
    }

    private void changeButtonScrollOrientation(Rect rect) {
        int i = rect.height() > rect.width() ? 1 : 0;
        ViewGroup viewGroup = i != 0 ? this.mHorizontalScrollView : this.mScrollView;
        ViewGroup viewGroup2 = i != 0 ? this.mScrollView : this.mHorizontalScrollView;
        if (viewGroup.getChildCount() == 1) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: orientation changed", new Object[]{TAG});
            viewGroup.removeView(this.mActionButtonsContainer);
            viewGroup.setVisibility(8);
            this.mActionButtonsContainer.setOrientation(i);
            viewGroup2.addView(this.mActionButtonsContainer);
            viewGroup2.setVisibility(0);
            View view = this.mFocusedButton;
            if (view != null) {
                view.requestFocus();
            }
        }
    }

    private void updateButtonGravity(Rect rect) {
        boolean z = rect.height() > rect.width();
        int max = Math.max(this.mActionButtonsContainer.getHeight(), this.mActionButtonsContainer.getWidth());
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: buttons container width: %s, height: %s", new Object[]{TAG, Integer.valueOf(this.mActionButtonsContainer.getWidth()), Integer.valueOf(this.mActionButtonsContainer.getHeight())});
        boolean z2 = !z ? max < rect.width() : max < rect.height();
        int i = z2 ? 17 : z ? 1 : 16;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionButtonsContainer.getLayoutParams();
        layoutParams.gravity = i;
        this.mActionButtonsContainer.setLayoutParams(layoutParams);
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: vertical: %b, buttonsFit: %b, gravity: %s", new Object[]{TAG, Boolean.valueOf(z), Boolean.valueOf(z2), Gravity.toString(i)});
    }

    private void refocusPreviousButton() {
        Rect rect;
        if (!this.mMoveMenuIsVisible && (rect = this.mCurrentPipBounds) != null && this.mFocusedButton != null) {
            boolean z = rect.height() > this.mCurrentPipBounds.width();
            if (!this.mFocusedButton.hasFocus()) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: request focus from: %s", new Object[]{TAG, this.mFocusedButton});
                this.mFocusedButton.requestFocus();
            } else {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: already focused: %s", new Object[]{TAG, this.mFocusedButton});
            }
            Rect rect2 = new Rect();
            Rect rect3 = new Rect();
            if (z) {
                this.mScrollView.getDrawingRect(rect3);
            } else {
                this.mHorizontalScrollView.getDrawingRect(rect3);
            }
            this.mFocusedButton.getHitRect(rect2);
            if (rect3.contains(rect2)) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: not scrolling", new Object[]{TAG});
                return;
            }
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: scrolling to focused button", new Object[]{TAG});
            if (z) {
                this.mScrollView.smoothScrollTo((int) this.mFocusedButton.getX(), (int) this.mFocusedButton.getY());
            } else {
                this.mHorizontalScrollView.smoothScrollTo((int) this.mFocusedButton.getX(), (int) this.mFocusedButton.getY());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Rect getPipMenuContainerBounds(Rect rect) {
        Rect rect2 = new Rect(rect);
        int i = this.mPipMenuOuterSpace;
        rect2.inset(-i, -i);
        rect2.bottom += this.mEduTextHeight;
        return rect2;
    }

    private void updatePipFrameBounds() {
        ViewGroup.LayoutParams layoutParams = this.mPipFrameView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.mCurrentPipBounds.width() + (this.mPipMenuBorderWidth * 2);
            layoutParams.height = this.mCurrentPipBounds.height() + (this.mPipMenuBorderWidth * 2);
            this.mPipFrameView.setLayoutParams(layoutParams);
        }
        ViewGroup.LayoutParams layoutParams2 = this.mPipView.getLayoutParams();
        if (layoutParams2 != null) {
            layoutParams2.width = this.mCurrentPipBounds.width();
            layoutParams2.height = this.mCurrentPipBounds.height();
            this.mPipView.setLayoutParams(layoutParams2);
        }
    }

    /* access modifiers changed from: package-private */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void setExpandedModeEnabled(boolean z) {
        this.mExpandButton.setVisibility(z ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public void setIsExpanded(boolean z) {
        this.mExpandButton.setImageResource(z ? C3353R.C3355drawable.pip_ic_collapse : C3353R.C3355drawable.pip_ic_expand);
        this.mExpandButton.setTextAndDescription(z ? C3353R.string.pip_collapse : C3353R.string.pip_expand);
    }

    /* access modifiers changed from: package-private */
    public void showMoveMenu(int i) {
        this.mButtonMenuIsVisible = false;
        this.mMoveMenuIsVisible = true;
        showButtonsMenu(false);
        showMovementHints(i);
        setFrameHighlighted(true);
    }

    /* access modifiers changed from: package-private */
    public void showButtonsMenu() {
        this.mButtonMenuIsVisible = true;
        this.mMoveMenuIsVisible = false;
        showButtonsMenu(true);
        hideMovementHints();
        setFrameHighlighted(true);
        if (this.mFocusedButton == null) {
            this.mFocusedButton = this.mActionButtonsContainer.getChildAt(1);
            this.mScrollView.scrollTo(0, 0);
            this.mHorizontalScrollView.scrollTo(isLayoutRtl() ? this.mActionButtonsContainer.getWidth() : 0, 0);
        }
        refocusPreviousButton();
    }

    /* access modifiers changed from: package-private */
    public void hideAllUserControls() {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: hideAllUserControls()", new Object[]{TAG});
        this.mFocusedButton = null;
        this.mButtonMenuIsVisible = false;
        this.mMoveMenuIsVisible = false;
        showButtonsMenu(false);
        hideMovementHints();
        setFrameHighlighted(false);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            hideAllUserControls();
        }
    }

    private void animateAlphaTo(float f, View view) {
        if (view.getAlpha() != f) {
            view.animate().alpha(f).setInterpolator(f == 0.0f ? TvPipInterpolators.EXIT : TvPipInterpolators.ENTER).setDuration((long) this.mPipMenuFadeAnimationDuration).withStartAction(new TvPipMenuView$$ExternalSyntheticLambda4(f, view)).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda5(f, view));
        }
    }

    static /* synthetic */ void lambda$animateAlphaTo$4(float f, View view) {
        if (f != 0.0f) {
            view.setVisibility(0);
        }
    }

    static /* synthetic */ void lambda$animateAlphaTo$5(float f, View view) {
        if (f == 0.0f) {
            view.setVisibility(8);
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdditionalActions(List<RemoteAction> list, RemoteAction remoteAction, Handler handler) {
        if (remoteAction != null) {
            setActionForButton(remoteAction, this.mCloseButton, handler);
        } else {
            this.mCloseButton.setTextAndDescription(C3353R.string.pip_close);
            this.mCloseButton.setImageResource(C3353R.C3355drawable.pip_ic_close_white);
        }
        this.mCloseButton.setIsCustomCloseAction(remoteAction != null);
        this.mCloseButton.setEnabled(true);
        int size = list.size();
        int size2 = this.mAdditionalButtons.size();
        if (size > size2) {
            while (size > size2) {
                TvPipMenuActionButton tvPipMenuActionButton = new TvPipMenuActionButton(this.mContext);
                tvPipMenuActionButton.setOnClickListener(this);
                this.mActionButtonsContainer.addView(tvPipMenuActionButton, size2 + 3);
                this.mAdditionalButtons.add(tvPipMenuActionButton);
                size2++;
            }
        } else if (size < size2) {
            while (size < size2) {
                View view = this.mAdditionalButtons.get(size2 - 1);
                view.setVisibility(8);
                view.setTag((Object) null);
                size2--;
            }
        }
        for (int i = 0; i < size; i++) {
            RemoteAction remoteAction2 = list.get(i);
            TvPipMenuActionButton tvPipMenuActionButton2 = this.mAdditionalButtons.get(i);
            if (PipUtils.remoteActionsMatch(remoteAction2, remoteAction)) {
                tvPipMenuActionButton2.setVisibility(8);
            } else {
                setActionForButton(remoteAction2, tvPipMenuActionButton2, handler);
            }
        }
        Rect rect = this.mCurrentPipBounds;
        if (rect != null) {
            updateButtonGravity(rect);
            refocusPreviousButton();
        }
    }

    private void setActionForButton(RemoteAction remoteAction, TvPipMenuActionButton tvPipMenuActionButton, Handler handler) {
        tvPipMenuActionButton.setVisibility(0);
        if (remoteAction.getContentDescription().length() > 0) {
            tvPipMenuActionButton.setTextAndDescription(remoteAction.getContentDescription());
        } else {
            tvPipMenuActionButton.setTextAndDescription(remoteAction.getTitle());
        }
        tvPipMenuActionButton.setEnabled(remoteAction.isEnabled());
        tvPipMenuActionButton.setTag(remoteAction);
        Icon icon = remoteAction.getIcon();
        Context context = this.mContext;
        Objects.requireNonNull(tvPipMenuActionButton);
        icon.loadDrawableAsync(context, new TvPipMenuView$$ExternalSyntheticLambda0(tvPipMenuActionButton), handler);
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getWindowSurfaceControl() {
        SurfaceControl surfaceControl;
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl == null || (surfaceControl = viewRootImpl.getSurfaceControl()) == null || !surfaceControl.isValid()) {
            return null;
        }
        return surfaceControl;
    }

    public void onClick(View view) {
        if (this.mListener != null) {
            int id = view.getId();
            if (id == C3353R.C3356id.tv_pip_menu_fullscreen_button) {
                this.mListener.onFullscreenButtonClick();
            } else if (id == C3353R.C3356id.tv_pip_menu_move_button) {
                this.mListener.onEnterMoveMode();
            } else if (id == C3353R.C3356id.tv_pip_menu_close_button) {
                this.mListener.onCloseButtonClick();
            } else if (id == C3353R.C3356id.tv_pip_menu_expand_button) {
                this.mListener.onToggleExpandedMode();
            } else {
                RemoteAction remoteAction = (RemoteAction) view.getTag();
                if (remoteAction != null) {
                    try {
                        remoteAction.getActionIntent().send();
                    } catch (PendingIntent.CanceledException e) {
                        ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to send action, %s", new Object[]{TAG, e});
                    }
                } else {
                    ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: RemoteAction is null", new Object[]{TAG});
                }
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.mListener != null && keyEvent.getAction() == 1) {
            if (!this.mMoveMenuIsVisible) {
                this.mFocusedButton = this.mActionButtonsContainer.getFocusedChild();
            }
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 4) {
                if (keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            if (this.mListener.onPipMovement(keyEvent.getKeyCode()) || super.dispatchKeyEvent(keyEvent)) {
                                return true;
                            }
                            return false;
                        case 23:
                            break;
                    }
                }
                if (this.mListener.onExitMoveMode() || super.dispatchKeyEvent(keyEvent)) {
                    return true;
                }
                return false;
            }
            this.mListener.onBackPress();
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public void showMovementHints(int i) {
        float f = 1.0f;
        animateAlphaTo(checkGravity(i, 80) ? 1.0f : 0.0f, this.mArrowUp);
        animateAlphaTo(checkGravity(i, 48) ? 1.0f : 0.0f, this.mArrowDown);
        animateAlphaTo(checkGravity(i, 5) ? 1.0f : 0.0f, this.mArrowLeft);
        if (!checkGravity(i, 3)) {
            f = 0.0f;
        }
        animateAlphaTo(f, this.mArrowRight);
    }

    public void hideMovementHints() {
        animateAlphaTo(0.0f, this.mArrowUp);
        animateAlphaTo(0.0f, this.mArrowRight);
        animateAlphaTo(0.0f, this.mArrowDown);
        animateAlphaTo(0.0f, this.mArrowLeft);
    }

    public void showButtonsMenu(boolean z) {
        if (z) {
            this.mActionButtonsContainer.setVisibility(0);
            refocusPreviousButton();
        }
        animateAlphaTo(z ? 1.0f : 0.0f, this.mActionButtonsContainer);
    }

    private void setFrameHighlighted(boolean z) {
        this.mMenuFrameView.setActivated(z);
    }
}
