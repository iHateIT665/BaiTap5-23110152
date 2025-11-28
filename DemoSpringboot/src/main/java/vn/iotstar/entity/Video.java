package vn.iotstar.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "videoId", length = 50)
    private String videoId; // Bạn có thể dùng String (nhập tay V01) hoặc Long (Identity) tùy ý. Ở đây mình để String cho linh hoạt.

    @Column(name = "title", columnDefinition = "NVARCHAR(200)")
    private String title;

    @Column(name = "poster", columnDefinition = "NVARCHAR(500)")
    private String poster;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private int views;
    private boolean active;

    // Quan hệ: Nhiều Video thuộc về một Category
    @ManyToOne
    @JoinColumn(name = "categoryId") // Tên cột khóa ngoại trong bảng Video
    private Category category;
}