package com.yc.community.sys.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrateRequest {

    @NotBlank(message = "邮箱不可为空")
    private String loginEmail;

    @NotBlank(message = "验证码不可为空")
    private String emailVerification;
}
