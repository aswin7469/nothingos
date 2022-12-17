package com.android.settings.network.telephony;

import com.android.settings.network.telephony.EnabledNetworkModePreferenceController;
import java.util.List;
import java.util.function.IntConsumer;

/* renamed from: com.android.settings.network.telephony.EnabledNetworkModePreferenceController$PreferenceEntriesBuilder$$ExternalSyntheticLambda3 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1102x7d702dd5 implements IntConsumer {
    public final /* synthetic */ EnabledNetworkModePreferenceController.PreferenceEntriesBuilder f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int[] f$3;

    public /* synthetic */ C1102x7d702dd5(EnabledNetworkModePreferenceController.PreferenceEntriesBuilder preferenceEntriesBuilder, List list, boolean z, int[] iArr) {
        this.f$0 = preferenceEntriesBuilder;
        this.f$1 = list;
        this.f$2 = z;
        this.f$3 = iArr;
    }

    public final void accept(int i) {
        this.f$0.lambda$setPreferenceEntries$0(this.f$1, this.f$2, this.f$3, i);
    }
}
