package service;

import dto.ObjectDTO;

public interface Redis {

    ObjectDTO get(String k);

    void set(String k, ObjectDTO v);

    void checkObjectTtl(String k, int ttl);


}
