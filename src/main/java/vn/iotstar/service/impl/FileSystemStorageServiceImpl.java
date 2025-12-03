package vn.iotstar.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.config.StorageProperties;
import vn.iotstar.exception.StorageException;
import vn.iotstar.exception.StorageFileNotFoundException;
import vn.iotstar.service.IStorageService;

@Service
public class FileSystemStorageServiceImpl implements IStorageService {

    private final Path rootLocation;

    @Override
    public String getSorageFilename(MultipartFile file, String id) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "p" + id + "." + ext; // Ví dụ: pUUID.jpg
    }

    public FileSystemStorageServiceImpl(StorageProperties properties) {
        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException("File upload location can not be Empty.");
        }
        this.rootLocation = Paths.get(properties.getLocation());
        
        // Tự động tạo thư mục uploads ngay khi chạy code để tránh lỗi không tìm thấy thư mục
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file, String storeFilename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            
            // 1. Chuẩn hóa đường dẫn tuyệt đối của thư mục gốc (uploads)
            // Điều này giúp loại bỏ các ký tự thừa như ./ hoặc ../
            Path root = this.rootLocation.toAbsolutePath().normalize();
            
            // 2. Tạo đường dẫn tuyệt đối đến file sẽ lưu
            Path destinationFile = root.resolve(Paths.get(storeFilename))
                    .normalize().toAbsolutePath();

            // 3. Kiểm tra bảo mật: Đảm bảo file nằm TRONG thư mục uploads
            // So sánh parent của file đích với thư mục root
            if (!destinationFile.getParent().equals(root)) {
                throw new StorageException(
                        "Cannot store file outside current directory. "
                        + "Root: " + root + ", Dest: " + destinationFile.getParent());
            }
            
            // 4. Lưu file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(String storeFilename) throws Exception {
        Path destinationFile = rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();
        Files.delete(destinationFile);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}