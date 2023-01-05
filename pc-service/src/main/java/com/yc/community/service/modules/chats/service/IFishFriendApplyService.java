package com.yc.community.service.modules.chats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.chats.entity.FishFriendApply;
import com.yc.community.service.modules.chats.response.FriendApplyListResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2023-01-05
 */
public interface IFishFriendApplyService extends IService<FishFriendApply> {

    void friendApply(FishFriendApply fishFriendApply);

    List<FriendApplyListResponse> getList(String userId);

    Integer getRemindCount(String userId);

    void hasReadApply(String userId);
}
