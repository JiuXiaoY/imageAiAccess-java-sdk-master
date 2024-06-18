package com.image.AiAccess.dev.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.image.AiAccess.dev.constant.RoleSettingConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/10
 */
@Slf4j
public class SparkWebSocket extends WebSocketListener {
    public String appid;
    // 个性化参数
    private final String userId;
    private Boolean wsCloseFlag;
    public String NewQuestion;
    CompletableFuture<String> future;

    public Boolean withPrompt;

    // 构造答案
    StringBuilder totalAnswerBuilder = new StringBuilder();

    public SparkWebSocket(String appid,
                          String userId,
                          Boolean wsCloseFlag,
                          String NewQuestion,
                          CompletableFuture<String> future,
                          Boolean withPrompt) {
        this.appid = appid;
        this.userId = userId;
        this.wsCloseFlag = wsCloseFlag;
        this.NewQuestion = NewQuestion;
        this.future = future;
        this.withPrompt = withPrompt;
    }

    public static final Gson gson = new Gson();

    // 连接建立成功时触发
    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        log.info("用户: " + userId + "  已建立连接成功......");
        // 开始发送消息
        MyThread myThread = new MyThread(webSocket);
        myThread.start();
    }

    // 返回接收消息,这里并不是一次性返回所有消息的
    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
        if (myJsonParse.header.code != 0) {
            System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
            System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
            webSocket.close(1000, "");
        }
        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            totalAnswerBuilder.append(temp.content);
        }
        if (myJsonParse.header.status == 2) {
            // 全部结果都返回来来了
            wsCloseFlag = true;
            future.complete(totalAnswerBuilder.toString());
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                System.out.println("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class MyThread extends Thread {
        private final WebSocket webSocket;

        public MyThread(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        @Override
        public void run() {
            try {
                JSONObject requestJson = new JSONObject();

                JSONObject header = new JSONObject();  // header参数
                header.set("app_id", appid);
                header.set("uid", UUID.randomUUID().toString().substring(0, 10));

                JSONObject parameter = new JSONObject(); // parameter参数
                JSONObject chat = new JSONObject();
                chat.set("domain", "generalv2");
                chat.set("temperature", 0.5);
                chat.set("max_tokens", 4096);
                parameter.set("chat", chat);

                JSONObject payload = new JSONObject(); // payload参数
                JSONObject message = new JSONObject();
                JSONArray text = new JSONArray();

                // 背景设定
                if (withPrompt) {
                    RoleContent roleContentBg = new RoleContent();
                    roleContentBg.role = "system";
                    roleContentBg.content = RoleSettingConstant.GEN_CHART_SPARK;
                    text.add(roleContentBg);
                }

                // 新问题
                RoleContent roleContent = new RoleContent();
                roleContent.role = "user";
                roleContent.content = NewQuestion;
                text.add(roleContent);

                message.set("text", text);
                payload.set("message", message);

                requestJson.set("header", header);
                requestJson.set("parameter", parameter);
                requestJson.set("payload", payload);

                webSocket.send(requestJson.toString());
                // 等待服务端返回完毕后关闭
                while (true) {
                    Thread.sleep(200);
                    if (wsCloseFlag) {
                        break;
                    }
                }
                log.info("结果已经全部返回,用户: " + userId + ",连接关闭......");
                webSocket.close(1000, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    static class RoleContent {
        String role;
        String content;
    }


    //返回的json结果拆解
    public static class JsonParse {
        Header header;
        Payload payload;
    }

    static class Header {
        int code;
        int status;
        String sid;
    }

    static class Payload {
        Choices choices;
    }

    static class Choices {
        List<Text> text;
    }

    static class Text {
        String role;
        String content;
    }
}
