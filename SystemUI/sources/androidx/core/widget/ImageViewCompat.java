package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class ImageViewCompat {
    public static ColorStateList getImageTintList(ImageView view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getImageTintList();
        }
        if (!(view instanceof TintableImageSourceView)) {
            return null;
        }
        return ((TintableImageSourceView) view).getSupportImageTintList();
    }

    public static void setImageTintList(ImageView view, ColorStateList tintList) {
        Drawable drawable;
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            view.setImageTintList(tintList);
            if (i != 21 || (drawable = view.getDrawable()) == null || view.getImageTintList() == null) {
                return;
            }
            if (drawable.isStateful()) {
                drawable.setState(view.getDrawableState());
            }
            view.setImageDrawable(drawable);
        } else if (!(view instanceof TintableImageSourceView)) {
        } else {
            ((TintableImageSourceView) view).setSupportImageTintList(tintList);
        }
    }

    public static PorterDuff.Mode getImageTintMode(ImageView view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getImageTintMode();
        }
        if (!(view instanceof TintableImageSourceView)) {
            return null;
        }
        return ((TintableImageSourceView) view).getSupportImageTintMode();
    }

    public static void setImageTintMode(ImageView view, PorterDuff.Mode mode) {
        Drawable drawable;
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            view.setImageTintMode(mode);
            if (i != 21 || (drawable = view.getDrawable()) == null || view.getImageTintList() == null) {
                return;
            }
            if (drawable.isStateful()) {
                drawable.setState(view.getDrawableState());
            }
            view.setImageDrawable(drawable);
        } else if (!(view instanceof TintableImageSourceView)) {
        } else {
            ((TintableImageSourceView) view).setSupportImageTintMode(mode);
        }
    }
}
