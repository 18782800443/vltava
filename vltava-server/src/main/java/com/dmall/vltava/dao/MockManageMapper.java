package com.testhuamou.vltava.dao;

import com.testhuamou.vltava.domain.DockerManage;
import com.testhuamou.vltava.domain.mock.MockManage;
import com.testhuamou.vltava.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("MockManageMapper")
public interface MockManageMapper extends MyMapper<MockManage> {
    @Select("select count(1) from mock_manage where app_id=#{appId} and _tenant_id=-1 and yn=1 and class_name=#{className} and method_name=#{methodName}")
    Integer exist(@Param("appId") Integer appId, @Param("className") String className, @Param("methodName") String methodName);

    @Select("select id from mock_manage where mock_key=#{mockKey} and _tenant_id=-1")
    Integer getId(@Param("mockKey") String mockKey);

    @Select({"<script>",
            "select * from mock_manage",
            "where yn=1 and _tenant_id=-1",
            "<when test='appId!=\"\" and appId!=null'>",
            "and app_id=#{appId} and _tenant_id=-1",
            "</when>",
            "<when test='className!=\"\" and className!=null'>",
            "and actions like '%${className}%'",
            "</when>",
            "<when test='methodName!=\"\" and methodName!=null'>",
            "and actions like '%${methodName}%'",
            "</when>",
            "<when test='description!=\"\" and description!=null'>",
            "and descriptions like '%${description}%'",
            "</when>",
            "order by modify_time desc",
            "</script>"})
    List<MockManage> filter(@Param("appId") Integer appId, @Param("className") String className, @Param("methodName") String methodName, @Param("description") String description);

    @Select("select id, app_id, task_status, mock_key, version from mock_manage where id=${id} and _tenant_id=-1")
    MockManage getSimpleInfoById(@Param("id") Integer id);

    @Select("select * from mock_manage where app_id=#{appId} and yn=1 and _tenant_id=-1")
    List<MockManage> selectByAppIdAndYn(@Param("appId") Integer appId);

    @Select("select version from mock_manage where id=#{id} and _tenant_id=-1")
    Integer selectVersionById(@Param("id") Integer id);

    @Update("update mock_manage set connect=${status} where app_id=${appId} and _tenant_id=-1")
    Integer updateConnect(@Param("appId") Integer appId, @Param("status") Integer status);

    @Update("update mock_manage set yn=0 where id=${id} and _tenant_id=-1")
    Integer remove(@Param("id") Integer id);

    @Select("select * from mock_manage where id=${id} and _tenant_id=-1")
    MockManage selectById(@Param("id") Integer id);

    @Select("select * from mock_manage as a where  _tenant_id=-1 and a.id in (select id from app_manage where system_unique_name=${systemUniqueName} " +
            "and zone=${zone} and group=${group})")
    List<MockManage> getMocks(@Param("systemUniqueName") String systemUniqueName,
                              @Param("zone") String zone,
                              @Param("group") String group);

}
