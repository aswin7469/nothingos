package java.net;

import com.android.launcher3.icons.cache.BaseIconCache;
import dalvik.system.VMRuntime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryCookieStore implements CookieStore {
    private final boolean applyMCompatibility;
    private ReentrantLock lock;
    private Map<URI, List<HttpCookie>> uriIndex;

    public InMemoryCookieStore() {
        this(VMRuntime.getRuntime().getTargetSdkVersion());
    }

    public InMemoryCookieStore(int i) {
        this.uriIndex = null;
        this.lock = null;
        this.uriIndex = new HashMap();
        boolean z = false;
        this.lock = new ReentrantLock(false);
        this.applyMCompatibility = i <= 23 ? true : z;
    }

    public void add(URI uri, HttpCookie httpCookie) {
        if (httpCookie != null) {
            this.lock.lock();
            try {
                addIndex(this.uriIndex, getEffectiveURI(uri), httpCookie);
            } finally {
                this.lock.unlock();
            }
        } else {
            throw new NullPointerException("cookie is null");
        }
    }

    public List<HttpCookie> get(URI uri) {
        if (uri != null) {
            ArrayList arrayList = new ArrayList();
            this.lock.lock();
            try {
                getInternal1(arrayList, this.uriIndex, uri.getHost());
                getInternal2(arrayList, this.uriIndex, getEffectiveURI(uri));
                return arrayList;
            } finally {
                this.lock.unlock();
            }
        } else {
            throw new NullPointerException("uri is null");
        }
    }

    public List<HttpCookie> getCookies() {
        ArrayList arrayList = new ArrayList();
        this.lock.lock();
        try {
            for (List<HttpCookie> it : this.uriIndex.values()) {
                Iterator it2 = it.iterator();
                while (it2.hasNext()) {
                    HttpCookie httpCookie = (HttpCookie) it2.next();
                    if (httpCookie.hasExpired()) {
                        it2.remove();
                    } else if (!arrayList.contains(httpCookie)) {
                        arrayList.add(httpCookie);
                    }
                }
            }
            return Collections.unmodifiableList(arrayList);
        } finally {
            Collections.unmodifiableList(arrayList);
            this.lock.unlock();
        }
    }

    public List<URI> getURIs() {
        this.lock.lock();
        try {
            ArrayList arrayList = new ArrayList(this.uriIndex.keySet());
            arrayList.remove((Object) null);
            return Collections.unmodifiableList(arrayList);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean remove(URI uri, HttpCookie httpCookie) {
        if (httpCookie != null) {
            this.lock.lock();
            try {
                URI effectiveURI = getEffectiveURI(uri);
                if (this.uriIndex.get(effectiveURI) != null) {
                    List list = this.uriIndex.get(effectiveURI);
                    if (list != null) {
                        boolean remove = list.remove((Object) httpCookie);
                        this.lock.unlock();
                        return remove;
                    }
                }
                return false;
            } finally {
                this.lock.unlock();
            }
        } else {
            throw new NullPointerException("cookie is null");
        }
    }

    public boolean removeAll() {
        this.lock.lock();
        try {
            boolean z = !this.uriIndex.isEmpty();
            this.uriIndex.clear();
            return z;
        } finally {
            this.lock.unlock();
        }
    }

    private boolean netscapeDomainMatches(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        boolean equalsIgnoreCase = ".local".equalsIgnoreCase(str);
        int indexOf = str.indexOf(46);
        if (indexOf == 0) {
            indexOf = str.indexOf(46, 1);
        }
        if (!equalsIgnoreCase && (indexOf == -1 || indexOf == str.length() - 1)) {
            return false;
        }
        if (str2.indexOf(46) == -1 && equalsIgnoreCase) {
            return true;
        }
        int length = str2.length() - str.length();
        if (length == 0) {
            return str2.equalsIgnoreCase(str);
        }
        if (length > 0) {
            String substring = str2.substring(length);
            if (!this.applyMCompatibility || str.startsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
                return substring.equalsIgnoreCase(str);
            }
            return false;
        } else if (length == -1 && str.charAt(0) == '.' && str2.equalsIgnoreCase(str.substring(1))) {
            return true;
        } else {
            return false;
        }
    }

    private void getInternal1(List<HttpCookie> list, Map<URI, List<HttpCookie>> map, String str) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<URI, List<HttpCookie>> value : map.entrySet()) {
            List<HttpCookie> list2 = (List) value.getValue();
            for (HttpCookie httpCookie : list2) {
                String domain = httpCookie.getDomain();
                if ((httpCookie.getVersion() == 0 && netscapeDomainMatches(domain, str)) || (httpCookie.getVersion() == 1 && HttpCookie.domainMatches(domain, str))) {
                    if (httpCookie.hasExpired()) {
                        arrayList.add(httpCookie);
                    } else if (!list.contains(httpCookie)) {
                        list.add(httpCookie);
                    }
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                list2.remove((Object) (HttpCookie) it.next());
            }
            arrayList.clear();
        }
    }

    private <T extends Comparable<T>> void getInternal2(List<HttpCookie> list, Map<T, List<HttpCookie>> map, T t) {
        List list2;
        for (T t2 : map.keySet()) {
            if ((t2 == t || (t2 != null && t.compareTo(t2) == 0)) && (list2 = map.get(t2)) != null) {
                Iterator it = list2.iterator();
                while (it.hasNext()) {
                    HttpCookie httpCookie = (HttpCookie) it.next();
                    if (httpCookie.hasExpired()) {
                        it.remove();
                    } else if (!list.contains(httpCookie)) {
                        list.add(httpCookie);
                    }
                }
            }
        }
    }

    private <T> void addIndex(Map<T, List<HttpCookie>> map, T t, HttpCookie httpCookie) {
        List list = map.get(t);
        if (list != null) {
            list.remove((Object) httpCookie);
            list.add(httpCookie);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(httpCookie);
        map.put(t, arrayList);
    }

    private URI getEffectiveURI(URI uri) {
        if (uri == null) {
            return null;
        }
        try {
            return new URI("http", uri.getHost(), (String) null, (String) null, (String) null);
        } catch (URISyntaxException unused) {
            return uri;
        }
    }
}
