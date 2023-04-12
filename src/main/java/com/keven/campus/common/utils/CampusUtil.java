package com.keven.campus.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keven.campus.entity.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Keven
 * @version 1.0
 */
public class CampusUtil {

    // 生成随机字符串 上传文件 上传图片
    public static String generateUUID() {
        // 只要字母和数字  把所有的横线替换为空的字符
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    // 返回存放分页参数的map,方便生成Page参数
    public static Map<String, Object> getPageMap(Integer curPage, Integer limit) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put(Constant.PAGE, curPage);
        pageMap.put(Constant.LIMIT, limit);
        return pageMap;
    }

    // 返回分页参数Query 实体
    public static IPage<Comment> getQuery(Integer curPage, Integer limit) {
        Map<String, Object> pageMap = CampusUtil.getPageMap(curPage, limit);
        return new Query().getPage(pageMap);
    }
}
