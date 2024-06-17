package com.image.AiAccess.dev.config;

import com.image.AiAccess.dev.client.ImageYuCongMingClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端配置
 *
 */
@Configuration
@ConfigurationProperties("image.yucongming.client")
@Data
@ComponentScan
public class ImageYuCongMingClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public ImageYuCongMingClient imageYuCongMingClient() {
        return new ImageYuCongMingClient(accessKey, secretKey);
    }

}
