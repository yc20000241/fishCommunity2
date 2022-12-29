package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishMessage;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-21
 */
public interface IFishMessageService extends IService<FishMessage> {

    List<FishMessage> getMessageList(String userId);
}
