package com.dmall.vltava.controller;

import com.dmall.vltava.domain.base.CommonResult;
import com.dmall.vltava.domain.git.GitVO;
import com.dmall.vltava.domain.annotation.HandleException;
import com.dmall.vltava.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rob
 * @date Create in 10:46 AM 2019/11/21
 */
@RestController
@RequestMapping("/git")
public class GitController {
    @Autowired
    GitService gitService;

    @RequestMapping("/getGitMap")
    @HandleException
    public CommonResult getGitMap(){
        return CommonResult.pass(gitService.getProjectMap());
    }

    @HandleException
    @RequestMapping("/getIncreasedCode")
    public CommonResult getIncreasedCode(@RequestBody GitVO gitVO){
        return CommonResult.pass(gitService.getIncreasedCode(gitVO));
    }

    @HandleException
    @RequestMapping("/getProjectCommits")
    public CommonResult getProjectCommits(@RequestBody GitVO gitVO){
        return CommonResult.pass(gitService.getProjectCommits(gitVO));
    }
}
