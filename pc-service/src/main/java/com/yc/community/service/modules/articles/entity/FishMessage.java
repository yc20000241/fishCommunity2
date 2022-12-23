package com.yc.community.service.modules.articles.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yc001
 * @since 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_message")
public class FishMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("created_id")
    private String createdId;

    @TableField("created_name")
    private String createdName;

    @TableField("receive_id")
    private String receiveId;

    @TableField("content")
    private String content;

    @TableField("category")
    private Integer category;

    @TableField("created_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("category_content")
    private String categoryContent;

    @TableField("url")
    private String url;
}
