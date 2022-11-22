package com.yc.community.sys.entity;

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
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("nick")
    private String nick;

    @TableField("email")
    private String email;

    @TableField("last_login")
    private Date lastLogin;

    @TableField("created_time")
    private Date createdTime;

    @TableField("active")
    private Integer active;


}
