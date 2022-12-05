package com.android.settings.network;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.AsyncTaskSidecar;
import com.android.settings.SidecarFragment;
/* loaded from: classes.dex */
public class SwitchSlotSidecar extends AsyncTaskSidecar<Param, Result> {
    private Exception mException;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Param {
        int command;
        int slotId;

        Param() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Result {
        Exception exception;

        Result() {
        }
    }

    public static SwitchSlotSidecar get(FragmentManager fragmentManager) {
        return (SwitchSlotSidecar) SidecarFragment.get(fragmentManager, "SwitchSlotSidecar", SwitchSlotSidecar.class, null);
    }

    @Override // com.android.settings.SidecarFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void runSwitchToRemovableSlot(int i) {
        Param param = new Param();
        param.command = 0;
        param.slotId = i;
        super.run(param);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.AsyncTaskSidecar
    public Result doInBackground(Param param) {
        Result result = new Result();
        if (param == null) {
            result.exception = new UiccSlotsException("Null param");
            return result;
        }
        try {
            if (param.command == 0) {
                UiccSlotUtil.switchToRemovableSlot(param.slotId, getContext());
            } else {
                Log.e("SwitchSlotSidecar", "Wrong command.");
            }
        } catch (UiccSlotsException e) {
            result.exception = e;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.AsyncTaskSidecar
    public void onPostExecute(Result result) {
        Exception exc = result.exception;
        if (exc == null) {
            setState(2, 0);
            return;
        }
        this.mException = exc;
        setState(3, 0);
    }
}
