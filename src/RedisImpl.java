import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public interface RedisImpl {

    ObjectDTO get(String k);

    void set(String k, ObjectDTO v);


}
