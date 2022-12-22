package com.yc.community.common.commonConst;


import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ConstList {

    public static List<String> NO_PERMISSION_LOGIN_MATCH_URL; // 不需要授权的url

    public static String ARTICLE_BUCKET = "articles";

    public static List<String> PUBLIC_PERMISSION_URL;

    static {
        NO_PERMISSION_LOGIN_MATCH_URL = Arrays.asList("/api/web/sys/**", "/api/web/home/**",
                "/api/common/minio/**","/websocket/**");
    }
}
