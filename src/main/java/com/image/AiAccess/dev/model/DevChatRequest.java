package com.image.AiAccess.dev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 开发者对话请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevChatRequest implements Serializable {

    /**
     * 模型（助手）id
     */
    private Long modelId;

    /**
     * 消息
     */
    private String message;

    @Serial
    private static final long serialVersionUID = 1L;

}