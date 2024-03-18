import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Redis {
    private Map<String, Object> map;
    private boolean isEnablePersistence;
    private String persistenceFilePath = "D:\\Project idea\\Redis\\src\\redis_data.txt";

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

    public Object get(String key) {
        return map.get(key);
    }

    public void set(String key, Object value) {
        map.put(key, value);
        if (isEnablePersistence) {
            writeData();
        }
    }

    public void set(String key, Object value, int expiration) {
        map.put(key, value);
        if (isEnablePersistence) {
            writeData();
        }
    }
    //запись данных из мапы
    private void writeData() {
        try (FileOutputStream fos = new FileOutputStream(persistenceFilePath);
             //сериализация
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        try (FileInputStream fis = new FileInputStream(persistenceFilePath);
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
                    Thread.sleep(60000); // Сохранение каждую минуту
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}