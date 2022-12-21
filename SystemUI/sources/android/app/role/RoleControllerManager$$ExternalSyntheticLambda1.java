package android.app.role;

import android.os.Bundle;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda1 implements BiConsumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda1(Executor executor, String str, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = str;
        this.f$2 = consumer;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.execute(new RoleControllerManager$$ExternalSyntheticLambda7((Throwable) obj2, this.f$1, this.f$2, (Bundle) obj));
    }
}
