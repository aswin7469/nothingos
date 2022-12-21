package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.util.Preconditions;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public abstract class ComplicationHostViewModule {
    public static final String COMPLICATIONS_FADE_IN_DURATION = "complications_fade_in_duration";
    public static final String COMPLICATIONS_FADE_OUT_DURATION = "complications_fade_out_duration";
    public static final String COMPLICATIONS_RESTORE_TIMEOUT = "complication_restore_timeout";
    public static final String COMPLICATION_MARGIN = "complication_margin";
    public static final String SCOPED_COMPLICATIONS_LAYOUT = "scoped_complications_layout";

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("scoped_complications_layout")
    static ConstraintLayout providesComplicationHostView(LayoutInflater layoutInflater) {
        return (ConstraintLayout) Preconditions.checkNotNull((ConstraintLayout) layoutInflater.inflate(C1893R.layout.dream_overlay_complications_layer, (ViewGroup) null), "R.layout.dream_overlay_complications_layer did not properly inflated");
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("complication_margin")
    static int providesComplicationPadding(@Main Resources resources) {
        return resources.getDimensionPixelSize(C1893R.dimen.dream_overlay_complication_margin);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("complications_fade_out_duration")
    static int providesComplicationsFadeOutDuration(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.complicationFadeOutMs);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("complications_fade_in_duration")
    static int providesComplicationsFadeInDuration(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.complicationFadeInMs);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("complication_restore_timeout")
    static int providesComplicationsRestoreTimeout(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.complicationRestoreMs);
    }
}
