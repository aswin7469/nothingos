package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModel;
import com.android.systemui.dreams.complication.Complication;
import javax.inject.Inject;

public class ComplicationViewModel extends ViewModel {
    private final Complication mComplication;
    private final Complication.Host mHost;
    private final ComplicationId mId;

    @Inject
    public ComplicationViewModel(Complication complication, ComplicationId complicationId, Complication.Host host) {
        this.mComplication = complication;
        this.mId = complicationId;
        this.mHost = host;
    }

    public ComplicationId getId() {
        return this.mId;
    }

    public Complication getComplication() {
        return this.mComplication;
    }

    public void exitDream() {
        this.mHost.requestExitDream();
    }
}
