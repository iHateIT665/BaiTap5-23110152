package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    // Tìm kiếm phân trang theo tên đăng nhập hoặc họ tên
    Page<User> findByUsernameContainingOrFullnameContaining(String username, String fullname, Pageable pageable);
}