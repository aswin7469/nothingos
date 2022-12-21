package androidx.core.content;

import android.content.LocusId;
import androidx.core.util.Preconditions;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public final class LocusIdCompat {
    private final String mId;
    private final LocusId mWrapped;

    public LocusIdCompat(String str) {
        this.mId = (String) Preconditions.checkStringNotEmpty(str, "id cannot be empty");
        this.mWrapped = Api29Impl.create(str);
    }

    public String getId() {
        return this.mId;
    }

    public int hashCode() {
        String str = this.mId;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocusIdCompat locusIdCompat = (LocusIdCompat) obj;
        String str = this.mId;
        if (str != null) {
            return str.equals(locusIdCompat.mId);
        }
        if (locusIdCompat.mId == null) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "LocusIdCompat[" + getSanitizedId() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public LocusId toLocusId() {
        return this.mWrapped;
    }

    public static LocusIdCompat toLocusIdCompat(LocusId locusId) {
        Preconditions.checkNotNull(locusId, "locusId cannot be null");
        return new LocusIdCompat((String) Preconditions.checkStringNotEmpty(Api29Impl.getId(locusId), "id cannot be empty"));
    }

    private String getSanitizedId() {
        return this.mId.length() + "_chars";
    }

    private static class Api29Impl {
        private Api29Impl() {
        }

        static LocusId create(String str) {
            return new LocusId(str);
        }

        static String getId(LocusId locusId) {
            return locusId.getId();
        }
    }
}
