import java.math.BigDecimal;

public class Main {
    static ObjectDTO objectDTO = new ObjectDTO("Sara", 2);

    public static void main(String[] args) {
        Redis redis = new Redis(true);
        redis.set("Women", objectDTO);
    }
}