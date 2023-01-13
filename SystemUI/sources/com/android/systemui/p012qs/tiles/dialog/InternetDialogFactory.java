package com.android.systemui.p012qs.tiles.dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.nothing.systemui.p024qs.tiles.settings.panel.BluetoothPanelDialog;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cBC\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J(\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0018\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0006\u0010\u001a\u001a\u00020\u0012J\u0006\u0010\u001b\u001a\u00020\u0012R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo65043d2 = {"Lcom/android/systemui/qs/tiles/dialog/InternetDialogFactory;", "", "handler", "Landroid/os/Handler;", "executor", "Ljava/util/concurrent/Executor;", "internetDialogController", "Lcom/android/systemui/qs/tiles/dialog/InternetDialogController;", "context", "Landroid/content/Context;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "(Landroid/os/Handler;Ljava/util/concurrent/Executor;Lcom/android/systemui/qs/tiles/dialog/InternetDialogController;Landroid/content/Context;Lcom/android/internal/logging/UiEventLogger;Lcom/android/systemui/animation/DialogLaunchAnimator;Lcom/android/systemui/statusbar/policy/KeyguardStateController;)V", "create", "", "aboveStatusBar", "", "canConfigMobileData", "canConfigWifi", "view", "Landroid/view/View;", "createBluetoothDialog", "destroyBluetoothDialog", "destroyDialog", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogFactory */
/* compiled from: InternetDialogFactory.kt */
public final class InternetDialogFactory {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static BluetoothPanelDialog bluetoothDialog;
    /* access modifiers changed from: private */
    public static InternetDialog internetDialog;
    private final Context context;
    private final DialogLaunchAnimator dialogLaunchAnimator;
    private final Executor executor;
    private final Handler handler;
    private final InternetDialogController internetDialogController;
    private final KeyguardStateController keyguardStateController;
    private final UiEventLogger uiEventLogger;

    @Inject
    public InternetDialogFactory(@Main Handler handler2, @Background Executor executor2, InternetDialogController internetDialogController2, Context context2, UiEventLogger uiEventLogger2, DialogLaunchAnimator dialogLaunchAnimator2, KeyguardStateController keyguardStateController2) {
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(internetDialogController2, "internetDialogController");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        this.handler = handler2;
        this.executor = executor2;
        this.internetDialogController = internetDialogController2;
        this.context = context2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.keyguardStateController = keyguardStateController2;
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/qs/tiles/dialog/InternetDialogFactory$Companion;", "", "()V", "bluetoothDialog", "Lcom/nothing/systemui/qs/tiles/settings/panel/BluetoothPanelDialog;", "getBluetoothDialog", "()Lcom/nothing/systemui/qs/tiles/settings/panel/BluetoothPanelDialog;", "setBluetoothDialog", "(Lcom/nothing/systemui/qs/tiles/settings/panel/BluetoothPanelDialog;)V", "internetDialog", "Lcom/android/systemui/qs/tiles/dialog/InternetDialog;", "getInternetDialog", "()Lcom/android/systemui/qs/tiles/dialog/InternetDialog;", "setInternetDialog", "(Lcom/android/systemui/qs/tiles/dialog/InternetDialog;)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogFactory$Companion */
    /* compiled from: InternetDialogFactory.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final InternetDialog getInternetDialog() {
            return InternetDialogFactory.internetDialog;
        }

        public final void setInternetDialog(InternetDialog internetDialog) {
            InternetDialogFactory.internetDialog = internetDialog;
        }

        public final BluetoothPanelDialog getBluetoothDialog() {
            return InternetDialogFactory.bluetoothDialog;
        }

        public final void setBluetoothDialog(BluetoothPanelDialog bluetoothPanelDialog) {
            InternetDialogFactory.bluetoothDialog = bluetoothPanelDialog;
        }
    }

    public final void create(boolean z, boolean z2, boolean z3, View view) {
        View view2 = view;
        if (internetDialog == null) {
            InternetDialog internetDialog2 = new InternetDialog(this.context, this, this.internetDialogController, z2, z3, z, this.uiEventLogger, this.handler, this.executor, this.keyguardStateController);
            internetDialog = internetDialog2;
            if (view2 != null) {
                DialogLaunchAnimator dialogLaunchAnimator2 = this.dialogLaunchAnimator;
                Intrinsics.checkNotNull(internetDialog2);
                dialogLaunchAnimator2.showFromView(internetDialog2, view2, true);
                return;
            }
            internetDialog2.show();
        } else if (InternetDialogFactoryKt.DEBUG) {
            Log.d("InternetDialogFactory", "InternetDialog is showing, do not create it twice.");
        }
    }

    public final void destroyDialog() {
        if (InternetDialogFactoryKt.DEBUG) {
            Log.d("InternetDialogFactory", "destroyDialog");
        }
        internetDialog = null;
    }

    public final void createBluetoothDialog(boolean z, View view) {
        if (bluetoothDialog != null) {
            Log.d("InternetDialogFactory", "BluetoothDialog is showing, do not create it twice.");
            return;
        }
        BluetoothPanelDialog bluetoothPanelDialog = new BluetoothPanelDialog(this.context, this, this.internetDialogController, z, this.handler);
        bluetoothDialog = bluetoothPanelDialog;
        if (view != null) {
            DialogLaunchAnimator dialogLaunchAnimator2 = this.dialogLaunchAnimator;
            Intrinsics.checkNotNull(bluetoothPanelDialog);
            dialogLaunchAnimator2.showFromView(bluetoothPanelDialog, view, true);
            return;
        }
        bluetoothPanelDialog.show();
    }

    public final void destroyBluetoothDialog() {
        Log.d("InternetDialogFactory", "destroyBluetoothDialog");
        bluetoothDialog = null;
    }
}
