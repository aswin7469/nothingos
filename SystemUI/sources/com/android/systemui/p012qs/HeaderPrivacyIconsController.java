package com.android.systemui.p012qs;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.permission.PermissionGroupUsage;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyChipEvent;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007*\u00014\u0018\u00002\u00020\u0001Bs\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0001\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0001\u0010\u0012\u001a\u00020\u0011\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\u0019\u001a\u00020\u001a¢\u0006\u0002\u0010\u001bJ\b\u00106\u001a\u00020*H\u0002J\u0006\u00107\u001a\u000208J\u0006\u00109\u001a\u000208J\u000e\u0010:\u001a\b\u0012\u0004\u0012\u00020<0;H\u0003J\u0010\u0010=\u001a\u0002082\u0006\u0010>\u001a\u00020*H\u0002J\b\u0010?\u001a\u000208H\u0002J\u0006\u0010@\u001a\u000208J\u0006\u0010A\u001a\u000208J\b\u0010B\u001a\u000208H\u0002R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u001c\u001a\u00020\u001d¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010 \u001a\n \"*\u0004\u0018\u00010!0!X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010#\u001a\u0004\u0018\u00010$X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010,\u001a\n \"*\u0004\u0018\u00010!0!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010.\u001a\n \"*\u0004\u0018\u00010!0!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u0010\u00103\u001a\u000204X\u0004¢\u0006\u0004\n\u0002\u00105R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000¨\u0006C"}, mo65043d2 = {"Lcom/android/systemui/qs/HeaderPrivacyIconsController;", "", "privacyItemController", "Lcom/android/systemui/privacy/PrivacyItemController;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "privacyChip", "Lcom/android/systemui/privacy/OngoingPrivacyChip;", "privacyDialogController", "Lcom/android/systemui/privacy/PrivacyDialogController;", "privacyLogger", "Lcom/android/systemui/privacy/logging/PrivacyLogger;", "iconContainer", "Lcom/android/systemui/statusbar/phone/StatusIconContainer;", "permissionManager", "Landroid/permission/PermissionManager;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "uiExecutor", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "appOpsController", "Lcom/android/systemui/appops/AppOpsController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "safetyCenterManager", "Landroid/safetycenter/SafetyCenterManager;", "(Lcom/android/systemui/privacy/PrivacyItemController;Lcom/android/internal/logging/UiEventLogger;Lcom/android/systemui/privacy/OngoingPrivacyChip;Lcom/android/systemui/privacy/PrivacyDialogController;Lcom/android/systemui/privacy/logging/PrivacyLogger;Lcom/android/systemui/statusbar/phone/StatusIconContainer;Landroid/permission/PermissionManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/appops/AppOpsController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Landroid/safetycenter/SafetyCenterManager;)V", "attachStateChangeListener", "Landroid/view/View$OnAttachStateChangeListener;", "getAttachStateChangeListener", "()Landroid/view/View$OnAttachStateChangeListener;", "cameraSlot", "", "kotlin.jvm.PlatformType", "chipVisibilityListener", "Lcom/android/systemui/qs/ChipVisibilityListener;", "getChipVisibilityListener", "()Lcom/android/systemui/qs/ChipVisibilityListener;", "setChipVisibilityListener", "(Lcom/android/systemui/qs/ChipVisibilityListener;)V", "listening", "", "locationIndicatorsEnabled", "locationSlot", "micCameraIndicatorsEnabled", "micSlot", "picCallback", "Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "privacyChipLogged", "safetyCenterEnabled", "safetyCenterReceiver", "com/android/systemui/qs/HeaderPrivacyIconsController$safetyCenterReceiver$1", "Lcom/android/systemui/qs/HeaderPrivacyIconsController$safetyCenterReceiver$1;", "getChipEnabled", "onParentInvisible", "", "onParentVisible", "permGroupUsage", "", "Landroid/permission/PermissionGroupUsage;", "setChipVisibility", "visible", "showSafetyCenter", "startListening", "stopListening", "updatePrivacyIconSlots", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.HeaderPrivacyIconsController */
/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController {
    private final ActivityStarter activityStarter;
    private final AppOpsController appOpsController;
    private final View.OnAttachStateChangeListener attachStateChangeListener;
    /* access modifiers changed from: private */
    public final Executor backgroundExecutor;
    /* access modifiers changed from: private */
    public final BroadcastDispatcher broadcastDispatcher;
    private final String cameraSlot;
    private ChipVisibilityListener chipVisibilityListener;
    private final StatusIconContainer iconContainer;
    private boolean listening;
    /* access modifiers changed from: private */
    public boolean locationIndicatorsEnabled;
    private final String locationSlot;
    /* access modifiers changed from: private */
    public boolean micCameraIndicatorsEnabled;
    private final String micSlot;
    private final PermissionManager permissionManager;
    private final PrivacyItemController.Callback picCallback;
    /* access modifiers changed from: private */
    public final OngoingPrivacyChip privacyChip;
    private boolean privacyChipLogged;
    private final PrivacyDialogController privacyDialogController;
    private final PrivacyItemController privacyItemController;
    private final PrivacyLogger privacyLogger;
    /* access modifiers changed from: private */
    public boolean safetyCenterEnabled;
    /* access modifiers changed from: private */
    public final SafetyCenterManager safetyCenterManager;
    /* access modifiers changed from: private */
    public final HeaderPrivacyIconsController$safetyCenterReceiver$1 safetyCenterReceiver;
    private final UiEventLogger uiEventLogger;
    private final Executor uiExecutor;

    @Inject
    public HeaderPrivacyIconsController(PrivacyItemController privacyItemController2, UiEventLogger uiEventLogger2, OngoingPrivacyChip ongoingPrivacyChip, PrivacyDialogController privacyDialogController2, PrivacyLogger privacyLogger2, StatusIconContainer statusIconContainer, PermissionManager permissionManager2, @Background Executor executor, @Main Executor executor2, ActivityStarter activityStarter2, AppOpsController appOpsController2, BroadcastDispatcher broadcastDispatcher2, SafetyCenterManager safetyCenterManager2) {
        PrivacyItemController privacyItemController3 = privacyItemController2;
        UiEventLogger uiEventLogger3 = uiEventLogger2;
        OngoingPrivacyChip ongoingPrivacyChip2 = ongoingPrivacyChip;
        PrivacyDialogController privacyDialogController3 = privacyDialogController2;
        PrivacyLogger privacyLogger3 = privacyLogger2;
        StatusIconContainer statusIconContainer2 = statusIconContainer;
        PermissionManager permissionManager3 = permissionManager2;
        Executor executor3 = executor;
        Executor executor4 = executor2;
        ActivityStarter activityStarter3 = activityStarter2;
        AppOpsController appOpsController3 = appOpsController2;
        BroadcastDispatcher broadcastDispatcher3 = broadcastDispatcher2;
        SafetyCenterManager safetyCenterManager3 = safetyCenterManager2;
        Intrinsics.checkNotNullParameter(privacyItemController3, "privacyItemController");
        Intrinsics.checkNotNullParameter(uiEventLogger3, "uiEventLogger");
        Intrinsics.checkNotNullParameter(ongoingPrivacyChip2, "privacyChip");
        Intrinsics.checkNotNullParameter(privacyDialogController3, "privacyDialogController");
        Intrinsics.checkNotNullParameter(privacyLogger3, "privacyLogger");
        Intrinsics.checkNotNullParameter(statusIconContainer2, "iconContainer");
        Intrinsics.checkNotNullParameter(permissionManager3, "permissionManager");
        Intrinsics.checkNotNullParameter(executor3, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor4, "uiExecutor");
        Intrinsics.checkNotNullParameter(activityStarter3, "activityStarter");
        Intrinsics.checkNotNullParameter(appOpsController3, "appOpsController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher3, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(safetyCenterManager3, "safetyCenterManager");
        this.privacyItemController = privacyItemController3;
        this.uiEventLogger = uiEventLogger3;
        this.privacyChip = ongoingPrivacyChip2;
        this.privacyDialogController = privacyDialogController3;
        this.privacyLogger = privacyLogger3;
        this.iconContainer = statusIconContainer2;
        this.permissionManager = permissionManager3;
        this.backgroundExecutor = executor3;
        this.uiExecutor = executor4;
        this.activityStarter = activityStarter3;
        this.appOpsController = appOpsController3;
        this.broadcastDispatcher = broadcastDispatcher3;
        this.safetyCenterManager = safetyCenterManager3;
        this.cameraSlot = ongoingPrivacyChip.getResources().getString(17041558);
        this.micSlot = ongoingPrivacyChip.getResources().getString(17041570);
        this.locationSlot = ongoingPrivacyChip.getResources().getString(17041568);
        HeaderPrivacyIconsController$safetyCenterReceiver$1 headerPrivacyIconsController$safetyCenterReceiver$1 = new HeaderPrivacyIconsController$safetyCenterReceiver$1(this);
        this.safetyCenterReceiver = headerPrivacyIconsController$safetyCenterReceiver$1;
        View.OnAttachStateChangeListener headerPrivacyIconsController$attachStateChangeListener$1 = new HeaderPrivacyIconsController$attachStateChangeListener$1(this);
        this.attachStateChangeListener = headerPrivacyIconsController$attachStateChangeListener$1;
        executor3.execute(new HeaderPrivacyIconsController$$ExternalSyntheticLambda0(this));
        if (ongoingPrivacyChip.isAttachedToWindow()) {
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, headerPrivacyIconsController$safetyCenterReceiver$1, new IntentFilter(SafetyCenterManager.ACTION_SAFETY_CENTER_ENABLED_CHANGED), executor, (UserHandle) null, 0, (String) null, 56, (Object) null);
        }
        ongoingPrivacyChip2.addOnAttachStateChangeListener(headerPrivacyIconsController$attachStateChangeListener$1);
        this.picCallback = new HeaderPrivacyIconsController$picCallback$1(this);
    }

    public final ChipVisibilityListener getChipVisibilityListener() {
        return this.chipVisibilityListener;
    }

    public final void setChipVisibilityListener(ChipVisibilityListener chipVisibilityListener2) {
        this.chipVisibilityListener = chipVisibilityListener2;
    }

    public final View.OnAttachStateChangeListener getAttachStateChangeListener() {
        return this.attachStateChangeListener;
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m2907_init_$lambda0(HeaderPrivacyIconsController headerPrivacyIconsController) {
        Intrinsics.checkNotNullParameter(headerPrivacyIconsController, "this$0");
        headerPrivacyIconsController.safetyCenterEnabled = headerPrivacyIconsController.safetyCenterManager.isSafetyCenterEnabled();
    }

    private final boolean getChipEnabled() {
        return this.micCameraIndicatorsEnabled || this.locationIndicatorsEnabled;
    }

    public final void onParentVisible() {
        this.privacyChip.setOnClickListener(new HeaderPrivacyIconsController$$ExternalSyntheticLambda3(this));
        setChipVisibility(this.privacyChip.getVisibility() == 0);
        this.micCameraIndicatorsEnabled = this.privacyItemController.getMicCameraAvailable();
        this.locationIndicatorsEnabled = this.privacyItemController.getLocationAvailable();
        updatePrivacyIconSlots();
    }

    /* access modifiers changed from: private */
    /* renamed from: onParentVisible$lambda-1  reason: not valid java name */
    public static final void m2908onParentVisible$lambda1(HeaderPrivacyIconsController headerPrivacyIconsController, View view) {
        Intrinsics.checkNotNullParameter(headerPrivacyIconsController, "this$0");
        headerPrivacyIconsController.uiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_CLICK);
        if (headerPrivacyIconsController.safetyCenterEnabled) {
            headerPrivacyIconsController.showSafetyCenter();
            return;
        }
        PrivacyDialogController privacyDialogController2 = headerPrivacyIconsController.privacyDialogController;
        Context context = headerPrivacyIconsController.privacyChip.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "privacyChip.context");
        privacyDialogController2.showDialog(context);
    }

    private final void showSafetyCenter() {
        this.backgroundExecutor.execute(new HeaderPrivacyIconsController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: showSafetyCenter$lambda-3  reason: not valid java name */
    public static final void m2909showSafetyCenter$lambda3(HeaderPrivacyIconsController headerPrivacyIconsController) {
        Intrinsics.checkNotNullParameter(headerPrivacyIconsController, "this$0");
        ArrayList arrayList = new ArrayList(headerPrivacyIconsController.permGroupUsage());
        headerPrivacyIconsController.privacyLogger.logUnfilteredPermGroupUsage(arrayList);
        Intent intent = new Intent("android.intent.action.VIEW_SAFETY_CENTER_QS");
        intent.putParcelableArrayListExtra("android.permission.extra.PERMISSION_USAGES", arrayList);
        intent.setFlags(268435456);
        headerPrivacyIconsController.uiExecutor.execute(new HeaderPrivacyIconsController$$ExternalSyntheticLambda1(headerPrivacyIconsController, intent));
    }

    /* access modifiers changed from: private */
    /* renamed from: showSafetyCenter$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2910showSafetyCenter$lambda3$lambda2(HeaderPrivacyIconsController headerPrivacyIconsController, Intent intent) {
        Intrinsics.checkNotNullParameter(headerPrivacyIconsController, "this$0");
        Intrinsics.checkNotNullParameter(intent, "$startSafetyCenter");
        headerPrivacyIconsController.activityStarter.startActivity(intent, true, ActivityLaunchAnimator.Controller.Companion.fromView$default(ActivityLaunchAnimator.Controller.Companion, headerPrivacyIconsController.privacyChip, (Integer) null, 2, (Object) null));
    }

    private final List<PermissionGroupUsage> permGroupUsage() {
        List<PermissionGroupUsage> indicatorAppOpUsageData = this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
        Intrinsics.checkNotNullExpressionValue(indicatorAppOpUsageData, "permissionManager.getInd…OpsController.isMicMuted)");
        return indicatorAppOpUsageData;
    }

    public final void onParentInvisible() {
        this.chipVisibilityListener = null;
        this.privacyChip.setOnClickListener((View.OnClickListener) null);
    }

    public final void startListening() {
        this.listening = true;
        this.micCameraIndicatorsEnabled = this.privacyItemController.getMicCameraAvailable();
        this.locationIndicatorsEnabled = this.privacyItemController.getLocationAvailable();
        this.privacyItemController.addCallback(this.picCallback);
    }

    public final void stopListening() {
        this.listening = false;
        this.privacyItemController.removeCallback(this.picCallback);
        this.privacyChipLogged = false;
    }

    /* access modifiers changed from: private */
    public final void setChipVisibility(boolean z) {
        int i = 0;
        if (!z || !getChipEnabled()) {
            this.privacyLogger.logChipVisible(false);
        } else {
            this.privacyLogger.logChipVisible(true);
            if (!this.privacyChipLogged && this.listening) {
                this.privacyChipLogged = true;
                this.uiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_VIEW);
            }
        }
        OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
        if (!z) {
            i = 8;
        }
        ongoingPrivacyChip.setVisibility(i);
        ChipVisibilityListener chipVisibilityListener2 = this.chipVisibilityListener;
        if (chipVisibilityListener2 != null) {
            chipVisibilityListener2.onChipVisibilityRefreshed(z);
        }
    }

    /* access modifiers changed from: private */
    public final void updatePrivacyIconSlots() {
        if (getChipEnabled()) {
            if (this.micCameraIndicatorsEnabled) {
                this.iconContainer.addIgnoredSlot(this.cameraSlot);
                this.iconContainer.addIgnoredSlot(this.micSlot);
            } else {
                this.iconContainer.removeIgnoredSlot(this.cameraSlot);
                this.iconContainer.removeIgnoredSlot(this.micSlot);
            }
            if (this.locationIndicatorsEnabled) {
                this.iconContainer.addIgnoredSlot(this.locationSlot);
            } else {
                this.iconContainer.removeIgnoredSlot(this.locationSlot);
            }
        } else {
            this.iconContainer.removeIgnoredSlot(this.cameraSlot);
            this.iconContainer.removeIgnoredSlot(this.micSlot);
            this.iconContainer.removeIgnoredSlot(this.locationSlot);
        }
    }
}
