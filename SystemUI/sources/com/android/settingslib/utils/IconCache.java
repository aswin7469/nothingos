package com.android.settingslib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import androidx.collection.ArrayMap;

public class IconCache {
    private final Context mContext;
    final ArrayMap<Icon, Drawable> mMap = new ArrayMap<>();

    public IconCache(Context context) {
        this.mContext = context;
    }

    public Drawable getIcon(Icon icon) {
        if (icon == null) {
            return null;
        }
        Drawable drawable = this.mMap.get(icon);
        if (drawable != null) {
            return drawable;
        }
        Drawable loadDrawable = icon.loadDrawable(this.mContext);
        updateIcon(icon, loadDrawable);
        return loadDrawable;
    }

    public void updateIcon(Icon icon, Drawable drawable) {
        this.mMap.put(icon, drawable);
    }
}
