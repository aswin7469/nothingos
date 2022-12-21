package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.PathParser;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.launcher3.icons.IconNormalizer;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.bubbles.BadgedImageView;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 .2\u00020\u0001:\u0001.B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u0016\u001a\u00020\u0017J\b\u0010\u0018\u001a\u0004\u0018\u00010\u000eJ\n\u0010\u0019\u001a\u0004\u0018\u00010\bH\u0016J\b\u0010\u001a\u001a\u00020\bH\u0016J\b\u0010\u001b\u001a\u00020\nH\u0016J\n\u0010\u001c\u001a\u0004\u0018\u00010\fH\u0016J\n\u0010\u001d\u001a\u0004\u0018\u00010\u000eH\u0016J\n\u0010\u001e\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u001f\u001a\u00020 H\u0016J\n\u0010!\u001a\u0004\u0018\u00010\bH\u0016J\b\u0010\"\u001a\u00020\nH\u0016J\u000e\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020%J\u000e\u0010&\u001a\u00020\u00172\u0006\u0010'\u001a\u00020\u0015J\u0010\u0010(\u001a\u00020\u00172\u0006\u0010)\u001a\u00020\u0015H\u0016J\u000e\u0010*\u001a\u00020\u00172\u0006\u0010)\u001a\u00020\nJ\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0006\u0010+\u001a\u00020\u0017J\b\u0010,\u001a\u00020\u0017H\u0002J\u0006\u0010-\u001a\u00020\u0017R\u000e\u0010\u0007\u001a\u00020\bX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000¨\u0006/"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/BubbleOverflow;", "Lcom/android/wm/shell/bubbles/BubbleViewProvider;", "context", "Landroid/content/Context;", "positioner", "Lcom/android/wm/shell/bubbles/BubblePositioner;", "(Landroid/content/Context;Lcom/android/wm/shell/bubbles/BubblePositioner;)V", "bitmap", "Landroid/graphics/Bitmap;", "dotColor", "", "dotPath", "Landroid/graphics/Path;", "expandedView", "Lcom/android/wm/shell/bubbles/BubbleExpandedView;", "inflater", "Landroid/view/LayoutInflater;", "overflowBtn", "Lcom/android/wm/shell/bubbles/BadgedImageView;", "overflowIconInset", "showDot", "", "cleanUpExpandedState", "", "createExpandedView", "getAppBadge", "getBubbleIcon", "getDotColor", "getDotPath", "getExpandedView", "getIconView", "getKey", "", "getRawAppBadge", "getTaskId", "initialize", "controller", "Lcom/android/wm/shell/bubbles/BubbleController;", "setShowDot", "show", "setTaskViewVisibility", "visible", "setVisible", "update", "updateBtnTheme", "updateResources", "Companion", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.BubbleOverflow */
/* compiled from: BubbleOverflow.kt */
public final class BubbleOverflow implements BubbleViewProvider {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String KEY = "Overflow";
    private Bitmap bitmap;
    private final Context context;
    private int dotColor;
    private Path dotPath;
    private BubbleExpandedView expandedView = null;
    private final LayoutInflater inflater;
    private BadgedImageView overflowBtn = null;
    private int overflowIconInset;
    private final BubblePositioner positioner;
    private boolean showDot;

    public Bitmap getAppBadge() {
        return null;
    }

    public String getKey() {
        return KEY;
    }

    public Bitmap getRawAppBadge() {
        return null;
    }

    public void setTaskViewVisibility(boolean z) {
    }

    public BubbleOverflow(Context context2, BubblePositioner bubblePositioner) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(bubblePositioner, "positioner");
        this.context = context2;
        this.positioner = bubblePositioner;
        LayoutInflater from = LayoutInflater.from(context2);
        Intrinsics.checkNotNullExpressionValue(from, "from(context)");
        this.inflater = from;
        updateResources();
    }

    public final void initialize(BubbleController bubbleController) {
        Intrinsics.checkNotNullParameter(bubbleController, "controller");
        createExpandedView();
        BubbleExpandedView expandedView2 = getExpandedView();
        if (expandedView2 != null) {
            expandedView2.initialize(bubbleController, bubbleController.getStackView(), true);
        }
    }

    public final void cleanUpExpandedState() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.cleanUpExpandedState();
        }
        this.expandedView = null;
    }

    public final void update() {
        updateResources();
        BubbleExpandedView expandedView2 = getExpandedView();
        if (expandedView2 != null) {
            expandedView2.applyThemeAttrs();
        }
        BadgedImageView iconView = getIconView();
        if (iconView != null) {
            iconView.setIconImageResource(C3343R.C3345drawable.bubble_ic_overflow_button);
        }
        updateBtnTheme();
    }

    public final void updateResources() {
        this.overflowIconInset = this.context.getResources().getDimensionPixelSize(C3343R.dimen.bubble_overflow_icon_inset);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setLayoutParams(new FrameLayout.LayoutParams(this.positioner.getBubbleSize(), this.positioner.getBubbleSize()));
        }
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.updateDimensions();
        }
    }

    private final void updateBtnTheme() {
        Drawable iconDrawable;
        Resources resources = this.context.getResources();
        TypedValue typedValue = new TypedValue();
        this.context.getTheme().resolveAttribute(17956900, typedValue, true);
        Path path = null;
        int color = resources.getColor(typedValue.resourceId, (Resources.Theme) null);
        this.dotColor = color;
        int color2 = resources.getColor(17170499);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (!(badgedImageView == null || (iconDrawable = badgedImageView.getIconDrawable()) == null)) {
            iconDrawable.setTint(color2);
        }
        BubbleIconFactory bubbleIconFactory = new BubbleIconFactory(this.context);
        BadgedImageView badgedImageView2 = this.overflowBtn;
        Bitmap bitmap2 = bubbleIconFactory.createBadgedIconBitmap(new AdaptiveIconDrawable(new ColorDrawable(color), new InsetDrawable(badgedImageView2 != null ? badgedImageView2.getIconDrawable() : null, this.overflowIconInset))).icon;
        Intrinsics.checkNotNullExpressionValue(bitmap2, "iconFactory.createBadged…e(colorAccent), fg)).icon");
        this.bitmap = bitmap2;
        Path createPathFromPathData = PathParser.createPathFromPathData(resources.getString(17039987));
        Intrinsics.checkNotNullExpressionValue(createPathFromPathData, "createPathFromPathData(\n…string.config_icon_mask))");
        this.dotPath = createPathFromPathData;
        IconNormalizer normalizer = bubbleIconFactory.getNormalizer();
        BadgedImageView iconView = getIconView();
        Intrinsics.checkNotNull(iconView);
        float scale = normalizer.getScale(iconView.getIconDrawable(), (RectF) null, (Path) null, (boolean[]) null);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale, 50.0f, 50.0f);
        Path path2 = this.dotPath;
        if (path2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dotPath");
        } else {
            path = path2;
        }
        path.transform(matrix);
        BadgedImageView badgedImageView3 = this.overflowBtn;
        if (badgedImageView3 != null) {
            badgedImageView3.setRenderedBubble(this);
        }
        BadgedImageView badgedImageView4 = this.overflowBtn;
        if (badgedImageView4 != null) {
            badgedImageView4.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        }
    }

    public final void setVisible(int i) {
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setVisibility(i);
        }
    }

    public final void setShowDot(boolean z) {
        this.showDot = z;
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.updateDotVisibility(true);
        }
    }

    public final BubbleExpandedView createExpandedView() {
        View inflate = this.inflater.inflate(C3343R.layout.bubble_expanded_view, (ViewGroup) null, false);
        if (inflate != null) {
            BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) inflate;
            this.expandedView = bubbleExpandedView;
            if (bubbleExpandedView != null) {
                bubbleExpandedView.applyThemeAttrs();
            }
            updateResources();
            return this.expandedView;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.bubbles.BubbleExpandedView");
    }

    public BubbleExpandedView getExpandedView() {
        return this.expandedView;
    }

    public int getDotColor() {
        return this.dotColor;
    }

    public Bitmap getBubbleIcon() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            return bitmap2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("bitmap");
        return null;
    }

    public boolean showDot() {
        return this.showDot;
    }

    public Path getDotPath() {
        Path path = this.dotPath;
        if (path != null) {
            return path;
        }
        Intrinsics.throwUninitializedPropertyAccessException("dotPath");
        return null;
    }

    public BadgedImageView getIconView() {
        if (this.overflowBtn == null) {
            View inflate = this.inflater.inflate(C3343R.layout.bubble_overflow_button, (ViewGroup) null, false);
            if (inflate != null) {
                BadgedImageView badgedImageView = (BadgedImageView) inflate;
                this.overflowBtn = badgedImageView;
                if (badgedImageView != null) {
                    badgedImageView.initialize(this.positioner);
                }
                BadgedImageView badgedImageView2 = this.overflowBtn;
                if (badgedImageView2 != null) {
                    badgedImageView2.setContentDescription(this.context.getResources().getString(C3343R.string.bubble_overflow_button_content_description));
                }
                int bubbleSize = this.positioner.getBubbleSize();
                BadgedImageView badgedImageView3 = this.overflowBtn;
                if (badgedImageView3 != null) {
                    badgedImageView3.setLayoutParams(new FrameLayout.LayoutParams(bubbleSize, bubbleSize));
                }
                updateBtnTheme();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.bubbles.BadgedImageView");
            }
        }
        return this.overflowBtn;
    }

    public int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView == null) {
            return -1;
        }
        Intrinsics.checkNotNull(bubbleExpandedView);
        return bubbleExpandedView.getTaskId();
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/BubbleOverflow$Companion;", "", "()V", "KEY", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.bubbles.BubbleOverflow$Companion */
    /* compiled from: BubbleOverflow.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
