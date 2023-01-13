package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.util.Preconditions;
import com.android.systemui.C1894R;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public interface DreamClockDateComplicationModule {
    public static final String DREAM_CLOCK_DATE_COMPLICATION_LAYOUT_PARAMS = "clock_date_complication_layout_params";
    public static final String DREAM_CLOCK_DATE_COMPLICATION_VIEW = "clock_date_complication_view";
    public static final int INSERT_ORDER_WEIGHT = 2;

    @Provides
    @Named("clock_date_complication_view")
    static View provideComplicationView(LayoutInflater layoutInflater) {
        return (View) Preconditions.checkNotNull(layoutInflater.inflate(C1894R.layout.dream_overlay_complication_clock_date, (ViewGroup) null, false), "R.layout.dream_overlay_complication_clock_date did not properly inflated");
    }

    @Provides
    @Named("clock_date_complication_layout_params")
    static ComplicationLayoutParams provideLayoutParams() {
        return new ComplicationLayoutParams(0, -2, 6, 8, 2);
    }
}
