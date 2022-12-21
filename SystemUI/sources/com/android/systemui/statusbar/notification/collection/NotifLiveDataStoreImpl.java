package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.Assert;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000e0\rR\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u000bX\u0004¢\u0006\u0002\n\u0000R \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u000bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\tR\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00120\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStoreImpl;", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Ljava/util/concurrent/Executor;)V", "activeNotifCount", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "", "getActiveNotifCount", "()Lcom/android/systemui/statusbar/notification/collection/NotifLiveData;", "activeNotifCountPrivate", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataImpl;", "activeNotifList", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getActiveNotifList", "activeNotifListPrivate", "hasActiveNotifs", "", "getHasActiveNotifs", "hasActiveNotifsPrivate", "setActiveNotifList", "", "flatEntryList", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifLiveDataStoreImpl.kt */
public final class NotifLiveDataStoreImpl implements NotifLiveDataStore {
    private final NotifLiveData<Integer> activeNotifCount;
    private final NotifLiveDataImpl<Integer> activeNotifCountPrivate;
    private final NotifLiveData<List<NotificationEntry>> activeNotifList;
    private final NotifLiveDataImpl<List<NotificationEntry>> activeNotifListPrivate;
    private final NotifLiveData<Boolean> hasActiveNotifs;
    private final NotifLiveDataImpl<Boolean> hasActiveNotifsPrivate;
    private final Executor mainExecutor;

    @Inject
    public NotifLiveDataStoreImpl(@Main Executor executor) {
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.mainExecutor = executor;
        NotifLiveDataImpl<Boolean> notifLiveDataImpl = new NotifLiveDataImpl<>("hasActiveNotifs", false, executor);
        this.hasActiveNotifsPrivate = notifLiveDataImpl;
        NotifLiveDataImpl<Integer> notifLiveDataImpl2 = new NotifLiveDataImpl<>("activeNotifCount", 0, executor);
        this.activeNotifCountPrivate = notifLiveDataImpl2;
        NotifLiveDataImpl<List<NotificationEntry>> notifLiveDataImpl3 = new NotifLiveDataImpl<>("activeNotifList", CollectionsKt.emptyList(), executor);
        this.activeNotifListPrivate = notifLiveDataImpl3;
        this.hasActiveNotifs = notifLiveDataImpl;
        this.activeNotifCount = notifLiveDataImpl2;
        this.activeNotifList = notifLiveDataImpl3;
    }

    public NotifLiveData<Boolean> getHasActiveNotifs() {
        return this.hasActiveNotifs;
    }

    public NotifLiveData<Integer> getActiveNotifCount() {
        return this.activeNotifCount;
    }

    public NotifLiveData<List<NotificationEntry>> getActiveNotifList() {
        return this.activeNotifList;
    }

    public final void setActiveNotifList(List<NotificationEntry> list) {
        Intrinsics.checkNotNullParameter(list, "flatEntryList");
        Trace.beginSection("NotifLiveDataStore.setActiveNotifList");
        try {
            Assert.isMainThread();
            List unmodifiableList = Collections.unmodifiableList(CollectionsKt.toList(list));
            Function0[] function0Arr = new Function0[3];
            NotifLiveDataImpl<List<NotificationEntry>> notifLiveDataImpl = this.activeNotifListPrivate;
            Intrinsics.checkNotNullExpressionValue(unmodifiableList, "unmodifiableCopy");
            boolean z = false;
            function0Arr[0] = notifLiveDataImpl.setValueAndProvideDispatcher(unmodifiableList);
            function0Arr[1] = this.activeNotifCountPrivate.setValueAndProvideDispatcher(Integer.valueOf(unmodifiableList.size()));
            NotifLiveDataImpl<Boolean> notifLiveDataImpl2 = this.hasActiveNotifsPrivate;
            if (!unmodifiableList.isEmpty()) {
                z = true;
            }
            function0Arr[2] = notifLiveDataImpl2.setValueAndProvideDispatcher(Boolean.valueOf(z));
            for (Function0 invoke : CollectionsKt.listOf(function0Arr)) {
                invoke.invoke();
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
