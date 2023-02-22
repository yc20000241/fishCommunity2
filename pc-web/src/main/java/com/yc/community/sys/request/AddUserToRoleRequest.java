package com.yc.community.sys.request;

import com.yc.community.service.modules.articles.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToRoleRequest {

    private List<String> idList;

    private String roleId;
}
