package com.nothing.systemui.p024qs;

import android.content.Context;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.customize.TileQueryHelper;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.tuner.TunerService;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 .2\u00020\u0001:\u0001.B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0012J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0019\u001a\u00020\u001aJ\u0016\u0010\u001b\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\u001c\u001a\u00020\u00042\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00170\u001eH\u0002J\u0006\u0010\u001f\u001a\u00020\u0004J\u0006\u0010 \u001a\u00020\u0004J\u0006\u0010!\u001a\u00020\u0004J.\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u00042\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020\u00122\u0006\u0010+\u001a\u00020,J\u0018\u0010-\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u0002\n\u0000R*\u0010\f\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006/"}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSTileHostEx;", "", "()V", "isDataConnected", "", "isWifiConnected", "notifPanelViewController", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "shadeController", "Lcom/android/systemui/statusbar/phone/ShadeController;", "value", "Lcom/nothing/systemui/qs/CirclePagedTileLayout;", "signalTilePage", "getSignalTilePage", "()Lcom/nothing/systemui/qs/CirclePagedTileLayout;", "setSignalTilePage", "(Lcom/nothing/systemui/qs/CirclePagedTileLayout;)V", "addPostFullyCollapseAction", "", "runnable", "Ljava/lang/Runnable;", "collapsePanel", "dealWithUnexpectedTiles", "", "tiles", "qsHost", "Lcom/android/systemui/qs/QSTileHost;", "init", "isInternetTilesChanged", "tileList", "Ljava/util/ArrayList;", "isPanelExpanded", "isPanelFullyCollapsed", "isQsExpanded", "onInternetTuningChanged", "spec", "enable", "tunerService", "Lcom/android/systemui/tuner/TunerService;", "host", "context", "Landroid/content/Context;", "onSignalPageChanged", "index", "", "reloadTiles", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.QSTileHostEx */
/* compiled from: QSTileHostEx.kt */
public final class QSTileHostEx {
    public static final Companion Companion;
    private static final String TAG = "QSTileHostEx";
    /* access modifiers changed from: private */
    public static int circleTileCount;
    /* access modifiers changed from: private */
    public static final ArrayList<String> internetList;
    /* access modifiers changed from: private */
    public static int signalPageIndex;
    private boolean isDataConnected;
    private boolean isWifiConnected;
    private NotificationPanelViewController notifPanelViewController;
    private ShadeController shadeController;
    private CirclePagedTileLayout signalTilePage;

    @JvmStatic
    public static final ArrayList<String> addCircleSpecs(List<String> list) {
        return Companion.addCircleSpecs(list);
    }

    @JvmStatic
    public static final int getBtExpectedIndexInVP() {
        return Companion.getBtExpectedIndexInVP();
    }

    @JvmStatic
    public static final String getBtSpec() {
        return Companion.getBtSpec();
    }

    @JvmStatic
    public static final String getCellularSpec() {
        return Companion.getCellularSpec();
    }

    @JvmStatic
    public static final int getFirstExpectedIndexInVP() {
        return Companion.getFirstExpectedIndexInVP();
    }

    @JvmStatic
    public static final String getFirstSpec() {
        return Companion.getFirstSpec();
    }

    @JvmStatic
    public static final String getInternetSpec() {
        return Companion.getInternetSpec();
    }

    @JvmStatic
    public static final int getSecondExpectedIndexInVP() {
        return Companion.getSecondExpectedIndexInVP();
    }

    @JvmStatic
    public static final String getWifiSpec() {
        return Companion.getWifiSpec();
    }

    @JvmStatic
    public static final boolean isBluetoothTile(String str) {
        return Companion.isBluetoothTile(str);
    }

    @JvmStatic
    public static final boolean isCellularTile(String str) {
        return Companion.isCellularTile(str);
    }

    @JvmStatic
    public static final boolean isFirstTile(String str) {
        return Companion.isFirstTile(str);
    }

    @JvmStatic
    public static final boolean isSecondTile(String str) {
        return Companion.isSecondTile(str);
    }

    @JvmStatic
    public static final boolean isSignalOrBtTile(String str) {
        return Companion.isSignalOrBtTile(str);
    }

    @JvmStatic
    public static final boolean isSignalTile(String str) {
        return Companion.isSignalTile(str);
    }

    @JvmStatic
    public static final boolean isTileListEmpty(String str) {
        return Companion.isTileListEmpty(str);
    }

    @JvmStatic
    public static final boolean isWifiTile(String str) {
        return Companion.isWifiTile(str);
    }

    @JvmStatic
    public static final List<String> removeCircleForSpecs(List<String> list) {
        return Companion.removeCircleForSpecs(list);
    }

    @JvmStatic
    public static final List<TileQueryHelper.TileInfo> removeCircleForTileInfo(List<TileQueryHelper.TileInfo> list) {
        return Companion.removeCircleForTileInfo(list);
    }

    @JvmStatic
    public static final boolean shouldAddTileToQQS(String str) {
        return Companion.shouldAddTileToQQS(str);
    }

    @JvmStatic
    public static final boolean shouldHideInternetTile() {
        return Companion.shouldHideInternetTile();
    }

    @JvmStatic
    public static final boolean shouldTileHideOnCustomizer(String str) {
        return Companion.shouldTileHideOnCustomizer(str);
    }

    @Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rH\u0007J\b\u0010\u000e\u001a\u00020\u0006H\u0007J\b\u0010\u000f\u001a\u00020\u0004H\u0007J\b\u0010\u0010\u001a\u00020\u0004H\u0007J\b\u0010\u0011\u001a\u00020\u0006H\u0007J\b\u0010\u0012\u001a\u00020\u0004H\u0007J\b\u0010\u0013\u001a\u00020\u0004H\u0007J\b\u0010\u0014\u001a\u00020\u0006H\u0007J\b\u0010\u0015\u001a\u00020\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u0004H\u0007J\u0010\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u001f\u001a\u00020\u00172\u0006\u0010\f\u001a\u00020\u0004H\u0007J\u0010\u0010 \u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00040\r2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rH\u0007J\u001c\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\r2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020#0\rH\u0007J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u0004H\u0007J\b\u0010&\u001a\u00020\u0017H\u0007J\u0010\u0010'\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0007\u001a\u0012\u0012\u0004\u0012\u00020\u00040\bj\b\u0012\u0004\u0012\u00020\u0004`\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006("}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSTileHostEx$Companion;", "", "()V", "TAG", "", "circleTileCount", "", "internetList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "signalPageIndex", "addCircleSpecs", "tiles", "Ljava/util/List;", "getBtExpectedIndexInVP", "getBtSpec", "getCellularSpec", "getFirstExpectedIndexInVP", "getFirstSpec", "getInternetSpec", "getSecondExpectedIndexInVP", "getWifiSpec", "isBluetoothTile", "", "tile", "isCellularTile", "isFirstTile", "isSecondTile", "isSignalOrBtTile", "spec", "isSignalTile", "isTileListEmpty", "isWifiTile", "removeCircleForSpecs", "removeCircleForTileInfo", "Lcom/android/systemui/qs/customize/TileQueryHelper$TileInfo;", "tileInfos", "shouldAddTileToQQS", "shouldHideInternetTile", "shouldTileHideOnCustomizer", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.nothing.systemui.qs.QSTileHostEx$Companion */
    /* compiled from: QSTileHostEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final int getBtExpectedIndexInVP() {
            return 2;
        }

        @JvmStatic
        public final String getBtSpec() {
            return "bt";
        }

        @JvmStatic
        public final String getCellularSpec() {
            return "cell";
        }

        @JvmStatic
        public final String getInternetSpec() {
            return "internet";
        }

        @JvmStatic
        public final String getWifiSpec() {
            return "wifi";
        }

        @JvmStatic
        public final boolean shouldHideInternetTile() {
            return true;
        }

        private Companion() {
        }

        @JvmStatic
        public final boolean shouldTileHideOnCustomizer(String str) {
            Intrinsics.checkNotNullParameter(str, "spec");
            return isSignalOrBtTile(str);
        }

        @JvmStatic
        public final boolean isSignalOrBtTile(String str) {
            Intrinsics.checkNotNullParameter(str, "spec");
            return isSignalTile(str) || isBluetoothTile(str);
        }

        @JvmStatic
        public final boolean isSignalTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return isCellularTile(str) || isWifiTile(str);
        }

        @JvmStatic
        public final boolean isCellularTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return getCellularSpec().equals(str);
        }

        @JvmStatic
        public final boolean isWifiTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return getWifiSpec().equals(str);
        }

        @JvmStatic
        public final boolean isBluetoothTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return getBtSpec().equals(str);
        }

        @JvmStatic
        public final boolean isFirstTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return str.equals(QSTileHostEx.internetList.get(QSTileHostEx.signalPageIndex));
        }

        @JvmStatic
        public final boolean isSecondTile(String str) {
            Intrinsics.checkNotNullParameter(str, "tile");
            return str.equals(QSTileHostEx.internetList.get(QSTileHostEx.signalPageIndex == 0 ? 1 : 0));
        }

        @JvmStatic
        public final String getFirstSpec() {
            Object obj = QSTileHostEx.internetList.get(QSTileHostEx.signalPageIndex);
            Intrinsics.checkNotNullExpressionValue(obj, "internetList.get(signalPageIndex)");
            return (String) obj;
        }

        @JvmStatic
        public final int getFirstExpectedIndexInVP() {
            return QSTileHostEx.signalPageIndex;
        }

        @JvmStatic
        public final int getSecondExpectedIndexInVP() {
            return QSTileHostEx.signalPageIndex == 0 ? 1 : 0;
        }

        @JvmStatic
        public final ArrayList<String> addCircleSpecs(List<String> list) {
            Intrinsics.checkNotNullParameter(list, "tiles");
            List<String> removeCircleForSpecs = removeCircleForSpecs(list);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(QSTileHostEx.internetList);
            arrayList.add(getBtSpec());
            arrayList.addAll(removeCircleForSpecs);
            return arrayList;
        }

        @JvmStatic
        public final List<String> removeCircleForSpecs(List<String> list) {
            Intrinsics.checkNotNullParameter(list, "tiles");
            List<String> arrayList = new ArrayList<>();
            for (String next : list) {
                if (next == null || QSTileHostEx.Companion.shouldTileHideOnCustomizer(next)) {
                    Companion companion = QSTileHostEx.Companion;
                    QSTileHostEx.circleTileCount = QSTileHostEx.circleTileCount + 1;
                    if (QSTileHostEx.circleTileCount > QSTileHostEx.Companion.getBtExpectedIndexInVP() + 1) {
                        NTLogUtil.m1686d(QSTileHostEx.TAG, "removeCircleForSpec: " + next);
                    }
                } else {
                    arrayList.add(next);
                }
            }
            return arrayList;
        }

        @JvmStatic
        public final List<TileQueryHelper.TileInfo> removeCircleForTileInfo(List<TileQueryHelper.TileInfo> list) {
            Intrinsics.checkNotNullParameter(list, "tileInfos");
            List<TileQueryHelper.TileInfo> arrayList = new ArrayList<>();
            for (TileQueryHelper.TileInfo next : list) {
                if (next != null) {
                    Companion companion = QSTileHostEx.Companion;
                    String str = next.spec;
                    Intrinsics.checkNotNullExpressionValue(str, "tileInfo.spec");
                    if (!companion.shouldTileHideOnCustomizer(str)) {
                        arrayList.add(next);
                    }
                }
            }
            return arrayList;
        }

        @JvmStatic
        public final boolean shouldAddTileToQQS(String str) {
            Intrinsics.checkNotNullParameter(str, "spec");
            return (QSTileHostEx.signalPageIndex == getFirstExpectedIndexInVP() && !isSecondTile(str)) || (QSTileHostEx.signalPageIndex == getSecondExpectedIndexInVP() && !isFirstTile(str));
        }

        @JvmStatic
        public final boolean isTileListEmpty(String str) {
            Intrinsics.checkNotNullParameter(str, "tiles");
            int i = 0;
            for (String isSignalOrBtTile : StringsKt.split$default((CharSequence) str, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) {
                if (!isSignalOrBtTile(isSignalOrBtTile)) {
                    i++;
                }
            }
            if (i <= 0) {
                return true;
            }
            return false;
        }
    }

    static {
        Companion companion = new Companion((DefaultConstructorMarker) null);
        Companion = companion;
        internetList = CollectionsKt.arrayListOf(companion.getWifiSpec(), companion.getCellularSpec());
    }

    public final CirclePagedTileLayout getSignalTilePage() {
        return this.signalTilePage;
    }

    public final void setSignalTilePage(CirclePagedTileLayout circlePagedTileLayout) {
        this.signalTilePage = circlePagedTileLayout;
    }

    public final void init(ShadeController shadeController2, NotificationPanelViewController notificationPanelViewController) {
        Intrinsics.checkNotNullParameter(shadeController2, "shadeController");
        Intrinsics.checkNotNullParameter(notificationPanelViewController, "notifPanelViewController");
        this.shadeController = shadeController2;
        this.notifPanelViewController = notificationPanelViewController;
    }

    public final String dealWithUnexpectedTiles(String str, QSTileHost qSTileHost) {
        Intrinsics.checkNotNullParameter(qSTileHost, "qsHost");
        if (str == null) {
            return str;
        }
        ArrayList arrayList = new ArrayList();
        for (String add : StringsKt.split$default((CharSequence) str, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) {
            arrayList.add(add);
        }
        Companion companion = Companion;
        boolean remove = arrayList.remove((Object) companion.getInternetSpec());
        circleTileCount = 0;
        companion.removeCircleForSpecs(CollectionsKt.toMutableList(arrayList));
        if (!remove) {
            remove = true;
            if (!isInternetTilesChanged(arrayList) && Objects.equals(Integer.valueOf(arrayList.indexOf(companion.getBtSpec())), Integer.valueOf(companion.getBtExpectedIndexInVP())) && circleTileCount <= companion.getBtExpectedIndexInVP() + 1) {
                remove = false;
            }
        }
        if (remove) {
            qSTileHost.saveTilesToSettings(arrayList);
        }
        return TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, arrayList);
    }

    public final void onInternetTuningChanged(String str, boolean z, TunerService tunerService, QSTileHost qSTileHost, Context context) {
        int i;
        Intrinsics.checkNotNullParameter(str, "spec");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(qSTileHost, "host");
        Intrinsics.checkNotNullParameter(context, "context");
        Companion companion = Companion;
        if (!companion.isCellularTile(str) || !z) {
            i = (!companion.isWifiTile(str) || !z) ? -1 : 0;
        } else {
            i = 1;
        }
        if (i != -1) {
            QSTileHostEx$$ExternalSyntheticLambda0 qSTileHostEx$$ExternalSyntheticLambda0 = new QSTileHostEx$$ExternalSyntheticLambda0(this, i);
            if (!isPanelFullyCollapsed()) {
                addPostFullyCollapseAction(qSTileHostEx$$ExternalSyntheticLambda0);
                return;
            }
            CirclePagedTileLayout signalTilePage2 = getSignalTilePage();
            Intrinsics.checkNotNull(signalTilePage2);
            signalTilePage2.post(qSTileHostEx$$ExternalSyntheticLambda0);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onInternetTuningChanged$lambda-0  reason: not valid java name */
    public static final void m3516onInternetTuningChanged$lambda0(QSTileHostEx qSTileHostEx, int i) {
        Intrinsics.checkNotNullParameter(qSTileHostEx, "this$0");
        CirclePagedTileLayout signalTilePage2 = qSTileHostEx.getSignalTilePage();
        Intrinsics.checkNotNull(signalTilePage2);
        signalTilePage2.setCurrentItem(i, false);
    }

    public final void addPostFullyCollapseAction(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        NotificationPanelViewController notificationPanelViewController = this.notifPanelViewController;
        Intrinsics.checkNotNull(notificationPanelViewController);
        notificationPanelViewController.addPostFullyCollapseAction(runnable);
    }

    private final boolean isInternetTilesChanged(ArrayList<String> arrayList) {
        Companion companion = Companion;
        return arrayList.indexOf(companion.getCellularSpec()) != companion.getSecondExpectedIndexInVP() || arrayList.indexOf(companion.getWifiSpec()) >= companion.getFirstExpectedIndexInVP();
    }

    public final void onSignalPageChanged(int i) {
        signalPageIndex = i;
    }

    public final boolean isPanelExpanded() {
        NotificationPanelViewController notificationPanelViewController = this.notifPanelViewController;
        Intrinsics.checkNotNull(notificationPanelViewController);
        return notificationPanelViewController.isPanelExpanded();
    }

    public final boolean isPanelFullyCollapsed() {
        NotificationPanelViewController notificationPanelViewController = this.notifPanelViewController;
        Intrinsics.checkNotNull(notificationPanelViewController);
        return notificationPanelViewController.isPanelFullyCollapsed();
    }

    public final boolean isQsExpanded() {
        NotificationPanelViewController notificationPanelViewController = this.notifPanelViewController;
        Intrinsics.checkNotNull(notificationPanelViewController);
        return notificationPanelViewController.isQsExpanded();
    }

    public final void collapsePanel() {
        ShadeController shadeController2 = this.shadeController;
        Intrinsics.checkNotNull(shadeController2);
        shadeController2.collapsePanel(true);
    }

    public final void reloadTiles(QSTileHost qSTileHost, String str) {
        Intrinsics.checkNotNullParameter(qSTileHost, "qsHost");
        qSTileHost.onTuningChanged(QSTileHost.TILES_SETTING, "");
        qSTileHost.onTuningChanged(QSTileHost.TILES_SETTING, str);
    }
}
