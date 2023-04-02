package com.keven.campus.common.utils.redis;

/**
 * @author Keven
 * @version 1.0
 */
public class RedisConstants {
    private static final String SPLIT = ":";

    private static final String PREFIX_USER_LIKE = "like:user";
    /**
     * 缓存空数据时间ttl
     */
    public static final Long CACHE_NULL_TTL = 2L;

    public static final String LOCK_POST_KEY = "lock:post:";

    public static final String LOGIN_USER_KEY = "login:user:";

    public static final String PREFIX_ENTITY_LIKE = "like:entity";

    // 某个实体的赞 , 这样就能知道是谁点赞 ，也可以统计赞的数量
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(Integer entityType, Long entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户获得的赞
    // like:user:userId -> int
    public static String getUserLikeKey(Long userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
}
