//package com.testhuamou.vltava.service.impl;
//
//import com.testhuamou.vltava.controller.AgentController;
//import com.testhuamou.vltava.dao.AppManageMapper;
//import com.testhuamou.vltava.dao.CoverageManageMapper;
//import com.testhuamou.vltava.domain.app.AppVO;
//import com.testhuamou.vltava.domain.base.BaseFilter;
//import com.testhuamou.vltava.domain.base.CommonException;
//import com.testhuamou.vltava.domain.base.FilteredResult;
//import com.testhuamou.vltava.domain.base.VersionVO;
//import com.testhuamou.vltava.domain.coverage.CoverageManage;
//import com.testhuamou.vltava.domain.coverage.CoverageVO;
//import com.testhuamou.vltava.domain.enums.AttachTypeEnum;
//import com.testhuamou.vltava.domain.enums.ErrorCodeEnum;
//import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
//import com.testhuamou.vltava.service.CoverageService;
//import com.testhuamou.vltava.service.RedisService;
//
//import com.testhuamou.vltava.utils.RedisUtils;
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageHelper;
//import com.sun.javafx.binding.StringFormatter;
//import org.apache.commons.lang3.StringUtils;
//import org.checkerframework.checker.units.qual.A;
//import org.redisson.Version;
//import org.redisson.api.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.*;
//
///**
// * @author Rob
// * @date Create in 2:32 PM 2020/2/7
// */
//@Service("CoverageService")
//public class CoverageServiceImpl implements CoverageService {
//    private static final Logger logger = LoggerFactory.getLogger(CoverageServiceImpl.class);
//
//    @Autowired
//    AppManageMapper appManageMapper;
//
//    @Autowired
//    CoverageManageMapper coverageManageMapper;
//
//    @Autowired
//    RedisService redisService;
//
//    @Override
//    public FilteredResult<List<CoverageManage>> getFilteredList(BaseFilter baseFilter) {
//        Page page = PageHelper.startPage(baseFilter.getPageNum(), baseFilter.getPageSize());
//        List<CoverageManage> filteredResult = coverageManageMapper.filter(baseFilter.getAppId(), baseFilter.getTaskName());
//        return new FilteredResult<List<CoverageManage>>(page.getPageNum(), filteredResult);
//    }
//
//    @Override
//    public void save(CoverageVO coverageVO) {
//        try {
//            CoverageManage cls = new CoverageManage();
//            cls.setAppId(coverageVO.getAppId());
//            Date now = new Date(System.currentTimeMillis());
//            cls.setCreateTime(now);
//            cls.setModifyTime(now);
//            cls.setTaskStatus(coverageVO.getTaskStatus());
//            cls.setFileFilters(coverageVO.getFileFilters());
//            cls.setMsgFilters(coverageVO.getMsgFilters());
//            cls.setTaskName(coverageVO.getTaskName());
//            cls.setVersion(0);
//            cls.setYn(1);
//            coverageManageMapper.insert(cls);
//        } catch (Throwable e) {
//            e.printStackTrace();
//            throw new CommonException(e.getMessage(), e.getStackTrace());
//        }
//    }
//
//    @Override
//    public Boolean exist(CoverageVO coverageVO) {
//        return coverageManageMapper.exist(coverageVO.getTaskName()) > 0;
//    }
//
//    @Override
//    public Integer getTaskId(CoverageVO coverageVO) {
//        Integer result = coverageManageMapper.selectIdByTaskName(coverageVO.getTaskName());
//        if (result == null) {
//            throw new CommonException("未找到该任务！");
//        } else {
//            return result;
//        }
//    }
//
//    @Override
//    public void prepareTrace(CoverageVO coverageVO) {
//        if (coverageVO.getAppId() == null) {
//            throw new CommonException("appId不能为空");
//        }
//        AppVO appVO = appManageMapper.selectByPrimaryKey(coverageVO.getAppId());
//        if (appVO.getServerIp() == null) {
//            throw new CommonException("serverIp不能为空");
//        }
//    }
//
//    @Override
//    public Boolean updateStatus(CoverageVO coverageVO) {
//        logger.info(String.format("尝试变更【 %s 】状态: %s ", coverageVO.getId(), TaskStatusEnum.getDescByKey(coverageVO.getTaskStatus())));
//        updateMysqlStatus(coverageVO);
//        return updateRedisStatus(coverageVO);
//    }
//
//    @Override
//    public Boolean updateRedisStatus(CoverageVO coverageVO) {
//        return redisService.update(getRedisKey(coverageVO),getVersion(coverageVO));
//    }
//
//    @Override
//    public void syncStatus() {
//        List<CoverageManage> manageList = coverageManageMapper.getAllValid();
//        logger.info("批量更新redis状态中，共有生效key：" + manageList.size());
//        Map<String, VersionVO> kvMap = new HashMap<>();
//        for (CoverageManage coverageManage : manageList) {
//            CoverageVO coverageVO = (CoverageVO) coverageManage;
//            kvMap.put(getRedisKey(coverageVO), new VersionVO(coverageVO.getTaskStatus(), coverageManage.getVersion()));
//        }
//        redisService.updateBatch(kvMap);
//    }
//
//    @Override
//    public Map<Integer, VersionVO> pullVersion(List<Integer> taskList) {
//        Map<Integer, VersionVO> result = new HashMap<>();
//        List<CoverageManage> coverageManageList = coverageManageMapper.getAllAgentReuqest(StringUtils.join(taskList, ","));
//        for (CoverageManage coverageManage : coverageManageList) {
//            result.put(coverageManage.getId(), new VersionVO(coverageManage.getTaskStatus(), coverageManage.getVersion()));
//        }
//        return result;
//    }
//
//    @Override
//    public void clearInvalid() {
//        logger.info("开始清理关闭时间超过一天的任务");
//        List<CoverageManage> coverageManageList = coverageManageMapper.getAllInvalid();
//        Long now  =System.currentTimeMillis();
//        Long aDay = 1000*60*60*24L;
//        Integer count = 0;
//        for (CoverageManage coverageManage: coverageManageList){
//            if (now - coverageManage.getModifyTime().getTime() > aDay){
//                count++;
//                coverageManage.setYn(0);
//                coverageManage.setModifyTime(new Date(now));
//                coverageManageMapper.updateByPrimaryKeySelective(coverageManage);
//                logger.info(String.format("任务：【 %s 】mysql已设为无效, redis key不会再同步状态，将于一天后失效", coverageManage.getId()));
//            }
//        }
//        logger.info(String.format("清理完成，共【 %s 】条", count));
//    }
//
//    private VersionVO getVersion(CoverageVO coverageVO) {
//        CoverageManage coverageManage = coverageManageMapper.selectByPrimaryKey(coverageVO.getId());
//        return new VersionVO(coverageManage.getTaskStatus(), coverageManage.getVersion());
//    }
//
//    private String getRedisKey(CoverageVO coverageVO) {
//        return String.format(RedisUtils.COMMON_KEY, AttachTypeEnum.COVERAGE.getKey(), coverageVO.getId());
//    }
//
//    private void updateMysqlStatus(CoverageVO coverageVO) {
//        try {
//            CoverageManage cls = coverageManageMapper.selectByPrimaryKey(coverageVO.getId());
//            cls.setTaskStatus(coverageVO.getTaskStatus());
//            cls.setModifyTime(new Date(System.currentTimeMillis()));
//            coverageManageMapper.updateByPrimaryKey(cls);
//            logger.info(String.format("[ %s ] MySQL状态当前修改为：%s", coverageVO.getTaskName() == null ? coverageVO.getId() : coverageVO.getTaskName(), TaskStatusEnum.getDescByKey(coverageVO.getTaskStatus())));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CommonException(e.getMessage(), e);
//        }
//    }
//}
