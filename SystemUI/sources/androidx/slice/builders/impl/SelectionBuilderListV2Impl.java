package androidx.slice.builders.impl;

import androidx.core.util.Pair;
import androidx.slice.Slice;
import androidx.slice.builders.SelectionBuilder;
import androidx.slice.core.SliceHints;

public class SelectionBuilderListV2Impl extends SelectionBuilderImpl {
    public SelectionBuilderListV2Impl(Slice.Builder builder, SelectionBuilder selectionBuilder) {
        super(builder, selectionBuilder);
    }

    public void apply(Slice.Builder builder) {
        Slice.Builder builder2 = new Slice.Builder(builder);
        SelectionBuilder selectionBuilder = getSelectionBuilder();
        selectionBuilder.check();
        if (selectionBuilder.getTitle() != null) {
            builder2.addText(selectionBuilder.getTitle(), (String) null, "title");
        }
        if (selectionBuilder.getSubtitle() != null) {
            builder2.addText(selectionBuilder.getSubtitle(), (String) null, new String[0]);
        }
        if (selectionBuilder.getContentDescription() != null) {
            builder2.addText(selectionBuilder.getContentDescription(), "content_description", new String[0]);
        }
        if (selectionBuilder.getLayoutDirection() != -1) {
            builder2.addInt(selectionBuilder.getLayoutDirection(), "layout_direction", new String[0]);
        }
        for (Pair next : selectionBuilder.getOptions()) {
            Slice.Builder builder3 = new Slice.Builder(builder);
            if (((String) next.first).equals(selectionBuilder.getSelectedOption())) {
                builder3.addHints("selected");
            }
            builder3.addText((CharSequence) next.first, SliceHints.SUBTYPE_SELECTION_OPTION_KEY, new String[0]);
            builder3.addText((CharSequence) next.second, SliceHints.SUBTYPE_SELECTION_OPTION_VALUE, new String[0]);
            builder3.addHints(SliceHints.HINT_SELECTION_OPTION);
            builder2.addSubSlice(builder3.build());
        }
        selectionBuilder.getPrimaryAction().setPrimaryAction(builder2);
        builder.addAction(selectionBuilder.getInputAction(), builder2.build(), SliceHints.SUBTYPE_SELECTION);
        builder.addHints("list_item");
    }
}
