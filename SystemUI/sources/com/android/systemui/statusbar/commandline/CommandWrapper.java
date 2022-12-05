package com.android.systemui.statusbar.commandline;

import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandWrapper {
    @NotNull
    private final Function0<Command> commandFactory;
    @NotNull
    private final Executor executor;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommandWrapper)) {
            return false;
        }
        CommandWrapper commandWrapper = (CommandWrapper) obj;
        return Intrinsics.areEqual(this.commandFactory, commandWrapper.commandFactory) && Intrinsics.areEqual(this.executor, commandWrapper.executor);
    }

    public int hashCode() {
        return (this.commandFactory.hashCode() * 31) + this.executor.hashCode();
    }

    @NotNull
    public String toString() {
        return "CommandWrapper(commandFactory=" + this.commandFactory + ", executor=" + this.executor + ')';
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CommandWrapper(@NotNull Function0<? extends Command> commandFactory, @NotNull Executor executor) {
        Intrinsics.checkNotNullParameter(commandFactory, "commandFactory");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.commandFactory = commandFactory;
        this.executor = executor;
    }

    @NotNull
    public final Function0<Command> getCommandFactory() {
        return this.commandFactory;
    }

    @NotNull
    public final Executor getExecutor() {
        return this.executor;
    }
}
