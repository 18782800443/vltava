package com.testhuamou.vltava.utils;

import com.alibaba.fastjson.JSON;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.domain.mock.RegisterVO;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Rob
 * @date Create in 5:11 PM 2019/11/20
 */
public class HttpUtils {

    public static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static OkHttpClient CLIENT;
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    static {
        CLIENT = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();
    }


    public static String updateData(RegisterVO registerVO, List<MockVO> mockVO) {
        String path = "/vltava-agent/updateData";
        return post(registerVO, JSON.toJSONString(mockVO), path);
    }

    public static String updateStatus(RegisterVO registerVO, MockVO mockVO) {
        String path = "/vltava-agent/updateStatus";
        return post(registerVO, JSON.toJSONString(mockVO), path);
    }

    public static String healthCheck(String ip, String port) {
//        RegisterVO registerVO = new RegisterVO(ip, Integer.valueOf(port));
//        String path = "/vltava-agent/health";
//        return post(registerVO, "{\"health\":0}", path);
        return null;
    }

    private static String post(RegisterVO registerVO, String strMockVO, String path) {
        logger.info(String.format("发送消息: [path]: %s [register]: %s, [mock]: %s", path, JSON.toJSONString(registerVO), strMockVO));
        RequestBody body = RequestBody.create(APPLICATION_JSON, strMockVO);
        String url = buildUrl(registerVO, path);
        logger.info("url-> " + url);
        Request request = new Request.Builder().post(body).url(url).build();
        try {
            Response response = CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new CommonException(e.getMessage(), e);
        }
    }

    private static String buildUrl(RegisterVO registerVO, String path) {
        return "http://" + registerVO.getIp() + ":" + registerVO.getPort() + path;
    }

}
