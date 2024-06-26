package com.hmacadamia.repo;

import java.util.List;

public interface Repositorio <T>{

    List<T> findall();

    T searchbyid(Long id);

    void save(T t);

    void remove(Long id);
}
