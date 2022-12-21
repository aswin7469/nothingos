package java.util.concurrent;

import java.util.concurrent.Flow;
import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SubmissionPublisher$ConsumerSubscriber$$ExternalSyntheticLambda0 implements BiConsumer {
    public final /* synthetic */ Flow.Subscription f$0;

    public /* synthetic */ SubmissionPublisher$ConsumerSubscriber$$ExternalSyntheticLambda0(Flow.Subscription subscription) {
        this.f$0 = subscription;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.cancel();
    }
}
