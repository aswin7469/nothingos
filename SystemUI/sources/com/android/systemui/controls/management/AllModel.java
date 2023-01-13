package com.android.systemui.controls.management;

import android.service.controls.Control;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableMap;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import sun.security.x509.PolicyMappingsExtension;

@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001!B1\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u0016H\u0016J\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0002R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u0011X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000fR\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/controls/management/AllModel;", "Lcom/android/systemui/controls/management/ControlsModel;", "controls", "", "Lcom/android/systemui/controls/ControlStatus;", "initialFavoriteIds", "", "emptyZoneString", "", "controlsModelCallback", "Lcom/android/systemui/controls/management/ControlsModel$ControlsModelCallback;", "(Ljava/util/List;Ljava/util/List;Ljava/lang/CharSequence;Lcom/android/systemui/controls/management/ControlsModel$ControlsModelCallback;)V", "elements", "Lcom/android/systemui/controls/management/ElementWrapper;", "getElements", "()Ljava/util/List;", "favoriteIds", "", "favorites", "Lcom/android/systemui/controls/controller/ControlInfo;", "getFavorites", "modified", "", "moveHelper", "", "getMoveHelper", "()Ljava/lang/Void;", "changeFavoriteStatus", "", "controlId", "favorite", "createWrappers", "list", "OrderedMap", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AllModel.kt */
public final class AllModel implements ControlsModel {
    private final List<ControlStatus> controls;
    private final ControlsModel.ControlsModelCallback controlsModelCallback;
    private final List<ElementWrapper> elements;
    private final CharSequence emptyZoneString;
    private final List<String> favoriteIds;
    private boolean modified;
    private final Void moveHelper;

    public AllModel(List<ControlStatus> list, List<String> list2, CharSequence charSequence, ControlsModel.ControlsModelCallback controlsModelCallback2) {
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        Intrinsics.checkNotNullParameter(list2, "initialFavoriteIds");
        Intrinsics.checkNotNullParameter(charSequence, "emptyZoneString");
        Intrinsics.checkNotNullParameter(controlsModelCallback2, "controlsModelCallback");
        this.controls = list;
        this.emptyZoneString = charSequence;
        this.controlsModelCallback = controlsModelCallback2;
        AllModel allModel = this;
        Collection hashSet = new HashSet();
        for (ControlStatus control : this.controls) {
            hashSet.add(control.getControl().getControlId());
        }
        HashSet hashSet2 = (HashSet) hashSet;
        Collection arrayList = new ArrayList();
        for (Object next : list2) {
            if (hashSet2.contains((String) next)) {
                arrayList.add(next);
            }
        }
        this.favoriteIds = CollectionsKt.toMutableList((List) arrayList);
        this.elements = createWrappers(this.controls);
    }

    public Void getMoveHelper() {
        return this.moveHelper;
    }

    public List<ControlInfo> getFavorites() {
        ControlInfo controlInfo;
        Object obj;
        Collection arrayList = new ArrayList();
        for (String str : this.favoriteIds) {
            Iterator it = this.controls.iterator();
            while (true) {
                controlInfo = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (Intrinsics.areEqual((Object) ((ControlStatus) obj).getControl().getControlId(), (Object) str)) {
                    break;
                }
            }
            ControlStatus controlStatus = (ControlStatus) obj;
            Control control = controlStatus != null ? controlStatus.getControl() : null;
            if (control != null) {
                controlInfo = ControlInfo.Companion.fromControl(control);
            }
            if (controlInfo != null) {
                arrayList.add(controlInfo);
            }
        }
        return (List) arrayList;
    }

    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    public void changeFavoriteStatus(String str, boolean z) {
        boolean z2;
        Object obj;
        boolean z3;
        ControlStatus controlStatus;
        boolean z4;
        Intrinsics.checkNotNullParameter(str, "controlId");
        Iterator it = getElements().iterator();
        while (true) {
            z2 = false;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ElementWrapper elementWrapper = (ElementWrapper) obj;
            if (!(elementWrapper instanceof ControlStatusWrapper) || !Intrinsics.areEqual((Object) ((ControlStatusWrapper) elementWrapper).getControlStatus().getControl().getControlId(), (Object) str)) {
                z4 = false;
                continue;
            } else {
                z4 = true;
                continue;
            }
            if (z4) {
                break;
            }
        }
        ControlStatusWrapper controlStatusWrapper = (ControlStatusWrapper) obj;
        if (!(controlStatusWrapper == null || (controlStatus = controlStatusWrapper.getControlStatus()) == null || z != controlStatus.getFavorite())) {
            z2 = true;
        }
        if (!z2) {
            if (z) {
                z3 = this.favoriteIds.add(str);
            } else {
                z3 = this.favoriteIds.remove((Object) str);
            }
            if (z3 && !this.modified) {
                this.modified = true;
                this.controlsModelCallback.onFirstChange();
            }
            if (controlStatusWrapper != null) {
                controlStatusWrapper.getControlStatus().setFavorite(z);
            }
        }
    }

    private final List<ElementWrapper> createWrappers(List<ControlStatus> list) {
        Map orderedMap = new OrderedMap(new ArrayMap());
        for (Object next : list) {
            CharSequence zone = ((ControlStatus) next).getControl().getZone();
            if (zone != null) {
                Intrinsics.checkNotNullExpressionValue(zone, "it.control.zone ?: \"\"");
            }
            Object obj = orderedMap.get(zone);
            if (obj == null) {
                obj = new ArrayList();
                orderedMap.put(zone, obj);
            }
            ((List) obj).add(next);
        }
        OrderedMap orderedMap2 = (OrderedMap) orderedMap;
        List<ElementWrapper> arrayList = new ArrayList<>();
        Sequence sequence = null;
        for (CharSequence charSequence : orderedMap2.getOrderedKeys()) {
            Object value = MapsKt.getValue(orderedMap2, charSequence);
            Intrinsics.checkNotNullExpressionValue(value, "map.getValue(zoneName)");
            Sequence map = SequencesKt.map(CollectionsKt.asSequence((Iterable) value), AllModel$createWrappers$values$1.INSTANCE);
            if (TextUtils.isEmpty(charSequence)) {
                sequence = map;
            } else {
                Intrinsics.checkNotNullExpressionValue(charSequence, "zoneName");
                arrayList.add(new ZoneNameWrapper(charSequence));
                CollectionsKt.addAll(arrayList, map);
            }
        }
        if (sequence != null) {
            if (orderedMap2.size() != 1) {
                arrayList.add(new ZoneNameWrapper(this.emptyZoneString));
            }
            CollectionsKt.addAll(arrayList, sequence);
        }
        return arrayList;
    }

    @Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010$\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003¢\u0006\u0002\u0010\u0005J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00028\u0000H\u0001¢\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00028\u0001H\u0001¢\u0006\u0002\u0010\u001eJ\u0018\u0010!\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u001d\u001a\u00028\u0000H\u0003¢\u0006\u0002\u0010\"J\t\u0010#\u001a\u00020\u001cH\u0001J\u001f\u0010$\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u001d\u001a\u00028\u00002\u0006\u0010 \u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010%J\u001f\u0010&\u001a\u00020\u001a2\u0014\u0010'\u001a\u0010\u0012\u0006\b\u0001\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010(H\u0001J\u0017\u0010)\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u001d\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\"R$\u0010\u0006\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\b0\u0007X\u0005¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0018\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0005¢\u0006\u0006\u001a\u0004\b\f\u0010\nR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003X\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u00020\u0012X\u0005¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0018\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00010\u0016X\u0005¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/controls/management/AllModel$OrderedMap;", "K", "V", "", "map", "(Ljava/util/Map;)V", "entries", "", "", "getEntries", "()Ljava/util/Set;", "keys", "getKeys", "orderedKeys", "", "getOrderedKeys", "()Ljava/util/List;", "size", "", "getSize", "()I", "values", "", "getValues", "()Ljava/util/Collection;", "clear", "", "containsKey", "", "key", "(Ljava/lang/Object;)Z", "containsValue", "value", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "isEmpty", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAll", "from", "", "remove", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AllModel.kt */
    private static final class OrderedMap<K, V> implements Map<K, V>, KMutableMap {
        private final Map<K, V> map;
        private final List<K> orderedKeys = new ArrayList();

        public boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        public V get(Object obj) {
            return this.map.get(obj);
        }

        public Set<Map.Entry<K, V>> getEntries() {
            return this.map.entrySet();
        }

        public Set<K> getKeys() {
            return this.map.keySet();
        }

        public int getSize() {
            return this.map.size();
        }

        public Collection<V> getValues() {
            return this.map.values();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void putAll(Map<? extends K, ? extends V> map2) {
            Intrinsics.checkNotNullParameter(map2, "from");
            this.map.putAll(map2);
        }

        public OrderedMap(Map<K, V> map2) {
            Intrinsics.checkNotNullParameter(map2, PolicyMappingsExtension.MAP);
            this.map = map2;
        }

        public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
            return getEntries();
        }

        public final /* bridge */ Set<K> keySet() {
            return getKeys();
        }

        public final /* bridge */ int size() {
            return getSize();
        }

        public final /* bridge */ Collection<V> values() {
            return getValues();
        }

        public final List<K> getOrderedKeys() {
            return this.orderedKeys;
        }

        public V put(K k, V v) {
            if (!this.map.containsKey(k)) {
                this.orderedKeys.add(k);
            }
            return this.map.put(k, v);
        }

        public void clear() {
            this.orderedKeys.clear();
            this.map.clear();
        }

        public V remove(Object obj) {
            V remove = this.map.remove(obj);
            if (remove != null) {
                this.orderedKeys.remove(obj);
            }
            return remove;
        }
    }
}
