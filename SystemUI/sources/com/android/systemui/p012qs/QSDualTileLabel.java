package com.android.systemui.p012qs;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.Objects;

/* renamed from: com.android.systemui.qs.QSDualTileLabel */
public class QSDualTileLabel extends LinearLayout {
    private final Context mContext;
    private final TextView mFirstLine;
    private final ImageView mFirstLineCaret;
    private final int mHorizontalPaddingPx;
    private final TextView mSecondLine;
    private String mText;
    private final Runnable mUpdateText = new Runnable() {
        public void run() {
            QSDualTileLabel.this.updateText();
        }
    };

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSDualTileLabel(Context context) {
        super(context);
        this.mContext = context;
        setOrientation(1);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1894R.dimen.qs_dual_tile_padding_horizontal);
        this.mHorizontalPaddingPx = dimensionPixelSize;
        TextView initTextView = initTextView();
        this.mFirstLine = initTextView;
        initTextView.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(0, 0, 0, 0);
        linearLayout.setOrientation(0);
        linearLayout.setClickable(false);
        linearLayout.setBackground((Drawable) null);
        linearLayout.addView(initTextView);
        ImageView imageView = new ImageView(context);
        this.mFirstLineCaret = imageView;
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setClickable(false);
        linearLayout.addView(imageView);
        addView(linearLayout, newLinearLayoutParams());
        TextView initTextView2 = initTextView();
        this.mSecondLine = initTextView2;
        initTextView2.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        initTextView2.setEllipsize(TextUtils.TruncateAt.END);
        initTextView2.setVisibility(8);
        addView(initTextView2, newLinearLayoutParams());
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (i7 - i5 != i3 - i) {
                    QSDualTileLabel.this.rescheduleUpdateText();
                }
            }
        });
    }

    private static LinearLayout.LayoutParams newLinearLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 1;
        return layoutParams;
    }

    public void setFirstLineCaret(Drawable drawable) {
        this.mFirstLineCaret.setImageDrawable(drawable);
        if (drawable != null) {
            this.mFirstLine.setMinHeight(drawable.getIntrinsicHeight());
            this.mFirstLine.setPadding(this.mHorizontalPaddingPx, 0, 0, 0);
        }
    }

    private TextView initTextView() {
        TextView textView = new TextView(this.mContext);
        textView.setPadding(0, 0, 0, 0);
        textView.setGravity(16);
        textView.setSingleLine(true);
        textView.setClickable(false);
        textView.setBackground((Drawable) null);
        return textView;
    }

    public void setText(CharSequence charSequence) {
        String trim = charSequence == null ? null : charSequence.toString().trim();
        if (!Objects.equals(trim, this.mText)) {
            this.mText = trim;
            rescheduleUpdateText();
        }
    }

    public String getText() {
        return this.mText;
    }

    public void setTextSize(int i, float f) {
        this.mFirstLine.setTextSize(i, f);
        this.mSecondLine.setTextSize(i, f);
        rescheduleUpdateText();
    }

    public void setTextColor(int i) {
        this.mFirstLine.setTextColor(i);
        this.mSecondLine.setTextColor(i);
        rescheduleUpdateText();
    }

    public void setTypeface(Typeface typeface) {
        this.mFirstLine.setTypeface(typeface);
        this.mSecondLine.setTypeface(typeface);
        rescheduleUpdateText();
    }

    /* access modifiers changed from: private */
    public void rescheduleUpdateText() {
        removeCallbacks(this.mUpdateText);
        post(this.mUpdateText);
    }

    /* access modifiers changed from: private */
    public void updateText() {
        if (getWidth() != 0) {
            if (TextUtils.isEmpty(this.mText)) {
                this.mFirstLine.setText((CharSequence) null);
                this.mSecondLine.setText((CharSequence) null);
                this.mSecondLine.setVisibility(8);
                return;
            }
            float width = (float) ((((getWidth() - this.mFirstLineCaret.getWidth()) - this.mHorizontalPaddingPx) - getPaddingLeft()) - getPaddingRight());
            if (this.mFirstLine.getPaint().measureText(this.mText) <= width) {
                this.mFirstLine.setText(this.mText);
                this.mSecondLine.setText((CharSequence) null);
                this.mSecondLine.setVisibility(8);
                return;
            }
            int length = this.mText.length();
            int i = -1;
            int i2 = 1;
            boolean z = false;
            while (i2 < length) {
                boolean z2 = this.mFirstLine.getPaint().measureText(this.mText.substring(0, i2)) > width;
                if (Character.isWhitespace(this.mText.charAt(i2))) {
                    if (!z && !z2) {
                        i = i2;
                    }
                    z = true;
                } else {
                    z = false;
                }
                if (z2) {
                    break;
                }
                i2++;
            }
            if (i == -1) {
                i = i2 - 1;
            }
            this.mFirstLine.setText(this.mText.substring(0, i));
            this.mSecondLine.setText(this.mText.substring(i).trim());
            this.mSecondLine.setVisibility(0);
        }
    }
}
