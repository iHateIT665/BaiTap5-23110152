package vn.iotstar.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.IStorageService;
import vn.iotstar.service.VideoService;

@Controller
@RequestMapping("admin/videos")
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    CategoryService categoryService; // Cần cái này để lấy danh sách Category

    @Autowired
    IStorageService storageService;

    // 1. Hiển thị danh sách Video
    @GetMapping("")
    public String list(Model model) {
        List<Video> list = videoService.findAll();
        model.addAttribute("videos", list);
        return "admin/videos/video-list";
    }

    // 2. Form Thêm mới
    @GetMapping("add")
    public String add(Model model) {
        Video video = new Video();
        video.setVideoId(UUID.randomUUID().toString().substring(0, 8)); // Tự sinh ID ngắn
        model.addAttribute("video", video);
        
        // QUAN TRỌNG: Gửi danh sách Category sang View để làm Dropdown
        model.addAttribute("categories", categoryService.findAll()); 
        
        return "admin/videos/video-addOrEdit";
    }

    // 3. Form Sửa
    @GetMapping("edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Optional<Video> opt = videoService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("video", opt.get());
            model.addAttribute("categories", categoryService.findAll()); // Cũng phải gửi list category
            return "admin/videos/video-addOrEdit";
        }
        return "redirect:/admin/videos";
    }

    // 4. Lưu dữ liệu
    @PostMapping("saveOrUpdate")
    public String saveOrUpdate(Model model, @ModelAttribute("video") Video video,
                               @RequestParam("posterFile") MultipartFile posterFile) {
        
        // Xử lý upload ảnh Poster
        if (!posterFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = storageService.getSorageFilename(posterFile, uuid.toString());
            storageService.store(posterFile, fileName);
            video.setPoster(fileName);
        } else {
            // Nếu không chọn ảnh mới, giữ ảnh cũ (Logic này cần xử lý kỹ hơn ở View input hidden)
            // Tạm thời để đơn giản ta bỏ qua logic giữ ảnh cũ phức tạp, bạn có thể áp dụng giống Category
        }

        videoService.save(video);
        return "redirect:/admin/videos";
    }

    // 5. Xóa
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") String id) {
        videoService.deleteById(id);
        return "redirect:/admin/videos";
    }
}