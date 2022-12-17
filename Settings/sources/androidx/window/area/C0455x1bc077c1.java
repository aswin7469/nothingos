package androidx.window.area;

import java.util.function.Consumer;
import kotlinx.coroutines.channels.Channel;

/* renamed from: androidx.window.area.WindowAreaControllerImpl$rearDisplayStatus$1$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0455x1bc077c1 implements Consumer {
    public final /* synthetic */ WindowAreaControllerImpl f$0;
    public final /* synthetic */ Channel f$1;

    public /* synthetic */ C0455x1bc077c1(WindowAreaControllerImpl windowAreaControllerImpl, Channel channel) {
        this.f$0 = windowAreaControllerImpl;
        this.f$1 = channel;
    }

    public final void accept(Object obj) {
        WindowAreaControllerImpl$rearDisplayStatus$1.m48invokeSuspend$lambda0(this.f$0, this.f$1, (Integer) obj);
    }
}
