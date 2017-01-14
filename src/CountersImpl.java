import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by trympyrym on 13.01.17.
 *
 *  ConcurrentLinkedQueue предоствляет параллельные потокобезопасные операции
 *  добавления элемента в конец очереди и удаления его из начала.
 *  Таким образом, множество потоков, выполняющих increment не будут мешать
 *  друг другу и poll-у из метода getCountersAndClear.
 *  Метод getCountersAndClear берет по одному элементу с начала очереди и
 *  формирует результат. Подсчет производится именно тут, чтобы разгрузить
 *  часто используемый метод increment. Параллельная работа двух getCountersAndClear
 *  сделает очень плохо, поэтому поставлен дополнительный synchronized блок.
 */
public class CountersImpl implements Counters {
    private ConcurrentLinkedQueue<String> tags = new ConcurrentLinkedQueue<>();
    private Object Lock = new Object();

    @Override
    public void increment(String tag) {
        tags.add(tag);
    }

//    @Override
//    public Map<String, Long> getCountersAndClear() {
//        Map<String, Long> result = new HashMap<>();
//        for (String tag: tags) {
//            Long prevValue = (result.containsKey(tag)) ? result.get(tag) : new Long(0);
//            Long newValue = prevValue + 1;
//            result.put(tag, newValue);
//            tags.poll();
//        }
//        return result;
//    }

    @Override
    public Map<String, Long> getCountersAndClear() {
        synchronized (Lock)
        {
            Long size = (long)tags.size();
            Map<String, Long> result = new HashMap<>();
            for (long index = 0; index < size; index++) {
                String tag = tags.poll();
                Long prevValue = (result.containsKey(tag)) ? result.get(tag) : new Long(0);
                Long newValue = prevValue + 1;
                result.put(tag, newValue);
            }
            return result;
        }
    }
}
