package com.android.systemui.util.condition.dagger;

import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.condition.Monitor;
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.util.Set;

@Subcomponent
public interface MonitorComponent {

    @Subcomponent.Factory
    public interface Factory {
        MonitorComponent create(@BindsInstance Set<Condition> set, @BindsInstance Set<Monitor.Callback> set2);
    }

    Monitor getMonitor();
}
