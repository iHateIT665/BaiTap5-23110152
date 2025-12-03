package vn.iotstar.controller;

import java.util.Optional;
import java.util.List;                  // cho List
import java.util.stream.IntStream;      // cho IntStream
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.CategoryService;

import vn.iotstar.service.IStorageService; // Import service lưu file
import java.util.UUID; // Import UUID

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	IStorageService storageService;

	@RequestMapping("")
    public String list(Model model, 
                       @RequestParam(name = "name", required = false) String name,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
        
        // Xử lý phân trang (Page trong JPA bắt đầu từ 0, nên ta trừ 1)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("categoryId").descending());
        
        Page<Category> resultPage;
        
        // Nếu có từ khóa tìm kiếm -> Gọi hàm tìm kiếm phân trang
        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
            model.addAttribute("name", name); // Gửi lại từ khóa ra View để giữ ở ô input
        } else {
            // Nếu không -> Gọi hàm tìm tất cả phân trang (Tự viết thêm vào Service nếu chưa có, hoặc dùng repository trực tiếp tạm thời)
            // Lưu ý: Tốt nhất nên thêm findAll(Pageable) vào Service. Ở đây mình gọi qua repo cho nhanh demo.
            resultPage = categoryService.findByCategoryNameContaining("", pageable); 
        }
        
        // Tính toán số trang để hiển thị lên View
        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(page + 2, totalPages);
            
            // Chuyển danh sách trang thành List<Integer> để loop trong Thymeleaf
            // Ví dụ: [1, 2, 3, 4, 5]
            if (totalPages > 0) {
                 List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                        .boxed()
                        .collect(Collectors.toList());
                 model.addAttribute("pageNumbers", pageNumbers);
            }
        }
        
        model.addAttribute("categoryPage", resultPage);
        return "admin/categories/list";
    }

	@GetMapping("add")
	public String add(Model model) {
		CategoryModel cateModel = new CategoryModel();
		cateModel.setIsEdit(false);
		model.addAttribute("category", cateModel);
		return "admin/categories/addOrEdit";
	}

	@GetMapping("edit/{id}")
	public ModelAndView edit(Model model, @PathVariable("id") Long id) {
		Optional<Category> opt = categoryService.findById(id);
		CategoryModel cateModel = new CategoryModel();

		if (opt.isPresent()) {
			Category entity = opt.get();

			// Copy properties từ Entity sang Model
			// Vì tên trường (categoryId, categoryName,...) giờ đã giống nhau nên BeanUtils
			// sẽ tự copy hết
			BeanUtils.copyProperties(entity, cateModel);

			cateModel.setIsEdit(true);

			model.addAttribute("category", cateModel);
			return new ModelAndView("admin/categories/addOrEdit", model.asMap());
		}

		model.addAttribute("message", "Category không tồn tại!");
		return new ModelAndView("forward:/admin/categories", model.asMap());
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(Model model, @Valid @ModelAttribute("category") CategoryModel cateModel,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/categories/addOrEdit");
		}

		Category entity = new Category();
		// Copy dữ liệu từ Model sang Entity (trừ file ảnh)
		BeanUtils.copyProperties(cateModel, entity);

		// XỬ LÝ LƯU FILE ẢNH
		if (cateModel.getImageFile() != null && !cateModel.getImageFile().isEmpty()) {
		    // 1. Tạo tên file duy nhất TRƯỚC
		    UUID uuid = UUID.randomUUID();
		    String uuString = uuid.toString();
		    
		    // 2. Lấy tên file đã sinh ra (Ví dụ: p123456.jpg)
		    String fileName = storageService.getSorageFilename(cateModel.getImageFile(), uuString);
		    
		    // 3. Thực hiện lưu file vật lý (Dùng tên file vừa tạo ở trên) -> QUAN TRỌNG: Phải dùng biến fileName
		    storageService.store(cateModel.getImageFile(), fileName);
		    
		    // 4. Lưu tên file vào database
		    entity.setImages(fileName);
		
		} else {
			// Nếu người dùng không chọn ảnh mới -> Giữ nguyên ảnh cũ (đã có trong
			// cateModel.images nhờ input hidden)
			entity.setImages(cateModel.getImages());
		}

		categoryService.save(entity);

		String message = cateModel.getIsEdit() ? "Category đã được cập nhật thành công"
				: "Category đã được thêm thành công";
		model.addAttribute("message", message);

		return new ModelAndView("redirect:/admin/categories");
	}

	@GetMapping("delete/{id}")
	public ModelAndView delete(Model model, @PathVariable("id") Long id) {
		categoryService.deleteById(id);
		model.addAttribute("message", "Category đã xóa thành công");
		return new ModelAndView("redirect:/admin/categories");
	}
}