package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.ResumeMediaBrowser;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaResumeListener.kt */
/* loaded from: classes.dex */
public final class MediaResumeListener implements MediaDataManager.Listener, Dumpable {
    @NotNull
    private final Executor backgroundExecutor;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Context context;
    private int currentUserId;
    @Nullable
    private ResumeMediaBrowser mediaBrowser;
    @NotNull
    private final ResumeMediaBrowserFactory mediaBrowserFactory;
    private MediaDataManager mediaDataManager;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final TunerService tunerService;
    private boolean useMediaResumption;
    @NotNull
    private final BroadcastReceiver userChangeReceiver;
    @NotNull
    private final ConcurrentLinkedQueue<Pair<ComponentName, Long>> resumeComponents = new ConcurrentLinkedQueue<>();
    @NotNull
    private final MediaResumeListener$mediaBrowserCallback$1 mediaBrowserCallback = new ResumeMediaBrowser.Callback() { // from class: com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1
        @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
        public void addTrack(@NotNull MediaDescription desc, @NotNull ComponentName component, @NotNull ResumeMediaBrowser browser) {
            Context context;
            Runnable resumeAction;
            MediaDataManager mediaDataManager;
            int i;
            Intrinsics.checkNotNullParameter(desc, "desc");
            Intrinsics.checkNotNullParameter(component, "component");
            Intrinsics.checkNotNullParameter(browser, "browser");
            MediaSession.Token token = browser.getToken();
            PendingIntent appIntent = browser.getAppIntent();
            context = MediaResumeListener.this.context;
            PackageManager packageManager = context.getPackageManager();
            String packageName = component.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "component.packageName");
            resumeAction = MediaResumeListener.this.getResumeAction(component);
            try {
                CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(component.getPackageName(), 0));
                Intrinsics.checkNotNullExpressionValue(applicationLabel, "pm.getApplicationLabel(\n                        pm.getApplicationInfo(component.packageName, 0))");
                packageName = applicationLabel;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MediaResumeListener", "Error getting package information", e);
            }
            Log.d("MediaResumeListener", Intrinsics.stringPlus("Adding resume controls ", desc));
            mediaDataManager = MediaResumeListener.this.mediaDataManager;
            if (mediaDataManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                throw null;
            }
            i = MediaResumeListener.this.currentUserId;
            Intrinsics.checkNotNullExpressionValue(token, "token");
            String obj = packageName.toString();
            Intrinsics.checkNotNullExpressionValue(appIntent, "appIntent");
            String packageName2 = component.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName2, "component.packageName");
            mediaDataManager.addResumptionControls(i, desc, resumeAction, token, obj, appIntent, packageName2);
        }
    };

    @VisibleForTesting
    public static /* synthetic */ void getUserChangeReceiver$annotations() {
    }

    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.media.MediaResumeListener$mediaBrowserCallback$1] */
    public MediaResumeListener(@NotNull Context context, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Executor backgroundExecutor, @NotNull TunerService tunerService, @NotNull ResumeMediaBrowserFactory mediaBrowserFactory, @NotNull DumpManager dumpManager, @NotNull SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(mediaBrowserFactory, "mediaBrowserFactory");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        this.context = context;
        this.broadcastDispatcher = broadcastDispatcher;
        this.backgroundExecutor = backgroundExecutor;
        this.tunerService = tunerService;
        this.mediaBrowserFactory = mediaBrowserFactory;
        this.systemClock = systemClock;
        this.useMediaResumption = Utils.useMediaResumption(context);
        this.currentUserId = context.getUserId();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.media.MediaResumeListener$userChangeReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context2, @NotNull Intent intent) {
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (Intrinsics.areEqual("android.intent.action.USER_UNLOCKED", intent.getAction())) {
                    MediaResumeListener.this.loadMediaResumptionControls();
                } else if (!Intrinsics.areEqual("android.intent.action.USER_SWITCHED", intent.getAction())) {
                } else {
                    MediaResumeListener.this.currentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    MediaResumeListener.this.loadSavedComponents();
                }
            }
        };
        this.userChangeReceiver = broadcastReceiver;
        if (this.useMediaResumption) {
            dumpManager.registerDumpable("MediaResumeListener", this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, null, UserHandle.ALL);
            loadSavedComponents();
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull String str) {
        MediaDataManager.Listener.DefaultImpls.onMediaDataRemoved(this, str);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    public final void setManager(@NotNull MediaDataManager manager) {
        Intrinsics.checkNotNullParameter(manager, "manager");
        this.mediaDataManager = manager;
        this.tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.media.MediaResumeListener$setManager$1
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public void onTuningChanged(@Nullable String str, @Nullable String str2) {
                Context context;
                MediaDataManager mediaDataManager;
                boolean z;
                MediaResumeListener mediaResumeListener = MediaResumeListener.this;
                context = mediaResumeListener.context;
                mediaResumeListener.useMediaResumption = Utils.useMediaResumption(context);
                mediaDataManager = MediaResumeListener.this.mediaDataManager;
                if (mediaDataManager == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                    throw null;
                }
                z = MediaResumeListener.this.useMediaResumption;
                mediaDataManager.setMediaResumptionEnabled(z);
            }
        }, "qs_media_resumption");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadSavedComponents() {
        List split$default;
        long currentTimeMillis;
        List<String> split;
        boolean z;
        this.resumeComponents.clear();
        boolean z2 = false;
        List<String> list = null;
        String string = this.context.getSharedPreferences("media_control_prefs", 0).getString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), null);
        if (string != null && (split = new Regex(":").split(string, 0)) != null) {
            if (!split.isEmpty()) {
                ListIterator<String> listIterator = split.listIterator(split.size());
                while (listIterator.hasPrevious()) {
                    if (listIterator.previous().length() == 0) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (!z) {
                        list = CollectionsKt___CollectionsKt.take(split, listIterator.nextIndex() + 1);
                        break;
                    }
                }
            }
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        if (list != null) {
            boolean z3 = false;
            for (String str : list) {
                split$default = StringsKt__StringsKt.split$default(str, new String[]{"/"}, false, 0, 6, null);
                ComponentName componentName = new ComponentName((String) split$default.get(0), (String) split$default.get(1));
                if (split$default.size() == 3) {
                    try {
                        currentTimeMillis = Long.parseLong((String) split$default.get(2));
                    } catch (NumberFormatException unused) {
                        currentTimeMillis = this.systemClock.currentTimeMillis();
                    }
                    this.resumeComponents.add(TuplesKt.to(componentName, Long.valueOf(currentTimeMillis)));
                } else {
                    currentTimeMillis = this.systemClock.currentTimeMillis();
                }
                z3 = true;
                this.resumeComponents.add(TuplesKt.to(componentName, Long.valueOf(currentTimeMillis)));
            }
            z2 = z3;
        }
        String arrays = Arrays.toString(this.resumeComponents.toArray());
        Intrinsics.checkNotNullExpressionValue(arrays, "java.util.Arrays.toString(this)");
        Log.d("MediaResumeListener", Intrinsics.stringPlus("loaded resume components ", arrays));
        if (z2) {
            writeSharedPrefs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadMediaResumptionControls() {
        if (!this.useMediaResumption) {
            return;
        }
        long currentTimeMillis = this.systemClock.currentTimeMillis();
        Iterator<T> it = this.resumeComponents.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (currentTimeMillis - ((Number) pair.getSecond()).longValue() <= MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT()) {
                this.mediaBrowserFactory.create(this.mediaBrowserCallback, (ComponentName) pair.getFirst()).findRecentMedia();
            }
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull final String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
        final ArrayList arrayList;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        if (this.useMediaResumption) {
            if (!key.equals(str)) {
                ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
                if (resumeMediaBrowser != null) {
                    resumeMediaBrowser.disconnect();
                }
                this.mediaBrowser = null;
            }
            if (data.getResumeAction() != null || data.getHasCheckedForResume() || !data.isLocalSession()) {
                return;
            }
            Log.d("MediaResumeListener", Intrinsics.stringPlus("Checking for service component for ", data.getPackageName()));
            List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);
            if (queryIntentServices == null) {
                arrayList = null;
            } else {
                arrayList = new ArrayList();
                for (Object obj : queryIntentServices) {
                    if (Intrinsics.areEqual(((ResolveInfo) obj).serviceInfo.packageName, data.getPackageName())) {
                        arrayList.add(obj);
                    }
                }
            }
            if (arrayList != null && arrayList.size() > 0) {
                this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaResumeListener$onMediaDataLoaded$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaResumeListener mediaResumeListener = MediaResumeListener.this;
                        String str2 = key;
                        Intrinsics.checkNotNull(arrayList);
                        ComponentName componentName = arrayList.get(0).getComponentInfo().getComponentName();
                        Intrinsics.checkNotNullExpressionValue(componentName, "!!.get(0).componentInfo.componentName");
                        mediaResumeListener.tryUpdateResumptionList(str2, componentName);
                    }
                });
                return;
            }
            MediaDataManager mediaDataManager = this.mediaDataManager;
            if (mediaDataManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                throw null;
            }
            mediaDataManager.setResumeAction(key, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void tryUpdateResumptionList(final String str, final ComponentName componentName) {
        Log.d("MediaResumeListener", Intrinsics.stringPlus("Testing if we can connect to ", componentName));
        MediaDataManager mediaDataManager = this.mediaDataManager;
        if (mediaDataManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
            throw null;
        }
        mediaDataManager.setResumeAction(str, null);
        ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
        if (resumeMediaBrowser != null) {
            resumeMediaBrowser.disconnect();
        }
        ResumeMediaBrowser create = this.mediaBrowserFactory.create(new ResumeMediaBrowser.Callback() { // from class: com.android.systemui.media.MediaResumeListener$tryUpdateResumptionList$1
            @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
            public void onConnected() {
                Log.d("MediaResumeListener", Intrinsics.stringPlus("Connected to ", componentName));
            }

            @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
            public void onError() {
                Log.e("MediaResumeListener", Intrinsics.stringPlus("Cannot resume with ", componentName));
                this.mediaBrowser = null;
            }

            @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
            public void addTrack(@NotNull MediaDescription desc, @NotNull ComponentName component, @NotNull ResumeMediaBrowser browser) {
                MediaDataManager mediaDataManager2;
                Runnable resumeAction;
                Intrinsics.checkNotNullParameter(desc, "desc");
                Intrinsics.checkNotNullParameter(component, "component");
                Intrinsics.checkNotNullParameter(browser, "browser");
                Log.d("MediaResumeListener", Intrinsics.stringPlus("Can get resumable media from ", componentName));
                mediaDataManager2 = this.mediaDataManager;
                if (mediaDataManager2 != null) {
                    String str2 = str;
                    resumeAction = this.getResumeAction(componentName);
                    mediaDataManager2.setResumeAction(str2, resumeAction);
                    this.updateResumptionList(componentName);
                    this.mediaBrowser = null;
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                throw null;
            }
        }, componentName);
        this.mediaBrowser = create;
        if (create == null) {
            return;
        }
        create.testConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateResumptionList(ComponentName componentName) {
        Object obj;
        ConcurrentLinkedQueue<Pair<ComponentName, Long>> concurrentLinkedQueue = this.resumeComponents;
        Iterator<T> it = concurrentLinkedQueue.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((ComponentName) ((Pair) obj).getFirst()).equals(componentName)) {
                break;
            }
        }
        concurrentLinkedQueue.remove(obj);
        this.resumeComponents.add(TuplesKt.to(componentName, Long.valueOf(this.systemClock.currentTimeMillis())));
        if (this.resumeComponents.size() > 5) {
            this.resumeComponents.remove();
        }
        writeSharedPrefs();
    }

    private final void writeSharedPrefs() {
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = this.resumeComponents.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            sb.append(((ComponentName) pair.getFirst()).flattenToString());
            sb.append("/");
            sb.append(((Number) pair.getSecond()).longValue());
            sb.append(":");
        }
        this.context.getSharedPreferences("media_control_prefs", 0).edit().putString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), sb.toString()).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Runnable getResumeAction(final ComponentName componentName) {
        return new Runnable() { // from class: com.android.systemui.media.MediaResumeListener$getResumeAction$1
            @Override // java.lang.Runnable
            public final void run() {
                ResumeMediaBrowserFactory resumeMediaBrowserFactory;
                ResumeMediaBrowser resumeMediaBrowser;
                MediaResumeListener mediaResumeListener = MediaResumeListener.this;
                resumeMediaBrowserFactory = mediaResumeListener.mediaBrowserFactory;
                mediaResumeListener.mediaBrowser = resumeMediaBrowserFactory.create(null, componentName);
                resumeMediaBrowser = MediaResumeListener.this.mediaBrowser;
                if (resumeMediaBrowser == null) {
                    return;
                }
                resumeMediaBrowser.restart();
            }
        };
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("resumeComponents: ", this.resumeComponents));
    }
}
