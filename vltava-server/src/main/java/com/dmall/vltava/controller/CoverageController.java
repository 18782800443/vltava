//package com.testhuamou.vltava.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.testhuamou.vltava.domain.annotation.HandleException;
//import com.testhuamou.vltava.domain.app.AppVO;
//import com.testhuamou.vltava.domain.base.BaseFilter;
//import com.testhuamou.vltava.domain.base.CommonResult;
//import com.testhuamou.vltava.domain.base.FilteredResult;
//import com.testhuamou.vltava.domain.coverage.CoverageManage;
//import com.testhuamou.vltava.domain.coverage.CoverageVO;
//import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
//import com.testhuamou.vltava.domain.git.GitVO;
//import com.testhuamou.vltava.service.AppService;
//import com.testhuamou.vltava.service.CoverageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Rob
// * @date Create in 2:30 PM 2020/2/7
// */
//@RequestMapping("/coverage")
//@RestController
//public class CoverageController {
//
//    @Autowired
//    CoverageService coverageService;
//
//    @Autowired
//    AppService appService;
//
//    @PostMapping("/filter")
//    CommonResult filter(@RequestBody BaseFilter baseFilter) {
//        if (baseFilter == null) {
//            return CommonResult.fail("数据错误");
//        }
//        FilteredResult<List<CoverageManage>> resp = coverageService.getFilteredList(baseFilter);
//        Map<String, AppVO> appNameMap = appService.getAppInfoMap();
//        List<CoverageVO> result = new ArrayList<>(resp.getResultList().size());
//        for (CoverageManage coverageManage : resp.getResultList()) {
//            CoverageVO cls = JSON.parseObject(JSON.toJSONString(coverageManage), CoverageVO.class);
//            cls.setAppName(appNameMap.get(cls.getAppId().toString()).getAppName());
//            result.add(cls);
//        }
//        FilteredResult<List<CoverageVO>> filteredResult = new FilteredResult<>(resp.getTotalPages(), result);
//        return CommonResult.pass("pass", filteredResult);
//    }
//
//    @HandleException
//    @PostMapping("/save")
//    CommonResult save(@RequestBody CoverageVO coverageVO) {
//        if (coverageVO.getAppId() == null || coverageVO.getTaskName() == null || coverageVO.getFileFilters() == null || coverageVO.getMsgFilters() == null) {
//            return CommonResult.fail("数据错误，必填值不能为空");
//        }
//        if (coverageService.exist(coverageVO)) {
//            return CommonResult.fail("已存在相同名称的需求，请检查");
//        }
//        if (coverageVO.getTaskStatus() == null) {
//            coverageVO.setTaskStatus(TaskStatusEnum.PREPARE.getKey());
//        }
//        coverageService.save(coverageVO);
//        coverageVO.setId(coverageService.getTaskId(coverageVO));
//        AppVO appVO = appService.getAppInfoMap().get(String.valueOf(coverageVO.getAppId()));
//        coverageVO.setAppName(appVO.getAppName());
//        coverageVO.setJenkinsJobName(appVO.getJenkinsJobName());
//        Boolean redisSuccess = coverageService.updateRedisStatus(coverageVO);
//        coverageService.prepareTrace(coverageVO);
//        return CommonResult.pass(redisSuccess ? "保存成功，可以点击任务状态开始追踪了" : "redis暂时无法连通，系统会在后台每分钟重试；稍后请主动刷新，待任务状态变为待运行时就可以点击任务状态开始追踪了", coverageVO.getId());
//    }
//
//    @HandleException
//    @PostMapping("/changeStatus")
//    CommonResult changeStatus(@RequestBody CoverageVO coverageVO) {
//        if (coverageVO.getTaskStatus() == null || coverageVO.getId() == null || coverageVO.getVersion() == null) {
//            return CommonResult.fail("数据错误，参数不能为空");
//        }
//        coverageService.updateStatus(coverageVO);
//        return CommonResult.pass("Success!");
//
//    }
//
//}
