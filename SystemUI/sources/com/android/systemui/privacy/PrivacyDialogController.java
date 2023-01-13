package com.android.systemui.privacy;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.permission.PermissionGroupUsage;
import android.permission.PermissionManager;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000³\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001!\b\u0007\u0018\u0000 C2\u00020\u0001:\u0002CDBc\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016¢\u0006\u0002\u0010\u0017Bg\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\b\b\u0001\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\u0006\u0010#\u001a\u00020$J\u001c\u0010%\u001a\b\u0012\u0004\u0012\u00020'0&2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020'0&H\u0002J\u0014\u0010)\u001a\u0004\u0018\u00010*2\b\u0010+\u001a\u0004\u0018\u00010*H\u0002J\u0016\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201J\u0018\u00102\u001a\u0002032\u0006\u0010.\u001a\u00020/2\u0006\u00104\u001a\u000201H\u0003J2\u00105\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u0002012\u0006\u00106\u001a\u0002032\b\u00107\u001a\u0004\u0018\u0001032\u0006\u00108\u001a\u000209H\u0003J\u0012\u0010:\u001a\u0004\u0018\u00010*2\u0006\u0010;\u001a\u00020/H\u0002J\u000e\u0010<\u001a\b\u0012\u0004\u0012\u00020=0&H\u0003J\u000e\u0010>\u001a\u00020$2\u0006\u0010?\u001a\u00020@J,\u0010A\u001a\u00020$2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u0002012\b\u00107\u001a\u0004\u0018\u0001032\b\u0010B\u001a\u0004\u0018\u00010-H\u0003R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u001d\u001a\n \u001f*\u0004\u0018\u00010\u001e0\u001eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u00020!X\u0004¢\u0006\u0004\n\u0002\u0010\"R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006E"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialogController;", "", "permissionManager", "Landroid/permission/PermissionManager;", "packageManager", "Landroid/content/pm/PackageManager;", "privacyItemController", "Lcom/android/systemui/privacy/PrivacyItemController;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "uiExecutor", "privacyLogger", "Lcom/android/systemui/privacy/logging/PrivacyLogger;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "appOpsController", "Lcom/android/systemui/appops/AppOpsController;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "(Landroid/permission/PermissionManager;Landroid/content/pm/PackageManager;Lcom/android/systemui/privacy/PrivacyItemController;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/plugins/ActivityStarter;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/privacy/logging/PrivacyLogger;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/appops/AppOpsController;Lcom/android/internal/logging/UiEventLogger;)V", "dialogProvider", "Lcom/android/systemui/privacy/PrivacyDialogController$DialogProvider;", "(Landroid/permission/PermissionManager;Landroid/content/pm/PackageManager;Lcom/android/systemui/privacy/PrivacyItemController;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/plugins/ActivityStarter;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/privacy/logging/PrivacyLogger;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/appops/AppOpsController;Lcom/android/internal/logging/UiEventLogger;Lcom/android/systemui/privacy/PrivacyDialogController$DialogProvider;)V", "dialog", "Landroid/app/Dialog;", "mEx", "Lcom/nothing/systemui/privacy/PrivacyDialogControllerEx;", "kotlin.jvm.PlatformType", "onDialogDismissed", "com/android/systemui/privacy/PrivacyDialogController$onDialogDismissed$1", "Lcom/android/systemui/privacy/PrivacyDialogController$onDialogDismissed$1;", "dismissDialog", "", "filterAndSelect", "", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "list", "filterType", "Lcom/android/systemui/privacy/PrivacyType;", "type", "getDefaultManageAppPermissionsIntent", "Landroid/content/Intent;", "packageName", "", "userId", "", "getLabelForPackage", "", "uid", "getManagePermissionIntent", "permGroupName", "attributionTag", "isAttributionSupported", "", "permGroupToPrivacyType", "group", "permGroupUsage", "Landroid/permission/PermissionGroupUsage;", "showDialog", "context", "Landroid/content/Context;", "startActivity", "navigationIntent", "Companion", "DialogProvider", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "PrivacyDialogController";
    private final ActivityStarter activityStarter;
    private final AppOpsController appOpsController;
    private final Executor backgroundExecutor;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private final DialogProvider dialogProvider;
    private final KeyguardStateController keyguardStateController;
    private PrivacyDialogControllerEx mEx;
    private final PrivacyDialogController$onDialogDismissed$1 onDialogDismissed;
    private final PackageManager packageManager;
    private final PermissionManager permissionManager;
    private final PrivacyItemController privacyItemController;
    /* access modifiers changed from: private */
    public final PrivacyLogger privacyLogger;
    /* access modifiers changed from: private */
    public final UiEventLogger uiEventLogger;
    private final Executor uiExecutor;
    private final UserTracker userTracker;

    @Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001JH\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072(\u0010\t\u001a$\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u000f0\nH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialogController$DialogProvider;", "", "makeDialog", "Lcom/android/systemui/privacy/PrivacyDialog;", "context", "Landroid/content/Context;", "list", "", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "starter", "Lkotlin/Function4;", "", "", "", "Landroid/content/Intent;", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDialogController.kt */
    public interface DialogProvider {
        PrivacyDialog makeDialog(Context context, List<PrivacyDialog.PrivacyElement> list, Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4);
    }

    public PrivacyDialogController(PermissionManager permissionManager2, PackageManager packageManager2, PrivacyItemController privacyItemController2, UserTracker userTracker2, ActivityStarter activityStarter2, Executor executor, Executor executor2, PrivacyLogger privacyLogger2, KeyguardStateController keyguardStateController2, AppOpsController appOpsController2, UiEventLogger uiEventLogger2, DialogProvider dialogProvider2) {
        Intrinsics.checkNotNullParameter(permissionManager2, "permissionManager");
        Intrinsics.checkNotNullParameter(packageManager2, "packageManager");
        Intrinsics.checkNotNullParameter(privacyItemController2, "privacyItemController");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger2, "privacyLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(appOpsController2, "appOpsController");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(dialogProvider2, "dialogProvider");
        this.permissionManager = permissionManager2;
        this.packageManager = packageManager2;
        this.privacyItemController = privacyItemController2;
        this.userTracker = userTracker2;
        this.activityStarter = activityStarter2;
        this.backgroundExecutor = executor;
        this.uiExecutor = executor2;
        this.privacyLogger = privacyLogger2;
        this.keyguardStateController = keyguardStateController2;
        this.appOpsController = appOpsController2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogProvider = dialogProvider2;
        this.mEx = (PrivacyDialogControllerEx) NTDependencyEx.get(PrivacyDialogControllerEx.class);
        this.onDialogDismissed = new PrivacyDialogController$onDialogDismissed$1(this);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public PrivacyDialogController(android.permission.PermissionManager r15, android.content.pm.PackageManager r16, com.android.systemui.privacy.PrivacyItemController r17, com.android.systemui.settings.UserTracker r18, com.android.systemui.plugins.ActivityStarter r19, @com.android.systemui.dagger.qualifiers.Background java.util.concurrent.Executor r20, @com.android.systemui.dagger.qualifiers.Main java.util.concurrent.Executor r21, com.android.systemui.privacy.logging.PrivacyLogger r22, com.android.systemui.statusbar.policy.KeyguardStateController r23, com.android.systemui.appops.AppOpsController r24, com.android.internal.logging.UiEventLogger r25) {
        /*
            r14 = this;
            java.lang.String r0 = "permissionManager"
            r2 = r15
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r0)
            java.lang.String r0 = "packageManager"
            r3 = r16
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "privacyItemController"
            r4 = r17
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.String r0 = "userTracker"
            r5 = r18
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "activityStarter"
            r6 = r19
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "backgroundExecutor"
            r7 = r20
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.String r0 = "uiExecutor"
            r8 = r21
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            java.lang.String r0 = "privacyLogger"
            r9 = r22
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            java.lang.String r0 = "keyguardStateController"
            r10 = r23
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            java.lang.String r0 = "appOpsController"
            r11 = r24
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "uiEventLogger"
            r12 = r25
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            com.android.systemui.privacy.PrivacyDialogControllerKt$defaultDialogProvider$1 r0 = com.android.systemui.privacy.PrivacyDialogControllerKt.access$getDefaultDialogProvider$p()
            r13 = r0
            com.android.systemui.privacy.PrivacyDialogController$DialogProvider r13 = (com.android.systemui.privacy.PrivacyDialogController.DialogProvider) r13
            r1 = r14
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyDialogController.<init>(android.permission.PermissionManager, android.content.pm.PackageManager, com.android.systemui.privacy.PrivacyItemController, com.android.systemui.settings.UserTracker, com.android.systemui.plugins.ActivityStarter, java.util.concurrent.Executor, java.util.concurrent.Executor, com.android.systemui.privacy.logging.PrivacyLogger, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.appops.AppOpsController, com.android.internal.logging.UiEventLogger):void");
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyDialogController$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDialogController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void startActivity(String str, int i, CharSequence charSequence, Intent intent) {
        Dialog dialog2;
        if (intent == null) {
            intent = getDefaultManageAppPermissionsIntent(str, i);
        }
        this.uiEventLogger.log(PrivacyDialogEvent.PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS, i, str);
        this.privacyLogger.logStartSettingsActivityFromDialog(str, i);
        if (!this.keyguardStateController.isUnlocked() && (dialog2 = this.dialog) != null) {
            dialog2.hide();
        }
        this.activityStarter.startActivity(intent, true, (ActivityStarter.Callback) new PrivacyDialogController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: startActivity$lambda-0  reason: not valid java name */
    public static final void m2879startActivity$lambda0(PrivacyDialogController privacyDialogController, int i) {
        Intrinsics.checkNotNullParameter(privacyDialogController, "this$0");
        if (ActivityManager.isStartResultSuccessful(i)) {
            privacyDialogController.dismissDialog();
            return;
        }
        Dialog dialog2 = privacyDialogController.dialog;
        if (dialog2 != null) {
            dialog2.show();
        }
    }

    private final Intent getManagePermissionIntent(String str, int i, CharSequence charSequence, CharSequence charSequence2, boolean z) {
        if (charSequence2 != null && z) {
            Intent intent = new Intent("android.intent.action.MANAGE_PERMISSION_USAGE");
            intent.setPackage(str);
            intent.putExtra("android.intent.extra.PERMISSION_GROUP_NAME", charSequence.toString());
            intent.putExtra("android.intent.extra.ATTRIBUTION_TAGS", new String[]{charSequence2.toString()});
            intent.putExtra("android.intent.extra.SHOWING_ATTRIBUTION", true);
            ResolveInfo resolveActivity = this.packageManager.resolveActivity(intent, PackageManager.ResolveInfoFlags.of(0));
            if (!(resolveActivity == null || resolveActivity.activityInfo == null || !Intrinsics.areEqual((Object) resolveActivity.activityInfo.permission, (Object) "android.permission.START_VIEW_PERMISSION_USAGE"))) {
                intent.setComponent(new ComponentName(str, resolveActivity.activityInfo.name));
                return intent;
            }
        }
        return getDefaultManageAppPermissionsIntent(str, i);
    }

    public final Intent getDefaultManageAppPermissionsIntent(String str, int i) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        return intent;
    }

    private final List<PermissionGroupUsage> permGroupUsage() {
        List<PermissionGroupUsage> indicatorAppOpUsageData = this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
        Intrinsics.checkNotNullExpressionValue(indicatorAppOpUsageData, "permissionManager.getInd…OpsController.isMicMuted)");
        return indicatorAppOpUsageData;
    }

    public final void showDialog(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        dismissDialog();
        this.backgroundExecutor.execute(new PrivacyDialogController$$ExternalSyntheticLambda1(this, context));
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-5  reason: not valid java name */
    public static final void m2877showDialog$lambda5(PrivacyDialogController privacyDialogController, Context context) {
        PrivacyDialog.PrivacyElement privacyElement;
        Object obj;
        CharSequence charSequence;
        boolean z;
        PrivacyDialogController privacyDialogController2 = privacyDialogController;
        Context context2 = context;
        Intrinsics.checkNotNullParameter(privacyDialogController2, "this$0");
        Intrinsics.checkNotNullParameter(context2, "$context");
        List<PermissionGroupUsage> permGroupUsage = privacyDialogController.permGroupUsage();
        List<UserInfo> userProfiles = privacyDialogController2.userTracker.getUserProfiles();
        privacyDialogController2.privacyLogger.logUnfilteredPermGroupUsage(permGroupUsage);
        Collection arrayList = new ArrayList();
        for (PermissionGroupUsage permissionGroupUsage : permGroupUsage) {
            String permissionGroupName = permissionGroupUsage.getPermissionGroupName();
            Intrinsics.checkNotNullExpressionValue(permissionGroupName, "it.permissionGroupName");
            PrivacyType filterType = privacyDialogController2.filterType(privacyDialogController2.permGroupToPrivacyType(permissionGroupName));
            Iterator it = userProfiles.iterator();
            while (true) {
                privacyElement = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (((UserInfo) obj).id == UserHandle.getUserId(permissionGroupUsage.getUid())) {
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
            if (userInfo == null && !permissionGroupUsage.isPhoneCall()) {
                PrivacyDialog.PrivacyElement privacyElement2 = null;
            } else if (filterType != null) {
                if (!permissionGroupUsage.isPhoneCall()) {
                    String packageName = permissionGroupUsage.getPackageName();
                    Intrinsics.checkNotNullExpressionValue(packageName, "it.packageName");
                    charSequence = privacyDialogController2.getLabelForPackage(packageName, permissionGroupUsage.getUid());
                }
                CharSequence charSequence2 = charSequence;
                int userId = UserHandle.getUserId(permissionGroupUsage.getUid());
                String packageName2 = permissionGroupUsage.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName2, "it.packageName");
                CharSequence attributionTag = permissionGroupUsage.getAttributionTag();
                CharSequence attributionLabel = permissionGroupUsage.getAttributionLabel();
                CharSequence proxyLabel = permissionGroupUsage.getProxyLabel();
                long lastAccessTimeMillis = permissionGroupUsage.getLastAccessTimeMillis();
                boolean isActive = permissionGroupUsage.isActive();
                boolean isManagedProfile = userInfo != null ? userInfo.isManagedProfile() : false;
                boolean isPhoneCall = permissionGroupUsage.isPhoneCall();
                String permissionGroupName2 = permissionGroupUsage.getPermissionGroupName();
                Intrinsics.checkNotNullExpressionValue(permissionGroupName2, "it.permissionGroupName");
                CharSequence charSequence3 = permissionGroupName2;
                String packageName3 = permissionGroupUsage.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName3, "it.packageName");
                String permissionGroupName3 = permissionGroupUsage.getPermissionGroupName();
                Intrinsics.checkNotNullExpressionValue(permissionGroupName3, "it.permissionGroupName");
                Intent managePermissionIntent = privacyDialogController.getManagePermissionIntent(packageName3, userId, permissionGroupName3, permissionGroupUsage.getAttributionTag(), permissionGroupUsage.getAttributionLabel() != null);
                ArrayMap<String, PrivacyDialogControllerEx.MicModeInfo> micModeInfos = privacyDialogController2.mEx.getMicModeInfos();
                privacyElement = new PrivacyDialog.PrivacyElement(filterType, packageName2, userId, charSequence2, attributionTag, attributionLabel, proxyLabel, lastAccessTimeMillis, isActive, isManagedProfile, isPhoneCall, charSequence3, managePermissionIntent, micModeInfos != null ? micModeInfos.get(permissionGroupUsage.getPackageName()) : null);
            }
            if (privacyElement != null) {
                arrayList.add(privacyElement);
            }
        }
        privacyDialogController2.uiExecutor.execute(new PrivacyDialogController$$ExternalSyntheticLambda0(privacyDialogController2, (List) arrayList, context2));
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-5$lambda-4  reason: not valid java name */
    public static final void m2878showDialog$lambda5$lambda4(PrivacyDialogController privacyDialogController, List list, Context context) {
        Intrinsics.checkNotNullParameter(privacyDialogController, "this$0");
        Intrinsics.checkNotNullParameter(list, "$items");
        Intrinsics.checkNotNullParameter(context, "$context");
        List<PrivacyDialog.PrivacyElement> filterAndSelect = privacyDialogController.filterAndSelect(list);
        if (!filterAndSelect.isEmpty()) {
            PrivacyDialog makeDialog = privacyDialogController.dialogProvider.makeDialog(context, filterAndSelect, new PrivacyDialogController$showDialog$1$1$d$1(privacyDialogController));
            makeDialog.setShowForAllUsers(true);
            makeDialog.addOnDismissListener(privacyDialogController.onDialogDismissed);
            makeDialog.show();
            privacyDialogController.privacyLogger.logShowDialogContents(filterAndSelect);
            privacyDialogController.dialog = makeDialog;
            return;
        }
        Log.w(TAG, "Trying to show empty dialog");
    }

    public final void dismissDialog() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
    }

    private final CharSequence getLabelForPackage(String str, int i) {
        try {
            CharSequence loadLabel = this.packageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(i)).loadLabel(this.packageManager);
            Intrinsics.checkNotNullExpressionValue(loadLabel, "{\n            packageMan…packageManager)\n        }");
            return loadLabel;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w(TAG, "Label not found for: " + str);
            return str;
        }
    }

    private final PrivacyType permGroupToPrivacyType(String str) {
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

    private final PrivacyType filterType(PrivacyType privacyType) {
        if (privacyType == null) {
            return null;
        }
        if ((!(privacyType == PrivacyType.TYPE_CAMERA || privacyType == PrivacyType.TYPE_MICROPHONE) || !this.privacyItemController.getMicCameraAvailable()) && (privacyType != PrivacyType.TYPE_LOCATION || !this.privacyItemController.getLocationAvailable())) {
            PrivacyType privacyType2 = null;
            privacyType = null;
        }
        return privacyType;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: com.android.systemui.privacy.PrivacyDialog$PrivacyElement} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.List<com.android.systemui.privacy.PrivacyDialog.PrivacyElement> filterAndSelect(java.util.List<com.android.systemui.privacy.PrivacyDialog.PrivacyElement> r9) {
        /*
            r8 = this;
            java.lang.Iterable r9 = (java.lang.Iterable) r9
            java.util.LinkedHashMap r8 = new java.util.LinkedHashMap
            r8.<init>()
            java.util.Map r8 = (java.util.Map) r8
            java.util.Iterator r9 = r9.iterator()
        L_0x000d:
            boolean r0 = r9.hasNext()
            if (r0 == 0) goto L_0x0034
            java.lang.Object r0 = r9.next()
            r1 = r0
            com.android.systemui.privacy.PrivacyDialog$PrivacyElement r1 = (com.android.systemui.privacy.PrivacyDialog.PrivacyElement) r1
            com.android.systemui.privacy.PrivacyType r1 = r1.getType()
            java.lang.Object r2 = r8.get(r1)
            if (r2 != 0) goto L_0x002e
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.List r2 = (java.util.List) r2
            r8.put(r1, r2)
        L_0x002e:
            java.util.List r2 = (java.util.List) r2
            r2.add(r0)
            goto L_0x000d
        L_0x0034:
            java.util.SortedMap r8 = kotlin.collections.MapsKt.toSortedMap(r8)
            java.util.Map r8 = (java.util.Map) r8
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.Collection r9 = (java.util.Collection) r9
            java.util.Set r8 = r8.entrySet()
            java.util.Iterator r8 = r8.iterator()
        L_0x0049:
            boolean r0 = r8.hasNext()
            if (r0 == 0) goto L_0x00eb
            java.lang.Object r0 = r8.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r0 = r0.getValue()
            java.util.List r0 = (java.util.List) r0
            java.lang.String r1 = "elements"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Collection r1 = (java.util.Collection) r1
            java.util.Iterator r2 = r0.iterator()
        L_0x006d:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0084
            java.lang.Object r3 = r2.next()
            r4 = r3
            com.android.systemui.privacy.PrivacyDialog$PrivacyElement r4 = (com.android.systemui.privacy.PrivacyDialog.PrivacyElement) r4
            boolean r4 = r4.getActive()
            if (r4 == 0) goto L_0x006d
            r1.add(r3)
            goto L_0x006d
        L_0x0084:
            java.util.List r1 = (java.util.List) r1
            r2 = r1
            java.util.Collection r2 = (java.util.Collection) r2
            boolean r2 = r2.isEmpty()
            r2 = r2 ^ 1
            if (r2 == 0) goto L_0x009f
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            com.android.systemui.privacy.PrivacyDialogController$filterAndSelect$lambda-12$$inlined$sortedByDescending$1 r0 = new com.android.systemui.privacy.PrivacyDialogController$filterAndSelect$lambda-12$$inlined$sortedByDescending$1
            r0.<init>()
            java.util.Comparator r0 = (java.util.Comparator) r0
            java.util.List r0 = kotlin.collections.CollectionsKt.sortedWith(r1, r0)
            goto L_0x00e4
        L_0x009f:
            java.util.Iterator r0 = r0.iterator()
            boolean r1 = r0.hasNext()
            if (r1 != 0) goto L_0x00ab
            r0 = 0
            goto L_0x00d6
        L_0x00ab:
            java.lang.Object r1 = r0.next()
            boolean r2 = r0.hasNext()
            if (r2 != 0) goto L_0x00b7
        L_0x00b5:
            r0 = r1
            goto L_0x00d6
        L_0x00b7:
            r2 = r1
            com.android.systemui.privacy.PrivacyDialog$PrivacyElement r2 = (com.android.systemui.privacy.PrivacyDialog.PrivacyElement) r2
            long r2 = r2.getLastActiveTimestamp()
        L_0x00be:
            java.lang.Object r4 = r0.next()
            r5 = r4
            com.android.systemui.privacy.PrivacyDialog$PrivacyElement r5 = (com.android.systemui.privacy.PrivacyDialog.PrivacyElement) r5
            long r5 = r5.getLastActiveTimestamp()
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 >= 0) goto L_0x00cf
            r1 = r4
            r2 = r5
        L_0x00cf:
            boolean r4 = r0.hasNext()
            if (r4 != 0) goto L_0x00be
            goto L_0x00b5
        L_0x00d6:
            com.android.systemui.privacy.PrivacyDialog$PrivacyElement r0 = (com.android.systemui.privacy.PrivacyDialog.PrivacyElement) r0
            if (r0 == 0) goto L_0x00e0
            java.util.List r0 = kotlin.collections.CollectionsKt.listOf(r0)
            if (r0 != 0) goto L_0x00e4
        L_0x00e0:
            java.util.List r0 = kotlin.collections.CollectionsKt.emptyList()
        L_0x00e4:
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            kotlin.collections.CollectionsKt.addAll(r9, r0)
            goto L_0x0049
        L_0x00eb:
            java.util.List r9 = (java.util.List) r9
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyDialogController.filterAndSelect(java.util.List):java.util.List");
    }
}
