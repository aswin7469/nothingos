package com.android.systemui.shared.system;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
/* loaded from: classes.dex */
public class RemoteTransitionCompat implements Parcelable {
    public static final Parcelable.Creator<RemoteTransitionCompat> CREATOR = new Parcelable.Creator<RemoteTransitionCompat>() { // from class: com.android.systemui.shared.system.RemoteTransitionCompat.3
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RemoteTransitionCompat[] mo978newArray(int i) {
            return new RemoteTransitionCompat[i];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RemoteTransitionCompat mo977createFromParcel(Parcel parcel) {
            return new RemoteTransitionCompat(parcel);
        }
    };
    TransitionFilter mFilter;
    final IRemoteTransition mTransition;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    static class RecentsControllerWrap extends RecentsAnimationControllerCompat {
        private RecentsAnimationControllerCompat mWrapped = null;
        private IRemoteTransitionFinishedCallback mFinishCB = null;
        private WindowContainerToken mPausingTask = null;
        private TransitionInfo mInfo = null;
        private SurfaceControl mOpeningLeash = null;
        private ArrayMap<SurfaceControl, SurfaceControl> mLeashMap = null;

        RecentsControllerWrap() {
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mFilter != null ? (byte) 2 : (byte) 0);
        parcel.writeStrongInterface(this.mTransition);
        TransitionFilter transitionFilter = this.mFilter;
        if (transitionFilter != null) {
            parcel.writeTypedObject(transitionFilter, i);
        }
    }

    protected RemoteTransitionCompat(Parcel parcel) {
        this.mFilter = null;
        byte readByte = parcel.readByte();
        IRemoteTransition asInterface = IRemoteTransition.Stub.asInterface(parcel.readStrongBinder());
        TransitionFilter transitionFilter = (readByte & 2) == 0 ? null : (TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR);
        this.mTransition = asInterface;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, asInterface);
        this.mFilter = transitionFilter;
    }
}
