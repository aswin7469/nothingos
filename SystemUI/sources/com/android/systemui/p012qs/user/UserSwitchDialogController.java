package com.android.systemui.p012qs.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.p012qs.QSUserSwitcherEvent;
import com.android.systemui.p012qs.tiles.UserDetailView;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import javax.inject.Inject;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0003\u0017\u0018\u0019B5\b\u0017\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rBI\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000f¢\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/systemui/qs/user/UserSwitchDialogController;", "", "userDetailViewAdapterProvider", "Ljavax/inject/Provider;", "Lcom/android/systemui/qs/tiles/UserDetailView$Adapter;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "(Ljavax/inject/Provider;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/animation/DialogLaunchAnimator;Lcom/android/internal/logging/UiEventLogger;)V", "dialogFactory", "Lkotlin/Function1;", "Landroid/content/Context;", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "(Ljavax/inject/Provider;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/animation/DialogLaunchAnimator;Lcom/android/internal/logging/UiEventLogger;Lkotlin/jvm/functions/Function1;)V", "showDialog", "", "view", "Landroid/view/View;", "Companion", "DialogShower", "DialogShowerImpl", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.user.UserSwitchDialogController */
/* compiled from: UserSwitchDialogController.kt */
public final class UserSwitchDialogController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final Intent USER_SETTINGS_INTENT = new Intent("android.settings.USER_SETTINGS");
    private final ActivityStarter activityStarter;
    private final Function1<Context, SystemUIDialog> dialogFactory;
    private final DialogLaunchAnimator dialogLaunchAnimator;
    private final FalsingManager falsingManager;
    private final UiEventLogger uiEventLogger;
    private final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/qs/user/UserSwitchDialogController$DialogShower;", "Landroid/content/DialogInterface;", "showDialog", "", "dialog", "Landroid/app/Dialog;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.user.UserSwitchDialogController$DialogShower */
    /* compiled from: UserSwitchDialogController.kt */
    public interface DialogShower extends DialogInterface {
        void showDialog(Dialog dialog);
    }

    public UserSwitchDialogController(Provider<UserDetailView.Adapter> provider, ActivityStarter activityStarter2, FalsingManager falsingManager2, DialogLaunchAnimator dialogLaunchAnimator2, UiEventLogger uiEventLogger2, Function1<? super Context, ? extends SystemUIDialog> function1) {
        Intrinsics.checkNotNullParameter(provider, "userDetailViewAdapterProvider");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(function1, "dialogFactory");
        this.userDetailViewAdapterProvider = provider;
        this.activityStarter = activityStarter2;
        this.falsingManager = falsingManager2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogFactory = function1;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Inject
    public UserSwitchDialogController(Provider<UserDetailView.Adapter> provider, ActivityStarter activityStarter2, FalsingManager falsingManager2, DialogLaunchAnimator dialogLaunchAnimator2, UiEventLogger uiEventLogger2) {
        this(provider, activityStarter2, falsingManager2, dialogLaunchAnimator2, uiEventLogger2, C24201.INSTANCE);
        Intrinsics.checkNotNullParameter(provider, "userDetailViewAdapterProvider");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/qs/user/UserSwitchDialogController$Companion;", "", "()V", "USER_SETTINGS_INTENT", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.user.UserSwitchDialogController$Companion */
    /* compiled from: UserSwitchDialogController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void showDialog(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        Function1<Context, SystemUIDialog> function1 = this.dialogFactory;
        ViewGroup notificationShadeView = ((NotificationShadeWindowController) Dependency.get(NotificationShadeWindowController.class)).getNotificationShadeView();
        Intrinsics.checkNotNull(notificationShadeView);
        Context context = notificationShadeView.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "get(\n            Notific…tionShadeView()!!.context");
        SystemUIDialog invoke = function1.invoke(context);
        invoke.setShowForAllUsers(true);
        invoke.setCanceledOnTouchOutside(true);
        invoke.setTitle(C1894R.string.qs_user_switch_dialog_title);
        invoke.setPositiveButton(C1894R.string.quick_settings_done, new UserSwitchDialogController$$ExternalSyntheticLambda0(this));
        invoke.setNeutralButton(C1894R.string.quick_settings_more_user_settings, new UserSwitchDialogController$$ExternalSyntheticLambda1(this, invoke), false);
        View inflate = LayoutInflater.from(invoke.getContext()).inflate(C1894R.layout.qs_user_dialog_content, (ViewGroup) null);
        invoke.setView(inflate);
        UserDetailView.Adapter adapter = this.userDetailViewAdapterProvider.get();
        adapter.linkToViewGroup((ViewGroup) inflate.findViewById(C1894R.C1898id.grid));
        Dialog dialog = invoke;
        DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, dialog, view, false, 4, (Object) null);
        this.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_OPEN);
        adapter.injectDialogShower(new DialogShowerImpl(dialog, this.dialogLaunchAnimator));
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-2$lambda-0  reason: not valid java name */
    public static final void m2995showDialog$lambda2$lambda0(UserSwitchDialogController userSwitchDialogController, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(userSwitchDialogController, "this$0");
        userSwitchDialogController.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_CLOSE);
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2996showDialog$lambda2$lambda1(UserSwitchDialogController userSwitchDialogController, SystemUIDialog systemUIDialog, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(userSwitchDialogController, "this$0");
        Intrinsics.checkNotNullParameter(systemUIDialog, "$this_with");
        if (!userSwitchDialogController.falsingManager.isFalseTap(1)) {
            userSwitchDialogController.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_MORE_SETTINGS);
            DialogLaunchAnimator dialogLaunchAnimator2 = userSwitchDialogController.dialogLaunchAnimator;
            Button button = systemUIDialog.getButton(-3);
            Intrinsics.checkNotNullExpressionValue(button, "getButton(BUTTON_NEUTRAL)");
            ActivityLaunchAnimator.Controller createActivityLaunchController$default = DialogLaunchAnimator.createActivityLaunchController$default(dialogLaunchAnimator2, button, (Integer) null, 2, (Object) null);
            if (createActivityLaunchController$default == null) {
                systemUIDialog.dismiss();
            }
            userSwitchDialogController.activityStarter.postStartActivityDismissingKeyguard(USER_SETTINGS_INTENT, 0, createActivityLaunchController$default);
        }
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\b\u001a\u00020\tH\u0001J\t\u0010\n\u001a\u00020\tH\u0001J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/qs/user/UserSwitchDialogController$DialogShowerImpl;", "Landroid/content/DialogInterface;", "Lcom/android/systemui/qs/user/UserSwitchDialogController$DialogShower;", "animateFrom", "Landroid/app/Dialog;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "(Landroid/app/Dialog;Lcom/android/systemui/animation/DialogLaunchAnimator;)V", "cancel", "", "dismiss", "showDialog", "dialog", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.user.UserSwitchDialogController$DialogShowerImpl */
    /* compiled from: UserSwitchDialogController.kt */
    private static final class DialogShowerImpl implements DialogInterface, DialogShower {
        private final Dialog animateFrom;
        private final DialogLaunchAnimator dialogLaunchAnimator;

        public void cancel() {
            this.animateFrom.cancel();
        }

        public void dismiss() {
            this.animateFrom.dismiss();
        }

        public DialogShowerImpl(Dialog dialog, DialogLaunchAnimator dialogLaunchAnimator2) {
            Intrinsics.checkNotNullParameter(dialog, "animateFrom");
            Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
            this.animateFrom = dialog;
            this.dialogLaunchAnimator = dialogLaunchAnimator2;
        }

        public void showDialog(Dialog dialog) {
            Intrinsics.checkNotNullParameter(dialog, "dialog");
            DialogLaunchAnimator.showFromDialog$default(this.dialogLaunchAnimator, dialog, this.animateFrom, false, 4, (Object) null);
        }
    }
}
