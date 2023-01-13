package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.util.ItemStyler;
import com.google.android.setupdesign.util.LayoutStyler;

public class Item extends AbstractItem {
    private CharSequence contentDescription;
    private boolean enabled;
    private Drawable icon;
    private int iconGravity;
    private int iconTint;
    private int layoutRes;
    private CharSequence summary;
    private CharSequence title;
    private boolean visible;

    public Item() {
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        this.layoutRes = getDefaultLayoutResource();
    }

    public Item(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3963R.styleable.SudItem);
        this.enabled = obtainStyledAttributes.getBoolean(C3963R.styleable.SudItem_android_enabled, true);
        this.icon = obtainStyledAttributes.getDrawable(C3963R.styleable.SudItem_android_icon);
        this.title = obtainStyledAttributes.getText(C3963R.styleable.SudItem_android_title);
        this.summary = obtainStyledAttributes.getText(C3963R.styleable.SudItem_android_summary);
        this.contentDescription = obtainStyledAttributes.getText(C3963R.styleable.SudItem_android_contentDescription);
        this.layoutRes = obtainStyledAttributes.getResourceId(C3963R.styleable.SudItem_android_layout, getDefaultLayoutResource());
        this.visible = obtainStyledAttributes.getBoolean(C3963R.styleable.SudItem_android_visible, true);
        this.iconTint = obtainStyledAttributes.getColor(C3963R.styleable.SudItem_sudIconTint, 0);
        this.iconGravity = obtainStyledAttributes.getInt(C3963R.styleable.SudItem_sudIconGravity, 16);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public int getDefaultLayoutResource() {
        return C3963R.layout.sud_items_default;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
        notifyItemChanged();
    }

    public int getCount() {
        return isVisible() ? 1 : 0;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
        notifyItemChanged();
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIconTint(int i) {
        this.iconTint = i;
    }

    public int getIconTint() {
        return this.iconTint;
    }

    public void setIconGravity(int i) {
        this.iconGravity = i;
    }

    public int getIconGravity() {
        return this.iconGravity;
    }

    public void setLayoutResource(int i) {
        this.layoutRes = i;
        notifyItemChanged();
    }

    public int getLayoutResource() {
        return this.layoutRes;
    }

    public void setSummary(CharSequence charSequence) {
        this.summary = charSequence;
        notifyItemChanged();
    }

    public CharSequence getSummary() {
        return this.summary;
    }

    public void setTitle(CharSequence charSequence) {
        this.title = charSequence;
        notifyItemChanged();
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public CharSequence getContentDescription() {
        return this.contentDescription;
    }

    public void setContentDescription(CharSequence charSequence) {
        this.contentDescription = charSequence;
        notifyItemChanged();
    }

    public void setVisible(boolean z) {
        if (this.visible != z) {
            this.visible = z;
            if (!z) {
                notifyItemRangeRemoved(0, 1);
            } else {
                notifyItemRangeInserted(0, 1);
            }
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    private boolean hasSummary(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }

    public int getViewId() {
        return getId();
    }

    public void onBindView(View view) {
        ((TextView) view.findViewById(C3963R.C3966id.sud_items_title)).setText(getTitle());
        TextView textView = (TextView) view.findViewById(C3963R.C3966id.sud_items_summary);
        CharSequence summary2 = getSummary();
        if (hasSummary(summary2)) {
            textView.setText(summary2);
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        view.setContentDescription(getContentDescription());
        View findViewById = view.findViewById(C3963R.C3966id.sud_items_icon_container);
        Drawable icon2 = getIcon();
        if (icon2 != null) {
            ImageView imageView = (ImageView) view.findViewById(C3963R.C3966id.sud_items_icon);
            imageView.setImageDrawable((Drawable) null);
            onMergeIconStateAndLevels(imageView, icon2);
            imageView.setImageDrawable(icon2);
            int i = this.iconTint;
            if (i != 0) {
                imageView.setColorFilter(i);
            } else {
                imageView.clearColorFilter();
            }
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).gravity = this.iconGravity;
            }
            findViewById.setVisibility(0);
        } else {
            findViewById.setVisibility(8);
        }
        view.setId(getViewId());
        if (!(this instanceof ExpandableSwitchItem) && view.getId() != C3963R.C3966id.sud_layout_header) {
            LayoutStyler.applyPartnerCustomizationLayoutPaddingStyle(view);
        }
        ItemStyler.applyPartnerCustomizationItemStyle(view);
    }

    /* access modifiers changed from: protected */
    public void onMergeIconStateAndLevels(ImageView imageView, Drawable drawable) {
        imageView.setImageState(drawable.getState(), false);
        imageView.setImageLevel(drawable.getLevel());
    }
}
