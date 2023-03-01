package com.yc.community.service.modules.articles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.MessageCategoryEnum;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.service.dataHandled.initMessage.MessageAdapter;
import com.yc.community.service.modules.articles.entity.FishFocusRelation;
import com.yc.community.service.modules.articles.mapper.FishFocusRelationMapper;
import com.yc.community.service.modules.articles.service.IFishFocusRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2023-03-01
 */
@Service
public class FishFocusRelationServiceImpl extends ServiceImpl<FishFocusRelationMapper, FishFocusRelation> implements IFishFocusRelationService {

    @Resource(name = "userInfoCache")
    private Cache<String, Object> userInfoCache;

    @Autowired
    private MessageAdapter messageAdapter;

    @Override
    public void addRelation(FishFocusRelation fishFocusRelation) {
        if(fishFocusRelation.getReadId().equals(fishFocusRelation.getWriterId()))
            throw new BusinessException(BusinessExceptionCode.FOCUS_YOU_SELF);

        List<FishFocusRelation> list = list(new QueryWrapper<FishFocusRelation>().eq("read_id", fishFocusRelation.getReadId()).eq("writer_id", fishFocusRelation.getWriterId()));
        fishFocusRelation.setId(UUIDUtil.getUUID());
        if(list.size() > 0){
            FishFocusRelation fishFocusRelation1 = list.get(0);
            removeById(fishFocusRelation1);
        }else{
            save(fishFocusRelation);

            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("receive", fishFocusRelation.getWriterId());
            stringObjectHashMap.put("userInfo", userInfoCache.getIfPresent(fishFocusRelation.getReadId()));
            messageAdapter.adapter(MessageCategoryEnum.USER_FOCUS.getCategory(), stringObjectHashMap);
        }
    }

    @Override
    public Boolean ifFocus(FishFocusRelation fishFocusRelation) {
        List<FishFocusRelation> list = list(new QueryWrapper<FishFocusRelation>().eq("read_id", fishFocusRelation.getReadId()).eq("writer_id", fishFocusRelation.getWriterId()));
        if(list.size() > 0)
            return true;
        return false;
    }
}
