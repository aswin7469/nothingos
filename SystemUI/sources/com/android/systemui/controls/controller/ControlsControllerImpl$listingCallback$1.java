package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsControllerImpl$listingCallback$1", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ ControlsControllerImpl this$0;

    ControlsControllerImpl$listingCallback$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        this.this$0.executor.execute(new C2028xacc0d33(list, this.this$0));
    }

    /* access modifiers changed from: private */
    /* renamed from: onServicesUpdated$lambda-6  reason: not valid java name */
    public static final void m2623onServicesUpdated$lambda6(List list, ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(list, "$serviceInfos");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Iterable<ControlsServiceInfo> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (ControlsServiceInfo controlsServiceInfo : iterable) {
            arrayList.add(controlsServiceInfo.componentName);
        }
        Set set = CollectionsKt.toSet((List) arrayList);
        Iterable<StructureInfo> allStructures = Favorites.INSTANCE.getAllStructures();
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(allStructures, 10));
        for (StructureInfo componentName : allStructures) {
            arrayList2.add(componentName.getComponentName());
        }
        Set set2 = CollectionsKt.toSet((List) arrayList2);
        boolean z = false;
        SharedPreferences sharedPreferences = controlsControllerImpl.userStructure.getUserContext().getSharedPreferences(DeviceControlsControllerImpl.PREFS_CONTROLS_FILE, 0);
        Set<String> stringSet = sharedPreferences.getStringSet(DeviceControlsControllerImpl.PREFS_CONTROLS_SEEDING_COMPLETED, new LinkedHashSet());
        Iterable<ComponentName> iterable2 = set;
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        for (ComponentName packageName : iterable2) {
            arrayList3.add(packageName.getPackageName());
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Intrinsics.checkNotNullExpressionValue(stringSet, "completedSeedingPackageSet");
        edit.putStringSet(DeviceControlsControllerImpl.PREFS_CONTROLS_SEEDING_COMPLETED, CollectionsKt.intersect(stringSet, (List) arrayList3)).apply();
        Iterable iterable3 = set2;
        for (ComponentName componentName2 : CollectionsKt.subtract(iterable3, iterable2)) {
            Favorites favorites = Favorites.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(componentName2, "it");
            favorites.removeStructures(componentName2);
            controlsControllerImpl.bindingController.onComponentRemoved(componentName2);
            z = true;
        }
        if (!controlsControllerImpl.getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease().getFavorites().isEmpty()) {
            for (ComponentName componentName3 : CollectionsKt.subtract(iterable2, iterable3)) {
                AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper$SystemUI_nothingRelease = controlsControllerImpl.getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease();
                Intrinsics.checkNotNullExpressionValue(componentName3, "it");
                List<StructureInfo> cachedFavoritesAndRemoveFor = auxiliaryPersistenceWrapper$SystemUI_nothingRelease.getCachedFavoritesAndRemoveFor(componentName3);
                if (!cachedFavoritesAndRemoveFor.isEmpty()) {
                    for (StructureInfo replaceControls : cachedFavoritesAndRemoveFor) {
                        Favorites.INSTANCE.replaceControls(replaceControls);
                    }
                    z = true;
                }
            }
            for (ComponentName componentName4 : CollectionsKt.intersect(iterable2, iterable3)) {
                AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper$SystemUI_nothingRelease2 = controlsControllerImpl.getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease();
                Intrinsics.checkNotNullExpressionValue(componentName4, "it");
                auxiliaryPersistenceWrapper$SystemUI_nothingRelease2.getCachedFavoritesAndRemoveFor(componentName4);
            }
        }
        if (z) {
            Log.d("ControlsControllerImpl", "Detected change in available services, storing updated favorites");
            controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        }
    }
}
