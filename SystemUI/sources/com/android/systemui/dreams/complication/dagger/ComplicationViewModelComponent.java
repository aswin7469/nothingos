package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent
public interface ComplicationViewModelComponent {

    @Subcomponent.Factory
    public interface Factory {
        ComplicationViewModelComponent create(@BindsInstance Complication complication, @BindsInstance ComplicationId complicationId);
    }

    ComplicationViewModelProvider getViewModelProvider();
}
