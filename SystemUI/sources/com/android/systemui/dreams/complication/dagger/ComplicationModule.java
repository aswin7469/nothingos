package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import dagger.Module;
import dagger.Provides;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Named;
import javax.inject.Scope;

@Module(includes = {ComplicationHostViewModule.class}, subcomponents = {ComplicationViewModelComponent.class})
public interface ComplicationModule {
    public static final String SCOPED_COMPLICATIONS_MODEL = "scoped_complications_model";

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ComplicationScope {
    }

    static /* synthetic */ ViewModel lambda$providesComplicationCollectionViewModel$0(ComplicationCollectionViewModel complicationCollectionViewModel) {
        return complicationCollectionViewModel;
    }

    @Provides
    static Complication.VisibilityController providesVisibilityController(ComplicationLayoutEngine complicationLayoutEngine) {
        return complicationLayoutEngine;
    }

    @Provides
    @Named("scoped_complications_model")
    static ComplicationCollectionViewModel providesComplicationCollectionViewModel(ViewModelStore viewModelStore, ComplicationCollectionViewModel complicationCollectionViewModel) {
        return (ComplicationCollectionViewModel) new ViewModelProvider(viewModelStore, (ViewModelProvider.Factory) new DaggerViewModelProviderFactory(new ComplicationModule$$ExternalSyntheticLambda0(complicationCollectionViewModel))).get(ComplicationCollectionViewModel.class);
    }
}
