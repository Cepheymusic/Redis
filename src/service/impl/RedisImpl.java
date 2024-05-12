package service.impl;

import dto.ObjectDTO;
import service.Redis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedisImpl implements Redis {
    private Map<String, ObjectDTO> map;
    private boolean isEnablePersistence;
    private String filePath = "D:\\Project idea\\Redis\\src\\redis_data.txt";

    public RedisImpl(boolean isEnablePersistence) {
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
    public synchronized ObjectDTO get(String key) {
        return map.get(key);
    }

    @Override
    public synchronized void set(String key, ObjectDTO object) {
        map.put(key, object);
        checkObjectTtl(key, object.getTtl());
    }

    @Override
    public void checkObjectTtl(String k, int ttl) {
        if (ttl > 0) {
            List<String> keysToDelete = new ArrayList<>();
            for (Map.Entry<String, ObjectDTO> entry : map.entrySet()) {
                long currentTime = System.currentTimeMillis();
                long entryTime = entry.getValue().getTimeAdded();
                if ((currentTime - entryTime) > ttl) {
                    keysToDelete.add(entry.getKey());
                }
            }
            for (String key : keysToDelete) {
                map.remove(key);
            }
        }
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
    }
}