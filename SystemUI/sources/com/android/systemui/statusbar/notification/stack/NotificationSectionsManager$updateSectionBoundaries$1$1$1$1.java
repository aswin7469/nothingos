package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "invoke", "(Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationSectionsManager.kt */
final class NotificationSectionsManager$updateSectionBoundaries$1$1$1$1 extends Lambda implements Function1<NotificationSectionsManager.SectionUpdateState<? extends ExpandableView>, Boolean> {
    final /* synthetic */ NotificationSectionsManager.SectionUpdateState<ExpandableView> $state;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NotificationSectionsManager$updateSectionBoundaries$1$1$1$1(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        super(1);
        this.$state = sectionUpdateState;
    }

    public final Boolean invoke(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        Intrinsics.checkNotNullParameter(sectionUpdateState, "it");
        return Boolean.valueOf(sectionUpdateState == this.$state);
    }
}
