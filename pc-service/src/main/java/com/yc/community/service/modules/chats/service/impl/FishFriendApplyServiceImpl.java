package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.util.CopyUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishFriendApply;
import com.yc.community.service.modules.chats.mapper.FishFriendApplyMapper;
import com.yc.community.service.modules.chats.response.FriendApplyListResponse;
import com.yc.community.service.modules.chats.service.IFishFriendApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public void friendApply(FishFriendApply fishFriendApply) {
        fishFriendApply.setId(UUIDUtil.getUUID());
        fishFriendApply.setStatus(0);
        fishFriendApply.setIfAccept(-1);

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
        Integer count = count(fishFriendApplyQueryWrapper);
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
}
