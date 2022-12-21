package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.UserHandle;
import com.android.settingslib.SliceBroadcastRelay;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001a\u001a\u00020\tHÆ\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u000bHÆ\u0003J=\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bHÆ\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010 \u001a\u00020!HÖ\u0001J\t\u0010\"\u001a\u00020\u000bHÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/broadcast/ReceiverData;", "", "receiver", "Landroid/content/BroadcastReceiver;", "filter", "Landroid/content/IntentFilter;", "executor", "Ljava/util/concurrent/Executor;", "user", "Landroid/os/UserHandle;", "permission", "", "(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/util/concurrent/Executor;Landroid/os/UserHandle;Ljava/lang/String;)V", "getExecutor", "()Ljava/util/concurrent/Executor;", "getFilter", "()Landroid/content/IntentFilter;", "getPermission", "()Ljava/lang/String;", "getReceiver", "()Landroid/content/BroadcastReceiver;", "getUser", "()Landroid/os/UserHandle;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BroadcastDispatcher.kt */
public final class ReceiverData {
    private final Executor executor;
    private final IntentFilter filter;
    private final String permission;
    private final BroadcastReceiver receiver;
    private final UserHandle user;

    public static /* synthetic */ ReceiverData copy$default(ReceiverData receiverData, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor2, UserHandle userHandle, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            broadcastReceiver = receiverData.receiver;
        }
        if ((i & 2) != 0) {
            intentFilter = receiverData.filter;
        }
        IntentFilter intentFilter2 = intentFilter;
        if ((i & 4) != 0) {
            executor2 = receiverData.executor;
        }
        Executor executor3 = executor2;
        if ((i & 8) != 0) {
            userHandle = receiverData.user;
        }
        UserHandle userHandle2 = userHandle;
        if ((i & 16) != 0) {
            str = receiverData.permission;
        }
        return receiverData.copy(broadcastReceiver, intentFilter2, executor3, userHandle2, str);
    }

    public final BroadcastReceiver component1() {
        return this.receiver;
    }

    public final IntentFilter component2() {
        return this.filter;
    }

    public final Executor component3() {
        return this.executor;
    }

    public final UserHandle component4() {
        return this.user;
    }

    public final String component5() {
        return this.permission;
    }

    public final ReceiverData copy(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor2, UserHandle userHandle, String str) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        return new ReceiverData(broadcastReceiver, intentFilter, executor2, userHandle, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReceiverData)) {
            return false;
        }
        ReceiverData receiverData = (ReceiverData) obj;
        return Intrinsics.areEqual((Object) this.receiver, (Object) receiverData.receiver) && Intrinsics.areEqual((Object) this.filter, (Object) receiverData.filter) && Intrinsics.areEqual((Object) this.executor, (Object) receiverData.executor) && Intrinsics.areEqual((Object) this.user, (Object) receiverData.user) && Intrinsics.areEqual((Object) this.permission, (Object) receiverData.permission);
    }

    public int hashCode() {
        int hashCode = ((((((this.receiver.hashCode() * 31) + this.filter.hashCode()) * 31) + this.executor.hashCode()) * 31) + this.user.hashCode()) * 31;
        String str = this.permission;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    public String toString() {
        return "ReceiverData(receiver=" + this.receiver + ", filter=" + this.filter + ", executor=" + this.executor + ", user=" + this.user + ", permission=" + this.permission + ')';
    }

    public ReceiverData(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor2, UserHandle userHandle, String str) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        this.receiver = broadcastReceiver;
        this.filter = intentFilter;
        this.executor = executor2;
        this.user = userHandle;
        this.permission = str;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ReceiverData(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor2, UserHandle userHandle, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(broadcastReceiver, intentFilter, executor2, userHandle, (i & 16) != 0 ? null : str);
    }

    public final BroadcastReceiver getReceiver() {
        return this.receiver;
    }

    public final IntentFilter getFilter() {
        return this.filter;
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    public final UserHandle getUser() {
        return this.user;
    }

    public final String getPermission() {
        return this.permission;
    }
}
