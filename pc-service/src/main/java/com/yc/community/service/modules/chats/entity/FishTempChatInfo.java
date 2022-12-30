package com.yc.community.service.modules.chats.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * @since 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_temp_chat_info")
public class FishTempChatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("last_chat_msg")
    private String lastChatMsg;

    @TableField("created_time")
    private Date createdTime;


}
