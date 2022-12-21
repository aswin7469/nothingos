package com.android.systemui.p012qs.tileimpl;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010J\b\u0010\u000e\u001a\u00020\u000fH&R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\tX¦\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0011À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/qs/tileimpl/HeightOverrideable;", "", "heightOverride", "", "getHeightOverride", "()I", "setHeightOverride", "(I)V", "squishinessFraction", "", "getSquishinessFraction", "()F", "setSquishinessFraction", "(F)V", "resetOverride", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.tileimpl.HeightOverrideable */
/* compiled from: HeightOverrideable.kt */
public interface HeightOverrideable {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final int NO_OVERRIDE = -1;

    int getHeightOverride();

    float getSquishinessFraction();

    void resetOverride();

    void setHeightOverride(int i);

    void setSquishinessFraction(float f);

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/qs/tileimpl/HeightOverrideable$Companion;", "", "()V", "NO_OVERRIDE", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.tileimpl.HeightOverrideable$Companion */
    /* compiled from: HeightOverrideable.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final int NO_OVERRIDE = -1;

        private Companion() {
        }
    }
}
