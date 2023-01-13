package com.android.systemui.p012qs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.external.CustomTileStatePersister;
import com.android.systemui.p012qs.external.TileLifecycleManager;
import com.android.systemui.p012qs.external.TileServiceKey;
import com.android.systemui.p012qs.external.TileServiceRequestController;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.p011qs.QSFactory;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.settings.SecureSettings;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSPanelExpandListener;
import com.nothing.systemui.p024qs.QSTileHostEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Provider;

@SysUISingleton
/* renamed from: com.android.systemui.qs.QSTileHost */
public class QSTileHost implements QSHost, TunerService.Tunable, PluginListener<QSFactory>, Dumpable {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int MAX_QS_INSTANCE_ID = 1048576;
    public static final int POSITION_AT_END = -1;
    private static final String TAG = "QSTileHost";
    public static final String TILES_SETTING = "sysui_qs_tiles";
    private AutoTileManager mAutoTiles;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final List<QSHost.Callback> mCallbacks = new ArrayList();
    private final Optional<CentralSurfaces> mCentralSurfacesOptional;
    private final Context mContext;
    private int mCurrentUser;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private final DumpManager mDumpManager;
    private QSTileHostEx mEx;
    private final StatusBarIconController mIconController;
    private final InstanceIdSequence mInstanceIdSequence;
    private final PluginManager mPluginManager;
    private final QSLogger mQSLogger;
    private QSPanelExpandListener mQSPanelExpandListener;
    private final ArrayList<QSFactory> mQsFactories;
    private SecureSettings mSecureSettings;
    private TileLifecycleManager.Factory mTileLifeCycleManagerFactory;
    private final TileServiceRequestController mTileServiceRequestController;
    protected final ArrayList<String> mTileSpecs = new ArrayList<>();
    private final LinkedHashMap<String, QSTile> mTiles = new LinkedHashMap<>();
    private final TunerService mTunerService;
    private final UiEventLogger mUiEventLogger;
    private Context mUserContext;
    private UserTracker mUserTracker;

    public void warn(String str, Throwable th) {
    }

    @Inject
    public QSTileHost(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, @Main Handler handler, @Background Looper looper, PluginManager pluginManager, TunerService tunerService, Provider<AutoTileManager> provider, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, Optional<CentralSurfaces> optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister, TileServiceRequestController.Builder builder, TileLifecycleManager.Factory factory) {
        Context context2 = context;
        DumpManager dumpManager2 = dumpManager;
        ArrayList<QSFactory> arrayList = new ArrayList<>();
        this.mQsFactories = arrayList;
        this.mEx = (QSTileHostEx) NTDependencyEx.get(QSTileHostEx.class);
        this.mIconController = statusBarIconController;
        this.mContext = context2;
        this.mUserContext = context2;
        this.mTunerService = tunerService;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager2;
        this.mQSLogger = qSLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTileServiceRequestController = builder.create(this);
        this.mTileLifeCycleManagerFactory = factory;
        this.mInstanceIdSequence = new InstanceIdSequence(1048576);
        this.mCentralSurfacesOptional = optional;
        QSFactory qSFactory2 = qSFactory;
        arrayList.add(qSFactory);
        pluginManager.addPluginListener(this, QSFactory.class, true);
        dumpManager2.registerDumpable(TAG, this);
        this.mUserTracker = userTracker;
        this.mSecureSettings = secureSettings;
        this.mCustomTileStatePersister = customTileStatePersister;
        Provider<AutoTileManager> provider2 = provider;
        Handler handler2 = handler;
        handler.post(new QSTileHost$$ExternalSyntheticLambda4(this, tunerService, provider));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-QSTileHost  reason: not valid java name */
    public /* synthetic */ void m2945lambda$new$0$comandroidsystemuiqsQSTileHost(TunerService tunerService, Provider provider) {
        tunerService.addTunable(this, TILES_SETTING);
        this.mAutoTiles = (AutoTileManager) provider.get();
        this.mTileServiceRequestController.init();
    }

    public StatusBarIconController getIconController() {
        return this.mIconController;
    }

    public InstanceId getNewInstanceId() {
        return this.mInstanceIdSequence.newInstanceId();
    }

    public void destroy() {
        this.mTiles.values().forEach(new QSTileHost$$ExternalSyntheticLambda5());
        this.mAutoTiles.destroy();
        this.mTunerService.removeTunable(this);
        this.mPluginManager.removePluginListener(this);
        this.mDumpManager.unregisterDumpable(TAG);
        this.mTileServiceRequestController.destroy();
    }

    public void onPluginConnected(QSFactory qSFactory, Context context) {
        this.mQsFactories.add(0, qSFactory);
        String value = this.mTunerService.getValue(TILES_SETTING);
        onTuningChanged(TILES_SETTING, "");
        onTuningChanged(TILES_SETTING, value);
    }

    public void onPluginDisconnected(QSFactory qSFactory) {
        this.mQsFactories.remove((Object) qSFactory);
        String value = this.mTunerService.getValue(TILES_SETTING);
        onTuningChanged(TILES_SETTING, "");
        onTuningChanged(TILES_SETTING, value);
    }

    public UiEventLogger getUiEventLogger() {
        return this.mUiEventLogger;
    }

    public void addCallback(QSHost.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(QSHost.Callback callback) {
        this.mCallbacks.remove((Object) callback);
    }

    public Collection<QSTile> getTiles() {
        return this.mTiles.values();
    }

    public void collapsePanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda2());
    }

    public void forceCollapsePanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda8());
    }

    public void openPanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda10());
    }

    public Context getContext() {
        return this.mContext;
    }

    public Context getUserContext() {
        return this.mUserContext;
    }

    public int getUserId() {
        return this.mCurrentUser;
    }

    public int indexOf(String str) {
        return this.mTileSpecs.indexOf(str);
    }

    public void onTuningChanged(String str, String str2) {
        boolean z;
        if (TILES_SETTING.equals(str)) {
            Log.d(TAG, "Recreating tiles");
            String dealWithUnexpectedTiles = this.mEx.dealWithUnexpectedTiles(str2, this);
            if (dealWithUnexpectedTiles == null && UserManager.isDeviceInDemoMode(this.mContext)) {
                dealWithUnexpectedTiles = this.mContext.getResources().getString(C1894R.string.quick_settings_tiles_retail_mode);
            }
            List<String> loadTileSpecs = loadTileSpecs(this.mContext, dealWithUnexpectedTiles);
            int userId = this.mUserTracker.getUserId();
            if (userId != this.mCurrentUser) {
                this.mUserContext = this.mUserTracker.getUserContext();
                AutoTileManager autoTileManager = this.mAutoTiles;
                if (autoTileManager != null) {
                    autoTileManager.mo43664xb844e8a5(UserHandle.of(userId));
                }
            }
            if (!loadTileSpecs.equals(this.mTileSpecs) || userId != this.mCurrentUser) {
                this.mTiles.entrySet().stream().filter(new QSTileHost$$ExternalSyntheticLambda11(loadTileSpecs)).forEach(new QSTileHost$$ExternalSyntheticLambda1(this));
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (String next : loadTileSpecs) {
                    QSTile qSTile = this.mTiles.get(next);
                    if (qSTile == null || (z && ((CustomTile) qSTile).getUser() != userId)) {
                        if (qSTile != null) {
                            qSTile.destroy();
                            Log.d(TAG, "Destroying tile for wrong user: " + next);
                            this.mQSLogger.logTileDestroyed(next, "Tile for wrong user");
                        }
                        Log.d(TAG, "Creating tile: " + next);
                        try {
                            QSTile createTile = createTile(next);
                            if (createTile != null) {
                                createTile.setTileSpec(next);
                                if (createTile.isAvailable()) {
                                    linkedHashMap.put(next, createTile);
                                    this.mQSLogger.logTileAdded(next);
                                } else {
                                    createTile.destroy();
                                    Log.d(TAG, "Destroying not available tile: " + next);
                                    this.mQSLogger.logTileDestroyed(next, "Tile not available");
                                }
                            }
                        } catch (Throwable th) {
                            Log.w(TAG, "Error creating tile for spec: " + next, th);
                        }
                    } else if (qSTile.isAvailable()) {
                        if (DEBUG) {
                            Log.d(TAG, "Adding " + qSTile);
                        }
                        qSTile.removeCallbacks();
                        if (!((z = qSTile instanceof CustomTile)) && this.mCurrentUser != userId) {
                            qSTile.userSwitch(userId);
                        }
                        linkedHashMap.put(next, qSTile);
                        this.mQSLogger.logTileAdded(next);
                    } else {
                        qSTile.destroy();
                        Log.d(TAG, "Destroying not available tile: " + next);
                        this.mQSLogger.logTileDestroyed(next, "Tile not available");
                    }
                }
                this.mCurrentUser = userId;
                ArrayList arrayList = new ArrayList(this.mTileSpecs);
                this.mTileSpecs.clear();
                this.mTileSpecs.addAll(loadTileSpecs);
                this.mTiles.clear();
                this.mTiles.putAll(linkedHashMap);
                if (!linkedHashMap.isEmpty() || loadTileSpecs.isEmpty()) {
                    for (int i = 0; i < this.mCallbacks.size(); i++) {
                        this.mCallbacks.get(i).onTilesChanged();
                    }
                    return;
                }
                Log.d(TAG, "No valid tiles on tuning changed. Setting to default.");
                changeTiles(arrayList, loadTileSpecs(this.mContext, ""));
            }
        }
    }

    static /* synthetic */ boolean lambda$onTuningChanged$2(List list, Map.Entry entry) {
        return !list.contains(entry.getKey());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTuningChanged$3$com-android-systemui-qs-QSTileHost  reason: not valid java name */
    public /* synthetic */ void m2946lambda$onTuningChanged$3$comandroidsystemuiqsQSTileHost(Map.Entry entry) {
        Log.d(TAG, "Destroying tile: " + ((String) entry.getKey()));
        this.mQSLogger.logTileDestroyed((String) entry.getKey(), "Tile removed");
        ((QSTile) entry.getValue()).destroy();
    }

    public void removeTile(String str) {
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda7(str));
    }

    public void removeTiles(Collection<String> collection) {
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda9(collection));
    }

    public void unmarkTileAsAutoAdded(String str) {
        AutoTileManager autoTileManager = this.mAutoTiles;
        if (autoTileManager != null) {
            autoTileManager.unmarkTileAsAutoAdded(str);
        }
    }

    public void addTile(String str) {
        addTile(str, -1);
    }

    public void addTile(String str, int i) {
        if (str.equals(AutoTileManager.WORK)) {
            Log.wtfStack(TAG, "Adding work tile");
        }
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda6(str, i));
    }

    static /* synthetic */ boolean lambda$addTile$6(String str, int i, List list) {
        if (list.contains(str)) {
            return false;
        }
        int size = list.size();
        if (i == -1 || i >= size) {
            list.add(str);
            return true;
        }
        list.add(i, str);
        return true;
    }

    public void saveTilesToSettings(List<String> list) {
        if (list.contains(AutoTileManager.WORK)) {
            Log.wtfStack(TAG, "Saving work tile");
        }
        this.mSecureSettings.putStringForUser(TILES_SETTING, TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, QSTileHostEx.addCircleSpecs(list)), (String) null, false, this.mCurrentUser, true);
    }

    private void changeTileSpecs(Predicate<List<String>> predicate) {
        List<String> loadTileSpecs = loadTileSpecs(this.mContext, this.mSecureSettings.getStringForUser(TILES_SETTING, this.mCurrentUser));
        if (predicate.test(loadTileSpecs)) {
            saveTilesToSettings(loadTileSpecs);
        }
    }

    public void addTile(ComponentName componentName) {
        addTile(componentName, false);
    }

    public void addTile(ComponentName componentName, boolean z) {
        String spec = CustomTile.toSpec(componentName);
        if (!this.mTileSpecs.contains(spec)) {
            ArrayList arrayList = new ArrayList(this.mTileSpecs);
            if (z) {
                arrayList.add(spec);
            } else {
                arrayList.add(0, spec);
            }
            changeTiles(this.mTileSpecs, arrayList);
        }
    }

    public void removeTile(ComponentName componentName) {
        ArrayList arrayList = new ArrayList(this.mTileSpecs);
        arrayList.remove((Object) CustomTile.toSpec(componentName));
        changeTiles(this.mTileSpecs, arrayList);
    }

    public void changeTiles(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList.get(i);
            if (str.startsWith(CustomTile.PREFIX) && !list2.contains(str)) {
                ComponentName componentFromSpec = CustomTile.getComponentFromSpec(str);
                TileLifecycleManager create = this.mTileLifeCycleManagerFactory.create(new Intent().setComponent(componentFromSpec), new UserHandle(this.mCurrentUser));
                create.onStopListening();
                create.onTileRemoved();
                this.mCustomTileStatePersister.removeState(new TileServiceKey(componentFromSpec, this.mCurrentUser));
                TileLifecycleManager.setTileAdded(this.mContext, componentFromSpec, false);
                create.flushMessagesAndUnbind();
            }
        }
        if (DEBUG) {
            Log.d(TAG, "saveCurrentTiles " + list2);
        }
        saveTilesToSettings(list2);
    }

    public QSTile createTile(String str) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTile createTile = this.mQsFactories.get(i).createTile(str);
            if (createTile != null) {
                return createTile;
            }
        }
        return null;
    }

    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTileView createTileView = this.mQsFactories.get(i).createTileView(context, qSTile, z);
            if (createTileView != null) {
                return createTileView;
            }
        }
        throw new RuntimeException("Default factory didn't create view for " + qSTile.getTileSpec());
    }

    public static List<String> loadTileSpecs(Context context, String str) {
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(str) || QSTileHostEx.isTileListEmpty(str)) {
            str = resources.getString(C1894R.string.quick_settings_tiles);
            if (DEBUG) {
                Log.d(TAG, "Loaded tile specs from config: " + str);
            }
        } else if (DEBUG) {
            Log.d(TAG, "Loaded tile specs from setting: " + str);
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (String trim : str.split(NavigationBarInflaterView.BUTTON_SEPARATOR)) {
            String trim2 = trim.trim();
            if (!trim2.isEmpty()) {
                if (trim2.equals("default")) {
                    if (!z) {
                        for (String next : getDefaultSpecs(context)) {
                            if (!arraySet.contains(next)) {
                                arrayList.add(next);
                                arraySet.add(next);
                            }
                        }
                        z = true;
                    }
                } else if (!arraySet.contains(trim2)) {
                    arrayList.add(trim2);
                    arraySet.add(trim2);
                }
            }
        }
        if (QSTileHostEx.shouldHideInternetTile()) {
            return arrayList;
        }
        if (arrayList.contains("internet")) {
            arrayList.remove((Object) "wifi");
            arrayList.remove((Object) "cell");
        } else if (arrayList.contains("wifi")) {
            arrayList.set(arrayList.indexOf("wifi"), "internet");
            arrayList.remove((Object) "cell");
        } else if (arrayList.contains("cell")) {
            arrayList.set(arrayList.indexOf("cell"), "internet");
        }
        return arrayList;
    }

    public static List<String> getDefaultSpecs(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(context.getResources().getString(C1894R.string.nt_quick_settings_tiles_default).split(NavigationBarInflaterView.BUTTON_SEPARATOR)));
        if (Build.IS_DEBUGGABLE) {
            arrayList.add(GarbageMonitor.MemoryTile.TILE_SPEC);
        }
        return arrayList;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("QSTileHost:");
        this.mTiles.values().stream().filter(new QSTileHost$$ExternalSyntheticLambda0()).forEach(new QSTileHost$$ExternalSyntheticLambda3(printWriter, strArr));
    }

    static /* synthetic */ boolean lambda$dump$7(QSTile qSTile) {
        return qSTile instanceof Dumpable;
    }

    public void onInternetTuningChanged(String str, boolean z) {
        this.mEx.onInternetTuningChanged(str, z, this.mTunerService, this, this.mContext);
    }

    public void setQSPanelExpandListener(QSPanelExpandListener qSPanelExpandListener) {
        this.mQSPanelExpandListener = qSPanelExpandListener;
    }

    public void setExpanded(boolean z) {
        QSPanelExpandListener qSPanelExpandListener = this.mQSPanelExpandListener;
        if (qSPanelExpandListener != null) {
            qSPanelExpandListener.setExpanded(z);
        }
    }

    public void reloadTiles() {
        this.mEx.reloadTiles(this, this.mTunerService.getValue(TILES_SETTING));
    }
}
