package com.android.systemui.media;

import android.os.Trace;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.animation.MeasurementOutput;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001cB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0005J\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rJ\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0005J\u000e\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rJ\u0016\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000fJ\u0016\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004X\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u000f0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b¨\u0006\u001d"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHostStatesManager;", "", "()V", "callbacks", "", "Lcom/android/systemui/media/MediaHostStatesManager$Callback;", "carouselSizes", "", "", "Lcom/android/systemui/util/animation/MeasurementOutput;", "getCarouselSizes", "()Ljava/util/Map;", "controllers", "Lcom/android/systemui/media/MediaViewController;", "mediaHostStates", "Lcom/android/systemui/media/MediaHostState;", "getMediaHostStates", "addCallback", "", "callback", "addController", "controller", "removeCallback", "removeController", "updateCarouselDimensions", "location", "hostState", "updateHostState", "Callback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHostStatesManager.kt */
public final class MediaHostStatesManager {
    private final Set<Callback> callbacks = new LinkedHashSet();
    private final Map<Integer, MeasurementOutput> carouselSizes = new LinkedHashMap();
    private final Set<MediaViewController> controllers = new LinkedHashSet();
    private final Map<Integer, MediaHostState> mediaHostStates = new LinkedHashMap();

    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHostStatesManager$Callback;", "", "onHostStateChanged", "", "location", "", "mediaHostState", "Lcom/android/systemui/media/MediaHostState;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaHostStatesManager.kt */
    public interface Callback {
        void onHostStateChanged(int i, MediaHostState mediaHostState);
    }

    public final Map<Integer, MeasurementOutput> getCarouselSizes() {
        return this.carouselSizes;
    }

    public final Map<Integer, MediaHostState> getMediaHostStates() {
        return this.mediaHostStates;
    }

    public final void addCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callbacks.add(callback);
    }

    public final void removeCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callbacks.remove(callback);
    }

    public final void addController(MediaViewController mediaViewController) {
        Intrinsics.checkNotNullParameter(mediaViewController, "controller");
        this.controllers.add(mediaViewController);
    }

    public final void removeController(MediaViewController mediaViewController) {
        Intrinsics.checkNotNullParameter(mediaViewController, "controller");
        this.controllers.remove(mediaViewController);
    }

    public final void updateHostState(int i, MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        Trace.beginSection("MediaHostStatesManager#updateHostState");
        try {
            if (!mediaHostState.equals(this.mediaHostStates.get(Integer.valueOf(i)))) {
                MediaHostState copy = mediaHostState.copy();
                this.mediaHostStates.put(Integer.valueOf(i), copy);
                updateCarouselDimensions(i, mediaHostState);
                for (MediaViewController stateCallback : this.controllers) {
                    stateCallback.getStateCallback().onHostStateChanged(i, copy);
                }
                for (Callback onHostStateChanged : this.callbacks) {
                    onHostStateChanged.onHostStateChanged(i, copy);
                }
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public final MeasurementOutput updateCarouselDimensions(int i, MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        Trace.beginSection("MediaHostStatesManager#updateCarouselDimensions");
        try {
            MeasurementOutput measurementOutput = new MeasurementOutput(0, 0);
            for (MediaViewController measurementsForState : this.controllers) {
                MeasurementOutput measurementsForState2 = measurementsForState.getMeasurementsForState(mediaHostState);
                if (measurementsForState2 != null) {
                    if (measurementsForState2.getMeasuredHeight() > measurementOutput.getMeasuredHeight()) {
                        measurementOutput.setMeasuredHeight(measurementsForState2.getMeasuredHeight());
                    }
                    if (measurementsForState2.getMeasuredWidth() > measurementOutput.getMeasuredWidth()) {
                        measurementOutput.setMeasuredWidth(measurementsForState2.getMeasuredWidth());
                    }
                }
            }
            this.carouselSizes.put(Integer.valueOf(i), measurementOutput);
            return measurementOutput;
        } finally {
            Trace.endSection();
        }
    }
}
