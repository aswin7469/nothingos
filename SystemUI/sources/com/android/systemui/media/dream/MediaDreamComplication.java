package com.android.systemui.media.dream;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent;
import javax.inject.Inject;

public class MediaDreamComplication implements Complication {
    MediaComplicationComponent.Factory mComponentFactory;

    public int getRequiredTypeAvailability() {
        return 16;
    }

    @Inject
    public MediaDreamComplication(MediaComplicationComponent.Factory factory) {
        this.mComponentFactory = factory;
    }

    public Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return this.mComponentFactory.create().getViewHolder();
    }
}
