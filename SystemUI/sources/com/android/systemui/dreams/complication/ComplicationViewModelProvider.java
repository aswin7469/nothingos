package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;
import javax.inject.Inject;

public class ComplicationViewModelProvider extends ViewModelProvider {
    static /* synthetic */ ViewModel lambda$new$0(ComplicationViewModel complicationViewModel) {
        return complicationViewModel;
    }

    @Inject
    public ComplicationViewModelProvider(ViewModelStore viewModelStore, ComplicationViewModel complicationViewModel) {
        super(viewModelStore, (ViewModelProvider.Factory) new DaggerViewModelProviderFactory(new ComplicationViewModelProvider$$ExternalSyntheticLambda0(complicationViewModel)));
    }
}
