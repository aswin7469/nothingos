package com.android.systemui.media;

import android.content.res.Resources;
import android.graphics.Outline;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.core.view.GestureDetectorCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.p012qs.PageIndicator;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0016*\u0003/=F\u0018\u0000 q2\u00020\u0001:\u0001qB\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012!\u0010\f\u001a\u001d\u0012\u0013\u0012\u00110\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\n0\r\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\n0\r\u0012\u0006\u0010\u0017\u001a\u00020\u0018¢\u0006\u0002\u0010\u0019J\b\u0010N\u001a\u00020\u001bH\u0002J\b\u0010O\u001a\u00020\u000eH\u0002J\u0018\u0010P\u001a\u00020\u000e2\u0006\u0010Q\u001a\u00020\u001e2\u0006\u0010R\u001a\u00020\u001eH\u0002J\u0010\u0010S\u001a\u00020\u000e2\u0006\u0010T\u001a\u00020UH\u0002J\u0018\u0010V\u001a\u00020\n2\u0006\u0010W\u001a\u00020\u001b2\u0006\u0010X\u001a\u00020\u001bH\u0002J\u0006\u0010Y\u001a\u00020\nJ\u000e\u0010Z\u001a\u00020\n2\u0006\u0010[\u001a\u00020\\J\u001e\u0010]\u001a\u00020\u000e2\u0006\u0010^\u001a\u00020U2\u0006\u0010_\u001a\u00020U2\u0006\u0010`\u001a\u00020\u001eJ\u000e\u0010a\u001a\u00020\n2\u0006\u0010b\u001a\u00020AJ\u0010\u0010c\u001a\u00020\u000e2\u0006\u0010T\u001a\u00020UH\u0002J\u0010\u0010d\u001a\u00020\n2\b\b\u0002\u0010e\u001a\u00020\u000eJ\u0018\u0010f\u001a\u00020\n2\b\b\u0002\u0010g\u001a\u00020\u001b2\u0006\u0010h\u001a\u00020\u001bJ\u0006\u0010i\u001a\u00020\nJ\u0016\u0010j\u001a\u00020\n2\u0006\u0010k\u001a\u00020\u001b2\u0006\u0010l\u001a\u00020\u001bJ\b\u0010m\u001a\u00020\nH\u0002J\b\u0010n\u001a\u00020\nH\u0002J\b\u0010o\u001a\u00020\nH\u0002J\b\u0010p\u001a\u00020\nH\u0002R\u000e\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R)\u0010\f\u001a\u001d\u0012\u0013\u0012\u00110\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\n0\rX\u0004¢\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u001d\u001a\u00020\u001e@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010'\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u000e\u0010,\u001a\u00020-X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u00020/X\u0004¢\u0006\u0004\n\u0002\u00100R\u0011\u00101\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b1\u0010)R\u001a\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\n0\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R$\u00104\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001b@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001a\u00109\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010)\"\u0004\b;\u0010+R\u0010\u0010<\u001a\u00020=X\u0004¢\u0006\u0004\n\u0002\u0010>R\u000e\u0010?\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020AX.¢\u0006\u0002\n\u0000R\u001a\u0010B\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010)\"\u0004\bD\u0010+R\u0010\u0010E\u001a\u00020FX\u0004¢\u0006\u0004\n\u0002\u0010GR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010I\u001a\u00020\u001b2\u0006\u0010H\u001a\u00020\u001b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bJ\u00106R\u001a\u0010K\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010)\"\u0004\bM\u0010+¨\u0006r"}, mo65043d2 = {"Lcom/android/systemui/media/MediaCarouselScrollHandler;", "", "scrollView", "Lcom/android/systemui/media/MediaScrollView;", "pageIndicator", "Lcom/android/systemui/qs/PageIndicator;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "dismissCallback", "Lkotlin/Function0;", "", "translationChangedListener", "closeGuts", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "immediate", "falsingCollector", "Lcom/android/systemui/classifier/FalsingCollector;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "logSmartspaceImpression", "logger", "Lcom/android/systemui/media/MediaUiEventLogger;", "(Lcom/android/systemui/media/MediaScrollView;Lcom/android/systemui/qs/PageIndicator;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lcom/android/systemui/classifier/FalsingCollector;Lcom/android/systemui/plugins/FalsingManager;Lkotlin/jvm/functions/Function1;Lcom/android/systemui/media/MediaUiEventLogger;)V", "carouselHeight", "", "carouselWidth", "value", "", "contentTranslation", "getContentTranslation", "()F", "setContentTranslation", "(F)V", "cornerRadius", "getDismissCallback", "()Lkotlin/jvm/functions/Function0;", "falsingProtectionNeeded", "getFalsingProtectionNeeded", "()Z", "setFalsingProtectionNeeded", "(Z)V", "gestureDetector", "Landroidx/core/view/GestureDetectorCompat;", "gestureListener", "com/android/systemui/media/MediaCarouselScrollHandler$gestureListener$1", "Lcom/android/systemui/media/MediaCarouselScrollHandler$gestureListener$1;", "isRtl", "mediaContent", "Landroid/view/ViewGroup;", "playerWidthPlusPadding", "getPlayerWidthPlusPadding", "()I", "setPlayerWidthPlusPadding", "(I)V", "qsExpanded", "getQsExpanded", "setQsExpanded", "scrollChangedListener", "com/android/systemui/media/MediaCarouselScrollHandler$scrollChangedListener$1", "Lcom/android/systemui/media/MediaCarouselScrollHandler$scrollChangedListener$1;", "scrollIntoCurrentMedia", "settingsButton", "Landroid/view/View;", "showsSettingsButton", "getShowsSettingsButton", "setShowsSettingsButton", "touchListener", "com/android/systemui/media/MediaCarouselScrollHandler$touchListener$1", "Lcom/android/systemui/media/MediaCarouselScrollHandler$touchListener$1;", "<set-?>", "visibleMediaIndex", "getVisibleMediaIndex", "visibleToUser", "getVisibleToUser", "setVisibleToUser", "getMaxTranslation", "isFalseTouch", "onFling", "vX", "vY", "onInterceptTouch", "motionEvent", "Landroid/view/MotionEvent;", "onMediaScrollingChanged", "newIndex", "scrollInAmount", "onPlayersChanged", "onPrePlayerRemoved", "removed", "Lcom/android/systemui/media/MediaControlPanel;", "onScroll", "down", "lastMotion", "distanceX", "onSettingsButtonUpdated", "button", "onTouch", "resetTranslation", "animate", "scrollToPlayer", "sourceIndex", "destIndex", "scrollToStart", "setCarouselBounds", "currentCarouselWidth", "currentCarouselHeight", "updateClipToOutline", "updateMediaPaddings", "updatePlayerVisibilities", "updateSettingsPresentation", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler {
    private static final MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 CONTENT_TRANSLATION = new MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1();
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public int carouselHeight;
    /* access modifiers changed from: private */
    public int carouselWidth;
    private final Function1<Boolean, Unit> closeGuts;
    private float contentTranslation;
    /* access modifiers changed from: private */
    public int cornerRadius;
    private final Function0<Unit> dismissCallback;
    /* access modifiers changed from: private */
    public final FalsingCollector falsingCollector;
    private final FalsingManager falsingManager;
    private boolean falsingProtectionNeeded;
    private final GestureDetectorCompat gestureDetector;
    private final MediaCarouselScrollHandler$gestureListener$1 gestureListener;
    private final Function1<Boolean, Unit> logSmartspaceImpression;
    private final MediaUiEventLogger logger;
    private final DelayableExecutor mainExecutor;
    private ViewGroup mediaContent;
    private final PageIndicator pageIndicator;
    private int playerWidthPlusPadding;
    private boolean qsExpanded;
    private final MediaCarouselScrollHandler$scrollChangedListener$1 scrollChangedListener;
    private int scrollIntoCurrentMedia;
    /* access modifiers changed from: private */
    public final MediaScrollView scrollView;
    private View settingsButton;
    private boolean showsSettingsButton;
    private final MediaCarouselScrollHandler$touchListener$1 touchListener;
    private Function0<Unit> translationChangedListener;
    private int visibleMediaIndex;
    private boolean visibleToUser;

    public MediaCarouselScrollHandler(MediaScrollView mediaScrollView, PageIndicator pageIndicator2, DelayableExecutor delayableExecutor, Function0<Unit> function0, Function0<Unit> function02, Function1<? super Boolean, Unit> function1, FalsingCollector falsingCollector2, FalsingManager falsingManager2, Function1<? super Boolean, Unit> function12, MediaUiEventLogger mediaUiEventLogger) {
        Intrinsics.checkNotNullParameter(mediaScrollView, "scrollView");
        Intrinsics.checkNotNullParameter(pageIndicator2, "pageIndicator");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(function0, "dismissCallback");
        Intrinsics.checkNotNullParameter(function02, "translationChangedListener");
        Intrinsics.checkNotNullParameter(function1, "closeGuts");
        Intrinsics.checkNotNullParameter(falsingCollector2, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(function12, "logSmartspaceImpression");
        Intrinsics.checkNotNullParameter(mediaUiEventLogger, "logger");
        this.scrollView = mediaScrollView;
        this.pageIndicator = pageIndicator2;
        this.mainExecutor = delayableExecutor;
        this.dismissCallback = function0;
        this.translationChangedListener = function02;
        this.closeGuts = function1;
        this.falsingCollector = falsingCollector2;
        this.falsingManager = falsingManager2;
        this.logSmartspaceImpression = function12;
        this.logger = mediaUiEventLogger;
        MediaCarouselScrollHandler$gestureListener$1 mediaCarouselScrollHandler$gestureListener$1 = new MediaCarouselScrollHandler$gestureListener$1(this);
        this.gestureListener = mediaCarouselScrollHandler$gestureListener$1;
        MediaCarouselScrollHandler$touchListener$1 mediaCarouselScrollHandler$touchListener$1 = new MediaCarouselScrollHandler$touchListener$1(this);
        this.touchListener = mediaCarouselScrollHandler$touchListener$1;
        MediaCarouselScrollHandler$scrollChangedListener$1 mediaCarouselScrollHandler$scrollChangedListener$1 = new MediaCarouselScrollHandler$scrollChangedListener$1(this);
        this.scrollChangedListener = mediaCarouselScrollHandler$scrollChangedListener$1;
        this.gestureDetector = new GestureDetectorCompat(mediaScrollView.getContext(), mediaCarouselScrollHandler$gestureListener$1);
        mediaScrollView.setTouchListener(mediaCarouselScrollHandler$touchListener$1);
        mediaScrollView.setOverScrollMode(2);
        this.mediaContent = mediaScrollView.getContentContainer();
        mediaScrollView.setOnScrollChangeListener(mediaCarouselScrollHandler$scrollChangedListener$1);
        mediaScrollView.setOutlineProvider(new ViewOutlineProvider(this) {
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            {
                this.this$0 = r1;
            }

            public void getOutline(View view, Outline outline) {
                if (outline != null) {
                    outline.setRoundRect(0, 0, this.this$0.carouselWidth, this.this$0.carouselHeight, (float) this.this$0.cornerRadius);
                }
            }
        });
    }

    public final Function0<Unit> getDismissCallback() {
        return this.dismissCallback;
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

    /* access modifiers changed from: private */
    public final void setContentTranslation(float f) {
        this.contentTranslation = f;
        this.mediaContent.setTranslationX(f);
        updateSettingsPresentation();
        this.translationChangedListener.invoke();
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

    public final boolean getShowsSettingsButton() {
        return this.showsSettingsButton;
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

    public final void onSettingsButtonUpdated(View view) {
        Intrinsics.checkNotNullParameter(view, "button");
        this.settingsButton = view;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            view = null;
        }
        Resources resources = view.getResources();
        View view3 = this.settingsButton;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        } else {
            view2 = view3;
        }
        this.cornerRadius = resources.getDimensionPixelSize(Utils.getThemeAttr(view2.getContext(), 16844145));
        updateSettingsPresentation();
        this.scrollView.invalidateOutline();
    }

    private final void updateSettingsPresentation() {
        int i = 4;
        View view = null;
        if (this.showsSettingsButton) {
            View view2 = this.settingsButton;
            if (view2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                view2 = null;
            }
            if (view2.getWidth() > 0) {
                float map = MathUtils.map(0.0f, (float) getMaxTranslation(), 0.0f, 1.0f, Math.abs(this.contentTranslation));
                float f = 1.0f - map;
                View view3 = this.settingsButton;
                if (view3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view3 = null;
                }
                float f2 = ((float) (-view3.getWidth())) * f * 0.3f;
                if (isRtl()) {
                    if (this.contentTranslation > 0.0f) {
                        float width = ((float) this.scrollView.getWidth()) - f2;
                        View view4 = this.settingsButton;
                        if (view4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                            view4 = null;
                        }
                        f2 = -(width - ((float) view4.getWidth()));
                    } else {
                        f2 = -f2;
                    }
                } else if (this.contentTranslation <= 0.0f) {
                    float width2 = ((float) this.scrollView.getWidth()) - f2;
                    View view5 = this.settingsButton;
                    if (view5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                        view5 = null;
                    }
                    f2 = width2 - ((float) view5.getWidth());
                }
                float f3 = f * ((float) 50);
                View view6 = this.settingsButton;
                if (view6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view6 = null;
                }
                view6.setRotation(f3 * (-Math.signum(this.contentTranslation)));
                float saturate = MathUtils.saturate(MathUtils.map(0.5f, 1.0f, 0.0f, 1.0f, map));
                View view7 = this.settingsButton;
                if (view7 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view7 = null;
                }
                view7.setAlpha(saturate);
                View view8 = this.settingsButton;
                if (view8 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view8 = null;
                }
                if (!(saturate == 0.0f)) {
                    i = 0;
                }
                view8.setVisibility(i);
                View view9 = this.settingsButton;
                if (view9 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view9 = null;
                }
                view9.setTranslationX(f2);
                View view10 = this.settingsButton;
                if (view10 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    view10 = null;
                }
                int height = this.scrollView.getHeight();
                View view11 = this.settingsButton;
                if (view11 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                } else {
                    view = view11;
                }
                view10.setTranslationY(((float) (height - view.getHeight())) / 2.0f);
                return;
            }
        }
        View view12 = this.settingsButton;
        if (view12 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        } else {
            view = view12;
        }
        view.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public final boolean onTouch(MotionEvent motionEvent) {
        float f;
        boolean z = true;
        boolean z2 = motionEvent.getAction() == 1;
        if (z2 && this.falsingProtectionNeeded) {
            this.falsingCollector.onNotificationStopDismissing();
        }
        if (!this.gestureDetector.onTouchEvent(motionEvent)) {
            if (z2 || motionEvent.getAction() == 3) {
                int relativeScrollX = this.scrollView.getRelativeScrollX();
                int i = this.playerWidthPlusPadding;
                int i2 = relativeScrollX % i;
                int i3 = i2 > i / 2 ? i - i2 : i2 * -1;
                if (i3 != 0) {
                    if (isRtl()) {
                        i3 = -i3;
                    }
                    this.mainExecutor.execute(new MediaCarouselScrollHandler$$ExternalSyntheticLambda0(this, this.scrollView.getRelativeScrollX() + i3));
                }
                float contentTranslation2 = this.scrollView.getContentTranslation();
                if (!(contentTranslation2 == 0.0f)) {
                    if (Math.abs(contentTranslation2) >= ((float) (getMaxTranslation() / 2)) && !isFalseTouch()) {
                        z = false;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = ((float) getMaxTranslation()) * Math.signum(contentTranslation2);
                        if (!this.showsSettingsButton) {
                            this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$$ExternalSyntheticLambda1(this), 100);
                        }
                    }
                    PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
                    this.scrollView.setAnimationTargetX(f);
                }
            }
            return false;
        } else if (!z2) {
            return false;
        } else {
            this.scrollView.cancelCurrentScroll();
            return true;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onTouch$lambda-0  reason: not valid java name */
    public static final void m2779onTouch$lambda0(MediaCarouselScrollHandler mediaCarouselScrollHandler, int i) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "this$0");
        MediaScrollView mediaScrollView = mediaCarouselScrollHandler.scrollView;
        mediaScrollView.smoothScrollTo(i, mediaScrollView.getScrollY());
    }

    /* access modifiers changed from: private */
    /* renamed from: onTouch$lambda-1  reason: not valid java name */
    public static final void m2780onTouch$lambda1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "this$0");
        mediaCarouselScrollHandler.dismissCallback.invoke();
    }

    private final boolean isFalseTouch() {
        return this.falsingProtectionNeeded && this.falsingManager.isFalseTouch(1);
    }

    private final int getMaxTranslation() {
        if (!this.showsSettingsButton) {
            return this.playerWidthPlusPadding;
        }
        View view = this.settingsButton;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            view = null;
        }
        return view.getWidth();
    }

    /* access modifiers changed from: private */
    public final boolean onInterceptTouch(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f) {
        Intrinsics.checkNotNullParameter(motionEvent, "down");
        Intrinsics.checkNotNullParameter(motionEvent2, "lastMotion");
        float x = motionEvent2.getX() - motionEvent.getX();
        float contentTranslation2 = this.scrollView.getContentTranslation();
        int i = (contentTranslation2 > 0.0f ? 1 : (contentTranslation2 == 0.0f ? 0 : -1));
        boolean z = false;
        if ((i == 0) && this.scrollView.canScrollHorizontally((int) (-x))) {
            return false;
        }
        float f2 = contentTranslation2 - f;
        float abs = Math.abs(f2);
        if (abs > ((float) getMaxTranslation())) {
            if (!(Math.signum(f) == Math.signum(contentTranslation2))) {
                f2 = Math.abs(contentTranslation2) > ((float) getMaxTranslation()) ? contentTranslation2 - (f * 0.2f) : Math.signum(f2) * (((float) getMaxTranslation()) + ((abs - ((float) getMaxTranslation())) * 0.2f));
            }
        }
        if (!(Math.signum(f2) == Math.signum(contentTranslation2))) {
            if (i == 0) {
                z = true;
            }
            if (!z && this.scrollView.canScrollHorizontally(-((int) f2))) {
                f2 = 0.0f;
            }
        }
        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(this);
        if (instance.isRunning()) {
            instance.spring(CONTENT_TRANSLATION, f2, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
        } else {
            setContentTranslation(f2);
        }
        this.scrollView.setAnimationTargetX(f2);
        return true;
    }

    /* access modifiers changed from: private */
    public final boolean onFling(float f, float f2) {
        float f3 = f * f;
        double d = (double) f2;
        boolean z = false;
        if (((double) f3) < 0.5d * d * d || f3 < 1000000.0f) {
            return false;
        }
        float contentTranslation2 = this.scrollView.getContentTranslation();
        float f4 = 0.0f;
        if (!(contentTranslation2 == 0.0f)) {
            if (Math.signum(f) == Math.signum(contentTranslation2)) {
                z = true;
            }
            if (z && !isFalseTouch()) {
                f4 = ((float) getMaxTranslation()) * Math.signum(contentTranslation2);
                if (!this.showsSettingsButton) {
                    this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$$ExternalSyntheticLambda2(this), 100);
                }
            }
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f4, f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(f4);
        } else {
            int relativeScrollX = this.scrollView.getRelativeScrollX();
            int i = this.playerWidthPlusPadding;
            int i2 = i > 0 ? relativeScrollX / i : 0;
            if (!isRtl() ? f < 0.0f : f > 0.0f) {
                i2++;
            }
            this.mainExecutor.execute(new MediaCarouselScrollHandler$$ExternalSyntheticLambda3(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, Math.max(0, i2)))));
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: onFling$lambda-2  reason: not valid java name */
    public static final void m2777onFling$lambda2(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "this$0");
        mediaCarouselScrollHandler.dismissCallback.invoke();
    }

    /* access modifiers changed from: private */
    /* renamed from: onFling$lambda-3  reason: not valid java name */
    public static final void m2778onFling$lambda3(MediaCarouselScrollHandler mediaCarouselScrollHandler, View view) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "this$0");
        mediaCarouselScrollHandler.scrollView.smoothScrollTo(view.getLeft(), mediaCarouselScrollHandler.scrollView.getScrollY());
    }

    public static /* synthetic */ void resetTranslation$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaCarouselScrollHandler.resetTranslation(z);
    }

    public final void resetTranslation(boolean z) {
        if (this.scrollView.getContentTranslation() == 0.0f) {
            return;
        }
        if (z) {
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(0.0f);
            return;
        }
        PhysicsAnimator.Companion.getInstance(this).cancel();
        setContentTranslation(0.0f);
    }

    private final void updateClipToOutline() {
        boolean z = true;
        if ((this.contentTranslation == 0.0f) && this.scrollIntoCurrentMedia == 0) {
            z = false;
        }
        this.scrollView.setClipToOutline(z);
    }

    /* access modifiers changed from: private */
    public final void onMediaScrollingChanged(int i, int i2) {
        boolean z = this.scrollIntoCurrentMedia != 0;
        this.scrollIntoCurrentMedia = i2;
        boolean z2 = i2 != 0;
        int i3 = this.visibleMediaIndex;
        if (!(i == i3 && z == z2)) {
            this.visibleMediaIndex = i;
            if (i3 != i && this.visibleToUser) {
                this.logSmartspaceImpression.invoke(Boolean.valueOf(this.qsExpanded));
                this.logger.logMediaCarouselPage(i);
            }
            this.closeGuts.invoke(false);
            updatePlayerVisibilities();
        }
        float f = (float) this.visibleMediaIndex;
        int i4 = this.playerWidthPlusPadding;
        float f2 = f + (i4 > 0 ? ((float) i2) / ((float) i4) : 0.0f);
        if (isRtl()) {
            f2 = (((float) this.mediaContent.getChildCount()) - f2) - ((float) 1);
        }
        this.pageIndicator.setLocation(f2);
        updateClipToOutline();
    }

    public final void onPlayersChanged() {
        updatePlayerVisibilities();
        updateMediaPaddings();
    }

    private final void updateMediaPaddings() {
        int dimensionPixelSize = this.scrollView.getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_padding);
        int childCount = this.mediaContent.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = this.mediaContent.getChildAt(i);
            int i2 = i == childCount + -1 ? 0 : dimensionPixelSize;
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (marginLayoutParams.getMarginEnd() != i2) {
                    marginLayoutParams.setMarginEnd(i2);
                    childAt.setLayoutParams(marginLayoutParams);
                }
                i++;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
        }
    }

    private final void updatePlayerVisibilities() {
        boolean z = this.scrollIntoCurrentMedia != 0;
        int childCount = this.mediaContent.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = this.mediaContent.getChildAt(i);
            int i2 = this.visibleMediaIndex;
            childAt.setVisibility(i == i2 || (i == i2 + 1 && z) ? 0 : 4);
            i++;
        }
    }

    public final void onPrePlayerRemoved(MediaControlPanel mediaControlPanel) {
        Intrinsics.checkNotNullParameter(mediaControlPanel, "removed");
        ViewGroup viewGroup = this.mediaContent;
        MediaViewHolder mediaViewHolder = mediaControlPanel.getMediaViewHolder();
        int indexOfChild = viewGroup.indexOfChild(mediaViewHolder != null ? mediaViewHolder.getPlayer() : null);
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
        if (i2 != i3 || i != i3) {
            this.carouselWidth = i;
            this.carouselHeight = i2;
            this.scrollView.invalidateOutline();
        }
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
        this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$$ExternalSyntheticLambda4(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, i2))), 100);
    }

    /* access modifiers changed from: private */
    /* renamed from: scrollToPlayer$lambda-4  reason: not valid java name */
    public static final void m2781scrollToPlayer$lambda4(MediaCarouselScrollHandler mediaCarouselScrollHandler, View view) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "this$0");
        mediaCarouselScrollHandler.scrollView.smoothScrollTo(view.getLeft(), mediaCarouselScrollHandler.scrollView.getScrollY());
    }

    @Metadata(mo65042d1 = {"\u0000\u0013\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\b\u0003*\u0001\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0004\n\u0002\u0010\u0005¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/media/MediaCarouselScrollHandler$Companion;", "", "()V", "CONTENT_TRANSLATION", "com/android/systemui/media/MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1", "Lcom/android/systemui/media/MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaCarouselScrollHandler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
