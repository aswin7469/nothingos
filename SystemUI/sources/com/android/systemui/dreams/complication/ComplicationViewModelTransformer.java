package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import java.util.HashMap;
import javax.inject.Inject;

public class ComplicationViewModelTransformer {
    private final ComplicationId.Factory mComplicationIdFactory = new ComplicationId.Factory();
    private final HashMap<Complication, ComplicationId> mComplicationIdMapping = new HashMap<>();
    private final ComplicationViewModelComponent.Factory mViewModelComponentFactory;

    @Inject
    public ComplicationViewModelTransformer(ComplicationViewModelComponent.Factory factory) {
        this.mViewModelComponentFactory = factory;
    }

    public ComplicationViewModel getViewModel(Complication complication) {
        ComplicationId complicationId = getComplicationId(complication);
        return (ComplicationViewModel) this.mViewModelComponentFactory.create(complication, complicationId).getViewModelProvider().get(complicationId.toString(), ComplicationViewModel.class);
    }

    private ComplicationId getComplicationId(Complication complication) {
        if (!this.mComplicationIdMapping.containsKey(complication)) {
            this.mComplicationIdMapping.put(complication, this.mComplicationIdFactory.getNextId());
        }
        return this.mComplicationIdMapping.get(complication);
    }
}
