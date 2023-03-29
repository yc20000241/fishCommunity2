package com.yc.community.service.modules.chats.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document("user")
public class FishUserMongo {

    private String id;          // 与usr_info表id相同

    private String address;     // 最近一次登录地址

    private String longitude;   // 经度

    private String latitude;    // 纬度

    private String ip;          // 上一次登录ip地址

    private String nick;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;          // 最近一次登录时间
}
