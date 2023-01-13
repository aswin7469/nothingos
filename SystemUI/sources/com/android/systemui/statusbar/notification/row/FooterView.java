package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.util.DumpUtilsKt;
import java.p026io.PrintWriter;

public class FooterView extends StackScrollerDecorView {
    private FooterViewButton mClearAllButton;
    private FooterViewButton mManageButton;
    private String mManageNotificationHistoryText;
    private String mManageNotificationText;
    private boolean mShowHistory;

    public FooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public View findContentView() {
        return findViewById(C1894R.C1898id.content);
    }

    /* access modifiers changed from: protected */
    public View findSecondaryView() {
        return findViewById(C1894R.C1898id.dismiss_text);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        super.dump(asIndenting, strArr);
        DumpUtilsKt.withIncreasedIndent(asIndenting, (Runnable) new FooterView$$ExternalSyntheticLambda0(this, asIndenting));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$dump$0$com-android-systemui-statusbar-notification-row-FooterView */
    public /* synthetic */ void mo41350x433bc4b0(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("visibility: " + DumpUtilsKt.visibilityString(getVisibility()));
        indentingPrintWriter.println("manageButton showHistory: " + this.mShowHistory);
        indentingPrintWriter.println("manageButton visibility: " + DumpUtilsKt.visibilityString(this.mClearAllButton.getVisibility()));
        indentingPrintWriter.println("dismissButton visibility: " + DumpUtilsKt.visibilityString(this.mClearAllButton.getVisibility()));
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mClearAllButton = (FooterViewButton) findSecondaryView();
        this.mManageButton = (FooterViewButton) findViewById(C1894R.C1898id.manage_text);
        updateResources();
        updateText();
    }

    public void setManageButtonClickListener(View.OnClickListener onClickListener) {
        this.mManageButton.setOnClickListener(onClickListener);
    }

    public void setClearAllButtonClickListener(View.OnClickListener onClickListener) {
        this.mClearAllButton.setOnClickListener(onClickListener);
    }

    public boolean isOnEmptySpace(float f, float f2) {
        return f < this.mContent.getX() || f > this.mContent.getX() + ((float) this.mContent.getWidth()) || f2 < this.mContent.getY() || f2 > this.mContent.getY() + ((float) this.mContent.getHeight());
    }

    public void showHistory(boolean z) {
        if (this.mShowHistory != z) {
            this.mShowHistory = z;
            updateText();
        }
    }

    private void updateText() {
        if (this.mShowHistory) {
            this.mManageButton.setText(this.mManageNotificationHistoryText);
            this.mManageButton.setContentDescription(this.mManageNotificationHistoryText);
            return;
        }
        this.mManageButton.setText(this.mManageNotificationText);
        this.mManageButton.setContentDescription(this.mManageNotificationText);
    }

    public boolean isHistoryShown() {
        return this.mShowHistory;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateColors();
        this.mClearAllButton.setText(C1894R.string.clear_all_notifications_text);
        this.mClearAllButton.setContentDescription(this.mContext.getString(C1894R.string.accessibility_clear_all));
        updateResources();
        updateText();
    }

    public void updateColors() {
        Resources.Theme theme = this.mContext.getTheme();
        int color = getResources().getColor(C1894R.C1895color.notif_pill_text, theme);
        this.mClearAllButton.setBackground(theme.getDrawable(C1894R.C1896drawable.notif_footer_btn_background));
        this.mClearAllButton.setTextColor(color);
        this.mManageButton.setBackground(theme.getDrawable(C1894R.C1896drawable.notif_footer_btn_background));
        this.mManageButton.setTextColor(color);
    }

    private void updateResources() {
        this.mManageNotificationText = getContext().getString(C1894R.string.manage_notifications_text);
        this.mManageNotificationHistoryText = getContext().getString(C1894R.string.manage_notifications_history_text);
    }

    public ExpandableViewState createExpandableViewState() {
        return new FooterViewState();
    }

    public class FooterViewState extends ExpandableViewState {
        public boolean hideContent;

        public FooterViewState() {
        }

        public void copyFrom(ViewState viewState) {
            super.copyFrom(viewState);
            if (viewState instanceof FooterViewState) {
                this.hideContent = ((FooterViewState) viewState).hideContent;
            }
        }

        public void applyToView(View view) {
            super.applyToView(view);
            if (view instanceof FooterView) {
                ((FooterView) view).setContentVisible(!this.hideContent);
            }
        }
    }
}
