package com.android.systemui.statusbar.notification.collection.coalescer;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\fJ\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0005HÆ\u0003J\t\u0010!\u001a\u00020\u0007HÆ\u0003J\t\u0010\"\u001a\u00020\tHÆ\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u000bHÆ\u0003J=\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bHÆ\u0001J\u0013\u0010%\u001a\u00020&2\b\u0010'\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010(\u001a\u00020\u0005HÖ\u0001J\b\u0010)\u001a\u00020\u0003H\u0016R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\b\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coalescer/CoalescedEvent;", "", "key", "", "position", "", "sbn", "Landroid/service/notification/StatusBarNotification;", "ranking", "Landroid/service/notification/NotificationListenerService$Ranking;", "batch", "Lcom/android/systemui/statusbar/notification/collection/coalescer/EventBatch;", "(Ljava/lang/String;ILandroid/service/notification/StatusBarNotification;Landroid/service/notification/NotificationListenerService$Ranking;Lcom/android/systemui/statusbar/notification/collection/coalescer/EventBatch;)V", "getBatch", "()Lcom/android/systemui/statusbar/notification/collection/coalescer/EventBatch;", "setBatch", "(Lcom/android/systemui/statusbar/notification/collection/coalescer/EventBatch;)V", "getKey", "()Ljava/lang/String;", "getPosition", "()I", "setPosition", "(I)V", "getRanking", "()Landroid/service/notification/NotificationListenerService$Ranking;", "setRanking", "(Landroid/service/notification/NotificationListenerService$Ranking;)V", "getSbn", "()Landroid/service/notification/StatusBarNotification;", "setSbn", "(Landroid/service/notification/StatusBarNotification;)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CoalescedEvent.kt */
public final class CoalescedEvent {
    private EventBatch batch;
    private final String key;
    private int position;
    private NotificationListenerService.Ranking ranking;
    private StatusBarNotification sbn;

    public static /* synthetic */ CoalescedEvent copy$default(CoalescedEvent coalescedEvent, String str, int i, StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking2, EventBatch eventBatch, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = coalescedEvent.key;
        }
        if ((i2 & 2) != 0) {
            i = coalescedEvent.position;
        }
        int i3 = i;
        if ((i2 & 4) != 0) {
            statusBarNotification = coalescedEvent.sbn;
        }
        StatusBarNotification statusBarNotification2 = statusBarNotification;
        if ((i2 & 8) != 0) {
            ranking2 = coalescedEvent.ranking;
        }
        NotificationListenerService.Ranking ranking3 = ranking2;
        if ((i2 & 16) != 0) {
            eventBatch = coalescedEvent.batch;
        }
        return coalescedEvent.copy(str, i3, statusBarNotification2, ranking3, eventBatch);
    }

    public final String component1() {
        return this.key;
    }

    public final int component2() {
        return this.position;
    }

    public final StatusBarNotification component3() {
        return this.sbn;
    }

    public final NotificationListenerService.Ranking component4() {
        return this.ranking;
    }

    public final EventBatch component5() {
        return this.batch;
    }

    public final CoalescedEvent copy(String str, int i, StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking2, EventBatch eventBatch) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(ranking2, "ranking");
        return new CoalescedEvent(str, i, statusBarNotification, ranking2, eventBatch);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoalescedEvent)) {
            return false;
        }
        CoalescedEvent coalescedEvent = (CoalescedEvent) obj;
        return Intrinsics.areEqual((Object) this.key, (Object) coalescedEvent.key) && this.position == coalescedEvent.position && Intrinsics.areEqual((Object) this.sbn, (Object) coalescedEvent.sbn) && Intrinsics.areEqual((Object) this.ranking, (Object) coalescedEvent.ranking) && Intrinsics.areEqual((Object) this.batch, (Object) coalescedEvent.batch);
    }

    public int hashCode() {
        int hashCode = ((((((this.key.hashCode() * 31) + Integer.hashCode(this.position)) * 31) + this.sbn.hashCode()) * 31) + this.ranking.hashCode()) * 31;
        EventBatch eventBatch = this.batch;
        return hashCode + (eventBatch == null ? 0 : eventBatch.hashCode());
    }

    public CoalescedEvent(String str, int i, StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking2, EventBatch eventBatch) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(ranking2, "ranking");
        this.key = str;
        this.position = i;
        this.sbn = statusBarNotification;
        this.ranking = ranking2;
        this.batch = eventBatch;
    }

    public final String getKey() {
        return this.key;
    }

    public final int getPosition() {
        return this.position;
    }

    public final void setPosition(int i) {
        this.position = i;
    }

    public final StatusBarNotification getSbn() {
        return this.sbn;
    }

    public final void setSbn(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "<set-?>");
        this.sbn = statusBarNotification;
    }

    public final NotificationListenerService.Ranking getRanking() {
        return this.ranking;
    }

    public final void setRanking(NotificationListenerService.Ranking ranking2) {
        Intrinsics.checkNotNullParameter(ranking2, "<set-?>");
        this.ranking = ranking2;
    }

    public final EventBatch getBatch() {
        return this.batch;
    }

    public final void setBatch(EventBatch eventBatch) {
        this.batch = eventBatch;
    }

    public String toString() {
        return "CoalescedEvent(key=" + this.key + ')';
    }
}
