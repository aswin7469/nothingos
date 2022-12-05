package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.R$styleable;
import com.android.systemui.statusbar.notification.NotificationUtils;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
/* loaded from: classes2.dex */
public class SmartReplyView extends ViewGroup {
    private final BreakIterator mBreakIterator;
    private PriorityQueue<Button> mCandidateButtonQueueForSqueezing;
    private int mCurrentBackgroundColor;
    private boolean mCurrentColorized;
    private int mCurrentRippleColor;
    private int mCurrentStrokeColor;
    private int mCurrentTextColor;
    private final int mDefaultBackgroundColor;
    private final int mDefaultStrokeColor;
    private int mMaxNumActions;
    private int mMaxSqueezeRemeasureAttempts;
    private int mMinNumSystemGeneratedReplies;
    private final double mMinStrokeContrast;
    private final int mRippleColor;
    private final int mRippleColorDarkBg;
    private View mSmartReplyContainer;
    private final int mSpacing;
    private final int mStrokeWidth;
    private static final int MEASURE_SPEC_ANY_LENGTH = View.MeasureSpec.makeMeasureSpec(0, 0);
    private static final Comparator<View> DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR = SmartReplyView$$ExternalSyntheticLambda0.INSTANCE;
    private boolean mSmartRepliesGeneratedByAssistant = false;
    private final int mHeightUpperLimit = NotificationUtils.getFontScaledHeight(((ViewGroup) this).mContext, R$dimen.smart_reply_button_max_height);
    private final int mDefaultTextColor = ((ViewGroup) this).mContext.getColor(R$color.smart_reply_button_text);
    private final int mDefaultTextColorDarkBg = ((ViewGroup) this).mContext.getColor(R$color.smart_reply_button_text_dark_bg);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public enum SmartButtonType {
        REPLY,
        ACTION
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(View view, View view2) {
        return ((view2.getMeasuredWidth() - view2.getPaddingLeft()) - view2.getPaddingRight()) - ((view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight());
    }

    public SmartReplyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int color = context.getColor(R$color.smart_reply_button_background);
        this.mDefaultBackgroundColor = color;
        int color2 = ((ViewGroup) this).mContext.getColor(R$color.smart_reply_button_stroke);
        this.mDefaultStrokeColor = color2;
        int color3 = ((ViewGroup) this).mContext.getColor(R$color.notification_ripple_untinted_color);
        this.mRippleColor = color3;
        this.mRippleColorDarkBg = Color.argb(Color.alpha(color3), 255, 255, 255);
        this.mMinStrokeContrast = ContrastColorUtil.calculateContrast(color2, color);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SmartReplyView, 0, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == R$styleable.SmartReplyView_spacing) {
                i2 = obtainStyledAttributes.getDimensionPixelSize(i3, 0);
            } else if (index == R$styleable.SmartReplyView_buttonStrokeWidth) {
                i = obtainStyledAttributes.getDimensionPixelSize(i3, 0);
            }
        }
        obtainStyledAttributes.recycle();
        this.mStrokeWidth = i;
        this.mSpacing = i2;
        this.mBreakIterator = BreakIterator.getLineInstance();
        setBackgroundTintColor(this.mDefaultBackgroundColor, false);
        reallocateCandidateButtonQueueForSqueezing();
    }

    public static SmartReplyView inflate(Context context, SmartReplyConstants smartReplyConstants) {
        SmartReplyView smartReplyView = (SmartReplyView) LayoutInflater.from(context).inflate(R$layout.smart_reply_view, (ViewGroup) null);
        smartReplyView.setMaxNumActions(smartReplyConstants.getMaxNumActions());
        smartReplyView.setMaxSqueezeRemeasureAttempts(smartReplyConstants.getMaxSqueezeRemeasureAttempts());
        smartReplyView.setMinNumSystemGeneratedReplies(smartReplyConstants.getMinNumSystemGeneratedReplies());
        return smartReplyView;
    }

    public int getHeightUpperLimit() {
        return this.mHeightUpperLimit;
    }

    private void reallocateCandidateButtonQueueForSqueezing() {
        this.mCandidateButtonQueueForSqueezing = new PriorityQueue<>(Math.max(getChildCount(), 1), DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR);
    }

    public void resetSmartSuggestions(View view) {
        this.mSmartReplyContainer = view;
        removeAllViews();
        setBackgroundTintColor(this.mDefaultBackgroundColor, false);
    }

    public void addPreInflatedButtons(List<Button> list) {
        for (Button button : list) {
            addView(button);
            setButtonColors(button);
        }
        reallocateCandidateButtonQueueForSqueezing();
    }

    public void setMaxNumActions(int i) {
        this.mMaxNumActions = i;
    }

    public void setMinNumSystemGeneratedReplies(int i) {
        this.mMinNumSystemGeneratedReplies = i;
    }

    public void setMaxSqueezeRemeasureAttempts(int i) {
        this.mMaxSqueezeRemeasureAttempts = i;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(((ViewGroup) this).mContext, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    private void clearLayoutLineCount(View view) {
        if (view instanceof TextView) {
            ((TextView) view).nullLayouts();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getMode(i) == 0 ? Integer.MAX_VALUE : View.MeasureSpec.getSize(i);
        resetButtonsLayoutParams();
        if (!this.mCandidateButtonQueueForSqueezing.isEmpty()) {
            Log.wtf("SmartReplyView", "Single line button queue leaked between onMeasure calls");
            this.mCandidateButtonQueueForSqueezing.clear();
        }
        SmartSuggestionMeasures smartSuggestionMeasures = new SmartSuggestionMeasures(((ViewGroup) this).mPaddingLeft + ((ViewGroup) this).mPaddingRight, 0);
        List<View> filterActionsOrReplies = filterActionsOrReplies(SmartButtonType.ACTION);
        List<View> filterActionsOrReplies2 = filterActionsOrReplies(SmartButtonType.REPLY);
        ArrayList<View> arrayList = new ArrayList(filterActionsOrReplies);
        arrayList.addAll(filterActionsOrReplies2);
        ArrayList arrayList2 = new ArrayList();
        SmartSuggestionMeasures smartSuggestionMeasures2 = null;
        int i3 = this.mMaxNumActions;
        int i4 = 0;
        int i5 = 0;
        for (View view : arrayList) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (i3 == -1 || layoutParams.mButtonType != SmartButtonType.ACTION || i4 < i3) {
                clearLayoutLineCount(view);
                view.measure(MEASURE_SPEC_ANY_LENGTH, i2);
                arrayList2.add(view);
                Button button = (Button) view;
                int lineCount = button.getLineCount();
                if (lineCount >= 1 && lineCount <= 2) {
                    if (lineCount == 1) {
                        this.mCandidateButtonQueueForSqueezing.add(button);
                    }
                    SmartSuggestionMeasures m1329clone = smartSuggestionMeasures.m1329clone();
                    if (smartSuggestionMeasures2 == null && layoutParams.mButtonType == SmartButtonType.REPLY) {
                        smartSuggestionMeasures2 = smartSuggestionMeasures.m1329clone();
                    }
                    int i6 = i5 == 0 ? 0 : this.mSpacing;
                    int measuredWidth = view.getMeasuredWidth();
                    int measuredHeight = view.getMeasuredHeight();
                    smartSuggestionMeasures.mMeasuredWidth += i6 + measuredWidth;
                    smartSuggestionMeasures.mMaxChildHeight = Math.max(smartSuggestionMeasures.mMaxChildHeight, measuredHeight);
                    if (smartSuggestionMeasures.mMeasuredWidth > size) {
                        while (smartSuggestionMeasures.mMeasuredWidth > size && !this.mCandidateButtonQueueForSqueezing.isEmpty()) {
                            Button poll = this.mCandidateButtonQueueForSqueezing.poll();
                            int squeezeButton = squeezeButton(poll, i2);
                            if (squeezeButton != -1) {
                                smartSuggestionMeasures.mMaxChildHeight = Math.max(smartSuggestionMeasures.mMaxChildHeight, poll.getMeasuredHeight());
                                smartSuggestionMeasures.mMeasuredWidth -= squeezeButton;
                            }
                        }
                        if (smartSuggestionMeasures.mMeasuredWidth > size) {
                            markButtonsWithPendingSqueezeStatusAs(3, arrayList2);
                            smartSuggestionMeasures = m1329clone;
                        } else {
                            markButtonsWithPendingSqueezeStatusAs(2, arrayList2);
                        }
                    }
                    layoutParams.show = true;
                    i5++;
                    if (layoutParams.mButtonType == SmartButtonType.ACTION) {
                        i4++;
                    }
                }
            }
        }
        if (this.mSmartRepliesGeneratedByAssistant && !gotEnoughSmartReplies(filterActionsOrReplies2)) {
            for (View view2 : filterActionsOrReplies2) {
                ((LayoutParams) view2.getLayoutParams()).show = false;
            }
            smartSuggestionMeasures = smartSuggestionMeasures2;
        }
        this.mCandidateButtonQueueForSqueezing.clear();
        remeasureButtonsIfNecessary(smartSuggestionMeasures.mMaxChildHeight);
        setMeasuredDimension(ViewGroup.resolveSize(Math.max(getSuggestedMinimumWidth(), smartSuggestionMeasures.mMeasuredWidth), i), ViewGroup.resolveSize(Math.max(getSuggestedMinimumHeight(), ((ViewGroup) this).mPaddingTop + smartSuggestionMeasures.mMaxChildHeight + ((ViewGroup) this).mPaddingBottom), i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSmartRepliesGeneratedByAssistant(boolean z) {
        this.mSmartRepliesGeneratedByAssistant = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideSmartSuggestions() {
        View view = this.mSmartReplyContainer;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    /* loaded from: classes2.dex */
    private static class SmartSuggestionMeasures {
        int mMaxChildHeight;
        int mMeasuredWidth;

        SmartSuggestionMeasures(int i, int i2) {
            this.mMeasuredWidth = -1;
            this.mMaxChildHeight = -1;
            this.mMeasuredWidth = i;
            this.mMaxChildHeight = i2;
        }

        /* renamed from: clone */
        public SmartSuggestionMeasures m1329clone() {
            return new SmartSuggestionMeasures(this.mMeasuredWidth, this.mMaxChildHeight);
        }
    }

    private boolean gotEnoughSmartReplies(List<View> list) {
        int i = 0;
        for (View view : list) {
            if (((LayoutParams) view.getLayoutParams()).show) {
                i++;
            }
        }
        return i == 0 || i >= this.mMinNumSystemGeneratedReplies;
    }

    private List<View> filterActionsOrReplies(SmartButtonType smartButtonType) {
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() == 0 && (childAt instanceof Button) && layoutParams.mButtonType == smartButtonType) {
                arrayList.add(childAt);
            }
        }
        return arrayList;
    }

    private void resetButtonsLayoutParams() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            layoutParams.show = false;
            layoutParams.squeezeStatus = 0;
        }
    }

    private int squeezeButton(Button button, int i) {
        int estimateOptimalSqueezedButtonTextWidth = estimateOptimalSqueezedButtonTextWidth(button);
        if (estimateOptimalSqueezedButtonTextWidth == -1) {
            return -1;
        }
        return squeezeButtonToTextWidth(button, i, estimateOptimalSqueezedButtonTextWidth);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0085, code lost:
        r6 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int estimateOptimalSqueezedButtonTextWidth(Button button) {
        String charSequence = button.getText().toString();
        TransformationMethod transformationMethod = button.getTransformationMethod();
        if (transformationMethod != null) {
            charSequence = transformationMethod.getTransformation(charSequence, button).toString();
        }
        int length = charSequence.length();
        this.mBreakIterator.setText(charSequence);
        if (this.mBreakIterator.preceding(length / 2) == -1 && this.mBreakIterator.next() == -1) {
            return -1;
        }
        TextPaint paint = button.getPaint();
        int current = this.mBreakIterator.current();
        float desiredWidth = Layout.getDesiredWidth(charSequence, 0, current, paint);
        float desiredWidth2 = Layout.getDesiredWidth(charSequence, current, length, paint);
        float max = Math.max(desiredWidth, desiredWidth2);
        int i = (desiredWidth > desiredWidth2 ? 1 : (desiredWidth == desiredWidth2 ? 0 : -1));
        if (i != 0) {
            boolean z = i > 0;
            int i2 = this.mMaxSqueezeRemeasureAttempts;
            int i3 = 0;
            while (true) {
                if (i3 >= i2) {
                    break;
                }
                BreakIterator breakIterator = this.mBreakIterator;
                int previous = z ? breakIterator.previous() : breakIterator.next();
                if (previous == -1) {
                    break;
                }
                float desiredWidth3 = Layout.getDesiredWidth(charSequence, 0, previous, paint);
                float desiredWidth4 = Layout.getDesiredWidth(charSequence, previous, length, paint);
                float max2 = Math.max(desiredWidth3, desiredWidth4);
                if (max2 >= max) {
                    break;
                }
                boolean z2 = z ? false : false;
                if (z2) {
                    max = max2;
                    break;
                }
                i3++;
                max = max2;
            }
        }
        return (int) Math.ceil(max);
    }

    private int getLeftCompoundDrawableWidthWithPadding(Button button) {
        Drawable drawable = button.getCompoundDrawables()[0];
        if (drawable == null) {
            return 0;
        }
        return drawable.getBounds().width() + button.getCompoundDrawablePadding();
    }

    private int squeezeButtonToTextWidth(Button button, int i, int i2) {
        int measuredWidth = button.getMeasuredWidth();
        clearLayoutLineCount(button);
        button.measure(View.MeasureSpec.makeMeasureSpec(button.getPaddingLeft() + button.getPaddingRight() + i2 + getLeftCompoundDrawableWidthWithPadding(button), Integer.MIN_VALUE), i);
        int measuredWidth2 = button.getMeasuredWidth();
        LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
        if (button.getLineCount() > 2 || measuredWidth2 >= measuredWidth) {
            layoutParams.squeezeStatus = 3;
            return -1;
        }
        layoutParams.squeezeStatus = 1;
        return measuredWidth - measuredWidth2;
    }

    private void remeasureButtonsIfNecessary(int i) {
        boolean z;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.show) {
                int measuredWidth = childAt.getMeasuredWidth();
                boolean z2 = true;
                if (layoutParams.squeezeStatus == 3) {
                    measuredWidth = Integer.MAX_VALUE;
                    z = true;
                } else {
                    z = false;
                }
                if (childAt.getMeasuredHeight() == i) {
                    z2 = z;
                }
                if (z2) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, Integer.MIN_VALUE), makeMeasureSpec);
                }
            }
        }
    }

    private void markButtonsWithPendingSqueezeStatusAs(int i, List<View> list) {
        for (View view : list) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (layoutParams.squeezeStatus == 1) {
                layoutParams.squeezeStatus = i;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2 = true;
        if (getLayoutDirection() != 1) {
            z2 = false;
        }
        int i5 = z2 ? (i3 - i) - ((ViewGroup) this).mPaddingRight : ((ViewGroup) this).mPaddingLeft;
        int childCount = getChildCount();
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (((LayoutParams) childAt.getLayoutParams()).show) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i7 = z2 ? i5 - measuredWidth : i5;
                childAt.layout(i7, 0, i7 + measuredWidth, measuredHeight);
                int i8 = measuredWidth + this.mSpacing;
                i5 = z2 ? i5 - i8 : i5 + i8;
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        return ((LayoutParams) view.getLayoutParams()).show && super.drawChild(canvas, view, j);
    }

    public void setBackgroundTintColor(int i, boolean z) {
        if (i == this.mCurrentBackgroundColor && z == this.mCurrentColorized) {
            return;
        }
        this.mCurrentBackgroundColor = i;
        this.mCurrentColorized = z;
        boolean isColorDark = Notification.Builder.isColorDark(i);
        int i2 = i | (-16777216);
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(isColorDark ? this.mDefaultTextColorDarkBg : this.mDefaultTextColor, i2, isColorDark);
        this.mCurrentTextColor = ensureTextContrast;
        if (!z) {
            ensureTextContrast = ContrastColorUtil.ensureContrast(this.mDefaultStrokeColor, i2, isColorDark, this.mMinStrokeContrast);
        }
        this.mCurrentStrokeColor = ensureTextContrast;
        this.mCurrentRippleColor = isColorDark ? this.mRippleColorDarkBg : this.mRippleColor;
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            setButtonColors((Button) getChildAt(i3));
        }
    }

    private void setButtonColors(Button button) {
        Drawable background = button.getBackground();
        if (background instanceof RippleDrawable) {
            Drawable mutate = background.mutate();
            RippleDrawable rippleDrawable = (RippleDrawable) mutate;
            rippleDrawable.setColor(ColorStateList.valueOf(this.mCurrentRippleColor));
            Drawable drawable = rippleDrawable.getDrawable(0);
            if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) drawable2;
                    gradientDrawable.setColor(this.mCurrentBackgroundColor);
                    gradientDrawable.setStroke(this.mStrokeWidth, this.mCurrentStrokeColor);
                }
            }
            button.setBackground(mutate);
        }
        button.setTextColor(this.mCurrentTextColor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        SmartButtonType mButtonType;
        private boolean show;
        private int squeezeStatus;

        private LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.show = false;
            this.squeezeStatus = 0;
            this.mButtonType = SmartButtonType.REPLY;
        }

        private LayoutParams(int i, int i2) {
            super(i, i2);
            this.show = false;
            this.squeezeStatus = 0;
            this.mButtonType = SmartButtonType.REPLY;
        }

        @VisibleForTesting
        boolean isShown() {
            return this.show;
        }
    }

    /* loaded from: classes2.dex */
    public static class SmartReplies {
        public final List<CharSequence> choices;
        public final boolean fromAssistant;
        public final PendingIntent pendingIntent;
        public final RemoteInput remoteInput;

        public SmartReplies(List<CharSequence> list, RemoteInput remoteInput, PendingIntent pendingIntent, boolean z) {
            this.choices = list;
            this.remoteInput = remoteInput;
            this.pendingIntent = pendingIntent;
            this.fromAssistant = z;
        }
    }

    /* loaded from: classes2.dex */
    public static class SmartActions {
        public final List<Notification.Action> actions;
        public final boolean fromAssistant;

        public SmartActions(List<Notification.Action> list, boolean z) {
            this.actions = list;
            this.fromAssistant = z;
        }
    }
}
