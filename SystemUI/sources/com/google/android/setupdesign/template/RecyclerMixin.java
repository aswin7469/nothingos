package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.DividerItemDecoration;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.items.ItemHierarchy;
import com.google.android.setupdesign.items.ItemInflater;
import com.google.android.setupdesign.items.RecyclerItemAdapter;
import com.google.android.setupdesign.util.DrawableLayoutDirectionHelper;
import com.google.android.setupdesign.util.PartnerStyleHelper;
import com.google.android.setupdesign.view.HeaderRecyclerView;

public class RecyclerMixin implements Mixin {
    private Drawable defaultDivider;
    private Drawable divider;
    private DividerItemDecoration dividerDecoration;
    private int dividerInsetEnd;
    private int dividerInsetStart;
    private View header;
    private boolean isDividerDisplay = true;
    private final RecyclerView recyclerView;
    private final TemplateLayout templateLayout;

    public RecyclerMixin(TemplateLayout templateLayout2, RecyclerView recyclerView2) {
        this.templateLayout = templateLayout2;
        this.dividerDecoration = new DividerItemDecoration(templateLayout2.getContext());
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(templateLayout2.getContext()));
        if (recyclerView2 instanceof HeaderRecyclerView) {
            this.header = ((HeaderRecyclerView) recyclerView2).getHeader();
        }
        boolean isShowItemsDivider = isShowItemsDivider(templateLayout2.getContext());
        this.isDividerDisplay = isShowItemsDivider;
        if (isShowItemsDivider) {
            recyclerView2.addItemDecoration(this.dividerDecoration);
        }
    }

    private boolean isShowItemsDivider(Context context) {
        TypedValue typedValue = new TypedValue();
        boolean z = true;
        context.getTheme().resolveAttribute(C3963R.attr.sudDividerShown, typedValue, true);
        if (typedValue.data == 0) {
            z = false;
        }
        return (!PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout) || !PartnerConfigHelper.get(this.recyclerView.getContext()).isPartnerConfigAvailable(PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN)) ? z : PartnerConfigHelper.get(this.recyclerView.getContext()).getBoolean(this.recyclerView.getContext(), PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN, z);
    }

    public void parseAttributes(AttributeSet attributeSet, int i) {
        boolean z;
        boolean z2;
        Context context = this.templateLayout.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3963R.styleable.SudRecyclerMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C3963R.styleable.SudRecyclerMixin_android_entries, 0);
        if (resourceId != 0) {
            ItemHierarchy itemHierarchy = (ItemHierarchy) new ItemInflater(context).inflate(resourceId);
            TemplateLayout templateLayout2 = this.templateLayout;
            if (templateLayout2 instanceof GlifLayout) {
                z2 = ((GlifLayout) templateLayout2).shouldApplyPartnerHeavyThemeResource();
                z = ((GlifLayout) this.templateLayout).useFullDynamicColor();
            } else {
                z2 = false;
                z = false;
            }
            RecyclerItemAdapter recyclerItemAdapter = new RecyclerItemAdapter(itemHierarchy, z2, z);
            recyclerItemAdapter.setHasStableIds(obtainStyledAttributes.getBoolean(C3963R.styleable.SudRecyclerMixin_sudHasStableIds, false));
            setAdapter(recyclerItemAdapter);
        }
        if (!this.isDividerDisplay) {
            obtainStyledAttributes.recycle();
            return;
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudRecyclerMixin_sudDividerInset, -1);
        if (dimensionPixelSize != -1) {
            setDividerInset(dimensionPixelSize);
        } else {
            int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudRecyclerMixin_sudDividerInsetStart, 0);
            int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudRecyclerMixin_sudDividerInsetEnd, 0);
            if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START)) {
                    dimensionPixelSize2 = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START);
                }
                if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_END)) {
                    dimensionPixelSize3 = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
                }
            }
            setDividerInsets(dimensionPixelSize2, dimensionPixelSize3);
        }
        obtainStyledAttributes.recycle();
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public View getHeader() {
        return this.header;
    }

    public void onLayout() {
        if (this.divider == null) {
            updateDivider();
        }
    }

    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter = this.recyclerView.getAdapter();
        return adapter instanceof HeaderRecyclerView.HeaderAdapter ? ((HeaderRecyclerView.HeaderAdapter) adapter).getWrappedAdapter() : adapter;
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        this.recyclerView.setAdapter(adapter);
    }

    @Deprecated
    public void setDividerInset(int i) {
        setDividerInsets(i, 0);
    }

    public void setDividerInsets(int i, int i2) {
        this.dividerInsetStart = i;
        this.dividerInsetEnd = i2;
        updateDivider();
    }

    @Deprecated
    public int getDividerInset() {
        return getDividerInsetStart();
    }

    public int getDividerInsetStart() {
        return this.dividerInsetStart;
    }

    public int getDividerInsetEnd() {
        return this.dividerInsetEnd;
    }

    public void removeDividerInset() {
        this.recyclerView.removeItemDecoration(this.dividerDecoration);
    }

    private void updateDivider() {
        if (this.templateLayout.isLayoutDirectionResolved()) {
            if (this.defaultDivider == null) {
                this.defaultDivider = this.dividerDecoration.getDivider();
            }
            InsetDrawable createRelativeInsetDrawable = DrawableLayoutDirectionHelper.createRelativeInsetDrawable(this.defaultDivider, this.dividerInsetStart, 0, this.dividerInsetEnd, 0, (View) this.templateLayout);
            this.divider = createRelativeInsetDrawable;
            this.dividerDecoration.setDivider(createRelativeInsetDrawable);
        }
    }

    public Drawable getDivider() {
        return this.divider;
    }

    public boolean hasDivider() {
        return this.isDividerDisplay;
    }

    public void setDividerItemDecoration(DividerItemDecoration dividerItemDecoration) {
        this.recyclerView.removeItemDecoration(this.dividerDecoration);
        this.dividerDecoration = dividerItemDecoration;
        this.recyclerView.addItemDecoration(dividerItemDecoration);
        updateDivider();
    }
}
