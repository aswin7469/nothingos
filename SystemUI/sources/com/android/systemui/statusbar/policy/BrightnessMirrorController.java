package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.C1893R;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.ToggleSlider;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import java.util.Objects;
import java.util.function.Consumer;

public class BrightnessMirrorController implements CallbackController<BrightnessMirrorListener> {
    private FrameLayout mBrightnessMirror;
    private int mBrightnessMirrorBackgroundPadding;
    private final ArraySet<BrightnessMirrorListener> mBrightnessMirrorListeners = new ArraySet<>();
    private final NotificationShadeDepthController mDepthController;
    private final int[] mInt2Cache = new int[2];
    private int mLastBrightnessSliderWidth = -1;
    private final NotificationPanelViewController mNotificationPanel;
    private final NotificationShadeWindowView mStatusBarWindow;
    private BrightnessSliderController mToggleSliderController;
    private final BrightnessSliderController.Factory mToggleSliderFactory;
    private final Consumer<Boolean> mVisibilityCallback;

    public interface BrightnessMirrorListener {
        void onBrightnessMirrorReinflated(View view);
    }

    public BrightnessMirrorController(NotificationShadeWindowView notificationShadeWindowView, NotificationPanelViewController notificationPanelViewController, NotificationShadeDepthController notificationShadeDepthController, BrightnessSliderController.Factory factory, Consumer<Boolean> consumer) {
        this.mStatusBarWindow = notificationShadeWindowView;
        this.mToggleSliderFactory = factory;
        this.mBrightnessMirror = (FrameLayout) notificationShadeWindowView.findViewById(C1893R.C1897id.brightness_mirror_container);
        this.mToggleSliderController = setMirrorLayout();
        this.mNotificationPanel = notificationPanelViewController;
        this.mDepthController = notificationShadeDepthController;
        notificationPanelViewController.setPanelAlphaEndAction(new BrightnessMirrorController$$ExternalSyntheticLambda0(this));
        this.mVisibilityCallback = consumer;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-policy-BrightnessMirrorController */
    public /* synthetic */ void mo45626xafd79370() {
        this.mBrightnessMirror.setVisibility(4);
    }

    public void showMirror() {
        this.mBrightnessMirror.setVisibility(0);
        this.mVisibilityCallback.accept(true);
        this.mNotificationPanel.setPanelAlpha(0, true);
        this.mDepthController.setBrightnessMirrorVisible(true);
    }

    public void hideMirror() {
        this.mVisibilityCallback.accept(false);
        this.mNotificationPanel.setPanelAlpha(255, true);
        this.mDepthController.setBrightnessMirrorVisible(false);
    }

    public void setLocationAndSize(View view) {
        view.getLocationInWindow(this.mInt2Cache);
        int[] iArr = this.mInt2Cache;
        int i = iArr[0];
        int i2 = this.mBrightnessMirrorBackgroundPadding;
        int i3 = i - i2;
        int i4 = iArr[1] - i2;
        this.mBrightnessMirror.setTranslationX(0.0f);
        this.mBrightnessMirror.setTranslationY(0.0f);
        this.mBrightnessMirror.getLocationInWindow(this.mInt2Cache);
        int[] iArr2 = this.mInt2Cache;
        int i5 = iArr2[0];
        int i6 = iArr2[1];
        this.mBrightnessMirror.setTranslationX((float) (i3 - i5));
        this.mBrightnessMirror.setTranslationY((float) (i4 - i6));
        int measuredWidth = view.getMeasuredWidth() + (this.mBrightnessMirrorBackgroundPadding * 2);
        if (measuredWidth != this.mLastBrightnessSliderWidth) {
            ViewGroup.LayoutParams layoutParams = this.mBrightnessMirror.getLayoutParams();
            layoutParams.width = measuredWidth;
            this.mBrightnessMirror.setLayoutParams(layoutParams);
        }
    }

    public ToggleSlider getToggleSlider() {
        return this.mToggleSliderController;
    }

    public void updateResources() {
        int dimensionPixelSize = this.mBrightnessMirror.getResources().getDimensionPixelSize(C1893R.dimen.rounded_slider_background_padding);
        this.mBrightnessMirrorBackgroundPadding = dimensionPixelSize;
        this.mBrightnessMirror.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
    }

    public void onOverlayChanged() {
        reinflate();
    }

    public void onDensityOrFontScaleChanged() {
        reinflate();
    }

    private BrightnessSliderController setMirrorLayout() {
        BrightnessSliderController create = this.mToggleSliderFactory.create(this.mBrightnessMirror.getContext(), this.mBrightnessMirror);
        create.init();
        this.mBrightnessMirror.addView(create.getRootView(), -1, -2);
        return create;
    }

    public void reinflate() {
        Context context;
        int indexOfChild = this.mStatusBarWindow.indexOfChild(this.mBrightnessMirror);
        this.mStatusBarWindow.removeView(this.mBrightnessMirror);
        if (((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class)).getQSFragment() == null) {
            context = this.mStatusBarWindow.getContext();
        } else {
            context = ((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class)).getQSFragment().getView().getContext();
        }
        this.mBrightnessMirror = (FrameLayout) LayoutInflater.from(context).inflate(C1893R.layout.brightness_mirror_container, this.mStatusBarWindow, false);
        this.mToggleSliderController = setMirrorLayout();
        this.mStatusBarWindow.addView(this.mBrightnessMirror, indexOfChild);
        for (int i = 0; i < this.mBrightnessMirrorListeners.size(); i++) {
            this.mBrightnessMirrorListeners.valueAt(i).onBrightnessMirrorReinflated(this.mBrightnessMirror);
        }
    }

    public void addCallback(BrightnessMirrorListener brightnessMirrorListener) {
        Objects.requireNonNull(brightnessMirrorListener);
        this.mBrightnessMirrorListeners.add(brightnessMirrorListener);
    }

    public void removeCallback(BrightnessMirrorListener brightnessMirrorListener) {
        this.mBrightnessMirrorListeners.remove(brightnessMirrorListener);
    }

    public void onUiModeChanged() {
        reinflate();
    }
}
