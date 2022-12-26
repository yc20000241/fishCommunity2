package com.yc.community.sys.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class EditUserInfoRequest {

    @NotBlank(message = "编辑信息id不可为空")
    private String id;

    @NotBlank(message = "昵称不可为空")
    private String nick;

    private String oldPassword;

    private String newPassword;

    private String sign;

    private MultipartFile userPictureFile;
}
