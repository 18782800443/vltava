package com.testhuamou.vltava.controller;

import com.alibaba.fastjson.JSON;
import com.testhuamou.vltava.domain.annotation.HandleException;
import com.testhuamou.vltava.domain.base.BaseFilter;
import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.base.FilteredResult;
import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
import com.testhuamou.vltava.domain.mock.MockActionVO;
import com.testhuamou.vltava.domain.mock.MockManage;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.testhuamou.vltava.manager.MockManager;
import com.testhuamou.vltava.service.AgentService;
import com.testhuamou.vltava.service.AppService;
import com.testhuamou.vltava.service.DockerManageService;
import com.testhuamou.vltava.service.MockService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob
 * @date Create in 3:09 下午 2020/5/26
 */
@RequestMapping("/mock")
@RestController
public class MockController {
    public static final Logger logger = LoggerFactory.getLogger(MockController.class);

    @Autowired
    MockService mockService;

    @Autowired
    AppService appService;

    @Autowired
    AgentService agentService;

    @Autowired
    DockerManageService dockerManageService;

    @Autowired
    MockManager mockManager;

    @PostMapping("/filter")
    CommonResult filter(@RequestBody BaseFilter baseFilter) {
        logger.info("开始查询！");
        if (baseFilter == null) {
            return CommonResult.fail("数据错误");
        }
        FilteredResult<List<MockManage>> resp = mockManager.getFilteredList(baseFilter);

        List<MockVO> result = new ArrayList<>(resp.getResultList().size());
        for (MockManage mockManage : resp.getResultList()) {
            MockVO cls = JSON.parseObject(JSON.toJSONString(mockManage), MockVO.class);
            cls.setActions(null);
            cls.setMockActionList(JSON.parseArray(mockManage.getActions(), MockActionVO.class));
            cls.setAppName(appService.getAppInfo(cls.getAppId()).getAppName());
            if (cls.hasImplicit()) {
                for (MockActionVO mockActionVO : cls.getMockActionList()) {
                    if (mockActionVO.getEntrance() != null && mockActionVO.getEntrance()) {
                        cls.setEntranceClassName(mockActionVO.getClassName());
                        cls.setEntranceMethodName(mockActionVO.getMethodName());
                    }
                }
            }
            result.add(cls);
        }

        FilteredResult<List<MockVO>> filteredResult = new FilteredResult<>(resp.getTotalPages(), result);
        return CommonResult.pass("pass", filteredResult);
    }

    @HandleException
    @PostMapping("/save")
    CommonResult save(@RequestBody MockVO mockVO) {
        logger.info("req is {}", mockVO);
        if (mockVO.getAppId() == null) {
            return CommonResult.fail("数据错误，必填值不能为空");
        }
        if (!mockVO.hasImplicit()) {
            if (mockVO.getMockActionList().stream().anyMatch(action -> action.getExpectKey() == null && action.getEntrance() != true)) {
                return CommonResult.fail("数据错误，必填值不能为空");
            }
        } else {
            if (StringUtils.isEmpty(mockVO.getEntranceClassName()) || StringUtils.isEmpty(mockVO.getEntranceMethodName())) {
                return CommonResult.fail("数据错误，必填值不能为空");
            }
        }
        mockService.formatData(mockVO);
        if (mockVO.getTaskStatus() == null) {
            mockVO.setTaskStatus(TaskStatusEnum.PREPARE.getKey());
        }
        if (mockVO.getVersion() == null) {
            mockVO.setVersion(0);
        }
        return mockService.save(mockVO);
    }

    @HandleException
    @PostMapping("/changeStatus")
    CommonResult changeStatus(@RequestBody MockVO mockVO) {
        if (mockVO.getTaskStatus() == null || mockVO.getId() == null || mockVO.getVersion() == null) {
            return CommonResult.fail("数据错误，参数不能为空");
        }
        mockService.updateStatus(mockVO);
        return CommonResult.pass("状态更新成功！", mockVO.getId());
    }

    @HandleException
    @PostMapping("/retry")
    CommonResult retry(@RequestBody Integer mockId) {
        MockVO mockVO = mockManager.getMockVoById(mockId);
        mockVO.setTaskStatus(TaskStatusEnum.READY.getKey());
        return mockService.retry(mockVO);
    }

    @HandleException
    @PostMapping("/remove")
    CommonResult remove(@RequestBody Integer mockId) {
        MockVO mockVO = mockManager.getMockVoById(mockId);
        if (mockVO == null) {
            return CommonResult.fail("id不存在");
        }
        if (!TaskStatusEnum.END.getKey().equals(mockVO.getTaskStatus())) {
            return CommonResult.fail("当前规则状态不为结束，无法删除");
        }
        return mockManager.remove(mockId) == 1 ? CommonResult.pass("success") : CommonResult.fail("删除失败，请刷新后重试");
    }
}
