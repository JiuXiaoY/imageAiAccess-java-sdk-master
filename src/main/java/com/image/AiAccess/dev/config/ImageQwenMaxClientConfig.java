package com.image.AiAccess.dev.config;

import com.image.AiAccess.dev.client.ImageQwenMaxClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/11
 */
@Configuration
@ConfigurationProperties("image.qwen.client")
@Data
@ComponentScan
public class ImageQwenMaxClientConfig {
    private String apikey;

    @Bean
    public ImageQwenMaxClient ImageQwenMaxClient() {
        return new ImageQwenMaxClient(apikey);
    }
}
