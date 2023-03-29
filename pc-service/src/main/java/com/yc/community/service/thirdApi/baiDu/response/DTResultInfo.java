package com.yc.community.service.thirdApi.baiDu.response;

import lombok.Data;

@Data
public class DTResultInfo<T> {

    private String address;

    private T content;

    private Integer status;
}
