package com.android.systemui.flags;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/flags/BooleanFlagSerializer;", "Lcom/android/systemui/flags/FlagSerializer;", "", "()V", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FlagSerializer.kt */
public final class BooleanFlagSerializer extends FlagSerializer<Boolean> {
    public static final BooleanFlagSerializer INSTANCE = new BooleanFlagSerializer();

    private BooleanFlagSerializer() {
        super("boolean", C21051.INSTANCE, C21062.INSTANCE);
    }
}
