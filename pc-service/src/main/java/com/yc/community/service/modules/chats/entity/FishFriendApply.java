package com.yc.community.service.modules.chats.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yc001
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_friend_apply")
public class FishFriendApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("from_user_id")
    private String fromUserId;

    @TableField("to_user_id")
    private String toUserId;

    @TableField("status")
    private String status;


}
