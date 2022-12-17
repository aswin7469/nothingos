package com.android.settings.deviceinfo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$layout;
import com.android.settings.deviceinfo.storage.StorageUtils;

public class StorageItemPreference extends Preference {
    private ProgressBar mProgressBar;
    private int mProgressPercent;
    private long mStorageSize;

    public StorageItemPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public StorageItemPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mProgressPercent = -1;
        setLayoutResource(R$layout.storage_item);
    }

    public void setStorageSize(long j, long j2, boolean z) {
        if (z) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new StorageItemPreference$$ExternalSyntheticLambda0(), new Object[]{Long.valueOf(this.mStorageSize), Long.valueOf(j)});
            ofObject.setDuration(1000);
            ofObject.addUpdateListener(new StorageItemPreference$$ExternalSyntheticLambda1(this, j2));
            ofObject.start();
        } else {
            updateProgressBarAndSizeInfo(j, j2);
        }
        this.mStorageSize = j;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Long lambda$setStorageSize$0(float f, Long l, Long l2) {
        if (f < 1.0f || l2.longValue() != 0) {
            return Long.valueOf(l.longValue() + ((long) (f * ((float) (l2.longValue() - l.longValue())))));
        }
        return l2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setStorageSize$1(long j, ValueAnimator valueAnimator) {
        updateProgressBarAndSizeInfo(((Long) valueAnimator.getAnimatedValue()).longValue(), j);
    }

    public long getStorageSize() {
        return this.mStorageSize;
    }

    /* access modifiers changed from: protected */
    public void updateProgressBar() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null && this.mProgressPercent != -1) {
            progressBar.setMax(100);
            this.mProgressBar.setProgress(this.mProgressPercent);
        }
    }

    private void updateProgressBarAndSizeInfo(long j, long j2) {
        int i;
        setSummary((CharSequence) StorageUtils.getStorageSizeLabel(getContext(), j));
        if (j2 == 0) {
            i = 0;
        } else {
            i = (int) ((j * 100) / j2);
        }
        this.mProgressPercent = i;
        updateProgressBar();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        this.mProgressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        updateProgressBar();
        super.onBindViewHolder(preferenceViewHolder);
    }
}
