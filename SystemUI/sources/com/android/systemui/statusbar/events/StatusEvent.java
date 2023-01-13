package com.android.systemui.statusbar.events;

import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0016\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u0000H\u0016J\u0012\u0010\u0018\u001a\u00020\u00192\b\u0010\u0017\u001a\u0004\u0018\u00010\u0000H\u0016R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005R1\u0010\f\u001a!\u0012\u0013\u0012\u00110\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\u00120\rj\u0002`\u0013X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u001aÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/StatusEvent;", "", "forceVisible", "", "getForceVisible", "()Z", "priority", "", "getPriority", "()I", "showAnimation", "getShowAnimation", "viewCreator", "Lkotlin/Function1;", "Landroid/content/Context;", "Lkotlin/ParameterName;", "name", "context", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "Lcom/android/systemui/statusbar/events/ViewCreator;", "getViewCreator", "()Lkotlin/jvm/functions/Function1;", "shouldUpdateFromEvent", "other", "updateFromEvent", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusEvent.kt */
public interface StatusEvent {
    boolean getForceVisible();

    int getPriority();

    boolean getShowAnimation();

    Function1<Context, BackgroundAnimatableView> getViewCreator();

    boolean shouldUpdateFromEvent(StatusEvent statusEvent) {
        return false;
    }

    void updateFromEvent(StatusEvent statusEvent) {
    }
}
