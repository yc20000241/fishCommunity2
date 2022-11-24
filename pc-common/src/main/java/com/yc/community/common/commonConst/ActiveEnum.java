package com.yc.community.common.commonConst;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActiveEnum {

    ACTIVE(1, "可用"),
    BAN(0,"不可用");

    private Integer code;

    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
