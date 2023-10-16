package com.dmall.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.dmall.vltava.dao.DockerManageMapper;
import com.dmall.vltava.domain.DockerManage;
import com.dmall.vltava.domain.base.CommonException;
import com.dmall.vltava.domain.mock.RegisterVO;
import com.dmall.vltava.service.AgentService;
import com.dmall.vltava.service.AppService;
import com.dmall.vltava.service.DockerManageService;
import com.dmall.vltava.service.stategy.MockMqStrategy;
import com.dmall.vltava.utils.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rob
 * @date Create in 3:56 下午 2020/7/29
 */
@Service("DockerManageService")
public class DockerManageServiceImpl implements DockerManageService {
    public static final Logger logger = LoggerFactory.getLogger(DockerManageServiceImpl.class);

    private static final String DOCKER_MAP = "VLTAVA_DOCKER_";

    @Autowired
    DockerManageMapper dockerManageMapper;

    @Autowired
    AgentService agentService;

    @Autowired
    AppService appService;


    @Override
    public Boolean save(RegisterVO registerVO) {
        DockerManage sqlData = dockerManageMapper.getExist(registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getGroup());
        //适配往全部分组发送消息
        List<DockerManage> dockerManageList = dockerManageMapper.getAllGroup(registerVO.getSystemUniqueName(), registerVO.getZone());
        DockerManage dockerManage = null;
        Boolean needUpload = false;
        // 不存在就insert
        if (sqlData == null) {
            registerVO.setTenantId(-1L);
            dockerManage = converter(registerVO);
            if(!dockerManageList.isEmpty()){
                updateCache(dockerManage);
                needUpload = true;
            }
            if (dockerManageMapper.insert(dockerManage) != 1) {
                throw new CommonException("保存失败 " + JSON.toJSONString(dockerManage));
            }
        } else {
            for(DockerManage dockerManage1 : dockerManageList){
                if (dockerManage1.getRegTime() < registerVO.getTime()) {
                    dockerManage = converter(registerVO);
                    dockerManage.setId(dockerManage1.getId());
                    if(dockerManage1.getBuildGroup().equals(dockerManage.getBuildGroup())){
                        if (dockerManageMapper.updateByPrimaryKey(dockerManage) != 1) {
                            throw new CommonException("更新失败 " + JSON.toJSONString(dockerManage));
                        }
                        if (appService.getAppInfoByRegisterInfo(registerVO) != null){
                            needUpload = true;
                            updateCache(dockerManage);
                        }
                    }
                } else {
                    logger.info(String.format("【%s】最新版本为 %s, 丢弃过期消息 %s",
                            dockerManage1.getSystemUniqueName(), dockerManage1.getRegTime(), JSON.toJSONString(registerVO)));
                }
            }
        }
        if (needUpload.equals(false)){
            logger.info("当前服务在平台无应用，仅保存 ：" + JSON.toJSONString(registerVO));
        }
        return needUpload;
    }

    @Override
    public RegisterVO getSystemInfo(RegisterVO registerVO) {
        DockerManage dockerManage = null;
        dockerManage = (DockerManage) CacheUtil.get(getKey(registerVO));
        if (dockerManage != null) {
            return converter(dockerManage);
        } else {
            dockerManage = dockerManageMapper.getExist(registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getGroup());
            if (dockerManage != null){
                updateCache(dockerManage);
                return converter(dockerManage);
            } else {
                return null;
            }
        }
    }

    @Override
    public List<RegisterVO> getSystemInfoAllGroup(RegisterVO registerVO) {
        List<RegisterVO> result = new ArrayList<>();
        List<String> allBuildGroup = Lists.newArrayList("blue", "gray", "gray02", "gray03", "gray04");
        for(String group:allBuildGroup){
            registerVO.setGroup(group);
            DockerManage dockerManage = (DockerManage) CacheUtil.get(getKey(registerVO));
            if (dockerManage != null) {
                result.add(converter(dockerManage));
            } else {
                dockerManage = dockerManageMapper.getExist(registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getGroup());
                if (dockerManage != null){
                    updateCache(dockerManage);
                    result.add(converter(dockerManage));
                }
            }
        }
        return result;
    }

    private void updateCache(DockerManage dockerManage) {
        CacheUtil.set(getKey(dockerManage), dockerManage);
    }

    private DockerManage converter(RegisterVO registerVO) {
        DockerManage cls = new DockerManage();
        cls.setIp(registerVO.getIp());
        cls.setPort(registerVO.getPort());
        cls.setRegTime(registerVO.getTime());
        cls.setSystemUniqueName(registerVO.getSystemUniqueName());
        cls.setZone(registerVO.getZone());
        cls.setBuildGroup(registerVO.getGroup());
        return cls;
    }

    private RegisterVO converter(DockerManage dockerManage){
        RegisterVO cls = new RegisterVO();
        cls.setIp(dockerManage.getIp());
        cls.setPort(dockerManage.getPort());
        cls.setTime(dockerManage.getRegTime());
        cls.setSystemUniqueName(dockerManage.getSystemUniqueName());
        cls.setZone(dockerManage.getZone());
        cls.setGroup(dockerManage.getBuildGroup());
        return cls;
    }

    private String getKey(Object input){
        if (input instanceof RegisterVO){
            return DOCKER_MAP + getKey((RegisterVO) input);
        } else {
            return DOCKER_MAP + getKey((DockerManage) input);
        }
    }

    private String getKey(RegisterVO registerVO) {
        return registerVO.getSystemUniqueName() + "-" + registerVO.getZone() + "-" + registerVO.getGroup();
    }

    private String getKey(DockerManage dockerManage) {
        return dockerManage.getSystemUniqueName() + "-" + dockerManage.getZone() + "-" + dockerManage.getBuildGroup();
    }
    }
