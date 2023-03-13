package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.common.utils.Query;
import com.keven.campus.entity.Group;
import com.keven.campus.entity.Tag;
import com.keven.campus.service.GroupService;
import com.keven.campus.service.TagService;
import com.keven.campus.mapper.TagMapper;
import com.keven.campus.service.UserTagService;
import com.keven.campus.entity.vo.GroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @description 针对表【t_campus_tag(用户各性标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:49
 */
@Service
@Validated
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private GroupService groupService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> tagIds) {
        //删除标签
        this.removeBatchByIds(tagIds);
        //删除标签与用户的关联
        userTagService.deleteBatchByTagId(tagIds);
    }

    @Override
    public Map<String, Object> queryPage(Map<String, Object> params) {

        if (ObjectUtils.isEmpty(params)) {
            throw new RRException("参数不为空");
        }
        HashMap<String, Object> map = new HashMap<>();

        String type = (String) params.get("type");

        if (StringUtils.isBlank(type)) {// 为空，查询每个组所有的标签
            // 查询所有组的信息
            List<Group> groupList = groupService.list()
                    .stream()
                    .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .collect(Collectors.toList());
            List<GroupVo> groupVoList = groupList.stream().map(group -> new GroupVo(group)).collect(Collectors.toList());
            // 查询所有标签的信息
            List<Tag> tagList = this.list(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId, Tag::getGroupId, Tag::getTagContent)
                    .isNotNull(Tag::getGroupId)
                    .orderByDesc(Tag::getGroupId));
            groupVoList.forEach(groupVo -> groupVo.setTags(
                    tagList.stream()
                            .filter(tag -> tag.getGroupId() == groupVo.getGroup().getId())
                            .collect(Collectors.toList())));
            map.put("page", groupVoList);

        } else { // 全查
            IPage<Tag> page = this.page(new Query<Tag>().getPage(params), new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getCreatedTime));
            map.put("page", page);
        }

        return map;
    }
}




