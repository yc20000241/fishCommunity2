package com.yc.community.service.modules.articles.entity;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;


import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;

import cn.easyes.annotation.rely.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@IndexName(value = "article_index")
public class EsArticle {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

//    @IndexField(value = "title",fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    @IndexField(value = "title",fieldType = FieldType.TEXT)
    @HighLight(mappingField = "highlightTitle", fragmentSize=50)
    private String title;

//    @IndexField(value = "article_content", fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    @IndexField(value = "article_content", fieldType = FieldType.TEXT)
    @HighLight(mappingField = "highlightContent", fragmentSize=50)
    private String articleContent;

    @IndexField("created_id")
    private String createdId;

    @IndexField("created_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @IndexField("look_count")
    private Integer lookCount;

    @IndexField("like_count")
    private Integer likeCount;

    @IndexField(exist = false)
    private String highlightContent;

    @IndexField(exist = false)
    private String highlightTitle;
}
