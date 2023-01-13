package com.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.assist.p009ui.DefaultUiController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.assist.AssistManagerEx;
import dagger.Lazy;
import javax.inject.Inject;

@SysUISingleton
public class AssistManager {
    protected static final String ACTION_KEY = "action";
    protected static final String CONSTRAINED_KEY = "should_constrain";
    public static final int DISMISS_REASON_BACK = 3;
    public static final int DISMISS_REASON_INVOCATION_CANCELLED = 1;
    public static final int DISMISS_REASON_TAP = 2;
    public static final int DISMISS_REASON_TIMEOUT = 4;
    private static final String INVOCATION_PHONE_STATE_KEY = "invocation_phone_state";
    private static final String INVOCATION_TIME_MS_KEY = "invocation_time_ms";
    public static final int INVOCATION_TYPE_GESTURE = 1;
    public static final int INVOCATION_TYPE_HOME_BUTTON_LONG_PRESS = 5;
    public static final String INVOCATION_TYPE_KEY = "invocation_type";
    public static final int INVOCATION_TYPE_OTHER = 2;
    public static final int INVOCATION_TYPE_POWER_BUTTON_LONG_PRESS = 6;
    public static final int INVOCATION_TYPE_QUICK_SEARCH_BAR = 4;
    public static final int INVOCATION_TYPE_UNKNOWN = 0;
    public static final int INVOCATION_TYPE_VOICE = 3;
    protected static final String SET_ASSIST_GESTURE_CONSTRAINED_ACTION = "set_assist_gesture_constrained";
    private static final String TAG = "AssistManager";
    private static final long TIMEOUT_ACTIVITY = 1000;
    private static final long TIMEOUT_SERVICE = 2500;
    private static final boolean VERBOSE = false;
    private final AssistDisclosure mAssistDisclosure;
    protected final AssistLogger mAssistLogger;
    protected final AssistUtils mAssistUtils;
    private final CommandQueue mCommandQueue;
    protected final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final PhoneStateMonitor mPhoneStateMonitor;
    protected final Lazy<SysUiState> mSysUiState;
    private final UiController mUiController;

    public interface UiController {
        void hide();

        void onGestureCompletion(float f);

        void onInvocationProgress(int i, float f);
    }

    /* access modifiers changed from: protected */
    public final int toLoggingSubType(int i, int i2) {
        return (i << 1) | 0 | (i2 << 4);
    }

    @Inject
    public AssistManager(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, CommandQueue commandQueue, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, Lazy<SysUiState> lazy, DefaultUiController defaultUiController, AssistLogger assistLogger, @Main Handler handler) {
        this.mContext = context;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mAssistUtils = assistUtils;
        this.mAssistDisclosure = new AssistDisclosure(context, handler);
        this.mPhoneStateMonitor = phoneStateMonitor;
        this.mAssistLogger = assistLogger;
        registerVoiceInteractionSessionListener();
        this.mUiController = defaultUiController;
        this.mSysUiState = lazy;
        overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) new OverviewProxyService.OverviewProxyListener() {
            public void onAssistantProgress(float f) {
                AssistManager.this.onInvocationProgress(1, f);
            }

            public void onAssistantGestureCompletion(float f) {
                AssistManager.this.onGestureCompletion(f);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void registerVoiceInteractionSessionListener() {
        this.mAssistUtils.registerVoiceInteractionSessionListener(new IVoiceInteractionSessionListener.Stub() {
            public void onVoiceSessionShown() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_UPDATE);
            }

            public void onVoiceSessionHidden() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
            }

            public void onVoiceSessionWindowVisibilityChanged(boolean z) throws RemoteException {
                ((AssistManagerEx) NTDependencyEx.get(AssistManagerEx.class)).onVoiceSessionWindowVisibilityChanged(z);
            }

            public void onSetUiHints(Bundle bundle) {
                if (AssistManager.SET_ASSIST_GESTURE_CONSTRAINED_ACTION.equals(bundle.getString(AssistManager.ACTION_KEY))) {
                    AssistManager.this.mSysUiState.get().setFlag(8192, bundle.getBoolean(AssistManager.CONSTRAINED_KEY, false)).commitUpdate(0);
                }
            }
        });
    }

    public void startAssist(Bundle bundle) {
        ComponentName assistInfo = getAssistInfo();
        if (assistInfo != null) {
            boolean equals = assistInfo.equals(getVoiceInteractorComponentName());
            if (bundle == null) {
                bundle = new Bundle();
            }
            int i = bundle.getInt(INVOCATION_TYPE_KEY, 0);
            int phoneState = this.mPhoneStateMonitor.getPhoneState();
            bundle.putInt(INVOCATION_PHONE_STATE_KEY, phoneState);
            bundle.putLong(INVOCATION_TIME_MS_KEY, SystemClock.elapsedRealtime());
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, true, assistInfo, Integer.valueOf(phoneState));
            logStartAssistLegacy(i, phoneState);
            startAssistInternal(bundle, assistInfo, equals);
        }
    }

    public void onInvocationProgress(int i, float f) {
        this.mUiController.onInvocationProgress(i, f);
    }

    public void onGestureCompletion(float f) {
        this.mUiController.onGestureCompletion(f);
    }

    public void hideAssist() {
        this.mAssistUtils.hideCurrentSession();
    }

    private void startAssistInternal(Bundle bundle, ComponentName componentName, boolean z) {
        if (z) {
            startVoiceInteractor(bundle);
        } else {
            startAssistActivity(bundle, componentName);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        r0 = r0.getAssistIntent(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startAssistActivity(android.os.Bundle r6, android.content.ComponentName r7) {
        /*
            r5 = this;
            com.android.systemui.statusbar.policy.DeviceProvisionedController r0 = r5.mDeviceProvisionedController
            boolean r0 = r0.isDeviceProvisioned()
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            com.android.systemui.statusbar.CommandQueue r0 = r5.mCommandQueue
            r1 = 3
            r2 = 0
            r0.animateCollapsePanels(r1, r2)
            android.content.Context r0 = r5.mContext
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r1 = "assist_structure_enabled"
            r3 = -2
            r4 = 1
            int r0 = android.provider.Settings.Secure.getIntForUser(r0, r1, r4, r3)
            if (r0 == 0) goto L_0x0021
            r2 = r4
        L_0x0021:
            android.content.Context r0 = r5.mContext
            java.lang.String r1 = "search"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.app.SearchManager r0 = (android.app.SearchManager) r0
            if (r0 != 0) goto L_0x002f
            return
        L_0x002f:
            android.content.Intent r0 = r0.getAssistIntent(r2)
            if (r0 != 0) goto L_0x0036
            return
        L_0x0036:
            r0.setComponent(r7)
            r0.putExtras(r6)
            if (r2 == 0) goto L_0x0049
            android.content.Context r6 = r5.mContext
            boolean r6 = com.android.internal.app.AssistUtils.isDisclosureEnabled(r6)
            if (r6 == 0) goto L_0x0049
            r5.showDisclosure()
        L_0x0049:
            android.content.Context r6 = r5.mContext     // Catch:{ ActivityNotFoundException -> 0x0063 }
            r7 = 2130772457(0x7f0101e9, float:1.7148033E38)
            r1 = 2130772458(0x7f0101ea, float:1.7148035E38)
            android.app.ActivityOptions r6 = android.app.ActivityOptions.makeCustomAnimation(r6, r7, r1)     // Catch:{ ActivityNotFoundException -> 0x0063 }
            r7 = 268435456(0x10000000, float:2.5243549E-29)
            r0.addFlags(r7)     // Catch:{ ActivityNotFoundException -> 0x0063 }
            com.android.systemui.assist.AssistManager$3 r7 = new com.android.systemui.assist.AssistManager$3     // Catch:{ ActivityNotFoundException -> 0x0063 }
            r7.<init>(r0, r6)     // Catch:{ ActivityNotFoundException -> 0x0063 }
            android.os.AsyncTask.execute(r7)     // Catch:{ ActivityNotFoundException -> 0x0063 }
            goto L_0x007b
        L_0x0063:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "Activity not found for "
            r5.<init>((java.lang.String) r6)
            java.lang.String r6 = r0.getAction()
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            java.lang.String r5 = r5.toString()
            java.lang.String r6 = "AssistManager"
            android.util.Log.w(r6, r5)
        L_0x007b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.assist.AssistManager.startAssistActivity(android.os.Bundle, android.content.ComponentName):void");
    }

    private void startVoiceInteractor(Bundle bundle) {
        this.mAssistUtils.showSessionForActiveService(bundle, 4, (IVoiceInteractionSessionShowCallback) null, (IBinder) null);
    }

    public void launchVoiceAssistFromKeyguard() {
        this.mAssistUtils.launchVoiceAssistFromKeyguard();
    }

    public boolean canVoiceAssistBeLaunchedFromKeyguard() {
        return ((Boolean) DejankUtils.whitelistIpcs(new AssistManager$$ExternalSyntheticLambda0(this))).booleanValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$canVoiceAssistBeLaunchedFromKeyguard$0$com-android-systemui-assist-AssistManager */
    public /* synthetic */ Boolean mo30368xbc9ad335() {
        return Boolean.valueOf(this.mAssistUtils.activeServiceSupportsLaunchFromKeyguard());
    }

    public ComponentName getVoiceInteractorComponentName() {
        return this.mAssistUtils.getActiveServiceComponentName();
    }

    private boolean isVoiceSessionRunning() {
        return this.mAssistUtils.isSessionRunning();
    }

    public ComponentName getAssistInfoForUser(int i) {
        return this.mAssistUtils.getAssistComponentForUser(i);
    }

    private ComponentName getAssistInfo() {
        return getAssistInfoForUser(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showDisclosure() {
        this.mAssistDisclosure.postShow();
    }

    public void onLockscreenShown() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                AssistManager.this.mAssistUtils.onLockscreenShown();
            }
        });
    }

    public int toLoggingSubType(int i) {
        return toLoggingSubType(i, this.mPhoneStateMonitor.getPhoneState());
    }

    /* access modifiers changed from: protected */
    public void logStartAssistLegacy(int i, int i2) {
        MetricsLogger.action(new LogMaker(1716).setType(1).setSubtype(toLoggingSubType(i, i2)));
    }
}
