package com.yc.community.security.component;

import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.security.entity.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Slf4j
public class AccessDecisionProcessor implements AccessDecisionVoter<FilterInvocation> {

    @Resource(name = "rolePermissionListCache")
    private Cache<String, List<String>> rolePermissionListCache;

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert object != null;

        // 拿到当前请求uri
        String requestUrl = object.getRequestUrl().split("\\?")[0];

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String url : ConstList.NO_PERMISSION_LOGIN_MATCH_URL) {
            if (antPathMatcher.match(url, requestUrl))
                return ACCESS_GRANTED;
        }

        if(ConstList.PUBLIC_PERMISSION_URL.contains(requestUrl))
            return ACCESS_GRANTED;

        String method = object.getRequest().getMethod();
        log.debug("进入自定义鉴权投票器，URI : {} {}", method, requestUrl);

        // 拿到当前用户所具有的权限
        List<String> roles = ((UserDetail) authentication.getPrincipal()).getRoles();
        for (String role : roles) {
            List<String> pathList = rolePermissionListCache.getIfPresent(role);
            if (pathList.contains(requestUrl)) {
                return ACCESS_GRANTED;
            }
        }

        return ACCESS_DENIED;

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
