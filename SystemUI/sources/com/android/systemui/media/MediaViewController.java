package com.android.systemui.media;

import android.content.Context;
import android.os.Trace;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.C1894R;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionLayoutController;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.animation.WidgetState;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0019*\u0001\u0015\u0018\u0000 q2\u00020\u0001:\u0002qrB'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0016\u0010O\u001a\u0002052\u0006\u0010P\u001a\u00020\u000e2\u0006\u0010Q\u001a\u00020\u000eJ\u0016\u0010R\u001a\u0002052\u0006\u0010D\u001a\u00020E2\u0006\u0010K\u001a\u00020LJ\u0012\u0010S\u001a\u0002052\b\b\u0002\u0010T\u001a\u00020\fH\u0007J\u0010\u0010U\u001a\u00020\u00112\u0006\u0010V\u001a\u00020%H\u0002J\b\u0010W\u001a\u000205H\u0002J \u0010X\u001a\u00020?2\u0006\u0010Y\u001a\u00020Z2\u0006\u0010[\u001a\u00020\f2\u0006\u0010\\\u001a\u00020?H\u0002J\u0010\u0010]\u001a\u0004\u0018\u0001022\u0006\u0010^\u001a\u00020ZJ\u0014\u0010_\u001a\u0004\u0018\u00010A2\b\u0010Y\u001a\u0004\u0018\u00010ZH\u0002J\u0012\u0010`\u001a\u0004\u0018\u00010A2\u0006\u0010a\u001a\u00020\u0018H\u0002J\u0006\u0010b\u001a\u000205J\u000e\u0010c\u001a\u0002052\u0006\u0010d\u001a\u00020\u0018J\u0006\u0010e\u001a\u000205J\u0006\u0010f\u001a\u000205J&\u0010g\u001a\u0002052\u0006\u0010h\u001a\u00020\u00182\u0006\u0010i\u001a\u00020\u00182\u0006\u0010j\u001a\u00020%2\u0006\u0010k\u001a\u00020\fJ\u0010\u0010l\u001a\u0002052\u0006\u0010m\u001a\u00020AH\u0002J\u0010\u0010n\u001a\u0002052\u0006\u0010K\u001a\u00020LH\u0002J$\u0010o\u001a\u0004\u0018\u00010A2\b\u0010m\u001a\u0004\u0018\u00010A2\u0006\u0010a\u001a\u00020\u00182\u0006\u0010p\u001a\u00020AH\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0010\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0004\n\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R \u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001c\"\u0004\b!\u0010\u001eR\u0014\u0010\"\u001a\u00020\u0018X\u000e¢\u0006\b\n\u0000\u0012\u0004\b#\u0010\u001aR\u000e\u0010$\u001a\u00020%X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u00020\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u001c\"\u0004\b(\u0010\u001eR\u0011\u0010)\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0013R\u000e\u0010+\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010-\u001a\u00020\f2\u0006\u0010,\u001a\u00020\f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u000e\u0010/\u001a\u000200X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R \u00103\u001a\b\u0012\u0004\u0012\u00020504X.¢\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u0011\u0010:\u001a\u00020;¢\u0006\b\n\u0000\u001a\u0004\b<\u0010=R\u000e\u0010>\u001a\u00020?X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020AX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020AX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020AX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010D\u001a\u0004\u0018\u00010EX\u000e¢\u0006\u0002\n\u0000R \u0010F\u001a\u00020%2\u0006\u0010,\u001a\u00020%8F@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bG\u0010HR \u0010I\u001a\u00020%2\u0006\u0010,\u001a\u00020%8F@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bJ\u0010HR\u000e\u0010K\u001a\u00020LX\u000e¢\u0006\u0002\n\u0000R\u001c\u0010M\u001a\u0010\u0012\u0004\u0012\u00020?\u0012\u0006\u0012\u0004\u0018\u00010A0NX\u0004¢\u0006\u0002\n\u0000¨\u0006s"}, mo65043d2 = {"Lcom/android/systemui/media/MediaViewController;", "", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "mediaHostStatesManager", "Lcom/android/systemui/media/MediaHostStatesManager;", "logger", "Lcom/android/systemui/media/MediaViewLogger;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/media/MediaHostStatesManager;Lcom/android/systemui/media/MediaViewLogger;)V", "animateNextStateChange", "", "animationDelay", "", "animationDuration", "collapsedLayout", "Landroidx/constraintlayout/widget/ConstraintSet;", "getCollapsedLayout", "()Landroidx/constraintlayout/widget/ConstraintSet;", "configurationListener", "com/android/systemui/media/MediaViewController$configurationListener$1", "Lcom/android/systemui/media/MediaViewController$configurationListener$1;", "currentEndLocation", "", "getCurrentEndLocation$annotations", "()V", "getCurrentEndLocation", "()I", "setCurrentEndLocation", "(I)V", "currentHeight", "getCurrentHeight", "setCurrentHeight", "currentStartLocation", "getCurrentStartLocation$annotations", "currentTransitionProgress", "", "currentWidth", "getCurrentWidth", "setCurrentWidth", "expandedLayout", "getExpandedLayout", "firstRefresh", "<set-?>", "isGutsVisible", "()Z", "layoutController", "Lcom/android/systemui/util/animation/TransitionLayoutController;", "measurement", "Lcom/android/systemui/util/animation/MeasurementOutput;", "sizeChangedListener", "Lkotlin/Function0;", "", "getSizeChangedListener", "()Lkotlin/jvm/functions/Function0;", "setSizeChangedListener", "(Lkotlin/jvm/functions/Function0;)V", "stateCallback", "Lcom/android/systemui/media/MediaHostStatesManager$Callback;", "getStateCallback", "()Lcom/android/systemui/media/MediaHostStatesManager$Callback;", "tmpKey", "Lcom/android/systemui/media/CacheKey;", "tmpState", "Lcom/android/systemui/util/animation/TransitionViewState;", "tmpState2", "tmpState3", "transitionLayout", "Lcom/android/systemui/util/animation/TransitionLayout;", "translationX", "getTranslationX", "()F", "translationY", "getTranslationY", "type", "Lcom/android/systemui/media/MediaViewController$TYPE;", "viewStates", "", "animatePendingStateChange", "duration", "delay", "attach", "closeGuts", "immediate", "constraintSetForExpansion", "expansion", "ensureAllMeasurements", "getKey", "state", "Lcom/android/systemui/media/MediaHostState;", "guts", "result", "getMeasurementsForState", "hostState", "obtainViewState", "obtainViewStateForLocation", "location", "onDestroy", "onLocationPreChange", "newLocation", "openGuts", "refreshState", "setCurrentState", "startLocation", "endLocation", "transitionProgress", "applyImmediately", "setGutsViewState", "viewState", "updateMediaViewControllerType", "updateViewStateToCarouselSize", "outState", "Companion", "TYPE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaViewController.kt */
public final class MediaViewController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final long GUTS_ANIMATION_DURATION = 500;
    private boolean animateNextStateChange;
    private long animationDelay;
    private long animationDuration;
    private final ConstraintSet collapsedLayout;
    private final ConfigurationController configurationController;
    private final MediaViewController$configurationListener$1 configurationListener;
    private final Context context;
    private int currentEndLocation;
    private int currentHeight;
    /* access modifiers changed from: private */
    public int currentStartLocation;
    /* access modifiers changed from: private */
    public float currentTransitionProgress;
    private int currentWidth;
    private final ConstraintSet expandedLayout;
    private boolean firstRefresh = true;
    private boolean isGutsVisible;
    private final TransitionLayoutController layoutController;
    private final MediaViewLogger logger;
    private final MeasurementOutput measurement;
    private final MediaHostStatesManager mediaHostStatesManager;
    public Function0<Unit> sizeChangedListener;
    private final MediaHostStatesManager.Callback stateCallback;
    private final CacheKey tmpKey;
    private final TransitionViewState tmpState;
    private final TransitionViewState tmpState2;
    private final TransitionViewState tmpState3;
    /* access modifiers changed from: private */
    public TransitionLayout transitionLayout;
    private float translationX;
    private float translationY;
    private TYPE type;
    private final Map<CacheKey, TransitionViewState> viewStates;

    @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/media/MediaViewController$TYPE;", "", "(Ljava/lang/String;I)V", "PLAYER", "RECOMMENDATION", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaViewController.kt */
    public enum TYPE {
        PLAYER,
        RECOMMENDATION
    }

    @Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaViewController.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[TYPE.values().length];
            iArr[TYPE.PLAYER.ordinal()] = 1;
            iArr[TYPE.RECOMMENDATION.ordinal()] = 2;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static /* synthetic */ void getCurrentEndLocation$annotations() {
    }

    private static /* synthetic */ void getCurrentStartLocation$annotations() {
    }

    public final void closeGuts() {
        closeGuts$default(this, false, 1, (Object) null);
    }

    @Inject
    public MediaViewController(Context context2, ConfigurationController configurationController2, MediaHostStatesManager mediaHostStatesManager2, MediaViewLogger mediaViewLogger) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager2, "mediaHostStatesManager");
        Intrinsics.checkNotNullParameter(mediaViewLogger, "logger");
        this.context = context2;
        this.configurationController = configurationController2;
        this.mediaHostStatesManager = mediaHostStatesManager2;
        this.logger = mediaViewLogger;
        TransitionLayoutController transitionLayoutController = new TransitionLayoutController();
        this.layoutController = transitionLayoutController;
        this.measurement = new MeasurementOutput(0, 0);
        this.type = TYPE.PLAYER;
        this.viewStates = new LinkedHashMap();
        this.currentEndLocation = -1;
        this.currentStartLocation = -1;
        this.currentTransitionProgress = 1.0f;
        this.tmpState = new TransitionViewState();
        this.tmpState2 = new TransitionViewState();
        this.tmpState3 = new TransitionViewState();
        this.tmpKey = new CacheKey(0, 0, 0.0f, false, 15, (DefaultConstructorMarker) null);
        MediaViewController$configurationListener$1 mediaViewController$configurationListener$1 = new MediaViewController$configurationListener$1(this);
        this.configurationListener = mediaViewController$configurationListener$1;
        this.stateCallback = new MediaViewController$stateCallback$1(this);
        this.collapsedLayout = new ConstraintSet();
        this.expandedLayout = new ConstraintSet();
        mediaHostStatesManager2.addController(this);
        transitionLayoutController.setSizeChangedListener(new Function2<Integer, Integer, Unit>(this) {
            final /* synthetic */ MediaViewController this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke(((Number) obj).intValue(), ((Number) obj2).intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int i, int i2) {
                this.this$0.setCurrentWidth(i);
                this.this$0.setCurrentHeight(i2);
                this.this$0.getSizeChangedListener().invoke();
            }
        });
        configurationController2.addCallback(mediaViewController$configurationListener$1);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006XD¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/media/MediaViewController$Companion;", "", "()V", "GUTS_ANIMATION_DURATION", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaViewController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final Function0<Unit> getSizeChangedListener() {
        Function0<Unit> function0 = this.sizeChangedListener;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sizeChangedListener");
        return null;
    }

    public final void setSizeChangedListener(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.sizeChangedListener = function0;
    }

    public final int getCurrentEndLocation() {
        return this.currentEndLocation;
    }

    public final void setCurrentEndLocation(int i) {
        this.currentEndLocation = i;
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
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            return transitionLayout2.getTranslationX();
        }
        return 0.0f;
    }

    public final float getTranslationY() {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            return transitionLayout2.getTranslationY();
        }
        return 0.0f;
    }

    public final MediaHostStatesManager.Callback getStateCallback() {
        return this.stateCallback;
    }

    public final ConstraintSet getCollapsedLayout() {
        return this.collapsedLayout;
    }

    public final ConstraintSet getExpandedLayout() {
        return this.expandedLayout;
    }

    public final boolean isGutsVisible() {
        return this.isGutsVisible;
    }

    public final void onDestroy() {
        this.mediaHostStatesManager.removeController(this);
        this.configurationController.removeCallback(this.configurationListener);
    }

    public final void openGuts() {
        if (!this.isGutsVisible) {
            this.isGutsVisible = true;
            animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, false);
        }
    }

    public static /* synthetic */ void closeGuts$default(MediaViewController mediaViewController, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaViewController.closeGuts(z);
    }

    public final void closeGuts(boolean z) {
        if (this.isGutsVisible) {
            this.isGutsVisible = false;
            if (!z) {
                animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            }
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
        }
    }

    private final void ensureAllMeasurements() {
        for (Map.Entry<Integer, MediaHostState> value : this.mediaHostStatesManager.getMediaHostStates().entrySet()) {
            obtainViewState((MediaHostState) value.getValue());
        }
    }

    private final ConstraintSet constraintSetForExpansion(float f) {
        return f > 0.0f ? this.expandedLayout : this.collapsedLayout;
    }

    private final void setGutsViewState(TransitionViewState transitionViewState) {
        Set<Integer> set;
        int i = WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()];
        if (i == 1) {
            set = MediaViewHolder.Companion.getControlsIds();
        } else if (i == 2) {
            set = RecommendationViewHolder.Companion.getControlsIds();
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Set<Integer> ids = GutsViewHolder.Companion.getIds();
        Iterator it = set.iterator();
        while (true) {
            float f = 0.0f;
            if (!it.hasNext()) {
                break;
            }
            WidgetState widgetState = transitionViewState.getWidgetStates().get(Integer.valueOf(((Number) it.next()).intValue()));
            if (widgetState != null) {
                if (!this.isGutsVisible) {
                    f = widgetState.getAlpha();
                }
                widgetState.setAlpha(f);
                widgetState.setGone(this.isGutsVisible ? true : widgetState.getGone());
            }
        }
        for (Number intValue : ids) {
            WidgetState widgetState2 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue.intValue()));
            if (widgetState2 != null) {
                widgetState2.setAlpha(this.isGutsVisible ? widgetState2.getAlpha() : 0.0f);
                widgetState2.setGone(this.isGutsVisible ? widgetState2.getGone() : true);
            }
        }
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
        CacheKey copy$default = CacheKey.copy$default(key, 0, 0, 0.0f, false, 15, (Object) null);
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
                if (obtainViewState != null) {
                    MediaHostState copy2 = mediaHostState.copy();
                    copy2.setExpansion(1.0f);
                    TransitionViewState obtainViewState2 = obtainViewState(copy2);
                    if (obtainViewState2 != null) {
                        return TransitionLayoutController.getInterpolatedState$default(this.layoutController, obtainViewState, obtainViewState2, mediaHostState.getExpansion(), (TransitionViewState) null, 8, (Object) null);
                    }
                    throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
            }
        }
        TransitionLayout transitionLayout2 = this.transitionLayout;
        Intrinsics.checkNotNull(transitionLayout2);
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        Intrinsics.checkNotNull(measurementInput);
        TransitionViewState calculateViewState = transitionLayout2.calculateViewState(measurementInput, constraintSetForExpansion(mediaHostState.getExpansion()), new TransitionViewState());
        setGutsViewState(calculateViewState);
        this.viewStates.put(copy$default, calculateViewState);
        return calculateViewState;
    }

    private final CacheKey getKey(MediaHostState mediaHostState, boolean z, CacheKey cacheKey) {
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        int i = 0;
        cacheKey.setHeightMeasureSpec(measurementInput != null ? measurementInput.getHeightMeasureSpec() : 0);
        MeasurementInput measurementInput2 = mediaHostState.getMeasurementInput();
        if (measurementInput2 != null) {
            i = measurementInput2.getWidthMeasureSpec();
        }
        cacheKey.setWidthMeasureSpec(i);
        cacheKey.setExpansion(mediaHostState.getExpansion());
        cacheKey.setGutsVisible(z);
        return cacheKey;
    }

    private final TransitionViewState updateViewStateToCarouselSize(TransitionViewState transitionViewState, int i, TransitionViewState transitionViewState2) {
        TransitionViewState copy;
        if (transitionViewState == null || (copy = transitionViewState.copy(transitionViewState2)) == null) {
            return null;
        }
        MeasurementOutput measurementOutput = this.mediaHostStatesManager.getCarouselSizes().get(Integer.valueOf(i));
        if (measurementOutput != null) {
            copy.setHeight(Math.max(measurementOutput.getMeasuredHeight(), copy.getHeight()));
            copy.setWidth(Math.max(measurementOutput.getMeasuredWidth(), copy.getWidth()));
        }
        this.logger.logMediaSize("update to carousel", copy.getWidth(), copy.getHeight());
        return copy;
    }

    private final void updateMediaViewControllerType(TYPE type2) {
        this.type = type2;
        int i = WhenMappings.$EnumSwitchMapping$0[type2.ordinal()];
        if (i == 1) {
            this.collapsedLayout.load(this.context, (int) C1894R.C1902xml.media_session_collapsed);
            this.expandedLayout.load(this.context, (int) C1894R.C1902xml.media_session_expanded);
        } else if (i == 2) {
            this.collapsedLayout.load(this.context, (int) C1894R.C1902xml.media_recommendation_collapsed);
            this.expandedLayout.load(this.context, (int) C1894R.C1902xml.media_recommendation_expanded);
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
        if (obtainViewStateForLocation != null) {
            this.layoutController.setMeasureState(obtainViewStateForLocation);
        }
    }

    public final void animatePendingStateChange(long j, long j2) {
        this.animateNextStateChange = true;
        this.animationDuration = j;
        this.animationDelay = j2;
    }

    public final void attach(TransitionLayout transitionLayout2, TYPE type2) {
        Intrinsics.checkNotNullParameter(transitionLayout2, "transitionLayout");
        Intrinsics.checkNotNullParameter(type2, "type");
        Trace.beginSection("MediaViewController#attach");
        try {
            updateMediaViewControllerType(type2);
            this.logger.logMediaLocation("attach", this.currentStartLocation, this.currentEndLocation);
            this.transitionLayout = transitionLayout2;
            this.layoutController.attach(transitionLayout2);
            int i = this.currentEndLocation;
            if (i != -1) {
                setCurrentState(this.currentStartLocation, i, this.currentTransitionProgress, true);
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    /* JADX INFO: finally extract failed */
    public final MeasurementOutput getMeasurementsForState(MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        Trace.beginSection("MediaViewController#getMeasurementsForState");
        try {
            TransitionViewState obtainViewState = obtainViewState(mediaHostState);
            if (obtainViewState == null) {
                Trace.endSection();
                return null;
            }
            this.measurement.setMeasuredWidth(obtainViewState.getWidth());
            this.measurement.setMeasuredHeight(obtainViewState.getHeight());
            MeasurementOutput measurementOutput = this.measurement;
            Trace.endSection();
            return measurementOutput;
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        TransitionViewState transitionViewState;
        int i3 = i;
        int i4 = i2;
        float f2 = f;
        Trace.beginSection("MediaViewController#setCurrentState");
        try {
            this.currentEndLocation = i4;
            this.currentStartLocation = i3;
            this.currentTransitionProgress = f2;
            this.logger.logMediaLocation("setCurrentState", i3, i4);
            boolean z2 = true;
            boolean z3 = this.animateNextStateChange && !z;
            MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i2));
            if (mediaHostState != null) {
                MediaHostState mediaHostState2 = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
                TransitionViewState obtainViewState = obtainViewState(mediaHostState);
                if (obtainViewState == null) {
                    Trace.endSection();
                    return;
                }
                TransitionViewState updateViewStateToCarouselSize = updateViewStateToCarouselSize(obtainViewState, i4, this.tmpState2);
                Intrinsics.checkNotNull(updateViewStateToCarouselSize);
                this.layoutController.setMeasureState(updateViewStateToCarouselSize);
                this.animateNextStateChange = false;
                if (this.transitionLayout == null) {
                    Trace.endSection();
                    return;
                }
                TransitionViewState updateViewStateToCarouselSize2 = updateViewStateToCarouselSize(obtainViewState(mediaHostState2), i3, this.tmpState3);
                if (!mediaHostState.getVisible()) {
                    if (!(updateViewStateToCarouselSize2 == null || mediaHostState2 == null)) {
                        if (mediaHostState2.getVisible()) {
                            updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize2, mediaHostState2.getDisappearParameters(), f2, this.tmpState);
                        }
                    }
                    transitionViewState = updateViewStateToCarouselSize;
                    this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                    this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                    Unit unit = Unit.INSTANCE;
                    Trace.endSection();
                } else if (mediaHostState2 == null || mediaHostState2.getVisible()) {
                    if (!(f2 == 1.0f)) {
                        if (updateViewStateToCarouselSize2 != null) {
                            if (f2 != 0.0f) {
                                z2 = false;
                            }
                            if (!z2) {
                                updateViewStateToCarouselSize2 = this.layoutController.getInterpolatedState(updateViewStateToCarouselSize2, updateViewStateToCarouselSize, f2, this.tmpState);
                            }
                        }
                    }
                    transitionViewState = updateViewStateToCarouselSize;
                    this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                    this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                    Unit unit2 = Unit.INSTANCE;
                    Trace.endSection();
                } else {
                    updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize, mediaHostState.getDisappearParameters(), 1.0f - f2, this.tmpState);
                }
                transitionViewState = updateViewStateToCarouselSize2;
                this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                Unit unit22 = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void refreshState() {
        Trace.beginSection("MediaViewController#refreshState");
        try {
            this.viewStates.clear();
            if (this.firstRefresh) {
                ensureAllMeasurements();
                this.firstRefresh = false;
            }
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
