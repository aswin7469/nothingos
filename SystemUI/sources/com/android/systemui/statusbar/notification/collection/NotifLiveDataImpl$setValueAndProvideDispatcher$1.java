package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import androidx.lifecycle.Observer;
import com.android.systemui.util.ListenerSetKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "T", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifLiveDataStoreImpl.kt */
final class NotifLiveDataImpl$setValueAndProvideDispatcher$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ T $value;
    final /* synthetic */ NotifLiveDataImpl<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotifLiveDataImpl$setValueAndProvideDispatcher$1(NotifLiveDataImpl<T> notifLiveDataImpl, T t) {
        super(0);
        this.this$0 = notifLiveDataImpl;
        this.$value = t;
    }

    public final void invoke() {
        if (ListenerSetKt.isNotEmpty(this.this$0.syncObservers)) {
            NotifLiveDataImpl<T> notifLiveDataImpl = this.this$0;
            T t = this.$value;
            Trace.beginSection("NotifLiveData(" + this.this$0.name + ").dispatchToSyncObservers");
            try {
                for (Observer onChanged : notifLiveDataImpl.syncObservers) {
                    onChanged.onChanged(t);
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
        if (ListenerSetKt.isNotEmpty(this.this$0.asyncObservers)) {
            this.this$0.mainExecutor.execute(new C2673x39b9cba(this.this$0));
        }
    }
}
