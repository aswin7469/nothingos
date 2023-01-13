package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0006\u0007J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0004H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/unfold/FoldStateLoggingProvider;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/unfold/FoldStateLoggingProvider$FoldStateLoggingListener;", "init", "", "uninit", "FoldStateLoggingListener", "LoggedFoldedStates", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FoldStateLoggingProvider.kt */
public interface FoldStateLoggingProvider extends CallbackController<FoldStateLoggingListener> {

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/unfold/FoldStateLoggingProvider$FoldStateLoggingListener;", "", "onFoldUpdate", "", "foldStateUpdate", "Lcom/android/systemui/unfold/FoldStateChange;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: FoldStateLoggingProvider.kt */
    public interface FoldStateLoggingListener {
        void onFoldUpdate(FoldStateChange foldStateChange);
    }

    @Metadata(mo65042d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo65043d2 = {"Lcom/android/systemui/unfold/FoldStateLoggingProvider$LoggedFoldedStates;", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    /* compiled from: FoldStateLoggingProvider.kt */
    public @interface LoggedFoldedStates {
    }

    void init();

    void uninit();
}
