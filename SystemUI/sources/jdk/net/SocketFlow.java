package jdk.net;

public class SocketFlow {
    private static final int ALREADY_CREATED_VALUE = 5;
    public static final int HIGH_PRIORITY = 2;
    private static final int IN_PROGRESS_VALUE = 6;
    public static final int NORMAL_PRIORITY = 1;
    private static final int NOT_CONNECTED_VALUE = 3;
    private static final int NOT_SUPPORTED_VALUE = 4;
    private static final int NO_PERMISSION_VALUE = 2;
    private static final int NO_STATUS_VALUE = 0;
    private static final int OK_VALUE = 1;
    private static final int OTHER_VALUE = 7;
    public static final int UNSET = -1;
    private long bandwidth = -1;
    private int priority = 1;
    private Status status = Status.NO_STATUS;

    public enum Status {
        NO_STATUS(0),
        OK(1),
        NO_PERMISSION(2),
        NOT_CONNECTED(3),
        NOT_SUPPORTED(4),
        ALREADY_CREATED(5),
        IN_PROGRESS(6),
        OTHER(7);
        
        private final int value;

        private Status(int i) {
            this.value = i;
        }

        static Status from(int i) {
            Status status = NO_STATUS;
            if (i == status.value) {
                return status;
            }
            Status status2 = OK;
            if (i == status2.value) {
                return status2;
            }
            Status status3 = NO_PERMISSION;
            if (i == status3.value) {
                return status3;
            }
            Status status4 = NOT_CONNECTED;
            if (i == status4.value) {
                return status4;
            }
            Status status5 = NOT_SUPPORTED;
            if (i == status5.value) {
                return status5;
            }
            Status status6 = ALREADY_CREATED;
            if (i == status6.value) {
                return status6;
            }
            Status status7 = IN_PROGRESS;
            if (i == status7.value) {
                return status7;
            }
            Status status8 = OTHER;
            if (i == status8.value) {
                return status8;
            }
            throw new InternalError("Unknown value: " + i);
        }
    }

    public static SocketFlow create() {
        return new SocketFlow();
    }

    private SocketFlow() {
    }

    public SocketFlow priority(int i) {
        if (i == 1 || i == 2) {
            this.priority = i;
            return this;
        }
        throw new IllegalArgumentException("invalid priority :" + i);
    }

    public SocketFlow bandwidth(long j) {
        if (j >= 0) {
            this.bandwidth = j;
            return this;
        }
        throw new IllegalArgumentException("invalid bandwidth: " + j);
    }

    public int priority() {
        return this.priority;
    }

    public long bandwidth() {
        return this.bandwidth;
    }

    public Status status() {
        return this.status;
    }

    /* access modifiers changed from: package-private */
    public void status(int i) {
        this.status = Status.from(i);
    }

    public String toString() {
        return super.toString() + " [ priority=" + priority() + ", bandwidth=" + bandwidth() + ", status=" + status() + " ]";
    }
}
