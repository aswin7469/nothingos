package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PeopleHubView.kt */
/* loaded from: classes.dex */
public final class PeopleHubView extends StackScrollerDecorView implements SwipeableView {
    private boolean canSwipe;
    private ViewGroup contents;
    private TextView label;
    private Sequence<?> personViewAdapters;

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    @Nullable
    public NotificationMenuRowPlugin createMenu() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    @Nullable
    protected View findSecondaryView() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public boolean hasFinishedInitialization() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return true;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleHubView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, android.view.View
    public void onFinishInflate() {
        IntRange until;
        Sequence asSequence;
        Sequence<?> asSequence2;
        View requireViewById = requireViewById(R$id.people_list);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(R.id.people_list)");
        this.contents = (ViewGroup) requireViewById;
        View requireViewById2 = requireViewById(R$id.header_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.header_label)");
        this.label = (TextView) requireViewById2;
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            throw null;
        }
        until = RangesKt___RangesKt.until(0, viewGroup.getChildCount());
        asSequence = CollectionsKt___CollectionsKt.asSequence(until);
        asSequence2 = CollectionsKt___CollectionsKt.asSequence(SequencesKt.toList(SequencesKt.mapNotNull(asSequence, new PeopleHubView$onFinishInflate$1(this))));
        this.personViewAdapters = asSequence2;
        super.onFinishInflate();
        setVisible(true, false);
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    @NotNull
    protected View findContentView() {
        ViewGroup viewGroup = this.contents;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contents");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public void resetTranslation() {
        setTranslationX(0.0f);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public void setTranslation(float f) {
        if (this.canSwipe) {
            super.setTranslation(f);
        }
    }

    public final boolean getCanSwipe() {
        return this.canSwipe;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            throw null;
        }
        int childCount = viewGroup.getChildCount();
        if (childCount <= 0) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i + 1;
            ViewGroup viewGroup2 = this.contents;
            if (viewGroup2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("contents");
                throw null;
            }
            View childAt = viewGroup2.getChildAt(i);
            childAt.setAlpha(f);
            childAt.setTranslationY(f2);
            if (i2 >= childCount) {
                return;
            }
            i = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PeopleHubView.kt */
    /* loaded from: classes.dex */
    public final class PersonDataListenerImpl {
        @NotNull
        private final ImageView avatarView;
        final /* synthetic */ PeopleHubView this$0;

        public PersonDataListenerImpl(@NotNull PeopleHubView this$0, ImageView avatarView) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(avatarView, "avatarView");
            this.this$0 = this$0;
            this.avatarView = avatarView;
        }
    }
}
