package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.setupdesign.C3963R;

public class ButtonItem extends AbstractItem implements View.OnClickListener {
    private Button button;
    private boolean enabled = true;
    private OnClickListener listener;
    private CharSequence text;
    private int theme = C3963R.style.SudButtonItem;

    public interface OnClickListener {
        void onClick(ButtonItem buttonItem);
    }

    public int getCount() {
        return 0;
    }

    public int getLayoutResource() {
        return 0;
    }

    public ButtonItem() {
    }

    public ButtonItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3963R.styleable.SudButtonItem);
        this.enabled = obtainStyledAttributes.getBoolean(C3963R.styleable.SudButtonItem_android_enabled, true);
        this.text = obtainStyledAttributes.getText(C3963R.styleable.SudButtonItem_android_text);
        this.theme = obtainStyledAttributes.getResourceId(C3963R.styleable.SudButtonItem_android_theme, C3963R.style.SudButtonItem);
        obtainStyledAttributes.recycle();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }

    public void setText(CharSequence charSequence) {
        this.text = charSequence;
    }

    public CharSequence getText() {
        return this.text;
    }

    public void setTheme(int i) {
        this.theme = i;
        this.button = null;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public final void onBindView(View view) {
        throw new UnsupportedOperationException("Cannot bind to ButtonItem's view");
    }

    /* access modifiers changed from: protected */
    public Button createButton(ViewGroup viewGroup) {
        Button button2 = this.button;
        if (button2 == null) {
            Context context = viewGroup.getContext();
            if (this.theme != 0) {
                context = new ContextThemeWrapper(context, this.theme);
            }
            Button createButton = createButton(context);
            this.button = createButton;
            createButton.setOnClickListener(this);
        } else if (button2.getParent() instanceof ViewGroup) {
            ((ViewGroup) this.button.getParent()).removeView(this.button);
        }
        this.button.setEnabled(this.enabled);
        this.button.setText(this.text);
        this.button.setId(getViewId());
        return this.button;
    }

    private Button createButton(Context context) {
        return (Button) LayoutInflater.from(context).inflate(C3963R.layout.sud_button, (ViewGroup) null, false);
    }

    public void onClick(View view) {
        OnClickListener onClickListener = this.listener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }
}
