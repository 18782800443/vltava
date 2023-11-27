package com.testhuamou.vltava.service.impl;

import com.testhuamou.vltava.dao.AppManageMapper;
import com.testhuamou.vltava.domain.app.AppVO;
import com.testhuamou.vltava.domain.base.BaseFilter;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.base.FilteredResult;
import com.testhuamou.vltava.domain.mock.RegisterVO;
import com.testhuamou.vltava.service.AppService;
import com.testhuamou.vltava.utils.CacheUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Rob
 * @date Create in 1:37 PM 2020/1/14
 */
@Service("AppService")
public class AppServiceImpl implements AppService {

    private static final String APP_CACHE = "VLTAVA_APP_";

    @Autowired
    AppManageMapper appManageMapper;

    @Override
    public void save(AppVO appVO) {
        try {
            appVO.setUpdateTime(new Date(System.currentTimeMillis()));
            appManageMapper.insert(appVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), e.getStackTrace().toString());
        }
    }

    @Override
    public Boolean appExists(AppVO appVO) {
        List<AppVO> resp = appManageMapper.exists(appVO.getAppName(), appVO.getSystemUniqueName(), appVO.getZone(), appVO.getBuildGroup());
        if (resp == null || resp.size() == 0) {
            return true;
        } else {
            StringBuilder errMsg = new StringBuilder();
            for (AppVO app : resp) {
                if (app.getAppName().equals(appVO.getAppName())) {
                    errMsg.append("已存在该应用名称，请检查修改后重试!");
                }
                if (app.getSystemUniqueName().equals(appVO.getSystemUniqueName()) && app.getZone().equals(appVO.getZone()) && app.getBuildGroup().equals(appVO.getBuildGroup())) {
                    errMsg.append("系统中已存在该容器，请检查修改后重试!");
                }
            }
            throw new CommonException(errMsg.toString());
        }
    }

    @Override
    public FilteredResult<List<AppVO>> getFilteredList(BaseFilter baseFilter) {
        Page page = PageHelper.startPage(baseFilter.getPageNum(), baseFilter.getPageSize());
        List<AppVO> filteredResult = appManageMapper.filter(baseFilter.getAppName());
        return new FilteredResult<List<AppVO>>(page.getPages(), filteredResult);
    }

    @Override
    public AppVO getAppInfo(Integer appId) {
        return (AppVO) CacheUtil.get(APP_CACHE + appId, new AppCall(appId));
    }

    @Override
    public AppVO getAppInfoByRegisterInfo(RegisterVO registerVO) {
        return appManageMapper.selectBySystemUniqueNameAndZone(registerVO.getSystemUniqueName(), registerVO.getZone(), registerVO.getGroup());
    }

    private class AppCall implements Callable {
        private Integer id;

        AppCall(Integer id) {
            this.id = id;
        }

        @Override
        public Object call() throws Exception {
            return appManageMapper.selectByPrimaryKey(id);
        }
    }
}
