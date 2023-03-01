package com.yc.community.common.exception;

public enum BusinessExceptionCode {

    EMAIL_FORMAT_ERROR("邮箱格式错误", 601),
    EMAIL_VERIFICATION_TOO_MUNCH("验证码发送过于频繁", 602),
    EMIAL_SYSTEM_ERROR("验证码发送错误，请联系管理员",603),
    USER_HAS_REGISTRATED("用户已注册", 604),
    VERIFICATION_ERROR("验证码错误", 605),
    VERIFICATION_NOT_EXISTS("验证码不存在",606),
    TODAY_HAS_SIGNED("今日已签到", 607),
    ARTICLE_UPLOAD_FILE("文件上传失败",608),
    ARTICLE_NOT_APPLIED("文章未审批",609),
    ARTICLE_HAS_LIKED("此篇文章已点赞", 610),
    COMMENT_HAS_LIKEN("此评论已点赞", 611),
    EDIT_PASSWORD_NEED_OLD_AND_NEW("修改密码，需要新密码和旧密码", 612),
    YOU_HAS_BEEN_FRIENDEN("你们已经是好友了",613),
    YOU_HAS_SEND_FRIEND_APPLY("你已向他发送过好友请求",614),
    PLease_ADMIN_GIVE_A_ROLE("请管理员分配角色", 615),
    FOCUS_YOU_SELF("关注自己，这河狸嘛", 616)
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
