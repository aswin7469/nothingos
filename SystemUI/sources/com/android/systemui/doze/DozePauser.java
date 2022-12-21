package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import com.android.systemui.util.AlarmTimeout;
import javax.inject.Inject;

@DozeScope
public class DozePauser implements DozeMachine.Part {
    public static final String TAG = "DozePauser";
    private DozeMachine mMachine;
    private final AlarmTimeout mPauseTimeout;
    private final AlwaysOnDisplayPolicy mPolicy;

    @Inject
    public DozePauser(@Main Handler handler, AlarmManager alarmManager, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy) {
        this.mPauseTimeout = new AlarmTimeout(alarmManager, new DozePauser$$ExternalSyntheticLambda0(this), TAG, handler);
        this.mPolicy = alwaysOnDisplayPolicy;
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* renamed from: com.android.systemui.doze.DozePauser$1 */
    static /* synthetic */ class C20591 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (C20591.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()] != 1) {
            this.mPauseTimeout.cancel();
        } else {
            this.mPauseTimeout.schedule(this.mPolicy.proxScreenOffDelayMs, 1);
        }
    }

    /* access modifiers changed from: private */
    public void onTimeout() {
        this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSED);
    }
}
