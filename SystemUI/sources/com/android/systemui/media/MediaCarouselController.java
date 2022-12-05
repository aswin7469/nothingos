package com.android.systemui.media;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.media.PlayerViewHolder;
import com.android.systemui.media.RecommendationViewHolder;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.systemui.util.animation.UniqueObjectHostViewKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaCarouselController implements Dumpable {
    @NotNull
    private final ActivityStarter activityStarter;
    private int carouselMeasureHeight;
    private int carouselMeasureWidth;
    @NotNull
    private final MediaCarouselController$configListener$1 configListener;
    @NotNull
    private final Context context;
    private int currentCarouselHeight;
    private int currentCarouselWidth;
    private boolean currentlyShowingOnlyActive;
    @Nullable
    private MediaHostState desiredHostState;
    private boolean isRtl;
    @NotNull
    private final MediaScrollView mediaCarousel;
    @NotNull
    private final MediaCarouselScrollHandler mediaCarouselScrollHandler;
    @NotNull
    private final ViewGroup mediaContent;
    @NotNull
    private final Provider<MediaControlPanel> mediaControlPanelFactory;
    @NotNull
    private final ViewGroup mediaFrame;
    @NotNull
    private final MediaHostStatesManager mediaHostStatesManager;
    @NotNull
    private final MediaDataManager mediaManager;
    private boolean needsReordering;
    @NotNull
    private final PageIndicator pageIndicator;
    private boolean playersVisible;
    private View settingsButton;
    private boolean shouldScrollToActivePlayer;
    @NotNull
    private final SystemClock systemClock;
    public Function0<Unit> updateUserVisibility;
    @NotNull
    private final VisualStabilityManager.Callback visualStabilityCallback;
    @NotNull
    private final VisualStabilityManager visualStabilityManager;
    private int desiredLocation = -1;
    private int currentEndLocation = -1;
    private int currentStartLocation = -1;
    private float currentTransitionProgress = 1.0f;
    @NotNull
    private Set<String> keysNeedRemoval = new LinkedHashSet();
    private int bgColor = getBackgroundColor();
    private boolean currentlyExpanded = true;

    public final void logSmartspaceCardReported(int i, int i2, int i3, boolean z, int i4, int i5, int i6) {
        logSmartspaceCardReported$default(this, i, i2, i3, z, i4, i5, i6, 0, 128, null);
    }

    /* JADX WARN: Type inference failed for: r5v1, types: [com.android.systemui.media.MediaCarouselController$configListener$1, java.lang.Object] */
    public MediaCarouselController(@NotNull Context context, @NotNull Provider<MediaControlPanel> mediaControlPanelFactory, @NotNull VisualStabilityManager visualStabilityManager, @NotNull MediaHostStatesManager mediaHostStatesManager, @NotNull ActivityStarter activityStarter, @NotNull SystemClock systemClock, @NotNull DelayableExecutor executor, @NotNull MediaDataManager mediaManager, @NotNull ConfigurationController configurationController, @NotNull FalsingCollector falsingCollector, @NotNull FalsingManager falsingManager, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mediaControlPanelFactory, "mediaControlPanelFactory");
        Intrinsics.checkNotNullParameter(visualStabilityManager, "visualStabilityManager");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager, "mediaHostStatesManager");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(mediaManager, "mediaManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context;
        this.mediaControlPanelFactory = mediaControlPanelFactory;
        this.visualStabilityManager = visualStabilityManager;
        this.mediaHostStatesManager = mediaHostStatesManager;
        this.activityStarter = activityStarter;
        this.systemClock = systemClock;
        this.mediaManager = mediaManager;
        ?? r5 = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.MediaCarouselController$configListener$1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                MediaCarouselController.this.recreatePlayers();
                MediaCarouselController.this.inflateSettingsButton();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                MediaCarouselController.this.recreatePlayers();
                MediaCarouselController.this.inflateSettingsButton();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                if (configuration == null) {
                    return;
                }
                MediaCarouselController mediaCarouselController = MediaCarouselController.this;
                boolean z = true;
                if (configuration.getLayoutDirection() != 1) {
                    z = false;
                }
                mediaCarouselController.setRtl(z);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                MediaCarouselController.this.recreatePlayers();
                MediaCarouselController.this.inflateSettingsButton();
            }
        };
        this.configListener = r5;
        dumpManager.registerDumpable("MediaCarouselController", this);
        ViewGroup inflateMediaCarousel = inflateMediaCarousel();
        this.mediaFrame = inflateMediaCarousel;
        View requireViewById = inflateMediaCarousel.requireViewById(R$id.media_carousel_scroller);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "mediaFrame.requireViewById(R.id.media_carousel_scroller)");
        MediaScrollView mediaScrollView = (MediaScrollView) requireViewById;
        this.mediaCarousel = mediaScrollView;
        View requireViewById2 = inflateMediaCarousel.requireViewById(R$id.media_page_indicator);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "mediaFrame.requireViewById(R.id.media_page_indicator)");
        PageIndicator pageIndicator = (PageIndicator) requireViewById2;
        this.pageIndicator = pageIndicator;
        this.mediaCarouselScrollHandler = new MediaCarouselScrollHandler(mediaScrollView, pageIndicator, executor, new AnonymousClass1(this), new AnonymousClass2(this), new AnonymousClass3(this), falsingCollector, falsingManager, new AnonymousClass4(this));
        setRtl(context.getResources().getConfiguration().getLayoutDirection() == 1);
        inflateSettingsButton();
        View requireViewById3 = mediaScrollView.requireViewById(R$id.media_carousel);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "mediaCarousel.requireViewById(R.id.media_carousel)");
        this.mediaContent = (ViewGroup) requireViewById3;
        configurationController.addCallback(r5);
        VisualStabilityManager.Callback callback = new VisualStabilityManager.Callback() { // from class: com.android.systemui.media.MediaCarouselController.5
            {
                MediaCarouselController.this = this;
            }

            @Override // com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.Callback
            public final void onChangeAllowed() {
                if (MediaCarouselController.this.needsReordering) {
                    MediaCarouselController.this.needsReordering = false;
                    MediaCarouselController.this.reorderAllPlayers(null);
                }
                Set<String> set = MediaCarouselController.this.keysNeedRemoval;
                MediaCarouselController mediaCarouselController = MediaCarouselController.this;
                for (String str : set) {
                    MediaCarouselController.removePlayer$default(mediaCarouselController, str, false, false, 6, null);
                }
                MediaCarouselController.this.keysNeedRemoval.clear();
                MediaCarouselController mediaCarouselController2 = MediaCarouselController.this;
                if (mediaCarouselController2.updateUserVisibility != null) {
                    mediaCarouselController2.getUpdateUserVisibility().mo1951invoke();
                }
                MediaCarouselController.this.getMediaCarouselScrollHandler().scrollToStart();
            }
        };
        this.visualStabilityCallback = callback;
        visualStabilityManager.addReorderingAllowedCallback(callback, true);
        mediaManager.addListener(new MediaDataManager.Listener() { // from class: com.android.systemui.media.MediaCarouselController.6
            {
                MediaCarouselController.this = this;
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
                Boolean isPlaying;
                MediaCarouselController mediaCarouselController;
                MediaPlayerData mediaPlayerData;
                MediaControlPanel mediaPlayer;
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(data, "data");
                if (MediaCarouselController.this.addOrUpdatePlayer(key, str, data) && (mediaPlayer = (mediaPlayerData = MediaPlayerData.INSTANCE).getMediaPlayer(key)) != null) {
                    MediaCarouselController.logSmartspaceCardReported$default(MediaCarouselController.this, 759, mediaPlayer.mInstanceId, mediaPlayer.mUid, false, mediaPlayer.getSurfaceForSmartspaceLogging(), 0, 0, mediaPlayerData.getMediaPlayerIndex(key), 96, null);
                }
                boolean z3 = false;
                if (z2) {
                    Collection<MediaControlPanel> players = MediaPlayerData.INSTANCE.players();
                    MediaCarouselController mediaCarouselController2 = MediaCarouselController.this;
                    int i = 0;
                    for (Object obj : players) {
                        int i2 = i + 1;
                        if (i < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        MediaControlPanel mediaControlPanel = (MediaControlPanel) obj;
                        if (mediaControlPanel.getRecommendationViewHolder() == null) {
                            int hash = SmallHash.hash(mediaControlPanel.mUid + ((int) mediaCarouselController2.systemClock.currentTimeMillis()));
                            mediaControlPanel.mInstanceId = hash;
                            mediaCarouselController = mediaCarouselController2;
                            MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController2, 759, hash, mediaControlPanel.mUid, false, mediaControlPanel.getSurfaceForSmartspaceLogging(), 0, 0, i, 96, null);
                        } else {
                            mediaCarouselController = mediaCarouselController2;
                        }
                        i = i2;
                        mediaCarouselController2 = mediaCarouselController;
                    }
                }
                if (MediaCarouselController.this.getMediaCarouselScrollHandler().getVisibleToUser() && z2 && !MediaCarouselController.this.getMediaCarouselScrollHandler().getQsExpanded()) {
                    MediaCarouselController mediaCarouselController3 = MediaCarouselController.this;
                    mediaCarouselController3.logSmartspaceImpression(mediaCarouselController3.getMediaCarouselScrollHandler().getQsExpanded());
                }
                Boolean valueOf = data.isPlaying() == null ? null : Boolean.valueOf(!isPlaying.booleanValue());
                if ((valueOf == null ? data.isClearable() : valueOf.booleanValue()) && !data.getActive()) {
                    z3 = true;
                }
                if (!z3 || Utils.useMediaResumption(MediaCarouselController.this.context)) {
                    MediaCarouselController.this.keysNeedRemoval.remove(key);
                } else if (!MediaCarouselController.this.visualStabilityManager.isReorderingAllowed()) {
                    MediaCarouselController.this.keysNeedRemoval.add(key);
                } else {
                    onMediaDataRemoved(key);
                }
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataLoaded(@NotNull String key, @NotNull SmartspaceMediaData data, boolean z) {
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(data, "data");
                if (data.isActive()) {
                    MediaCarouselController.this.addSmartspaceMediaRecommendations(key, data, z);
                    MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                    MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(key);
                    if (mediaPlayer == null) {
                        return;
                    }
                    MediaCarouselController mediaCarouselController = MediaCarouselController.this;
                    MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController, 759, mediaPlayer.mInstanceId, mediaPlayer.mUid, true, mediaPlayer.getSurfaceForSmartspaceLogging(), 0, 0, mediaPlayerData.getMediaPlayerIndex(key), 96, null);
                    if (!mediaCarouselController.getMediaCarouselScrollHandler().getVisibleToUser() || mediaCarouselController.getMediaCarouselScrollHandler().getVisibleMediaIndex() != mediaPlayerData.getMediaPlayerIndex(key)) {
                        return;
                    }
                    MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController, 800, mediaPlayer.mInstanceId, mediaPlayer.mUid, true, mediaPlayer.getSurfaceForSmartspaceLogging(), 0, 0, 0, 224, null);
                    return;
                }
                onSmartspaceMediaDataRemoved(data.getTargetId(), true);
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataRemoved(@NotNull String key) {
                Intrinsics.checkNotNullParameter(key, "key");
                MediaCarouselController.removePlayer$default(MediaCarouselController.this, key, false, false, 6, null);
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataRemoved(@NotNull String key, boolean z) {
                Intrinsics.checkNotNullParameter(key, "key");
                if (!z && !MediaCarouselController.this.visualStabilityManager.isReorderingAllowed()) {
                    MediaCarouselController.this.keysNeedRemoval.add(key);
                } else {
                    onMediaDataRemoved(key);
                }
            }
        });
        inflateMediaCarousel.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.media.MediaCarouselController.7
            {
                MediaCarouselController.this = this;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                MediaCarouselController.this.updatePageIndicatorLocation();
            }
        });
        mediaHostStatesManager.addCallback(new MediaHostStatesManager.Callback() { // from class: com.android.systemui.media.MediaCarouselController.8
            {
                MediaCarouselController.this = this;
            }

            @Override // com.android.systemui.media.MediaHostStatesManager.Callback
            public void onHostStateChanged(int i, @NotNull MediaHostState mediaHostState) {
                Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
                if (i == MediaCarouselController.this.desiredLocation) {
                    MediaCarouselController mediaCarouselController = MediaCarouselController.this;
                    MediaCarouselController.onDesiredLocationChanged$default(mediaCarouselController, mediaCarouselController.desiredLocation, mediaHostState, false, 0L, 0L, 24, null);
                }
            }
        });
    }

    @NotNull
    public final MediaCarouselScrollHandler getMediaCarouselScrollHandler() {
        return this.mediaCarouselScrollHandler;
    }

    @NotNull
    public final ViewGroup getMediaFrame() {
        return this.mediaFrame;
    }

    public final void setShouldScrollToActivePlayer(boolean z) {
        this.shouldScrollToActivePlayer = z;
    }

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
            for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
                mediaControlPanel.setListening(this.currentlyExpanded);
            }
        }
    }

    @NotNull
    public final Function0<Unit> getUpdateUserVisibility() {
        Function0<Unit> function0 = this.updateUserVisibility;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("updateUserVisibility");
        throw null;
    }

    public final void setUpdateUserVisibility(@NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.updateUserVisibility = function0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$1 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Unit> {
        AnonymousClass1(MediaCarouselController mediaCarouselController) {
            super(0, mediaCarouselController, MediaCarouselController.class, "onSwipeToDismiss", "onSwipeToDismiss()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1951invoke() {
            mo1951invoke();
            return Unit.INSTANCE;
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public final void mo1951invoke() {
            ((MediaCarouselController) this.receiver).onSwipeToDismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$2 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function0<Unit> {
        AnonymousClass2(MediaCarouselController mediaCarouselController) {
            super(0, mediaCarouselController, MediaCarouselController.class, "updatePageIndicatorLocation", "updatePageIndicatorLocation()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1951invoke() {
            mo1951invoke();
            return Unit.INSTANCE;
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public final void mo1951invoke() {
            ((MediaCarouselController) this.receiver).updatePageIndicatorLocation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$3 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass3 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
        AnonymousClass3(MediaCarouselController mediaCarouselController) {
            super(1, mediaCarouselController, MediaCarouselController.class, "closeGuts", "closeGuts(Z)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1949invoke(Boolean bool) {
            invoke(bool.booleanValue());
            return Unit.INSTANCE;
        }

        public final void invoke(boolean z) {
            ((MediaCarouselController) this.receiver).closeGuts(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MediaCarouselController.kt */
    /* renamed from: com.android.systemui.media.MediaCarouselController$4 */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass4 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
        AnonymousClass4(MediaCarouselController mediaCarouselController) {
            super(1, mediaCarouselController, MediaCarouselController.class, "logSmartspaceImpression", "logSmartspaceImpression(Z)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1949invoke(Boolean bool) {
            invoke(bool.booleanValue());
            return Unit.INSTANCE;
        }

        public final void invoke(boolean z) {
            ((MediaCarouselController) this.receiver).logSmartspaceImpression(z);
        }
    }

    public final void inflateSettingsButton() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel_settings_button, this.mediaFrame, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.View");
        View view = this.settingsButton;
        if (view != null) {
            ViewGroup viewGroup = this.mediaFrame;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                throw null;
            }
            viewGroup.removeView(view);
        }
        this.settingsButton = inflate;
        this.mediaFrame.addView(inflate);
        this.mediaCarouselScrollHandler.onSettingsButtonUpdated(inflate);
        View view2 = this.settingsButton;
        if (view2 != null) {
            view2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaCarouselController$inflateSettingsButton$2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ActivityStarter activityStarter;
                    Intent intent;
                    activityStarter = MediaCarouselController.this.activityStarter;
                    intent = MediaCarouselControllerKt.settingsIntent;
                    activityStarter.startActivity(intent, true);
                }
            });
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
    }

    private final ViewGroup inflateMediaCarousel() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel, (ViewGroup) new UniqueObjectHostView(this.context), false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        viewGroup.setLayoutDirection(3);
        return viewGroup;
    }

    public final void reorderAllPlayers(MediaPlayerData.MediaSortKey mediaSortKey) {
        Unit unit;
        RecommendationViewHolder recommendationViewHolder;
        this.mediaContent.removeAllViews();
        Iterator<MediaControlPanel> it = MediaPlayerData.INSTANCE.players().iterator();
        while (true) {
            unit = null;
            if (!it.hasNext()) {
                break;
            }
            MediaControlPanel next = it.next();
            PlayerViewHolder playerViewHolder = next.getPlayerViewHolder();
            if (playerViewHolder != null) {
                this.mediaContent.addView(playerViewHolder.getPlayer());
                unit = Unit.INSTANCE;
            }
            if (unit == null && (recommendationViewHolder = next.getRecommendationViewHolder()) != null) {
                this.mediaContent.addView(recommendationViewHolder.getRecommendations());
            }
        }
        this.mediaCarouselScrollHandler.onPlayersChanged();
        if (this.shouldScrollToActivePlayer) {
            int i = 0;
            this.shouldScrollToActivePlayer = false;
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            int firstActiveMediaIndex = mediaPlayerData.firstActiveMediaIndex();
            int i2 = -1;
            if (firstActiveMediaIndex == -1) {
                return;
            }
            if (mediaSortKey != null) {
                Iterator<T> it2 = mediaPlayerData.playerKeys().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object next2 = it2.next();
                    if (i < 0) {
                        CollectionsKt__CollectionsKt.throwIndexOverflow();
                    }
                    if (Intrinsics.areEqual(mediaSortKey, (MediaPlayerData.MediaSortKey) next2)) {
                        i2 = i;
                        break;
                    }
                    i++;
                }
                getMediaCarouselScrollHandler().scrollToPlayer(i2, firstActiveMediaIndex);
                unit = Unit.INSTANCE;
            }
            if (unit != null) {
                return;
            }
            new MediaCarouselController$reorderAllPlayers$4(this, firstActiveMediaIndex);
        }
    }

    public final boolean addOrUpdatePlayer(String str, String str2, MediaData mediaData) {
        MediaData copy;
        TransitionLayout player;
        copy = mediaData.copy((i3 & 1) != 0 ? mediaData.userId : 0, (i3 & 2) != 0 ? mediaData.initialized : false, (i3 & 4) != 0 ? mediaData.backgroundColor : this.bgColor, (i3 & 8) != 0 ? mediaData.app : null, (i3 & 16) != 0 ? mediaData.appIcon : null, (i3 & 32) != 0 ? mediaData.artist : null, (i3 & 64) != 0 ? mediaData.song : null, (i3 & 128) != 0 ? mediaData.artwork : null, (i3 & 256) != 0 ? mediaData.actions : null, (i3 & 512) != 0 ? mediaData.actionsToShowInCompact : null, (i3 & 1024) != 0 ? mediaData.packageName : null, (i3 & 2048) != 0 ? mediaData.token : null, (i3 & 4096) != 0 ? mediaData.clickIntent : null, (i3 & 8192) != 0 ? mediaData.device : null, (i3 & 16384) != 0 ? mediaData.active : false, (i3 & 32768) != 0 ? mediaData.resumeAction : null, (i3 & 65536) != 0 ? mediaData.isLocalSession : false, (i3 & 131072) != 0 ? mediaData.resumption : false, (i3 & 262144) != 0 ? mediaData.notificationKey : null, (i3 & 524288) != 0 ? mediaData.hasCheckedForResume : false, (i3 & 1048576) != 0 ? mediaData.isPlaying : null, (i3 & 2097152) != 0 ? mediaData.isClearable : false, (i3 & 4194304) != 0 ? mediaData.lastActive : 0L);
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        mediaPlayerData.moveIfExists(str2, str);
        MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str);
        MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex());
        if (mediaPlayer == null) {
            MediaControlPanel newPlayer = this.mediaControlPanelFactory.mo1933get();
            PlayerViewHolder.Companion companion = PlayerViewHolder.Companion;
            LayoutInflater from = LayoutInflater.from(this.context);
            Intrinsics.checkNotNullExpressionValue(from, "from(context)");
            newPlayer.attachPlayer(companion.create(from, this.mediaContent));
            newPlayer.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addOrUpdatePlayer$1(this));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            PlayerViewHolder playerViewHolder = newPlayer.getPlayerViewHolder();
            if (playerViewHolder != null && (player = playerViewHolder.getPlayer()) != null) {
                player.setLayoutParams(layoutParams);
            }
            newPlayer.bindPlayer(copy, str);
            newPlayer.setListening(this.currentlyExpanded);
            Intrinsics.checkNotNullExpressionValue(newPlayer, "newPlayer");
            mediaPlayerData.addMediaPlayer(str, copy, newPlayer, this.systemClock);
            Intrinsics.checkNotNullExpressionValue(newPlayer, "newPlayer");
            updatePlayerToState(newPlayer, true);
            reorderAllPlayers(mediaSortKey);
        } else {
            mediaPlayer.bindPlayer(copy, str);
            mediaPlayerData.addMediaPlayer(str, copy, mediaPlayer, this.systemClock);
            if (this.visualStabilityManager.isReorderingAllowed() || this.shouldScrollToActivePlayer) {
                reorderAllPlayers(mediaSortKey);
            } else {
                this.needsReordering = true;
            }
        }
        updatePageIndicator();
        this.mediaCarouselScrollHandler.onPlayersChanged();
        UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaCarousel, true);
        if (mediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
            Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
        }
        return mediaPlayer == null;
    }

    public final void addSmartspaceMediaRecommendations(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        SmartspaceMediaData copy;
        TransitionLayout recommendations;
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.getMediaPlayer(str) != null) {
            Log.w("MediaCarouselController", "Skip adding smartspace target in carousel");
            return;
        }
        String smartspaceMediaKey = mediaPlayerData.smartspaceMediaKey();
        if (smartspaceMediaKey != null) {
            mediaPlayerData.removeMediaPlayer(smartspaceMediaKey);
        }
        MediaControlPanel newRecs = this.mediaControlPanelFactory.mo1933get();
        RecommendationViewHolder.Companion companion = RecommendationViewHolder.Companion;
        LayoutInflater from = LayoutInflater.from(this.context);
        Intrinsics.checkNotNullExpressionValue(from, "from(context)");
        newRecs.attachRecommendation(companion.create(from, this.mediaContent));
        newRecs.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addSmartspaceMediaRecommendations$2(this));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        RecommendationViewHolder recommendationViewHolder = newRecs.getRecommendationViewHolder();
        if (recommendationViewHolder != null && (recommendations = recommendationViewHolder.getRecommendations()) != null) {
            recommendations.setLayoutParams(layoutParams);
        }
        copy = smartspaceMediaData.copy((r18 & 1) != 0 ? smartspaceMediaData.targetId : null, (r18 & 2) != 0 ? smartspaceMediaData.isActive : false, (r18 & 4) != 0 ? smartspaceMediaData.isValid : false, (r18 & 8) != 0 ? smartspaceMediaData.packageName : null, (r18 & 16) != 0 ? smartspaceMediaData.cardAction : null, (r18 & 32) != 0 ? smartspaceMediaData.recommendations : null, (r18 & 64) != 0 ? smartspaceMediaData.dismissIntent : null, (r18 & 128) != 0 ? smartspaceMediaData.backgroundColor : this.bgColor);
        newRecs.bindRecommendation(copy);
        Intrinsics.checkNotNullExpressionValue(newRecs, "newRecs");
        mediaPlayerData.addMediaRecommendation(str, smartspaceMediaData, newRecs, z, this.systemClock);
        Intrinsics.checkNotNullExpressionValue(newRecs, "newRecs");
        updatePlayerToState(newRecs, true);
        reorderAllPlayers((MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex()));
        updatePageIndicator();
        UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaCarousel, true);
        if (mediaPlayerData.players().size() == this.mediaContent.getChildCount()) {
            return;
        }
        Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
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

    public final void removePlayer(@NotNull String key, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaControlPanel removeMediaPlayer = MediaPlayerData.INSTANCE.removeMediaPlayer(key);
        if (removeMediaPlayer == null) {
            return;
        }
        getMediaCarouselScrollHandler().onPrePlayerRemoved(removeMediaPlayer);
        ViewGroup viewGroup = this.mediaContent;
        PlayerViewHolder playerViewHolder = removeMediaPlayer.getPlayerViewHolder();
        TransitionLayout transitionLayout = null;
        viewGroup.removeView(playerViewHolder == null ? null : playerViewHolder.getPlayer());
        ViewGroup viewGroup2 = this.mediaContent;
        RecommendationViewHolder recommendationViewHolder = removeMediaPlayer.getRecommendationViewHolder();
        if (recommendationViewHolder != null) {
            transitionLayout = recommendationViewHolder.getRecommendations();
        }
        viewGroup2.removeView(transitionLayout);
        removeMediaPlayer.onDestroy();
        getMediaCarouselScrollHandler().onPlayersChanged();
        updatePageIndicator();
        if (z) {
            this.mediaManager.dismissMediaData(key, 0L);
        }
        if (!z2) {
            return;
        }
        this.mediaManager.dismissSmartspaceRecommendation(key, 0L);
    }

    public final void recreatePlayers() {
        this.bgColor = getBackgroundColor();
        this.pageIndicator.setTintList(ColorStateList.valueOf(getForegroundColor()));
        Iterator<T> it = MediaPlayerData.INSTANCE.mediaData().iterator();
        while (it.hasNext()) {
            Triple triple = (Triple) it.next();
            String str = (String) triple.component1();
            MediaData mediaData = (MediaData) triple.component2();
            if (((Boolean) triple.component3()).booleanValue()) {
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                SmartspaceMediaData smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                removePlayer(str, false, false);
                if (smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core != null) {
                    addSmartspaceMediaRecommendations(smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getTargetId(), smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core, mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
                }
            } else {
                removePlayer(str, false, false);
                addOrUpdatePlayer(str, null, mediaData);
            }
        }
    }

    private final int getBackgroundColor() {
        return this.context.getColor(17170502);
    }

    private final int getForegroundColor() {
        return this.context.getColor(17170511);
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
        for (MediaControlPanel mediaPlayer : MediaPlayerData.INSTANCE.players()) {
            Intrinsics.checkNotNullExpressionValue(mediaPlayer, "mediaPlayer");
            updatePlayerToState(mediaPlayer, z);
        }
        maybeResetSettingsCog();
        updatePageIndicatorAlpha();
    }

    private final void updatePageIndicatorAlpha() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean z = false;
        boolean visible = mediaHostState == null ? false : mediaHostState.getVisible();
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

    public final void updatePageIndicatorLocation() {
        int i;
        int width;
        if (this.isRtl) {
            i = this.pageIndicator.getWidth();
            width = this.currentCarouselWidth;
        } else {
            i = this.currentCarouselWidth;
            width = this.pageIndicator.getWidth();
        }
        this.pageIndicator.setTranslationX(((i - width) / 2.0f) + this.mediaCarouselScrollHandler.getContentTranslation());
        ViewGroup.LayoutParams layoutParams = this.pageIndicator.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        PageIndicator pageIndicator = this.pageIndicator;
        pageIndicator.setTranslationY((this.currentCarouselHeight - pageIndicator.getHeight()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
    }

    public final void updateCarouselDimensions() {
        int i = 0;
        int i2 = 0;
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            MediaViewController mediaViewController = mediaControlPanel.getMediaViewController();
            Intrinsics.checkNotNullExpressionValue(mediaViewController, "mediaPlayer.mediaViewController");
            i = Math.max(i, mediaViewController.getCurrentWidth() + ((int) mediaViewController.getTranslationX()));
            i2 = Math.max(i2, mediaViewController.getCurrentHeight() + ((int) mediaViewController.getTranslationY()));
        }
        if (i == this.currentCarouselWidth && i2 == this.currentCarouselHeight) {
            return;
        }
        this.currentCarouselWidth = i;
        this.currentCarouselHeight = i2;
        this.mediaCarouselScrollHandler.setCarouselBounds(i, i2);
        updatePageIndicatorLocation();
    }

    private final void maybeResetSettingsCog() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean showsOnlyActiveMedia = mediaHostState == null ? true : mediaHostState.getShowsOnlyActiveMedia();
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        boolean showsOnlyActiveMedia2 = mediaHostState2 == null ? showsOnlyActiveMedia : mediaHostState2.getShowsOnlyActiveMedia();
        if (this.currentlyShowingOnlyActive == showsOnlyActiveMedia) {
            float f = this.currentTransitionProgress;
            boolean z = false;
            if (f == 1.0f) {
                return;
            }
            if (f == 0.0f) {
                z = true;
            }
            if (z || showsOnlyActiveMedia2 == showsOnlyActiveMedia) {
                return;
            }
        }
        this.currentlyShowingOnlyActive = showsOnlyActiveMedia;
        this.mediaCarouselScrollHandler.resetTranslation(true);
    }

    private final void updatePlayerToState(MediaControlPanel mediaControlPanel, boolean z) {
        mediaControlPanel.getMediaViewController().setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
    }

    public static /* synthetic */ void onDesiredLocationChanged$default(MediaCarouselController mediaCarouselController, int i, MediaHostState mediaHostState, boolean z, long j, long j2, int i2, Object obj) {
        mediaCarouselController.onDesiredLocationChanged(i, mediaHostState, z, (i2 & 8) != 0 ? 200L : j, (i2 & 16) != 0 ? 0L : j2);
    }

    public final void onDesiredLocationChanged(int i, @Nullable MediaHostState mediaHostState, boolean z, long j, long j2) {
        if (mediaHostState == null) {
            return;
        }
        this.desiredLocation = i;
        this.desiredHostState = mediaHostState;
        setCurrentlyExpanded(mediaHostState.getExpansion() > 0.0f);
        boolean z2 = !this.currentlyExpanded && !this.mediaManager.hasActiveMedia() && mediaHostState.getShowsOnlyActiveMedia();
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            if (z) {
                mediaControlPanel.getMediaViewController().animatePendingStateChange(j, j2);
            }
            if (z2 && mediaControlPanel.getMediaViewController().isGutsVisible()) {
                mediaControlPanel.closeGuts(!z);
            }
            mediaControlPanel.getMediaViewController().onLocationPreChange(i);
        }
        getMediaCarouselScrollHandler().setShowsSettingsButton(!mediaHostState.getShowsOnlyActiveMedia());
        getMediaCarouselScrollHandler().setFalsingProtectionNeeded(mediaHostState.getFalsingProtectionNeeded());
        boolean visible = mediaHostState.getVisible();
        if (visible != this.playersVisible) {
            this.playersVisible = visible;
            if (visible) {
                MediaCarouselScrollHandler.resetTranslation$default(getMediaCarouselScrollHandler(), false, 1, null);
            }
        }
        updateCarouselSize();
    }

    public static /* synthetic */ void closeGuts$default(MediaCarouselController mediaCarouselController, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        mediaCarouselController.closeGuts(z);
    }

    public final void closeGuts(boolean z) {
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            mediaControlPanel.closeGuts(z);
        }
    }

    private final void updateCarouselSize() {
        MediaHostState mediaHostState = this.desiredHostState;
        MeasurementInput measurementInput = null;
        MeasurementInput measurementInput2 = mediaHostState == null ? null : mediaHostState.getMeasurementInput();
        int width = measurementInput2 == null ? 0 : measurementInput2.getWidth();
        MediaHostState mediaHostState2 = this.desiredHostState;
        MeasurementInput measurementInput3 = mediaHostState2 == null ? null : mediaHostState2.getMeasurementInput();
        int height = measurementInput3 == null ? 0 : measurementInput3.getHeight();
        if ((width == this.carouselMeasureWidth || width == 0) && (height == this.carouselMeasureHeight || height == 0)) {
            return;
        }
        this.carouselMeasureWidth = width;
        this.carouselMeasureHeight = height;
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R$dimen.qs_media_padding) + width;
        MediaHostState mediaHostState3 = this.desiredHostState;
        MeasurementInput measurementInput4 = mediaHostState3 == null ? null : mediaHostState3.getMeasurementInput();
        int widthMeasureSpec = measurementInput4 == null ? 0 : measurementInput4.getWidthMeasureSpec();
        MediaHostState mediaHostState4 = this.desiredHostState;
        if (mediaHostState4 != null) {
            measurementInput = mediaHostState4.getMeasurementInput();
        }
        this.mediaCarousel.measure(widthMeasureSpec, measurementInput == null ? 0 : measurementInput.getHeightMeasureSpec());
        MediaScrollView mediaScrollView = this.mediaCarousel;
        mediaScrollView.layout(0, 0, width, mediaScrollView.getMeasuredHeight());
        this.mediaCarouselScrollHandler.setPlayerWidthPlusPadding(dimensionPixelSize);
    }

    public final void logSmartspaceImpression(boolean z) {
        int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.players().size() > visibleMediaIndex) {
            Object elementAt = CollectionsKt.elementAt(mediaPlayerData.players(), visibleMediaIndex);
            Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.players().elementAt(visibleMediaIndex)");
            MediaControlPanel mediaControlPanel = (MediaControlPanel) elementAt;
            boolean hasActiveMediaOrRecommendationCard = mediaPlayerData.hasActiveMediaOrRecommendationCard();
            boolean z2 = mediaControlPanel.getRecommendationViewHolder() != null;
            if (!hasActiveMediaOrRecommendationCard && !z) {
                return;
            }
            logSmartspaceCardReported$default(this, 800, mediaControlPanel.mInstanceId, mediaControlPanel.mUid, z2, mediaControlPanel.getSurfaceForSmartspaceLogging(), 0, 0, 0, 224, null);
        }
    }

    public static /* synthetic */ void logSmartspaceCardReported$default(MediaCarouselController mediaCarouselController, int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7, int i8, Object obj) {
        mediaCarouselController.logSmartspaceCardReported(i, i2, i3, z, i4, (i8 & 32) != 0 ? 0 : i5, (i8 & 64) != 0 ? 0 : i6, (i8 & 128) != 0 ? mediaCarouselController.mediaCarouselScrollHandler.getVisibleMediaIndex() : i7);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7) {
        if (z || this.mediaManager.getSmartspaceMediaData().isActive() || MediaPlayerData.INSTANCE.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() != null) {
            SysUiStatsLog.write(352, i, i2, 0, i4, i7, this.mediaContent.getChildCount(), z ? 15 : 31, i3, i5, i6, 0);
        }
    }

    public final void onSwipeToDismiss() {
        Collection<MediaControlPanel> players = MediaPlayerData.INSTANCE.players();
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = players.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (((MediaControlPanel) next).getRecommendationViewHolder() != null) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            logSmartspaceCardReported$default(this, 761, ((MediaControlPanel) arrayList.get(0)).mInstanceId, ((MediaControlPanel) arrayList.get(0)).mUid, true, ((MediaControlPanel) arrayList.get(0)).getSurfaceForSmartspaceLogging(), 0, 0, -1, 96, null);
        } else {
            int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            if (mediaPlayerData.players().size() > visibleMediaIndex) {
                Object elementAt = CollectionsKt.elementAt(mediaPlayerData.players(), visibleMediaIndex);
                Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.players().elementAt(visibleMediaIndex)");
                MediaControlPanel mediaControlPanel = (MediaControlPanel) elementAt;
                logSmartspaceCardReported$default(this, 761, mediaControlPanel.mInstanceId, mediaControlPanel.mUid, false, mediaControlPanel.getSurfaceForSmartspaceLogging(), 0, 0, -1, 96, null);
            }
        }
        this.mediaManager.onSwipeToDismiss();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("keysNeedRemoval: ", this.keysNeedRemoval));
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        pw.println(Intrinsics.stringPlus("playerKeys: ", mediaPlayerData.playerKeys()));
        pw.println(Intrinsics.stringPlus("smartspaceMediaData: ", mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        pw.println(Intrinsics.stringPlus("shouldPrioritizeSs: ", Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
    }
}
