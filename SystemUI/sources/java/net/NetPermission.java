package java.net;

import java.security.BasicPermission;

public final class NetPermission extends BasicPermission {
    public NetPermission(String str) {
        super("");
    }

    public NetPermission(String str, String str2) {
        super("", "");
    }
}
