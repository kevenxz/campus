package com.keven.campus.entity.vo;

import com.keven.campus.entity.Tag;
import com.keven.campus.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ::: 用户信息 :::
 * 用户基本信息
 * 用户的所有个性标签
 *
 * @author Keven
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private User user;
    private List<Tag> tags;
}
