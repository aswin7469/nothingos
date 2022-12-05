package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$id;
import com.android.systemui.R$xml;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionLayoutController;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.animation.WidgetState;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class MediaViewController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    public static final long GUTS_ANIMATION_DURATION = 500;
    private boolean animateNextStateChange;
    private long animationDelay;
    private long animationDuration;
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final MediaViewController$configurationListener$1 configurationListener;
    @NotNull
    private final Context context;
    private int currentHeight;
    private int currentWidth;
    private boolean isGutsVisible;
    @NotNull
    private final TransitionLayoutController layoutController;
    @NotNull
    private final MediaHostStatesManager mediaHostStatesManager;
    private boolean shouldHideGutsSettings;
    public Function0<Unit> sizeChangedListener;
    @Nullable
    private TransitionLayout transitionLayout;
    private boolean firstRefresh = true;
    @NotNull
    private final MeasurementOutput measurement = new MeasurementOutput(0, 0);
    @NotNull
    private TYPE type = TYPE.PLAYER;
    @NotNull
    private final Map<CacheKey, TransitionViewState> viewStates = new LinkedHashMap();
    private int currentEndLocation = -1;
    private int currentStartLocation = -1;
    private float currentTransitionProgress = 1.0f;
    @NotNull
    private final TransitionViewState tmpState = new TransitionViewState();
    @NotNull
    private final TransitionViewState tmpState2 = new TransitionViewState();
    @NotNull
    private final TransitionViewState tmpState3 = new TransitionViewState();
    @NotNull
    private final CacheKey tmpKey = new CacheKey(0, 0, 0.0f, false, 15, null);
    @NotNull
    private final MediaHostStatesManager.Callback stateCallback = new MediaHostStatesManager.Callback() { // from class: com.android.systemui.media.MediaViewController$stateCallback$1
        @Override // com.android.systemui.media.MediaHostStatesManager.Callback
        public void onHostStateChanged(int i, @NotNull MediaHostState mediaHostState) {
            int i2;
            float f;
            int i3;
            Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
            if (i != MediaViewController.this.getCurrentEndLocation()) {
                i3 = MediaViewController.this.currentStartLocation;
                if (i != i3) {
                    return;
                }
            }
            MediaViewController mediaViewController = MediaViewController.this;
            i2 = mediaViewController.currentStartLocation;
            int currentEndLocation = MediaViewController.this.getCurrentEndLocation();
            f = MediaViewController.this.currentTransitionProgress;
            mediaViewController.setCurrentState(i2, currentEndLocation, f, false);
        }
    };
    @NotNull
    private final ConstraintSet collapsedLayout = new ConstraintSet();
    @NotNull
    private final ConstraintSet expandedLayout = new ConstraintSet();

    /* compiled from: MediaViewController.kt */
    /* loaded from: classes.dex */
    public enum TYPE {
        PLAYER,
        RECOMMENDATION;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static TYPE[] valuesCustom() {
            TYPE[] valuesCustom = values();
            TYPE[] typeArr = new TYPE[valuesCustom.length];
            System.arraycopy(valuesCustom, 0, typeArr, 0, valuesCustom.length);
            return typeArr;
        }
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [java.lang.Object, com.android.systemui.media.MediaViewController$configurationListener$1] */
    public MediaViewController(@NotNull Context context, @NotNull ConfigurationController configurationController, @NotNull MediaHostStatesManager mediaHostStatesManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager, "mediaHostStatesManager");
        this.context = context;
        this.configurationController = configurationController;
        this.mediaHostStatesManager = mediaHostStatesManager;
        TransitionLayoutController transitionLayoutController = new TransitionLayoutController();
        this.layoutController = transitionLayoutController;
        ?? r0 = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.MediaViewController$configurationListener$1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                TransitionLayout transitionLayout;
                TransitionLayout transitionLayout2;
                if (configuration == null) {
                    return;
                }
                MediaViewController mediaViewController = MediaViewController.this;
                transitionLayout = mediaViewController.transitionLayout;
                Integer valueOf = transitionLayout == null ? null : Integer.valueOf(transitionLayout.getRawLayoutDirection());
                int layoutDirection = configuration.getLayoutDirection();
                if (valueOf != null && valueOf.intValue() == layoutDirection) {
                    return;
                }
                transitionLayout2 = mediaViewController.transitionLayout;
                if (transitionLayout2 != null) {
                    transitionLayout2.setLayoutDirection(configuration.getLayoutDirection());
                }
                mediaViewController.refreshState();
            }
        };
        this.configurationListener = r0;
        mediaHostStatesManager.addController(this);
        transitionLayoutController.setSizeChangedListener(new AnonymousClass1());
        configurationController.addCallback(r0);
    }

    /* compiled from: MediaViewController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final Function0<Unit> getSizeChangedListener() {
        Function0<Unit> function0 = this.sizeChangedListener;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sizeChangedListener");
        throw null;
    }

    public final void setSizeChangedListener(@NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.sizeChangedListener = function0;
    }

    public final int getCurrentEndLocation() {
        return this.currentEndLocation;
    }

    public final int getCurrentWidth() {
        return this.currentWidth;
    }

    public final void setCurrentWidth(int i) {
        this.currentWidth = i;
    }

    public final int getCurrentHeight() {
        return this.currentHeight;
    }

    public final void setCurrentHeight(int i) {
        this.currentHeight = i;
    }

    public final float getTranslationX() {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout == null) {
            return 0.0f;
        }
        return transitionLayout.getTranslationX();
    }

    public final float getTranslationY() {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout == null) {
            return 0.0f;
        }
        return transitionLayout.getTranslationY();
    }

    @NotNull
    public final MediaHostStatesManager.Callback getStateCallback() {
        return this.stateCallback;
    }

    @NotNull
    public final ConstraintSet getCollapsedLayout() {
        return this.collapsedLayout;
    }

    @NotNull
    public final ConstraintSet getExpandedLayout() {
        return this.expandedLayout;
    }

    public final boolean isGutsVisible() {
        return this.isGutsVisible;
    }

    public final void setShouldHideGutsSettings(boolean z) {
        this.shouldHideGutsSettings = z;
    }

    /* compiled from: MediaViewController.kt */
    /* renamed from: com.android.systemui.media.MediaViewController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static final class AnonymousClass1 extends Lambda implements Function2<Integer, Integer, Unit> {
        AnonymousClass1() {
            super(2);
        }

        @Override // kotlin.jvm.functions.Function2
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1950invoke(Integer num, Integer num2) {
            invoke(num.intValue(), num2.intValue());
            return Unit.INSTANCE;
        }

        public final void invoke(int i, int i2) {
            MediaViewController.this.setCurrentWidth(i);
            MediaViewController.this.setCurrentHeight(i2);
            MediaViewController.this.getSizeChangedListener().mo1951invoke();
        }
    }

    public final void onDestroy() {
        this.mediaHostStatesManager.removeController(this);
        this.configurationController.removeCallback(this.configurationListener);
    }

    public final void openGuts() {
        if (this.isGutsVisible) {
            return;
        }
        this.isGutsVisible = true;
        animatePendingStateChange(GUTS_ANIMATION_DURATION, 0L);
        setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, false);
    }

    public final void closeGuts(boolean z) {
        if (!this.isGutsVisible) {
            return;
        }
        this.isGutsVisible = false;
        if (!z) {
            animatePendingStateChange(GUTS_ANIMATION_DURATION, 0L);
        }
        setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
    }

    private final void ensureAllMeasurements() {
        for (Map.Entry<Integer, MediaHostState> entry : this.mediaHostStatesManager.getMediaHostStates().entrySet()) {
            obtainViewState(entry.getValue());
        }
    }

    private final ConstraintSet constraintSetForExpansion(float f) {
        return f > 0.0f ? this.expandedLayout : this.collapsedLayout;
    }

    private final void setGutsViewState(TransitionViewState transitionViewState) {
        WidgetState widgetState;
        if (this.type == TYPE.PLAYER) {
            for (Number number : PlayerViewHolder.Companion.getControlsIds()) {
                WidgetState widgetState2 = transitionViewState.getWidgetStates().get(Integer.valueOf(number.intValue()));
                if (widgetState2 != null) {
                    widgetState2.setAlpha(isGutsVisible() ? 0.0f : widgetState2.getAlpha());
                    widgetState2.setGone(isGutsVisible() ? true : widgetState2.getGone());
                }
            }
            for (Number number2 : PlayerViewHolder.Companion.getGutsIds()) {
                int intValue = number2.intValue();
                WidgetState widgetState3 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue));
                if (widgetState3 != null) {
                    widgetState3.setAlpha(isGutsVisible() ? 1.0f : 0.0f);
                }
                WidgetState widgetState4 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue));
                if (widgetState4 != null) {
                    widgetState4.setGone(!isGutsVisible());
                }
            }
        } else {
            for (Number number3 : RecommendationViewHolder.Companion.getControlsIds()) {
                WidgetState widgetState5 = transitionViewState.getWidgetStates().get(Integer.valueOf(number3.intValue()));
                if (widgetState5 != null) {
                    widgetState5.setAlpha(isGutsVisible() ? 0.0f : widgetState5.getAlpha());
                    widgetState5.setGone(isGutsVisible() ? true : widgetState5.getGone());
                }
            }
            for (Number number4 : RecommendationViewHolder.Companion.getGutsIds()) {
                int intValue2 = number4.intValue();
                WidgetState widgetState6 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue2));
                if (widgetState6 != null) {
                    widgetState6.setAlpha(isGutsVisible() ? 1.0f : 0.0f);
                }
                WidgetState widgetState7 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue2));
                if (widgetState7 != null) {
                    widgetState7.setGone(!isGutsVisible());
                }
            }
        }
        if (!this.shouldHideGutsSettings || (widgetState = transitionViewState.getWidgetStates().get(Integer.valueOf(R$id.settings))) == null) {
            return;
        }
        widgetState.setGone(true);
    }

    private final TransitionViewState obtainViewState(MediaHostState mediaHostState) {
        if (mediaHostState == null || mediaHostState.getMeasurementInput() == null) {
            return null;
        }
        CacheKey key = getKey(mediaHostState, this.isGutsVisible, this.tmpKey);
        TransitionViewState transitionViewState = this.viewStates.get(key);
        if (transitionViewState != null) {
            return transitionViewState;
        }
        CacheKey copy$default = CacheKey.copy$default(key, 0, 0, 0.0f, false, 15, null);
        if (this.transitionLayout == null) {
            return null;
        }
        boolean z = true;
        if (!(mediaHostState.getExpansion() == 0.0f)) {
            if (mediaHostState.getExpansion() != 1.0f) {
                z = false;
            }
            if (!z) {
                MediaHostState copy = mediaHostState.copy();
                copy.setExpansion(0.0f);
                TransitionViewState obtainViewState = obtainViewState(copy);
                Objects.requireNonNull(obtainViewState, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                MediaHostState copy2 = mediaHostState.copy();
                copy2.setExpansion(1.0f);
                TransitionViewState obtainViewState2 = obtainViewState(copy2);
                Objects.requireNonNull(obtainViewState2, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                return TransitionLayoutController.getInterpolatedState$default(this.layoutController, obtainViewState, obtainViewState2, mediaHostState.getExpansion(), null, 8, null);
            }
        }
        TransitionLayout transitionLayout = this.transitionLayout;
        Intrinsics.checkNotNull(transitionLayout);
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        Intrinsics.checkNotNull(measurementInput);
        TransitionViewState calculateViewState = transitionLayout.calculateViewState(measurementInput, constraintSetForExpansion(mediaHostState.getExpansion()), new TransitionViewState());
        setGutsViewState(calculateViewState);
        this.viewStates.put(copy$default, calculateViewState);
        return calculateViewState;
    }

    private final CacheKey getKey(MediaHostState mediaHostState, boolean z, CacheKey cacheKey) {
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        int i = 0;
        cacheKey.setHeightMeasureSpec(measurementInput == null ? 0 : measurementInput.getHeightMeasureSpec());
        MeasurementInput measurementInput2 = mediaHostState.getMeasurementInput();
        if (measurementInput2 != null) {
            i = measurementInput2.getWidthMeasureSpec();
        }
        cacheKey.setWidthMeasureSpec(i);
        cacheKey.setExpansion(mediaHostState.getExpansion());
        cacheKey.setGutsVisible(z);
        return cacheKey;
    }

    public final void attach(@NotNull TransitionLayout transitionLayout, @NotNull TYPE type) {
        Intrinsics.checkNotNullParameter(transitionLayout, "transitionLayout");
        Intrinsics.checkNotNullParameter(type, "type");
        updateMediaViewControllerType(type);
        this.transitionLayout = transitionLayout;
        this.layoutController.attach(transitionLayout);
        int i = this.currentEndLocation;
        if (i == -1) {
            return;
        }
        setCurrentState(this.currentStartLocation, i, this.currentTransitionProgress, true);
    }

    @Nullable
    public final MeasurementOutput getMeasurementsForState(@NotNull MediaHostState hostState) {
        Intrinsics.checkNotNullParameter(hostState, "hostState");
        TransitionViewState obtainViewState = obtainViewState(hostState);
        if (obtainViewState == null) {
            return null;
        }
        this.measurement.setMeasuredWidth(obtainViewState.getWidth());
        this.measurement.setMeasuredHeight(obtainViewState.getHeight());
        return this.measurement;
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        TransitionViewState transitionViewState;
        this.currentEndLocation = i2;
        this.currentStartLocation = i;
        this.currentTransitionProgress = f;
        boolean z2 = true;
        boolean z3 = this.animateNextStateChange && !z;
        MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i2));
        if (mediaHostState == null) {
            return;
        }
        MediaHostState mediaHostState2 = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
        TransitionViewState obtainViewState = obtainViewState(mediaHostState);
        if (obtainViewState == null) {
            return;
        }
        TransitionViewState updateViewStateToCarouselSize = updateViewStateToCarouselSize(obtainViewState, i2, this.tmpState2);
        Intrinsics.checkNotNull(updateViewStateToCarouselSize);
        this.layoutController.setMeasureState(updateViewStateToCarouselSize);
        this.animateNextStateChange = false;
        if (this.transitionLayout == null) {
            return;
        }
        TransitionViewState updateViewStateToCarouselSize2 = updateViewStateToCarouselSize(obtainViewState(mediaHostState2), i, this.tmpState3);
        if (!mediaHostState.getVisible()) {
            if (updateViewStateToCarouselSize2 != null && mediaHostState2 != null && mediaHostState2.getVisible()) {
                updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize2, mediaHostState2.getDisappearParameters(), f, this.tmpState);
                transitionViewState = updateViewStateToCarouselSize2;
            }
            transitionViewState = updateViewStateToCarouselSize;
        } else {
            if (mediaHostState2 == null || mediaHostState2.getVisible()) {
                if (!(f == 1.0f) && updateViewStateToCarouselSize2 != null) {
                    if (f != 0.0f) {
                        z2 = false;
                    }
                    if (!z2) {
                        updateViewStateToCarouselSize2 = this.layoutController.getInterpolatedState(updateViewStateToCarouselSize2, updateViewStateToCarouselSize, f, this.tmpState);
                    }
                }
                transitionViewState = updateViewStateToCarouselSize;
            } else {
                updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize, mediaHostState.getDisappearParameters(), 1.0f - f, this.tmpState);
            }
            transitionViewState = updateViewStateToCarouselSize2;
        }
        this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
    }

    private final TransitionViewState updateViewStateToCarouselSize(TransitionViewState transitionViewState, int i, TransitionViewState transitionViewState2) {
        TransitionViewState copy = transitionViewState == null ? null : transitionViewState.copy(transitionViewState2);
        if (copy == null) {
            return null;
        }
        MeasurementOutput measurementOutput = this.mediaHostStatesManager.getCarouselSizes().get(Integer.valueOf(i));
        if (measurementOutput != null) {
            copy.setHeight(Math.max(measurementOutput.getMeasuredHeight(), copy.getHeight()));
            copy.setWidth(Math.max(measurementOutput.getMeasuredWidth(), copy.getWidth()));
        }
        return copy;
    }

    private final void updateMediaViewControllerType(TYPE type) {
        this.type = type;
        if (type == TYPE.PLAYER) {
            this.collapsedLayout.load(this.context, R$xml.media_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_expanded);
        } else {
            this.collapsedLayout.load(this.context, R$xml.media_recommendation_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_recommendation_expanded);
        }
        refreshState();
    }

    private final TransitionViewState obtainViewStateForLocation(int i) {
        MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
        if (mediaHostState == null) {
            return null;
        }
        return obtainViewState(mediaHostState);
    }

    public final void onLocationPreChange(int i) {
        TransitionViewState obtainViewStateForLocation = obtainViewStateForLocation(i);
        if (obtainViewStateForLocation == null) {
            return;
        }
        this.layoutController.setMeasureState(obtainViewStateForLocation);
    }

    public final void animatePendingStateChange(long j, long j2) {
        this.animateNextStateChange = true;
        this.animationDuration = j;
        this.animationDelay = j2;
    }

    public final void refreshState() {
        this.viewStates.clear();
        if (this.firstRefresh) {
            ensureAllMeasurements();
            this.firstRefresh = false;
        }
        setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
    }
}
