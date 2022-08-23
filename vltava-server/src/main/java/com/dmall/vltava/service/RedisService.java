package com.dmall.vltava.service;


import com.dmall.vltava.domain.base.VersionVO;
import com.dmall.vltava.domain.enums.AttachTypeEnum;

import java.util.Map;

/**
 * @author Rob
 * @date Create in 4:03 下午 2020/5/27
 */
public interface RedisService {

    public Boolean update(String key, VersionVO versionVO);

    public void updateBatch(Map<String, VersionVO> versionMap);

    public void syncFailedList();
}
