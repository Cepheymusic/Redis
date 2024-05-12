import dto.ObjectDTO;
import service.impl.RedisImpl;

public class Main {
    static ObjectDTO objectDTO = new ObjectDTO("Sara", 2, System.currentTimeMillis());

    public static void main(String[] args) {
        RedisImpl redisImpl = new RedisImpl(true);
        redisImpl.set("Women", objectDTO);
    }
}