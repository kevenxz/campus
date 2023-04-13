package com.keven.campus.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列的消息事件
 *
 * @author Keven
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class Event {

    /**
     * 主题(事件的类型 -> 评论、点赞、关注)
     */
    private String topic;
    /**
     * 事件触发人
     */
    private Long userId;

    /**
     * 事件对应的实体
     */
    private Integer entityType;
    private Long entityId;
    /**
     * 实体作者(通知到实体的人)
     */
    private Long entityUserId;
    /**
     * 处理其他额外事件的数据存放
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 让调用这个方法可以只传key-value 不用传map对象，方便调用
     *
     * @param key
     * @param value
     * @return
     */
    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
