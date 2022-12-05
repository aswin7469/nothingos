package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsProvider;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsProviderLifecycleManager.kt */
/* loaded from: classes.dex */
public final class ControlsProviderLifecycleManager implements IBinder.DeathRecipient {
    @NotNull
    private final IControlsActionCallback.Stub actionCallbackService;
    private int bindTryCount;
    @NotNull
    private final ComponentName componentName;
    @NotNull
    private final Context context;
    @NotNull
    private final DelayableExecutor executor;
    @NotNull
    private final Intent intent;
    @Nullable
    private Runnable onLoadCanceller;
    private boolean requiresBound;
    @NotNull
    private final UserHandle user;
    @Nullable
    private ServiceWrapper wrapper;
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final int BIND_FLAGS = 67109121;
    @NotNull
    private final IBinder token = new Binder();
    @GuardedBy({"queuedServiceMethods"})
    @NotNull
    private final Set<ServiceMethod> queuedServiceMethods = new ArraySet();
    private final String TAG = ControlsProviderLifecycleManager.class.getSimpleName();
    @NotNull
    private final ControlsProviderLifecycleManager$serviceConnection$1 serviceConnection = new ServiceConnection() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$serviceConnection$1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(@NotNull ComponentName name, @NotNull IBinder service) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(service, "service");
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onServiceConnected ", name));
            ControlsProviderLifecycleManager.this.bindTryCount = 0;
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            IControlsProvider asInterface = IControlsProvider.Stub.asInterface(service);
            Intrinsics.checkNotNullExpressionValue(asInterface, "asInterface(service)");
            controlsProviderLifecycleManager.wrapper = new ServiceWrapper(asInterface);
            try {
                service.linkToDeath(ControlsProviderLifecycleManager.this, 0);
            } catch (RemoteException unused) {
            }
            ControlsProviderLifecycleManager.this.handlePendingServiceMethods();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(@Nullable ComponentName componentName) {
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onServiceDisconnected ", componentName));
            ControlsProviderLifecycleManager.this.wrapper = null;
            ControlsProviderLifecycleManager.this.bindService(false);
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(@Nullable ComponentName componentName) {
            Context context;
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onNullBinding ", componentName));
            ControlsProviderLifecycleManager.this.wrapper = null;
            context = ControlsProviderLifecycleManager.this.context;
            context.unbindService(this);
        }
    };

    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.controls.controller.ControlsProviderLifecycleManager$serviceConnection$1] */
    public ControlsProviderLifecycleManager(@NotNull Context context, @NotNull DelayableExecutor executor, @NotNull IControlsActionCallback.Stub actionCallbackService, @NotNull UserHandle user, @NotNull ComponentName componentName) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(actionCallbackService, "actionCallbackService");
        Intrinsics.checkNotNullParameter(user, "user");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.context = context;
        this.executor = executor;
        this.actionCallbackService = actionCallbackService;
        this.user = user;
        this.componentName = componentName;
        Intent intent = new Intent();
        intent.setComponent(getComponentName());
        Bundle bundle = new Bundle();
        bundle.putBinder("CALLBACK_TOKEN", getToken());
        Unit unit = Unit.INSTANCE;
        intent.putExtra("CALLBACK_BUNDLE", bundle);
        this.intent = intent;
    }

    @NotNull
    public final UserHandle getUser() {
        return this.user;
    }

    @NotNull
    public final ComponentName getComponentName() {
        return this.componentName;
    }

    @NotNull
    public final IBinder getToken() {
        return this.token;
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void bindService(final boolean z) {
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$bindService$1
            @Override // java.lang.Runnable
            public final void run() {
                Intent intent;
                Context context;
                ControlsProviderLifecycleManager$serviceConnection$1 controlsProviderLifecycleManager$serviceConnection$1;
                int i;
                Intent intent2;
                int i2;
                Context context2;
                Intent intent3;
                ControlsProviderLifecycleManager$serviceConnection$1 controlsProviderLifecycleManager$serviceConnection$12;
                int i3;
                ControlsProviderLifecycleManager.this.requiresBound = z;
                if (z) {
                    i = ControlsProviderLifecycleManager.this.bindTryCount;
                    if (i == 5) {
                        return;
                    }
                    String str = ControlsProviderLifecycleManager.this.TAG;
                    intent2 = ControlsProviderLifecycleManager.this.intent;
                    Log.d(str, Intrinsics.stringPlus("Binding service ", intent2));
                    ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
                    i2 = controlsProviderLifecycleManager.bindTryCount;
                    controlsProviderLifecycleManager.bindTryCount = i2 + 1;
                    try {
                        context2 = ControlsProviderLifecycleManager.this.context;
                        intent3 = ControlsProviderLifecycleManager.this.intent;
                        controlsProviderLifecycleManager$serviceConnection$12 = ControlsProviderLifecycleManager.this.serviceConnection;
                        i3 = ControlsProviderLifecycleManager.BIND_FLAGS;
                        context2.bindServiceAsUser(intent3, controlsProviderLifecycleManager$serviceConnection$12, i3, ControlsProviderLifecycleManager.this.getUser());
                        return;
                    } catch (SecurityException e) {
                        Log.e(ControlsProviderLifecycleManager.this.TAG, "Failed to bind to service", e);
                        return;
                    }
                }
                String str2 = ControlsProviderLifecycleManager.this.TAG;
                intent = ControlsProviderLifecycleManager.this.intent;
                Log.d(str2, Intrinsics.stringPlus("Unbinding service ", intent));
                ControlsProviderLifecycleManager.this.bindTryCount = 0;
                if (ControlsProviderLifecycleManager.this.wrapper != null) {
                    ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = ControlsProviderLifecycleManager.this;
                    context = controlsProviderLifecycleManager2.context;
                    controlsProviderLifecycleManager$serviceConnection$1 = controlsProviderLifecycleManager2.serviceConnection;
                    context.unbindService(controlsProviderLifecycleManager$serviceConnection$1);
                }
                ControlsProviderLifecycleManager.this.wrapper = null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handlePendingServiceMethods() {
        ArraySet<ServiceMethod> arraySet;
        synchronized (this.queuedServiceMethods) {
            arraySet = new ArraySet(this.queuedServiceMethods);
            this.queuedServiceMethods.clear();
        }
        for (ServiceMethod serviceMethod : arraySet) {
            serviceMethod.run();
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        if (this.wrapper == null) {
            return;
        }
        this.wrapper = null;
        if (!this.requiresBound) {
            return;
        }
        Log.d(this.TAG, "binderDied");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void queueServiceMethod(ServiceMethod serviceMethod) {
        synchronized (this.queuedServiceMethods) {
            this.queuedServiceMethods.add(serviceMethod);
        }
    }

    private final void invokeOrQueue(ServiceMethod serviceMethod) {
        Unit unit;
        if (this.wrapper == null) {
            unit = null;
        } else {
            serviceMethod.run();
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            queueServiceMethod(serviceMethod);
            bindService(true);
        }
    }

    public final void maybeBindAndLoad(@NotNull final IControlsSubscriber.Stub subscriber) {
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoad$1
            @Override // java.lang.Runnable
            public final void run() {
                Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("Timeout waiting onLoad for ", ControlsProviderLifecycleManager.this.getComponentName()));
                subscriber.onError(ControlsProviderLifecycleManager.this.getToken(), "Timeout waiting onLoad");
                ControlsProviderLifecycleManager.this.unbindService();
            }
        }, 20L, TimeUnit.SECONDS);
        invokeOrQueue(new Load(this, subscriber));
    }

    public final void maybeBindAndLoadSuggested(@NotNull final IControlsSubscriber.Stub subscriber) {
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoadSuggested$1
            @Override // java.lang.Runnable
            public final void run() {
                Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("Timeout waiting onLoadSuggested for ", ControlsProviderLifecycleManager.this.getComponentName()));
                subscriber.onError(ControlsProviderLifecycleManager.this.getToken(), "Timeout waiting onLoadSuggested");
                ControlsProviderLifecycleManager.this.unbindService();
            }
        }, 20L, TimeUnit.SECONDS);
        invokeOrQueue(new Suggest(this, subscriber));
    }

    public final void cancelLoadTimeout() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
    }

    public final void maybeBindAndSubscribe(@NotNull List<String> controlIds, @NotNull IControlsSubscriber subscriber) {
        Intrinsics.checkNotNullParameter(controlIds, "controlIds");
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        invokeOrQueue(new Subscribe(this, controlIds, subscriber));
    }

    public final void maybeBindAndSendAction(@NotNull String controlId, @NotNull ControlAction action) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Intrinsics.checkNotNullParameter(action, "action");
        invokeOrQueue(new Action(this, controlId, action));
    }

    public final void startSubscription(@NotNull IControlsSubscription subscription, long j) {
        Intrinsics.checkNotNullParameter(subscription, "subscription");
        Log.d(this.TAG, Intrinsics.stringPlus("startSubscription: ", subscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper == null) {
            return;
        }
        serviceWrapper.request(subscription, j);
    }

    public final void cancelSubscription(@NotNull IControlsSubscription subscription) {
        Intrinsics.checkNotNullParameter(subscription, "subscription");
        Log.d(this.TAG, Intrinsics.stringPlus("cancelSubscription: ", subscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper == null) {
            return;
        }
        serviceWrapper.cancel(subscription);
    }

    public final void unbindService() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
        bindService(false);
    }

    @NotNull
    public String toString() {
        String str = "ControlsProviderLifecycleManager(" + Intrinsics.stringPlus("component=", getComponentName()) + Intrinsics.stringPlus(", user=", getUser()) + ")";
        Intrinsics.checkNotNullExpressionValue(str, "StringBuilder(\"ControlsProviderLifecycleManager(\").apply {\n            append(\"component=$componentName\")\n            append(\", user=$user\")\n            append(\")\")\n        }.toString()");
        return str;
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public abstract class ServiceMethod {
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        public abstract boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();

        public ServiceMethod(ControlsProviderLifecycleManager this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        public final void run() {
            if (!callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                this.this$0.queueServiceMethod(this);
                this.this$0.binderDied();
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Load extends ServiceMethod {
        @NotNull
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Load(@NotNull ControlsProviderLifecycleManager this$0, IControlsSubscriber.Stub subscriber) {
            super(this$0);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(subscriber, "subscriber");
            this.this$0 = this$0;
            this.subscriber = subscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(this.this$0.TAG, Intrinsics.stringPlus("load ", this.this$0.getComponentName()));
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.load(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Suggest extends ServiceMethod {
        @NotNull
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Suggest(@NotNull ControlsProviderLifecycleManager this$0, IControlsSubscriber.Stub subscriber) {
            super(this$0);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(subscriber, "subscriber");
            this.this$0 = this$0;
            this.subscriber = subscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(this.this$0.TAG, Intrinsics.stringPlus("suggest ", this.this$0.getComponentName()));
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.loadSuggested(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Subscribe extends ServiceMethod {
        @NotNull
        private final List<String> list;
        @NotNull
        private final IControlsSubscriber subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Subscribe(@NotNull ControlsProviderLifecycleManager this$0, @NotNull List<String> list, IControlsSubscriber subscriber) {
            super(this$0);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(subscriber, "subscriber");
            this.this$0 = this$0;
            this.list = list;
            this.subscriber = subscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = this.this$0.TAG;
            Log.d(str, "subscribe " + this.this$0.getComponentName() + " - " + this.list);
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.subscribe(this.list, this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Action extends ServiceMethod {
        @NotNull
        private final ControlAction action;
        @NotNull
        private final String id;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Action(@NotNull ControlsProviderLifecycleManager this$0, @NotNull String id, ControlAction action) {
            super(this$0);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(action, "action");
            this.this$0 = this$0;
            this.id = id;
            this.action = action;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = this.this$0.TAG;
            Log.d(str, "onAction " + this.this$0.getComponentName() + " - " + this.id);
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.action(this.id, this.action, this.this$0.actionCallbackService);
        }
    }
}
