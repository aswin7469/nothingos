package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.Bundle;
import com.android.systemui.dagger.qualifiers.DisplayId;
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Subcomponent(modules = {NavigationBarModule.class})
@NavigationBarScope
public interface NavigationBarComponent {

    @Subcomponent.Factory
    public interface Factory {
        NavigationBarComponent create(@DisplayId @BindsInstance Context context, @BindsInstance Bundle bundle);
    }

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NavigationBarScope {
    }

    NavigationBar getNavigationBar();
}
