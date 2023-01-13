package com.android.systemui.media;

import android.view.View;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.UniqueObjectHostView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/media/MediaHost$init$2", "Lcom/android/systemui/util/animation/UniqueObjectHostView$MeasurementManager;", "onMeasure", "Lcom/android/systemui/util/animation/MeasurementOutput;", "input", "Lcom/android/systemui/util/animation/MeasurementInput;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHost.kt */
public final class MediaHost$init$2 implements UniqueObjectHostView.MeasurementManager {
    final /* synthetic */ int $location;
    final /* synthetic */ MediaHost this$0;

    MediaHost$init$2(MediaHost mediaHost, int i) {
        this.this$0 = mediaHost;
        this.$location = i;
    }

    public MeasurementOutput onMeasure(MeasurementInput measurementInput) {
        Intrinsics.checkNotNullParameter(measurementInput, "input");
        if (View.MeasureSpec.getMode(measurementInput.getWidthMeasureSpec()) == Integer.MIN_VALUE) {
            measurementInput.setWidthMeasureSpec(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measurementInput.getWidthMeasureSpec()), 1073741824));
        }
        this.this$0.state.setMeasurementInput(measurementInput);
        return this.this$0.mediaHostStatesManager.updateCarouselDimensions(this.$location, this.this$0.state);
    }
}
