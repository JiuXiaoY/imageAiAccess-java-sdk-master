# 这是一个接入AI模型的的sdk

## 讯飞 Spark 大模型

[Spark](https://xinghuo.xfyun.cn/)

### 使用示例

```java
public class Temp {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 引入SDK之后，可以在配置文件中配置好appid, apiSecret, apiKey（yml，properties）
        String appid = "3dd67985";
        String apiSecret = "MTJkNmRhYjRiM2RjYWMxNjI5YjgxYTcy";
        String apiKey = "0ad9891ef06fc7e5888b7641ace89a72";
        // 注入对象
        ImageSparkClient imageSparkClient = new ImageSparkClient(appid, apiSecret, apiKey);
        String userId = "123";
        String NewQuestion = "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,40\n" +
                "3号,80";
        // 参数要求
        SparkChatRequest sparkChatRequest = new SparkChatRequest(userId, NewQuestion);
        BaseResponse<SparkChatResponse> sparkChatResponse = imageSparkClient.doChatWithNoPrompt(sparkChatRequest);
        System.out.println(sparkChatResponse.getData());
    }
}
```

## 阿里云百炼，Qwen大模型

[Qwen](https://bailian.console.aliyun.com)

### 使用示例

```java
public class Temp {
    public static void main(String[] args) {
        try {
            // 引入SDK之后，可以在配置文件中配置好apiKey（yml，properties）
            ImageQwenMaxClient imageQwenMaxClient = new ImageQwenMaxClient("sk-56bd3bbc269d43939d41cea923145e49");
            // 参数要求
            QwenChartRequest qwenChartRequest = new QwenChartRequest();
            qwenChartRequest.setQuestion("分析需求：\n" +
                    "分析网站用户的增长情况\n" +
                    "原始数据：\n" +
                    "日期,用户数\n" +
                    "1号,10\n" +
                    "2号,40\n" +
                    "3号,80");
            BaseResponse<QwenChatResponse> qwenChat = imageQwenMaxClient.doChat(qwenChartRequest);
            System.out.println(qwenChat);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            // Logger.error("An error occurred while calling the generation service", e);
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        System.exit(0);
    }
}
```
