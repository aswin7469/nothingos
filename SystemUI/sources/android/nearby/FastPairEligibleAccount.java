package android.nearby;

import android.accounts.Account;
import android.nearby.aidl.FastPairEligibleAccountParcel;

public class FastPairEligibleAccount {
    FastPairEligibleAccountParcel mAccountParcel;

    FastPairEligibleAccount(FastPairEligibleAccountParcel fastPairEligibleAccountParcel) {
        this.mAccountParcel = fastPairEligibleAccountParcel;
    }

    public Account getAccount() {
        return this.mAccountParcel.account;
    }

    public boolean isOptIn() {
        return this.mAccountParcel.optIn;
    }

    public static final class Builder {
        private final FastPairEligibleAccountParcel mBuilderParcel;

        public Builder() {
            FastPairEligibleAccountParcel fastPairEligibleAccountParcel = new FastPairEligibleAccountParcel();
            this.mBuilderParcel = fastPairEligibleAccountParcel;
            fastPairEligibleAccountParcel.account = null;
            fastPairEligibleAccountParcel.optIn = false;
        }

        public Builder setAccount(Account account) {
            this.mBuilderParcel.account = account;
            return this;
        }

        public Builder setOptIn(boolean z) {
            this.mBuilderParcel.optIn = z;
            return this;
        }

        public FastPairEligibleAccount build() {
            return new FastPairEligibleAccount(this.mBuilderParcel);
        }
    }
}
