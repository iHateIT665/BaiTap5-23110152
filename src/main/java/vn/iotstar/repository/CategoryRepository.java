package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCategoryNameContaining(String categoryName);

    Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);

}
