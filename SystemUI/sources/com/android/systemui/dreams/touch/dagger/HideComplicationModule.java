package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class HideComplicationModule {
    @IntoSet
    @Provides
    public static DreamTouchHandler providesHideComplicationTouchHandler(HideComplicationTouchHandler hideComplicationTouchHandler) {
        return hideComplicationTouchHandler;
    }
}
