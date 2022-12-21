package com.android.systemui.unfold.updates;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\t\nJ\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\u0007H&R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/updates/FoldStateProvider;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/unfold/updates/FoldStateProvider$FoldUpdatesListener;", "isFinishedOpening", "", "()Z", "start", "", "stop", "FoldUpdate", "FoldUpdatesListener", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FoldStateProvider.kt */
public interface FoldStateProvider extends CallbackController<FoldUpdatesListener> {

    @Metadata(mo64986d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo64987d2 = {"Lcom/android/systemui/unfold/updates/FoldStateProvider$FoldUpdate;", "", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    /* compiled from: FoldStateProvider.kt */
    public @interface FoldUpdate {
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/updates/FoldStateProvider$FoldUpdatesListener;", "", "onFoldUpdate", "", "update", "", "onHingeAngleUpdate", "angle", "", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FoldStateProvider.kt */
    public interface FoldUpdatesListener {
        void onFoldUpdate(int i);

        void onHingeAngleUpdate(float f);
    }

    boolean isFinishedOpening();

    void start();

    void stop();
}
