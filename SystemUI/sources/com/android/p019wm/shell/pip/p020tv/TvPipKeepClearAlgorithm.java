package com.android.p019wm.shell.pip.p020tv;

import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import com.android.systemui.biometrics.AuthDialog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.IntRange;

@Metadata(mo65042d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010#\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010!\n\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0002WXB\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0018H\u0002J*\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00182\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J,\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u0018\u0010'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\u0005H\u0002J.\u0010)\u001a\u0004\u0018\u00010\u00052\u0006\u0010&\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J,\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J,\u0010-\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J.\u0010.\u001a\u0004\u0018\u00010\u00052\u0006\u0010&\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J6\u0010.\u001a\u0004\u0018\u00010\u00052\u0006\u0010/\u001a\u00020\u000e2\u0006\u0010&\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u0005002\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u0010\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0002J\u001e\u00103\u001a\u00020\u00052\u0006\u00104\u001a\u00020\u00052\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u0018\u00106\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u00182\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u001a\u00107\u001a\u00020\u000e2\u0006\u00108\u001a\u00020\u00052\b\u00109\u001a\u0004\u0018\u00010\u0005H\u0002J\b\u0010:\u001a\u00020;H\u0002J\u0010\u0010<\u001a\u00020\u00052\u0006\u00104\u001a\u00020\u0005H\u0002J\u000e\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u000eJ\u000e\u0010@\u001a\u00020>2\u0006\u00104\u001a\u00020\u0005J\u000e\u0010A\u001a\u00020>2\u0006\u0010B\u001a\u00020\u0015J\u000e\u0010C\u001a\u00020>2\u0006\u0010\u001f\u001a\u00020\u0018J\b\u0010D\u001a\u00020;H\u0002J\b\u0010E\u001a\u00020;H\u0002J\b\u0010F\u001a\u00020;H\u0002J.\u0010G\u001a\u00020+2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020+0I2\u0006\u0010J\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\u000e2\u0006\u0010L\u001a\u00020\u000eH\u0002J\u0010\u0010M\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0002J\u001c\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u0014\u0010P\u001a\u00020;*\u00020\u00052\u0006\u0010Q\u001a\u00020\u0005H\u0002J\u0014\u0010R\u001a\u00020;*\u00020\u00052\u0006\u0010Q\u001a\u00020\u0005H\u0002J\u0014\u0010S\u001a\u00020;*\u00020\u00052\u0006\u0010Q\u001a\u00020\u0005H\u0002J\u001c\u0010T\u001a\u00020\u0005*\u00020\u00052\u0006\u0010U\u001a\u00020\u000e2\u0006\u0010V\u001a\u00020\u000eH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\n \u0016*\u0004\u0018\u00010\u00150\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0010\"\u0004\b\u001b\u0010\u0012R\u000e\u0010\u001c\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000¨\u0006Y"}, mo65043d2 = {"Lcom/android/wm/shell/pip/tv/TvPipKeepClearAlgorithm;", "", "()V", "lastAreasOverlappingUnstashPosition", "", "Landroid/graphics/Rect;", "maxRestrictedDistanceFraction", "", "getMaxRestrictedDistanceFraction", "()D", "setMaxRestrictedDistanceFraction", "(D)V", "movementBounds", "pipAreaPadding", "", "getPipAreaPadding", "()I", "setPipAreaPadding", "(I)V", "pipGravity", "pipPermanentDecorInsets", "Landroid/graphics/Insets;", "kotlin.jvm.PlatformType", "screenSize", "Landroid/util/Size;", "stashOffset", "getStashOffset", "setStashOffset", "transformedMovementBounds", "transformedScreenBounds", "addDecors", "size", "calculatePipPosition", "Lcom/android/wm/shell/pip/tv/TvPipKeepClearAlgorithm$Placement;", "pipSize", "restrictedAreas", "unrestrictedAreas", "calculatePipPositionTransformed", "pipAnchorBounds", "candidateCost", "candidateBounds", "findFreeMovePosition", "findMinMoveDown", "Lcom/android/wm/shell/pip/tv/TvPipKeepClearAlgorithm$SweepLineEvent;", "pipBounds", "findMinMoveUp", "findRelaxedMovePosition", "depth", "", "fromTransformedSpace", "r", "getNearbyStashedPosition", "bounds", "keepClearAreas", "getNormalPipAnchorBounds", "getStashType", "stashedBounds", "unstashedDestBounds", "isPipAnchoredToCorner", "", "removePermanentDecors", "setGravity", "", "gravity", "setMovementBounds", "setPipPermanentDecorInsets", "insets", "setScreenSize", "shouldTransformFlipX", "shouldTransformFlipY", "shouldTransformRotate", "sweepLineFindEarliestGap", "events", "", "gapSize", "startPos", "startGapSize", "toTransformedSpace", "transformAndFilterAreas", "areas", "intersects", "other", "intersectsX", "intersectsY", "offsetCopy", "dx", "dy", "Placement", "SweepLineEvent", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm */
/* compiled from: TvPipKeepClearAlgorithm.kt */
public final class TvPipKeepClearAlgorithm {
    private Set<Rect> lastAreasOverlappingUnstashPosition = SetsKt.emptySet();
    private double maxRestrictedDistanceFraction = 0.15d;
    private Rect movementBounds = new Rect();
    private int pipAreaPadding = 48;
    private int pipGravity = 85;
    private Insets pipPermanentDecorInsets = Insets.NONE;
    private Size screenSize = new Size(0, 0);
    private int stashOffset = 48;
    private Rect transformedMovementBounds = new Rect();
    private Rect transformedScreenBounds = new Rect();

    @Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0006HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\tHÆ\u0003J=\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u0019\u001a\u00020\t2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0006\u0010\u001b\u001a\u00020\u0003J\t\u0010\u001c\u001a\u00020\u0006HÖ\u0001J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f¨\u0006\u001f"}, mo65043d2 = {"Lcom/android/wm/shell/pip/tv/TvPipKeepClearAlgorithm$Placement;", "", "bounds", "Landroid/graphics/Rect;", "anchorBounds", "stashType", "", "unstashDestinationBounds", "triggerStash", "", "(Landroid/graphics/Rect;Landroid/graphics/Rect;ILandroid/graphics/Rect;Z)V", "getAnchorBounds", "()Landroid/graphics/Rect;", "getBounds", "getStashType", "()I", "getTriggerStash", "()Z", "getUnstashDestinationBounds", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "getUnstashedBounds", "hashCode", "toString", "", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement */
    /* compiled from: TvPipKeepClearAlgorithm.kt */
    public static final class Placement {
        private final Rect anchorBounds;
        private final Rect bounds;
        private final int stashType;
        private final boolean triggerStash;
        private final Rect unstashDestinationBounds;

        public static /* synthetic */ Placement copy$default(Placement placement, Rect rect, Rect rect2, int i, Rect rect3, boolean z, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                rect = placement.bounds;
            }
            if ((i2 & 2) != 0) {
                rect2 = placement.anchorBounds;
            }
            Rect rect4 = rect2;
            if ((i2 & 4) != 0) {
                i = placement.stashType;
            }
            int i3 = i;
            if ((i2 & 8) != 0) {
                rect3 = placement.unstashDestinationBounds;
            }
            Rect rect5 = rect3;
            if ((i2 & 16) != 0) {
                z = placement.triggerStash;
            }
            return placement.copy(rect, rect4, i3, rect5, z);
        }

        public final Rect component1() {
            return this.bounds;
        }

        public final Rect component2() {
            return this.anchorBounds;
        }

        public final int component3() {
            return this.stashType;
        }

        public final Rect component4() {
            return this.unstashDestinationBounds;
        }

        public final boolean component5() {
            return this.triggerStash;
        }

        public final Placement copy(Rect rect, Rect rect2, int i, Rect rect3, boolean z) {
            Intrinsics.checkNotNullParameter(rect, "bounds");
            Intrinsics.checkNotNullParameter(rect2, "anchorBounds");
            return new Placement(rect, rect2, i, rect3, z);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Placement)) {
                return false;
            }
            Placement placement = (Placement) obj;
            return Intrinsics.areEqual((Object) this.bounds, (Object) placement.bounds) && Intrinsics.areEqual((Object) this.anchorBounds, (Object) placement.anchorBounds) && this.stashType == placement.stashType && Intrinsics.areEqual((Object) this.unstashDestinationBounds, (Object) placement.unstashDestinationBounds) && this.triggerStash == placement.triggerStash;
        }

        public int hashCode() {
            int hashCode = ((((this.bounds.hashCode() * 31) + this.anchorBounds.hashCode()) * 31) + Integer.hashCode(this.stashType)) * 31;
            Rect rect = this.unstashDestinationBounds;
            int hashCode2 = (hashCode + (rect == null ? 0 : rect.hashCode())) * 31;
            boolean z = this.triggerStash;
            if (z) {
                z = true;
            }
            return hashCode2 + (z ? 1 : 0);
        }

        public String toString() {
            return "Placement(bounds=" + this.bounds + ", anchorBounds=" + this.anchorBounds + ", stashType=" + this.stashType + ", unstashDestinationBounds=" + this.unstashDestinationBounds + ", triggerStash=" + this.triggerStash + ')';
        }

        public Placement(Rect rect, Rect rect2, int i, Rect rect3, boolean z) {
            Intrinsics.checkNotNullParameter(rect, "bounds");
            Intrinsics.checkNotNullParameter(rect2, "anchorBounds");
            this.bounds = rect;
            this.anchorBounds = rect2;
            this.stashType = i;
            this.unstashDestinationBounds = rect3;
            this.triggerStash = z;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ Placement(Rect rect, Rect rect2, int i, Rect rect3, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(rect, rect2, (i2 & 4) != 0 ? 0 : i, (i2 & 8) != 0 ? null : rect3, (i2 & 16) != 0 ? false : z);
        }

        public final Rect getBounds() {
            return this.bounds;
        }

        public final Rect getAnchorBounds() {
            return this.anchorBounds;
        }

        public final int getStashType() {
            return this.stashType;
        }

        public final Rect getUnstashDestinationBounds() {
            return this.unstashDestinationBounds;
        }

        public final boolean getTriggerStash() {
            return this.triggerStash;
        }

        public final Rect getUnstashedBounds() {
            Rect rect = this.unstashDestinationBounds;
            return rect == null ? this.bounds : rect;
        }
    }

    public final int getPipAreaPadding() {
        return this.pipAreaPadding;
    }

    public final void setPipAreaPadding(int i) {
        this.pipAreaPadding = i;
    }

    public final int getStashOffset() {
        return this.stashOffset;
    }

    public final void setStashOffset(int i) {
        this.stashOffset = i;
    }

    public final double getMaxRestrictedDistanceFraction() {
        return this.maxRestrictedDistanceFraction;
    }

    public final void setMaxRestrictedDistanceFraction(double d) {
        this.maxRestrictedDistanceFraction = d;
    }

    public final Placement calculatePipPosition(Size size, Set<Rect> set, Set<Rect> set2) {
        Intrinsics.checkNotNullParameter(size, "pipSize");
        Intrinsics.checkNotNullParameter(set, "restrictedAreas");
        Intrinsics.checkNotNullParameter(set2, "unrestrictedAreas");
        Placement calculatePipPositionTransformed = calculatePipPositionTransformed(getNormalPipAnchorBounds(addDecors(size), this.transformedMovementBounds), transformAndFilterAreas(set), transformAndFilterAreas(set2));
        Rect removePermanentDecors = removePermanentDecors(fromTransformedSpace(calculatePipPositionTransformed.getBounds()));
        Rect removePermanentDecors2 = removePermanentDecors(fromTransformedSpace(calculatePipPositionTransformed.getAnchorBounds()));
        Rect unstashDestinationBounds = calculatePipPositionTransformed.getUnstashDestinationBounds();
        Rect removePermanentDecors3 = unstashDestinationBounds != null ? removePermanentDecors(fromTransformedSpace(unstashDestinationBounds)) : null;
        return new Placement(removePermanentDecors, removePermanentDecors2, getStashType(removePermanentDecors, removePermanentDecors3), removePermanentDecors3, calculatePipPositionTransformed.getTriggerStash());
    }

    private final Set<Rect> transformAndFilterAreas(Set<Rect> set) {
        Rect rect;
        Collection linkedHashSet = new LinkedHashSet();
        for (Rect rect2 : set) {
            if (rect2.contains(this.movementBounds)) {
                rect = null;
            } else {
                rect = toTransformedSpace(rect2);
            }
            if (rect != null) {
                linkedHashSet.add(rect);
            }
        }
        return (Set) linkedHashSet;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.android.p019wm.shell.pip.p020tv.TvPipKeepClearAlgorithm.Placement calculatePipPositionTransformed(android.graphics.Rect r11, java.util.Set<android.graphics.Rect> r12, java.util.Set<android.graphics.Rect> r13) {
        /*
            r10 = this;
            r3 = r13
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.Set r3 = kotlin.collections.SetsKt.plus(r12, r3)
            r5 = r3
            java.lang.Iterable r5 = (java.lang.Iterable) r5
            boolean r6 = r5 instanceof java.util.Collection
            r7 = 1
            if (r6 == 0) goto L_0x001a
            r6 = r5
            java.util.Collection r6 = (java.util.Collection) r6
            boolean r6 = r6.isEmpty()
            if (r6 == 0) goto L_0x001a
        L_0x0018:
            r6 = r7
            goto L_0x0031
        L_0x001a:
            java.util.Iterator r6 = r5.iterator()
        L_0x001e:
            boolean r8 = r6.hasNext()
            if (r8 == 0) goto L_0x0018
            java.lang.Object r8 = r6.next()
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            boolean r8 = r10.intersects(r8, r11)
            if (r8 == 0) goto L_0x001e
            r6 = 0
        L_0x0031:
            if (r6 == 0) goto L_0x0048
            java.util.Set r1 = kotlin.collections.SetsKt.emptySet()
            r10.lastAreasOverlappingUnstashPosition = r1
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r8 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 28
            r7 = 0
            r0 = r8
            r1 = r11
            r2 = r11
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r8
        L_0x0048:
            android.graphics.Rect r6 = r10.findFreeMovePosition(r11, r12, r13)
            if (r6 == 0) goto L_0x0065
            java.util.Set r1 = kotlin.collections.SetsKt.emptySet()
            r10.lastAreasOverlappingUnstashPosition = r1
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r8 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            r3 = 0
            r4 = 0
            r5 = 0
            r7 = 28
            r9 = 0
            r0 = r8
            r1 = r6
            r2 = r11
            r6 = r7
            r7 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r8
        L_0x0065:
            android.graphics.Rect r4 = r10.findRelaxedMovePosition(r11, r12, r13)
            if (r4 != 0) goto L_0x0078
            java.util.Set r4 = kotlin.collections.SetsKt.emptySet()
            android.graphics.Rect r1 = r10.findFreeMovePosition(r11, r4, r13)
            if (r1 != 0) goto L_0x0077
            r4 = r11
            goto L_0x0078
        L_0x0077:
            r4 = r1
        L_0x0078:
            java.util.LinkedHashSet r1 = new java.util.LinkedHashSet
            r1.<init>()
            java.util.Set r1 = (java.util.Set) r1
            java.util.Collection r1 = (java.util.Collection) r1
            java.util.Iterator r5 = r5.iterator()
        L_0x0085:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x009c
            java.lang.Object r6 = r5.next()
            r8 = r6
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            boolean r8 = r10.intersects(r8, r4)
            if (r8 == 0) goto L_0x0085
            r1.add(r6)
            goto L_0x0085
        L_0x009c:
            java.util.Set r1 = (java.util.Set) r1
            java.util.Set<android.graphics.Rect> r5 = r10.lastAreasOverlappingUnstashPosition
            r6 = r1
            java.util.Collection r6 = (java.util.Collection) r6
            boolean r5 = r5.containsAll(r6)
            r5 = r5 ^ r7
            r10.lastAreasOverlappingUnstashPosition = r1
            android.graphics.Rect r1 = r10.getNearbyStashedPosition(r4, r3)
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r6 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            int r3 = r10.getStashType(r1, r4)
            r0 = r6
            r2 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.pip.p020tv.TvPipKeepClearAlgorithm.calculatePipPositionTransformed(android.graphics.Rect, java.util.Set, java.util.Set):com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement");
    }

    private final int getStashType(Rect rect, Rect rect2) {
        if (rect2 == null) {
            return 0;
        }
        if (rect.left < rect2.left) {
            return 1;
        }
        if (rect.right > rect2.right) {
            return 2;
        }
        if (rect.top < rect2.top) {
            return 4;
        }
        if (rect.bottom > rect2.bottom) {
            return 3;
        }
        return 0;
    }

    private final Rect findRelaxedMovePosition(Rect rect, Set<Rect> set, Set<Rect> set2) {
        return findRelaxedMovePosition(1, rect, CollectionsKt.toMutableSet(set), set2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: android.graphics.Rect} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final android.graphics.Rect findRelaxedMovePosition(int r5, android.graphics.Rect r6, java.util.Set<android.graphics.Rect> r7, java.util.Set<android.graphics.Rect> r8) {
        /*
            r4 = this;
            if (r5 != 0) goto L_0x0007
            android.graphics.Rect r4 = r4.findFreeMovePosition(r6, r7, r8)
            return r4
        L_0x0007:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.List r0 = (java.util.List) r0
            r1 = r7
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.List r1 = kotlin.collections.CollectionsKt.toList(r1)
            java.util.Iterator r1 = r1.iterator()
        L_0x0019:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0037
            java.lang.Object r2 = r1.next()
            android.graphics.Rect r2 = (android.graphics.Rect) r2
            r7.remove(r2)
            int r3 = r5 + -1
            android.graphics.Rect r3 = r4.findRelaxedMovePosition(r3, r6, r7, r8)
            r7.add(r2)
            if (r3 == 0) goto L_0x0019
            r0.add(r3)
            goto L_0x0019
        L_0x0037:
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r5 = r0.iterator()
            boolean r7 = r5.hasNext()
            if (r7 != 0) goto L_0x0045
            r4 = 0
            goto L_0x006e
        L_0x0045:
            java.lang.Object r7 = r5.next()
            boolean r8 = r5.hasNext()
            if (r8 != 0) goto L_0x0051
        L_0x004f:
            r4 = r7
            goto L_0x006e
        L_0x0051:
            r8 = r7
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            int r8 = r4.candidateCost(r8, r6)
        L_0x0058:
            java.lang.Object r0 = r5.next()
            r1 = r0
            android.graphics.Rect r1 = (android.graphics.Rect) r1
            int r1 = r4.candidateCost(r1, r6)
            if (r8 <= r1) goto L_0x0067
            r7 = r0
            r8 = r1
        L_0x0067:
            boolean r0 = r5.hasNext()
            if (r0 != 0) goto L_0x0058
            goto L_0x004f
        L_0x006e:
            android.graphics.Rect r4 = (android.graphics.Rect) r4
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.pip.p020tv.TvPipKeepClearAlgorithm.findRelaxedMovePosition(int, android.graphics.Rect, java.util.Set, java.util.Set):android.graphics.Rect");
    }

    /* access modifiers changed from: private */
    public final int candidateCost(Rect rect, Rect rect2) {
        int i = rect.left - rect2.left;
        int i2 = rect.top - rect2.top;
        return (i * i) + (i2 * i2);
    }

    private final Rect findFreeMovePosition(Rect rect, Set<Rect> set, Set<Rect> set2) {
        boolean z;
        Rect rect2 = rect;
        Set<Rect> set3 = set;
        Set<Rect> set4 = set2;
        Rect rect3 = this.transformedMovementBounds;
        List arrayList = new ArrayList();
        double width = ((double) rect2.right) - (((double) this.screenSize.getWidth()) * this.maxRestrictedDistanceFraction);
        int i = 0;
        arrayList.add(offsetCopy(rect3, rect3.width() + this.pipAreaPadding, 0));
        arrayList.addAll(set4);
        Collection arrayList2 = new ArrayList();
        Iterator it = set3.iterator();
        while (true) {
            z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (((double) ((Rect) next).left) < width) {
                z = false;
            }
            if (z) {
                arrayList2.add(next);
            }
        }
        arrayList.addAll((List) arrayList2);
        CollectionsKt.retainAll(arrayList, new TvPipKeepClearAlgorithm$findFreeMovePosition$2(this, rect3.left + rect.width()));
        if (arrayList.size() > 1) {
            CollectionsKt.sortWith(arrayList, new TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$1());
        }
        int roundToInt = MathKt.roundToInt(((double) this.screenSize.getHeight()) * this.maxRestrictedDistanceFraction);
        List arrayList3 = new ArrayList();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            Rect rect4 = (Rect) it2.next();
            int width2 = ((rect4.left - this.pipAreaPadding) - rect.width()) - rect2.left;
            Rect offsetCopy = offsetCopy(rect2, width2, i);
            boolean isPipAnchoredToCorner = isPipAnchoredToCorner() ^ z;
            SweepLineEvent findMinMoveUp = findMinMoveUp(offsetCopy, set3, set4);
            int pos = (findMinMoveUp.getPos() - rect2.bottom) - (findMinMoveUp.getStart() ? i : this.pipAreaPadding);
            int height = findMinMoveUp.getUnrestricted() ? rect3.height() : roundToInt;
            Rect offsetCopy2 = offsetCopy(rect2, width2, pos);
            Iterator it3 = it2;
            boolean z2 = offsetCopy2.top > rect3.top;
            boolean z3 = !intersectsY(offsetCopy2, rect4);
            if (z2 && Math.abs(pos) <= height && !z3) {
                arrayList3.add(offsetCopy2);
            }
            if (isPipAnchoredToCorner) {
                SweepLineEvent findMinMoveDown = findMinMoveDown(offsetCopy, set3, set4);
                int pos2 = (findMinMoveDown.getPos() - rect2.top) + (findMinMoveDown.getStart() ? 0 : this.pipAreaPadding);
                int height2 = findMinMoveDown.getUnrestricted() ? rect3.height() : roundToInt;
                Rect offsetCopy3 = offsetCopy(rect2, width2, pos2);
                boolean z4 = offsetCopy3.bottom < rect3.bottom;
                boolean z5 = !intersectsY(offsetCopy3, rect4);
                if (z4 && Math.abs(pos2) <= height2 && !z5) {
                    arrayList3.add(offsetCopy3);
                }
            }
            it2 = it3;
            i = 0;
            z = true;
        }
        if (arrayList3.size() > 1) {
            CollectionsKt.sortWith(arrayList3, new TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$2(this, rect2));
        }
        return (Rect) CollectionsKt.firstOrNull(arrayList3);
    }

    private final Rect getNearbyStashedPosition(Rect rect, Set<Rect> set) {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        Rect rect2 = this.transformedScreenBounds;
        List arrayList = new ArrayList();
        Iterable iterable = set;
        Collection arrayList2 = new ArrayList();
        for (Object next : iterable) {
            if (intersectsX((Rect) next, rect)) {
                arrayList2.add(next);
            }
        }
        List list = (List) arrayList2;
        Collection arrayList3 = new ArrayList();
        for (Object next2 : iterable) {
            if (intersectsY((Rect) next2, rect)) {
                arrayList3.add(next2);
            }
        }
        List list2 = (List) arrayList3;
        Object obj5 = null;
        if (!list.isEmpty()) {
            if (rect2.bottom - rect.bottom <= rect.top - rect2.top) {
                int i = rect2.bottom - this.stashOffset;
                Iterator it = list.iterator();
                if (!it.hasNext()) {
                    obj4 = null;
                } else {
                    obj4 = it.next();
                    if (it.hasNext()) {
                        int i2 = ((Rect) obj4).bottom;
                        do {
                            Object next3 = it.next();
                            int i3 = ((Rect) next3).bottom;
                            if (i2 < i3) {
                                obj4 = next3;
                                i2 = i3;
                            }
                        } while (it.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj4);
                int min = Math.min(i, ((Rect) obj4).bottom + this.pipAreaPadding);
                if (min > rect.top) {
                    Rect rect3 = new Rect(rect);
                    rect3.offsetTo(rect.left, min);
                    arrayList.add(rect3);
                }
            }
            if (rect2.bottom - rect.bottom >= rect.top - rect2.top) {
                int height = (rect2.top - rect.height()) + this.stashOffset;
                Iterator it2 = list.iterator();
                if (!it2.hasNext()) {
                    obj3 = null;
                } else {
                    obj3 = it2.next();
                    if (it2.hasNext()) {
                        int i4 = ((Rect) obj3).top;
                        do {
                            Object next4 = it2.next();
                            int i5 = ((Rect) next4).top;
                            if (i4 > i5) {
                                obj3 = next4;
                                i4 = i5;
                            }
                        } while (it2.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj3);
                int max = Math.max(height, (((Rect) obj3).top - rect.height()) - this.pipAreaPadding);
                if (max < rect.top) {
                    Rect rect4 = new Rect(rect);
                    rect4.offsetTo(rect.left, max);
                    arrayList.add(rect4);
                }
            }
        }
        if (!list2.isEmpty()) {
            if (rect2.right - rect.right <= rect.left - rect2.left) {
                int i6 = rect2.right - this.stashOffset;
                Iterator it3 = list2.iterator();
                if (!it3.hasNext()) {
                    obj2 = null;
                } else {
                    obj2 = it3.next();
                    if (it3.hasNext()) {
                        int i7 = ((Rect) obj2).right;
                        do {
                            Object next5 = it3.next();
                            int i8 = ((Rect) next5).right;
                            if (i7 < i8) {
                                obj2 = next5;
                                i7 = i8;
                            }
                        } while (it3.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj2);
                int min2 = Math.min(i6, ((Rect) obj2).right + this.pipAreaPadding);
                if (min2 > rect.left) {
                    Rect rect5 = new Rect(rect);
                    rect5.offsetTo(min2, rect.top);
                    arrayList.add(rect5);
                }
            }
            if (rect2.right - rect.right >= rect.left - rect2.left) {
                int width = (rect2.left - rect.width()) + this.stashOffset;
                Iterator it4 = list2.iterator();
                if (!it4.hasNext()) {
                    obj = null;
                } else {
                    obj = it4.next();
                    if (it4.hasNext()) {
                        int i9 = ((Rect) obj).left;
                        do {
                            Object next6 = it4.next();
                            int i10 = ((Rect) next6).left;
                            if (i9 > i10) {
                                obj = next6;
                                i9 = i10;
                            }
                        } while (it4.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj);
                int max2 = Math.max(width, (((Rect) obj).left - rect.width()) - this.pipAreaPadding);
                if (max2 < rect.left) {
                    Rect rect6 = new Rect(rect);
                    rect6.offsetTo(max2, rect.top);
                    arrayList.add(rect6);
                }
            }
        }
        Iterator it5 = arrayList.iterator();
        if (it5.hasNext()) {
            obj5 = it5.next();
            if (it5.hasNext()) {
                Rect rect7 = (Rect) obj5;
                int abs = Math.abs(rect7.left - rect.left) + Math.abs(rect7.top - rect.top);
                do {
                    Object next7 = it5.next();
                    Rect rect8 = (Rect) next7;
                    int abs2 = Math.abs(rect8.left - rect.left) + Math.abs(rect8.top - rect.top);
                    if (abs > abs2) {
                        obj5 = next7;
                        abs = abs2;
                    }
                } while (it5.hasNext());
            }
        }
        Rect rect9 = (Rect) obj5;
        return rect9 == null ? rect : rect9;
    }

    public final void setScreenSize(Size size) {
        Intrinsics.checkNotNullParameter(size, AuthDialog.KEY_BIOMETRIC_DIALOG_SIZE);
        if (!Intrinsics.areEqual((Object) this.screenSize, (Object) size)) {
            this.screenSize = size;
            this.transformedScreenBounds = toTransformedSpace(new Rect(0, 0, this.screenSize.getWidth(), this.screenSize.getHeight()));
            this.transformedMovementBounds = toTransformedSpace(this.transformedMovementBounds);
        }
    }

    public final void setMovementBounds(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "bounds");
        if (!Intrinsics.areEqual((Object) this.movementBounds, (Object) rect)) {
            this.movementBounds.set(rect);
            this.transformedMovementBounds = toTransformedSpace(this.movementBounds);
        }
    }

    public final void setGravity(int i) {
        if (this.pipGravity != i) {
            this.pipGravity = i;
            this.transformedScreenBounds = toTransformedSpace(new Rect(0, 0, this.screenSize.getWidth(), this.screenSize.getHeight()));
            this.transformedMovementBounds = toTransformedSpace(this.movementBounds);
        }
    }

    public final void setPipPermanentDecorInsets(Insets insets) {
        Intrinsics.checkNotNullParameter(insets, "insets");
        this.pipPermanentDecorInsets = insets;
    }

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00032\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/wm/shell/pip/tv/TvPipKeepClearAlgorithm$SweepLineEvent;", "", "open", "", "pos", "", "unrestricted", "start", "(ZIZZ)V", "getOpen", "()Z", "getPos", "()I", "getStart", "getUnrestricted", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$SweepLineEvent */
    /* compiled from: TvPipKeepClearAlgorithm.kt */
    public static final class SweepLineEvent {
        private final boolean open;
        private final int pos;
        private final boolean start;
        private final boolean unrestricted;

        public static /* synthetic */ SweepLineEvent copy$default(SweepLineEvent sweepLineEvent, boolean z, int i, boolean z2, boolean z3, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = sweepLineEvent.open;
            }
            if ((i2 & 2) != 0) {
                i = sweepLineEvent.pos;
            }
            if ((i2 & 4) != 0) {
                z2 = sweepLineEvent.unrestricted;
            }
            if ((i2 & 8) != 0) {
                z3 = sweepLineEvent.start;
            }
            return sweepLineEvent.copy(z, i, z2, z3);
        }

        public final boolean component1() {
            return this.open;
        }

        public final int component2() {
            return this.pos;
        }

        public final boolean component3() {
            return this.unrestricted;
        }

        public final boolean component4() {
            return this.start;
        }

        public final SweepLineEvent copy(boolean z, int i, boolean z2, boolean z3) {
            return new SweepLineEvent(z, i, z2, z3);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SweepLineEvent)) {
                return false;
            }
            SweepLineEvent sweepLineEvent = (SweepLineEvent) obj;
            return this.open == sweepLineEvent.open && this.pos == sweepLineEvent.pos && this.unrestricted == sweepLineEvent.unrestricted && this.start == sweepLineEvent.start;
        }

        public int hashCode() {
            boolean z = this.open;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int hashCode = (((z ? 1 : 0) * true) + Integer.hashCode(this.pos)) * 31;
            boolean z3 = this.unrestricted;
            if (z3) {
                z3 = true;
            }
            int i = (hashCode + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.start;
            if (!z4) {
                z2 = z4;
            }
            return i + (z2 ? 1 : 0);
        }

        public String toString() {
            return "SweepLineEvent(open=" + this.open + ", pos=" + this.pos + ", unrestricted=" + this.unrestricted + ", start=" + this.start + ')';
        }

        public SweepLineEvent(boolean z, int i, boolean z2, boolean z3) {
            this.open = z;
            this.pos = i;
            this.unrestricted = z2;
            this.start = z3;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ SweepLineEvent(boolean z, int i, boolean z2, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(z, i, z2, (i2 & 8) != 0 ? false : z3);
        }

        public final boolean getOpen() {
            return this.open;
        }

        public final int getPos() {
            return this.pos;
        }

        public final boolean getUnrestricted() {
            return this.unrestricted;
        }

        public final boolean getStart() {
            return this.start;
        }
    }

    private final SweepLineEvent findMinMoveUp(Rect rect, Set<Rect> set, Set<Rect> set2) {
        List arrayList = new ArrayList();
        Function1 tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1 = new TvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1(this, rect, arrayList);
        Function1 function1 = (Function1) tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1.invoke(false);
        for (Object invoke : set) {
            function1.invoke(invoke);
        }
        Function1 function12 = (Function1) tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1.invoke(true);
        for (Object invoke2 : set2) {
            function12.invoke(invoke2);
        }
        return sweepLineFindEarliestGap(arrayList, rect.height() + this.pipAreaPadding, rect.bottom, rect.height());
    }

    private final SweepLineEvent findMinMoveDown(Rect rect, Set<Rect> set, Set<Rect> set2) {
        List arrayList = new ArrayList();
        Function1 tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 = new TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1(this, rect, arrayList);
        Function1 function1 = (Function1) tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1.invoke(false);
        for (Object invoke : set) {
            function1.invoke(invoke);
        }
        Function1 function12 = (Function1) tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1.invoke(true);
        for (Object invoke2 : set2) {
            function12.invoke(invoke2);
        }
        SweepLineEvent sweepLineFindEarliestGap = sweepLineFindEarliestGap(arrayList, rect.height() + this.pipAreaPadding, -rect.top, rect.height());
        return SweepLineEvent.copy$default(sweepLineFindEarliestGap, false, -sweepLineFindEarliestGap.getPos(), false, false, 13, (Object) null);
    }

    private final SweepLineEvent sweepLineFindEarliestGap(List<SweepLineEvent> list, int i, int i2, int i3) {
        int pos;
        list.add(new SweepLineEvent(false, i2, true, true));
        if (list.size() > 1) {
            CollectionsKt.sortWith(list, new C3552xdc47610d());
        }
        int i4 = 0;
        for (int i5 = 0; i5 < list.size(); i5++) {
            SweepLineEvent sweepLineEvent = list.get(i5);
            if (!sweepLineEvent.getStart()) {
                i4 = sweepLineEvent.getOpen() ? i4 + 1 : i4 - 1;
            }
            if (i4 == 0 && (pos = sweepLineEvent.getPos()) <= i2) {
                int i6 = sweepLineEvent.getStart() ? i3 : i;
                SweepLineEvent sweepLineEvent2 = (SweepLineEvent) CollectionsKt.getOrNull(list, i5 + 1);
                if (sweepLineEvent2 == null || sweepLineEvent2.getPos() < pos - i6) {
                    return sweepLineEvent;
                }
            }
        }
        return (SweepLineEvent) CollectionsKt.last(list);
    }

    private final boolean shouldTransformFlipX() {
        int i = this.pipGravity;
        return i == 3 || i == 19 || i == 51 || i == 83 || i == 48 || i == 49;
    }

    private final boolean shouldTransformFlipY() {
        int i = this.pipGravity;
        return i == 51 || i == 53;
    }

    private final boolean shouldTransformRotate() {
        int i = this.pipGravity;
        int i2 = i & 7;
        if (i2 == 3 || i2 == 5) {
            return false;
        }
        int i3 = i & 112;
        return i3 == 48 || i3 == 80;
    }

    private final Rect toTransformedSpace(Rect rect) {
        int width = this.screenSize.getWidth();
        int height = this.screenSize.getHeight();
        Point[] pointArr = {new Point(rect.left, rect.top), new Point(rect.right, rect.top), new Point(rect.right, rect.bottom), new Point(rect.left, rect.bottom)};
        if (shouldTransformRotate()) {
            for (int i = 0; i < 4; i++) {
                Point point = pointArr[i];
                int i2 = point.x;
                point.x = point.y;
                point.y = -i2;
                point.y += width;
            }
            width = this.screenSize.getHeight();
            height = this.screenSize.getWidth();
        }
        for (int i3 = 0; i3 < 4; i3++) {
            Point point2 = pointArr[i3];
            if (shouldTransformFlipX()) {
                point2.x = width - point2.x;
            }
            if (shouldTransformFlipY()) {
                point2.y = height - point2.y;
            }
        }
        Point point3 = pointArr[0];
        int lastIndex = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex != 0) {
            int i4 = point3.y;
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                Point point4 = pointArr[it.nextInt()];
                int i5 = point4.y;
                if (i4 > i5) {
                    point3 = point4;
                    i4 = i5;
                }
            }
        }
        Intrinsics.checkNotNull(point3);
        int i6 = point3.y;
        Point point5 = pointArr[0];
        int lastIndex2 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex2 != 0) {
            int i7 = point5.x;
            IntIterator it2 = new IntRange(1, lastIndex2).iterator();
            while (it2.hasNext()) {
                Point point6 = pointArr[it2.nextInt()];
                int i8 = point6.x;
                if (i7 < i8) {
                    point5 = point6;
                    i7 = i8;
                }
            }
        }
        Intrinsics.checkNotNull(point5);
        int i9 = point5.x;
        Point point7 = pointArr[0];
        int lastIndex3 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex3 != 0) {
            int i10 = point7.y;
            IntIterator it3 = new IntRange(1, lastIndex3).iterator();
            while (it3.hasNext()) {
                Point point8 = pointArr[it3.nextInt()];
                int i11 = point8.y;
                if (i10 < i11) {
                    point7 = point8;
                    i10 = i11;
                }
            }
        }
        Intrinsics.checkNotNull(point7);
        int i12 = point7.y;
        Point point9 = pointArr[0];
        int lastIndex4 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex4 != 0) {
            int i13 = point9.x;
            IntIterator it4 = new IntRange(1, lastIndex4).iterator();
            while (it4.hasNext()) {
                Point point10 = pointArr[it4.nextInt()];
                int i14 = point10.x;
                if (i13 > i14) {
                    point9 = point10;
                    i13 = i14;
                }
            }
        }
        Intrinsics.checkNotNull(point9);
        return new Rect(point9.x, i6, i9, i12);
    }

    private final Rect fromTransformedSpace(Rect rect) {
        boolean shouldTransformRotate = shouldTransformRotate();
        Size size = this.screenSize;
        int height = shouldTransformRotate ? size.getHeight() : size.getWidth();
        Size size2 = this.screenSize;
        int width = shouldTransformRotate ? size2.getWidth() : size2.getHeight();
        Point[] pointArr = {new Point(rect.left, rect.top), new Point(rect.right, rect.top), new Point(rect.right, rect.bottom), new Point(rect.left, rect.bottom)};
        for (int i = 0; i < 4; i++) {
            Point point = pointArr[i];
            if (shouldTransformFlipX()) {
                point.x = height - point.x;
            }
            if (shouldTransformFlipY()) {
                point.y = width - point.y;
            }
        }
        if (shouldTransformRotate) {
            for (int i2 = 0; i2 < 4; i2++) {
                Point point2 = pointArr[i2];
                point2.y -= this.screenSize.getWidth();
                int i3 = point2.x;
                point2.x = -point2.y;
                point2.y = i3;
            }
        }
        Point point3 = pointArr[0];
        int lastIndex = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex != 0) {
            int i4 = point3.y;
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                Point point4 = pointArr[it.nextInt()];
                int i5 = point4.y;
                if (i4 > i5) {
                    point3 = point4;
                    i4 = i5;
                }
            }
        }
        Intrinsics.checkNotNull(point3);
        int i6 = point3.y;
        Point point5 = pointArr[0];
        int lastIndex2 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex2 != 0) {
            int i7 = point5.x;
            IntIterator it2 = new IntRange(1, lastIndex2).iterator();
            while (it2.hasNext()) {
                Point point6 = pointArr[it2.nextInt()];
                int i8 = point6.x;
                if (i7 < i8) {
                    point5 = point6;
                    i7 = i8;
                }
            }
        }
        Intrinsics.checkNotNull(point5);
        int i9 = point5.x;
        Point point7 = pointArr[0];
        int lastIndex3 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex3 != 0) {
            int i10 = point7.y;
            IntIterator it3 = new IntRange(1, lastIndex3).iterator();
            while (it3.hasNext()) {
                Point point8 = pointArr[it3.nextInt()];
                int i11 = point8.y;
                if (i10 < i11) {
                    point7 = point8;
                    i10 = i11;
                }
            }
        }
        Intrinsics.checkNotNull(point7);
        int i12 = point7.y;
        Point point9 = pointArr[0];
        int lastIndex4 = ArraysKt.getLastIndex((T[]) pointArr);
        if (lastIndex4 != 0) {
            int i13 = point9.x;
            IntIterator it4 = new IntRange(1, lastIndex4).iterator();
            while (it4.hasNext()) {
                Point point10 = pointArr[it4.nextInt()];
                int i14 = point10.x;
                if (i13 > i14) {
                    point9 = point10;
                    i13 = i14;
                }
            }
        }
        Intrinsics.checkNotNull(point9);
        return new Rect(point9.x, i6, i9, i12);
    }

    private final Rect getNormalPipAnchorBounds(Size size, Rect rect) {
        if (shouldTransformRotate()) {
            size = new Size(size.getHeight(), size.getWidth());
        }
        Rect rect2 = new Rect();
        if (isPipAnchoredToCorner()) {
            Gravity.apply(85, size.getWidth(), size.getHeight(), rect, rect2);
            return rect2;
        }
        Gravity.apply(5, size.getWidth(), size.getHeight(), rect, rect2);
        return rect2;
    }

    private final boolean isPipAnchoredToCorner() {
        int i = this.pipGravity;
        return (((i & 7) == 3) || ((i & 7) == 5)) && (((i & 112) == 48) || ((i & 112) == 80));
    }

    private final Size addDecors(Size size) {
        Rect rect = new Rect(0, 0, size.getWidth(), size.getHeight());
        rect.inset(this.pipPermanentDecorInsets);
        return new Size(rect.width(), rect.height());
    }

    private final Rect removePermanentDecors(Rect rect) {
        rect.inset(Insets.subtract(Insets.NONE, this.pipPermanentDecorInsets));
        return rect;
    }

    private final Rect offsetCopy(Rect rect, int i, int i2) {
        Rect rect2 = new Rect(rect);
        rect2.offset(i, i2);
        return rect2;
    }

    /* access modifiers changed from: private */
    public final boolean intersectsX(Rect rect, Rect rect2) {
        return rect.right >= rect2.left && rect.left <= rect2.right;
    }

    private final boolean intersectsY(Rect rect, Rect rect2) {
        return rect.bottom >= rect2.top && rect.top <= rect2.bottom;
    }

    private final boolean intersects(Rect rect, Rect rect2) {
        return intersectsX(rect, rect2) && intersectsY(rect, rect2);
    }
}
