package com.android.systemui.statusbar.phone;

import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, mo64987d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarContentInsetsProvider.kt */
final class StatusBarContentInsetsProvider$isPrivacyDotEnabled$2 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ StatusBarContentInsetsProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(0);
        this.this$0 = statusBarContentInsetsProvider;
    }

    public final Boolean invoke() {
        return Boolean.valueOf(this.this$0.getContext().getResources().getBoolean(C1893R.bool.config_enablePrivacyDot));
    }
}
