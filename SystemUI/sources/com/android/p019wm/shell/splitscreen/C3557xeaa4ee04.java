package com.android.p019wm.shell.splitscreen;

import android.os.Bundle;
import android.os.UserHandle;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3557xeaa4ee04 implements Consumer {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ UserHandle f$4;

    public /* synthetic */ C3557xeaa4ee04(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = i;
        this.f$3 = bundle;
        this.f$4 = userHandle;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).startShortcut(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
