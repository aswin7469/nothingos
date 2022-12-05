package androidx.slice.builders.impl;

import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Pair;
import androidx.slice.Clock;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpec;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class ListBuilderImpl extends TemplateBuilderImpl implements ListBuilder {
    private boolean mFirstRowChecked;
    private boolean mFirstRowHasText;
    private Bundle mHostExtras;
    private boolean mIsError;
    private boolean mIsFirstRowTypeValid;
    private Set<String> mKeywords;
    private List<Slice> mSliceActions;
    private Slice mSliceHeader;

    public ListBuilderImpl(final Slice.Builder b, final SliceSpec spec, final Clock clock) {
        super(b, spec, clock);
    }

    @Override // androidx.slice.builders.impl.TemplateBuilderImpl
    public void apply(final Slice.Builder builder) {
        builder.addLong(getClock().currentTimeMillis(), "millis", "last_updated");
        Slice slice = this.mSliceHeader;
        if (slice != null) {
            builder.addSubSlice(slice);
        }
        if (this.mSliceActions != null) {
            Slice.Builder builder2 = new Slice.Builder(builder);
            for (int i = 0; i < this.mSliceActions.size(); i++) {
                builder2.addSubSlice(this.mSliceActions.get(i));
            }
            builder.addSubSlice(builder2.addHints("actions").build());
        }
        if (this.mIsError) {
            builder.addHints("error");
        }
        if (this.mKeywords != null) {
            Slice.Builder builder3 = new Slice.Builder(getBuilder());
            for (String str : this.mKeywords) {
                builder3.addText(str, (String) null, new String[0]);
            }
            getBuilder().addSubSlice(builder3.addHints("keywords").build());
        }
        Bundle bundle = this.mHostExtras;
        if (bundle != null) {
            builder.addItem(new SliceItem(bundle, "bundle", "host_extras", new String[0]));
        }
    }

    @Override // androidx.slice.builders.impl.TemplateBuilderImpl
    public Slice build() {
        Slice build = super.build();
        boolean z = true;
        boolean z2 = SliceQuery.find(build, (String) null, "partial", (String) null) != null;
        if (SliceQuery.find(build, "slice", "list_item", (String) null) != null) {
            z = false;
        }
        String[] strArr = {"shortcut", "title"};
        SliceItem find = SliceQuery.find(build, "action", strArr, (String[]) null);
        List<SliceItem> findAll = SliceQuery.findAll(build, "slice", strArr, (String[]) null);
        if (!z2 && !z && find == null && (findAll == null || findAll.isEmpty())) {
            throw new IllegalStateException("A slice requires a primary action; ensure one of your builders has called #setPrimaryAction with a valid SliceAction.");
        }
        boolean z3 = this.mFirstRowChecked;
        if (z3 && !this.mIsFirstRowTypeValid) {
            throw new IllegalStateException("A slice cannot have the first row be constructed from a GridRowBuilder, consider using #setHeader.");
        }
        if (z3 && !this.mFirstRowHasText) {
            throw new IllegalStateException("A slice requires the first row to have some text.");
        }
        return build;
    }

    @Override // androidx.slice.builders.impl.ListBuilder
    public void addRow(ListBuilder.RowBuilder builder) {
        RowBuilderImpl rowBuilderImpl = new RowBuilderImpl(createChildBuilder());
        rowBuilderImpl.fillFrom(builder);
        checkRow(true, rowBuilderImpl.hasText());
        addRow(rowBuilderImpl);
    }

    public void addRow(RowBuilderImpl builder) {
        checkRow(true, builder.hasText());
        builder.getBuilder().addHints("list_item");
        if (builder.isEndOfSection()) {
            builder.getBuilder().addHints("end_of_section");
        }
        getBuilder().addSubSlice(builder.build());
    }

    @Override // androidx.slice.builders.impl.ListBuilder
    public void setTtl(long ttl) {
        long j = -1;
        if (ttl != -1) {
            j = getClock().currentTimeMillis() + ttl;
        }
        getBuilder().addTimestamp(j, "millis", "ttl");
    }

    private void checkRow(boolean isTypeValid, boolean hasText) {
        if (!this.mFirstRowChecked) {
            this.mFirstRowChecked = true;
            this.mIsFirstRowTypeValid = isTypeValid;
            this.mFirstRowHasText = hasText;
        }
    }

    /* loaded from: classes.dex */
    public static class RowBuilderImpl extends TemplateBuilderImpl {
        private CharSequence mContentDescr;
        private final ArrayList<Slice> mEndItems = new ArrayList<>();
        private boolean mIsEndOfSection;
        private SliceAction mPrimaryAction;
        private Slice mStartItem;
        private SliceItem mSubtitleItem;
        private SliceItem mTitleItem;

        RowBuilderImpl(Slice.Builder builder) {
            super(builder, null);
        }

        void fillFrom(ListBuilder.RowBuilder builder) {
            if (builder.getUri() != null) {
                setBuilder(new Slice.Builder(builder.getUri()));
            }
            setPrimaryAction(builder.getPrimaryAction());
            this.mIsEndOfSection = builder.isEndOfSection();
            if (builder.getLayoutDirection() != -1) {
                setLayoutDirection(builder.getLayoutDirection());
            }
            if (builder.getTitleAction() != null || builder.isTitleActionLoading()) {
                setTitleItem(builder.getTitleAction(), builder.isTitleActionLoading());
            } else if (builder.getTitleIcon() != null || builder.isTitleItemLoading()) {
                setTitleItem(builder.getTitleIcon(), builder.getTitleImageMode(), builder.isTitleItemLoading());
            } else if (builder.getTimeStamp() != -1) {
                setTitleItem(builder.getTimeStamp());
            }
            if (builder.getTitle() != null || builder.isTitleLoading()) {
                setTitle(builder.getTitle(), builder.isTitleLoading());
            }
            if (builder.getSubtitle() != null || builder.isSubtitleLoading()) {
                setSubtitle(builder.getSubtitle(), builder.isSubtitleLoading());
            }
            if (builder.getContentDescription() != null) {
                setContentDescription(builder.getContentDescription());
            }
            List<Object> endItems = builder.getEndItems();
            List<Integer> endTypes = builder.getEndTypes();
            List<Boolean> endLoads = builder.getEndLoads();
            for (int i = 0; i < endItems.size(); i++) {
                int intValue = endTypes.get(i).intValue();
                if (intValue == 0) {
                    addEndItem(((Long) endItems.get(i)).longValue());
                } else if (intValue == 1) {
                    Pair pair = (Pair) endItems.get(i);
                    addEndItem((IconCompat) pair.first, ((Integer) pair.second).intValue(), endLoads.get(i).booleanValue());
                } else if (intValue == 2) {
                    addEndItem((SliceAction) endItems.get(i), endLoads.get(i).booleanValue());
                }
            }
        }

        private void setTitleItem(long timeStamp) {
            this.mStartItem = new Slice.Builder(getBuilder()).addTimestamp(timeStamp, null, new String[0]).addHints("title").build();
        }

        private void setTitleItem(final IconCompat icon, final int imageMode, final boolean isLoading) {
            Slice.Builder addIcon = new Slice.Builder(getBuilder()).addIcon(icon, (String) null, parseImageMode(imageMode, isLoading));
            if (isLoading) {
                addIcon.addHints("partial");
            }
            this.mStartItem = addIcon.addHints("title").build();
        }

        private void setTitleItem(final SliceAction action, final boolean isLoading) {
            Slice.Builder addHints = new Slice.Builder(getBuilder()).addHints("title");
            if (isLoading) {
                addHints.addHints("partial");
            }
            this.mStartItem = action.buildSlice(addHints);
        }

        private void setPrimaryAction(SliceAction action) {
            this.mPrimaryAction = action;
        }

        private void setTitle(final CharSequence title, final boolean isLoading) {
            SliceItem sliceItem = new SliceItem(title, "text", (String) null, new String[]{"title"});
            this.mTitleItem = sliceItem;
            if (isLoading) {
                sliceItem.addHint("partial");
            }
        }

        private void setSubtitle(final CharSequence subtitle, final boolean isLoading) {
            SliceItem sliceItem = new SliceItem(subtitle, "text", (String) null, new String[0]);
            this.mSubtitleItem = sliceItem;
            if (isLoading) {
                sliceItem.addHint("partial");
            }
        }

        protected void addEndItem(long timeStamp) {
            this.mEndItems.add(new Slice.Builder(getBuilder()).addTimestamp(timeStamp, null, new String[0]).build());
        }

        private void addEndItem(final IconCompat icon, final int imageMode, final boolean isLoading) {
            Slice.Builder addIcon = new Slice.Builder(getBuilder()).addIcon(icon, (String) null, parseImageMode(imageMode, isLoading));
            if (isLoading) {
                addIcon.addHints("partial");
            }
            this.mEndItems.add(addIcon.build());
        }

        private void addEndItem(final SliceAction action, final boolean isLoading) {
            Slice.Builder builder = new Slice.Builder(getBuilder());
            if (isLoading) {
                builder.addHints("partial");
            }
            this.mEndItems.add(action.buildSlice(builder));
        }

        private void setContentDescription(CharSequence description) {
            this.mContentDescr = description;
        }

        private void setLayoutDirection(int layoutDirection) {
            getBuilder().addInt(layoutDirection, "layout_direction", new String[0]);
        }

        public boolean isEndOfSection() {
            return this.mIsEndOfSection;
        }

        boolean hasText() {
            return (this.mTitleItem == null && this.mSubtitleItem == null) ? false : true;
        }

        @Override // androidx.slice.builders.impl.TemplateBuilderImpl
        public void apply(Slice.Builder b) {
            Slice slice = this.mStartItem;
            if (slice != null) {
                b.addSubSlice(slice);
            }
            SliceItem sliceItem = this.mTitleItem;
            if (sliceItem != null) {
                b.addItem(sliceItem);
            }
            SliceItem sliceItem2 = this.mSubtitleItem;
            if (sliceItem2 != null) {
                b.addItem(sliceItem2);
            }
            for (int i = 0; i < this.mEndItems.size(); i++) {
                b.addSubSlice(this.mEndItems.get(i));
            }
            CharSequence charSequence = this.mContentDescr;
            if (charSequence != null) {
                b.addText(charSequence, "content_description", new String[0]);
            }
            SliceAction sliceAction = this.mPrimaryAction;
            if (sliceAction != null) {
                sliceAction.setPrimaryAction(b);
            }
        }
    }
}
