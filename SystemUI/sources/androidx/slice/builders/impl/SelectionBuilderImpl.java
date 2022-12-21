package androidx.slice.builders.impl;

import androidx.slice.Slice;
import androidx.slice.SliceSpec;
import androidx.slice.builders.SelectionBuilder;

public abstract class SelectionBuilderImpl extends TemplateBuilderImpl {
    private final SelectionBuilder mSelectionBuilder;

    public abstract void apply(Slice.Builder builder);

    public SelectionBuilderImpl(Slice.Builder builder, SelectionBuilder selectionBuilder) {
        super(builder, (SliceSpec) null);
        this.mSelectionBuilder = selectionBuilder;
    }

    /* access modifiers changed from: protected */
    public SelectionBuilder getSelectionBuilder() {
        return this.mSelectionBuilder;
    }
}
