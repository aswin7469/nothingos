package com.android.keyguard;

import android.content.res.ColorStateList;

public interface SecurityMessageDisplay {
    void formatMessage(int i, Object... objArr);

    void setMessage(int i);

    void setMessage(CharSequence charSequence);

    void setNextMessageColor(ColorStateList colorStateList);
}
