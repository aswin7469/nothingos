package com.android.p019wm.shell.bubbles.storage;

import android.content.pm.LauncherApps;
import android.icu.text.DateFormat;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.p019wm.shell.bubbles.ShortcutKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\f2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u0016\u0010\u0018\u001a\u00020\u00162\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u00142\u0006\u0010\u0017\u001a\u00020\fJ\u001c\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\f2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\fJ\u0018\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\fH\u0002J\u0014\u0010\u001f\u001a\u00020\u001c2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\f0\u0007J\u0016\u0010!\u001a\u00020\u00162\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR$\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00140\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/wm/shell/bubbles/storage/BubbleVolatileRepository;", "", "launcherApps", "Landroid/content/pm/LauncherApps;", "(Landroid/content/pm/LauncherApps;)V", "bubbles", "Landroid/util/SparseArray;", "", "Lcom/android/wm/shell/bubbles/storage/BubbleEntity;", "getBubbles", "()Landroid/util/SparseArray;", "capacity", "", "getCapacity$annotations", "()V", "getCapacity", "()I", "setCapacity", "(I)V", "entitiesByUser", "", "addBubbles", "", "userId", "cache", "getEntities", "removeBubbles", "removeBubblesForUser", "", "parentUserId", "removeBubblesForUserWithParent", "sanitizeBubbles", "activeUsers", "uncache", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository */
/* compiled from: BubbleVolatileRepository.kt */
public final class BubbleVolatileRepository {
    private int capacity = 16;
    private SparseArray<List<BubbleEntity>> entitiesByUser = new SparseArray<>();
    private final LauncherApps launcherApps;

    public static /* synthetic */ void getCapacity$annotations() {
    }

    public BubbleVolatileRepository(LauncherApps launcherApps2) {
        Intrinsics.checkNotNullParameter(launcherApps2, "launcherApps");
        this.launcherApps = launcherApps2;
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public final void setCapacity(int i) {
        this.capacity = i;
    }

    public final synchronized SparseArray<List<BubbleEntity>> getBubbles() {
        SparseArray<List<BubbleEntity>> sparseArray;
        sparseArray = new SparseArray<>();
        int size = this.entitiesByUser.size();
        for (int i = 0; i < size; i++) {
            int keyAt = this.entitiesByUser.keyAt(i);
            List valueAt = this.entitiesByUser.valueAt(i);
            Intrinsics.checkNotNullExpressionValue(valueAt, DateFormat.ABBR_GENERIC_TZ);
            sparseArray.put(keyAt, CollectionsKt.toList(valueAt));
        }
        return sparseArray;
    }

    public final synchronized List<BubbleEntity> getEntities(int i) {
        List<BubbleEntity> list;
        list = this.entitiesByUser.get(i);
        if (list == null) {
            list = new ArrayList<>();
            this.entitiesByUser.put(i, list);
        } else {
            Intrinsics.checkNotNullExpressionValue(list, "entities");
        }
        return list;
    }

    public final synchronized void addBubbles(int i, List<BubbleEntity> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        if (!list.isEmpty()) {
            List<BubbleEntity> entities = getEntities(i);
            List<T> takeLast = CollectionsKt.takeLast(list, this.capacity);
            Collection arrayList = new ArrayList();
            for (Object next : takeLast) {
                if (!entities.removeIf(new BubbleVolatileRepository$$ExternalSyntheticLambda0((BubbleEntity) next))) {
                    arrayList.add(next);
                }
            }
            List list2 = (List) arrayList;
            int size = (entities.size() + takeLast.size()) - this.capacity;
            if (size > 0) {
                uncache(CollectionsKt.take(entities, size));
                entities = CollectionsKt.toMutableList(CollectionsKt.drop(entities, size));
            }
            entities.addAll(takeLast);
            this.entitiesByUser.put(i, entities);
            cache(list2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addBubbles$lambda-2$lambda-1  reason: not valid java name */
    public static final boolean m3443addBubbles$lambda2$lambda1(BubbleEntity bubbleEntity, BubbleEntity bubbleEntity2) {
        Intrinsics.checkNotNullParameter(bubbleEntity, "$b");
        Intrinsics.checkNotNullParameter(bubbleEntity2, "e");
        return Intrinsics.areEqual((Object) bubbleEntity.getKey(), (Object) bubbleEntity2.getKey());
    }

    public final synchronized void removeBubbles(int i, List<BubbleEntity> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        Collection arrayList = new ArrayList();
        for (Object next : list) {
            if (getEntities(i).removeIf(new BubbleVolatileRepository$$ExternalSyntheticLambda2((BubbleEntity) next))) {
                arrayList.add(next);
            }
        }
        uncache((List) arrayList);
    }

    /* access modifiers changed from: private */
    /* renamed from: removeBubbles$lambda-4$lambda-3  reason: not valid java name */
    public static final boolean m3444removeBubbles$lambda4$lambda3(BubbleEntity bubbleEntity, BubbleEntity bubbleEntity2) {
        Intrinsics.checkNotNullParameter(bubbleEntity, "$b");
        Intrinsics.checkNotNullParameter(bubbleEntity2, "e");
        return Intrinsics.areEqual((Object) bubbleEntity.getKey(), (Object) bubbleEntity2.getKey());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
        return r3 != null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean removeBubblesForUser(int r2, int r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            r0 = -1
            if (r3 == r0) goto L_0x000a
            boolean r2 = r1.removeBubblesForUserWithParent(r2, r3)     // Catch:{ all -> 0x001e }
            monitor-exit(r1)
            return r2
        L_0x000a:
            android.util.SparseArray<java.util.List<com.android.wm.shell.bubbles.storage.BubbleEntity>> r3 = r1.entitiesByUser     // Catch:{ all -> 0x001e }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x001e }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x001e }
            android.util.SparseArray<java.util.List<com.android.wm.shell.bubbles.storage.BubbleEntity>> r0 = r1.entitiesByUser     // Catch:{ all -> 0x001e }
            r0.remove(r2)     // Catch:{ all -> 0x001e }
            if (r3 == 0) goto L_0x001b
            r2 = 1
            goto L_0x001c
        L_0x001b:
            r2 = 0
        L_0x001c:
            monitor-exit(r1)
            return r2
        L_0x001e:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.bubbles.storage.BubbleVolatileRepository.removeBubblesForUser(int, int):boolean");
    }

    private final synchronized boolean removeBubblesForUserWithParent(int i, int i2) {
        if (this.entitiesByUser.get(i2) == null) {
            return false;
        }
        return this.entitiesByUser.get(i2).removeIf(new BubbleVolatileRepository$$ExternalSyntheticLambda1(i));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeBubblesForUserWithParent$lambda-5  reason: not valid java name */
    public static final boolean m3445removeBubblesForUserWithParent$lambda5(int i, BubbleEntity bubbleEntity) {
        Intrinsics.checkNotNullParameter(bubbleEntity, "b");
        return bubbleEntity.getUserId() == i;
    }

    public final synchronized boolean sanitizeBubbles(List<Integer> list) {
        Intrinsics.checkNotNullParameter(list, "activeUsers");
        int size = this.entitiesByUser.size();
        int i = 0;
        while (i < size) {
            int keyAt = this.entitiesByUser.keyAt(i);
            if (!list.contains(Integer.valueOf(keyAt))) {
                this.entitiesByUser.remove(keyAt);
                return true;
            } else if (this.entitiesByUser.get(keyAt) != null) {
                return this.entitiesByUser.get(keyAt).removeIf(new BubbleVolatileRepository$$ExternalSyntheticLambda3(list));
            } else {
                i++;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: sanitizeBubbles$lambda-6  reason: not valid java name */
    public static final boolean m3446sanitizeBubbles$lambda6(List list, BubbleEntity bubbleEntity) {
        Intrinsics.checkNotNullParameter(list, "$activeUsers");
        Intrinsics.checkNotNullParameter(bubbleEntity, "b");
        return !list.contains(Integer.valueOf(bubbleEntity.getUserId()));
    }

    private final void cache(List<BubbleEntity> list) {
        Map linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) next;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj = linkedHashMap.get(shortcutKey);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(shortcutKey, obj);
            }
            ((List) obj).add(next);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            LauncherApps launcherApps2 = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            Iterable<BubbleEntity> iterable = (List) entry.getValue();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity shortcutId : iterable) {
                arrayList.add(shortcutId.getShortcutId());
            }
            launcherApps2.cacheShortcuts(pkg, (List) arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }

    private final void uncache(List<BubbleEntity> list) {
        Map linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) next;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj = linkedHashMap.get(shortcutKey);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(shortcutKey, obj);
            }
            ((List) obj).add(next);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            LauncherApps launcherApps2 = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            Iterable<BubbleEntity> iterable = (List) entry.getValue();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity shortcutId : iterable) {
                arrayList.add(shortcutId.getShortcutId());
            }
            launcherApps2.uncacheShortcuts(pkg, (List) arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }
}
