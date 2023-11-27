package com.testhuamou.vltava.controller;

import com.testhuamou.vltava.domain.annotation.HandleException;
import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.mock.client.MockRule;
import com.testhuamou.vltava.domain.mock.client.MockStatus;
import com.testhuamou.vltava.service.MockClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rob
 * @date Create in 1:33 下午 2020/9/10
 */
@Controller
@RequestMapping("/mockClient")
public class MockClientController {
    @Autowired
    MockClient mockClient;

    @ResponseBody
    @PostMapping("/putRule")
    public CommonResult putRule(@RequestBody MockRule mockRule) {
        return mockClient.put(mockRule);
    }

    @ResponseBody
    @PostMapping("/removeRule")
    public CommonResult removeRule(@RequestBody MockRule mockRule) {
        return mockClient.remove(mockRule);
    }

    @ResponseBody
    @PostMapping("/updateStatus")
    public CommonResult updateStatus(@RequestBody MockStatus mockStatus) {
        return mockClient.updateStatus(mockStatus);
    }

}
