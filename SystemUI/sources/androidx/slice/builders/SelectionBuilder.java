package androidx.slice.builders;

import android.app.PendingIntent;
import androidx.collection.ArraySet;
import androidx.core.util.Pair;
import androidx.remotecallback.RemoteCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectionBuilder {
    private CharSequence mContentDescription;
    private PendingIntent mInputAction;
    private int mLayoutDirection = -1;
    private final Set<String> mOptionKeys = new ArraySet();
    private final List<Pair<String, CharSequence>> mOptions = new ArrayList();
    private SliceAction mPrimaryAction;
    private String mSelectedOption = null;
    private CharSequence mSubtitle;
    private CharSequence mTitle;

    public SelectionBuilder addOption(String str, CharSequence charSequence) {
        if (!this.mOptionKeys.contains(str)) {
            this.mOptions.add(new Pair(str, charSequence));
            this.mOptionKeys.add(str);
            return this;
        }
        throw new IllegalArgumentException("optionKey " + str + " is a duplicate");
    }

    public SelectionBuilder setPrimaryAction(SliceAction sliceAction) {
        this.mPrimaryAction = sliceAction;
        return this;
    }

    public SelectionBuilder setInputAction(PendingIntent pendingIntent) {
        this.mInputAction = pendingIntent;
        return this;
    }

    public SelectionBuilder setInputAction(RemoteCallback remoteCallback) {
        this.mInputAction = remoteCallback.toPendingIntent();
        return this;
    }

    public SelectionBuilder setSelectedOption(String str) {
        this.mSelectedOption = str;
        return this;
    }

    public SelectionBuilder setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        return this;
    }

    public SelectionBuilder setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        return this;
    }

    public SelectionBuilder setContentDescription(CharSequence charSequence) {
        this.mContentDescription = charSequence;
        return this;
    }

    public SelectionBuilder setLayoutDirection(int i) {
        this.mLayoutDirection = i;
        return this;
    }

    public List<Pair<String, CharSequence>> getOptions() {
        return this.mOptions;
    }

    public SliceAction getPrimaryAction() {
        return this.mPrimaryAction;
    }

    public PendingIntent getInputAction() {
        return this.mInputAction;
    }

    public String getSelectedOption() {
        return this.mSelectedOption;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    public void check() {
        if (getPrimaryAction() == null) {
            throw new IllegalArgumentException("primaryAction must be set");
        } else if (getInputAction() != null) {
            String str = this.mSelectedOption;
            if (str != null && !this.mOptionKeys.contains(str)) {
                throw new IllegalArgumentException("selectedOption must be present in options");
            }
        } else {
            throw new IllegalArgumentException("inputAction must be set");
        }
    }
}
