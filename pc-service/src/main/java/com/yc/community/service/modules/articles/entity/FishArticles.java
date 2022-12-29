package com.yc.community.service.modules.articles.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author yc001
 * @since 2022-12-07
 */
@Data
@TableName("fish_articles")
public class FishArticles implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("title")
    private String title;

    @TableField("created_id")
    private String createdId;

    @TableField("created_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("updated_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @TableField("file_path")
    private String filePath;

    @TableField("look_count")
    private Integer lookCount;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("picture_path")
    private String picturePath;

    @TableField("publish_status")
    private Integer publishStatus;

    @TableField("status")
    private Integer status;

    @TableField("describe")
    private String describe;

    @TableField(exist = false)
    private String createdName;
}
