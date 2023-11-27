package com.testhuamou.vltava.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.testhuamou.vltava.dao.MockManageMapper;
import com.testhuamou.vltava.domain.base.BaseFilter;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.base.FilteredResult;
import com.testhuamou.vltava.domain.enums.TaskStatusEnum;
import com.testhuamou.vltava.domain.mock.MockActionVO;
import com.testhuamou.vltava.domain.mock.MockManage;
import com.testhuamou.vltava.domain.mock.MockVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rob
 * @date Create in 8:09 下午 2020/8/31
 */
@Service
public class MockManager {
    @Autowired
    MockManageMapper mockMapper;

    public FilteredResult<List<MockManage>> getFilteredList(BaseFilter baseFilter) {
        Page page = PageHelper.startPage(baseFilter.getPageNum(), baseFilter.getPageSize());
        String className = StringUtils.isEmpty(baseFilter.getClassName()) ? null : "className\":\"" + baseFilter.getClassName();
        String methodName = StringUtils.isEmpty(baseFilter.getMethodName()) ? null : "methodName\":\"" + baseFilter.getMethodName();
        List<MockManage> filteredResult = mockMapper.filter(baseFilter.getAppId(), className, methodName, baseFilter.getDescription());
        return new FilteredResult<List<MockManage>>(page.getPages(), filteredResult);
    }

    public int save(MockVO mockVO) {
        try {
            if (mockVO.getVersion() != null && mockVO.getId() != null) {
                Integer oldVersion = mockMapper.selectById(mockVO.getId()).getVersion();
                if (!mockVO.getVersion().equals(oldVersion)) {
                    throw new CommonException("已有人捷足先登了， 请刷新页面重试");
                }
            }
            MockManage cls = new MockManage();
            Date now = new Date(System.currentTimeMillis());
            Integer affectRow = 0;
            cls.setModifyTime(now);
            cls.setVersion(mockVO.getVersion());
            cls.setActions(JSON.toJSONString(mockVO.getMockActionList()));
            cls.setImplicit(mockVO.getImplicit());
            cls.setDescriptions(mockVO.getDescriptions());
            cls.setMockKey(mockVO.getMockKey());
            cls.setTenantId(-1L);
            if (mockVO.getId() == null) {
                cls.setAppId(mockVO.getAppId());
                cls.setYn(1);
                cls.setCreateTime(now);
                cls.setTaskStatus(TaskStatusEnum.PREPARE.getKey());
                return mockMapper.insert(cls);
            } else {
                cls.setId(mockVO.getId());
                return mockMapper.updateByPrimaryKeySelective(cls);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), e.getStackTrace());
        }
    }

    public Boolean exist(MockVO mockVO) {
        Example example = new Example(MockManage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appId", mockVO.getAppId());
        return mockMapper.selectByExample(example).size() > 0;
    }

    public Integer getTaskId(MockVO mockVO) {
        return mockMapper.getId(mockVO.getMockKey());
    }

    public MockVO getMockVoById(Integer mockId) {
        MockManage mockManage = mockMapper.selectById(mockId);

        if (mockManage == null) {
            throw new CommonException("没有对应ID");
        }
        return convert(mockManage);
    }

    public List<MockVO> getMockVoByAppId(Integer appId) {
        List<MockManage> mockManageList = mockMapper.selectByAppIdAndYn(appId);
        List<MockVO> result = new ArrayList<>();
        if (mockManageList == null || mockManageList.size() == 0) {
            return result;
        }
        for (MockManage mockManage : mockManageList) {
            result.add(convert(mockManage));
        }
        return result;
    }

    public MockVO getStatusById(Integer mockId) {
        MockManage mockManage = mockMapper.getSimpleInfoById(mockId);
        if (mockManage == null) {
            throw new CommonException("没有对应ID");
        }
        return convert(mockManage);
    }

    public int updateMysqlStatus(MockVO mockVO) {
        MockManage target = new MockManage();
        target.setId(mockVO.getId());
        target.setTaskStatus(mockVO.getTaskStatus());
        target.setVersion(mockVO.getVersion());
        return mockMapper.updateByPrimaryKeySelective(target);
    }

    public void updateConnect(Integer appId, Boolean isConnected) {
        mockMapper.updateConnect(appId, isConnected ? 1 : 0);
    }

    public int remove(Integer id){
        return mockMapper.remove(id);
    }

    public MockVO convert(MockManage mockManage) {
        MockVO cls = JSON.parseObject(JSON.toJSONString(mockManage), MockVO.class);
        if (mockManage.getActions() != null) {
            JSONArray jsonArray = JSON.parseArray(mockManage.getActions());
            cls.setMockActionList(jsonArray.toJavaList(MockActionVO.class));
        }
        return cls;
    }




}
