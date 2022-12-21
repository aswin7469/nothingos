package com.android.systemui.dreams.complication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class ComplicationCollectionViewModel extends ViewModel {
    private final LiveData<Collection<ComplicationViewModel>> mComplications;
    private final ComplicationViewModelTransformer mTransformer;

    @Inject
    public ComplicationCollectionViewModel(ComplicationCollectionLiveData complicationCollectionLiveData, ComplicationViewModelTransformer complicationViewModelTransformer) {
        this.mComplications = Transformations.map(complicationCollectionLiveData, new ComplicationCollectionViewModel$$ExternalSyntheticLambda1(this));
        this.mTransformer = complicationViewModelTransformer;
    }

    /* access modifiers changed from: private */
    /* renamed from: convert */
    public Collection<ComplicationViewModel> mo32558xc354972b(Collection<Complication> collection) {
        return (Collection) collection.stream().map(new ComplicationCollectionViewModel$$ExternalSyntheticLambda0(this)).collect(Collectors.toSet());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$convert$1$com-android-systemui-dreams-complication-ComplicationCollectionViewModel */
    public /* synthetic */ ComplicationViewModel mo32557x6ea173d7(Complication complication) {
        return this.mTransformer.getViewModel(complication);
    }

    public LiveData<Collection<ComplicationViewModel>> getComplications() {
        return this.mComplications;
    }
}
