package com.yc.community.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.community.common.config.PropertiesConfig;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.IpAddrUtil;
import com.yc.community.common.util.UUIDUtil;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.sys.entity.UserInfo;
import com.yc.community.sys.request.EmailRequest;
import com.yc.community.sys.util.AccessToken;
import com.yc.community.sys.util.AuthProvider;
import com.yc.community.sys.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@Slf4j
@Service
public class AuthServiceImpl{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private PropertiesConfig propertiesConfig;

    public AccessToken login(String loginAccount, String password) {
        // 1 创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernameAuthentication = new UsernamePasswordAuthenticationToken(loginAccount, password);
        // 2 认证
        Authentication authentication = this.authenticationManager.authenticate(usernameAuthentication);
        // 3 保存认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4 生成自定义token
        AccessToken accessToken = jwtProvider.createToken((UserDetails) authentication.getPrincipal());
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        // 放入缓存
        String username = userDetail.getUsername();
        redisTemplate.opsForValue().set(username, userDetail);
        // json解析不了双重对象
//        stringRedisTemplate.opsForValue().set(userDetail.getUsername(), JSON.toJSONString(userDetail));
        return accessToken;
    }

    public void logout() {
        redisTemplate.opsForValue().set(AuthProvider.getLoginAccount(), "");
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
        simpleMailMessage.setSubject("咸鱼社区注册验证码");
        simpleMailMessage.setText("欢迎注册咸鱼社区，验证码为"+emailVerification+"，有效时间为5分钟");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setCc(propertiesConfig.getMailSendFrom());
        simpleMailMessage.setFrom(propertiesConfig.getMailSendFrom());

        try {
            mailSender.send(simpleMailMessage);
            stringRedisTemplate.opsForValue().set(key, emailVerification, 60 * 5, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionCode.EMIAL_SYSTEM_ERROR);
        }

    }

    /**
     *  校验是否是多次发送
     * */
    private String getVerificationExpire(String email, HttpServletRequest request) {
        List<UserInfo> list = userInfoService.list(new QueryWrapper<UserInfo>().eq("user_name", email));
        if(list.size() > 0){  // 用户已注册
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(email + "_email");
            if(expire == -2){   // 不存在此键，即不久前未发送验证码
                return email + "_email";
            }else{
                throw new BusinessException(BusinessExceptionCode.EMAIL_VERIFICATION_TOO_MUNCH);
            }
        }else{  // 用户未注册
            String ipAddr = IpAddrUtil.getIpAddr(request);
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(ipAddr + "_email");
            if(expire == -2){   // 不存在此键，即不久前未发送验证码
                return ipAddr + "_email";
            }else
                throw new BusinessException(BusinessExceptionCode.EMAIL_VERIFICATION_TOO_MUNCH);
        }
    }
}
