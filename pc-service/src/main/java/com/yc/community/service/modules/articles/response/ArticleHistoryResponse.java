package com.yc.community.service.modules.articles.response;

import com.yc.community.service.modules.articles.entity.FishArticles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHistoryResponse {

    private Integer total;

    private List<FishArticles> list;
}
