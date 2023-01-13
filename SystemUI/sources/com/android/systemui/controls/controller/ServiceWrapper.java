package com.android.systemui.controls.controller;

import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsProvider;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.ControlActionWrapper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0017\u0010\u000e\u001a\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\bJ\u000e\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bJ\u001c\u0010\u001c\u001a\u00020\b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\n0\u001e2\u0006\u0010\u0016\u001a\u00020\u0017R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006 "}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ServiceWrapper;", "", "service", "Landroid/service/controls/IControlsProvider;", "(Landroid/service/controls/IControlsProvider;)V", "getService", "()Landroid/service/controls/IControlsProvider;", "action", "", "controlId", "", "Landroid/service/controls/actions/ControlAction;", "cb", "Landroid/service/controls/IControlsActionCallback;", "callThroughService", "block", "Lkotlin/Function0;", "", "cancel", "subscription", "Landroid/service/controls/IControlsSubscription;", "load", "subscriber", "Landroid/service/controls/IControlsSubscriber;", "loadSuggested", "request", "num", "", "subscribe", "controlIds", "", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ServiceWrapper.kt */
public final class ServiceWrapper {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ServiceWrapper";
    private final IControlsProvider service;

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ServiceWrapper$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ServiceWrapper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ServiceWrapper(IControlsProvider iControlsProvider) {
        Intrinsics.checkNotNullParameter(iControlsProvider, NotificationCompat.CATEGORY_SERVICE);
        this.service = iControlsProvider;
    }

    public final IControlsProvider getService() {
        return this.service;
    }

    private final boolean callThroughService(Function0<Unit> function0) {
        try {
            function0.invoke();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean load(IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            this.service.load(iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean loadSuggested(IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            this.service.loadSuggested(iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean subscribe(List<String> list, IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(list, "controlIds");
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            this.service.subscribe(list, iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean request(IControlsSubscription iControlsSubscription, long j) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        try {
            iControlsSubscription.request(j);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean cancel(IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        try {
            iControlsSubscription.cancel();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean action(String str, ControlAction controlAction, IControlsActionCallback iControlsActionCallback) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        Intrinsics.checkNotNullParameter(iControlsActionCallback, "cb");
        try {
            this.service.action(str, new ControlActionWrapper(controlAction), iControlsActionCallback);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Caught exception from ControlsProviderService", e);
            return false;
        }
    }
}
