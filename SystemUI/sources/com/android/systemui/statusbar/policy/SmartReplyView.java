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
import android.os.SystemClock;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.C1894R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.notification.NotificationUtils;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SmartReplyView extends ViewGroup {
    private static final Comparator<View> DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR = new SmartReplyView$$ExternalSyntheticLambda0();
    private static final int MEASURE_SPEC_ANY_LENGTH = View.MeasureSpec.makeMeasureSpec(0, 0);
    private static final int SQUEEZE_FAILED = -1;
    private static final String TAG = "SmartReplyView";
    private final BreakIterator mBreakIterator;
    private PriorityQueue<Button> mCandidateButtonQueueForSqueezing;
    private int mCurrentBackgroundColor;
    private boolean mCurrentColorized;
    private int mCurrentRippleColor;
    private int mCurrentStrokeColor;
    private int mCurrentTextColor;
    private final int mDefaultBackgroundColor;
    private final int mDefaultStrokeColor;
    private final int mDefaultTextColor;
    private final int mDefaultTextColorDarkBg;
    private boolean mDidHideSystemReplies;
    private final int mHeightUpperLimit = NotificationUtils.getFontScaledHeight(this.mContext, C1894R.dimen.smart_reply_button_max_height);
    private long mLastDispatchDrawTime;
    private long mLastDrawChildTime;
    private long mLastMeasureTime;
    private int mMaxNumActions;
    private int mMaxSqueezeRemeasureAttempts;
    private int mMinNumSystemGeneratedReplies;
    private final double mMinStrokeContrast;
    private final int mRippleColor;
    private final int mRippleColorDarkBg;
    private boolean mSmartRepliesGeneratedByAssistant = false;
    private View mSmartReplyContainer;
    private final int mSpacing;
    private final int mStrokeWidth;
    private int mTotalSqueezeRemeasureAttempts;

    enum SmartButtonType {
        REPLY,
        ACTION
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    static /* synthetic */ int lambda$static$0(View view, View view2) {
        return ((view2.getMeasuredWidth() - view2.getPaddingLeft()) - view2.getPaddingRight()) - ((view.getMeasuredWidth() - view.getPaddingLeft()) - view.getPaddingRight());
    }

    public SmartReplyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int color = context.getColor(C1894R.C1895color.smart_reply_button_background);
        this.mDefaultBackgroundColor = color;
        this.mDefaultTextColor = this.mContext.getColor(C1894R.C1895color.smart_reply_button_text);
        this.mDefaultTextColorDarkBg = this.mContext.getColor(C1894R.C1895color.smart_reply_button_text_dark_bg);
        int color2 = this.mContext.getColor(C1894R.C1895color.smart_reply_button_stroke);
        this.mDefaultStrokeColor = color2;
        int color3 = this.mContext.getColor(C1894R.C1895color.notification_ripple_untinted_color);
        this.mRippleColor = color3;
        this.mRippleColorDarkBg = Color.argb(Color.alpha(color3), 255, 255, 255);
        this.mMinStrokeContrast = ContrastColorUtil.calculateContrast(color2, color);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.SmartReplyView, 0, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == 1) {
                i2 = obtainStyledAttributes.getDimensionPixelSize(i3, 0);
            } else if (index == 0) {
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
        SmartReplyView smartReplyView = (SmartReplyView) LayoutInflater.from(context).inflate(C1894R.layout.smart_reply_view, (ViewGroup) null);
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
        for (Button next : list) {
            addView(next);
            setButtonColors(next);
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

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.mContext, attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    private void clearLayoutLineCount(View view) {
        if (view instanceof TextView) {
            ((TextView) view).nullLayouts();
            view.forceLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        String str;
        int i4;
        int i5 = i2;
        if (View.MeasureSpec.getMode(i) == 0) {
            i3 = Integer.MAX_VALUE;
        } else {
            i3 = View.MeasureSpec.getSize(i);
        }
        resetButtonsLayoutParams();
        boolean z = false;
        this.mTotalSqueezeRemeasureAttempts = 0;
        boolean isEmpty = this.mCandidateButtonQueueForSqueezing.isEmpty();
        String str2 = TAG;
        if (!isEmpty) {
            Log.wtf(str2, "Single line button queue leaked between onMeasure calls");
            this.mCandidateButtonQueueForSqueezing.clear();
        }
        SmartSuggestionMeasures smartSuggestionMeasures = new SmartSuggestionMeasures(this.mPaddingLeft + this.mPaddingRight, 0);
        List<View> filterActionsOrReplies = filterActionsOrReplies(SmartButtonType.ACTION);
        List<View> filterActionsOrReplies2 = filterActionsOrReplies(SmartButtonType.REPLY);
        ArrayList<View> arrayList = new ArrayList<>(filterActionsOrReplies);
        arrayList.addAll(filterActionsOrReplies2);
        ArrayList arrayList2 = new ArrayList();
        int i6 = this.mMaxNumActions;
        SmartSuggestionMeasures smartSuggestionMeasures2 = null;
        int i7 = 0;
        int i8 = 0;
        for (View view : arrayList) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (i6 == -1 || layoutParams.mButtonType != SmartButtonType.ACTION || i7 < i6) {
                clearLayoutLineCount(view);
                view.measure(MEASURE_SPEC_ANY_LENGTH, i5);
                Button button = (Button) view;
                if (button.getLayout() == null) {
                    Log.wtf(str2, "Button layout is null after measure.");
                }
                arrayList2.add(view);
                int lineCount = button.getLineCount();
                str = str2;
                if (lineCount < 1) {
                    layoutParams.mNoShowReason = "line-count-0";
                } else if (lineCount > 2) {
                    layoutParams.mNoShowReason = "line-count-3+";
                } else {
                    if (lineCount == 1) {
                        this.mCandidateButtonQueueForSqueezing.add(button);
                    }
                    SmartSuggestionMeasures clone = smartSuggestionMeasures.clone();
                    if (smartSuggestionMeasures2 == null && layoutParams.mButtonType == SmartButtonType.REPLY) {
                        smartSuggestionMeasures2 = smartSuggestionMeasures.clone();
                    }
                    if (i8 == 0) {
                        i4 = 0;
                    } else {
                        i4 = this.mSpacing;
                    }
                    int measuredWidth = view.getMeasuredWidth();
                    int measuredHeight = view.getMeasuredHeight();
                    SmartSuggestionMeasures smartSuggestionMeasures3 = clone;
                    smartSuggestionMeasures.mMeasuredWidth += i4 + measuredWidth;
                    smartSuggestionMeasures.mMaxChildHeight = Math.max(smartSuggestionMeasures.mMaxChildHeight, measuredHeight);
                    if (smartSuggestionMeasures.mMeasuredWidth > i3) {
                        while (smartSuggestionMeasures.mMeasuredWidth > i3 && !this.mCandidateButtonQueueForSqueezing.isEmpty()) {
                            Button poll = this.mCandidateButtonQueueForSqueezing.poll();
                            int squeezeButton = squeezeButton(poll, i5);
                            if (squeezeButton != -1) {
                                smartSuggestionMeasures.mMaxChildHeight = Math.max(smartSuggestionMeasures.mMaxChildHeight, poll.getMeasuredHeight());
                                smartSuggestionMeasures.mMeasuredWidth -= squeezeButton;
                            }
                        }
                        if (smartSuggestionMeasures.mMeasuredWidth > i3) {
                            markButtonsWithPendingSqueezeStatusAs(3, arrayList2);
                            layoutParams.mNoShowReason = "overflow";
                            str2 = str;
                            smartSuggestionMeasures = smartSuggestionMeasures3;
                            z = false;
                        } else {
                            markButtonsWithPendingSqueezeStatusAs(2, arrayList2);
                        }
                    }
                    boolean unused = layoutParams.show = true;
                    layoutParams.mNoShowReason = "n/a";
                    i8++;
                    if (layoutParams.mButtonType == SmartButtonType.ACTION) {
                        i7++;
                    }
                }
            } else {
                layoutParams.mNoShowReason = "max-actions-shown";
                str = str2;
            }
            str2 = str;
            z = false;
        }
        this.mDidHideSystemReplies = z;
        if (this.mSmartRepliesGeneratedByAssistant && !gotEnoughSmartReplies(filterActionsOrReplies2)) {
            for (View layoutParams2 : filterActionsOrReplies2) {
                LayoutParams layoutParams3 = (LayoutParams) layoutParams2.getLayoutParams();
                boolean unused2 = layoutParams3.show = false;
                layoutParams3.mNoShowReason = "not-enough-system-replies";
            }
            this.mDidHideSystemReplies = true;
            smartSuggestionMeasures = smartSuggestionMeasures2;
        }
        this.mCandidateButtonQueueForSqueezing.clear();
        remeasureButtonsIfNecessary(smartSuggestionMeasures.mMaxChildHeight);
        setMeasuredDimension(resolveSize(Math.max(getSuggestedMinimumWidth(), smartSuggestionMeasures.mMeasuredWidth), i), resolveSize(Math.max(getSuggestedMinimumHeight(), this.mPaddingTop + smartSuggestionMeasures.mMaxChildHeight + this.mPaddingBottom), i5));
        this.mLastMeasureTime = SystemClock.elapsedRealtime();
    }

    /* access modifiers changed from: package-private */
    public void setSmartRepliesGeneratedByAssistant(boolean z) {
        this.mSmartRepliesGeneratedByAssistant = z;
    }

    /* access modifiers changed from: package-private */
    public void hideSmartSuggestions() {
        View view = this.mSmartReplyContainer;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println(this);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("mMaxSqueezeRemeasureAttempts=");
        indentingPrintWriter.println(this.mMaxSqueezeRemeasureAttempts);
        indentingPrintWriter.print("mTotalSqueezeRemeasureAttempts=");
        indentingPrintWriter.println(this.mTotalSqueezeRemeasureAttempts);
        indentingPrintWriter.print("mMaxNumActions=");
        indentingPrintWriter.println(this.mMaxNumActions);
        indentingPrintWriter.print("mSmartRepliesGeneratedByAssistant=");
        indentingPrintWriter.println(this.mSmartRepliesGeneratedByAssistant);
        indentingPrintWriter.print("mMinNumSystemGeneratedReplies=");
        indentingPrintWriter.println(this.mMinNumSystemGeneratedReplies);
        indentingPrintWriter.print("mHeightUpperLimit=");
        indentingPrintWriter.println(this.mHeightUpperLimit);
        indentingPrintWriter.print("mDidHideSystemReplies=");
        indentingPrintWriter.println(this.mDidHideSystemReplies);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        indentingPrintWriter.print("lastMeasureAge (s)=");
        long j = this.mLastMeasureTime;
        float f = Float.NaN;
        indentingPrintWriter.println(j == 0 ? Float.NaN : ((float) (elapsedRealtime - j)) / 1000.0f);
        indentingPrintWriter.print("lastDrawChildAge (s)=");
        long j2 = this.mLastDrawChildTime;
        indentingPrintWriter.println(j2 == 0 ? Float.NaN : ((float) (elapsedRealtime - j2)) / 1000.0f);
        indentingPrintWriter.print("lastDispatchDrawAge (s)=");
        long j3 = this.mLastDispatchDrawTime;
        if (j3 != 0) {
            f = ((float) (elapsedRealtime - j3)) / 1000.0f;
        }
        indentingPrintWriter.println(f);
        int childCount = getChildCount();
        indentingPrintWriter.print("children: num=");
        indentingPrintWriter.println(childCount);
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            indentingPrintWriter.print(NavigationBarInflaterView.SIZE_MOD_START);
            indentingPrintWriter.print(i);
            indentingPrintWriter.print("] type=");
            indentingPrintWriter.print(layoutParams.mButtonType);
            indentingPrintWriter.print(" squeezeStatus=");
            indentingPrintWriter.print(layoutParams.squeezeStatus);
            indentingPrintWriter.print(" show=");
            indentingPrintWriter.print(layoutParams.show);
            indentingPrintWriter.print(" noShowReason=");
            indentingPrintWriter.print(layoutParams.mNoShowReason);
            indentingPrintWriter.print(" view=");
            indentingPrintWriter.println(childAt);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    private static class SmartSuggestionMeasures {
        int mMaxChildHeight;
        int mMeasuredWidth;

        SmartSuggestionMeasures(int i, int i2) {
            this.mMeasuredWidth = i;
            this.mMaxChildHeight = i2;
        }

        public SmartSuggestionMeasures clone() {
            return new SmartSuggestionMeasures(this.mMeasuredWidth, this.mMaxChildHeight);
        }
    }

    private boolean gotEnoughSmartReplies(List<View> list) {
        if (this.mMinNumSystemGeneratedReplies <= 1) {
            return true;
        }
        int i = 0;
        for (View layoutParams : list) {
            if (((LayoutParams) layoutParams.getLayoutParams()).show) {
                i++;
            }
        }
        if (i == 0 || i >= this.mMinNumSystemGeneratedReplies) {
            return true;
        }
        return false;
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
            boolean unused = layoutParams.show = false;
            int unused2 = layoutParams.squeezeStatus = 0;
            layoutParams.mNoShowReason = "reset";
        }
    }

    private int squeezeButton(Button button, int i) {
        int estimateOptimalSqueezedButtonTextWidth = estimateOptimalSqueezedButtonTextWidth(button);
        if (estimateOptimalSqueezedButtonTextWidth == -1) {
            return -1;
        }
        return squeezeButtonToTextWidth(button, i, estimateOptimalSqueezedButtonTextWidth);
    }

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
                this.mTotalSqueezeRemeasureAttempts++;
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
                if (!z ? desiredWidth3 >= desiredWidth4 : desiredWidth3 <= desiredWidth4) {
                    max = max2;
                    break;
                }
                i3++;
                max = max2;
            }
        }
        return (int) Math.ceil((double) max);
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
        if (button.getLayout() == null) {
            Log.wtf(TAG, "Button layout is null after measure.");
        }
        int measuredWidth2 = button.getMeasuredWidth();
        LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
        if (button.getLineCount() > 2 || measuredWidth2 >= measuredWidth) {
            int unused = layoutParams.squeezeStatus = 3;
            return -1;
        }
        int unused2 = layoutParams.squeezeStatus = 1;
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
        for (View layoutParams : list) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams.getLayoutParams();
            if (layoutParams2.squeezeStatus == 1) {
                int unused = layoutParams2.squeezeStatus = i;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2 = true;
        if (getLayoutDirection() != 1) {
            z2 = false;
        }
        int i5 = z2 ? (i3 - i) - this.mPaddingRight : this.mPaddingLeft;
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

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        if (!((LayoutParams) view.getLayoutParams()).show) {
            return false;
        }
        this.mLastDrawChildTime = SystemClock.elapsedRealtime();
        return super.drawChild(canvas, view, j);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mLastDispatchDrawTime = SystemClock.elapsedRealtime();
    }

    public void setBackgroundTintColor(int i, boolean z) {
        if (i != this.mCurrentBackgroundColor || z != this.mCurrentColorized) {
            this.mCurrentBackgroundColor = i;
            this.mCurrentColorized = z;
            boolean isColorDark = Notification.Builder.isColorDark(i);
            int i2 = isColorDark ? this.mDefaultTextColorDarkBg : this.mDefaultTextColor;
            int i3 = i | ViewCompat.MEASURED_STATE_MASK;
            int ensureTextContrast = ContrastColorUtil.ensureTextContrast(i2, i3, isColorDark);
            this.mCurrentTextColor = ensureTextContrast;
            if (!z) {
                ensureTextContrast = ContrastColorUtil.ensureContrast(this.mDefaultStrokeColor, i3, isColorDark, this.mMinStrokeContrast);
            }
            this.mCurrentStrokeColor = ensureTextContrast;
            this.mCurrentRippleColor = isColorDark ? this.mRippleColorDarkBg : this.mRippleColor;
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                setButtonColors((Button) getChildAt(i4));
            }
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

    static class LayoutParams extends ViewGroup.LayoutParams {
        private static final int SQUEEZE_STATUS_FAILED = 3;
        private static final int SQUEEZE_STATUS_NONE = 0;
        private static final int SQUEEZE_STATUS_PENDING = 1;
        private static final int SQUEEZE_STATUS_SUCCESSFUL = 2;
        SmartButtonType mButtonType;
        String mNoShowReason;
        /* access modifiers changed from: private */
        public boolean show;
        /* access modifiers changed from: private */
        public int squeezeStatus;

        private LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.show = false;
            this.squeezeStatus = 0;
            this.mButtonType = SmartButtonType.REPLY;
            this.mNoShowReason = "new";
        }

        private LayoutParams(int i, int i2) {
            super(i, i2);
            this.show = false;
            this.squeezeStatus = 0;
            this.mButtonType = SmartButtonType.REPLY;
            this.mNoShowReason = "new";
        }

        /* access modifiers changed from: package-private */
        public boolean isShown() {
            return this.show;
        }
    }

    public static class SmartReplies {
        public final List<CharSequence> choices;
        public final boolean fromAssistant;
        public final PendingIntent pendingIntent;
        public final RemoteInput remoteInput;

        public SmartReplies(List<CharSequence> list, RemoteInput remoteInput2, PendingIntent pendingIntent2, boolean z) {
            this.choices = list;
            this.remoteInput = remoteInput2;
            this.pendingIntent = pendingIntent2;
            this.fromAssistant = z;
        }
    }

    public static class SmartActions {
        public final List<Notification.Action> actions;
        public final boolean fromAssistant;

        public SmartActions(List<Notification.Action> list, boolean z) {
            this.actions = list;
            this.fromAssistant = z;
        }
    }
}
