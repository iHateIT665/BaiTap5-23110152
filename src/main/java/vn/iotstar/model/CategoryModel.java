package vn.iotstar.model;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    private Long categoryId;
    private String categoryName;
    private String categoryCode;
    private String images;
    private int status;
    private Boolean isEdit = false;

    // Thêm trường này để hứng file
    private MultipartFile imageFile; 
}