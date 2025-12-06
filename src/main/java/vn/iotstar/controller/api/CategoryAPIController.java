package vn.iotstar.controller.api;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;
import vn.iotstar.model.Response;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryAPIController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // 1. Lấy tất cả Category
    @GetMapping
    public ResponseEntity<Response> getAllCategory() {
        return new ResponseEntity<>(new Response(true, "Thành công", categoryService.findAll()), HttpStatus.OK);
    }

    // 2. Thêm Category (Có upload ảnh)
    @PostMapping(path = "/addCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("categoryCode") String categoryCode,
            @RequestParam("status") int status,
            @RequestParam(value = "icon", required = false) MultipartFile icon) { // Trong tài liệu là 'icon', project bạn là 'images'

        // Kiểm tra trùng tên (Optional - như tài liệu)
        if (!categoryService.findByCategoryNameContaining(categoryName).isEmpty()) {
             // Logic check trùng đơn giản
             // return new ResponseEntity<>(new Response(false, "Tên đã tồn tại", null), HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setStatus(status);

        // Xử lý file ảnh
        if (icon != null && !icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = storageService.getSorageFilename(icon, uuid.toString());
            storageService.store(icon, fileName);
            category.setImages(fileName); // Entity của bạn tên là images
        }

        categoryService.save(category);
        return new ResponseEntity<>(new Response(true, "Thêm thành công", category), HttpStatus.OK);
    }

    // 3. Cập nhật Category
    @PutMapping(path = "/updateCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("categoryCode") String categoryCode,
            @RequestParam("status") int status,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }

        Category category = optCategory.get();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setStatus(status);

        if (icon != null && !icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = storageService.getSorageFilename(icon, uuid.toString());
            storageService.store(icon, fileName);
            category.setImages(fileName);
        }

        categoryService.save(category);
        return new ResponseEntity<>(new Response(true, "Cập nhật thành công", category), HttpStatus.OK);
    }

    // 4. Xóa Category
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<Response> deleteCategory(@RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }
        
        categoryService.deleteById(categoryId);
        return new ResponseEntity<>(new Response(true, "Xóa thành công", null), HttpStatus.OK);
    }
    
    // 5. Lấy chi tiết 1 Category (Theo tài liệu)
    @PostMapping(path = "/getCategory")
    public ResponseEntity<Response> getCategory(@RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thành công", category.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
        }
    }
}