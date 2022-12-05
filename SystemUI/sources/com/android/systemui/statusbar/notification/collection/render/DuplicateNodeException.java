package com.android.systemui.statusbar.notification.collection.render;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
final class DuplicateNodeException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DuplicateNodeException(@NotNull String message) {
        super(message);
        Intrinsics.checkNotNullParameter(message, "message");
    }
}
