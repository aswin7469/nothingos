package com.android.settings.display;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settings.R$color;
import com.android.settings.R$dimen;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$styleable;

public class ConversationMessageView extends FrameLayout {
    private TextView mContactIconView;
    private final int mIconBackgroundColor;
    private final CharSequence mIconText;
    private final int mIconTextColor;
    private final boolean mIncoming;
    private LinearLayout mMessageBubble;
    private final CharSequence mMessageText;
    private ViewGroup mMessageTextAndInfoView;
    private TextView mMessageTextView;
    private TextView mStatusTextView;
    private final CharSequence mTimestampText;

    public ConversationMessageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ConversationMessageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ConversationMessageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ConversationMessageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ConversationMessageView);
        this.mIncoming = obtainStyledAttributes.getBoolean(R$styleable.ConversationMessageView_incoming, true);
        this.mMessageText = obtainStyledAttributes.getString(R$styleable.ConversationMessageView_messageText);
        this.mTimestampText = obtainStyledAttributes.getString(R$styleable.ConversationMessageView_timestampText);
        this.mIconText = obtainStyledAttributes.getString(R$styleable.ConversationMessageView_iconText);
        this.mIconTextColor = obtainStyledAttributes.getColor(R$styleable.ConversationMessageView_iconTextColor, 0);
        this.mIconBackgroundColor = obtainStyledAttributes.getColor(R$styleable.ConversationMessageView_iconBackgroundColor, 0);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(context).inflate(R$layout.conversation_message_icon, this);
        LayoutInflater.from(context).inflate(R$layout.conversation_message_content, this);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mMessageBubble = (LinearLayout) findViewById(R$id.message_content);
        this.mMessageTextAndInfoView = (ViewGroup) findViewById(R$id.message_text_and_info);
        this.mMessageTextView = (TextView) findViewById(R$id.message_text);
        this.mStatusTextView = (TextView) findViewById(R$id.message_status);
        this.mContactIconView = (TextView) findViewById(R$id.conversation_icon);
        updateViewContent();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        updateViewAppearance();
        int size = View.MeasureSpec.getSize(i);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mContactIconView.measure(makeMeasureSpec2, makeMeasureSpec2);
        int makeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(Math.max(this.mContactIconView.getMeasuredWidth(), this.mContactIconView.getMeasuredHeight()), 1073741824);
        this.mContactIconView.measure(makeMeasureSpec3, makeMeasureSpec3);
        this.mMessageBubble.measure(View.MeasureSpec.makeMeasureSpec((((size - (this.mContactIconView.getMeasuredWidth() * 2)) - getResources().getDimensionPixelOffset(R$dimen.message_bubble_left_right_padding)) - getPaddingLeft()) - getPaddingRight(), Integer.MIN_VALUE), makeMeasureSpec);
        setMeasuredDimension(size, Math.max(this.mContactIconView.getMeasuredHeight(), this.mMessageBubble.getMeasuredHeight()) + getPaddingBottom() + getPaddingTop());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        boolean isLayoutRtl = isLayoutRtl(this);
        int measuredWidth = this.mContactIconView.getMeasuredWidth();
        int measuredHeight = this.mContactIconView.getMeasuredHeight();
        int paddingTop = getPaddingTop();
        int i8 = i3 - i;
        int paddingLeft = ((i8 - measuredWidth) - getPaddingLeft()) - getPaddingRight();
        int measuredHeight2 = this.mMessageBubble.getMeasuredHeight();
        if (this.mIncoming) {
            if (isLayoutRtl) {
                i7 = getPaddingRight();
                i5 = (i8 - i7) - measuredWidth;
                i6 = i5 - paddingLeft;
                this.mContactIconView.layout(i5, paddingTop, measuredWidth + i5, measuredHeight + paddingTop);
                this.mMessageBubble.layout(i6, paddingTop, paddingLeft + i6, measuredHeight2 + paddingTop);
            }
            i5 = getPaddingLeft();
        } else if (isLayoutRtl) {
            i5 = getPaddingLeft();
        } else {
            i7 = getPaddingRight();
            i5 = (i8 - i7) - measuredWidth;
            i6 = i5 - paddingLeft;
            this.mContactIconView.layout(i5, paddingTop, measuredWidth + i5, measuredHeight + paddingTop);
            this.mMessageBubble.layout(i6, paddingTop, paddingLeft + i6, measuredHeight2 + paddingTop);
        }
        i6 = i5 + measuredWidth;
        this.mContactIconView.layout(i5, paddingTop, measuredWidth + i5, measuredHeight + paddingTop);
        this.mMessageBubble.layout(i6, paddingTop, paddingLeft + i6, measuredHeight2 + paddingTop);
    }

    private static boolean isLayoutRtl(View view) {
        return 1 == view.getLayoutDirection();
    }

    private void updateViewContent() {
        this.mMessageTextView.setText(this.mMessageText);
        this.mStatusTextView.setText(this.mTimestampText);
        this.mStatusTextView.setVisibility(TextUtils.isEmpty(this.mTimestampText) ? 8 : 0);
        this.mContactIconView.setText(this.mIconText);
        this.mContactIconView.setTextColor(this.mIconTextColor);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{getTintedDrawable(getContext(), getContext().getDrawable(R$drawable.conversation_message_icon), this.mIconBackgroundColor), getTintedDrawable(getContext(), getContext().getDrawable(R$drawable.ic_person), getContext().getColor(R$color.message_icon_color))});
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R$dimen.message_icon_inset);
        layerDrawable.setLayerInset(1, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
        this.mContactIconView.setBackground(layerDrawable);
    }

    private void updateViewAppearance() {
        int i;
        Resources resources = getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R$dimen.message_bubble_left_right_padding);
        int dimensionPixelOffset2 = resources.getDimensionPixelOffset(R$dimen.message_text_left_right_padding);
        int dimensionPixelOffset3 = resources.getDimensionPixelOffset(R$dimen.message_text_top_padding);
        int dimensionPixelOffset4 = resources.getDimensionPixelOffset(R$dimen.message_text_bottom_padding);
        boolean z = this.mIncoming;
        int i2 = z ? dimensionPixelOffset : 0;
        if (z) {
            dimensionPixelOffset = 0;
        }
        int i3 = z ? 8388627 : 8388629;
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.message_padding_default);
        int dimensionPixelOffset5 = resources.getDimensionPixelOffset(R$dimen.message_metadata_top_padding);
        if (this.mIncoming) {
            i = R$color.message_bubble_incoming;
        } else {
            i = R$color.message_bubble_outgoing;
        }
        Context context = getContext();
        this.mMessageTextAndInfoView.setBackground(getTintedDrawable(context, context.getDrawable(R$drawable.conversation_message_text_info_view_background), context.getColor(i)));
        if (isLayoutRtl(this)) {
            this.mMessageTextAndInfoView.setPadding(dimensionPixelOffset2, dimensionPixelOffset3 + dimensionPixelOffset5, dimensionPixelOffset2, dimensionPixelOffset4);
        } else {
            this.mMessageTextAndInfoView.setPadding(dimensionPixelOffset2, dimensionPixelOffset3 + dimensionPixelOffset5, dimensionPixelOffset2, dimensionPixelOffset4);
        }
        setPadding(getPaddingLeft(), dimensionPixelSize, getPaddingRight(), 0);
        this.mMessageBubble.setGravity(i3);
        this.mMessageBubble.setPaddingRelative(i2, 0, dimensionPixelOffset, 0);
        updateTextAppearance();
    }

    private void updateTextAppearance() {
        int i;
        int i2;
        boolean z = this.mIncoming;
        if (z) {
            i = R$color.message_text_incoming;
        } else {
            i = R$color.message_text_outgoing;
        }
        if (z) {
            i2 = R$color.timestamp_text_incoming;
        } else {
            i2 = R$color.timestamp_text_outgoing;
        }
        int color = getContext().getColor(i);
        this.mMessageTextView.setTextColor(color);
        this.mMessageTextView.setLinkTextColor(color);
        this.mStatusTextView.setTextColor(i2);
    }

    private static Drawable getTintedDrawable(Context context, Drawable drawable, int i) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        if (constantState != null) {
            drawable = constantState.newDrawable(context.getResources()).mutate();
        }
        drawable.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }
}
