package com.android.systemui.controls.controller;

import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsProvider;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.ControlActionWrapper;
import android.util.Log;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ServiceWrapper.kt */
/* loaded from: classes.dex */
public final class ServiceWrapper {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final IControlsProvider service;

    /* compiled from: ServiceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ServiceWrapper(@NotNull IControlsProvider service) {
        Intrinsics.checkNotNullParameter(service, "service");
        this.service = service;
    }

    @NotNull
    public final IControlsProvider getService() {
        return this.service;
    }

    public final boolean load(@NotNull IControlsSubscriber subscriber) {
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        try {
            getService().load(subscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean loadSuggested(@NotNull IControlsSubscriber subscriber) {
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        try {
            getService().loadSuggested(subscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean subscribe(@NotNull List<String> controlIds, @NotNull IControlsSubscriber subscriber) {
        Intrinsics.checkNotNullParameter(controlIds, "controlIds");
        Intrinsics.checkNotNullParameter(subscriber, "subscriber");
        try {
            getService().subscribe(controlIds, subscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean request(@NotNull IControlsSubscription subscription, long j) {
        Intrinsics.checkNotNullParameter(subscription, "subscription");
        try {
            subscription.request(j);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean cancel(@NotNull IControlsSubscription subscription) {
        Intrinsics.checkNotNullParameter(subscription, "subscription");
        try {
            subscription.cancel();
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean action(@NotNull String controlId, @NotNull ControlAction action, @NotNull IControlsActionCallback cb) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(cb, "cb");
        try {
            getService().action(controlId, new ControlActionWrapper(action), cb);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }
}
