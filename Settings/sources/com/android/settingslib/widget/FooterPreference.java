package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class FooterPreference extends Preference {
    private CharSequence mContentDescription;
    int mIconVisibility;
    View.OnClickListener mLearnMoreListener;
    private FooterLearnMoreSpan mLearnMoreSpan;
    private CharSequence mLearnMoreText;

    public FooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, R$attr.footerPreferenceStyle);
        this.mIconVisibility = 0;
        init();
    }

    public FooterPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908310);
        if (!TextUtils.isEmpty(this.mContentDescription)) {
            textView.setContentDescription(this.mContentDescription);
        }
        TextView textView2 = (TextView) preferenceViewHolder.itemView.findViewById(R$id.settingslib_learn_more);
        if (textView2 == null || this.mLearnMoreListener == null) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            if (TextUtils.isEmpty(this.mLearnMoreText)) {
                this.mLearnMoreText = textView2.getText();
            } else {
                textView2.setText(this.mLearnMoreText);
            }
            SpannableString spannableString = new SpannableString(this.mLearnMoreText);
            FooterLearnMoreSpan footerLearnMoreSpan = this.mLearnMoreSpan;
            if (footerLearnMoreSpan != null) {
                spannableString.removeSpan(footerLearnMoreSpan);
            }
            FooterLearnMoreSpan footerLearnMoreSpan2 = new FooterLearnMoreSpan(this.mLearnMoreListener);
            this.mLearnMoreSpan = footerLearnMoreSpan2;
            spannableString.setSpan(footerLearnMoreSpan2, 0, spannableString.length(), 0);
            textView2.setText(spannableString);
        }
        preferenceViewHolder.itemView.findViewById(R$id.icon_frame).setVisibility(this.mIconVisibility);
    }

    public void setSummary(CharSequence charSequence) {
        setTitle(charSequence);
    }

    public void setSummary(int i) {
        setTitle(i);
    }

    public CharSequence getSummary() {
        return getTitle();
    }

    public void setContentDescription(CharSequence charSequence) {
        if (!TextUtils.equals(this.mContentDescription, charSequence)) {
            this.mContentDescription = charSequence;
            notifyChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public void setLearnMoreText(CharSequence charSequence) {
        if (!TextUtils.equals(this.mLearnMoreText, charSequence)) {
            this.mLearnMoreText = charSequence;
            notifyChanged();
        }
    }

    public void setLearnMoreAction(View.OnClickListener onClickListener) {
        if (this.mLearnMoreListener != onClickListener) {
            this.mLearnMoreListener = onClickListener;
            notifyChanged();
        }
    }

    private void init() {
        setLayoutResource(R$layout.preference_footer);
        if (getIcon() == null) {
            setIcon(R$drawable.settingslib_ic_info_outline_24);
        }
        setOrder(2147483646);
        if (TextUtils.isEmpty(getKey())) {
            setKey("footer_preference");
        }
        setSelectable(false);
    }

    public static class Builder {
        private CharSequence mContentDescription;
        private Context mContext;
        private String mKey;
        private CharSequence mLearnMoreText;
        private CharSequence mTitle;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        public Builder setTitle(int i) {
            this.mTitle = this.mContext.getText(i);
            return this;
        }

        public FooterPreference build() {
            FooterPreference footerPreference = new FooterPreference(this.mContext);
            footerPreference.setSelectable(false);
            if (!TextUtils.isEmpty(this.mTitle)) {
                footerPreference.setTitle(this.mTitle);
                if (!TextUtils.isEmpty(this.mKey)) {
                    footerPreference.setKey(this.mKey);
                }
                if (!TextUtils.isEmpty(this.mContentDescription)) {
                    footerPreference.setContentDescription(this.mContentDescription);
                }
                if (!TextUtils.isEmpty(this.mLearnMoreText)) {
                    footerPreference.setLearnMoreText(this.mLearnMoreText);
                }
                return footerPreference;
            }
            throw new IllegalArgumentException("Footer title cannot be empty!");
        }
    }

    static class FooterLearnMoreSpan extends URLSpan {
        private final View.OnClickListener mClickListener;

        FooterLearnMoreSpan(View.OnClickListener onClickListener) {
            super("");
            this.mClickListener = onClickListener;
        }

        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }
}
