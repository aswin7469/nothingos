package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.policy.SystemBarUtils;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import java.lang.ref.WeakReference;

public class KeyguardMessageArea extends TextView implements SecurityMessageDisplay {
    private static final long ANNOUNCEMENT_DELAY = 250;
    private static final Object ANNOUNCE_TOKEN = new Object();
    private static final int DEFAULT_COLOR = -1;
    private boolean mAltBouncerShowing;
    private boolean mBouncerShowing;
    private ViewGroup mContainer;
    private ColorStateList mDefaultColorState;
    private final Handler mHandler;
    private CharSequence mMessage;
    private ColorStateList mNextMessageColorState = ColorStateList.valueOf(-1);
    private int mTopMargin;

    public KeyguardMessageArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayerType(2, (Paint) null);
        this.mHandler = new Handler(Looper.myLooper());
        onThemeChanged();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContainer = (ViewGroup) getRootView().findViewById(C1893R.C1897id.keyguard_message_area_container);
    }

    /* access modifiers changed from: package-private */
    public void onConfigChanged() {
        int statusBarHeight;
        if (this.mContainer != null && this.mTopMargin != (statusBarHeight = SystemBarUtils.getStatusBarHeight(getContext()))) {
            this.mTopMargin = statusBarHeight;
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mContainer.getLayoutParams();
            marginLayoutParams.topMargin = this.mTopMargin;
            this.mContainer.setLayoutParams(marginLayoutParams);
        }
    }

    public void setNextMessageColor(ColorStateList colorStateList) {
        this.mNextMessageColorState = colorStateList;
    }

    /* access modifiers changed from: package-private */
    public void onThemeChanged() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16842806});
        ColorStateList valueOf = ColorStateList.valueOf(obtainStyledAttributes.getColor(0, -65536));
        obtainStyledAttributes.recycle();
        this.mDefaultColorState = valueOf;
        update();
    }

    /* access modifiers changed from: package-private */
    public void reloadColor() {
        this.mDefaultColorState = Utils.getColorAttr(getContext(), 16842806);
        update();
    }

    /* access modifiers changed from: package-private */
    public void onDensityOrFontScaleChanged() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(C1893R.style.Keyguard_TextView, new int[]{16842901});
        setTextSize(0, (float) obtainStyledAttributes.getDimensionPixelSize(0, 0));
        obtainStyledAttributes.recycle();
    }

    public void setMessage(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            securityMessageChanged(charSequence);
        } else {
            clearMessage();
        }
    }

    public void setMessage(int i) {
        setMessage(i != 0 ? getContext().getResources().getText(i) : null);
    }

    public void formatMessage(int i, Object... objArr) {
        setMessage((CharSequence) i != 0 ? getContext().getString(i, objArr) : null);
    }

    /* JADX WARNING: type inference failed for: r0v2, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.keyguard.KeyguardMessageArea findSecurityMessageDisplay(android.view.View r3) {
        /*
            r0 = 2131428148(0x7f0b0334, float:1.8477932E38)
            android.view.View r1 = r3.findViewById(r0)
            com.android.keyguard.KeyguardMessageArea r1 = (com.android.keyguard.KeyguardMessageArea) r1
            if (r1 != 0) goto L_0x0016
            android.view.View r1 = r3.getRootView()
            android.view.View r0 = r1.findViewById(r0)
            r1 = r0
            com.android.keyguard.KeyguardMessageArea r1 = (com.android.keyguard.KeyguardMessageArea) r1
        L_0x0016:
            if (r1 == 0) goto L_0x0019
            return r1
        L_0x0019:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Can't find keyguard_message_area in "
            r1.<init>((java.lang.String) r2)
            java.lang.Class r3 = r3.getClass()
            java.lang.StringBuilder r3 = r1.append((java.lang.Object) r3)
            java.lang.String r3 = r3.toString()
            r0.<init>((java.lang.String) r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardMessageArea.findSecurityMessageDisplay(android.view.View):com.android.keyguard.KeyguardMessageArea");
    }

    private void securityMessageChanged(CharSequence charSequence) {
        this.mMessage = charSequence;
        update();
        Handler handler = this.mHandler;
        Object obj = ANNOUNCE_TOKEN;
        handler.removeCallbacksAndMessages(obj);
        this.mHandler.postAtTime(new AnnounceRunnable(this, getText()), obj, SystemClock.uptimeMillis() + ANNOUNCEMENT_DELAY);
    }

    private void clearMessage() {
        this.mMessage = null;
        update();
    }

    /* access modifiers changed from: package-private */
    public void update() {
        CharSequence charSequence = this.mMessage;
        setVisibility((TextUtils.isEmpty(charSequence) || (!this.mBouncerShowing && !this.mAltBouncerShowing)) ? 4 : 0);
        setText(charSequence);
        ColorStateList colorStateList = this.mDefaultColorState;
        if (this.mNextMessageColorState.getDefaultColor() != -1) {
            colorStateList = this.mNextMessageColorState;
            this.mNextMessageColorState = ColorStateList.valueOf(-1);
        }
        if (this.mAltBouncerShowing) {
            colorStateList = ColorStateList.valueOf(-1);
        }
        setTextColor(colorStateList);
    }

    public void setBouncerShowing(boolean z) {
        if (this.mBouncerShowing != z) {
            this.mBouncerShowing = z;
            update();
        }
    }

    /* access modifiers changed from: package-private */
    public void setAltBouncerShowing(boolean z) {
        if (this.mAltBouncerShowing != z) {
            this.mAltBouncerShowing = z;
            update();
        }
    }

    private static class AnnounceRunnable implements Runnable {
        private final WeakReference<View> mHost;
        private final CharSequence mTextToAnnounce;

        AnnounceRunnable(View view, CharSequence charSequence) {
            this.mHost = new WeakReference<>(view);
            this.mTextToAnnounce = charSequence;
        }

        public void run() {
            View view = this.mHost.get();
            if (view != null) {
                view.announceForAccessibility(this.mTextToAnnounce);
            }
        }
    }
}
