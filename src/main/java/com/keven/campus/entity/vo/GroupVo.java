package com.keven.campus.entity.vo;

import com.keven.campus.entity.Group;
import com.keven.campus.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Keven
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class GroupVo {
    private Group group;
    private List<Tag> tags;

    public GroupVo(Group group) {
        this.group = group;
    }
}
