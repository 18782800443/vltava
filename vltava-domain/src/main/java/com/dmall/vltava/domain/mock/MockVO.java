package com.dmall.vltava.domain.mock;

import com.dmall.vltava.domain.app.AppVO;
import com.dmall.vltava.domain.enums.MockTypeEnum;
import lombok.Data;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rob
 * @date Create in 4:07 下午 2020/5/25
 */
@Data
public class MockVO extends MockManage implements Serializable {
    Integer paramIndex;
    List<MockActionVO> mockActionList;
    AppVO appVo;
    String appName;
    String entranceClassName;
    String entranceMethodName;

    public Boolean hasConnected(){
        return  getConnect() == null || getConnect() == 1 ;
    }

    public Boolean hasImplicit(){
        return getImplicit() == 1;
    }
}
