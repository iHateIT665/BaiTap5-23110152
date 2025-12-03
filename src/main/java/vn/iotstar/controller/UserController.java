package vn.iotstar.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.User;
import vn.iotstar.service.IStorageService;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("admin/users")
public class UserController {

    @Autowired UserService userService;
    @Autowired IStorageService storageService;

    @RequestMapping("")
    public String list(Model model, 
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
        
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> resultPage;

        if (StringUtils.hasText(keyword)) {
            resultPage = userService.search(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            resultPage = userService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(page + 2, totalPages);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("userPage", resultPage);
        return "admin/users/user-list";
    }

    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false); // Cờ báo là đang thêm mới
        return "admin/users/user-addOrEdit";
    }

    @GetMapping("edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        Optional<User> opt = userService.findById(username);
        if (opt.isPresent()) {
            model.addAttribute("user", opt.get());
            model.addAttribute("isEdit", true); // Cờ báo là đang sửa
            return "admin/users/user-addOrEdit";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("saveOrUpdate")
    public String saveOrUpdate(Model model, @ModelAttribute("user") User user,
                               @RequestParam("imageFile") MultipartFile imageFile) {
        
        // Xử lý upload ảnh
        if (!imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = storageService.getSorageFilename(imageFile, uuid.toString());
            storageService.store(imageFile, fileName);
            user.setImages(fileName);
        } 
        // Nếu không chọn ảnh mới, user.getImages() sẽ giữ giá trị cũ (nhờ input hidden ở view)

        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("delete/{username}")
    public String delete(@PathVariable("username") String username) {
        userService.deleteById(username);
        return "redirect:/admin/users";
    }
}