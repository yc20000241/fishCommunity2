package com.yc.community.service.modules.chats.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.community.service.modules.chats.entity.FishTempChatInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yc001
 * @since 2022-12-30
 */
@Mapper
@Component
public interface FishTempChatInfoMapper extends BaseMapper<FishTempChatInfo> {

}
