package androidx.slice.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.SliceItem;
import androidx.slice.SliceUtils;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class GridContent extends SliceContent {
    private boolean mAllImages;
    private boolean mIsLastIndex;
    private int mMaxCellLineCount;
    private SliceItem mPrimaryAction;
    private SliceItem mSeeMoreItem;
    private SliceItem mTitleItem;
    private final ArrayList<CellContent> mGridContent = new ArrayList<>();
    private int mLargestImageMode = 5;
    private IconCompat mFirstImage = null;
    private Point mFirstImageSize = null;

    public GridContent(SliceItem gridItem, int position) {
        super(gridItem, position);
        populate(gridItem);
    }

    private boolean populate(SliceItem gridItem) {
        List<SliceItem> items;
        SliceItem find = SliceQuery.find(gridItem, (String) null, "see_more", (String) null);
        this.mSeeMoreItem = find;
        if (find != null && "slice".equals(find.getFormat()) && (items = this.mSeeMoreItem.getSlice().getItems()) != null && items.size() > 0) {
            this.mSeeMoreItem = items.get(0);
        }
        this.mPrimaryAction = SliceQuery.find(gridItem, "slice", new String[]{"shortcut", "title"}, new String[]{"actions"});
        this.mAllImages = true;
        if ("slice".equals(gridItem.getFormat())) {
            List<SliceItem> filterAndProcessItems = filterAndProcessItems(gridItem.getSlice().getItems());
            for (int i = 0; i < filterAndProcessItems.size(); i++) {
                SliceItem sliceItem = filterAndProcessItems.get(i);
                if (!"content_description".equals(sliceItem.getSubType())) {
                    processContent(new CellContent(sliceItem));
                }
            }
        } else {
            processContent(new CellContent(gridItem));
        }
        return isValid();
    }

    private void processContent(CellContent cc) {
        int max;
        if (cc.isValid()) {
            if (this.mTitleItem == null && cc.getTitleItem() != null) {
                this.mTitleItem = cc.getTitleItem();
            }
            this.mGridContent.add(cc);
            if (!cc.isImageOnly()) {
                this.mAllImages = false;
            }
            this.mMaxCellLineCount = Math.max(this.mMaxCellLineCount, cc.getTextCount());
            if (this.mFirstImage == null && cc.hasImage()) {
                this.mFirstImage = cc.getImageIcon();
            }
            int i = this.mLargestImageMode;
            if (i == 5) {
                max = cc.getImageMode();
            } else {
                max = Math.max(i, cc.getImageMode());
            }
            this.mLargestImageMode = max;
        }
    }

    public ArrayList<CellContent> getGridContent() {
        return this.mGridContent;
    }

    public SliceItem getContentIntent() {
        return this.mPrimaryAction;
    }

    public SliceItem getSeeMoreItem() {
        return this.mSeeMoreItem;
    }

    @Override // androidx.slice.widget.SliceContent
    public boolean isValid() {
        return super.isValid() && this.mGridContent.size() > 0;
    }

    public boolean isAllImages() {
        return this.mAllImages;
    }

    public int getLargestImageMode() {
        return this.mLargestImageMode;
    }

    public Point getFirstImageSize(Context context) {
        IconCompat iconCompat = this.mFirstImage;
        if (iconCompat == null) {
            return new Point(-1, -1);
        }
        if (this.mFirstImageSize == null) {
            Drawable loadDrawable = iconCompat.loadDrawable(context);
            this.mFirstImageSize = new Point(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
        }
        return this.mFirstImageSize;
    }

    private List<SliceItem> filterAndProcessItems(List<SliceItem> items) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            SliceItem sliceItem = items.get(i);
            boolean z = true;
            if (!(SliceQuery.find(sliceItem, (String) null, "see_more", (String) null) != null) && !sliceItem.hasAnyHints("shortcut", "see_more", "keywords", "ttl", "last_updated", "overlay")) {
                z = false;
            }
            if ("content_description".equals(sliceItem.getSubType())) {
                this.mContentDescr = sliceItem;
            } else if (!z) {
                arrayList.add(sliceItem);
            }
        }
        return arrayList;
    }

    public int getMaxCellLineCount() {
        return this.mMaxCellLineCount;
    }

    public boolean hasImage() {
        return this.mFirstImage != null;
    }

    public boolean getIsLastIndex() {
        return this.mIsLastIndex;
    }

    public void setIsLastIndex(boolean isLast) {
        this.mIsLastIndex = isLast;
    }

    @Override // androidx.slice.widget.SliceContent
    public int getHeight(SliceStyle style, SliceViewPolicy policy) {
        return style.getGridHeight(this, policy);
    }

    /* loaded from: classes.dex */
    public static class CellContent {
        private SliceItem mContentDescr;
        private SliceItem mContentIntent;
        private IconCompat mImage;
        private int mImageCount;
        private SliceItem mOverlayItem;
        private SliceItem mPicker;
        private int mTextCount;
        private SliceItem mTitleItem;
        private SliceItem mToggleItem;
        private final ArrayList<SliceItem> mCellItems = new ArrayList<>();
        private int mImageMode = -1;

        public CellContent(SliceItem cellItem) {
            populate(cellItem);
        }

        public boolean populate(SliceItem cellItem) {
            String format = cellItem.getFormat();
            if (!cellItem.hasHint("shortcut") && ("slice".equals(format) || "action".equals(format))) {
                List<SliceItem> items = cellItem.getSlice().getItems();
                List<SliceItem> list = null;
                Iterator<SliceItem> it = items.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SliceItem next = it.next();
                    if ("action".equals(next.getFormat()) || "slice".equals(next.getFormat())) {
                        if (!"date_picker".equals(next.getSubType()) && !"time_picker".equals(next.getSubType())) {
                            list = next.getSlice().getItems();
                            if (new SliceActionImpl(next).isToggle()) {
                                this.mToggleItem = next;
                            } else {
                                this.mContentIntent = items.get(0);
                            }
                        }
                    }
                }
                if ("action".equals(format)) {
                    this.mContentIntent = cellItem;
                }
                this.mTextCount = 0;
                this.mImageCount = 0;
                fillCellItems(items);
                if (this.mTextCount == 0 && this.mImageCount == 0 && list != null) {
                    fillCellItems(list);
                }
            } else if (isValidCellContent(cellItem)) {
                this.mCellItems.add(cellItem);
            }
            return isValid();
        }

        private void fillCellItems(List<SliceItem> items) {
            for (int i = 0; i < items.size(); i++) {
                SliceItem sliceItem = items.get(i);
                String format = sliceItem.getFormat();
                if (this.mPicker == null && ("date_picker".equals(sliceItem.getSubType()) || "time_picker".equals(sliceItem.getSubType()))) {
                    this.mPicker = sliceItem;
                } else if ("content_description".equals(sliceItem.getSubType())) {
                    this.mContentDescr = sliceItem;
                } else if (this.mTextCount < 2 && ("text".equals(format) || "long".equals(format))) {
                    SliceItem sliceItem2 = this.mTitleItem;
                    if (sliceItem2 == null || (!sliceItem2.hasHint("title") && sliceItem.hasHint("title"))) {
                        this.mTitleItem = sliceItem;
                    }
                    if (sliceItem.hasHint("overlay")) {
                        if (this.mOverlayItem == null) {
                            this.mOverlayItem = sliceItem;
                        }
                    } else {
                        this.mTextCount++;
                        this.mCellItems.add(sliceItem);
                    }
                } else if (this.mImageCount < 1 && "image".equals(sliceItem.getFormat())) {
                    this.mImageMode = SliceUtils.parseImageMode(sliceItem);
                    this.mImageCount++;
                    this.mImage = sliceItem.getIcon();
                    this.mCellItems.add(sliceItem);
                }
            }
        }

        public SliceItem getToggleItem() {
            return this.mToggleItem;
        }

        public SliceItem getTitleItem() {
            return this.mTitleItem;
        }

        public SliceItem getOverlayItem() {
            return this.mOverlayItem;
        }

        public SliceItem getContentIntent() {
            return this.mContentIntent;
        }

        public SliceItem getPicker() {
            return this.mPicker;
        }

        public ArrayList<SliceItem> getCellItems() {
            return this.mCellItems;
        }

        private boolean isValidCellContent(SliceItem cellItem) {
            String format = cellItem.getFormat();
            if (!("content_description".equals(cellItem.getSubType()) || cellItem.hasAnyHints("keywords", "ttl", "last_updated"))) {
                return "text".equals(format) || "long".equals(format) || "image".equals(format);
            }
            return false;
        }

        public boolean isValid() {
            return this.mPicker != null || (this.mCellItems.size() > 0 && this.mCellItems.size() <= 3);
        }

        public boolean isImageOnly() {
            return this.mCellItems.size() == 1 && "image".equals(this.mCellItems.get(0).getFormat());
        }

        public int getTextCount() {
            return this.mTextCount;
        }

        public boolean hasImage() {
            return this.mImage != null;
        }

        public int getImageMode() {
            return this.mImageMode;
        }

        public IconCompat getImageIcon() {
            return this.mImage;
        }

        public CharSequence getContentDescription() {
            SliceItem sliceItem = this.mContentDescr;
            if (sliceItem != null) {
                return sliceItem.getText();
            }
            return null;
        }
    }
}