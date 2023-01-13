package com.android.systemui.statusbar.events;

import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bXD¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R/\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u00130\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/BatteryEvent;", "Lcom/android/systemui/statusbar/events/StatusEvent;", "()V", "forceVisible", "", "getForceVisible", "()Z", "priority", "", "getPriority", "()I", "showAnimation", "getShowAnimation", "viewCreator", "Lkotlin/Function1;", "Landroid/content/Context;", "Lkotlin/ParameterName;", "name", "context", "Lcom/android/systemui/statusbar/events/BGImageView;", "getViewCreator", "()Lkotlin/jvm/functions/Function1;", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusEvent.kt */
public final class BatteryEvent implements StatusEvent {
    private final boolean forceVisible;
    private final int priority = 50;
    private final boolean showAnimation = true;
    private final Function1<Context, BGImageView> viewCreator = BatteryEvent$viewCreator$1.INSTANCE;

    public int getPriority() {
        return this.priority;
    }

    public boolean getForceVisible() {
        return this.forceVisible;
    }

    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    public Function1<Context, BGImageView> getViewCreator() {
        return this.viewCreator;
    }

    public String toString() {
        String simpleName = getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
