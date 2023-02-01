package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.CopyUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishFriendApply;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.mapper.FishFriendApplyMapper;
import com.yc.community.service.modules.chats.response.FriendApplyListResponse;
import com.yc.community.service.modules.chats.service.IFishFriendApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2023-01-05
 */
@Service
public class FishFriendApplyServiceImpl extends ServiceImpl<FishFriendApplyMapper, FishFriendApply> implements IFishFriendApplyService {

    @Resource(description = "userInfoCache")
    private Cache<String, Object> userInfoCache;

    @Autowired
    private MessageAdapter messageAdapter;

    @Autowired
    private FishUserFriendRelationServiceImpl fishUserFriendRelationService;


    @Override
    public void friendApply(FishFriendApply fishFriendApply) {
        QueryWrapper<FishUserFriendRelation> fishUserFriendRelationQueryWrapper = new QueryWrapper<>();
        fishUserFriendRelationQueryWrapper.eq("user_id", fishFriendApply.getFromUserId())
                .eq("friend_id", fishFriendApply.getToUserId());
        int count = fishUserFriendRelationService.count(fishUserFriendRelationQueryWrapper);
        if(count > 0)
            throw new BusinessException(BusinessExceptionCode.YOU_HAS_BEEN_FRIENDEN);

        QueryWrapper<FishFriendApply> fishFriendApplyQueryWrapper = new QueryWrapper<>();
        fishFriendApplyQueryWrapper.eq("from_user_id", fishFriendApply.getFromUserId())
                .eq("to_user_id", fishFriendApply.getToUserId())
                .eq("if_accept", -1);
        int count1 = count(fishFriendApplyQueryWrapper);
        if(count1 > 0)
            throw new BusinessException(BusinessExceptionCode.YOU_HAS_SEND_FRIEND_APPLY);

        fishFriendApply.setId(UUIDUtil.getUUID());
        fishFriendApply.setStatus(0);
        fishFriendApply.setIfAccept(-1);
        fishFriendApply.setCreatedTime(new Date());
        save(fishFriendApply);

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        Object fromUserInfo = userInfoCache.getIfPresent(fishFriendApply.getFromUserId());
        stringObjectHashMap.put("fromUserInfo",fromUserInfo);
        stringObjectHashMap.put("toUserId", fishFriendApply.getToUserId());
        stringObjectHashMap.put("content", fishFriendApply.getContent());

        messageAdapter.adapter(MessageCategoryEnum.ADD_FRIEND_APPLY.getCategory(),stringObjectHashMap);
    }

    @Override
    public List<FriendApplyListResponse> getList(String userId) {
        QueryWrapper<FishFriendApply> fishFriendApplyQueryWrapper = new QueryWrapper<>();
        fishFriendApplyQueryWrapper.eq("to_user_id", userId)
                .eq("if_accept",-1).orderByDesc("created_time").last("limit 30");
        List<FishFriendApply> list = list(fishFriendApplyQueryWrapper);
        List<FriendApplyListResponse> friendApplyListResponses = CopyUtil.copyList(list, FriendApplyListResponse.class);
        friendApplyListResponses.forEach(x -> {
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(x.getFromUserId());
            x.setUserName(userInfo.getNick());
            x.setUserPicture(userInfo.getPicturePath());
        });
        return friendApplyListResponses;
    }

    @Override
    public Integer getRemindCount(String userId) {
        QueryWrapper<FishFriendApply> fishFriendApplyQueryWrapper = new QueryWrapper<>();
        fishFriendApplyQueryWrapper.eq("status", 0)
                .eq("to_user_id", userId);
        int count = count(fishFriendApplyQueryWrapper);
        return count;
    }

    @Override
    public void hasReadApply(String userId) {
        QueryWrapper<FishFriendApply> fishFriendApplyQueryWrapper = new QueryWrapper<>();
        fishFriendApplyQueryWrapper.eq("to_user_id",userId).eq("status", 0);
        List<FishFriendApply> list = list(fishFriendApplyQueryWrapper);
        list.forEach(x -> {
            x.setStatus(1);
        });
        updateBatchById(list);
    }

    @Override
    @Transactional
    public void changeFriendApply(String id, Integer changeStatus) {
        FishFriendApply byId = getById(id);
        byId.setIfAccept(changeStatus);
        updateById(byId);

        ArrayList<FishUserFriendRelation> fishUserFriendRelations = new ArrayList<>();
        FishUserFriendRelation fishUserFriendRelation1 = new FishUserFriendRelation();
        fishUserFriendRelation1.setId(UUIDUtil.getUUID());
        fishUserFriendRelation1.setFriendId(byId.getFromUserId());
        fishUserFriendRelation1.setUserId(byId.getToUserId());
        fishUserFriendRelations.add(fishUserFriendRelation1);

        FishUserFriendRelation fishUserFriendRelation2 = new FishUserFriendRelation();
        fishUserFriendRelation2.setId(UUIDUtil.getUUID());
        fishUserFriendRelation2.setFriendId(byId.getFromUserId());
        fishUserFriendRelation2.setUserId(byId.getToUserId());
        fishUserFriendRelations.add(fishUserFriendRelation2);

        fishUserFriendRelationService.saveBatch(fishUserFriendRelations);
    }
}
