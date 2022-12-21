package androidx.slice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import androidx.core.math.MathUtils;
import androidx.core.util.Pair;
import androidx.slice.compat.SliceProviderCompat;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceHints;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.ListContent;
import androidx.slice.widget.RowContent;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SliceMetadata {
    public static final int LOADED_ALL = 2;
    public static final int LOADED_NONE = 0;
    public static final int LOADED_PARTIAL = 1;
    private Context mContext;
    private long mExpiry;
    private RowContent mHeaderContent;
    private final Bundle mHostExtras;
    private long mLastUpdated;
    private ListContent mListContent;
    private SliceAction mPrimaryAction;
    private Slice mSlice;
    private List<SliceAction> mSliceActions;
    private int mTemplateType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceLoadingState {
    }

    public static SliceMetadata from(Context context, Slice slice) {
        return new SliceMetadata(context, slice);
    }

    private SliceMetadata(Context context, Slice slice) {
        RowContent rowContent;
        this.mSlice = slice;
        this.mContext = context;
        SliceItem find = SliceQuery.find(slice, "long", "ttl", (String) null);
        if (find != null) {
            this.mExpiry = find.getLong();
        }
        SliceItem find2 = SliceQuery.find(slice, "long", "last_updated", (String) null);
        if (find2 != null) {
            this.mLastUpdated = find2.getLong();
        }
        SliceItem findSubtype = SliceQuery.findSubtype(slice, "bundle", SliceHints.SUBTYPE_HOST_EXTRAS);
        if (findSubtype == null || !(findSubtype.mObj instanceof Bundle)) {
            this.mHostExtras = Bundle.EMPTY;
        } else {
            this.mHostExtras = (Bundle) findSubtype.mObj;
        }
        ListContent listContent = new ListContent(slice);
        this.mListContent = listContent;
        this.mHeaderContent = listContent.getHeader();
        this.mTemplateType = this.mListContent.getHeaderTemplateType();
        this.mPrimaryAction = this.mListContent.getShortcut(this.mContext);
        List<SliceAction> sliceActions = this.mListContent.getSliceActions();
        this.mSliceActions = sliceActions;
        if (sliceActions == null && (rowContent = this.mHeaderContent) != null && SliceQuery.hasHints(rowContent.getSliceItem(), "list_item")) {
            ArrayList<SliceItem> endItems = this.mHeaderContent.getEndItems();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < endItems.size(); i++) {
                if (SliceQuery.find(endItems.get(i), "action") != null) {
                    arrayList.add(new SliceActionImpl(endItems.get(i)));
                }
            }
            if (arrayList.size() > 0) {
                this.mSliceActions = arrayList;
            }
        }
    }

    public CharSequence getTitle() {
        SliceAction sliceAction;
        RowContent rowContent = this.mHeaderContent;
        CharSequence text = (rowContent == null || rowContent.getTitleItem() == null) ? null : this.mHeaderContent.getTitleItem().getText();
        return (!TextUtils.isEmpty(text) || (sliceAction = this.mPrimaryAction) == null) ? text : sliceAction.getTitle();
    }

    public CharSequence getSubtitle() {
        RowContent rowContent = this.mHeaderContent;
        if (rowContent == null || rowContent.getSubtitleItem() == null) {
            return null;
        }
        return this.mHeaderContent.getSubtitleItem().getText();
    }

    public CharSequence getSummary() {
        RowContent rowContent = this.mHeaderContent;
        if (rowContent == null || rowContent.getSummaryItem() == null) {
            return null;
        }
        return this.mHeaderContent.getSummaryItem().getText();
    }

    public List<SliceAction> getSliceActions() {
        return this.mSliceActions;
    }

    public SliceAction getPrimaryAction() {
        return this.mPrimaryAction;
    }

    public int getHeaderType() {
        return this.mTemplateType;
    }

    public boolean hasLargeMode() {
        return this.mListContent.getRowItems().size() > 1;
    }

    public List<SliceAction> getToggles() {
        ArrayList arrayList = new ArrayList();
        SliceAction sliceAction = this.mPrimaryAction;
        if (sliceAction == null || !sliceAction.isToggle()) {
            List<SliceAction> list = this.mSliceActions;
            if (list == null || list.size() <= 0) {
                RowContent rowContent = this.mHeaderContent;
                if (rowContent != null) {
                    arrayList.addAll(rowContent.getToggleItems());
                }
            } else {
                for (int i = 0; i < this.mSliceActions.size(); i++) {
                    SliceAction sliceAction2 = this.mSliceActions.get(i);
                    if (sliceAction2.isToggle()) {
                        arrayList.add(sliceAction2);
                    }
                }
            }
        } else {
            arrayList.add(this.mPrimaryAction);
        }
        return arrayList;
    }

    public Bundle getHostExtras() {
        return this.mHostExtras;
    }

    public boolean sendToggleAction(SliceAction sliceAction, boolean z) throws PendingIntent.CanceledException {
        if (sliceAction == null) {
            return false;
        }
        Intent putExtra = new Intent().addFlags(268435456).putExtra("android.app.slice.extra.TOGGLE_STATE", z);
        if (this.mContext == null) {
            return true;
        }
        sliceAction.getAction().send(this.mContext, 0, putExtra, (PendingIntent.OnFinished) null, (Handler) null);
        return true;
    }

    public PendingIntent getInputRangeAction() {
        SliceItem range;
        if (this.mTemplateType != 4 || (range = this.mHeaderContent.getRange()) == null) {
            return null;
        }
        return range.getAction();
    }

    public boolean sendInputRangeAction(int i) throws PendingIntent.CanceledException {
        SliceItem range;
        if (this.mTemplateType != 4 || (range = this.mHeaderContent.getRange()) == null) {
            return false;
        }
        Pair<Integer, Integer> range2 = getRange();
        range.fireAction(this.mContext, new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", MathUtils.clamp(i, ((Integer) range2.first).intValue(), ((Integer) range2.second).intValue())));
        return true;
    }

    public Pair<Integer, Integer> getRange() {
        int i = this.mTemplateType;
        if (i != 4 && i != 5) {
            return null;
        }
        SliceItem range = this.mHeaderContent.getRange();
        SliceItem findSubtype = SliceQuery.findSubtype(range, "int", "max");
        SliceItem findSubtype2 = SliceQuery.findSubtype(range, "int", SliceHints.SUBTYPE_MIN);
        return new Pair<>(Integer.valueOf(findSubtype2 != null ? findSubtype2.getInt() : 0), Integer.valueOf(findSubtype != null ? findSubtype.getInt() : 100));
    }

    public int getRangeValue() {
        SliceItem findSubtype;
        int i = this.mTemplateType;
        if ((i == 4 || i == 5) && (findSubtype = SliceQuery.findSubtype(this.mHeaderContent.getRange(), "int", "value")) != null) {
            return findSubtype.getInt();
        }
        return -1;
    }

    public boolean isSelection() {
        return this.mTemplateType == 6;
    }

    public List<String> getSliceKeywords() {
        List<SliceItem> findAll;
        SliceItem find = SliceQuery.find(this.mSlice, SliceProviderCompat.EXTRA_SLICE, BaseIconCache.IconDB.COLUMN_KEYWORDS, (String) null);
        if (find == null || (findAll = SliceQuery.findAll(find, "text")) == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < findAll.size(); i++) {
            String str = (String) findAll.get(i).getText();
            if (!TextUtils.isEmpty(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public int getLoadingState() {
        boolean z = SliceQuery.find(this.mSlice, (String) null, "partial", (String) null) != null;
        if (!this.mListContent.isValid()) {
            return 0;
        }
        if (z) {
            return 1;
        }
        return 2;
    }

    public long getExpiry() {
        return this.mExpiry;
    }

    public long getLastUpdatedTime() {
        return this.mLastUpdated;
    }

    public boolean isPermissionSlice() {
        return this.mSlice.hasHint("permission_request");
    }

    public boolean isErrorSlice() {
        return this.mSlice.hasHint("error");
    }

    public boolean isCachedSlice() {
        return this.mSlice.hasHint(SliceHints.HINT_CACHED);
    }

    public static List<SliceAction> getSliceActions(Slice slice) {
        SliceItem find = SliceQuery.find(slice, SliceProviderCompat.EXTRA_SLICE, "actions", (String) null);
        List<SliceItem> findAll = find != null ? SliceQuery.findAll(find, SliceProviderCompat.EXTRA_SLICE, new String[]{"actions", "shortcut"}, (String[]) null) : null;
        if (findAll == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(findAll.size());
        for (int i = 0; i < findAll.size(); i++) {
            arrayList.add(new SliceActionImpl(findAll.get(i)));
        }
        return arrayList;
    }

    public boolean isExpired() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mExpiry;
        return (j == 0 || j == -1 || currentTimeMillis <= j) ? false : true;
    }

    public boolean neverExpires() {
        return this.mExpiry == -1;
    }

    public long getTimeToExpiry() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mExpiry;
        if (j == 0 || j == -1 || currentTimeMillis > j) {
            return 0;
        }
        return j - currentTimeMillis;
    }

    public ListContent getListContent() {
        return this.mListContent;
    }
}
