package androidx.slice.builders;

import android.content.Context;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Pair;
import androidx.slice.Slice;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.builders.impl.ListBuilderBasicImpl;
import androidx.slice.builders.impl.ListBuilderImpl;
import androidx.slice.builders.impl.TemplateBuilderImpl;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ListBuilder extends TemplateSliceBuilder {
    private androidx.slice.builders.impl.ListBuilder mImpl;

    public ListBuilder(Context context, Uri uri, long ttl) {
        super(context, uri);
        this.mImpl.setTtl(ttl);
    }

    public Slice build() {
        return ((TemplateBuilderImpl) this.mImpl).build();
    }

    @Override // androidx.slice.builders.TemplateSliceBuilder
    void setImpl(TemplateBuilderImpl impl) {
        this.mImpl = (androidx.slice.builders.impl.ListBuilder) impl;
    }

    public ListBuilder addRow(RowBuilder builder) {
        this.mImpl.addRow(builder);
        return this;
    }

    @Override // androidx.slice.builders.TemplateSliceBuilder
    protected TemplateBuilderImpl selectImpl() {
        SliceSpec sliceSpec = SliceSpecs.LIST_V2;
        if (checkCompatible(sliceSpec)) {
            return new ListBuilderImpl(getBuilder(), sliceSpec, getClock());
        }
        SliceSpec sliceSpec2 = SliceSpecs.LIST;
        if (checkCompatible(sliceSpec2)) {
            return new ListBuilderImpl(getBuilder(), sliceSpec2, getClock());
        }
        SliceSpec sliceSpec3 = SliceSpecs.BASIC;
        if (!checkCompatible(sliceSpec3)) {
            return null;
        }
        return new ListBuilderBasicImpl(getBuilder(), sliceSpec3);
    }

    /* loaded from: classes.dex */
    public static class RowBuilder {
        private CharSequence mContentDescription;
        private boolean mHasEndActionOrToggle;
        private boolean mHasEndImage;
        private boolean mIsEndOfSection;
        private SliceAction mPrimaryAction;
        private CharSequence mSubtitle;
        private boolean mSubtitleLoading;
        private CharSequence mTitle;
        private SliceAction mTitleAction;
        private boolean mTitleActionLoading;
        private IconCompat mTitleIcon;
        private int mTitleImageMode;
        private boolean mTitleItemLoading;
        private boolean mTitleLoading;
        private final Uri mUri;
        private long mTimeStamp = -1;
        private int mLayoutDirection = -1;
        private final List<Object> mEndItems = new ArrayList();
        private final List<Integer> mEndTypes = new ArrayList();
        private final List<Boolean> mEndLoads = new ArrayList();

        public RowBuilder(final Uri uri) {
            this.mUri = uri;
        }

        public RowBuilder setPrimaryAction(SliceAction action) {
            this.mPrimaryAction = action;
            return this;
        }

        public RowBuilder setTitle(CharSequence title) {
            return setTitle(title, false);
        }

        public RowBuilder setTitle(CharSequence title, boolean isLoading) {
            this.mTitle = title;
            this.mTitleLoading = isLoading;
            return this;
        }

        public RowBuilder addEndItem(IconCompat icon, int imageMode) {
            return addEndItem(icon, imageMode, false);
        }

        public RowBuilder addEndItem(IconCompat icon, int imageMode, boolean isLoading) {
            if (this.mHasEndActionOrToggle) {
                throw new IllegalArgumentException("Trying to add an icon to end items when anaction has already been added. End items cannot have a mixture of actions and icons.");
            }
            this.mEndItems.add(new Pair(icon, Integer.valueOf(imageMode)));
            this.mEndTypes.add(1);
            this.mEndLoads.add(Boolean.valueOf(isLoading));
            this.mHasEndImage = true;
            return this;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public boolean isEndOfSection() {
            return this.mIsEndOfSection;
        }

        public long getTimeStamp() {
            return this.mTimeStamp;
        }

        public boolean isTitleItemLoading() {
            return this.mTitleItemLoading;
        }

        public int getTitleImageMode() {
            return this.mTitleImageMode;
        }

        public IconCompat getTitleIcon() {
            return this.mTitleIcon;
        }

        public SliceAction getTitleAction() {
            return this.mTitleAction;
        }

        public SliceAction getPrimaryAction() {
            return this.mPrimaryAction;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public boolean isTitleLoading() {
            return this.mTitleLoading;
        }

        public CharSequence getSubtitle() {
            return this.mSubtitle;
        }

        public boolean isSubtitleLoading() {
            return this.mSubtitleLoading;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        public int getLayoutDirection() {
            return this.mLayoutDirection;
        }

        public List<Object> getEndItems() {
            return this.mEndItems;
        }

        public List<Integer> getEndTypes() {
            return this.mEndTypes;
        }

        public List<Boolean> getEndLoads() {
            return this.mEndLoads;
        }

        public boolean isTitleActionLoading() {
            return this.mTitleActionLoading;
        }
    }
}
