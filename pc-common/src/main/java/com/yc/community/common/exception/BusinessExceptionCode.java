package com.yc.community.common.exception;

public enum BusinessExceptionCode {

    EMAIL_FORMAT_ERROR("邮箱格式错误", 601),
    EMAIL_VERIFICATION_TOO_MUNCH("验证码发送过于频繁", 602),
    EMIAL_SYSTEM_ERROR("验证码发送错误，请联系管理员",603)
    ;

    private String desc;

    private int statuCode;

    BusinessExceptionCode(String desc, int statuCode) {
        this.desc = desc;
        this.statuCode = statuCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(int statuCode) {
        this.statuCode = statuCode;
    }
}
