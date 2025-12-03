package vn.iotstar.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Đặt tên bảng là 'users' để tránh trùng từ khóa hệ thống
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username", length = 50, nullable = false)
    private String username; // Khóa chính là String theo đề bài

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(100)")
    private String fullname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    private boolean admin; // true: Admin, false: User
    private boolean active;

    // Quan hệ: Một User tạo ra nhiều Category
    // mappedBy = "user" nghĩa là bên Category sẽ có thuộc tính tên là 'user'
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categories;
}