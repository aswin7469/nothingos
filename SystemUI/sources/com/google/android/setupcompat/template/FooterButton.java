package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.setupcompat.C3941R;
import com.google.android.setupcompat.logging.CustomEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public final class FooterButton implements View.OnClickListener {
    private static final String KEY_BUTTON_ON_CLICK_COUNT = "_onClickCount";
    private static final String KEY_BUTTON_TEXT = "_text";
    private static final String KEY_BUTTON_TYPE = "_type";
    private static final int MAX_BUTTON_TYPE = 8;
    private OnButtonEventListener buttonListener;
    private final int buttonType;
    private int clickCount;
    private int direction;
    private boolean enabled;
    private Locale locale;
    private View.OnClickListener onClickListener;
    private View.OnClickListener onClickListenerWhenDisabled;
    private CharSequence text;
    private int theme;
    private int visibility;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonType {
        public static final int ADD_ANOTHER = 1;
        public static final int CANCEL = 2;
        public static final int CLEAR = 3;
        public static final int DONE = 4;
        public static final int NEXT = 5;
        public static final int OPT_IN = 6;
        public static final int OTHER = 0;
        public static final int SKIP = 7;
        public static final int STOP = 8;
    }

    interface OnButtonEventListener {
        void onDirectionChanged(int i);

        void onEnabledChanged(boolean z);

        void onLocaleChanged(Locale locale);

        void onTextChanged(CharSequence charSequence);

        void onVisibilityChanged(int i);
    }

    public FooterButton(Context context, AttributeSet attributeSet) {
        this.enabled = true;
        this.visibility = 0;
        this.clickCount = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3941R.styleable.SucFooterButton);
        this.text = obtainStyledAttributes.getString(C3941R.styleable.SucFooterButton_android_text);
        this.onClickListener = null;
        this.buttonType = getButtonTypeValue(obtainStyledAttributes.getInt(C3941R.styleable.SucFooterButton_sucButtonType, 0));
        this.theme = obtainStyledAttributes.getResourceId(C3941R.styleable.SucFooterButton_android_theme, 0);
        obtainStyledAttributes.recycle();
    }

    private FooterButton(CharSequence charSequence, View.OnClickListener onClickListener2, int i, int i2, Locale locale2, int i3) {
        this.enabled = true;
        this.visibility = 0;
        this.clickCount = 0;
        this.text = charSequence;
        this.onClickListener = onClickListener2;
        this.buttonType = i;
        this.theme = i2;
        this.locale = locale2;
        this.direction = i3;
    }

    public CharSequence getText() {
        return this.text;
    }

    public void setOnClickListener(View.OnClickListener onClickListener2) {
        this.onClickListener = onClickListener2;
    }

    public View.OnClickListener getOnClickListenerWhenDisabled() {
        return this.onClickListenerWhenDisabled;
    }

    public void setOnClickListenerWhenDisabled(View.OnClickListener onClickListener2) {
        this.onClickListenerWhenDisabled = onClickListener2;
    }

    public int getButtonType() {
        return this.buttonType;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onEnabledChanged(z);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getLayoutDirection() {
        return this.direction;
    }

    public Locale getTextLocale() {
        return this.locale;
    }

    public void setVisibility(int i) {
        this.visibility = i;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onVisibilityChanged(i);
        }
    }

    public int getVisibility() {
        return this.visibility;
    }

    public void setText(Context context, int i) {
        setText(context.getText(i));
    }

    public void setText(CharSequence charSequence) {
        this.text = charSequence;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onTextChanged(charSequence);
        }
    }

    public void setTextLocale(Locale locale2) {
        this.locale = locale2;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onLocaleChanged(locale2);
        }
    }

    public void setLayoutDirection(int i) {
        this.direction = i;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onDirectionChanged(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnButtonEventListener(OnButtonEventListener onButtonEventListener) {
        if (onButtonEventListener != null) {
            this.buttonListener = onButtonEventListener;
            return;
        }
        throw new NullPointerException("Event listener of footer button may not be null.");
    }

    public void onClick(View view) {
        View.OnClickListener onClickListener2 = this.onClickListener;
        if (onClickListener2 != null) {
            this.clickCount++;
            onClickListener2.onClick(view);
        }
    }

    private int getButtonTypeValue(int i) {
        if (i >= 0 && i <= 8) {
            return i;
        }
        throw new IllegalArgumentException("Not a ButtonType");
    }

    private String getButtonTypeName() {
        switch (this.buttonType) {
            case 1:
                return "ADD_ANOTHER";
            case 2:
                return "CANCEL";
            case 3:
                return "CLEAR";
            case 4:
                return "DONE";
            case 5:
                return "NEXT";
            case 6:
                return "OPT_IN";
            case 7:
                return "SKIP";
            case 8:
                return "STOP";
            default:
                return "OTHER";
        }
    }

    public PersistableBundle getMetrics(String str) {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(str + KEY_BUTTON_TEXT, CustomEvent.trimsStringOverMaxLength(getText().toString()));
        persistableBundle.putString(str + KEY_BUTTON_TYPE, getButtonTypeName());
        persistableBundle.putInt(str + KEY_BUTTON_ON_CLICK_COUNT, this.clickCount);
        return persistableBundle;
    }

    public static class Builder {
        private int buttonType = 0;
        private final Context context;
        private int direction = -1;
        private Locale locale = null;
        private View.OnClickListener onClickListener = null;
        private String text = "";
        private int theme = 0;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setText(String str) {
            this.text = str;
            return this;
        }

        public Builder setText(int i) {
            this.text = this.context.getString(i);
            return this;
        }

        public Builder setTextLocale(Locale locale2) {
            this.locale = locale2;
            return this;
        }

        public Builder setLayoutDirection(int i) {
            this.direction = i;
            return this;
        }

        public Builder setListener(View.OnClickListener onClickListener2) {
            this.onClickListener = onClickListener2;
            return this;
        }

        public Builder setButtonType(int i) {
            this.buttonType = i;
            return this;
        }

        public Builder setTheme(int i) {
            this.theme = i;
            return this;
        }

        public FooterButton build() {
            return new FooterButton(this.text, this.onClickListener, this.buttonType, this.theme, this.locale, this.direction);
        }
    }
}
