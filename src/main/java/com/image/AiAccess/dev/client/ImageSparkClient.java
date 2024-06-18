package com.image.AiAccess.dev.client;

import com.image.AiAccess.dev.common.BaseResponse;
import com.image.AiAccess.dev.config.SparkWebSocket;
import com.image.AiAccess.dev.model.SparkChatRequest;
import com.image.AiAccess.dev.model.SparkChatResponse;
import com.image.AiAccess.dev.utils.AuthUrlUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 调用讯飞星火大模型的客户端
 *
 * @author JXY
 * @version 1.0
 * @since 2024/6/10
 */
@SuppressWarnings("all")
public class ImageSparkClient {
    // 模型参数
    public static final String hostUrl = "https://spark-api.xf-yun.com/v3.5/chat";
    public String appid;
    public String apiSecret;
    public String apiKey;
    static CompletableFuture<String> future = new CompletableFuture<>();

    public ImageSparkClient(String appid, String apiSecret, String apiKey) {
        this.appid = appid;
        this.apiSecret = apiSecret;
        this.apiKey = apiKey;
    }

    /**
     * 带有背景设定的
     *
     * @param sparkChatRequest 请求参数
     * @return 返回结果
     * @throws ExecutionException 异常
     * @throws InterruptedException 异常
     */
    public BaseResponse<SparkChatResponse> doChat(SparkChatRequest sparkChatRequest) throws ExecutionException, InterruptedException {
        // 获取到用户 id 以及问题
        String question = sparkChatRequest.getQuestion();
        String userId = sparkChatRequest.getUserId();
        // sparkWebSocket
        SparkWebSocket sparkWebSocket = new SparkWebSocket(appid, userId, false, question, future, true);
        // 构建鉴权url
        String authUrl = AuthUrlUtils.getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        // 建立 webSocket 连接
        WebSocket webSocket = client.newWebSocket(request, sparkWebSocket);
        // 确保在webSocket连接关闭时在执行以下语句
        String result = future.get();
        return new BaseResponse<SparkChatResponse>(200, new SparkChatResponse(result), "ok");
    }

    /**
     * 不带有背景设定的
     *
     * @param sparkChatRequest 请求参数
     * @return 返回结果
     * @throws ExecutionException 异常
     * @throws InterruptedException 异常
     */
    public BaseResponse<SparkChatResponse> doChatWithNoPrompt(SparkChatRequest sparkChatRequest) throws ExecutionException, InterruptedException {
        // 获取到用户 id 以及问题
        String question = sparkChatRequest.getQuestion();
        String userId = sparkChatRequest.getUserId();
        // sparkWebSocket
        SparkWebSocket sparkWebSocket = new SparkWebSocket(appid, userId, false, question, future, false);
        // 构建鉴权url
        String authUrl = AuthUrlUtils.getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        // 建立 webSocket 连接
        WebSocket webSocket = client.newWebSocket(request, sparkWebSocket);
        // 确保在webSocket连接关闭时在执行以下语句
        String result = future.get();
        return new BaseResponse<SparkChatResponse>(200, new SparkChatResponse(result), "ok");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String appid = "3dd67985";
        String apiSecret = "MTJkNmRhYjRiM2RjYWMxNjI5YjgxYTcy";
        String apiKey = "0ad9891ef06fc7e5888b7641ace89a72";
        ImageSparkClient imageSparkClient = new ImageSparkClient(appid, apiSecret, apiKey);
        String userId = "123";
        String NewQuestion = "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,40\n" +
                "3号,80";
        SparkChatRequest sparkChatRequest = new SparkChatRequest(userId, NewQuestion);
        BaseResponse<SparkChatResponse> sparkChatResponse = imageSparkClient.doChatWithNoPrompt(sparkChatRequest);
        System.out.println(sparkChatResponse.getData());
    }
}

