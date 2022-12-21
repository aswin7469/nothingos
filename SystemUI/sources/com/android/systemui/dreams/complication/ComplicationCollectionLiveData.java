package com.android.systemui.dreams.complication;

import androidx.lifecycle.LiveData;
import com.android.systemui.dreams.DreamOverlayStateController;
import java.util.Collection;
import javax.inject.Inject;

public class ComplicationCollectionLiveData extends LiveData<Collection<Complication>> {
    final DreamOverlayStateController mDreamOverlayStateController;
    final DreamOverlayStateController.Callback mStateControllerCallback = new DreamOverlayStateController.Callback() {
        public void onComplicationsChanged() {
            ComplicationCollectionLiveData complicationCollectionLiveData = ComplicationCollectionLiveData.this;
            complicationCollectionLiveData.setValue(complicationCollectionLiveData.mDreamOverlayStateController.getComplications());
        }

        public void onAvailableComplicationTypesChanged() {
            ComplicationCollectionLiveData complicationCollectionLiveData = ComplicationCollectionLiveData.this;
            complicationCollectionLiveData.setValue(complicationCollectionLiveData.mDreamOverlayStateController.getComplications());
        }
    };

    @Inject
    public ComplicationCollectionLiveData(DreamOverlayStateController dreamOverlayStateController) {
        this.mDreamOverlayStateController = dreamOverlayStateController;
    }

    /* access modifiers changed from: protected */
    public void onActive() {
        super.onActive();
        this.mDreamOverlayStateController.addCallback(this.mStateControllerCallback);
        setValue(this.mDreamOverlayStateController.getComplications());
    }

    /* access modifiers changed from: protected */
    public void onInactive() {
        this.mDreamOverlayStateController.removeCallback(this.mStateControllerCallback);
        super.onInactive();
    }
}
