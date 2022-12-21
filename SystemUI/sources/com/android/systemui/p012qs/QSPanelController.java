package com.android.systemui.p012qs;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.p012qs.PagedTileLayout;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QSTileRevealController;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.tuner.TunerService;
import javax.inject.Inject;
import javax.inject.Named;

@QSScope
/* renamed from: com.android.systemui.qs.QSPanelController */
public class QSPanelController extends QSPanelControllerBase<QSPanel> {
    private final BrightnessController mBrightnessController;
    private final BrightnessMirrorHandler mBrightnessMirrorHandler;
    private final BrightnessSliderController mBrightnessSliderController;
    /* access modifiers changed from: private */
    public final FalsingManager mFalsingManager;
    private boolean mGridContentVisible = true;
    private final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() {
        public void onConfigurationChange(Configuration configuration) {
            ((QSPanel) QSPanelController.this.mView).updateResources();
            if (((QSPanel) QSPanelController.this.mView).isListening()) {
                QSPanelController.this.refreshAllTiles();
            }
        }
    };
    private final QSCustomizerController mQsCustomizerController;
    private final QSTileRevealController.Factory mQsTileRevealControllerFactory;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private View.OnTouchListener mTileLayoutTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 1) {
                return false;
            }
            QSPanelController.this.mFalsingManager.isFalseTouch(15);
            return false;
        }
    };
    private final TunerService mTunerService;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    QSPanelController(QSPanel qSPanel, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, @Named("qs_using_media_player") boolean z, @Named("media_qs_panel") MediaHost mediaHost, QSTileRevealController.Factory factory, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory2, BrightnessSliderController.Factory factory3, FalsingManager falsingManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        super(qSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mTunerService = tunerService;
        this.mQsCustomizerController = qSCustomizerController;
        this.mQsTileRevealControllerFactory = factory;
        this.mFalsingManager = falsingManager;
        BrightnessSliderController create = factory3.create(getContext(), (ViewGroup) this.mView);
        this.mBrightnessSliderController = create;
        ((QSPanel) this.mView).setBrightnessView(create.getRootView());
        BrightnessController create2 = factory2.create(create);
        this.mBrightnessController = create2;
        this.mBrightnessMirrorHandler = new BrightnessMirrorHandler(create2);
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void onInit() {
        super.onInit();
        this.mMediaHost.setExpansion(1.0f);
        this.mMediaHost.setShowsOnlyActiveMedia(false);
        this.mMediaHost.init(0);
        this.mQsCustomizerController.init();
        this.mBrightnessSliderController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        updateMediaDisappearParameters();
        this.mTunerService.addTunable((TunerService.Tunable) this.mView, QSPanel.QS_SHOW_BRIGHTNESS);
        ((QSPanel) this.mView).updateResources();
        if (((QSPanel) this.mView).isListening()) {
            refreshAllTiles();
        }
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        switchTileLayout(true);
        this.mBrightnessMirrorHandler.onQsPanelAttached();
        ((PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout()).setOnTouchListener(this.mTileLayoutTouchListener);
    }

    /* access modifiers changed from: protected */
    public QSTileRevealController createTileRevealController() {
        return this.mQsTileRevealControllerFactory.create(this, (PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout());
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mTunerService.removeTunable((TunerService.Tunable) this.mView);
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mBrightnessMirrorHandler.onQsPanelDettached();
        super.onViewDetached();
    }

    public void setVisibility(int i) {
        ((QSPanel) this.mView).setVisibility(i);
    }

    public void setListening(boolean z, boolean z2) {
        setListening(z && z2);
        if (z) {
            this.mBrightnessController.registerCallbacks();
        } else {
            this.mBrightnessController.unregisterCallbacks();
        }
    }

    public void setBrightnessMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mBrightnessMirrorHandler.setController(brightnessMirrorController);
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public void updateResources() {
        ((QSPanel) this.mView).updateResources();
    }

    public void refreshAllTiles() {
        this.mBrightnessController.checkRestrictionAndSetEnabled();
        super.refreshAllTiles();
    }

    public void showEdit(View view) {
        view.post(new QSPanelController$$ExternalSyntheticLambda0(this, view));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showEdit$0$com-android-systemui-qs-QSPanelController  reason: not valid java name */
    public /* synthetic */ void m2914lambda$showEdit$0$comandroidsystemuiqsQSPanelController(View view) {
        if (!this.mQsCustomizerController.isCustomizing()) {
            int[] locationOnScreen = view.getLocationOnScreen();
            this.mQsCustomizerController.show(locationOnScreen[0] + (view.getWidth() / 2), locationOnScreen[1] + (view.getHeight() / 2), false);
        }
    }

    public void setGridContentVisibility(boolean z) {
        int i = z ? 0 : 4;
        setVisibility(i);
        if (this.mGridContentVisible != z) {
            this.mMetricsLogger.visibility(111, i);
        }
        this.mGridContentVisible = z;
    }

    public boolean isLayoutRtl() {
        return ((QSPanel) this.mView).isLayoutRtl();
    }

    public void setPageListener(PagedTileLayout.PageListener pageListener) {
        ((QSPanel) this.mView).setPageListener(pageListener);
    }

    public boolean isShown() {
        return ((QSPanel) this.mView).isShown();
    }

    public void setContentMargins(int i, int i2) {
        ((QSPanel) this.mView).setContentMargins(i, i2, this.mMediaHost.getHostView());
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        ((QSPanel) this.mView).setFooterPageIndicator(pageIndicator);
    }

    public boolean isExpanded() {
        return ((QSPanel) this.mView).isExpanded();
    }

    /* access modifiers changed from: package-private */
    public void setPageMargin(int i) {
        ((QSPanel) this.mView).setPageMargin(i);
    }

    public boolean isBouncerInTransit() {
        return this.mStatusBarKeyguardViewManager.isBouncerInTransit();
    }

    public int getBtTileIndex(QSTileView qSTileView) {
        return ((QSPanel) this.mView).getBtTileIndex(qSTileView);
    }

    public QSTileView getBtTile(int i) {
        return ((QSPanel) this.mView).getBtTile(i);
    }
}
