package com.hmacadamia.repo;

import java.util.List;

public interface RepositorioGenerico<T>{

    List<T> findall();

    T searchById(Long id);
    T searchById(List<T> l, Long id);
    void save(T t);
    void remove(Long id);
}
