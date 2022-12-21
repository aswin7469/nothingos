package androidx.leanback.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.leanback.C0742R;

public final class ListRowHoverCardView extends LinearLayout {
    private final TextView mDescriptionView;
    private final TextView mTitleView;

    public ListRowHoverCardView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ListRowHoverCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ListRowHoverCardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(C0742R.layout.lb_list_row_hovercard, this);
        this.mTitleView = (TextView) findViewById(C0742R.C0745id.title);
        this.mDescriptionView = (TextView) findViewById(C0742R.C0745id.description);
    }

    public final CharSequence getTitle() {
        return this.mTitleView.getText();
    }

    public final void setTitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            this.mTitleView.setText(charSequence);
            this.mTitleView.setVisibility(0);
            return;
        }
        this.mTitleView.setVisibility(8);
    }

    public final CharSequence getDescription() {
        return this.mDescriptionView.getText();
    }

    public final void setDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            this.mDescriptionView.setText(charSequence);
            this.mDescriptionView.setVisibility(0);
            return;
        }
        this.mDescriptionView.setVisibility(8);
    }
}
