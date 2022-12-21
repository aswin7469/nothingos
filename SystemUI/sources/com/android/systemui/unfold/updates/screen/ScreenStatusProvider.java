package com.android.systemui.unfold.updates.screen;

import com.android.systemui.statusbar.policy.CallbackController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0003ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider$ScreenListener;", "ScreenListener", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ScreenStatusProvider.kt */
public interface ScreenStatusProvider extends CallbackController<ScreenListener> {

    @Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider$ScreenListener;", "", "onScreenTurnedOn", "", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ScreenStatusProvider.kt */
    public interface ScreenListener {
        void onScreenTurnedOn();
    }
}
