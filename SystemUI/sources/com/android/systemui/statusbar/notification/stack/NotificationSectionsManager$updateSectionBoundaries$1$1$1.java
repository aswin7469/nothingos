package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager$updateSectionBoundaries$1$1$1 extends Lambda implements Function1<NotificationSectionsManager.SectionUpdateState<? extends ExpandableView>, Boolean> {
    final /* synthetic */ NotificationSectionsManager.SectionUpdateState<ExpandableView> $state;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public NotificationSectionsManager$updateSectionBoundaries$1$1$1(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        super(1);
        this.$state = sectionUpdateState;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> sectionUpdateState) {
        return Boolean.valueOf(invoke2(sectionUpdateState));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(@NotNull NotificationSectionsManager.SectionUpdateState<? extends ExpandableView> it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it == this.$state;
    }
}
