package com.android.settings.deviceinfo.storage;

import android.os.storage.VolumeInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StorageUtils$$ExternalSyntheticLambda0 implements Predicate {
    public final boolean test(Object obj) {
        return StorageUtils.isStorageSettingsInterestedVolume((VolumeInfo) obj);
    }
}
