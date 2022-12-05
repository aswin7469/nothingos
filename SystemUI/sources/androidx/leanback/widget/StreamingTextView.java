package androidx.leanback.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import androidx.core.widget.TextViewCompat;
import androidx.leanback.R$drawable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class StreamingTextView extends EditText {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\S+");
    private static final Property<StreamingTextView, Integer> STREAM_POSITION_PROPERTY = new Property<StreamingTextView, Integer>(Integer.class, "streamPosition") { // from class: androidx.leanback.widget.StreamingTextView.1
        @Override // android.util.Property
        public Integer get(StreamingTextView view) {
            return Integer.valueOf(view.getStreamPosition());
        }

        @Override // android.util.Property
        public void set(StreamingTextView view, Integer value) {
            view.setStreamPosition(value.intValue());
        }
    };
    Bitmap mOneDot;
    final Random mRandom = new Random();
    int mStreamPosition;
    private ObjectAnimator mStreamingAnimation;
    Bitmap mTwoDot;

    public StreamingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StreamingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mOneDot = getScaledBitmap(R$drawable.lb_text_dot_one, 1.3f);
        this.mTwoDot = getScaledBitmap(R$drawable.lb_text_dot_two, 1.3f);
        reset();
    }

    private Bitmap getScaledBitmap(int resourceId, float scaled) {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), resourceId);
        return Bitmap.createScaledBitmap(decodeResource, (int) (decodeResource.getWidth() * scaled), (int) (decodeResource.getHeight() * scaled), false);
    }

    public void reset() {
        this.mStreamPosition = -1;
        cancelStreamAnimation();
        setText("");
    }

    public void updateRecognizedText(String stableText, String pendingText) {
        if (stableText == null) {
            stableText = "";
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stableText);
        if (pendingText != null) {
            int length = spannableStringBuilder.length();
            spannableStringBuilder.append((CharSequence) pendingText);
            addDottySpans(spannableStringBuilder, pendingText, length);
        }
        this.mStreamPosition = Math.max(stableText.length(), this.mStreamPosition);
        updateText(new SpannedString(spannableStringBuilder));
        startStreamAnimation();
    }

    int getStreamPosition() {
        return this.mStreamPosition;
    }

    void setStreamPosition(int streamPosition) {
        this.mStreamPosition = streamPosition;
        invalidate();
    }

    private void startStreamAnimation() {
        cancelStreamAnimation();
        int streamPosition = getStreamPosition();
        int length = length();
        int i = length - streamPosition;
        if (i > 0) {
            if (this.mStreamingAnimation == null) {
                ObjectAnimator objectAnimator = new ObjectAnimator();
                this.mStreamingAnimation = objectAnimator;
                objectAnimator.setTarget(this);
                this.mStreamingAnimation.setProperty(STREAM_POSITION_PROPERTY);
            }
            this.mStreamingAnimation.setIntValues(streamPosition, length);
            this.mStreamingAnimation.setDuration(i * 50);
            this.mStreamingAnimation.start();
        }
    }

    private void cancelStreamAnimation() {
        ObjectAnimator objectAnimator = this.mStreamingAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void addDottySpans(SpannableStringBuilder displayText, String text, int textStart) {
        Matcher matcher = SPLIT_PATTERN.matcher(text);
        while (matcher.find()) {
            int start = matcher.start() + textStart;
            displayText.setSpan(new DottySpan(text.charAt(matcher.start()), start), start, matcher.end() + textStart, 33);
        }
    }

    private void updateText(CharSequence displayText) {
        setText(displayText);
        bringPointIntoView(length());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName("androidx.leanback.widget.StreamingTextView");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DottySpan extends ReplacementSpan {
        private final int mPosition;
        private final int mSeed;

        public DottySpan(int seed, int pos) {
            this.mSeed = seed;
            this.mPosition = pos;
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Bitmap bitmap;
            Bitmap bitmap2;
            int measureText = (int) paint.measureText(text, start, end);
            int width = StreamingTextView.this.mOneDot.getWidth();
            int i = width * 2;
            int i2 = measureText / i;
            int i3 = (measureText % i) / 2;
            boolean isLayoutRtl = StreamingTextView.isLayoutRtl(StreamingTextView.this);
            StreamingTextView.this.mRandom.setSeed(this.mSeed);
            int alpha = paint.getAlpha();
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = this.mPosition + i4;
                StreamingTextView streamingTextView = StreamingTextView.this;
                if (i5 >= streamingTextView.mStreamPosition) {
                    break;
                }
                float f = (i4 * i) + i3 + (width / 2);
                float f2 = isLayoutRtl ? ((measureText + x) - f) - width : x + f;
                paint.setAlpha((streamingTextView.mRandom.nextInt(4) + 1) * 63);
                if (StreamingTextView.this.mRandom.nextBoolean()) {
                    canvas.drawBitmap(StreamingTextView.this.mTwoDot, f2, y - bitmap2.getHeight(), paint);
                } else {
                    canvas.drawBitmap(StreamingTextView.this.mOneDot, f2, y - bitmap.getHeight(), paint);
                }
            }
            paint.setAlpha(alpha);
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
            return (int) paint.measureText(text, start, end);
        }
    }

    public static boolean isLayoutRtl(View view) {
        return Build.VERSION.SDK_INT >= 17 && 1 == view.getLayoutDirection();
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionModeCallback));
    }
}
