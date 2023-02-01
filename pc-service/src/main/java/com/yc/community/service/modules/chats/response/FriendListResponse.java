package com.yc.community.service.modules.chats.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendListResponse {

    private String id;

    private String nick;

    private String picturePath;

    private String sign;
}
