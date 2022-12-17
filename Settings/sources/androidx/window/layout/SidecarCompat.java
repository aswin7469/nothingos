package androidx.window.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.window.core.SpecificationComputer;
import androidx.window.core.Version;
import androidx.window.layout.ExtensionInterfaceCompat;
import androidx.window.sidecar.SidecarDeviceState;
import androidx.window.sidecar.SidecarInterface;
import androidx.window.sidecar.SidecarProvider;
import androidx.window.sidecar.SidecarWindowLayoutInfo;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SidecarCompat.kt */
public final class SidecarCompat implements ExtensionInterfaceCompat {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    private static final String TAG = "SidecarCompat";
    @NotNull
    private final Map<Activity, ComponentCallbacks> componentCallbackMap;
    /* access modifiers changed from: private */
    @Nullable
    public ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallback;
    @Nullable
    private final SidecarInterface sidecar;
    /* access modifiers changed from: private */
    @NotNull
    public final SidecarAdapter sidecarAdapter;
    /* access modifiers changed from: private */
    @NotNull
    public final Map<IBinder, Activity> windowListenerRegisteredContexts;

    public SidecarCompat(@Nullable SidecarInterface sidecarInterface, @NotNull SidecarAdapter sidecarAdapter2) {
        Intrinsics.checkNotNullParameter(sidecarAdapter2, "sidecarAdapter");
        this.sidecar = sidecarInterface;
        this.sidecarAdapter = sidecarAdapter2;
        this.windowListenerRegisteredContexts = new LinkedHashMap();
        this.componentCallbackMap = new LinkedHashMap();
    }

    @Nullable
    public final SidecarInterface getSidecar() {
        return this.sidecar;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public SidecarCompat(@NotNull Context context) {
        this(Companion.getSidecarCompat$window_release(context), new SidecarAdapter((SpecificationComputer.VerificationMode) null, 1, (DefaultConstructorMarker) null));
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void setExtensionCallback(@NotNull ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface) {
        Intrinsics.checkNotNullParameter(extensionCallbackInterface, "extensionCallback");
        this.extensionCallback = new DistinctElementCallback(extensionCallbackInterface);
        SidecarInterface sidecarInterface = this.sidecar;
        if (sidecarInterface != null) {
            sidecarInterface.setSidecarCallback(new DistinctSidecarElementCallback(this.sidecarAdapter, new TranslatingCallback()));
        }
    }

    @NotNull
    public final WindowLayoutInfo getWindowLayoutInfo(@NotNull Activity activity) {
        SidecarDeviceState sidecarDeviceState;
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release == null) {
            return new WindowLayoutInfo(CollectionsKt__CollectionsKt.emptyList());
        }
        SidecarInterface sidecarInterface = this.sidecar;
        SidecarWindowLayoutInfo windowLayoutInfo = sidecarInterface != null ? sidecarInterface.getWindowLayoutInfo(activityWindowToken$window_release) : null;
        SidecarAdapter sidecarAdapter2 = this.sidecarAdapter;
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 == null || (sidecarDeviceState = sidecarInterface2.getDeviceState()) == null) {
            sidecarDeviceState = new SidecarDeviceState();
        }
        return sidecarAdapter2.translate(windowLayoutInfo, sidecarDeviceState);
    }

    public void onWindowLayoutChangeListenerAdded(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release != null) {
            register(activityWindowToken$window_release, activity);
            return;
        }
        activity.getWindow().getDecorView().addOnAttachStateChangeListener(new FirstAttachAdapter(this, activity));
    }

    public final void register(@NotNull IBinder iBinder, @NotNull Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(iBinder, "windowToken");
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.windowListenerRegisteredContexts.put(iBinder, activity);
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 != null) {
            sidecarInterface2.onWindowLayoutChangeListenerAdded(iBinder);
        }
        if (this.windowListenerRegisteredContexts.size() == 1 && (sidecarInterface = this.sidecar) != null) {
            sidecarInterface.onDeviceStateListenersChanged(false);
        }
        ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = this.extensionCallback;
        if (extensionCallbackInterface != null) {
            extensionCallbackInterface.onWindowLayoutChanged(activity, getWindowLayoutInfo(activity));
        }
        registerConfigurationChangeListener(activity);
    }

    private final void registerConfigurationChangeListener(Activity activity) {
        if (this.componentCallbackMap.get(activity) == null) {
            C0458xe5f1d4c7 sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1 = new C0458xe5f1d4c7(this, activity);
            this.componentCallbackMap.put(activity, sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1);
            activity.registerComponentCallbacks(sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1);
        }
    }

    public void onWindowLayoutChangeListenerRemoved(@NotNull Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release != null) {
            SidecarInterface sidecarInterface2 = this.sidecar;
            if (sidecarInterface2 != null) {
                sidecarInterface2.onWindowLayoutChangeListenerRemoved(activityWindowToken$window_release);
            }
            unregisterComponentCallback(activity);
            boolean z = this.windowListenerRegisteredContexts.size() == 1;
            this.windowListenerRegisteredContexts.remove(activityWindowToken$window_release);
            if (z && (sidecarInterface = this.sidecar) != null) {
                sidecarInterface.onDeviceStateListenersChanged(true);
            }
        }
    }

    private final void unregisterComponentCallback(Activity activity) {
        activity.unregisterComponentCallbacks(this.componentCallbackMap.get(activity));
        this.componentCallbackMap.remove(activity);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:51|52|53|54|62|63|64|65|66|(2:68|(2:70|90)(2:71|72))(2:73|74)) */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        return true;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x010d */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x013d A[Catch:{ NoSuchFieldError -> 0x00b9, all -> 0x01c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x014f A[Catch:{ NoSuchFieldError -> 0x00b9, all -> 0x01c3 }] */
    @android.annotation.SuppressLint({"BanUncheckedReflection"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean validateExtensionInterface() {
        /*
            r7 = this;
            r0 = 1
            r1 = 0
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            r3 = 0
            if (r2 == 0) goto L_0x001b
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x001b
            java.lang.String r4 = "setSidecarCallback"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class<androidx.window.sidecar.SidecarInterface$SidecarCallback> r6 = androidx.window.sidecar.SidecarInterface.SidecarCallback.class
            r5[r1] = r6     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            goto L_0x001c
        L_0x001b:
            r2 = r3
        L_0x001c:
            if (r2 == 0) goto L_0x0023
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x01c3 }
            goto L_0x0024
        L_0x0023:
            r2 = r3
        L_0x0024:
            java.lang.Class r4 = java.lang.Void.TYPE     // Catch:{ all -> 0x01c3 }
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x01c3 }
            if (r4 == 0) goto L_0x01ac
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x0033
            r2.getDeviceState()     // Catch:{ all -> 0x01c3 }
        L_0x0033:
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x003a
            r2.onDeviceStateListenersChanged(r0)     // Catch:{ all -> 0x01c3 }
        L_0x003a:
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x0051
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x0051
            java.lang.String r4 = "getWindowLayoutInfo"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class<android.os.IBinder> r6 = android.os.IBinder.class
            r5[r1] = r6     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            goto L_0x0052
        L_0x0051:
            r2 = r3
        L_0x0052:
            if (r2 == 0) goto L_0x0059
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x01c3 }
            goto L_0x005a
        L_0x0059:
            r2 = r3
        L_0x005a:
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r4 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x01c3 }
            if (r4 == 0) goto L_0x0195
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x0079
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x01c3 }
            if (r2 == 0) goto L_0x0079
            java.lang.String r4 = "onWindowLayoutChangeListenerAdded"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class<android.os.IBinder> r6 = android.os.IBinder.class
            r5[r1] = r6     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            goto L_0x007a
        L_0x0079:
            r2 = r3
        L_0x007a:
            if (r2 == 0) goto L_0x0081
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x01c3 }
            goto L_0x0082
        L_0x0081:
            r2 = r3
        L_0x0082:
            java.lang.Class r4 = java.lang.Void.TYPE     // Catch:{ all -> 0x01c3 }
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x01c3 }
            if (r4 == 0) goto L_0x017e
            androidx.window.sidecar.SidecarInterface r7 = r7.sidecar     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x00a1
            java.lang.Class r7 = r7.getClass()     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x00a1
            java.lang.String r2 = "onWindowLayoutChangeListenerRemoved"
            java.lang.Class[] r4 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class<android.os.IBinder> r5 = android.os.IBinder.class
            r4[r1] = r5     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r7 = r7.getMethod(r2, r4)     // Catch:{ all -> 0x01c3 }
            goto L_0x00a2
        L_0x00a1:
            r7 = r3
        L_0x00a2:
            if (r7 == 0) goto L_0x00a8
            java.lang.Class r3 = r7.getReturnType()     // Catch:{ all -> 0x01c3 }
        L_0x00a8:
            java.lang.Class r7 = java.lang.Void.TYPE     // Catch:{ all -> 0x01c3 }
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r7)     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x0167
            androidx.window.sidecar.SidecarDeviceState r7 = new androidx.window.sidecar.SidecarDeviceState     // Catch:{ all -> 0x01c3 }
            r7.<init>()     // Catch:{ all -> 0x01c3 }
            r2 = 3
            r7.posture = r2     // Catch:{ NoSuchFieldError -> 0x00b9 }
            goto L_0x00ed
        L_0x00b9:
            java.lang.Class<androidx.window.sidecar.SidecarDeviceState> r3 = androidx.window.sidecar.SidecarDeviceState.class
            java.lang.String r4 = "setPosture"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class r6 = java.lang.Integer.TYPE     // Catch:{ all -> 0x01c3 }
            r5[r1] = r6     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x01c3 }
            r4[r1] = r5     // Catch:{ all -> 0x01c3 }
            r3.invoke(r7, r4)     // Catch:{ all -> 0x01c3 }
            java.lang.Class<androidx.window.sidecar.SidecarDeviceState> r3 = androidx.window.sidecar.SidecarDeviceState.class
            java.lang.String r4 = "getPosture"
            java.lang.Class[] r5 = new java.lang.Class[r1]     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ all -> 0x01c3 }
            java.lang.Object r7 = r3.invoke(r7, r4)     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x015f
            java.lang.Integer r7 = (java.lang.Integer) r7     // Catch:{ all -> 0x01c3 }
            int r7 = r7.intValue()     // Catch:{ all -> 0x01c3 }
            if (r7 != r2) goto L_0x0157
        L_0x00ed:
            androidx.window.sidecar.SidecarDisplayFeature r7 = new androidx.window.sidecar.SidecarDisplayFeature     // Catch:{ all -> 0x01c3 }
            r7.<init>()     // Catch:{ all -> 0x01c3 }
            android.graphics.Rect r2 = r7.getRect()     // Catch:{ all -> 0x01c3 }
            java.lang.String r3 = "displayFeature.rect"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)     // Catch:{ all -> 0x01c3 }
            r7.setRect(r2)     // Catch:{ all -> 0x01c3 }
            r7.getType()     // Catch:{ all -> 0x01c3 }
            r7.setType(r0)     // Catch:{ all -> 0x01c3 }
            androidx.window.sidecar.SidecarWindowLayoutInfo r2 = new androidx.window.sidecar.SidecarWindowLayoutInfo     // Catch:{ all -> 0x01c3 }
            r2.<init>()     // Catch:{ all -> 0x01c3 }
            java.util.List r7 = r2.displayFeatures     // Catch:{ NoSuchFieldError -> 0x010d }
            goto L_0x01c4
        L_0x010d:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x01c3 }
            r3.<init>()     // Catch:{ all -> 0x01c3 }
            r3.add(r7)     // Catch:{ all -> 0x01c3 }
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r7 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            java.lang.String r4 = "setDisplayFeatures"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x01c3 }
            java.lang.Class<java.util.List> r6 = java.util.List.class
            r5[r1] = r6     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r7 = r7.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ all -> 0x01c3 }
            r4[r1] = r3     // Catch:{ all -> 0x01c3 }
            r7.invoke(r2, r4)     // Catch:{ all -> 0x01c3 }
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r7 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            java.lang.String r4 = "getDisplayFeatures"
            java.lang.Class[] r5 = new java.lang.Class[r1]     // Catch:{ all -> 0x01c3 }
            java.lang.reflect.Method r7 = r7.getMethod(r4, r5)     // Catch:{ all -> 0x01c3 }
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ all -> 0x01c3 }
            java.lang.Object r7 = r7.invoke(r2, r4)     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x014f
            java.util.List r7 = (java.util.List) r7     // Catch:{ all -> 0x01c3 }
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r7)     // Catch:{ all -> 0x01c3 }
            if (r7 == 0) goto L_0x0147
            goto L_0x01c4
        L_0x0147:
            java.lang.Exception r7 = new java.lang.Exception     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = "Invalid display feature getter/setter"
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x014f:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = "null cannot be cast to non-null type kotlin.collections.List<androidx.window.sidecar.SidecarDisplayFeature>"
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x0157:
            java.lang.Exception r7 = new java.lang.Exception     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = "Invalid device posture getter/setter"
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x015f:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = "null cannot be cast to non-null type kotlin.Int"
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x0167:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x01c3 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c3 }
            r0.<init>()     // Catch:{ all -> 0x01c3 }
            java.lang.String r2 = "Illegal return type for 'onWindowLayoutChangeListenerRemoved': "
            r0.append(r2)     // Catch:{ all -> 0x01c3 }
            r0.append(r3)     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x01c3 }
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x017e:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x01c3 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c3 }
            r0.<init>()     // Catch:{ all -> 0x01c3 }
            java.lang.String r3 = "Illegal return type for 'onWindowLayoutChangeListenerAdded': "
            r0.append(r3)     // Catch:{ all -> 0x01c3 }
            r0.append(r2)     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x01c3 }
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x0195:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x01c3 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c3 }
            r0.<init>()     // Catch:{ all -> 0x01c3 }
            java.lang.String r3 = "Illegal return type for 'getWindowLayoutInfo': "
            r0.append(r3)     // Catch:{ all -> 0x01c3 }
            r0.append(r2)     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x01c3 }
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x01ac:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x01c3 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c3 }
            r0.<init>()     // Catch:{ all -> 0x01c3 }
            java.lang.String r3 = "Illegal return type for 'setSidecarCallback': "
            r0.append(r3)     // Catch:{ all -> 0x01c3 }
            r0.append(r2)     // Catch:{ all -> 0x01c3 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x01c3 }
            r7.<init>(r0)     // Catch:{ all -> 0x01c3 }
            throw r7     // Catch:{ all -> 0x01c3 }
        L_0x01c3:
            r0 = r1
        L_0x01c4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.layout.SidecarCompat.validateExtensionInterface():boolean");
    }

    /* compiled from: SidecarCompat.kt */
    private static final class FirstAttachAdapter implements View.OnAttachStateChangeListener {
        @NotNull
        private final WeakReference<Activity> activityWeakReference;
        @NotNull
        private final SidecarCompat sidecarCompat;

        public void onViewDetachedFromWindow(@NotNull View view) {
            Intrinsics.checkNotNullParameter(view, "view");
        }

        public FirstAttachAdapter(@NotNull SidecarCompat sidecarCompat2, @NotNull Activity activity) {
            Intrinsics.checkNotNullParameter(sidecarCompat2, "sidecarCompat");
            Intrinsics.checkNotNullParameter(activity, "activity");
            this.sidecarCompat = sidecarCompat2;
            this.activityWeakReference = new WeakReference<>(activity);
        }

        public void onViewAttachedToWindow(@NotNull View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.removeOnAttachStateChangeListener(this);
            Activity activity = (Activity) this.activityWeakReference.get();
            IBinder activityWindowToken$window_release = SidecarCompat.Companion.getActivityWindowToken$window_release(activity);
            if (activity != null && activityWindowToken$window_release != null) {
                this.sidecarCompat.register(activityWindowToken$window_release, activity);
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    public final class TranslatingCallback implements SidecarInterface.SidecarCallback {
        public TranslatingCallback() {
        }

        @SuppressLint({"SyntheticAccessor"})
        public void onDeviceStateChanged(@NotNull SidecarDeviceState sidecarDeviceState) {
            SidecarInterface sidecar;
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "newDeviceState");
            SidecarCompat sidecarCompat = SidecarCompat.this;
            for (Activity activity : SidecarCompat.this.windowListenerRegisteredContexts.values()) {
                IBinder activityWindowToken$window_release = SidecarCompat.Companion.getActivityWindowToken$window_release(activity);
                SidecarWindowLayoutInfo sidecarWindowLayoutInfo = null;
                if (!(activityWindowToken$window_release == null || (sidecar = sidecarCompat.getSidecar()) == null)) {
                    sidecarWindowLayoutInfo = sidecar.getWindowLayoutInfo(activityWindowToken$window_release);
                }
                ExtensionInterfaceCompat.ExtensionCallbackInterface access$getExtensionCallback$p = sidecarCompat.extensionCallback;
                if (access$getExtensionCallback$p != null) {
                    access$getExtensionCallback$p.onWindowLayoutChanged(activity, sidecarCompat.sidecarAdapter.translate(sidecarWindowLayoutInfo, sidecarDeviceState));
                }
            }
        }

        @SuppressLint({"SyntheticAccessor"})
        public void onWindowLayoutChanged(@NotNull IBinder iBinder, @NotNull SidecarWindowLayoutInfo sidecarWindowLayoutInfo) {
            SidecarDeviceState sidecarDeviceState;
            Intrinsics.checkNotNullParameter(iBinder, "windowToken");
            Intrinsics.checkNotNullParameter(sidecarWindowLayoutInfo, "newLayout");
            Activity activity = (Activity) SidecarCompat.this.windowListenerRegisteredContexts.get(iBinder);
            if (activity == null) {
                Log.w(SidecarCompat.TAG, "Unable to resolve activity from window token. Missing a call to #onWindowLayoutChangeListenerAdded()?");
                return;
            }
            SidecarAdapter access$getSidecarAdapter$p = SidecarCompat.this.sidecarAdapter;
            SidecarInterface sidecar = SidecarCompat.this.getSidecar();
            if (sidecar == null || (sidecarDeviceState = sidecar.getDeviceState()) == null) {
                sidecarDeviceState = new SidecarDeviceState();
            }
            WindowLayoutInfo translate = access$getSidecarAdapter$p.translate(sidecarWindowLayoutInfo, sidecarDeviceState);
            ExtensionInterfaceCompat.ExtensionCallbackInterface access$getExtensionCallback$p = SidecarCompat.this.extensionCallback;
            if (access$getExtensionCallback$p != null) {
                access$getExtensionCallback$p.onWindowLayoutChanged(activity, translate);
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    private static final class DistinctElementCallback implements ExtensionInterfaceCompat.ExtensionCallbackInterface {
        @NotNull
        private final WeakHashMap<Activity, WindowLayoutInfo> activityWindowLayoutInfo = new WeakHashMap<>();
        @NotNull
        private final ExtensionInterfaceCompat.ExtensionCallbackInterface callbackInterface;
        @NotNull
        private final ReentrantLock lock = new ReentrantLock();

        public DistinctElementCallback(@NotNull ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface) {
            Intrinsics.checkNotNullParameter(extensionCallbackInterface, "callbackInterface");
            this.callbackInterface = extensionCallbackInterface;
        }

        public void onWindowLayoutChanged(@NotNull Activity activity, @NotNull WindowLayoutInfo windowLayoutInfo) {
            Intrinsics.checkNotNullParameter(activity, "activity");
            Intrinsics.checkNotNullParameter(windowLayoutInfo, "newLayout");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (!Intrinsics.areEqual(windowLayoutInfo, this.activityWindowLayoutInfo.get(activity))) {
                    WindowLayoutInfo put = this.activityWindowLayoutInfo.put(activity, windowLayoutInfo);
                    reentrantLock.unlock();
                    this.callbackInterface.onWindowLayoutChanged(activity, windowLayoutInfo);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    private static final class DistinctSidecarElementCallback implements SidecarInterface.SidecarCallback {
        @NotNull
        private final SidecarInterface.SidecarCallback callbackInterface;
        @Nullable
        private SidecarDeviceState lastDeviceState;
        @NotNull
        private final ReentrantLock lock = new ReentrantLock();
        @NotNull
        private final WeakHashMap<IBinder, SidecarWindowLayoutInfo> mActivityWindowLayoutInfo = new WeakHashMap<>();
        @NotNull
        private final SidecarAdapter sidecarAdapter;

        public DistinctSidecarElementCallback(@NotNull SidecarAdapter sidecarAdapter2, @NotNull SidecarInterface.SidecarCallback sidecarCallback) {
            Intrinsics.checkNotNullParameter(sidecarAdapter2, "sidecarAdapter");
            Intrinsics.checkNotNullParameter(sidecarCallback, "callbackInterface");
            this.sidecarAdapter = sidecarAdapter2;
            this.callbackInterface = sidecarCallback;
        }

        public void onDeviceStateChanged(@NotNull SidecarDeviceState sidecarDeviceState) {
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "newDeviceState");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (!this.sidecarAdapter.isEqualSidecarDeviceState(this.lastDeviceState, sidecarDeviceState)) {
                    this.lastDeviceState = sidecarDeviceState;
                    this.callbackInterface.onDeviceStateChanged(sidecarDeviceState);
                    Unit unit = Unit.INSTANCE;
                    reentrantLock.unlock();
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        public void onWindowLayoutChanged(@NotNull IBinder iBinder, @NotNull SidecarWindowLayoutInfo sidecarWindowLayoutInfo) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(sidecarWindowLayoutInfo, "newLayout");
            synchronized (this.lock) {
                if (!this.sidecarAdapter.isEqualSidecarWindowLayoutInfo(this.mActivityWindowLayoutInfo.get(iBinder), sidecarWindowLayoutInfo)) {
                    SidecarWindowLayoutInfo put = this.mActivityWindowLayoutInfo.put(iBinder, sidecarWindowLayoutInfo);
                    this.callbackInterface.onWindowLayoutChanged(iBinder, sidecarWindowLayoutInfo);
                }
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @Nullable
        public final Version getSidecarVersion() {
            try {
                String apiVersion = SidecarProvider.getApiVersion();
                if (!TextUtils.isEmpty(apiVersion)) {
                    return Version.Companion.parse(apiVersion);
                }
                return null;
            } catch (NoClassDefFoundError | UnsupportedOperationException unused) {
                return null;
            }
        }

        @Nullable
        public final SidecarInterface getSidecarCompat$window_release(@NotNull Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return SidecarProvider.getSidecarImpl(context.getApplicationContext());
        }

        @Nullable
        public final IBinder getActivityWindowToken$window_release(@Nullable Activity activity) {
            Window window;
            WindowManager.LayoutParams attributes;
            if (activity == null || (window = activity.getWindow()) == null || (attributes = window.getAttributes()) == null) {
                return null;
            }
            return attributes.token;
        }
    }
}
