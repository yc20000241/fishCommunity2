package com.yc.community.sys.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUserInfoResponse {

    private String nick;

    private String picturePath;

    private String id;
}
