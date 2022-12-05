package com.android.systemui.statusbar.policy;

import android.widget.Button;
import java.util.List;
import org.jetbrains.annotations.Nullable;
/* compiled from: InflatedSmartReplyViewHolder.kt */
/* loaded from: classes2.dex */
public final class InflatedSmartReplyViewHolder {
    @Nullable
    private final SmartReplyView smartReplyView;
    @Nullable
    private final List<Button> smartSuggestionButtons;

    /* JADX WARN: Multi-variable type inference failed */
    public InflatedSmartReplyViewHolder(@Nullable SmartReplyView smartReplyView, @Nullable List<? extends Button> list) {
        this.smartReplyView = smartReplyView;
        this.smartSuggestionButtons = list;
    }

    @Nullable
    public final SmartReplyView getSmartReplyView() {
        return this.smartReplyView;
    }

    @Nullable
    public final List<Button> getSmartSuggestionButtons() {
        return this.smartSuggestionButtons;
    }
}
