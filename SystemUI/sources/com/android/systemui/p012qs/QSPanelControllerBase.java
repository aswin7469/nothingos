package com.android.systemui.p012qs;

import android.content.ComponentName;
import android.content.res.Configuration;
import android.metrics.LogMaker;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.animation.DisappearParameters;
import com.nothing.systemui.p024qs.QSTileHostEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Named;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* renamed from: com.android.systemui.qs.QSPanelControllerBase */
public abstract class QSPanelControllerBase<T extends QSPanel> extends ViewController<T> implements Dumpable {
    private String mCachedSpecs = "";
    private final DumpManager mDumpManager;
    protected final QSTileHost mHost;
    /* access modifiers changed from: private */
    public int mLastOrientation;
    public final MediaHost mMediaHost;
    private final Function1<Boolean, Unit> mMediaHostVisibilityListener = new QSPanelControllerBase$$ExternalSyntheticLambda1(this);
    private Consumer<Boolean> mMediaVisibilityChangedListener;
    protected final MetricsLogger mMetricsLogger;
    protected final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() {
        public void onConfigurationChange(Configuration configuration) {
            QSPanelControllerBase qSPanelControllerBase = QSPanelControllerBase.this;
            qSPanelControllerBase.mShouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(qSPanelControllerBase.getResources());
            QSPanelControllerBase.this.onConfigurationChanged();
            if (configuration.orientation != QSPanelControllerBase.this.mLastOrientation) {
                int unused = QSPanelControllerBase.this.mLastOrientation = configuration.orientation;
                QSPanelControllerBase.this.switchTileLayout(false);
            }
        }
    };
    private final QSHost.Callback mQSHostCallback = new QSPanelControllerBase$$ExternalSyntheticLambda0(this);
    private final QSLogger mQSLogger;
    private final QSCustomizerController mQsCustomizerController;
    private QSTileRevealController mQsTileRevealController;
    protected final ArrayList<TileRecord> mRecords = new ArrayList<>();
    private float mRevealExpansion;
    protected boolean mShouldUseSplitNotificationShade;
    private final UiEventLogger mUiEventLogger;
    private boolean mUsingHorizontalLayout;
    private Runnable mUsingHorizontalLayoutChangedListener;
    private final boolean mUsingMediaPlayer;

    /* access modifiers changed from: protected */
    public QSTileRevealController createTileRevealController() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-QSPanelControllerBase  reason: not valid java name */
    public /* synthetic */ Unit m2915lambda$new$0$comandroidsystemuiqsQSPanelControllerBase(Boolean bool) {
        Consumer<Boolean> consumer = this.mMediaVisibilityChangedListener;
        if (consumer != null) {
            consumer.accept(bool);
        }
        switchTileLayout(false);
        return null;
    }

    protected QSPanelControllerBase(T t, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, @Named("qs_using_media_player") boolean z, MediaHost mediaHost, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(t);
        this.mHost = qSTileHost;
        this.mQsCustomizerController = qSCustomizerController;
        this.mUsingMediaPlayer = z;
        this.mMediaHost = mediaHost;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mQSLogger = qSLogger;
        this.mDumpManager = dumpManager;
        this.mShouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        ((QSPanel) this.mView).initialize();
        ((QSPanel) this.mView).setHost(this.mHost);
        this.mQSLogger.logAllTilesChangeListening(((QSPanel) this.mView).isListening(), ((QSPanel) this.mView).getDumpableTag(), "");
    }

    public MediaHost getMediaHost() {
        return this.mMediaHost;
    }

    public void setSquishinessFraction(float f) {
        ((QSPanel) this.mView).setSquishinessFraction(f);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        QSTileRevealController createTileRevealController = createTileRevealController();
        this.mQsTileRevealController = createTileRevealController;
        if (createTileRevealController != null) {
            createTileRevealController.setExpansion(this.mRevealExpansion);
        }
        this.mMediaHost.addVisibilityChangeListener(this.mMediaHostVisibilityListener);
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.addCallback(this.mQSHostCallback);
        setTiles();
        this.mLastOrientation = getResources().getConfiguration().orientation;
        switchTileLayout(true);
        this.mDumpManager.registerDumpable(((QSPanel) this.mView).getDumpableTag(), this);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.removeCallback(this.mQSHostCallback);
        ((QSPanel) this.mView).getTileLayout().setListening(false, this.mUiEventLogger);
        this.mMediaHost.removeVisibilityChangeListener(this.mMediaHostVisibilityListener);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.removeCallbacks();
        }
        this.mRecords.clear();
        this.mDumpManager.unregisterDumpable(((QSPanel) this.mView).getDumpableTag());
    }

    public void setTiles() {
        setTiles(this.mHost.getTiles(), false);
    }

    public void setTiles(Collection<QSTile> collection, boolean z) {
        QSTileRevealController qSTileRevealController;
        if (!z && (qSTileRevealController = this.mQsTileRevealController) != null) {
            qSTileRevealController.updateRevealedTiles(collection);
        }
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            ((QSPanel) this.mView).removeTile(next);
            next.tile.removeCallback(next.callback);
        }
        this.mRecords.clear();
        this.mCachedSpecs = "";
        ((QSPanel) this.mView).removeAndRecreateCirclePagedTileLayouts();
        for (QSTile addTile : collection) {
            addTile(addTile, z);
        }
    }

    public void refreshAllTiles() {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (!next.tile.isListening()) {
                next.tile.refreshState();
            }
        }
    }

    private void addTile(QSTile qSTile, boolean z) {
        TileRecord tileRecord = new TileRecord(qSTile, this.mHost.createTileView(getContext(), qSTile, z));
        tileRecord.isSignalTile = QSTileHostEx.isSignalTile(qSTile.getTileSpec());
        boolean z2 = true;
        tileRecord.isCollapsedSignalTile = z && QSTileHostEx.isFirstTile(qSTile.getTileSpec());
        tileRecord.isCircleSignalTile = !z && tileRecord.isSignalTile;
        tileRecord.isBtTile = QSTileHostEx.isBluetoothTile(qSTile.getTileSpec());
        tileRecord.isCollapsedBtTile = z && tileRecord.isBtTile;
        if (z || !tileRecord.isBtTile) {
            z2 = false;
        }
        tileRecord.isCircleBluetoothTile = z2;
        ((QSPanel) this.mView).addTile(tileRecord);
        this.mRecords.add(tileRecord);
        this.mCachedSpecs = getTilesSpecs();
    }

    public void clickTile(ComponentName componentName) {
        String spec = CustomTile.toSpec(componentName);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile.getTileSpec().equals(spec)) {
                next.tile.click((View) null);
                return;
            }
        }
    }

    public QSTile getTile(String str) {
        for (int i = 0; i < this.mRecords.size(); i++) {
            if (str.equals(this.mRecords.get(i).tile.getTileSpec())) {
                return this.mRecords.get(i).tile;
            }
        }
        return this.mHost.createTile(str);
    }

    public boolean areThereTiles() {
        return !this.mRecords.isEmpty();
    }

    public QSTileView getTileView(QSTile qSTile) {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile == qSTile) {
                return next.tileView;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public QSTileView getTileView(String str) {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (Objects.equals(next.tile.getTileSpec(), str)) {
                return next.tileView;
            }
        }
        return null;
    }

    private String getTilesSpecs() {
        return (String) this.mRecords.stream().map(new QSPanelControllerBase$$ExternalSyntheticLambda2()).collect(Collectors.joining(NavigationBarInflaterView.BUTTON_SEPARATOR));
    }

    public void setExpanded(boolean z) {
        if (((QSPanel) this.mView).isExpanded() != z) {
            this.mQSLogger.logPanelExpanded(z, ((QSPanel) this.mView).getDumpableTag());
            ((QSPanel) this.mView).setExpanded(z);
            this.mMetricsLogger.visibility(111, z);
            if (!z) {
                this.mUiEventLogger.log(((QSPanel) this.mView).closePanelEvent());
                closeDetail();
                return;
            }
            this.mUiEventLogger.log(((QSPanel) this.mView).openPanelEvent());
            logTiles();
        }
    }

    public void closeDetail() {
        if (this.mQsCustomizerController.isShown()) {
            this.mQsCustomizerController.hide();
        }
    }

    /* access modifiers changed from: package-private */
    public void setListening(boolean z) {
        if (((QSPanel) this.mView).isListening() != z) {
            ((QSPanel) this.mView).setListening(z);
            if (((QSPanel) this.mView).getTileLayout() != null) {
                this.mQSLogger.logAllTilesChangeListening(z, ((QSPanel) this.mView).getDumpableTag(), this.mCachedSpecs);
                ((QSPanel) this.mView).getTileLayout().setListening(z, this.mUiEventLogger);
            }
            if (((QSPanel) this.mView).isListening()) {
                refreshAllTiles();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean switchTileLayout(boolean z) {
        boolean shouldUseHorizontalLayout = shouldUseHorizontalLayout();
        if (shouldUseHorizontalLayout == this.mUsingHorizontalLayout && !z) {
            return false;
        }
        this.mUsingHorizontalLayout = shouldUseHorizontalLayout;
        ((QSPanel) this.mView).setUsingHorizontalLayout(this.mUsingHorizontalLayout, this.mMediaHost.getHostView(), z);
        updateMediaDisappearParameters();
        Runnable runnable = this.mUsingHorizontalLayoutChangedListener;
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateMediaDisappearParameters() {
        if (this.mUsingMediaPlayer) {
            DisappearParameters disappearParameters = this.mMediaHost.getDisappearParameters();
            if (this.mUsingHorizontalLayout) {
                disappearParameters.getDisappearSize().set(0.0f, 0.4f);
                disappearParameters.getGonePivot().set(1.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.25f, 1.0f);
                disappearParameters.setDisappearEnd(0.6f);
            } else {
                disappearParameters.getDisappearSize().set(1.0f, 0.0f);
                disappearParameters.getGonePivot().set(0.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.0f, 1.05f);
                disappearParameters.setDisappearEnd(0.95f);
            }
            disappearParameters.setFadeStartPosition(0.95f);
            disappearParameters.setDisappearStart(0.0f);
            this.mMediaHost.setDisappearParameters(disappearParameters);
        }
    }

    public boolean shouldUseHorizontalLayout() {
        if (!this.mShouldUseSplitNotificationShade && this.mUsingMediaPlayer && this.mMediaHost.getVisible() && this.mLastOrientation == 2) {
            return true;
        }
        return false;
    }

    private void logTiles() {
        for (int i = 0; i < this.mRecords.size(); i++) {
            QSTile qSTile = this.mRecords.get(i).tile;
            this.mMetricsLogger.write(qSTile.populate(new LogMaker(qSTile.getMetricsCategory()).setType(1)));
        }
    }

    public void setRevealExpansion(float f) {
        this.mRevealExpansion = f;
        QSTileRevealController qSTileRevealController = this.mQsTileRevealController;
        if (qSTileRevealController != null) {
            qSTileRevealController.setExpansion(f);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.println("  Tile records:");
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile instanceof Dumpable) {
                printWriter.print("    ");
                ((Dumpable) next.tile).dump(printWriter, strArr);
                printWriter.print("    ");
                printWriter.println(next.tileView.toString());
            }
        }
        if (this.mMediaHost != null) {
            printWriter.println("  media bounds: " + this.mMediaHost.getCurrentBounds());
        }
    }

    public QSPanel.QSTileLayout getTileLayout() {
        return ((QSPanel) this.mView).getTileLayout();
    }

    public void setMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mMediaVisibilityChangedListener = consumer;
    }

    public void setUsingHorizontalLayoutChangeListener(Runnable runnable) {
        this.mUsingHorizontalLayoutChangedListener = runnable;
    }

    public View getBrightnessView() {
        return ((QSPanel) this.mView).getBrightnessView();
    }

    public void setCollapseExpandAction(Runnable runnable) {
        ((QSPanel) this.mView).setCollapseExpandAction(runnable);
    }

    public void setIsOnKeyguard(boolean z) {
        ((QSPanel) this.mView).setShouldMoveMediaOnExpansion(!(this.mShouldUseSplitNotificationShade && z));
    }

    public void setExpansion(float f, float f2) {
        ((QSPanel) this.mView).setExpansion(f, f2);
    }

    /* renamed from: com.android.systemui.qs.QSPanelControllerBase$TileRecord */
    public static final class TileRecord {
        public QSTile.Callback callback;
        public boolean isBtTile;
        public boolean isCircleBluetoothTile;
        public boolean isCircleSignalTile;
        public boolean isCollapsedBtTile;
        public boolean isCollapsedSignalTile;
        public boolean isSignalTile;
        public boolean scanState;
        public QSTile tile;
        public QSTileView tileView;

        public TileRecord(QSTile qSTile, QSTileView qSTileView) {
            this.tile = qSTile;
            this.tileView = qSTileView;
        }

        public boolean isCircleTile() {
            return this.isCircleSignalTile || this.isCircleBluetoothTile;
        }
    }
}
