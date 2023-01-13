package com.android.p019wm.shell.common;

import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\u0018\u0000 \u00152\u00020\u0001:\u0002\u0015\u0016B\u0005¢\u0006\u0002\u0010\u0002J\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005H\u0002J\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0005J\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0005J\u000e\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0005J\b\u0010\u0014\u001a\u00020\fH\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/wm/shell/common/FloatingContentCoordinator;", "", "()V", "allContentBounds", "", "Lcom/android/wm/shell/common/FloatingContentCoordinator$FloatingContent;", "Landroid/graphics/Rect;", "currentlyResolvingConflicts", "", "getOccupiedBounds", "", "maybeMoveConflictingContent", "", "fromContent", "onContentAdded", "newContent", "onContentMoved", "content", "onContentRemoved", "removedContent", "updateContentBounds", "Companion", "FloatingContent", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.common.FloatingContentCoordinator */
/* compiled from: FloatingContentCoordinator.kt */
public final class FloatingContentCoordinator {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final Map<FloatingContent, Rect> allContentBounds = new HashMap();
    private boolean currentlyResolvingConflicts;

    @JvmStatic
    public static final Rect findAreaForContentVertically(Rect rect, Rect rect2, Collection<Rect> collection, Rect rect3) {
        return Companion.findAreaForContentVertically(rect, rect2, collection, rect3);
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006H\u0017J\b\u0010\u0007\u001a\u00020\u0003H&J\b\u0010\b\u001a\u00020\u0003H&J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/wm/shell/common/FloatingContentCoordinator$FloatingContent;", "", "calculateNewBoundsOnOverlap", "Landroid/graphics/Rect;", "overlappingContentBounds", "otherContentBounds", "", "getAllowedFloatingBoundsRegion", "getFloatingBoundsOnScreen", "moveToBounds", "", "bounds", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.wm.shell.common.FloatingContentCoordinator$FloatingContent */
    /* compiled from: FloatingContentCoordinator.kt */
    public interface FloatingContent {
        Rect getAllowedFloatingBoundsRegion();

        Rect getFloatingBoundsOnScreen();

        void moveToBounds(Rect rect);

        @JvmDefault
        Rect calculateNewBoundsOnOverlap(Rect rect, List<Rect> list) {
            Intrinsics.checkNotNullParameter(rect, "overlappingContentBounds");
            Intrinsics.checkNotNullParameter(list, "otherContentBounds");
            return FloatingContentCoordinator.Companion.findAreaForContentVertically(getFloatingBoundsOnScreen(), rect, list, getAllowedFloatingBoundsRegion());
        }
    }

    public final void onContentAdded(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "newContent");
        updateContentBounds();
        this.allContentBounds.put(floatingContent, floatingContent.getFloatingBoundsOnScreen());
        maybeMoveConflictingContent(floatingContent);
    }

    public final void onContentMoved(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "content");
        if (!this.currentlyResolvingConflicts) {
            if (!this.allContentBounds.containsKey(floatingContent)) {
                Log.wtf("FloatingCoordinator", "Received onContentMoved call before onContentAdded! This should never happen.");
                return;
            }
            updateContentBounds();
            maybeMoveConflictingContent(floatingContent);
        }
    }

    public final void onContentRemoved(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "removedContent");
        this.allContentBounds.remove(floatingContent);
    }

    public final Collection<Rect> getOccupiedBounds() {
        return this.allContentBounds.values();
    }

    private final void maybeMoveConflictingContent(FloatingContent floatingContent) {
        this.currentlyResolvingConflicts = true;
        Rect rect = this.allContentBounds.get(floatingContent);
        Intrinsics.checkNotNull(rect);
        Rect rect2 = rect;
        Map<FloatingContent, Rect> map = this.allContentBounds;
        Map linkedHashMap = new LinkedHashMap();
        Iterator<Map.Entry<FloatingContent, Rect>> it = map.entrySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry next = it.next();
            Rect rect3 = (Rect) next.getValue();
            if (!Intrinsics.areEqual((Object) (FloatingContent) next.getKey(), (Object) floatingContent) && Rect.intersects(rect2, rect3)) {
                z = true;
            }
            if (z) {
                linkedHashMap.put(next.getKey(), next.getValue());
            }
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            FloatingContent floatingContent2 = (FloatingContent) entry.getKey();
            Rect calculateNewBoundsOnOverlap = floatingContent2.calculateNewBoundsOnOverlap(rect2, CollectionsKt.minus(CollectionsKt.minus(this.allContentBounds.values(), (Rect) entry.getValue()), rect2));
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

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\u0006\u0010\b\u001a\u00020\tJ.\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\u0006\u0010\f\u001a\u00020\u0004H\u0007J\u0018\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004H\u0002¨\u0006\u0010²\u0006\n\u0010\u0011\u001a\u00020\u0004X\u0002²\u0006\n\u0010\u0012\u001a\u00020\u0004X\u0002²\u0006\n\u0010\u0013\u001a\u00020\tX\u0002²\u0006\n\u0010\u0014\u001a\u00020\tX\u0002"}, mo65043d2 = {"Lcom/android/wm/shell/common/FloatingContentCoordinator$Companion;", "", "()V", "findAreaForContentAboveOrBelow", "Landroid/graphics/Rect;", "contentRect", "exclusionRects", "", "findAbove", "", "findAreaForContentVertically", "newlyOverlappingRect", "allowedBounds", "rectsIntersectVertically", "r1", "r2", "WMShell_release", "newContentBoundsAbove", "newContentBoundsBelow", "positionAboveInBounds", "positionBelowInBounds"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.wm.shell.common.FloatingContentCoordinator$Companion */
    /* compiled from: FloatingContentCoordinator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final Rect findAreaForContentVertically(Rect rect, Rect rect2, Collection<Rect> collection, Rect rect3) {
            Intrinsics.checkNotNullParameter(rect, "contentRect");
            Intrinsics.checkNotNullParameter(rect2, "newlyOverlappingRect");
            Intrinsics.checkNotNullParameter(collection, "exclusionRects");
            Intrinsics.checkNotNullParameter(rect3, "allowedBounds");
            boolean z = true;
            boolean z2 = rect2.centerY() < rect.centerY();
            Collection arrayList = new ArrayList();
            for (Object next : collection) {
                if (FloatingContentCoordinator.Companion.rectsIntersectVertically((Rect) next, rect)) {
                    arrayList.add(next);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (Object next2 : (List) arrayList) {
                if (((Rect) next2).top < rect.top) {
                    arrayList2.add(next2);
                } else {
                    arrayList3.add(next2);
                }
            }
            Pair pair = new Pair(arrayList2, arrayList3);
            Ref.ObjectRef objectRef = new Ref.ObjectRef();
            objectRef.element = pair.component1();
            Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
            objectRef2.element = pair.component2();
            Lazy lazy = LazyKt.lazy(new C3443xa8ad3931(rect, objectRef, rect2));
            Lazy lazy2 = LazyKt.lazy(new C3444xe284ccc5(rect, objectRef2, rect2));
            Lazy lazy3 = LazyKt.lazy(new C3445x994e5850(rect3, lazy));
            Lazy lazy4 = LazyKt.lazy(new C3446xf7317e4(rect3, lazy2));
            if ((!z2 || !m3453findAreaForContentVertically$lambda5(lazy4)) && (z2 || m3452findAreaForContentVertically$lambda4(lazy3))) {
                z = false;
            }
            Rect r7 = z ? m3451findAreaForContentVertically$lambda3(lazy2) : m3450findAreaForContentVertically$lambda2(lazy);
            return rect3.contains(r7) ? r7 : new Rect();
        }

        /* access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-2  reason: not valid java name */
        public static final Rect m3450findAreaForContentVertically$lambda2(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-3  reason: not valid java name */
        public static final Rect m3451findAreaForContentVertically$lambda3(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-4  reason: not valid java name */
        private static final boolean m3452findAreaForContentVertically$lambda4(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-5  reason: not valid java name */
        private static final boolean m3453findAreaForContentVertically$lambda5(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        public final Rect findAreaForContentAboveOrBelow(Rect rect, Collection<Rect> collection, boolean z) {
            Intrinsics.checkNotNullParameter(rect, "contentRect");
            Intrinsics.checkNotNullParameter(collection, "exclusionRects");
            List<Rect> sortedWith = CollectionsKt.sortedWith(collection, new C3442x8b489ee0(z));
            Rect rect2 = new Rect(rect);
            for (Rect rect3 : sortedWith) {
                if (!Rect.intersects(rect2, rect3)) {
                    break;
                }
                rect2.offsetTo(rect2.left, rect3.top + (z ? -rect.height() : rect3.height()));
            }
            return rect2;
        }

        private final boolean rectsIntersectVertically(Rect rect, Rect rect2) {
            return (rect.left >= rect2.left && rect.left <= rect2.right) || (rect.right <= rect2.right && rect.right >= rect2.left);
        }
    }
}
