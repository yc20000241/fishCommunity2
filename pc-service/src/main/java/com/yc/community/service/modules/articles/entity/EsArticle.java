package com.yc.community.service.modules.articles.entity;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;

import cn.easyes.annotation.rely.FieldType;

import cn.easyes.annotation.rely.IdType;
import lombok.Data;

@Data
@IndexName(value = "article_index")
public class EsArticle {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    @IndexField(fieldType = FieldType.TEXT)
    private String title;

    @IndexField(fieldType = FieldType.TEXT)
    private String articleContent;
}
