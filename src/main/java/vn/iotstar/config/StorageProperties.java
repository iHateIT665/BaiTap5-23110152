package vn.iotstar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data

@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    private String location = "uploads"; // Tên thư mục chứa ảnh
}