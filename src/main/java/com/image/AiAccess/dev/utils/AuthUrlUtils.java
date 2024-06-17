package com.image.AiAccess.dev.utils;

import cn.hutool.core.net.url.UrlBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/10
 */
public class AuthUrlUtils {

    /**
     * 获取鉴权 Url，用于对接 spark 后台
     *
     * @param hostUrl   接口地址
     * @param apiKey    密钥
     * @param apiSecret 密钥
     * @return 鉴权 Url
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) {
        UrlBuilder urlBuilder = UrlBuilder.of(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + urlBuilder.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + urlBuilder.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = null;
        try {
            mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
            mac.init(spec);
        } catch (Exception e) {
            throw new RuntimeException("加密异常" + e.getMessage());
        }

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);

        // 返回拼接地址
        return urlBuilder.addQuery("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQuery("date", date)
                .addQuery("host", urlBuilder.getHost())
                .build();
    }
}
