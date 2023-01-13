package com.android.systemui.statusbar.notification.row;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\tB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog;", "Landroid/app/Dialog;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "updateDoneButtonText", "", "hasChanges", "", "Builder", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialog extends Dialog {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ChannelEditorDialog(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public final void updateDoneButtonText(boolean z) {
        TextView textView = (TextView) findViewById(C1894R.C1898id.done_button);
        if (textView != null) {
            textView.setText(z ? C1894R.string.inline_ok_button : C1894R.string.inline_done_button);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog$Builder;", "", "()V", "context", "Landroid/content/Context;", "build", "Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialog;", "setContext", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ChannelEditorDialogController.kt */
    public static final class Builder {
        private Context context;

        public final Builder setContext(Context context2) {
            Intrinsics.checkNotNullParameter(context2, "context");
            this.context = context2;
            return this;
        }

        public final ChannelEditorDialog build() {
            Context context2 = this.context;
            if (context2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("context");
                context2 = null;
            }
            return new ChannelEditorDialog(context2);
        }
    }
}
