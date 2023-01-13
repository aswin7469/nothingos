package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.privacy.OngoingPrivacyChip;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/privacy/OngoingPrivacyChip;", "context", "Landroid/content/Context;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusEvent.kt */
final class PrivacyEvent$viewCreator$1 extends Lambda implements Function1<Context, OngoingPrivacyChip> {
    final /* synthetic */ PrivacyEvent this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PrivacyEvent$viewCreator$1(PrivacyEvent privacyEvent) {
        super(1);
        this.this$0 = privacyEvent;
    }

    public final OngoingPrivacyChip invoke(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        View inflate = LayoutInflater.from(context).inflate(C1894R.layout.ongoing_privacy_chip, (ViewGroup) null);
        if (inflate != null) {
            OngoingPrivacyChip ongoingPrivacyChip = (OngoingPrivacyChip) inflate;
            ongoingPrivacyChip.setPrivacyList(this.this$0.getPrivacyItems());
            this.this$0.privacyChip = ongoingPrivacyChip;
            return ongoingPrivacyChip;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.privacy.OngoingPrivacyChip");
    }
}
