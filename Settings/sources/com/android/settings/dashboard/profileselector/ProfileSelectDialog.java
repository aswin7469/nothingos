package com.android.settings.dashboard.profileselector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.android.internal.widget.DialogTitle;
import com.android.internal.widget.LinearLayoutManager;
import com.android.internal.widget.RecyclerView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.dashboard.profileselector.UserAdapter;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.R$string;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.drawer.Tile;
import java.util.ArrayList;
import java.util.List;

public class ProfileSelectDialog extends DialogFragment implements UserAdapter.OnClickListener {
    private static final boolean DEBUG = Log.isLoggable("ProfileSelectDialog", 3);
    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;
    private Tile mSelectedTile;
    private int mSourceMetricCategory;

    public static void show(FragmentManager fragmentManager, Tile tile, int i, DialogInterface.OnShowListener onShowListener, DialogInterface.OnDismissListener onDismissListener, DialogInterface.OnCancelListener onCancelListener) {
        ProfileSelectDialog profileSelectDialog = new ProfileSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedTile", tile);
        bundle.putInt("sourceMetricCategory", i);
        profileSelectDialog.setArguments(bundle);
        profileSelectDialog.mOnShowListener = onShowListener;
        profileSelectDialog.mOnDismissListener = onDismissListener;
        profileSelectDialog.mOnCancelListener = onCancelListener;
        profileSelectDialog.show(fragmentManager, "select_profile");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle requireArguments = requireArguments();
        this.mSelectedTile = (Tile) requireArguments.getParcelable("selectedTile", Tile.class);
        this.mSourceMetricCategory = requireArguments.getInt("sourceMetricCategory");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return createDialog(getContext(), this.mSelectedTile.userHandle, this);
    }

    public static Dialog createDialog(Context context, List<UserHandle> list, UserAdapter.OnClickListener onClickListener) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LayoutInflater.class);
        DialogTitle inflate = layoutInflater.inflate(R$layout.user_select_title, (ViewGroup) null);
        inflate.setText(R$string.choose_profile);
        View inflate2 = layoutInflater.inflate(R$layout.user_select, (ViewGroup) null);
        RecyclerView findViewById = inflate2.findViewById(R$id.list);
        findViewById.setAdapter(UserAdapter.createUserRecycleViewAdapter(context, list, onClickListener));
        findViewById.setLayoutManager(new LinearLayoutManager(context, 0, false));
        return new AlertDialog.Builder(context).setCustomTitle(inflate).setView(inflate2).create();
    }

    public void onClick(int i) {
        UserHandle userHandle = this.mSelectedTile.userHandle.get(i);
        Intent intent = new Intent(this.mSelectedTile.getIntent());
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
        int i2 = this.mSourceMetricCategory;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        metricsFeatureProvider.logStartedIntentWithProfile(intent, i2, z);
        intent.addFlags(32768);
        getActivity().startActivityAsUser(intent, userHandle);
        dismiss();
    }

    public void onStart() {
        super.onStart();
        DialogInterface.OnShowListener onShowListener = this.mOnShowListener;
        if (onShowListener != null) {
            onShowListener.onShow(getDialog());
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogInterface.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    public static void updateUserHandlesIfNeeded(Context context, Tile tile) {
        ArrayList<UserHandle> arrayList = tile.userHandle;
        if (arrayList != null && arrayList.size() > 1) {
            UserManager userManager = UserManager.get(context);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (userManager.getUserInfo(arrayList.get(size).getIdentifier()) == null) {
                    if (DEBUG) {
                        Log.d("ProfileSelectDialog", "Delete the user: " + arrayList.get(size).getIdentifier());
                    }
                    arrayList.remove(size);
                }
            }
        }
    }
}
