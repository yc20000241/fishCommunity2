package com.yc.community.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission_info")
public class PermissionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("path")
    private String path;

    @TableField("name")
    private String name;

    @TableField("parent_id")
    private String parentId;


}
