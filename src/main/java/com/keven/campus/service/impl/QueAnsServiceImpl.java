package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.PageUtils;
import com.keven.campus.common.utils.Query;
import com.keven.campus.entity.QueAns;
import com.keven.campus.service.QueAnsService;
import com.keven.campus.mapper.QueAnsMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author
 * @description 针对表【t_campus_que_ans((匿名问答表) -- 默认提问为(无名氏))】的数据库操作Service实现
 * @createDate 2023-01-16 11:15:22
 */
@Service
public class QueAnsServiceImpl extends ServiceImpl<QueAnsMapper, QueAns>
        implements QueAnsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long userId = Long.parseLong((String) params.get("userId"));
        String isDelete = (String) params.get("isDelete");
        // 不为空就查
        IPage<QueAns> page = this.page(
                new Query<QueAns>().getPage(params),
                new LambdaQueryWrapper<QueAns>()
                        .eq(QueAns::getAnswerId, userId)
                        .eq(StringUtils.isNotBlank(isDelete), QueAns::getIsDeleted, 0)
                        .orderByDesc(QueAns::getQuestionTime)
        );
        return new PageUtils(page);

    }
}




