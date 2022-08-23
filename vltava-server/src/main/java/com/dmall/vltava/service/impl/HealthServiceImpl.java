package com.dmall.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.dmall.vltava.dao.DockerManageMapper;
import com.dmall.vltava.domain.DockerManage;
import com.dmall.vltava.manager.MockManager;
import com.dmall.vltava.service.HealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Rob
 * @date Create in 6:11 下午 2020/11/23
 */
@Service("HealthService")
public class HealthServiceImpl implements HealthService {
    private static final Logger logger = LoggerFactory.getLogger(HealthServiceImpl.class);

    @Autowired
    MockManager mockManager;

    @Autowired
    DockerManageMapper dockerManageMapper;

    @Override
    public void check() {
        List<DockerManage> dockerManageList = dockerManageMapper.getHealth();
        logger.info(JSON.toJSONString(dockerManageList));
    }
}
