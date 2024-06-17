package com.image.AiAccess.dev.config;

import com.image.AiAccess.dev.client.ImageSparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/10
 */
@Configuration
@ConfigurationProperties("image.spark.client")
@Data
@ComponentScan
public class ImageSparkClientConfig {

    public String appid;
    public String apiSecret;
    public String apiKey;

    @Bean
    public ImageSparkClient ImageSparkClient() {
        return new ImageSparkClient(appid, apiSecret, apiKey);
    }


}
