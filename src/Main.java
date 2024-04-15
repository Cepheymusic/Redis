import java.math.BigDecimal;

public class Main {
    private static String v;
    private static String k;

    public static void main(String[] args) {
        RedisImpl redis = new RedisImpl();
        redis.set(k, v, BigDecimal.valueOf(1));
    }
}