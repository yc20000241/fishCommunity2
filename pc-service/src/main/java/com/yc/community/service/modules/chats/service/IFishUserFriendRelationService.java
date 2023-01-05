package com.yc.community.service.modules.chats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
public interface IFishUserFriendRelationService extends IService<FishUserFriendRelation> {

    void addFriend(FishUserFriendRelation fishUserFriendRelation);

}
