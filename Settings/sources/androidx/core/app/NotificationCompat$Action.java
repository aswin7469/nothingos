package androidx.core.app;

import android.app.PendingIntent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;
import java.util.Arrays;

public class NotificationCompat$Action {
    public PendingIntent actionIntent;
    @Deprecated
    public int icon;
    private boolean mAllowGeneratedReplies;
    private boolean mAuthenticationRequired;
    private final RemoteInput[] mDataOnlyRemoteInputs;
    final Bundle mExtras;
    private IconCompat mIcon;
    private final boolean mIsContextual;
    private final RemoteInput[] mRemoteInputs;
    private final int mSemanticAction;
    boolean mShowsUserInterface = true;
    public CharSequence title;

    NotificationCompat$Action(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr, RemoteInput[] remoteInputArr2, boolean z, int i, boolean z2, boolean z3, boolean z4) {
        this.mIcon = iconCompat;
        if (iconCompat != null && iconCompat.getType() == 2) {
            this.icon = iconCompat.getResId();
        }
        this.title = NotificationCompat$Builder.limitCharSequenceLength(charSequence);
        this.actionIntent = pendingIntent;
        this.mExtras = bundle == null ? new Bundle() : bundle;
        this.mRemoteInputs = remoteInputArr;
        this.mDataOnlyRemoteInputs = remoteInputArr2;
        this.mAllowGeneratedReplies = z;
        this.mSemanticAction = i;
        this.mShowsUserInterface = z2;
        this.mIsContextual = z3;
        this.mAuthenticationRequired = z4;
    }

    public IconCompat getIconCompat() {
        int i;
        if (this.mIcon == null && (i = this.icon) != 0) {
            this.mIcon = IconCompat.createWithResource((Resources) null, "", i);
        }
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public PendingIntent getActionIntent() {
        return this.actionIntent;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public boolean getAllowGeneratedReplies() {
        return this.mAllowGeneratedReplies;
    }

    public boolean isAuthenticationRequired() {
        return this.mAuthenticationRequired;
    }

    public RemoteInput[] getRemoteInputs() {
        return this.mRemoteInputs;
    }

    public int getSemanticAction() {
        return this.mSemanticAction;
    }

    public boolean isContextual() {
        return this.mIsContextual;
    }

    public boolean getShowsUserInterface() {
        return this.mShowsUserInterface;
    }

    public static final class Builder {
        private boolean mAllowGeneratedReplies;
        private boolean mAuthenticationRequired;
        private final Bundle mExtras;
        private final IconCompat mIcon;
        private final PendingIntent mIntent;
        private boolean mIsContextual;
        private ArrayList<RemoteInput> mRemoteInputs;
        private int mSemanticAction;
        private boolean mShowsUserInterface;
        private final CharSequence mTitle;

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public Builder(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            this(i != 0 ? IconCompat.createWithResource((Resources) null, "", i) : null, charSequence, pendingIntent, new Bundle(), (RemoteInput[]) null, true, 0, true, false, false);
        }

        private Builder(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr, boolean z, int i, boolean z2, boolean z3, boolean z4) {
            ArrayList<RemoteInput> arrayList;
            this.mAllowGeneratedReplies = true;
            this.mShowsUserInterface = true;
            this.mIcon = iconCompat;
            this.mTitle = NotificationCompat$Builder.limitCharSequenceLength(charSequence);
            this.mIntent = pendingIntent;
            this.mExtras = bundle;
            if (remoteInputArr == null) {
                arrayList = null;
            } else {
                arrayList = new ArrayList<>(Arrays.asList(remoteInputArr));
            }
            this.mRemoteInputs = arrayList;
            this.mAllowGeneratedReplies = z;
            this.mSemanticAction = i;
            this.mShowsUserInterface = z2;
            this.mIsContextual = z3;
            this.mAuthenticationRequired = z4;
        }

        private void checkContextualActionNullFields() {
            if (this.mIsContextual && this.mIntent == null) {
                throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
            }
        }

        /* JADX WARNING: type inference failed for: r1v5, types: [java.lang.Object[]] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public androidx.core.app.NotificationCompat$Action build() {
            /*
                r17 = this;
                r0 = r17
                r17.checkContextualActionNullFields()
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                java.util.ArrayList r2 = new java.util.ArrayList
                r2.<init>()
                java.util.ArrayList<androidx.core.app.RemoteInput> r3 = r0.mRemoteInputs
                if (r3 == 0) goto L_0x0031
                java.util.Iterator r3 = r3.iterator()
            L_0x0017:
                boolean r4 = r3.hasNext()
                if (r4 == 0) goto L_0x0031
                java.lang.Object r4 = r3.next()
                androidx.core.app.RemoteInput r4 = (androidx.core.app.RemoteInput) r4
                boolean r5 = r4.isDataOnly()
                if (r5 == 0) goto L_0x002d
                r1.add(r4)
                goto L_0x0017
            L_0x002d:
                r2.add(r4)
                goto L_0x0017
            L_0x0031:
                boolean r3 = r1.isEmpty()
                r4 = 0
                if (r3 == 0) goto L_0x003a
                r11 = r4
                goto L_0x0047
            L_0x003a:
                int r3 = r1.size()
                androidx.core.app.RemoteInput[] r3 = new androidx.core.app.RemoteInput[r3]
                java.lang.Object[] r1 = r1.toArray(r3)
                androidx.core.app.RemoteInput[] r1 = (androidx.core.app.RemoteInput[]) r1
                r11 = r1
            L_0x0047:
                boolean r1 = r2.isEmpty()
                if (r1 == 0) goto L_0x004e
                goto L_0x005b
            L_0x004e:
                int r1 = r2.size()
                androidx.core.app.RemoteInput[] r1 = new androidx.core.app.RemoteInput[r1]
                java.lang.Object[] r1 = r2.toArray(r1)
                r4 = r1
                androidx.core.app.RemoteInput[] r4 = (androidx.core.app.RemoteInput[]) r4
            L_0x005b:
                r10 = r4
                androidx.core.app.NotificationCompat$Action r1 = new androidx.core.app.NotificationCompat$Action
                androidx.core.graphics.drawable.IconCompat r6 = r0.mIcon
                java.lang.CharSequence r7 = r0.mTitle
                android.app.PendingIntent r8 = r0.mIntent
                android.os.Bundle r9 = r0.mExtras
                boolean r12 = r0.mAllowGeneratedReplies
                int r13 = r0.mSemanticAction
                boolean r14 = r0.mShowsUserInterface
                boolean r15 = r0.mIsContextual
                boolean r0 = r0.mAuthenticationRequired
                r5 = r1
                r16 = r0
                r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompat$Action.Builder.build():androidx.core.app.NotificationCompat$Action");
        }
    }
}
