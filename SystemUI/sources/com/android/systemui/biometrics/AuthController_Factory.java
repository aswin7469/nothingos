package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.UserManager;
import android.view.WindowManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AuthController_Factory implements Factory<AuthController> {
    private final Provider<ActivityTaskManager> activityTaskManagerProvider;
    private final Provider<DelayableExecutor> bgExecutorProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DisplayManager> displayManagerProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<FaceManager> faceManagerProvider;
    private final Provider<FingerprintManager> fingerprintManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<SidefpsController> sidefpsControllerFactoryProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UdfpsController> udfpsControllerFactoryProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public AuthController_Factory(Provider<Context> provider, Provider<Execution> provider2, Provider<CommandQueue> provider3, Provider<ActivityTaskManager> provider4, Provider<WindowManager> provider5, Provider<FingerprintManager> provider6, Provider<FaceManager> provider7, Provider<UdfpsController> provider8, Provider<SidefpsController> provider9, Provider<DisplayManager> provider10, Provider<WakefulnessLifecycle> provider11, Provider<UserManager> provider12, Provider<LockPatternUtils> provider13, Provider<StatusBarStateController> provider14, Provider<Handler> provider15, Provider<DelayableExecutor> provider16) {
        this.contextProvider = provider;
        this.executionProvider = provider2;
        this.commandQueueProvider = provider3;
        this.activityTaskManagerProvider = provider4;
        this.windowManagerProvider = provider5;
        this.fingerprintManagerProvider = provider6;
        this.faceManagerProvider = provider7;
        this.udfpsControllerFactoryProvider = provider8;
        this.sidefpsControllerFactoryProvider = provider9;
        this.displayManagerProvider = provider10;
        this.wakefulnessLifecycleProvider = provider11;
        this.userManagerProvider = provider12;
        this.lockPatternUtilsProvider = provider13;
        this.statusBarStateControllerProvider = provider14;
        this.handlerProvider = provider15;
        this.bgExecutorProvider = provider16;
    }

    public AuthController get() {
        return newInstance(this.contextProvider.get(), this.executionProvider.get(), this.commandQueueProvider.get(), this.activityTaskManagerProvider.get(), this.windowManagerProvider.get(), this.fingerprintManagerProvider.get(), this.faceManagerProvider.get(), this.udfpsControllerFactoryProvider, this.sidefpsControllerFactoryProvider, this.displayManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.userManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.statusBarStateControllerProvider.get(), this.handlerProvider.get(), this.bgExecutorProvider.get());
    }

    public static AuthController_Factory create(Provider<Context> provider, Provider<Execution> provider2, Provider<CommandQueue> provider3, Provider<ActivityTaskManager> provider4, Provider<WindowManager> provider5, Provider<FingerprintManager> provider6, Provider<FaceManager> provider7, Provider<UdfpsController> provider8, Provider<SidefpsController> provider9, Provider<DisplayManager> provider10, Provider<WakefulnessLifecycle> provider11, Provider<UserManager> provider12, Provider<LockPatternUtils> provider13, Provider<StatusBarStateController> provider14, Provider<Handler> provider15, Provider<DelayableExecutor> provider16) {
        return new AuthController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static AuthController newInstance(Context context, Execution execution, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils, StatusBarStateController statusBarStateController, Handler handler, DelayableExecutor delayableExecutor) {
        return new AuthController(context, execution, commandQueue, activityTaskManager, windowManager, fingerprintManager, faceManager, provider, provider2, displayManager, wakefulnessLifecycle, userManager, lockPatternUtils, statusBarStateController, handler, delayableExecutor);
    }
}
