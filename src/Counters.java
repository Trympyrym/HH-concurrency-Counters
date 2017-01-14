import java.util.Map;

/**
 * Created by trympyrym on 13.01.17.
 */
public interface Counters {
    void increment(String tag);
    Map<String, Long> getCountersAndClear();
}
