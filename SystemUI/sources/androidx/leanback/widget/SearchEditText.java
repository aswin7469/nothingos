package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.leanback.R$style;
/* loaded from: classes.dex */
public class SearchEditText extends StreamingTextView {
    private static final String TAG = SearchEditText.class.getSimpleName();
    OnKeyboardDismissListener mKeyboardDismissListener;

    /* loaded from: classes.dex */
    public interface OnKeyboardDismissListener {
        void onKeyboardDismiss();
    }

    @Override // androidx.leanback.widget.StreamingTextView, android.view.View
    public /* bridge */ /* synthetic */ void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    @Override // androidx.leanback.widget.StreamingTextView
    public /* bridge */ /* synthetic */ void reset() {
        super.reset();
    }

    @Override // androidx.leanback.widget.StreamingTextView, android.widget.TextView
    public /* bridge */ /* synthetic */ void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        super.setCustomSelectionActionModeCallback(actionModeCallback);
    }

    @Override // androidx.leanback.widget.StreamingTextView
    public /* bridge */ /* synthetic */ void updateRecognizedText(String stableText, String pendingText) {
        super.updateRecognizedText(stableText, pendingText);
    }

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R$style.TextAppearance_Leanback_SearchTextEdit);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4 && this.mKeyboardDismissListener != null) {
            post(new Runnable() { // from class: androidx.leanback.widget.SearchEditText.1
                @Override // java.lang.Runnable
                public void run() {
                    OnKeyboardDismissListener onKeyboardDismissListener = SearchEditText.this.mKeyboardDismissListener;
                    if (onKeyboardDismissListener != null) {
                        onKeyboardDismissListener.onKeyboardDismiss();
                    }
                }
            });
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setOnKeyboardDismissListener(OnKeyboardDismissListener listener) {
        this.mKeyboardDismissListener = listener;
    }
}
