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
            startThread();
            readData();
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

    private void startThread() {
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        scheduled.scheduleAtFixedRate(this::writeData, 1000, 1000, TimeUnit.MILLISECONDS);
        scheduled.scheduleAtFixedRate(this::checkObjectsTtl, 1000, 1000, TimeUnit.MILLISECONDS);
        scheduled.scheduleAtFixedRate(this::readData, 1000, 1000, TimeUnit.MILLISECONDS);
    }


    public void checkObjectsTtl() {
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, ObjectDTO> entry : map.entrySet()) {
            checkObjectTtl(entry.getKey(), entry.getValue().getTtl());
            if (!map.containsKey(entry.getKey())) {
                keysToRemove.add(entry.getKey());
            }
        }
        for (String key : keysToRemove) {
            map.remove(key);
        }
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

    private synchronized void writeData() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(map);
            System.out.println("Данные записаны в файл");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void readData() {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Map<String, ObjectDTO> newMap = (Map<String, ObjectDTO>) ois.readObject();
//            if (!newMap.equals(map)) {
//                map = newMap;
                System.out.println("Данные прочитаны из файла");
//            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}