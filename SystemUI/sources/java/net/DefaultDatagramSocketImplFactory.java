package java.net;

import java.p026io.PrintStream;
import java.security.AccessController;
import sun.security.action.GetPropertyAction;

class DefaultDatagramSocketImplFactory {
    static Class<?> prefixImplClass;

    DefaultDatagramSocketImplFactory() {
    }

    static {
        String str = null;
        try {
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("impl.prefix", (String) null));
            if (str2 != null) {
                try {
                    prefixImplClass = Class.forName("java.net." + str2 + "DatagramSocketImpl");
                } catch (Exception unused) {
                    str = str2;
                }
            }
        } catch (Exception unused2) {
            PrintStream printStream = System.err;
            printStream.println("Can't find class: java.net." + str + "DatagramSocketImpl: check impl.prefix property");
        }
    }

    static DatagramSocketImpl createDatagramSocketImpl(boolean z) throws SocketException {
        Class<?> cls = prefixImplClass;
        if (cls == null) {
            return new PlainDatagramSocketImpl();
        }
        try {
            return (DatagramSocketImpl) cls.newInstance();
        } catch (Exception unused) {
            throw new SocketException("can't instantiate DatagramSocketImpl");
        }
    }
}
