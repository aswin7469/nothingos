package com.android.systemui.media;

import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\bf\u0018\u0000  2\u00020\u0001:\u0001 J\b\u0010\u001f\u001a\u00020\u0000H&R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\tX¦\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0018\u0010\u000e\u001a\u00020\u000fX¦\u000e¢\u0006\f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0015X¦\u000e¢\u0006\f\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u0018\u0010\u001a\u001a\u00020\u000fX¦\u000e¢\u0006\f\u001a\u0004\b\u001b\u0010\u0011\"\u0004\b\u001c\u0010\u0013R\u0012\u0010\u001d\u001a\u00020\u000fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u0011ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006!À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHostState;", "", "disappearParameters", "Lcom/android/systemui/util/animation/DisappearParameters;", "getDisappearParameters", "()Lcom/android/systemui/util/animation/DisappearParameters;", "setDisappearParameters", "(Lcom/android/systemui/util/animation/DisappearParameters;)V", "expansion", "", "getExpansion", "()F", "setExpansion", "(F)V", "falsingProtectionNeeded", "", "getFalsingProtectionNeeded", "()Z", "setFalsingProtectionNeeded", "(Z)V", "measurementInput", "Lcom/android/systemui/util/animation/MeasurementInput;", "getMeasurementInput", "()Lcom/android/systemui/util/animation/MeasurementInput;", "setMeasurementInput", "(Lcom/android/systemui/util/animation/MeasurementInput;)V", "showsOnlyActiveMedia", "getShowsOnlyActiveMedia", "setShowsOnlyActiveMedia", "visible", "getVisible", "copy", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHost.kt */
public interface MediaHostState {
    public static final float COLLAPSED = 0.0f;
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final float EXPANDED = 1.0f;

    MediaHostState copy();

    DisappearParameters getDisappearParameters();

    float getExpansion();

    boolean getFalsingProtectionNeeded();

    MeasurementInput getMeasurementInput();

    boolean getShowsOnlyActiveMedia();

    boolean getVisible();

    void setDisappearParameters(DisappearParameters disappearParameters);

    void setExpansion(float f);

    void setFalsingProtectionNeeded(boolean z);

    void setMeasurementInput(MeasurementInput measurementInput);

    void setShowsOnlyActiveMedia(boolean z);

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHostState$Companion;", "", "()V", "COLLAPSED", "", "EXPANDED", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaHost.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final float COLLAPSED = 0.0f;
        public static final float EXPANDED = 1.0f;

        private Companion() {
        }
    }
}
