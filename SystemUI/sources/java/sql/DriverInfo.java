package java.sql;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

/* compiled from: DriverManager */
class DriverInfo {
    final Driver driver;

    DriverInfo(Driver driver2) {
        this.driver = driver2;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DriverInfo) && this.driver == ((DriverInfo) obj).driver;
    }

    public int hashCode() {
        return this.driver.hashCode();
    }

    public String toString() {
        return "driver[className=" + this.driver + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
