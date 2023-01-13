package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.settingslib.applications.ServiceListing;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 -2\u00020\u0001:\u0001-B\u001f\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bB5\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\t\u001a\u00020\u0005\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000b\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\rJ\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0016H\u0016J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020&H\u0016J\u0012\u0010'\u001a\u0004\u0018\u00010(2\u0006\u0010)\u001a\u00020\u0010H\u0016J\u000e\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u0012H\u0016J\u0010\u0010,\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0016H\u0016R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0017\u001a\u00020\u0018@RX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000¨\u0006."}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsListingControllerImpl;", "Lcom/android/systemui/controls/management/ControlsListingController;", "context", "Landroid/content/Context;", "executor", "Ljava/util/concurrent/Executor;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "(Landroid/content/Context;Ljava/util/concurrent/Executor;Lcom/android/systemui/settings/UserTracker;)V", "backgroundExecutor", "serviceListingBuilder", "Lkotlin/Function1;", "Lcom/android/settingslib/applications/ServiceListing;", "(Landroid/content/Context;Ljava/util/concurrent/Executor;Lkotlin/jvm/functions/Function1;Lcom/android/systemui/settings/UserTracker;)V", "availableComponents", "", "Landroid/content/ComponentName;", "availableServices", "", "Landroid/content/pm/ServiceInfo;", "callbacks", "", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "<set-?>", "", "currentUserId", "getCurrentUserId", "()I", "serviceListing", "serviceListingCallback", "Lcom/android/settingslib/applications/ServiceListing$Callback;", "userChangeInProgress", "Ljava/util/concurrent/atomic/AtomicInteger;", "addCallback", "", "listener", "changeUser", "newUser", "Landroid/os/UserHandle;", "getAppLabel", "", "name", "getCurrentServices", "Lcom/android/systemui/controls/ControlsServiceInfo;", "removeCallback", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsListingControllerImpl.kt */
public final class ControlsListingControllerImpl implements ControlsListingController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ControlsListingControllerImpl";
    private Set<ComponentName> availableComponents;
    private List<? extends ServiceInfo> availableServices;
    private final Executor backgroundExecutor;
    private final Set<ControlsListingController.ControlsListingCallback> callbacks;
    private final Context context;
    private int currentUserId;
    private ServiceListing serviceListing;
    private final Function1<Context, ServiceListing> serviceListingBuilder;
    private final ServiceListing.Callback serviceListingCallback;
    private AtomicInteger userChangeInProgress;

    public ControlsListingControllerImpl(Context context2, @Background Executor executor, Function1<? super Context, ? extends ServiceListing> function1, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(function1, "serviceListingBuilder");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context2;
        this.backgroundExecutor = executor;
        this.serviceListingBuilder = function1;
        this.serviceListing = (ServiceListing) function1.invoke(context2);
        this.callbacks = new LinkedHashSet();
        this.availableComponents = SetsKt.emptySet();
        this.availableServices = CollectionsKt.emptyList();
        this.userChangeInProgress = new AtomicInteger(0);
        this.currentUserId = userTracker.getUserId();
        ControlsListingControllerImpl$$ExternalSyntheticLambda0 controlsListingControllerImpl$$ExternalSyntheticLambda0 = new ControlsListingControllerImpl$$ExternalSyntheticLambda0(this);
        this.serviceListingCallback = controlsListingControllerImpl$$ExternalSyntheticLambda0;
        Log.d(TAG, "Initializing");
        this.serviceListing.addCallback(controlsListingControllerImpl$$ExternalSyntheticLambda0);
        this.serviceListing.setListening(true);
        this.serviceListing.reload();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Inject
    public ControlsListingControllerImpl(Context context2, Executor executor, UserTracker userTracker) {
        this(context2, executor, C20351.INSTANCE, userTracker);
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsListingControllerImpl$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsListingControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public int getCurrentUserId() {
        return this.currentUserId;
    }

    /* access modifiers changed from: private */
    /* renamed from: serviceListingCallback$lambda-3  reason: not valid java name */
    public static final void m2659serviceListingCallback$lambda3(ControlsListingControllerImpl controlsListingControllerImpl, List list) {
        Intrinsics.checkNotNullParameter(controlsListingControllerImpl, "this$0");
        Intrinsics.checkNotNullExpressionValue(list, "it");
        List<ServiceInfo> list2 = CollectionsKt.toList(list);
        Collection linkedHashSet = new LinkedHashSet();
        for (ServiceInfo componentName : list2) {
            linkedHashSet.add(componentName.getComponentName());
        }
        controlsListingControllerImpl.backgroundExecutor.execute(new ControlsListingControllerImpl$$ExternalSyntheticLambda4(controlsListingControllerImpl, (Set) linkedHashSet, list2));
    }

    /* access modifiers changed from: private */
    /* renamed from: serviceListingCallback$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2660serviceListingCallback$lambda3$lambda2(ControlsListingControllerImpl controlsListingControllerImpl, Set set, List list) {
        Intrinsics.checkNotNullParameter(controlsListingControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(set, "$newComponents");
        Intrinsics.checkNotNullParameter(list, "$newServices");
        if (controlsListingControllerImpl.userChangeInProgress.get() <= 0 && !set.equals(controlsListingControllerImpl.availableComponents)) {
            Log.d(TAG, "ServiceConfig reloaded, count: " + set.size());
            controlsListingControllerImpl.availableComponents = set;
            controlsListingControllerImpl.availableServices = list;
            List<ControlsServiceInfo> currentServices = controlsListingControllerImpl.getCurrentServices();
            for (ControlsListingController.ControlsListingCallback onServicesUpdated : controlsListingControllerImpl.callbacks) {
                onServicesUpdated.onServicesUpdated(currentServices);
            }
        }
    }

    public void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
        this.userChangeInProgress.incrementAndGet();
        this.serviceListing.setListening(false);
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$$ExternalSyntheticLambda3(this, userHandle));
    }

    /* access modifiers changed from: private */
    /* renamed from: changeUser$lambda-4  reason: not valid java name */
    public static final void m2657changeUser$lambda4(ControlsListingControllerImpl controlsListingControllerImpl, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(controlsListingControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(userHandle, "$newUser");
        if (controlsListingControllerImpl.userChangeInProgress.decrementAndGet() == 0) {
            controlsListingControllerImpl.currentUserId = userHandle.getIdentifier();
            Context createContextAsUser = controlsListingControllerImpl.context.createContextAsUser(userHandle, 0);
            Function1<Context, ServiceListing> function1 = controlsListingControllerImpl.serviceListingBuilder;
            Intrinsics.checkNotNullExpressionValue(createContextAsUser, "contextForUser");
            ServiceListing invoke = function1.invoke(createContextAsUser);
            controlsListingControllerImpl.serviceListing = invoke;
            invoke.addCallback(controlsListingControllerImpl.serviceListingCallback);
            controlsListingControllerImpl.serviceListing.setListening(true);
            controlsListingControllerImpl.serviceListing.reload();
        }
    }

    public void addCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingCallback, "listener");
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$$ExternalSyntheticLambda2(this, controlsListingCallback));
    }

    /* access modifiers changed from: private */
    /* renamed from: addCallback$lambda-5  reason: not valid java name */
    public static final void m2656addCallback$lambda5(ControlsListingControllerImpl controlsListingControllerImpl, ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingCallback, "$listener");
        if (controlsListingControllerImpl.userChangeInProgress.get() > 0) {
            controlsListingControllerImpl.addCallback(controlsListingCallback);
            return;
        }
        List<ControlsServiceInfo> currentServices = controlsListingControllerImpl.getCurrentServices();
        Log.d(TAG, "Subscribing callback, service count: " + currentServices.size());
        controlsListingControllerImpl.callbacks.add(controlsListingCallback);
        controlsListingCallback.onServicesUpdated(currentServices);
    }

    public void removeCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingCallback, "listener");
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$$ExternalSyntheticLambda1(this, controlsListingCallback));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-6  reason: not valid java name */
    public static final void m2658removeCallback$lambda6(ControlsListingControllerImpl controlsListingControllerImpl, ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingCallback, "$listener");
        Log.d(TAG, "Unsubscribing callback");
        controlsListingControllerImpl.callbacks.remove(controlsListingCallback);
    }

    public List<ControlsServiceInfo> getCurrentServices() {
        Iterable<ServiceInfo> iterable = this.availableServices;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (ServiceInfo controlsServiceInfo : iterable) {
            arrayList.add(new ControlsServiceInfo(this.context, controlsServiceInfo));
        }
        return (List) arrayList;
    }

    public CharSequence getAppLabel(ComponentName componentName) {
        Object obj;
        Intrinsics.checkNotNullParameter(componentName, ZoneGetter.KEY_DISPLAYNAME);
        Iterator it = getCurrentServices().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual((Object) ((ControlsServiceInfo) obj).componentName, (Object) componentName)) {
                break;
            }
        }
        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) obj;
        if (controlsServiceInfo != null) {
            return controlsServiceInfo.loadLabel();
        }
        return null;
    }
}
