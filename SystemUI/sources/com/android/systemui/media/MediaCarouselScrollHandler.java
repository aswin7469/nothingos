package com.android.systemui.media;

import android.content.res.Resources;
import android.graphics.Outline;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaCarouselScrollHandler.kt */
/* loaded from: classes.dex */
public final class MediaCarouselScrollHandler {
    private int carouselHeight;
    private int carouselWidth;
    @NotNull
    private final Function1<Boolean, Unit> closeGuts;
    private float contentTranslation;
    private int cornerRadius;
    @NotNull
    private final Function0<Unit> dismissCallback;
    @NotNull
    private final FalsingCollector falsingCollector;
    @NotNull
    private final FalsingManager falsingManager;
    private boolean falsingProtectionNeeded;
    @NotNull
    private final GestureDetectorCompat gestureDetector;
    @NotNull
    private final MediaCarouselScrollHandler$gestureListener$1 gestureListener;
    @NotNull
    private final Function1<Boolean, Unit> logSmartspaceImpression;
    @NotNull
    private final DelayableExecutor mainExecutor;
    @NotNull
    private ViewGroup mediaContent;
    @NotNull
    private final PageIndicator pageIndicator;
    private int playerWidthPlusPadding;
    private boolean qsExpanded;
    @NotNull
    private final MediaCarouselScrollHandler$scrollChangedListener$1 scrollChangedListener;
    private int scrollIntoCurrentMedia;
    @NotNull
    private final MediaScrollView scrollView;
    private View settingsButton;
    private boolean showsSettingsButton;
    @NotNull
    private final MediaCarouselScrollHandler$touchListener$1 touchListener;
    @NotNull
    private Function0<Unit> translationChangedListener;
    private int visibleMediaIndex;
    private boolean visibleToUser;
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 CONTENT_TRANSLATION = new FloatPropertyCompat<MediaCarouselScrollHandler>() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@NotNull MediaCarouselScrollHandler handler) {
            Intrinsics.checkNotNullParameter(handler, "handler");
            return handler.getContentTranslation();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@NotNull MediaCarouselScrollHandler handler, float f) {
            Intrinsics.checkNotNullParameter(handler, "handler");
            handler.setContentTranslation(f);
        }
    };

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [android.view.GestureDetector$OnGestureListener, com.android.systemui.media.MediaCarouselScrollHandler$gestureListener$1] */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.Gefingerpoken, com.android.systemui.media.MediaCarouselScrollHandler$touchListener$1] */
    /* JADX WARN: Type inference failed for: r5v1, types: [com.android.systemui.media.MediaCarouselScrollHandler$scrollChangedListener$1, android.view.View$OnScrollChangeListener] */
    public MediaCarouselScrollHandler(@NotNull MediaScrollView scrollView, @NotNull PageIndicator pageIndicator, @NotNull DelayableExecutor mainExecutor, @NotNull Function0<Unit> dismissCallback, @NotNull Function0<Unit> translationChangedListener, @NotNull Function1<? super Boolean, Unit> closeGuts, @NotNull FalsingCollector falsingCollector, @NotNull FalsingManager falsingManager, @NotNull Function1<? super Boolean, Unit> logSmartspaceImpression) {
        Intrinsics.checkNotNullParameter(scrollView, "scrollView");
        Intrinsics.checkNotNullParameter(pageIndicator, "pageIndicator");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(dismissCallback, "dismissCallback");
        Intrinsics.checkNotNullParameter(translationChangedListener, "translationChangedListener");
        Intrinsics.checkNotNullParameter(closeGuts, "closeGuts");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(logSmartspaceImpression, "logSmartspaceImpression");
        this.scrollView = scrollView;
        this.pageIndicator = pageIndicator;
        this.mainExecutor = mainExecutor;
        this.dismissCallback = dismissCallback;
        this.translationChangedListener = translationChangedListener;
        this.closeGuts = closeGuts;
        this.falsingCollector = falsingCollector;
        this.falsingManager = falsingManager;
        this.logSmartspaceImpression = logSmartspaceImpression;
        ?? r3 = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$gestureListener$1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(@Nullable MotionEvent motionEvent, @Nullable MotionEvent motionEvent2, float f, float f2) {
                boolean onFling;
                onFling = MediaCarouselScrollHandler.this.onFling(f, f2);
                return onFling;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(@Nullable MotionEvent motionEvent, @Nullable MotionEvent motionEvent2, float f, float f2) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                Intrinsics.checkNotNull(motionEvent2);
                return mediaCarouselScrollHandler.onScroll(motionEvent, motionEvent2, f);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(@Nullable MotionEvent motionEvent) {
                FalsingCollector falsingCollector2;
                if (MediaCarouselScrollHandler.this.getFalsingProtectionNeeded()) {
                    falsingCollector2 = MediaCarouselScrollHandler.this.falsingCollector;
                    falsingCollector2.onNotificationStartDismissing();
                    return false;
                }
                return false;
            }
        };
        this.gestureListener = r3;
        ?? r4 = new Gefingerpoken() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$touchListener$1
            @Override // com.android.systemui.Gefingerpoken
            public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
                boolean onTouch;
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                onTouch = mediaCarouselScrollHandler.onTouch(motionEvent);
                return onTouch;
            }

            @Override // com.android.systemui.Gefingerpoken
            public boolean onInterceptTouchEvent(@Nullable MotionEvent motionEvent) {
                boolean onInterceptTouch;
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                onInterceptTouch = mediaCarouselScrollHandler.onInterceptTouch(motionEvent);
                return onInterceptTouch;
            }
        };
        this.touchListener = r4;
        ?? r5 = new View.OnScrollChangeListener() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollChangedListener$1
            @Override // android.view.View.OnScrollChangeListener
            public void onScrollChange(@Nullable View view, int i, int i2, int i3, int i4) {
                MediaScrollView mediaScrollView;
                if (MediaCarouselScrollHandler.this.getPlayerWidthPlusPadding() == 0) {
                    return;
                }
                mediaScrollView = MediaCarouselScrollHandler.this.scrollView;
                int relativeScrollX = mediaScrollView.getRelativeScrollX();
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                mediaCarouselScrollHandler.onMediaScrollingChanged(relativeScrollX / mediaCarouselScrollHandler.getPlayerWidthPlusPadding(), relativeScrollX % MediaCarouselScrollHandler.this.getPlayerWidthPlusPadding());
            }
        };
        this.scrollChangedListener = r5;
        this.gestureDetector = new GestureDetectorCompat(scrollView.getContext(), r3);
        scrollView.setTouchListener(r4);
        scrollView.setOverScrollMode(2);
        this.mediaContent = scrollView.getContentContainer();
        scrollView.setOnScrollChangeListener(r5);
        scrollView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.systemui.media.MediaCarouselScrollHandler.1
            {
                MediaCarouselScrollHandler.this = this;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(@Nullable View view, @Nullable Outline outline) {
                if (outline == null) {
                    return;
                }
                outline.setRoundRect(0, 0, MediaCarouselScrollHandler.this.carouselWidth, MediaCarouselScrollHandler.this.carouselHeight, MediaCarouselScrollHandler.this.cornerRadius);
            }
        });
    }

    public final boolean isRtl() {
        return this.scrollView.isLayoutRtl();
    }

    public final boolean getFalsingProtectionNeeded() {
        return this.falsingProtectionNeeded;
    }

    public final void setFalsingProtectionNeeded(boolean z) {
        this.falsingProtectionNeeded = z;
    }

    public final int getVisibleMediaIndex() {
        return this.visibleMediaIndex;
    }

    public final float getContentTranslation() {
        return this.contentTranslation;
    }

    public final void setContentTranslation(float f) {
        this.contentTranslation = f;
        this.mediaContent.setTranslationX(f);
        updateSettingsPresentation();
        this.translationChangedListener.mo1951invoke();
        updateClipToOutline();
    }

    public final int getPlayerWidthPlusPadding() {
        return this.playerWidthPlusPadding;
    }

    public final void setPlayerWidthPlusPadding(int i) {
        this.playerWidthPlusPadding = i;
        int i2 = this.visibleMediaIndex * i;
        int i3 = this.scrollIntoCurrentMedia;
        this.scrollView.setRelativeScrollX(i3 > i ? i2 + (i - (i3 - i)) : i2 + i3);
    }

    public final void setShowsSettingsButton(boolean z) {
        this.showsSettingsButton = z;
    }

    public final boolean getVisibleToUser() {
        return this.visibleToUser;
    }

    public final void setVisibleToUser(boolean z) {
        this.visibleToUser = z;
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final void onSettingsButtonUpdated(@NotNull View button) {
        Intrinsics.checkNotNullParameter(button, "button");
        this.settingsButton = button;
        if (button != null) {
            Resources resources = button.getResources();
            View view = this.settingsButton;
            if (view != null) {
                this.cornerRadius = resources.getDimensionPixelSize(Utils.getThemeAttr(view.getContext(), 16844145));
                updateSettingsPresentation();
                this.scrollView.invalidateOutline();
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        throw null;
    }

    private final void updateSettingsPresentation() {
        View view;
        View view2;
        View view3;
        View view4;
        int i = 4;
        if (this.showsSettingsButton) {
            View view5 = this.settingsButton;
            if (view5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                throw null;
            } else if (view5.getWidth() > 0) {
                float map = MathUtils.map(0.0f, getMaxTranslation(), 0.0f, 1.0f, Math.abs(this.contentTranslation));
                float f = 1.0f - map;
                if (this.settingsButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                float f2 = (-view.getWidth()) * f * 0.3f;
                if (isRtl()) {
                    if (this.contentTranslation > 0.0f) {
                        float width = this.scrollView.getWidth() - f2;
                        if (this.settingsButton == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                            throw null;
                        }
                        f2 = -(width - view4.getWidth());
                    } else {
                        f2 = -f2;
                    }
                } else if (this.contentTranslation <= 0.0f) {
                    float width2 = this.scrollView.getWidth() - f2;
                    if (this.settingsButton == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                        throw null;
                    }
                    f2 = width2 - view2.getWidth();
                }
                float f3 = f * 50;
                View view6 = this.settingsButton;
                if (view6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                view6.setRotation(f3 * (-Math.signum(this.contentTranslation)));
                float saturate = MathUtils.saturate(MathUtils.map(0.5f, 1.0f, 0.0f, 1.0f, map));
                View view7 = this.settingsButton;
                if (view7 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                view7.setAlpha(saturate);
                View view8 = this.settingsButton;
                if (view8 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                if (!(saturate == 0.0f)) {
                    i = 0;
                }
                view8.setVisibility(i);
                View view9 = this.settingsButton;
                if (view9 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                view9.setTranslationX(f2);
                View view10 = this.settingsButton;
                if (view10 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
                int height = this.scrollView.getHeight();
                if (this.settingsButton != null) {
                    view10.setTranslationY((height - view3.getHeight()) / 2.0f);
                    return;
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
            }
        }
        View view11 = this.settingsButton;
        if (view11 != null) {
            view11.setVisibility(4);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
    }

    public final boolean onTouch(MotionEvent motionEvent) {
        float maxTranslation;
        PhysicsAnimator.SpringConfig springConfig;
        boolean z = true;
        boolean z2 = motionEvent.getAction() == 1;
        if (z2 && this.falsingProtectionNeeded) {
            this.falsingCollector.onNotificationStopDismissing();
        }
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            if (!z2) {
                return false;
            }
            this.scrollView.cancelCurrentScroll();
            return true;
        }
        if (z2 || motionEvent.getAction() == 3) {
            int relativeScrollX = this.scrollView.getRelativeScrollX();
            int i = this.playerWidthPlusPadding;
            int i2 = relativeScrollX % i;
            int i3 = i2 > i / 2 ? i - i2 : i2 * (-1);
            if (i3 != 0) {
                if (isRtl()) {
                    i3 = -i3;
                }
                final int relativeScrollX2 = this.scrollView.getRelativeScrollX() + i3;
                this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaScrollView mediaScrollView;
                        MediaScrollView mediaScrollView2;
                        mediaScrollView = MediaCarouselScrollHandler.this.scrollView;
                        int i4 = relativeScrollX2;
                        mediaScrollView2 = MediaCarouselScrollHandler.this.scrollView;
                        mediaScrollView.smoothScrollTo(i4, mediaScrollView2.getScrollY());
                    }
                });
            }
            float contentTranslation = this.scrollView.getContentTranslation();
            if (!(contentTranslation == 0.0f)) {
                if (Math.abs(contentTranslation) >= getMaxTranslation() / 2 && !isFalseTouch()) {
                    z = false;
                }
                if (z) {
                    maxTranslation = 0.0f;
                } else {
                    maxTranslation = getMaxTranslation() * Math.signum(contentTranslation);
                    if (!this.showsSettingsButton) {
                        this.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$2
                            @Override // java.lang.Runnable
                            public final void run() {
                                Function0 function0;
                                function0 = MediaCarouselScrollHandler.this.dismissCallback;
                                function0.mo1951invoke();
                            }
                        }, 100L);
                    }
                }
                PhysicsAnimator companion = PhysicsAnimator.Companion.getInstance(this);
                MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 = CONTENT_TRANSLATION;
                springConfig = MediaCarouselScrollHandlerKt.translationConfig;
                companion.spring(mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1, maxTranslation, 0.0f, springConfig).start();
                this.scrollView.setAnimationTargetX(maxTranslation);
            }
        }
        return false;
    }

    private final boolean isFalseTouch() {
        return this.falsingProtectionNeeded && this.falsingManager.isFalseTouch(1);
    }

    private final int getMaxTranslation() {
        if (this.showsSettingsButton) {
            View view = this.settingsButton;
            if (view != null) {
                return view.getWidth();
            }
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
        return this.playerWidthPlusPadding;
    }

    public final boolean onInterceptTouch(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public final boolean onScroll(@NotNull MotionEvent down, @NotNull MotionEvent lastMotion, float f) {
        PhysicsAnimator.SpringConfig springConfig;
        Intrinsics.checkNotNullParameter(down, "down");
        Intrinsics.checkNotNullParameter(lastMotion, "lastMotion");
        float x = lastMotion.getX() - down.getX();
        float contentTranslation = this.scrollView.getContentTranslation();
        int i = (contentTranslation > 0.0f ? 1 : (contentTranslation == 0.0f ? 0 : -1));
        boolean z = false;
        if (!(i == 0) || !this.scrollView.canScrollHorizontally((int) (-x))) {
            float f2 = contentTranslation - f;
            float abs = Math.abs(f2);
            if (abs > getMaxTranslation()) {
                if (!(Math.signum(f) == Math.signum(contentTranslation))) {
                    f2 = Math.abs(contentTranslation) > ((float) getMaxTranslation()) ? contentTranslation - (f * 0.2f) : Math.signum(f2) * (getMaxTranslation() + ((abs - getMaxTranslation()) * 0.2f));
                }
            }
            if (!(Math.signum(f2) == Math.signum(contentTranslation))) {
                if (i == 0) {
                    z = true;
                }
                if (!z && this.scrollView.canScrollHorizontally(-((int) f2))) {
                    f2 = 0.0f;
                }
            }
            PhysicsAnimator companion = PhysicsAnimator.Companion.getInstance(this);
            if (companion.isRunning()) {
                MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 = CONTENT_TRANSLATION;
                springConfig = MediaCarouselScrollHandlerKt.translationConfig;
                companion.spring(mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1, f2, 0.0f, springConfig).start();
            } else {
                setContentTranslation(f2);
            }
            this.scrollView.setAnimationTargetX(f2);
            return true;
        }
        return false;
    }

    public final boolean onFling(float f, float f2) {
        PhysicsAnimator.SpringConfig springConfig;
        float f3 = f * f;
        double d = f2;
        boolean z = false;
        if (f3 >= 0.5d * d * d && f3 >= 1000000.0f) {
            float contentTranslation = this.scrollView.getContentTranslation();
            float f4 = 0.0f;
            if (!(contentTranslation == 0.0f)) {
                if (Math.signum(f) == Math.signum(contentTranslation)) {
                    z = true;
                }
                if (z && !isFalseTouch()) {
                    f4 = getMaxTranslation() * Math.signum(contentTranslation);
                    if (!this.showsSettingsButton) {
                        this.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                Function0 function0;
                                function0 = MediaCarouselScrollHandler.this.dismissCallback;
                                function0.mo1951invoke();
                            }
                        }, 100L);
                    }
                }
                PhysicsAnimator companion = PhysicsAnimator.Companion.getInstance(this);
                MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 = CONTENT_TRANSLATION;
                springConfig = MediaCarouselScrollHandlerKt.translationConfig;
                companion.spring(mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1, f4, f, springConfig).start();
                this.scrollView.setAnimationTargetX(f4);
            } else {
                int relativeScrollX = this.scrollView.getRelativeScrollX();
                int i = this.playerWidthPlusPadding;
                int i2 = i > 0 ? relativeScrollX / i : 0;
                if (!isRtl() ? f < 0.0f : f > 0.0f) {
                    i2++;
                }
                final View childAt = this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, Math.max(0, i2)));
                this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaScrollView mediaScrollView;
                        MediaScrollView mediaScrollView2;
                        mediaScrollView = MediaCarouselScrollHandler.this.scrollView;
                        int left = childAt.getLeft();
                        mediaScrollView2 = MediaCarouselScrollHandler.this.scrollView;
                        mediaScrollView.smoothScrollTo(left, mediaScrollView2.getScrollY());
                    }
                });
            }
            return true;
        }
        return false;
    }

    public static /* synthetic */ void resetTranslation$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaCarouselScrollHandler.resetTranslation(z);
    }

    public final void resetTranslation(boolean z) {
        PhysicsAnimator.SpringConfig springConfig;
        if (!(this.scrollView.getContentTranslation() == 0.0f)) {
            if (z) {
                PhysicsAnimator companion = PhysicsAnimator.Companion.getInstance(this);
                MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 = CONTENT_TRANSLATION;
                springConfig = MediaCarouselScrollHandlerKt.translationConfig;
                companion.spring(mediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1, 0.0f, springConfig).start();
                this.scrollView.setAnimationTargetX(0.0f);
                return;
            }
            PhysicsAnimator.Companion.getInstance(this).cancel();
            setContentTranslation(0.0f);
        }
    }

    private final void updateClipToOutline() {
        boolean z = true;
        if ((this.contentTranslation == 0.0f) && this.scrollIntoCurrentMedia == 0) {
            z = false;
        }
        this.scrollView.setClipToOutline(z);
    }

    public final void onMediaScrollingChanged(int i, int i2) {
        boolean z = false;
        boolean z2 = this.scrollIntoCurrentMedia != 0;
        this.scrollIntoCurrentMedia = i2;
        if (i2 != 0) {
            z = true;
        }
        int i3 = this.visibleMediaIndex;
        if (i != i3 || z2 != z) {
            this.visibleMediaIndex = i;
            if (i3 != i && this.visibleToUser) {
                this.logSmartspaceImpression.mo1949invoke(Boolean.valueOf(this.qsExpanded));
            }
            this.closeGuts.mo1949invoke(Boolean.FALSE);
            updatePlayerVisibilities();
        }
        float f = this.visibleMediaIndex;
        int i4 = this.playerWidthPlusPadding;
        float f2 = f + (i4 > 0 ? i2 / i4 : 0.0f);
        if (isRtl()) {
            f2 = (this.mediaContent.getChildCount() - f2) - 1;
        }
        this.pageIndicator.setLocation(f2);
        updateClipToOutline();
    }

    public final void onPlayersChanged() {
        updatePlayerVisibilities();
        updateMediaPaddings();
    }

    private final void updateMediaPaddings() {
        int dimensionPixelSize = this.scrollView.getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_padding);
        int childCount = this.mediaContent.getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = this.mediaContent.getChildAt(i);
                int i3 = i == childCount + (-1) ? 0 : dimensionPixelSize;
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (marginLayoutParams.getMarginEnd() != i3) {
                    marginLayoutParams.setMarginEnd(i3);
                    childAt.setLayoutParams(marginLayoutParams);
                }
                if (i2 >= childCount) {
                    return;
                }
                i = i2;
            }
        }
    }

    private final void updatePlayerVisibilities() {
        boolean z = this.scrollIntoCurrentMedia != 0;
        int childCount = this.mediaContent.getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = this.mediaContent.getChildAt(i);
                int i3 = this.visibleMediaIndex;
                childAt.setVisibility(i == i3 || (i == i3 + 1 && z) ? 0 : 4);
                if (i2 >= childCount) {
                    return;
                }
                i = i2;
            }
        }
    }

    public final void onPrePlayerRemoved(@NotNull MediaControlPanel removed) {
        Intrinsics.checkNotNullParameter(removed, "removed");
        ViewGroup viewGroup = this.mediaContent;
        PlayerViewHolder playerViewHolder = removed.getPlayerViewHolder();
        int indexOfChild = viewGroup.indexOfChild(playerViewHolder == null ? null : playerViewHolder.getPlayer());
        int i = this.visibleMediaIndex;
        boolean z = true;
        boolean z2 = indexOfChild <= i;
        if (z2) {
            this.visibleMediaIndex = Math.max(0, i - 1);
        }
        if (!isRtl()) {
            z = z2;
        } else if (z2) {
            z = false;
        }
        if (z) {
            MediaScrollView mediaScrollView = this.scrollView;
            mediaScrollView.setScrollX(Math.max(mediaScrollView.getScrollX() - this.playerWidthPlusPadding, 0));
        }
    }

    public final void setCarouselBounds(int i, int i2) {
        int i3 = this.carouselHeight;
        if (i2 == i3 && i == i3) {
            return;
        }
        this.carouselWidth = i;
        this.carouselHeight = i2;
        this.scrollView.invalidateOutline();
    }

    public final void scrollToStart() {
        this.scrollView.setRelativeScrollX(0);
    }

    public static /* synthetic */ void scrollToPlayer$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = -1;
        }
        mediaCarouselScrollHandler.scrollToPlayer(i, i2);
    }

    public final void scrollToPlayer(int i, int i2) {
        if (i >= 0 && i < this.mediaContent.getChildCount()) {
            this.scrollView.setRelativeScrollX(i * this.playerWidthPlusPadding);
        }
        final View childAt = this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, i2));
        this.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollToPlayer$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaScrollView mediaScrollView;
                MediaScrollView mediaScrollView2;
                mediaScrollView = MediaCarouselScrollHandler.this.scrollView;
                int left = childAt.getLeft();
                mediaScrollView2 = MediaCarouselScrollHandler.this.scrollView;
                mediaScrollView.smoothScrollTo(left, mediaScrollView2.getScrollY());
            }
        }, 100L);
    }

    /* compiled from: MediaCarouselScrollHandler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
