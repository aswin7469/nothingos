package com.android.systemui.statusbar.notification.row;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.service.notification.StatusBarNotification;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.C1894R;

public class HybridGroupManager {
    private final Context mContext;
    private int mOverflowNumberColor;
    private int mOverflowNumberPadding;
    private float mOverflowNumberSize;

    public HybridGroupManager(Context context) {
        this.mContext = context;
        initDimens();
    }

    public void initDimens() {
        Resources resources = this.mContext.getResources();
        this.mOverflowNumberSize = (float) resources.getDimensionPixelSize(C1894R.dimen.group_overflow_number_size);
        this.mOverflowNumberPadding = resources.getDimensionPixelSize(C1894R.dimen.group_overflow_number_padding);
    }

    private HybridNotificationView inflateHybridViewWithStyle(int i, View view, ViewGroup viewGroup) {
        HybridNotificationView hybridNotificationView = (HybridNotificationView) ((LayoutInflater) new ContextThemeWrapper(this.mContext, i).getSystemService(LayoutInflater.class)).inflate(view instanceof ConversationLayout ? C1894R.layout.hybrid_conversation_notification : C1894R.layout.hybrid_notification, viewGroup, false);
        viewGroup.addView(hybridNotificationView);
        return hybridNotificationView;
    }

    private TextView inflateOverflowNumber(ViewGroup viewGroup) {
        TextView textView = (TextView) ((LayoutInflater) this.mContext.getSystemService(LayoutInflater.class)).inflate(C1894R.layout.hybrid_overflow_number, viewGroup, false);
        viewGroup.addView(textView);
        updateOverFlowNumberColor(textView);
        return textView;
    }

    private void updateOverFlowNumberColor(TextView textView) {
        textView.setTextColor(this.mOverflowNumberColor);
    }

    public void setOverflowNumberColor(TextView textView, int i) {
        this.mOverflowNumberColor = i;
        if (textView != null) {
            updateOverFlowNumberColor(textView);
        }
    }

    public HybridNotificationView bindFromNotification(HybridNotificationView hybridNotificationView, View view, StatusBarNotification statusBarNotification, ViewGroup viewGroup) {
        return bindFromNotificationWithStyle(hybridNotificationView, view, statusBarNotification, C1894R.style.HybridNotification, viewGroup);
    }

    private HybridNotificationView bindFromNotificationWithStyle(HybridNotificationView hybridNotificationView, View view, StatusBarNotification statusBarNotification, int i, ViewGroup viewGroup) {
        if (hybridNotificationView == null) {
            hybridNotificationView = inflateHybridViewWithStyle(i, view, viewGroup);
        }
        hybridNotificationView.bind(resolveTitle(statusBarNotification.getNotification()), resolveText(statusBarNotification.getNotification()), view);
        return hybridNotificationView;
    }

    public static CharSequence resolveText(Notification notification) {
        CharSequence charSequence = notification.extras.getCharSequence(NotificationCompat.EXTRA_TEXT);
        return charSequence == null ? notification.extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT) : charSequence;
    }

    public static CharSequence resolveTitle(Notification notification) {
        CharSequence charSequence = notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE);
        return charSequence == null ? notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG) : charSequence;
    }

    public TextView bindOverflowNumber(TextView textView, int i, ViewGroup viewGroup) {
        if (textView == null) {
            textView = inflateOverflowNumber(viewGroup);
        }
        String string = this.mContext.getResources().getString(C1894R.string.notification_group_overflow_indicator, new Object[]{Integer.valueOf(i)});
        if (!string.equals(textView.getText())) {
            textView.setText(string);
        }
        textView.setContentDescription(String.format(this.mContext.getResources().getQuantityString(C1894R.plurals.notification_group_overflow_description, i), Integer.valueOf(i)));
        textView.setTextSize(0, this.mOverflowNumberSize);
        textView.setPaddingRelative(textView.getPaddingStart(), textView.getPaddingTop(), this.mOverflowNumberPadding, textView.getPaddingBottom());
        updateOverFlowNumberColor(textView);
        return textView;
    }
}
