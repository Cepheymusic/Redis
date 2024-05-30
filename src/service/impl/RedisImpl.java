package service.impl;

import dto.ObjectDTO;
import service.Redis;

import java.io.*;
import java.util.*;
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

        if (isEnablePersistence) {
            //если isEnablePersistence true,
//        // вызываются методы readData() для загрузки данных
//        // и startThread()  для запуска потока,
//        // который будет сохранять данные в хранилище
            startThread();
            readData();
//            startTtlCheckScheduler();
        }
    }

    @Override
    public synchronized ObjectDTO get(String key) {
        return map.get(key);
    }

    @Override
    public synchronized void set(String key, ObjectDTO object) {
        map.put(key, object);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            checkObjectTtl(key, object.getTtl());
        }, object.getTtl(), TimeUnit.MILLISECONDS);
        System.out.println("Объект добавлен: " + map.get(key));
    }

    @Override
    public void checkObjectTtl(String k, int ttl) {
        if (map.containsKey(k)) {
            long currentTime = System.currentTimeMillis();
            long objectTime = map.get(k).getTimeAdded();
            long timeElapsed = currentTime - objectTime;
            if (timeElapsed > ttl) {
                ObjectDTO removedObject = map.remove(k);
                System.out.println("Объект удалён: " + removedObject);
            }
        }
    }

    private void writeData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
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
        scheduled.scheduleAtFixedRate(this::writeData, 12000, 12000, TimeUnit.MILLISECONDS);
    }
}