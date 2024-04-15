import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Redis implements RedisImpl {
    //данные из мап он же редис должны сохраняться на диск то есть в файл, в случае слёта сервера то есть мапы
    // далее, данные должны считываться из файла.
    //
    private Map<String, Object> map;
    //создать dto object со структурой значение и persistance
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
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, ObjectDTO value) {
        map.put(key, value);
    }

    public void set(String key, Object value, int expiration) {
        map.put(key, value);
    }

    //запись данных из мапы
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
            map = (Map<String, Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startThread() {
        Thread thread = new Thread(() -> {
            while (isEnablePersistence) {
                writeData();
                try {
                    Thread.sleep(60000);
                    //использовать таймер
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}