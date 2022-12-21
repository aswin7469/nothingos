package com.android.systemui.statusbar.notification;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.util.Collection;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0014\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\rR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X.¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/SectionClassifier;", "", "()V", "lowPrioritySections", "", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "isMinimizedSection", "", "section", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "setMinimizedSections", "", "sections", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SectionClassifier.kt */
public final class SectionClassifier {
    private Set<? extends NotifSectioner> lowPrioritySections;

    public final void setMinimizedSections(Collection<? extends NotifSectioner> collection) {
        Intrinsics.checkNotNullParameter(collection, "sections");
        this.lowPrioritySections = CollectionsKt.toSet(collection);
    }

    public final boolean isMinimizedSection(NotifSection notifSection) {
        Intrinsics.checkNotNullParameter(notifSection, "section");
        Set<? extends NotifSectioner> set = this.lowPrioritySections;
        if (set == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lowPrioritySections");
            set = null;
        }
        return set.contains(notifSection.getSectioner());
    }
}
