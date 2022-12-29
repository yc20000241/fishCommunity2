package com.yc.community.common.util;

public class HTMLUtil {

    public static String font(String str, String color){
        str = "<font style=\"color:"+color+";\">"+ str + "</font>";
        return str;
    }

    public static String aFont(String url, String str, String color){
        str = font(str,color);
        str = "<a href=\""+url+"\" style=\"cursor:pointer;\">"+str+"</a>";
        return str;
    }


}
