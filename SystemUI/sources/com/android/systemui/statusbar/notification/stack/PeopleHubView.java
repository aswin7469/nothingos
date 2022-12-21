package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1893R;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.people.DataListener;
import com.android.systemui.statusbar.notification.people.PersonViewModel;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002:\u00010B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u0014J\n\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010!\u001a\u00020\"H\u0014J\n\u0010#\u001a\u0004\u0018\u00010\"H\u0014J\b\u0010$\u001a\u00020\tH\u0016J\b\u0010%\u001a\u00020\tH\u0016J\b\u0010&\u001a\u00020\u001bH\u0014J\b\u0010'\u001a\u00020\u001bH\u0016J\u000e\u0010(\u001a\u00020\u001b2\u0006\u0010)\u001a\u00020*J\u000e\u0010+\u001a\u00020\u001b2\u0006\u0010,\u001a\u00020-J\u0010\u0010.\u001a\u00020\u001b2\u0006\u0010/\u001a\u00020\u001dH\u0016R$\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X.¢\u0006\u0002\n\u0000R:\u0010\u0017\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u00150\u00142\u0014\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u00150\u0014@BX.¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u00061"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/PeopleHubView;", "Lcom/android/systemui/statusbar/notification/row/StackScrollerDecorView;", "Lcom/android/systemui/statusbar/notification/stack/SwipeableView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "value", "", "canSwipe", "getCanSwipe", "()Z", "setCanSwipe", "(Z)V", "contents", "Landroid/view/ViewGroup;", "label", "Landroid/widget/TextView;", "<set-?>", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "personViewAdapters", "getPersonViewAdapters", "()Lkotlin/sequences/Sequence;", "applyContentTransformation", "", "contentAlpha", "", "translationY", "createMenu", "Lcom/android/systemui/plugins/statusbar/NotificationMenuRowPlugin;", "findContentView", "Landroid/view/View;", "findSecondaryView", "hasFinishedInitialization", "needsClippingToShelf", "onFinishInflate", "resetTranslation", "setOnHeaderClickListener", "listener", "Landroid/view/View$OnClickListener;", "setTextColor", "color", "", "setTranslation", "translation", "PersonDataListenerImpl", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubView.kt */
public final class PeopleHubView extends StackScrollerDecorView implements SwipeableView {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private boolean canSwipe;
    /* access modifiers changed from: private */
    public ViewGroup contents;
    private TextView label;
    private Sequence<? extends DataListener<? super PersonViewModel>> personViewAdapters;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public NotificationMenuRowPlugin createMenu() {
        return null;
    }

    /* access modifiers changed from: protected */
    public View findSecondaryView() {
        return null;
    }

    public boolean hasFinishedInitialization() {
        return true;
    }

    public boolean needsClippingToShelf() {
        return true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PeopleHubView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    public final Sequence<DataListener<PersonViewModel>> getPersonViewAdapters() {
        Sequence<? extends DataListener<? super PersonViewModel>> sequence = this.personViewAdapters;
        if (sequence != null) {
            return sequence;
        }
        Intrinsics.throwUninitializedPropertyAccessException("personViewAdapters");
        return null;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        View requireViewById = requireViewById(C1893R.C1897id.people_list);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(R.id.people_list)");
        this.contents = (ViewGroup) requireViewById;
        View requireViewById2 = requireViewById(C1893R.C1897id.header_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.header_label)");
        this.label = (TextView) requireViewById2;
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            viewGroup = null;
        }
        this.personViewAdapters = CollectionsKt.asSequence(SequencesKt.toList(SequencesKt.mapNotNull(CollectionsKt.asSequence(RangesKt.until(0, viewGroup.getChildCount())), new PeopleHubView$onFinishInflate$1(this))));
        super.onFinishInflate();
        setVisible(true, false);
    }

    public final void setTextColor(int i) {
        TextView textView = this.label;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(BaseIconCache.IconDB.COLUMN_LABEL);
            textView = null;
        }
        textView.setTextColor(i);
    }

    /* access modifiers changed from: protected */
    public View findContentView() {
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            viewGroup = null;
        }
        return viewGroup;
    }

    public void resetTranslation() {
        setTranslationX(0.0f);
    }

    public void setTranslation(float f) {
        if (this.canSwipe) {
            super.setTranslation(f);
        }
    }

    public final boolean getCanSwipe() {
        return this.canSwipe;
    }

    public final void setCanSwipe(boolean z) {
        boolean z2 = this.canSwipe;
        if (z2 != z) {
            if (z2) {
                resetTranslation();
            }
            this.canSwipe = z;
        }
    }

    /* access modifiers changed from: protected */
    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            viewGroup = null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup viewGroup2 = this.contents;
            if (viewGroup2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("contents");
                viewGroup2 = null;
            }
            View childAt = viewGroup2.getChildAt(i);
            childAt.setAlpha(f);
            childAt.setTranslationY(f2);
        }
    }

    public final void setOnHeaderClickListener(View.OnClickListener onClickListener) {
        Intrinsics.checkNotNullParameter(onClickListener, "listener");
        TextView textView = this.label;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(BaseIconCache.IconDB.COLUMN_LABEL);
            textView = null;
        }
        textView.setOnClickListener(onClickListener);
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/PeopleHubView$PersonDataListenerImpl;", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "avatarView", "Landroid/widget/ImageView;", "(Lcom/android/systemui/statusbar/notification/stack/PeopleHubView;Landroid/widget/ImageView;)V", "getAvatarView", "()Landroid/widget/ImageView;", "onDataChanged", "", "data", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PeopleHubView.kt */
    private final class PersonDataListenerImpl implements DataListener<PersonViewModel> {
        private final ImageView avatarView;
        final /* synthetic */ PeopleHubView this$0;

        public PersonDataListenerImpl(PeopleHubView peopleHubView, ImageView imageView) {
            Intrinsics.checkNotNullParameter(imageView, "avatarView");
            this.this$0 = peopleHubView;
            this.avatarView = imageView;
        }

        public final ImageView getAvatarView() {
            return this.avatarView;
        }

        public void onDataChanged(PersonViewModel personViewModel) {
            this.avatarView.setVisibility(personViewModel != null ? 0 : 8);
            this.avatarView.setImageDrawable(personViewModel != null ? personViewModel.getIcon() : null);
            this.avatarView.setOnClickListener(new PeopleHubView$PersonDataListenerImpl$$ExternalSyntheticLambda0(personViewModel));
        }

        /* access modifiers changed from: private */
        /* renamed from: onDataChanged$lambda-1  reason: not valid java name */
        public static final void m3151onDataChanged$lambda1(PersonViewModel personViewModel, View view) {
            Function0<Unit> onClick;
            if (personViewModel != null && (onClick = personViewModel.getOnClick()) != null) {
                onClick.invoke();
            }
        }
    }
}
