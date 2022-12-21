package com.android.systemui.p012qs.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.C1893R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.statusbar.phone.UserAvatarView;

/* renamed from: com.android.systemui.qs.tiles.UserDetailItemView */
public class UserDetailItemView extends LinearLayout {
    protected static int layoutResId = 2131624413;
    private int mActivatedStyle;
    private UserAvatarView mAvatar;
    protected TextView mName;
    private int mRegularStyle;
    private View mRestrictedPadlock;

    /* access modifiers changed from: protected */
    public int getFontSizeDimen() {
        return C1893R.dimen.qs_tile_text_size;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public UserDetailItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public UserDetailItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UserDetailItemView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public UserDetailItemView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1893R.styleable.UserDetailItemView, i, i2);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == 1) {
                this.mRegularStyle = obtainStyledAttributes.getResourceId(index, 0);
            } else if (index == 0) {
                this.mActivatedStyle = obtainStyledAttributes.getResourceId(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }

    public static UserDetailItemView convertOrInflate(Context context, View view, ViewGroup viewGroup) {
        if (!(view instanceof UserDetailItemView)) {
            view = LayoutInflater.from(context).inflate(layoutResId, viewGroup, false);
        }
        return (UserDetailItemView) view;
    }

    public void bind(String str, Bitmap bitmap, int i) {
        this.mName.setText(str);
        this.mAvatar.setAvatarWithBadge(bitmap, i);
    }

    public void bind(String str, Drawable drawable, int i) {
        this.mName.setText(str);
        this.mAvatar.setDrawableWithBadge(drawable, i);
    }

    public void setDisabledByAdmin(boolean z) {
        View view = this.mRestrictedPadlock;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
        setEnabled(!z);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mName.setEnabled(z);
        this.mAvatar.setEnabled(z);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mAvatar = (UserAvatarView) findViewById(C1893R.C1897id.user_picture);
        TextView textView = (TextView) findViewById(C1893R.C1897id.user_name);
        this.mName = textView;
        if (this.mRegularStyle == 0) {
            this.mRegularStyle = textView.getExplicitStyle();
        }
        if (this.mActivatedStyle == 0) {
            this.mActivatedStyle = this.mName.getExplicitStyle();
        }
        updateTextStyle();
        this.mRestrictedPadlock = findViewById(C1893R.C1897id.restricted_padlock);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FontSizeUtils.updateFontSize(this.mName, getFontSizeDimen());
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        updateTextStyle();
    }

    private void updateTextStyle() {
        this.mName.setTextAppearance(ArrayUtils.contains(getDrawableState(), 16843518) ? this.mActivatedStyle : this.mRegularStyle);
    }
}
