package com.yc.community.service.modules.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.community.service.modules.articles.entity.FishComments;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yc001
 * @since 2022-12-19
 */
public interface IFishCommentsService extends IService<FishComments> {

    void commitComment(FishComments fishComments);

    List<FishComments> getCommentList(String articleId);
}
