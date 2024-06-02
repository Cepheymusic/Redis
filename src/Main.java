import dto.ObjectDTO;
import service.impl.RedisImpl;

public class Main {
    static ObjectDTO objectDTO = new ObjectDTO("Sara", 2000, System.currentTimeMillis());
    static RedisImpl redisImpl = new RedisImpl(true);

    public static synchronized void main(String[] args) {
        redisImpl.set("Women", objectDTO);
        System.out.println(redisImpl.get("Women"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(redisImpl.get("Women"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(redisImpl.get("Women"));
    }
}