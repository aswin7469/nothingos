package androidx.leanback.transition;

import android.content.Context;
import androidx.leanback.C0742R;

public class LeanbackTransitionHelper {
    public static Object loadTitleInTransition(Context context) {
        return TransitionHelper.loadTransition(context, C0742R.C0746transition.lb_title_in);
    }

    public static Object loadTitleOutTransition(Context context) {
        return TransitionHelper.loadTransition(context, C0742R.C0746transition.lb_title_out);
    }

    private LeanbackTransitionHelper() {
    }
}
