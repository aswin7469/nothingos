package com.android.systemui.dreams.dagger;

import android.content.Context;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.dreams.complication.dagger.RegisteredComplicationsModule;
import dagger.Module;
import dagger.Provides;

@Module(includes = {RegisteredComplicationsModule.class}, subcomponents = {DreamOverlayComponent.class})
public interface DreamModule {
    @Provides
    static DreamBackend providesDreamBackend(Context context) {
        return DreamBackend.getInstance(context);
    }
}
