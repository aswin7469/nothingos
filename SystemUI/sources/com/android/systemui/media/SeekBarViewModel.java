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
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel {
    @NotNull
    private final MutableLiveData<Progress> _progress;
    @NotNull
    private final RepeatableExecutor bgExecutor;
    @Nullable
    private Runnable cancel;
    @Nullable
    private MediaController controller;
    private boolean isFalseSeek;
    public Function0<Unit> logSmartspaceClick;
    @Nullable
    private PlaybackState playbackState;
    private boolean scrubbing;
    @NotNull
    private Progress _data = new Progress(false, false, null, 0);
    @NotNull
    private SeekBarViewModel$callback$1 callback = new MediaController.Callback() { // from class: com.android.systemui.media.SeekBarViewModel$callback$1
        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(@Nullable PlaybackState playbackState) {
            PlaybackState playbackState2;
            PlaybackState playbackState3;
            SeekBarViewModel.this.playbackState = playbackState;
            playbackState2 = SeekBarViewModel.this.playbackState;
            if (playbackState2 != null) {
                Integer num = 0;
                playbackState3 = SeekBarViewModel.this.playbackState;
                if (!num.equals(playbackState3)) {
                    SeekBarViewModel.this.checkIfPollingNeeded();
                    return;
                }
            }
            SeekBarViewModel.this.clearController();
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            SeekBarViewModel.this.clearController();
        }
    };
    private boolean listening = true;

    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.media.SeekBarViewModel$callback$1] */
    public SeekBarViewModel(@NotNull RepeatableExecutor bgExecutor) {
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        this.bgExecutor = bgExecutor;
        MutableLiveData<Progress> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(this._data);
        Unit unit = Unit.INSTANCE;
        this._progress = mutableLiveData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void set_data(Progress progress) {
        this._data = progress;
        this._progress.postValue(progress);
    }

    @NotNull
    public final LiveData<Progress> getProgress() {
        return this._progress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setController(MediaController mediaController) {
        MediaController mediaController2 = this.controller;
        MediaSession.Token token = null;
        MediaSession.Token sessionToken = mediaController2 == null ? null : mediaController2.getSessionToken();
        if (mediaController != null) {
            token = mediaController.getSessionToken();
        }
        if (!Intrinsics.areEqual(sessionToken, token)) {
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

    public final void setListening(final boolean z) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$listening$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z2;
                z2 = SeekBarViewModel.this.listening;
                boolean z3 = z;
                if (z2 != z3) {
                    SeekBarViewModel.this.listening = z3;
                    SeekBarViewModel.this.checkIfPollingNeeded();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setScrubbing(boolean z) {
        if (this.scrubbing != z) {
            this.scrubbing = z;
            checkIfPollingNeeded();
        }
    }

    @NotNull
    public final Function0<Unit> getLogSmartspaceClick() {
        Function0<Unit> function0 = this.logSmartspaceClick;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("logSmartspaceClick");
        throw null;
    }

    public final void setLogSmartspaceClick(@NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.logSmartspaceClick = function0;
    }

    public final void onSeekStarting() {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekStarting$1
            @Override // java.lang.Runnable
            public final void run() {
                SeekBarViewModel.this.setScrubbing(true);
                SeekBarViewModel.this.isFalseSeek = false;
            }
        });
    }

    public final void onSeekProgress(final long j) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekProgress$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                SeekBarViewModel.Progress progress;
                z = SeekBarViewModel.this.scrubbing;
                if (z) {
                    SeekBarViewModel seekBarViewModel = SeekBarViewModel.this;
                    progress = seekBarViewModel._data;
                    seekBarViewModel.set_data(SeekBarViewModel.Progress.copy$default(progress, false, false, Integer.valueOf((int) j), 0, 11, null));
                }
            }
        });
    }

    public final void onSeekFalse() {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekFalse$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                z = SeekBarViewModel.this.scrubbing;
                if (z) {
                    SeekBarViewModel.this.isFalseSeek = true;
                }
            }
        });
    }

    public final void onSeek(final long j) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeek$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                MediaController mediaController;
                MediaController.TransportControls transportControls;
                z = SeekBarViewModel.this.isFalseSeek;
                if (z) {
                    SeekBarViewModel.this.setScrubbing(false);
                    SeekBarViewModel.this.checkPlaybackPosition();
                    return;
                }
                SeekBarViewModel.this.getLogSmartspaceClick().mo1951invoke();
                mediaController = SeekBarViewModel.this.controller;
                if (mediaController != null && (transportControls = mediaController.getTransportControls()) != null) {
                    transportControls.seekTo(j);
                }
                SeekBarViewModel.this.playbackState = null;
                SeekBarViewModel.this.setScrubbing(false);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x006e, code lost:
        if (r0.intValue() != 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0070, code lost:
        if (r9 > 0) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void updateController(@Nullable MediaController mediaController) {
        setController(mediaController);
        MediaController mediaController2 = this.controller;
        Integer num = null;
        this.playbackState = mediaController2 == null ? null : mediaController2.getPlaybackState();
        MediaController mediaController3 = this.controller;
        MediaMetadata metadata = mediaController3 == null ? null : mediaController3.getMetadata();
        PlaybackState playbackState = this.playbackState;
        boolean z = true;
        boolean z2 = ((playbackState == null ? 0L : playbackState.getActions()) & 256) != 0;
        PlaybackState playbackState2 = this.playbackState;
        Integer valueOf = playbackState2 == null ? null : Integer.valueOf((int) playbackState2.getPosition());
        Long valueOf2 = metadata == null ? null : Long.valueOf(metadata.getLong("android.media.metadata.DURATION"));
        int longValue = valueOf2 == null ? 0 : (int) valueOf2.longValue();
        PlaybackState playbackState3 = this.playbackState;
        if (playbackState3 != null) {
            if (playbackState3 != null) {
                num = Integer.valueOf(playbackState3.getState());
            }
            if (num != null) {
            }
        }
        z = false;
        set_data(new Progress(z, z2, valueOf, longValue));
        checkIfPollingNeeded();
    }

    public final void clearController() {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$clearController$1
            @Override // java.lang.Runnable
            public final void run() {
                Runnable runnable;
                SeekBarViewModel.Progress progress;
                SeekBarViewModel.this.setController(null);
                SeekBarViewModel.this.playbackState = null;
                runnable = SeekBarViewModel.this.cancel;
                if (runnable != null) {
                    runnable.run();
                }
                SeekBarViewModel.this.cancel = null;
                SeekBarViewModel seekBarViewModel = SeekBarViewModel.this;
                progress = seekBarViewModel._data;
                seekBarViewModel.set_data(SeekBarViewModel.Progress.copy$default(progress, false, false, null, 0, 14, null));
            }
        });
    }

    public final void onDestroy() {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onDestroy$1
            @Override // java.lang.Runnable
            public final void run() {
                Runnable runnable;
                SeekBarViewModel.this.setController(null);
                SeekBarViewModel.this.playbackState = null;
                runnable = SeekBarViewModel.this.cancel;
                if (runnable != null) {
                    runnable.run();
                }
                SeekBarViewModel.this.cancel = null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkPlaybackPosition() {
        long computePosition;
        Integer valueOf;
        int duration = this._data.getDuration();
        PlaybackState playbackState = this.playbackState;
        if (playbackState == null) {
            valueOf = null;
        } else {
            computePosition = SeekBarViewModelKt.computePosition(playbackState, duration);
            valueOf = Integer.valueOf((int) computePosition);
        }
        Integer num = valueOf;
        if (num == null || Intrinsics.areEqual(this._data.getElapsedTime(), num)) {
            return;
        }
        set_data(Progress.copy$default(this._data, false, false, num, 0, 11, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkIfPollingNeeded() {
        boolean z = false;
        if (this.listening && !this.scrubbing) {
            PlaybackState playbackState = this.playbackState;
            if (playbackState == null ? false : SeekBarViewModelKt.isInMotion(playbackState)) {
                z = true;
            }
        }
        if (z) {
            if (this.cancel != null) {
                return;
            }
            this.cancel = this.bgExecutor.executeRepeatedly(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1
                @Override // java.lang.Runnable
                public final void run() {
                    SeekBarViewModel.this.checkPlaybackPosition();
                }
            }, 0L, 100L);
            return;
        }
        Runnable runnable = this.cancel;
        if (runnable != null) {
            runnable.run();
        }
        this.cancel = null;
    }

    @NotNull
    public final SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBarChangeListener(this);
    }

    public final void attachTouchHandlers(@NotNull SeekBar bar) {
        Intrinsics.checkNotNullParameter(bar, "bar");
        bar.setOnSeekBarChangeListener(getSeekBarListener());
        bar.setOnTouchListener(new SeekBarTouchListener(this, bar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @NotNull
        private final SeekBarViewModel viewModel;

        public SeekBarChangeListener(@NotNull SeekBarViewModel viewModel) {
            Intrinsics.checkNotNullParameter(viewModel, "viewModel");
            this.viewModel = viewModel;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(@NotNull SeekBar bar, int i, boolean z) {
            Intrinsics.checkNotNullParameter(bar, "bar");
            if (z) {
                this.viewModel.onSeekProgress(i);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(@NotNull SeekBar bar) {
            Intrinsics.checkNotNullParameter(bar, "bar");
            this.viewModel.onSeekStarting();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(@NotNull SeekBar bar) {
            Intrinsics.checkNotNullParameter(bar, "bar");
            this.viewModel.onSeek(bar.getProgress());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
        @NotNull
        private final SeekBar bar;
        @NotNull
        private final GestureDetectorCompat detector;
        private final int flingVelocity;
        private boolean shouldGoToSeekBar;
        @NotNull
        private final SeekBarViewModel viewModel;

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(@NotNull MotionEvent event) {
            Intrinsics.checkNotNullParameter(event, "event");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(@NotNull MotionEvent event) {
            Intrinsics.checkNotNullParameter(event, "event");
        }

        public SeekBarTouchListener(@NotNull SeekBarViewModel viewModel, @NotNull SeekBar bar) {
            Intrinsics.checkNotNullParameter(viewModel, "viewModel");
            Intrinsics.checkNotNullParameter(bar, "bar");
            this.viewModel = viewModel;
            this.bar = bar;
            this.detector = new GestureDetectorCompat(bar.getContext(), this);
            this.flingVelocity = ViewConfiguration.get(bar.getContext()).getScaledMinimumFlingVelocity() * 10;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(@NotNull View view, @NotNull MotionEvent event) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(event, "event");
            if (!Intrinsics.areEqual(view, this.bar)) {
                return false;
            }
            this.detector.onTouchEvent(event);
            return !this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(@NotNull MotionEvent event) {
            double d;
            double d2;
            ViewParent parent;
            Intrinsics.checkNotNullParameter(event, "event");
            int paddingLeft = this.bar.getPaddingLeft();
            int paddingRight = this.bar.getPaddingRight();
            int progress = this.bar.getProgress();
            int max = this.bar.getMax() - this.bar.getMin();
            double min = max > 0 ? (progress - this.bar.getMin()) / max : 0.0d;
            int width = (this.bar.getWidth() - paddingLeft) - paddingRight;
            if (this.bar.isLayoutRtl()) {
                d = paddingLeft;
                d2 = width * (1 - min);
            } else {
                d = paddingLeft;
                d2 = width * min;
            }
            double d3 = d + d2;
            long height = this.bar.getHeight() / 2;
            int round = (int) (Math.round(d3) - height);
            int round2 = (int) (Math.round(d3) + height);
            int round3 = Math.round(event.getX());
            boolean z = round3 >= round && round3 <= round2;
            this.shouldGoToSeekBar = z;
            if (z && (parent = this.bar.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(@NotNull MotionEvent event) {
            Intrinsics.checkNotNullParameter(event, "event");
            this.shouldGoToSeekBar = true;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(@NotNull MotionEvent eventStart, @NotNull MotionEvent event, float f, float f2) {
            Intrinsics.checkNotNullParameter(eventStart, "eventStart");
            Intrinsics.checkNotNullParameter(event, "event");
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(@NotNull MotionEvent eventStart, @NotNull MotionEvent event, float f, float f2) {
            Intrinsics.checkNotNullParameter(eventStart, "eventStart");
            Intrinsics.checkNotNullParameter(event, "event");
            if (Math.abs(f) > this.flingVelocity || Math.abs(f2) > this.flingVelocity) {
                this.viewModel.onSeekFalse();
            }
            return this.shouldGoToSeekBar;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Progress {
        private final int duration;
        @Nullable
        private final Integer elapsedTime;
        private final boolean enabled;
        private final boolean seekAvailable;

        public static /* synthetic */ Progress copy$default(Progress progress, boolean z, boolean z2, Integer num, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = progress.enabled;
            }
            if ((i2 & 2) != 0) {
                z2 = progress.seekAvailable;
            }
            if ((i2 & 4) != 0) {
                num = progress.elapsedTime;
            }
            if ((i2 & 8) != 0) {
                i = progress.duration;
            }
            return progress.copy(z, z2, num, i);
        }

        @NotNull
        public final Progress copy(boolean z, boolean z2, @Nullable Integer num, int i) {
            return new Progress(z, z2, num, i);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Progress)) {
                return false;
            }
            Progress progress = (Progress) obj;
            return this.enabled == progress.enabled && this.seekAvailable == progress.seekAvailable && Intrinsics.areEqual(this.elapsedTime, progress.elapsedTime) && this.duration == progress.duration;
        }

        public int hashCode() {
            boolean z = this.enabled;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = i2 * 31;
            boolean z2 = this.seekAvailable;
            if (!z2) {
                i = z2 ? 1 : 0;
            }
            int i5 = (i4 + i) * 31;
            Integer num = this.elapsedTime;
            return ((i5 + (num == null ? 0 : num.hashCode())) * 31) + Integer.hashCode(this.duration);
        }

        @NotNull
        public String toString() {
            return "Progress(enabled=" + this.enabled + ", seekAvailable=" + this.seekAvailable + ", elapsedTime=" + this.elapsedTime + ", duration=" + this.duration + ')';
        }

        public Progress(boolean z, boolean z2, @Nullable Integer num, int i) {
            this.enabled = z;
            this.seekAvailable = z2;
            this.elapsedTime = num;
            this.duration = i;
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final boolean getSeekAvailable() {
            return this.seekAvailable;
        }

        @Nullable
        public final Integer getElapsedTime() {
            return this.elapsedTime;
        }

        public final int getDuration() {
            return this.duration;
        }
    }
}
