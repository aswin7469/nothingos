package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceUtils;
import androidx.slice.compat.SliceProviderCompat;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;

public class SliceContent {
    protected SliceItem mColorItem;
    protected SliceItem mContentDescr;
    protected SliceItem mLayoutDirItem;
    protected int mRowIndex;
    protected SliceItem mSliceItem;

    public int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        return 0;
    }

    public SliceContent(Slice slice) {
        if (slice != null) {
            init(new SliceItem((Object) slice, SliceProviderCompat.EXTRA_SLICE, (String) null, slice.getHints()));
            this.mRowIndex = -1;
        }
    }

    public SliceContent(SliceItem sliceItem, int i) {
        if (sliceItem != null) {
            init(sliceItem);
            this.mRowIndex = i;
        }
    }

    private void init(SliceItem sliceItem) {
        this.mSliceItem = sliceItem;
        if (SliceProviderCompat.EXTRA_SLICE.equals(sliceItem.getFormat()) || "action".equals(sliceItem.getFormat())) {
            this.mColorItem = SliceQuery.findTopLevelItem(sliceItem.getSlice(), "int", "color", (String[]) null, (String[]) null);
            this.mLayoutDirItem = SliceQuery.findTopLevelItem(sliceItem.getSlice(), "int", "layout_direction", (String[]) null, (String[]) null);
        }
        this.mContentDescr = SliceQuery.findSubtype(sliceItem, "text", "content_description");
    }

    public SliceItem getSliceItem() {
        return this.mSliceItem;
    }

    public int getAccentColor() {
        SliceItem sliceItem = this.mColorItem;
        if (sliceItem != null) {
            return sliceItem.getInt();
        }
        return -1;
    }

    public int getLayoutDir() {
        SliceItem sliceItem = this.mLayoutDirItem;
        if (sliceItem != null) {
            return SliceViewUtil.resolveLayoutDirection(sliceItem.getInt());
        }
        return -1;
    }

    public CharSequence getContentDescription() {
        SliceItem sliceItem = this.mContentDescr;
        if (sliceItem != null) {
            return sliceItem.getText();
        }
        return null;
    }

    public int getRowIndex() {
        return this.mRowIndex;
    }

    public boolean isValid() {
        return this.mSliceItem != null;
    }

    public SliceAction getShortcut(Context context) {
        SliceItem sliceItem;
        SliceItem sliceItem2;
        SliceItem sliceItem3;
        SliceItem sliceItem4;
        SliceItem sliceItem5 = this.mSliceItem;
        if (sliceItem5 == null) {
            return null;
        }
        SliceItem find = SliceQuery.find(sliceItem5, "action", new String[]{"title", "shortcut"}, (String[]) null);
        if (find != null) {
            sliceItem2 = SliceQuery.find(find, "image", "title", (String) null);
            String str = null;
            sliceItem = SliceQuery.find(find, "text", (String) null, (String) null);
        } else {
            sliceItem2 = null;
            sliceItem = null;
        }
        if (find == null) {
            String str2 = null;
            find = SliceQuery.find(this.mSliceItem, "action", (String) null, (String) null);
        }
        SliceItem sliceItem6 = find;
        if (sliceItem2 == null) {
            sliceItem2 = SliceQuery.find(this.mSliceItem, "image", "title", (String) null);
        }
        if (sliceItem == null) {
            sliceItem = SliceQuery.find(this.mSliceItem, "text", "title", (String) null);
        }
        if (sliceItem2 == null) {
            String str3 = null;
            sliceItem3 = SliceQuery.find(this.mSliceItem, "image", (String) null, (String) null);
        } else {
            sliceItem3 = sliceItem2;
        }
        if (sliceItem == null) {
            String str4 = null;
            sliceItem4 = SliceQuery.find(this.mSliceItem, "text", (String) null, (String) null);
        } else {
            sliceItem4 = sliceItem;
        }
        int parseImageMode = sliceItem3 != null ? SliceUtils.parseImageMode(sliceItem3) : 5;
        if (context != null) {
            return fallBackToAppData(context, sliceItem4, sliceItem3, parseImageMode, sliceItem6);
        }
        if (sliceItem3 == null || sliceItem6 == null || sliceItem4 == null) {
            return null;
        }
        return new SliceActionImpl(sliceItem6.getAction(), sliceItem3.getIcon(), parseImageMode, sliceItem4.getText());
    }

    private SliceAction fallBackToAppData(Context context, SliceItem sliceItem, SliceItem sliceItem2, int i, SliceItem sliceItem3) {
        Intent launchIntentForPackage;
        String str = null;
        SliceItem find = SliceQuery.find(this.mSliceItem, SliceProviderCompat.EXTRA_SLICE, (String) null, (String) null);
        if (find == null) {
            return null;
        }
        Uri uri = find.getSlice().getUri();
        IconCompat icon = sliceItem2 != null ? sliceItem2.getIcon() : null;
        CharSequence text = sliceItem != null ? sliceItem.getText() : null;
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(uri.getAuthority(), 0);
            ApplicationInfo applicationInfo = resolveContentProvider != null ? resolveContentProvider.applicationInfo : null;
            if (applicationInfo != null) {
                if (icon == null) {
                    icon = SliceViewUtil.createIconFromDrawable(packageManager.getApplicationIcon(applicationInfo));
                    i = 2;
                }
                if (text == null) {
                    text = packageManager.getApplicationLabel(applicationInfo);
                }
                if (sliceItem3 == null && (launchIntentForPackage = packageManager.getLaunchIntentForPackage(applicationInfo.packageName)) != null) {
                    sliceItem3 = new SliceItem(PendingIntent.getActivity(context, 0, launchIntentForPackage, 67108864), new Slice.Builder(uri).build(), "action", (String) null, new String[0]);
                }
            }
        }
        if (sliceItem3 == null) {
            sliceItem3 = new SliceItem(PendingIntent.getActivity(context, 0, new Intent(), 0), (Slice) null, "action", (String) null, (String[]) null);
        }
        if (text == null || icon == null || sliceItem3 == null) {
            return null;
        }
        return new SliceActionImpl(sliceItem3.getAction(), icon, i, text);
    }
}
