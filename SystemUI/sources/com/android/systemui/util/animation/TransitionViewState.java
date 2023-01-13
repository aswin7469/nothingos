package com.android.systemui.util.animation;

import android.graphics.PointF;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u001f\u001a\u00020\u00002\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0000J\u000e\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\fR&\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00170\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012¨\u0006%"}, mo65043d2 = {"Lcom/android/systemui/util/animation/TransitionViewState;", "", "()V", "alpha", "", "getAlpha", "()F", "setAlpha", "(F)V", "contentTranslation", "Landroid/graphics/PointF;", "getContentTranslation", "()Landroid/graphics/PointF;", "height", "", "getHeight", "()I", "setHeight", "(I)V", "translation", "getTranslation", "widgetStates", "", "Lcom/android/systemui/util/animation/WidgetState;", "getWidgetStates", "()Ljava/util/Map;", "setWidgetStates", "(Ljava/util/Map;)V", "width", "getWidth", "setWidth", "copy", "reusedState", "initFromLayout", "", "transitionLayout", "Lcom/android/systemui/util/animation/TransitionLayout;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TransitionLayout.kt */
public final class TransitionViewState {
    private float alpha = 1.0f;
    private final PointF contentTranslation = new PointF();
    private int height;
    private final PointF translation = new PointF();
    private Map<Integer, WidgetState> widgetStates = new LinkedHashMap();
    private int width;

    public final Map<Integer, WidgetState> getWidgetStates() {
        return this.widgetStates;
    }

    public final void setWidgetStates(Map<Integer, WidgetState> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.widgetStates = map;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int i) {
        this.width = i;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int i) {
        this.height = i;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final PointF getTranslation() {
        return this.translation;
    }

    public final PointF getContentTranslation() {
        return this.contentTranslation;
    }

    public static /* synthetic */ TransitionViewState copy$default(TransitionViewState transitionViewState, TransitionViewState transitionViewState2, int i, Object obj) {
        if ((i & 1) != 0) {
            transitionViewState2 = null;
        }
        return transitionViewState.copy(transitionViewState2);
    }

    public final TransitionViewState copy(TransitionViewState transitionViewState) {
        TransitionViewState transitionViewState2 = transitionViewState == null ? new TransitionViewState() : transitionViewState;
        transitionViewState2.width = this.width;
        transitionViewState2.height = this.height;
        transitionViewState2.alpha = this.alpha;
        transitionViewState2.translation.set(this.translation.x, this.translation.y);
        transitionViewState2.contentTranslation.set(this.contentTranslation.x, this.contentTranslation.y);
        for (Map.Entry next : this.widgetStates.entrySet()) {
            transitionViewState2.widgetStates.put(next.getKey(), WidgetState.copy$default((WidgetState) next.getValue(), 0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, (Object) null));
        }
        return transitionViewState2;
    }

    public final void initFromLayout(TransitionLayout transitionLayout) {
        TransitionLayout transitionLayout2 = transitionLayout;
        Intrinsics.checkNotNullParameter(transitionLayout2, "transitionLayout");
        int childCount = transitionLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = transitionLayout2.getChildAt(i);
            Map<Integer, WidgetState> map = this.widgetStates;
            Integer valueOf = Integer.valueOf(childAt.getId());
            WidgetState widgetState = map.get(valueOf);
            if (widgetState == null) {
                widgetState = new WidgetState(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 384, (DefaultConstructorMarker) null);
                map.put(valueOf, widgetState);
            }
            Intrinsics.checkNotNullExpressionValue(childAt, "child");
            widgetState.initFromLayout(childAt);
        }
        this.width = transitionLayout.getMeasuredWidth();
        this.height = transitionLayout.getMeasuredHeight();
        this.translation.set(0.0f, 0.0f);
        this.contentTranslation.set(0.0f, 0.0f);
        this.alpha = 1.0f;
    }
}
