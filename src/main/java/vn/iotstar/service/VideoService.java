package vn.iotstar.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Video;

public interface VideoService {
    List<Video> findAll();
    Optional<Video> findById(String id);
    Video save(Video video);
    void deleteById(String id);

    // THÊM 2 HÀM NÀY VÀO ĐỂ KHỚP VỚI IMPL
    Page<Video> findByTitleContaining(String title, Pageable pageable);
    Page<Video> findAll(Pageable pageable);
}