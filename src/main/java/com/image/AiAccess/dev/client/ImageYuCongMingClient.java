package com.image.AiAccess.dev.client;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.image.AiAccess.dev.common.BaseResponse;
import com.image.AiAccess.dev.model.DevChatRequest;
import com.image.AiAccess.dev.model.DevChatResponse;

import java.util.HashMap;
import java.util.Map;

import static com.image.AiAccess.dev.utils.SignUtils.genSign;

/**
 * 调用鱼聪明 AI 的客户端
 */
public class ImageYuCongMingClient {

    // 接入地址
    private static final String HOST = "https://www.yucongming.com/api/dev";

    private final String accessKey;

    private final String secretKey;

    public ImageYuCongMingClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 对话
     *
     * @param devChatRequest
     * @return
     */
    public BaseResponse<DevChatResponse> doChat(DevChatRequest devChatRequest) {
        String url = HOST + "/chat";
        String json = JSONUtil.toJsonStr(devChatRequest);
        String result = HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute()
                .body();
        TypeReference<BaseResponse<DevChatResponse>> typeRef = new TypeReference<BaseResponse<DevChatResponse>>() {
        };
        // result 转换为指定类型的 Java 对象
        return JSONUtil.toBean(result, typeRef, false);
    }

    /**
     * 获取请求头
     *
     * @param body 请求参数
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        String encodedBody = SecureUtil.md5(body);
        hashMap.put("body", encodedBody);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(encodedBody, secretKey));
        return hashMap;
    }

    public static void main(String[] args) {
        String accessKey = "你的 accessKey";
        String secretKey = "你的 secretKey";
        ImageYuCongMingClient imageYuCongMingClient = new ImageYuCongMingClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1651468516836098050L);
        devChatRequest.setMessage("鱼皮");
        BaseResponse<DevChatResponse> devChatResponseBaseResponse = imageYuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
        DevChatResponse data = devChatResponseBaseResponse.getData();
        if (data != null) {
            String content = data.getContent();
            System.out.println(content);
        }
    }
}
