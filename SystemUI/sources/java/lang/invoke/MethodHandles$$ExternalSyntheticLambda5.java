package java.lang.invoke;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MethodHandles$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ List f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ List f$3;

    public /* synthetic */ MethodHandles$$ExternalSyntheticLambda5(List list, List list2, List list3, List list4) {
        this.f$0 = list;
        this.f$1 = list2;
        this.f$2 = list3;
        this.f$3 = list4;
    }

    public final void accept(Object obj) {
        MethodHandles.lambda$loop$1(this.f$0, this.f$1, this.f$2, this.f$3, (MethodHandle[]) obj);
    }
}
