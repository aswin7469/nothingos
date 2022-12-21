package com.android.systemui.flags;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/flags/FlagManager$getFlagsFuture$1$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FlagManager.kt */
public final class FlagManager$getFlagsFuture$1$1 extends BroadcastReceiver {
    final /* synthetic */ CallbackToFutureAdapter.Completer<Collection<Flag<?>>> $completer;

    FlagManager$getFlagsFuture$1$1(CallbackToFutureAdapter.Completer<Collection<Flag<?>>> completer) {
        this.$completer = completer;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Bundle resultExtras = getResultExtras(false);
        ArrayList parcelableArrayList = resultExtras != null ? resultExtras.getParcelableArrayList("flags") : null;
        if (parcelableArrayList != null) {
            this.$completer.set(parcelableArrayList);
        } else {
            this.$completer.setException(new NoFlagResultsException());
        }
    }
}