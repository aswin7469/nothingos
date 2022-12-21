package com.android.systemui.power.dagger;

import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.EnhancedEstimatesImpl;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.power.PowerUI;
import dagger.Binds;
import dagger.Module;

@Module
public interface PowerModule {
    @Binds
    EnhancedEstimates bindEnhancedEstimates(EnhancedEstimatesImpl enhancedEstimatesImpl);

    @Binds
    PowerUI.WarningsUI provideWarningsUi(PowerNotificationWarnings powerNotificationWarnings);
}
