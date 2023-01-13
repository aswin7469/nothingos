package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.external.TileRequestDialog;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006*\u0001\u000f\u0018\u0000 -2\u00020\u0001:\u0004,-./B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0002\u0010\rJ\u0010\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u001e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0002J\u0006\u0010\u001f\u001a\u00020\u0014J\u0006\u0010 \u001a\u00020\u0014J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J=\u0010#\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020%2\b\u0010'\u001a\u0004\u0018\u00010(2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001e0*H\u0001¢\u0006\u0002\b+R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u00060"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceRequestController;", "", "qsTileHost", "Lcom/android/systemui/qs/QSTileHost;", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "eventLogger", "Lcom/android/systemui/qs/external/TileRequestDialogEventLogger;", "dialogCreator", "Lkotlin/Function0;", "Lcom/android/systemui/qs/external/TileRequestDialog;", "(Lcom/android/systemui/qs/QSTileHost;Lcom/android/systemui/statusbar/CommandQueue;Lcom/android/systemui/statusbar/commandline/CommandRegistry;Lcom/android/systemui/qs/external/TileRequestDialogEventLogger;Lkotlin/jvm/functions/Function0;)V", "commandQueueCallback", "com/android/systemui/qs/external/TileServiceRequestController$commandQueueCallback$1", "Lcom/android/systemui/qs/external/TileServiceRequestController$commandQueueCallback$1;", "dialogCanceller", "Lkotlin/Function1;", "", "", "addTile", "componentName", "Landroid/content/ComponentName;", "createDialog", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "tileData", "Lcom/android/systemui/qs/external/TileRequestDialog$TileData;", "responseHandler", "Lcom/android/systemui/qs/external/TileServiceRequestController$SingleShotConsumer;", "", "destroy", "init", "isTileAlreadyAdded", "", "requestTileAdd", "appName", "", "label", "icon", "Landroid/graphics/drawable/Icon;", "callback", "Ljava/util/function/Consumer;", "requestTileAdd$SystemUI_nothingRelease", "Builder", "Companion", "SingleShotConsumer", "TileServiceRequestCommand", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.external.TileServiceRequestController */
/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController {
    public static final int ADD_TILE = 2;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int DISMISSED = 3;
    public static final int DONT_ADD_TILE = 0;
    public static final int TILE_ALREADY_ADDED = 1;
    private final CommandQueue commandQueue;
    private final TileServiceRequestController$commandQueueCallback$1 commandQueueCallback;
    private final CommandRegistry commandRegistry;
    /* access modifiers changed from: private */
    public Function1<? super String, Unit> dialogCanceller;
    private final Function0<TileRequestDialog> dialogCreator;
    private final TileRequestDialogEventLogger eventLogger;
    private final QSTileHost qsTileHost;

    public TileServiceRequestController(QSTileHost qSTileHost, CommandQueue commandQueue2, CommandRegistry commandRegistry2, TileRequestDialogEventLogger tileRequestDialogEventLogger, Function0<TileRequestDialog> function0) {
        Intrinsics.checkNotNullParameter(qSTileHost, "qsTileHost");
        Intrinsics.checkNotNullParameter(commandQueue2, "commandQueue");
        Intrinsics.checkNotNullParameter(commandRegistry2, "commandRegistry");
        Intrinsics.checkNotNullParameter(tileRequestDialogEventLogger, "eventLogger");
        Intrinsics.checkNotNullParameter(function0, "dialogCreator");
        this.qsTileHost = qSTileHost;
        this.commandQueue = commandQueue2;
        this.commandRegistry = commandRegistry2;
        this.eventLogger = tileRequestDialogEventLogger;
        this.dialogCreator = function0;
        this.commandQueueCallback = new TileServiceRequestController$commandQueueCallback$1(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TileServiceRequestController(final QSTileHost qSTileHost, CommandQueue commandQueue2, CommandRegistry commandRegistry2, TileRequestDialogEventLogger tileRequestDialogEventLogger, Function0 function0, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(qSTileHost, commandQueue2, commandRegistry2, tileRequestDialogEventLogger, (i & 16) != 0 ? new Function0<TileRequestDialog>() {
            public final TileRequestDialog invoke() {
                Context context = qSTileHost.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "qsTileHost.context");
                return new TileRequestDialog(context);
            }
        } : function0);
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceRequestController$Companion;", "", "()V", "ADD_TILE", "", "DISMISSED", "DONT_ADD_TILE", "TILE_ALREADY_ADDED", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileServiceRequestController$Companion */
    /* compiled from: TileServiceRequestController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void init() {
        this.commandRegistry.registerCommand("tile-service-add", new TileServiceRequestController$init$1(this));
        this.commandQueue.addCallback((CommandQueue.Callbacks) this.commandQueueCallback);
    }

    public final void destroy() {
        this.commandRegistry.unregisterCommand("tile-service-add");
        this.commandQueue.removeCallback((CommandQueue.Callbacks) this.commandQueueCallback);
    }

    private final void addTile(ComponentName componentName) {
        this.qsTileHost.addTile(componentName, true);
    }

    public final void requestTileAdd$SystemUI_nothingRelease(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, Consumer<Integer> consumer) {
        CharSequence charSequence3 = charSequence;
        CharSequence charSequence4 = charSequence2;
        Consumer<Integer> consumer2 = consumer;
        ComponentName componentName2 = componentName;
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence3, "appName");
        Intrinsics.checkNotNullParameter(charSequence4, BaseIconCache.IconDB.COLUMN_LABEL);
        Intrinsics.checkNotNullParameter(consumer2, "callback");
        InstanceId newInstanceId = this.eventLogger.newInstanceId();
        String packageName = componentName.getPackageName();
        if (isTileAlreadyAdded(componentName)) {
            consumer2.accept(1);
            TileRequestDialogEventLogger tileRequestDialogEventLogger = this.eventLogger;
            Intrinsics.checkNotNullExpressionValue(packageName, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            tileRequestDialogEventLogger.logTileAlreadyAdded(packageName, newInstanceId);
            return;
        }
        SystemUIDialog createDialog = createDialog(new TileRequestDialog.TileData(charSequence3, charSequence4, icon), new SingleShotConsumer(new TileServiceRequestController$$ExternalSyntheticLambda3(this, componentName, packageName, newInstanceId, consumer)));
        this.dialogCanceller = new TileServiceRequestController$requestTileAdd$1$1(packageName, createDialog, this);
        createDialog.show();
        TileRequestDialogEventLogger tileRequestDialogEventLogger2 = this.eventLogger;
        Intrinsics.checkNotNullExpressionValue(packageName, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        tileRequestDialogEventLogger2.logDialogShown(packageName, newInstanceId);
    }

    /* access modifiers changed from: private */
    /* renamed from: requestTileAdd$lambda-0  reason: not valid java name */
    public static final void m2958requestTileAdd$lambda0(TileServiceRequestController tileServiceRequestController, ComponentName componentName, String str, InstanceId instanceId, Consumer consumer, Integer num) {
        Intrinsics.checkNotNullParameter(tileServiceRequestController, "this$0");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(instanceId, "$instanceId");
        Intrinsics.checkNotNullParameter(consumer, "$callback");
        Intrinsics.checkNotNullParameter(num, "response");
        if (num.intValue() == 2) {
            tileServiceRequestController.addTile(componentName);
        }
        tileServiceRequestController.dialogCanceller = null;
        TileRequestDialogEventLogger tileRequestDialogEventLogger = tileServiceRequestController.eventLogger;
        int intValue = num.intValue();
        Intrinsics.checkNotNullExpressionValue(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        tileRequestDialogEventLogger.logUserResponse(intValue, str, instanceId);
        consumer.accept(num);
    }

    private final SystemUIDialog createDialog(TileRequestDialog.TileData tileData, SingleShotConsumer<Integer> singleShotConsumer) {
        TileServiceRequestController$$ExternalSyntheticLambda0 tileServiceRequestController$$ExternalSyntheticLambda0 = new TileServiceRequestController$$ExternalSyntheticLambda0(singleShotConsumer);
        TileRequestDialog invoke = this.dialogCreator.invoke();
        TileRequestDialog tileRequestDialog = invoke;
        tileRequestDialog.setTileData(tileData);
        tileRequestDialog.setShowForAllUsers(true);
        tileRequestDialog.setCanceledOnTouchOutside(true);
        tileRequestDialog.setOnCancelListener(new TileServiceRequestController$$ExternalSyntheticLambda1(singleShotConsumer));
        tileRequestDialog.setOnDismissListener(new TileServiceRequestController$$ExternalSyntheticLambda2(singleShotConsumer));
        tileRequestDialog.setPositiveButton(C1894R.string.qs_tile_request_dialog_add, tileServiceRequestController$$ExternalSyntheticLambda0);
        tileRequestDialog.setNegativeButton(C1894R.string.qs_tile_request_dialog_not_add, tileServiceRequestController$$ExternalSyntheticLambda0);
        return invoke;
    }

    /* access modifiers changed from: private */
    /* renamed from: createDialog$lambda-2  reason: not valid java name */
    public static final void m2955createDialog$lambda2(SingleShotConsumer singleShotConsumer, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(singleShotConsumer, "$responseHandler");
        if (i == -1) {
            singleShotConsumer.accept(2);
        } else {
            singleShotConsumer.accept(0);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: createDialog$lambda-5$lambda-3  reason: not valid java name */
    public static final void m2956createDialog$lambda5$lambda3(SingleShotConsumer singleShotConsumer, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(singleShotConsumer, "$responseHandler");
        singleShotConsumer.accept(3);
    }

    /* access modifiers changed from: private */
    /* renamed from: createDialog$lambda-5$lambda-4  reason: not valid java name */
    public static final void m2957createDialog$lambda5$lambda4(SingleShotConsumer singleShotConsumer, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(singleShotConsumer, "$responseHandler");
        singleShotConsumer.accept(3);
    }

    private final boolean isTileAlreadyAdded(ComponentName componentName) {
        return this.qsTileHost.indexOf(CustomTile.toSpec(componentName)) != -1;
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceRequestController$TileServiceRequestCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/qs/external/TileServiceRequestController;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileServiceRequestController$TileServiceRequestCommand */
    /* compiled from: TileServiceRequestController.kt */
    public final class TileServiceRequestCommand implements Command {
        public TileServiceRequestCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            ComponentName unflattenFromString = ComponentName.unflattenFromString(list.get(0));
            if (unflattenFromString == null) {
                TileServiceRequestCommand tileServiceRequestCommand = this;
                Log.w("TileServiceRequestController", "Malformed componentName " + list.get(0));
                return;
            }
            TileServiceRequestController.this.requestTileAdd$SystemUI_nothingRelease(unflattenFromString, list.get(1), list.get(2), (Icon) null, new C2379x7cac3252());
        }

        /* access modifiers changed from: private */
        /* renamed from: execute$lambda-1  reason: not valid java name */
        public static final void m2959execute$lambda1(Integer num) {
            Intrinsics.checkNotNullParameter(num, "it");
            Log.d("TileServiceRequestController", "Response: " + num.intValue());
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar tile-service-add <componentName> <appName> <label>");
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0004J\u0015\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\nR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceRequestController$SingleShotConsumer;", "T", "Ljava/util/function/Consumer;", "consumer", "(Ljava/util/function/Consumer;)V", "dispatched", "Ljava/util/concurrent/atomic/AtomicBoolean;", "accept", "", "t", "(Ljava/lang/Object;)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileServiceRequestController$SingleShotConsumer */
    /* compiled from: TileServiceRequestController.kt */
    private static final class SingleShotConsumer<T> implements Consumer<T> {
        private final Consumer<T> consumer;
        private final AtomicBoolean dispatched = new AtomicBoolean(false);

        public SingleShotConsumer(Consumer<T> consumer2) {
            Intrinsics.checkNotNullParameter(consumer2, "consumer");
            this.consumer = consumer2;
        }

        public void accept(T t) {
            if (this.dispatched.compareAndSet(false, true)) {
                this.consumer.accept(t);
            }
        }
    }

    @SysUISingleton
    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceRequestController$Builder;", "", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "(Lcom/android/systemui/statusbar/CommandQueue;Lcom/android/systemui/statusbar/commandline/CommandRegistry;)V", "create", "Lcom/android/systemui/qs/external/TileServiceRequestController;", "qsTileHost", "Lcom/android/systemui/qs/QSTileHost;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileServiceRequestController$Builder */
    /* compiled from: TileServiceRequestController.kt */
    public static final class Builder {
        private final CommandQueue commandQueue;
        private final CommandRegistry commandRegistry;

        @Inject
        public Builder(CommandQueue commandQueue2, CommandRegistry commandRegistry2) {
            Intrinsics.checkNotNullParameter(commandQueue2, "commandQueue");
            Intrinsics.checkNotNullParameter(commandRegistry2, "commandRegistry");
            this.commandQueue = commandQueue2;
            this.commandRegistry = commandRegistry2;
        }

        public final TileServiceRequestController create(QSTileHost qSTileHost) {
            Intrinsics.checkNotNullParameter(qSTileHost, "qsTileHost");
            return new TileServiceRequestController(qSTileHost, this.commandQueue, this.commandRegistry, new TileRequestDialogEventLogger(), (Function0) null, 16, (DefaultConstructorMarker) null);
        }
    }
}
