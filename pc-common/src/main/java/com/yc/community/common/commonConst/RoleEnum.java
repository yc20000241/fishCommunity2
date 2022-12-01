package com.yc.community.common.commonConst;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleEnum {
    ADMIN("1", "管理员"),
    USER("2", "普通用户"),
    BAN_USER("3", "封禁用户");

    private String code;

    private String value;

    public String getCode(){
        return code;
    }

    public String getValue(){
        return value;
    }
}
