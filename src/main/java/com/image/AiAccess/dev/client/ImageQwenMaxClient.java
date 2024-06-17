package com.image.AiAccess.dev.client;// Copyright (c) Alibaba, Inc. and its affiliates.

// 建议dashscope SDK的版本 >= 2.12.0

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.image.AiAccess.dev.common.BaseResponse;
import com.image.AiAccess.dev.constant.RoleSettingConstant;
import com.image.AiAccess.dev.model.QwenChartRequest;
import com.image.AiAccess.dev.model.QwenChatResponse;

import java.util.Arrays;

public class ImageQwenMaxClient {
    private final String apiKey;

    public ImageQwenMaxClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public BaseResponse<QwenChatResponse> doChat(QwenChartRequest qwenChartRequest) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        String question = qwenChartRequest.getQuestion();

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(RoleSettingConstant.GEN_CHART_QWEN)
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(question)
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .build();

        GenerationResult generationResult = gen.call(param);
        String content = generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        return new BaseResponse<QwenChatResponse>(200, new QwenChatResponse(content), "success");
    }

    public static void main(String[] args) {
        try {
            ImageQwenMaxClient imageQwenMaxClient = new ImageQwenMaxClient("sk-56bd3bbc269d43939d41cea923145e49");
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