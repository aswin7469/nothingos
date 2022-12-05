package com.android.wm.shell.bubbles.storage;

import android.content.pm.LauncherApps;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.bubbles.ShortcutKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: BubbleVolatileRepository.kt */
/* loaded from: classes2.dex */
public final class BubbleVolatileRepository {
    @NotNull
    private final LauncherApps launcherApps;
    @NotNull
    private SparseArray<List<BubbleEntity>> entitiesByUser = new SparseArray<>();
    private int capacity = 16;

    @VisibleForTesting
    public static /* synthetic */ void getCapacity$annotations() {
    }

    public BubbleVolatileRepository(@NotNull LauncherApps launcherApps) {
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        this.launcherApps = launcherApps;
    }

    @NotNull
    public final synchronized SparseArray<List<BubbleEntity>> getBubbles() {
        SparseArray<List<BubbleEntity>> sparseArray;
        List<BubbleEntity> list;
        sparseArray = new SparseArray<>();
        int i = 0;
        int size = this.entitiesByUser.size();
        if (size > 0) {
            while (true) {
                int i2 = i + 1;
                int keyAt = this.entitiesByUser.keyAt(i);
                List<BubbleEntity> v = this.entitiesByUser.valueAt(i);
                Intrinsics.checkNotNullExpressionValue(v, "v");
                list = CollectionsKt___CollectionsKt.toList(v);
                sparseArray.put(keyAt, list);
                if (i2 >= size) {
                    break;
                }
                i = i2;
            }
        }
        return sparseArray;
    }

    @NotNull
    public final synchronized List<BubbleEntity> getEntities(int i) {
        List<BubbleEntity> entities;
        entities = this.entitiesByUser.get(i);
        if (entities == null) {
            entities = new ArrayList<>();
            this.entitiesByUser.put(i, entities);
        } else {
            Intrinsics.checkNotNullExpressionValue(entities, "entities");
        }
        return entities;
    }

    public final synchronized void addBubbles(int i, @NotNull List<BubbleEntity> bubbles) {
        List takeLast;
        List<BubbleEntity> take;
        List drop;
        Intrinsics.checkNotNullParameter(bubbles, "bubbles");
        if (bubbles.isEmpty()) {
            return;
        }
        List<BubbleEntity> entities = getEntities(i);
        takeLast = CollectionsKt___CollectionsKt.takeLast(bubbles, this.capacity);
        List<BubbleEntity> arrayList = new ArrayList<>();
        for (Object obj : takeLast) {
            final BubbleEntity bubbleEntity = (BubbleEntity) obj;
            if (!entities.removeIf(new Predicate<BubbleEntity>() { // from class: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$addBubbles$uniqueBubbles$1$1
                @Override // java.util.function.Predicate
                public final boolean test(@NotNull BubbleEntity e) {
                    Intrinsics.checkNotNullParameter(e, "e");
                    return Intrinsics.areEqual(BubbleEntity.this.getKey(), e.getKey());
                }
            })) {
                arrayList.add(obj);
            }
        }
        int size = (entities.size() + takeLast.size()) - this.capacity;
        if (size > 0) {
            take = CollectionsKt___CollectionsKt.take(entities, size);
            uncache(take);
            drop = CollectionsKt___CollectionsKt.drop(entities, size);
            entities = CollectionsKt___CollectionsKt.toMutableList((Collection) drop);
        }
        entities.addAll(takeLast);
        this.entitiesByUser.put(i, entities);
        cache(arrayList);
    }

    public final synchronized void removeBubbles(int i, @NotNull List<BubbleEntity> bubbles) {
        Intrinsics.checkNotNullParameter(bubbles, "bubbles");
        List<BubbleEntity> arrayList = new ArrayList<>();
        for (Object obj : bubbles) {
            final BubbleEntity bubbleEntity = (BubbleEntity) obj;
            if (getEntities(i).removeIf(new Predicate<BubbleEntity>() { // from class: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$removeBubbles$1$1
                @Override // java.util.function.Predicate
                public final boolean test(@NotNull BubbleEntity e) {
                    Intrinsics.checkNotNullParameter(e, "e");
                    return Intrinsics.areEqual(BubbleEntity.this.getKey(), e.getKey());
                }
            })) {
                arrayList.add(obj);
            }
        }
        uncache(arrayList);
    }

    private final void cache(List<BubbleEntity> list) {
        int collectionSizeOrDefault;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) obj;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj2 = linkedHashMap.get(shortcutKey);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(shortcutKey, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list2 = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10);
            ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
            for (BubbleEntity bubbleEntity2 : list2) {
                arrayList.add(bubbleEntity2.getShortcutId());
            }
            launcherApps.cacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }

    private final void uncache(List<BubbleEntity> list) {
        int collectionSizeOrDefault;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) obj;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj2 = linkedHashMap.get(shortcutKey);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(shortcutKey, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list2 = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10);
            ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
            for (BubbleEntity bubbleEntity2 : list2) {
                arrayList.add(bubbleEntity2.getShortcutId());
            }
            launcherApps.uncacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }
}
