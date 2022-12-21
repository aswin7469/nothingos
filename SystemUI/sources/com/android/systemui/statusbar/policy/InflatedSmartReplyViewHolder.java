package com.android.systemui.statusbar.policy;

import android.widget.Button;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u001f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005¢\u0006\u0002\u0010\u0007R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/InflatedSmartReplyViewHolder;", "", "smartReplyView", "Lcom/android/systemui/statusbar/policy/SmartReplyView;", "smartSuggestionButtons", "", "Landroid/widget/Button;", "(Lcom/android/systemui/statusbar/policy/SmartReplyView;Ljava/util/List;)V", "getSmartReplyView", "()Lcom/android/systemui/statusbar/policy/SmartReplyView;", "getSmartSuggestionButtons", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: InflatedSmartReplyViewHolder.kt */
public final class InflatedSmartReplyViewHolder {
    private final SmartReplyView smartReplyView;
    private final List<Button> smartSuggestionButtons;

    public InflatedSmartReplyViewHolder(SmartReplyView smartReplyView2, List<? extends Button> list) {
        this.smartReplyView = smartReplyView2;
        this.smartSuggestionButtons = list;
    }

    public final SmartReplyView getSmartReplyView() {
        return this.smartReplyView;
    }

    public final List<Button> getSmartSuggestionButtons() {
        return this.smartSuggestionButtons;
    }
}
