package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0012\u0010 \u001a\u00020\u00032\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u0016J\b\u0010\"\u001a\u00020#H\u0016J\u0012\u0010$\u001a\u00020%2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u0016R\u0014\u0010\u0005\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\tXD¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R \u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R3\u0010\u0016\u001a!\u0012\u0013\u0012\u00110\u0018¢\u0006\f\b\u0019\u0012\b\b\u001a\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u0017j\u0002`\u001dX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u0006&"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/PrivacyEvent;", "Lcom/android/systemui/statusbar/events/StatusEvent;", "showAnimation", "", "(Z)V", "forceVisible", "getForceVisible", "()Z", "priority", "", "getPriority", "()I", "privacyChip", "Lcom/android/systemui/privacy/OngoingPrivacyChip;", "privacyItems", "", "Lcom/android/systemui/privacy/PrivacyItem;", "getPrivacyItems", "()Ljava/util/List;", "setPrivacyItems", "(Ljava/util/List;)V", "getShowAnimation", "viewCreator", "Lkotlin/Function1;", "Landroid/content/Context;", "Lkotlin/ParameterName;", "name", "context", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "Lcom/android/systemui/statusbar/events/ViewCreator;", "getViewCreator", "()Lkotlin/jvm/functions/Function1;", "shouldUpdateFromEvent", "other", "toString", "", "updateFromEvent", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusEvent.kt */
public final class PrivacyEvent implements StatusEvent {
    private final boolean forceVisible;
    private final int priority;
    /* access modifiers changed from: private */
    public OngoingPrivacyChip privacyChip;
    private List<PrivacyItem> privacyItems;
    private final boolean showAnimation;
    private final Function1<Context, BackgroundAnimatableView> viewCreator;

    public PrivacyEvent() {
        this(false, 1, (DefaultConstructorMarker) null);
    }

    public PrivacyEvent(boolean z) {
        this.showAnimation = z;
        this.priority = 100;
        this.forceVisible = true;
        this.privacyItems = CollectionsKt.emptyList();
        this.viewCreator = new PrivacyEvent$viewCreator$1(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ PrivacyEvent(boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? true : z);
    }

    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean getForceVisible() {
        return this.forceVisible;
    }

    public final List<PrivacyItem> getPrivacyItems() {
        return this.privacyItems;
    }

    public final void setPrivacyItems(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.privacyItems = list;
    }

    public Function1<Context, BackgroundAnimatableView> getViewCreator() {
        return this.viewCreator;
    }

    public String toString() {
        return getClass().getSimpleName() + "(forceVisible=" + getForceVisible() + ", privacyItems=" + this.privacyItems + ')';
    }

    public boolean shouldUpdateFromEvent(StatusEvent statusEvent) {
        return (statusEvent instanceof PrivacyEvent) && !Intrinsics.areEqual((Object) ((PrivacyEvent) statusEvent).privacyItems, (Object) this.privacyItems);
    }

    public void updateFromEvent(StatusEvent statusEvent) {
        if (statusEvent instanceof PrivacyEvent) {
            PrivacyEvent privacyEvent = (PrivacyEvent) statusEvent;
            this.privacyItems = privacyEvent.privacyItems;
            OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
            if (ongoingPrivacyChip != null) {
                ongoingPrivacyChip.setPrivacyList(privacyEvent.privacyItems);
            }
        }
    }
}
