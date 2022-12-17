package com.nothing.settings.privacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import java.util.Objects;

public class PrivacyPolicyFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Uri parse = Uri.parse("https://nothing.tech/pages/privacy-policy");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(parse);
        intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
        startActivity(intent);
        return null;
    }

    public void onStop() {
        super.onStop();
        FragmentActivity activity = getActivity();
        Objects.requireNonNull(activity);
        activity.onBackPressed();
    }
}
