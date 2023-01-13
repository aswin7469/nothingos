package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.C1894R;
import com.android.systemui.DualToneHandler;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.connectivity.MobileSignalControllerEx;
import java.util.ArrayList;

public class StatusBarMobileView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    private static final String TAG = "StatusBarMobileView";
    private StatusBarIconView mDotView;
    private DualToneHandler mDualToneHandler;
    private boolean mForceHidden;
    private ImageView mIn;
    private View mInoutContainer;
    private boolean mIsRtl = false;
    private ImageView mMobile;
    private SignalDrawable mMobileDrawable;
    private LinearLayout mMobileGroup;
    private ImageView mMobileRoaming;
    private View mMobileRoamingSpace;
    private ImageView mMobileType;
    private Drawable mNewMobileDrawable = null;
    private ImageView mOut;
    private boolean mProviderModel;
    private String mSlot;
    private StatusBarSignalPolicy.MobileIconState mState;
    private int mVisibleState = -1;
    private ImageView mVolte;

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

    public static StatusBarMobileView fromContext(Context context, String str, boolean z) {
        StatusBarMobileView statusBarMobileView = (StatusBarMobileView) LayoutInflater.from(context).inflate(C1894R.layout.status_bar_mobile_signal_group, (ViewGroup) null);
        statusBarMobileView.setSlot(str);
        statusBarMobileView.init(z);
        statusBarMobileView.setVisibleState(0);
        return statusBarMobileView;
    }

    public StatusBarMobileView(Context context) {
        super(context);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
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

    private void init(boolean z) {
        this.mIsRtl = isLayoutRtl();
        this.mProviderModel = z;
        this.mDualToneHandler = new DualToneHandler(getContext());
        this.mMobileGroup = (LinearLayout) findViewById(C1894R.C1898id.mobile_group);
        this.mMobile = (ImageView) findViewById(C1894R.C1898id.mobile_signal);
        this.mMobileType = (ImageView) findViewById(C1894R.C1898id.mobile_type);
        if (this.mProviderModel) {
            this.mMobileRoaming = (ImageView) findViewById(C1894R.C1898id.mobile_roaming_large);
        } else {
            this.mMobileRoaming = (ImageView) findViewById(C1894R.C1898id.mobile_roaming);
        }
        this.mMobileRoamingSpace = findViewById(C1894R.C1898id.mobile_roaming_space);
        this.mIn = (ImageView) findViewById(C1894R.C1898id.mobile_in);
        this.mOut = (ImageView) findViewById(C1894R.C1898id.mobile_out);
        this.mInoutContainer = findViewById(C1894R.C1898id.inout_container);
        this.mVolte = (ImageView) findViewById(C1894R.C1898id.mobile_volte);
        SignalDrawable signalDrawable = new SignalDrawable(getContext());
        this.mMobileDrawable = signalDrawable;
        this.mMobile.setImageDrawable(signalDrawable);
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

    public void applyMobileState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z = true;
        if (mobileIconState == null) {
            if (getVisibility() == 8) {
                z = false;
            }
            setVisibility(8);
            this.mState = null;
        } else {
            StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
            if (mobileIconState2 == null) {
                this.mState = mobileIconState.copy();
                initViewState();
            } else {
                z = !mobileIconState2.equals(mobileIconState) ? updateState(mobileIconState.copy()) : false;
            }
        }
        if (z) {
            requestLayout();
        }
    }

    private void initViewState() {
        setContentDescription(this.mState.contentDescription);
        if (!this.mState.visible || this.mForceHidden) {
            this.mMobileGroup.setVisibility(8);
        } else {
            this.mMobileGroup.setVisibility(0);
        }
        if (this.mState.strengthId >= 0) {
            this.mMobile.setVisibility(0);
            this.mMobileDrawable.setLevel(this.mState.strengthId);
            updateMobileDrawable();
        } else {
            this.mMobile.setVisibility(8);
        }
        if (this.mState.typeId > 0) {
            this.mMobileType.setContentDescription(this.mState.typeContentDescription);
            this.mMobileType.setImageResource(this.mState.typeId);
            this.mMobileType.setVisibility(0);
        } else {
            this.mMobileType.setVisibility(8);
        }
        this.mMobile.setVisibility(this.mState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(this.mState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(this.mState.roaming ? 0 : 8);
        this.mIn.setVisibility(this.mState.activityIn ? 0 : 8);
        this.mOut.setVisibility(this.mState.activityOut ? 0 : 8);
        this.mInoutContainer.setVisibility(8);
        if (this.mState.volteId > 0) {
            this.mVolte.setImageResource(this.mState.volteId);
            this.mVolte.setVisibility(0);
            return;
        }
        this.mVolte.setVisibility(8);
    }

    private boolean updateState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z;
        setContentDescription(mobileIconState.contentDescription);
        boolean z2 = false;
        int i = (!mobileIconState.visible || this.mForceHidden) ? 8 : 0;
        if (i == this.mMobileGroup.getVisibility() || this.mVisibleState != 0) {
            z = false;
        } else {
            this.mMobileGroup.setVisibility(i);
            z = true;
        }
        if (mobileIconState.strengthId >= 0) {
            this.mMobileDrawable.setLevel(mobileIconState.strengthId);
            updateMobileDrawable();
            this.mMobile.setVisibility(0);
        } else {
            this.mMobile.setVisibility(8);
        }
        if (this.mState.typeId != mobileIconState.typeId) {
            z |= mobileIconState.typeId == 0 || this.mState.typeId == 0;
            if (mobileIconState.typeId != 0) {
                this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
                this.mMobileType.setImageResource(mobileIconState.typeId);
                this.mMobileType.setVisibility(0);
            } else {
                this.mMobileType.setVisibility(8);
            }
        }
        this.mMobile.setVisibility(mobileIconState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mIn.setVisibility(mobileIconState.activityIn ? 0 : 8);
        this.mOut.setVisibility(mobileIconState.activityOut ? 0 : 8);
        this.mInoutContainer.setVisibility(8);
        if (this.mState.volteId != mobileIconState.volteId) {
            if (mobileIconState.volteId != 0) {
                this.mVolte.setImageResource(mobileIconState.volteId);
                this.mVolte.setVisibility(0);
            } else {
                this.mVolte.setVisibility(8);
            }
        }
        if (!(mobileIconState.roaming == this.mState.roaming && mobileIconState.activityIn == this.mState.activityIn && mobileIconState.activityOut == this.mState.activityOut && mobileIconState.showTriangle == this.mState.showTriangle)) {
            z2 = true;
        }
        boolean z3 = z | z2;
        this.mState = mobileIconState;
        return z3;
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        if (!DarkIconDispatcher.isInAreas(arrayList, this)) {
            f = 0.0f;
        }
        this.mMobile.setImageTintList(ColorStateList.valueOf(this.mDualToneHandler.getSingleColor(f)));
        ColorStateList valueOf = ColorStateList.valueOf(DarkIconDispatcher.getTint(arrayList, this, i));
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
        this.mDotView.setIconColor(i, false);
    }

    public String getSlot() {
        return this.mSlot;
    }

    public void setSlot(String str) {
        this.mSlot = str;
    }

    public void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mMobile.setImageTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    public void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    public boolean isIconVisible() {
        return this.mState.visible && !this.mForceHidden;
    }

    public void setVisibleState(int i, boolean z) {
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            if (i == 0) {
                this.mMobileGroup.setVisibility(0);
                this.mDotView.setVisibility(8);
            } else if (i != 1) {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(4);
            } else {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(0);
            }
        }
    }

    public void forceHidden(boolean z) {
        if (this.mForceHidden != z) {
            this.mForceHidden = z;
            updateState(this.mState);
            requestLayout();
        }
    }

    public int getVisibleState() {
        return this.mVisibleState;
    }

    public StatusBarSignalPolicy.MobileIconState getState() {
        return this.mState;
    }

    public String toString() {
        return "StatusBarMobileView(slot=" + this.mSlot + " state=" + this.mState + NavigationBarInflaterView.KEY_CODE_END;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updateMobileDrawable();
    }

    private void updateMobileDrawable() {
        ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).initSidePadding(this.mMobileDrawable, this.mVolte, this.mMobileRoaming);
        Drawable checkClipOutRect = ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).checkClipOutRect(this.mContext, this.mMobileDrawable, this.mMobile, this.mVolte, this.mMobileRoaming);
        if (checkClipOutRect != null) {
            this.mNewMobileDrawable = checkClipOutRect;
            this.mMobile.setImageDrawable(checkClipOutRect);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (isLayoutRtl() != this.mIsRtl) {
            this.mIsRtl = isLayoutRtl();
            updateMobileDrawable();
        }
    }
}
