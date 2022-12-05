package com.nt.settings.security;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
/* loaded from: classes2.dex */
public class NtPrivacyPolicyFragment extends Fragment {
    private static String TAG = "NtPrivacyPolicyFragment";
    private String URL = "https://hk.nothing.tech/pages/privacy-policy";

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Uri parse = Uri.parse(this.URL);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(parse);
        intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
        startActivity(intent);
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        Log.d(TAG, "@_@ ------ onStop");
        getActivity().onBackPressed();
    }
}
