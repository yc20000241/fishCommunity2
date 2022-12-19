package com.yc.community.service.modules.articles.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * @since 2022-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_comments")
public class FishComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("from_user_id")
    private String fromUserId;

    @TableField("to_user_id")
    private String toUserId;

    @TableField("parent_id")
    private String parentId;

    @TableField("article_id")
    private String articleId;

    @TableField("article_comment")
    private String articleComment;

    @TableField("createt_time")
    private Date createtTime;


}
