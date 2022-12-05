package androidx.core.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;
/* loaded from: classes.dex */
public final class MenuItemCompat {
    public static MenuItem setActionProvider(MenuItem item, ActionProvider provider) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).setSupportActionProvider(provider);
        }
        Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return item;
    }

    public static void setContentDescription(MenuItem item, CharSequence contentDescription) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).mo41setContentDescription(contentDescription);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setContentDescription(contentDescription);
        }
    }

    public static void setTooltipText(MenuItem item, CharSequence tooltipText) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).mo43setTooltipText(tooltipText);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setTooltipText(tooltipText);
        }
    }

    public static void setNumericShortcut(MenuItem item, char numericChar, int numericModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setNumericShortcut(numericChar, numericModifiers);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setNumericShortcut(numericChar, numericModifiers);
        }
    }

    public static void setAlphabeticShortcut(MenuItem item, char alphaChar, int alphaModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setAlphabeticShortcut(alphaChar, alphaModifiers);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setAlphabeticShortcut(alphaChar, alphaModifiers);
        }
    }

    public static void setIconTintList(MenuItem item, ColorStateList tint) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintList(tint);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setIconTintList(tint);
        }
    }

    public static void setIconTintMode(MenuItem item, PorterDuff.Mode tintMode) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintMode(tintMode);
        } else if (Build.VERSION.SDK_INT < 26) {
        } else {
            item.setIconTintMode(tintMode);
        }
    }
}
