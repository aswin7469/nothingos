package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.C1893R;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;

public class HeadsUpStatusBarView extends AlphaOptimizedLinearLayout {
    private static final String ALPHA = "alpha";
    private static final String HEADS_UP_STATUS_BAR_VIEW_SUPER_PARCELABLE = "heads_up_status_bar_view_super_parcelable";
    private static final String VISIBILITY = "visibility";
    private final Rect mIconDrawingRect;
    private View mIconPlaceholder;
    private final Rect mLayoutedIconRect;
    private Runnable mOnDrawingRectChangedListener;
    private final NotificationEntry.OnSensitivityChangedListener mOnSensitivityChangedListener;
    private NotificationEntry mShowingEntry;
    private TextView mTextView;
    private final int[] mTmpPosition;

    public HeadsUpStatusBarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public HeadsUpStatusBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HeadsUpStatusBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public HeadsUpStatusBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mLayoutedIconRect = new Rect();
        this.mTmpPosition = new int[2];
        this.mIconDrawingRect = new Rect();
        this.mOnSensitivityChangedListener = new HeadsUpStatusBarView$$ExternalSyntheticLambda0(this);
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(HEADS_UP_STATUS_BAR_VIEW_SUPER_PARCELABLE, super.onSaveInstanceState());
        bundle.putInt("visibility", getVisibility());
        bundle.putFloat("alpha", getAlpha());
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof Bundle)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(HEADS_UP_STATUS_BAR_VIEW_SUPER_PARCELABLE));
        if (bundle.containsKey("visibility")) {
            setVisibility(bundle.getInt("visibility"));
        }
        if (bundle.containsKey("alpha")) {
            setAlpha(bundle.getFloat("alpha"));
        }
    }

    public HeadsUpStatusBarView(Context context, View view, TextView textView) {
        this(context);
        this.mIconPlaceholder = view;
        this.mTextView = textView;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mIconPlaceholder = findViewById(C1893R.C1897id.icon_placeholder);
        this.mTextView = (TextView) findViewById(C1893R.C1897id.text);
    }

    public void setEntry(NotificationEntry notificationEntry) {
        NotificationEntry notificationEntry2 = this.mShowingEntry;
        if (notificationEntry2 != null) {
            notificationEntry2.removeOnSensitivityChangedListener(this.mOnSensitivityChangedListener);
        }
        this.mShowingEntry = notificationEntry;
        if (notificationEntry != null) {
            CharSequence charSequence = notificationEntry.headsUpStatusBarText;
            if (notificationEntry.isSensitive()) {
                charSequence = notificationEntry.headsUpStatusBarTextPublic;
            }
            this.mTextView.setText(charSequence);
            this.mShowingEntry.addOnSensitivityChangedListener(this.mOnSensitivityChangedListener);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-HeadsUpStatusBarView  reason: not valid java name */
    public /* synthetic */ void m3024lambda$new$0$comandroidsystemuistatusbarHeadsUpStatusBarView(NotificationEntry notificationEntry) {
        if (notificationEntry == this.mShowingEntry) {
            setEntry(notificationEntry);
            return;
        }
        throw new IllegalStateException("Got a sensitivity change for " + notificationEntry + " but mShowingEntry is " + this.mShowingEntry);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mIconPlaceholder.getLocationOnScreen(this.mTmpPosition);
        int[] iArr = this.mTmpPosition;
        int i5 = iArr[0];
        int i6 = iArr[1];
        this.mLayoutedIconRect.set(i5, i6, this.mIconPlaceholder.getWidth() + i5, this.mIconPlaceholder.getHeight() + i6);
        updateDrawingRect();
    }

    private void updateDrawingRect() {
        Runnable runnable;
        this.mIconDrawingRect.set(this.mLayoutedIconRect);
        if (((float) this.mIconDrawingRect.left) != ((float) this.mIconDrawingRect.left) && (runnable = this.mOnDrawingRectChangedListener) != null) {
            runnable.run();
        }
    }

    public NotificationEntry getShowingEntry() {
        return this.mShowingEntry;
    }

    public Rect getIconDrawingRect() {
        return this.mIconDrawingRect;
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        this.mTextView.setTextColor(DarkIconDispatcher.getTint(arrayList, this, i));
    }

    public void setOnDrawingRectChangedListener(Runnable runnable) {
        this.mOnDrawingRectChangedListener = runnable;
    }
}
