package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u0002¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, mo65043d2 = {"repeated", "Lkotlin/sequences/Sequence;", "T", "value", "(Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubViewControllerKt {
    /* access modifiers changed from: private */
    public static final <T> Sequence<T> repeated(T t) {
        return SequencesKt.sequence(new PeopleHubViewControllerKt$repeated$1(t, (Continuation<? super PeopleHubViewControllerKt$repeated$1>) null));
    }
}
