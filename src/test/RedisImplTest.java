package test;

import dto.ObjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.impl.RedisImpl;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RedisImplTest {
    private RedisImpl redis;

    @BeforeEach
    public void setUp() {
        redis = new RedisImpl(false);
    }

    @Test
    void getAndReturnObject() {
        ObjectDTO object = new ObjectDTO("value", 60, System.currentTimeMillis());
        redis.set("key", object);
        ObjectDTO result = redis.get("key");
        assertEquals(object, result);
    }

    @Test
    void setAndReturnObject() {
        ObjectDTO object = new ObjectDTO("value", 60, System.currentTimeMillis());


    }

    @Test
    void checkObjectTtl() {
        ObjectDTO object = new ObjectDTO("value", 1, System.currentTimeMillis());
        redis.set("key", object);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectDTO result = redis.get("key");
        assertNull(result);
    }
}