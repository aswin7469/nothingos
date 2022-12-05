package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.quicksettings.Tile;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileServiceKey;
import com.android.systemui.qs.external.TileServices;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class QSTileHost implements QSHost, TunerService.Tunable, PluginListener<QSFactory>, Dumpable {
    private static final boolean DEBUG = Log.isLoggable("QSTileHost", 3);
    private AutoTileManager mAutoTiles;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Context mContext;
    private int mCurrentUser;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private final DumpManager mDumpManager;
    private final FeatureFlags mFeatureFlags;
    private final StatusBarIconController mIconController;
    private final PluginManager mPluginManager;
    private final QSLogger mQSLogger;
    private final ArrayList<QSFactory> mQsFactories;
    private SecureSettings mSecureSettings;
    private final TileServices mServices;
    private final Optional<StatusBar> mStatusBarOptional;
    private final TunerService mTunerService;
    private final UiEventLogger mUiEventLogger;
    private Context mUserContext;
    private UserTracker mUserTracker;
    private final LinkedHashMap<String, QSTile> mTiles = new LinkedHashMap<>();
    protected final ArrayList<String> mTileSpecs = new ArrayList<>();
    private final List<QSHost.Callback> mCallbacks = new ArrayList();
    private final InstanceIdSequence mInstanceIdSequence = new InstanceIdSequence(1048576);

    public static int getBtExpectedIndexInVP() {
        return 3;
    }

    public static String getBtSpec() {
        return "bt";
    }

    public static int getCellExpectedIndexInVP() {
        return 0;
    }

    public static String getCellularSpec() {
        return "cell";
    }

    public static int getHotspotExpectedIndexInVP() {
        return 2;
    }

    public static String getHotspotSpec() {
        return "hotspot";
    }

    public static String getInternetSpec() {
        return "internet";
    }

    public static int getWifiExpectedIndexInVP() {
        return 1;
    }

    public static String getWifiSpec() {
        return "wifi";
    }

    @Override // com.android.systemui.qs.QSHost
    public void warn(String str, Throwable th) {
    }

    public QSTileHost(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, Handler handler, Looper looper, PluginManager pluginManager, final TunerService tunerService, final Provider<AutoTileManager> provider, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, Optional<StatusBar> optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister, FeatureFlags featureFlags) {
        ArrayList<QSFactory> arrayList = new ArrayList<>();
        this.mQsFactories = arrayList;
        this.mIconController = statusBarIconController;
        this.mContext = context;
        this.mUserContext = context;
        this.mTunerService = tunerService;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager;
        this.mQSLogger = qSLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mServices = new TileServices(this, looper, broadcastDispatcher, userTracker);
        this.mStatusBarOptional = optional;
        arrayList.add(qSFactory);
        pluginManager.addPluginListener((PluginListener) this, QSFactory.class, true);
        dumpManager.registerDumpable("QSTileHost", this);
        this.mUserTracker = userTracker;
        this.mSecureSettings = secureSettings;
        this.mCustomTileStatePersister = customTileStatePersister;
        this.mFeatureFlags = featureFlags;
        handler.post(new Runnable() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QSTileHost.this.lambda$new$0(tunerService, provider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TunerService tunerService, Provider provider) {
        tunerService.addTunable(this, "sysui_qs_tiles");
        this.mAutoTiles = (AutoTileManager) provider.mo1933get();
    }

    public StatusBarIconController getIconController() {
        return this.mIconController;
    }

    @Override // com.android.systemui.qs.QSHost
    public InstanceId getNewInstanceId() {
        return this.mInstanceIdSequence.newInstanceId();
    }

    @Override // com.android.systemui.plugins.PluginListener
    public void onPluginConnected(QSFactory qSFactory, Context context) {
        this.mQsFactories.add(0, qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public void onPluginDisconnected(QSFactory qSFactory) {
        this.mQsFactories.remove(qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    @Override // com.android.systemui.qs.QSHost
    public UiEventLogger getUiEventLogger() {
        return this.mUiEventLogger;
    }

    public void addCallback(QSHost.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(QSHost.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public Collection<QSTile> getTiles() {
        return this.mTiles.values();
    }

    @Override // com.android.systemui.qs.QSHost
    public void collapsePanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda3.INSTANCE);
    }

    public void forceCollapsePanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda4.INSTANCE);
    }

    @Override // com.android.systemui.qs.QSHost
    public void openPanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda5.INSTANCE);
    }

    @Override // com.android.systemui.qs.QSHost
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public Context getUserContext() {
        return this.mUserContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public int getUserId() {
        return this.mCurrentUser;
    }

    @Override // com.android.systemui.qs.QSHost
    public TileServices getTileServices() {
        return this.mServices;
    }

    @Override // com.android.systemui.qs.QSHost
    public int indexOf(String str) {
        return this.mTileSpecs.indexOf(str);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        boolean z;
        if (!"sysui_qs_tiles".equals(str)) {
            return;
        }
        Log.d("QSTileHost", "Recreating tiles,key " + str + " newValue " + str2);
        if (dealWithUnexpectedTiles(str2)) {
            Log.d("QSTileHost", "onTuningChanged: reload tiles later.");
            return;
        }
        if (str2 == null && UserManager.isDeviceInDemoMode(this.mContext)) {
            str2 = this.mContext.getResources().getString(R$string.quick_settings_tiles_retail_mode);
        }
        final List<String> loadTileSpecs = loadTileSpecs(this.mContext, str2, this.mFeatureFlags);
        int userId = this.mUserTracker.getUserId();
        if (userId != this.mCurrentUser) {
            this.mUserContext = this.mUserTracker.getUserContext();
            AutoTileManager autoTileManager = this.mAutoTiles;
            if (autoTileManager != null) {
                autoTileManager.lambda$changeUser$0(UserHandle.of(userId));
            }
        }
        if (loadTileSpecs.equals(this.mTileSpecs) && userId == this.mCurrentUser) {
            return;
        }
        this.mTiles.entrySet().stream().filter(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onTuningChanged$2;
                lambda$onTuningChanged$2 = QSTileHost.lambda$onTuningChanged$2(loadTileSpecs, (Map.Entry) obj);
                return lambda$onTuningChanged$2;
            }
        }).forEach(new Consumer() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QSTileHost.this.lambda$onTuningChanged$3((Map.Entry) obj);
            }
        });
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String str3 : loadTileSpecs) {
            QSTile qSTile = this.mTiles.get(str3);
            if (qSTile != null && (!((z = qSTile instanceof CustomTile)) || ((CustomTile) qSTile).getUser() == userId)) {
                if (qSTile.isAvailable()) {
                    if (DEBUG) {
                        Log.d("QSTileHost", "Adding " + qSTile);
                    }
                    qSTile.removeCallbacks();
                    if (!z && this.mCurrentUser != userId) {
                        qSTile.userSwitch(userId);
                    }
                    linkedHashMap.put(str3, qSTile);
                    this.mQSLogger.logTileAdded(str3);
                } else {
                    qSTile.destroy();
                    Log.d("QSTileHost", "Destroying not available tile: " + str3);
                    this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                }
            } else {
                if (qSTile != null) {
                    qSTile.destroy();
                    Log.d("QSTileHost", "Destroying tile for wrong user: " + str3);
                    this.mQSLogger.logTileDestroyed(str3, "Tile for wrong user");
                }
                Log.d("QSTileHost", "Creating tile: " + str3);
                try {
                    QSTile createTile = createTile(str3);
                    if (createTile != null) {
                        createTile.setTileSpec(str3);
                        if (createTile.isAvailable()) {
                            linkedHashMap.put(str3, createTile);
                            this.mQSLogger.logTileAdded(str3);
                        } else {
                            createTile.destroy();
                            Log.d("QSTileHost", "Destroying not available tile: " + str3);
                            this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                        }
                    }
                } catch (Throwable th) {
                    Log.w("QSTileHost", "Error creating tile for spec: " + str3, th);
                }
            }
        }
        this.mCurrentUser = userId;
        ArrayList arrayList = new ArrayList(this.mTileSpecs);
        this.mTileSpecs.clear();
        this.mTileSpecs.addAll(loadTileSpecs);
        this.mTiles.clear();
        this.mTiles.putAll(linkedHashMap);
        if (linkedHashMap.isEmpty() && !loadTileSpecs.isEmpty()) {
            Log.d("QSTileHost", "No valid tiles on tuning changed. Setting to default.");
            changeTiles(arrayList, loadTileSpecs(this.mContext, "", this.mFeatureFlags));
            return;
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onTilesChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onTuningChanged$2(List list, Map.Entry entry) {
        return !list.contains(entry.getKey());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTuningChanged$3(Map.Entry entry) {
        Log.d("QSTileHost", "Destroying tile: " + ((String) entry.getKey()));
        this.mQSLogger.logTileDestroyed((String) entry.getKey(), "Tile removed");
        ((QSTile) entry.getValue()).destroy();
    }

    @Override // com.android.systemui.qs.QSHost
    public void removeTile(final String str) {
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean remove;
                remove = ((List) obj).remove(str);
                return remove;
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public void removeTiles(final Collection<String> collection) {
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean removeAll;
                removeAll = ((List) obj).removeAll(collection);
                return removeAll;
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public void unmarkTileAsAutoAdded(String str) {
        AutoTileManager autoTileManager = this.mAutoTiles;
        if (autoTileManager != null) {
            autoTileManager.unmarkTileAsAutoAdded(str);
        }
    }

    public void addTile(String str) {
        addTile(str, -1);
    }

    public void addTile(final String str, final int i) {
        if (str.equals("work")) {
            Log.wtfStack("QSTileHost", "Adding work tile");
        }
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$addTile$6;
                lambda$addTile$6 = QSTileHost.lambda$addTile$6(str, i, (List) obj);
                return lambda$addTile$6;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$addTile$6(String str, int i, List list) {
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

    void saveTilesToSettings(List<String> list) {
        if (list.contains("work")) {
            Log.wtfStack("QSTileHost", "Saving work tile");
        }
        this.mSecureSettings.putStringForUser("sysui_qs_tiles", TextUtils.join(",", addCircleSpecs(list)), null, false, this.mCurrentUser, true);
    }

    private void changeTileSpecs(Predicate<List<String>> predicate) {
        List<String> loadTileSpecs = loadTileSpecs(this.mContext, this.mSecureSettings.getStringForUser("sysui_qs_tiles", this.mCurrentUser), this.mFeatureFlags);
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
        arrayList.remove(CustomTile.toSpec(componentName));
        changeTiles(this.mTileSpecs, arrayList);
    }

    public void changeTiles(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList.get(i);
            if (str.startsWith("custom(") && !list2.contains(str)) {
                ComponentName componentFromSpec = CustomTile.getComponentFromSpec(str);
                TileLifecycleManager tileLifecycleManager = new TileLifecycleManager(new Handler(), this.mContext, this.mServices, new Tile(), new Intent().setComponent(componentFromSpec), new UserHandle(this.mCurrentUser), this.mBroadcastDispatcher);
                tileLifecycleManager.onStopListening();
                tileLifecycleManager.onTileRemoved();
                this.mCustomTileStatePersister.removeState(new TileServiceKey(componentFromSpec, this.mCurrentUser));
                TileLifecycleManager.setTileAdded(this.mContext, componentFromSpec, false);
                tileLifecycleManager.flushMessagesAndUnbind();
            }
        }
        if (DEBUG) {
            Log.d("QSTileHost", "saveCurrentTiles " + list2);
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

    protected static List<String> loadTileSpecs(Context context, String str, FeatureFlags featureFlags) {
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(str)) {
            str = resources.getString(R$string.quick_settings_tiles);
            if (DEBUG) {
                Log.d("QSTileHost", "Loaded tile specs from config: " + str);
            }
        } else if (DEBUG) {
            Log.d("QSTileHost", "Loaded tile specs from setting: " + str);
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (String str2 : str.split(",")) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                if (trim.equals("default")) {
                    if (!z) {
                        for (String str3 : getDefaultSpecs(context)) {
                            if (!arraySet.contains(str3)) {
                                arrayList.add(str3);
                                arraySet.add(str3);
                            }
                        }
                        z = true;
                    }
                } else if (!arraySet.contains(trim)) {
                    arrayList.add(trim);
                    arraySet.add(trim);
                }
            }
        }
        if (featureFlags.isProviderModelSettingEnabled()) {
            if (!arrayList.contains("internet")) {
                if (arrayList.contains("wifi")) {
                    arrayList.set(arrayList.indexOf("wifi"), "internet");
                    arrayList.remove("cell");
                } else if (arrayList.contains("cell")) {
                    arrayList.set(arrayList.indexOf("cell"), "internet");
                }
            } else {
                arrayList.remove("wifi");
                arrayList.remove("cell");
            }
        }
        return arrayList;
    }

    public static List<String> getDefaultSpecs(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(context.getResources().getString(R$string.quick_settings_tiles_default).split(",")));
        if (Build.IS_DEBUGGABLE) {
            arrayList.add("dbg:mem");
        }
        return arrayList;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] strArr) {
        printWriter.println("QSTileHost:");
        this.mTiles.values().stream().filter(QSTileHost$$ExternalSyntheticLambda10.INSTANCE).forEach(new Consumer() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QSTileHost.lambda$dump$8(fileDescriptor, printWriter, strArr, (QSTile) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dump$7(QSTile qSTile) {
        return qSTile instanceof Dumpable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$8(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, QSTile qSTile) {
        ((Dumpable) qSTile).dump(fileDescriptor, printWriter, strArr);
    }

    public static boolean shouldTileHideOnCustomizer(String str) {
        return isSignalTile(str) || isBluetoothTile(str);
    }

    public static boolean isSignalTile(String str) {
        return isCellularTile(str) || isWifiTile(str) || isHotspot(str);
    }

    public static boolean isCellularTile(String str) {
        return getCellularSpec().equals(str);
    }

    public static boolean isWifiTile(String str) {
        return getWifiSpec().equals(str);
    }

    public static boolean isHotspot(String str) {
        return getHotspotSpec().equals(str);
    }

    public static boolean isBluetoothTile(String str) {
        return getBtSpec().equals(str);
    }

    public static List<String> addCircleSpecs(List<String> list) {
        removeCircleForSpecs(list);
        ArrayList arrayList = new ArrayList();
        arrayList.add(getCellularSpec());
        arrayList.add(getWifiSpec());
        arrayList.add(getHotspotSpec());
        arrayList.add(getBtSpec());
        arrayList.addAll(list);
        return arrayList;
    }

    public static List<String> removeCircleForSpecs(List<String> list) {
        if (list != null) {
            list.remove(getCellularSpec());
            list.remove(getWifiSpec());
            list.remove(getHotspotSpec());
            list.remove(getBtSpec());
        }
        return list;
    }

    public static List<TileQueryHelper.TileInfo> removeCircleForTileInfo(List<TileQueryHelper.TileInfo> list) {
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                TileQueryHelper.TileInfo tileInfo = list.get(i);
                if (tileInfo != null && shouldTileHideOnCustomizer(tileInfo.spec)) {
                    list.remove(i);
                    i--;
                }
                i++;
            }
        }
        return list;
    }

    private boolean dealWithUnexpectedTiles(String str) {
        boolean z = false;
        if (str == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split(",")) {
            arrayList.add(str2);
        }
        boolean remove = arrayList.remove(getInternetSpec());
        if (!remove) {
            if (!Objects.equals(Integer.valueOf(arrayList.indexOf(getCellularSpec())), Integer.valueOf(getCellExpectedIndexInVP())) || !Objects.equals(Integer.valueOf(arrayList.indexOf(getWifiSpec())), Integer.valueOf(getWifiExpectedIndexInVP())) || !Objects.equals(Integer.valueOf(arrayList.indexOf(getHotspotSpec())), Integer.valueOf(getHotspotExpectedIndexInVP())) || !Objects.equals(Integer.valueOf(arrayList.indexOf(getBtSpec())), Integer.valueOf(getBtExpectedIndexInVP()))) {
                z = true;
            }
            remove = z;
        }
        if (remove) {
            saveTilesToSettings(arrayList);
        }
        return remove;
    }
}
