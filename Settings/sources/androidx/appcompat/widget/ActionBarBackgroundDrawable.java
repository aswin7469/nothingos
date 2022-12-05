package androidx.appcompat.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ActionBarBackgroundDrawable extends Drawable {
    final ActionBarContainer mContainer;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ActionBarBackgroundDrawable(ActionBarContainer actionBarContainer) {
        this.mContainer = actionBarContainer;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        ActionBarContainer actionBarContainer = this.mContainer;
        if (actionBarContainer.mIsSplit) {
            Drawable drawable = actionBarContainer.mSplitBackground;
            if (drawable == null) {
                return;
            }
            drawable.draw(canvas);
            return;
        }
        Drawable drawable2 = actionBarContainer.mBackground;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        ActionBarContainer actionBarContainer2 = this.mContainer;
        Drawable drawable3 = actionBarContainer2.mStackedBackground;
        if (drawable3 == null || !actionBarContainer2.mIsStacked) {
            return;
        }
        drawable3.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        ActionBarContainer actionBarContainer = this.mContainer;
        if (actionBarContainer.mIsSplit) {
            Drawable drawable = actionBarContainer.mSplitBackground;
            if (drawable == null) {
                return;
            }
            drawable.getOutline(outline);
            return;
        }
        Drawable drawable2 = actionBarContainer.mBackground;
        if (drawable2 == null) {
            return;
        }
        drawable2.getOutline(outline);
    }
}
