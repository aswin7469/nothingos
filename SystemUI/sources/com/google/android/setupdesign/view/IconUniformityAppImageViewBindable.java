package com.google.android.setupdesign.view;

import android.graphics.drawable.Drawable;

public interface IconUniformityAppImageViewBindable {
    void bindView(IconUniformityAppImageViewData iconUniformityAppImageViewData);

    void onRecycle();

    public static class IconUniformityAppImageViewData {
        public Drawable icon;

        public IconUniformityAppImageViewData(Drawable drawable) {
            this.icon = drawable;
        }
    }
}
