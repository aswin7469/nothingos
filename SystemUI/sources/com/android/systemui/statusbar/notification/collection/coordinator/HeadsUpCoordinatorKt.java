package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo64986d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u0001*\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00022\u0006\u0010\u0004\u001a\u00020\u0003H\u0002\u001a+\u0010\u0005\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006*\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H\u00060\tH\u0002¢\u0006\u0002\u0010\u000b¨\u0006\f"}, mo64987d2 = {"getLocation", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", "", "", "key", "modifyHuns", "R", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "block", "Lkotlin/Function1;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "(Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinatorKt {
    /* access modifiers changed from: private */
    public static final GroupLocation getLocation(Map<String, ? extends GroupLocation> map, String str) {
        return (GroupLocation) map.getOrDefault(str, GroupLocation.Detached);
    }

    /* access modifiers changed from: private */
    public static final <R> R modifyHuns(HeadsUpManager headsUpManager, Function1<? super HunMutator, ? extends R> function1) {
        HunMutatorImpl hunMutatorImpl = new HunMutatorImpl(headsUpManager);
        R invoke = function1.invoke(hunMutatorImpl);
        hunMutatorImpl.commitModifications();
        return invoke;
    }
}
