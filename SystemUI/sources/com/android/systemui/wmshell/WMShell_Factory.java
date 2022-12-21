package com.android.systemui.wmshell;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tracing.ProtoTracer;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class WMShell_Factory implements Factory<WMShell> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Optional<DragAndDrop>> dragAndDropOptionalProvider;
    private final Provider<Optional<HideDisplayCutout>> hideDisplayCutoutOptionalProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<Optional<OneHanded>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<ProtoTracer> protoTracerProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<Optional<ShellCommandHandler>> shellCommandHandlerProvider;
    private final Provider<Optional<CompatUI>> sizeCompatUIOptionalProvider;
    private final Provider<Optional<SplitScreen>> splitScreenOptionalProvider;
    private final Provider<Executor> sysUiMainExecutorProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public WMShell_Factory(Provider<Context> provider, Provider<Optional<Pip>> provider2, Provider<Optional<SplitScreen>> provider3, Provider<Optional<OneHanded>> provider4, Provider<Optional<HideDisplayCutout>> provider5, Provider<Optional<ShellCommandHandler>> provider6, Provider<Optional<CompatUI>> provider7, Provider<Optional<DragAndDrop>> provider8, Provider<CommandQueue> provider9, Provider<ConfigurationController> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<NavigationModeController> provider13, Provider<ScreenLifecycle> provider14, Provider<SysUiState> provider15, Provider<ProtoTracer> provider16, Provider<WakefulnessLifecycle> provider17, Provider<UserInfoController> provider18, Provider<Executor> provider19) {
        this.contextProvider = provider;
        this.pipOptionalProvider = provider2;
        this.splitScreenOptionalProvider = provider3;
        this.oneHandedOptionalProvider = provider4;
        this.hideDisplayCutoutOptionalProvider = provider5;
        this.shellCommandHandlerProvider = provider6;
        this.sizeCompatUIOptionalProvider = provider7;
        this.dragAndDropOptionalProvider = provider8;
        this.commandQueueProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.keyguardStateControllerProvider = provider11;
        this.keyguardUpdateMonitorProvider = provider12;
        this.navigationModeControllerProvider = provider13;
        this.screenLifecycleProvider = provider14;
        this.sysUiStateProvider = provider15;
        this.protoTracerProvider = provider16;
        this.wakefulnessLifecycleProvider = provider17;
        this.userInfoControllerProvider = provider18;
        this.sysUiMainExecutorProvider = provider19;
    }

    public WMShell get() {
        return newInstance(this.contextProvider.get(), this.pipOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.hideDisplayCutoutOptionalProvider.get(), this.shellCommandHandlerProvider.get(), this.sizeCompatUIOptionalProvider.get(), this.dragAndDropOptionalProvider.get(), this.commandQueueProvider.get(), this.configurationControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.navigationModeControllerProvider.get(), this.screenLifecycleProvider.get(), this.sysUiStateProvider.get(), this.protoTracerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.userInfoControllerProvider.get(), this.sysUiMainExecutorProvider.get());
    }

    public static WMShell_Factory create(Provider<Context> provider, Provider<Optional<Pip>> provider2, Provider<Optional<SplitScreen>> provider3, Provider<Optional<OneHanded>> provider4, Provider<Optional<HideDisplayCutout>> provider5, Provider<Optional<ShellCommandHandler>> provider6, Provider<Optional<CompatUI>> provider7, Provider<Optional<DragAndDrop>> provider8, Provider<CommandQueue> provider9, Provider<ConfigurationController> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<NavigationModeController> provider13, Provider<ScreenLifecycle> provider14, Provider<SysUiState> provider15, Provider<ProtoTracer> provider16, Provider<WakefulnessLifecycle> provider17, Provider<UserInfoController> provider18, Provider<Executor> provider19) {
        return new WMShell_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static WMShell newInstance(Context context, Optional<Pip> optional, Optional<SplitScreen> optional2, Optional<OneHanded> optional3, Optional<HideDisplayCutout> optional4, Optional<ShellCommandHandler> optional5, Optional<CompatUI> optional6, Optional<DragAndDrop> optional7, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, UserInfoController userInfoController, Executor executor) {
        return new WMShell(context, optional, optional2, optional3, optional4, optional5, optional6, optional7, commandQueue, configurationController, keyguardStateController, keyguardUpdateMonitor, navigationModeController, screenLifecycle, sysUiState, protoTracer, wakefulnessLifecycle, userInfoController, executor);
    }
}
