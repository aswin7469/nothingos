package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
public final class StatefulControlSubscriber extends IControlsSubscriber.Stub {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final DelayableExecutor bgExecutor;
    @NotNull
    private final ControlsController controller;
    @NotNull
    private final ControlsProviderLifecycleManager provider;
    private final long requestLimit;
    @Nullable
    private IControlsSubscription subscription;
    private boolean subscriptionOpen;

    public StatefulControlSubscriber(@NotNull ControlsController controller, @NotNull ControlsProviderLifecycleManager provider, @NotNull DelayableExecutor bgExecutor, long j) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        Intrinsics.checkNotNullParameter(provider, "provider");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        this.controller = controller;
        this.provider = provider;
        this.bgExecutor = bgExecutor;
        this.requestLimit = j;
    }

    /* compiled from: StatefulControlSubscriber.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void run(IBinder iBinder, final Function0<Unit> function0) {
        if (Intrinsics.areEqual(this.provider.getToken(), iBinder)) {
            this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$run$1
                @Override // java.lang.Runnable
                public final void run() {
                    function0.mo1951invoke();
                }
            });
        }
    }

    public void onSubscribe(@NotNull IBinder token, @NotNull IControlsSubscription subs) {
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(subs, "subs");
        run(token, new StatefulControlSubscriber$onSubscribe$1(this, subs));
    }

    public void onNext(@NotNull IBinder token, @NotNull Control control) {
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(control, "control");
        run(token, new StatefulControlSubscriber$onNext$1(this, token, control));
    }

    public void onError(@NotNull IBinder token, @NotNull String error) {
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(error, "error");
        run(token, new StatefulControlSubscriber$onError$1(this, error));
    }

    public void onComplete(@NotNull IBinder token) {
        Intrinsics.checkNotNullParameter(token, "token");
        run(token, new StatefulControlSubscriber$onComplete$1(this));
    }

    public final void cancel() {
        if (!this.subscriptionOpen) {
            return;
        }
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$cancel$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                IControlsSubscription iControlsSubscription;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager;
                z = StatefulControlSubscriber.this.subscriptionOpen;
                if (z) {
                    StatefulControlSubscriber.this.subscriptionOpen = false;
                    iControlsSubscription = StatefulControlSubscriber.this.subscription;
                    if (iControlsSubscription != null) {
                        controlsProviderLifecycleManager = StatefulControlSubscriber.this.provider;
                        controlsProviderLifecycleManager.cancelSubscription(iControlsSubscription);
                    }
                    StatefulControlSubscriber.this.subscription = null;
                }
            }
        });
    }
}
