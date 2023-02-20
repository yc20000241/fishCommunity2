package com.yc.community.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.yc.community.common.commonConst.ConstList;
import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.minio.MinioUtil;
import com.yc.community.common.util.CopyUtil;
import com.yc.community.security.entity.UserDetail;
import com.yc.community.service.modules.articles.entity.FishArticles;
import com.yc.community.service.modules.articles.entity.FishComments;
import com.yc.community.service.modules.articles.entity.FishMessage;
import com.yc.community.service.modules.articles.entity.UserInfo;
import com.yc.community.service.modules.articles.service.impl.FishArticlesServiceImpl;
import com.yc.community.service.modules.articles.service.impl.FishCommentsServiceImpl;
import com.yc.community.service.modules.articles.service.impl.FishMessageServiceImpl;
import com.yc.community.service.modules.chats.entity.FishFriendApply;
import com.yc.community.service.modules.chats.entity.FishUserFriendRelation;
import com.yc.community.service.modules.chats.service.impl.FishFriendApplyServiceImpl;
import com.yc.community.service.modules.chats.service.impl.FishUserFriendRelationServiceImpl;
import com.yc.community.sys.entity.RoleUser;
import com.yc.community.sys.mapper.RoleMenuMapper;
import com.yc.community.sys.mapper.UserInfoMapper;
import com.yc.community.sys.request.EditUserInfoRequest;
import com.yc.community.sys.response.AuthorUserInfoResponse;
import com.yc.community.sys.response.InitUserInfoResponse;
import com.yc.community.sys.response.MenuVo;
import com.yc.community.sys.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.community.sys.util.JwtProperties;
import com.yc.community.sys.util.JwtProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioUtil minioUtil;

    @Resource(name = "chatUserChannelCache")
    private Cache<String, Object> chatUserCache;

    @Autowired
    private RoleUserServiceImpl roleUserService;

    @Autowired
    private FishArticlesServiceImpl fishArticlesService;

    @Autowired
    private FishCommentsServiceImpl fishCommentsService;

    @Autowired
    private FishFriendApplyServiceImpl fishFriendApplyService;

    @Autowired
    private FishUserFriendRelationServiceImpl fishUserFriendRelationService;

    @Autowired
    private FishMessageServiceImpl fishMessageService;

    @Override
    public InitUserInfoResponse getInitUserInfo(HttpServletRequest request) {
        String authToken = jwtProvider.getToken(request);
        int length = jwtProperties.getTokenPrefix().length();
        authToken = authToken.substring(length);
        String loginAccount = jwtProvider.getSubjectFromToken(authToken);
        UserDetail userDetails = (UserDetail)redisTemplate.opsForValue().get(loginAccount);
        UserInfo userInfo = userDetails.getUserInfo();

        List<String> roles = userDetails.getRoles();
        List<MenuVo> list = roleMenuMapper.getRolesMenuList(roles);

        InitUserInfoResponse copy = CopyUtil.copy(userInfo, InitUserInfoResponse.class);
        copy.setMenuVoList(list);

        return copy;
    }

    @Override
    public AuthorUserInfoResponse getUserInfoById(String id) {
        UserInfo byId = getById(id);
        AuthorUserInfoResponse copy = CopyUtil.copy(byId, AuthorUserInfoResponse.class);
        return copy;
    }

    @Override
    public void editUserInfoById(EditUserInfoRequest editUserInfoRequest) {
        if((StringUtils.isBlank(editUserInfoRequest.getOldPassword()) && !StringUtils.isBlank(editUserInfoRequest.getNewPassword()))
            || (!StringUtils.isBlank(editUserInfoRequest.getOldPassword()) && StringUtils.isBlank(editUserInfoRequest.getNewPassword())))
            throw new BusinessException(BusinessExceptionCode.EDIT_PASSWORD_NEED_OLD_AND_NEW);

        UserInfo byId = getById(editUserInfoRequest.getId());
        byId.setNick(editUserInfoRequest.getNick());
        byId.setSign(editUserInfoRequest.getSign());
        boolean matches = passwordEncoder.matches(editUserInfoRequest.getOldPassword(), byId.getPassword());
        if(StringUtils.isNotBlank(editUserInfoRequest.getNewPassword()) && matches){
            byId.setPassword(passwordEncoder.encode(editUserInfoRequest.getNewPassword()));
        }
        if(editUserInfoRequest.getUserPictureFile() != null){
            List<String> upload = minioUtil.upload(editUserInfoRequest.getUserPictureFile(), ConstList.PICTURE_BUCKET, null);
            byId.setPicturePath(upload.get(0));
        }

        updateById(byId);
    }

    @Override
    public void downline(String userId) {
        chatUserCache.put(userId, "downline");
    }

    @Override
    public List<UserInfo> listAll(String keyWord) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(keyWord)){
            userInfoQueryWrapper.like("user_name", keyWord).or().like("nick", keyWord);
        }
        List<UserInfo> list = list(userInfoQueryWrapper);
        return list;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        removeById(userId);
        roleUserService.remove(new QueryWrapper<RoleUser>().eq("user_id", userId));
        fishArticlesService.remove(new QueryWrapper<FishArticles>().eq("created_id", userId));
        fishCommentsService.remove(new QueryWrapper<FishComments>().eq("from_user_id", userId).or().eq("to_user_id", userId));
        fishFriendApplyService.remove(new QueryWrapper<FishFriendApply>().eq("from_user_id",userId));
        fishUserFriendRelationService.remove(new QueryWrapper<FishUserFriendRelation>().eq("friend_id", userId));
        fishMessageService.remove(new QueryWrapper<FishMessage>().eq("created_id", userId));
    }

    @Async
    public void updateUserInfoById(UserInfo userInfo) {
        updateById(userInfo);
    }
}
