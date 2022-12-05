package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.applications.ServiceListing;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsListingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsListingControllerImpl implements ControlsListingController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private Set<ComponentName> availableComponents;
    @NotNull
    private List<? extends ServiceInfo> availableServices;
    @NotNull
    private final Executor backgroundExecutor;
    @NotNull
    private final Set<ControlsListingController.ControlsListingCallback> callbacks;
    @NotNull
    private final Context context;
    private int currentUserId;
    @NotNull
    private ServiceListing serviceListing;
    @NotNull
    private final Function1<Context, ServiceListing> serviceListingBuilder;
    @NotNull
    private final ServiceListing.Callback serviceListingCallback;
    @NotNull
    private AtomicInteger userChangeInProgress;

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public ControlsListingControllerImpl(@NotNull Context context, @NotNull Executor backgroundExecutor, @NotNull Function1<? super Context, ? extends ServiceListing> serviceListingBuilder, @NotNull UserTracker userTracker) {
        Set<ComponentName> emptySet;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(serviceListingBuilder, "serviceListingBuilder");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.backgroundExecutor = backgroundExecutor;
        this.serviceListingBuilder = serviceListingBuilder;
        this.serviceListing = (ServiceListing) serviceListingBuilder.mo1949invoke(context);
        this.callbacks = new LinkedHashSet();
        emptySet = SetsKt__SetsKt.emptySet();
        this.availableComponents = emptySet;
        this.availableServices = CollectionsKt.emptyList();
        this.userChangeInProgress = new AtomicInteger(0);
        this.currentUserId = userTracker.getUserId();
        ServiceListing.Callback callback = new ServiceListing.Callback() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1
            @Override // com.android.settingslib.applications.ServiceListing.Callback
            public final void onServicesReloaded(List<ServiceInfo> it) {
                final List<ServiceInfo> list;
                Executor executor;
                Intrinsics.checkNotNullExpressionValue(it, "it");
                list = CollectionsKt___CollectionsKt.toList(it);
                final LinkedHashSet linkedHashSet = new LinkedHashSet();
                for (ServiceInfo serviceInfo : list) {
                    linkedHashSet.add(serviceInfo.getComponentName());
                }
                executor = ControlsListingControllerImpl.this.backgroundExecutor;
                final ControlsListingControllerImpl controlsListingControllerImpl = ControlsListingControllerImpl.this;
                executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AtomicInteger atomicInteger;
                        Set set;
                        Set<ControlsListingController.ControlsListingCallback> set2;
                        atomicInteger = ControlsListingControllerImpl.this.userChangeInProgress;
                        if (atomicInteger.get() > 0) {
                            return;
                        }
                        Set<ComponentName> set3 = linkedHashSet;
                        set = ControlsListingControllerImpl.this.availableComponents;
                        if (set3.equals(set)) {
                            return;
                        }
                        Log.d("ControlsListingControllerImpl", Intrinsics.stringPlus("ServiceConfig reloaded, count: ", Integer.valueOf(linkedHashSet.size())));
                        ControlsListingControllerImpl.this.availableComponents = linkedHashSet;
                        ControlsListingControllerImpl.this.availableServices = list;
                        List<ControlsServiceInfo> currentServices = ControlsListingControllerImpl.this.getCurrentServices();
                        set2 = ControlsListingControllerImpl.this.callbacks;
                        for (ControlsListingController.ControlsListingCallback controlsListingCallback : set2) {
                            controlsListingCallback.onServicesUpdated(currentServices);
                        }
                    }
                });
            }
        };
        this.serviceListingCallback = callback;
        Log.d("ControlsListingControllerImpl", "Initializing");
        this.serviceListing.addCallback(callback);
        this.serviceListing.setListening(true);
        this.serviceListing.reload();
    }

    /* compiled from: ControlsListingControllerImpl.kt */
    /* renamed from: com.android.systemui.controls.management.ControlsListingControllerImpl$1  reason: invalid class name */
    /* loaded from: classes.dex */
    /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function1<Context, ServiceListing> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(1, ControlsListingControllerImplKt.class, "createServiceListing", "createServiceListing(Landroid/content/Context;)Lcom/android/settingslib/applications/ServiceListing;", 1);
        }

        @Override // kotlin.jvm.functions.Function1
        @NotNull
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final ServiceListing mo1949invoke(@NotNull Context p0) {
            ServiceListing createServiceListing;
            Intrinsics.checkNotNullParameter(p0, "p0");
            createServiceListing = ControlsListingControllerImplKt.createServiceListing(p0);
            return createServiceListing;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ControlsListingControllerImpl(@NotNull Context context, @NotNull Executor executor, @NotNull UserTracker userTracker) {
        this(context, executor, AnonymousClass1.INSTANCE, userTracker);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
    }

    /* compiled from: ControlsListingControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    public int getCurrentUserId() {
        return this.currentUserId;
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(@NotNull final UserHandle newUser) {
        Intrinsics.checkNotNullParameter(newUser, "newUser");
        this.userChangeInProgress.incrementAndGet();
        this.serviceListing.setListening(false);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$changeUser$1
            @Override // java.lang.Runnable
            public final void run() {
                AtomicInteger atomicInteger;
                Context context;
                Function1 function1;
                ServiceListing serviceListing;
                ServiceListing.Callback callback;
                ServiceListing serviceListing2;
                ServiceListing serviceListing3;
                atomicInteger = ControlsListingControllerImpl.this.userChangeInProgress;
                if (atomicInteger.decrementAndGet() == 0) {
                    ControlsListingControllerImpl.this.currentUserId = newUser.getIdentifier();
                    context = ControlsListingControllerImpl.this.context;
                    Context contextForUser = context.createContextAsUser(newUser, 0);
                    ControlsListingControllerImpl controlsListingControllerImpl = ControlsListingControllerImpl.this;
                    function1 = controlsListingControllerImpl.serviceListingBuilder;
                    Intrinsics.checkNotNullExpressionValue(contextForUser, "contextForUser");
                    controlsListingControllerImpl.serviceListing = (ServiceListing) function1.mo1949invoke(contextForUser);
                    serviceListing = ControlsListingControllerImpl.this.serviceListing;
                    callback = ControlsListingControllerImpl.this.serviceListingCallback;
                    serviceListing.addCallback(callback);
                    serviceListing2 = ControlsListingControllerImpl.this.serviceListing;
                    serviceListing2.setListening(true);
                    serviceListing3 = ControlsListingControllerImpl.this.serviceListing;
                    serviceListing3.reload();
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull final ControlsListingController.ControlsListingCallback listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$addCallback$1
            @Override // java.lang.Runnable
            public final void run() {
                AtomicInteger atomicInteger;
                Set set;
                atomicInteger = ControlsListingControllerImpl.this.userChangeInProgress;
                if (atomicInteger.get() > 0) {
                    ControlsListingControllerImpl.this.addCallback(listener);
                    return;
                }
                List<ControlsServiceInfo> currentServices = ControlsListingControllerImpl.this.getCurrentServices();
                Log.d("ControlsListingControllerImpl", Intrinsics.stringPlus("Subscribing callback, service count: ", Integer.valueOf(currentServices.size())));
                set = ControlsListingControllerImpl.this.callbacks;
                set.add(listener);
                listener.onServicesUpdated(currentServices);
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull final ControlsListingController.ControlsListingCallback listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$removeCallback$1
            @Override // java.lang.Runnable
            public final void run() {
                Set set;
                Log.d("ControlsListingControllerImpl", "Unsubscribing callback");
                set = ControlsListingControllerImpl.this.callbacks;
                set.remove(listener);
            }
        });
    }

    @NotNull
    public List<ControlsServiceInfo> getCurrentServices() {
        List<? extends ServiceInfo> list = this.availableServices;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (ServiceInfo serviceInfo : list) {
            arrayList.add(new ControlsServiceInfo(this.context, serviceInfo));
        }
        return arrayList;
    }

    @Override // com.android.systemui.controls.management.ControlsListingController
    @Nullable
    public CharSequence getAppLabel(@NotNull ComponentName name) {
        Object obj;
        Intrinsics.checkNotNullParameter(name, "name");
        Iterator<T> it = getCurrentServices().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((ControlsServiceInfo) obj).componentName, name)) {
                break;
            }
        }
        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) obj;
        if (controlsServiceInfo == null) {
            return null;
        }
        return controlsServiceInfo.loadLabel();
    }
}
