package com.android.settings.development.tare;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class TareFactorExpandableListAdapter extends BaseExpandableListAdapter {
    private final String[][] mChildren;
    private final TareFactorController mFactorController;
    private final String[] mGroups;
    private final String[][] mKeys;
    private final LayoutInflater mLayoutInflater;

    public long getChildId(int i, int i2) {
        return (long) i2;
    }

    public long getGroupId(int i) {
        return (long) i;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    TareFactorExpandableListAdapter(TareFactorController tareFactorController, LayoutInflater layoutInflater, String[] strArr, String[][] strArr2, String[][] strArr3) {
        this.mLayoutInflater = layoutInflater;
        this.mFactorController = tareFactorController;
        this.mGroups = strArr;
        this.mChildren = strArr2;
        this.mKeys = strArr3;
        validateMappings();
    }

    private void validateMappings() {
        int length = this.mGroups.length;
        String[][] strArr = this.mChildren;
        if (length != strArr.length) {
            throw new IllegalStateException("groups and children don't have the same length");
        } else if (strArr.length == this.mKeys.length) {
            int i = 0;
            while (true) {
                String[][] strArr2 = this.mChildren;
                if (i >= strArr2.length) {
                    return;
                }
                if (strArr2[i].length == this.mKeys[i].length) {
                    i++;
                } else {
                    throw new IllegalStateException("children and keys don't have the same length in row " + i);
                }
            }
        } else {
            throw new IllegalStateException("children and keys don't have the same length");
        }
    }

    public int getGroupCount() {
        return this.mGroups.length;
    }

    public int getChildrenCount(int i) {
        return this.mChildren[i].length;
    }

    public Object getGroup(int i) {
        return this.mGroups[i];
    }

    public Object getChild(int i, int i2) {
        return this.mChildren[i][i2];
    }

    /* access modifiers changed from: package-private */
    public String getKey(int i, int i2) {
        return this.mKeys[i][i2];
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(17367043, viewGroup, false);
        }
        ((TextView) view.findViewById(16908308)).setText(getGroup(i).toString());
        return view;
    }

    @SuppressLint({"InflateParams"})
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(R$layout.tare_child_item, (ViewGroup) null);
        }
        ((TextView) view.findViewById(R$id.factor)).setText(getChild(i, i2).toString());
        ((TextView) view.findViewById(R$id.factor_number)).setText(cakeToString(this.mFactorController.getValue(getKey(i, i2))));
        return view;
    }

    private static String cakeToString(long j) {
        if (j == 0) {
            return "0";
        }
        long j2 = j % 1000000000;
        long j3 = (long) ((int) (j / 1000000000));
        if (j3 == 0) {
            return j2 + " c";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(j3);
        if (j2 > 0) {
            sb.append(".");
            sb.append(String.format("%03d", new Object[]{Long.valueOf(j2 / 1000000)}));
        }
        sb.append(" A");
        return sb.toString();
    }
}
