package com.keven.campus.service;

import com.keven.campus.common.utils.PageUtils;
import com.keven.campus.common.utils.R;
import com.keven.campus.entity.QueAns;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author
 * @description 针对表【t_campus_que_ans((匿名问答表) -- 默认提问为(无名氏))】的数据库操作Service
 * @createDate 2023-01-16 11:15:22
 */
public interface QueAnsService extends IService<QueAns> {

    /**
     * 分页查询匿名提问的消息
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 创建匿名提问
     *
     * @param queAns
     * @return
     */
    R create(QueAns queAns);
}
