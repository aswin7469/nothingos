package com.android.systemui.statusbar.events;

import android.provider.DeviceConfig;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000-\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0016\u0010\u0016\u001a\u00020\u00152\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0016J$\u0010\u0018\u001a\u00020\u00132\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0002R \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u001b"}, mo65043d2 = {"com/android/systemui/statusbar/events/SystemEventCoordinator$privacyStateListener$1", "Lcom/android/systemui/privacy/PrivacyItemController$Callback;", "currentPrivacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "getCurrentPrivacyItems", "()Ljava/util/List;", "setCurrentPrivacyItems", "(Ljava/util/List;)V", "previousPrivacyItems", "getPreviousPrivacyItems", "setPreviousPrivacyItems", "timeLastEmpty", "", "getTimeLastEmpty", "()J", "setTimeLastEmpty", "(J)V", "isChipAnimationEnabled", "", "notifyListeners", "", "onPrivacyItemsChanged", "privacyItems", "uniqueItemsMatch", "one", "two", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator$privacyStateListener$1 implements PrivacyItemController.Callback {
    private List<PrivacyItem> currentPrivacyItems = CollectionsKt.emptyList();
    private List<PrivacyItem> previousPrivacyItems = CollectionsKt.emptyList();
    final /* synthetic */ SystemEventCoordinator this$0;
    private long timeLastEmpty;

    SystemEventCoordinator$privacyStateListener$1(SystemEventCoordinator systemEventCoordinator) {
        this.this$0 = systemEventCoordinator;
        this.timeLastEmpty = systemEventCoordinator.systemClock.elapsedRealtime();
    }

    public final List<PrivacyItem> getCurrentPrivacyItems() {
        return this.currentPrivacyItems;
    }

    public final void setCurrentPrivacyItems(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.currentPrivacyItems = list;
    }

    public final List<PrivacyItem> getPreviousPrivacyItems() {
        return this.previousPrivacyItems;
    }

    public final void setPreviousPrivacyItems(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.previousPrivacyItems = list;
    }

    public final long getTimeLastEmpty() {
        return this.timeLastEmpty;
    }

    public final void setTimeLastEmpty(long j) {
        this.timeLastEmpty = j;
    }

    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "privacyItems");
        if (!uniqueItemsMatch(list, this.currentPrivacyItems)) {
            if (list.isEmpty()) {
                this.previousPrivacyItems = this.currentPrivacyItems;
                this.timeLastEmpty = this.this$0.systemClock.elapsedRealtime();
            }
            this.currentPrivacyItems = list;
            notifyListeners();
        }
    }

    private final void notifyListeners() {
        if (this.currentPrivacyItems.isEmpty()) {
            this.this$0.notifyPrivacyItemsEmpty();
        } else {
            this.this$0.notifyPrivacyItemsChanged(isChipAnimationEnabled() && (!uniqueItemsMatch(this.currentPrivacyItems, this.previousPrivacyItems) || this.this$0.systemClock.elapsedRealtime() - this.timeLastEmpty >= 3000));
        }
    }

    private final boolean uniqueItemsMatch(List<PrivacyItem> list, List<PrivacyItem> list2) {
        Iterable<PrivacyItem> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyItem privacyItem : iterable) {
            arrayList.add(TuplesKt.m1802to(Integer.valueOf(privacyItem.getApplication().getUid()), privacyItem.getPrivacyType().getPermGroupName()));
        }
        Set set = CollectionsKt.toSet((List) arrayList);
        Iterable<PrivacyItem> iterable2 = list2;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        for (PrivacyItem privacyItem2 : iterable2) {
            arrayList2.add(TuplesKt.m1802to(Integer.valueOf(privacyItem2.getApplication().getUid()), privacyItem2.getPrivacyType().getPermGroupName()));
        }
        return Intrinsics.areEqual((Object) set, (Object) CollectionsKt.toSet((List) arrayList2));
    }

    private final boolean isChipAnimationEnabled() {
        return DeviceConfig.getBoolean("privacy", "privacy_chip_animation_enabled", true);
    }
}
