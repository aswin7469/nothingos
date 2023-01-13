package com.android.systemui.privacy;

import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.util.IndentingPrintWriter;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0002\u000e\u0013\b\u0007\u0018\u0000 <2\u00020\u0001:\u0001<B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010'\u001a\u00020(H\u0002J%\u0010)\u001a\u00020(2\u0006\u0010*\u001a\u00020+2\u000e\u0010,\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001d0-H\u0016¢\u0006\u0002\u0010.J\u000e\u0010/\u001a\b\u0012\u0004\u0012\u00020100H\u0016J\b\u00102\u001a\u00020(H\u0002J\u0010\u00103\u001a\u00020\u00162\u0006\u00104\u001a\u000205H\u0003J\b\u00106\u001a\u00020(H\u0003J\u0010\u00107\u001a\u00020(2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u00108\u001a\u00020(H\u0016J\u0012\u00109\u001a\u0004\u0018\u0001012\u0006\u0010:\u001a\u00020;H\u0003R\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00118\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0004\n\u0002\u0010\u0014R\u0012\u0010\u0015\u001a\u00020\u00168\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0017\u001a\u00020\u00168\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u001a\u001a\u00020\u00168\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R%\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u001d0\u001cj\n\u0012\u0006\u0012\u0004\u0018\u00010\u001d`\u001e¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010!\u001a\u00020\"8\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b#\u0010$\u001a\u0004\b%\u0010&¨\u0006="}, mo65043d2 = {"Lcom/android/systemui/privacy/AppOpsPrivacyItemMonitor;", "Lcom/android/systemui/privacy/PrivacyItemMonitor;", "appOpsController", "Lcom/android/systemui/appops/AppOpsController;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "privacyConfig", "Lcom/android/systemui/privacy/PrivacyConfig;", "bgExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "logger", "Lcom/android/systemui/privacy/logging/PrivacyLogger;", "(Lcom/android/systemui/appops/AppOpsController;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/privacy/PrivacyConfig;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/privacy/logging/PrivacyLogger;)V", "appOpsCallback", "com/android/systemui/privacy/AppOpsPrivacyItemMonitor$appOpsCallback$1", "Lcom/android/systemui/privacy/AppOpsPrivacyItemMonitor$appOpsCallback$1;", "callback", "Lcom/android/systemui/privacy/PrivacyItemMonitor$Callback;", "configCallback", "com/android/systemui/privacy/AppOpsPrivacyItemMonitor$configCallback$1", "Lcom/android/systemui/privacy/AppOpsPrivacyItemMonitor$configCallback$1;", "listening", "", "locationAvailable", "lock", "", "micCameraAvailable", "privacyCameraWhiteList", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getPrivacyCameraWhiteList", "()Ljava/util/ArrayList;", "userTrackerCallback", "Lcom/android/systemui/settings/UserTracker$Callback;", "getUserTrackerCallback$SystemUI_nothingRelease$annotations", "()V", "getUserTrackerCallback$SystemUI_nothingRelease", "()Lcom/android/systemui/settings/UserTracker$Callback;", "dispatchOnPrivacyItemsChanged", "", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getActivePrivacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "onCurrentProfilesChanged", "privacyItemForAppOpEnabledLocked", "code", "", "setListeningStateLocked", "startListening", "stopListening", "toPrivacyItemLocked", "appOpItem", "Lcom/android/systemui/appops/AppOpItem;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor implements PrivacyItemMonitor {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final int[] OPS;
    /* access modifiers changed from: private */
    public static final int[] OPS_LOCATION;
    /* access modifiers changed from: private */
    public static final int[] OPS_MIC_CAMERA;
    /* access modifiers changed from: private */
    public static final int[] USER_INDEPENDENT_OPS = {101, 100};
    private final AppOpsPrivacyItemMonitor$appOpsCallback$1 appOpsCallback;
    private final AppOpsController appOpsController;
    private final DelayableExecutor bgExecutor;
    private PrivacyItemMonitor.Callback callback;
    private final AppOpsPrivacyItemMonitor$configCallback$1 configCallback;
    private boolean listening;
    /* access modifiers changed from: private */
    public boolean locationAvailable;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    /* access modifiers changed from: private */
    public final PrivacyLogger logger;
    /* access modifiers changed from: private */
    public boolean micCameraAvailable;
    private final ArrayList<String> privacyCameraWhiteList;
    /* access modifiers changed from: private */
    public final PrivacyConfig privacyConfig;
    /* access modifiers changed from: private */
    public final UserTracker userTracker;
    private final UserTracker.Callback userTrackerCallback;

    public static /* synthetic */ void getUserTrackerCallback$SystemUI_nothingRelease$annotations() {
    }

    @Inject
    public AppOpsPrivacyItemMonitor(AppOpsController appOpsController2, UserTracker userTracker2, PrivacyConfig privacyConfig2, @Background DelayableExecutor delayableExecutor, PrivacyLogger privacyLogger) {
        Intrinsics.checkNotNullParameter(appOpsController2, "appOpsController");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(privacyConfig2, "privacyConfig");
        Intrinsics.checkNotNullParameter(delayableExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger, "logger");
        this.appOpsController = appOpsController2;
        this.userTracker = userTracker2;
        this.privacyConfig = privacyConfig2;
        this.bgExecutor = delayableExecutor;
        this.logger = privacyLogger;
        this.micCameraAvailable = privacyConfig2.getMicCameraAvailable();
        this.locationAvailable = privacyConfig2.getLocationAvailable();
        this.appOpsCallback = new AppOpsPrivacyItemMonitor$appOpsCallback$1(this);
        this.userTrackerCallback = new AppOpsPrivacyItemMonitor$userTrackerCallback$1(this);
        AppOpsPrivacyItemMonitor$configCallback$1 appOpsPrivacyItemMonitor$configCallback$1 = new AppOpsPrivacyItemMonitor$configCallback$1(this);
        this.configCallback = appOpsPrivacyItemMonitor$configCallback$1;
        privacyConfig2.addCallback((PrivacyConfig.Callback) appOpsPrivacyItemMonitor$configCallback$1);
        this.privacyCameraWhiteList = new AppOpsPrivacyItemMonitor$privacyCameraWhiteList$1();
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\t\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/privacy/AppOpsPrivacyItemMonitor$Companion;", "", "()V", "OPS", "", "getOPS", "()[I", "OPS_LOCATION", "getOPS_LOCATION", "OPS_MIC_CAMERA", "getOPS_MIC_CAMERA", "USER_INDEPENDENT_OPS", "getUSER_INDEPENDENT_OPS", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AppOpsPrivacyItemMonitor.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int[] getOPS_MIC_CAMERA() {
            return AppOpsPrivacyItemMonitor.OPS_MIC_CAMERA;
        }

        public final int[] getOPS_LOCATION() {
            return AppOpsPrivacyItemMonitor.OPS_LOCATION;
        }

        public final int[] getOPS() {
            return AppOpsPrivacyItemMonitor.OPS;
        }

        public final int[] getUSER_INDEPENDENT_OPS() {
            return AppOpsPrivacyItemMonitor.USER_INDEPENDENT_OPS;
        }
    }

    static {
        int[] iArr = {26, 101, 27, 100, 120};
        OPS_MIC_CAMERA = iArr;
        int[] iArr2 = {0, 1};
        OPS_LOCATION = iArr2;
        OPS = ArraysKt.plus(iArr, iArr2);
    }

    public final UserTracker.Callback getUserTrackerCallback$SystemUI_nothingRelease() {
        return this.userTrackerCallback;
    }

    public void startListening(PrivacyItemMonitor.Callback callback2) {
        Intrinsics.checkNotNullParameter(callback2, "callback");
        synchronized (this.lock) {
            this.callback = callback2;
            setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    public void stopListening() {
        synchronized (this.lock) {
            this.callback = null;
            setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    /* access modifiers changed from: private */
    public final void setListeningStateLocked() {
        boolean z = this.callback != null && (this.micCameraAvailable || this.locationAvailable);
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.appOpsController.addCallback(OPS, this.appOpsCallback);
                this.userTracker.addCallback(this.userTrackerCallback, this.bgExecutor);
                onCurrentProfilesChanged();
                return;
            }
            this.appOpsController.removeCallback(OPS, this.appOpsCallback);
            this.userTracker.removeCallback(this.userTrackerCallback);
        }
    }

    public List<PrivacyItem> getActivePrivacyItems() {
        List list;
        boolean z;
        boolean z2;
        List<AppOpItem> activeAppOps = this.appOpsController.getActiveAppOps(true);
        List<UserInfo> userProfiles = this.userTracker.getUserProfiles();
        synchronized (this.lock) {
            Intrinsics.checkNotNullExpressionValue(activeAppOps, "activeAppOps");
            Collection arrayList = new ArrayList();
            for (Object next : activeAppOps) {
                AppOpItem appOpItem = (AppOpItem) next;
                Iterable iterable = userProfiles;
                boolean z3 = false;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it = iterable.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (((UserInfo) it.next()).id == UserHandle.getUserId(appOpItem.getUid())) {
                            z2 = true;
                            continue;
                        } else {
                            z2 = false;
                            continue;
                        }
                        if (z2) {
                            z = true;
                            break;
                        }
                    }
                }
                z = false;
                if (z || ArraysKt.contains(USER_INDEPENDENT_OPS, appOpItem.getCode())) {
                    z3 = true;
                }
                if (z3) {
                    arrayList.add(next);
                }
            }
            Collection arrayList2 = new ArrayList();
            for (AppOpItem appOpItem2 : (List) arrayList) {
                Intrinsics.checkNotNullExpressionValue(appOpItem2, "it");
                PrivacyItem privacyItemLocked = toPrivacyItemLocked(appOpItem2);
                if (privacyItemLocked != null) {
                    arrayList2.add(privacyItemLocked);
                }
            }
            list = (List) arrayList2;
        }
        return CollectionsKt.distinct(list);
    }

    private final boolean privacyItemForAppOpEnabledLocked(int i) {
        if (ArraysKt.contains(OPS_LOCATION, i)) {
            return this.locationAvailable;
        }
        if (ArraysKt.contains(OPS_MIC_CAMERA, i)) {
            return this.micCameraAvailable;
        }
        return false;
    }

    private final PrivacyItem toPrivacyItemLocked(AppOpItem appOpItem) {
        PrivacyType privacyType;
        if (!privacyItemForAppOpEnabledLocked(appOpItem.getCode())) {
            return null;
        }
        int code = appOpItem.getCode();
        if (code == 0 || code == 1) {
            privacyType = PrivacyType.TYPE_LOCATION;
        } else {
            if (code != 26) {
                if (!(code == 27 || code == 100)) {
                    if (code != 101) {
                        if (code != 120) {
                            return null;
                        }
                    }
                }
                privacyType = PrivacyType.TYPE_MICROPHONE;
            }
            privacyType = PrivacyType.TYPE_CAMERA;
        }
        PrivacyType privacyType2 = privacyType;
        if (this.privacyCameraWhiteList.contains(appOpItem.getPackageName()) && privacyType2 == PrivacyType.TYPE_CAMERA) {
            return null;
        }
        String packageName = appOpItem.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "appOpItem.packageName");
        return new PrivacyItem(privacyType2, new PrivacyApplication(packageName, appOpItem.getUid()), appOpItem.getTimeStartedElapsed(), appOpItem.isDisabled());
    }

    public final ArrayList<String> getPrivacyCameraWhiteList() {
        return this.privacyCameraWhiteList;
    }

    /* access modifiers changed from: private */
    public final void onCurrentProfilesChanged() {
        Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(userProfiles, 10));
        for (UserInfo userInfo : userProfiles) {
            arrayList.add(Integer.valueOf(userInfo.id));
        }
        this.logger.logCurrentProfilesChanged((List) arrayList);
        dispatchOnPrivacyItemsChanged();
    }

    /* access modifiers changed from: private */
    public final void dispatchOnPrivacyItemsChanged() {
        PrivacyItemMonitor.Callback callback2;
        synchronized (this.lock) {
            callback2 = this.callback;
        }
        if (callback2 != null) {
            this.bgExecutor.execute(new AppOpsPrivacyItemMonitor$$ExternalSyntheticLambda0(callback2));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchOnPrivacyItemsChanged$lambda-8  reason: not valid java name */
    public static final void m2867dispatchOnPrivacyItemsChanged$lambda8(PrivacyItemMonitor.Callback callback2) {
        callback2.onPrivacyItemsChanged();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("AppOpsPrivacyItemMonitor:");
        asIndenting.increaseIndent();
        try {
            synchronized (this.lock) {
                asIndenting.println("Listening: " + this.listening);
                asIndenting.println("micCameraAvailable: " + this.micCameraAvailable);
                asIndenting.println("locationAvailable: " + this.locationAvailable);
                asIndenting.println("Callback: " + this.callback);
                Unit unit = Unit.INSTANCE;
            }
            StringBuilder append = new StringBuilder().append("Current user ids: ");
            Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo userInfo : userProfiles) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            asIndenting.println(append.append((Object) (List) arrayList).toString());
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }
}
