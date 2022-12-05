package com.android.systemui.controls.management;

import android.service.controls.Control;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableMap;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AllModel.kt */
/* loaded from: classes.dex */
public final class AllModel implements ControlsModel {
    @NotNull
    private final List<ControlStatus> controls;
    @NotNull
    private final ControlsModel.ControlsModelCallback controlsModelCallback;
    @NotNull
    private final List<ElementWrapper> elements;
    @NotNull
    private final CharSequence emptyZoneString;
    @NotNull
    private final List<String> favoriteIds;
    private boolean modified;
    @Nullable
    private final Void moveHelper;

    public AllModel(@NotNull List<ControlStatus> controls, @NotNull List<String> initialFavoriteIds, @NotNull CharSequence emptyZoneString, @NotNull ControlsModel.ControlsModelCallback controlsModelCallback) {
        List<String> mutableList;
        Intrinsics.checkNotNullParameter(controls, "controls");
        Intrinsics.checkNotNullParameter(initialFavoriteIds, "initialFavoriteIds");
        Intrinsics.checkNotNullParameter(emptyZoneString, "emptyZoneString");
        Intrinsics.checkNotNullParameter(controlsModelCallback, "controlsModelCallback");
        this.controls = controls;
        this.emptyZoneString = emptyZoneString;
        this.controlsModelCallback = controlsModelCallback;
        HashSet hashSet = new HashSet();
        for (ControlStatus controlStatus : controls) {
            hashSet.add(controlStatus.getControl().getControlId());
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : initialFavoriteIds) {
            if (hashSet.contains((String) obj)) {
                arrayList.add(obj);
            }
        }
        mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) arrayList);
        this.favoriteIds = mutableList;
        this.elements = createWrappers(this.controls);
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    /* renamed from: getMoveHelper */
    public /* bridge */ /* synthetic */ ControlsModel.MoveHelper mo411getMoveHelper() {
        return (ControlsModel.MoveHelper) mo411getMoveHelper();
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    @Nullable
    /* renamed from: getMoveHelper  reason: collision with other method in class */
    public Void mo411getMoveHelper() {
        return this.moveHelper;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    @NotNull
    public List<ControlInfo> getFavorites() {
        ControlInfo controlInfo;
        Object obj;
        List<String> list = this.favoriteIds;
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            Iterator<T> it = this.controls.iterator();
            while (true) {
                controlInfo = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (Intrinsics.areEqual(((ControlStatus) obj).getControl().getControlId(), str)) {
                    break;
                }
            }
            ControlStatus controlStatus = (ControlStatus) obj;
            Control control = controlStatus == null ? null : controlStatus.getControl();
            if (control != null) {
                controlInfo = ControlInfo.Companion.fromControl(control);
            }
            if (controlInfo != null) {
                arrayList.add(controlInfo);
            }
        }
        return arrayList;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    @NotNull
    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public void changeFavoriteStatus(@NotNull String controlId, boolean z) {
        Boolean bool;
        Object obj;
        boolean remove;
        ControlStatus controlStatus;
        boolean z2;
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Iterator<T> it = getElements().iterator();
        while (true) {
            bool = null;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ElementWrapper elementWrapper = (ElementWrapper) obj;
            if (!(elementWrapper instanceof ControlStatusWrapper) || !Intrinsics.areEqual(((ControlStatusWrapper) elementWrapper).getControlStatus().getControl().getControlId(), controlId)) {
                z2 = false;
                continue;
            } else {
                z2 = true;
                continue;
            }
            if (z2) {
                break;
            }
        }
        ControlStatusWrapper controlStatusWrapper = (ControlStatusWrapper) obj;
        Boolean valueOf = Boolean.valueOf(z);
        if (controlStatusWrapper != null && (controlStatus = controlStatusWrapper.getControlStatus()) != null) {
            bool = Boolean.valueOf(controlStatus.getFavorite());
        }
        if (Intrinsics.areEqual(valueOf, bool)) {
            return;
        }
        if (z) {
            remove = this.favoriteIds.add(controlId);
        } else {
            remove = this.favoriteIds.remove(controlId);
        }
        if (remove && !this.modified) {
            this.modified = true;
            this.controlsModelCallback.onFirstChange();
        }
        if (controlStatusWrapper == null) {
            return;
        }
        controlStatusWrapper.getControlStatus().setFavorite(z);
    }

    private final List<ElementWrapper> createWrappers(List<ControlStatus> list) {
        Sequence asSequence;
        Sequence map;
        OrderedMap orderedMap = new OrderedMap(new ArrayMap());
        for (Object obj : list) {
            CharSequence zone = ((ControlStatus) obj).getControl().getZone();
            if (zone == null) {
                zone = "";
            }
            Object obj2 = orderedMap.get(zone);
            if (obj2 == null) {
                obj2 = new ArrayList();
                orderedMap.put(zone, obj2);
            }
            ((List) obj2).add(obj);
        }
        ArrayList arrayList = new ArrayList();
        Sequence sequence = null;
        for (CharSequence zoneName : orderedMap.getOrderedKeys()) {
            Object value = MapsKt.getValue(orderedMap, zoneName);
            Intrinsics.checkNotNullExpressionValue(value, "map.getValue(zoneName)");
            asSequence = CollectionsKt___CollectionsKt.asSequence((Iterable) value);
            map = SequencesKt___SequencesKt.map(asSequence, AllModel$createWrappers$values$1.INSTANCE);
            if (TextUtils.isEmpty(zoneName)) {
                sequence = map;
            } else {
                Intrinsics.checkNotNullExpressionValue(zoneName, "zoneName");
                arrayList.add(new ZoneNameWrapper(zoneName));
                CollectionsKt__MutableCollectionsKt.addAll(arrayList, map);
            }
        }
        if (sequence != null) {
            if (orderedMap.size() != 1) {
                arrayList.add(new ZoneNameWrapper(this.emptyZoneString));
            }
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, sequence);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AllModel.kt */
    /* loaded from: classes.dex */
    public static final class OrderedMap<K, V> implements Map<K, V>, KMutableMap {
        @NotNull
        private final Map<K, V> map;
        @NotNull
        private final List<K> orderedKeys = new ArrayList();

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        @Override // java.util.Map
        @Nullable
        public V get(Object obj) {
            return this.map.get(obj);
        }

        @NotNull
        public Set<Map.Entry<K, V>> getEntries() {
            return this.map.entrySet();
        }

        @NotNull
        public Set<K> getKeys() {
            return this.map.keySet();
        }

        public int getSize() {
            return this.map.size();
        }

        @NotNull
        public Collection<V> getValues() {
            return this.map.values();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Map
        public void putAll(@NotNull Map<? extends K, ? extends V> from) {
            Intrinsics.checkNotNullParameter(from, "from");
            this.map.putAll(from);
        }

        public OrderedMap(@NotNull Map<K, V> map) {
            Intrinsics.checkNotNullParameter(map, "map");
            this.map = map;
        }

        @Override // java.util.Map
        public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
            return getEntries();
        }

        @Override // java.util.Map
        public final /* bridge */ Set<K> keySet() {
            return getKeys();
        }

        @Override // java.util.Map
        public final /* bridge */ int size() {
            return getSize();
        }

        @Override // java.util.Map
        public final /* bridge */ Collection<V> values() {
            return getValues();
        }

        @NotNull
        public final List<K> getOrderedKeys() {
            return this.orderedKeys;
        }

        @Override // java.util.Map
        @Nullable
        public V put(K k, V v) {
            if (!this.map.containsKey(k)) {
                this.orderedKeys.add(k);
            }
            return this.map.put(k, v);
        }

        @Override // java.util.Map
        public void clear() {
            this.orderedKeys.clear();
            this.map.clear();
        }

        @Override // java.util.Map
        @Nullable
        public V remove(Object obj) {
            V remove = this.map.remove(obj);
            if (remove != null) {
                this.orderedKeys.remove(obj);
            }
            return remove;
        }
    }
}
