package vn.iotstar.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.User;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    Page<User> search(String keyword, Pageable pageable);
    Optional<User> findById(String username);
    User save(User user);
    void deleteById(String username);
}