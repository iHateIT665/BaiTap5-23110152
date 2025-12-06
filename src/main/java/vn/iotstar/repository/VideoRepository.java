package vn.iotstar.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
	Page<Video> findByTitleContaining(String title, Pageable pageable);
    // Tìm kiếm video theo tiêu đề (nếu cần sau này)
}