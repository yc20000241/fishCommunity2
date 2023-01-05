package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.mapper.FishUserFriendRelationMapper;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
@Service
public class FishUserFriendRelationServiceImpl extends ServiceImpl<FishUserFriendRelationMapper, FishUserFriendRelation> implements IFishUserFriendRelationService {

    @Override
    public void addFriend(FishUserFriendRelation fishUserFriendRelation) {
        
    }
}
