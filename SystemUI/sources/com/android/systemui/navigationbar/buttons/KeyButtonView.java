package com.android.systemui.navigationbar.buttons;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.nothingos.gamemode.GameModeHelper;
import com.android.systemui.Dependency;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
/* loaded from: classes.dex */
public class KeyButtonView extends ImageView implements ButtonInterface {
    private static final String TAG = KeyButtonView.class.getSimpleName();
    private AudioManager mAudioManager;
    private long mBackInterceptTime;
    private final Runnable mCheckLongPress;
    private int mCode;
    private int mContentDescriptionRes;
    private float mDarkIntensity;
    private long mDownTime;
    private boolean mGestureAborted;
    private boolean mHasOvalBg;
    private long mHomeInterceptTime;
    private final InputManager mInputManager;
    private boolean mIsVertical;
    @VisibleForTesting
    boolean mLongClicked;
    private final MetricsLogger mMetricsLogger;
    private View.OnClickListener mOnClickListener;
    private final Paint mOvalBgPaint;
    private final OverviewProxyService mOverviewProxyService;
    private final boolean mPlaySounds;
    private final KeyButtonRipple mRipple;
    private Toast mToast;
    private int mTouchDownX;
    private int mTouchDownY;
    private final UiEventLogger mUiEventLogger;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum NavBarButtonEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_HOME_BUTTON_TAP(533),
        NAVBAR_BACK_BUTTON_TAP(534),
        NAVBAR_OVERVIEW_BUTTON_TAP(535),
        NAVBAR_HOME_BUTTON_LONGPRESS(536),
        NAVBAR_BACK_BUTTON_LONGPRESS(537),
        NAVBAR_OVERVIEW_BUTTON_LONGPRESS(538),
        NONE(0);
        
        private final int mId;

        NavBarButtonEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public KeyButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyButtonView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, InputManager.getInstance(), new UiEventLoggerImpl());
    }

    @VisibleForTesting
    public KeyButtonView(Context context, AttributeSet attributeSet, int i, InputManager inputManager, UiEventLogger uiEventLogger) {
        super(context, attributeSet);
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOvalBgPaint = new Paint(3);
        this.mHasOvalBg = false;
        this.mCheckLongPress = new Runnable() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonView.1
            @Override // java.lang.Runnable
            public void run() {
                if (KeyButtonView.this.isPressed()) {
                    if (!KeyButtonView.this.isLongClickable()) {
                        if (KeyButtonView.this.mCode != 0) {
                            KeyButtonView.this.sendEvent(0, 128);
                            KeyButtonView.this.sendAccessibilityEvent(2);
                        }
                        KeyButtonView.this.mLongClicked = true;
                        return;
                    }
                    KeyButtonView keyButtonView = KeyButtonView.this;
                    keyButtonView.mLongClicked = true;
                    if (keyButtonView.mCode == 3 && GameModeHelper.getInstance(((ImageView) KeyButtonView.this).mContext).isMistouchPreventionOn() && SystemClock.uptimeMillis() - KeyButtonView.this.mHomeInterceptTime >= 2000) {
                        KeyButtonView.this.mHomeInterceptTime = SystemClock.uptimeMillis();
                        KeyButtonView.this.showToast();
                        return;
                    }
                    KeyButtonView.this.performLongClick();
                }
            }
        };
        this.mUiEventLogger = uiEventLogger;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyButtonView, i, 0);
        this.mCode = obtainStyledAttributes.getInteger(R$styleable.KeyButtonView_keyCode, 0);
        this.mPlaySounds = obtainStyledAttributes.getBoolean(R$styleable.KeyButtonView_playSound, true);
        TypedValue typedValue = new TypedValue();
        if (obtainStyledAttributes.getValue(R$styleable.KeyButtonView_android_contentDescription, typedValue)) {
            this.mContentDescriptionRes = typedValue.resourceId;
        }
        obtainStyledAttributes.recycle();
        setClickable(true);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        KeyButtonRipple keyButtonRipple = new KeyButtonRipple(context, this);
        this.mRipple = keyButtonRipple;
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mInputManager = inputManager;
        setBackground(keyButtonRipple);
        setWillNotDraw(false);
        forceHasOverlappingRendering(false);
    }

    @Override // android.view.View
    public boolean isClickable() {
        return this.mCode != 0 || super.isClickable();
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
    }

    public void loadAsync(Icon icon) {
        new AsyncTask<Icon, Void, Drawable>() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonView.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Drawable doInBackground(Icon... iconArr) {
                return iconArr[0].loadDrawable(((ImageView) KeyButtonView.this).mContext);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Drawable drawable) {
                KeyButtonView.this.setImageDrawable(drawable);
            }
        }.execute(icon);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = this.mContentDescriptionRes;
        if (i != 0) {
            setContentDescription(((ImageView) this).mContext.getString(i));
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mCode != 0) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, null));
            if (!isLongClickable()) {
                return;
            }
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, null));
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            jumpDrawablesToCurrentState();
        }
    }

    public boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (i == 16 && this.mCode != 0) {
            sendEvent(0, 0, SystemClock.uptimeMillis());
            sendEvent(1, 0);
            sendAccessibilityEvent(1);
            playSoundEffect(0);
            return true;
        } else if (i == 32 && this.mCode != 0) {
            sendEvent(0, 128);
            sendEvent(1, 0);
            sendAccessibilityEvent(2);
            return true;
        } else {
            return super.performAccessibilityActionInternal(i, bundle);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        View.OnClickListener onClickListener;
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mGestureAborted = false;
        }
        if (this.mGestureAborted) {
            setPressed(false);
            return false;
        }
        if (action == 0) {
            this.mDownTime = SystemClock.uptimeMillis();
            this.mLongClicked = false;
            setPressed(true);
            this.mTouchDownX = (int) motionEvent.getRawX();
            this.mTouchDownY = (int) motionEvent.getRawY();
            if (this.mCode != 0) {
                sendEvent(0, 0, this.mDownTime);
            } else {
                performHapticFeedback(1);
            }
            if (!shouldShowSwipeUpUI) {
                playSoundEffect(0);
            }
            removeCallbacks(this.mCheckLongPress);
            postDelayed(this.mCheckLongPress, ViewConfiguration.getLongPressTimeout());
        } else if (action == 1) {
            boolean z = isPressed() && !this.mLongClicked;
            setPressed(false);
            boolean z2 = SystemClock.uptimeMillis() - this.mDownTime > 150;
            if (shouldShowSwipeUpUI) {
                if (z) {
                    performHapticFeedback(1);
                    playSoundEffect(0);
                }
            } else if (z2 && !this.mLongClicked) {
                performHapticFeedback(8);
            }
            int i = this.mCode;
            if (i != 0) {
                if (z) {
                    if (i == 4 && GameModeHelper.getInstance(((ImageView) this).mContext).isMistouchPreventionOn() && SystemClock.uptimeMillis() - this.mBackInterceptTime >= 2000) {
                        this.mBackInterceptTime = SystemClock.uptimeMillis();
                        showToast();
                        sendEvent(1, 32);
                    } else if (this.mCode == 3 && GameModeHelper.getInstance(((ImageView) this).mContext).isMistouchPreventionOn() && SystemClock.uptimeMillis() - this.mHomeInterceptTime >= 2000) {
                        this.mHomeInterceptTime = SystemClock.uptimeMillis();
                        showToast();
                        sendEvent(1, 32);
                        removeCallbacks(this.mCheckLongPress);
                    } else {
                        sendEvent(1, 0);
                        sendAccessibilityEvent(1);
                    }
                } else {
                    sendEvent(1, 32);
                }
            } else if (z && (onClickListener = this.mOnClickListener) != null) {
                onClickListener.onClick(this);
                sendAccessibilityEvent(1);
            }
            removeCallbacks(this.mCheckLongPress);
        } else if (action == 2) {
            int rawY = (int) motionEvent.getRawY();
            float quickStepTouchSlopPx = QuickStepContract.getQuickStepTouchSlopPx(getContext());
            if (Math.abs(((int) motionEvent.getRawX()) - this.mTouchDownX) > quickStepTouchSlopPx || Math.abs(rawY - this.mTouchDownY) > quickStepTouchSlopPx) {
                setPressed(false);
                removeCallbacks(this.mCheckLongPress);
            }
        } else if (action == 3) {
            setPressed(false);
            if (this.mCode != 0) {
                sendEvent(1, 32);
            }
            removeCallbacks(this.mCheckLongPress);
        }
        return true;
    }

    @Override // android.widget.ImageView, com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable == null) {
            return;
        }
        KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) drawable;
        keyButtonDrawable.setDarkIntensity(this.mDarkIntensity);
        boolean hasOvalBg = keyButtonDrawable.hasOvalBg();
        this.mHasOvalBg = hasOvalBg;
        if (hasOvalBg) {
            this.mOvalBgPaint.setColor(keyButtonDrawable.getDrawableBackgroundColor());
        }
        this.mRipple.setType(keyButtonDrawable.hasOvalBg() ? KeyButtonRipple.Type.OVAL : KeyButtonRipple.Type.ROUNDED_RECT);
    }

    @Override // android.view.View
    public void playSoundEffect(int i) {
        if (!this.mPlaySounds) {
            return;
        }
        this.mAudioManager.playSoundEffect(i, ActivityManager.getCurrentUser());
    }

    public void sendEvent(int i, int i2) {
        sendEvent(i, i2, SystemClock.uptimeMillis());
    }

    private void logSomePresses(int i, int i2) {
        NavBarButtonEvent navBarButtonEvent;
        boolean z = (i2 & 128) != 0;
        NavBarButtonEvent navBarButtonEvent2 = NavBarButtonEvent.NONE;
        if (i != 1 || !this.mLongClicked) {
            if ((i == 0 && !z) || (i2 & 32) != 0 || (i2 & 256) != 0) {
                return;
            }
            int i3 = this.mCode;
            if (i3 != 3) {
                if (i3 != 4) {
                    if (i3 != 187) {
                        navBarButtonEvent = navBarButtonEvent2;
                    } else if (z) {
                        navBarButtonEvent = NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_LONGPRESS;
                    } else {
                        navBarButtonEvent = NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_TAP;
                    }
                } else if (z) {
                    navBarButtonEvent = NavBarButtonEvent.NAVBAR_BACK_BUTTON_LONGPRESS;
                } else {
                    navBarButtonEvent = NavBarButtonEvent.NAVBAR_BACK_BUTTON_TAP;
                }
            } else if (z) {
                navBarButtonEvent = NavBarButtonEvent.NAVBAR_HOME_BUTTON_LONGPRESS;
            } else {
                navBarButtonEvent = NavBarButtonEvent.NAVBAR_HOME_BUTTON_TAP;
            }
            if (navBarButtonEvent == navBarButtonEvent2) {
                return;
            }
            this.mUiEventLogger.log(navBarButtonEvent);
        }
    }

    private void sendEvent(int i, int i2, long j) {
        this.mMetricsLogger.write(new LogMaker(931).setType(4).setSubtype(this.mCode).addTaggedData(933, Integer.valueOf(i)).addTaggedData(932, Integer.valueOf(i2)));
        logSomePresses(i, i2);
        if (this.mCode == 4 && i2 != 128) {
            String str = TAG;
            Log.i(str, "Back button event: " + KeyEvent.actionToString(i));
            if (i == 1) {
                this.mOverviewProxyService.notifyBackAction((i2 & 32) == 0, -1, -1, true, false);
            }
        }
        KeyEvent keyEvent = new KeyEvent(this.mDownTime, j, i, this.mCode, (i2 & 128) != 0 ? 1 : 0, 0, -1, 0, i2 | 8 | 64, 257);
        int displayId = getDisplay() != null ? getDisplay().getDisplayId() : -1;
        if (displayId != -1) {
            keyEvent.setDisplayId(displayId);
        }
        this.mInputManager.injectInputEvent(keyEvent, 0);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void abortCurrentGesture() {
        Log.d("b/63783866", "KeyButtonView.abortCurrentGesture");
        if (this.mCode != 0) {
            sendEvent(1, 32);
        }
        setPressed(false);
        this.mRipple.abortDelayedRipple();
        this.mGestureAborted = true;
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDarkIntensity(float f) {
        this.mDarkIntensity = f;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            ((KeyButtonDrawable) drawable).setDarkIntensity(f);
            invalidate();
        }
        this.mRipple.setDarkIntensity(f);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDelayTouchFeedback(boolean z) {
        this.mRipple.setDelayTouchFeedback(z);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.mHasOvalBg) {
            float min = Math.min(getWidth(), getHeight());
            canvas.drawOval(0.0f, 0.0f, min, min, this.mOvalBgPaint);
        }
        super.draw(canvas);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setVertical(boolean z) {
        this.mIsVertical = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast() {
        Toast makeText = Toast.makeText(((ImageView) this).mContext, R$string.click_again, 0);
        this.mToast = makeText;
        makeText.show();
    }
}
