package com.android.systemui.wmshell;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.SystemUI;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.android.systemui.wmshell.WMShell;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.nano.WmShellTraceProto;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedEventCallback;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final class WMShell extends SystemUI implements CommandQueue.Callbacks, ProtoTraceable<SystemUiTraceProto> {
    private final CommandQueue mCommandQueue;
    private final ConfigurationController mConfigurationController;
    private final Optional<HideDisplayCutout> mHideDisplayCutoutOptional;
    private boolean mIsSysUiStateValid;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final NavigationModeController mNavigationModeController;
    private KeyguardUpdateMonitorCallback mOneHandedKeyguardCallback;
    private final Optional<OneHanded> mOneHandedOptional;
    private KeyguardUpdateMonitorCallback mPipKeyguardCallback;
    private final Optional<Pip> mPipOptional;
    private final ProtoTracer mProtoTracer;
    private final ScreenLifecycle mScreenLifecycle;
    private final Optional<ShellCommandHandler> mShellCommandHandler;
    private KeyguardUpdateMonitorCallback mSplitScreenKeyguardCallback;
    private final Optional<LegacySplitScreen> mSplitScreenOptional;
    private final Executor mSysUiMainExecutor;
    private final SysUiState mSysUiState;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private WakefulnessLifecycle.Observer mWakefulnessObserver;

    public WMShell(Context context, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<OneHanded> optional3, Optional<HideDisplayCutout> optional4, Optional<ShellCommandHandler> optional5, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, Executor executor) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mConfigurationController = configurationController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mNavigationModeController = navigationModeController;
        this.mScreenLifecycle = screenLifecycle;
        this.mSysUiState = sysUiState;
        this.mPipOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mOneHandedOptional = optional3;
        this.mHideDisplayCutoutOptional = optional4;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mProtoTracer = protoTracer;
        this.mShellCommandHandler = optional5;
        this.mSysUiMainExecutor = executor;
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mProtoTracer.add(this);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mPipOptional.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WMShell.this.initPip((Pip) obj);
            }
        });
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WMShell.this.initSplitScreen((LegacySplitScreen) obj);
            }
        });
        this.mOneHandedOptional.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WMShell.this.initOneHanded((OneHanded) obj);
            }
        });
        this.mHideDisplayCutoutOptional.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WMShell.this.initHideDisplayCutout((HideDisplayCutout) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void initPip(final Pip pip) {
        this.mCommandQueue.addCallback(new CommandQueue.Callbacks() { // from class: com.android.systemui.wmshell.WMShell.1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void showPictureInPictureMenu() {
                pip.showPictureInPictureMenu();
            }
        });
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.wmshell.WMShell.2
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onKeyguardVisibilityChanged(boolean z) {
                if (z) {
                    pip.hidePipMenu(null, null);
                }
            }
        };
        this.mPipKeyguardCallback = keyguardUpdateMonitorCallback;
        this.mKeyguardUpdateMonitor.registerCallback(keyguardUpdateMonitorCallback);
        this.mSysUiState.addCallback(new SysUiState.SysUiStateCallback() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda0
            @Override // com.android.systemui.model.SysUiState.SysUiStateCallback
            public final void onSystemUiStateChanged(int i) {
                WMShell.this.lambda$initPip$0(pip, i);
            }
        });
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.WMShell.3
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                pip.onConfigurationChanged(configuration);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                pip.onDensityOrFontScaleChanged();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                pip.onOverlayChanged();
            }
        });
        ((UserInfoController) Dependency.get(UserInfoController.class)).addCallback(new UserInfoController.OnUserInfoChangedListener() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda1
            @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
            public final void onUserInfoChanged(String str, Drawable drawable, String str2) {
                Pip.this.registerSessionListenerForCurrentUser();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initPip$0(Pip pip, int i) {
        boolean z = (51788 & i) == 0;
        this.mIsSysUiStateValid = z;
        pip.onSystemUiStateChanged(z, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void initSplitScreen(final LegacySplitScreen legacySplitScreen) {
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.wmshell.WMShell.4
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onKeyguardVisibilityChanged(boolean z) {
                legacySplitScreen.onKeyguardVisibilityChanged(z);
            }
        };
        this.mSplitScreenKeyguardCallback = keyguardUpdateMonitorCallback;
        this.mKeyguardUpdateMonitor.registerCallback(keyguardUpdateMonitorCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.wmshell.WMShell$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OneHandedTransitionCallback {
        AnonymousClass5() {
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public void onStartTransition(boolean z) {
            WMShell.this.mSysUiMainExecutor.execute(new Runnable() { // from class: com.android.systemui.wmshell.WMShell$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    WMShell.AnonymousClass5.this.lambda$onStartTransition$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartTransition$0() {
            WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public void onStartFinished(Rect rect) {
            WMShell.this.mSysUiMainExecutor.execute(new Runnable() { // from class: com.android.systemui.wmshell.WMShell$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    WMShell.AnonymousClass5.this.lambda$onStartFinished$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartFinished$1() {
            WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public void onStopFinished(Rect rect) {
            WMShell.this.mSysUiMainExecutor.execute(new Runnable() { // from class: com.android.systemui.wmshell.WMShell$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WMShell.AnonymousClass5.this.lambda$onStopFinished$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStopFinished$2() {
            WMShell.this.mSysUiState.setFlag(65536, false).commitUpdate(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void initOneHanded(final OneHanded oneHanded) {
        oneHanded.registerTransitionCallback(new AnonymousClass5());
        oneHanded.registerEventCallback(new AnonymousClass6());
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.wmshell.WMShell.7
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onKeyguardVisibilityChanged(boolean z) {
                oneHanded.onKeyguardVisibilityChanged(z);
                oneHanded.stopOneHanded();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onUserSwitchComplete(int i) {
                oneHanded.onUserSwitch(i);
            }
        };
        this.mOneHandedKeyguardCallback = keyguardUpdateMonitorCallback;
        this.mKeyguardUpdateMonitor.registerCallback(keyguardUpdateMonitorCallback);
        WakefulnessLifecycle.Observer observer = new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.wmshell.WMShell.8
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                oneHanded.setLockedDisabled(false, false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                oneHanded.stopOneHanded();
                oneHanded.setLockedDisabled(true, false);
            }
        };
        this.mWakefulnessObserver = observer;
        this.mWakefulnessLifecycle.addObserver(observer);
        this.mScreenLifecycle.addObserver(new ScreenLifecycle.Observer() { // from class: com.android.systemui.wmshell.WMShell.9
            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurningOff() {
                oneHanded.stopOneHanded(7);
            }
        });
        this.mCommandQueue.addCallback(new CommandQueue.Callbacks() { // from class: com.android.systemui.wmshell.WMShell.10
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void onCameraLaunchGestureDetected(int i) {
                oneHanded.stopOneHanded();
            }

            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
                if (i != 0 || (i2 & 2) == 0) {
                    return;
                }
                oneHanded.stopOneHanded(3);
            }
        });
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.WMShell.11
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                oneHanded.onConfigChanged(configuration);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.wmshell.WMShell$6  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OneHandedEventCallback {
        AnonymousClass6() {
        }

        @Override // com.android.wm.shell.onehanded.OneHandedEventCallback
        public void notifyExpandNotification() {
            WMShell.this.mSysUiMainExecutor.execute(new Runnable() { // from class: com.android.systemui.wmshell.WMShell$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WMShell.AnonymousClass6.this.lambda$notifyExpandNotification$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyExpandNotification$0() {
            WMShell.this.mCommandQueue.handleSystemKey(281);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void initHideDisplayCutout(final HideDisplayCutout hideDisplayCutout) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.WMShell.12
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                hideDisplayCutout.onConfigurationChanged(configuration);
            }
        });
    }

    @Override // com.android.systemui.shared.tracing.ProtoTraceable
    public void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.wmShell == null) {
            systemUiTraceProto.wmShell = new WmShellTraceProto();
        }
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] strArr) {
        if ((!this.mShellCommandHandler.isPresent() || !this.mShellCommandHandler.get().handleCommand(strArr, printWriter)) && !handleLoggingCommand(strArr, printWriter)) {
            this.mShellCommandHandler.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ShellCommandHandler) obj).dump(printWriter);
                }
            });
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        PrintWriter printWriter = new PrintWriter(new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor));
        handleLoggingCommand(strArr, printWriter);
        printWriter.flush();
        printWriter.close();
    }

    private boolean handleLoggingCommand(String[] strArr, PrintWriter printWriter) {
        String[] strArr2;
        String[] strArr3;
        ShellProtoLogImpl singleInstance = ShellProtoLogImpl.getSingleInstance();
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            str.hashCode();
            if (str.equals("enable-text")) {
                if (singleInstance.startTextLogging((String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length), printWriter) == 0) {
                    printWriter.println("Starting logging on groups: " + Arrays.toString(strArr2));
                }
                return true;
            } else if (str.equals("disable-text")) {
                if (singleInstance.stopTextLogging((String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length), printWriter) == 0) {
                    printWriter.println("Stopping logging on groups: " + Arrays.toString(strArr3));
                }
                return true;
            }
        }
        return false;
    }
}
