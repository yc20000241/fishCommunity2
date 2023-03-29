package com.yc.community.service.thirdApi.baiDu;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.yc.community.service.thirdApi.baiDu.response.DTResultInfo;
import com.yc.community.service.thirdApi.baiDu.response.IpResponse;
import org.springframework.stereotype.Component;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${baidu.ditu.domain}")
@Component
public interface DiTuApi {

    /**
     * 根据ip获取经纬度
     *
     * @param ip
     * @param ak
     * @return data
     */
    @GET("/location/ip")
    DTResultInfo<IpResponse> getLonAndLatByIp(@Query("ak") String ak, @Query("ip") String ip, @Query("coor") String coor);

}
