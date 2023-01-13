package com.android.systemui.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import androidx.media.MediaBrowserServiceCompat;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.ResumeMediaBrowser;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t*\u0001\u001a\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002BA\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J%\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.2\u000e\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020100H\u0016¢\u0006\u0002\u00102J\u0010\u00103\u001a\u0002042\u0006\u00105\u001a\u00020!H\u0002J\b\u00106\u001a\u00020,H\u0002J\b\u00107\u001a\u00020,H\u0002J:\u00108\u001a\u00020,2\u0006\u00109\u001a\u0002012\b\u0010:\u001a\u0004\u0018\u0001012\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020$2\u0006\u0010>\u001a\u00020\u00132\u0006\u0010?\u001a\u00020$H\u0016J\u000e\u0010@\u001a\u00020,2\u0006\u0010A\u001a\u00020\u001dJ\u0018\u0010B\u001a\u00020,2\u0006\u00109\u001a\u0002012\u0006\u00105\u001a\u00020!H\u0002J\u0010\u0010C\u001a\u00020,2\u0006\u00105\u001a\u00020!H\u0002J\b\u0010D\u001a\u00020,H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\"\u0010\u0016\u001a\u0004\u0018\u00010\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0017\u0010\u0018R\u0010\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0004\n\u0002\u0010\u001bR\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX.¢\u0006\u0002\n\u0000R \u0010\u001e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\"0 0\u001fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010%\u001a\u00020&8\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b'\u0010(\u001a\u0004\b)\u0010*¨\u0006E"}, mo65043d2 = {"Lcom/android/systemui/media/MediaResumeListener;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "tunerService", "Lcom/android/systemui/tuner/TunerService;", "mediaBrowserFactory", "Lcom/android/systemui/media/ResumeMediaBrowserFactory;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "(Landroid/content/Context;Lcom/android/systemui/broadcast/BroadcastDispatcher;Ljava/util/concurrent/Executor;Lcom/android/systemui/tuner/TunerService;Lcom/android/systemui/media/ResumeMediaBrowserFactory;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/time/SystemClock;)V", "currentUserId", "", "value", "Lcom/android/systemui/media/ResumeMediaBrowser;", "mediaBrowser", "setMediaBrowser", "(Lcom/android/systemui/media/ResumeMediaBrowser;)V", "mediaBrowserCallback", "com/android/systemui/media/MediaResumeListener$mediaBrowserCallback$1", "Lcom/android/systemui/media/MediaResumeListener$mediaBrowserCallback$1;", "mediaDataManager", "Lcom/android/systemui/media/MediaDataManager;", "resumeComponents", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Lkotlin/Pair;", "Landroid/content/ComponentName;", "", "useMediaResumption", "", "userChangeReceiver", "Landroid/content/BroadcastReceiver;", "getUserChangeReceiver$annotations", "()V", "getUserChangeReceiver", "()Landroid/content/BroadcastReceiver;", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getResumeAction", "Ljava/lang/Runnable;", "componentName", "loadMediaResumptionControls", "loadSavedComponents", "onMediaDataLoaded", "key", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "immediately", "receivedSmartspaceCardLatency", "isSsReactivated", "setManager", "manager", "tryUpdateResumptionList", "updateResumptionList", "writeSharedPrefs", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener implements MediaDataManager.Listener, Dumpable {
    private final Executor backgroundExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public int currentUserId;
    private ResumeMediaBrowser mediaBrowser;
    private final MediaResumeListener$mediaBrowserCallback$1 mediaBrowserCallback;
    private final ResumeMediaBrowserFactory mediaBrowserFactory;
    /* access modifiers changed from: private */
    public MediaDataManager mediaDataManager;
    private final ConcurrentLinkedQueue<Pair<ComponentName, Long>> resumeComponents = new ConcurrentLinkedQueue<>();
    private final SystemClock systemClock;
    private final TunerService tunerService;
    /* access modifiers changed from: private */
    public boolean useMediaResumption;
    private final BroadcastReceiver userChangeReceiver;

    public static /* synthetic */ void getUserChangeReceiver$annotations() {
    }

    @Inject
    public MediaResumeListener(Context context2, BroadcastDispatcher broadcastDispatcher2, @Background Executor executor, TunerService tunerService2, ResumeMediaBrowserFactory resumeMediaBrowserFactory, DumpManager dumpManager, SystemClock systemClock2) {
        ResumeMediaBrowserFactory resumeMediaBrowserFactory2 = resumeMediaBrowserFactory;
        DumpManager dumpManager2 = dumpManager;
        SystemClock systemClock3 = systemClock2;
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(tunerService2, "tunerService");
        Intrinsics.checkNotNullParameter(resumeMediaBrowserFactory2, "mediaBrowserFactory");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock3, "systemClock");
        this.context = context2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.backgroundExecutor = executor;
        this.tunerService = tunerService2;
        this.mediaBrowserFactory = resumeMediaBrowserFactory2;
        this.systemClock = systemClock3;
        this.useMediaResumption = Utils.useMediaResumption(context2);
        this.currentUserId = context2.getUserId();
        BroadcastReceiver mediaResumeListener$userChangeReceiver$1 = new MediaResumeListener$userChangeReceiver$1(this);
        this.userChangeReceiver = mediaResumeListener$userChangeReceiver$1;
        this.mediaBrowserCallback = new MediaResumeListener$mediaBrowserCallback$1(this);
        if (this.useMediaResumption) {
            dumpManager2.registerDumpable("MediaResumeListener", this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, mediaResumeListener$userChangeReceiver$1, intentFilter, (Executor) null, UserHandle.ALL, 0, (String) null, 48, (Object) null);
            loadSavedComponents();
        }
    }

    /* access modifiers changed from: private */
    public final void setMediaBrowser(ResumeMediaBrowser resumeMediaBrowser) {
        ResumeMediaBrowser resumeMediaBrowser2 = this.mediaBrowser;
        if (resumeMediaBrowser2 != null) {
            resumeMediaBrowser2.disconnect();
        }
        this.mediaBrowser = resumeMediaBrowser;
    }

    public final BroadcastReceiver getUserChangeReceiver() {
        return this.userChangeReceiver;
    }

    public final void setManager(MediaDataManager mediaDataManager2) {
        Intrinsics.checkNotNullParameter(mediaDataManager2, "manager");
        this.mediaDataManager = mediaDataManager2;
        this.tunerService.addTunable(new MediaResumeListener$setManager$1(this), "qs_media_resumption");
    }

    /* access modifiers changed from: private */
    public final void loadSavedComponents() {
        long j;
        List<String> split;
        boolean z;
        this.resumeComponents.clear();
        boolean z2 = false;
        List<String> list = null;
        String string = this.context.getSharedPreferences("media_control_prefs", 0).getString("browser_components_" + this.currentUserId, (String) null);
        if (string != null && (split = new Regex(":").split(string, 0)) != null) {
            if (!split.isEmpty()) {
                ListIterator<String> listIterator = split.listIterator(split.size());
                while (true) {
                    if (!listIterator.hasPrevious()) {
                        break;
                    }
                    if (listIterator.previous().length() == 0) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (!z) {
                        list = CollectionsKt.take(split, listIterator.nextIndex() + 1);
                        break;
                    }
                }
            }
            list = CollectionsKt.emptyList();
        }
        if (list != null) {
            boolean z3 = false;
            for (String split$default : list) {
                List split$default2 = StringsKt.split$default((CharSequence) split$default, new String[]{"/"}, false, 0, 6, (Object) null);
                ComponentName componentName = new ComponentName((String) split$default2.get(0), (String) split$default2.get(1));
                if (split$default2.size() == 3) {
                    try {
                        j = Long.parseLong((String) split$default2.get(2));
                    } catch (NumberFormatException unused) {
                        j = this.systemClock.currentTimeMillis();
                    }
                } else {
                    j = this.systemClock.currentTimeMillis();
                    z3 = true;
                }
                this.resumeComponents.add(TuplesKt.m1802to(componentName, Long.valueOf(j)));
            }
            z2 = z3;
        }
        StringBuilder sb = new StringBuilder("loaded resume components ");
        String arrays = Arrays.toString(this.resumeComponents.toArray());
        Intrinsics.checkNotNullExpressionValue(arrays, "toString(this)");
        Log.d("MediaResumeListener", sb.append(arrays).toString());
        if (z2) {
            writeSharedPrefs();
        }
    }

    /* access modifiers changed from: private */
    public final void loadMediaResumptionControls() {
        if (this.useMediaResumption) {
            long currentTimeMillis = this.systemClock.currentTimeMillis();
            for (Pair pair : this.resumeComponents) {
                if (currentTimeMillis - ((Number) pair.getSecond()).longValue() <= MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT()) {
                    this.mediaBrowserFactory.create(this.mediaBrowserCallback, (ComponentName) pair.getFirst()).findRecentMedia();
                }
            }
        }
    }

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        List list;
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (this.useMediaResumption) {
            if (!str.equals(str2)) {
                setMediaBrowser((ResumeMediaBrowser) null);
            }
            if (mediaData.getResumeAction() == null && !mediaData.getHasCheckedForResume() && mediaData.isLocalSession()) {
                Log.d("MediaResumeListener", "Checking for service component for " + mediaData.getPackageName());
                List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(new Intent(MediaBrowserServiceCompat.SERVICE_INTERFACE), 0);
                if (queryIntentServices != null) {
                    Collection arrayList = new ArrayList();
                    for (Object next : queryIntentServices) {
                        if (Intrinsics.areEqual((Object) ((ResolveInfo) next).serviceInfo.packageName, (Object) mediaData.getPackageName())) {
                            arrayList.add(next);
                        }
                    }
                    list = (List) arrayList;
                } else {
                    list = null;
                }
                if (list == null || list.size() <= 0) {
                    MediaDataManager mediaDataManager2 = this.mediaDataManager;
                    if (mediaDataManager2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                        mediaDataManager2 = null;
                    }
                    mediaDataManager2.setResumeAction(str, (Runnable) null);
                    return;
                }
                this.backgroundExecutor.execute(new MediaResumeListener$$ExternalSyntheticLambda0(this, str, list));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onMediaDataLoaded$lambda-4  reason: not valid java name */
    public static final void m2818onMediaDataLoaded$lambda4(MediaResumeListener mediaResumeListener, String str, List list) {
        Intrinsics.checkNotNullParameter(mediaResumeListener, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        Intrinsics.checkNotNull(list);
        ComponentName componentName = ((ResolveInfo) list.get(0)).getComponentInfo().getComponentName();
        Intrinsics.checkNotNullExpressionValue(componentName, "inf!!.get(0).componentInfo.componentName");
        mediaResumeListener.tryUpdateResumptionList(str, componentName);
    }

    private final void tryUpdateResumptionList(String str, ComponentName componentName) {
        Log.d("MediaResumeListener", "Testing if we can connect to " + componentName);
        MediaDataManager mediaDataManager2 = this.mediaDataManager;
        if (mediaDataManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
            mediaDataManager2 = null;
        }
        mediaDataManager2.setResumeAction(str, (Runnable) null);
        setMediaBrowser(this.mediaBrowserFactory.create(new MediaResumeListener$tryUpdateResumptionList$1(componentName, this, str), componentName));
        ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
        if (resumeMediaBrowser != null) {
            resumeMediaBrowser.testConnection();
        }
    }

    /* access modifiers changed from: private */
    public final void updateResumptionList(ComponentName componentName) {
        Object obj;
        ConcurrentLinkedQueue<Pair<ComponentName, Long>> concurrentLinkedQueue = this.resumeComponents;
        Iterator it = concurrentLinkedQueue.iterator();
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
        this.resumeComponents.add(TuplesKt.m1802to(componentName, Long.valueOf(this.systemClock.currentTimeMillis())));
        if (this.resumeComponents.size() > 5) {
            this.resumeComponents.remove();
        }
        writeSharedPrefs();
    }

    private final void writeSharedPrefs() {
        StringBuilder sb = new StringBuilder();
        for (Pair pair : this.resumeComponents) {
            sb.append(((ComponentName) pair.getFirst()).flattenToString());
            sb.append("/");
            sb.append(((Number) pair.getSecond()).longValue());
            sb.append(":");
        }
        this.context.getSharedPreferences("media_control_prefs", 0).edit().putString("browser_components_" + this.currentUserId, sb.toString()).apply();
    }

    /* access modifiers changed from: private */
    public final Runnable getResumeAction(ComponentName componentName) {
        return new MediaResumeListener$$ExternalSyntheticLambda1(this, componentName);
    }

    /* access modifiers changed from: private */
    /* renamed from: getResumeAction$lambda-7  reason: not valid java name */
    public static final void m2817getResumeAction$lambda7(MediaResumeListener mediaResumeListener, ComponentName componentName) {
        Intrinsics.checkNotNullParameter(mediaResumeListener, "this$0");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        mediaResumeListener.setMediaBrowser(mediaResumeListener.mediaBrowserFactory.create((ResumeMediaBrowser.Callback) null, componentName));
        ResumeMediaBrowser resumeMediaBrowser = mediaResumeListener.mediaBrowser;
        if (resumeMediaBrowser != null) {
            resumeMediaBrowser.restart();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("resumeComponents: " + this.resumeComponents);
    }
}
