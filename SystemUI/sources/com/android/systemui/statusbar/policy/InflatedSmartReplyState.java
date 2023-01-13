package com.android.systemui.statusbar.policy;

import android.app.Notification;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u001eB-\b\u0000\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00108F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00108F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0013R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00108F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0013R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001d¨\u0006\u001f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState;", "", "smartReplies", "Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartReplies;", "smartActions", "Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartActions;", "suppressedActions", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState$SuppressedActions;", "hasPhishingAction", "", "(Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartReplies;Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartActions;Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState$SuppressedActions;Z)V", "getHasPhishingAction", "()Z", "getSmartActions", "()Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartActions;", "smartActionsList", "", "Landroid/app/Notification$Action;", "getSmartActionsList", "()Ljava/util/List;", "getSmartReplies", "()Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartReplies;", "smartRepliesList", "", "getSmartRepliesList", "suppressedActionIndices", "", "getSuppressedActionIndices", "getSuppressedActions", "()Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState$SuppressedActions;", "SuppressedActions", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: InflatedSmartReplyState.kt */
public final class InflatedSmartReplyState {
    private final boolean hasPhishingAction;
    private final SmartReplyView.SmartActions smartActions;
    private final SmartReplyView.SmartReplies smartReplies;
    private final SuppressedActions suppressedActions;

    public InflatedSmartReplyState(SmartReplyView.SmartReplies smartReplies2, SmartReplyView.SmartActions smartActions2, SuppressedActions suppressedActions2, boolean z) {
        this.smartReplies = smartReplies2;
        this.smartActions = smartActions2;
        this.suppressedActions = suppressedActions2;
        this.hasPhishingAction = z;
    }

    public final SmartReplyView.SmartReplies getSmartReplies() {
        return this.smartReplies;
    }

    public final SmartReplyView.SmartActions getSmartActions() {
        return this.smartActions;
    }

    public final SuppressedActions getSuppressedActions() {
        return this.suppressedActions;
    }

    public final boolean getHasPhishingAction() {
        return this.hasPhishingAction;
    }

    public final List<CharSequence> getSmartRepliesList() {
        SmartReplyView.SmartReplies smartReplies2 = this.smartReplies;
        List<CharSequence> list = smartReplies2 != null ? smartReplies2.choices : null;
        return list == null ? CollectionsKt.emptyList() : list;
    }

    public final List<Notification.Action> getSmartActionsList() {
        SmartReplyView.SmartActions smartActions2 = this.smartActions;
        List<Notification.Action> list = smartActions2 != null ? smartActions2.actions : null;
        return list == null ? CollectionsKt.emptyList() : list;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r0.getSuppressedActionIndices();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<java.lang.Integer> getSuppressedActionIndices() {
        /*
            r0 = this;
            com.android.systemui.statusbar.policy.InflatedSmartReplyState$SuppressedActions r0 = r0.suppressedActions
            if (r0 == 0) goto L_0x000a
            java.util.List r0 = r0.getSuppressedActionIndices()
            if (r0 != 0) goto L_0x000e
        L_0x000a:
            java.util.List r0 = kotlin.collections.CollectionsKt.emptyList()
        L_0x000e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.InflatedSmartReplyState.getSuppressedActionIndices():java.util.List");
    }

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState$SuppressedActions;", "", "suppressedActionIndices", "", "", "(Ljava/util/List;)V", "getSuppressedActionIndices", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: InflatedSmartReplyState.kt */
    public static final class SuppressedActions {
        private final List<Integer> suppressedActionIndices;

        public SuppressedActions(List<Integer> list) {
            Intrinsics.checkNotNullParameter(list, "suppressedActionIndices");
            this.suppressedActionIndices = list;
        }

        public final List<Integer> getSuppressedActionIndices() {
            return this.suppressedActionIndices;
        }
    }
}
