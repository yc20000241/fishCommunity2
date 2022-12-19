package com.yc.community.articles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="FishComments对象", description="")
public class FishComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableField("id")
    private String id;

    @ApiModelProperty(value = "发送评论的人")
    @TableField("from_user_id")
    private String fromUserId;

    @ApiModelProperty(value = "评论接收者")
    @TableField("to_user_id")
    private String toUserId;

    @ApiModelProperty(value = "父id")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty(value = "文章id")
    @TableField("article_id")
    private String articleId;

    @ApiModelProperty(value = "评论")
    @TableField("article_comment")
    private String articleComment;

    @ApiModelProperty(value = "创建时间")
    @TableField("createt_time")
    private Date createtTime;


}
