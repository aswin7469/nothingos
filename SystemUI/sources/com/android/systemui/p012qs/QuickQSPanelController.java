package com.android.systemui.p012qs;

import android.content.res.Configuration;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.util.leak.RotationUtils;
import com.nothing.systemui.p024qs.QSTileHostEx;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

@QSScope
/* renamed from: com.android.systemui.qs.QuickQSPanelController */
public class QuickQSPanelController extends QSPanelControllerBase<QuickQSPanel> {
    private final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QuickQSPanelController$$ExternalSyntheticLambda0(this);
    private final boolean mUsingCollapsedLandscapeMedia;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-QuickQSPanelController  reason: not valid java name */
    public /* synthetic */ void m2948lambda$new$0$comandroidsystemuiqsQuickQSPanelController(Configuration configuration) {
        int integer = getResources().getInteger(C1894R.integer.quick_qs_panel_max_tiles);
        if (integer != ((QuickQSPanel) this.mView).getNumQuickTiles()) {
            setMaxTiles(integer);
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    QuickQSPanelController(QuickQSPanel quickQSPanel, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, @Named("qs_using_media_player") boolean z, @Named("media_quick_qs_panel") MediaHost mediaHost, @Named("qs_using_collapsed_landscape_media") boolean z2, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(quickQSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mUsingCollapsedLandscapeMedia = z2;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        super.onInit();
        updateMediaExpansion();
        this.mMediaHost.setShowsOnlyActiveMedia(true);
        this.mMediaHost.init(1);
    }

    private void updateMediaExpansion() {
        int rotation = getRotation();
        boolean z = true;
        if (!(rotation == 1 || rotation == 3)) {
            z = false;
        }
        if (!this.mUsingCollapsedLandscapeMedia || !z) {
            this.mMediaHost.setExpansion(1.0f);
        } else {
            this.mMediaHost.setExpansion(0.0f);
        }
    }

    /* access modifiers changed from: protected */
    public int getRotation() {
        return RotationUtils.getRotation(getContext());
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        ((QuickQSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        ((QuickQSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
    }

    private void setMaxTiles(int i) {
        ((QuickQSPanel) this.mView).setMaxTiles(i);
        setTiles();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        updateMediaExpansion();
    }

    public void setTiles() {
        ArrayList arrayList = new ArrayList();
        for (QSTile next : this.mHost.getTiles()) {
            if (QSTileHostEx.shouldAddTileToQQS(next.getTileSpec())) {
                arrayList.add(next);
            }
            if (arrayList.size() == ((QuickQSPanel) this.mView).getNumQuickTiles()) {
                break;
            }
        }
        super.setTiles(arrayList, true);
    }

    public void setContentMargins(int i, int i2) {
        ((QuickQSPanel) this.mView).setContentMargins(i, i2, this.mMediaHost.getHostView());
    }

    public int getNumQuickTiles() {
        return ((QuickQSPanel) this.mView).getNumQuickTiles();
    }
}
