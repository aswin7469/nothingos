package com.android.settings.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes.dex */
public class GroupOptionsPreference extends Preference {
    private final ButtonInfo mBtnAddSrcGroup = new ButtonInfo();
    private final ButtonInfo mBtnConnect = new ButtonInfo();
    private final ButtonInfo mBtnForget = new ButtonInfo();
    private final ButtonInfo mBtnDisconnect = new ButtonInfo();
    private final ButtonInfo mBtnRefresh = new ButtonInfo();
    private final ButtonInfo mBtnCancelRefresh = new ButtonInfo();
    private final TextViewInfo mTvGroupId = new TextViewInfo();
    private final TextViewInfo mTvStatus = new TextViewInfo();
    private final ProgressInfo mProgressScan = new ProgressInfo();

    public GroupOptionsPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public GroupOptionsPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public GroupOptionsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setLayoutResource(R.layout.bluetooth_group_options);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(true);
        preferenceViewHolder.setDividerAllowedBelow(true);
        this.mBtnAddSrcGroup.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_group_add_source);
        this.mBtnConnect.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_connect);
        this.mBtnForget.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_forget);
        this.mBtnDisconnect.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_disconnect);
        this.mBtnRefresh.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_refresh);
        this.mBtnCancelRefresh.mButton = (Button) preferenceViewHolder.findViewById(R.id.id_btn_refresh_cancel);
        this.mTvGroupId.mTextView = (TextView) preferenceViewHolder.findViewById(R.id.id_tv_groupid);
        this.mTvStatus.mTextView = (TextView) preferenceViewHolder.findViewById(R.id.id_tv_status);
        this.mProgressScan.mProgress = (ProgressBar) preferenceViewHolder.findViewById(R.id.id_progress_group_scan);
        this.mBtnAddSrcGroup.setUpButton();
        this.mBtnConnect.setUpButton();
        this.mBtnForget.setUpButton();
        this.mBtnDisconnect.setUpButton();
        this.mBtnRefresh.setUpButton();
        this.mBtnCancelRefresh.setUpButton();
        this.mTvGroupId.setUpTextView();
        this.mTvStatus.setUpTextView();
        this.mProgressScan.setUpProgress();
    }

    public GroupOptionsPreference setAddSourceGroupButtonVisible(boolean z) {
        if (z != this.mBtnAddSrcGroup.mIsVisible) {
            this.mBtnAddSrcGroup.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setAddSourceGroupButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnAddSrcGroup.mText)) {
            this.mBtnAddSrcGroup.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setAddSourceGroupButtonEnabled(boolean z) {
        if (z != this.mBtnAddSrcGroup.mIsEnabled) {
            this.mBtnAddSrcGroup.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setAddSourceGroupButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnAddSrcGroup.mListener) {
            this.mBtnAddSrcGroup.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setConnectButtonVisible(boolean z) {
        if (z != this.mBtnConnect.mIsVisible) {
            this.mBtnConnect.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setConnectButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnConnect.mText)) {
            this.mBtnConnect.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setConnectButtonEnabled(boolean z) {
        if (z != this.mBtnConnect.mIsEnabled) {
            this.mBtnConnect.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setConnectButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnConnect.mListener) {
            this.mBtnConnect.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setForgetButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnForget.mText)) {
            this.mBtnForget.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setForgetButtonEnabled(boolean z) {
        if (z != this.mBtnForget.mIsEnabled) {
            this.mBtnForget.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setForgetButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnForget.mListener) {
            this.mBtnForget.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setDisconnectButtonVisible(boolean z) {
        if (z != this.mBtnDisconnect.mIsVisible) {
            this.mBtnDisconnect.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setDisconnectButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnDisconnect.mText)) {
            this.mBtnDisconnect.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setDisconnectButtonEnabled(boolean z) {
        if (z != this.mBtnDisconnect.mIsEnabled) {
            this.mBtnDisconnect.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setDisconnectButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnDisconnect.mListener) {
            this.mBtnDisconnect.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setRefreshButtonVisible(boolean z) {
        if (z != this.mBtnRefresh.mIsVisible) {
            this.mBtnRefresh.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setRefreshButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnRefresh.mText)) {
            this.mBtnRefresh.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setRefreshButtonEnabled(boolean z) {
        if (z != this.mBtnRefresh.mIsEnabled) {
            this.mBtnRefresh.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setRefreshButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnRefresh.mListener) {
            this.mBtnRefresh.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setCancelRefreshButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mBtnCancelRefresh.mText)) {
            this.mBtnCancelRefresh.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setCancelRefreshButtonVisible(boolean z) {
        if (z != this.mBtnCancelRefresh.mIsVisible) {
            this.mBtnCancelRefresh.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setCancelRefreshButtonEnabled(boolean z) {
        if (z != this.mBtnCancelRefresh.mIsEnabled) {
            this.mBtnCancelRefresh.mIsEnabled = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setCancelRefreshButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mBtnCancelRefresh.mListener) {
            this.mBtnCancelRefresh.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setTextViewText(String str) {
        if (!TextUtils.equals(str, this.mTvGroupId.mText)) {
            this.mTvGroupId.mText = str;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setTvStatusVisible(boolean z) {
        if (z != this.mTvStatus.mIsVisible) {
            this.mTvStatus.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setTexStatusText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mTvStatus.mText)) {
            this.mTvStatus.mText = string;
            notifyChanged();
        }
        return this;
    }

    public GroupOptionsPreference setProgressScanVisible(boolean z) {
        if (z != this.mProgressScan.mIsVisible) {
            this.mProgressScan.mIsVisible = z;
            notifyChanged();
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ButtonInfo {
        private Button mButton;
        private boolean mIsEnabled = true;
        private boolean mIsVisible = true;
        private View.OnClickListener mListener;
        private CharSequence mText;

        ButtonInfo() {
        }

        void setUpButton() {
            this.mButton.setText(this.mText);
            this.mButton.setOnClickListener(this.mListener);
            this.mButton.setEnabled(this.mIsEnabled);
            this.mButton.setTypeface(null, 1);
            if (shouldBeVisible()) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }

        private boolean shouldBeVisible() {
            return this.mIsVisible && !TextUtils.isEmpty(this.mText);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TextViewInfo {
        private boolean mIsVisible = true;
        private CharSequence mText;
        private TextView mTextView;

        TextViewInfo() {
        }

        void setUpTextView() {
            this.mTextView.setText(this.mText);
            this.mTextView.setTypeface(null, 1);
            if (shouldBeVisible()) {
                this.mTextView.setVisibility(0);
            } else {
                this.mTextView.setVisibility(4);
            }
        }

        private boolean shouldBeVisible() {
            return this.mIsVisible && !TextUtils.isEmpty(this.mText);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ProgressInfo {
        private boolean mIsVisible = true;
        private ProgressBar mProgress;

        ProgressInfo() {
        }

        void setUpProgress() {
            if (shouldBeVisible()) {
                this.mProgress.setVisibility(0);
            } else {
                this.mProgress.setVisibility(4);
            }
        }

        private boolean shouldBeVisible() {
            return this.mIsVisible;
        }
    }
}
