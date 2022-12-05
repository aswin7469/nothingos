package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
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
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BadgedImageView;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BubbleOverflow.kt */
/* loaded from: classes2.dex */
public final class BubbleOverflow implements BubbleViewProvider {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private Bitmap bitmap;
    @NotNull
    private final Context context;
    private int dotColor;
    private Path dotPath;
    @NotNull
    private final LayoutInflater inflater;
    private int overflowIconInset;
    @NotNull
    private final BubblePositioner positioner;
    private boolean showDot;
    @Nullable
    private BubbleExpandedView expandedView = null;
    @Nullable
    private BadgedImageView overflowBtn = null;

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @Nullable
    public Bitmap getAppBadge() {
        return null;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @NotNull
    public String getKey() {
        return "Overflow";
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public void setTaskViewVisibility(boolean z) {
    }

    public BubbleOverflow(@NotNull Context context, @NotNull BubblePositioner positioner) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(positioner, "positioner");
        this.context = context;
        this.positioner = positioner;
        LayoutInflater from = LayoutInflater.from(context);
        Intrinsics.checkNotNullExpressionValue(from, "from(context)");
        this.inflater = from;
        updateResources();
    }

    public final void initialize(@NotNull BubbleController controller) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        BubbleExpandedView expandedView = getExpandedView();
        if (expandedView == null) {
            return;
        }
        expandedView.initialize(controller, controller.getStackView(), true);
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
        BubbleExpandedView expandedView = getExpandedView();
        if (expandedView != null) {
            expandedView.applyThemeAttrs();
        }
        BadgedImageView mo1645getIconView = mo1645getIconView();
        if (mo1645getIconView != null) {
            mo1645getIconView.setImageResource(R.drawable.bubble_ic_overflow_button);
        }
        updateBtnTheme();
    }

    public final void updateResources() {
        this.overflowIconInset = this.context.getResources().getDimensionPixelSize(R.dimen.bubble_overflow_icon_inset);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setLayoutParams(new FrameLayout.LayoutParams(this.positioner.getBubbleSize(), this.positioner.getBubbleSize()));
        }
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView == null) {
            return;
        }
        bubbleExpandedView.updateDimensions();
    }

    private final void updateBtnTheme() {
        Drawable drawable;
        Resources resources = this.context.getResources();
        TypedValue typedValue = new TypedValue();
        this.context.getTheme().resolveAttribute(17956900, typedValue, true);
        int color = resources.getColor(typedValue.resourceId, null);
        this.dotColor = color;
        int color2 = resources.getColor(17170499);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null && (drawable = badgedImageView.getDrawable()) != null) {
            drawable.setTint(color2);
        }
        BubbleIconFactory bubbleIconFactory = new BubbleIconFactory(this.context);
        BadgedImageView badgedImageView2 = this.overflowBtn;
        Bitmap bitmap = bubbleIconFactory.createBadgedIconBitmap(new AdaptiveIconDrawable(new ColorDrawable(color), new InsetDrawable(badgedImageView2 == null ? null : badgedImageView2.getDrawable(), this.overflowIconInset)), null, true).icon;
        Intrinsics.checkNotNullExpressionValue(bitmap, "iconFactory.createBadgedIconBitmap(AdaptiveIconDrawable(\n                ColorDrawable(colorAccent), fg),\n            null /* user */, true /* shrinkNonAdaptiveIcons */).icon");
        this.bitmap = bitmap;
        Path createPathFromPathData = PathParser.createPathFromPathData(resources.getString(17039952));
        Intrinsics.checkNotNullExpressionValue(createPathFromPathData, "createPathFromPathData(\n            res.getString(com.android.internal.R.string.config_icon_mask))");
        this.dotPath = createPathFromPathData;
        IconNormalizer normalizer = bubbleIconFactory.getNormalizer();
        BadgedImageView mo1645getIconView = mo1645getIconView();
        Intrinsics.checkNotNull(mo1645getIconView);
        float scale = normalizer.getScale(mo1645getIconView.getDrawable(), null, null, null);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale, 50.0f, 50.0f);
        Path path = this.dotPath;
        if (path == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dotPath");
            throw null;
        }
        path.transform(matrix);
        BadgedImageView badgedImageView3 = this.overflowBtn;
        if (badgedImageView3 != null) {
            badgedImageView3.setRenderedBubble(this);
        }
        BadgedImageView badgedImageView4 = this.overflowBtn;
        if (badgedImageView4 == null) {
            return;
        }
        badgedImageView4.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
    }

    public final void setVisible(int i) {
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView == null) {
            return;
        }
        badgedImageView.setVisibility(i);
    }

    public final void setShowDot(boolean z) {
        this.showDot = z;
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView == null) {
            return;
        }
        badgedImageView.updateDotVisibility(true);
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @Nullable
    public BubbleExpandedView getExpandedView() {
        if (this.expandedView == null) {
            View inflate = this.inflater.inflate(R.layout.bubble_expanded_view, (ViewGroup) null, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.wm.shell.bubbles.BubbleExpandedView");
            BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) inflate;
            this.expandedView = bubbleExpandedView;
            bubbleExpandedView.applyThemeAttrs();
            updateResources();
        }
        return this.expandedView;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public int getDotColor() {
        return this.dotColor;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @NotNull
    public Bitmap getBubbleIcon() {
        Bitmap bitmap = this.bitmap;
        if (bitmap != null) {
            return bitmap;
        }
        Intrinsics.throwUninitializedPropertyAccessException("bitmap");
        throw null;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public boolean showDot() {
        return this.showDot;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @Nullable
    public Path getDotPath() {
        Path path = this.dotPath;
        if (path != null) {
            return path;
        }
        Intrinsics.throwUninitializedPropertyAccessException("dotPath");
        throw null;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public void setExpandedContentAlpha(float f) {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView == null) {
            return;
        }
        bubbleExpandedView.setAlpha(f);
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    @Nullable
    /* renamed from: getIconView  reason: collision with other method in class */
    public BadgedImageView mo1645getIconView() {
        if (this.overflowBtn == null) {
            View inflate = this.inflater.inflate(R.layout.bubble_overflow_button, (ViewGroup) null, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.wm.shell.bubbles.BadgedImageView");
            BadgedImageView badgedImageView = (BadgedImageView) inflate;
            this.overflowBtn = badgedImageView;
            badgedImageView.initialize(this.positioner);
            BadgedImageView badgedImageView2 = this.overflowBtn;
            if (badgedImageView2 != null) {
                badgedImageView2.setContentDescription(this.context.getResources().getString(R.string.bubble_overflow_button_content_description));
            }
            int bubbleSize = this.positioner.getBubbleSize();
            BadgedImageView badgedImageView3 = this.overflowBtn;
            if (badgedImageView3 != null) {
                badgedImageView3.setLayoutParams(new FrameLayout.LayoutParams(bubbleSize, bubbleSize));
            }
            updateBtnTheme();
        }
        return this.overflowBtn;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            Intrinsics.checkNotNull(bubbleExpandedView);
            return bubbleExpandedView.getTaskId();
        }
        return -1;
    }

    /* compiled from: BubbleOverflow.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
