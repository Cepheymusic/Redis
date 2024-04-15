import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RedisImpl {
    private Map<String, String> map = new HashMap<>();

    public Object get(String k) {
        return map.get(k);
    }

    public void set(String k, String v, BigDecimal ttl) {


    }

}
