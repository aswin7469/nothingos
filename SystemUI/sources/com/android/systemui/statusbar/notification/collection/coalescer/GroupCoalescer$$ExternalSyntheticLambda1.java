package com.android.systemui.statusbar.notification.collection.coalescer;

import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class GroupCoalescer$$ExternalSyntheticLambda1 implements Comparator {
    public static final /* synthetic */ GroupCoalescer$$ExternalSyntheticLambda1 INSTANCE = new GroupCoalescer$$ExternalSyntheticLambda1();

    private /* synthetic */ GroupCoalescer$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$new$1;
        lambda$new$1 = GroupCoalescer.lambda$new$1((CoalescedEvent) obj, (CoalescedEvent) obj2);
        return lambda$new$1;
    }
}
