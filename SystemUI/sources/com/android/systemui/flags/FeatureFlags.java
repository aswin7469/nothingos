package com.android.systemui.flags;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0006H&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\tH&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\nH&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u000bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/flags/FeatureFlags;", "Lcom/android/systemui/flags/FlagListenable;", "getString", "", "flag", "Lcom/android/systemui/flags/ResourceStringFlag;", "Lcom/android/systemui/flags/StringFlag;", "isEnabled", "", "Lcom/android/systemui/flags/BooleanFlag;", "Lcom/android/systemui/flags/ResourceBooleanFlag;", "Lcom/android/systemui/flags/SysPropBooleanFlag;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FeatureFlags.kt */
public interface FeatureFlags extends FlagListenable {
    String getString(ResourceStringFlag resourceStringFlag);

    String getString(StringFlag stringFlag);

    boolean isEnabled(BooleanFlag booleanFlag);

    boolean isEnabled(ResourceBooleanFlag resourceBooleanFlag);

    boolean isEnabled(SysPropBooleanFlag sysPropBooleanFlag);
}
