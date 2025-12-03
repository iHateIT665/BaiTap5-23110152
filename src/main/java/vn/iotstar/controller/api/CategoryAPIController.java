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

    // 2. Thêm mới Category (Có upload ảnh)
    @PostMapping(path = "/addCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addCategory(
            @Validated @RequestParam("categoryName") String categoryName,
            @Validated @RequestParam("categoryCode") String categoryCode,
            @RequestParam("status") int status,
            @RequestParam("images") MultipartFile file) { // Lưu ý: key bên Postman phải là 'images'

        // Check trùng tên (Optional)
        if (!categoryService.findByCategoryNameContaining(categoryName).isEmpty()) {
             // Logic check trùng đơn giản, bạn có thể viết hàm findByCategoryName chính xác hơn
             // return new ResponseEntity<>(new Response(false, "Tên Category đã tồn tại", null), HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setStatus(status);

        // Xử lý upload ảnh
        if (!file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            // Lưu file vào folder uploads
            storageService.store(file, storageService.getSorageFilename(file, uuString));
            // Lưu tên file vào database
            category.setImages(storageService.getSorageFilename(file, uuString));
        }

        categoryService.save(category);
        return new ResponseEntity<>(new Response(true, "Thêm thành công", category), HttpStatus.OK);
    }

    // 3. Cập nhật Category
    @PutMapping(path = "/updateCategory", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateCategory(
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("categoryName") String categoryName,
            @Validated @RequestParam("categoryCode") String categoryCode,
            @RequestParam("status") int status,
            @RequestParam(value = "images", required = false) MultipartFile file) { // required=false vì update có thể không đổi ảnh

        Optional<Category> optCategory = categoryService.findById(categoryId);
        
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.NOT_FOUND);
        }

        Category category = optCategory.get();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setStatus(status);

        // Nếu có chọn ảnh mới thì mới upload và thay đổi
        if (file != null && !file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            
            storageService.store(file, storageService.getSorageFilename(file, uuString));
            category.setImages(storageService.getSorageFilename(file, uuString));
        }

        categoryService.save(category);
        return new ResponseEntity<>(new Response(true, "Cập nhật thành công", category), HttpStatus.OK);
    }

    // 4. Xóa Category
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<Response> deleteCategory(@Validated @RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);

        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.NOT_FOUND);
        }

        // Có thể thêm logic xóa file ảnh trong thư mục uploads nếu muốn
        // storageService.delete(optCategory.get().getImages());

        categoryService.deleteById(categoryId);
        return new ResponseEntity<>(new Response(true, "Xóa thành công", optCategory.get()), HttpStatus.OK);
    }
}