package com.dmall.vltava.controller;

import com.dmall.vltava.domain.base.CommonResult;
import com.dmall.vltava.domain.base.VersionVO;
import com.dmall.vltava.domain.enums.AttachTypeEnum;
import com.dmall.vltava.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 6:24 PM 2020/3/12
 */
@RequestMapping("/agent")
@RestController
public class AgentController {

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping("/pullVersion")
    public CommonResult pullVersion(@RequestBody List<String> keyList) {
        Map<Integer, List<Integer>> requestMap = new HashMap<>();
        Map<String, VersionVO> result = new HashMap<>();
        for (String key : keyList) {
            String tmp = key.substring(7, key.length());
            Integer attachType = Integer.parseInt(tmp.split(":")[0]);
            Integer taskId = Integer.parseInt(tmp.split(":")[1]);
            requestMap.computeIfAbsent(attachType, k -> new ArrayList<>());
            // 理论上应该不会出现重复taskId
            requestMap.get(attachType).add(taskId);
        }
        Object service = null;
        for (Integer attachType : requestMap.keySet()) {
            String serviceName = AttachTypeEnum.getDescByKey(attachType) + "Service";
            service = applicationContext.getBean(serviceName);
            Class serviceCls = service.getClass();
            for (Method method : serviceCls.getMethods()) {
                if ("pullVersion".equals(method.getName())) {
                    try {
                        Map<Integer, VersionVO> serviceResp = (Map<Integer, VersionVO>) method.invoke(service, requestMap.get(attachType));
                        for (Integer taskId: serviceResp.keySet()){
                            result.put(String.format(RedisUtils.COMMON_KEY,attachType,taskId), serviceResp.get(taskId));
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return CommonResult.pass(result);


    }
}
