package com.android.systemui.dreams.complication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.android.systemui.C1894R;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.touch.TouchInsetManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

@DreamOverlayComponent.DreamOverlayScope
public class ComplicationLayoutEngine implements Complication.VisibilityController {
    public static final String TAG = "ComplicationLayoutEngine";
    private final HashMap<ComplicationId, ViewEntry> mEntries = new HashMap<>();
    private final int mFadeInDuration;
    private final int mFadeOutDuration;
    /* access modifiers changed from: private */
    public final ConstraintLayout mLayout;
    private final int mMargin;
    private final HashMap<Integer, PositionGroup> mPositions = new HashMap<>();
    private final TouchInsetManager.TouchInsetSession mSession;
    private ViewPropertyAnimator mViewPropertyAnimator;

    private static class ViewEntry implements Comparable<ViewEntry> {
        private final int mCategory;
        private final ComplicationLayoutParams mLayoutParams;
        private final int mMargin;
        private final Parent mParent;
        private final TouchInsetManager.TouchInsetSession mTouchInsetSession;
        private final View mView;

        interface Parent {
            void removeEntry(ViewEntry viewEntry);
        }

        ViewEntry(View view, ComplicationLayoutParams complicationLayoutParams, TouchInsetManager.TouchInsetSession touchInsetSession, int i, Parent parent, int i2) {
            this.mView = view;
            view.setId(View.generateViewId());
            this.mLayoutParams = complicationLayoutParams;
            this.mTouchInsetSession = touchInsetSession;
            this.mCategory = i;
            this.mParent = parent;
            this.mMargin = i2;
            touchInsetSession.addViewToTracking(view);
        }

        /* access modifiers changed from: private */
        public View getView() {
            return this.mView;
        }

        public ComplicationLayoutParams getLayoutParams() {
            return this.mLayoutParams;
        }

        public void applyLayoutParams(View view) {
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(this.mLayoutParams.width, this.mLayoutParams.height);
            this.mLayoutParams.iteratePositions(new ComplicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0(this, view == this.mView, getLayoutParams().getDirection(), layoutParams, view, getLayoutParams().snapsToGuide()));
            this.mView.setLayoutParams(layoutParams);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$applyLayoutParams$0$com-android-systemui-dreams-complication-ComplicationLayoutEngine$ViewEntry */
        public /* synthetic */ void mo32591xd35d1ce(boolean z, int i, ConstraintLayout.LayoutParams layoutParams, View view, boolean z2, Integer num) {
            int intValue = num.intValue();
            if (intValue == 1) {
                if (z || i != 2) {
                    layoutParams.topToTop = 0;
                } else {
                    layoutParams.topToBottom = view.getId();
                }
                if (z2 && (i == 8 || i == 4)) {
                    layoutParams.endToStart = C1894R.C1898id.complication_top_guide;
                }
            } else if (intValue == 2) {
                if (z || i != 1) {
                    layoutParams.bottomToBottom = 0;
                } else {
                    layoutParams.bottomToTop = view.getId();
                }
                if (z2 && (i == 8 || i == 4)) {
                    layoutParams.topToBottom = C1894R.C1898id.complication_bottom_guide;
                }
            } else if (intValue == 4) {
                if (z || i != 8) {
                    layoutParams.startToStart = 0;
                } else {
                    layoutParams.startToEnd = view.getId();
                }
                if (z2 && (i == 2 || i == 1)) {
                    layoutParams.endToStart = C1894R.C1898id.complication_start_guide;
                }
            } else if (intValue == 8) {
                if (z || i != 4) {
                    layoutParams.endToEnd = 0;
                } else {
                    layoutParams.endToStart = view.getId();
                }
                if (z2 && (i == 1 || i == 2)) {
                    layoutParams.startToEnd = C1894R.C1898id.complication_end_guide;
                }
            }
            if (z) {
                return;
            }
            if (i == 1) {
                layoutParams.setMargins(0, 0, 0, this.mMargin);
            } else if (i == 2) {
                layoutParams.setMargins(0, this.mMargin, 0, 0);
            } else if (i == 4) {
                layoutParams.setMarginEnd(this.mMargin);
            } else if (i == 8) {
                layoutParams.setMarginStart(this.mMargin);
            }
        }

        private void setGuide(ConstraintLayout.LayoutParams layoutParams, int i, Consumer<ConstraintLayout.LayoutParams> consumer) {
            if (getLayoutParams().snapsToGuide()) {
                consumer.accept(layoutParams);
            }
        }

        public void remove() {
            this.mParent.removeEntry(this);
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            this.mTouchInsetSession.removeViewFromTracking(this.mView);
        }

        public int compareTo(ViewEntry viewEntry) {
            int i = viewEntry.mCategory;
            int i2 = this.mCategory;
            if (i != i2) {
                return i2 == 2 ? 1 : -1;
            }
            if (viewEntry.mLayoutParams.getWeight() == this.mLayoutParams.getWeight()) {
                return 0;
            }
            if (this.mLayoutParams.getWeight() > viewEntry.mLayoutParams.getWeight()) {
                return 1;
            }
            return -1;
        }

        private static class Builder {
            private final int mCategory;
            private final ComplicationLayoutParams mLayoutParams;
            private int mMargin;
            private Parent mParent;
            private final TouchInsetManager.TouchInsetSession mTouchSession;
            private final View mView;

            Builder(View view, TouchInsetManager.TouchInsetSession touchInsetSession, ComplicationLayoutParams complicationLayoutParams, int i) {
                this.mView = view;
                this.mLayoutParams = complicationLayoutParams;
                this.mCategory = i;
                this.mTouchSession = touchInsetSession;
            }

            public ComplicationLayoutParams getLayoutParams() {
                return this.mLayoutParams;
            }

            public int getCategory() {
                return this.mCategory;
            }

            /* access modifiers changed from: package-private */
            public Builder setParent(Parent parent) {
                this.mParent = parent;
                return this;
            }

            /* access modifiers changed from: package-private */
            public Builder setMargin(int i) {
                this.mMargin = i;
                return this;
            }

            /* access modifiers changed from: package-private */
            public ViewEntry build() {
                return new ViewEntry(this.mView, this.mLayoutParams, this.mTouchSession, this.mCategory, this.mParent, this.mMargin);
            }
        }
    }

    private static class PositionGroup implements DirectionGroup.Parent {
        private final HashMap<Integer, DirectionGroup> mDirectionGroups;

        private PositionGroup() {
            this.mDirectionGroups = new HashMap<>();
        }

        public ViewEntry add(ViewEntry.Builder builder) {
            int direction = builder.getLayoutParams().getDirection();
            if (!this.mDirectionGroups.containsKey(Integer.valueOf(direction))) {
                this.mDirectionGroups.put(Integer.valueOf(direction), new DirectionGroup(this));
            }
            return this.mDirectionGroups.get(Integer.valueOf(direction)).add(builder);
        }

        public void onEntriesChanged() {
            updateViews();
        }

        private void updateViews() {
            ViewEntry viewEntry = null;
            for (DirectionGroup head : this.mDirectionGroups.values()) {
                ViewEntry head2 = head.getHead();
                if (viewEntry == null || (head2 != null && head2.compareTo(viewEntry) > 0)) {
                    viewEntry = head2;
                }
            }
            if (viewEntry != null) {
                for (DirectionGroup updateViews : this.mDirectionGroups.values()) {
                    updateViews.updateViews(viewEntry.getView());
                }
            }
        }

        /* access modifiers changed from: private */
        public ArrayList<ViewEntry> getViews() {
            ArrayList<ViewEntry> arrayList = new ArrayList<>();
            for (DirectionGroup access$100 : this.mDirectionGroups.values()) {
                arrayList.addAll(access$100.getViews());
            }
            return arrayList;
        }
    }

    private static class DirectionGroup implements ViewEntry.Parent {
        private final Parent mParent;
        private final ArrayList<ViewEntry> mViews = new ArrayList<>();

        interface Parent {
            void onEntriesChanged();
        }

        DirectionGroup(Parent parent) {
            this.mParent = parent;
        }

        public ViewEntry getHead() {
            if (this.mViews.isEmpty()) {
                return null;
            }
            return this.mViews.get(0);
        }

        public ViewEntry add(ViewEntry.Builder builder) {
            ViewEntry build = builder.setParent(this).build();
            this.mViews.add(build);
            Collections.sort(this.mViews);
            Collections.reverse(this.mViews);
            this.mParent.onEntriesChanged();
            return build;
        }

        public void removeEntry(ViewEntry viewEntry) {
            this.mViews.remove((Object) viewEntry);
            this.mParent.onEntriesChanged();
        }

        public void updateViews(View view) {
            Iterator<ViewEntry> it = this.mViews.iterator();
            while (it.hasNext()) {
                ViewEntry next = it.next();
                next.applyLayoutParams(view);
                view = next.getView();
            }
        }

        /* access modifiers changed from: private */
        public List<ViewEntry> getViews() {
            return this.mViews;
        }
    }

    @Inject
    public ComplicationLayoutEngine(@Named("scoped_complications_layout") ConstraintLayout constraintLayout, @Named("complication_margin") int i, TouchInsetManager.TouchInsetSession touchInsetSession, @Named("complications_fade_in_duration") int i2, @Named("complications_fade_out_duration") int i3) {
        this.mLayout = constraintLayout;
        this.mMargin = i;
        this.mSession = touchInsetSession;
        this.mFadeInDuration = i2;
        this.mFadeOutDuration = i3;
    }

    public void setVisibility(final int i, boolean z) {
        boolean z2 = i == 0;
        ViewPropertyAnimator viewPropertyAnimator = this.mViewPropertyAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (z2) {
            this.mLayout.setVisibility(0);
        }
        this.mViewPropertyAnimator = this.mLayout.animate().alpha(z2 ? 1.0f : 0.0f).setDuration((long) (z2 ? this.mFadeInDuration : this.mFadeOutDuration)).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ComplicationLayoutEngine.this.mLayout.setVisibility(i);
            }
        });
    }

    public void addComplication(ComplicationId complicationId, View view, ComplicationLayoutParams complicationLayoutParams, int i) {
        Log.d(TAG, "engine: " + this + " addComplication");
        if (this.mEntries.containsKey(complicationId)) {
            removeComplication(complicationId);
        }
        ViewEntry.Builder margin = new ViewEntry.Builder(view, this.mSession, complicationLayoutParams, i).setMargin(this.mMargin);
        int position = complicationLayoutParams.getPosition();
        if (!this.mPositions.containsKey(Integer.valueOf(position))) {
            this.mPositions.put(Integer.valueOf(position), new PositionGroup());
        }
        ViewEntry add = this.mPositions.get(Integer.valueOf(position)).add(margin);
        this.mEntries.put(complicationId, add);
        this.mLayout.addView(add.getView());
    }

    public boolean removeComplication(ComplicationId complicationId) {
        ViewEntry remove = this.mEntries.remove(complicationId);
        if (remove == null) {
            Log.e(TAG, "could not find id:" + complicationId);
            return false;
        }
        remove.remove();
        return true;
    }

    public List<View> getViewsAtPosition(int i) {
        return (List) this.mPositions.entrySet().stream().filter(new ComplicationLayoutEngine$$ExternalSyntheticLambda0(i)).flatMap(new ComplicationLayoutEngine$$ExternalSyntheticLambda1()).map(new ComplicationLayoutEngine$$ExternalSyntheticLambda2()).collect(Collectors.toList());
    }

    static /* synthetic */ boolean lambda$getViewsAtPosition$0(int i, Map.Entry entry) {
        return (((Integer) entry.getKey()).intValue() & i) == i;
    }
}
