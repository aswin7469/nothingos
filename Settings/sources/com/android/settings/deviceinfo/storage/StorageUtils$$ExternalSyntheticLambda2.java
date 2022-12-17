package com.android.settings.deviceinfo.storage;

import android.os.storage.DiskInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StorageUtils$$ExternalSyntheticLambda2 implements Predicate {
    public final boolean test(Object obj) {
        return StorageUtils.isDiskUnsupported((DiskInfo) obj);
    }
}
