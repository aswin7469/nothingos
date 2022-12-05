package com.android.systemui.statusbar.policy;

import android.app.Notification;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: InflatedSmartReplyState.kt */
/* loaded from: classes2.dex */
public final class InflatedSmartReplyState {
    private final boolean hasPhishingAction;
    @Nullable
    private final SmartReplyView.SmartActions smartActions;
    @Nullable
    private final SmartReplyView.SmartReplies smartReplies;
    @Nullable
    private final SuppressedActions suppressedActions;

    public InflatedSmartReplyState(@Nullable SmartReplyView.SmartReplies smartReplies, @Nullable SmartReplyView.SmartActions smartActions, @Nullable SuppressedActions suppressedActions, boolean z) {
        this.smartReplies = smartReplies;
        this.smartActions = smartActions;
        this.suppressedActions = suppressedActions;
        this.hasPhishingAction = z;
    }

    @Nullable
    public final SmartReplyView.SmartReplies getSmartReplies() {
        return this.smartReplies;
    }

    @Nullable
    public final SmartReplyView.SmartActions getSmartActions() {
        return this.smartActions;
    }

    public final boolean getHasPhishingAction() {
        return this.hasPhishingAction;
    }

    @NotNull
    public final List<CharSequence> getSmartRepliesList() {
        SmartReplyView.SmartReplies smartReplies = this.smartReplies;
        List<CharSequence> list = smartReplies == null ? null : smartReplies.choices;
        return list == null ? CollectionsKt.emptyList() : list;
    }

    @NotNull
    public final List<Notification.Action> getSmartActionsList() {
        SmartReplyView.SmartActions smartActions = this.smartActions;
        List<Notification.Action> list = smartActions == null ? null : smartActions.actions;
        return list == null ? CollectionsKt.emptyList() : list;
    }

    @NotNull
    public final List<Integer> getSuppressedActionIndices() {
        SuppressedActions suppressedActions = this.suppressedActions;
        return suppressedActions == null ? CollectionsKt.emptyList() : suppressedActions.getSuppressedActionIndices();
    }

    /* compiled from: InflatedSmartReplyState.kt */
    /* loaded from: classes2.dex */
    public static final class SuppressedActions {
        @NotNull
        private final List<Integer> suppressedActionIndices;

        public SuppressedActions(@NotNull List<Integer> suppressedActionIndices) {
            Intrinsics.checkNotNullParameter(suppressedActionIndices, "suppressedActionIndices");
            this.suppressedActionIndices = suppressedActionIndices;
        }

        @NotNull
        public final List<Integer> getSuppressedActionIndices() {
            return this.suppressedActionIndices;
        }
    }
}
