package com.android.wm.shell.common;

import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Map<FloatingContent, Rect> allContentBounds = new HashMap();
    private boolean currentlyResolvingConflicts;

    /* compiled from: FloatingContentCoordinator.kt */
    /* loaded from: classes2.dex */
    public interface FloatingContent {
        @NotNull
        Rect getAllowedFloatingBoundsRegion();

        @NotNull
        Rect getFloatingBoundsOnScreen();

        void moveToBounds(@NotNull Rect rect);

        @NotNull
        default Rect calculateNewBoundsOnOverlap(@NotNull Rect overlappingContentBounds, @NotNull List<Rect> otherContentBounds) {
            Intrinsics.checkNotNullParameter(overlappingContentBounds, "overlappingContentBounds");
            Intrinsics.checkNotNullParameter(otherContentBounds, "otherContentBounds");
            return FloatingContentCoordinator.Companion.findAreaForContentVertically(getFloatingBoundsOnScreen(), overlappingContentBounds, otherContentBounds, getAllowedFloatingBoundsRegion());
        }
    }

    public final void onContentAdded(@NotNull FloatingContent newContent) {
        Intrinsics.checkNotNullParameter(newContent, "newContent");
        updateContentBounds();
        this.allContentBounds.put(newContent, newContent.getFloatingBoundsOnScreen());
        maybeMoveConflictingContent(newContent);
    }

    public final void onContentMoved(@NotNull FloatingContent content) {
        Intrinsics.checkNotNullParameter(content, "content");
        if (this.currentlyResolvingConflicts) {
            return;
        }
        if (!this.allContentBounds.containsKey(content)) {
            Log.wtf("FloatingCoordinator", "Received onContentMoved call before onContentAdded! This should never happen.");
            return;
        }
        updateContentBounds();
        maybeMoveConflictingContent(content);
    }

    public final void onContentRemoved(@NotNull FloatingContent removedContent) {
        Intrinsics.checkNotNullParameter(removedContent, "removedContent");
        this.allContentBounds.remove(removedContent);
    }

    private final void maybeMoveConflictingContent(FloatingContent floatingContent) {
        List minus;
        List<Rect> minus2;
        this.currentlyResolvingConflicts = true;
        Rect rect = this.allContentBounds.get(floatingContent);
        Intrinsics.checkNotNull(rect);
        Map<FloatingContent, Rect> map = this.allContentBounds;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<Map.Entry<FloatingContent, Rect>> it = map.entrySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<FloatingContent, Rect> next = it.next();
            Rect value = next.getValue();
            if (!Intrinsics.areEqual(next.getKey(), floatingContent) && Rect.intersects(rect, value)) {
                z = true;
            }
            if (z) {
                linkedHashMap.put(next.getKey(), next.getValue());
            }
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            FloatingContent floatingContent2 = (FloatingContent) entry.getKey();
            minus = CollectionsKt___CollectionsKt.minus(this.allContentBounds.values(), (Rect) entry.getValue());
            minus2 = CollectionsKt___CollectionsKt.minus(minus, rect);
            Rect calculateNewBoundsOnOverlap = floatingContent2.calculateNewBoundsOnOverlap(rect, minus2);
            if (!calculateNewBoundsOnOverlap.isEmpty()) {
                floatingContent2.moveToBounds(calculateNewBoundsOnOverlap);
                this.allContentBounds.put(floatingContent2, floatingContent2.getFloatingBoundsOnScreen());
            }
        }
        this.currentlyResolvingConflicts = false;
    }

    private final void updateContentBounds() {
        for (FloatingContent floatingContent : this.allContentBounds.keySet()) {
            this.allContentBounds.put(floatingContent, floatingContent.getFloatingBoundsOnScreen());
        }
    }

    /* compiled from: FloatingContentCoordinator.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
        @NotNull
        public final Rect findAreaForContentVertically(@NotNull Rect contentRect, @NotNull Rect newlyOverlappingRect, @NotNull Collection<Rect> exclusionRects, @NotNull Rect allowedBounds) {
            Lazy lazy;
            Lazy lazy2;
            Lazy lazy3;
            Lazy lazy4;
            Intrinsics.checkNotNullParameter(contentRect, "contentRect");
            Intrinsics.checkNotNullParameter(newlyOverlappingRect, "newlyOverlappingRect");
            Intrinsics.checkNotNullParameter(exclusionRects, "exclusionRects");
            Intrinsics.checkNotNullParameter(allowedBounds, "allowedBounds");
            boolean z = true;
            boolean z2 = newlyOverlappingRect.centerY() < contentRect.centerY();
            ArrayList arrayList = new ArrayList();
            for (Object obj : exclusionRects) {
                if (FloatingContentCoordinator.Companion.rectsIntersectVertically((Rect) obj, contentRect)) {
                    arrayList.add(obj);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (Object obj2 : arrayList) {
                if (((Rect) obj2).top < contentRect.top) {
                    arrayList2.add(obj2);
                } else {
                    arrayList3.add(obj2);
                }
            }
            Pair pair = new Pair(arrayList2, arrayList3);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = pair.component1();
            Ref$ObjectRef ref$ObjectRef2 = new Ref$ObjectRef();
            ref$ObjectRef2.element = pair.component2();
            lazy = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(contentRect, ref$ObjectRef, newlyOverlappingRect));
            lazy2 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2(contentRect, ref$ObjectRef2, newlyOverlappingRect));
            lazy3 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2(allowedBounds, lazy));
            lazy4 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(allowedBounds, lazy2));
            if ((!z2 || !m1698findAreaForContentVertically$lambda5(lazy4)) && (z2 || m1697findAreaForContentVertically$lambda4(lazy3))) {
                z = false;
            }
            Rect m1696findAreaForContentVertically$lambda3 = z ? m1696findAreaForContentVertically$lambda3(lazy2) : m1695findAreaForContentVertically$lambda2(lazy);
            return allowedBounds.contains(m1696findAreaForContentVertically$lambda3) ? m1696findAreaForContentVertically$lambda3 : new Rect();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-2  reason: not valid java name */
        public static final Rect m1695findAreaForContentVertically$lambda2(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-3  reason: not valid java name */
        public static final Rect m1696findAreaForContentVertically$lambda3(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-4  reason: not valid java name */
        private static final boolean m1697findAreaForContentVertically$lambda4(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-5  reason: not valid java name */
        private static final boolean m1698findAreaForContentVertically$lambda5(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        private final boolean rectsIntersectVertically(Rect rect, Rect rect2) {
            int i;
            int i2 = rect.left;
            int i3 = rect2.left;
            return (i2 >= i3 && i2 <= rect2.right) || ((i = rect.right) <= rect2.right && i >= i3);
        }

        @NotNull
        public final Rect findAreaForContentAboveOrBelow(@NotNull Rect contentRect, @NotNull Collection<Rect> exclusionRects, final boolean z) {
            List<Rect> sortedWith;
            Intrinsics.checkNotNullParameter(contentRect, "contentRect");
            Intrinsics.checkNotNullParameter(exclusionRects, "exclusionRects");
            sortedWith = CollectionsKt___CollectionsKt.sortedWith(exclusionRects, new Comparator<T>() { // from class: com.android.wm.shell.common.FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    boolean z2 = z;
                    int i = ((Rect) t).top;
                    if (z2) {
                        i = -i;
                    }
                    Rect rect = (Rect) t2;
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(i), Integer.valueOf(z ? -rect.top : rect.top));
                    return compareValues;
                }
            });
            Rect rect = new Rect(contentRect);
            for (Rect rect2 : sortedWith) {
                if (!Rect.intersects(rect, rect2)) {
                    break;
                }
                rect.offsetTo(rect.left, rect2.top + (z ? -contentRect.height() : rect2.height()));
            }
            return rect;
        }
    }
}
