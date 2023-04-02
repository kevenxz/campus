package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.exception.RRException;
import com.keven.campus.common.utils.CampusConstant;
import com.keven.campus.common.utils.PageUtils;
import com.keven.campus.common.utils.Query;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.File;
import com.keven.campus.entity.Topic;
import com.keven.campus.entity.User;
import com.keven.campus.entity.dto.DiscussPostDTO;
import com.keven.campus.entity.vo.DiscussPostVo;
import com.keven.campus.mapper.FileMapper;
import com.keven.campus.mapper.TopicMapper;
import com.keven.campus.mapper.UserMapper;
import com.keven.campus.service.CommentService;
import com.keven.campus.service.DiscussPostService;
import com.keven.campus.mapper.DiscussPostMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @description 针对表【t_campus_discuss_post(帖子表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
        implements DiscussPostService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long topicId, Long discussPostId, Integer status) {
        LambdaUpdateWrapper<DiscussPost> wrapper =
                new LambdaUpdateWrapper<DiscussPost>()
                        .eq(DiscussPost::getId, discussPostId)
                        .set(status != null, DiscussPost::getPostStatus, status);
        this.update(wrapper);
        //减去对应话题中的数量
        topicMapper.update(null, new LambdaUpdateWrapper<Topic>()
                .setSql("discuss_count = (case when t_campus_topic.discuss_count > 0 then t_campus_topic.discuss_count-1 else 0  end)")
                .eq(Topic::getId, 1));

    }

    /**
     * 查询帖子列表 ---> 包括简单评论 点赞 关注等操作
     *
     * @param topicId
     * @param params
     * @return
     */
    @Override
    public R queryPage(Long topicId, Map<String, Object> params) {
        LambdaQueryWrapper<DiscussPost> wrapper = new LambdaQueryWrapper<>();
        if (topicId != null) {
            wrapper.eq(DiscussPost::getTopicId, topicId);
        }
        if ((Integer) params.get("type") == 0) {
            wrapper.ne(DiscussPost::getPostStatus, 2);
        }
        wrapper.orderByDesc(DiscussPost::getCreatedTime);
        // 分页查询
        IPage<DiscussPost> page = this.page(new Query<DiscussPost>().getPage(params), wrapper);
        // 转换
        PageUtils pageUtils = new PageUtils(page);
        List<DiscussPost> discussPosts = page.getRecords();
        // 查询前三条评论
        List<Map<String, Object>> res = discussPosts.stream().map(discussPost -> {
            Map<String, Object> map = new HashMap<>();
            // 设置discussPostVo
            map.put("discussPostVo", getDiscussPostVo(discussPost));
            // 查询帖子的前三条评论 和对应的子评论
            R pageComments =
                    commentService.getPageComments(CampusConstant.ENTITY_TYPE_POST, discussPost.getId(), 1, 3);
            map.put("comments", pageComments.get("data"));
            return map;
        }).collect(Collectors.toList());
        pageUtils.setList(res);
        return R.ok().put(pageUtils);
    }

    /**
     * 返回帖子的详情
     *
     * @param discussPostId
     * @return
     */
    @Override
    public R getPostDetail(Long discussPostId) {
        try {
            if (ObjectUtils.isNull(discussPostId)) {
                throw new RRException(ResultCode.RequestParamsNull);
            }
            // 查帖子
            DiscussPost discussPost =
                    baseMapper.selectOne(new LambdaQueryWrapper<DiscussPost>().eq(DiscussPost::getId, discussPostId));
            if (ObjectUtils.isNull(discussPost)) {
                throw new RRException(ResultCode.NullData);
            }
            return R.ok().put(getDiscussPostVo(discussPost));
        } catch (RRException rrException) {
            return R.error(rrException.getCode(), rrException.getMsg());
        }
    }

    /**
     * 添加帖子
     *
     * @param discussPostDTO 讨论帖子dto
     * @return {@link R}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R add(DiscussPostDTO discussPostDTO) {

        try {
            DiscussPost discussPost = new DiscussPost();
            BeanUtils.copyProperties(discussPostDTO, discussPost);
            discussPost.setPostType(0);
            discussPost.setPostStatus(0);

            // 插入帖子数据
            discussPostMapper.insert(discussPost);
            // 类型为1 则说明有照片等文件插入
            if (discussPostDTO.getPostPic() == CampusConstant.HAS_PIC) {
                List<File> files = discussPostDTO.getFiles();
                int num = fileMapper.insertByForeach(files);
            }
        } catch (Exception e) {
            return R.error("发布失败！");
        }
        return R.ok().put("成功发布！");
    }


    /**
     * 获得帖子共同信息
     *
     * @param discussPost 原始帖子
     * @return {@link DiscussPostVo}
     */
    private DiscussPostVo getDiscussPostVo(DiscussPost discussPost) {
        DiscussPostVo discussPostVo = new DiscussPostVo();
        BeanUtils.copyProperties(discussPost, discussPostVo);
        // 帖子的作者 todo 可以用redis来获取
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, discussPost.getUserId())
                .select(User::getAvatarurl, User::getNickname, User::getGender, User::getId));
        if (user != null) {
            discussPostVo.setAvatarUrl(user.getAvatarurl());
            discussPostVo.setNickName(user.getNickname());
        }

        // TODO 获取当前用户的点赞状态，没有登录直接设为没点赞
//            discussPostVo.setLikeStatus();
        // todo 获取当前的点赞数量
//            discussPostVo.setPraiseCount(;);
        // todo 获取当前的围观数 Redis
//            discussPostVo.setOpenCount();
        //  获取话题的名字
        Topic topic = topicMapper.selectOne(new LambdaQueryWrapper<Topic>()
                .eq(Topic::getId, discussPost.getTopicId())
                .select(Topic::getTopicName));
        if (topic != null) {
            discussPostVo.setTopicName(topic.getTopicName());
        }
        Integer postPic = discussPost.getPostPic();
        // 有照片的帖子
        List<File> files = null;
        if (ObjectUtils.isNotNull(postPic) && discussPost.getPostPic() == 1) {
            // 查询照片
            files = fileMapper.selectList(new LambdaQueryWrapper<File>()
                    .eq(File::getEntityType, CampusConstant.ENTITY_TYPE_POST)
                    .eq(File::getBusinessId, discussPost.getId()));
        }
        discussPostVo.setFiles(files);
        return discussPostVo;
    }


}




