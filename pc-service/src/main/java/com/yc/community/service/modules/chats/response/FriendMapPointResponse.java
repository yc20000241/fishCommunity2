package com.yc.community.service.modules.chats.response;

import com.yc.community.service.modules.chats.entity.FishUserMongo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendMapPointResponse {

    private List<FishUserMongo> list;

    private String centerX;

    private String centerY;
}
