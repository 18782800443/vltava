package com.testhuamou.vltava.controller;

import com.testhuamou.vltava.domain.annotation.HandleException;
import com.testhuamou.vltava.domain.base.CommonResult;
import com.testhuamou.vltava.domain.jenkins.JenkinsBuildVO;
import com.testhuamou.vltava.service.JenkinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rob
 * @date Create in 3:13 PM 2019/11/26
 */
@RequestMapping("/jenkins")
@RestController
public class JenkinsController {

    @Autowired
    JenkinsService jenkinsService;

    @HandleException
    @RequestMapping("/getInfo")
    public CommonResult getInfo(@RequestBody JenkinsBuildVO jenkinsBuildVO){
        jenkinsService.getLatestBuildInfo(jenkinsBuildVO);
        return CommonResult.pass(jenkinsBuildVO);
    }
}
