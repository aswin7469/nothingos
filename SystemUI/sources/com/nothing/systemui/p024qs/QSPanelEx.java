package com.nothing.systemui.p024qs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.p012qs.PageIndicator;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QSPanelControllerBase;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b*\u0001\u0014\b\u0016\u0018\u0000 92\u00020\u0001:\u00029:B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0018\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0012H\u0016J \u0010#\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020!2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010$\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)H\u0016J\u0010\u0010*\u001a\u00020)2\u0006\u0010+\u001a\u00020'H\u0016J\u0010\u0010,\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020!H\u0016J\u0018\u0010-\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010.\u001a\u00020\u001b2\u0006\u0010/\u001a\u000200H\u0016J\u0018\u00101\u001a\u00020\u001b2\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000203H\u0016J\u0010\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u0019H\u0016J\u0010\u00107\u001a\u00020\u001b2\u0006\u00108\u001a\u000200H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00060\u000eR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0004\n\u0002\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X.¢\u0006\u0002\n\u0000¨\u0006;"}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSPanelEx;", "", "()V", "btIndicator", "Lcom/android/systemui/qs/PageIndicator;", "btTilePage", "Lcom/nothing/systemui/qs/CirclePagedTileLayout;", "cacheBtDevices", "Landroid/util/ArrayMap;", "", "Lcom/android/systemui/qs/QSPanelControllerBase$TileRecord;", "circleTileContainer", "Landroid/view/View;", "handler", "Lcom/nothing/systemui/qs/QSPanelEx$H;", "hostEx", "Lcom/nothing/systemui/qs/QSTileHostEx;", "qsPanel", "Lcom/android/systemui/qs/QSPanel;", "runnable", "com/nothing/systemui/qs/QSPanelEx$runnable$1", "Lcom/nothing/systemui/qs/QSPanelEx$runnable$1;", "signalIndicator", "signalTilePage", "tileHost", "Lcom/android/systemui/qs/QSTileHost;", "addTile", "", "tileRecord", "tileLayout", "Lcom/android/systemui/qs/QSPanel$QSTileLayout;", "createCirclePagedTileLayouts", "context", "Landroid/content/Context;", "qs", "drawTileForBtPage", "state", "Lcom/android/systemui/plugins/qs/QSTile$State;", "getBtTile", "Lcom/android/systemui/plugins/qs/QSTileView;", "index", "", "getBtTileIndex", "tileView", "removeAndRecreateCirclePagedTileLayouts", "removeTile", "setCurrentItemIfNeeded", "expanded", "", "setExpansion", "expansion", "", "proposedTranslation", "setHost", "host", "setListening", "isListening", "Companion", "H", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.QSPanelEx */
/* compiled from: QSPanelEx.kt */
public class QSPanelEx {
    private static final int ADD_TILE = 0;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int REMOVE_TILE = 1;
    private static final String TAG = "QSPanelEx";
    private PageIndicator btIndicator;
    /* access modifiers changed from: private */
    public CirclePagedTileLayout btTilePage;
    private final ArrayMap<CharSequence, QSPanelControllerBase.TileRecord> cacheBtDevices = new ArrayMap<>();
    private View circleTileContainer;
    private final C4203H handler = new C4203H();
    private final QSTileHostEx hostEx;
    private QSPanel qsPanel;
    private QSPanelEx$runnable$1 runnable = new QSPanelEx$runnable$1(this);
    private PageIndicator signalIndicator;
    private CirclePagedTileLayout signalTilePage;
    private QSTileHost tileHost;

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSPanelEx$Companion;", "", "()V", "ADD_TILE", "", "REMOVE_TILE", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.nothing.systemui.qs.QSPanelEx$Companion */
    /* compiled from: QSPanelEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public QSPanelEx() {
        Object obj = NTDependencyEx.get(QSTileHostEx.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(QSTileHostEx::class.java)");
        this.hostEx = (QSTileHostEx) obj;
    }

    public void addTile(QSPanelControllerBase.TileRecord tileRecord, QSPanel.QSTileLayout qSTileLayout) {
        Intrinsics.checkNotNullParameter(tileRecord, "tileRecord");
        Intrinsics.checkNotNullParameter(qSTileLayout, "tileLayout");
        CirclePagedTileLayout circlePagedTileLayout = null;
        if (tileRecord.isCircleSignalTile) {
            CirclePagedTileLayout circlePagedTileLayout2 = this.signalTilePage;
            if (circlePagedTileLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            } else {
                circlePagedTileLayout = circlePagedTileLayout2;
            }
            circlePagedTileLayout.addTile(tileRecord);
        } else if (tileRecord.isCircleBluetoothTile) {
            CirclePagedTileLayout circlePagedTileLayout3 = this.btTilePage;
            if (circlePagedTileLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            } else {
                circlePagedTileLayout = circlePagedTileLayout3;
            }
            circlePagedTileLayout.addTile(tileRecord);
        } else {
            qSTileLayout.addTile(tileRecord);
        }
    }

    public void removeTile(QSPanelControllerBase.TileRecord tileRecord, QSPanel.QSTileLayout qSTileLayout) {
        Intrinsics.checkNotNullParameter(tileRecord, "tileRecord");
        Intrinsics.checkNotNullParameter(qSTileLayout, "tileLayout");
        CirclePagedTileLayout circlePagedTileLayout = null;
        if (tileRecord.isCircleSignalTile) {
            CirclePagedTileLayout circlePagedTileLayout2 = this.signalTilePage;
            if (circlePagedTileLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            } else {
                circlePagedTileLayout = circlePagedTileLayout2;
            }
            circlePagedTileLayout.removeTile(tileRecord);
        } else if (tileRecord.isCircleBluetoothTile) {
            CirclePagedTileLayout circlePagedTileLayout3 = this.btTilePage;
            if (circlePagedTileLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            } else {
                circlePagedTileLayout = circlePagedTileLayout3;
            }
            circlePagedTileLayout.removeTile(tileRecord);
        } else {
            qSTileLayout.removeTile(tileRecord);
        }
    }

    public void drawTileForBtPage(Context context, QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(tileRecord, "tileRecord");
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        if (tileRecord.isCircleBluetoothTile) {
            if (this.btTilePage == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            }
            CirclePagedTileLayout circlePagedTileLayout = null;
            if (state.addressList != null && !state.addressList.isEmpty()) {
                for (Map.Entry entry : this.cacheBtDevices.entrySet()) {
                    CharSequence charSequence = (CharSequence) entry.getKey();
                    QSPanelControllerBase.TileRecord tileRecord2 = (QSPanelControllerBase.TileRecord) entry.getValue();
                    if (!state.addressList.contains(charSequence)) {
                        NTLogUtil.m1686d(TAG, "onStateChanged: remove pre device " + charSequence);
                        this.handler.obtainMessage(1, tileRecord2).sendToTarget();
                        this.cacheBtDevices.remove(charSequence);
                    }
                }
                int size = state.addressList.size();
                for (int i = 0; i < size; i++) {
                    CharSequence charSequence2 = state.addressList.get(i);
                    Intrinsics.checkNotNullExpressionValue(charSequence2, "state.addressList.get(i)");
                    CharSequence charSequence3 = charSequence2;
                    QSPanelControllerBase.TileRecord tileRecord3 = this.cacheBtDevices.get(charSequence3);
                    if (tileRecord3 == null) {
                        QSTile qSTile = tileRecord.tile;
                        QSTileHost qSTileHost = this.tileHost;
                        if (qSTileHost == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("tileHost");
                            qSTileHost = null;
                        }
                        tileRecord3 = new QSPanelControllerBase.TileRecord(qSTile, qSTileHost.createTileView(context, tileRecord.tile, false));
                        tileRecord3.tileView.init(tileRecord.tile);
                        this.cacheBtDevices.put(charSequence3, tileRecord3);
                        NTLogUtil.m1686d(TAG, "onStateChanged: create bt device " + charSequence3);
                    }
                    QSTile.State copy = state.copy();
                    Intrinsics.checkNotNullExpressionValue(copy, "state.copy()");
                    copy.icon = state.iconList.get(i);
                    copy.label = state.labelList.get(i);
                    copy.secondaryLabel = state.secondaryLabelList.get(i);
                    copy.state = state.stateList[i];
                    this.handler.obtainMessage(0, tileRecord3).sendToTarget();
                    QSPanel qSPanel = this.qsPanel;
                    if (qSPanel == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("qsPanel");
                        qSPanel = null;
                    }
                    qSPanel.drawTile(tileRecord3, copy);
                }
            } else if (!this.cacheBtDevices.isEmpty()) {
                for (Map.Entry entry2 : this.cacheBtDevices.entrySet()) {
                    this.handler.obtainMessage(1, (QSPanelControllerBase.TileRecord) entry2.getValue()).sendToTarget();
                    NTLogUtil.m1686d(TAG, "onStateChanged: remove device " + ((CharSequence) entry2.getKey()));
                }
                this.cacheBtDevices.clear();
                CirclePagedTileLayout circlePagedTileLayout2 = this.btTilePage;
                if (circlePagedTileLayout2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
                } else {
                    circlePagedTileLayout = circlePagedTileLayout2;
                }
                circlePagedTileLayout.post(this.runnable);
            }
        }
    }

    public void createCirclePagedTileLayouts(Context context, QSPanel qSPanel) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSPanel, "qs");
        this.qsPanel = qSPanel;
        NTLogUtil.m1686d(TAG, "createCirclePagedTileLayouts");
        LayoutInflater from = LayoutInflater.from(context);
        Intrinsics.checkNotNullExpressionValue(from, "from(context)");
        QSPanel qSPanel2 = this.qsPanel;
        CirclePagedTileLayout circlePagedTileLayout = null;
        if (qSPanel2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qsPanel");
            qSPanel2 = null;
        }
        View inflate = from.inflate(C1894R.layout.circle_qs_tile_layout, qSPanel2, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "inflater?.inflate(R.layo…e_layout, qsPanel, false)");
        this.circleTileContainer = inflate;
        if (inflate == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            inflate = null;
        }
        CirclePagedTileLayout circlePagedTileLayout2 = (CirclePagedTileLayout) inflate.findViewById(C1894R.C1898id.signal_tile_page);
        Intrinsics.checkNotNullExpressionValue(circlePagedTileLayout2, "circleTileContainer?.fin…Id(R.id.signal_tile_page)");
        this.signalTilePage = circlePagedTileLayout2;
        if (circlePagedTileLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            circlePagedTileLayout2 = null;
        }
        circlePagedTileLayout2.markSignal();
        View view = this.circleTileContainer;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            view = null;
        }
        CirclePagedTileLayout circlePagedTileLayout3 = (CirclePagedTileLayout) view.findViewById(C1894R.C1898id.bt_tile_page);
        Intrinsics.checkNotNullExpressionValue(circlePagedTileLayout3, "circleTileContainer?.fin…ewById(R.id.bt_tile_page)");
        this.btTilePage = circlePagedTileLayout3;
        View view2 = this.circleTileContainer;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            view2 = null;
        }
        PageIndicator pageIndicator = (PageIndicator) view2.findViewById(C1894R.C1898id.signal_tile_indicator);
        Intrinsics.checkNotNullExpressionValue(pageIndicator, "circleTileContainer?.fin…id.signal_tile_indicator)");
        this.signalIndicator = pageIndicator;
        View view3 = this.circleTileContainer;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            view3 = null;
        }
        PageIndicator pageIndicator2 = (PageIndicator) view3.findViewById(C1894R.C1898id.bt_tile_indicator);
        Intrinsics.checkNotNullExpressionValue(pageIndicator2, "circleTileContainer?.fin…d(R.id.bt_tile_indicator)");
        this.btIndicator = pageIndicator2;
        CirclePagedTileLayout circlePagedTileLayout4 = this.signalTilePage;
        if (circlePagedTileLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            circlePagedTileLayout4 = null;
        }
        PageIndicator pageIndicator3 = this.signalIndicator;
        if (pageIndicator3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalIndicator");
            pageIndicator3 = null;
        }
        circlePagedTileLayout4.setPageIndicator(pageIndicator3);
        CirclePagedTileLayout circlePagedTileLayout5 = this.btTilePage;
        if (circlePagedTileLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            circlePagedTileLayout5 = null;
        }
        PageIndicator pageIndicator4 = this.btIndicator;
        if (pageIndicator4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btIndicator");
            pageIndicator4 = null;
        }
        circlePagedTileLayout5.setPageIndicator(pageIndicator4);
        QSPanel qSPanel3 = this.qsPanel;
        if (qSPanel3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qsPanel");
            qSPanel3 = null;
        }
        View view4 = this.circleTileContainer;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            view4 = null;
        }
        qSPanel3.addView(view4, 0);
        QSTileHostEx qSTileHostEx = this.hostEx;
        CirclePagedTileLayout circlePagedTileLayout6 = this.signalTilePage;
        if (circlePagedTileLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
        } else {
            circlePagedTileLayout = circlePagedTileLayout6;
        }
        qSTileHostEx.setSignalTilePage(circlePagedTileLayout);
        if (this.circleTileContainer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
        }
        NTLogUtil.m1686d(TAG, "checkNull: false");
    }

    public void setCurrentItemIfNeeded(boolean z) {
        if (!z) {
            CirclePagedTileLayout circlePagedTileLayout = this.btTilePage;
            if (circlePagedTileLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
                circlePagedTileLayout = null;
            }
            circlePagedTileLayout.setCurrentItem(0, false);
        }
    }

    public void setExpansion(float f, float f2) {
        CirclePagedTileLayout circlePagedTileLayout = this.signalTilePage;
        CirclePagedTileLayout circlePagedTileLayout2 = null;
        if (circlePagedTileLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            circlePagedTileLayout = null;
        }
        circlePagedTileLayout.setExpansion(f, f2);
        CirclePagedTileLayout circlePagedTileLayout3 = this.btTilePage;
        if (circlePagedTileLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
        } else {
            circlePagedTileLayout2 = circlePagedTileLayout3;
        }
        circlePagedTileLayout2.setExpansion(f, f2);
    }

    public void setListening(boolean z) {
        CirclePagedTileLayout circlePagedTileLayout = this.signalTilePage;
        if (circlePagedTileLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            circlePagedTileLayout = null;
        }
        circlePagedTileLayout.setListening(z, (UiEventLogger) null);
        CirclePagedTileLayout circlePagedTileLayout2 = this.btTilePage;
        if (circlePagedTileLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            circlePagedTileLayout2 = null;
        }
        circlePagedTileLayout2.setListening(z, (UiEventLogger) null);
    }

    public void setHost(QSTileHost qSTileHost) {
        Intrinsics.checkNotNullParameter(qSTileHost, "host");
        this.tileHost = qSTileHost;
    }

    public QSTileView getBtTile(int i) {
        CirclePagedTileLayout circlePagedTileLayout = this.btTilePage;
        if (circlePagedTileLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            circlePagedTileLayout = null;
        }
        QSTileView btTile = circlePagedTileLayout.getBtTile(i);
        Intrinsics.checkNotNullExpressionValue(btTile, "btTilePage?.getBtTile(index)");
        return btTile;
    }

    public int getBtTileIndex(QSTileView qSTileView) {
        Intrinsics.checkNotNullParameter(qSTileView, "tileView");
        CirclePagedTileLayout circlePagedTileLayout = this.btTilePage;
        if (circlePagedTileLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            circlePagedTileLayout = null;
        }
        return circlePagedTileLayout.getBtTileIndex(qSTileView);
    }

    public void removeAndRecreateCirclePagedTileLayouts(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (this.circleTileContainer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
        }
        QSPanel qSPanel = this.qsPanel;
        QSPanel qSPanel2 = null;
        if (qSPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qsPanel");
            qSPanel = null;
        }
        View view = this.circleTileContainer;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("circleTileContainer");
            view = null;
        }
        qSPanel.removeView(view);
        CirclePagedTileLayout circlePagedTileLayout = this.signalTilePage;
        if (circlePagedTileLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signalTilePage");
            circlePagedTileLayout = null;
        }
        circlePagedTileLayout.removeAllViews();
        CirclePagedTileLayout circlePagedTileLayout2 = this.btTilePage;
        if (circlePagedTileLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            circlePagedTileLayout2 = null;
        }
        circlePagedTileLayout2.removeAllViews();
        NTLogUtil.m1686d(TAG, "removeAndRecreateCirclePagedTileLayouts");
        QSPanel qSPanel3 = this.qsPanel;
        if (qSPanel3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qsPanel");
        } else {
            qSPanel2 = qSPanel3;
        }
        createCirclePagedTileLayouts(context, qSPanel2);
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSPanelEx$H;", "Landroid/os/Handler;", "(Lcom/nothing/systemui/qs/QSPanelEx;)V", "handleMessage", "", "msg", "Landroid/os/Message;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.nothing.systemui.qs.QSPanelEx$H */
    /* compiled from: QSPanelEx.kt */
    private final class C4203H extends Handler {
        public C4203H() {
        }

        public void handleMessage(Message message) {
            Intrinsics.checkNotNullParameter(message, NotificationCompat.CATEGORY_MESSAGE);
            int i = message.what;
            CirclePagedTileLayout circlePagedTileLayout = null;
            if (i == 0) {
                CirclePagedTileLayout access$getBtTilePage$p = QSPanelEx.this.btTilePage;
                if (access$getBtTilePage$p == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
                } else {
                    circlePagedTileLayout = access$getBtTilePage$p;
                }
                Object obj = message.obj;
                if (obj != null) {
                    circlePagedTileLayout.addTile((QSPanelControllerBase.TileRecord) obj);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.qs.QSPanelControllerBase.TileRecord");
            } else if (i == 1 && message.obj != null) {
                Object obj2 = message.obj;
                if (obj2 != null) {
                    QSPanelControllerBase.TileRecord tileRecord = (QSPanelControllerBase.TileRecord) obj2;
                    CirclePagedTileLayout access$getBtTilePage$p2 = QSPanelEx.this.btTilePage;
                    if (access$getBtTilePage$p2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
                        access$getBtTilePage$p2 = null;
                    }
                    access$getBtTilePage$p2.removeTile(tileRecord);
                    tileRecord.tileView = null;
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.qs.QSPanelControllerBase.TileRecord");
            }
        }
    }
}
