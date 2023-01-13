package com.android.systemui.flags;

import android.content.Intent;
import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FlagManager$$ExternalSyntheticLambda0 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ FlagManager f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ FlagManager$$ExternalSyntheticLambda0(FlagManager flagManager, Intent intent) {
        this.f$0 = flagManager;
        this.f$1 = intent;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return FlagManager.m2757getFlagsFuture$lambda0(this.f$0, this.f$1, completer);
    }
}
