package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.common.utils.*;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.File;
import com.keven.campus.entity.Topic;
import com.keven.campus.entity.User;
import com.keven.campus.mapper.DiscussPostMapper;
import com.keven.campus.mapper.FileMapper;
import com.keven.campus.mapper.UserMapper;
import com.keven.campus.service.TopicService;
import com.keven.campus.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author
 * @description 针对表【t_campus_topic】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:49
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
        implements TopicService {

    @Autowired
    private TopicMapper topicMapper;


    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    /**
     * 获得信息和dis帖子
     *
     * @param topicId 主题id
     * @param curPage 当前页面
     * @param limit   页面显示条数
     * @return {@link R}
     */
    @Override
    public R getInfoAndDisPosts(Long topicId, Integer curPage, Integer limit) {
        Map<String, Object> pageMap = CampusUtil.getPageMap(curPage, limit);
        PageUtils pageUtils = new PageUtils();
        try {
            // 查询话题的详情信息
            if (topicId == null) {
                throw new RRException(ResultCode.RequestParamsNull);
            }
            Topic topic = this.getOne(new LambdaQueryWrapper<Topic>()
                    .eq(Topic::getId, topicId));
            if (ObjectUtils.isNull(topic)) {
                throw new RRException(ResultCode.SelectDataIsNull);
            }

            // 分页查询帖子  (先根据帖子类型来查询置顶的先，再根据时间降序)
            LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DiscussPost::getTopicId, topicId)
                    .ne(DiscussPost::getPostStatus, 2)
                    .orderByDesc(DiscussPost::getPostType)
                    .orderByDesc(DiscussPost::getCreatedTime);
            IPage<DiscussPost> discussPostIPage = discussPostMapper.selectPage(
                    new Query<DiscussPost>().getPage(pageMap), wrapper);
            // 放入分页的对应参数
            pageUtils.setCurrPage((int) discussPostIPage.getCurrent());
            pageUtils.setPageSize((int) discussPostIPage.getPages());
            pageUtils.setTotalCount((int) discussPostIPage.getTotal());

            // 列出所有的查询帖子里对应的用户数据
            List<DiscussPost> list = discussPostIPage.getRecords();
            if (ObjectUtils.isNotNull(list)) {
                List<Map<String, Object>> discussPosts = list
                        .stream()
                        .map(discussPost -> {
                            Map<String, Object> map = new HashMap<>();
                            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                                    .eq(User::getId, discussPost.getUserId()));
                            // 有照片就查询文件信息
                            List<File> files = null;
                            if (discussPost.getPostPic() == 1) {
                                files = fileMapper.selectList(new LambdaQueryWrapper<File>()
                                        .eq(File::getBusinessId, discussPost.getId())
                                        .eq(File::getEntityType, CampusConstant.ENTITY_TYPE_POST));
                            }
                            map.put("files", files);
                            map.put("user", user);
                            map.put("post", discussPost);
                            // todo 加上一个帖子的点赞数量 long likecount =redis....
                            return map;
                        }).collect(Collectors.toList());
                pageUtils.setList(discussPosts);
            }
            return R.ok().put(pageUtils);
        } catch (Exception e) {

        }
        return R.error();
    }

}




