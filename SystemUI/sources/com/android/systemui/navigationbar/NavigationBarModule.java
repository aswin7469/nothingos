package com.android.systemui.navigationbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import dagger.Module;
import dagger.Provides;

@Module
public interface NavigationBarModule {
    @DisplayId
    @NavigationBarComponent.NavigationBarScope
    @Provides
    static LayoutInflater provideLayoutInflater(@DisplayId Context context) {
        return LayoutInflater.from(context);
    }

    @NavigationBarComponent.NavigationBarScope
    @Provides
    static NavigationBarFrame provideNavigationBarFrame(@DisplayId LayoutInflater layoutInflater) {
        return (NavigationBarFrame) layoutInflater.inflate(C1894R.layout.navigation_bar_window, (ViewGroup) null);
    }

    @NavigationBarComponent.NavigationBarScope
    @Provides
    static NavigationBarView provideNavigationBarview(@DisplayId LayoutInflater layoutInflater, NavigationBarFrame navigationBarFrame) {
        return (NavigationBarView) layoutInflater.inflate(C1894R.layout.navigation_bar, navigationBarFrame).findViewById(C1894R.C1898id.navigation_bar_view);
    }

    @NavigationBarComponent.NavigationBarScope
    @Provides
    static EdgeBackGestureHandler provideEdgeBackGestureHandler(EdgeBackGestureHandler.Factory factory, @DisplayId Context context) {
        return factory.create(context);
    }

    @DisplayId
    @NavigationBarComponent.NavigationBarScope
    @Provides
    static WindowManager provideWindowManager(@DisplayId Context context) {
        return (WindowManager) context.getSystemService(WindowManager.class);
    }
}
