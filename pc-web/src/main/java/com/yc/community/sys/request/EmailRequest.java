package com.yc.community.sys.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailRequest {

    @NotBlank(message = "邮箱不可为空")
    private String loginEmail;
}
