package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.util.Utils;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: MediaFeatureFlag.kt */
/* loaded from: classes.dex */
public final class MediaFeatureFlag {
    @NotNull
    private final Context context;

    public MediaFeatureFlag(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final boolean getEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }
}
