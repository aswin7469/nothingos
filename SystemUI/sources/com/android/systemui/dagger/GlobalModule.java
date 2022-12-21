package com.android.systemui.dagger;

import android.content.Context;
import android.util.DisplayMetrics;
import com.android.systemui.dagger.qualifiers.Application;
import com.android.systemui.plugins.PluginsModule;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule;
import dagger.Module;
import dagger.Provides;

@Module(includes = {AndroidInternalsModule.class, FrameworkServicesModule.class, GlobalConcurrencyModule.class, UnfoldTransitionModule.class, PluginsModule.class})
public class GlobalModule {
    @Application
    @Provides
    public Context provideApplicationContext(Context context) {
        return context.getApplicationContext();
    }

    @Provides
    public DisplayMetrics provideDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
