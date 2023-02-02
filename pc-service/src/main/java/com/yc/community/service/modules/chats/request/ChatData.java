package com.yc.community.service.modules.chats.request;

import com.yc.community.service.modules.chats.entity.FishChatInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatData {

    private Integer action;

    private FishChatInfo fishChatInfo;

    private String ctxId;
}
