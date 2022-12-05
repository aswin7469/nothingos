package com.android.systemui.media;

import android.graphics.Rect;
import android.util.ArraySet;
import android.view.View;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public final class MediaHost implements MediaHostState {
    public UniqueObjectHostView hostView;
    private boolean inited;
    private boolean listeningToMediaData;
    @NotNull
    private final MediaDataManager mediaDataManager;
    @NotNull
    private final MediaHierarchyManager mediaHierarchyManager;
    @NotNull
    private final MediaHostStatesManager mediaHostStatesManager;
    @NotNull
    private final MediaHostStateHolder state;
    private int location = -1;
    @NotNull
    private ArraySet<Function1<Boolean, Unit>> visibleChangedListeners = new ArraySet<>();
    @NotNull
    private final int[] tmpLocationOnScreen = {0, 0};
    @NotNull
    private final Rect currentBounds = new Rect();
    @NotNull
    private final MediaHost$listener$1 listener = new MediaDataManager.Listener() { // from class: com.android.systemui.media.MediaHost$listener$1
        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(data, "data");
            if (z) {
                MediaHost.this.updateViewVisibility();
            }
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onSmartspaceMediaDataLoaded(@NotNull String key, @NotNull SmartspaceMediaData data, boolean z) {
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(data, "data");
            MediaHost.this.updateViewVisibility();
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onMediaDataRemoved(@NotNull String key) {
            Intrinsics.checkNotNullParameter(key, "key");
            MediaHost.this.updateViewVisibility();
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onSmartspaceMediaDataRemoved(@NotNull String key, boolean z) {
            Intrinsics.checkNotNullParameter(key, "key");
            if (z) {
                MediaHost.this.updateViewVisibility();
            }
        }
    };

    @Override // com.android.systemui.media.MediaHostState
    @NotNull
    public MediaHostState copy() {
        return this.state.copy();
    }

    @Override // com.android.systemui.media.MediaHostState
    @NotNull
    public DisappearParameters getDisappearParameters() {
        return this.state.getDisappearParameters();
    }

    @Override // com.android.systemui.media.MediaHostState
    public float getExpansion() {
        return this.state.getExpansion();
    }

    @Override // com.android.systemui.media.MediaHostState
    public boolean getFalsingProtectionNeeded() {
        return this.state.getFalsingProtectionNeeded();
    }

    @Override // com.android.systemui.media.MediaHostState
    @Nullable
    public MeasurementInput getMeasurementInput() {
        return this.state.getMeasurementInput();
    }

    @Override // com.android.systemui.media.MediaHostState
    public boolean getShowsOnlyActiveMedia() {
        return this.state.getShowsOnlyActiveMedia();
    }

    @Override // com.android.systemui.media.MediaHostState
    public boolean getVisible() {
        return this.state.getVisible();
    }

    public void setDisappearParameters(@NotNull DisappearParameters disappearParameters) {
        Intrinsics.checkNotNullParameter(disappearParameters, "<set-?>");
        this.state.setDisappearParameters(disappearParameters);
    }

    @Override // com.android.systemui.media.MediaHostState
    public void setExpansion(float f) {
        this.state.setExpansion(f);
    }

    public void setFalsingProtectionNeeded(boolean z) {
        this.state.setFalsingProtectionNeeded(z);
    }

    public void setShowsOnlyActiveMedia(boolean z) {
        this.state.setShowsOnlyActiveMedia(z);
    }

    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.media.MediaHost$listener$1] */
    public MediaHost(@NotNull MediaHostStateHolder state, @NotNull MediaHierarchyManager mediaHierarchyManager, @NotNull MediaDataManager mediaDataManager, @NotNull MediaHostStatesManager mediaHostStatesManager) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(mediaHierarchyManager, "mediaHierarchyManager");
        Intrinsics.checkNotNullParameter(mediaDataManager, "mediaDataManager");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager, "mediaHostStatesManager");
        this.state = state;
        this.mediaHierarchyManager = mediaHierarchyManager;
        this.mediaDataManager = mediaDataManager;
        this.mediaHostStatesManager = mediaHostStatesManager;
    }

    @NotNull
    public final UniqueObjectHostView getHostView() {
        UniqueObjectHostView uniqueObjectHostView = this.hostView;
        if (uniqueObjectHostView != null) {
            return uniqueObjectHostView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("hostView");
        throw null;
    }

    public final void setHostView(@NotNull UniqueObjectHostView uniqueObjectHostView) {
        Intrinsics.checkNotNullParameter(uniqueObjectHostView, "<set-?>");
        this.hostView = uniqueObjectHostView;
    }

    public final int getLocation() {
        return this.location;
    }

    @NotNull
    public final Rect getCurrentBounds() {
        getHostView().getLocationOnScreen(this.tmpLocationOnScreen);
        int i = 0;
        int paddingLeft = this.tmpLocationOnScreen[0] + getHostView().getPaddingLeft();
        int paddingTop = this.tmpLocationOnScreen[1] + getHostView().getPaddingTop();
        int width = (this.tmpLocationOnScreen[0] + getHostView().getWidth()) - getHostView().getPaddingRight();
        int height = (this.tmpLocationOnScreen[1] + getHostView().getHeight()) - getHostView().getPaddingBottom();
        if (width < paddingLeft) {
            paddingLeft = 0;
            width = 0;
        }
        if (height < paddingTop) {
            height = 0;
        } else {
            i = paddingTop;
        }
        this.currentBounds.set(paddingLeft, i, width, height);
        return this.currentBounds;
    }

    public final void addVisibilityChangeListener(@NotNull Function1<? super Boolean, Unit> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.visibleChangedListeners.add(listener);
    }

    public final void removeVisibilityChangeListener(@NotNull Function1<? super Boolean, Unit> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.visibleChangedListeners.remove(listener);
    }

    public final void init(final int i) {
        if (this.inited) {
            return;
        }
        this.inited = true;
        this.location = i;
        setHostView(this.mediaHierarchyManager.register(this));
        setListeningToMediaData(true);
        getHostView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.media.MediaHost$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(@Nullable View view) {
                MediaHost.this.setListeningToMediaData(true);
                MediaHost.this.updateViewVisibility();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(@Nullable View view) {
                MediaHost.this.setListeningToMediaData(false);
            }
        });
        getHostView().setMeasurementManager(new UniqueObjectHostView.MeasurementManager() { // from class: com.android.systemui.media.MediaHost$init$2
            @Override // com.android.systemui.util.animation.UniqueObjectHostView.MeasurementManager
            @NotNull
            public MeasurementOutput onMeasure(@NotNull MeasurementInput input) {
                MediaHost.MediaHostStateHolder mediaHostStateHolder;
                MediaHostStatesManager mediaHostStatesManager;
                MediaHost.MediaHostStateHolder mediaHostStateHolder2;
                Intrinsics.checkNotNullParameter(input, "input");
                if (View.MeasureSpec.getMode(input.getWidthMeasureSpec()) == Integer.MIN_VALUE) {
                    input.setWidthMeasureSpec(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(input.getWidthMeasureSpec()), 1073741824));
                }
                mediaHostStateHolder = MediaHost.this.state;
                mediaHostStateHolder.setMeasurementInput(input);
                mediaHostStatesManager = MediaHost.this.mediaHostStatesManager;
                int i2 = i;
                mediaHostStateHolder2 = MediaHost.this.state;
                return mediaHostStatesManager.updateCarouselDimensions(i2, mediaHostStateHolder2);
            }
        });
        this.state.setChangedListener(new MediaHost$init$3(this, i));
        updateViewVisibility();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setListeningToMediaData(boolean z) {
        if (z != this.listeningToMediaData) {
            this.listeningToMediaData = z;
            if (z) {
                this.mediaDataManager.addListener(this.listener);
            } else {
                this.mediaDataManager.removeListener(this.listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateViewVisibility() {
        boolean hasAnyMedia;
        MediaHostStateHolder mediaHostStateHolder = this.state;
        if (getShowsOnlyActiveMedia()) {
            hasAnyMedia = this.mediaDataManager.hasActiveMedia();
        } else {
            hasAnyMedia = this.mediaDataManager.hasAnyMedia();
        }
        mediaHostStateHolder.setVisible(hasAnyMedia);
        int i = getVisible() ? 0 : 8;
        if (i != getHostView().getVisibility()) {
            getHostView().setVisibility(i);
            Iterator<T> it = this.visibleChangedListeners.iterator();
            while (it.hasNext()) {
                ((Function1) it.next()).mo1949invoke(Boolean.valueOf(getVisible()));
            }
        }
    }

    /* compiled from: MediaHost.kt */
    /* loaded from: classes.dex */
    public static final class MediaHostStateHolder implements MediaHostState {
        @Nullable
        private Function0<Unit> changedListener;
        private float expansion;
        private boolean falsingProtectionNeeded;
        @Nullable
        private MeasurementInput measurementInput;
        private boolean showsOnlyActiveMedia;
        private boolean visible = true;
        @NotNull
        private DisappearParameters disappearParameters = new DisappearParameters();
        private int lastDisappearHash = getDisappearParameters().hashCode();

        @Override // com.android.systemui.media.MediaHostState
        @Nullable
        public MeasurementInput getMeasurementInput() {
            return this.measurementInput;
        }

        public void setMeasurementInput(@Nullable MeasurementInput measurementInput) {
            if (!Intrinsics.areEqual(measurementInput == null ? null : Boolean.valueOf(measurementInput.equals(this.measurementInput)), Boolean.TRUE)) {
                this.measurementInput = measurementInput;
                Function0<Unit> function0 = this.changedListener;
                if (function0 == null) {
                    return;
                }
                function0.mo1951invoke();
            }
        }

        @Override // com.android.systemui.media.MediaHostState
        public float getExpansion() {
            return this.expansion;
        }

        @Override // com.android.systemui.media.MediaHostState
        public void setExpansion(float f) {
            if (!Float.valueOf(f).equals(Float.valueOf(this.expansion))) {
                this.expansion = f;
                Function0<Unit> function0 = this.changedListener;
                if (function0 == null) {
                    return;
                }
                function0.mo1951invoke();
            }
        }

        @Override // com.android.systemui.media.MediaHostState
        public boolean getShowsOnlyActiveMedia() {
            return this.showsOnlyActiveMedia;
        }

        public void setShowsOnlyActiveMedia(boolean z) {
            if (!Boolean.valueOf(z).equals(Boolean.valueOf(this.showsOnlyActiveMedia))) {
                this.showsOnlyActiveMedia = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 == null) {
                    return;
                }
                function0.mo1951invoke();
            }
        }

        @Override // com.android.systemui.media.MediaHostState
        public boolean getVisible() {
            return this.visible;
        }

        public void setVisible(boolean z) {
            if (this.visible == z) {
                return;
            }
            this.visible = z;
            Function0<Unit> function0 = this.changedListener;
            if (function0 == null) {
                return;
            }
            function0.mo1951invoke();
        }

        @Override // com.android.systemui.media.MediaHostState
        public boolean getFalsingProtectionNeeded() {
            return this.falsingProtectionNeeded;
        }

        public void setFalsingProtectionNeeded(boolean z) {
            if (this.falsingProtectionNeeded == z) {
                return;
            }
            this.falsingProtectionNeeded = z;
            Function0<Unit> function0 = this.changedListener;
            if (function0 == null) {
                return;
            }
            function0.mo1951invoke();
        }

        @Override // com.android.systemui.media.MediaHostState
        @NotNull
        public DisappearParameters getDisappearParameters() {
            return this.disappearParameters;
        }

        public void setDisappearParameters(@NotNull DisappearParameters value) {
            Intrinsics.checkNotNullParameter(value, "value");
            int hashCode = value.hashCode();
            if (Integer.valueOf(this.lastDisappearHash).equals(Integer.valueOf(hashCode))) {
                return;
            }
            this.disappearParameters = value;
            this.lastDisappearHash = hashCode;
            Function0<Unit> function0 = this.changedListener;
            if (function0 == null) {
                return;
            }
            function0.mo1951invoke();
        }

        public final void setChangedListener(@Nullable Function0<Unit> function0) {
            this.changedListener = function0;
        }

        @Override // com.android.systemui.media.MediaHostState
        @NotNull
        public MediaHostState copy() {
            MediaHostStateHolder mediaHostStateHolder = new MediaHostStateHolder();
            mediaHostStateHolder.setExpansion(getExpansion());
            mediaHostStateHolder.setShowsOnlyActiveMedia(getShowsOnlyActiveMedia());
            MeasurementInput measurementInput = getMeasurementInput();
            MeasurementInput measurementInput2 = null;
            if (measurementInput != null) {
                measurementInput2 = MeasurementInput.copy$default(measurementInput, 0, 0, 3, null);
            }
            mediaHostStateHolder.setMeasurementInput(measurementInput2);
            mediaHostStateHolder.setVisible(getVisible());
            mediaHostStateHolder.setDisappearParameters(getDisappearParameters().deepCopy());
            mediaHostStateHolder.setFalsingProtectionNeeded(getFalsingProtectionNeeded());
            return mediaHostStateHolder;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof MediaHostState)) {
                return false;
            }
            MediaHostState mediaHostState = (MediaHostState) obj;
            if (!Objects.equals(getMeasurementInput(), mediaHostState.getMeasurementInput())) {
                return false;
            }
            return ((getExpansion() > mediaHostState.getExpansion() ? 1 : (getExpansion() == mediaHostState.getExpansion() ? 0 : -1)) == 0) && getShowsOnlyActiveMedia() == mediaHostState.getShowsOnlyActiveMedia() && getVisible() == mediaHostState.getVisible() && getFalsingProtectionNeeded() == mediaHostState.getFalsingProtectionNeeded() && getDisappearParameters().equals(mediaHostState.getDisappearParameters());
        }

        public int hashCode() {
            MeasurementInput measurementInput = getMeasurementInput();
            return ((((((((((measurementInput == null ? 0 : measurementInput.hashCode()) * 31) + Float.hashCode(getExpansion())) * 31) + Boolean.hashCode(getFalsingProtectionNeeded())) * 31) + Boolean.hashCode(getShowsOnlyActiveMedia())) * 31) + (getVisible() ? 1 : 2)) * 31) + getDisappearParameters().hashCode();
        }
    }
}
