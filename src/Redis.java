import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Redis implements RedisImpl {
    private Map<String, ObjectDTO> map;
    private boolean isEnablePersistence;
    private String filePath = "D:\\Project idea\\Redis\\src\\redis_data.txt";

    public Redis(boolean isEnablePersistence) {
        this.map = new HashMap<>();
        this.isEnablePersistence = isEnablePersistence;
        //если isEnablePersistence true,
        // вызываются методы readData() для загрузки данных
        // и startThread() для запуска потока,
        // который будет сохранять данные в хранилище

        if (isEnablePersistence) {
            readData();
            startThread();
        }
    }

    @Override
    public ObjectDTO get(String key) {
        return map.get(key);
    }

    public void set(String key, ObjectDTO object) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> map.remove(key), object.getTtl(), TimeUnit.SECONDS);
        map.put(key, object);
    }

    private void writeData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             //сериализация
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            map = (Map<String, ObjectDTO>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void startThread() {
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        while (isEnablePersistence) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    writeData();
                }
            };
            scheduled.schedule(task, 2000, TimeUnit.MILLISECONDS);
        }
//        Thread thread = new Thread(() -> {
//            while (isEnablePersistence) {
//                writeData();
//                try {
//                    Thread.sleep(60000);
//                    //использовать таймер
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
    }
}