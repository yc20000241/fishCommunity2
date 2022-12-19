package com.yc.community.service.modules.articles.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.mapper.FishCommentsMapper;
import com.yc.community.service.modules.articles.service.IFishCommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-12-19
 */
@Service
public class FishCommentsServiceImpl extends ServiceImpl<FishCommentsMapper, FishComments> implements IFishCommentsService {

}
