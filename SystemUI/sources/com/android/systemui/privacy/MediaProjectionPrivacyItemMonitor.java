package com.android.systemui.privacy;

import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.util.IndentingPrintWriter;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0002\u0017\u001a\b\u0007\u0018\u0000 52\u00020\u0001:\u00015B9\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\b\u0010\u001f\u001a\u00020 H\u0002J%\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020#2\u000e\u0010$\u001a\n\u0012\u0006\b\u0001\u0012\u00020&0%H\u0016¢\u0006\u0002\u0010'J\u000e\u0010(\u001a\b\u0012\u0004\u0012\u00020\u001e0)H\u0016J\u0018\u0010*\u001a\u00020 2\u0006\u0010+\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\u0012H\u0002J\u0010\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020/H\u0003J\u0010\u00100\u001a\u00020 2\u0006\u0010.\u001a\u00020/H\u0003J\u0010\u00101\u001a\u00020 2\u0006\u0010.\u001a\u00020/H\u0003J\b\u00102\u001a\u00020 H\u0003J\u0010\u00103\u001a\u00020 2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u00104\u001a\u00020 H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u0004\u0018\u00010\u00108\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00020\u00128\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u00020\u00128\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0004\n\u0002\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0004\n\u0002\u0010\u001bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d8\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u00066"}, mo64987d2 = {"Lcom/android/systemui/privacy/MediaProjectionPrivacyItemMonitor;", "Lcom/android/systemui/privacy/PrivacyItemMonitor;", "mediaProjectionManager", "Landroid/media/projection/MediaProjectionManager;", "packageManager", "Landroid/content/pm/PackageManager;", "privacyConfig", "Lcom/android/systemui/privacy/PrivacyConfig;", "bgHandler", "Landroid/os/Handler;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "logger", "Lcom/android/systemui/privacy/logging/PrivacyLogger;", "(Landroid/media/projection/MediaProjectionManager;Landroid/content/pm/PackageManager;Lcom/android/systemui/privacy/PrivacyConfig;Landroid/os/Handler;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/privacy/logging/PrivacyLogger;)V", "callback", "Lcom/android/systemui/privacy/PrivacyItemMonitor$Callback;", "listening", "", "lock", "", "mediaProjectionAvailable", "mediaProjectionCallback", "com/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1", "Lcom/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1;", "optionsCallback", "com/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$optionsCallback$1", "Lcom/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$optionsCallback$1;", "privacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "dispatchOnPrivacyItemsChanged", "", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getActivePrivacyItems", "", "logItemActive", "item", "active", "makePrivacyItem", "info", "Landroid/media/projection/MediaProjectionInfo;", "onMediaProjectionStartedLocked", "onMediaProjectionStoppedLocked", "setListeningStateLocked", "startListening", "stopListening", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor implements PrivacyItemMonitor {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final boolean DEBUG = false;
    public static final String TAG = "MediaProjectionPrivacyItemMonitor";
    private final Handler bgHandler;
    private PrivacyItemMonitor.Callback callback;
    private boolean listening;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    private final PrivacyLogger logger;
    /* access modifiers changed from: private */
    public boolean mediaProjectionAvailable;
    private final MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1 mediaProjectionCallback;
    private final MediaProjectionManager mediaProjectionManager;
    private final MediaProjectionPrivacyItemMonitor$optionsCallback$1 optionsCallback;
    private final PackageManager packageManager;
    /* access modifiers changed from: private */
    public final PrivacyConfig privacyConfig;
    private final List<PrivacyItem> privacyItems;
    private final SystemClock systemClock;

    @Inject
    public MediaProjectionPrivacyItemMonitor(MediaProjectionManager mediaProjectionManager2, PackageManager packageManager2, PrivacyConfig privacyConfig2, @Background Handler handler, SystemClock systemClock2, PrivacyLogger privacyLogger) {
        Intrinsics.checkNotNullParameter(mediaProjectionManager2, "mediaProjectionManager");
        Intrinsics.checkNotNullParameter(packageManager2, "packageManager");
        Intrinsics.checkNotNullParameter(privacyConfig2, "privacyConfig");
        Intrinsics.checkNotNullParameter(handler, "bgHandler");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(privacyLogger, "logger");
        this.mediaProjectionManager = mediaProjectionManager2;
        this.packageManager = packageManager2;
        this.privacyConfig = privacyConfig2;
        this.bgHandler = handler;
        this.systemClock = systemClock2;
        this.logger = privacyLogger;
        this.mediaProjectionAvailable = privacyConfig2.getMediaProjectionAvailable();
        this.privacyItems = new ArrayList();
        MediaProjectionPrivacyItemMonitor$optionsCallback$1 mediaProjectionPrivacyItemMonitor$optionsCallback$1 = new MediaProjectionPrivacyItemMonitor$optionsCallback$1(this);
        this.optionsCallback = mediaProjectionPrivacyItemMonitor$optionsCallback$1;
        this.mediaProjectionCallback = new MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1(this);
        privacyConfig2.addCallback((PrivacyConfig.Callback) mediaProjectionPrivacyItemMonitor$optionsCallback$1);
        setListeningStateLocked();
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$Companion;", "", "()V", "DEBUG", "", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaProjectionPrivacyItemMonitor.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void startListening(PrivacyItemMonitor.Callback callback2) {
        Intrinsics.checkNotNullParameter(callback2, "callback");
        synchronized (this.lock) {
            this.callback = callback2;
            Unit unit = Unit.INSTANCE;
        }
    }

    public void stopListening() {
        synchronized (this.lock) {
            this.callback = null;
            Unit unit = Unit.INSTANCE;
        }
    }

    /* access modifiers changed from: private */
    public final void onMediaProjectionStartedLocked(MediaProjectionInfo mediaProjectionInfo) {
        PrivacyItem makePrivacyItem = makePrivacyItem(mediaProjectionInfo);
        this.privacyItems.add(makePrivacyItem);
        logItemActive(makePrivacyItem, true);
    }

    /* access modifiers changed from: private */
    public final void onMediaProjectionStoppedLocked(MediaProjectionInfo mediaProjectionInfo) {
        PrivacyItem makePrivacyItem = makePrivacyItem(mediaProjectionInfo);
        List<PrivacyItem> list = this.privacyItems;
        Iterator<PrivacyItem> it = list.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (Intrinsics.areEqual((Object) it.next().getApplication(), (Object) makePrivacyItem.getApplication())) {
                break;
            } else {
                i++;
            }
        }
        list.remove(i);
        logItemActive(makePrivacyItem, false);
    }

    private final PrivacyItem makePrivacyItem(MediaProjectionInfo mediaProjectionInfo) {
        int packageUidAsUser = this.packageManager.getPackageUidAsUser(mediaProjectionInfo.getPackageName(), mediaProjectionInfo.getUserHandle().getIdentifier());
        String packageName = mediaProjectionInfo.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "info.packageName");
        return new PrivacyItem(PrivacyType.TYPE_MEDIA_PROJECTION, new PrivacyApplication(packageName, packageUidAsUser), this.systemClock.elapsedRealtime(), false, 8, (DefaultConstructorMarker) null);
    }

    private final void logItemActive(PrivacyItem privacyItem, boolean z) {
        this.logger.logUpdatedItemFromMediaProjection(privacyItem.getApplication().getUid(), privacyItem.getApplication().getPackageName(), z);
    }

    /* access modifiers changed from: private */
    public final void setListeningStateLocked() {
        boolean z = this.mediaProjectionAvailable;
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.mediaProjectionManager.addCallback(this.mediaProjectionCallback, this.bgHandler);
                MediaProjectionInfo activeProjectionInfo = this.mediaProjectionManager.getActiveProjectionInfo();
                if (activeProjectionInfo != null) {
                    onMediaProjectionStartedLocked(activeProjectionInfo);
                    dispatchOnPrivacyItemsChanged();
                    return;
                }
                return;
            }
            this.mediaProjectionManager.removeCallback(this.mediaProjectionCallback);
            for (PrivacyItem logItemActive : this.privacyItems) {
                logItemActive(logItemActive, false);
            }
            this.privacyItems.clear();
            dispatchOnPrivacyItemsChanged();
        }
    }

    public List<PrivacyItem> getActivePrivacyItems() {
        List<PrivacyItem> list;
        synchronized (this.lock) {
            list = CollectionsKt.toList(this.privacyItems);
        }
        return list;
    }

    /* access modifiers changed from: private */
    public final void dispatchOnPrivacyItemsChanged() {
        PrivacyItemMonitor.Callback callback2;
        synchronized (this.lock) {
            callback2 = this.callback;
        }
        if (callback2 != null) {
            this.bgHandler.post(new MediaProjectionPrivacyItemMonitor$$ExternalSyntheticLambda0(callback2));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchOnPrivacyItemsChanged$lambda-6  reason: not valid java name */
    public static final void m2864dispatchOnPrivacyItemsChanged$lambda6(PrivacyItemMonitor.Callback callback2) {
        callback2.onPrivacyItemsChanged();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("MediaProjectionPrivacyItemMonitor:");
        asIndenting.increaseIndent();
        try {
            synchronized (this.lock) {
                asIndenting.println("Listening: " + this.listening);
                asIndenting.println("mediaProjectionAvailable: " + this.mediaProjectionAvailable);
                asIndenting.println("Callback: " + this.callback);
                asIndenting.println("Privacy Items: " + this.privacyItems);
                Unit unit = Unit.INSTANCE;
            }
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }
}
