package androidx.slice.builders.impl;

import androidx.slice.Slice;
import androidx.slice.builders.SelectionBuilder;

public class SelectionBuilderBasicImpl extends SelectionBuilderImpl {
    public SelectionBuilderBasicImpl(Slice.Builder builder, SelectionBuilder selectionBuilder) {
        super(builder, selectionBuilder);
    }

    public void apply(Slice.Builder builder) {
        SelectionBuilder selectionBuilder = getSelectionBuilder();
        selectionBuilder.check();
        builder.addHints("list_item");
        selectionBuilder.getPrimaryAction().setPrimaryAction(builder);
        if (selectionBuilder.getTitle() != null) {
            builder.addText(selectionBuilder.getTitle(), (String) null, "title");
        }
        if (selectionBuilder.getSubtitle() != null) {
            builder.addText(selectionBuilder.getSubtitle(), (String) null, new String[0]);
        }
        if (selectionBuilder.getContentDescription() != null) {
            builder.addText(selectionBuilder.getContentDescription(), "content_description", new String[0]);
        }
        if (selectionBuilder.getLayoutDirection() != -1) {
            builder.addInt(selectionBuilder.getLayoutDirection(), "layout_direction", new String[0]);
        }
    }
}
