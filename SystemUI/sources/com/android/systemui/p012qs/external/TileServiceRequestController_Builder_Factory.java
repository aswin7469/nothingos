package com.android.systemui.p012qs.external;

import com.android.systemui.p012qs.external.TileServiceRequestController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController_Builder_Factory */
public final class TileServiceRequestController_Builder_Factory implements Factory<TileServiceRequestController.Builder> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<CommandRegistry> commandRegistryProvider;

    public TileServiceRequestController_Builder_Factory(Provider<CommandQueue> provider, Provider<CommandRegistry> provider2) {
        this.commandQueueProvider = provider;
        this.commandRegistryProvider = provider2;
    }

    public TileServiceRequestController.Builder get() {
        return newInstance(this.commandQueueProvider.get(), this.commandRegistryProvider.get());
    }

    public static TileServiceRequestController_Builder_Factory create(Provider<CommandQueue> provider, Provider<CommandRegistry> provider2) {
        return new TileServiceRequestController_Builder_Factory(provider, provider2);
    }

    public static TileServiceRequestController.Builder newInstance(CommandQueue commandQueue, CommandRegistry commandRegistry) {
        return new TileServiceRequestController.Builder(commandQueue, commandRegistry);
    }
}
