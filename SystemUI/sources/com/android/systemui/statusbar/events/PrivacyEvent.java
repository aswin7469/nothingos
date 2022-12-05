package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public final class PrivacyEvent implements StatusEvent {
    @Nullable
    private String contentDescription;
    private final boolean forceVisible;
    private final int priority;
    @Nullable
    private OngoingPrivacyChip privacyChip;
    @NotNull
    private List<PrivacyItem> privacyItems;
    private final boolean showAnimation;
    @NotNull
    private final Function1<Context, View> viewCreator;

    public PrivacyEvent() {
        this(false, 1, null);
    }

    public PrivacyEvent(boolean z) {
        List<PrivacyItem> emptyList;
        this.showAnimation = z;
        this.priority = 100;
        this.forceVisible = true;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.privacyItems = emptyList;
        this.viewCreator = new PrivacyEvent$viewCreator$1(this);
    }

    public /* synthetic */ PrivacyEvent(boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? true : z);
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    @Nullable
    public String getContentDescription() {
        return this.contentDescription;
    }

    public void setContentDescription(@Nullable String str) {
        this.contentDescription = str;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public int getPriority() {
        return this.priority;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getForceVisible() {
        return this.forceVisible;
    }

    @NotNull
    public final List<PrivacyItem> getPrivacyItems() {
        return this.privacyItems;
    }

    public final void setPrivacyItems(@NotNull List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.privacyItems = list;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    @NotNull
    public Function1<Context, View> getViewCreator() {
        return this.viewCreator;
    }

    @NotNull
    public String toString() {
        String simpleName = PrivacyEvent.class.getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean shouldUpdateFromEvent(@Nullable StatusEvent statusEvent) {
        return (statusEvent instanceof PrivacyEvent) && (!Intrinsics.areEqual(((PrivacyEvent) statusEvent).privacyItems, this.privacyItems) || !Intrinsics.areEqual(statusEvent.getContentDescription(), getContentDescription()));
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public void updateFromEvent(@Nullable StatusEvent statusEvent) {
        if (!(statusEvent instanceof PrivacyEvent)) {
            return;
        }
        this.privacyItems = ((PrivacyEvent) statusEvent).privacyItems;
        setContentDescription(statusEvent.getContentDescription());
        OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
        if (ongoingPrivacyChip != null) {
            ongoingPrivacyChip.setContentDescription(statusEvent.getContentDescription());
        }
        OngoingPrivacyChip ongoingPrivacyChip2 = this.privacyChip;
        if (ongoingPrivacyChip2 == null) {
            return;
        }
        ongoingPrivacyChip2.setPrivacyList(((PrivacyEvent) statusEvent).privacyItems);
    }
}
