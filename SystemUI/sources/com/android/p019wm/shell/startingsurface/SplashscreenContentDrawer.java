package com.android.p019wm.shell.startingsurface;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import android.view.SurfaceControl;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import androidx.core.view.ViewCompat;
import com.android.internal.R;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.Quantizer;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import com.android.internal.protolog.common.ProtoLog;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.nothing.icons.IconFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer */
public class SplashscreenContentDrawer {
    private static final float ENLARGE_FOREGROUND_ICON_THRESHOLD = 0.44444445f;
    private static final float NO_BACKGROUND_SCALE = 1.2f;
    private static final String TAG = "ShellStartingWindow";
    /* access modifiers changed from: private */
    public int mBrandingImageHeight;
    /* access modifiers changed from: private */
    public int mBrandingImageWidth;
    final ColorCache mColorCache;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public int mDefaultIconSize;
    /* access modifiers changed from: private */
    public final HighResIconProvider mHighResIconProvider;
    /* access modifiers changed from: private */
    public int mIconSize;
    /* access modifiers changed from: private */
    public int mLastPackageContextConfigHash;
    private int mMainWindowShiftLength;
    /* access modifiers changed from: private */
    public final Handler mSplashscreenWorkerHandler;
    /* access modifiers changed from: private */
    public final SplashScreenWindowAttrs mTmpAttrs = new SplashScreenWindowAttrs();
    private final TransactionPool mTransactionPool;

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$SplashScreenWindowAttrs */
    public static class SplashScreenWindowAttrs {
        /* access modifiers changed from: private */
        public Drawable mBrandingImage = null;
        /* access modifiers changed from: private */
        public int mIconBgColor = 0;
        /* access modifiers changed from: private */
        public Drawable mSplashScreenIcon = null;
        /* access modifiers changed from: private */
        public int mWindowBgColor = 0;
        /* access modifiers changed from: private */
        public int mWindowBgResId = 0;
    }

    static long getShowingDuration(long j, long j2) {
        return (j > j2 && j2 < 500) ? (j > 500 || j2 < 400) ? 400 : 500 : j2;
    }

    SplashscreenContentDrawer(Context context, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        this.mHighResIconProvider = new HighResIconProvider(context, iconProvider);
        this.mTransactionPool = transactionPool;
        HandlerThread handlerThread = new HandlerThread("wmshell.splashworker", -10);
        handlerThread.start();
        Handler threadHandler = handlerThread.getThreadHandler();
        this.mSplashscreenWorkerHandler = threadHandler;
        this.mColorCache = new ColorCache(context, threadHandler);
    }

    /* access modifiers changed from: package-private */
    public void createContentView(Context context, int i, StartingWindowInfo startingWindowInfo, Consumer<SplashScreenView> consumer, Consumer<Runnable> consumer2) {
        this.mSplashscreenWorkerHandler.post(new SplashscreenContentDrawer$$ExternalSyntheticLambda8(this, context, startingWindowInfo, i, consumer2, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createContentView$0$com-android-wm-shell-startingsurface-SplashscreenContentDrawer */
    public /* synthetic */ void mo51118x7fa7d540(Context context, StartingWindowInfo startingWindowInfo, int i, Consumer consumer, Consumer consumer2) {
        SplashScreenView splashScreenView;
        try {
            Trace.traceBegin(32, "makeSplashScreenContentView");
            splashScreenView = makeSplashScreenContentView(context, startingWindowInfo, i, consumer);
            Trace.traceEnd(32);
        } catch (RuntimeException e) {
            Slog.w("ShellStartingWindow", "failed creating starting window content at taskId: " + startingWindowInfo.taskInfo.taskId, e);
            splashScreenView = null;
        }
        consumer2.accept(splashScreenView);
    }

    /* access modifiers changed from: package-private */
    public void createContentView(Context context, int i, StartingWindowInfo startingWindowInfo, Consumer<SplashScreenView> consumer, Consumer<Runnable> consumer2, Drawable drawable) {
        this.mSplashscreenWorkerHandler.post(new SplashscreenContentDrawer$$ExternalSyntheticLambda0(this, context, startingWindowInfo, i, consumer2, drawable, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createContentView$1$com-android-wm-shell-startingsurface-SplashscreenContentDrawer */
    public /* synthetic */ void mo51119xfe08d91f(Context context, StartingWindowInfo startingWindowInfo, int i, Consumer consumer, Drawable drawable, Consumer consumer2) {
        SplashScreenView splashScreenView;
        try {
            Trace.traceBegin(32, "makeSplashScreenContentView");
            splashScreenView = makeSplashScreenContentView(context, startingWindowInfo, i, consumer, drawable);
            Trace.traceEnd(32);
        } catch (RuntimeException e) {
            Slog.w("ShellStartingWindow", "failed creating starting window content at taskId: " + startingWindowInfo.taskInfo.taskId, e);
            splashScreenView = null;
        }
        consumer2.accept(splashScreenView);
    }

    private void updateDensity() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(17105545);
        this.mDefaultIconSize = this.mContext.getResources().getDimensionPixelSize(17105544);
        this.mBrandingImageWidth = this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.starting_surface_brand_image_width);
        this.mBrandingImageHeight = this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.starting_surface_brand_image_height);
        this.mMainWindowShiftLength = this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.starting_surface_exit_animation_window_shift_length);
    }

    public static int getSystemBGColor() {
        Application currentApplication = ActivityThread.currentApplication();
        if (currentApplication != null) {
            return currentApplication.getResources().getColor(C3353R.C3354color.splash_window_background_default);
        }
        Slog.e("ShellStartingWindow", "System context does not exist!");
        return ViewCompat.MEASURED_STATE_MASK;
    }

    /* access modifiers changed from: package-private */
    public int estimateTaskBackgroundColor(Context context) {
        SplashScreenWindowAttrs splashScreenWindowAttrs = new SplashScreenWindowAttrs();
        getWindowAttrs(context, splashScreenWindowAttrs);
        return peekWindowBGColor(context, splashScreenWindowAttrs);
    }

    /* access modifiers changed from: private */
    public static Drawable createDefaultBackgroundDrawable() {
        return new ColorDrawable(getSystemBGColor());
    }

    private static int peekWindowBGColor(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        Drawable drawable;
        Trace.traceBegin(32, "peekWindowBGColor");
        if (splashScreenWindowAttrs.mWindowBgColor != 0) {
            drawable = new ColorDrawable(splashScreenWindowAttrs.mWindowBgColor);
        } else if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            drawable = context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        } else {
            drawable = createDefaultBackgroundDrawable();
            Slog.w("ShellStartingWindow", "Window background does not exist, using " + drawable);
        }
        int estimateWindowBGColor = estimateWindowBGColor(drawable);
        Trace.traceEnd(32);
        return estimateWindowBGColor;
    }

    /* access modifiers changed from: private */
    public static int estimateWindowBGColor(Drawable drawable) {
        DrawableColorTester drawableColorTester = new DrawableColorTester(drawable, 1);
        if (drawableColorTester.passFilterRatio() != 0.0f) {
            return drawableColorTester.getDominateColor();
        }
        Slog.w("ShellStartingWindow", "Window background is transparent, fill background with black color");
        return getSystemBGColor();
    }

    private static Drawable peekLegacySplashscreenContent(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Window);
        int intValue = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda7(obtainStyledAttributes), 0)).intValue();
        obtainStyledAttributes.recycle();
        if (intValue != 0) {
            return context.getDrawable(intValue);
        }
        if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            return context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        }
        return null;
    }

    private SplashScreenView makeSplashScreenContentView(Context context, StartingWindowInfo startingWindowInfo, int i, Consumer<Runnable> consumer) {
        ActivityInfo activityInfo;
        int i2;
        updateDensity();
        getWindowAttrs(context, this.mTmpAttrs);
        this.mLastPackageContextConfigHash = context.getResources().getConfiguration().hashCode();
        Drawable peekLegacySplashscreenContent = i == 4 ? peekLegacySplashscreenContent(context, this.mTmpAttrs) : null;
        if (startingWindowInfo.targetActivityInfo != null) {
            activityInfo = startingWindowInfo.targetActivityInfo;
        } else {
            activityInfo = startingWindowInfo.taskInfo.topActivityInfo;
        }
        if (peekLegacySplashscreenContent != null) {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda10(peekLegacySplashscreenContent));
        } else {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda11(this, context));
        }
        return new StartingWindowViewBuilder(context, activityInfo).setWindowBGColor(i2).overlayDrawable(peekLegacySplashscreenContent).chooseStyle(i).setUiThreadInitConsumer(consumer).setAllowHandleSolidColor(startingWindowInfo.allowHandleSolidColorSplashScreen()).build();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeSplashScreenContentView$4$com-android-wm-shell-startingsurface-SplashscreenContentDrawer */
    public /* synthetic */ int mo51120x8eaa16c1(Context context) {
        return peekWindowBGColor(context, this.mTmpAttrs);
    }

    private SplashScreenView makeSplashScreenContentView(Context context, StartingWindowInfo startingWindowInfo, int i, Consumer<Runnable> consumer, Drawable drawable) {
        ActivityInfo activityInfo;
        int i2;
        updateDensity();
        getWindowAttrs(context, this.mTmpAttrs);
        this.mLastPackageContextConfigHash = context.getResources().getConfiguration().hashCode();
        Drawable peekLegacySplashscreenContent = i == 4 ? peekLegacySplashscreenContent(context, this.mTmpAttrs) : null;
        if (startingWindowInfo.targetActivityInfo != null) {
            activityInfo = startingWindowInfo.targetActivityInfo;
        } else {
            activityInfo = startingWindowInfo.taskInfo.topActivityInfo;
        }
        if (peekLegacySplashscreenContent != null) {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda1(peekLegacySplashscreenContent));
        } else {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda2(this, context));
        }
        return new StartingWindowViewBuilder(context, activityInfo).setWindowBGColor(i2).overlayDrawable(peekLegacySplashscreenContent).chooseStyle(i).setUiThreadInitConsumer(consumer).setAllowHandleSolidColor(startingWindowInfo.allowHandleSolidColorSplashScreen()).iconPackDrawable(drawable).build();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeSplashScreenContentView$6$com-android-wm-shell-startingsurface-SplashscreenContentDrawer */
    public /* synthetic */ int mo51121x8b6c1e7f(Context context) {
        return peekWindowBGColor(context, this.mTmpAttrs);
    }

    private int getBGColorFromCache(ActivityInfo activityInfo, IntSupplier intSupplier) {
        return this.mColorCache.getWindowColor(activityInfo.packageName, this.mLastPackageContextConfigHash, this.mTmpAttrs.mWindowBgColor, this.mTmpAttrs.mWindowBgResId, intSupplier).mBgColor;
    }

    private static <T> T safeReturnAttrDefault(UnaryOperator<T> unaryOperator, T t) {
        try {
            return unaryOperator.apply(t);
        } catch (RuntimeException e) {
            Slog.w("ShellStartingWindow", "Get attribute fail, return default: " + e.getMessage());
            return t;
        }
    }

    private static void getWindowAttrs(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Window);
        boolean z = false;
        int unused = splashScreenWindowAttrs.mWindowBgResId = obtainStyledAttributes.getResourceId(1, 0);
        int unused2 = splashScreenWindowAttrs.mWindowBgColor = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda3(obtainStyledAttributes), 0)).intValue();
        Drawable unused3 = splashScreenWindowAttrs.mSplashScreenIcon = (Drawable) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda4(obtainStyledAttributes), (Object) null);
        Drawable unused4 = splashScreenWindowAttrs.mBrandingImage = (Drawable) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda5(obtainStyledAttributes), (Object) null);
        int unused5 = splashScreenWindowAttrs.mIconBgColor = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda6(obtainStyledAttributes), 0)).intValue();
        obtainStyledAttributes.recycle();
        ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
        Object[] objArr = new Object[2];
        objArr[0] = Integer.toHexString(splashScreenWindowAttrs.mWindowBgColor);
        if (splashScreenWindowAttrs.mSplashScreenIcon != null) {
            z = true;
        }
        objArr[1] = Boolean.valueOf(z);
        ProtoLog.v(shellProtoLogGroup, "getWindowAttrs: window attributes color: %s, replace icon: %b", objArr);
    }

    /* access modifiers changed from: package-private */
    public ContextThemeWrapper createViewContextWrapper(Context context) {
        return new ContextThemeWrapper(context, this.mContext.getTheme());
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$StartingWindowViewBuilder */
    private class StartingWindowViewBuilder {
        private final ActivityInfo mActivityInfo;
        private boolean mAllowHandleSolidColor;
        private final Context mContext;
        private Drawable[] mFinalIconDrawables;
        private int mFinalIconSize;
        private Drawable mIconPackDrawable;
        private Drawable mOverlayDrawable;
        private int mSuggestType;
        private int mThemeColor;
        private Consumer<Runnable> mUiThreadInitTask;
        private boolean mUseIconPack = false;

        StartingWindowViewBuilder(Context context, ActivityInfo activityInfo) {
            this.mFinalIconSize = SplashscreenContentDrawer.this.mIconSize;
            this.mContext = context;
            this.mActivityInfo = activityInfo;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder setWindowBGColor(int i) {
            this.mThemeColor = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder overlayDrawable(Drawable drawable) {
            this.mOverlayDrawable = drawable;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder chooseStyle(int i) {
            this.mSuggestType = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder iconPackDrawable(Drawable drawable) {
            this.mIconPackDrawable = drawable;
            this.mUseIconPack = true;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder setUiThreadInitConsumer(Consumer<Runnable> consumer) {
            this.mUiThreadInitTask = consumer;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartingWindowViewBuilder setAllowHandleSolidColor(boolean z) {
            this.mAllowHandleSolidColor = z;
            return this;
        }

        /* access modifiers changed from: package-private */
        public SplashScreenView build() {
            Drawable drawable;
            int i = this.mSuggestType;
            if (i == 3 || i == 4) {
                this.mFinalIconSize = 0;
            } else if (SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon != null) {
                Drawable access$200 = SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon;
                if (SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == 0 || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == this.mThemeColor) {
                    this.mFinalIconSize = (int) (((float) this.mFinalIconSize) * SplashscreenContentDrawer.NO_BACKGROUND_SCALE);
                }
                if (this.mUseIconPack) {
                    createIconDrawable(this.mIconPackDrawable, true, false);
                } else {
                    createIconDrawable(IconFactory.getInstance().createNormalizedIcon(this.mContext, access$200), false, false);
                }
            } else {
                float access$500 = ((float) SplashscreenContentDrawer.this.mIconSize) / ((float) SplashscreenContentDrawer.this.mDefaultIconSize);
                int i2 = this.mContext.getResources().getConfiguration().densityDpi;
                int i3 = (int) ((access$500 * ((float) i2) * SplashscreenContentDrawer.NO_BACKGROUND_SCALE) + 0.5f);
                Trace.traceBegin(32, "getIcon");
                if (this.mUseIconPack) {
                    drawable = this.mIconPackDrawable;
                    this.mFinalIconSize = (int) (((float) this.mFinalIconSize) * SplashscreenContentDrawer.NO_BACKGROUND_SCALE);
                    createIconDrawable(drawable, true, false);
                } else {
                    drawable = IconFactory.getInstance().createNormalizedIcon(this.mContext, SplashscreenContentDrawer.this.mHighResIconProvider.getIcon(this.mActivityInfo, i2, i3));
                }
                Trace.traceEnd(32);
                if (!processAdaptiveIcon(drawable) && this.mIconPackDrawable == null) {
                    ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "The icon is not an AdaptiveIconDrawable", new Object[0]);
                    Trace.traceBegin(32, "legacy_icon_factory");
                    Bitmap createScaledBitmapWithoutShadow = new ShapeIconFactory(SplashscreenContentDrawer.this.mContext, i3, this.mFinalIconSize).createScaledBitmapWithoutShadow(drawable);
                    Trace.traceEnd(32);
                    createIconDrawable(new BitmapDrawable(createScaledBitmapWithoutShadow), true, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
                }
            }
            return fillViewWithIcon(this.mFinalIconSize, this.mFinalIconDrawables, this.mUiThreadInitTask);
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$StartingWindowViewBuilder$ShapeIconFactory */
        private class ShapeIconFactory extends BaseIconFactory {
            protected ShapeIconFactory(Context context, int i, int i2) {
                super(context, i, i2, true);
            }
        }

        private void createIconDrawable(Drawable drawable, boolean z, boolean z2) {
            if (z) {
                this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeLegacyIconDrawable(drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, z2, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
                return;
            }
            this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeIconDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor, this.mThemeColor, drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, z2, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
        }

        private boolean processAdaptiveIcon(Drawable drawable) {
            if (!(drawable instanceof AdaptiveIconDrawable)) {
                return false;
            }
            Trace.traceBegin(32, "processAdaptiveIcon");
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable;
            Drawable foreground = adaptiveIconDrawable.getForeground();
            ColorCache.IconColor iconColor = SplashscreenContentDrawer.this.mColorCache.getIconColor(this.mActivityInfo.packageName, this.mActivityInfo.getIconResource(), SplashscreenContentDrawer.this.mLastPackageContextConfigHash, new C3600xa4ba13a8(foreground), new C3601xa4ba13a9(adaptiveIconDrawable));
            ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
            Object[] objArr = new Object[5];
            objArr[0] = Integer.toHexString(iconColor.mFgColor);
            objArr[1] = Integer.toHexString(iconColor.mBgColor);
            objArr[2] = Boolean.valueOf(iconColor.mIsBgComplex);
            objArr[3] = Boolean.valueOf(iconColor.mReuseCount > 0);
            objArr[4] = Integer.toHexString(this.mThemeColor);
            ProtoLog.v(shellProtoLogGroup, "processAdaptiveIcon: FgMainColor=%s, BgMainColor=%s, IsBgComplex=%b, FromCache=%b, ThemeColor=%s", objArr);
            if (iconColor.mIsBgComplex || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor != 0 || (!SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mBgColor) && (!iconColor.mIsBgGrayscale || SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mFgColor)))) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "processAdaptiveIcon: draw whole icon", new Object[0]);
                createIconDrawable(drawable, false, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
            } else {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "processAdaptiveIcon: choose fg icon", new Object[0]);
                this.mFinalIconSize = (int) ((((float) SplashscreenContentDrawer.this.mIconSize) * (iconColor.mFgNonTranslucentRatio < SplashscreenContentDrawer.ENLARGE_FOREGROUND_ICON_THRESHOLD ? SplashscreenContentDrawer.NO_BACKGROUND_SCALE : 1.0f)) + 0.5f);
                createIconDrawable(foreground, false, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
            }
            Trace.traceEnd(32);
            return true;
        }

        static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$0(Drawable drawable) {
            return new DrawableColorTester(drawable, 2);
        }

        static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$1(AdaptiveIconDrawable adaptiveIconDrawable) {
            return new DrawableColorTester(adaptiveIconDrawable.getBackground());
        }

        private SplashScreenView fillViewWithIcon(int i, Drawable[] drawableArr, Consumer<Runnable> consumer) {
            Drawable drawable;
            Drawable drawable2 = null;
            if (drawableArr != null) {
                drawable = drawableArr.length > 0 ? drawableArr[0] : null;
                if (drawableArr.length > 1) {
                    drawable2 = drawableArr[1];
                }
            } else {
                drawable = null;
            }
            Trace.traceBegin(32, "fillViewWithIcon");
            SplashScreenView.Builder allowHandleSolidColor = new SplashScreenView.Builder(SplashscreenContentDrawer.this.createViewContextWrapper(this.mContext)).setBackgroundColor(this.mThemeColor).setOverlayDrawable(this.mOverlayDrawable).setIconSize(i).setIconBackground(drawable2).setCenterViewDrawable(drawable).setUiThreadInitConsumer(consumer).setAllowHandleSolidColor(this.mAllowHandleSolidColor);
            if (this.mSuggestType == 1 && SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage != null) {
                allowHandleSolidColor.setBrandingDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage, SplashscreenContentDrawer.this.mBrandingImageWidth, SplashscreenContentDrawer.this.mBrandingImageHeight);
            }
            SplashScreenView build = allowHandleSolidColor.build();
            Trace.traceEnd(32);
            return build;
        }
    }

    /* access modifiers changed from: private */
    public static boolean isRgbSimilarInHsv(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (i3 == i4) {
            return true;
        }
        float luminance = Color.luminance(i);
        float luminance2 = Color.luminance(i2);
        float f = luminance > luminance2 ? (luminance + 0.05f) / (luminance2 + 0.05f) : (luminance2 + 0.05f) / (luminance + 0.05f);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "isRgbSimilarInHsv a:%s, b:%s, contrast ratio:%f", new Object[]{Integer.toHexString(i), Integer.toHexString(i2), Float.valueOf(f)});
        if (f < 2.0f) {
            return true;
        }
        float[] fArr = new float[3];
        float[] fArr2 = new float[3];
        Color.colorToHSV(i3, fArr);
        Color.colorToHSV(i4, fArr2);
        int abs = ((((int) Math.abs(fArr[0] - fArr2[0])) + 180) % StackStateAnimator.ANIMATION_DURATION_STANDARD) - 180;
        double pow = Math.pow((double) (((float) abs) / 180.0f), 2.0d);
        double pow2 = Math.pow((double) (fArr[1] - fArr2[1]), 2.0d);
        double pow3 = Math.pow((double) (fArr[2] - fArr2[2]), 2.0d);
        double sqrt = Math.sqrt(((pow + pow2) + pow3) / 3.0d);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "isRgbSimilarInHsv hsvDiff: %d, ah: %f, bh: %f, as: %f, bs: %f, av: %f, bv: %f, sqH: %f, sqS: %f, sqV: %f, rsm: %f", new Object[]{Integer.valueOf(abs), Float.valueOf(fArr[0]), Float.valueOf(fArr2[0]), Float.valueOf(fArr[1]), Float.valueOf(fArr2[1]), Float.valueOf(fArr[2]), Float.valueOf(fArr2[2]), Double.valueOf(pow), Double.valueOf(pow2), Double.valueOf(pow3), Double.valueOf(sqrt)});
        if (sqrt < 0.1d) {
            return true;
        }
        return false;
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester */
    private static class DrawableColorTester {
        private static final int NO_ALPHA_FILTER = 0;
        private static final int TRANSLUCENT_FILTER = 2;
        private static final int TRANSPARENT_FILTER = 1;
        private final ColorTester mColorChecker;

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ColorTester */
        private interface ColorTester {
            int getDominantColor();

            boolean isComplexColor();

            boolean isGrayscale();

            float passFilterRatio();
        }

        DrawableColorTester(Drawable drawable) {
            this(drawable, 0);
        }

        DrawableColorTester(Drawable drawable, int i) {
            ColorTester colorTester;
            if (drawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                if (layerDrawable.getNumberOfLayers() > 0) {
                    ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "DrawableColorTester: replace drawable with bottom layer drawable", new Object[0]);
                    drawable = layerDrawable.getDrawable(0);
                }
            }
            if (drawable == null) {
                this.mColorChecker = new SingleColorTester((ColorDrawable) SplashscreenContentDrawer.createDefaultBackgroundDrawable());
                return;
            }
            if (drawable instanceof ColorDrawable) {
                colorTester = new SingleColorTester((ColorDrawable) drawable);
            } else {
                colorTester = new ComplexDrawableTester(drawable, i);
            }
            this.mColorChecker = colorTester;
        }

        public float passFilterRatio() {
            return this.mColorChecker.passFilterRatio();
        }

        public boolean isComplexColor() {
            return this.mColorChecker.isComplexColor();
        }

        public int getDominateColor() {
            return this.mColorChecker.getDominantColor();
        }

        public boolean isGrayscale() {
            return this.mColorChecker.isGrayscale();
        }

        /* access modifiers changed from: private */
        public static boolean isGrayscaleColor(int i) {
            int red = Color.red(i);
            int green = Color.green(i);
            return red == green && green == Color.blue(i);
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$SingleColorTester */
        private static class SingleColorTester implements ColorTester {
            private final ColorDrawable mColorDrawable;

            public boolean isComplexColor() {
                return false;
            }

            SingleColorTester(ColorDrawable colorDrawable) {
                this.mColorDrawable = colorDrawable;
            }

            public float passFilterRatio() {
                return (float) (this.mColorDrawable.getAlpha() / 255);
            }

            public int getDominantColor() {
                return this.mColorDrawable.getColor();
            }

            public boolean isGrayscale() {
                return DrawableColorTester.isGrayscaleColor(this.mColorDrawable.getColor());
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester */
        private static class ComplexDrawableTester implements ColorTester {
            private static final AlphaFilterQuantizer ALPHA_FILTER_QUANTIZER = new AlphaFilterQuantizer();
            private static final int MAX_BITMAP_SIZE = 40;
            private final boolean mFilterTransparent;
            private final Palette mPalette;

            ComplexDrawableTester(Drawable drawable, int i) {
                int i2;
                Palette.Builder builder;
                Trace.traceBegin(32, "ComplexDrawableTester");
                Rect copyBounds = drawable.copyBounds();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int i3 = 40;
                if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                    i2 = 40;
                } else {
                    i3 = Math.min(intrinsicWidth, 40);
                    i2 = Math.min(intrinsicHeight, 40);
                }
                Bitmap createBitmap = Bitmap.createBitmap(i3, i2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                boolean z = false;
                drawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                drawable.draw(canvas);
                drawable.setBounds(copyBounds);
                z = i != 0 ? true : z;
                this.mFilterTransparent = z;
                if (z) {
                    AlphaFilterQuantizer alphaFilterQuantizer = ALPHA_FILTER_QUANTIZER;
                    alphaFilterQuantizer.setFilter(i);
                    builder = new Palette.Builder(createBitmap, alphaFilterQuantizer).maximumColorCount(5);
                } else {
                    builder = new Palette.Builder(createBitmap, (Quantizer) null).maximumColorCount(5);
                }
                this.mPalette = builder.generate();
                createBitmap.recycle();
                Trace.traceEnd(32);
            }

            public float passFilterRatio() {
                if (this.mFilterTransparent) {
                    return ALPHA_FILTER_QUANTIZER.mPassFilterRatio;
                }
                return 1.0f;
            }

            public boolean isComplexColor() {
                return this.mPalette.getSwatches().size() > 1;
            }

            public int getDominantColor() {
                Palette.Swatch dominantSwatch = this.mPalette.getDominantSwatch();
                return dominantSwatch != null ? dominantSwatch.getInt() : ViewCompat.MEASURED_STATE_MASK;
            }

            public boolean isGrayscale() {
                List swatches = this.mPalette.getSwatches();
                if (swatches != null) {
                    for (int size = swatches.size() - 1; size >= 0; size--) {
                        if (!DrawableColorTester.isGrayscaleColor(((Palette.Swatch) swatches.get(size)).getInt())) {
                            return false;
                        }
                    }
                }
                return true;
            }

            /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer */
            private static class AlphaFilterQuantizer implements Quantizer {
                private static final int NON_TRANSPARENT = -16777216;
                private IntPredicate mFilter;
                private final Quantizer mInnerQuantizer;
                /* access modifiers changed from: private */
                public float mPassFilterRatio;
                private final IntPredicate mTranslucentFilter;
                private final IntPredicate mTransparentFilter;

                static /* synthetic */ boolean lambda$new$0(int i) {
                    return (i & -16777216) != 0;
                }

                static /* synthetic */ boolean lambda$new$1(int i) {
                    return (i & -16777216) == -16777216;
                }

                private AlphaFilterQuantizer() {
                    this.mInnerQuantizer = new VariationalKMeansQuantizer();
                    C3598xf5e80cfe splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 = new C3598xf5e80cfe();
                    this.mTransparentFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                    this.mTranslucentFilter = new C3599xf5e80cff();
                    this.mFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                }

                /* access modifiers changed from: package-private */
                public void setFilter(int i) {
                    if (i != 2) {
                        this.mFilter = this.mTransparentFilter;
                    } else {
                        this.mFilter = this.mTranslucentFilter;
                    }
                }

                public void quantize(int[] iArr, int i) {
                    this.mPassFilterRatio = 0.0f;
                    int i2 = 0;
                    int i3 = 0;
                    for (int length = iArr.length - 1; length > 0; length--) {
                        if (this.mFilter.test(iArr[length])) {
                            i3++;
                        }
                    }
                    if (i3 == 0) {
                        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "DrawableTester quantize: pure transparent image", new Object[0]);
                        this.mInnerQuantizer.quantize(iArr, i);
                        return;
                    }
                    this.mPassFilterRatio = ((float) i3) / ((float) iArr.length);
                    int[] iArr2 = new int[i3];
                    for (int length2 = iArr.length - 1; length2 > 0; length2--) {
                        if (this.mFilter.test(iArr[length2])) {
                            iArr2[i2] = iArr[length2];
                            i2++;
                        }
                    }
                    this.mInnerQuantizer.quantize(iArr2, i);
                }

                public List<Palette.Swatch> getQuantizedColors() {
                    return this.mInnerQuantizer.getQuantizedColors();
                }
            }
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache */
    static class ColorCache extends BroadcastReceiver {
        private static final int CACHE_SIZE = 2;
        private final ArrayMap<String, Colors> mColorMap = new ArrayMap<>();

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors */
        private static class Colors {
            final IconColor[] mIconColors;
            final WindowColor[] mWindowColors;

            private Colors() {
                this.mWindowColors = new WindowColor[2];
                this.mIconColors = new IconColor[2];
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Cache */
        private static class Cache {
            final int mHash;
            int mReuseCount;

            Cache(int i) {
                this.mHash = i;
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor */
        static class WindowColor extends Cache {
            final int mBgColor;

            WindowColor(int i, int i2) {
                super(i);
                this.mBgColor = i2;
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$IconColor */
        static class IconColor extends Cache {
            final int mBgColor;
            final int mFgColor;
            final float mFgNonTranslucentRatio;
            final boolean mIsBgComplex;
            final boolean mIsBgGrayscale;

            IconColor(int i, int i2, int i3, boolean z, boolean z2, float f) {
                super(i);
                this.mFgColor = i2;
                this.mBgColor = i3;
                this.mIsBgComplex = z;
                this.mIsBgGrayscale = z2;
                this.mFgNonTranslucentRatio = f;
            }
        }

        ColorCache(Context context, Handler handler) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, (String) null, handler);
        }

        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                this.mColorMap.remove(data.getEncodedSchemeSpecificPart());
            }
        }

        private static <T extends Cache> T getCache(T[] tArr, int i, int[] iArr) {
            int i2 = Integer.MAX_VALUE;
            for (int i3 = 0; i3 < 2; i3++) {
                T t = tArr[i3];
                if (t == null) {
                    iArr[0] = i3;
                    i2 = -1;
                } else if (t.mHash == i) {
                    t.mReuseCount++;
                    return t;
                } else if (t.mReuseCount < i2) {
                    i2 = t.mReuseCount;
                    iArr[0] = i3;
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public WindowColor getWindowColor(String str, int i, int i2, int i3, IntSupplier intSupplier) {
            Colors colors = this.mColorMap.get(str);
            int i4 = (((i * 31) + i2) * 31) + i3;
            int[] iArr = {0};
            if (colors != null) {
                WindowColor windowColor = (WindowColor) getCache(colors.mWindowColors, i4, iArr);
                if (windowColor != null) {
                    return windowColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            WindowColor windowColor2 = new WindowColor(i4, intSupplier.getAsInt());
            colors.mWindowColors[iArr[0]] = windowColor2;
            return windowColor2;
        }

        /* access modifiers changed from: package-private */
        public IconColor getIconColor(String str, int i, int i2, Supplier<DrawableColorTester> supplier, Supplier<DrawableColorTester> supplier2) {
            Colors colors = this.mColorMap.get(str);
            int i3 = (i * 31) + i2;
            int[] iArr = {0};
            if (colors != null) {
                IconColor iconColor = (IconColor) getCache(colors.mIconColors, i3, iArr);
                if (iconColor != null) {
                    return iconColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            DrawableColorTester drawableColorTester = supplier.get();
            DrawableColorTester drawableColorTester2 = supplier2.get();
            IconColor iconColor2 = new IconColor(i3, drawableColorTester.getDominateColor(), drawableColorTester2.getDominateColor(), drawableColorTester2.isComplexColor(), drawableColorTester2.isGrayscale(), drawableColorTester.passFilterRatio());
            colors.mIconColors[iArr[0]] = iconColor2;
            return iconColor2;
        }
    }

    /* access modifiers changed from: package-private */
    public void applyExitAnimation(SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable, long j) {
        SplashscreenContentDrawer$$ExternalSyntheticLambda9 splashscreenContentDrawer$$ExternalSyntheticLambda9 = new SplashscreenContentDrawer$$ExternalSyntheticLambda9(this, splashScreenView, surfaceControl, rect, runnable);
        if (splashScreenView.getIconView() == null) {
            splashscreenContentDrawer$$ExternalSyntheticLambda9.run();
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis() - j;
        long showingDuration = getShowingDuration(splashScreenView.getIconAnimationDuration() != null ? splashScreenView.getIconAnimationDuration().toMillis() : 0, uptimeMillis) - uptimeMillis;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "applyExitAnimation delayed: %s", new Object[]{Long.valueOf(showingDuration)});
        if (showingDuration > 0) {
            splashScreenView.postDelayed(splashscreenContentDrawer$$ExternalSyntheticLambda9, showingDuration);
        } else {
            splashscreenContentDrawer$$ExternalSyntheticLambda9.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyExitAnimation$11$com-android-wm-shell-startingsurface-SplashscreenContentDrawer */
    public /* synthetic */ void mo51117xe85b2b62(SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable) {
        new SplashScreenExitAnimation(this.mContext, splashScreenView, surfaceControl, rect, this.mMainWindowShiftLength, this.mTransactionPool, runnable).startAnimations();
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$HighResIconProvider */
    private static class HighResIconProvider {
        /* access modifiers changed from: private */
        public boolean mLoadInDetail;
        private final Context mSharedContext;
        private final IconProvider mSharedIconProvider;
        private Context mStandaloneContext;
        private IconProvider mStandaloneIconProvider;

        HighResIconProvider(Context context, IconProvider iconProvider) {
            this.mSharedContext = context;
            this.mSharedIconProvider = iconProvider;
        }

        /* access modifiers changed from: package-private */
        public Drawable getIcon(ActivityInfo activityInfo, int i, int i2) {
            Drawable drawable;
            this.mLoadInDetail = false;
            if (i >= i2 || i >= 320) {
                drawable = this.mSharedIconProvider.getIcon(activityInfo, i2);
            } else {
                drawable = loadFromStandalone(activityInfo, i, i2);
            }
            return drawable == null ? this.mSharedContext.getPackageManager().getDefaultActivityIcon() : drawable;
        }

        private Drawable loadFromStandalone(ActivityInfo activityInfo, int i, int i2) {
            Resources resources;
            if (this.mStandaloneContext == null) {
                this.mStandaloneContext = this.mSharedContext.createConfigurationContext(this.mSharedContext.getResources().getConfiguration());
                this.mStandaloneIconProvider = new IconProvider(this.mStandaloneContext);
            }
            try {
                resources = this.mStandaloneContext.getPackageManager().getResourcesForApplication(activityInfo.applicationInfo);
            } catch (PackageManager.NameNotFoundException | Resources.NotFoundException unused) {
                resources = null;
            }
            if (resources != null) {
                updateResourcesDpi(resources, i2);
            }
            Drawable icon = this.mStandaloneIconProvider.getIcon(activityInfo, i2);
            this.mLoadInDetail = true;
            if (resources != null) {
                updateResourcesDpi(resources, i);
            }
            return icon;
        }

        private void updateResourcesDpi(Resources resources, int i) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.densityDpi = i;
            displayMetrics.densityDpi = i;
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }
}
