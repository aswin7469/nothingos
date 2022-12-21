package com.android.systemui.flags;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0005¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/flags/InvalidFlagStorageException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "()V", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FlagSerializer.kt */
public final class InvalidFlagStorageException extends Exception {
    public InvalidFlagStorageException() {
        super("Data found but is invalid");
    }
}
