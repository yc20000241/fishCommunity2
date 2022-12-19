package com.yc.community.service.modules.articles.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.community.service.modules.articles.entity.FishComments;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yc001
 * @since 2022-12-19
 */
@Mapper
@Component
public interface FishCommentsMapper extends BaseMapper<FishComments> {

}
