package com.android.systemui.media;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000y\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0010*\u0001\r\u0018\u00002\u00020\u0001:\u0005GHIJKB\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u00103\u001a\u00020 2\u0006\u00104\u001a\u000205J\b\u00106\u001a\u00020 H\u0003J\b\u00107\u001a\u00020 H\u0003J\b\u00108\u001a\u00020 H\u0007J\b\u00109\u001a\u00020 H\u0007J\u0010\u0010:\u001a\u00020 2\u0006\u0010;\u001a\u00020<H\u0007J\b\u0010=\u001a\u00020 H\u0007J\u0010\u0010>\u001a\u00020 2\u0006\u0010;\u001a\u00020<H\u0007J\b\u0010?\u001a\u00020 H\u0007J\u000e\u0010@\u001a\u00020 2\u0006\u0010A\u001a\u00020\u0016J\u000e\u0010B\u001a\u00020 2\u0006\u0010A\u001a\u00020.J\u000e\u0010C\u001a\u00020 2\u0006\u0010A\u001a\u00020\u0016J\u000e\u0010D\u001a\u00020 2\u0006\u0010A\u001a\u00020.J\u0012\u0010E\u001a\u00020 2\b\u0010F\u001a\u0004\u0018\u00010\u0011H\u0007R\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\"\u0010\u0012\u001a\u0004\u0018\u00010\u00112\b\u0010\u0005\u001a\u0004\u0018\u00010\u0011@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0013\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R$\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0005\u001a\u00020\u0018@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR \u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001fX.¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u000e¢\u0006\u0002\n\u0000R\u0017\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00060(8F¢\u0006\u0006\u001a\u0004\b)\u0010*R\u001e\u0010+\u001a\u00020\u00182\u0006\u0010\u0005\u001a\u00020\u0018@BX\u000e¢\u0006\b\n\u0000\"\u0004\b,\u0010\u001dR\u0010\u0010-\u001a\u0004\u0018\u00010.X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010/\u001a\u0002008F¢\u0006\u0006\u001a\u0004\b1\u00102¨\u0006L"}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel;", "", "bgExecutor", "Lcom/android/systemui/util/concurrency/RepeatableExecutor;", "(Lcom/android/systemui/util/concurrency/RepeatableExecutor;)V", "value", "Lcom/android/systemui/media/SeekBarViewModel$Progress;", "_data", "set_data", "(Lcom/android/systemui/media/SeekBarViewModel$Progress;)V", "_progress", "Landroidx/lifecycle/MutableLiveData;", "callback", "com/android/systemui/media/SeekBarViewModel$callback$1", "Lcom/android/systemui/media/SeekBarViewModel$callback$1;", "cancel", "Ljava/lang/Runnable;", "Landroid/media/session/MediaController;", "controller", "setController", "(Landroid/media/session/MediaController;)V", "enabledChangeListener", "Lcom/android/systemui/media/SeekBarViewModel$EnabledChangeListener;", "isFalseSeek", "", "listening", "getListening", "()Z", "setListening", "(Z)V", "logSeek", "Lkotlin/Function0;", "", "getLogSeek", "()Lkotlin/jvm/functions/Function0;", "setLogSeek", "(Lkotlin/jvm/functions/Function0;)V", "playbackState", "Landroid/media/session/PlaybackState;", "progress", "Landroidx/lifecycle/LiveData;", "getProgress", "()Landroidx/lifecycle/LiveData;", "scrubbing", "setScrubbing", "scrubbingChangeListener", "Lcom/android/systemui/media/SeekBarViewModel$ScrubbingChangeListener;", "seekBarListener", "Landroid/widget/SeekBar$OnSeekBarChangeListener;", "getSeekBarListener", "()Landroid/widget/SeekBar$OnSeekBarChangeListener;", "attachTouchHandlers", "bar", "Landroid/widget/SeekBar;", "checkIfPollingNeeded", "checkPlaybackPosition", "clearController", "onDestroy", "onSeek", "position", "", "onSeekFalse", "onSeekProgress", "onSeekStarting", "removeEnabledChangeListener", "listener", "removeScrubbingChangeListener", "setEnabledChangeListener", "setScrubbingChangeListener", "updateController", "mediaController", "EnabledChangeListener", "Progress", "ScrubbingChangeListener", "SeekBarChangeListener", "SeekBarTouchListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel {
    private Progress _data = new Progress(false, false, false, false, (Integer) null, 0);
    private final MutableLiveData<Progress> _progress;
    private final RepeatableExecutor bgExecutor;
    private SeekBarViewModel$callback$1 callback;
    private Runnable cancel;
    private MediaController controller;
    private EnabledChangeListener enabledChangeListener;
    private boolean isFalseSeek;
    private boolean listening;
    public Function0<Unit> logSeek;
    /* access modifiers changed from: private */
    public PlaybackState playbackState;
    private boolean scrubbing;
    private ScrubbingChangeListener scrubbingChangeListener;

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel$EnabledChangeListener;", "", "onEnabledChanged", "", "enabled", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SeekBarViewModel.kt */
    public interface EnabledChangeListener {
        void onEnabledChanged(boolean z);
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel$ScrubbingChangeListener;", "", "onScrubbingChanged", "", "scrubbing", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SeekBarViewModel.kt */
    public interface ScrubbingChangeListener {
        void onScrubbingChanged(boolean z);
    }

    @Inject
    public SeekBarViewModel(@Background RepeatableExecutor repeatableExecutor) {
        Intrinsics.checkNotNullParameter(repeatableExecutor, "bgExecutor");
        this.bgExecutor = repeatableExecutor;
        MutableLiveData<Progress> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(this._data);
        this._progress = mutableLiveData;
        this.callback = new SeekBarViewModel$callback$1(this);
        this.listening = true;
    }

    private final void set_data(Progress progress) {
        EnabledChangeListener enabledChangeListener2;
        boolean z = progress.getEnabled() != this._data.getEnabled();
        this._data = progress;
        if (z && (enabledChangeListener2 = this.enabledChangeListener) != null) {
            enabledChangeListener2.onEnabledChanged(progress.getEnabled());
        }
        this._progress.postValue(progress);
    }

    public final LiveData<Progress> getProgress() {
        return this._progress;
    }

    private final void setController(MediaController mediaController) {
        MediaController mediaController2 = this.controller;
        MediaSession.Token token = null;
        MediaSession.Token sessionToken = mediaController2 != null ? mediaController2.getSessionToken() : null;
        if (mediaController != null) {
            token = mediaController.getSessionToken();
        }
        if (!Intrinsics.areEqual((Object) sessionToken, (Object) token)) {
            MediaController mediaController3 = this.controller;
            if (mediaController3 != null) {
                mediaController3.unregisterCallback(this.callback);
            }
            if (mediaController != null) {
                mediaController.registerCallback(this.callback);
            }
            this.controller = mediaController;
        }
    }

    public final boolean getListening() {
        return this.listening;
    }

    public final void setListening(boolean z) {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda7(this, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: _set_listening_$lambda-1  reason: not valid java name */
    public static final void m2833_set_listening_$lambda1(SeekBarViewModel seekBarViewModel, boolean z) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        if (seekBarViewModel.listening != z) {
            seekBarViewModel.listening = z;
            seekBarViewModel.checkIfPollingNeeded();
        }
    }

    private final void setScrubbing(boolean z) {
        if (this.scrubbing != z) {
            this.scrubbing = z;
            checkIfPollingNeeded();
            ScrubbingChangeListener scrubbingChangeListener2 = this.scrubbingChangeListener;
            if (scrubbingChangeListener2 != null) {
                scrubbingChangeListener2.onScrubbingChanged(z);
            }
            set_data(Progress.copy$default(this._data, false, false, false, z, (Integer) null, 0, 55, (Object) null));
        }
    }

    public final Function0<Unit> getLogSeek() {
        Function0<Unit> function0 = this.logSeek;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("logSeek");
        return null;
    }

    public final void setLogSeek(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.logSeek = function0;
    }

    public final void onSeekStarting() {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSeekStarting$lambda-2  reason: not valid java name */
    public static final void m2839onSeekStarting$lambda2(SeekBarViewModel seekBarViewModel) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        seekBarViewModel.setScrubbing(true);
        seekBarViewModel.isFalseSeek = false;
    }

    public final void onSeekProgress(long j) {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda6(this, j));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSeekProgress$lambda-3  reason: not valid java name */
    public static final void m2838onSeekProgress$lambda3(SeekBarViewModel seekBarViewModel, long j) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        if (seekBarViewModel.scrubbing) {
            seekBarViewModel.set_data(Progress.copy$default(seekBarViewModel._data, false, false, false, false, Integer.valueOf((int) j), 0, 47, (Object) null));
        } else {
            seekBarViewModel.onSeek(j);
        }
    }

    public final void onSeekFalse() {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSeekFalse$lambda-4  reason: not valid java name */
    public static final void m2837onSeekFalse$lambda4(SeekBarViewModel seekBarViewModel) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        if (seekBarViewModel.scrubbing) {
            seekBarViewModel.isFalseSeek = true;
        }
    }

    public final void onSeek(long j) {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda1(this, j));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSeek$lambda-5  reason: not valid java name */
    public static final void m2836onSeek$lambda5(SeekBarViewModel seekBarViewModel, long j) {
        MediaController.TransportControls transportControls;
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        if (seekBarViewModel.isFalseSeek) {
            seekBarViewModel.setScrubbing(false);
            seekBarViewModel.checkPlaybackPosition();
            return;
        }
        seekBarViewModel.getLogSeek().invoke();
        MediaController mediaController = seekBarViewModel.controller;
        if (!(mediaController == null || (transportControls = mediaController.getTransportControls()) == null)) {
            transportControls.seekTo(j);
        }
        seekBarViewModel.playbackState = null;
        seekBarViewModel.setScrubbing(false);
    }

    public final void updateController(MediaController mediaController) {
        boolean z;
        setController(mediaController);
        MediaController mediaController2 = this.controller;
        Integer num = null;
        this.playbackState = mediaController2 != null ? mediaController2.getPlaybackState() : null;
        MediaController mediaController3 = this.controller;
        MediaMetadata metadata = mediaController3 != null ? mediaController3.getMetadata() : null;
        PlaybackState playbackState2 = this.playbackState;
        boolean z2 = ((playbackState2 != null ? playbackState2.getActions() : 0) & 256) != 0;
        PlaybackState playbackState3 = this.playbackState;
        if (playbackState3 != null) {
            num = Integer.valueOf((int) playbackState3.getPosition());
        }
        Integer num2 = num;
        int i = metadata != null ? (int) metadata.getLong("android.media.metadata.DURATION") : 0;
        PlaybackState playbackState4 = this.playbackState;
        boolean isPlayingState = NotificationMediaManager.isPlayingState(playbackState4 != null ? playbackState4.getState() : 0);
        PlaybackState playbackState5 = this.playbackState;
        if (playbackState5 != null) {
            if (!(playbackState5 != null && playbackState5.getState() == 0) && i > 0) {
                z = true;
                set_data(new Progress(z, z2, isPlayingState, this.scrubbing, num2, i));
                checkIfPollingNeeded();
            }
        }
        z = false;
        set_data(new Progress(z, z2, isPlayingState, this.scrubbing, num2, i));
        checkIfPollingNeeded();
    }

    public final void clearController() {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: clearController$lambda-6  reason: not valid java name */
    public static final void m2834clearController$lambda6(SeekBarViewModel seekBarViewModel) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        seekBarViewModel.setController((MediaController) null);
        seekBarViewModel.playbackState = null;
        Runnable runnable = seekBarViewModel.cancel;
        if (runnable != null) {
            runnable.run();
        }
        seekBarViewModel.cancel = null;
        seekBarViewModel.set_data(Progress.copy$default(seekBarViewModel._data, false, false, false, false, (Integer) null, 0, 62, (Object) null));
    }

    public final void onDestroy() {
        this.bgExecutor.execute(new SeekBarViewModel$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onDestroy$lambda-7  reason: not valid java name */
    public static final void m2835onDestroy$lambda7(SeekBarViewModel seekBarViewModel) {
        Intrinsics.checkNotNullParameter(seekBarViewModel, "this$0");
        seekBarViewModel.setController((MediaController) null);
        seekBarViewModel.playbackState = null;
        Runnable runnable = seekBarViewModel.cancel;
        if (runnable != null) {
            runnable.run();
        }
        seekBarViewModel.cancel = null;
        seekBarViewModel.scrubbingChangeListener = null;
        seekBarViewModel.enabledChangeListener = null;
    }

    /* access modifiers changed from: private */
    public final void checkPlaybackPosition() {
        int duration = this._data.getDuration();
        PlaybackState playbackState2 = this.playbackState;
        Integer valueOf = playbackState2 != null ? Integer.valueOf((int) SeekBarViewModelKt.computePosition(playbackState2, (long) duration)) : null;
        if (valueOf != null && !Intrinsics.areEqual((Object) this._data.getElapsedTime(), (Object) valueOf)) {
            set_data(Progress.copy$default(this._data, false, false, false, false, valueOf, 0, 47, (Object) null));
        }
    }

    /* access modifiers changed from: private */
    public final void checkIfPollingNeeded() {
        boolean z = false;
        if (this.listening && !this.scrubbing) {
            PlaybackState playbackState2 = this.playbackState;
            if (playbackState2 != null ? SeekBarViewModelKt.isInMotion(playbackState2) : false) {
                z = true;
            }
        }
        if (!z) {
            Runnable runnable = this.cancel;
            if (runnable != null) {
                runnable.run();
            }
            this.cancel = null;
        } else if (this.cancel == null) {
            this.cancel = this.bgExecutor.executeRepeatedly(new SeekBarViewModel$$ExternalSyntheticLambda0(this), 0, 100);
        }
    }

    public final SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBarChangeListener(this);
    }

    public final void attachTouchHandlers(SeekBar seekBar) {
        Intrinsics.checkNotNullParameter(seekBar, "bar");
        seekBar.setOnSeekBarChangeListener(getSeekBarListener());
        seekBar.setOnTouchListener(new SeekBarTouchListener(this, seekBar));
    }

    public final void setScrubbingChangeListener(ScrubbingChangeListener scrubbingChangeListener2) {
        Intrinsics.checkNotNullParameter(scrubbingChangeListener2, "listener");
        this.scrubbingChangeListener = scrubbingChangeListener2;
    }

    public final void removeScrubbingChangeListener(ScrubbingChangeListener scrubbingChangeListener2) {
        Intrinsics.checkNotNullParameter(scrubbingChangeListener2, "listener");
        if (Intrinsics.areEqual((Object) scrubbingChangeListener2, (Object) this.scrubbingChangeListener)) {
            this.scrubbingChangeListener = null;
        }
    }

    public final void setEnabledChangeListener(EnabledChangeListener enabledChangeListener2) {
        Intrinsics.checkNotNullParameter(enabledChangeListener2, "listener");
        this.enabledChangeListener = enabledChangeListener2;
    }

    public final void removeEnabledChangeListener(EnabledChangeListener enabledChangeListener2) {
        Intrinsics.checkNotNullParameter(enabledChangeListener2, "listener");
        if (Intrinsics.areEqual((Object) enabledChangeListener2, (Object) this.enabledChangeListener)) {
            this.enabledChangeListener = null;
        }
    }

    @Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel$SeekBarChangeListener;", "Landroid/widget/SeekBar$OnSeekBarChangeListener;", "viewModel", "Lcom/android/systemui/media/SeekBarViewModel;", "(Lcom/android/systemui/media/SeekBarViewModel;)V", "getViewModel", "()Lcom/android/systemui/media/SeekBarViewModel;", "onProgressChanged", "", "bar", "Landroid/widget/SeekBar;", "progress", "", "fromUser", "", "onStartTrackingTouch", "onStopTrackingTouch", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SeekBarViewModel.kt */
    private static final class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private final SeekBarViewModel viewModel;

        public SeekBarChangeListener(SeekBarViewModel seekBarViewModel) {
            Intrinsics.checkNotNullParameter(seekBarViewModel, "viewModel");
            this.viewModel = seekBarViewModel;
        }

        public final SeekBarViewModel getViewModel() {
            return this.viewModel;
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            if (z) {
                this.viewModel.onSeekProgress((long) i);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel.onSeekStarting();
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel.onSeek((long) seekBar.getProgress());
        }
    }

    @Metadata(mo64986d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J(\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0016J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J(\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u0014H\u0016J\u0010\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u001c\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel$SeekBarTouchListener;", "Landroid/view/View$OnTouchListener;", "Landroid/view/GestureDetector$OnGestureListener;", "viewModel", "Lcom/android/systemui/media/SeekBarViewModel;", "bar", "Landroid/widget/SeekBar;", "(Lcom/android/systemui/media/SeekBarViewModel;Landroid/widget/SeekBar;)V", "detector", "Landroidx/core/view/GestureDetectorCompat;", "flingVelocity", "", "shouldGoToSeekBar", "", "onDown", "event", "Landroid/view/MotionEvent;", "onFling", "eventStart", "velocityX", "", "velocityY", "onLongPress", "", "onScroll", "distanceX", "distanceY", "onShowPress", "onSingleTapUp", "onTouch", "view", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SeekBarViewModel.kt */
    private static final class SeekBarTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private final SeekBar bar;
        private final GestureDetectorCompat detector;
        private final int flingVelocity;
        private boolean shouldGoToSeekBar;
        private final SeekBarViewModel viewModel;

        public void onLongPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        }

        public void onShowPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        }

        public SeekBarTouchListener(SeekBarViewModel seekBarViewModel, SeekBar seekBar) {
            Intrinsics.checkNotNullParameter(seekBarViewModel, "viewModel");
            Intrinsics.checkNotNullParameter(seekBar, "bar");
            this.viewModel = seekBarViewModel;
            this.bar = seekBar;
            this.detector = new GestureDetectorCompat(seekBar.getContext(), this);
            this.flingVelocity = ViewConfiguration.get(seekBar.getContext()).getScaledMinimumFlingVelocity() * 10;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
            if (!Intrinsics.areEqual((Object) view, (Object) this.bar)) {
                return false;
            }
            this.detector.onTouchEvent(motionEvent);
            return !this.shouldGoToSeekBar;
        }

        public boolean onDown(MotionEvent motionEvent) {
            double d;
            double d2;
            ViewParent parent;
            Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
            int paddingLeft = this.bar.getPaddingLeft();
            int paddingRight = this.bar.getPaddingRight();
            int progress = this.bar.getProgress();
            int max = this.bar.getMax() - this.bar.getMin();
            double min = max > 0 ? ((double) (progress - this.bar.getMin())) / ((double) max) : 0.0d;
            int width = (this.bar.getWidth() - paddingLeft) - paddingRight;
            if (this.bar.isLayoutRtl()) {
                d2 = (double) paddingLeft;
                d = ((double) width) * (((double) 1) - min);
            } else {
                d2 = (double) paddingLeft;
                d = ((double) width) * min;
            }
            double d3 = d2 + d;
            long height = (long) (this.bar.getHeight() / 2);
            int round = (int) (Math.round(d3) - height);
            int round2 = (int) (Math.round(d3) + height);
            int round3 = Math.round(motionEvent.getX());
            boolean z = round3 >= round && round3 <= round2;
            this.shouldGoToSeekBar = z;
            if (z && (parent = this.bar.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            return this.shouldGoToSeekBar;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
            this.shouldGoToSeekBar = true;
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "eventStart");
            Intrinsics.checkNotNullParameter(motionEvent2, NotificationCompat.CATEGORY_EVENT);
            return this.shouldGoToSeekBar;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "eventStart");
            Intrinsics.checkNotNullParameter(motionEvent2, NotificationCompat.CATEGORY_EVENT);
            if (Math.abs(f) > ((float) this.flingVelocity) || Math.abs(f2) > ((float) this.flingVelocity)) {
                this.viewModel.onSeekFalse();
            }
            return this.shouldGoToSeekBar;
        }
    }

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0018\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\bHÆ\u0003¢\u0006\u0002\u0010\u000eJ\t\u0010\u001a\u001a\u00020\bHÆ\u0003JL\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\bHÆ\u0001¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00032\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020\bHÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011¨\u0006\""}, mo64987d2 = {"Lcom/android/systemui/media/SeekBarViewModel$Progress;", "", "enabled", "", "seekAvailable", "playing", "scrubbing", "elapsedTime", "", "duration", "(ZZZZLjava/lang/Integer;I)V", "getDuration", "()I", "getElapsedTime", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getEnabled", "()Z", "getPlaying", "getScrubbing", "getSeekAvailable", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ZZZZLjava/lang/Integer;I)Lcom/android/systemui/media/SeekBarViewModel$Progress;", "equals", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SeekBarViewModel.kt */
    public static final class Progress {
        private final int duration;
        private final Integer elapsedTime;
        private final boolean enabled;
        private final boolean playing;
        private final boolean scrubbing;
        private final boolean seekAvailable;

        public static /* synthetic */ Progress copy$default(Progress progress, boolean z, boolean z2, boolean z3, boolean z4, Integer num, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = progress.enabled;
            }
            if ((i2 & 2) != 0) {
                z2 = progress.seekAvailable;
            }
            boolean z5 = z2;
            if ((i2 & 4) != 0) {
                z3 = progress.playing;
            }
            boolean z6 = z3;
            if ((i2 & 8) != 0) {
                z4 = progress.scrubbing;
            }
            boolean z7 = z4;
            if ((i2 & 16) != 0) {
                num = progress.elapsedTime;
            }
            Integer num2 = num;
            if ((i2 & 32) != 0) {
                i = progress.duration;
            }
            return progress.copy(z, z5, z6, z7, num2, i);
        }

        public final boolean component1() {
            return this.enabled;
        }

        public final boolean component2() {
            return this.seekAvailable;
        }

        public final boolean component3() {
            return this.playing;
        }

        public final boolean component4() {
            return this.scrubbing;
        }

        public final Integer component5() {
            return this.elapsedTime;
        }

        public final int component6() {
            return this.duration;
        }

        public final Progress copy(boolean z, boolean z2, boolean z3, boolean z4, Integer num, int i) {
            return new Progress(z, z2, z3, z4, num, i);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Progress)) {
                return false;
            }
            Progress progress = (Progress) obj;
            return this.enabled == progress.enabled && this.seekAvailable == progress.seekAvailable && this.playing == progress.playing && this.scrubbing == progress.scrubbing && Intrinsics.areEqual((Object) this.elapsedTime, (Object) progress.elapsedTime) && this.duration == progress.duration;
        }

        public int hashCode() {
            boolean z = this.enabled;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (z ? 1 : 0) * true;
            boolean z3 = this.seekAvailable;
            if (z3) {
                z3 = true;
            }
            int i2 = (i + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.playing;
            if (z4) {
                z4 = true;
            }
            int i3 = (i2 + (z4 ? 1 : 0)) * 31;
            boolean z5 = this.scrubbing;
            if (!z5) {
                z2 = z5;
            }
            int i4 = (i3 + (z2 ? 1 : 0)) * 31;
            Integer num = this.elapsedTime;
            return ((i4 + (num == null ? 0 : num.hashCode())) * 31) + Integer.hashCode(this.duration);
        }

        public String toString() {
            return "Progress(enabled=" + this.enabled + ", seekAvailable=" + this.seekAvailable + ", playing=" + this.playing + ", scrubbing=" + this.scrubbing + ", elapsedTime=" + this.elapsedTime + ", duration=" + this.duration + ')';
        }

        public Progress(boolean z, boolean z2, boolean z3, boolean z4, Integer num, int i) {
            this.enabled = z;
            this.seekAvailable = z2;
            this.playing = z3;
            this.scrubbing = z4;
            this.elapsedTime = num;
            this.duration = i;
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final boolean getSeekAvailable() {
            return this.seekAvailable;
        }

        public final boolean getPlaying() {
            return this.playing;
        }

        public final boolean getScrubbing() {
            return this.scrubbing;
        }

        public final Integer getElapsedTime() {
            return this.elapsedTime;
        }

        public final int getDuration() {
            return this.duration;
        }
    }
}
