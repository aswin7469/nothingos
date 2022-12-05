package com.android.settings.panel;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.widget.EventInfo;
import androidx.slice.widget.SliceView;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.panel.PanelSlicesAdapter;
import com.android.settings.slices.CustomSliceRegistry;
import com.google.android.setupdesign.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class PanelSlicesAdapter extends RecyclerView.Adapter<SliceRowViewHolder> {
    static final int MAX_NUM_OF_SLICES = 6;
    private final int mMetricsCategory;
    private final PanelFragment mPanelFragment;
    private final List<LiveData<Slice>> mSliceLiveData;

    public PanelSlicesAdapter(PanelFragment panelFragment, Map<Uri, LiveData<Slice>> map, int i) {
        this.mPanelFragment = panelFragment;
        this.mSliceLiveData = new ArrayList(map.values());
        this.mMetricsCategory = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public SliceRowViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate;
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 1) {
            inflate = from.inflate(R.layout.panel_slice_slider_row, viewGroup, false);
        } else {
            inflate = from.inflate(R.layout.panel_slice_row, viewGroup, false);
        }
        return new SliceRowViewHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SliceRowViewHolder sliceRowViewHolder, int i) {
        sliceRowViewHolder.onBind(this.mSliceLiveData.get(i), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return Math.min(this.mSliceLiveData.size(), 6);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return this.mPanelFragment.getPanelViewType();
    }

    List<LiveData<Slice>> getData() {
        return this.mSliceLiveData.subList(0, getItemCount());
    }

    /* loaded from: classes.dex */
    public class SliceRowViewHolder extends RecyclerView.ViewHolder implements DividerItemDecoration.DividedViewHolder {
        private boolean mDividerAllowedAbove = true;
        final LinearLayout mSliceSliderLayout;
        final SliceView sliceView;

        @Override // com.google.android.setupdesign.DividerItemDecoration.DividedViewHolder
        public boolean isDividerAllowedBelow() {
            return true;
        }

        public SliceRowViewHolder(View view) {
            super(view);
            SliceView sliceView = (SliceView) view.findViewById(R.id.slice_view);
            this.sliceView = sliceView;
            if (sliceView != null) {
                sliceView.setMode(2);
                sliceView.setShowTitleItems(true);
                sliceView.setImportantForAccessibility(2);
            }
            this.mSliceSliderLayout = (LinearLayout) view.findViewById(R.id.slice_slider_layout);
        }

        public void onBind(final LiveData<Slice> liveData, int i) {
            liveData.observe(PanelSlicesAdapter.this.mPanelFragment.getViewLifecycleOwner(), this.sliceView);
            Slice value = liveData.getValue();
            if (value == null || value.getUri().equals(CustomSliceRegistry.MEDIA_OUTPUT_INDICATOR_SLICE_URI)) {
                this.mDividerAllowedAbove = false;
            }
            this.sliceView.setOnSliceActionListener(new SliceView.OnSliceActionListener() { // from class: com.android.settings.panel.PanelSlicesAdapter$SliceRowViewHolder$$ExternalSyntheticLambda0
                @Override // androidx.slice.widget.SliceView.OnSliceActionListener
                public final void onSliceAction(EventInfo eventInfo, SliceItem sliceItem) {
                    PanelSlicesAdapter.SliceRowViewHolder.this.lambda$onBind$0(liveData, eventInfo, sliceItem);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$0(LiveData liveData, EventInfo eventInfo, SliceItem sliceItem) {
            FeatureFactory.getFactory(this.sliceView.getContext()).getMetricsFeatureProvider().action(0, 1658, PanelSlicesAdapter.this.mMetricsCategory, ((Slice) liveData.getValue()).getUri().getLastPathSegment(), eventInfo.actionType);
        }

        @Override // com.google.android.setupdesign.DividerItemDecoration.DividedViewHolder
        public boolean isDividerAllowedAbove() {
            return this.mDividerAllowedAbove;
        }
    }
}
