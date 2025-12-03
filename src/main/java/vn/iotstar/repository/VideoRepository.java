package vn.iotstar.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
    // Tìm kiếm video theo tiêu đề (nếu cần sau này)
}