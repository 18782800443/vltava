package com.testhuamou.vltava.controller;

import com.testhuamou.vltava.domain.annotation.HandleException;
import com.testhuamou.vltava.domain.app.AppVO;
import com.testhuamou.vltava.domain.base.BaseFilter;
import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.git.GitVO;
import com.testhuamou.vltava.service.AppService;
import com.testhuamou.vltava.service.GitService;
import com.testhuamou.vltava.service.JenkinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rob
 * @date Create in 3:43 PM 2020/1/13
 */
@RequestMapping("/application")
@RestController
public class AppController {
    @Autowired
    JenkinsService jenkinsService;

    @Autowired
    GitService gitService;

    @Autowired
    AppService appService;

    @PostMapping("/filter")
    CommonResult filter(@RequestBody BaseFilter baseFilter) {
        if (baseFilter==null){
            return CommonResult.fail("数据错误");
        }
        return CommonResult.pass(appService.getFilteredList(baseFilter));
    }

    @HandleException
    @PostMapping("/save")
    CommonResult save(@RequestBody AppVO appVO) {
        if (StringUtils.isEmpty(appVO.getAppName())) {
            return CommonResult.fail("保存参数不能为空!");
        }
        appVO.setBuildGroup("blue");
        appService.appExists(appVO);
        appService.save(appVO);
        return CommonResult.pass("保存成功");
    }


}
