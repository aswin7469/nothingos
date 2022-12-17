package androidx.lifecycle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

@Deprecated
public class ViewModelProviders {
    @Deprecated
    /* renamed from: of */
    public static ViewModelProvider m6of(Fragment fragment) {
        return new ViewModelProvider(fragment);
    }

    @Deprecated
    /* renamed from: of */
    public static ViewModelProvider m7of(FragmentActivity fragmentActivity) {
        return new ViewModelProvider(fragmentActivity);
    }
}
