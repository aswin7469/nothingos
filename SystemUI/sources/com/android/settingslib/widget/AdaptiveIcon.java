package com.android.settingslib.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import com.android.settingslib.drawer.Tile;

public class AdaptiveIcon extends LayerDrawable {
    private static final String TAG = "AdaptiveHomepageIcon";
    private AdaptiveConstantState mAdaptiveConstantState;
    int mBackgroundColor;

    public AdaptiveIcon(Context context, Drawable drawable) {
        this(context, drawable, C1861R.dimen.dashboard_tile_foreground_image_inset);
    }

    public AdaptiveIcon(Context context, Drawable drawable, int i) {
        super(new Drawable[]{new AdaptiveIconShapeDrawable(context.getResources()), drawable});
        this.mBackgroundColor = -1;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(i);
        setLayerInset(1, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        this.mAdaptiveConstantState = new AdaptiveConstantState(context, drawable);
    }

    public void setBackgroundColor(Context context, Tile tile) {
        int i;
        Bundle metaData = tile.getMetaData();
        if (metaData != null) {
            try {
                int i2 = metaData.getInt("com.android.settings.bg.argb", 0);
                if (i2 == 0 && (i = metaData.getInt("com.android.settings.bg.hint", 0)) != 0) {
                    i2 = context.getPackageManager().getResourcesForApplication(tile.getPackageName()).getColor(i, (Resources.Theme) null);
                }
                if (i2 != 0) {
                    setBackgroundColor(i2);
                    return;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e(TAG, "Failed to set background color for " + tile.getPackageName());
            }
        }
        setBackgroundColor(context.getColor(C1861R.C1862color.homepage_generic_icon_background));
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        getDrawable(0).setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        Log.d(TAG, "Setting background color " + this.mBackgroundColor);
        this.mAdaptiveConstantState.mColor = i;
    }

    public Drawable.ConstantState getConstantState() {
        return this.mAdaptiveConstantState;
    }

    static class AdaptiveConstantState extends Drawable.ConstantState {
        int mColor;
        Context mContext;
        Drawable mDrawable;

        public int getChangingConfigurations() {
            return 0;
        }

        AdaptiveConstantState(Context context, Drawable drawable) {
            this.mContext = context;
            this.mDrawable = drawable;
        }

        public Drawable newDrawable() {
            AdaptiveIcon adaptiveIcon = new AdaptiveIcon(this.mContext, this.mDrawable);
            adaptiveIcon.setBackgroundColor(this.mColor);
            return adaptiveIcon;
        }
    }
}
