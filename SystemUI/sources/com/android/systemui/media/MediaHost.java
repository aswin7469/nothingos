package com.android.systemui.media;

import android.graphics.Rect;
import android.util.ArraySet;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000{\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\t*\u0001+\u0018\u00002\u00020\u0001:\u0001KB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001a\u0010D\u001a\u00020C2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020C0BJ\t\u0010E\u001a\u00020\u0001H\u0001J\u000e\u0010F\u001a\u00020C2\u0006\u00100\u001a\u00020/J\u001a\u0010G\u001a\u00020C2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020C0BJ\u0010\u0010H\u001a\u00020C2\u0006\u0010I\u001a\u00020\u001eH\u0002J\b\u0010J\u001a\u00020CH\u0002R\u0013\u0010\u000b\u001a\u00020\f8F¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0018\u0010\u0011\u001a\u00020\u0012X\u000f¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0018\u0010\u0017\u001a\u00020\u0018X\u000f¢\u0006\f\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0018\u0010\u001d\u001a\u00020\u001eX\u000f¢\u0006\f\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020$X.¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\u001eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u00020+X\u0004¢\u0006\u0004\n\u0002\u0010,R\u000e\u0010-\u001a\u00020\u001eX\u000e¢\u0006\u0002\n\u0000R\u001e\u00100\u001a\u00020/2\u0006\u0010.\u001a\u00020/@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u001a\u00103\u001a\u0004\u0018\u000104X\u000f¢\u0006\f\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0018\u00109\u001a\u00020\u001eX\u000f¢\u0006\f\u001a\u0004\b:\u0010 \"\u0004\b;\u0010\"R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020=X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010>\u001a\u00020\u001eX\u0005¢\u0006\u0006\u001a\u0004\b?\u0010 R \u0010@\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020C0B0AX\u000e¢\u0006\u0002\n\u0000¨\u0006L"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHost;", "Lcom/android/systemui/media/MediaHostState;", "state", "Lcom/android/systemui/media/MediaHost$MediaHostStateHolder;", "mediaHierarchyManager", "Lcom/android/systemui/media/MediaHierarchyManager;", "mediaDataManager", "Lcom/android/systemui/media/MediaDataManager;", "mediaHostStatesManager", "Lcom/android/systemui/media/MediaHostStatesManager;", "(Lcom/android/systemui/media/MediaHost$MediaHostStateHolder;Lcom/android/systemui/media/MediaHierarchyManager;Lcom/android/systemui/media/MediaDataManager;Lcom/android/systemui/media/MediaHostStatesManager;)V", "currentBounds", "Landroid/graphics/Rect;", "getCurrentBounds", "()Landroid/graphics/Rect;", "currentClipping", "getCurrentClipping", "disappearParameters", "Lcom/android/systemui/util/animation/DisappearParameters;", "getDisappearParameters", "()Lcom/android/systemui/util/animation/DisappearParameters;", "setDisappearParameters", "(Lcom/android/systemui/util/animation/DisappearParameters;)V", "expansion", "", "getExpansion", "()F", "setExpansion", "(F)V", "falsingProtectionNeeded", "", "getFalsingProtectionNeeded", "()Z", "setFalsingProtectionNeeded", "(Z)V", "hostView", "Lcom/android/systemui/util/animation/UniqueObjectHostView;", "getHostView", "()Lcom/android/systemui/util/animation/UniqueObjectHostView;", "setHostView", "(Lcom/android/systemui/util/animation/UniqueObjectHostView;)V", "inited", "listener", "com/android/systemui/media/MediaHost$listener$1", "Lcom/android/systemui/media/MediaHost$listener$1;", "listeningToMediaData", "<set-?>", "", "location", "getLocation", "()I", "measurementInput", "Lcom/android/systemui/util/animation/MeasurementInput;", "getMeasurementInput", "()Lcom/android/systemui/util/animation/MeasurementInput;", "setMeasurementInput", "(Lcom/android/systemui/util/animation/MeasurementInput;)V", "showsOnlyActiveMedia", "getShowsOnlyActiveMedia", "setShowsOnlyActiveMedia", "tmpLocationOnScreen", "", "visible", "getVisible", "visibleChangedListeners", "Landroid/util/ArraySet;", "Lkotlin/Function1;", "", "addVisibilityChangeListener", "copy", "init", "removeVisibilityChangeListener", "setListeningToMediaData", "listen", "updateViewVisibility", "MediaHostStateHolder", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHost.kt */
public final class MediaHost implements MediaHostState {
    private final Rect currentBounds = new Rect();
    private final Rect currentClipping = new Rect();
    public UniqueObjectHostView hostView;
    private boolean inited;
    private final MediaHost$listener$1 listener = new MediaHost$listener$1(this);
    private boolean listeningToMediaData;
    private int location = -1;
    private final MediaDataManager mediaDataManager;
    private final MediaHierarchyManager mediaHierarchyManager;
    /* access modifiers changed from: private */
    public final MediaHostStatesManager mediaHostStatesManager;
    /* access modifiers changed from: private */
    public final MediaHostStateHolder state;
    private final int[] tmpLocationOnScreen = {0, 0};
    private ArraySet<Function1<Boolean, Unit>> visibleChangedListeners = new ArraySet<>();

    public MediaHostState copy() {
        return this.state.copy();
    }

    public DisappearParameters getDisappearParameters() {
        return this.state.getDisappearParameters();
    }

    public float getExpansion() {
        return this.state.getExpansion();
    }

    public boolean getFalsingProtectionNeeded() {
        return this.state.getFalsingProtectionNeeded();
    }

    public MeasurementInput getMeasurementInput() {
        return this.state.getMeasurementInput();
    }

    public boolean getShowsOnlyActiveMedia() {
        return this.state.getShowsOnlyActiveMedia();
    }

    public boolean getVisible() {
        return this.state.getVisible();
    }

    public void setDisappearParameters(DisappearParameters disappearParameters) {
        Intrinsics.checkNotNullParameter(disappearParameters, "<set-?>");
        this.state.setDisappearParameters(disappearParameters);
    }

    public void setExpansion(float f) {
        this.state.setExpansion(f);
    }

    public void setFalsingProtectionNeeded(boolean z) {
        this.state.setFalsingProtectionNeeded(z);
    }

    public void setMeasurementInput(MeasurementInput measurementInput) {
        this.state.setMeasurementInput(measurementInput);
    }

    public void setShowsOnlyActiveMedia(boolean z) {
        this.state.setShowsOnlyActiveMedia(z);
    }

    public MediaHost(MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager2, MediaDataManager mediaDataManager2, MediaHostStatesManager mediaHostStatesManager2) {
        Intrinsics.checkNotNullParameter(mediaHostStateHolder, AuthDialog.KEY_BIOMETRIC_STATE);
        Intrinsics.checkNotNullParameter(mediaHierarchyManager2, "mediaHierarchyManager");
        Intrinsics.checkNotNullParameter(mediaDataManager2, "mediaDataManager");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager2, "mediaHostStatesManager");
        this.state = mediaHostStateHolder;
        this.mediaHierarchyManager = mediaHierarchyManager2;
        this.mediaDataManager = mediaDataManager2;
        this.mediaHostStatesManager = mediaHostStatesManager2;
    }

    public final UniqueObjectHostView getHostView() {
        UniqueObjectHostView uniqueObjectHostView = this.hostView;
        if (uniqueObjectHostView != null) {
            return uniqueObjectHostView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("hostView");
        return null;
    }

    public final void setHostView(UniqueObjectHostView uniqueObjectHostView) {
        Intrinsics.checkNotNullParameter(uniqueObjectHostView, "<set-?>");
        this.hostView = uniqueObjectHostView;
    }

    public final int getLocation() {
        return this.location;
    }

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

    public final Rect getCurrentClipping() {
        return this.currentClipping;
    }

    public final void addVisibilityChangeListener(Function1<? super Boolean, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "listener");
        this.visibleChangedListeners.add(function1);
    }

    public final void removeVisibilityChangeListener(Function1<? super Boolean, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "listener");
        this.visibleChangedListeners.remove(function1);
    }

    public final void init(int i) {
        if (!this.inited) {
            this.inited = true;
            this.location = i;
            setHostView(this.mediaHierarchyManager.register(this));
            setListeningToMediaData(true);
            getHostView().addOnAttachStateChangeListener(new MediaHost$init$1(this));
            getHostView().setMeasurementManager(new MediaHost$init$2(this, i));
            this.state.setChangedListener(new MediaHost$init$3(this, i));
            updateViewVisibility();
        }
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public final void updateViewVisibility() {
        boolean z;
        MediaHostStateHolder mediaHostStateHolder = this.state;
        if (getShowsOnlyActiveMedia()) {
            z = this.mediaDataManager.hasActiveMediaOrRecommendation();
        } else {
            z = this.mediaDataManager.hasAnyMediaOrRecommendation();
        }
        mediaHostStateHolder.setVisible(z);
        int i = getVisible() ? 0 : 8;
        if (i != getHostView().getVisibility()) {
            getHostView().setVisibility(i);
            for (Function1 invoke : this.visibleChangedListeners) {
                invoke.invoke(Boolean.valueOf(getVisible()));
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010+\u001a\u00020\u0001H\u0016J\u0013\u0010,\u001a\u00020\u00172\b\u0010-\u001a\u0004\u0018\u00010.H\u0002J\b\u0010/\u001a\u00020\u001eH\u0016R\"\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R$\u0010\u0012\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u0011@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R$\u0010\u0018\u001a\u00020\u00172\u0006\u0010\n\u001a\u00020\u0017@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u000e¢\u0006\u0002\n\u0000R(\u0010 \u001a\u0004\u0018\u00010\u001f2\b\u0010\n\u001a\u0004\u0018\u00010\u001f@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R$\u0010%\u001a\u00020\u00172\u0006\u0010\n\u001a\u00020\u0017@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u001a\"\u0004\b'\u0010\u001cR$\u0010(\u001a\u00020\u00172\u0006\u0010\n\u001a\u00020\u0017@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001a\"\u0004\b*\u0010\u001c¨\u00060"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHost$MediaHostStateHolder;", "Lcom/android/systemui/media/MediaHostState;", "()V", "changedListener", "Lkotlin/Function0;", "", "getChangedListener", "()Lkotlin/jvm/functions/Function0;", "setChangedListener", "(Lkotlin/jvm/functions/Function0;)V", "value", "Lcom/android/systemui/util/animation/DisappearParameters;", "disappearParameters", "getDisappearParameters", "()Lcom/android/systemui/util/animation/DisappearParameters;", "setDisappearParameters", "(Lcom/android/systemui/util/animation/DisappearParameters;)V", "", "expansion", "getExpansion", "()F", "setExpansion", "(F)V", "", "falsingProtectionNeeded", "getFalsingProtectionNeeded", "()Z", "setFalsingProtectionNeeded", "(Z)V", "lastDisappearHash", "", "Lcom/android/systemui/util/animation/MeasurementInput;", "measurementInput", "getMeasurementInput", "()Lcom/android/systemui/util/animation/MeasurementInput;", "setMeasurementInput", "(Lcom/android/systemui/util/animation/MeasurementInput;)V", "showsOnlyActiveMedia", "getShowsOnlyActiveMedia", "setShowsOnlyActiveMedia", "visible", "getVisible", "setVisible", "copy", "equals", "other", "", "hashCode", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaHost.kt */
    public static final class MediaHostStateHolder implements MediaHostState {
        private Function0<Unit> changedListener;
        private DisappearParameters disappearParameters = new DisappearParameters();
        private float expansion;
        private boolean falsingProtectionNeeded;
        private int lastDisappearHash = getDisappearParameters().hashCode();
        private MeasurementInput measurementInput;
        private boolean showsOnlyActiveMedia;
        private boolean visible = true;

        public MeasurementInput getMeasurementInput() {
            return this.measurementInput;
        }

        public void setMeasurementInput(MeasurementInput measurementInput2) {
            boolean z = false;
            if (measurementInput2 != null && measurementInput2.equals(this.measurementInput)) {
                z = true;
            }
            if (!z) {
                this.measurementInput = measurementInput2;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public float getExpansion() {
            return this.expansion;
        }

        public void setExpansion(float f) {
            if (!Float.valueOf(f).equals(Float.valueOf(this.expansion))) {
                this.expansion = f;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getShowsOnlyActiveMedia() {
            return this.showsOnlyActiveMedia;
        }

        public void setShowsOnlyActiveMedia(boolean z) {
            if (!Boolean.valueOf(z).equals(Boolean.valueOf(this.showsOnlyActiveMedia))) {
                this.showsOnlyActiveMedia = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getVisible() {
            return this.visible;
        }

        public void setVisible(boolean z) {
            if (this.visible != z) {
                this.visible = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getFalsingProtectionNeeded() {
            return this.falsingProtectionNeeded;
        }

        public void setFalsingProtectionNeeded(boolean z) {
            if (this.falsingProtectionNeeded != z) {
                this.falsingProtectionNeeded = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public DisappearParameters getDisappearParameters() {
            return this.disappearParameters;
        }

        public void setDisappearParameters(DisappearParameters disappearParameters2) {
            Intrinsics.checkNotNullParameter(disappearParameters2, "value");
            int hashCode = disappearParameters2.hashCode();
            if (!Integer.valueOf(this.lastDisappearHash).equals(Integer.valueOf(hashCode))) {
                this.disappearParameters = disappearParameters2;
                this.lastDisappearHash = hashCode;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public final Function0<Unit> getChangedListener() {
            return this.changedListener;
        }

        public final void setChangedListener(Function0<Unit> function0) {
            this.changedListener = function0;
        }

        public MediaHostState copy() {
            MediaHostStateHolder mediaHostStateHolder = new MediaHostStateHolder();
            mediaHostStateHolder.setExpansion(getExpansion());
            mediaHostStateHolder.setShowsOnlyActiveMedia(getShowsOnlyActiveMedia());
            MeasurementInput measurementInput2 = getMeasurementInput();
            MeasurementInput measurementInput3 = null;
            if (measurementInput2 != null) {
                measurementInput3 = MeasurementInput.copy$default(measurementInput2, 0, 0, 3, (Object) null);
            }
            mediaHostStateHolder.setMeasurementInput(measurementInput3);
            mediaHostStateHolder.setVisible(getVisible());
            mediaHostStateHolder.setDisappearParameters(getDisappearParameters().deepCopy());
            mediaHostStateHolder.setFalsingProtectionNeeded(getFalsingProtectionNeeded());
            return mediaHostStateHolder;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof MediaHostState)) {
                return false;
            }
            MediaHostState mediaHostState = (MediaHostState) obj;
            if (!Objects.equals(getMeasurementInput(), mediaHostState.getMeasurementInput())) {
                return false;
            }
            if ((getExpansion() == mediaHostState.getExpansion()) && getShowsOnlyActiveMedia() == mediaHostState.getShowsOnlyActiveMedia() && getVisible() == mediaHostState.getVisible() && getFalsingProtectionNeeded() == mediaHostState.getFalsingProtectionNeeded() && getDisappearParameters().equals(mediaHostState.getDisappearParameters())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            MeasurementInput measurementInput2 = getMeasurementInput();
            return ((((((((((measurementInput2 != null ? measurementInput2.hashCode() : 0) * 31) + Float.hashCode(getExpansion())) * 31) + Boolean.hashCode(getFalsingProtectionNeeded())) * 31) + Boolean.hashCode(getShowsOnlyActiveMedia())) * 31) + (getVisible() ? 1 : 2)) * 31) + getDisappearParameters().hashCode();
        }
    }
}
