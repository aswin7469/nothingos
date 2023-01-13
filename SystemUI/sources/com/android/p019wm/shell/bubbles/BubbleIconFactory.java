package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.bubbles.BubbleIconFactory */
public class BubbleIconFactory extends BaseIconFactory {
    public BubbleIconFactory(Context context) {
        super(context, context.getResources().getConfiguration().densityDpi, context.getResources().getDimensionPixelSize(C3353R.dimen.bubble_size));
    }

    /* access modifiers changed from: package-private */
    public Drawable getBubbleDrawable(Context context, ShortcutInfo shortcutInfo, Icon icon) {
        if (shortcutInfo != null) {
            return ((LauncherApps) context.getSystemService("launcherapps")).getShortcutIconDrawable(shortcutInfo, context.getResources().getConfiguration().densityDpi);
        }
        if (icon == null) {
            return null;
        }
        if (icon.getType() == 4 || icon.getType() == 6) {
            context.grantUriPermission(context.getPackageName(), icon.getUri(), 1);
        }
        return icon.loadDrawable(context);
    }
}
