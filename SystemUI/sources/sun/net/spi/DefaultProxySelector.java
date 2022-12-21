package sun.net.spi;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import sun.net.NetProperties;
import sun.net.SocksProxy;

public class DefaultProxySelector extends ProxySelector {
    private static final String SOCKS_PROXY_VERSION = "socksProxyVersion";
    private static boolean hasSystemProxies = false;
    static final String[][] props = {new String[]{"http", "http.proxy", "proxy", "socksProxy"}, new String[]{"https", "https.proxy", "proxy", "socksProxy"}, new String[]{"ftp", "ftp.proxy", "ftpProxy", "proxy", "socksProxy"}, new String[]{"gopher", "gopherProxy", "socksProxy"}, new String[]{"socket", "socksProxy"}};

    static class NonProxyInfo {
        static final String defStringVal = "localhost|127.*|[::1]|0.0.0.0|[::0]";
        static NonProxyInfo ftpNonProxyInfo = new NonProxyInfo("ftp.nonProxyHosts", (String) null, (Pattern) null, defStringVal);
        static NonProxyInfo httpNonProxyInfo = new NonProxyInfo("http.nonProxyHosts", (String) null, (Pattern) null, defStringVal);
        static NonProxyInfo httpsNonProxyInfo = new NonProxyInfo("https.nonProxyHosts", (String) null, (Pattern) null, defStringVal);
        static NonProxyInfo socksNonProxyInfo = new NonProxyInfo("socksNonProxyHosts", (String) null, (Pattern) null, defStringVal);
        final String defaultVal;
        String hostsSource;
        Pattern pattern;
        final String property;

        NonProxyInfo(String str, String str2, Pattern pattern2, String str3) {
            this.property = str;
            this.hostsSource = str2;
            this.pattern = pattern2;
            this.defaultVal = str3;
        }
    }

    public List<Proxy> select(URI uri) {
        final NonProxyInfo nonProxyInfo;
        String authority;
        if (uri != null) {
            final String scheme = uri.getScheme();
            String host = uri.getHost();
            if (host == null && (authority = uri.getAuthority()) != null) {
                int indexOf = authority.indexOf(64);
                if (indexOf >= 0) {
                    authority = authority.substring(indexOf + 1);
                }
                int lastIndexOf = authority.lastIndexOf(58);
                if (lastIndexOf >= 0) {
                    authority = authority.substring(0, lastIndexOf);
                }
                host = authority;
            }
            if (scheme == null || host == null) {
                throw new IllegalArgumentException("protocol = " + scheme + " host = " + host);
            }
            ArrayList arrayList = new ArrayList(1);
            if ("http".equalsIgnoreCase(scheme)) {
                nonProxyInfo = NonProxyInfo.httpNonProxyInfo;
            } else if ("https".equalsIgnoreCase(scheme)) {
                nonProxyInfo = NonProxyInfo.httpsNonProxyInfo;
            } else if ("ftp".equalsIgnoreCase(scheme)) {
                nonProxyInfo = NonProxyInfo.ftpNonProxyInfo;
            } else {
                nonProxyInfo = "socket".equalsIgnoreCase(scheme) ? NonProxyInfo.socksNonProxyInfo : null;
            }
            final String lowerCase = host.toLowerCase();
            arrayList.add((Proxy) AccessController.doPrivileged(new PrivilegedAction<Proxy>() {
                public Proxy run() {
                    for (int i = 0; i < DefaultProxySelector.props.length; i++) {
                        if (DefaultProxySelector.props[i][0].equalsIgnoreCase(scheme)) {
                            int i2 = 1;
                            String str = null;
                            while (i2 < DefaultProxySelector.props[i].length) {
                                str = NetProperties.get(DefaultProxySelector.props[i][i2] + "Host");
                                if (str != null && str.length() != 0) {
                                    break;
                                }
                                i2++;
                            }
                            if (str == null || str.length() == 0) {
                                return Proxy.NO_PROXY;
                            }
                            NonProxyInfo nonProxyInfo = nonProxyInfo;
                            if (nonProxyInfo != null) {
                                String str2 = NetProperties.get(nonProxyInfo.property);
                                synchronized (nonProxyInfo) {
                                    if (str2 == null) {
                                        if (nonProxyInfo.defaultVal != null) {
                                            str2 = nonProxyInfo.defaultVal;
                                        } else {
                                            nonProxyInfo.hostsSource = null;
                                            nonProxyInfo.pattern = null;
                                        }
                                    } else if (str2.length() != 0) {
                                        str2 = str2 + "|localhost|127.*|[::1]|0.0.0.0|[::0]";
                                    }
                                    if (str2 != null && !str2.equals(nonProxyInfo.hostsSource)) {
                                        nonProxyInfo.pattern = DefaultProxySelector.toPattern(str2);
                                        nonProxyInfo.hostsSource = str2;
                                    }
                                    if (DefaultProxySelector.shouldNotUseProxyFor(nonProxyInfo.pattern, lowerCase)) {
                                        Proxy proxy = Proxy.NO_PROXY;
                                        return proxy;
                                    }
                                }
                            }
                            int intValue = NetProperties.getInteger(DefaultProxySelector.props[i][i2] + "Port", 0).intValue();
                            if (intValue == 0 && i2 < DefaultProxySelector.props[i].length - 1) {
                                for (int i3 = 1; i3 < DefaultProxySelector.props[i].length - 1; i3++) {
                                    if (i3 != i2 && intValue == 0) {
                                        intValue = NetProperties.getInteger(DefaultProxySelector.props[i][i3] + "Port", 0).intValue();
                                    }
                                }
                            }
                            if (intValue == 0) {
                                if (i2 == DefaultProxySelector.props[i].length - 1) {
                                    intValue = DefaultProxySelector.this.defaultPort("socket");
                                } else {
                                    intValue = DefaultProxySelector.this.defaultPort(scheme);
                                }
                            }
                            InetSocketAddress createUnresolved = InetSocketAddress.createUnresolved(str, intValue);
                            if (i2 == DefaultProxySelector.props[i].length - 1) {
                                return SocksProxy.create(createUnresolved, NetProperties.getInteger(DefaultProxySelector.SOCKS_PROXY_VERSION, 5).intValue());
                            }
                            return new Proxy(Proxy.Type.HTTP, createUnresolved);
                        }
                    }
                    return Proxy.NO_PROXY;
                }
            }));
            return arrayList;
        }
        throw new IllegalArgumentException("URI can't be null.");
    }

    public void connectFailed(URI uri, SocketAddress socketAddress, IOException iOException) {
        if (uri == null || socketAddress == null || iOException == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }
    }

    /* access modifiers changed from: private */
    public int defaultPort(String str) {
        if ("http".equalsIgnoreCase(str)) {
            return 80;
        }
        if ("https".equalsIgnoreCase(str)) {
            return 443;
        }
        if ("ftp".equalsIgnoreCase(str)) {
            return 80;
        }
        if ("socket".equalsIgnoreCase(str)) {
            return SocksConsts.DEFAULT_PORT;
        }
        if ("gopher".equalsIgnoreCase(str)) {
            return 80;
        }
        return -1;
    }

    static boolean shouldNotUseProxyFor(Pattern pattern, String str) {
        if (pattern == null || str.isEmpty()) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    static Pattern toPattern(String str) {
        StringJoiner stringJoiner = new StringJoiner("|");
        boolean z = true;
        for (String str2 : str.split("\\|")) {
            if (!str2.isEmpty()) {
                stringJoiner.add(disjunctToRegex(str2.toLowerCase()));
                z = false;
            }
        }
        if (z) {
            return null;
        }
        return Pattern.compile(stringJoiner.toString());
    }

    static String disjunctToRegex(String str) {
        if (str.startsWith("*")) {
            return ".*" + Pattern.quote(str.substring(1));
        } else if (!str.endsWith("*")) {
            return Pattern.quote(str);
        } else {
            return Pattern.quote(str.substring(0, str.length() - 1)) + ".*";
        }
    }
}
