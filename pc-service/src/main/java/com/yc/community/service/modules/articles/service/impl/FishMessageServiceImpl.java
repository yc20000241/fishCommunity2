package com.yc.community.service.modules.articles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.mapper.FishMessageMapper;
import com.yc.community.service.modules.articles.service.IFishMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-21
 */
@Service
public class FishMessageServiceImpl extends ServiceImpl<FishMessageMapper, FishMessage> implements IFishMessageService {

    @Override
    public List<FishMessage> getMessageList(String userId) {
        List<FishMessage> list = list(new QueryWrapper<FishMessage>().eq("receive_id", userId).orderByDesc("created_time"));
        return list;
    }
}
