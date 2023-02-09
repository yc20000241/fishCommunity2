package com.yc.community.service.modules.articles.esMapper;

import cn.easyes.core.conditions.interfaces.BaseEsMapper;
import com.yc.community.service.modules.articles.entity.EsArticle;
import org.springframework.stereotype.Component;

@Component
public interface ArticleMapper extends BaseEsMapper<EsArticle> {
}
