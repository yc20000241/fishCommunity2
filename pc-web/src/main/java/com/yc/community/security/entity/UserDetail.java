package com.yc.community.security.entity;

import com.yc.community.sys.entity.RoleInfo;
import com.yc.community.service.modules.articles.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    private UserInfo userInfo;
    private List<RoleInfo> roleInfoList;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private List<String> roles;


    public String getUserId() {
        return this.userInfo.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (grantedAuthorities != null) return this.grantedAuthorities;
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<String> authorities = new ArrayList<>();
        if(roleInfoList != null)
            roleInfoList.forEach(roleInfo -> {
                authorities.add(roleInfo.getId());
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleInfo.getId()));
            });
        this.grantedAuthorities = grantedAuthorities;
        this.roles = authorities;
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userInfo.getUserName();
    }

    /**
     * 账户是否没过期
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否没被锁定
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账户凭据是否没过期
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
