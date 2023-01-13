package com.android.systemui.privacy;

import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\r*\u0002%(\b\u0007\u0018\u0000 G2\u00020\u0001:\u0004FGHIBN\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0011\u0010\u0007\u001a\r\u0012\t\u0012\u00070\t¢\u0006\u0002\b\n0\b\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J\u000e\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0019J\u0016\u00105\u001a\u0002062\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018H\u0002J%\u00108\u001a\u0002062\u0006\u00109\u001a\u00020:2\u000e\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020=0<H\u0016¢\u0006\u0002\u0010>J\u001c\u0010?\u001a\b\u0012\u0004\u0012\u00020,0+2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020,0+H\u0002J\u000e\u0010A\u001a\u0002062\u0006\u00107\u001a\u00020\u0019J\u0016\u0010A\u001a\u0002062\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018H\u0002J\b\u0010B\u001a\u000206H\u0002J\b\u0010C\u001a\u000206H\u0002J\b\u0010D\u001a\u000206H\u0002J\u001b\u0010E\u001a\u00020\u0013*\u00020,2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020,0+H\u0004R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00180\u0017X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001c\u001a\u00060\u001dR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001f\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b \u0010\u0015R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010!\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\"\u0010\u0015R\u000e\u0010#\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u00020%X\u0004¢\u0006\u0004\n\u0002\u0010&R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u00020(X\u0004¢\u0006\u0004\n\u0002\u0010)R\u0019\u0010\u0007\u001a\r\u0012\t\u0012\u00070\t¢\u0006\u0002\b\n0\bX\u0004¢\u0006\u0002\n\u0000R8\u0010-\u001a\b\u0012\u0004\u0012\u00020,0+2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020,0+8@@@X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b.\u0010/\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000¨\u0006J"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemController;", "Lcom/android/systemui/Dumpable;", "uiExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "bgExecutor", "privacyConfig", "Lcom/android/systemui/privacy/PrivacyConfig;", "privacyItemMonitors", "", "Lcom/android/systemui/privacy/PrivacyItemMonitor;", "Lkotlin/jvm/JvmSuppressWildcards;", "logger", "Lcom/android/systemui/privacy/logging/PrivacyLogger;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/privacy/PrivacyConfig;Ljava/util/Set;Lcom/android/systemui/privacy/logging/PrivacyLogger;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/dump/DumpManager;)V", "allIndicatorsAvailable", "", "getAllIndicatorsAvailable", "()Z", "callbacks", "", "Ljava/lang/ref/WeakReference;", "Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "holdingRunnableCanceler", "Ljava/lang/Runnable;", "internalUiExecutor", "Lcom/android/systemui/privacy/PrivacyItemController$MyExecutor;", "listening", "locationAvailable", "getLocationAvailable", "micCameraAvailable", "getMicCameraAvailable", "notifyChanges", "optionsCallback", "com/android/systemui/privacy/PrivacyItemController$optionsCallback$1", "Lcom/android/systemui/privacy/PrivacyItemController$optionsCallback$1;", "privacyItemMonitorCallback", "com/android/systemui/privacy/PrivacyItemController$privacyItemMonitorCallback$1", "Lcom/android/systemui/privacy/PrivacyItemController$privacyItemMonitorCallback$1;", "<set-?>", "", "Lcom/android/systemui/privacy/PrivacyItem;", "privacyList", "getPrivacyList$SystemUI_nothingRelease$annotations", "()V", "getPrivacyList$SystemUI_nothingRelease", "()Ljava/util/List;", "setPrivacyList$SystemUI_nothingRelease", "(Ljava/util/List;)V", "updateListAndNotifyChanges", "addCallback", "", "callback", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "processNewList", "list", "removeCallback", "setListeningState", "update", "updatePrivacyList", "isIn", "Callback", "Companion", "MyExecutor", "NotifyChangesToCallback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String TAG = "PrivacyItemController";
    public static final long TIME_TO_HOLD_INDICATORS = 5000;
    private final DelayableExecutor bgExecutor;
    /* access modifiers changed from: private */
    public final List<WeakReference<Callback>> callbacks = new ArrayList();
    private Runnable holdingRunnableCanceler;
    private final MyExecutor internalUiExecutor;
    private boolean listening;
    private final PrivacyLogger logger;
    private final Runnable notifyChanges;
    private final PrivacyItemController$optionsCallback$1 optionsCallback;
    private final PrivacyConfig privacyConfig;
    private final PrivacyItemController$privacyItemMonitorCallback$1 privacyItemMonitorCallback;
    private final Set<PrivacyItemMonitor> privacyItemMonitors;
    private List<PrivacyItem> privacyList = CollectionsKt.emptyList();
    private final SystemClock systemClock;
    private final Runnable updateListAndNotifyChanges;

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0016\u0010\u0006\u001a\u00020\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "onFlagAllChanged", "", "flag", "", "onPrivacyItemsChanged", "privacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyItemController.kt */
    public interface Callback extends PrivacyConfig.Callback {
        @JvmDefault
        void onFlagAllChanged(boolean z) {
        }

        void onPrivacyItemsChanged(List<PrivacyItem> list);
    }

    public static /* synthetic */ void getPrivacyList$SystemUI_nothingRelease$annotations() {
    }

    @Inject
    public PrivacyItemController(@Main DelayableExecutor delayableExecutor, @Background DelayableExecutor delayableExecutor2, PrivacyConfig privacyConfig2, Set<PrivacyItemMonitor> set, PrivacyLogger privacyLogger, SystemClock systemClock2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(privacyConfig2, "privacyConfig");
        Intrinsics.checkNotNullParameter(set, "privacyItemMonitors");
        Intrinsics.checkNotNullParameter(privacyLogger, "logger");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.bgExecutor = delayableExecutor2;
        this.privacyConfig = privacyConfig2;
        this.privacyItemMonitors = set;
        this.logger = privacyLogger;
        this.systemClock = systemClock2;
        this.internalUiExecutor = new MyExecutor(this, delayableExecutor);
        this.notifyChanges = new PrivacyItemController$$ExternalSyntheticLambda1(this);
        this.updateListAndNotifyChanges = new PrivacyItemController$$ExternalSyntheticLambda2(this, delayableExecutor);
        PrivacyItemController$optionsCallback$1 privacyItemController$optionsCallback$1 = new PrivacyItemController$optionsCallback$1(this);
        this.optionsCallback = privacyItemController$optionsCallback$1;
        this.privacyItemMonitorCallback = new PrivacyItemController$privacyItemMonitorCallback$1(this);
        dumpManager.registerDumpable(TAG, this);
        privacyConfig2.addCallback((PrivacyConfig.Callback) privacyItemController$optionsCallback$1);
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\u00020\u00068\u0006XT¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0002¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemController$Companion;", "", "()V", "TAG", "", "TIME_TO_HOLD_INDICATORS", "", "getTIME_TO_HOLD_INDICATORS$annotations", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyItemController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getTIME_TO_HOLD_INDICATORS$annotations() {
        }

        private Companion() {
        }
    }

    public final synchronized List<PrivacyItem> getPrivacyList$SystemUI_nothingRelease() {
        return CollectionsKt.toList(this.privacyList);
    }

    public final synchronized void setPrivacyList$SystemUI_nothingRelease(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.privacyList = list;
    }

    public final boolean getMicCameraAvailable() {
        return this.privacyConfig.getMicCameraAvailable();
    }

    public final boolean getLocationAvailable() {
        return this.privacyConfig.getLocationAvailable();
    }

    public final boolean getAllIndicatorsAvailable() {
        return getMicCameraAvailable() && getLocationAvailable() && this.privacyConfig.getMediaProjectionAvailable();
    }

    /* access modifiers changed from: private */
    /* renamed from: notifyChanges$lambda-1  reason: not valid java name */
    public static final void m2881notifyChanges$lambda1(PrivacyItemController privacyItemController) {
        Intrinsics.checkNotNullParameter(privacyItemController, "this$0");
        List<PrivacyItem> privacyList$SystemUI_nothingRelease = privacyItemController.getPrivacyList$SystemUI_nothingRelease();
        for (WeakReference weakReference : privacyItemController.callbacks) {
            Callback callback = (Callback) weakReference.get();
            if (callback != null) {
                callback.onPrivacyItemsChanged(privacyList$SystemUI_nothingRelease);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateListAndNotifyChanges$lambda-2  reason: not valid java name */
    public static final void m2884updateListAndNotifyChanges$lambda2(PrivacyItemController privacyItemController, DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(privacyItemController, "this$0");
        Intrinsics.checkNotNullParameter(delayableExecutor, "$uiExecutor");
        privacyItemController.updatePrivacyList();
        delayableExecutor.execute(privacyItemController.notifyChanges);
    }

    /* access modifiers changed from: private */
    public final void update() {
        this.bgExecutor.execute(new PrivacyItemController$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: update$lambda-3  reason: not valid java name */
    public static final void m2883update$lambda3(PrivacyItemController privacyItemController) {
        Intrinsics.checkNotNullParameter(privacyItemController, "this$0");
        privacyItemController.updateListAndNotifyChanges.run();
    }

    /* access modifiers changed from: private */
    public final void setListeningState() {
        boolean z = !this.callbacks.isEmpty();
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                for (PrivacyItemMonitor startListening : this.privacyItemMonitors) {
                    startListening.startListening(this.privacyItemMonitorCallback);
                }
                update();
                return;
            }
            for (PrivacyItemMonitor stopListening : this.privacyItemMonitors) {
                stopListening.stopListening();
            }
            update();
        }
    }

    private final void addCallback(WeakReference<Callback> weakReference) {
        this.callbacks.add(weakReference);
        if ((!this.callbacks.isEmpty()) && !this.listening) {
            this.internalUiExecutor.updateListeningState();
        } else if (this.listening) {
            this.internalUiExecutor.execute(new NotifyChangesToCallback(weakReference.get(), getPrivacyList$SystemUI_nothingRelease()));
        }
    }

    private final void removeCallback(WeakReference<Callback> weakReference) {
        this.callbacks.removeIf(new PrivacyItemController$$ExternalSyntheticLambda0(weakReference));
        if (this.callbacks.isEmpty()) {
            this.internalUiExecutor.updateListeningState();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-6  reason: not valid java name */
    public static final boolean m2882removeCallback$lambda6(WeakReference weakReference, WeakReference weakReference2) {
        Intrinsics.checkNotNullParameter(weakReference, "$callback");
        Intrinsics.checkNotNullParameter(weakReference2, "it");
        Callback callback = (Callback) weakReference2.get();
        if (callback != null) {
            return callback.equals(weakReference.get());
        }
        return true;
    }

    public final void addCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        addCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    public final void removeCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    private final void updatePrivacyList() {
        Runnable runnable = this.holdingRunnableCanceler;
        if (runnable != null) {
            runnable.run();
            Unit unit = Unit.INSTANCE;
            this.holdingRunnableCanceler = null;
        }
        if (!this.listening) {
            this.privacyList = CollectionsKt.emptyList();
            return;
        }
        Collection arrayList = new ArrayList();
        for (PrivacyItemMonitor activePrivacyItems : this.privacyItemMonitors) {
            CollectionsKt.addAll(arrayList, activePrivacyItems.getActivePrivacyItems());
        }
        this.privacyList = processNewList(CollectionsKt.distinct((List) arrayList));
    }

    private final List<PrivacyItem> processNewList(List<PrivacyItem> list) {
        Object obj;
        this.logger.logRetrievedPrivacyItemsList(list);
        long elapsedRealtime = this.systemClock.elapsedRealtime() - 5000;
        Collection arrayList = new ArrayList();
        Iterator it = getPrivacyList$SystemUI_nothingRelease().iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            PrivacyItem privacyItem = (PrivacyItem) next;
            if (privacyItem.getTimeStampElapsed() <= elapsedRealtime || isIn(privacyItem, list)) {
                z = false;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        List list2 = (List) arrayList;
        if (!list2.isEmpty()) {
            this.logger.logPrivacyItemsToHold(list2);
            Iterator it2 = list2.iterator();
            if (!it2.hasNext()) {
                obj = null;
            } else {
                Object next2 = it2.next();
                if (!it2.hasNext()) {
                    obj = next2;
                } else {
                    long timeStampElapsed = ((PrivacyItem) next2).getTimeStampElapsed();
                    do {
                        Object next3 = it2.next();
                        long timeStampElapsed2 = ((PrivacyItem) next3).getTimeStampElapsed();
                        if (timeStampElapsed > timeStampElapsed2) {
                            next2 = next3;
                            timeStampElapsed = timeStampElapsed2;
                        }
                    } while (it2.hasNext());
                }
                obj = next2;
            }
            Intrinsics.checkNotNull(obj);
            long timeStampElapsed3 = ((PrivacyItem) obj).getTimeStampElapsed() - elapsedRealtime;
            this.logger.logPrivacyItemsUpdateScheduled(timeStampElapsed3);
            this.holdingRunnableCanceler = this.bgExecutor.executeDelayed(this.updateListAndNotifyChanges, timeStampElapsed3);
        }
        Collection arrayList2 = new ArrayList();
        for (Object next4 : list) {
            if (!((PrivacyItem) next4).getPaused()) {
                arrayList2.add(next4);
            }
        }
        return CollectionsKt.plus((List) arrayList2, list2);
    }

    private final boolean isIn(PrivacyItem privacyItem, List<PrivacyItem> list) {
        boolean z;
        Iterable<PrivacyItem> iterable = list;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (PrivacyItem privacyItem2 : iterable) {
            if (privacyItem2.getPrivacyType() == privacyItem.getPrivacyType() && Intrinsics.areEqual((Object) privacyItem2.getApplication(), (Object) privacyItem.getApplication()) && privacyItem2.getTimeStampElapsed() == privacyItem.getTimeStampElapsed()) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemController$NotifyChangesToCallback;", "Ljava/lang/Runnable;", "callback", "Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "list", "", "Lcom/android/systemui/privacy/PrivacyItem;", "(Lcom/android/systemui/privacy/PrivacyItemController$Callback;Ljava/util/List;)V", "run", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyItemController.kt */
    private static final class NotifyChangesToCallback implements Runnable {
        private final Callback callback;
        private final List<PrivacyItem> list;

        public NotifyChangesToCallback(Callback callback2, List<PrivacyItem> list2) {
            Intrinsics.checkNotNullParameter(list2, "list");
            this.callback = callback2;
            this.list = list2;
        }

        public void run() {
            Callback callback2 = this.callback;
            if (callback2 != null) {
                callback2.onPrivacyItemsChanged(this.list);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b5, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b9, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ba, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00be, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(java.p026io.PrintWriter r3, java.lang.String[] r4) {
        /*
            r2 = this;
            java.lang.String r0 = "Listening: "
            java.lang.String r1 = "pw"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r1)
            java.lang.String r1 = "args"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r1)
            android.util.IndentingPrintWriter r3 = com.android.systemui.util.DumpUtilsKt.asIndenting(r3)
            java.lang.String r1 = "PrivacyItemController state:"
            r3.println(r1)
            r3.increaseIndent()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bf }
            r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x00bf }
            boolean r0 = r2.listening     // Catch:{ all -> 0x00bf }
            java.lang.StringBuilder r0 = r1.append((boolean) r0)     // Catch:{ all -> 0x00bf }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00bf }
            r3.println(r0)     // Catch:{ all -> 0x00bf }
            java.lang.String r0 = "Privacy Items:"
            r3.println(r0)     // Catch:{ all -> 0x00bf }
            r3.increaseIndent()     // Catch:{ all -> 0x00bf }
            java.util.List r0 = r2.getPrivacyList$SystemUI_nothingRelease()     // Catch:{ all -> 0x00ba }
            java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ all -> 0x00ba }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00ba }
        L_0x003d:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00ba }
            if (r1 == 0) goto L_0x0051
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00ba }
            com.android.systemui.privacy.PrivacyItem r1 = (com.android.systemui.privacy.PrivacyItem) r1     // Catch:{ all -> 0x00ba }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00ba }
            r3.println(r1)     // Catch:{ all -> 0x00ba }
            goto L_0x003d
        L_0x0051:
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            java.lang.String r0 = "Callbacks:"
            r3.println(r0)     // Catch:{ all -> 0x00bf }
            r3.increaseIndent()     // Catch:{ all -> 0x00bf }
            java.util.List<java.lang.ref.WeakReference<com.android.systemui.privacy.PrivacyItemController$Callback>> r0 = r2.callbacks     // Catch:{ all -> 0x00b5 }
            java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ all -> 0x00b5 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00b5 }
        L_0x0064:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x0080
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00b5 }
            java.lang.ref.WeakReference r1 = (java.lang.ref.WeakReference) r1     // Catch:{ all -> 0x00b5 }
            java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x00b5 }
            com.android.systemui.privacy.PrivacyItemController$Callback r1 = (com.android.systemui.privacy.PrivacyItemController.Callback) r1     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x0064
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00b5 }
            r3.println(r1)     // Catch:{ all -> 0x00b5 }
            goto L_0x0064
        L_0x0080:
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            java.lang.String r0 = "PrivacyItemMonitors:"
            r3.println(r0)     // Catch:{ all -> 0x00bf }
            r3.increaseIndent()     // Catch:{ all -> 0x00bf }
            java.util.Set<com.android.systemui.privacy.PrivacyItemMonitor> r2 = r2.privacyItemMonitors     // Catch:{ all -> 0x00b0 }
            java.lang.Iterable r2 = (java.lang.Iterable) r2     // Catch:{ all -> 0x00b0 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x00b0 }
        L_0x0093:
            boolean r0 = r2.hasNext()     // Catch:{ all -> 0x00b0 }
            if (r0 == 0) goto L_0x00a6
            java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x00b0 }
            com.android.systemui.privacy.PrivacyItemMonitor r0 = (com.android.systemui.privacy.PrivacyItemMonitor) r0     // Catch:{ all -> 0x00b0 }
            r1 = r3
            java.io.PrintWriter r1 = (java.p026io.PrintWriter) r1     // Catch:{ all -> 0x00b0 }
            r0.dump(r1, r4)     // Catch:{ all -> 0x00b0 }
            goto L_0x0093
        L_0x00a6:
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            r3.decreaseIndent()
            r3.flush()
            return
        L_0x00b0:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            throw r2     // Catch:{ all -> 0x00bf }
        L_0x00b5:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            throw r2     // Catch:{ all -> 0x00bf }
        L_0x00ba:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00bf }
            throw r2     // Catch:{ all -> 0x00bf }
        L_0x00bf:
            r2 = move-exception
            r3.decreaseIndent()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyItemController.dump(java.io.PrintWriter, java.lang.String[]):void");
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006H\u0016J\u0006\u0010\n\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyItemController$MyExecutor;", "Ljava/util/concurrent/Executor;", "delegate", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Lcom/android/systemui/privacy/PrivacyItemController;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "listeningCanceller", "Ljava/lang/Runnable;", "execute", "", "command", "updateListeningState", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyItemController.kt */
    private final class MyExecutor implements Executor {
        private final DelayableExecutor delegate;
        private Runnable listeningCanceller;
        final /* synthetic */ PrivacyItemController this$0;

        public MyExecutor(PrivacyItemController privacyItemController, DelayableExecutor delayableExecutor) {
            Intrinsics.checkNotNullParameter(delayableExecutor, "delegate");
            this.this$0 = privacyItemController;
            this.delegate = delayableExecutor;
        }

        public void execute(Runnable runnable) {
            Intrinsics.checkNotNullParameter(runnable, DemoMode.EXTRA_COMMAND);
            this.delegate.execute(runnable);
        }

        public final void updateListeningState() {
            Runnable runnable = this.listeningCanceller;
            if (runnable != null) {
                runnable.run();
            }
            this.listeningCanceller = this.delegate.executeDelayed(new PrivacyItemController$MyExecutor$$ExternalSyntheticLambda0(this.this$0), 0);
        }

        /* access modifiers changed from: private */
        /* renamed from: updateListeningState$lambda-0  reason: not valid java name */
        public static final void m2885updateListeningState$lambda0(PrivacyItemController privacyItemController) {
            Intrinsics.checkNotNullParameter(privacyItemController, "this$0");
            privacyItemController.setListeningState();
        }
    }
}
