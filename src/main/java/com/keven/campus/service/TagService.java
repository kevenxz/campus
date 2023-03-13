package com.keven.campus.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.common.utils.PageUtils;
import com.keven.campus.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @description 针对表【t_campus_tag(用户各性标签表)】的数据库操作Service
 * @createDate 2023-01-12 14:26:49
 */
public interface TagService extends IService<Tag> {
    /**
     * 批量删除
     *
     * @param tagIds
     */
    void deleteBatch(List<Long> tagIds);

    /**
     * 分页查询标签
     *
     * @param params
     * @return
     */
    Map<String, Object> queryPage(Map<String, Object> params);
}
