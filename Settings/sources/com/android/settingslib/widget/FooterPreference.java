package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class FooterPreference extends Preference {
    private CharSequence mContentDescription;
    private CharSequence mLearnMoreContentDescription;
    View.OnClickListener mLearnMoreListener;

    public FooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, R$attr.footerPreferenceStyle);
        init();
    }

    public FooterPreference(Context context) {
        this(context, null);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908310);
        textView.setMovementMethod(new LinkMovementMethod());
        textView.setClickable(false);
        textView.setLongClickable(false);
        if (!TextUtils.isEmpty(this.mContentDescription)) {
            textView.setContentDescription(this.mContentDescription);
        }
        TextView textView2 = (TextView) preferenceViewHolder.itemView.findViewById(R$id.settingslib_learn_more);
        if (textView2 != null && this.mLearnMoreListener != null) {
            textView2.setVisibility(0);
            SpannableString spannableString = new SpannableString(textView2.getText());
            spannableString.setSpan(new FooterLearnMoreSpan(this.mLearnMoreListener), 0, spannableString.length(), 0);
            textView2.setText(spannableString);
            if (TextUtils.isEmpty(this.mLearnMoreContentDescription)) {
                return;
            }
            textView2.setContentDescription(this.mLearnMoreContentDescription);
            return;
        }
        textView2.setVisibility(8);
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        setTitle(charSequence);
    }

    @Override // androidx.preference.Preference
    public void setSummary(int i) {
        setTitle(i);
    }

    @Override // androidx.preference.Preference
    public CharSequence getSummary() {
        return getTitle();
    }

    public void setContentDescription(CharSequence charSequence) {
        if (!TextUtils.equals(this.mContentDescription, charSequence)) {
            this.mContentDescription = charSequence;
            notifyChanged();
        }
    }

    CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public void setLearnMoreContentDescription(CharSequence charSequence) {
        if (!TextUtils.equals(this.mContentDescription, charSequence)) {
            this.mLearnMoreContentDescription = charSequence;
            notifyChanged();
        }
    }

    CharSequence getLearnMoreContentDescription() {
        return this.mLearnMoreContentDescription;
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
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private CharSequence mContentDescription;
        private Context mContext;
        private String mKey;
        private CharSequence mLearnMoreContentDescription;
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
            if (TextUtils.isEmpty(this.mTitle)) {
                throw new IllegalArgumentException("Footer title cannot be empty!");
            }
            footerPreference.setTitle(this.mTitle);
            if (!TextUtils.isEmpty(this.mKey)) {
                footerPreference.setKey(this.mKey);
            }
            if (!TextUtils.isEmpty(this.mContentDescription)) {
                footerPreference.setContentDescription(this.mContentDescription);
            }
            if (!TextUtils.isEmpty(this.mLearnMoreContentDescription)) {
                footerPreference.setLearnMoreContentDescription(this.mLearnMoreContentDescription);
            }
            return footerPreference;
        }
    }

    /* loaded from: classes.dex */
    static class FooterLearnMoreSpan extends URLSpan {
        private final View.OnClickListener mClickListener;

        FooterLearnMoreSpan(View.OnClickListener onClickListener) {
            super("");
            this.mClickListener = onClickListener;
        }

        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }
}
