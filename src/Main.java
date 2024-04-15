import java.math.BigDecimal;

public class Main {
    private static String k;
    private static ObjectDTO v;
    public static boolean isEnablePersistence;

    public static void main(String[] args) {
        Redis redis = new Redis(isEnablePersistence);
        redis.set(k, v, 1);
    }
}