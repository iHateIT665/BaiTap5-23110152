package vn.iotstar.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data // Tự động sinh Getter, Setter, toString, equals, hashCode
@Entity
@Table(name = "videos")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "videoId")
    private String videoId;

    @Column(name = "title", columnDefinition = "NVARCHAR(200)")
    private String title;

    @Column(name = "poster", columnDefinition = "NVARCHAR(500)")
    private String poster;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "views")
    private int views; // Có thể đổi thành Integer nếu muốn chấp nhận giá trị null

    @Column(name = "active")
    private boolean active; // Có thể đổi thành Boolean

    @Column(name = "videoCode", columnDefinition = "NVARCHAR(500)")
    private String videoCode;

    // Quan hệ nhiều-một với Category
    @ManyToOne
    @JoinColumn(name = "categoryId")
    // @ToString.Exclude là mẹo quan trọng: Ngăn chặn vòng lặp vô tận khi in log (Video gọi Category, Category gọi lại Video)
    @ToString.Exclude 
    private Category category;

}