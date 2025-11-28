package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.entity.Category;

public interface CategoryService {
    List<Category> findAll();

    void deleteAllById(Iterable<? extends Long> longs);

    List<Category> findByCategoryNameContaining(String name);

    void deleteById(Long aLong);

    Optional<Category> findById(Long id);

    <S extends Category> S save(S entity);
}
