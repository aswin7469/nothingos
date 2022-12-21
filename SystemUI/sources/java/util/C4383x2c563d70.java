package java.util;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/* renamed from: java.util.Collections$UnmodifiableMap$UnmodifiableEntrySet$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4383x2c563d70 implements Consumer {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ C4383x2c563d70(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.accept(new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry((Map.Entry) obj));
    }
}
