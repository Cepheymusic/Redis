import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RedisImpl {
    private Map<String, String> map = new HashMap<>();

    public String get(String k) {
        return map.get(k);
    }

    public void set(String k, String v, BigDecimal ttl) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.put(k, v);
            }
        }, ttl.longValue() * 60 * 60 * 1000);

    }

}
