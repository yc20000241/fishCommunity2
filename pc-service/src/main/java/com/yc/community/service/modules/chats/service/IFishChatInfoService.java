package com.yc.community.service.modules.chats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.chats.entity.FishChatInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
public interface IFishChatInfoService extends IService<FishChatInfo> {

    Integer getNotReadChatCount(String userId);

    List<FishChatInfo> getNotReadChatList(String userId);

    List<FishChatInfo> getFriendChatInfoList(String userId, String friendId);
}
