package com.android.systemui.statusbar;

import android.app.ITransientNotificationCallback;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.os.SomeArgs;
import com.android.internal.statusbar.IStatusBar;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.GcUtils;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.tracing.ProtoTracer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class CommandQueue extends IStatusBar.Stub implements CallbackController<Callbacks>, DisplayManager.DisplayListener {
    private static final String TAG = CommandQueue.class.getSimpleName();
    private ProtoTracer mProtoTracer;
    private final CommandRegistry mRegistry;
    private final Object mLock = new Object();
    private ArrayList<Callbacks> mCallbacks = new ArrayList<>();
    private Handler mHandler = new H(Looper.getMainLooper());
    private SparseArray<Pair<Integer, Integer>> mDisplayDisabled = new SparseArray<>();
    private int mLastUpdatedImeDisplayId = -1;
    private final ArraySet<Integer> mDisplayDisabledIgnoreSetup = new ArraySet<>();

    /* loaded from: classes.dex */
    public interface Callbacks {
        default void abortTransient(int i, int[] iArr) {
        }

        default void addQsTile(ComponentName componentName) {
        }

        default void animateCollapsePanels(int i, boolean z) {
        }

        default void animateExpandNotificationsPanel() {
        }

        default void animateExpandSettingsPanel(String str) {
        }

        default void appTransitionCancelled(int i) {
        }

        default void appTransitionFinished(int i) {
        }

        default void appTransitionPending(int i, boolean z) {
        }

        default void appTransitionStarting(int i, long j, long j2, boolean z) {
        }

        default void cancelPreloadRecentApps() {
        }

        default void clickTile(ComponentName componentName) {
        }

        default void disable(int i, int i2, int i3, boolean z) {
        }

        default void dismissInattentiveSleepWarning(boolean z) {
        }

        default void dismissKeyboardShortcutsMenu() {
        }

        default void handleShowGlobalActionsMenu() {
        }

        default void handleShowShutdownUi(boolean z, String str) {
        }

        default void handleSystemKey(int i) {
        }

        default void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        }

        default void hideAuthenticationDialog() {
        }

        default void hideRecentApps(boolean z, boolean z2) {
        }

        default void hideToast(String str, IBinder iBinder) {
        }

        default void onBiometricAuthenticated() {
        }

        default void onBiometricError(int i, int i2, int i3) {
        }

        default void onBiometricHelp(int i, String str) {
        }

        default void onCameraLaunchGestureDetected(int i) {
        }

        default void onDisplayReady(int i) {
        }

        default void onDisplayRemoved(int i) {
        }

        default void onEmergencyActionLaunchGestureDetected() {
        }

        default void onRecentsAnimationStateChanged(boolean z) {
        }

        default void onRotationProposal(int i, boolean z) {
        }

        default void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
        }

        default void onTracingStateChanged(boolean z) {
        }

        default void preloadRecentApps() {
        }

        default void remQsTile(ComponentName componentName) {
        }

        default void removeIcon(String str) {
        }

        default void requestWindowMagnificationConnection(boolean z) {
        }

        default void setIcon(String str, StatusBarIcon statusBarIcon) {
        }

        default void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        }

        default void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        }

        default void setTopAppHidesStatusBar(boolean z) {
        }

        default void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        }

        default void setWindowState(int i, int i2, int i3) {
        }

        default void showAssistDisclosure() {
        }

        default void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        }

        default void showInattentiveSleepWarning() {
        }

        default void showPictureInPictureMenu() {
        }

        default void showPinningEnterExitToast(boolean z) {
        }

        default void showPinningEscapeToast() {
        }

        default void showRecentApps(boolean z) {
        }

        default void showScreenPinningRequest(int i) {
        }

        default void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
        }

        default void showTransient(int i, int[] iArr) {
        }

        default void showWirelessChargingAnimation(int i) {
        }

        default void startAssist(Bundle bundle) {
        }

        default void suppressAmbientDisplay(boolean z) {
        }

        default void toggleKeyboardShortcutsMenu(int i) {
        }

        default void togglePanel() {
        }

        default void toggleRecentApps() {
        }

        default void toggleSplitScreen() {
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int i) {
    }

    public CommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        this.mProtoTracer = protoTracer;
        this.mRegistry = commandRegistry;
        ((DisplayManager) context.getSystemService(DisplayManager.class)).registerDisplayListener(this, this.mHandler);
        setDisabled(0, 0, 0);
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int i) {
        synchronized (this.mLock) {
            this.mDisplayDisabled.remove(i);
            this.mDisplayDisabledIgnoreSetup.remove(Integer.valueOf(i));
        }
        for (int size = this.mCallbacks.size() - 1; size >= 0; size--) {
            this.mCallbacks.get(size).onDisplayRemoved(i);
        }
    }

    public boolean panelsEnabled() {
        return (getDisabled1(0) & 65536) == 0 && (getDisabled2(0) & 4) == 0 && !StatusBar.ONLY_CORE_APPS;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(Callbacks callbacks) {
        this.mCallbacks.add(callbacks);
        for (int i = 0; i < this.mDisplayDisabled.size(); i++) {
            int keyAt = this.mDisplayDisabled.keyAt(i);
            callbacks.disable(keyAt, getDisabled1(keyAt), getDisabled2(keyAt), false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(Callbacks callbacks) {
        this.mCallbacks.remove(callbacks);
    }

    public void setIcon(String str, StatusBarIcon statusBarIcon) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 1, 0, new Pair(str, statusBarIcon)).sendToTarget();
        }
    }

    public void removeIcon(String str) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 2, 0, str).sendToTarget();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        synchronized (this.mLock) {
            setDisabled(i, i2, i3);
            this.mHandler.removeMessages(131072);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            obtain.argi4 = z ? 1 : 0;
            Message obtainMessage = this.mHandler.obtainMessage(131072, obtain);
            if (Looper.myLooper() == this.mHandler.getLooper()) {
                this.mHandler.handleMessage(obtainMessage);
                obtainMessage.recycle();
            } else {
                obtainMessage.sendToTarget();
            }
        }
    }

    public void disable(int i, int i2, int i3) {
        synchronized (this.mLock) {
            this.mDisplayDisabledIgnoreSetup.add(Integer.valueOf(i));
            disable(i, i2, i3, true);
        }
    }

    public void recomputeDisableFlags(int i, boolean z) {
        synchronized (this.mLock) {
            disable(i, getDisabled1(i), getDisabled2(i), z);
        }
    }

    private void setDisabled(int i, int i2, int i3) {
        this.mDisplayDisabled.put(i, new Pair<>(Integer.valueOf(i2), Integer.valueOf(i3)));
    }

    private int getDisabled1(int i) {
        return ((Integer) getDisabled(i).first).intValue();
    }

    private int getDisabled2(int i) {
        return ((Integer) getDisabled(i).second).intValue();
    }

    private Pair<Integer, Integer> getDisabled(int i) {
        Pair<Integer, Integer> pair = this.mDisplayDisabled.get(i);
        if (pair == null) {
            Pair<Integer, Integer> pair2 = new Pair<>(0, 0);
            this.mDisplayDisabled.put(i, pair2);
            return pair2;
        }
        return pair;
    }

    public void animateExpandNotificationsPanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(196608);
            this.mHandler.sendEmptyMessage(196608);
        }
    }

    public void animateCollapsePanels() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, 0, 0).sendToTarget();
        }
    }

    public void animateCollapsePanels(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void togglePanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2293760);
            this.mHandler.obtainMessage(2293760, 0, 0).sendToTarget();
        }
    }

    public void animateExpandSettingsPanel(String str) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(327680);
            this.mHandler.obtainMessage(327680, str).sendToTarget();
        }
    }

    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z, boolean z2) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(524288);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            int i4 = 1;
            obtain.argi4 = z ? 1 : 0;
            if (!z2) {
                i4 = 0;
            }
            obtain.argi5 = i4;
            obtain.arg1 = iBinder;
            this.mHandler.obtainMessage(524288, obtain).sendToTarget();
        }
    }

    public void showRecentApps(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(851968);
            this.mHandler.obtainMessage(851968, z ? 1 : 0, 0, null).sendToTarget();
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(917504);
            this.mHandler.obtainMessage(917504, z ? 1 : 0, z2 ? 1 : 0, null).sendToTarget();
        }
    }

    public void toggleSplitScreen() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1966080);
            this.mHandler.obtainMessage(1966080, 0, 0, null).sendToTarget();
        }
    }

    public void toggleRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(589824);
            Message obtainMessage = this.mHandler.obtainMessage(589824, 0, 0, null);
            obtainMessage.setAsynchronous(true);
            obtainMessage.sendToTarget();
        }
    }

    public void preloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(655360);
            this.mHandler.obtainMessage(655360, 0, 0, null).sendToTarget();
        }
    }

    public void cancelPreloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(720896);
            this.mHandler.obtainMessage(720896, 0, 0, null).sendToTarget();
        }
    }

    public void dismissKeyboardShortcutsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2097152);
            this.mHandler.obtainMessage(2097152).sendToTarget();
        }
    }

    public void toggleKeyboardShortcutsMenu(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1638400);
            this.mHandler.obtainMessage(1638400, i, 0).sendToTarget();
        }
    }

    public void showPictureInPictureMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1703936);
            this.mHandler.obtainMessage(1703936).sendToTarget();
        }
    }

    public void setWindowState(int i, int i2, int i3) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(786432, i, i2, Integer.valueOf(i3)).sendToTarget();
        }
    }

    public void showScreenPinningRequest(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1179648, i, 0, null).sendToTarget();
        }
    }

    public void appTransitionPending(int i) {
        appTransitionPending(i, false);
    }

    public void appTransitionPending(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1245184, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void appTransitionCancelled(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1310720, i, 0).sendToTarget();
        }
    }

    public void appTransitionStarting(int i, long j, long j2) {
        appTransitionStarting(i, j, j2, false);
    }

    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = z ? 1 : 0;
            obtain.arg1 = Long.valueOf(j);
            obtain.arg2 = Long.valueOf(j2);
            this.mHandler.obtainMessage(1376256, obtain).sendToTarget();
        }
    }

    public void appTransitionFinished(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2031616, i, 0).sendToTarget();
        }
    }

    public void showAssistDisclosure() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1441792);
            this.mHandler.obtainMessage(1441792).sendToTarget();
        }
    }

    public void startAssist(Bundle bundle) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1507328);
            this.mHandler.obtainMessage(1507328, bundle).sendToTarget();
        }
    }

    public void onCameraLaunchGestureDetected(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1572864);
            this.mHandler.obtainMessage(1572864, i, 0).sendToTarget();
        }
    }

    public void onEmergencyActionLaunchGestureDetected() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(3801088);
            this.mHandler.obtainMessage(3801088).sendToTarget();
        }
    }

    public void addQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1769472, componentName).sendToTarget();
        }
    }

    public void remQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1835008, componentName).sendToTarget();
        }
    }

    public void clickQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1900544, componentName).sendToTarget();
        }
    }

    public void handleSystemKey(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2162688, i, 0).sendToTarget();
        }
    }

    public void showPinningEnterExitToast(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2949120, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void showPinningEscapeToast() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3014656).sendToTarget();
        }
    }

    public void showGlobalActionsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2228224);
            this.mHandler.obtainMessage(2228224).sendToTarget();
        }
    }

    public void setTopAppHidesStatusBar(boolean z) {
        this.mHandler.removeMessages(2424832);
        this.mHandler.obtainMessage(2424832, z ? 1 : 0, 0).sendToTarget();
    }

    public void showShutdownUi(boolean z, String str) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2359296);
            this.mHandler.obtainMessage(2359296, z ? 1 : 0, 0, str).sendToTarget();
        }
    }

    public void showWirelessChargingAnimation(int i) {
        this.mHandler.removeMessages(2883584);
        this.mHandler.obtainMessage(2883584, i, 0).sendToTarget();
    }

    public void onProposedRotationChanged(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2490368);
            this.mHandler.obtainMessage(2490368, i, z ? 1 : 0, null).sendToTarget();
        }
    }

    public void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = promptInfo;
            obtain.arg2 = iBiometricSysuiReceiver;
            obtain.arg3 = iArr;
            obtain.arg4 = Boolean.valueOf(z);
            obtain.arg5 = Boolean.valueOf(z2);
            obtain.argi1 = i;
            obtain.arg6 = str;
            obtain.arg7 = Long.valueOf(j);
            obtain.arg8 = Long.valueOf(j2);
            obtain.argi2 = i2;
            this.mHandler.obtainMessage(2555904, obtain).sendToTarget();
        }
    }

    public void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            obtain.arg3 = charSequence;
            obtain.arg4 = iBinder2;
            obtain.arg5 = iTransientNotificationCallback;
            obtain.argi1 = i;
            obtain.argi2 = i2;
            this.mHandler.obtainMessage(3407872, obtain).sendToTarget();
        }
    }

    public void hideToast(String str, IBinder iBinder) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            this.mHandler.obtainMessage(3473408, obtain).sendToTarget();
        }
    }

    public void onBiometricAuthenticated() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2621440).sendToTarget();
        }
    }

    public void onBiometricHelp(int i, String str) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.arg1 = str;
            this.mHandler.obtainMessage(2686976, obtain).sendToTarget();
        }
    }

    public void onBiometricError(int i, int i2, int i3) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            this.mHandler.obtainMessage(2752512, obtain).sendToTarget();
        }
    }

    public void hideAuthenticationDialog() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2818048).sendToTarget();
        }
    }

    public void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3932160, iUdfpsHbmListener).sendToTarget();
        }
    }

    public void onDisplayReady(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(458752, i, 0).sendToTarget();
        }
    }

    public void onRecentsAnimationStateChanged(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3080192, z ? 1 : 0, 0).sendToTarget();
        }
    }

    public void showInattentiveSleepWarning() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3276800).sendToTarget();
        }
    }

    public void dismissInattentiveSleepWarning(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3342336, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void requestWindowMagnificationConnection(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3670016, Boolean.valueOf(z)).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShowImeButton(int i, IBinder iBinder, int i2, int i3, boolean z, boolean z2) {
        int i4;
        if (i == -1) {
            return;
        }
        if (!z2 && (i4 = this.mLastUpdatedImeDisplayId) != i && i4 != -1) {
            sendImeInvisibleStatusForPrevNavBar();
        }
        for (int i5 = 0; i5 < this.mCallbacks.size(); i5++) {
            this.mCallbacks.get(i5).setImeWindowStatus(i, iBinder, i2, i3, z);
        }
        this.mLastUpdatedImeDisplayId = i;
    }

    private void sendImeInvisibleStatusForPrevNavBar() {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).setImeWindowStatus(this.mLastUpdatedImeDisplayId, null, 4, 0, false);
        }
    }

    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            int i4 = 1;
            obtain.argi3 = z ? 1 : 0;
            obtain.arg1 = appearanceRegionArr;
            obtain.argi4 = i3;
            if (!z2) {
                i4 = 0;
            }
            obtain.argi5 = i4;
            this.mHandler.obtainMessage(393216, obtain).sendToTarget();
        }
    }

    public void showTransient(int i, int[] iArr) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3145728, i, 0, iArr).sendToTarget();
        }
    }

    public void abortTransient(int i, int[] iArr) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3211264, i, 0, iArr).sendToTarget();
        }
    }

    public void startTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                protoTracer.start();
            }
            this.mHandler.obtainMessage(3538944, Boolean.TRUE).sendToTarget();
        }
    }

    public void stopTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                protoTracer.stop();
            }
            this.mHandler.obtainMessage(3538944, Boolean.FALSE).sendToTarget();
        }
    }

    public void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = strArr;
            obtain.arg2 = parcelFileDescriptor;
            this.mHandler.obtainMessage(3735552, obtain).sendToTarget();
        }
    }

    public void suppressAmbientDisplay(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3604480, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3866624, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void passThroughShellCommand(final String[] strArr, final ParcelFileDescriptor parcelFileDescriptor) {
        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
        new Thread("Sysui.passThroughShellCommand") { // from class: com.android.systemui.statusbar.CommandQueue.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (CommandQueue.this.mRegistry != null) {
                        CommandQueue.this.mRegistry.onShellCommand(printWriter, strArr);
                        try {
                            parcelFileDescriptor.close();
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    }
                    try {
                        parcelFileDescriptor.close();
                    } catch (Exception unused2) {
                    }
                } finally {
                    printWriter.flush();
                    try {
                        parcelFileDescriptor.close();
                    } catch (Exception unused3) {
                    }
                }
            }
        }.start();
    }

    public void runGcForTest() {
        GcUtils.runGcAndFinalizersSync();
    }

    /* loaded from: classes.dex */
    private final class H extends Handler {
        private H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = 0;
            switch (message.what & (-65536)) {
                case 65536:
                    int i2 = message.arg1;
                    if (i2 == 1) {
                        Pair pair = (Pair) message.obj;
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            ((Callbacks) CommandQueue.this.mCallbacks.get(i)).setIcon((String) pair.first, (StatusBarIcon) pair.second);
                            i++;
                        }
                        return;
                    } else if (i2 != 2) {
                        return;
                    } else {
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            ((Callbacks) CommandQueue.this.mCallbacks.get(i)).removeIcon((String) message.obj);
                            i++;
                        }
                        return;
                    }
                case 131072:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    for (int i3 = 0; i3 < CommandQueue.this.mCallbacks.size(); i3++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i3)).disable(someArgs.argi1, someArgs.argi2, someArgs.argi3, someArgs.argi4 != 0);
                    }
                    return;
                case 196608:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).animateExpandNotificationsPanel();
                        i++;
                    }
                    return;
                case 262144:
                    for (int i4 = 0; i4 < CommandQueue.this.mCallbacks.size(); i4++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i4)).animateCollapsePanels(message.arg1, message.arg2 != 0);
                    }
                    return;
                case 327680:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).animateExpandSettingsPanel((String) message.obj);
                        i++;
                    }
                    return;
                case 393216:
                    SomeArgs someArgs2 = (SomeArgs) message.obj;
                    for (int i5 = 0; i5 < CommandQueue.this.mCallbacks.size(); i5++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i5)).onSystemBarAttributesChanged(someArgs2.argi1, someArgs2.argi2, (AppearanceRegion[]) someArgs2.arg1, someArgs2.argi3 == 1, someArgs2.argi4, someArgs2.argi5 == 1);
                    }
                    someArgs2.recycle();
                    return;
                case 458752:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onDisplayReady(message.arg1);
                        i++;
                    }
                    return;
                case 524288:
                    SomeArgs someArgs3 = (SomeArgs) message.obj;
                    CommandQueue.this.handleShowImeButton(someArgs3.argi1, (IBinder) someArgs3.arg1, someArgs3.argi2, someArgs3.argi3, someArgs3.argi4 != 0, someArgs3.argi5 != 0);
                    return;
                case 589824:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).toggleRecentApps();
                        i++;
                    }
                    return;
                case 655360:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).preloadRecentApps();
                        i++;
                    }
                    return;
                case 720896:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).cancelPreloadRecentApps();
                        i++;
                    }
                    return;
                case 786432:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).setWindowState(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                        i++;
                    }
                    return;
                case 851968:
                    for (int i6 = 0; i6 < CommandQueue.this.mCallbacks.size(); i6++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i6)).showRecentApps(message.arg1 != 0);
                    }
                    return;
                case 917504:
                    for (int i7 = 0; i7 < CommandQueue.this.mCallbacks.size(); i7++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i7)).hideRecentApps(message.arg1 != 0, message.arg2 != 0);
                    }
                    return;
                case 1179648:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showScreenPinningRequest(message.arg1);
                        i++;
                    }
                    return;
                case 1245184:
                    for (int i8 = 0; i8 < CommandQueue.this.mCallbacks.size(); i8++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i8)).appTransitionPending(message.arg1, message.arg2 != 0);
                    }
                    return;
                case 1310720:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).appTransitionCancelled(message.arg1);
                        i++;
                    }
                    return;
                case 1376256:
                    SomeArgs someArgs4 = (SomeArgs) message.obj;
                    for (int i9 = 0; i9 < CommandQueue.this.mCallbacks.size(); i9++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i9)).appTransitionStarting(someArgs4.argi1, ((Long) someArgs4.arg1).longValue(), ((Long) someArgs4.arg2).longValue(), someArgs4.argi2 != 0);
                    }
                    return;
                case 1441792:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showAssistDisclosure();
                        i++;
                    }
                    return;
                case 1507328:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).startAssist((Bundle) message.obj);
                        i++;
                    }
                    return;
                case 1572864:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onCameraLaunchGestureDetected(message.arg1);
                        i++;
                    }
                    return;
                case 1638400:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).toggleKeyboardShortcutsMenu(message.arg1);
                        i++;
                    }
                    return;
                case 1703936:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showPictureInPictureMenu();
                        i++;
                    }
                    return;
                case 1769472:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).addQsTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1835008:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).remQsTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1900544:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).clickTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1966080:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).toggleSplitScreen();
                        i++;
                    }
                    return;
                case 2031616:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).appTransitionFinished(message.arg1);
                        i++;
                    }
                    return;
                case 2097152:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).dismissKeyboardShortcutsMenu();
                        i++;
                    }
                    return;
                case 2162688:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).handleSystemKey(message.arg1);
                        i++;
                    }
                    return;
                case 2228224:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).handleShowGlobalActionsMenu();
                        i++;
                    }
                    return;
                case 2293760:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).togglePanel();
                        i++;
                    }
                    return;
                case 2359296:
                    for (int i10 = 0; i10 < CommandQueue.this.mCallbacks.size(); i10++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i10)).handleShowShutdownUi(message.arg1 != 0, (String) message.obj);
                    }
                    return;
                case 2424832:
                    for (int i11 = 0; i11 < CommandQueue.this.mCallbacks.size(); i11++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i11)).setTopAppHidesStatusBar(message.arg1 != 0);
                    }
                    return;
                case 2490368:
                    for (int i12 = 0; i12 < CommandQueue.this.mCallbacks.size(); i12++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i12)).onRotationProposal(message.arg1, message.arg2 != 0);
                    }
                    return;
                case 2555904:
                    CommandQueue.this.mHandler.removeMessages(2752512);
                    CommandQueue.this.mHandler.removeMessages(2686976);
                    CommandQueue.this.mHandler.removeMessages(2621440);
                    SomeArgs someArgs5 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showAuthenticationDialog((PromptInfo) someArgs5.arg1, (IBiometricSysuiReceiver) someArgs5.arg2, (int[]) someArgs5.arg3, ((Boolean) someArgs5.arg4).booleanValue(), ((Boolean) someArgs5.arg5).booleanValue(), someArgs5.argi1, ((Long) someArgs5.arg7).longValue(), (String) someArgs5.arg6, ((Long) someArgs5.arg8).longValue(), someArgs5.argi2);
                        i++;
                    }
                    someArgs5.recycle();
                    return;
                case 2621440:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onBiometricAuthenticated();
                        i++;
                    }
                    return;
                case 2686976:
                    SomeArgs someArgs6 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onBiometricHelp(someArgs6.argi1, (String) someArgs6.arg1);
                        i++;
                    }
                    someArgs6.recycle();
                    return;
                case 2752512:
                    SomeArgs someArgs7 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onBiometricError(someArgs7.argi1, someArgs7.argi2, someArgs7.argi3);
                        i++;
                    }
                    someArgs7.recycle();
                    return;
                case 2818048:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).hideAuthenticationDialog();
                        i++;
                    }
                    return;
                case 2883584:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showWirelessChargingAnimation(message.arg1);
                        i++;
                    }
                    return;
                case 2949120:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showPinningEnterExitToast(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3014656:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showPinningEscapeToast();
                        i++;
                    }
                    return;
                case 3080192:
                    for (int i13 = 0; i13 < CommandQueue.this.mCallbacks.size(); i13++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i13)).onRecentsAnimationStateChanged(message.arg1 > 0);
                    }
                    return;
                case 3145728:
                    int i14 = message.arg1;
                    int[] iArr = (int[]) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showTransient(i14, iArr);
                        i++;
                    }
                    return;
                case 3211264:
                    int i15 = message.arg1;
                    int[] iArr2 = (int[]) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).abortTransient(i15, iArr2);
                        i++;
                    }
                    return;
                case 3276800:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).showInattentiveSleepWarning();
                        i++;
                    }
                    return;
                case 3342336:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).dismissInattentiveSleepWarning(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3407872:
                    SomeArgs someArgs8 = (SomeArgs) message.obj;
                    String str = (String) someArgs8.arg1;
                    IBinder iBinder = (IBinder) someArgs8.arg2;
                    CharSequence charSequence = (CharSequence) someArgs8.arg3;
                    IBinder iBinder2 = (IBinder) someArgs8.arg4;
                    ITransientNotificationCallback iTransientNotificationCallback = (ITransientNotificationCallback) someArgs8.arg5;
                    int i16 = someArgs8.argi1;
                    int i17 = someArgs8.argi2;
                    Iterator it = CommandQueue.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((Callbacks) it.next()).showToast(i16, str, iBinder, charSequence, iBinder2, i17, iTransientNotificationCallback);
                    }
                    return;
                case 3473408:
                    SomeArgs someArgs9 = (SomeArgs) message.obj;
                    String str2 = (String) someArgs9.arg1;
                    IBinder iBinder3 = (IBinder) someArgs9.arg2;
                    Iterator it2 = CommandQueue.this.mCallbacks.iterator();
                    while (it2.hasNext()) {
                        ((Callbacks) it2.next()).hideToast(str2, iBinder3);
                    }
                    return;
                case 3538944:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onTracingStateChanged(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3604480:
                    Iterator it3 = CommandQueue.this.mCallbacks.iterator();
                    while (it3.hasNext()) {
                        ((Callbacks) it3.next()).suppressAmbientDisplay(((Boolean) message.obj).booleanValue());
                    }
                    return;
                case 3670016:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).requestWindowMagnificationConnection(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3735552:
                    SomeArgs someArgs10 = (SomeArgs) message.obj;
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) someArgs10.arg2;
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            ((Callbacks) CommandQueue.this.mCallbacks.get(i)).handleWindowManagerLoggingCommand((String[]) someArgs10.arg1, parcelFileDescriptor);
                            i++;
                        }
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor.close();
                        }
                    } catch (IOException e) {
                        Log.e(CommandQueue.TAG, "Failed to handle logging command", e);
                    }
                    someArgs10.recycle();
                    return;
                case 3801088:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).onEmergencyActionLaunchGestureDetected();
                        i++;
                    }
                    return;
                case 3866624:
                    for (int i18 = 0; i18 < CommandQueue.this.mCallbacks.size(); i18++) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i18)).setNavigationBarLumaSamplingEnabled(message.arg1, message.arg2 != 0);
                    }
                    return;
                case 3932160:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        ((Callbacks) CommandQueue.this.mCallbacks.get(i)).setUdfpsHbmListener((IUdfpsHbmListener) message.obj);
                        i++;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public boolean isDumpCommandQueue(String[] strArr) {
        return strArr.length > 1 && "commandqueue".equals(strArr[1]);
    }

    public void dump(PrintWriter printWriter) {
        String sparseArray;
        synchronized (this.mLock) {
            sparseArray = this.mDisplayDisabled.toString();
        }
        printWriter.println("  CommandQueue: ");
        printWriter.println(" displayDisabled: ");
        printWriter.println(sparseArray);
    }

    public void setDisableFlagsForSetup(int i, int i2, int i3) {
        synchronized (this.mLock) {
            if (this.mDisplayDisabledIgnoreSetup.contains(Integer.valueOf(i))) {
                String str = TAG;
                Log.d(str, "ignore setDisabledFlags for setup, state1: " + i2 + ", state2: " + i3);
                return;
            }
            disable(i, i2, i3, false);
        }
    }
}
