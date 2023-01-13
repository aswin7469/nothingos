package com.android.systemui.statusbar.p013tv.notifications;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.SparseArray;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.leanback.widget.VerticalGridView;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler;
import java.util.function.Consumer;
import javax.inject.Inject;

/* renamed from: com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity */
public class TvNotificationPanelActivity extends Activity implements TvNotificationHandler.Listener {
    private final Consumer<Boolean> mBlurConsumer = new TvNotificationPanelActivity$$ExternalSyntheticLambda0(this);
    private VerticalGridView mNotificationListView;
    private View mNotificationPlaceholder;
    private boolean mPanelAlreadyOpen = false;
    private TvNotificationAdapter mTvNotificationAdapter;
    private final TvNotificationHandler mTvNotificationHandler;

    @Inject
    public TvNotificationPanelActivity(TvNotificationHandler tvNotificationHandler) {
        this.mTvNotificationHandler = tvNotificationHandler;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!maybeClosePanel(getIntent())) {
            this.mPanelAlreadyOpen = true;
            setContentView(C1894R.layout.tv_notification_panel);
            this.mNotificationPlaceholder = findViewById(C1894R.C1898id.no_tv_notifications);
            this.mTvNotificationAdapter = new TvNotificationAdapter();
            VerticalGridView verticalGridView = (VerticalGridView) findViewById(C1894R.C1898id.notifications_list);
            this.mNotificationListView = verticalGridView;
            verticalGridView.setAdapter(this.mTvNotificationAdapter);
            this.mNotificationListView.setColumnWidth((int) C1894R.dimen.tv_notification_panel_width);
            this.mTvNotificationHandler.setTvNotificationListener(this);
            notificationsUpdated(this.mTvNotificationHandler.getCurrentNotifications());
        }
    }

    public void notificationsUpdated(SparseArray<StatusBarNotification> sparseArray) {
        this.mTvNotificationAdapter.setNotifications(sparseArray);
        int i = 0;
        boolean z = sparseArray.size() == 0;
        this.mNotificationListView.setVisibility(z ? 8 : 0);
        View view = this.mNotificationPlaceholder;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        maybeClosePanel(intent);
    }

    private boolean maybeClosePanel(Intent intent) {
        if (!"android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()) && (!this.mPanelAlreadyOpen || !"android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()))) {
            return false;
        }
        finish();
        return true;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setGravity(GravityCompat.END);
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    /* access modifiers changed from: private */
    public void enableBlur(boolean z) {
        if (z) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.tv_notification_blur_radius);
            getWindow().setBackgroundDrawable(new ColorDrawable(getColor(C1894R.C1895color.tv_notification_blur_background_color)));
            getWindow().setBackgroundBlurRadius(dimensionPixelSize);
            return;
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(getColor(C1894R.C1895color.tv_notification_default_background_color)));
        getWindow().setBackgroundBlurRadius(0);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mTvNotificationHandler.setTvNotificationListener((TvNotificationHandler.Listener) null);
    }
}
