package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotifBindPipelineLogger.kt */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logFinishedPipeline$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logFinishedPipeline$2 INSTANCE = new NotifBindPipelineLogger$logFinishedPipeline$2();

    NotifBindPipelineLogger$logFinishedPipeline$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Finished pipeline for notif " + ((Object) log.getStr1()) + " with " + log.getInt1() + " callbacks";
    }
}
