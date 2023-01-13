package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsControllerImpl$loadForComponent$2", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "accept", "", "controls", "", "Landroid/service/controls/Control;", "error", "message", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$loadForComponent$2 implements ControlsBindingController.LoadCallback {
    final /* synthetic */ ComponentName $componentName;
    final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
    final /* synthetic */ ControlsControllerImpl this$0;

    ControlsControllerImpl$loadForComponent$2(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, Consumer<ControlsController.LoadData> consumer) {
        this.this$0 = controlsControllerImpl;
        this.$componentName = componentName;
        this.$dataCallback = consumer;
    }

    public void accept(List<Control> list) {
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        this.this$0.executor.execute(new C2029x9ad8d229(this.$componentName, list, this.this$0, this.$dataCallback));
    }

    /* access modifiers changed from: private */
    /* renamed from: accept$lambda-4  reason: not valid java name */
    public static final void m2624accept$lambda4(ComponentName componentName, List list, ControlsControllerImpl controlsControllerImpl, Consumer consumer) {
        ComponentName componentName2 = componentName;
        List list2 = list;
        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
        Consumer consumer2 = consumer;
        Intrinsics.checkNotNullParameter(componentName2, "$componentName");
        Intrinsics.checkNotNullParameter(list2, "$controls");
        Intrinsics.checkNotNullParameter(controlsControllerImpl2, "this$0");
        Intrinsics.checkNotNullParameter(consumer2, "$dataCallback");
        Iterable<ControlInfo> controlsForComponent = Favorites.INSTANCE.getControlsForComponent(componentName2);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(controlsForComponent, 10));
        for (ControlInfo controlId : controlsForComponent) {
            arrayList.add(controlId.getControlId());
        }
        List list3 = (List) arrayList;
        if (Favorites.INSTANCE.updateControls(componentName2, list2)) {
            controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        }
        Set access$findRemoved = controlsControllerImpl2.findRemoved(CollectionsKt.toSet(list3), list2);
        Iterable<Control> iterable = list2;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Control control : iterable) {
            arrayList2.add(new ControlStatus(control, componentName, list3.contains(control.getControlId()), false, 8, (DefaultConstructorMarker) null));
        }
        List list4 = (List) arrayList2;
        List arrayList3 = new ArrayList();
        for (StructureInfo structureInfo : Favorites.INSTANCE.getStructuresForComponent(componentName2)) {
            for (ControlInfo controlInfo : structureInfo.getControls()) {
                if (access$findRemoved.contains(controlInfo.getControlId())) {
                    arrayList3.add(ControlsControllerImpl.createRemovedStatus$default(controlsControllerImpl, componentName, controlInfo, structureInfo.getStructure(), false, 8, (Object) null));
                }
            }
        }
        consumer2.accept(ControlsControllerKt.createLoadDataObject$default(CollectionsKt.plus(arrayList3, list4), list3, false, 4, (Object) null));
    }

    public void error(String str) {
        Intrinsics.checkNotNullParameter(str, "message");
        this.this$0.executor.execute(new C2030x9ad8d22a(this.$componentName, this.$dataCallback, this.this$0));
    }

    /* access modifiers changed from: private */
    /* renamed from: error$lambda-8  reason: not valid java name */
    public static final void m2625error$lambda8(ComponentName componentName, Consumer consumer, ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(consumer, "$dataCallback");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Collection arrayList = new ArrayList();
        for (StructureInfo structureInfo : Favorites.INSTANCE.getStructuresForComponent(componentName)) {
            Iterable<ControlInfo> controls = structureInfo.getControls();
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(controls, 10));
            for (ControlInfo access$createRemovedStatus : controls) {
                arrayList2.add(controlsControllerImpl.createRemovedStatus(componentName, access$createRemovedStatus, structureInfo.getStructure(), false));
            }
            CollectionsKt.addAll(arrayList, (List) arrayList2);
        }
        List list = (List) arrayList;
        Iterable<ControlStatus> iterable = list;
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (ControlStatus control : iterable) {
            arrayList3.add(control.getControl().getControlId());
        }
        consumer.accept(ControlsControllerKt.createLoadDataObject(list, (List) arrayList3, true));
    }
}
