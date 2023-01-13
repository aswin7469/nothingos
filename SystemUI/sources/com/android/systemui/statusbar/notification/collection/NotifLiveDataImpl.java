package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import androidx.lifecycle.Observer;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.service.dagger.ObservableServiceModule;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0016\u0010\u0017\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0016J\b\u0010\u001b\u001a\u00020\u0018H\u0002J\u0016\u0010\u001c\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0016J\u0019\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00180\u001e2\u0006\u0010\u0012\u001a\u00028\u0000¢\u0006\u0002\u0010\u001fR\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b0\nX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00018\u00008\u00000\rX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u0004\u0018\u00018\u0000X\u000e¢\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b0\nX\u0004¢\u0006\u0002\n\u0000R$\u0010\u0012\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00028\u00008V@VX\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016¨\u0006 "}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataImpl;", "T", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "name", "", "initialValue", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Ljava/lang/String;Ljava/lang/Object;Ljava/util/concurrent/Executor;)V", "asyncObservers", "Lcom/android/systemui/util/ListenerSet;", "Landroidx/lifecycle/Observer;", "atomicValue", "Ljava/util/concurrent/atomic/AtomicReference;", "kotlin.jvm.PlatformType", "lastAsyncValue", "Ljava/lang/Object;", "syncObservers", "value", "getValue", "()Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;)V", "addAsyncObserver", "", "observer", "addSyncObserver", "dispatchToAsyncObservers", "removeObserver", "setValueAndProvideDispatcher", "Lkotlin/Function0;", "(Ljava/lang/Object;)Lkotlin/jvm/functions/Function0;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifLiveDataStoreImpl.kt */
public final class NotifLiveDataImpl<T> implements NotifLiveData<T> {
    /* access modifiers changed from: private */
    public final ListenerSet<Observer<T>> asyncObservers = new ListenerSet<>();
    private final AtomicReference<T> atomicValue;
    private T lastAsyncValue;
    /* access modifiers changed from: private */
    public final Executor mainExecutor;
    /* access modifiers changed from: private */
    public final String name;
    /* access modifiers changed from: private */
    public final ListenerSet<Observer<T>> syncObservers = new ListenerSet<>();

    public NotifLiveDataImpl(String str, T t, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.name = str;
        this.mainExecutor = executor;
        this.atomicValue = new AtomicReference<>(t);
    }

    /* access modifiers changed from: private */
    public final void dispatchToAsyncObservers() {
        T t = this.atomicValue.get();
        if (!Intrinsics.areEqual((Object) this.lastAsyncValue, (Object) t)) {
            this.lastAsyncValue = t;
            Trace.beginSection("NotifLiveData(" + this.name + ").dispatchToAsyncObservers");
            try {
                for (Observer onChanged : this.asyncObservers) {
                    onChanged.onChanged(t);
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
    }

    public T getValue() {
        return this.atomicValue.get();
    }

    public void setValue(T t) {
        setValueAndProvideDispatcher(t).invoke();
    }

    public final Function0<Unit> setValueAndProvideDispatcher(T t) {
        return !Intrinsics.areEqual((Object) this.atomicValue.getAndSet(t), (Object) t) ? new NotifLiveDataImpl$setValueAndProvideDispatcher$1(this, t) : NotifLiveDataImpl$setValueAndProvideDispatcher$2.INSTANCE;
    }

    public void addSyncObserver(Observer<T> observer) {
        Intrinsics.checkNotNullParameter(observer, ObservableServiceModule.OBSERVER);
        this.syncObservers.addIfAbsent(observer);
    }

    public void addAsyncObserver(Observer<T> observer) {
        Intrinsics.checkNotNullParameter(observer, ObservableServiceModule.OBSERVER);
        this.asyncObservers.addIfAbsent(observer);
    }

    public void removeObserver(Observer<T> observer) {
        Intrinsics.checkNotNullParameter(observer, ObservableServiceModule.OBSERVER);
        this.syncObservers.remove(observer);
        this.asyncObservers.remove(observer);
    }
}
