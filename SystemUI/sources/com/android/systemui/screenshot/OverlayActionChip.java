package com.android.systemui.screenshot;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.Objects;

public class OverlayActionChip extends FrameLayout {
    private static final String TAG = "ScreenshotActionChip";
    private ImageView mIconView;
    private boolean mIsPending;
    private TextView mTextView;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public OverlayActionChip(Context context) {
        this(context, (AttributeSet) null);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsPending = false;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mIconView = (ImageView) Objects.requireNonNull((ImageView) findViewById(C1894R.C1898id.overlay_action_chip_icon));
        TextView textView = (TextView) Objects.requireNonNull((TextView) findViewById(C1894R.C1898id.overlay_action_chip_text));
        this.mTextView = textView;
        updatePadding(textView.getText().length() > 0);
    }

    public void setPressed(boolean z) {
        super.setPressed(this.mIsPending || z);
    }

    public void setIcon(Icon icon, boolean z) {
        this.mIconView.setImageIcon(icon);
        if (!z) {
            this.mIconView.setImageTintList((ColorStateList) null);
        }
    }

    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
        updatePadding(charSequence.length() > 0);
    }

    public void setPendingIntent(PendingIntent pendingIntent, Runnable runnable) {
        setOnClickListener(new OverlayActionChip$$ExternalSyntheticLambda0(pendingIntent, runnable));
    }

    static /* synthetic */ void lambda$setPendingIntent$0(PendingIntent pendingIntent, Runnable runnable, View view) {
        try {
            pendingIntent.send();
            runnable.run();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "Intent cancelled", e);
        }
    }

    public void setIsPending(boolean z) {
        this.mIsPending = z;
        setPressed(z);
    }

    private void updatePadding(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mTextView.getLayoutParams();
        if (z) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.overlay_action_chip_padding_horizontal);
            int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.overlay_action_chip_spacing);
            layoutParams.setMarginStart(dimensionPixelSize);
            layoutParams.setMarginEnd(dimensionPixelSize2);
            layoutParams2.setMarginEnd(dimensionPixelSize);
        } else {
            int dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.overlay_action_chip_icon_only_padding_horizontal);
            layoutParams.setMarginStart(dimensionPixelSize3);
            layoutParams.setMarginEnd(dimensionPixelSize3);
        }
        this.mIconView.setLayoutParams(layoutParams);
        this.mTextView.setLayoutParams(layoutParams2);
    }
}
