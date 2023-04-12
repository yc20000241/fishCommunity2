package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.entity.FishUserMongo;
import com.yc.community.service.modules.chats.mapper.FishUserFriendRelationMapper;
import com.yc.community.service.modules.chats.response.FriendListResponse;
import com.yc.community.service.modules.chats.response.FriendMapPointResponse;
import com.yc.community.service.modules.chats.service.IFishUserFriendRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Resource
    private MongoTemplate mongoTemplate;

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
    public FriendMapPointResponse searchFriendPoint(String userId, Integer radius) {

        List<FishUserMongo> fishUserMongoList = mongoTemplate.find(new Query(Criteria.where("id").is(userId)), FishUserMongo.class);
        if(CollectionUtils.isEmpty(fishUserMongoList))
            throw new BusinessException(BusinessExceptionCode.USR_POINT_NOT_EXIST);
        String longitude = fishUserMongoList.get(0).getLongitude();
        String latitude = fishUserMongoList.get(0).getLatitude();

        List<FishUserFriendRelation> fishUserFriendRelations = list(new QueryWrapper<FishUserFriendRelation>().eq("user_id", userId));
        List<String> friendUserIdList = new ArrayList<>();
        List<FishUserMongo> fishUserMongos = null;
        if(!CollectionUtils.isEmpty(fishUserFriendRelations))
            friendUserIdList = fishUserFriendRelations.stream().map(FishUserFriendRelation::getFriendId).collect(Collectors.toList());

        Query query = new Query();
        if(!CollectionUtils.isEmpty(friendUserIdList)){
            query = new Query(Criteria.where("id").in(friendUserIdList));
        }
        Point point = new Point(Double.valueOf(longitude), Double.valueOf(latitude));
        Distance distance = new Distance(radius.doubleValue() * 0.00062137119 / 3963.2);
        Circle circle = new Circle(point, distance);
        query.addCriteria(Criteria.where("location").withinSphere(circle));
        fishUserMongos = mongoTemplate.find(query, FishUserMongo.class);

        fishUserMongos = Optional.ofNullable(fishUserMongos).orElse(new ArrayList<>());
        FriendMapPointResponse friendMapPointResponse = FriendMapPointResponse.builder().list(fishUserMongos).centerX(latitude).centerY(longitude).build();
        return friendMapPointResponse;
    }
}
