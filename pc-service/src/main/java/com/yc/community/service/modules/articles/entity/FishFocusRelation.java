package com.yc.community.service.modules.articles.entity;

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
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fish_focus_relation")
public class FishFocusRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("read_id")
    private String readId;

    @TableField("writer_id")
    private String writerId;


}
