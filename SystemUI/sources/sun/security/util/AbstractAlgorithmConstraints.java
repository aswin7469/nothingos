package sun.security.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Set;

public abstract class AbstractAlgorithmConstraints implements AlgorithmConstraints {
    protected final AlgorithmDecomposer decomposer;

    protected AbstractAlgorithmConstraints(AlgorithmDecomposer algorithmDecomposer) {
        this.decomposer = algorithmDecomposer;
    }

    static String[] getAlgorithms(final String str) {
        String[] strArr;
        String str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty(String.this);
            }
        });
        if (str2 == null || str2.isEmpty()) {
            strArr = null;
        } else {
            if (str2.length() >= 2 && str2.charAt(0) == '\"' && str2.charAt(str2.length() - 1) == '\"') {
                str2 = str2.substring(1, str2.length() - 1);
            }
            strArr = str2.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = strArr[i].trim();
            }
        }
        return strArr == null ? new String[0] : strArr;
    }

    static boolean checkAlgorithm(String[] strArr, String str, AlgorithmDecomposer algorithmDecomposer) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("No algorithm name specified");
        }
        Set<String> set = null;
        for (String str2 : strArr) {
            if (str2 != null && !str2.isEmpty()) {
                if (str2.equalsIgnoreCase(str)) {
                    return false;
                }
                if (set == null) {
                    set = algorithmDecomposer.decompose(str);
                }
                for (String equalsIgnoreCase : set) {
                    if (str2.equalsIgnoreCase(equalsIgnoreCase)) {
                        return false;
                    }
                }
                continue;
            }
        }
        return true;
    }
}
