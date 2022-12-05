package com.android.systemui.people;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda4 INSTANCE = new PeopleSpaceUtils$$ExternalSyntheticLambda4();

    private /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Stream lambda$getNotificationsByUri$0;
        lambda$getNotificationsByUri$0 = PeopleSpaceUtils.lambda$getNotificationsByUri$0((Map.Entry) obj);
        return lambda$getNotificationsByUri$0;
    }
}
