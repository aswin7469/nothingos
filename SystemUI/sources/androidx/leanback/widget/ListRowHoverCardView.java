package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.leanback.R$id;
import androidx.leanback.R$layout;
/* loaded from: classes.dex */
public final class ListRowHoverCardView extends LinearLayout {
    private final TextView mDescriptionView;
    private final TextView mTitleView;

    public ListRowHoverCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListRowHoverCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R$layout.lb_list_row_hovercard, this);
        this.mTitleView = (TextView) findViewById(R$id.title);
        this.mDescriptionView = (TextView) findViewById(R$id.description);
    }
}
