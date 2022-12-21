package com.android.systemui.statusbar.notification.collection;

import androidx.lifecycle.Observer;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\u0016\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH&J\u0016\u0010\n\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH&J\u0016\u0010\u000b\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH&R\u0012\u0010\u0003\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "T", "", "value", "getValue", "()Ljava/lang/Object;", "addAsyncObserver", "", "observer", "Landroidx/lifecycle/Observer;", "addSyncObserver", "removeObserver", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifLiveDataStore.kt */
public interface NotifLiveData<T> {
    void addAsyncObserver(Observer<T> observer);

    void addSyncObserver(Observer<T> observer);

    T getValue();

    void removeObserver(Observer<T> observer);
}
