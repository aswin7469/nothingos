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
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.buttons.KeyButtonViewEx;

public class KeyButtonView extends ImageView implements ButtonInterface {
    private static final String TAG = "KeyButtonView";
    private AudioManager mAudioManager;
    private final Runnable mCheckLongPress;
    /* access modifiers changed from: private */
    public int mCode;
    private int mContentDescriptionRes;
    private float mDarkIntensity;
    private long mDownTime;
    /* access modifiers changed from: private */
    public KeyButtonViewEx mEx;
    private boolean mGestureAborted;
    private boolean mHasOvalBg;
    private final InputManager mInputManager;
    private boolean mIsVertical;
    boolean mLongClicked;
    private final MetricsLogger mMetricsLogger;
    private View.OnClickListener mOnClickListener;
    private final Paint mOvalBgPaint;
    private final OverviewProxyService mOverviewProxyService;
    private final boolean mPlaySounds;
    private final KeyButtonRipple mRipple;
    private int mTouchDownX;
    private int mTouchDownY;
    private final UiEventLogger mUiEventLogger;

    public enum NavBarButtonEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_HOME_BUTTON_TAP(533),
        NAVBAR_BACK_BUTTON_TAP(534),
        NAVBAR_OVERVIEW_BUTTON_TAP(535),
        NAVBAR_IME_SWITCHER_BUTTON_TAP(923),
        NAVBAR_HOME_BUTTON_LONGPRESS(536),
        NAVBAR_BACK_BUTTON_LONGPRESS(537),
        NAVBAR_OVERVIEW_BUTTON_LONGPRESS(538),
        NONE(0);
        
        private final int mId;

        private NavBarButtonEvent(int i) {
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

    public KeyButtonView(Context context, AttributeSet attributeSet, int i, InputManager inputManager, UiEventLogger uiEventLogger) {
        super(context, attributeSet);
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOvalBgPaint = new Paint(3);
        this.mHasOvalBg = false;
        this.mCheckLongPress = new Runnable() {
            public void run() {
                if (!KeyButtonView.this.isPressed()) {
                    return;
                }
                if (!KeyButtonView.this.isLongClickable()) {
                    if (KeyButtonView.this.mCode != 0) {
                        KeyButtonView.this.sendEvent(0, 128);
                        KeyButtonView.this.sendAccessibilityEvent(2);
                    }
                    KeyButtonView.this.mLongClicked = true;
                } else if (KeyButtonView.this.mCode != 3 || !KeyButtonView.this.mEx.shouldInterceptHomeKey(KeyButtonView.this.mContext)) {
                    KeyButtonView.this.performLongClick();
                    KeyButtonView.this.mLongClicked = true;
                } else {
                    KeyButtonView.this.mLongClicked = true;
                }
            }
        };
        this.mUiEventLogger = uiEventLogger;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1893R.styleable.KeyButtonView, i, 0);
        this.mCode = obtainStyledAttributes.getInteger(1, 0);
        this.mPlaySounds = obtainStyledAttributes.getBoolean(2, true);
        TypedValue typedValue = new TypedValue();
        if (obtainStyledAttributes.getValue(0, typedValue)) {
            this.mContentDescriptionRes = typedValue.resourceId;
        }
        obtainStyledAttributes.recycle();
        setClickable(true);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        KeyButtonRipple keyButtonRipple = new KeyButtonRipple(context, this, C1893R.dimen.key_button_ripple_max_width);
        this.mRipple = keyButtonRipple;
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mInputManager = inputManager;
        setBackground(keyButtonRipple);
        setWillNotDraw(false);
        forceHasOverlappingRendering(false);
        this.mEx = (KeyButtonViewEx) NTDependencyEx.get(KeyButtonViewEx.class);
    }

    public boolean isClickable() {
        return this.mCode != 0 || super.isClickable();
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
    }

    public void loadAsync(Icon icon) {
        new AsyncTask<Icon, Void, Drawable>() {
            /* access modifiers changed from: protected */
            public Drawable doInBackground(Icon... iconArr) {
                return iconArr[0].loadDrawable(KeyButtonView.this.mContext);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Drawable drawable) {
                KeyButtonView.this.setImageDrawable(drawable);
            }
        }.execute(new Icon[]{icon});
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mContentDescriptionRes != 0) {
            setContentDescription(this.mContext.getString(this.mContentDescriptionRes));
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mCode != 0) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, (CharSequence) null));
            if (isLongClickable()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, (CharSequence) null));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
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
        } else if (i != 32 || this.mCode == 0) {
            return super.performAccessibilityActionInternal(i, bundle);
        } else {
            sendEvent(0, 128);
            sendEvent(1, 0);
            sendAccessibilityEvent(2);
            return true;
        }
    }

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
            this.mTouchDownX = (int) motionEvent.getX();
            this.mTouchDownY = (int) motionEvent.getY();
            if (this.mCode != 0) {
                sendEvent(0, 0, this.mDownTime);
            } else {
                performHapticFeedback(1);
            }
            if (!shouldShowSwipeUpUI) {
                playSoundEffect(0);
            }
            removeCallbacks(this.mCheckLongPress);
            postDelayed(this.mCheckLongPress, (long) ViewConfiguration.getLongPressTimeout());
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
                if (!z) {
                    sendEvent(1, 32);
                } else if (i == 4 && this.mEx.shouldInterceptBackKey(this.mContext)) {
                    sendEvent(1, 32);
                } else if (this.mCode != 3 || !this.mEx.shouldInterceptHomeKey(this.mContext)) {
                    sendEvent(1, 0);
                    sendAccessibilityEvent(1);
                } else {
                    sendEvent(1, 32);
                    removeCallbacks(this.mCheckLongPress);
                }
            } else if (z && (onClickListener = this.mOnClickListener) != null) {
                onClickListener.onClick(this);
                sendAccessibilityEvent(1);
            }
            removeCallbacks(this.mCheckLongPress);
        } else if (action == 2) {
            int y = (int) motionEvent.getY();
            float quickStepTouchSlopPx = QuickStepContract.getQuickStepTouchSlopPx(getContext());
            if (((float) Math.abs(((int) motionEvent.getX()) - this.mTouchDownX)) > quickStepTouchSlopPx || ((float) Math.abs(y - this.mTouchDownY)) > quickStepTouchSlopPx) {
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

    public void setImageDrawable(Drawable drawable) {
        KeyButtonRipple.Type type;
        super.setImageDrawable(drawable);
        if (drawable != null) {
            KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) drawable;
            keyButtonDrawable.setDarkIntensity(this.mDarkIntensity);
            boolean hasOvalBg = keyButtonDrawable.hasOvalBg();
            this.mHasOvalBg = hasOvalBg;
            if (hasOvalBg) {
                this.mOvalBgPaint.setColor(keyButtonDrawable.getDrawableBackgroundColor());
            }
            KeyButtonRipple keyButtonRipple = this.mRipple;
            if (keyButtonDrawable.hasOvalBg()) {
                type = KeyButtonRipple.Type.OVAL;
            } else {
                type = KeyButtonRipple.Type.ROUNDED_RECT;
            }
            keyButtonRipple.setType(type);
        }
    }

    public void playSoundEffect(int i) {
        if (this.mPlaySounds) {
            this.mAudioManager.playSoundEffect(i, ActivityManager.getCurrentUser());
        }
    }

    public void sendEvent(int i, int i2) {
        sendEvent(i, i2, SystemClock.uptimeMillis());
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void logSomePresses(int r4, int r5) {
        /*
            r3 = this;
            r0 = r5 & 128(0x80, float:1.794E-43)
            r1 = 1
            if (r0 == 0) goto L_0x0007
            r0 = r1
            goto L_0x0008
        L_0x0007:
            r0 = 0
        L_0x0008:
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r2 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NONE
            if (r4 != r1) goto L_0x0011
            boolean r1 = r3.mLongClicked
            if (r1 == 0) goto L_0x0011
            return
        L_0x0011:
            if (r4 != 0) goto L_0x0016
            if (r0 != 0) goto L_0x0016
            return
        L_0x0016:
            r4 = r5 & 32
            if (r4 != 0) goto L_0x004e
            r4 = r5 & 256(0x100, float:3.59E-43)
            if (r4 == 0) goto L_0x001f
            goto L_0x004e
        L_0x001f:
            int r4 = r3.mCode
            r5 = 3
            if (r4 == r5) goto L_0x003d
            r5 = 4
            if (r4 == r5) goto L_0x0035
            r5 = 187(0xbb, float:2.62E-43)
            if (r4 == r5) goto L_0x002c
            goto L_0x0045
        L_0x002c:
            if (r0 == 0) goto L_0x0031
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_LONGPRESS
            goto L_0x0033
        L_0x0031:
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_TAP
        L_0x0033:
            r2 = r4
            goto L_0x0045
        L_0x0035:
            if (r0 == 0) goto L_0x003a
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_BACK_BUTTON_LONGPRESS
            goto L_0x0033
        L_0x003a:
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_BACK_BUTTON_TAP
            goto L_0x0033
        L_0x003d:
            if (r0 == 0) goto L_0x0042
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_HOME_BUTTON_LONGPRESS
            goto L_0x0033
        L_0x0042:
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NAVBAR_HOME_BUTTON_TAP
            goto L_0x0033
        L_0x0045:
            com.android.systemui.navigationbar.buttons.KeyButtonView$NavBarButtonEvent r4 = com.android.systemui.navigationbar.buttons.KeyButtonView.NavBarButtonEvent.NONE
            if (r2 == r4) goto L_0x004e
            com.android.internal.logging.UiEventLogger r3 = r3.mUiEventLogger
            r3.log(r2)
        L_0x004e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.buttons.KeyButtonView.logSomePresses(int, int):void");
    }

    private void sendEvent(int i, int i2, long j) {
        int i3 = i2;
        this.mMetricsLogger.write(new LogMaker(931).setType(4).setSubtype(this.mCode).addTaggedData(933, Integer.valueOf(i)).addTaggedData(932, Integer.valueOf(i2)));
        logSomePresses(i, i2);
        if (this.mCode != 4 || i3 == 128) {
            int i4 = i;
        } else {
            Log.i(TAG, "Back button event: " + KeyEvent.actionToString(i));
            if (i == 1) {
                this.mOverviewProxyService.notifyBackAction((i3 & 32) == 0, -1, -1, true, false);
            }
        }
        KeyEvent keyEvent = new KeyEvent(this.mDownTime, j, i, this.mCode, (i3 & 128) != 0 ? 1 : 0, 0, -1, 0, i3 | 8 | 64, 257);
        int displayId = getDisplay() != null ? getDisplay().getDisplayId() : -1;
        if (displayId != -1) {
            keyEvent.setDisplayId(displayId);
        }
        this.mInputManager.injectInputEvent(keyEvent, 0);
    }

    public void abortCurrentGesture() {
        Log.d("b/63783866", "KeyButtonView.abortCurrentGesture");
        if (this.mCode != 0) {
            sendEvent(1, 32);
        }
        setPressed(false);
        this.mRipple.abortDelayedRipple();
        this.mGestureAborted = true;
    }

    public void setDarkIntensity(float f) {
        this.mDarkIntensity = f;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            ((KeyButtonDrawable) drawable).setDarkIntensity(f);
            invalidate();
        }
        this.mRipple.setDarkIntensity(f);
    }

    public void setDelayTouchFeedback(boolean z) {
        this.mRipple.setDelayTouchFeedback(z);
    }

    public void draw(Canvas canvas) {
        if (this.mHasOvalBg) {
            float min = (float) Math.min(getWidth(), getHeight());
            canvas.drawOval(0.0f, 0.0f, min, min, this.mOvalBgPaint);
        }
        super.draw(canvas);
    }

    public void setVertical(boolean z) {
        this.mIsVertical = z;
    }
}
