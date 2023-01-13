package com.android.p019wm.shell.bubbles;

import android.view.KeyEvent;
import android.view.View;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/wm/shell/bubbles/StackEducationView$onAttachedToWindow$1", "Landroid/view/View$OnKeyListener;", "onKey", "", "v", "Landroid/view/View;", "keyCode", "", "event", "Landroid/view/KeyEvent;", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.StackEducationView$onAttachedToWindow$1 */
/* compiled from: StackEducationView.kt */
public final class StackEducationView$onAttachedToWindow$1 implements View.OnKeyListener {
    final /* synthetic */ StackEducationView this$0;

    StackEducationView$onAttachedToWindow$1(StackEducationView stackEducationView) {
        this.this$0 = stackEducationView;
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Intrinsics.checkNotNullParameter(keyEvent, NotificationCompat.CATEGORY_EVENT);
        if (keyEvent.getAction() != 1 || i != 4 || this.this$0.isHiding) {
            return false;
        }
        this.this$0.hide(false);
        return true;
    }
}
