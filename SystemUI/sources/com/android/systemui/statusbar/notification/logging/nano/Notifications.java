package com.android.systemui.statusbar.notification.logging.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.p026io.IOException;

public interface Notifications {

    public static final class Notification extends MessageNano {
        public static final int SECTION_ALERTING = 4;
        public static final int SECTION_FOREGROUND_SERVICE = 6;
        public static final int SECTION_HEADS_UP = 1;
        public static final int SECTION_MEDIA_CONTROLS = 2;
        public static final int SECTION_PEOPLE = 3;
        public static final int SECTION_SILENT = 5;
        public static final int SECTION_UNKNOWN = 0;
        private static volatile Notification[] _emptyArray;
        public int groupInstanceId;
        public int instanceId;
        public boolean isGroupSummary;
        public String packageName;
        public int section;
        public int uid;

        public static Notification[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new Notification[0];
                    }
                }
            }
            return _emptyArray;
        }

        public Notification() {
            clear();
        }

        public Notification clear() {
            this.uid = 0;
            this.packageName = "";
            this.instanceId = 0;
            this.groupInstanceId = 0;
            this.isGroupSummary = false;
            this.section = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.uid;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            if (!this.packageName.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.packageName);
            }
            int i2 = this.instanceId;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(3, i2);
            }
            int i3 = this.groupInstanceId;
            if (i3 != 0) {
                codedOutputByteBufferNano.writeInt32(4, i3);
            }
            boolean z = this.isGroupSummary;
            if (z) {
                codedOutputByteBufferNano.writeBool(5, z);
            }
            int i4 = this.section;
            if (i4 != 0) {
                codedOutputByteBufferNano.writeInt32(6, i4);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int computeSerializedSize = super.computeSerializedSize();
            int i = this.uid;
            if (i != 0) {
                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            if (!this.packageName.equals("")) {
                computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.packageName);
            }
            int i2 = this.instanceId;
            if (i2 != 0) {
                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
            }
            int i3 = this.groupInstanceId;
            if (i3 != 0) {
                computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i3);
            }
            boolean z = this.isGroupSummary;
            if (z) {
                computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z);
            }
            int i4 = this.section;
            return i4 != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(6, i4) : computeSerializedSize;
        }

        public Notification mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int readTag = codedInputByteBufferNano.readTag();
                if (readTag == 0) {
                    return this;
                }
                if (readTag == 8) {
                    this.uid = codedInputByteBufferNano.readInt32();
                } else if (readTag == 18) {
                    this.packageName = codedInputByteBufferNano.readString();
                } else if (readTag == 24) {
                    this.instanceId = codedInputByteBufferNano.readInt32();
                } else if (readTag == 32) {
                    this.groupInstanceId = codedInputByteBufferNano.readInt32();
                } else if (readTag != 40) {
                    if (readTag == 48) {
                        int readInt32 = codedInputByteBufferNano.readInt32();
                        switch (readInt32) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.section = readInt32;
                                break;
                        }
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                } else {
                    this.isGroupSummary = codedInputByteBufferNano.readBool();
                }
            }
        }

        public static Notification parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (Notification) MessageNano.mergeFrom(new Notification(), bArr);
        }

        public static Notification parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new Notification().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class NotificationList extends MessageNano {
        private static volatile NotificationList[] _emptyArray;
        public Notification[] notifications;

        public static NotificationList[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NotificationList[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NotificationList() {
            clear();
        }

        public NotificationList clear() {
            this.notifications = Notification.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            Notification[] notificationArr = this.notifications;
            if (notificationArr != null && notificationArr.length > 0) {
                int i = 0;
                while (true) {
                    Notification[] notificationArr2 = this.notifications;
                    if (i >= notificationArr2.length) {
                        break;
                    }
                    Notification notification = notificationArr2[i];
                    if (notification != null) {
                        codedOutputByteBufferNano.writeMessage(1, notification);
                    }
                    i++;
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int computeSerializedSize = super.computeSerializedSize();
            Notification[] notificationArr = this.notifications;
            if (notificationArr != null && notificationArr.length > 0) {
                int i = 0;
                while (true) {
                    Notification[] notificationArr2 = this.notifications;
                    if (i >= notificationArr2.length) {
                        break;
                    }
                    Notification notification = notificationArr2[i];
                    if (notification != null) {
                        computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, notification);
                    }
                    i++;
                }
            }
            return computeSerializedSize;
        }

        public NotificationList mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int readTag = codedInputByteBufferNano.readTag();
                if (readTag == 0) {
                    return this;
                }
                if (readTag == 10) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                    Notification[] notificationArr = this.notifications;
                    int length = notificationArr == null ? 0 : notificationArr.length;
                    int i = repeatedFieldArrayLength + length;
                    Notification[] notificationArr2 = new Notification[i];
                    if (length != 0) {
                        System.arraycopy((Object) notificationArr, 0, (Object) notificationArr2, 0, length);
                    }
                    while (length < i - 1) {
                        Notification notification = new Notification();
                        notificationArr2[length] = notification;
                        codedInputByteBufferNano.readMessage(notification);
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    Notification notification2 = new Notification();
                    notificationArr2[length] = notification2;
                    codedInputByteBufferNano.readMessage(notification2);
                    this.notifications = notificationArr2;
                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                    return this;
                }
            }
        }

        public static NotificationList parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (NotificationList) MessageNano.mergeFrom(new NotificationList(), bArr);
        }

        public static NotificationList parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new NotificationList().mergeFrom(codedInputByteBufferNano);
        }
    }
}
