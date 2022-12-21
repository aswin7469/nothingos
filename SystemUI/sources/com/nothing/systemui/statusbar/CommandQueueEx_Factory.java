package com.nothing.systemui.statusbar;

import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CommandQueueEx_Factory implements Factory<CommandQueueEx> {
    private final Provider<CommandQueue> commandQueueProvider;

    public CommandQueueEx_Factory(Provider<CommandQueue> provider) {
        this.commandQueueProvider = provider;
    }

    public CommandQueueEx get() {
        return newInstance(this.commandQueueProvider.get());
    }

    public static CommandQueueEx_Factory create(Provider<CommandQueue> provider) {
        return new CommandQueueEx_Factory(provider);
    }

    public static CommandQueueEx newInstance(CommandQueue commandQueue) {
        return new CommandQueueEx(commandQueue);
    }
}
