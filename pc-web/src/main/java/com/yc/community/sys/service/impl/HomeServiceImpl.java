package com.yc.community.sys.service.impl;

import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.DateUtil;
import com.yc.community.security.component.UserDetailAcessUserInfo;
import com.yc.community.service.modules.articles.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class HomeServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDetailAcessUserInfo userDetailAcessUserInfo;

    public void sign(HttpServletRequest request) {
        UserInfo userInfo = userDetailAcessUserInfo.getUserInfo(request);
        String key = userInfo.getId()+"_"+ DateUtil.getCurYearAndMonth();
        int currentDay = DateUtil.getCurrentDay();
        Boolean bit = redisTemplate.opsForValue().getBit(key, currentDay);
        if(bit == true)
            throw new BusinessException(BusinessExceptionCode.TODAY_HAS_SIGNED);

        redisTemplate.opsForValue().setBit(key, currentDay, true);
    }

    public List<Integer> curMonthSignList(HttpServletRequest request) throws ParseException {
        UserInfo userInfo = userDetailAcessUserInfo.getUserInfo(request);
        String curYearAndMonth = DateUtil.getCurYearAndMonth();
        String key = userInfo.getId() + "_" + curYearAndMonth;

        int daysOfMonth = DateUtil.getDaysOfMonth(new Date());
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create();

        for (int i = 0; i < daysOfMonth; i++)
            bitFieldSubCommands = bitFieldSubCommands.get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(i);
        List<Integer> signList = redisTemplate.opsForValue().bitField(key, bitFieldSubCommands);
        return signList;
    }


    public List<Long> getYearSignList(HttpServletRequest request) {
        List<Long> result = new ArrayList<>();
        List<String> months = Arrays.asList(new String[]{"01", "02", "03", "04", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
        int currentYear = DateUtil.getCurrentYear();
        String id = userDetailAcessUserInfo.getUserInfo(request).getId();

        for(int i = 1; i <= 12; i++){
            String key = id + "_" + currentYear + "-" +months.get(i);
            Long bitCount = (Long) redisTemplate.execute( (RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
            result.add(bitCount);
        }

        return result;
    }
}
