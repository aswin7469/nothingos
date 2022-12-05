package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.R$string;
import com.android.systemui.privacy.PrivacyChipBuilder;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SystemEventCoordinator.kt */
/* loaded from: classes.dex */
public final class SystemEventCoordinator$notifyPrivacyItemsChanged$1 extends Lambda implements Function0<String> {
    final /* synthetic */ PrivacyEvent $event;
    final /* synthetic */ SystemEventCoordinator this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SystemEventCoordinator$notifyPrivacyItemsChanged$1(SystemEventCoordinator systemEventCoordinator, PrivacyEvent privacyEvent) {
        super(0);
        this.this$0 = systemEventCoordinator;
        this.$event = privacyEvent;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final String mo1951invoke() {
        Context context;
        Context context2;
        context = this.this$0.context;
        String joinTypes = new PrivacyChipBuilder(context, this.$event.getPrivacyItems()).joinTypes();
        context2 = this.this$0.context;
        return context2.getString(R$string.ongoing_privacy_chip_content_multiple_apps, joinTypes);
    }
}
