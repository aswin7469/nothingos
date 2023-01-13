package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import java.util.ArrayList;

public class StatusBarWifiView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    private static final String TAG = "StatusBarWifiView";
    private View mAirplaneSpacer;
    private StatusBarIconView mDotView;
    private ImageView mIn;
    private View mInoutContainer;
    private ImageView mOut;
    private View mSignalSpacer;
    private String mSlot;
    private StatusBarSignalPolicy.WifiIconState mState;
    private int mVisibleState = -1;
    private LinearLayout mWifiGroup;
    private ImageView mWifiIcon;

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

    public static StatusBarWifiView fromContext(Context context, String str) {
        StatusBarWifiView statusBarWifiView = (StatusBarWifiView) LayoutInflater.from(context).inflate(C1894R.layout.status_bar_wifi_group, (ViewGroup) null);
        statusBarWifiView.setSlot(str);
        statusBarWifiView.init();
        statusBarWifiView.setVisibleState(0);
        return statusBarWifiView;
    }

    public StatusBarWifiView(Context context) {
        super(context);
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setSlot(String str) {
        this.mSlot = str;
    }

    public void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mWifiIcon.setImageTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    public void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    public String getSlot() {
        return this.mSlot;
    }

    public boolean isIconVisible() {
        StatusBarSignalPolicy.WifiIconState wifiIconState = this.mState;
        return wifiIconState != null && wifiIconState.visible;
    }

    public void setVisibleState(int i, boolean z) {
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            if (i == 0) {
                this.mWifiGroup.setVisibility(0);
                this.mDotView.setVisibility(8);
            } else if (i != 1) {
                this.mWifiGroup.setVisibility(8);
                this.mDotView.setVisibility(8);
            } else {
                this.mWifiGroup.setVisibility(8);
                this.mDotView.setVisibility(0);
            }
        }
    }

    public int getVisibleState() {
        return this.mVisibleState;
    }

    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (((float) rect.left) + translationX);
        rect.right = (int) (((float) rect.right) + translationX);
        rect.top = (int) (((float) rect.top) + translationY);
        rect.bottom = (int) (((float) rect.bottom) + translationY);
    }

    private void init() {
        this.mWifiGroup = (LinearLayout) findViewById(C1894R.C1898id.wifi_group);
        this.mWifiIcon = (ImageView) findViewById(C1894R.C1898id.wifi_signal);
        this.mIn = (ImageView) findViewById(C1894R.C1898id.wifi_in);
        this.mOut = (ImageView) findViewById(C1894R.C1898id.wifi_out);
        this.mSignalSpacer = findViewById(C1894R.C1898id.wifi_signal_spacer);
        this.mAirplaneSpacer = findViewById(C1894R.C1898id.wifi_airplane_spacer);
        this.mInoutContainer = findViewById(C1894R.C1898id.inout_container);
        initDotView();
    }

    private void initDotView() {
        StatusBarIconView statusBarIconView = new StatusBarIconView(this.mContext, this.mSlot, (StatusBarNotification) null);
        this.mDotView = statusBarIconView;
        statusBarIconView.setVisibleState(1);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.status_bar_icon_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 8388627;
        addView(this.mDotView, layoutParams);
    }

    public void applyWifiState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        boolean z = true;
        if (wifiIconState == null) {
            if (getVisibility() == 8) {
                z = false;
            }
            setVisibility(8);
            this.mState = null;
        } else {
            StatusBarSignalPolicy.WifiIconState wifiIconState2 = this.mState;
            if (wifiIconState2 == null) {
                this.mState = wifiIconState.copy();
                initViewState();
            } else {
                z = !wifiIconState2.equals(wifiIconState) ? updateState(wifiIconState.copy()) : false;
            }
        }
        if (z) {
            requestLayout();
        }
    }

    private boolean updateState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        setContentDescription(wifiIconState.contentDescription);
        if (this.mState.resId != wifiIconState.resId && wifiIconState.resId >= 0) {
            this.mWifiIcon.setImageDrawable(this.mContext.getDrawable(wifiIconState.resId));
        }
        int i = 0;
        this.mIn.setVisibility(wifiIconState.activityIn ? 0 : 8);
        this.mOut.setVisibility(wifiIconState.activityOut ? 0 : 8);
        this.mInoutContainer.setVisibility(8);
        this.mAirplaneSpacer.setVisibility(wifiIconState.airplaneSpacerVisible ? 0 : 8);
        this.mSignalSpacer.setVisibility(wifiIconState.signalSpacerVisible ? 0 : 8);
        boolean z = (wifiIconState.activityIn == this.mState.activityIn && wifiIconState.activityOut == this.mState.activityOut) ? false : true;
        if (this.mState.visible != wifiIconState.visible) {
            z |= true;
            if (!wifiIconState.visible) {
                i = 8;
            }
            setVisibility(i);
        }
        this.mState = wifiIconState;
        return z;
    }

    private void initViewState() {
        setContentDescription(this.mState.contentDescription);
        if (this.mState.resId >= 0) {
            this.mWifiIcon.setImageDrawable(this.mContext.getDrawable(this.mState.resId));
        }
        int i = 0;
        this.mIn.setVisibility(this.mState.activityIn ? 0 : 8);
        this.mOut.setVisibility(this.mState.activityOut ? 0 : 8);
        this.mInoutContainer.setVisibility(8);
        this.mAirplaneSpacer.setVisibility(this.mState.airplaneSpacerVisible ? 0 : 8);
        this.mSignalSpacer.setVisibility(this.mState.signalSpacerVisible ? 0 : 8);
        if (!this.mState.visible) {
            i = 8;
        }
        setVisibility(i);
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        ColorStateList valueOf = ColorStateList.valueOf(tint);
        this.mWifiIcon.setImageTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mDotView.setDecorColor(tint);
        this.mDotView.setIconColor(tint, false);
    }

    public String toString() {
        return "StatusBarWifiView(slot=" + this.mSlot + " state=" + this.mState + NavigationBarInflaterView.KEY_CODE_END;
    }
}
