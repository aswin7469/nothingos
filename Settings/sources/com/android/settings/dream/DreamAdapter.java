package com.android.settings.dream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$color;
import com.android.settings.R$dimen;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settingslib.utils.ColorUtil;
import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /* access modifiers changed from: private */
    public boolean mEnabled = true;
    private final List<IDreamItem> mItemList;
    /* access modifiers changed from: private */
    public int mLastSelectedPos = -1;
    private final int mLayoutRes;

    private class DreamViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        private final Button mCustomizeButton;
        private final float mDisabledAlphaValue;
        private final ImageView mPreviewPlaceholderView;
        private final ImageView mPreviewView;
        private final TextView mSummaryView;
        private final TextView mTitleView;

        DreamViewHolder(View view, Context context) {
            super(view);
            this.mContext = context;
            this.mPreviewView = (ImageView) view.findViewById(R$id.preview);
            this.mPreviewPlaceholderView = (ImageView) view.findViewById(R$id.preview_placeholder);
            this.mTitleView = (TextView) view.findViewById(R$id.title_text);
            this.mSummaryView = (TextView) view.findViewById(R$id.summary_text);
            this.mCustomizeButton = (Button) view.findViewById(R$id.customize_button);
            this.mDisabledAlphaValue = ColorUtil.getDisabledAlpha(context);
        }

        public void bindView(IDreamItem iDreamItem, int i) {
            Drawable drawable;
            this.mTitleView.setText(iDreamItem.getTitle());
            CharSequence summary = iDreamItem.getSummary();
            int i2 = 8;
            if (TextUtils.isEmpty(summary)) {
                this.mSummaryView.setVisibility(8);
            } else {
                this.mSummaryView.setText(summary);
                this.mSummaryView.setVisibility(0);
            }
            Drawable previewImage = iDreamItem.getPreviewImage();
            if (previewImage != null) {
                this.mPreviewView.setImageDrawable(previewImage);
                this.mPreviewView.setClipToOutline(true);
                this.mPreviewPlaceholderView.setVisibility(8);
            } else {
                this.mPreviewView.setImageDrawable((Drawable) null);
                this.mPreviewPlaceholderView.setVisibility(0);
            }
            if (iDreamItem.isActive()) {
                drawable = this.mContext.getDrawable(R$drawable.ic_dream_check_circle);
            } else {
                drawable = iDreamItem.getIcon().mutate();
            }
            if (drawable instanceof VectorDrawable) {
                drawable.setTintList(this.mContext.getColorStateList(R$color.dream_card_icon_color_state_list));
            }
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.dream_item_icon_size);
            drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
            this.mTitleView.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            this.itemView.setOnClickListener(new DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda0(this, iDreamItem, i));
            if (iDreamItem.isActive()) {
                DreamAdapter.this.mLastSelectedPos = i;
                this.itemView.setSelected(true);
                this.itemView.setClickable(false);
            } else {
                this.itemView.setSelected(false);
                this.itemView.setClickable(true);
            }
            this.mCustomizeButton.setOnClickListener(new DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda1(iDreamItem));
            Button button = this.mCustomizeButton;
            if (iDreamItem.allowCustomization() && DreamAdapter.this.mEnabled) {
                i2 = 0;
            }
            button.setVisibility(i2);
            this.mCustomizeButton.setSelected(false);
            setEnabledStateOnViews(this.itemView, DreamAdapter.this.mEnabled);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$bindView$0(IDreamItem iDreamItem, int i, View view) {
            iDreamItem.onItemClicked();
            if (DreamAdapter.this.mLastSelectedPos > -1 && DreamAdapter.this.mLastSelectedPos != i) {
                DreamAdapter dreamAdapter = DreamAdapter.this;
                dreamAdapter.notifyItemChanged(dreamAdapter.mLastSelectedPos);
            }
            DreamAdapter.this.notifyItemChanged(i);
        }

        private void setEnabledStateOnViews(View view, boolean z) {
            float f;
            view.setEnabled(z);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                    setEnabledStateOnViews(viewGroup.getChildAt(childCount), z);
                }
                return;
            }
            if (z) {
                f = 1.0f;
            } else {
                f = this.mDisabledAlphaValue;
            }
            view.setAlpha(f);
        }
    }

    public DreamAdapter(int i, List<IDreamItem> list) {
        this.mItemList = list;
        this.mLayoutRes = i;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DreamViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutRes, viewGroup, false), viewGroup.getContext());
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((DreamViewHolder) viewHolder).bindView(this.mItemList.get(i), i);
    }

    public int getItemCount() {
        return this.mItemList.size();
    }

    public void setEnabled(boolean z) {
        if (this.mEnabled != z) {
            this.mEnabled = z;
            notifyDataSetChanged();
        }
    }

    public boolean getEnabled() {
        return this.mEnabled;
    }
}
