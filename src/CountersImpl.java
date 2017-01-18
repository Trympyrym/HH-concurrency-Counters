import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by trympyrym on 13.01.17.
 *
 */
public class CountersImpl implements Counters {
    private ConcurrentMap<String, AtomicInteger> statistics = new ConcurrentHashMap<>();

    @Override
    public void increment(String tag)
    {
        statistics.putIfAbsent(tag, new AtomicInteger(0));
        statistics.get(tag).incrementAndGet();
    }

    @Override
    public Map<String, Long> getCountersAndClear() {
        Map<String, Long> result = new HashMap<>();
        for (ConcurrentMap.Entry<String, AtomicInteger> entry : statistics.entrySet())
        {
            result.put(entry.getKey(), entry.getValue().longValue());
            statistics.get(entry.getKey()).addAndGet((int)-entry.getValue().longValue());
            statistics.remove(entry.getKey(), 0);
        }
        return result;
    }
}
