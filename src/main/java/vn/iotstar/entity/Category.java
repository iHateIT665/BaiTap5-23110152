package vn.iotstar.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="categories")
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId; // Sửa id thành categoryId cho giống đề (hoặc giữ id cũng được)

    @Column(columnDefinition = "NVARCHAR(200)")
    private String categoryName;

    @Column(columnDefinition = "NVARCHAR(200)")
    private String categoryCode; // Thêm trường này

    @Column(columnDefinition = "NVARCHAR(500)")
    private String images;

    private int status; // Thêm trường này (1: Active, 0: Inactive)

    // Quan hệ: Một Category có nhiều Video
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Video> videos;
    
    @ManyToOne
    @JoinColumn(name = "username") // Khóa ngoại trỏ tới bảng User
    private User user;
}