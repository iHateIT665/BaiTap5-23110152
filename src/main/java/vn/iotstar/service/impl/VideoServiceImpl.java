package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Video;
import vn.iotstar.repository.VideoRepository;
import vn.iotstar.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoRepository videoRepository;

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Optional<Video> findById(String id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public void deleteById(String id) {
        videoRepository.deleteById(id);
    }
    public Page<Video> findByTitleContaining(String title, Pageable pageable) {
        return videoRepository.findByTitleContaining(title, pageable);
    }
    
    // Hàm quan trọng: Chỉ phân trang (không tìm kiếm)
    public Page<Video> findAll(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }
}