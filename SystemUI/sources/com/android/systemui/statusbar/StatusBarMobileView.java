package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.DualToneHandler;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
/* loaded from: classes.dex */
public class StatusBarMobileView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    private StatusBarIconView mDotView;
    private DualToneHandler mDualToneHandler;
    private boolean mForceHidden;
    private ImageView mIn;
    private View mInoutContainer;
    private ImageView mMobile;
    private SignalDrawable mMobileDrawable;
    private LinearLayout mMobileGroup;
    private ImageView mMobileRoaming;
    private View mMobileRoamingSpace;
    private ImageView mMobileType;
    private ImageView mOut;
    private boolean mProviderModel;
    private String mSlot;
    private StatusBarSignalPolicy.MobileIconState mState;
    private ImageView mVolte;
    private int mVisibleState = -1;
    private boolean mShowActivityInOut = false;
    private int mSidePadding = -1;
    private final Rect mClipOutRectLeftTop = new Rect();
    private final Rect mClipOutRectRightBottom = new Rect();

    public static StatusBarMobileView fromContext(Context context, String str, boolean z) {
        StatusBarMobileView statusBarMobileView = (StatusBarMobileView) LayoutInflater.from(context).inflate(R$layout.status_bar_mobile_signal_group, (ViewGroup) null);
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

    @Override // android.view.View
    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (rect.left + translationX);
        rect.right = (int) (rect.right + translationX);
        rect.top = (int) (rect.top + translationY);
        rect.bottom = (int) (rect.bottom + translationY);
    }

    private void init(boolean z) {
        this.mProviderModel = z;
        this.mDualToneHandler = new DualToneHandler(getContext());
        this.mMobileGroup = (LinearLayout) findViewById(R$id.mobile_group);
        this.mMobile = (ImageView) findViewById(R$id.mobile_signal);
        this.mMobileType = (ImageView) findViewById(R$id.mobile_type);
        if (this.mProviderModel) {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming_large);
        } else {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming);
        }
        this.mMobileRoamingSpace = findViewById(R$id.mobile_roaming_space);
        this.mIn = (ImageView) findViewById(R$id.mobile_in);
        this.mOut = (ImageView) findViewById(R$id.mobile_out);
        this.mInoutContainer = findViewById(R$id.inout_container);
        this.mVolte = (ImageView) findViewById(R$id.mobile_volte);
        SignalDrawable signalDrawable = new SignalDrawable(getContext());
        this.mMobileDrawable = signalDrawable;
        this.mMobile.setImageDrawable(signalDrawable);
        initDotView();
    }

    private void initDotView() {
        StatusBarIconView statusBarIconView = new StatusBarIconView(((FrameLayout) this).mContext, this.mSlot, null);
        this.mDotView = statusBarIconView;
        statusBarIconView.setVisibleState(1);
        int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_icon_size);
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
        if (needFixVisibleState()) {
            Log.d("StatusBarMobileView", "fix VisibleState width=" + getWidth() + " height=" + getHeight());
            setVisibleState(0, false);
            setVisibility(0);
            requestLayout();
        } else if (!needFixInVisibleState()) {
        } else {
            Log.d("StatusBarMobileView", "fix InVisibleState width=" + getWidth() + " height=" + getHeight());
            setVisibleState(-1, false);
            setVisibility(4);
            requestLayout();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void initViewState() {
        int i;
        int i2;
        setContentDescription(this.mState.contentDescription);
        if (!this.mState.visible || this.mForceHidden) {
            this.mMobileGroup.setVisibility(8);
        } else {
            this.mMobileGroup.setVisibility(0);
        }
        if (this.mState.strengthId >= 0) {
            this.mMobile.setVisibility(0);
            this.mMobileDrawable.setLevel(this.mState.strengthId);
        } else {
            this.mMobile.setVisibility(8);
        }
        StatusBarSignalPolicy.MobileIconState mobileIconState = this.mState;
        if (mobileIconState.typeId > 0) {
            this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
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
        View view = this.mInoutContainer;
        if (this.mShowActivityInOut) {
            StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
            if (mobileIconState2.activityIn || mobileIconState2.activityOut) {
                i = 0;
                view.setVisibility(i);
                i2 = this.mState.volteId;
                if (i2 <= 0) {
                    this.mVolte.setImageResource(i2);
                    this.mVolte.setVisibility(0);
                    return;
                }
                this.mVolte.setVisibility(8);
                return;
            }
        }
        i = 8;
        view.setVisibility(i);
        i2 = this.mState.volteId;
        if (i2 <= 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00c2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean updateState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z;
        int i;
        int i2;
        int i3;
        boolean z2;
        StatusBarSignalPolicy.MobileIconState mobileIconState2;
        setContentDescription(mobileIconState.contentDescription);
        boolean z3 = false;
        int i4 = (!mobileIconState.visible || this.mForceHidden) ? 8 : 0;
        if (i4 == this.mMobileGroup.getVisibility() || this.mVisibleState != 0) {
            z = false;
        } else {
            this.mMobileGroup.setVisibility(i4);
            z = true;
        }
        int i5 = mobileIconState.strengthId;
        if (i5 >= 0) {
            this.mMobileDrawable.setLevel(i5);
            this.mMobile.setVisibility(0);
        } else {
            this.mMobile.setVisibility(8);
        }
        int i6 = this.mState.typeId;
        int i7 = mobileIconState.typeId;
        if (i6 != i7) {
            z |= i7 == 0 || i6 == 0;
            if (i7 != 0) {
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
        View view = this.mInoutContainer;
        if (this.mShowActivityInOut) {
            StatusBarSignalPolicy.MobileIconState mobileIconState3 = this.mState;
            if (mobileIconState3.activityIn || mobileIconState3.activityOut) {
                i = 0;
                view.setVisibility(i);
                i2 = this.mState.volteId;
                i3 = mobileIconState.volteId;
                if (i2 != i3) {
                    if (i3 != 0) {
                        this.mVolte.setImageResource(i3);
                        this.mVolte.setVisibility(0);
                    } else {
                        this.mVolte.setVisibility(8);
                    }
                }
                z2 = mobileIconState.roaming;
                mobileIconState2 = this.mState;
                if (z2 == mobileIconState2.roaming || mobileIconState.activityIn != mobileIconState2.activityIn || mobileIconState.activityOut != mobileIconState2.activityOut || mobileIconState.showTriangle != mobileIconState2.showTriangle) {
                    z3 = true;
                }
                boolean z4 = z | z3;
                this.mState = mobileIconState;
                return z4;
            }
        }
        i = 8;
        view.setVisibility(i);
        i2 = this.mState.volteId;
        i3 = mobileIconState.volteId;
        if (i2 != i3) {
        }
        z2 = mobileIconState.roaming;
        mobileIconState2 = this.mState;
        if (z2 == mobileIconState2.roaming) {
        }
        z3 = true;
        boolean z42 = z | z3;
        this.mState = mobileIconState;
        return z42;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        if (!DarkIconDispatcher.isInArea(rect, this)) {
            f = 0.0f;
        }
        this.mMobileDrawable.setTintList(ColorStateList.valueOf(this.mDualToneHandler.getSingleColor(f)));
        ColorStateList valueOf = ColorStateList.valueOf(DarkIconDispatcher.getTint(rect, this, i));
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
        this.mDotView.setIconColor(i, false);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public String getSlot() {
        return this.mSlot;
    }

    public void setSlot(String str) {
        this.mSlot = str;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mMobileDrawable.setTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public boolean isIconVisible() {
        return this.mState.visible && !this.mForceHidden;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setVisibleState(int i, boolean z) {
        if (i == this.mVisibleState) {
            return;
        }
        this.mVisibleState = i;
        if (i == 0) {
            this.mMobileGroup.setVisibility(0);
            this.mDotView.setVisibility(8);
        } else if (i == 1) {
            this.mMobileGroup.setVisibility(4);
            this.mDotView.setVisibility(0);
        } else {
            this.mMobileGroup.setVisibility(4);
            this.mDotView.setVisibility(4);
        }
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public int getVisibleState() {
        return this.mVisibleState;
    }

    @VisibleForTesting
    public StatusBarSignalPolicy.MobileIconState getState() {
        return this.mState;
    }

    private boolean needFixVisibleState() {
        return this.mState.visible && getVisibility() != 0;
    }

    private boolean needFixInVisibleState() {
        return !this.mState.visible && getVisibility() == 0;
    }

    @Override // android.view.View
    public String toString() {
        return "StatusBarMobileView(slot=" + this.mSlot + " state=" + this.mState + ")";
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        initSidePadding();
        super.onMeasure(i, i2);
        checkClipOutRect();
    }

    private void checkClipOutRect() {
        if (this.mVolte.getVisibility() == 0) {
            this.mClipOutRectLeftTop.set(0, 0, ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).getMarginStart() + ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).getMarginEnd() + this.mVolte.getMeasuredWidth(), ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).topMargin + ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).bottomMargin + this.mVolte.getMeasuredHeight());
        } else {
            this.mClipOutRectLeftTop.setEmpty();
        }
        if (this.mMobileRoaming.getVisibility() == 0) {
            int measuredWidth = this.mMobile.getMeasuredWidth();
            int measuredHeight = this.mMobile.getMeasuredHeight();
            this.mClipOutRectRightBottom.set(measuredWidth - ((((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).getMarginStart() + ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).getMarginEnd()) + this.mMobileRoaming.getMeasuredWidth()), measuredHeight - ((((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).topMargin + ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).bottomMargin) + this.mMobileRoaming.getMeasuredHeight()), measuredWidth, measuredHeight);
        } else {
            this.mClipOutRectRightBottom.setEmpty();
        }
        this.mMobileDrawable.setClipOutRect(this.mClipOutRectLeftTop, this.mClipOutRectRightBottom, this.mMobile.getMeasuredWidth());
    }

    private void initSidePadding() {
        int intrinsicWidth = (int) (this.mMobileDrawable.getIntrinsicWidth() / 12.0f);
        if (this.mSidePadding == intrinsicWidth) {
            return;
        }
        this.mSidePadding = intrinsicWidth;
        int i = intrinsicWidth >> 1;
        ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).setMarginStart(intrinsicWidth);
        ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).topMargin = intrinsicWidth;
        ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).setMarginEnd(i);
        ((FrameLayout.LayoutParams) this.mVolte.getLayoutParams()).bottomMargin = i;
        ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).setMarginStart(intrinsicWidth);
        ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).topMargin = i;
        ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).setMarginEnd(intrinsicWidth);
        ((FrameLayout.LayoutParams) this.mMobileRoaming.getLayoutParams()).bottomMargin = intrinsicWidth;
        this.mVolte.resolveLayoutParams();
        this.mMobileRoaming.resolveLayoutParams();
    }
}
