package com.keven.campus.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设置为存到Redis中的key  包含逻辑过期时间
 *
 * @author Keven
 * @version 1.0
 */
@Data
public class RedisData {
    private Object data;
    private LocalDateTime expireTime;
}
