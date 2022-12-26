package com.yc.community.sys.response;

import lombok.Data;

import java.util.List;

@Data
public class InitUserInfoResponse {

    private String id;

    private String userName;

    private String nick;

    private String picturePath;

    private String sign;

    private List<MenuVo> menuVoList;
}
