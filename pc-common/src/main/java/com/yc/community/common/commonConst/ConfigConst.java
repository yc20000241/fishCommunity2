package com.yc.community.common.commonConst;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigConst {

    @Value("${baidu.ditu.ak}")
    public String BAI_DU_DI_TU_AK;
}
