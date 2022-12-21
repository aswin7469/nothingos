package java.lang.invoke;

class MethodHandleNatives {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    static boolean refKindIsField(byte b) {
        return b <= 4;
    }

    static boolean refKindIsValid(int i) {
        return i > 0 && i < 10;
    }

    static String refKindName(byte b) {
        switch (b) {
            case 1:
                return "getField";
            case 2:
                return "getStatic";
            case 3:
                return "putField";
            case 4:
                return "putStatic";
            case 5:
                return "invokeVirtual";
            case 6:
                return "invokeStatic";
            case 7:
                return "invokeSpecial";
            case 8:
                return "newInvokeSpecial";
            case 9:
                return "invokeInterface";
            default:
                return "REF_???";
        }
    }

    MethodHandleNatives() {
    }

    static class Constants {
        static final byte REF_LIMIT = 10;
        static final byte REF_NONE = 0;
        static final byte REF_getField = 1;
        static final byte REF_getStatic = 2;
        static final byte REF_invokeInterface = 9;
        static final byte REF_invokeSpecial = 7;
        static final byte REF_invokeStatic = 6;
        static final byte REF_invokeVirtual = 5;
        static final byte REF_newInvokeSpecial = 8;
        static final byte REF_putField = 3;
        static final byte REF_putStatic = 4;

        Constants() {
        }
    }
}
