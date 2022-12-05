package com.android.systemui.statusbar.events;

import android.provider.DeviceConfig;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SystemEventCoordinator.kt */
/* loaded from: classes.dex */
public final class SystemEventCoordinator$privacyStateListener$1 implements PrivacyItemController.Callback {
    @NotNull
    private List<PrivacyItem> currentPrivacyItems;
    @NotNull
    private List<PrivacyItem> previousPrivacyItems;
    final /* synthetic */ SystemEventCoordinator this$0;
    private long timeLastEmpty;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemEventCoordinator$privacyStateListener$1(SystemEventCoordinator systemEventCoordinator) {
        List<PrivacyItem> emptyList;
        List<PrivacyItem> emptyList2;
        SystemClock systemClock;
        this.this$0 = systemEventCoordinator;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.currentPrivacyItems = emptyList;
        emptyList2 = CollectionsKt__CollectionsKt.emptyList();
        this.previousPrivacyItems = emptyList2;
        systemClock = systemEventCoordinator.systemClock;
        this.timeLastEmpty = systemClock.elapsedRealtime();
    }

    @NotNull
    public final List<PrivacyItem> getCurrentPrivacyItems() {
        return this.currentPrivacyItems;
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public void onPrivacyItemsChanged(@NotNull List<PrivacyItem> privacyItems) {
        SystemClock systemClock;
        Intrinsics.checkNotNullParameter(privacyItems, "privacyItems");
        if (uniqueItemsMatch(privacyItems, this.currentPrivacyItems)) {
            return;
        }
        if (privacyItems.isEmpty()) {
            this.previousPrivacyItems = this.currentPrivacyItems;
            systemClock = this.this$0.systemClock;
            this.timeLastEmpty = systemClock.elapsedRealtime();
        }
        this.currentPrivacyItems = privacyItems;
        notifyListeners();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002f, code lost:
        if ((r0.elapsedRealtime() - r4.timeLastEmpty) >= 3000) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void notifyListeners() {
        boolean z;
        SystemClock systemClock;
        if (this.currentPrivacyItems.isEmpty()) {
            this.this$0.notifyPrivacyItemsEmpty();
            return;
        }
        if (isChipAnimationEnabled()) {
            if (uniqueItemsMatch(this.currentPrivacyItems, this.previousPrivacyItems)) {
                systemClock = this.this$0.systemClock;
            }
            z = true;
            this.this$0.notifyPrivacyItemsChanged(z);
        }
        z = false;
        this.this$0.notifyPrivacyItemsChanged(z);
    }

    private final boolean isChipAnimationEnabled() {
        return DeviceConfig.getBoolean("privacy", "privacy_chip_animation_enabled", true);
    }

    private final boolean uniqueItemsMatch(List<PrivacyItem> list, List<PrivacyItem> list2) {
        int collectionSizeOrDefault;
        Set set;
        int collectionSizeOrDefault2;
        Set set2;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        for (PrivacyItem privacyItem : list) {
            arrayList.add(TuplesKt.to(Integer.valueOf(privacyItem.getApplication().getUid()), privacyItem.getPrivacyType().getPermGroupName()));
        }
        set = CollectionsKt___CollectionsKt.toSet(arrayList);
        collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10);
        ArrayList arrayList2 = new ArrayList(collectionSizeOrDefault2);
        for (PrivacyItem privacyItem2 : list2) {
            arrayList2.add(TuplesKt.to(Integer.valueOf(privacyItem2.getApplication().getUid()), privacyItem2.getPrivacyType().getPermGroupName()));
        }
        set2 = CollectionsKt___CollectionsKt.toSet(arrayList2);
        return Intrinsics.areEqual(set, set2);
    }
}
