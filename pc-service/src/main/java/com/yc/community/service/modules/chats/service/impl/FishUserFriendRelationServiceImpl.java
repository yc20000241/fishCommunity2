package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.entity.FishUserMongo;
import com.yc.community.service.modules.chats.mapper.FishUserFriendRelationMapper;
import com.yc.community.service.modules.chats.response.FriendListResponse;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.awt.*;
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

    @Resource(name = "chatUserChannelCache")
    private Cache<String, Object> chatUserCache;

    @Override
    public List<FriendListResponse> friendList(String userId) {
        List<FriendListResponse> friendListResponses = new ArrayList<>();

        List<FishUserFriendRelation> fishUserFriendRelations = list(new QueryWrapper<FishUserFriendRelation>().eq("user_id", userId));
        fishUserFriendRelations.forEach(x -> {
            FriendListResponse friendListResponse = new FriendListResponse();
            friendListResponse.setId(x.getFriendId());
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getFriendId());
            friendListResponse.setNick(userInfo.getNick());
            friendListResponse.setPicturePath(userInfo.getPicturePath());
            friendListResponse.setSign(userInfo.getSign());
            friendListResponses.add(friendListResponse);
        });
        return friendListResponses;
    }

    @Override
    public List<FishUserMongo> searchFriendPoint(String userId, Integer radius , String longitude, String latitude) {
        List<FishUserFriendRelation> fishUserFriendRelations = list(new QueryWrapper<FishUserFriendRelation>().eq("user_id", userId));
        ArrayList<String> onlineList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(fishUserFriendRelations))
            fishUserFriendRelations.forEach(x -> {
                if("online".equals(chatUserCache.getIfPresent(x.getFriendId())))
                    onlineList.add(x.getFriendId());
            });
        if(!CollectionUtils.isEmpty(onlineList)){
            Query query = new Query(Criteria.where("id").in(onlineList));

//            Point point = new Point(Double.valueOf(longitude), Double.valueOf(latitude));
//            Distance distance = new Distance(radius.doubleValue() * 0.00062137119 / 3963.2);
//            Circle circle = new Circle(point, distance);
//            Query query = new Query();
//            query.addCriteria(Criteria.where("location").withinSphere(circle));
        }

        return null;
    }
}
