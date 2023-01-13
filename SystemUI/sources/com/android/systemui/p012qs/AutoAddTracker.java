package com.android.systemui.p012qs;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\b\u0006\n\u0002\b\u0003\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t*\u0002\u0016\u001c\u0018\u0000 =2\u00020\u00012\u00020\u0002:\u0002<=BA\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0006\u0010$\u001a\u00020!J%\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020'2\u000e\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140)H\u0016¢\u0006\u0002\u0010*J\u000e\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00140,H\u0002J\b\u0010-\u001a\u00020\u0014H\u0002J\u0006\u0010.\u001a\u00020!J\u000e\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020\u0014J\b\u00102\u001a\u00020!H\u0002J\u0010\u00103\u001a\u00020!2\u0006\u00104\u001a\u000205H\u0002J\b\u00106\u001a\u00020!H\u0002J\u0010\u00107\u001a\u00020!2\u0006\u00108\u001a\u00020\u0014H\u0002J\u000e\u00109\u001a\u00020!2\u0006\u00101\u001a\u00020\u0014J\u000e\u0010:\u001a\u00020!2\u0006\u00101\u001a\u00020\u0014J\b\u0010;\u001a\u00020!H\u0002R\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00138\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0004\n\u0002\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\u00108VX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0004\n\u0002\u0010\u001dR\u0016\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006>"}, mo65043d2 = {"Lcom/android/systemui/qs/AutoAddTracker;", "Lcom/android/systemui/util/UserAwareController;", "Lcom/android/systemui/Dumpable;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "qsHost", "Lcom/android/systemui/qs/QSHost;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "mainHandler", "Landroid/os/Handler;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "userId", "", "(Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/qs/QSHost;Lcom/android/systemui/dump/DumpManager;Landroid/os/Handler;Ljava/util/concurrent/Executor;I)V", "autoAdded", "Landroid/util/ArraySet;", "", "contentObserver", "com/android/systemui/qs/AutoAddTracker$contentObserver$1", "Lcom/android/systemui/qs/AutoAddTracker$contentObserver$1;", "currentUserId", "getCurrentUserId", "()I", "restoreReceiver", "com/android/systemui/qs/AutoAddTracker$restoreReceiver$1", "Lcom/android/systemui/qs/AutoAddTracker$restoreReceiver$1;", "restoredTiles", "", "changeUser", "", "newUser", "Landroid/os/UserHandle;", "destroy", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getAdded", "", "getTilesFromListLocked", "initialize", "isAdded", "", "tile", "loadTiles", "processRestoreIntent", "intent", "Landroid/content/Intent;", "registerBroadcastReceiver", "saveTiles", "tiles", "setTileAdded", "setTileRemoved", "unregisterBroadcastReceiver", "Builder", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.AutoAddTracker */
/* compiled from: AutoAddTracker.kt */
public final class AutoAddTracker implements UserAwareController, Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final IntentFilter FILTER = new IntentFilter("android.os.action.SETTING_RESTORED");
    private final ArraySet<String> autoAdded = new ArraySet<>();
    private final Executor backgroundExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    private final AutoAddTracker$contentObserver$1 contentObserver;
    private final DumpManager dumpManager;
    private final Handler mainHandler;
    private final QSHost qsHost;
    private final AutoAddTracker$restoreReceiver$1 restoreReceiver;
    private Set<String> restoredTiles;
    private final SecureSettings secureSettings;
    /* access modifiers changed from: private */
    public int userId;

    public AutoAddTracker(SecureSettings secureSettings2, BroadcastDispatcher broadcastDispatcher2, QSHost qSHost, DumpManager dumpManager2, Handler handler, Executor executor, int i) {
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(qSHost, "qsHost");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        this.secureSettings = secureSettings2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.qsHost = qSHost;
        this.dumpManager = dumpManager2;
        this.mainHandler = handler;
        this.backgroundExecutor = executor;
        this.userId = i;
        this.contentObserver = new AutoAddTracker$contentObserver$1(this, handler);
        this.restoreReceiver = new AutoAddTracker$restoreReceiver$1(this);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/qs/AutoAddTracker$Companion;", "", "()V", "FILTER", "Landroid/content/IntentFilter;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.AutoAddTracker$Companion */
    /* compiled from: AutoAddTracker.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public int getCurrentUserId() {
        return this.userId;
    }

    /* access modifiers changed from: private */
    public final void processRestoreIntent(Intent intent) {
        List list;
        List list2;
        String tilesFromListLocked;
        Set<String> set;
        List split$default;
        String stringExtra = intent.getStringExtra("setting_name");
        if (Intrinsics.areEqual((Object) stringExtra, (Object) QSTileHost.TILES_SETTING)) {
            String stringExtra2 = intent.getStringExtra("new_value");
            if (stringExtra2 == null || (split$default = StringsKt.split$default((CharSequence) stringExtra2, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) == null || (set = CollectionsKt.toSet(split$default)) == null) {
                AutoAddTracker autoAddTracker = this;
                Log.w("AutoAddTracker", "Null restored tiles for user " + this.userId);
                set = SetsKt.emptySet();
            }
            this.restoredTiles = set;
        } else if (Intrinsics.areEqual((Object) stringExtra, (Object) "qs_auto_tiles")) {
            Set<String> set2 = this.restoredTiles;
            if (set2 != null) {
                String stringExtra3 = intent.getStringExtra("new_value");
                if (stringExtra3 == null || (list = StringsKt.split$default((CharSequence) stringExtra3, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) == null) {
                    list = CollectionsKt.emptyList();
                }
                String stringExtra4 = intent.getStringExtra("previous_value");
                if (stringExtra4 == null || (list2 = StringsKt.split$default((CharSequence) stringExtra4, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) == null) {
                    list2 = CollectionsKt.emptyList();
                }
                Collection arrayList = new ArrayList();
                for (Object next : list) {
                    if (!set2.contains((String) next)) {
                        arrayList.add(next);
                    }
                }
                Collection collection = (List) arrayList;
                if (!collection.isEmpty()) {
                    this.qsHost.removeTiles(collection);
                }
                synchronized (this.autoAdded) {
                    this.autoAdded.clear();
                    this.autoAdded.addAll(CollectionsKt.plus(list, list2));
                    tilesFromListLocked = getTilesFromListLocked();
                }
                saveTiles(tilesFromListLocked);
                return;
            }
            AutoAddTracker autoAddTracker2 = this;
            Log.w("AutoAddTracker", "qs_auto_tiles restored before sysui_qs_tiles for user " + this.userId);
        }
    }

    public final void initialize() {
        this.dumpManager.registerDumpable("AutoAddTracker", this);
        loadTiles();
        SecureSettings secureSettings2 = this.secureSettings;
        secureSettings2.registerContentObserverForUser(secureSettings2.getUriFor("qs_auto_tiles"), (ContentObserver) this.contentObserver, -1);
        registerBroadcastReceiver();
    }

    public final void destroy() {
        this.dumpManager.unregisterDumpable("AutoAddTracker");
        this.secureSettings.unregisterContentObserver(this.contentObserver);
        unregisterBroadcastReceiver();
    }

    private final void registerBroadcastReceiver() {
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.restoreReceiver, FILTER, this.backgroundExecutor, UserHandle.of(this.userId), 0, (String) null, 48, (Object) null);
    }

    private final void unregisterBroadcastReceiver() {
        this.broadcastDispatcher.unregisterReceiver(this.restoreReceiver);
    }

    public void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
        if (userHandle.getIdentifier() != this.userId) {
            unregisterBroadcastReceiver();
            this.userId = userHandle.getIdentifier();
            this.restoredTiles = null;
            loadTiles();
            registerBroadcastReceiver();
        }
    }

    public final boolean isAdded(String str) {
        boolean contains;
        Intrinsics.checkNotNullParameter(str, "tile");
        synchronized (this.autoAdded) {
            contains = this.autoAdded.contains(str);
        }
        return contains;
    }

    public final void setTileAdded(String str) {
        String str2;
        Intrinsics.checkNotNullParameter(str, "tile");
        synchronized (this.autoAdded) {
            if (this.autoAdded.add(str)) {
                str2 = getTilesFromListLocked();
            } else {
                str2 = null;
                String str3 = null;
            }
        }
        if (str2 != null) {
            saveTiles(str2);
        }
    }

    public final void setTileRemoved(String str) {
        String str2;
        Intrinsics.checkNotNullParameter(str, "tile");
        synchronized (this.autoAdded) {
            if (this.autoAdded.remove(str)) {
                str2 = getTilesFromListLocked();
            } else {
                str2 = null;
                String str3 = null;
            }
        }
        if (str2 != null) {
            saveTiles(str2);
        }
    }

    private final String getTilesFromListLocked() {
        String join = TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.autoAdded);
        Intrinsics.checkNotNullExpressionValue(join, "join(\",\", autoAdded)");
        return join;
    }

    private final void saveTiles(String str) {
        this.secureSettings.putStringForUser("qs_auto_tiles", str, (String) null, false, this.userId, true);
    }

    /* access modifiers changed from: private */
    public final void loadTiles() {
        synchronized (this.autoAdded) {
            this.autoAdded.clear();
            this.autoAdded.addAll(getAdded());
        }
    }

    private final Collection<String> getAdded() {
        Collection<String> collection;
        String stringForUser = this.secureSettings.getStringForUser("qs_auto_tiles", this.userId);
        if (stringForUser == null || (collection = StringsKt.split$default((CharSequence) stringForUser, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null)) == null) {
            collection = SetsKt.emptySet();
        }
        return collection;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Current user: " + this.userId);
        printWriter.println("Added tiles: " + this.autoAdded);
    }

    @SysUISingleton
    @Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B;\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/qs/AutoAddTracker$Builder;", "", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "qsHost", "Lcom/android/systemui/qs/QSHost;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "handler", "Landroid/os/Handler;", "executor", "Ljava/util/concurrent/Executor;", "(Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/qs/QSHost;Lcom/android/systemui/dump/DumpManager;Landroid/os/Handler;Ljava/util/concurrent/Executor;)V", "userId", "", "build", "Lcom/android/systemui/qs/AutoAddTracker;", "setUserId", "_userId", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.AutoAddTracker$Builder */
    /* compiled from: AutoAddTracker.kt */
    public static final class Builder {
        private final BroadcastDispatcher broadcastDispatcher;
        private final DumpManager dumpManager;
        private final Executor executor;
        private final Handler handler;
        private final QSHost qsHost;
        private final SecureSettings secureSettings;
        private int userId;

        @Inject
        public Builder(SecureSettings secureSettings2, BroadcastDispatcher broadcastDispatcher2, QSHost qSHost, DumpManager dumpManager2, @Main Handler handler2, @Background Executor executor2) {
            Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
            Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
            Intrinsics.checkNotNullParameter(qSHost, "qsHost");
            Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
            Intrinsics.checkNotNullParameter(handler2, "handler");
            Intrinsics.checkNotNullParameter(executor2, "executor");
            this.secureSettings = secureSettings2;
            this.broadcastDispatcher = broadcastDispatcher2;
            this.qsHost = qSHost;
            this.dumpManager = dumpManager2;
            this.handler = handler2;
            this.executor = executor2;
        }

        public final Builder setUserId(int i) {
            this.userId = i;
            return this;
        }

        public final AutoAddTracker build() {
            return new AutoAddTracker(this.secureSettings, this.broadcastDispatcher, this.qsHost, this.dumpManager, this.handler, this.executor, this.userId);
        }
    }
}
