package vn.iotstar.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
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
    private int views;

    @Column(name = "active")
    private boolean active;

    @Column(name = "videoCode", columnDefinition = "NVARCHAR(500)")
    private String videoCode;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    // =======================================================
    // GETTER & SETTER THỦ CÔNG (Đảm bảo Thymeleaf đọc được)
    // =======================================================

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // ĐÂY LÀ CÁI QUAN TRỌNG NHẤT
    public String getVideoCode() { return videoCode; }
    public void setVideoCode(String videoCode) { this.videoCode = videoCode; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}