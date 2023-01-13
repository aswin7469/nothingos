package com.android.systemui.flags;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, mo65043d2 = {"Lcom/android/systemui/flags/BooleanFlagSerializer;", "Lcom/android/systemui/flags/FlagSerializer;", "", "()V", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FlagSerializer.kt */
public final class BooleanFlagSerializer extends FlagSerializer<Boolean> {
    public static final BooleanFlagSerializer INSTANCE = new BooleanFlagSerializer();

    private BooleanFlagSerializer() {
        super("boolean", C21071.INSTANCE, C21082.INSTANCE);
    }
}
