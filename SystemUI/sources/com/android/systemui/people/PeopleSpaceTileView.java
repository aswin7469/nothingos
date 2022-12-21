package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1893R;

public class PeopleSpaceTileView extends LinearLayout {
    private TextView mNameView;
    private ImageView mPersonIconView;
    private View mTileView;

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

    public PeopleSpaceTileView(Context context, ViewGroup viewGroup, String str, boolean z) {
        super(context);
        View findViewWithTag = viewGroup.findViewWithTag(str);
        this.mTileView = findViewWithTag;
        if (findViewWithTag == null) {
            LayoutInflater from = LayoutInflater.from(context);
            View inflate = from.inflate(C1893R.layout.people_space_tile_view, viewGroup, false);
            this.mTileView = inflate;
            viewGroup.addView(inflate, -1, -1);
            this.mTileView.setTag(str);
            if (!z) {
                from.inflate(C1893R.layout.people_space_activity_list_divider, viewGroup, true);
            }
        }
        this.mNameView = (TextView) this.mTileView.findViewById(C1893R.C1897id.tile_view_name);
        this.mPersonIconView = (ImageView) this.mTileView.findViewById(C1893R.C1897id.tile_view_person_icon);
    }

    public void setName(String str) {
        this.mNameView.setText(str);
    }

    public void setPersonIcon(Bitmap bitmap) {
        this.mPersonIconView.setImageBitmap(bitmap);
    }

    public void setOnClickListener(LauncherApps launcherApps, PeopleSpaceTile peopleSpaceTile) {
        this.mTileView.setOnClickListener(new PeopleSpaceTileView$$ExternalSyntheticLambda0(launcherApps, peopleSpaceTile));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mTileView.setOnClickListener(onClickListener);
    }
}
