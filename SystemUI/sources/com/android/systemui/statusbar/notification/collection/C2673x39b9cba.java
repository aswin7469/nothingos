package com.android.systemui.statusbar.notification.collection;

/* renamed from: com.android.systemui.statusbar.notification.collection.NotifLiveDataImpl$setValueAndProvideDispatcher$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2673x39b9cba implements Runnable {
    public final /* synthetic */ NotifLiveDataImpl f$0;

    public /* synthetic */ C2673x39b9cba(NotifLiveDataImpl notifLiveDataImpl) {
        this.f$0 = notifLiveDataImpl;
    }

    public final void run() {
        this.f$0.dispatchToAsyncObservers();
    }
}
