package com.android.settings.dream;

import com.android.settingslib.dream.DreamBackend;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class CurrentDreamPreferenceController$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ CurrentDreamPreferenceController$$ExternalSyntheticLambda1 INSTANCE = new CurrentDreamPreferenceController$$ExternalSyntheticLambda1();

    private /* synthetic */ CurrentDreamPreferenceController$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z;
        z = ((DreamBackend.DreamInfo) obj).isActive;
        return z;
    }
}
