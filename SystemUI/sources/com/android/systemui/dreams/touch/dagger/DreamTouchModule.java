package com.android.systemui.dreams.touch.dagger;

import dagger.Module;

@Module(includes = {BouncerSwipeModule.class, HideComplicationModule.class}, subcomponents = {InputSessionComponent.class})
public interface DreamTouchModule {
    public static final String INPUT_SESSION_NAME = "INPUT_SESSION_NAME";
    public static final String PILFER_ON_GESTURE_CONSUME = "PILFER_ON_GESTURE_CONSUME";
}
