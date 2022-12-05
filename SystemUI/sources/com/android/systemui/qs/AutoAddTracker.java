package com.android.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AutoAddTracker.kt */
/* loaded from: classes.dex */
public final class AutoAddTracker implements UserAwareController, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final IntentFilter FILTER = new IntentFilter("android.os.action.SETTING_RESTORED");
    @NotNull
    private final Executor backgroundExecutor;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final AutoAddTracker$contentObserver$1 contentObserver;
    @NotNull
    private final DumpManager dumpManager;
    @Nullable
    private final Handler mainHandler;
    @NotNull
    private final QSHost qsHost;
    @Nullable
    private Set<String> restoredTiles;
    @NotNull
    private final SecureSettings secureSettings;
    private int userId;
    @NotNull
    private final ArraySet<String> autoAdded = new ArraySet<>();
    @NotNull
    private final AutoAddTracker$restoreReceiver$1 restoreReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.AutoAddTracker$restoreReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(@NotNull Context context, @NotNull Intent intent) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(intent, "intent");
            if (!Intrinsics.areEqual(intent.getAction(), "android.os.action.SETTING_RESTORED")) {
                return;
            }
            AutoAddTracker.this.processRestoreIntent(intent);
        }
    };

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.qs.AutoAddTracker$contentObserver$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.qs.AutoAddTracker$restoreReceiver$1] */
    public AutoAddTracker(@NotNull SecureSettings secureSettings, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull QSHost qsHost, @NotNull DumpManager dumpManager, @Nullable final Handler handler, @NotNull Executor backgroundExecutor, int i) {
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(qsHost, "qsHost");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        this.secureSettings = secureSettings;
        this.broadcastDispatcher = broadcastDispatcher;
        this.qsHost = qsHost;
        this.dumpManager = dumpManager;
        this.mainHandler = handler;
        this.backgroundExecutor = backgroundExecutor;
        this.userId = i;
        this.contentObserver = new ContentObserver(handler) { // from class: com.android.systemui.qs.AutoAddTracker$contentObserver$1
            public void onChange(boolean z, @NotNull Collection<? extends Uri> uris, int i2, int i3) {
                int i4;
                Intrinsics.checkNotNullParameter(uris, "uris");
                i4 = AutoAddTracker.this.userId;
                if (i3 != i4) {
                    return;
                }
                AutoAddTracker.this.loadTiles();
            }
        };
    }

    /* compiled from: AutoAddTracker.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0019, code lost:
        r11 = kotlin.text.StringsKt__StringsKt.split$default(r3, new java.lang.String[]{","}, false, 0, 6, null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void processRestoreIntent(Intent intent) {
        List plus;
        String tilesFromListLocked;
        List split$default;
        String stringExtra = intent.getStringExtra("setting_name");
        Unit unit = null;
        r2 = null;
        Set<String> set = null;
        List list = null;
        if (Intrinsics.areEqual(stringExtra, "sysui_qs_tiles")) {
            String stringExtra2 = intent.getStringExtra("new_value");
            if (stringExtra2 != null && split$default != null) {
                set = CollectionsKt___CollectionsKt.toSet(split$default);
            }
            if (set == null) {
                Log.w("AutoAddTracker", Intrinsics.stringPlus("Null restored tiles for user ", Integer.valueOf(this.userId)));
                set = SetsKt__SetsKt.emptySet();
            }
            this.restoredTiles = set;
        } else if (Intrinsics.areEqual(stringExtra, "qs_auto_tiles")) {
            Set<String> set2 = this.restoredTiles;
            if (set2 != null) {
                String stringExtra3 = intent.getStringExtra("new_value");
                List split$default2 = stringExtra3 == null ? null : StringsKt__StringsKt.split$default(stringExtra3, new String[]{","}, false, 0, 6, null);
                if (split$default2 == null) {
                    split$default2 = CollectionsKt__CollectionsKt.emptyList();
                }
                String stringExtra4 = intent.getStringExtra("previous_value");
                if (stringExtra4 != null) {
                    list = StringsKt__StringsKt.split$default(stringExtra4, new String[]{","}, false, 0, 6, null);
                }
                if (list == null) {
                    list = CollectionsKt__CollectionsKt.emptyList();
                }
                ArrayList arrayList = new ArrayList();
                for (Object obj : split$default2) {
                    if (!set2.contains((String) obj)) {
                        arrayList.add(obj);
                    }
                }
                if (!arrayList.isEmpty()) {
                    this.qsHost.removeTiles(arrayList);
                }
                synchronized (this.autoAdded) {
                    this.autoAdded.clear();
                    ArraySet<String> arraySet = this.autoAdded;
                    plus = CollectionsKt___CollectionsKt.plus((Collection) split$default2, (Iterable) list);
                    arraySet.addAll(plus);
                    tilesFromListLocked = getTilesFromListLocked();
                }
                saveTiles(tilesFromListLocked);
                unit = Unit.INSTANCE;
            }
            if (unit != null) {
                return;
            }
            Log.w("AutoAddTracker", Intrinsics.stringPlus("qs_auto_tiles restored before sysui_qs_tiles for user ", Integer.valueOf(this.userId)));
        }
    }

    public final void initialize() {
        this.dumpManager.registerDumpable("AutoAddTracker", this);
        loadTiles();
        SecureSettings secureSettings = this.secureSettings;
        secureSettings.registerContentObserverForUser(secureSettings.getUriFor("qs_auto_tiles"), this.contentObserver, -1);
        registerBroadcastReceiver();
    }

    private final void registerBroadcastReceiver() {
        this.broadcastDispatcher.registerReceiver(this.restoreReceiver, FILTER, this.backgroundExecutor, UserHandle.of(this.userId));
    }

    private final void unregisterBroadcastReceiver() {
        this.broadcastDispatcher.unregisterReceiver(this.restoreReceiver);
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(@NotNull UserHandle newUser) {
        Intrinsics.checkNotNullParameter(newUser, "newUser");
        if (newUser.getIdentifier() == this.userId) {
            return;
        }
        unregisterBroadcastReceiver();
        this.userId = newUser.getIdentifier();
        this.restoredTiles = null;
        loadTiles();
        registerBroadcastReceiver();
    }

    public final boolean isAdded(@NotNull String tile) {
        boolean contains;
        Intrinsics.checkNotNullParameter(tile, "tile");
        synchronized (this.autoAdded) {
            contains = this.autoAdded.contains(tile);
        }
        return contains;
    }

    public final void setTileAdded(@NotNull String tile) {
        String tilesFromListLocked;
        Intrinsics.checkNotNullParameter(tile, "tile");
        synchronized (this.autoAdded) {
            tilesFromListLocked = this.autoAdded.add(tile) ? getTilesFromListLocked() : null;
        }
        if (tilesFromListLocked == null) {
            return;
        }
        saveTiles(tilesFromListLocked);
    }

    public final void setTileRemoved(@NotNull String tile) {
        String tilesFromListLocked;
        Intrinsics.checkNotNullParameter(tile, "tile");
        synchronized (this.autoAdded) {
            tilesFromListLocked = this.autoAdded.remove(tile) ? getTilesFromListLocked() : null;
        }
        if (tilesFromListLocked == null) {
            return;
        }
        saveTiles(tilesFromListLocked);
    }

    private final String getTilesFromListLocked() {
        String join = TextUtils.join(",", this.autoAdded);
        Intrinsics.checkNotNullExpressionValue(join, "join(\",\", autoAdded)");
        return join;
    }

    private final void saveTiles(String str) {
        this.secureSettings.putStringForUser("qs_auto_tiles", str, null, false, this.userId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadTiles() {
        synchronized (this.autoAdded) {
            this.autoAdded.clear();
            this.autoAdded.addAll(getAdded());
        }
    }

    private final Collection<String> getAdded() {
        Set emptySet;
        String stringForUser = this.secureSettings.getStringForUser("qs_auto_tiles", this.userId);
        List split$default = stringForUser == null ? null : StringsKt__StringsKt.split$default(stringForUser, new String[]{","}, false, 0, 6, null);
        if (split$default == null) {
            emptySet = SetsKt__SetsKt.emptySet();
            return emptySet;
        }
        return split$default;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("Current user: ", Integer.valueOf(this.userId)));
        pw.println(Intrinsics.stringPlus("Added tiles: ", this.autoAdded));
    }

    /* compiled from: AutoAddTracker.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        @NotNull
        private final BroadcastDispatcher broadcastDispatcher;
        @NotNull
        private final DumpManager dumpManager;
        @NotNull
        private final Executor executor;
        @NotNull
        private final Handler handler;
        @NotNull
        private final QSHost qsHost;
        @NotNull
        private final SecureSettings secureSettings;
        private int userId;

        public Builder(@NotNull SecureSettings secureSettings, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull QSHost qsHost, @NotNull DumpManager dumpManager, @NotNull Handler handler, @NotNull Executor executor) {
            Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
            Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
            Intrinsics.checkNotNullParameter(qsHost, "qsHost");
            Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
            Intrinsics.checkNotNullParameter(handler, "handler");
            Intrinsics.checkNotNullParameter(executor, "executor");
            this.secureSettings = secureSettings;
            this.broadcastDispatcher = broadcastDispatcher;
            this.qsHost = qsHost;
            this.dumpManager = dumpManager;
            this.handler = handler;
            this.executor = executor;
        }

        @NotNull
        public final Builder setUserId(int i) {
            this.userId = i;
            return this;
        }

        @NotNull
        public final AutoAddTracker build() {
            return new AutoAddTracker(this.secureSettings, this.broadcastDispatcher, this.qsHost, this.dumpManager, this.handler, this.executor, this.userId);
        }
    }
}
