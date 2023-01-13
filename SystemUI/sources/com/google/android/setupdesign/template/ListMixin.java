package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.items.ItemAdapter;
import com.google.android.setupdesign.items.ItemGroup;
import com.google.android.setupdesign.items.ItemInflater;
import com.google.android.setupdesign.util.DrawableLayoutDirectionHelper;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class ListMixin implements Mixin {
    private Drawable defaultDivider;
    private Drawable divider;
    private int dividerInsetEnd;
    private int dividerInsetStart;
    private ListView listView;
    private final TemplateLayout templateLayout;

    public ListMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        Context context = templateLayout2.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3963R.styleable.SudListMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C3963R.styleable.SudListMixin_android_entries, 0);
        if (resourceId != 0) {
            setAdapter(new ItemAdapter((ItemGroup) new ItemInflater(context).inflate(resourceId)));
        }
        if (isDividerShown(context, obtainStyledAttributes.getBoolean(C3963R.styleable.SudListMixin_sudDividerShown, true))) {
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudListMixin_sudDividerInset, -1);
            if (dimensionPixelSize != -1) {
                setDividerInset(dimensionPixelSize);
            } else {
                int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudListMixin_sudDividerInsetStart, 0);
                int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(C3963R.styleable.SudListMixin_sudDividerInsetEnd, 0);
                if (PartnerStyleHelper.shouldApplyPartnerResource((View) templateLayout2)) {
                    dimensionPixelSize2 = PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_START) ? (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_START) : dimensionPixelSize2;
                    if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_LAYOUT_MARGIN_END)) {
                        dimensionPixelSize3 = (int) PartnerConfigHelper.get(context).getDimension(context, PartnerConfig.CONFIG_LAYOUT_MARGIN_END);
                    }
                }
                setDividerInsets(dimensionPixelSize2, dimensionPixelSize3);
            }
        } else {
            getListView().setDivider((Drawable) null);
        }
        obtainStyledAttributes.recycle();
    }

    private boolean isDividerShown(Context context, boolean z) {
        return (!PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout) || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN)) ? z : PartnerConfigHelper.get(context).getBoolean(context, PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN, true);
    }

    public ListView getListView() {
        return getListViewInternal();
    }

    private ListView getListViewInternal() {
        if (this.listView == null) {
            View findManagedViewById = this.templateLayout.findManagedViewById(16908298);
            if (findManagedViewById instanceof ListView) {
                this.listView = (ListView) findManagedViewById;
            }
        }
        return this.listView;
    }

    public void onLayout() {
        if (this.divider == null) {
            updateDivider();
        }
    }

    public ListAdapter getAdapter() {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal == null) {
            return null;
        }
        ListAdapter adapter = listViewInternal.getAdapter();
        return adapter instanceof HeaderViewListAdapter ? ((HeaderViewListAdapter) adapter).getWrappedAdapter() : adapter;
    }

    public void setAdapter(ListAdapter listAdapter) {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal != null) {
            listViewInternal.setAdapter(listAdapter);
        }
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

    private void updateDivider() {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal != null && this.templateLayout.isLayoutDirectionResolved()) {
            if (this.defaultDivider == null) {
                this.defaultDivider = listViewInternal.getDivider();
            }
            Drawable drawable = this.defaultDivider;
            if (drawable != null) {
                InsetDrawable createRelativeInsetDrawable = DrawableLayoutDirectionHelper.createRelativeInsetDrawable(drawable, this.dividerInsetStart, 0, this.dividerInsetEnd, 0, (View) this.templateLayout);
                this.divider = createRelativeInsetDrawable;
                listViewInternal.setDivider(createRelativeInsetDrawable);
            }
        }
    }

    public Drawable getDivider() {
        return this.divider;
    }
}
