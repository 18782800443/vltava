package com.dmall.vltava.dao;

import com.dmall.vltava.utils.MyMapper;
import com.dmall.vltava.domain.app.AppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("AppManageMapper")
public interface AppManageMapper extends MyMapper<AppVO> {

    @Select("select * from app_manage where app_name=#{appName} or (system_unique_name=#{name} and zone=#{zone} and build_group=#{group}) and _tenant_id=-1")
    List<AppVO> exists(@Param("appName") String appName, @Param("name") String name, @Param("zone") String zone, @Param("group") String group);

    @Select({"<script>",
            "select * from app_manage",
            "where 1=1 and _tenant_id=-1",
            "<when test='appName!=\"\" and appName!=null'>",
            "and app_name=#{appName}",
            "</when>",
            "order by update_time desc",
            "</script>"})
    List<AppVO> filter(@Param("appName") String appName);

    @Select("Select * from app_manage where system_unique_name=#{uName} and zone=#{zone} and build_group=#{group} and _tenant_id=-1")
    AppVO selectBySystemUniqueNameAndZone(@Param("uName") String systemUniqueName, @Param("zone") String zone, @Param("group") String group);
}