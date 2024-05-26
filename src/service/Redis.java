package service;

import dto.ObjectDTO;

import java.util.Map;

public interface Redis {

    ObjectDTO get(String k);

    void set(String k, ObjectDTO v);

    void checkObjectTtl(String k, int ttl, Map<String, ObjectDTO> map1);


}
