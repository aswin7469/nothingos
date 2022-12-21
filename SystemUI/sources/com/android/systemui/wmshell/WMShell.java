package com.android.systemui.wmshell;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.p019wm.shell.nano.WmShellTraceProto;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.onehanded.OneHandedEventCallback;
import com.android.p019wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.protolog.ShellProtoLogImpl;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import java.p026io.OutputStream;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public final class WMShell extends CoreStartable implements CommandQueue.Callbacks, ProtoTraceable<SystemUiTraceProto> {
    private static final int INVALID_SYSUI_STATE_MASK = 8440396;
    private static final String TAG = "com.android.systemui.wmshell.WMShell";
    /* access modifiers changed from: private */
    public final CommandQueue mCommandQueue;
    private KeyguardStateController.Callback mCompatUIKeyguardCallback;
    private final Optional<CompatUI> mCompatUIOptional;
    private final ConfigurationController mConfigurationController;
    private final Optional<DragAndDrop> mDragAndDropOptional;
    private final Optional<HideDisplayCutout> mHideDisplayCutoutOptional;
    private boolean mIsSysUiStateValid;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
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
    private final Optional<SplitScreen> mSplitScreenOptional;
    /* access modifiers changed from: private */
    public final Executor mSysUiMainExecutor;
    /* access modifiers changed from: private */
    public final SysUiState mSysUiState;
    private final UserInfoController mUserInfoController;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private WakefulnessLifecycle.Observer mWakefulnessObserver;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public WMShell(Context context, Optional<Pip> optional, Optional<SplitScreen> optional2, Optional<OneHanded> optional3, Optional<HideDisplayCutout> optional4, Optional<ShellCommandHandler> optional5, Optional<CompatUI> optional6, Optional<DragAndDrop> optional7, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, UserInfoController userInfoController, @Main Executor executor) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mConfigurationController = configurationController;
        this.mKeyguardStateController = keyguardStateController;
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
        this.mCompatUIOptional = optional6;
        this.mDragAndDropOptional = optional7;
        this.mUserInfoController = userInfoController;
        this.mSysUiMainExecutor = executor;
    }

    public void start() {
        this.mProtoTracer.add(this);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mPipOptional.ifPresent(new WMShell$$ExternalSyntheticLambda3(this));
        this.mSplitScreenOptional.ifPresent(new WMShell$$ExternalSyntheticLambda4(this));
        this.mOneHandedOptional.ifPresent(new WMShell$$ExternalSyntheticLambda5(this));
        this.mHideDisplayCutoutOptional.ifPresent(new WMShell$$ExternalSyntheticLambda6(this));
        this.mCompatUIOptional.ifPresent(new WMShell$$ExternalSyntheticLambda7(this));
        this.mDragAndDropOptional.ifPresent(new WMShell$$ExternalSyntheticLambda8(this));
    }

    /* access modifiers changed from: package-private */
    public void initPip(final Pip pip) {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void showPictureInPictureMenu() {
                pip.showPictureInPictureMenu();
            }
        });
        C33252 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                pip.onKeyguardVisibilityChanged(z, WMShell.this.mKeyguardStateController.isAnimatingBetweenKeyguardAndSurfaceBehind());
            }

            public void onKeyguardDismissAnimationFinished() {
                pip.onKeyguardDismissAnimationFinished();
            }
        };
        this.mPipKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        this.mSysUiState.addCallback(new WMShell$$ExternalSyntheticLambda0(this, pip));
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                pip.onConfigurationChanged(configuration);
            }

            public void onDensityOrFontScaleChanged() {
                pip.onDensityOrFontScaleChanged();
            }

            public void onThemeChanged() {
                pip.onOverlayChanged();
            }
        });
        this.mUserInfoController.addCallback(new WMShell$$ExternalSyntheticLambda1(pip));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initPip$0$com-android-systemui-wmshell-WMShell  reason: not valid java name */
    public /* synthetic */ void m3326lambda$initPip$0$comandroidsystemuiwmshellWMShell(Pip pip, int i) {
        boolean z = (INVALID_SYSUI_STATE_MASK & i) == 0;
        this.mIsSysUiStateValid = z;
        pip.onSystemUiStateChanged(z, i);
    }

    /* access modifiers changed from: package-private */
    public void initSplitScreen(final SplitScreen splitScreen) {
        C33274 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                splitScreen.onKeyguardVisibilityChanged(z);
            }
        };
        this.mSplitScreenKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        this.mWakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                splitScreen.onFinishedWakingUp();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void initOneHanded(final OneHanded oneHanded) {
        oneHanded.registerTransitionCallback(new OneHandedTransitionCallback() {
            public void onStartTransition(boolean z) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda2(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStartTransition$0$com-android-systemui-wmshell-WMShell$6 */
            public /* synthetic */ void mo47563x3fd4fb56() {
                WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
            }

            public void onStartFinished(Rect rect) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStartFinished$1$com-android-systemui-wmshell-WMShell$6  reason: not valid java name */
            public /* synthetic */ void m3327lambda$onStartFinished$1$comandroidsystemuiwmshellWMShell$6() {
                WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
            }

            public void onStopFinished(Rect rect) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda1(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStopFinished$2$com-android-systemui-wmshell-WMShell$6  reason: not valid java name */
            public /* synthetic */ void m3328lambda$onStopFinished$2$comandroidsystemuiwmshellWMShell$6() {
                WMShell.this.mSysUiState.setFlag(65536, false).commitUpdate(0);
            }
        });
        oneHanded.registerEventCallback(new OneHandedEventCallback() {
            public void notifyExpandNotification() {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$7$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$notifyExpandNotification$0$com-android-systemui-wmshell-WMShell$7 */
            public /* synthetic */ void mo47568x2ae04327() {
                WMShell.this.mCommandQueue.handleSystemKey(281);
            }
        });
        C33318 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                oneHanded.onKeyguardVisibilityChanged(z);
                oneHanded.stopOneHanded();
            }

            public void onUserSwitchComplete(int i) {
                oneHanded.onUserSwitch(i);
            }
        };
        this.mOneHandedKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        C33329 r02 = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                oneHanded.setLockedDisabled(false, false);
            }

            public void onStartedGoingToSleep() {
                oneHanded.stopOneHanded();
                oneHanded.setLockedDisabled(true, false);
            }
        };
        this.mWakefulnessObserver = r02;
        this.mWakefulnessLifecycle.addObserver(r02);
        this.mScreenLifecycle.addObserver(new ScreenLifecycle.Observer() {
            public void onScreenTurningOff() {
                oneHanded.stopOneHanded(7);
            }
        });
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void onCameraLaunchGestureDetected(int i) {
                oneHanded.stopOneHanded();
            }

            public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
                if (i == 0 && (i2 & 2) != 0) {
                    oneHanded.stopOneHanded(3);
                }
            }
        });
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                oneHanded.onConfigChanged(configuration);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void initHideDisplayCutout(final HideDisplayCutout hideDisplayCutout) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                hideDisplayCutout.onConfigurationChanged(configuration);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void initCompatUi(final CompatUI compatUI) {
        C332314 r0 = new KeyguardStateController.Callback() {
            public void onKeyguardShowingChanged() {
                compatUI.onKeyguardShowingChanged(WMShell.this.mKeyguardStateController.isShowing());
            }
        };
        this.mCompatUIKeyguardCallback = r0;
        this.mKeyguardStateController.addCallback(r0);
    }

    /* access modifiers changed from: package-private */
    public void initDragAndDrop(final DragAndDrop dragAndDrop) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                dragAndDrop.onConfigChanged(configuration);
            }

            public void onThemeChanged() {
                dragAndDrop.onThemeChanged();
            }
        });
    }

    public void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.wmShell == null) {
            systemUiTraceProto.wmShell = new WmShellTraceProto();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        if ((!this.mShellCommandHandler.isPresent() || !this.mShellCommandHandler.get().handleCommand(strArr, printWriter)) && !handleLoggingCommand(strArr, printWriter)) {
            this.mShellCommandHandler.ifPresent(new WMShell$$ExternalSyntheticLambda2(printWriter));
        }
    }

    public void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        PrintWriter printWriter = new PrintWriter((OutputStream) new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor));
        handleLoggingCommand(strArr, printWriter);
        printWriter.flush();
        printWriter.close();
    }

    private boolean handleLoggingCommand(String[] strArr, PrintWriter printWriter) {
        ShellProtoLogImpl singleInstance = ShellProtoLogImpl.getSingleInstance();
        int i = 0;
        while (i < strArr.length) {
            String str = strArr[i];
            str.hashCode();
            if (str.equals("enable-text")) {
                String[] strArr2 = (String[]) Arrays.copyOfRange((T[]) strArr, i + 1, strArr.length);
                if (singleInstance.startTextLogging(strArr2, printWriter) == 0) {
                    printWriter.println("Starting logging on groups: " + Arrays.toString((Object[]) strArr2));
                }
                return true;
            } else if (!str.equals("disable-text")) {
                i++;
            } else {
                String[] strArr3 = (String[]) Arrays.copyOfRange((T[]) strArr, i + 1, strArr.length);
                if (singleInstance.stopTextLogging(strArr3, printWriter) == 0) {
                    printWriter.println("Stopping logging on groups: " + Arrays.toString((Object[]) strArr3));
                }
                return true;
            }
        }
        return false;
    }
}
