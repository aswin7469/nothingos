package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010J\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00062\u0006\u0010\n\u001a\u00020\u0005J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00062\u0006\u0010\u0014\u001a\u00020\u0007J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\n\u001a\u00020\u0005J\u0014\u0010\u0016\u001a\u00020\u00102\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u000e\u0010\u0018\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0005J\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0007J\u001c\u0010\u001b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0006R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/Favorites;", "", "()V", "favMap", "", "Landroid/content/ComponentName;", "", "Lcom/android/systemui/controls/controller/StructureInfo;", "addFavorite", "", "componentName", "structureName", "", "controlInfo", "Lcom/android/systemui/controls/controller/ControlInfo;", "clear", "", "getAllStructures", "getControlsForComponent", "getControlsForStructure", "structure", "getStructuresForComponent", "load", "structures", "removeStructures", "replaceControls", "updatedStructure", "updateControls", "controls", "Landroid/service/controls/Control;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsControllerImpl.kt */
final class Favorites {
    public static final Favorites INSTANCE = new Favorites();
    private static Map<ComponentName, ? extends List<StructureInfo>> favMap = MapsKt.emptyMap();

    private Favorites() {
    }

    public final List<StructureInfo> getAllStructures() {
        Map<ComponentName, ? extends List<StructureInfo>> map = favMap;
        Collection arrayList = new ArrayList();
        for (Map.Entry<ComponentName, ? extends List<StructureInfo>> value : map.entrySet()) {
            CollectionsKt.addAll(arrayList, (List) value.getValue());
        }
        return (List) arrayList;
    }

    public final List<StructureInfo> getStructuresForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        List<StructureInfo> list = (List) favMap.get(componentName);
        return list == null ? CollectionsKt.emptyList() : list;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0035, code lost:
        r3 = r0.getControls();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.android.systemui.controls.controller.ControlInfo> getControlsForStructure(com.android.systemui.controls.controller.StructureInfo r4) {
        /*
            r3 = this;
            java.lang.String r0 = "structure"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            android.content.ComponentName r0 = r4.getComponentName()
            java.util.List r3 = r3.getStructuresForComponent(r0)
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.Iterator r3 = r3.iterator()
        L_0x0014:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x0030
            java.lang.Object r0 = r3.next()
            r1 = r0
            com.android.systemui.controls.controller.StructureInfo r1 = (com.android.systemui.controls.controller.StructureInfo) r1
            java.lang.CharSequence r1 = r1.getStructure()
            java.lang.CharSequence r2 = r4.getStructure()
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r1 == 0) goto L_0x0014
            goto L_0x0031
        L_0x0030:
            r0 = 0
        L_0x0031:
            com.android.systemui.controls.controller.StructureInfo r0 = (com.android.systemui.controls.controller.StructureInfo) r0
            if (r0 == 0) goto L_0x003c
            java.util.List r3 = r0.getControls()
            if (r3 == 0) goto L_0x003c
            goto L_0x0040
        L_0x003c:
            java.util.List r3 = kotlin.collections.CollectionsKt.emptyList()
        L_0x0040:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.Favorites.getControlsForStructure(com.android.systemui.controls.controller.StructureInfo):java.util.List");
    }

    public final List<ControlInfo> getControlsForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Collection arrayList = new ArrayList();
        for (StructureInfo controls : getStructuresForComponent(componentName)) {
            CollectionsKt.addAll(arrayList, controls.getControls());
        }
        return (List) arrayList;
    }

    public final void load(List<StructureInfo> list) {
        Intrinsics.checkNotNullParameter(list, "structures");
        Map<ComponentName, ? extends List<StructureInfo>> linkedHashMap = new LinkedHashMap<>();
        for (Object next : list) {
            ComponentName componentName = ((StructureInfo) next).getComponentName();
            Object obj = linkedHashMap.get(componentName);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(componentName, obj);
            }
            ((List) obj).add(next);
        }
        favMap = linkedHashMap;
    }

    public final boolean updateControls(ComponentName componentName, List<Control> list) {
        Pair pair;
        ComponentName componentName2 = componentName;
        List<Control> list2 = list;
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(list2, AutoTileManager.DEVICE_CONTROLS);
        Iterable iterable = list2;
        Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
        for (Object next : iterable) {
            linkedHashMap.put(((Control) next).getControlId(), next);
        }
        Map linkedHashMap2 = new LinkedHashMap();
        boolean z = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            for (ControlInfo controlInfo : structureInfo.getControls()) {
                Control control = (Control) linkedHashMap.get(controlInfo.getControlId());
                if (control != null) {
                    if (!Intrinsics.areEqual((Object) control.getTitle(), (Object) controlInfo.getControlTitle()) || !Intrinsics.areEqual((Object) control.getSubtitle(), (Object) controlInfo.getControlSubtitle()) || control.getDeviceType() != controlInfo.getDeviceType()) {
                        CharSequence title = control.getTitle();
                        Intrinsics.checkNotNullExpressionValue(title, "updatedControl.title");
                        CharSequence subtitle = control.getSubtitle();
                        Intrinsics.checkNotNullExpressionValue(subtitle, "updatedControl.subtitle");
                        controlInfo = ControlInfo.copy$default(controlInfo, (String) null, title, subtitle, control.getDeviceType(), 1, (Object) null);
                        z = true;
                    }
                    CharSequence structure = control.getStructure();
                    if (structure != null) {
                        Intrinsics.checkNotNullExpressionValue(structure, "updatedControl.structure ?: \"\"");
                    }
                    if (!Intrinsics.areEqual((Object) structureInfo.getStructure(), (Object) structure)) {
                        z = true;
                    }
                    pair = new Pair(structure, controlInfo);
                } else {
                    pair = new Pair(structureInfo.getStructure(), controlInfo);
                }
                CharSequence charSequence = (CharSequence) pair.component1();
                ControlInfo controlInfo2 = (ControlInfo) pair.component2();
                Object obj = linkedHashMap2.get(charSequence);
                if (obj == null) {
                    obj = new ArrayList();
                    linkedHashMap2.put(charSequence, obj);
                }
                ((List) obj).add(controlInfo2);
            }
        }
        if (!z) {
            return false;
        }
        Collection arrayList = new ArrayList(linkedHashMap2.size());
        for (Map.Entry entry : linkedHashMap2.entrySet()) {
            arrayList.add(new StructureInfo(componentName2, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt.toMutableMap(favMap);
        mutableMap.put(componentName2, (List) arrayList);
        favMap = mutableMap;
        return true;
    }

    public final void removeStructures(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt.toMutableMap(favMap);
        mutableMap.remove(componentName);
        favMap = mutableMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0074, code lost:
        if (r1 != null) goto L_0x0080;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addFavorite(android.content.ComponentName r10, java.lang.CharSequence r11, com.android.systemui.controls.controller.ControlInfo r12) {
        /*
            r9 = this;
            java.lang.String r0 = "componentName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            java.lang.String r0 = "structureName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "controlInfo"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.util.List r0 = r9.getControlsForComponent(r10)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            boolean r1 = r0 instanceof java.util.Collection
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0027
            r1 = r0
            java.util.Collection r1 = (java.util.Collection) r1
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L_0x0027
        L_0x0025:
            r0 = r3
            goto L_0x0046
        L_0x0027:
            java.util.Iterator r0 = r0.iterator()
        L_0x002b:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0025
            java.lang.Object r1 = r0.next()
            com.android.systemui.controls.controller.ControlInfo r1 = (com.android.systemui.controls.controller.ControlInfo) r1
            java.lang.String r1 = r1.getControlId()
            java.lang.String r4 = r12.getControlId()
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r4)
            if (r1 == 0) goto L_0x002b
            r0 = r2
        L_0x0046:
            if (r0 == 0) goto L_0x0049
            return r3
        L_0x0049:
            java.util.Map<android.content.ComponentName, ? extends java.util.List<com.android.systemui.controls.controller.StructureInfo>> r0 = favMap
            java.lang.Object r0 = r0.get(r10)
            java.util.List r0 = (java.util.List) r0
            if (r0 == 0) goto L_0x0077
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r0 = r0.iterator()
        L_0x0059:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0071
            java.lang.Object r1 = r0.next()
            r3 = r1
            com.android.systemui.controls.controller.StructureInfo r3 = (com.android.systemui.controls.controller.StructureInfo) r3
            java.lang.CharSequence r3 = r3.getStructure()
            boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r11)
            if (r3 == 0) goto L_0x0059
            goto L_0x0072
        L_0x0071:
            r1 = 0
        L_0x0072:
            com.android.systemui.controls.controller.StructureInfo r1 = (com.android.systemui.controls.controller.StructureInfo) r1
            if (r1 == 0) goto L_0x0077
            goto L_0x0080
        L_0x0077:
            com.android.systemui.controls.controller.StructureInfo r1 = new com.android.systemui.controls.controller.StructureInfo
            java.util.List r0 = kotlin.collections.CollectionsKt.emptyList()
            r1.<init>(r10, r11, r0)
        L_0x0080:
            r3 = r1
            r4 = 0
            r5 = 0
            java.util.List r10 = r3.getControls()
            java.util.Collection r10 = (java.util.Collection) r10
            java.util.List r6 = kotlin.collections.CollectionsKt.plus(r10, r12)
            r7 = 3
            r8 = 0
            com.android.systemui.controls.controller.StructureInfo r10 = com.android.systemui.controls.controller.StructureInfo.copy$default(r3, r4, r5, r6, r7, r8)
            r9.replaceControls(r10)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.Favorites.addFavorite(android.content.ComponentName, java.lang.CharSequence, com.android.systemui.controls.controller.ControlInfo):boolean");
    }

    public final void replaceControls(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "updatedStructure");
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt.toMutableMap(favMap);
        List arrayList = new ArrayList();
        ComponentName componentName = structureInfo.getComponentName();
        boolean z = false;
        for (StructureInfo structureInfo2 : getStructuresForComponent(componentName)) {
            if (Intrinsics.areEqual((Object) structureInfo2.getStructure(), (Object) structureInfo.getStructure())) {
                z = true;
                structureInfo2 = structureInfo;
            }
            if (!structureInfo2.getControls().isEmpty()) {
                arrayList.add(structureInfo2);
            }
        }
        if (!z && !structureInfo.getControls().isEmpty()) {
            arrayList.add(structureInfo);
        }
        mutableMap.put(componentName, arrayList);
        favMap = mutableMap;
    }

    public final void clear() {
        favMap = MapsKt.emptyMap();
    }
}
