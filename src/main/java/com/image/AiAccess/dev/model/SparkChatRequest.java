package com.image.AiAccess.dev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkChatRequest implements Serializable {
    /**
     * 用户 Id
     */
    private String userId;
    /**
     * 用户问题
     */
    private String question;

    // 序列化字段
    @Serial
    private static final long serialVersionUID = 1L;
}
