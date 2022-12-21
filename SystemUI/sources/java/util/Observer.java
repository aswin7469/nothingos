package java.util;

@Deprecated(since = "9")
public interface Observer {
    void update(Observable observable, Object obj);
}
