package com.yc.community.service.modules.chats.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_chat_info")
public class FishChatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("friend_id")
    private String friendId;

    @TableField("chat_msg")
    private String chatMsg;

    @TableField("created_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("has_read")
    private Integer hasRead;

    @TableField(exist = false)
    private String nick;

    @TableField(exist = false)
    private String picturePath;
}
