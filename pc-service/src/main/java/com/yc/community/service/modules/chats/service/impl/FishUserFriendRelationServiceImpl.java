package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.mapper.FishUserFriendRelationMapper;
import com.yc.community.service.modules.chats.response.FriendListResponse;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource(description = "userInfoCache")
    private Cache<String, Object> userInfoCache;

    @Override
    public List<FriendListResponse> friendList(String userId) {
        List<FriendListResponse> friendListResponses = new ArrayList<>();

        List<FishUserFriendRelation> fishUserFriendRelations = list(new QueryWrapper<FishUserFriendRelation>().eq("user_id", userId));
        fishUserFriendRelations.forEach(x -> {
            FriendListResponse friendListResponse = new FriendListResponse();
            friendListResponse.setId(x.getId());
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getId());
            friendListResponse.setNick(userInfo.getNick());
            friendListResponse.setPicturePath(userInfo.getPicturePath());
            friendListResponse.setSign(userInfo.getSign());
        });
        return friendListResponses;
    }
}
