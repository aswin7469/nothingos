package com.android.systemui.dreams.complication;

import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.util.ViewController;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

public class ComplicationHostViewController extends ViewController<ConstraintLayout> {
    public static final String TAG = "ComplicationHostViewController";
    private final ComplicationCollectionViewModel mComplicationCollectionViewModel;
    private final HashMap<ComplicationId, Complication.ViewHolder> mComplications = new HashMap<>();
    private final ComplicationLayoutEngine mLayoutEngine;
    private final LifecycleOwner mLifecycleOwner;

    /* access modifiers changed from: protected */
    public void onViewAttached() {
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
    }

    @Inject
    protected ComplicationHostViewController(@Named("scoped_complications_layout") ConstraintLayout constraintLayout, ComplicationLayoutEngine complicationLayoutEngine, LifecycleOwner lifecycleOwner, @Named("scoped_complications_model") ComplicationCollectionViewModel complicationCollectionViewModel) {
        super(constraintLayout);
        this.mLayoutEngine = complicationLayoutEngine;
        this.mLifecycleOwner = lifecycleOwner;
        this.mComplicationCollectionViewModel = complicationCollectionViewModel;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        super.onInit();
        this.mComplicationCollectionViewModel.getComplications().observe(this.mLifecycleOwner, new ComplicationHostViewController$$ExternalSyntheticLambda0(this));
    }

    public Region getTouchRegions() {
        Region region = new Region();
        Rect rect = new Rect();
        int childCount = ((ConstraintLayout) this.mView).getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (((ConstraintLayout) this.mView).getChildAt(i).getGlobalVisibleRect(rect)) {
                region.op(rect, Region.Op.UNION);
            }
        }
        return region;
    }

    /* access modifiers changed from: private */
    /* renamed from: updateComplications */
    public void mo32562xf450732d(Collection<ComplicationViewModel> collection) {
        ((Collection) this.mComplications.keySet().stream().filter(new ComplicationHostViewController$$ExternalSyntheticLambda2((Collection) collection.stream().map(new ComplicationHostViewController$$ExternalSyntheticLambda1()).collect(Collectors.toSet()))).collect(Collectors.toSet())).forEach(new ComplicationHostViewController$$ExternalSyntheticLambda3(this));
        ((Collection) collection.stream().filter(new ComplicationHostViewController$$ExternalSyntheticLambda4(this)).collect(Collectors.toSet())).forEach(new ComplicationHostViewController$$ExternalSyntheticLambda5(this));
    }

    static /* synthetic */ boolean lambda$updateComplications$2(Collection collection, ComplicationId complicationId) {
        return !collection.contains(complicationId);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateComplications$3$com-android-systemui-dreams-complication-ComplicationHostViewController */
    public /* synthetic */ void mo32563xdb58a6b3(ComplicationId complicationId) {
        this.mLayoutEngine.removeComplication(complicationId);
        this.mComplications.remove(complicationId);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateComplications$4$com-android-systemui-dreams-complication-ComplicationHostViewController */
    public /* synthetic */ boolean mo32564xa2648db4(ComplicationViewModel complicationViewModel) {
        return !this.mComplications.containsKey(complicationViewModel.getId());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateComplications$5$com-android-systemui-dreams-complication-ComplicationHostViewController */
    public /* synthetic */ void mo32565x697074b5(ComplicationViewModel complicationViewModel) {
        ComplicationId id = complicationViewModel.getId();
        Complication.ViewHolder createView = complicationViewModel.getComplication().createView(complicationViewModel);
        this.mComplications.put(id, createView);
        if (createView.getView().getParent() != null) {
            Log.e(TAG, "View for complication " + complicationViewModel.getComplication().getClass() + " already has a parent. Make sure not to reuse complication views!");
        }
        this.mLayoutEngine.addComplication(id, createView.getView(), createView.getLayoutParams(), createView.getCategory());
    }

    public View getView() {
        return this.mView;
    }

    public List<View> getViewsAtPosition(int i) {
        return this.mLayoutEngine.getViewsAtPosition(i);
    }
}
