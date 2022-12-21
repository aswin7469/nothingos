package com.android.systemui.media;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.media.MediaViewHolder;
import com.android.systemui.media.RecommendationViewHolder;
import com.android.systemui.p012qs.PageIndicator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.provider.OnReorderingAllowedListener;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.systemui.util.animation.UniqueObjectHostViewKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\b\n\u0002\u0010\u0015\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0010*\u0001$\b\u0007\u0018\u00002\u00020\u0001B\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\b\b\u0001\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\u0019\u001a\u00020\u001a\u0012\u0006\u0010\u001b\u001a\u00020\u001c\u0012\u0006\u0010\u001d\u001a\u00020\u001e¢\u0006\u0002\u0010\u001fJ*\u0010a\u001a\u0002002\u0006\u0010b\u001a\u00020?2\b\u0010c\u001a\u0004\u0018\u00010?2\u0006\u0010d\u001a\u00020e2\u0006\u0010f\u001a\u000200H\u0002J \u0010g\u001a\u00020Z2\u0006\u0010b\u001a\u00020?2\u0006\u0010d\u001a\u00020h2\u0006\u0010i\u001a\u000200H\u0002J\u0010\u0010j\u001a\u00020Z2\b\b\u0002\u0010k\u001a\u000200J%\u0010l\u001a\u00020Z2\u0006\u0010m\u001a\u00020n2\u000e\u0010o\u001a\n\u0012\u0006\b\u0001\u0012\u00020?0pH\u0016¢\u0006\u0002\u0010qJ\b\u0010r\u001a\u00020GH\u0002J\b\u0010s\u001a\u00020ZH\u0002JZ\u0010t\u001a\u00020Z2\u0006\u0010u\u001a\u00020!2\u0006\u0010v\u001a\u00020!2\u0006\u0010w\u001a\u00020!2\u0006\u0010x\u001a\u00020y2\b\b\u0002\u0010z\u001a\u00020!2\b\b\u0002\u0010{\u001a\u00020!2\b\b\u0002\u0010|\u001a\u00020!2\b\b\u0002\u0010}\u001a\u00020!2\b\b\u0002\u0010~\u001a\u000200H\u0007J\u000f\u0010\u001a\u00020Z2\u0007\u0010\u0001\u001a\u000200J\t\u0010\u0001\u001a\u00020ZH\u0002JB\u0010\u0001\u001a\u0004\u0018\u00010Z2\u0006\u00107\u001a\u00020!2\b\u00105\u001a\u0004\u0018\u0001062\u0007\u0010\u0001\u001a\u0002002\n\b\u0002\u0010\u0001\u001a\u00030\u00012\n\b\u0002\u0010\u0001\u001a\u00030\u0001¢\u0006\u0003\u0010\u0001J\t\u0010\u0001\u001a\u00020ZH\u0002J%\u0010\u0001\u001a\u00020Z2\u0006\u0010b\u001a\u00020?2\t\b\u0002\u0010\u0001\u001a\u0002002\t\b\u0002\u0010\u0001\u001a\u000200J\u0015\u0010\u0001\u001a\u00020Z2\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0002J+\u0010\u0001\u001a\u00020Z2\u0007\u0010\u0001\u001a\u00020!2\u0007\u0010\u0001\u001a\u00020!2\u0007\u0010\u0001\u001a\u00020.2\u0007\u0010\u0001\u001a\u000200J\t\u0010\u0001\u001a\u00020ZH\u0002J\t\u0010\u0001\u001a\u00020ZH\u0002J\t\u0010\u0001\u001a\u00020ZH\u0002J\t\u0010\u0001\u001a\u00020ZH\u0002J\t\u0010\u0001\u001a\u00020ZH\u0002J\u001b\u0010\u0001\u001a\u00020Z2\u0007\u0010\u0001\u001a\u00020\u00062\u0007\u0010\u0001\u001a\u000200H\u0002J\u0012\u0010\u0001\u001a\u00020Z2\u0007\u0010\u0001\u001a\u000200H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020!X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u00020$X\u0004¢\u0006\u0004\n\u0002\u0010%R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020!X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020!X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010(\u001a\u00020!X\u000e¢\u0006\b\n\u0000\u0012\u0004\b)\u0010*R\u0014\u0010+\u001a\u00020!X\u000e¢\u0006\b\n\u0000\u0012\u0004\b,\u0010*R\u000e\u0010-\u001a\u00020.X\u000e¢\u0006\u0002\n\u0000R\u001e\u00101\u001a\u0002002\u0006\u0010/\u001a\u000200@BX\u000e¢\u0006\b\n\u0000\"\u0004\b2\u00103R\u000e\u00104\u001a\u000200X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u000106X\u000e¢\u0006\u0002\n\u0000R\u0014\u00107\u001a\u00020!X\u000e¢\u0006\b\n\u0000\u0012\u0004\b8\u0010*R\u0014\u00109\u001a\u0002008BX\u0004¢\u0006\u0006\u001a\u0004\b9\u0010:R\u001e\u0010;\u001a\u0002002\u0006\u0010/\u001a\u000200@BX\u000e¢\u0006\b\n\u0000\"\u0004\b<\u00103R\u0014\u0010=\u001a\b\u0012\u0004\u0012\u00020?0>X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020AX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010B\u001a\u00020C¢\u0006\b\n\u0000\u001a\u0004\bD\u0010ER\u000e\u0010F\u001a\u00020GX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010H\u001a\u00020G¢\u0006\b\n\u0000\u001a\u0004\bI\u0010JR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u000200X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020MX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u000200X\u000e¢\u0006\u0002\n\u0000R&\u0010Q\u001a\u00020P2\u0006\u0010O\u001a\u00020P8\u0006@BX.¢\u0006\u000e\n\u0000\u0012\u0004\bR\u0010*\u001a\u0004\bS\u0010TR\u001a\u0010U\u001a\u000200X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010:\"\u0004\bW\u00103R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R \u0010X\u001a\b\u0012\u0004\u0012\u00020Z0YX.¢\u0006\u000e\n\u0000\u001a\u0004\b[\u0010\\\"\u0004\b]\u0010^R\u000e\u0010_\u001a\u00020`X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/MediaCarouselController;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "mediaControlPanelFactory", "Ljavax/inject/Provider;", "Lcom/android/systemui/media/MediaControlPanel;", "visualStabilityProvider", "Lcom/android/systemui/statusbar/notification/collection/provider/VisualStabilityProvider;", "mediaHostStatesManager", "Lcom/android/systemui/media/MediaHostStatesManager;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "mediaManager", "Lcom/android/systemui/media/MediaDataManager;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "falsingCollector", "Lcom/android/systemui/classifier/FalsingCollector;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "logger", "Lcom/android/systemui/media/MediaUiEventLogger;", "debugLogger", "Lcom/android/systemui/media/MediaCarouselControllerLogger;", "(Landroid/content/Context;Ljavax/inject/Provider;Lcom/android/systemui/statusbar/notification/collection/provider/VisualStabilityProvider;Lcom/android/systemui/media/MediaHostStatesManager;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/media/MediaDataManager;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/classifier/FalsingCollector;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/media/MediaUiEventLogger;Lcom/android/systemui/media/MediaCarouselControllerLogger;)V", "carouselMeasureHeight", "", "carouselMeasureWidth", "configListener", "com/android/systemui/media/MediaCarouselController$configListener$1", "Lcom/android/systemui/media/MediaCarouselController$configListener$1;", "currentCarouselHeight", "currentCarouselWidth", "currentEndLocation", "getCurrentEndLocation$annotations", "()V", "currentStartLocation", "getCurrentStartLocation$annotations", "currentTransitionProgress", "", "value", "", "currentlyExpanded", "setCurrentlyExpanded", "(Z)V", "currentlyShowingOnlyActive", "desiredHostState", "Lcom/android/systemui/media/MediaHostState;", "desiredLocation", "getDesiredLocation$annotations", "isReorderingAllowed", "()Z", "isRtl", "setRtl", "keysNeedRemoval", "", "", "mediaCarousel", "Lcom/android/systemui/media/MediaScrollView;", "mediaCarouselScrollHandler", "Lcom/android/systemui/media/MediaCarouselScrollHandler;", "getMediaCarouselScrollHandler", "()Lcom/android/systemui/media/MediaCarouselScrollHandler;", "mediaContent", "Landroid/view/ViewGroup;", "mediaFrame", "getMediaFrame", "()Landroid/view/ViewGroup;", "needsReordering", "pageIndicator", "Lcom/android/systemui/qs/PageIndicator;", "playersVisible", "<set-?>", "Landroid/view/View;", "settingsButton", "getSettingsButton$annotations", "getSettingsButton", "()Landroid/view/View;", "shouldScrollToActivePlayer", "getShouldScrollToActivePlayer", "setShouldScrollToActivePlayer", "updateUserVisibility", "Lkotlin/Function0;", "", "getUpdateUserVisibility", "()Lkotlin/jvm/functions/Function0;", "setUpdateUserVisibility", "(Lkotlin/jvm/functions/Function0;)V", "visualStabilityCallback", "Lcom/android/systemui/statusbar/notification/collection/provider/OnReorderingAllowedListener;", "addOrUpdatePlayer", "key", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "isSsReactivated", "addSmartspaceMediaRecommendations", "Lcom/android/systemui/media/SmartspaceMediaData;", "shouldPrioritize", "closeGuts", "immediate", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "inflateMediaCarousel", "inflateSettingsButton", "logSmartspaceCardReported", "eventId", "instanceId", "uid", "surfaces", "", "interactedSubcardRank", "interactedSubcardCardinality", "rank", "receivedLatencyMillis", "isSwipeToDismiss", "logSmartspaceImpression", "qsExpanded", "maybeResetSettingsCog", "onDesiredLocationChanged", "animate", "duration", "", "startDelay", "(ILcom/android/systemui/media/MediaHostState;ZJJ)Lkotlin/Unit;", "onSwipeToDismiss", "removePlayer", "dismissMediaData", "dismissRecommendation", "reorderAllPlayers", "previousVisiblePlayerKey", "Lcom/android/systemui/media/MediaPlayerData$MediaSortKey;", "setCurrentState", "startLocation", "endLocation", "progress", "immediately", "updateCarouselDimensions", "updateCarouselSize", "updatePageIndicator", "updatePageIndicatorAlpha", "updatePageIndicatorLocation", "updatePlayerToState", "mediaPlayer", "noAnimation", "updatePlayers", "recreateMedia", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselController implements Dumpable {
    private final ActivityStarter activityStarter;
    private int carouselMeasureHeight;
    private int carouselMeasureWidth;
    private final MediaCarouselController$configListener$1 configListener;
    /* access modifiers changed from: private */
    public final Context context;
    private int currentCarouselHeight;
    private int currentCarouselWidth;
    private int currentEndLocation = -1;
    private int currentStartLocation = -1;
    private float currentTransitionProgress = 1.0f;
    private boolean currentlyExpanded = true;
    private boolean currentlyShowingOnlyActive;
    private final MediaCarouselControllerLogger debugLogger;
    private MediaHostState desiredHostState;
    /* access modifiers changed from: private */
    public int desiredLocation = -1;
    private boolean isRtl;
    /* access modifiers changed from: private */
    public Set<String> keysNeedRemoval = new LinkedHashSet();
    private final MediaUiEventLogger logger;
    private final MediaScrollView mediaCarousel;
    private final MediaCarouselScrollHandler mediaCarouselScrollHandler;
    private final ViewGroup mediaContent;
    private final Provider<MediaControlPanel> mediaControlPanelFactory;
    private final ViewGroup mediaFrame;
    private final MediaHostStatesManager mediaHostStatesManager;
    /* access modifiers changed from: private */
    public final MediaDataManager mediaManager;
    private boolean needsReordering;
    private final PageIndicator pageIndicator;
    private boolean playersVisible;
    private View settingsButton;
    private boolean shouldScrollToActivePlayer;
    /* access modifiers changed from: private */
    public final SystemClock systemClock;
    public Function0<Unit> updateUserVisibility;
    private final OnReorderingAllowedListener visualStabilityCallback;
    private final VisualStabilityProvider visualStabilityProvider;

    private static /* synthetic */ void getCurrentEndLocation$annotations() {
    }

    private static /* synthetic */ void getCurrentStartLocation$annotations() {
    }

    private static /* synthetic */ void getDesiredLocation$annotations() {
    }

    public static /* synthetic */ void getSettingsButton$annotations() {
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr) {
        int[] iArr2 = iArr;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        logSmartspaceCardReported$default(this, i, i2, i3, iArr2, 0, 0, 0, 0, false, 496, (Object) null);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr, int i4) {
        int[] iArr2 = iArr;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        logSmartspaceCardReported$default(this, i, i2, i3, iArr2, i4, 0, 0, 0, false, 480, (Object) null);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr, int i4, int i5) {
        int[] iArr2 = iArr;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        logSmartspaceCardReported$default(this, i, i2, i3, iArr2, i4, i5, 0, 0, false, StackStateAnimator.ANIMATION_DURATION_GO_TO_FULL_SHADE, (Object) null);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr, int i4, int i5, int i6) {
        int[] iArr2 = iArr;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        logSmartspaceCardReported$default(this, i, i2, i3, iArr2, i4, i5, i6, 0, false, 384, (Object) null);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7) {
        int[] iArr2 = iArr;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        logSmartspaceCardReported$default(this, i, i2, i3, iArr2, i4, i5, i6, i7, false, 256, (Object) null);
    }

    @Inject
    public MediaCarouselController(Context context2, Provider<MediaControlPanel> provider, VisualStabilityProvider visualStabilityProvider2, MediaHostStatesManager mediaHostStatesManager2, ActivityStarter activityStarter2, SystemClock systemClock2, @Main DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, DumpManager dumpManager, MediaUiEventLogger mediaUiEventLogger, MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler2;
        Context context3 = context2;
        Provider<MediaControlPanel> provider2 = provider;
        VisualStabilityProvider visualStabilityProvider3 = visualStabilityProvider2;
        MediaHostStatesManager mediaHostStatesManager3 = mediaHostStatesManager2;
        ActivityStarter activityStarter3 = activityStarter2;
        SystemClock systemClock3 = systemClock2;
        MediaDataManager mediaDataManager2 = mediaDataManager;
        ConfigurationController configurationController2 = configurationController;
        DumpManager dumpManager2 = dumpManager;
        MediaUiEventLogger mediaUiEventLogger2 = mediaUiEventLogger;
        MediaCarouselControllerLogger mediaCarouselControllerLogger2 = mediaCarouselControllerLogger;
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(provider2, "mediaControlPanelFactory");
        Intrinsics.checkNotNullParameter(visualStabilityProvider3, "visualStabilityProvider");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager3, "mediaHostStatesManager");
        Intrinsics.checkNotNullParameter(activityStarter3, "activityStarter");
        Intrinsics.checkNotNullParameter(systemClock3, "systemClock");
        DelayableExecutor delayableExecutor2 = delayableExecutor;
        Intrinsics.checkNotNullParameter(delayableExecutor2, "executor");
        Intrinsics.checkNotNullParameter(mediaDataManager2, "mediaManager");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(mediaUiEventLogger2, "logger");
        Intrinsics.checkNotNullParameter(mediaCarouselControllerLogger2, "debugLogger");
        this.context = context3;
        this.mediaControlPanelFactory = provider2;
        this.visualStabilityProvider = visualStabilityProvider3;
        this.mediaHostStatesManager = mediaHostStatesManager3;
        this.activityStarter = activityStarter3;
        this.systemClock = systemClock3;
        this.mediaManager = mediaDataManager2;
        this.logger = mediaUiEventLogger2;
        this.debugLogger = mediaCarouselControllerLogger2;
        MediaCarouselController$configListener$1 mediaCarouselController$configListener$1 = new MediaCarouselController$configListener$1(this);
        this.configListener = mediaCarouselController$configListener$1;
        dumpManager2.registerDumpable("MediaCarouselController", this);
        ViewGroup inflateMediaCarousel = inflateMediaCarousel();
        this.mediaFrame = inflateMediaCarousel;
        View requireViewById = inflateMediaCarousel.requireViewById(C1893R.C1897id.media_carousel_scroller);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "mediaFrame.requireViewBy….media_carousel_scroller)");
        MediaScrollView mediaScrollView = (MediaScrollView) requireViewById;
        this.mediaCarousel = mediaScrollView;
        View requireViewById2 = inflateMediaCarousel.requireViewById(C1893R.C1897id.media_page_indicator);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "mediaFrame.requireViewBy….id.media_page_indicator)");
        PageIndicator pageIndicator2 = (PageIndicator) requireViewById2;
        this.pageIndicator = pageIndicator2;
        MediaScrollView mediaScrollView2 = mediaScrollView;
        new MediaCarouselScrollHandler(mediaScrollView, pageIndicator2, delayableExecutor2, new Function0<Unit>(this) {
            public final void invoke() {
                ((MediaCarouselController) this.receiver).onSwipeToDismiss();
            }
        }, new Function0<Unit>(this) {
            public final void invoke() {
                ((MediaCarouselController) this.receiver).updatePageIndicatorLocation();
            }
        }, new Function1<Boolean, Unit>(this) {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).closeGuts(z);
            }
        }, falsingCollector, falsingManager, new Function1<Boolean, Unit>(this) {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).logSmartspaceImpression(z);
            }
        }, mediaUiEventLogger);
        this.mediaCarouselScrollHandler = mediaCarouselScrollHandler2;
        setRtl(context2.getResources().getConfiguration().getLayoutDirection() != 1 ? false : true);
        inflateSettingsButton();
        View requireViewById3 = mediaScrollView2.requireViewById(C1893R.C1897id.media_carousel);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "mediaCarousel.requireViewById(R.id.media_carousel)");
        this.mediaContent = (ViewGroup) requireViewById3;
        configurationController2.addCallback(mediaCarouselController$configListener$1);
        MediaCarouselController$$ExternalSyntheticLambda0 mediaCarouselController$$ExternalSyntheticLambda0 = new MediaCarouselController$$ExternalSyntheticLambda0(this);
        this.visualStabilityCallback = mediaCarouselController$$ExternalSyntheticLambda0;
        visualStabilityProvider3.addPersistentReorderingAllowedListener(mediaCarouselController$$ExternalSyntheticLambda0);
        mediaDataManager2.addListener(new MediaDataManager.Listener(this) {
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
                MediaCarouselController mediaCarouselController;
                String str3 = str;
                MediaData mediaData2 = mediaData;
                Intrinsics.checkNotNullParameter(str3, "key");
                Intrinsics.checkNotNullParameter(mediaData2, "data");
                boolean z3 = false;
                if (this.this$0.addOrUpdatePlayer(str3, str2, mediaData2, z2)) {
                    MediaControlPanel mediaPlayer = MediaPlayerData.INSTANCE.getMediaPlayer(str3);
                    if (mediaPlayer != null) {
                        MediaCarouselController.logSmartspaceCardReported$default(this.this$0, 759, mediaPlayer.mSmartspaceId, mediaPlayer.mUid, new int[]{4, 2, 5}, 0, 0, MediaPlayerData.INSTANCE.getMediaPlayerIndex(str3), 0, false, 432, (Object) null);
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && this.this$0.getMediaCarouselScrollHandler().getVisibleMediaIndex() == MediaPlayerData.INSTANCE.getMediaPlayerIndex(str3)) {
                        MediaCarouselController mediaCarouselController2 = this.this$0;
                        mediaCarouselController2.logSmartspaceImpression(mediaCarouselController2.getMediaCarouselScrollHandler().getQsExpanded());
                    }
                } else if (i != 0) {
                    MediaCarouselController mediaCarouselController3 = this.this$0;
                    int i2 = 0;
                    for (Object next : MediaPlayerData.INSTANCE.players()) {
                        int i3 = i2 + 1;
                        if (i2 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
                        if (mediaControlPanel.getRecommendationViewHolder() == null) {
                            mediaControlPanel.mSmartspaceId = SmallHash.hash(mediaControlPanel.mUid + ((int) mediaCarouselController3.systemClock.currentTimeMillis()));
                            mediaControlPanel.mIsImpressed = false;
                            mediaCarouselController = mediaCarouselController3;
                            MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController3, 759, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{4, 2, 5}, 0, 0, i2, i, false, 304, (Object) null);
                        } else {
                            mediaCarouselController = mediaCarouselController3;
                        }
                        i2 = i3;
                        mediaCarouselController3 = mediaCarouselController;
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && !this.this$0.getMediaCarouselScrollHandler().getQsExpanded()) {
                        MediaCarouselController mediaCarouselController4 = this.this$0;
                        mediaCarouselController4.logSmartspaceImpression(mediaCarouselController4.getMediaCarouselScrollHandler().getQsExpanded());
                    }
                }
                Boolean isPlaying = mediaData.isPlaying();
                if ((isPlaying != null ? !isPlaying.booleanValue() : mediaData.isClearable()) && !mediaData.getActive()) {
                    z3 = true;
                }
                if (!z3 || Utils.useMediaResumption(this.this$0.context)) {
                    this.this$0.keysNeedRemoval.remove(str3);
                } else if (this.this$0.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str3);
                }
            }

            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
                MediaCarouselController mediaCarouselController;
                String str2 = str;
                SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
                boolean z2 = z;
                Intrinsics.checkNotNullParameter(str2, "key");
                Intrinsics.checkNotNullParameter(smartspaceMediaData2, "data");
                if (MediaCarouselControllerKt.DEBUG) {
                    Log.d("MediaCarouselController", "Loading Smartspace media update");
                }
                boolean z3 = true;
                if (smartspaceMediaData.isActive()) {
                    boolean z4 = false;
                    if (this.this$0.mediaManager.hasActiveMedia() || !this.this$0.mediaManager.hasAnyMedia() || !z2) {
                        z3 = false;
                    }
                    if (z3) {
                        MediaCarouselController mediaCarouselController2 = this.this$0;
                        int i = 0;
                        for (Object next : MediaPlayerData.INSTANCE.players()) {
                            int i2 = i + 1;
                            if (i < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
                            if (mediaControlPanel.getRecommendationViewHolder() == null) {
                                mediaControlPanel.mSmartspaceId = SmallHash.hash(mediaControlPanel.mUid + ((int) mediaCarouselController2.systemClock.currentTimeMillis()));
                                mediaControlPanel.mIsImpressed = z4;
                                mediaCarouselController = mediaCarouselController2;
                                MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController2, 759, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{4, 2, 5}, 0, 0, i, (int) (mediaCarouselController2.systemClock.currentTimeMillis() - smartspaceMediaData.getHeadphoneConnectionTimeMillis()), false, 304, (Object) null);
                            } else {
                                mediaCarouselController = mediaCarouselController2;
                            }
                            i = i2;
                            mediaCarouselController2 = mediaCarouselController;
                            z4 = false;
                        }
                    }
                    this.this$0.addSmartspaceMediaRecommendations(str2, smartspaceMediaData2, z2);
                    MediaControlPanel mediaPlayer = MediaPlayerData.INSTANCE.getMediaPlayer(str2);
                    if (mediaPlayer != null) {
                        MediaCarouselController mediaCarouselController3 = this.this$0;
                        MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController3, 759, mediaPlayer.mSmartspaceId, mediaPlayer.mUid, new int[]{4, 2, 5}, 0, 0, MediaPlayerData.INSTANCE.getMediaPlayerIndex(str2), (int) (mediaCarouselController3.systemClock.currentTimeMillis() - smartspaceMediaData.getHeadphoneConnectionTimeMillis()), false, 304, (Object) null);
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && this.this$0.getMediaCarouselScrollHandler().getVisibleMediaIndex() == MediaPlayerData.INSTANCE.getMediaPlayerIndex(str2)) {
                        MediaCarouselController mediaCarouselController4 = this.this$0;
                        mediaCarouselController4.logSmartspaceImpression(mediaCarouselController4.getMediaCarouselScrollHandler().getQsExpanded());
                        return;
                    }
                    return;
                }
                onSmartspaceMediaDataRemoved(smartspaceMediaData.getTargetId(), true);
            }

            public void onMediaDataRemoved(String str) {
                Intrinsics.checkNotNullParameter(str, "key");
                MediaCarouselController.removePlayer$default(this.this$0, str, false, false, 6, (Object) null);
            }

            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
                Intrinsics.checkNotNullParameter(str, "key");
                if (MediaCarouselControllerKt.DEBUG) {
                    Log.d("MediaCarouselController", "My Smartspace media removal request is received");
                }
                if (z || this.this$0.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str);
                }
            }
        });
        inflateMediaCarousel.addOnLayoutChangeListener(new MediaCarouselController$$ExternalSyntheticLambda1(this));
        mediaHostStatesManager2.addCallback(new MediaHostStatesManager.Callback(this) {
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public void onHostStateChanged(int i, MediaHostState mediaHostState) {
                Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
                if (i == this.this$0.desiredLocation) {
                    MediaCarouselController mediaCarouselController = this.this$0;
                    MediaCarouselController.onDesiredLocationChanged$default(mediaCarouselController, mediaCarouselController.desiredLocation, mediaHostState, false, 0, 0, 24, (Object) null);
                }
            }
        });
    }

    public final MediaCarouselScrollHandler getMediaCarouselScrollHandler() {
        return this.mediaCarouselScrollHandler;
    }

    public final ViewGroup getMediaFrame() {
        return this.mediaFrame;
    }

    public final View getSettingsButton() {
        View view = this.settingsButton;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        return null;
    }

    public final boolean getShouldScrollToActivePlayer() {
        return this.shouldScrollToActivePlayer;
    }

    public final void setShouldScrollToActivePlayer(boolean z) {
        this.shouldScrollToActivePlayer = z;
    }

    /* access modifiers changed from: private */
    public final void setRtl(boolean z) {
        if (z != this.isRtl) {
            this.isRtl = z;
            this.mediaFrame.setLayoutDirection(z ? 1 : 0);
            this.mediaCarouselScrollHandler.scrollToStart();
        }
    }

    private final void setCurrentlyExpanded(boolean z) {
        if (this.currentlyExpanded != z) {
            this.currentlyExpanded = z;
            for (MediaControlPanel listening : MediaPlayerData.INSTANCE.players()) {
                listening.setListening(this.currentlyExpanded);
            }
        }
    }

    public final Function0<Unit> getUpdateUserVisibility() {
        Function0<Unit> function0 = this.updateUserVisibility;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("updateUserVisibility");
        return null;
    }

    public final void setUpdateUserVisibility(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.updateUserVisibility = function0;
    }

    /* access modifiers changed from: private */
    public final boolean isReorderingAllowed() {
        return this.visualStabilityProvider.isReorderingAllowed();
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-1  reason: not valid java name */
    public static final void m2769_init_$lambda1(MediaCarouselController mediaCarouselController) {
        Intrinsics.checkNotNullParameter(mediaCarouselController, "this$0");
        if (mediaCarouselController.needsReordering) {
            mediaCarouselController.needsReordering = false;
            mediaCarouselController.reorderAllPlayers((MediaPlayerData.MediaSortKey) null);
        }
        for (String removePlayer$default : mediaCarouselController.keysNeedRemoval) {
            removePlayer$default(mediaCarouselController, removePlayer$default, false, false, 6, (Object) null);
        }
        mediaCarouselController.keysNeedRemoval.clear();
        if (mediaCarouselController.updateUserVisibility != null) {
            mediaCarouselController.getUpdateUserVisibility().invoke();
        }
        mediaCarouselController.mediaCarouselScrollHandler.scrollToStart();
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-2  reason: not valid java name */
    public static final void m2770_init_$lambda2(MediaCarouselController mediaCarouselController, View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Intrinsics.checkNotNullParameter(mediaCarouselController, "this$0");
        mediaCarouselController.updatePageIndicatorLocation();
    }

    /* access modifiers changed from: private */
    public final void inflateSettingsButton() {
        View inflate = LayoutInflater.from(this.context).inflate(C1893R.layout.media_carousel_settings_button, this.mediaFrame, false);
        if (inflate != null) {
            if (this.settingsButton != null) {
                this.mediaFrame.removeView(getSettingsButton());
            }
            this.settingsButton = inflate;
            this.mediaFrame.addView(getSettingsButton());
            this.mediaCarouselScrollHandler.onSettingsButtonUpdated(inflate);
            getSettingsButton().setOnClickListener(new MediaCarouselController$$ExternalSyntheticLambda2(this));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    /* access modifiers changed from: private */
    /* renamed from: inflateSettingsButton$lambda-3  reason: not valid java name */
    public static final void m2771inflateSettingsButton$lambda3(MediaCarouselController mediaCarouselController, View view) {
        Intrinsics.checkNotNullParameter(mediaCarouselController, "this$0");
        mediaCarouselController.logger.logCarouselSettings();
        mediaCarouselController.activityStarter.startActivity(MediaCarouselControllerKt.settingsIntent, true);
    }

    private final ViewGroup inflateMediaCarousel() {
        View inflate = LayoutInflater.from(this.context).inflate(C1893R.layout.media_carousel, new UniqueObjectHostView(this.context), false);
        if (inflate != null) {
            ViewGroup viewGroup = (ViewGroup) inflate;
            viewGroup.setLayoutDirection(3);
            return viewGroup;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    private final void reorderAllPlayers(MediaPlayerData.MediaSortKey mediaSortKey) {
        Unit unit;
        this.mediaContent.removeAllViews();
        for (MediaControlPanel next : MediaPlayerData.INSTANCE.players()) {
            MediaViewHolder mediaViewHolder = next.getMediaViewHolder();
            if (mediaViewHolder != null) {
                this.mediaContent.addView(mediaViewHolder.getPlayer());
            } else {
                RecommendationViewHolder recommendationViewHolder = next.getRecommendationViewHolder();
                if (recommendationViewHolder != null) {
                    this.mediaContent.addView(recommendationViewHolder.getRecommendations());
                }
            }
        }
        this.mediaCarouselScrollHandler.onPlayersChanged();
        if (this.shouldScrollToActivePlayer) {
            this.shouldScrollToActivePlayer = false;
            int firstActiveMediaIndex = MediaPlayerData.INSTANCE.firstActiveMediaIndex();
            int i = -1;
            if (firstActiveMediaIndex != -1) {
                if (mediaSortKey != null) {
                    Iterator it = MediaPlayerData.INSTANCE.playerKeys().iterator();
                    int i2 = 0;
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Object next2 = it.next();
                        if (i2 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        if (Intrinsics.areEqual((Object) mediaSortKey, (Object) (MediaPlayerData.MediaSortKey) next2)) {
                            i = i2;
                            break;
                        }
                        i2++;
                    }
                    this.mediaCarouselScrollHandler.scrollToPlayer(i, firstActiveMediaIndex);
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                if (unit == null) {
                    MediaCarouselScrollHandler.scrollToPlayer$default(this.mediaCarouselScrollHandler, 0, firstActiveMediaIndex, 1, (Object) null);
                }
            }
        }
    }

    public static /* synthetic */ void removePlayer$default(MediaCarouselController mediaCarouselController, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        if ((i & 4) != 0) {
            z2 = true;
        }
        mediaCarouselController.removePlayer(str, z, z2);
    }

    public final void removePlayer(String str, boolean z, boolean z2) {
        SmartspaceMediaData smartspaceMediaData$SystemUI_nothingRelease;
        Intrinsics.checkNotNullParameter(str, "key");
        if (Intrinsics.areEqual((Object) str, (Object) MediaPlayerData.INSTANCE.smartspaceMediaKey()) && (smartspaceMediaData$SystemUI_nothingRelease = MediaPlayerData.INSTANCE.getSmartspaceMediaData$SystemUI_nothingRelease()) != null) {
            this.logger.logRecommendationRemoved(smartspaceMediaData$SystemUI_nothingRelease.getPackageName(), smartspaceMediaData$SystemUI_nothingRelease.getInstanceId());
        }
        MediaControlPanel removeMediaPlayer = MediaPlayerData.INSTANCE.removeMediaPlayer(str);
        if (removeMediaPlayer != null) {
            this.mediaCarouselScrollHandler.onPrePlayerRemoved(removeMediaPlayer);
            ViewGroup viewGroup = this.mediaContent;
            MediaViewHolder mediaViewHolder = removeMediaPlayer.getMediaViewHolder();
            TransitionLayout transitionLayout = null;
            viewGroup.removeView(mediaViewHolder != null ? mediaViewHolder.getPlayer() : null);
            ViewGroup viewGroup2 = this.mediaContent;
            RecommendationViewHolder recommendationViewHolder = removeMediaPlayer.getRecommendationViewHolder();
            if (recommendationViewHolder != null) {
                transitionLayout = recommendationViewHolder.getRecommendations();
            }
            viewGroup2.removeView(transitionLayout);
            removeMediaPlayer.onDestroy();
            this.mediaCarouselScrollHandler.onPlayersChanged();
            updatePageIndicator();
            if (z) {
                this.mediaManager.dismissMediaData(str, 0);
            }
            if (z2) {
                this.mediaManager.dismissSmartspaceRecommendation(str, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void updatePlayers(boolean z) {
        this.pageIndicator.setTintList(ColorStateList.valueOf(this.context.getColor(C1893R.C1894color.media_paging_indicator)));
        for (Triple triple : MediaPlayerData.INSTANCE.mediaData()) {
            String str = (String) triple.component1();
            MediaData mediaData = (MediaData) triple.component2();
            if (((Boolean) triple.component3()).booleanValue()) {
                SmartspaceMediaData smartspaceMediaData$SystemUI_nothingRelease = MediaPlayerData.INSTANCE.getSmartspaceMediaData$SystemUI_nothingRelease();
                removePlayer(str, false, false);
                if (smartspaceMediaData$SystemUI_nothingRelease != null) {
                    addSmartspaceMediaRecommendations(smartspaceMediaData$SystemUI_nothingRelease.getTargetId(), smartspaceMediaData$SystemUI_nothingRelease, MediaPlayerData.INSTANCE.getShouldPrioritizeSs$SystemUI_nothingRelease());
                }
            } else {
                boolean isSsReactivated = MediaPlayerData.INSTANCE.isSsReactivated(str);
                if (z) {
                    removePlayer(str, false, false);
                }
                addOrUpdatePlayer(str, (String) null, mediaData, isSsReactivated);
            }
        }
    }

    private final void updatePageIndicator() {
        int childCount = this.mediaContent.getChildCount();
        this.pageIndicator.setNumPages(childCount);
        if (childCount == 1) {
            this.pageIndicator.setLocation(0.0f);
        }
        updatePageIndicatorAlpha();
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        if (i == this.currentStartLocation && i2 == this.currentEndLocation) {
            if ((f == this.currentTransitionProgress) && !z) {
                return;
            }
        }
        this.currentStartLocation = i;
        this.currentEndLocation = i2;
        this.currentTransitionProgress = f;
        for (MediaControlPanel next : MediaPlayerData.INSTANCE.players()) {
            Intrinsics.checkNotNullExpressionValue(next, "mediaPlayer");
            updatePlayerToState(next, z);
        }
        maybeResetSettingsCog();
        updatePageIndicatorAlpha();
    }

    private final void updatePageIndicatorAlpha() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean z = false;
        boolean visible = mediaHostState != null ? mediaHostState.getVisible() : false;
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        if (mediaHostState2 != null) {
            z = mediaHostState2.getVisible();
        }
        float f = 1.0f;
        float f2 = z ? 1.0f : 0.0f;
        float f3 = visible ? 1.0f : 0.0f;
        if (!visible || !z) {
            float f4 = this.currentTransitionProgress;
            if (!visible) {
                f4 = 1.0f - f4;
            }
            f = MathUtils.lerp(f2, f3, MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 0.0f, 1.0f, f4), 0.0f, 1.0f));
        }
        this.pageIndicator.setAlpha(f);
    }

    /* access modifiers changed from: private */
    public final void updatePageIndicatorLocation() {
        int i;
        int i2;
        if (this.isRtl) {
            i2 = this.pageIndicator.getWidth();
            i = this.currentCarouselWidth;
        } else {
            i2 = this.currentCarouselWidth;
            i = this.pageIndicator.getWidth();
        }
        this.pageIndicator.setTranslationX((((float) (i2 - i)) / 2.0f) + this.mediaCarouselScrollHandler.getContentTranslation());
        ViewGroup.LayoutParams layoutParams = this.pageIndicator.getLayoutParams();
        if (layoutParams != null) {
            PageIndicator pageIndicator2 = this.pageIndicator;
            pageIndicator2.setTranslationY((float) ((this.currentCarouselHeight - pageIndicator2.getHeight()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
    }

    /* access modifiers changed from: private */
    public final void updateCarouselDimensions() {
        int i = 0;
        int i2 = 0;
        for (MediaControlPanel mediaViewController : MediaPlayerData.INSTANCE.players()) {
            MediaViewController mediaViewController2 = mediaViewController.getMediaViewController();
            Intrinsics.checkNotNullExpressionValue(mediaViewController2, "mediaPlayer.mediaViewController");
            i = Math.max(i, mediaViewController2.getCurrentWidth() + ((int) mediaViewController2.getTranslationX()));
            i2 = Math.max(i2, mediaViewController2.getCurrentHeight() + ((int) mediaViewController2.getTranslationY()));
        }
        if (i != this.currentCarouselWidth || i2 != this.currentCarouselHeight) {
            this.currentCarouselWidth = i;
            this.currentCarouselHeight = i2;
            this.mediaCarouselScrollHandler.setCarouselBounds(i, i2);
            updatePageIndicatorLocation();
        }
    }

    private final void maybeResetSettingsCog() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean showsOnlyActiveMedia = mediaHostState != null ? mediaHostState.getShowsOnlyActiveMedia() : true;
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        boolean showsOnlyActiveMedia2 = mediaHostState2 != null ? mediaHostState2.getShowsOnlyActiveMedia() : showsOnlyActiveMedia;
        if (this.currentlyShowingOnlyActive == showsOnlyActiveMedia) {
            float f = this.currentTransitionProgress;
            boolean z = false;
            if (!(f == 1.0f)) {
                if (f == 0.0f) {
                    z = true;
                }
                if (z || showsOnlyActiveMedia2 == showsOnlyActiveMedia) {
                    return;
                }
            } else {
                return;
            }
        }
        this.currentlyShowingOnlyActive = showsOnlyActiveMedia;
        this.mediaCarouselScrollHandler.resetTranslation(true);
    }

    private final void updatePlayerToState(MediaControlPanel mediaControlPanel, boolean z) {
        mediaControlPanel.getMediaViewController().setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
    }

    public static /* synthetic */ Unit onDesiredLocationChanged$default(MediaCarouselController mediaCarouselController, int i, MediaHostState mediaHostState, boolean z, long j, long j2, int i2, Object obj) {
        return mediaCarouselController.onDesiredLocationChanged(i, mediaHostState, z, (i2 & 8) != 0 ? 200 : j, (i2 & 16) != 0 ? 0 : j2);
    }

    public static /* synthetic */ void closeGuts$default(MediaCarouselController mediaCarouselController, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        mediaCarouselController.closeGuts(z);
    }

    public final void closeGuts(boolean z) {
        for (MediaControlPanel closeGuts : MediaPlayerData.INSTANCE.players()) {
            closeGuts.closeGuts(z);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r0 = r0.getMeasurementInput();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateCarouselSize() {
        /*
            r6 = this;
            com.android.systemui.media.MediaHostState r0 = r6.desiredHostState
            r1 = 0
            if (r0 == 0) goto L_0x0010
            com.android.systemui.util.animation.MeasurementInput r0 = r0.getMeasurementInput()
            if (r0 == 0) goto L_0x0010
            int r0 = r0.getWidth()
            goto L_0x0011
        L_0x0010:
            r0 = r1
        L_0x0011:
            com.android.systemui.media.MediaHostState r2 = r6.desiredHostState
            if (r2 == 0) goto L_0x0020
            com.android.systemui.util.animation.MeasurementInput r2 = r2.getMeasurementInput()
            if (r2 == 0) goto L_0x0020
            int r2 = r2.getHeight()
            goto L_0x0021
        L_0x0020:
            r2 = r1
        L_0x0021:
            int r3 = r6.carouselMeasureWidth
            if (r0 == r3) goto L_0x0027
            if (r0 != 0) goto L_0x002d
        L_0x0027:
            int r3 = r6.carouselMeasureHeight
            if (r2 == r3) goto L_0x0072
            if (r2 == 0) goto L_0x0072
        L_0x002d:
            r6.carouselMeasureWidth = r0
            r6.carouselMeasureHeight = r2
            android.content.Context r2 = r6.context
            android.content.res.Resources r2 = r2.getResources()
            r3 = 2131166895(0x7f0706af, float:1.7948048E38)
            int r2 = r2.getDimensionPixelSize(r3)
            int r2 = r2 + r0
            com.android.systemui.media.MediaHostState r3 = r6.desiredHostState
            if (r3 == 0) goto L_0x004e
            com.android.systemui.util.animation.MeasurementInput r3 = r3.getMeasurementInput()
            if (r3 == 0) goto L_0x004e
            int r3 = r3.getWidthMeasureSpec()
            goto L_0x004f
        L_0x004e:
            r3 = r1
        L_0x004f:
            com.android.systemui.media.MediaHostState r4 = r6.desiredHostState
            if (r4 == 0) goto L_0x005e
            com.android.systemui.util.animation.MeasurementInput r4 = r4.getMeasurementInput()
            if (r4 == 0) goto L_0x005e
            int r4 = r4.getHeightMeasureSpec()
            goto L_0x005f
        L_0x005e:
            r4 = r1
        L_0x005f:
            com.android.systemui.media.MediaScrollView r5 = r6.mediaCarousel
            r5.measure(r3, r4)
            com.android.systemui.media.MediaScrollView r3 = r6.mediaCarousel
            int r4 = r3.getMeasuredHeight()
            r3.layout(r1, r1, r0, r4)
            com.android.systemui.media.MediaCarouselScrollHandler r6 = r6.mediaCarouselScrollHandler
            r6.setPlayerWidthPlusPadding(r2)
        L_0x0072:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaCarouselController.updateCarouselSize():void");
    }

    public final void logSmartspaceImpression(boolean z) {
        int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
        if (visibleMediaIndex > 0 && MediaPlayerData.INSTANCE.players().size() > visibleMediaIndex) {
            Object elementAt = CollectionsKt.elementAt(MediaPlayerData.INSTANCE.players(), visibleMediaIndex);
            Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.players(…mentAt(visibleMediaIndex)");
            MediaControlPanel mediaControlPanel = (MediaControlPanel) elementAt;
            if (MediaPlayerData.INSTANCE.hasActiveMediaOrRecommendationCard() || z) {
                logSmartspaceCardReported$default(this, 800, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{mediaControlPanel.getSurfaceForSmartspaceLogging()}, 0, 0, 0, 0, false, 496, (Object) null);
                mediaControlPanel.mIsImpressed = true;
            }
        }
    }

    public static /* synthetic */ void logSmartspaceCardReported$default(MediaCarouselController mediaCarouselController, int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, boolean z, int i8, Object obj) {
        int i9;
        int i10 = i8;
        int i11 = (i10 & 16) != 0 ? 0 : i4;
        int i12 = (i10 & 32) != 0 ? 0 : i5;
        if ((i10 & 64) != 0) {
            i9 = mediaCarouselController.mediaCarouselScrollHandler.getVisibleMediaIndex();
        } else {
            MediaCarouselController mediaCarouselController2 = mediaCarouselController;
            i9 = i6;
        }
        mediaCarouselController.logSmartspaceCardReported(i, i2, i3, iArr, i11, i12, i9, (i10 & 128) != 0 ? 0 : i7, (i10 & 256) != 0 ? false : z);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, boolean z) {
        int i8;
        int[] iArr2 = iArr;
        int i9 = i6;
        Intrinsics.checkNotNullParameter(iArr2, "surfaces");
        if (MediaPlayerData.INSTANCE.players().size() > i9) {
            Object elementAt = CollectionsKt.elementAt(MediaPlayerData.INSTANCE.playerKeys(), i9);
            Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.playerKeys().elementAt(rank)");
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) elementAt;
            if (mediaSortKey.isSsMediaRec() || this.mediaManager.getSmartspaceMediaData().isActive() || MediaPlayerData.INSTANCE.getSmartspaceMediaData$SystemUI_nothingRelease() != null) {
                int childCount = this.mediaContent.getChildCount();
                int length = iArr2.length;
                int i10 = 0;
                while (i10 < length) {
                    int i11 = iArr2[i10];
                    int i12 = z ? -1 : i9;
                    if (mediaSortKey.isSsMediaRec()) {
                        i8 = 15;
                    } else {
                        i8 = mediaSortKey.isSsReactivated() ? 43 : 31;
                    }
                    int i13 = i11;
                    int i14 = i10;
                    int i15 = length;
                    SysUiStatsLog.write(SysUiStatsLog.SMARTSPACE_CARD_REPORTED, i, i2, 0, i11, i12, childCount, i8, i3, i4, i5, i7, (byte[]) null);
                    if (MediaCarouselControllerKt.DEBUG) {
                        Log.d("MediaCarouselController", "Log Smartspace card event id: " + i + " instance id: " + i2 + " surface: " + i13 + " rank: " + i9 + " cardinality: " + childCount + " isRecommendationCard: " + mediaSortKey.isSsMediaRec() + " isSsReactivated: " + mediaSortKey.isSsReactivated() + "uid: " + i3 + " interactedSubcardRank: " + i4 + " interactedSubcardCardinality: " + i5 + " received_latency_millis: " + i7);
                    } else {
                        int i16 = i;
                        int i17 = i2;
                        int i18 = i3;
                        int i19 = i4;
                        int i20 = i5;
                        int i21 = i7;
                    }
                    i10 = i14 + 1;
                    length = i15;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void onSwipeToDismiss() {
        Iterator it;
        Iterator it2 = MediaPlayerData.INSTANCE.players().iterator();
        int i = 0;
        while (it2.hasNext()) {
            Object next = it2.next();
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
            if (mediaControlPanel.mIsImpressed) {
                it = it2;
                logSmartspaceCardReported$default(this, 761, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{mediaControlPanel.getSurfaceForSmartspaceLogging()}, 0, 0, i, 0, true, 176, (Object) null);
                mediaControlPanel.mIsImpressed = false;
            } else {
                it = it2;
            }
            i = i2;
            it2 = it;
        }
        this.logger.logSwipeDismiss();
        this.mediaManager.onSwipeToDismiss();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("keysNeedRemoval: " + this.keysNeedRemoval);
        printWriter.println("dataKeys: " + MediaPlayerData.INSTANCE.dataKeys());
        printWriter.println("playerSortKeys: " + MediaPlayerData.INSTANCE.playerKeys());
        printWriter.println("smartspaceMediaData: " + MediaPlayerData.INSTANCE.getSmartspaceMediaData$SystemUI_nothingRelease());
        printWriter.println("shouldPrioritizeSs: " + MediaPlayerData.INSTANCE.getShouldPrioritizeSs$SystemUI_nothingRelease());
        printWriter.println("current size: " + this.currentCarouselWidth + " x " + this.currentCarouselHeight);
        printWriter.println("location: " + this.desiredLocation);
        StringBuilder sb = new StringBuilder("state: ");
        MediaHostState mediaHostState = this.desiredHostState;
        Boolean bool = null;
        StringBuilder append = sb.append((Object) mediaHostState != null ? Float.valueOf(mediaHostState.getExpansion()) : null).append(", only active ");
        MediaHostState mediaHostState2 = this.desiredHostState;
        if (mediaHostState2 != null) {
            bool = Boolean.valueOf(mediaHostState2.getShowsOnlyActiveMedia());
        }
        printWriter.println(append.append((Object) bool).toString());
    }

    /* access modifiers changed from: private */
    public final boolean addOrUpdatePlayer(String str, String str2, MediaData mediaData, boolean z) {
        TransitionLayout player;
        String str3 = str;
        MediaData mediaData2 = mediaData;
        Trace.beginSection("MediaCarouselController#addOrUpdatePlayer");
        try {
            MediaPlayerData.moveIfExists$default(MediaPlayerData.INSTANCE, str2, str, (MediaCarouselControllerLogger) null, 4, (Object) null);
            MediaControlPanel mediaPlayer = MediaPlayerData.INSTANCE.getMediaPlayer(str);
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(MediaPlayerData.INSTANCE.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex());
            boolean z2 = true;
            if (mediaPlayer == null) {
                MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
                MediaViewHolder.Companion companion = MediaViewHolder.Companion;
                LayoutInflater from = LayoutInflater.from(this.context);
                Intrinsics.checkNotNullExpressionValue(from, "from(context)");
                mediaControlPanel.attachPlayer(companion.create(from, this.mediaContent));
                mediaControlPanel.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addOrUpdatePlayer$1$1(this));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                MediaViewHolder mediaViewHolder = mediaControlPanel.getMediaViewHolder();
                if (!(mediaViewHolder == null || (player = mediaViewHolder.getPlayer()) == null)) {
                    player.setLayoutParams(layoutParams);
                }
                mediaControlPanel.bindPlayer(mediaData2, str);
                mediaControlPanel.setListening(this.currentlyExpanded);
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newPlayer");
                mediaPlayerData.addMediaPlayer(str, mediaData, mediaControlPanel, this.systemClock, z, this.debugLogger);
                updatePlayerToState(mediaControlPanel, true);
                reorderAllPlayers(mediaSortKey);
            } else {
                mediaPlayer.bindPlayer(mediaData2, str);
                MediaPlayerData.INSTANCE.addMediaPlayer(str, mediaData, mediaPlayer, this.systemClock, z, this.debugLogger);
                if (!isReorderingAllowed()) {
                    if (!this.shouldScrollToActivePlayer) {
                        this.needsReordering = true;
                    }
                }
                reorderAllPlayers(mediaSortKey);
            }
            updatePageIndicator();
            this.mediaCarouselScrollHandler.onPlayersChanged();
            UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaFrame, true);
            if (MediaPlayerData.INSTANCE.players().size() != this.mediaContent.getChildCount()) {
                Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
            }
            if (mediaPlayer != null) {
                z2 = false;
            }
            return z2;
        } finally {
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public final void addSmartspaceMediaRecommendations(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        TransitionLayout recommendations;
        Trace.beginSection("MediaCarouselController#addSmartspaceMediaRecommendations");
        try {
            if (MediaCarouselControllerKt.DEBUG) {
                Log.d("MediaCarouselController", "Updating smartspace target in carousel");
            }
            if (MediaPlayerData.INSTANCE.getMediaPlayer(str) != null) {
                Log.w("MediaCarouselController", "Skip adding smartspace target in carousel");
                return;
            }
            String smartspaceMediaKey = MediaPlayerData.INSTANCE.smartspaceMediaKey();
            if (!(smartspaceMediaKey == null || MediaPlayerData.INSTANCE.removeMediaPlayer(smartspaceMediaKey) == null)) {
                this.debugLogger.logPotentialMemoryLeak(smartspaceMediaKey);
            }
            MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
            RecommendationViewHolder.Companion companion = RecommendationViewHolder.Companion;
            LayoutInflater from = LayoutInflater.from(this.context);
            Intrinsics.checkNotNullExpressionValue(from, "from(context)");
            mediaControlPanel.attachRecommendation(companion.create(from, this.mediaContent));
            mediaControlPanel.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addSmartspaceMediaRecommendations$1$2(this));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            RecommendationViewHolder recommendationViewHolder = mediaControlPanel.getRecommendationViewHolder();
            if (!(recommendationViewHolder == null || (recommendations = recommendationViewHolder.getRecommendations()) == null)) {
                recommendations.setLayoutParams(layoutParams);
            }
            mediaControlPanel.bindRecommendation(smartspaceMediaData);
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newRecs");
            mediaPlayerData.addMediaRecommendation(str, smartspaceMediaData, mediaControlPanel, z, this.systemClock, this.debugLogger);
            updatePlayerToState(mediaControlPanel, true);
            reorderAllPlayers((MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(MediaPlayerData.INSTANCE.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex()));
            updatePageIndicator();
            UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaFrame, true);
            if (MediaPlayerData.INSTANCE.players().size() != this.mediaContent.getChildCount()) {
                Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
            }
            Unit unit = Unit.INSTANCE;
            Trace.endSection();
        } finally {
            Trace.endSection();
        }
    }

    public final Unit onDesiredLocationChanged(int i, MediaHostState mediaHostState, boolean z, long j, long j2) {
        Trace.beginSection("MediaCarouselController#onDesiredLocationChanged");
        Unit unit = null;
        if (mediaHostState != null) {
            try {
                if (this.desiredLocation != i) {
                    this.logger.logCarouselPosition(i);
                }
                this.desiredLocation = i;
                this.desiredHostState = mediaHostState;
                setCurrentlyExpanded(mediaHostState.getExpansion() > 0.0f);
                boolean z2 = !this.currentlyExpanded && !this.mediaManager.hasActiveMediaOrRecommendation() && mediaHostState.getShowsOnlyActiveMedia();
                for (MediaControlPanel next : MediaPlayerData.INSTANCE.players()) {
                    if (z) {
                        next.getMediaViewController().animatePendingStateChange(j, j2);
                    }
                    if (z2 && next.getMediaViewController().isGutsVisible()) {
                        next.closeGuts(!z);
                    }
                    next.getMediaViewController().onLocationPreChange(i);
                }
                this.mediaCarouselScrollHandler.setShowsSettingsButton(!mediaHostState.getShowsOnlyActiveMedia());
                this.mediaCarouselScrollHandler.setFalsingProtectionNeeded(mediaHostState.getFalsingProtectionNeeded());
                boolean visible = mediaHostState.getVisible();
                if (visible != this.playersVisible) {
                    this.playersVisible = visible;
                    if (visible) {
                        MediaCarouselScrollHandler.resetTranslation$default(this.mediaCarouselScrollHandler, false, 1, (Object) null);
                    }
                }
                updateCarouselSize();
                unit = Unit.INSTANCE;
            } catch (Throwable th) {
                Trace.endSection();
                throw th;
            }
        }
        Trace.endSection();
        return unit;
    }
}
