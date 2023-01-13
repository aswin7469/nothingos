package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0018\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u001e\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00100\u001eH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000¨\u0006 "}, mo65043d2 = {"Lcom/android/systemui/controls/controller/StatefulControlSubscriber;", "Landroid/service/controls/IControlsSubscriber$Stub;", "controller", "Lcom/android/systemui/controls/controller/ControlsController;", "provider", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "bgExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "requestLimit", "", "(Lcom/android/systemui/controls/controller/ControlsController;Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;Lcom/android/systemui/util/concurrency/DelayableExecutor;J)V", "subscription", "Landroid/service/controls/IControlsSubscription;", "subscriptionOpen", "", "cancel", "", "onComplete", "token", "Landroid/os/IBinder;", "onError", "error", "", "onNext", "control", "Landroid/service/controls/Control;", "onSubscribe", "subs", "run", "f", "Lkotlin/Function0;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatefulControlSubscriber.kt */
public final class StatefulControlSubscriber extends IControlsSubscriber.Stub {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "StatefulControlSubscriber";
    private final DelayableExecutor bgExecutor;
    /* access modifiers changed from: private */
    public final ControlsController controller;
    /* access modifiers changed from: private */
    public final ControlsProviderLifecycleManager provider;
    /* access modifiers changed from: private */
    public final long requestLimit;
    /* access modifiers changed from: private */
    public IControlsSubscription subscription;
    /* access modifiers changed from: private */
    public boolean subscriptionOpen;

    public StatefulControlSubscriber(ControlsController controlsController, ControlsProviderLifecycleManager controlsProviderLifecycleManager, DelayableExecutor delayableExecutor, long j) {
        Intrinsics.checkNotNullParameter(controlsController, "controller");
        Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "provider");
        Intrinsics.checkNotNullParameter(delayableExecutor, "bgExecutor");
        this.controller = controlsController;
        this.provider = controlsProviderLifecycleManager;
        this.bgExecutor = delayableExecutor;
        this.requestLimit = j;
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/StatefulControlSubscriber$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StatefulControlSubscriber.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void run(IBinder iBinder, Function0<Unit> function0) {
        if (Intrinsics.areEqual((Object) this.provider.getToken(), (Object) iBinder)) {
            this.bgExecutor.execute(new StatefulControlSubscriber$$ExternalSyntheticLambda0(function0));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: run$lambda-0  reason: not valid java name */
    public static final void m2636run$lambda0(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$f");
        function0.invoke();
    }

    public void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subs");
        run(iBinder, new StatefulControlSubscriber$onSubscribe$1(this, iControlsSubscription));
    }

    public void onNext(IBinder iBinder, Control control) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(control, "control");
        run(iBinder, new StatefulControlSubscriber$onNext$1(this, iBinder, control));
    }

    public void onError(IBinder iBinder, String str) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(str, "error");
        run(iBinder, new StatefulControlSubscriber$onError$1(this, str));
    }

    public void onComplete(IBinder iBinder) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        run(iBinder, new StatefulControlSubscriber$onComplete$1(this));
    }

    public final void cancel() {
        if (this.subscriptionOpen) {
            this.bgExecutor.execute(new StatefulControlSubscriber$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: cancel$lambda-2  reason: not valid java name */
    public static final void m2635cancel$lambda2(StatefulControlSubscriber statefulControlSubscriber) {
        Intrinsics.checkNotNullParameter(statefulControlSubscriber, "this$0");
        if (statefulControlSubscriber.subscriptionOpen) {
            statefulControlSubscriber.subscriptionOpen = false;
            IControlsSubscription iControlsSubscription = statefulControlSubscriber.subscription;
            if (iControlsSubscription != null) {
                statefulControlSubscriber.provider.cancelSubscription(iControlsSubscription);
            }
            statefulControlSubscriber.subscription = null;
        }
    }
}
