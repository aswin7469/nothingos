package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.provider.Settings;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.android.wm.shell.bubbles.BubbleDebugConfig */
public class BubbleDebugConfig {
    static final boolean DEBUG_BUBBLE_CONTROLLER = false;
    static final boolean DEBUG_BUBBLE_DATA = false;
    static final boolean DEBUG_BUBBLE_EXPANDED_VIEW = false;
    static final boolean DEBUG_BUBBLE_STACK_VIEW = false;
    static final boolean DEBUG_EXPERIMENTS = true;
    static final boolean DEBUG_OVERFLOW = false;
    static final boolean DEBUG_POSITIONER = false;
    static final boolean DEBUG_USER_EDUCATION = false;
    private static final boolean FORCE_SHOW_USER_EDUCATION = false;
    private static final String FORCE_SHOW_USER_EDUCATION_SETTING = "force_show_bubbles_user_education";
    public static final String TAG_BUBBLES = "Bubbles";
    public static final boolean TAG_WITH_CLASS_NAME = false;

    static boolean forceShowUserEducation(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), FORCE_SHOW_USER_EDUCATION_SETTING, 0) != 0;
    }

    static String formatBubblesString(List<Bubble> list, BubbleViewProvider bubbleViewProvider) {
        StringBuilder sb = new StringBuilder();
        Iterator<Bubble> it = list.iterator();
        while (it.hasNext()) {
            Bubble next = it.next();
            if (next == null) {
                sb.append("   <null> !!!!!\n");
            } else {
                sb.append(String.format("%s Bubble{act=%12d, showInShade=%d, key=%s}\n", bubbleViewProvider != null && bubbleViewProvider.getKey() != BubbleOverflow.KEY && next == bubbleViewProvider ? "=>" : "  ", Long.valueOf(next.getLastActivity()), Integer.valueOf(next.showInShade() ? 1 : 0), next.getKey()));
            }
        }
        return sb.toString();
    }
}
