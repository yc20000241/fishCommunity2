package com.yc.community.service.modules.chats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.entity.FishUserMongo;
import com.yc.community.service.modules.chats.response.FriendListResponse;
import com.yc.community.service.modules.chats.response.FriendMapPointResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
public interface IFishUserFriendRelationService extends IService<FishUserFriendRelation> {


    List<FriendListResponse> friendList(String userId);

    FriendMapPointResponse searchFriendPoint(String userId, Integer distance);

    void saveTestMongoData();
}
