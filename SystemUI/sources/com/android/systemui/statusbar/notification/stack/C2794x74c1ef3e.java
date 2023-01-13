package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.Grouping;
import kotlin.sequences.Sequence;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0001J\u0015\u0010\u0002\u001a\u00028\u00012\u0006\u0010\u0003\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006H\u0016¨\u0006\u0007¸\u0006\u0000"}, mo65043d2 = {"kotlin/sequences/SequencesKt___SequencesKt$groupingBy$1", "Lkotlin/collections/Grouping;", "keyOf", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "sourceIterator", "", "kotlin-stdlib"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 */
/* compiled from: _Sequences.kt */
public final class C2794x74c1ef3e implements Grouping<ExpandableView, Integer> {
    final /* synthetic */ Sequence $this_groupingBy;
    final /* synthetic */ NotificationSectionsManager this$0;

    public C2794x74c1ef3e(Sequence sequence, NotificationSectionsManager notificationSectionsManager) {
        this.$this_groupingBy = sequence;
        this.this$0 = notificationSectionsManager;
    }

    public Iterator<ExpandableView> sourceIterator() {
        return this.$this_groupingBy.iterator();
    }

    public Integer keyOf(ExpandableView expandableView) {
        Integer access$getBucket = this.this$0.getBucket(expandableView);
        if (access$getBucket != null) {
            return Integer.valueOf(access$getBucket.intValue());
        }
        throw new IllegalArgumentException("Cannot find section bucket for view");
    }
}
