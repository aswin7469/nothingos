package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import com.android.internal.util.Preconditions;
import com.android.systemui.C1893R;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public interface DreamClockTimeComplicationModule {
    public static final String DREAM_CLOCK_TIME_COMPLICATION_LAYOUT_PARAMS = "clock_time_complication_layout_params";
    public static final String DREAM_CLOCK_TIME_COMPLICATION_VIEW = "clock_time_complication_view";
    public static final int INSERT_ORDER_WEIGHT = 0;
    public static final String TAG_WEIGHT = "'wght' ";
    public static final int WEIGHT = 200;

    @Provides
    @Named("clock_time_complication_view")
    static View provideComplicationView(LayoutInflater layoutInflater) {
        TextClock textClock = (TextClock) Preconditions.checkNotNull((TextClock) layoutInflater.inflate(C1893R.layout.dream_overlay_complication_clock_time, (ViewGroup) null, false), "R.layout.dream_overlay_complication_clock_time did not properly inflated");
        textClock.setFontVariationSettings("'wght' 200");
        return textClock;
    }

    @Provides
    @Named("clock_time_complication_layout_params")
    static ComplicationLayoutParams provideLayoutParams() {
        return new ComplicationLayoutParams(0, -2, 6, 1, 0);
    }
}
