package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        // Gửi dữ liệu từ Java sang HTML
        model.addAttribute("message", "Dữ liệu này được gửi từ Controller!");
        
        // Trả về tên file view (không cần đuôi .html)
        // Nó sẽ tìm file: src/main/resources/templates/home.html
        return "home"; 
    }
    @Autowired
    VideoService videoService;

    // Xem chi tiết phim: http://localhost:9191/watch/123
    @GetMapping("/watch/{id}")
    public String watchVideo(Model model, @PathVariable("id") String id) {
        Optional<Video> opt = videoService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("video", opt.get());
            return "web/video-detail"; // Trả về file giao diện xem phim
        }
        return "redirect:/";
    }
}