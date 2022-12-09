package com.yc.community.service.modules.articles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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

    @TableField("id")
    private String id;

    @TableField("title")
    private String title;

    @TableField("created_id")
    private String createdId;

    @TableField("created_time")
    private Date createdTime;

    @TableField("created_name")
    private String createdName;

    @TableField("updated_time")
    private Date updatedTime;

    @TableField("file_path")
    private String filePath;

    @TableField("look_count")
    private Integer lookCount;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("picture_path")
    private String picturePath;


}
