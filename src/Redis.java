import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Redis {
    private Map<String, Object> map;
    private boolean isEnablePersistence;
    private String persistenceFilePath = "redis_data.txt";

    public Redis(boolean isEnablePersistence) {
        this.map = new HashMap<>();
        this.isEnablePersistence = isEnablePersistence;
        //если isEnablePersistence true,
        // вызываются методы loadPersistenceData() для загрузки данных
        // и startPersistenceThread() для запуска потока,
        // который будет сохранять данные в хранилище

        if (isEnablePersistence) {
            loadPersistenceData();
            startPersistenceThread();
        }
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void set(String key, Object value) {
        map.put(key, value);
        if (isEnablePersistence) {
            persistData();
        }
    }

    public void set(String key, Object value, int expiration) {
        map.put(key, value);
        if (isEnablePersistence) {
            persistData();
        }
    }
    //запись данных из мапы
    private void persistData() {
        try (FileOutputStream fos = new FileOutputStream(persistenceFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPersistenceData() {
        try (FileInputStream fis = new FileInputStream(persistenceFilePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            map = (Map<String, Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startPersistenceThread() {
        Thread persistenceThread = new Thread(() -> {
            while (isEnablePersistence) {
                persistData();
                try {
                    Thread.sleep(60000); // Сохранение каждую минуту
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        persistenceThread.start();
    }
}