package com.yc.community.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.community.common.commonConst.ActiveEnum;
import com.yc.community.common.commonConst.RoleEnum;
import com.yc.community.common.config.PropertiesConfig;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.IpAddrUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.RoleUser;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.sys.request.EmailRequest;
import com.yc.community.sys.request.RegistrateRequest;
import com.yc.community.sys.util.AccessToken;
import com.yc.community.sys.util.AuthProvider;
import com.yc.community.sys.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AuthServiceImpl{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "CustomMailSender")
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private RoleUserServiceImpl roleUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AccessToken login(String loginAccount, String password) {
        // 1 ??????UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernameAuthentication = new UsernamePasswordAuthenticationToken(loginAccount, password);
        // 2 ??????
        Authentication authentication = this.authenticationManager.authenticate(usernameAuthentication);
        // 3 ??????????????????
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4 ???????????????token
        AccessToken accessToken = jwtProvider.createToken((UserDetails) authentication.getPrincipal());
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        //??????????????????
        List<RoleUser> user_id = roleUserService.list(new QueryWrapper<RoleUser>().eq("user_id", userDetail.getUserInfo().getId()));
        List<String> roleIds = user_id.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        userDetail.setRoles(roleIds);
        // ????????????
        String username = userDetail.getUsername();
        redisTemplate.opsForValue().set(username, userDetail, 3600 * 24 * 7, TimeUnit.SECONDS);
        // json????????????????????????
//        stringRedisTemplate.opsForValue().set(userDetail.getUsername(), JSON.toJSONString(userDetail));
        return accessToken;
    }

    public void logout() {
        redisTemplate.opsForValue().set(AuthProvider.getLoginAccount(), null);
        SecurityContextHolder.clearContext();
    }


    public void refreshToken(String token) {
        AccessToken accessToken = jwtProvider.refreshToken(token);
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get(accessToken.getLoginAccount());
        redisTemplate.opsForValue().set(accessToken.getLoginAccount(), userDetail);
//        String userDetail = stringRedisTemplate.opsForValue().get(accessToken.getLoginAccount());
//        stringRedisTemplate.opsForValue().set(accessToken.getLoginAccount(), userDetail);
    }

    public void sendEmail(EmailRequest emailRequest, HttpServletRequest request) {
        String email = emailRequest.getLoginEmail();

        boolean isMatch = Pattern.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email);
        if(!isMatch)
            throw new BusinessException(BusinessExceptionCode.EMAIL_FORMAT_ERROR);

        String key = getVerificationExpire(email, request);

        String emailVerification = UUIDUtil.getUUID().substring(0, 6);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("???????????????????????????");
        simpleMailMessage.setText("???????????????????????????????????????"+emailVerification+"??????????????????5??????");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setCc(propertiesConfig.getMailSendFrom());
        simpleMailMessage.setFrom(propertiesConfig.getMailSendFrom());

        try {
            javaMailSender.send(simpleMailMessage);
            stringRedisTemplate.opsForValue().set(key, emailVerification, 60 * 5, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionCode.EMIAL_SYSTEM_ERROR);
        }

    }

    /**
     *  ???????????????????????????
     * */
    private String getVerificationExpire(String email, HttpServletRequest request) {
        List<UserInfo> list = userInfoService.list(new QueryWrapper<UserInfo>().eq("user_name", email));
        if(list.size() > 0){  // ???????????????
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(email + "_email");
            if(expire == -2){   // ????????????????????????????????????????????????
                return email + "_email";
            }else{
                throw new BusinessException(BusinessExceptionCode.EMAIL_VERIFICATION_TOO_MUNCH);
            }
        }else{  // ???????????????
            String ipAddr = IpAddrUtil.getIpAddr(request);
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(ipAddr + "_email");
            if(expire == -2){   // ????????????????????????????????????????????????
                return ipAddr + "_email";
            }else
                throw new BusinessException(BusinessExceptionCode.EMAIL_VERIFICATION_TOO_MUNCH);
        }
    }

    @Transactional
    public void registration(RegistrateRequest registrateRequest, HttpServletRequest request) {
        boolean isMatch = Pattern.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", registrateRequest.getLoginEmail());
        if(!isMatch)
            throw new BusinessException(BusinessExceptionCode.EMAIL_FORMAT_ERROR);

        List<UserInfo> list = userInfoService.list(new QueryWrapper<UserInfo>().eq("user_name", registrateRequest.getLoginEmail()));
        if(list.size() > 0)
            throw new BusinessException(BusinessExceptionCode.USER_HAS_REGISTRATED);

        String key = getIsVerification(registrateRequest.getLoginEmail(), request);
        String verification = stringRedisTemplate.opsForValue().get(key);
        if(!verification.equals(registrateRequest.getEmailVerification()))
            throw new BusinessException(BusinessExceptionCode.VERIFICATION_ERROR);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(registrateRequest.getLoginEmail());
        userInfo.setActive(ActiveEnum.ACTIVE.getCode());
        userInfo.setCreatedTime(new Date());
        userInfo.setPassword(passwordEncoder.encode(registrateRequest.getPassword()));
        userInfo.setNick("?????????");
        userInfo.setPicturePath("/article-image/default-avatar.b7d77977.png");
        String uuid = UUIDUtil.getUUID();
        userInfo.setId(uuid);
        userInfoService.save(userInfo);

        RoleUser roleUser = new RoleUser();
        roleUser.setRoleId(RoleEnum.USER.getCode());
        roleUser.setUserId(uuid);
        roleUserService.save(roleUser);
    }

    private String getIsVerification(String email, HttpServletRequest request) {
        Long expire1 = stringRedisTemplate.opsForValue().getOperations().getExpire(email + "_email");
        if(expire1 > 0)
            return email + "_email";

        String ipAddr = IpAddrUtil.getIpAddr(request);
        Long expire2 = stringRedisTemplate.opsForValue().getOperations().getExpire(ipAddr + "_email");
        if(expire2 > 0)
            return ipAddr + "_email";

        throw new BusinessException(BusinessExceptionCode.VERIFICATION_NOT_EXISTS);
    }
}
