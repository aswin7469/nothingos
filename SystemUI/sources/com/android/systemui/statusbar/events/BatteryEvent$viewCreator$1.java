package com.android.systemui.statusbar.events;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.statusbar.phone.ScrimController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/events/BGImageView;", "context", "Landroid/content/Context;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusEvent.kt */
final class BatteryEvent$viewCreator$1 extends Lambda implements Function1<Context, BGImageView> {
    public static final BatteryEvent$viewCreator$1 INSTANCE = new BatteryEvent$viewCreator$1();

    BatteryEvent$viewCreator$1() {
        super(1);
    }

    public final BGImageView invoke(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        BGImageView bGImageView = new BGImageView(context);
        bGImageView.setImageDrawable(new ThemedBatteryDrawable(context, -1));
        bGImageView.setBackgroundDrawable(new ColorDrawable(ScrimController.DEBUG_FRONT_TINT));
        return bGImageView;
    }
}
