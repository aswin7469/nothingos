package com.android.systemui.doze.dagger;

import com.android.systemui.doze.DozeMachine;
import dagger.BindsInstance;
import dagger.Subcomponent;

@DozeScope
@Subcomponent(modules = {DozeModule.class})
public interface DozeComponent {

    @Subcomponent.Factory
    public interface Builder {
        DozeComponent build(@BindsInstance DozeMachine.Service service);
    }

    @DozeScope
    DozeMachine getDozeMachine();
}
