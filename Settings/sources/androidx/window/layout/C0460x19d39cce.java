package androidx.window.layout;

import androidx.core.util.Consumer;
import kotlinx.coroutines.channels.Channel;

/* renamed from: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0460x19d39cce implements Consumer {
    public final /* synthetic */ Channel f$0;

    public /* synthetic */ C0460x19d39cce(Channel channel) {
        this.f$0 = channel;
    }

    public final void accept(Object obj) {
        WindowInfoTrackerImpl$windowLayoutInfo$1.m53invokeSuspend$lambda0(this.f$0, (WindowLayoutInfo) obj);
    }
}
