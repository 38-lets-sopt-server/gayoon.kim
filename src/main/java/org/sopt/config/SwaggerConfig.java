package org.sopt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI assignmentOpenAPI() {
        Server server = new Server();
        server.setUrl("https://beneruufin.store");
        server.setDescription("배포 서버");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Post API")
                        .description("게시글 API 명세서")
                        .version("v1.0.0"));
    }
}