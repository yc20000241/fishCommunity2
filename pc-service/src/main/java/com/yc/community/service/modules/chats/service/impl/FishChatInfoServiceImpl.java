package com.yc.community.service.modules.chats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.chats.entity.FishChatInfo;
import com.yc.community.service.modules.chats.mapper.FishChatInfoMapper;
import com.yc.community.service.modules.chats.service.IFishChatInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
public class FishChatInfoServiceImpl extends ServiceImpl<FishChatInfoMapper, FishChatInfo> implements IFishChatInfoService {

    @Resource(description = "userInfoCache")
    private Cache<String, Object> userInfoCache;

    public Integer getNotReadChatCount(String userId) {
        QueryWrapper<FishChatInfo> fishChatInfoQueryWrapper = new QueryWrapper<>();
        fishChatInfoQueryWrapper.eq("friend_id", userId)
                .eq("has_read", 0).select("distinct user_id");
        int count = count(fishChatInfoQueryWrapper);
        return count;
    }

    @Override
    public List<FishChatInfo> getNotReadChatList(String userId) {
        QueryWrapper<FishChatInfo> fishChatInfoQueryWrapper = new QueryWrapper<>();
        fishChatInfoQueryWrapper.eq("friend_id", userId)
                .eq("has_read", 0);
        List<FishChatInfo> list = list(fishChatInfoQueryWrapper);

        List<FishChatInfo> result = new ArrayList<>();
        Map<String, List<FishChatInfo>> collect = list.stream().collect(Collectors.groupingBy(FishChatInfo::getUserId));
        collect.keySet().forEach(key -> {
            //根据时间倒序
            List<FishChatInfo> list1 = collect.get(key).stream().sorted(Comparator.comparing(FishChatInfo::getCreatedTime).reversed()).collect(Collectors.toList());
            FishChatInfo fishChatInfo = list1.get(0);
            UserInfo userInfo = (UserInfo)userInfoCache.getIfPresent(fishChatInfo.getUserId());
            fishChatInfo.setNick(userInfo.getNick());
            fishChatInfo.setPicturePath(userInfo.getPicturePath());
            result.add(fishChatInfo);
        });

        return result;
    }

    @Override
    public List<FishChatInfo> getFriendChatInfoList(String userId, String friendId) {
        QueryWrapper<FishChatInfo> fishChatInfoQueryWrapper = new QueryWrapper<>();
        fishChatInfoQueryWrapper.and(wrapper -> wrapper.eq("user_id", userId).eq("friend_id", friendId))
                .or(wrapper1 -> wrapper1.eq("user_id", friendId).eq("friend_id", userId));
        fishChatInfoQueryWrapper.last("limit 20").orderByAsc("created_time");
        List<FishChatInfo> list = list(fishChatInfoQueryWrapper);
        return list;
    }
}
