package androidx.slice.builders.impl;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpec;
import androidx.slice.builders.GridRowBuilder;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SelectionBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.core.SliceHints;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.time.Duration;
import java.util.Set;

public class ListBuilderBasicImpl extends TemplateBuilderImpl implements ListBuilder {
    private Bundle mHostExtras;
    private IconCompat mIconCompat;
    boolean mIsError;
    private Set<String> mKeywords;
    private SliceAction mSliceAction;
    private CharSequence mSubtitle;
    private CharSequence mTitle;

    public void addAction(SliceAction sliceAction) {
    }

    public void setSeeMoreAction(PendingIntent pendingIntent) {
    }

    public void setSeeMoreRow(ListBuilder.RowBuilder rowBuilder) {
    }

    public ListBuilderBasicImpl(Slice.Builder builder, SliceSpec sliceSpec) {
        super(builder, sliceSpec);
    }

    public void addRow(ListBuilder.RowBuilder rowBuilder) {
        if (this.mTitle == null && rowBuilder.getTitle() != null) {
            this.mTitle = rowBuilder.getTitle();
        }
        if (this.mSubtitle == null && rowBuilder.getSubtitle() != null) {
            this.mSubtitle = rowBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && rowBuilder.getPrimaryAction() != null) {
            this.mSliceAction = rowBuilder.getPrimaryAction();
        }
        if (this.mSliceAction == null && rowBuilder.getTitleAction() != null) {
            this.mSliceAction = rowBuilder.getTitleAction();
        }
        if (this.mIconCompat == null && rowBuilder.getTitleIcon() != null) {
            this.mIconCompat = rowBuilder.getTitleIcon();
        }
    }

    public void addGridRow(GridRowBuilder gridRowBuilder) {
        for (GridRowBuilder.CellBuilder next : gridRowBuilder.getCells()) {
            if (this.mTitle == null) {
                if (next.getTitle() != null) {
                    this.mTitle = next.getTitle();
                } else if (next.getSubtitle() != null) {
                    this.mTitle = next.getSubtitle();
                } else if (next.getCellDescription() != null) {
                    this.mTitle = next.getCellDescription();
                }
            }
            if (this.mSubtitle == null && next.getSubtitle() != null) {
                this.mSubtitle = next.getSubtitle();
            }
            if (this.mTitle != null && this.mSubtitle != null) {
                break;
            }
        }
        if (this.mSliceAction == null && gridRowBuilder.getPrimaryAction() != null) {
            SliceAction primaryAction = gridRowBuilder.getPrimaryAction();
            this.mSliceAction = primaryAction;
            if (this.mTitle == null && primaryAction.getTitle() != null) {
                this.mTitle = this.mSliceAction.getTitle();
            }
        }
    }

    public void setHeader(ListBuilder.HeaderBuilder headerBuilder) {
        if (headerBuilder.getTitle() != null) {
            this.mTitle = headerBuilder.getTitle();
        }
        if (headerBuilder.getSubtitle() != null) {
            this.mSubtitle = headerBuilder.getSubtitle();
        }
        if (headerBuilder.getPrimaryAction() != null) {
            this.mSliceAction = headerBuilder.getPrimaryAction();
        }
    }

    public void addRating(ListBuilder.RatingBuilder ratingBuilder) {
        if (this.mTitle == null && ratingBuilder.getTitle() != null) {
            this.mTitle = ratingBuilder.getTitle();
        }
        if (this.mSubtitle == null && ratingBuilder.getSubtitle() != null) {
            this.mSubtitle = ratingBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && ratingBuilder.getPrimaryAction() != null) {
            this.mSliceAction = ratingBuilder.getPrimaryAction();
        }
    }

    public void addInputRange(ListBuilder.InputRangeBuilder inputRangeBuilder) {
        if (this.mTitle == null && inputRangeBuilder.getTitle() != null) {
            this.mTitle = inputRangeBuilder.getTitle();
        }
        if (this.mSubtitle == null && inputRangeBuilder.getSubtitle() != null) {
            this.mSubtitle = inputRangeBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && inputRangeBuilder.getPrimaryAction() != null) {
            this.mSliceAction = inputRangeBuilder.getPrimaryAction();
        }
        if (this.mIconCompat == null && inputRangeBuilder.getThumb() != null) {
            this.mIconCompat = inputRangeBuilder.getThumb();
        }
    }

    public void addRange(ListBuilder.RangeBuilder rangeBuilder) {
        if (this.mTitle == null && rangeBuilder.getTitle() != null) {
            this.mTitle = rangeBuilder.getTitle();
        }
        if (this.mSubtitle == null && rangeBuilder.getSubtitle() != null) {
            this.mSubtitle = rangeBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && rangeBuilder.getPrimaryAction() != null) {
            this.mSliceAction = rangeBuilder.getPrimaryAction();
        }
    }

    public void addSelection(SelectionBuilder selectionBuilder) {
        if (this.mTitle == null && selectionBuilder.getTitle() != null) {
            this.mTitle = selectionBuilder.getTitle();
        }
        if (this.mSubtitle == null && selectionBuilder.getSubtitle() != null) {
            this.mSubtitle = selectionBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && selectionBuilder.getPrimaryAction() != null) {
            this.mSliceAction = selectionBuilder.getPrimaryAction();
        }
    }

    public void setColor(int i) {
        getBuilder().addInt(i, "color", new String[0]);
    }

    public void setKeywords(Set<String> set) {
        this.mKeywords = set;
    }

    public void setTtl(long j) {
        long j2 = -1;
        if (j != -1) {
            j2 = getClock().currentTimeMillis() + j;
        }
        getBuilder().addTimestamp(j2, SliceHints.SUBTYPE_MILLIS, "ttl");
    }

    public void setTtl(Duration duration) {
        setTtl(duration == null ? -1 : duration.toMillis());
    }

    public void setIsError(boolean z) {
        this.mIsError = z;
    }

    public void setLayoutDirection(int i) {
        getBuilder().addInt(i, "layout_direction", new String[0]);
    }

    public void setHostExtras(PersistableBundle persistableBundle) {
        this.mHostExtras = ConvertPersistableBundleApi21Impl.toBundle(persistableBundle);
    }

    private static class ConvertPersistableBundleApi21Impl {
        private ConvertPersistableBundleApi21Impl() {
        }

        static Bundle toBundle(PersistableBundle persistableBundle) {
            return new Bundle(persistableBundle);
        }
    }

    public void apply(Slice.Builder builder) {
        if (this.mIsError) {
            builder.addHints("error");
        }
        if (this.mKeywords != null) {
            Slice.Builder builder2 = new Slice.Builder(getBuilder());
            for (String addText : this.mKeywords) {
                builder2.addText((CharSequence) addText, (String) null, new String[0]);
            }
            builder.addSubSlice(builder2.addHints(BaseIconCache.IconDB.COLUMN_KEYWORDS).build());
        }
        Slice.Builder builder3 = new Slice.Builder(getBuilder());
        SliceAction sliceAction = this.mSliceAction;
        if (sliceAction != null) {
            if (this.mTitle == null && sliceAction.getTitle() != null) {
                this.mTitle = this.mSliceAction.getTitle();
            }
            if (this.mIconCompat == null && this.mSliceAction.getIcon() != null) {
                this.mIconCompat = this.mSliceAction.getIcon();
            }
            this.mSliceAction.setPrimaryAction(builder3);
        }
        if (this.mTitle != null) {
            builder3.addItem(new SliceItem((Object) this.mTitle, "text", (String) null, new String[]{"title"}));
        }
        if (this.mSubtitle != null) {
            builder3.addItem(new SliceItem((Object) this.mSubtitle, "text", (String) null, new String[0]));
        }
        IconCompat iconCompat = this.mIconCompat;
        if (iconCompat != null) {
            builder.addIcon(iconCompat, (String) null, "title");
        }
        if (this.mHostExtras != null) {
            builder3.addItem(new SliceItem((Object) this.mHostExtras, "bundle", SliceHints.SUBTYPE_HOST_EXTRAS, new String[0]));
        }
        builder.addSubSlice(builder3.build());
    }
}
