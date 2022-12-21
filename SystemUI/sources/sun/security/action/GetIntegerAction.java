package sun.security.action;

import java.security.PrivilegedAction;

public class GetIntegerAction implements PrivilegedAction<Integer> {
    private boolean defaultSet = false;
    private int defaultVal;
    private String theProp;

    public GetIntegerAction(String str) {
        this.theProp = str;
    }

    public GetIntegerAction(String str, int i) {
        this.theProp = str;
        this.defaultVal = i;
    }

    public Integer run() {
        Integer integer = Integer.getInteger(this.theProp);
        return (integer != null || !this.defaultSet) ? integer : new Integer(this.defaultVal);
    }
}
