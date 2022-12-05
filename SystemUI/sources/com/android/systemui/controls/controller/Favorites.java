package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class Favorites {
    @NotNull
    public static final Favorites INSTANCE = new Favorites();
    @NotNull
    private static Map<ComponentName, ? extends List<StructureInfo>> favMap;

    private Favorites() {
    }

    static {
        Map<ComponentName, ? extends List<StructureInfo>> emptyMap;
        emptyMap = MapsKt__MapsKt.emptyMap();
        favMap = emptyMap;
    }

    @NotNull
    public final List<StructureInfo> getAllStructures() {
        Map<ComponentName, ? extends List<StructureInfo>> map = favMap;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<ComponentName, ? extends List<StructureInfo>> entry : map.entrySet()) {
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, entry.getValue());
        }
        return arrayList;
    }

    @NotNull
    public final List<StructureInfo> getStructuresForComponent(@NotNull ComponentName componentName) {
        List<StructureInfo> emptyList;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        List<StructureInfo> list = favMap.get(componentName);
        if (list == null) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        }
        return list;
    }

    @NotNull
    public final List<ControlInfo> getControlsForStructure(@NotNull StructureInfo structure) {
        List<ControlInfo> list;
        Object obj;
        List<ControlInfo> emptyList;
        Intrinsics.checkNotNullParameter(structure, "structure");
        Iterator<T> it = getStructuresForComponent(structure.getComponentName()).iterator();
        while (true) {
            list = null;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((StructureInfo) obj).getStructure(), structure.getStructure())) {
                break;
            }
        }
        StructureInfo structureInfo = (StructureInfo) obj;
        if (structureInfo != null) {
            list = structureInfo.getControls();
        }
        if (list == null) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        }
        return list;
    }

    @NotNull
    public final List<ControlInfo> getControlsForComponent(@NotNull ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        List<StructureInfo> structuresForComponent = getStructuresForComponent(componentName);
        ArrayList arrayList = new ArrayList();
        for (StructureInfo structureInfo : structuresForComponent) {
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, structureInfo.getControls());
        }
        return arrayList;
    }

    public final void removeStructures(@NotNull ComponentName componentName) {
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        mutableMap.remove(componentName);
        favMap = mutableMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean addFavorite(@NotNull ComponentName componentName, @NotNull CharSequence structureName, @NotNull ControlInfo controlInfo) {
        boolean z;
        List plus;
        List emptyList;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(structureName, "structureName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        List<ControlInfo> controlsForComponent = getControlsForComponent(componentName);
        if (!(controlsForComponent instanceof Collection) || !controlsForComponent.isEmpty()) {
            for (ControlInfo controlInfo2 : controlsForComponent) {
                if (Intrinsics.areEqual(controlInfo2.getControlId(), controlInfo.getControlId())) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (z) {
            return false;
        }
        List<StructureInfo> list = favMap.get(componentName);
        StructureInfo structureInfo = null;
        if (list != null) {
            Iterator<T> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (Intrinsics.areEqual(((StructureInfo) next).getStructure(), structureName)) {
                    structureInfo = next;
                    break;
                }
            }
            structureInfo = structureInfo;
        }
        if (structureInfo == null) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            structureInfo = new StructureInfo(componentName, structureName, emptyList);
        }
        StructureInfo structureInfo2 = structureInfo;
        plus = CollectionsKt___CollectionsKt.plus(structureInfo2.getControls(), controlInfo);
        replaceControls(StructureInfo.copy$default(structureInfo2, null, null, plus, 3, null));
        return true;
    }

    public final void replaceControls(@NotNull StructureInfo updatedStructure) {
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap;
        Intrinsics.checkNotNullParameter(updatedStructure, "updatedStructure");
        mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        ArrayList arrayList = new ArrayList();
        ComponentName componentName = updatedStructure.getComponentName();
        boolean z = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            if (Intrinsics.areEqual(structureInfo.getStructure(), updatedStructure.getStructure())) {
                z = true;
                structureInfo = updatedStructure;
            }
            if (!structureInfo.getControls().isEmpty()) {
                arrayList.add(structureInfo);
            }
        }
        if (!z && !updatedStructure.getControls().isEmpty()) {
            arrayList.add(updatedStructure);
        }
        mutableMap.put(componentName, arrayList);
        favMap = mutableMap;
    }

    public final void clear() {
        Map<ComponentName, ? extends List<StructureInfo>> emptyMap;
        emptyMap = MapsKt__MapsKt.emptyMap();
        favMap = emptyMap;
    }

    public final boolean updateControls(@NotNull ComponentName componentName, @NotNull List<Control> controls) {
        int collectionSizeOrDefault;
        int mapCapacity;
        int coerceAtLeast;
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap;
        ControlInfo copy$default;
        boolean z;
        Pair pair;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controls, "controls");
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10);
        mapCapacity = MapsKt__MapsJVMKt.mapCapacity(collectionSizeOrDefault);
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(mapCapacity, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(coerceAtLeast);
        for (Object obj : controls) {
            linkedHashMap.put(((Control) obj).getControlId(), obj);
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        boolean z2 = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            for (ControlInfo controlInfo : structureInfo.getControls()) {
                Control control = (Control) linkedHashMap.get(controlInfo.getControlId());
                if (control == null) {
                    pair = null;
                } else {
                    if (!Intrinsics.areEqual(control.getTitle(), controlInfo.getControlTitle()) || !Intrinsics.areEqual(control.getSubtitle(), controlInfo.getControlSubtitle()) || control.getDeviceType() != controlInfo.getDeviceType()) {
                        CharSequence title = control.getTitle();
                        Intrinsics.checkNotNullExpressionValue(title, "updatedControl.title");
                        CharSequence subtitle = control.getSubtitle();
                        Intrinsics.checkNotNullExpressionValue(subtitle, "updatedControl.subtitle");
                        copy$default = ControlInfo.copy$default(controlInfo, null, title, subtitle, control.getDeviceType(), 1, null);
                        z = true;
                    } else {
                        z = z2;
                        copy$default = controlInfo;
                    }
                    CharSequence structure = control.getStructure();
                    if (structure == null) {
                        structure = "";
                    }
                    if (!Intrinsics.areEqual(structureInfo.getStructure(), structure)) {
                        z = true;
                    }
                    Pair pair2 = new Pair(structure, copy$default);
                    z2 = z;
                    pair = pair2;
                }
                if (pair == null) {
                    pair = new Pair(structureInfo.getStructure(), controlInfo);
                }
                CharSequence charSequence = (CharSequence) pair.component1();
                ControlInfo controlInfo2 = (ControlInfo) pair.component2();
                Object obj2 = linkedHashMap2.get(charSequence);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap2.put(charSequence, obj2);
                }
                ((List) obj2).add(controlInfo2);
            }
        }
        if (!z2) {
            return false;
        }
        ArrayList arrayList = new ArrayList(linkedHashMap2.size());
        for (Map.Entry entry : linkedHashMap2.entrySet()) {
            arrayList.add(new StructureInfo(componentName, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        mutableMap.put(componentName, arrayList);
        favMap = mutableMap;
        return true;
    }

    public final void load(@NotNull List<StructureInfo> structures) {
        Intrinsics.checkNotNullParameter(structures, "structures");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : structures) {
            ComponentName componentName = ((StructureInfo) obj).getComponentName();
            Object obj2 = linkedHashMap.get(componentName);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(componentName, obj2);
            }
            ((List) obj2).add(obj);
        }
        favMap = linkedHashMap;
    }
}
