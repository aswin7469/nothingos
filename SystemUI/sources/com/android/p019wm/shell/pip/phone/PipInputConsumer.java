package com.android.p019wm.shell.pip.phone;

import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.IWindowManager;
import android.view.InputChannel;
import android.view.InputEvent;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.pip.phone.PipInputConsumer */
public class PipInputConsumer {
    private static final String TAG = "PipInputConsumer";
    private InputEventReceiver mInputEventReceiver;
    /* access modifiers changed from: private */
    public InputListener mListener;
    private final ShellExecutor mMainExecutor;
    private final String mName;
    private RegistrationListener mRegistrationListener;
    private final IBinder mToken = new Binder();
    private final IWindowManager mWindowManager;

    /* renamed from: com.android.wm.shell.pip.phone.PipInputConsumer$InputListener */
    public interface InputListener {
        boolean onInputEvent(InputEvent inputEvent);
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipInputConsumer$RegistrationListener */
    public interface RegistrationListener {
        void onRegistrationChanged(boolean z);
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipInputConsumer$InputEventReceiver */
    private final class InputEventReceiver extends BatchedInputEventReceiver {
        InputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer) {
            super(inputChannel, looper, choreographer);
        }

        public void onInputEvent(InputEvent inputEvent) {
            boolean z = true;
            try {
                if (PipInputConsumer.this.mListener != null) {
                    z = PipInputConsumer.this.mListener.onInputEvent(inputEvent);
                }
            } finally {
                finishInputEvent(inputEvent, z);
            }
        }
    }

    public PipInputConsumer(IWindowManager iWindowManager, String str, ShellExecutor shellExecutor) {
        this.mWindowManager = iWindowManager;
        this.mName = str;
        this.mMainExecutor = shellExecutor;
    }

    public void setInputListener(InputListener inputListener) {
        this.mListener = inputListener;
    }

    public void setRegistrationListener(RegistrationListener registrationListener) {
        this.mRegistrationListener = registrationListener;
        this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setRegistrationListener$0$com-android-wm-shell-pip-phone-PipInputConsumer */
    public /* synthetic */ void mo50365x5df529e2() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(this.mInputEventReceiver != null);
        }
    }

    public boolean isRegistered() {
        return this.mInputEventReceiver != null;
    }

    public void registerInputConsumer() {
        if (this.mInputEventReceiver == null) {
            InputChannel inputChannel = new InputChannel();
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
                this.mWindowManager.createInputConsumer(this.mToken, this.mName, 0, inputChannel);
            } catch (RemoteException e) {
                ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to create input consumer, %s", new Object[]{TAG, e});
            }
            this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda0(this, inputChannel));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerInputConsumer$1$com-android-wm-shell-pip-phone-PipInputConsumer */
    public /* synthetic */ void mo50364x1da7ed11(InputChannel inputChannel) {
        this.mInputEventReceiver = new InputEventReceiver(inputChannel, Looper.myLooper(), Choreographer.getSfInstance());
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(true);
        }
    }

    public void unregisterInputConsumer() {
        if (this.mInputEventReceiver != null) {
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
            } catch (RemoteException e) {
                ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to destroy input consumer, %s", new Object[]{TAG, e});
            }
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
            this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unregisterInputConsumer$2$com-android-wm-shell-pip-phone-PipInputConsumer */
    public /* synthetic */ void mo50366xa3f75599() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(false);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "registered=" + (this.mInputEventReceiver != null));
    }
}
