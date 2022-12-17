package com.android.settings.deviceinfo;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StorageItemPreference$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StorageItemPreference f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ StorageItemPreference$$ExternalSyntheticLambda1(StorageItemPreference storageItemPreference, long j) {
        this.f$0 = storageItemPreference;
        this.f$1 = j;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setStorageSize$1(this.f$1, valueAnimator);
    }
}
