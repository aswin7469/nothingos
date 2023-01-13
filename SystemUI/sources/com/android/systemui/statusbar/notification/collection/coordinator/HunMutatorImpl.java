package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\tH\u0016J\u0018\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\tH\u0016R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutatorImpl;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "(Lcom/android/systemui/statusbar/policy/HeadsUpManager;)V", "deferred", "", "Lkotlin/Pair;", "", "", "commitModifications", "", "removeNotification", "key", "releaseImmediately", "updateNotification", "alert", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HunMutatorImpl implements HunMutator {
    private final List<Pair<String, Boolean>> deferred = new ArrayList();
    private final HeadsUpManager headsUpManager;

    public HunMutatorImpl(HeadsUpManager headsUpManager2) {
        Intrinsics.checkNotNullParameter(headsUpManager2, "headsUpManager");
        this.headsUpManager = headsUpManager2;
    }

    public void updateNotification(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.headsUpManager.updateNotification(str, z);
    }

    public void removeNotification(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.deferred.add(new Pair(str, Boolean.valueOf(z)));
    }

    public final void commitModifications() {
        for (Pair pair : this.deferred) {
            boolean booleanValue = ((Boolean) pair.component2()).booleanValue();
            this.headsUpManager.removeNotification((String) pair.component1(), booleanValue);
        }
        this.deferred.clear();
    }
}
