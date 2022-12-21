package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.ArraySet;
import android.util.Log;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t*\u0001\u001e\u0018\u0000 F2\u00020\u0001:\u0006EFGHIJB-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0006\u0010(\u001a\u00020)J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u001cH\u0002J\b\u0010+\u001a\u00020)H\u0016J\u0006\u0010,\u001a\u00020)J\u000e\u0010-\u001a\u00020)2\u0006\u0010.\u001a\u00020/J\b\u00100\u001a\u00020)H\u0002J\u0014\u00101\u001a\u00020)2\n\u00102\u001a\u00060\u001aR\u00020\u0000H\u0002J\u000e\u00103\u001a\u00020)2\u0006\u00104\u001a\u000205J\u000e\u00106\u001a\u00020)2\u0006\u00104\u001a\u000205J\u0016\u00107\u001a\u00020)2\u0006\u00108\u001a\u00020\u000e2\u0006\u00109\u001a\u00020:J\u001c\u0010;\u001a\u00020)2\f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u000e0=2\u0006\u00104\u001a\u00020>J\u0014\u0010?\u001a\u00020)2\n\u00102\u001a\u00060\u001aR\u00020\u0000H\u0002J\u0016\u0010@\u001a\u00020)2\u0006\u0010.\u001a\u00020/2\u0006\u0010A\u001a\u00020BJ\b\u0010C\u001a\u00020\u000eH\u0016J\u0006\u0010D\u001a\u00020)R\u0016\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\f\u0012\b\u0012\u00060\u001aR\u00020\u00000\u00198\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0004\n\u0002\u0010\u001fR\u0011\u0010 \u001a\u00020!¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u000e¢\u0006\u0002\n\u0000¨\u0006K"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "Landroid/os/IBinder$DeathRecipient;", "context", "Landroid/content/Context;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "actionCallbackService", "Landroid/service/controls/IControlsActionCallback$Stub;", "user", "Landroid/os/UserHandle;", "componentName", "Landroid/content/ComponentName;", "(Landroid/content/Context;Lcom/android/systemui/util/concurrency/DelayableExecutor;Landroid/service/controls/IControlsActionCallback$Stub;Landroid/os/UserHandle;Landroid/content/ComponentName;)V", "TAG", "", "kotlin.jvm.PlatformType", "bindTryCount", "", "getComponentName", "()Landroid/content/ComponentName;", "intent", "Landroid/content/Intent;", "onLoadCanceller", "Ljava/lang/Runnable;", "queuedServiceMethods", "", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "requiresBound", "", "serviceConnection", "com/android/systemui/controls/controller/ControlsProviderLifecycleManager$serviceConnection$1", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$serviceConnection$1;", "token", "Landroid/os/IBinder;", "getToken", "()Landroid/os/IBinder;", "getUser", "()Landroid/os/UserHandle;", "wrapper", "Lcom/android/systemui/controls/controller/ServiceWrapper;", "bindService", "", "bind", "binderDied", "cancelLoadTimeout", "cancelSubscription", "subscription", "Landroid/service/controls/IControlsSubscription;", "handlePendingServiceMethods", "invokeOrQueue", "sm", "maybeBindAndLoad", "subscriber", "Landroid/service/controls/IControlsSubscriber$Stub;", "maybeBindAndLoadSuggested", "maybeBindAndSendAction", "controlId", "action", "Landroid/service/controls/actions/ControlAction;", "maybeBindAndSubscribe", "controlIds", "", "Landroid/service/controls/IControlsSubscriber;", "queueServiceMethod", "startSubscription", "requestLimit", "", "toString", "unbindService", "Action", "Companion", "Load", "ServiceMethod", "Subscribe", "Suggest", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsProviderLifecycleManager.kt */
public final class ControlsProviderLifecycleManager implements IBinder.DeathRecipient {
    private static final int BIND_FLAGS = 67109121;
    private static final long BIND_RETRY_DELAY = 1000;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG = true;
    private static final long LOAD_TIMEOUT_SECONDS = 20;
    private static final int MAX_BIND_RETRIES = 5;
    /* access modifiers changed from: private */
    public final String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public final IControlsActionCallback.Stub actionCallbackService;
    /* access modifiers changed from: private */
    public int bindTryCount;
    private final ComponentName componentName;
    /* access modifiers changed from: private */
    public final Context context;
    private final DelayableExecutor executor;
    private final Intent intent;
    private Runnable onLoadCanceller;
    private final Set<ServiceMethod> queuedServiceMethods = new ArraySet();
    private boolean requiresBound;
    private final ControlsProviderLifecycleManager$serviceConnection$1 serviceConnection;
    private final IBinder token;
    private final UserHandle user;
    /* access modifiers changed from: private */
    public ServiceWrapper wrapper;

    public ControlsProviderLifecycleManager(Context context2, DelayableExecutor delayableExecutor, IControlsActionCallback.Stub stub, UserHandle userHandle, ComponentName componentName2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        Intrinsics.checkNotNullParameter(stub, "actionCallbackService");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        this.context = context2;
        this.executor = delayableExecutor;
        this.actionCallbackService = stub;
        this.user = userHandle;
        this.componentName = componentName2;
        IBinder binder = new Binder();
        this.token = binder;
        Intent intent2 = new Intent();
        intent2.setComponent(componentName2);
        Bundle bundle = new Bundle();
        bundle.putBinder("CALLBACK_TOKEN", binder);
        Unit unit = Unit.INSTANCE;
        intent2.putExtra("CALLBACK_BUNDLE", bundle);
        this.intent = intent2;
        this.serviceConnection = new ControlsProviderLifecycleManager$serviceConnection$1(this);
    }

    public final UserHandle getUser() {
        return this.user;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final IBinder getToken() {
        return this.token;
    }

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$Companion;", "", "()V", "BIND_FLAGS", "", "BIND_RETRY_DELAY", "", "DEBUG", "", "LOAD_TIMEOUT_SECONDS", "MAX_BIND_RETRIES", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void bindService(boolean z) {
        this.executor.execute(new ControlsProviderLifecycleManager$$ExternalSyntheticLambda2(this, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: bindService$lambda-3  reason: not valid java name */
    public static final void m2627bindService$lambda3(ControlsProviderLifecycleManager controlsProviderLifecycleManager, boolean z) {
        Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
        controlsProviderLifecycleManager.requiresBound = z;
        if (!z) {
            Log.d(controlsProviderLifecycleManager.TAG, "Unbinding service " + controlsProviderLifecycleManager.intent);
            controlsProviderLifecycleManager.bindTryCount = 0;
            if (controlsProviderLifecycleManager.wrapper != null) {
                controlsProviderLifecycleManager.context.unbindService(controlsProviderLifecycleManager.serviceConnection);
            }
            controlsProviderLifecycleManager.wrapper = null;
        } else if (controlsProviderLifecycleManager.bindTryCount != 5) {
            Log.d(controlsProviderLifecycleManager.TAG, "Binding service " + controlsProviderLifecycleManager.intent);
            controlsProviderLifecycleManager.bindTryCount++;
            try {
                if (!controlsProviderLifecycleManager.context.bindServiceAsUser(controlsProviderLifecycleManager.intent, controlsProviderLifecycleManager.serviceConnection, BIND_FLAGS, controlsProviderLifecycleManager.user)) {
                    controlsProviderLifecycleManager.context.unbindService(controlsProviderLifecycleManager.serviceConnection);
                }
            } catch (SecurityException e) {
                Log.e(controlsProviderLifecycleManager.TAG, "Failed to bind to service", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void handlePendingServiceMethods() {
        ArraySet<ServiceMethod> arraySet;
        synchronized (this.queuedServiceMethods) {
            arraySet = new ArraySet<>(this.queuedServiceMethods);
            this.queuedServiceMethods.clear();
        }
        for (ServiceMethod run : arraySet) {
            run.run();
        }
    }

    public void binderDied() {
        if (this.wrapper != null) {
            this.wrapper = null;
            if (this.requiresBound) {
                Log.d(this.TAG, "binderDied");
            }
        }
    }

    /* access modifiers changed from: private */
    public final void queueServiceMethod(ServiceMethod serviceMethod) {
        synchronized (this.queuedServiceMethods) {
            this.queuedServiceMethods.add(serviceMethod);
        }
    }

    private final void invokeOrQueue(ServiceMethod serviceMethod) {
        Unit unit;
        if (this.wrapper != null) {
            serviceMethod.run();
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this;
            queueServiceMethod(serviceMethod);
            bindService(true);
        }
    }

    public final void maybeBindAndLoad(IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(stub, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new ControlsProviderLifecycleManager$$ExternalSyntheticLambda1(this, stub), LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        invokeOrQueue(new Load(this, stub));
    }

    /* access modifiers changed from: private */
    /* renamed from: maybeBindAndLoad$lambda-10  reason: not valid java name */
    public static final void m2628maybeBindAndLoad$lambda10(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
        Intrinsics.checkNotNullParameter(stub, "$subscriber");
        Log.d(controlsProviderLifecycleManager.TAG, "Timeout waiting onLoad for " + controlsProviderLifecycleManager.componentName);
        stub.onError(controlsProviderLifecycleManager.token, "Timeout waiting onLoad");
        controlsProviderLifecycleManager.unbindService();
    }

    public final void maybeBindAndLoadSuggested(IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(stub, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new ControlsProviderLifecycleManager$$ExternalSyntheticLambda0(this, stub), LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        invokeOrQueue(new Suggest(this, stub));
    }

    /* access modifiers changed from: private */
    /* renamed from: maybeBindAndLoadSuggested$lambda-11  reason: not valid java name */
    public static final void m2629maybeBindAndLoadSuggested$lambda11(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
        Intrinsics.checkNotNullParameter(stub, "$subscriber");
        Log.d(controlsProviderLifecycleManager.TAG, "Timeout waiting onLoadSuggested for " + controlsProviderLifecycleManager.componentName);
        stub.onError(controlsProviderLifecycleManager.token, "Timeout waiting onLoadSuggested");
        controlsProviderLifecycleManager.unbindService();
    }

    public final void cancelLoadTimeout() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
    }

    public final void maybeBindAndSubscribe(List<String> list, IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(list, "controlIds");
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        invokeOrQueue(new Subscribe(this, list, iControlsSubscriber));
    }

    public final void maybeBindAndSendAction(String str, ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        invokeOrQueue(new Action(this, str, controlAction));
    }

    public final void startSubscription(IControlsSubscription iControlsSubscription, long j) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        Log.d(this.TAG, "startSubscription: " + iControlsSubscription);
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.request(iControlsSubscription, j);
        }
    }

    public final void cancelSubscription(IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        Log.d(this.TAG, "cancelSubscription: " + iControlsSubscription);
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.cancel(iControlsSubscription);
        }
    }

    public final void bindService() {
        bindService(true);
    }

    public final void unbindService() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
        bindService(false);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ControlsProviderLifecycleManager(");
        sb.append("component=" + this.componentName);
        sb.append(", user=" + this.user);
        sb.append(NavigationBarInflaterView.KEY_CODE_END);
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(\"ControlsP…\")\")\n        }.toString()");
        return sb2;
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b¦\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\r\u0010\u0003\u001a\u00020\u0004H ¢\u0006\u0002\b\u0005J\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "", "(Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;)V", "callWrapper", "", "callWrapper$SystemUI_nothingRelease", "run", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public abstract class ServiceMethod {
        public abstract boolean callWrapper$SystemUI_nothingRelease();

        public ServiceMethod() {
        }

        public final void run() {
            if (!callWrapper$SystemUI_nothingRelease()) {
                ControlsProviderLifecycleManager.this.queueServiceMethod(this);
                ControlsProviderLifecycleManager.this.binderDied();
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\r\u0010\b\u001a\u00020\tH\u0010¢\u0006\u0002\b\nR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$Load;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "subscriber", "Landroid/service/controls/IControlsSubscriber$Stub;", "(Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;Landroid/service/controls/IControlsSubscriber$Stub;)V", "getSubscriber", "()Landroid/service/controls/IControlsSubscriber$Stub;", "callWrapper", "", "callWrapper$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Load extends ServiceMethod {
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Load(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
            super();
            Intrinsics.checkNotNullParameter(stub, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.subscriber = stub;
        }

        public final IControlsSubscriber.Stub getSubscriber() {
            return this.subscriber;
        }

        public boolean callWrapper$SystemUI_nothingRelease() {
            Log.d(this.this$0.TAG, "load " + this.this$0.getComponentName());
            ServiceWrapper access$getWrapper$p = this.this$0.wrapper;
            if (access$getWrapper$p != null) {
                return access$getWrapper$p.load(this.subscriber);
            }
            return false;
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\r\u0010\b\u001a\u00020\tH\u0010¢\u0006\u0002\b\nR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$Suggest;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "subscriber", "Landroid/service/controls/IControlsSubscriber$Stub;", "(Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;Landroid/service/controls/IControlsSubscriber$Stub;)V", "getSubscriber", "()Landroid/service/controls/IControlsSubscriber$Stub;", "callWrapper", "", "callWrapper$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Suggest extends ServiceMethod {
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Suggest(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
            super();
            Intrinsics.checkNotNullParameter(stub, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.subscriber = stub;
        }

        public final IControlsSubscriber.Stub getSubscriber() {
            return this.subscriber;
        }

        public boolean callWrapper$SystemUI_nothingRelease() {
            Log.d(this.this$0.TAG, "suggest " + this.this$0.getComponentName());
            ServiceWrapper access$getWrapper$p = this.this$0.wrapper;
            if (access$getWrapper$p != null) {
                return access$getWrapper$p.loadSuggested(this.subscriber);
            }
            return false;
        }
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\r\u0010\r\u001a\u00020\u000eH\u0010¢\u0006\u0002\b\u000fR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$Subscribe;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "list", "", "", "subscriber", "Landroid/service/controls/IControlsSubscriber;", "(Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;Ljava/util/List;Landroid/service/controls/IControlsSubscriber;)V", "getList", "()Ljava/util/List;", "getSubscriber", "()Landroid/service/controls/IControlsSubscriber;", "callWrapper", "", "callWrapper$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Subscribe extends ServiceMethod {
        private final List<String> list;
        private final IControlsSubscriber subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Subscribe(ControlsProviderLifecycleManager controlsProviderLifecycleManager, List<String> list2, IControlsSubscriber iControlsSubscriber) {
            super();
            Intrinsics.checkNotNullParameter(list2, "list");
            Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.list = list2;
            this.subscriber = iControlsSubscriber;
        }

        public final List<String> getList() {
            return this.list;
        }

        public final IControlsSubscriber getSubscriber() {
            return this.subscriber;
        }

        public boolean callWrapper$SystemUI_nothingRelease() {
            Log.d(this.this$0.TAG, "subscribe " + this.this$0.getComponentName() + " - " + this.list);
            ServiceWrapper access$getWrapper$p = this.this$0.wrapper;
            if (access$getWrapper$p != null) {
                return access$getWrapper$p.subscribe(this.list, this.subscriber);
            }
            return false;
        }
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\r\u0010\f\u001a\u00020\rH\u0010¢\u0006\u0002\b\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$Action;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager$ServiceMethod;", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "id", "", "action", "Landroid/service/controls/actions/ControlAction;", "(Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;Ljava/lang/String;Landroid/service/controls/actions/ControlAction;)V", "getAction", "()Landroid/service/controls/actions/ControlAction;", "getId", "()Ljava/lang/String;", "callWrapper", "", "callWrapper$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Action extends ServiceMethod {
        private final ControlAction action;

        /* renamed from: id */
        private final String f298id;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Action(ControlsProviderLifecycleManager controlsProviderLifecycleManager, String str, ControlAction controlAction) {
            super();
            Intrinsics.checkNotNullParameter(str, "id");
            Intrinsics.checkNotNullParameter(controlAction, "action");
            this.this$0 = controlsProviderLifecycleManager;
            this.f298id = str;
            this.action = controlAction;
        }

        public final ControlAction getAction() {
            return this.action;
        }

        public final String getId() {
            return this.f298id;
        }

        public boolean callWrapper$SystemUI_nothingRelease() {
            Log.d(this.this$0.TAG, "onAction " + this.this$0.getComponentName() + " - " + this.f298id);
            ServiceWrapper access$getWrapper$p = this.this$0.wrapper;
            if (access$getWrapper$p != null) {
                return access$getWrapper$p.action(this.f298id, this.action, this.this$0.actionCallbackService);
            }
            return false;
        }
    }
}
