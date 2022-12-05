package com.android.systemui.privacy;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.permission.PermGroupUsage;
import android.permission.PermissionManager;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ActivityStarter activityStarter;
    @NotNull
    private final AppOpsController appOpsController;
    @NotNull
    private final Executor backgroundExecutor;
    @Nullable
    private Dialog dialog;
    @NotNull
    private final DialogProvider dialogProvider;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @Nullable
    private ArrayMap<String, MicModeInfo> micModeInfos;
    @NotNull
    private final PrivacyDialogController$onDialogDismissed$1 onDialogDismissed;
    @NotNull
    private final PackageManager packageManager;
    @NotNull
    private final PermissionManager permissionManager;
    @NotNull
    private final PrivacyItemController privacyItemController;
    @NotNull
    private final PrivacyLogger privacyLogger;
    @NotNull
    private final UiEventLogger uiEventLogger;
    @NotNull
    private final Executor uiExecutor;
    @NotNull
    private final UserTracker userTracker;

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public interface DialogProvider {
        @NotNull
        PrivacyDialog makeDialog(@NotNull Context context, @NotNull List<PrivacyDialog.PrivacyElement> list, @NotNull Function2<? super String, ? super Integer, Unit> function2);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.privacy.PrivacyDialogController$onDialogDismissed$1] */
    public PrivacyDialogController(@NotNull PermissionManager permissionManager, @NotNull PackageManager packageManager, @NotNull PrivacyItemController privacyItemController, @NotNull UserTracker userTracker, @NotNull ActivityStarter activityStarter, @NotNull Executor backgroundExecutor, @NotNull Executor uiExecutor, @NotNull PrivacyLogger privacyLogger, @NotNull KeyguardStateController keyguardStateController, @NotNull AppOpsController appOpsController, @NotNull UiEventLogger uiEventLogger, @NotNull DialogProvider dialogProvider) {
        Intrinsics.checkNotNullParameter(permissionManager, "permissionManager");
        Intrinsics.checkNotNullParameter(packageManager, "packageManager");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyItemController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger, "privacyLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        Intrinsics.checkNotNullParameter(dialogProvider, "dialogProvider");
        this.permissionManager = permissionManager;
        this.packageManager = packageManager;
        this.privacyItemController = privacyItemController;
        this.userTracker = userTracker;
        this.activityStarter = activityStarter;
        this.backgroundExecutor = backgroundExecutor;
        this.uiExecutor = uiExecutor;
        this.privacyLogger = privacyLogger;
        this.keyguardStateController = keyguardStateController;
        this.appOpsController = appOpsController;
        this.uiEventLogger = uiEventLogger;
        this.dialogProvider = dialogProvider;
        this.onDialogDismissed = new PrivacyDialog.OnDialogDismissed() { // from class: com.android.systemui.privacy.PrivacyDialogController$onDialogDismissed$1
            @Override // com.android.systemui.privacy.PrivacyDialog.OnDialogDismissed
            public void onDialogDismissed() {
                PrivacyLogger privacyLogger2;
                UiEventLogger uiEventLogger2;
                privacyLogger2 = PrivacyDialogController.this.privacyLogger;
                privacyLogger2.logPrivacyDialogDismissed();
                uiEventLogger2 = PrivacyDialogController.this.uiEventLogger;
                uiEventLogger2.log(PrivacyDialogEvent.PRIVACY_DIALOG_DISMISSED);
                PrivacyDialogController.this.dialog = null;
            }
        };
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PrivacyDialogController(@NotNull PermissionManager permissionManager, @NotNull PackageManager packageManager, @NotNull PrivacyItemController privacyItemController, @NotNull UserTracker userTracker, @NotNull ActivityStarter activityStarter, @NotNull Executor backgroundExecutor, @NotNull Executor uiExecutor, @NotNull PrivacyLogger privacyLogger, @NotNull KeyguardStateController keyguardStateController, @NotNull AppOpsController appOpsController, @NotNull UiEventLogger uiEventLogger) {
        this(permissionManager, packageManager, privacyItemController, userTracker, activityStarter, backgroundExecutor, uiExecutor, privacyLogger, keyguardStateController, appOpsController, uiEventLogger, r13);
        PrivacyDialogControllerKt$defaultDialogProvider$1 privacyDialogControllerKt$defaultDialogProvider$1;
        Intrinsics.checkNotNullParameter(permissionManager, "permissionManager");
        Intrinsics.checkNotNullParameter(packageManager, "packageManager");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyItemController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger, "privacyLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        privacyDialogControllerKt$defaultDialogProvider$1 = PrivacyDialogControllerKt.defaultDialogProvider;
    }

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startActivity(String str, int i) {
        Dialog dialog;
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        this.uiEventLogger.log(PrivacyDialogEvent.PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS, i, str);
        this.privacyLogger.logStartSettingsActivityFromDialog(str, i);
        if (!this.keyguardStateController.isUnlocked() && (dialog = this.dialog) != null) {
            dialog.hide();
        }
        this.activityStarter.startActivity(intent, true, new ActivityStarter.Callback() { // from class: com.android.systemui.privacy.PrivacyDialogController$startActivity$1
            @Override // com.android.systemui.plugins.ActivityStarter.Callback
            public final void onActivityStarted(int i2) {
                Dialog dialog2;
                if (!ActivityManager.isStartResultSuccessful(i2)) {
                    dialog2 = PrivacyDialogController.this.dialog;
                    if (dialog2 == null) {
                        return;
                    }
                    dialog2.show();
                    return;
                }
                PrivacyDialogController.this.dismissDialog();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final List<PermGroupUsage> permGroupUsage() {
        List<PermGroupUsage> indicatorAppOpUsageData = this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
        Intrinsics.checkNotNullExpressionValue(indicatorAppOpUsageData, "permissionManager.getIndicatorAppOpUsageData(appOpsController.isMicMuted)");
        return indicatorAppOpUsageData;
    }

    public final void showDialog(@NotNull final Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        dismissDialog();
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyDialogController$showDialog$1
            @Override // java.lang.Runnable
            public final void run() {
                List<PermGroupUsage> permGroupUsage;
                UserTracker userTracker;
                PrivacyLogger privacyLogger;
                Executor executor;
                PrivacyType permGroupToPrivacyType;
                PrivacyType filterType;
                PrivacyDialog.PrivacyElement privacyElement;
                Object obj;
                CharSequence labelForPackage;
                ArrayMap arrayMap;
                boolean z;
                permGroupUsage = PrivacyDialogController.this.permGroupUsage();
                userTracker = PrivacyDialogController.this.userTracker;
                List<UserInfo> userProfiles = userTracker.getUserProfiles();
                privacyLogger = PrivacyDialogController.this.privacyLogger;
                privacyLogger.logUnfilteredPermGroupUsage(permGroupUsage);
                PrivacyDialogController privacyDialogController = PrivacyDialogController.this;
                final ArrayList arrayList = new ArrayList();
                for (PermGroupUsage permGroupUsage2 : permGroupUsage) {
                    String permGroupName = permGroupUsage2.getPermGroupName();
                    Intrinsics.checkNotNullExpressionValue(permGroupName, "it.permGroupName");
                    permGroupToPrivacyType = privacyDialogController.permGroupToPrivacyType(permGroupName);
                    filterType = privacyDialogController.filterType(permGroupToPrivacyType);
                    Iterator<T> it = userProfiles.iterator();
                    while (true) {
                        privacyElement = null;
                        if (!it.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it.next();
                        if (((UserInfo) obj).id == UserHandle.getUserId(permGroupUsage2.getUid())) {
                            z = true;
                            continue;
                        } else {
                            z = false;
                            continue;
                        }
                        if (z) {
                            break;
                        }
                    }
                    UserInfo userInfo = (UserInfo) obj;
                    if ((userInfo != null || permGroupUsage2.isPhoneCall()) && filterType != null) {
                        if (permGroupUsage2.isPhoneCall()) {
                            labelForPackage = "";
                        } else {
                            String packageName = permGroupUsage2.getPackageName();
                            Intrinsics.checkNotNullExpressionValue(packageName, "it.packageName");
                            labelForPackage = privacyDialogController.getLabelForPackage(packageName, permGroupUsage2.getUid());
                        }
                        String packageName2 = permGroupUsage2.getPackageName();
                        Intrinsics.checkNotNullExpressionValue(packageName2, "it.packageName");
                        int userId = UserHandle.getUserId(permGroupUsage2.getUid());
                        CharSequence attribution = permGroupUsage2.getAttribution();
                        long lastAccess = permGroupUsage2.getLastAccess();
                        boolean isActive = permGroupUsage2.isActive();
                        boolean isManagedProfile = userInfo == null ? false : userInfo.isManagedProfile();
                        boolean isPhoneCall = permGroupUsage2.isPhoneCall();
                        arrayMap = privacyDialogController.micModeInfos;
                        privacyElement = new PrivacyDialog.PrivacyElement(filterType, packageName2, userId, labelForPackage, attribution, lastAccess, isActive, isManagedProfile, isPhoneCall, arrayMap == null ? null : (PrivacyDialogController.MicModeInfo) arrayMap.get(permGroupUsage2.getPackageName()));
                    }
                    if (privacyElement != null) {
                        arrayList.add(privacyElement);
                    }
                }
                executor = PrivacyDialogController.this.uiExecutor;
                final PrivacyDialogController privacyDialogController2 = PrivacyDialogController.this;
                final Context context2 = context;
                executor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyDialogController$showDialog$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        List<PrivacyDialog.PrivacyElement> filterAndSelect;
                        PrivacyDialogController.DialogProvider dialogProvider;
                        PrivacyDialogController$onDialogDismissed$1 privacyDialogController$onDialogDismissed$1;
                        PrivacyLogger privacyLogger2;
                        filterAndSelect = PrivacyDialogController.this.filterAndSelect(arrayList);
                        if (!filterAndSelect.isEmpty()) {
                            dialogProvider = PrivacyDialogController.this.dialogProvider;
                            PrivacyDialog makeDialog = dialogProvider.makeDialog(context2, filterAndSelect, new PrivacyDialogController$showDialog$1$1$d$1(PrivacyDialogController.this));
                            makeDialog.setShowForAllUsers(true);
                            privacyDialogController$onDialogDismissed$1 = PrivacyDialogController.this.onDialogDismissed;
                            makeDialog.addOnDismissListener(privacyDialogController$onDialogDismissed$1);
                            makeDialog.show();
                            privacyLogger2 = PrivacyDialogController.this.privacyLogger;
                            privacyLogger2.logShowDialogContents(filterAndSelect);
                            PrivacyDialogController.this.dialog = makeDialog;
                            return;
                        }
                        Log.w("PrivacyDialogController", "Trying to show empty dialog");
                    }
                });
            }
        });
    }

    public final void dismissDialog() {
        Dialog dialog = this.dialog;
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CharSequence getLabelForPackage(String str, int i) {
        try {
            CharSequence loadLabel = this.packageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(i)).loadLabel(this.packageManager);
            Intrinsics.checkNotNullExpressionValue(loadLabel, "{\n            packageManager\n                    .getApplicationInfoAsUser(packageName, 0, UserHandle.getUserId(uid))\n                    .loadLabel(packageManager)\n        }");
            return loadLabel;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("PrivacyDialogController", Intrinsics.stringPlus("Label not found for: ", str));
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final PrivacyType permGroupToPrivacyType(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -1140935117) {
            if (hashCode != 828638019) {
                if (hashCode == 1581272376 && str.equals("android.permission-group.MICROPHONE")) {
                    return PrivacyType.TYPE_MICROPHONE;
                }
            } else if (str.equals("android.permission-group.LOCATION")) {
                return PrivacyType.TYPE_LOCATION;
            }
        } else if (str.equals("android.permission-group.CAMERA")) {
            return PrivacyType.TYPE_CAMERA;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final PrivacyType filterType(PrivacyType privacyType) {
        if (privacyType == null) {
            return null;
        }
        if (((privacyType != PrivacyType.TYPE_CAMERA && privacyType != PrivacyType.TYPE_MICROPHONE) || !this.privacyItemController.getMicCameraAvailable()) && (privacyType != PrivacyType.TYPE_LOCATION || !this.privacyItemController.getLocationAvailable())) {
            privacyType = null;
        }
        return privacyType;
    }

    public final void setMicModeInfo(@NotNull ArrayMap<String, MicModeInfo> infos) {
        Intrinsics.checkNotNullParameter(infos, "infos");
        this.micModeInfos = infos;
    }

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public static final class MicModeInfo {
        private int micMode;
        private boolean showUI;
        private boolean speakerMode;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MicModeInfo)) {
                return false;
            }
            MicModeInfo micModeInfo = (MicModeInfo) obj;
            return this.showUI == micModeInfo.showUI && this.micMode == micModeInfo.micMode && this.speakerMode == micModeInfo.speakerMode;
        }

        public int hashCode() {
            boolean z = this.showUI;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int hashCode = ((i2 * 31) + Integer.hashCode(this.micMode)) * 31;
            boolean z2 = this.speakerMode;
            if (!z2) {
                i = z2 ? 1 : 0;
            }
            return hashCode + i;
        }

        @NotNull
        public String toString() {
            return "MicModeInfo(showUI=" + this.showUI + ", micMode=" + this.micMode + ", speakerMode=" + this.speakerMode + ')';
        }

        public MicModeInfo(boolean z, int i, boolean z2) {
            this.showUI = z;
            this.micMode = i;
            this.speakerMode = z2;
        }

        public final int getMicMode() {
            return this.micMode;
        }

        public final boolean getShowUI() {
            return this.showUI;
        }

        public final boolean getSpeakerMode() {
            return this.speakerMode;
        }

        public final void setMicMode(int i) {
            this.micMode = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final List<PrivacyDialog.PrivacyElement> filterAndSelect(List<PrivacyDialog.PrivacyElement> list) {
        SortedMap sortedMap;
        Object next;
        List listOf;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            PrivacyType type = ((PrivacyDialog.PrivacyElement) obj).getType();
            Object obj2 = linkedHashMap.get(type);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(type, obj2);
            }
            ((List) obj2).add(obj);
        }
        sortedMap = MapsKt__MapsJVMKt.toSortedMap(linkedHashMap);
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : sortedMap.entrySet()) {
            List elements = (List) entry.getValue();
            Intrinsics.checkNotNullExpressionValue(elements, "elements");
            ArrayList arrayList2 = new ArrayList();
            for (Object obj3 : elements) {
                if (((PrivacyDialog.PrivacyElement) obj3).getActive()) {
                    arrayList2.add(obj3);
                }
            }
            if (!arrayList2.isEmpty()) {
                listOf = CollectionsKt___CollectionsKt.sortedWith(arrayList2, new Comparator<T>() { // from class: com.android.systemui.privacy.PrivacyDialogController$filterAndSelect$lambda-6$$inlined$sortedByDescending$1
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        int compareValues;
                        compareValues = ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((PrivacyDialog.PrivacyElement) t2).getLastActiveTimestamp()), Long.valueOf(((PrivacyDialog.PrivacyElement) t).getLastActiveTimestamp()));
                        return compareValues;
                    }
                });
            } else {
                Iterator it = elements.iterator();
                if (!it.hasNext()) {
                    next = null;
                } else {
                    next = it.next();
                    if (it.hasNext()) {
                        long lastActiveTimestamp = ((PrivacyDialog.PrivacyElement) next).getLastActiveTimestamp();
                        do {
                            Object next2 = it.next();
                            long lastActiveTimestamp2 = ((PrivacyDialog.PrivacyElement) next2).getLastActiveTimestamp();
                            if (lastActiveTimestamp < lastActiveTimestamp2) {
                                next = next2;
                                lastActiveTimestamp = lastActiveTimestamp2;
                            }
                        } while (it.hasNext());
                    }
                }
                PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) next;
                listOf = privacyElement == null ? null : CollectionsKt__CollectionsJVMKt.listOf(privacyElement);
                if (listOf == null) {
                    listOf = CollectionsKt__CollectionsKt.emptyList();
                }
            }
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, listOf);
        }
        return arrayList;
    }
}
