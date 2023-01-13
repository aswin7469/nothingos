package com.android.systemui.flags;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, mo65043d2 = {"Lcom/android/systemui/flags/StringFlagSerializer;", "Lcom/android/systemui/flags/FlagSerializer;", "", "()V", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FlagSerializer.kt */
public final class StringFlagSerializer extends FlagSerializer<String> {
    public static final StringFlagSerializer INSTANCE = new StringFlagSerializer();

    private StringFlagSerializer() {
        super("string", C21111.INSTANCE, C21122.INSTANCE);
    }
}
