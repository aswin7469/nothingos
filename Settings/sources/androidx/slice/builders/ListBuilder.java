package androidx.slice.builders;

import android.app.PendingIntent;
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
import java.util.Set;

public class ListBuilder extends TemplateSliceBuilder {
    private androidx.slice.builders.impl.ListBuilder mImpl;

    public static class RangeBuilder {
    }

    public ListBuilder(Context context, Uri uri, long j) {
        super(context, uri);
        this.mImpl.setTtl(j);
    }

    public Slice build() {
        return ((TemplateBuilderImpl) this.mImpl).build();
    }

    /* access modifiers changed from: package-private */
    public void setImpl(TemplateBuilderImpl templateBuilderImpl) {
        this.mImpl = (androidx.slice.builders.impl.ListBuilder) templateBuilderImpl;
    }

    public ListBuilder addRow(RowBuilder rowBuilder) {
        this.mImpl.addRow(rowBuilder);
        return this;
    }

    public ListBuilder setHeader(HeaderBuilder headerBuilder) {
        this.mImpl.setHeader(headerBuilder);
        return this;
    }

    public ListBuilder addAction(SliceAction sliceAction) {
        this.mImpl.addAction(sliceAction);
        return this;
    }

    public ListBuilder setAccentColor(int i) {
        this.mImpl.setColor(i);
        return this;
    }

    public ListBuilder setKeywords(Set<String> set) {
        this.mImpl.setKeywords(set);
        return this;
    }

    public ListBuilder setIsError(boolean z) {
        this.mImpl.setIsError(z);
        return this;
    }

    /* access modifiers changed from: protected */
    public TemplateBuilderImpl selectImpl() {
        SliceSpec sliceSpec = SliceSpecs.LIST_V2;
        if (checkCompatible(sliceSpec)) {
            return new ListBuilderImpl(getBuilder(), sliceSpec, getClock());
        }
        SliceSpec sliceSpec2 = SliceSpecs.LIST;
        if (checkCompatible(sliceSpec2)) {
            return new ListBuilderImpl(getBuilder(), sliceSpec2, getClock());
        }
        SliceSpec sliceSpec3 = SliceSpecs.BASIC;
        if (checkCompatible(sliceSpec3)) {
            return new ListBuilderBasicImpl(getBuilder(), sliceSpec3);
        }
        return null;
    }

    public ListBuilder addInputRange(InputRangeBuilder inputRangeBuilder) {
        this.mImpl.addInputRange(inputRangeBuilder);
        return this;
    }

    public static class InputRangeBuilder {
        private CharSequence mContentDescription;
        private final List<Object> mEndItems = new ArrayList();
        private final List<Boolean> mEndLoads = new ArrayList();
        private final List<Integer> mEndTypes = new ArrayList();
        private boolean mHasDefaultToggle;
        private PendingIntent mInputAction;
        private int mLayoutDirection = -1;
        private int mMax = 100;
        private int mMin = 0;
        private SliceAction mPrimaryAction;
        private CharSequence mSubtitle;
        private IconCompat mThumb;
        private CharSequence mTitle;
        private IconCompat mTitleIcon;
        private int mTitleImageMode;
        private boolean mTitleItemLoading;
        private int mValue = 0;
        private boolean mValueSet = false;

        public InputRangeBuilder setTitleItem(IconCompat iconCompat, int i) {
            return setTitleItem(iconCompat, i, false);
        }

        public InputRangeBuilder addEndItem(SliceAction sliceAction) {
            return addEndItem(sliceAction, false);
        }

        public InputRangeBuilder addEndItem(SliceAction sliceAction, boolean z) {
            if (!this.mHasDefaultToggle) {
                this.mEndItems.add(sliceAction);
                this.mEndTypes.add(2);
                this.mEndLoads.add(Boolean.valueOf(z));
                this.mHasDefaultToggle = sliceAction.getImpl().isDefaultToggle();
                return this;
            }
            throw new IllegalStateException("Only one non-custom toggle can be added in a single row. If you would like to include multiple toggles in a row, set a custom icon for each toggle.");
        }

        public InputRangeBuilder setTitleItem(IconCompat iconCompat, int i, boolean z) {
            this.mTitleIcon = iconCompat;
            this.mTitleImageMode = i;
            this.mTitleItemLoading = z;
            return this;
        }

        public InputRangeBuilder setMin(int i) {
            this.mMin = i;
            return this;
        }

        public InputRangeBuilder setMax(int i) {
            this.mMax = i;
            return this;
        }

        public InputRangeBuilder setValue(int i) {
            this.mValueSet = true;
            this.mValue = i;
            return this;
        }

        public InputRangeBuilder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        public InputRangeBuilder setSubtitle(CharSequence charSequence) {
            this.mSubtitle = charSequence;
            return this;
        }

        public InputRangeBuilder setInputAction(PendingIntent pendingIntent) {
            this.mInputAction = pendingIntent;
            return this;
        }

        public InputRangeBuilder setPrimaryAction(SliceAction sliceAction) {
            this.mPrimaryAction = sliceAction;
            return this;
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

        public List<Object> getEndItems() {
            return this.mEndItems;
        }

        public List<Integer> getEndTypes() {
            return this.mEndTypes;
        }

        public List<Boolean> getEndLoads() {
            return this.mEndLoads;
        }

        public int getMin() {
            return this.mMin;
        }

        public int getMax() {
            return this.mMax;
        }

        public int getValue() {
            return this.mValue;
        }

        public boolean isValueSet() {
            return this.mValueSet;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public CharSequence getSubtitle() {
            return this.mSubtitle;
        }

        public PendingIntent getInputAction() {
            return this.mInputAction;
        }

        public IconCompat getThumb() {
            return this.mThumb;
        }

        public SliceAction getPrimaryAction() {
            return this.mPrimaryAction;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        public int getLayoutDirection() {
            return this.mLayoutDirection;
        }
    }

    public static class RowBuilder {
        private CharSequence mContentDescription;
        private final List<Object> mEndItems = new ArrayList();
        private final List<Boolean> mEndLoads = new ArrayList();
        private final List<Integer> mEndTypes = new ArrayList();
        private boolean mHasDefaultToggle;
        private boolean mHasEndActionOrToggle;
        private boolean mHasEndImage;
        private boolean mIsEndOfSection;
        private int mLayoutDirection = -1;
        private SliceAction mPrimaryAction;
        private CharSequence mSubtitle;
        private boolean mSubtitleLoading;
        private long mTimeStamp = -1;
        private CharSequence mTitle;
        private SliceAction mTitleAction;
        private boolean mTitleActionLoading;
        private IconCompat mTitleIcon;
        private int mTitleImageMode;
        private boolean mTitleItemLoading;
        private boolean mTitleLoading;
        private final Uri mUri = null;

        public RowBuilder setTitleItem(IconCompat iconCompat, int i) {
            return setTitleItem(iconCompat, i, false);
        }

        public RowBuilder setTitleItem(IconCompat iconCompat, int i, boolean z) {
            this.mTitleAction = null;
            this.mTitleIcon = iconCompat;
            this.mTitleImageMode = i;
            this.mTitleItemLoading = z;
            return this;
        }

        public RowBuilder setTitleItem(SliceAction sliceAction) {
            return setTitleItem(sliceAction, false);
        }

        public RowBuilder setTitleItem(SliceAction sliceAction, boolean z) {
            this.mTitleAction = sliceAction;
            this.mTitleIcon = null;
            this.mTitleImageMode = 0;
            this.mTitleActionLoading = z;
            return this;
        }

        public RowBuilder setPrimaryAction(SliceAction sliceAction) {
            this.mPrimaryAction = sliceAction;
            return this;
        }

        public RowBuilder setTitle(CharSequence charSequence) {
            return setTitle(charSequence, false);
        }

        public RowBuilder setTitle(CharSequence charSequence, boolean z) {
            this.mTitle = charSequence;
            this.mTitleLoading = z;
            return this;
        }

        public RowBuilder setSubtitle(CharSequence charSequence) {
            return setSubtitle(charSequence, false);
        }

        public RowBuilder setSubtitle(CharSequence charSequence, boolean z) {
            this.mSubtitle = charSequence;
            this.mSubtitleLoading = z;
            return this;
        }

        public RowBuilder addEndItem(IconCompat iconCompat, int i) {
            return addEndItem(iconCompat, i, false);
        }

        public RowBuilder addEndItem(IconCompat iconCompat, int i, boolean z) {
            if (!this.mHasEndActionOrToggle) {
                this.mEndItems.add(new Pair(iconCompat, Integer.valueOf(i)));
                this.mEndTypes.add(1);
                this.mEndLoads.add(Boolean.valueOf(z));
                this.mHasEndImage = true;
                return this;
            }
            throw new IllegalArgumentException("Trying to add an icon to end items when anaction has already been added. End items cannot have a mixture of actions and icons.");
        }

        public RowBuilder addEndItem(SliceAction sliceAction) {
            return addEndItem(sliceAction, false);
        }

        public RowBuilder addEndItem(SliceAction sliceAction, boolean z) {
            if (this.mHasEndImage) {
                throw new IllegalArgumentException("Trying to add an action to end items when anicon has already been added. End items cannot have a mixture of actions and icons.");
            } else if (!this.mHasDefaultToggle) {
                this.mEndItems.add(sliceAction);
                this.mEndTypes.add(2);
                this.mEndLoads.add(Boolean.valueOf(z));
                this.mHasDefaultToggle = sliceAction.getImpl().isDefaultToggle();
                this.mHasEndActionOrToggle = true;
                return this;
            } else {
                throw new IllegalStateException("Only one non-custom toggle can be added in a single row. If you would like to include multiple toggles in a row, set a custom icon for each toggle.");
            }
        }

        public RowBuilder setContentDescription(CharSequence charSequence) {
            this.mContentDescription = charSequence;
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

    public static class HeaderBuilder {
        private CharSequence mContentDescription;
        private int mLayoutDirection;
        private SliceAction mPrimaryAction;
        private CharSequence mSubtitle;
        private boolean mSubtitleLoading;
        private CharSequence mSummary;
        private boolean mSummaryLoading;
        private CharSequence mTitle;
        private boolean mTitleLoading;
        private final Uri mUri = null;

        public HeaderBuilder setTitle(CharSequence charSequence) {
            return setTitle(charSequence, false);
        }

        public HeaderBuilder setTitle(CharSequence charSequence, boolean z) {
            this.mTitle = charSequence;
            this.mTitleLoading = z;
            return this;
        }

        public HeaderBuilder setSubtitle(CharSequence charSequence) {
            return setSubtitle(charSequence, false);
        }

        public HeaderBuilder setSubtitle(CharSequence charSequence, boolean z) {
            this.mSubtitle = charSequence;
            this.mSubtitleLoading = z;
            return this;
        }

        public HeaderBuilder setPrimaryAction(SliceAction sliceAction) {
            this.mPrimaryAction = sliceAction;
            return this;
        }

        public Uri getUri() {
            return this.mUri;
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

        public CharSequence getSummary() {
            return this.mSummary;
        }

        public boolean isSummaryLoading() {
            return this.mSummaryLoading;
        }

        public SliceAction getPrimaryAction() {
            return this.mPrimaryAction;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        public int getLayoutDirection() {
            return this.mLayoutDirection;
        }
    }
}
