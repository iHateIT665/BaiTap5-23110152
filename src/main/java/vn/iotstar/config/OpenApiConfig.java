package vn.iotstar.config; // Đổi lại theo package của bạn

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Dự Án Shop Bán Hàng")
                        .version("1.0")
                        .description("Tài liệu API cho chức năng quản lý Video và Category"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Server Local")
                ));
    }
}