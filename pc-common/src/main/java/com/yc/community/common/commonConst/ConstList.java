package com.yc.community.common.commonConst;


import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ConstList {

    public static String NO_PERMISSION_LOGIN_MATCH_URL; // 不需要授权的url

    static {
        NO_PERMISSION_LOGIN_MATCH_URL = "/api/web/sys/**";
    }
}
