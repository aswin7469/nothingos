package dalvik.system;

import android.annotation.SystemApi;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class BlockGuard {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final Policy LAX_POLICY = new Policy() {
        public int getPolicyMask() {
            return 0;
        }

        public void onExplicitGc() {
        }

        public void onNetwork() {
        }

        public void onReadFromDisk() {
        }

        public void onUnbufferedIO() {
        }

        public void onWriteToDisk() {
        }

        public String toString() {
            return "LAX_POLICY";
        }
    };
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final VmPolicy LAX_VM_POLICY;
    private static ThreadLocal<Policy> threadPolicy = new ThreadLocal<Policy>() {
        /* access modifiers changed from: protected */
        public Policy initialValue() {
            return BlockGuard.LAX_POLICY;
        }
    };
    private static volatile VmPolicy vmPolicy;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface Policy {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        int getPolicyMask();

        void onExplicitGc();

        void onNetwork();

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onReadFromDisk();

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onUnbufferedIO();

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onWriteToDisk();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface VmPolicy {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onPathAccess(String str);
    }

    @Deprecated
    public static class BlockGuardPolicyException extends RuntimeException {
        private final String mMessage;
        private final int mPolicyState;
        private final int mPolicyViolated;

        public BlockGuardPolicyException(int i, int i2) {
            this(i, i2, (String) null);
        }

        public BlockGuardPolicyException(int i, int i2, String str) {
            this.mPolicyState = i;
            this.mPolicyViolated = i2;
            this.mMessage = str;
            fillInStackTrace();
        }

        public int getPolicy() {
            return this.mPolicyState;
        }

        public int getPolicyViolation() {
            return this.mPolicyViolated;
        }

        public String getMessage() {
            String str;
            StringBuilder sb = new StringBuilder("policy=");
            sb.append(this.mPolicyState);
            sb.append(" violation=");
            sb.append(this.mPolicyViolated);
            if (this.mMessage == null) {
                str = "";
            } else {
                str = " msg=" + this.mMessage;
            }
            sb.append(str);
            return sb.toString();
        }
    }

    static {
        C42822 r0 = new VmPolicy() {
            public void onPathAccess(String str) {
            }

            public String toString() {
                return "LAX_VM_POLICY";
            }
        };
        LAX_VM_POLICY = r0;
        vmPolicy = r0;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Policy getThreadPolicy() {
        return threadPolicy.get();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setThreadPolicy(Policy policy) {
        threadPolicy.set((Policy) Objects.requireNonNull(policy));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static VmPolicy getVmPolicy() {
        return vmPolicy;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setVmPolicy(VmPolicy vmPolicy2) {
        vmPolicy = (VmPolicy) Objects.requireNonNull(vmPolicy2);
    }

    private BlockGuard() {
    }
}
