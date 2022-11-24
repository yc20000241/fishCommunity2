package com.yc.community.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_info")
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    @TableField("role_name")
    private String roleName;

    @TableField("active")
    private Integer active;

    @TableField("role_code")
    private Integer roleCode;
}
