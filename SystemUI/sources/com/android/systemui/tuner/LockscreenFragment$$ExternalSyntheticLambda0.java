package com.android.systemui.tuner;

import android.app.AlertDialog;
import com.android.systemui.tuner.LockscreenFragment;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenFragment$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ LockscreenFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ AlertDialog f$2;

    public /* synthetic */ LockscreenFragment$$ExternalSyntheticLambda0(LockscreenFragment lockscreenFragment, String str, AlertDialog alertDialog) {
        this.f$0 = lockscreenFragment;
        this.f$1 = str;
        this.f$2 = alertDialog;
    }

    public final void accept(Object obj) {
        this.f$0.mo46406x9fc62522(this.f$1, this.f$2, (LockscreenFragment.Item) obj);
    }
}
