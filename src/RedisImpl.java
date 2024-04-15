import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public interface RedisImpl {
    private Map<String, String> map = new HashMap<>();

    public Object get(String k);

    public void set(String k, ObjectDTO v);

    public void set(String k, ObjectDTO v, int ttl) {


    }

}
