package vn.iotstar.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Dùng cho RestController
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Video;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.IStorageService; // Giả sử bạn có service này để lưu ảnh
import vn.iotstar.service.VideoService;

@RestController // Đổi từ @Controller sang @RestController
@RequestMapping("/api/videos") // Đổi đường dẫn để phân biệt với web thường
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    IStorageService storageService;

    // 1. Lấy tất cả Video (Có phân trang & Sắp xếp)
    @GetMapping
    public ResponseEntity<Page<Video>> getAllVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Video> videos = videoService.findAll(pageable);
        return ResponseEntity.ok(videos);
    }

    // 2. Tìm kiếm Video theo tiêu đề (Có phân trang)
    @GetMapping("/search")
    public ResponseEntity<Page<Video>> searchVideos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Video> videos = videoService.findByTitleContaining(keyword, pageable);
        return ResponseEntity.ok(videos);
    }

    // 3. Lấy chi tiết 1 video
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        Optional<Video> video = videoService.findById(id);
        return video.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Thêm mới Video (Upload ảnh Poster)
    // Lưu ý: Dùng @RequestPart để nhận file và JSON cùng lúc
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Video> createVideo(
            @RequestPart("video") Video video,
            @RequestPart(value = "posterFile", required = false) MultipartFile posterFile) {

        // Tạo ID ngẫu nhiên nếu chưa có
        if (video.getVideoId() == null || video.getVideoId().isEmpty()) {
            video.setVideoId(UUID.randomUUID().toString().substring(0, 8));
        }

        // Xử lý upload ảnh
        if (posterFile != null && !posterFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = storageService.getSorageFilename(posterFile, uuid.toString());
            storageService.store(posterFile, fileName);
            video.setPoster(fileName);
        }
        
        Video savedVideo = videoService.save(video);
        return ResponseEntity.ok(savedVideo);
    }

    // 5. Cập nhật Video
    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable String id,
            @RequestBody Video videoDetail) {
        
        Optional<Video> videoOpt = videoService.findById(id);
        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Video video = videoOpt.get();
        
        // Cập nhật thông tin
        video.setTitle(videoDetail.getTitle());
        video.setDescription(videoDetail.getDescription());
        video.setActive(videoDetail.isActive());
        video.setViews(videoDetail.getViews());
        video.setVideoCode(videoDetail.getVideoCode());
        video.setCategory(videoDetail.getCategory());
        
        // Lưu ý: Ảnh poster thường cập nhật qua API riêng hoặc logic upload khác
        // Ở đây cập nhật thông tin text trước.

        Video updatedVideo = videoService.save(video);
        return ResponseEntity.ok(updatedVideo);
    }

    // 6. Xóa Video
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        if (videoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        videoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}