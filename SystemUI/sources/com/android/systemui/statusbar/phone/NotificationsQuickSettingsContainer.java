package com.android.systemui.statusbar.phone;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.C1893R;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

public class NotificationsQuickSettingsContainer extends ConstraintLayout implements FragmentHostManager.FragmentListener, AboveShelfObserver.HasViewAboveShelfChangedListener {
    private Consumer<Configuration> mConfigurationChangedListener;
    private ArrayList<View> mDrawingOrderedChildren = new ArrayList<>();
    private final Comparator<View> mIndexComparator = Comparator.comparingInt(new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda1(this));
    private Consumer<WindowInsets> mInsetsChangedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda2();
    private View mKeyguardStatusBar;
    private ArrayList<View> mLayoutDrawingOrder = new ArrayList<>();
    private View mQSContainer;
    private Consumer<C2301QS> mQSFragmentAttachedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda3();
    private View mQSScrollView;
    private C2301QS mQs;
    private View mQsFrame;
    private View mStackScroller;

    static /* synthetic */ void lambda$new$0(WindowInsets windowInsets) {
    }

    static /* synthetic */ void lambda$new$1(C2301QS qs) {
    }

    static /* synthetic */ void lambda$removeOnInsetsChangedListener$2(WindowInsets windowInsets) {
    }

    static /* synthetic */ void lambda$removeQSFragmentAttachedListener$3(C2301QS qs) {
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public NotificationsQuickSettingsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQsFrame = findViewById(C1893R.C1897id.qs_frame);
        this.mStackScroller = findViewById(C1893R.C1897id.notification_stack_scroller);
        this.mKeyguardStatusBar = findViewById(C1893R.C1897id.keyguard_header);
    }

    public void onFragmentViewCreated(String str, Fragment fragment) {
        C2301QS qs = (C2301QS) fragment;
        this.mQs = qs;
        this.mQSFragmentAttachedListener.accept(qs);
        this.mQSScrollView = this.mQs.getView().findViewById(C1893R.C1897id.expanded_qs_scroll_view);
        this.mQSContainer = this.mQs.getView().findViewById(C1893R.C1897id.quick_settings_container);
    }

    public void onHasViewsAboveShelfChanged(boolean z) {
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Consumer<Configuration> consumer = this.mConfigurationChangedListener;
        if (consumer != null) {
            consumer.accept(configuration);
        }
    }

    public void setConfigurationChangedListener(Consumer<Configuration> consumer) {
        this.mConfigurationChangedListener = consumer;
    }

    public void setNotificationsMarginBottom(int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mStackScroller.getLayoutParams();
        layoutParams.bottomMargin = i;
        this.mStackScroller.setLayoutParams(layoutParams);
    }

    public void setQSContainerPaddingBottom(int i) {
        View view = this.mQSContainer;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.mQSContainer.getPaddingTop(), this.mQSContainer.getPaddingRight(), i);
        }
    }

    public void setInsetsChangedListener(Consumer<WindowInsets> consumer) {
        this.mInsetsChangedListener = consumer;
    }

    public void removeOnInsetsChangedListener() {
        this.mInsetsChangedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0();
    }

    public void setQSFragmentAttachedListener(Consumer<C2301QS> consumer) {
        this.mQSFragmentAttachedListener = consumer;
        C2301QS qs = this.mQs;
        if (qs != null) {
            consumer.accept(qs);
        }
    }

    public void removeQSFragmentAttachedListener() {
        this.mQSFragmentAttachedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda4();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentHostManager.get(this).addTagListener(C2301QS.TAG, this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FragmentHostManager.get(this).removeTagListener(C2301QS.TAG, this);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsetsChangedListener.accept(windowInsets);
        return windowInsets;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        this.mDrawingOrderedChildren.clear();
        this.mLayoutDrawingOrder.clear();
        if (this.mKeyguardStatusBar.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mKeyguardStatusBar);
            this.mLayoutDrawingOrder.add(this.mKeyguardStatusBar);
        }
        if (this.mQsFrame.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mQsFrame);
            this.mLayoutDrawingOrder.add(this.mQsFrame);
        }
        if (this.mStackScroller.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mStackScroller);
            this.mLayoutDrawingOrder.add(this.mStackScroller);
        }
        this.mLayoutDrawingOrder.sort(this.mIndexComparator);
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        int indexOf = this.mLayoutDrawingOrder.indexOf(view);
        if (indexOf >= 0) {
            return super.drawChild(canvas, this.mDrawingOrderedChildren.get(indexOf), j);
        }
        return super.drawChild(canvas, view, j);
    }

    public void applyConstraints(ConstraintSet constraintSet) {
        constraintSet.applyTo(this);
    }
}
