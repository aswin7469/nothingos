package com.android.systemui.screenshot;

import android.net.Uri;
import android.os.Messenger;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TakeScreenshotService$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ Messenger f$0;

    public /* synthetic */ TakeScreenshotService$$ExternalSyntheticLambda3(Messenger messenger) {
        this.f$0 = messenger;
    }

    public final void accept(Object obj) {
        TakeScreenshotService.reportUri(this.f$0, (Uri) obj);
    }
}
